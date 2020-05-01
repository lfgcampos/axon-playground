package io.axoniq.labs.chat;

import io.axoniq.labs.chat.query.rooms.messages.ChatMessage;
import io.axoniq.labs.chat.query.rooms.messages.ChatMessageDAO;
import io.axoniq.labs.chat.query.rooms.messages.ChatMessageProjection;
import io.axoniq.labs.chat.query.rooms.participants.RoomParticipant;
import io.axoniq.labs.chat.query.rooms.participants.RoomParticipantsDAO;
import io.axoniq.labs.chat.query.rooms.participants.RoomParticipantsProjection;
import io.axoniq.labs.chat.query.rooms.summary.RoomSummary;
import io.axoniq.labs.chat.query.rooms.summary.RoomSummaryDAO;
import io.axoniq.labs.chat.query.rooms.summary.RoomSummaryProjection;
import io.axoniq.labs.chat.restapi.CommandController;
import io.axoniq.labs.chat.restapi.QueryController;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.axonframework.config.Configuration;

public class ChatGettingStartedApplication extends Application<ChatGettingStartedConfiguration> {

    private final HibernateBundle<ChatGettingStartedConfiguration> hibernateBundle =
            new HibernateBundle<ChatGettingStartedConfiguration>(ChatMessage.class,
                                                                 RoomParticipant.class,
                                                                 RoomSummary.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ChatGettingStartedConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new ChatGettingStartedApplication().run(args);
    }

    @Override
    public String getName() {
        return "ChatGettingStarted";
    }

    @Override
    public void initialize(final Bootstrap<ChatGettingStartedConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        // Enable Migration feature based on Liquibase, which is the default
        bootstrap.addBundle(new MigrationsBundle<ChatGettingStartedConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ChatGettingStartedConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final ChatGettingStartedConfiguration configuration,
                    final Environment environment) {
        // DAO
        final ChatMessageDAO chatMessageDAO = new ChatMessageDAO(hibernateBundle.getSessionFactory());
        final RoomParticipantsDAO roomParticipantsDAO = new RoomParticipantsDAO(hibernateBundle.getSessionFactory());
        final RoomSummaryDAO roomSummaryDAO = new RoomSummaryDAO(hibernateBundle.getSessionFactory());

        // Projections
        final ChatMessageProjection chatMessageProjection = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(ChatMessageProjection.class, ChatMessageDAO.class, chatMessageDAO);
        final RoomParticipantsProjection roomParticipantsProjection = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(RoomParticipantsProjection.class, RoomParticipantsDAO.class, roomParticipantsDAO);
        final RoomSummaryProjection roomSummaryProjection = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(RoomSummaryProjection.class, RoomSummaryDAO.class, roomSummaryDAO);
        // Axon
        Configuration axonConfiguration = new AxonConfiguration()
                .buildAndGetConfiguration(chatMessageProjection,
                                          roomParticipantsProjection,
                                          roomSummaryProjection);
        axonConfiguration.start();

        // REST Controllers
        QueryController queryController = new QueryController(axonConfiguration.queryGateway());
        environment.jersey().register(queryController);
        CommandController commandController = new CommandController(axonConfiguration.commandGateway());
        environment.jersey().register(commandController);
    }
}
