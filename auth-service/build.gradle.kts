plugins {
    id("java")
}

group = "org.uber.popug"
version = "0.0.1"

val junitVersion: String by project


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}