plugins {
    java
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.3"
    id("org.openapi.generator") version "7.0.1"
    id("de.undercouch.download") version "5.6.0"
    id("org.jsonschema2pojo") version "1.2.1"
}

group = "org.uber.popug.task.tracker"
version = "0.0.1"

val junitVersion: String by project
val springBootVersion: String by project
val springKafkaVersion: String by project
val swaggerV3AnnotationsVersion: String by project
val jakartaValidationApiVersion: String by project
val mapstructVersion: String by project
val lombokVersion: String by project
val liquibaseVersion: String by project
val postgresVersion: String by project
val uuidCreatorVersion: String by project

val openApiGeneratedPath: String = "${buildDir}/generated/openapi"
val jsonSchemaDownloadPath: String = "${buildDir}/downloads/json/schema"
val jsonSchemaGeneratedPath: String = "${buildDir}/generated/jsonSchema2Pojo/src/main/java"

val schemaRegistryHost: String by project
val schemaRegistryPath: String = "${schemaRegistryHost}/registry/%s/schemas/%s/%d"
val necessaryJsonSchemas: Map<String, String> = mapOf(
    schemaRegistryPath.format("cud", "task-created", 1) to "task-created-replication-event-v1.json",
    schemaRegistryPath.format("business", "task-created", 1) to "task-created-event-v1.json",
    schemaRegistryPath.format("business", "task-reassigned", 1) to "task-reassigned-event-v1.json",
    schemaRegistryPath.format("business", "task-completed", 1) to "task-completed-event-v1.json",
)

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
    // Spring Boot starter JDBC.
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    // Spring Boot starter security.
    implementation("org.springframework.boot:spring-boot-starter-security")
    // Spring Boot starter OAuth2 resource server.
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // Spring Kafka.
    implementation("org.springframework.kafka:spring-kafka:${springKafkaVersion}")
    // Swagger V3 annotations.
    implementation("io.swagger.core.v3:swagger-annotations:${swaggerV3AnnotationsVersion}")
    // Jakarta.
    implementation("jakarta.validation:jakarta.validation-api:${jakartaValidationApiVersion}")
    // Liquibase.
    implementation("org.liquibase:liquibase-core:${liquibaseVersion}")
    // PostgreSQL.
    implementation("org.postgresql:postgresql:${postgresVersion}")
    // UUID creator library.
    implementation("com.github.f4b6a3:uuid-creator:${uuidCreatorVersion}")


    // --> Compile-Only Dependencies <--
    // Lombok.
    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    // --> Development-Only Dependencies <--
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

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
            "useTags" to "true",
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
            named("openApiGenerate"),
            named("generateJsonSchema2Pojo")
        )
    }

    register<de.undercouch.gradle.tasks.download.Download>("downloadMissingJsonSchemas") {
        // Do not download the schemas that are already present.
        val schemasToDownload: MutableSet<String> = mutableSetOf()
        necessaryJsonSchemas.forEach {
            if (!file("${jsonSchemaDownloadPath}/${it.value}").exists()) {
                schemasToDownload.add(it.key)
            }
            else {
                println("Skipping download of ${it.value} because it's already loaded.")
            }
        }

        src(schemasToDownload)
        overwrite(false)
        dest(jsonSchemaDownloadPath)

        if (schemasToDownload.size == 1) {
            dest("${jsonSchemaDownloadPath}/${necessaryJsonSchemas[schemasToDownload.first()]}")
        }
        else if (schemasToDownload.size > 1) {
            eachFile {
                val sourceUrl = sourceURL.toString()
                name = necessaryJsonSchemas[sourceUrl] ?: error("URL not found in the mapping: $sourceUrl")
            }
        }
    }

    named<org.jsonschema2pojo.gradle.GenerateJsonSchemaJavaTask>("generateJsonSchema2Pojo") {
        dependsOn(
            named("downloadMissingJsonSchemas")
        )
    }

    jsonSchema2Pojo {
        targetPackage = "${group}.kafka.generated.dto"
        targetDirectory = file(jsonSchemaGeneratedPath)
        sourceFiles = files(jsonSchemaDownloadPath)
        includeAdditionalProperties = false
        usePrimitives = true
        useTitleAsClassname = true
    }
}
