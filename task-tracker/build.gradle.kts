plugins {
    java
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.3"
    id("org.openapi.generator") version "7.0.1"
}

group = "org.uber.popug.task.tracker"
version = "0.0.1"

val junitVersion: String by project
val springBootVersion: String by project
val swaggerV3AnnotationsVersion: String by project
val jakartaValidationApiVersion: String by project
val mapstructVersion: String by project
val lombokVersion: String by project
val liquibaseVersion: String by project
val postgresVersion: String by project

val openApiGeneratedPath: String = "${buildDir}/generated/openapi"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    sourceSets {
        getByName("main").java {
            srcDir("${openApiGeneratedPath}/src/main/java")
        }
    }
}


repositories {
    mavenCentral()
}

dependencies {
    // --> Annotation Processors <--
    // Lombok - annotation processing.
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    // MapStruct - annotation processing.
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    // --> Implementation Dependencies <--
    // MapStruct - implementation.
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    // Spring Boot starter web.
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Swagger V3 annotations.
    implementation("io.swagger.core.v3:swagger-annotations:${swaggerV3AnnotationsVersion}")
    // Jakarta.
    implementation("jakarta.validation:jakarta.validation-api:${jakartaValidationApiVersion}")
    // Liquibase.
    implementation("org.liquibase:liquibase-core:${liquibaseVersion}")
    // PostgreSQL.
    implementation("org.postgresql:postgresql:${postgresVersion}")

    // --> Compile-Only Dependencies <--
    // Lombok.
    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    // --> Test Implementation Dependencies <--
    // JUnit API.
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")

    // --> Test Runtime-Only Dependencies <--
    // JUnit engine.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }

    named<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerate") {
        generatorName.set("spring")
        inputSpec.set("${projectDir}/src/main/resources/openapi/uber-popug-task-tracker-contract.yaml")
        outputDir.set(openApiGeneratedPath)
        apiPackage.set("org.uber.popug.task.tracker.rest.generated.api")
        modelPackage.set("org.uber.popug.task.tracker.rest.generated.model")
        configOptions.set(mapOf(
            "dateLibrary" to "java8",
            "generateSupportingFiles" to "false",
            "hideGenerationTimestamp" to "true",
            "interfaceOnly" to "true",
            "java8" to "false",
            "library" to "spring-boot",
            "openApiNullable"  to "false",
            "useBeanValidation" to "true",
            "useSpringBoot3" to "true",
            "serializableModel" to "true",
            "skipDefaultInterface" to "true",
            "legacyDiscriminatorBehavior" to "true"
        ))
    }

    named<JavaCompile>("compileJava") {
        inputs.files(named("processResources"))
        dependsOn(
            named("openApiGenerate")
        )
    }
}
