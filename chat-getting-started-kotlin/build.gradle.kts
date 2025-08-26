import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val axonVersion: String by extra { "4.3.3" }
val swaggerVersion: String by extra { "2.9.2" }

plugins {
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "2.1.21"
}

group = "io.axoniq.labs.chat"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.projectreactor:reactor-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion")
    implementation("io.springfox:springfox-swagger2:$swaggerVersion")
    implementation("io.springfox:springfox-swagger-ui:$swaggerVersion")
    implementation("com.h2database:h2")
    implementation("javax.validation:validation-api")
    testImplementation("org.axonframework:axon-test:$axonVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

apply {
    plugin("io.spring.dependency-management")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
