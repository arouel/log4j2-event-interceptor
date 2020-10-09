import version.Dependency

plugins {
    id("dev-config")
    id("ide-config")
    id("checks-config")
    `java-library`
}

group = "com.github.arouel"

description = "A utility which helps intercepting Apache Log4j 2 events from any given logger."

dependencies {
    api(group = "org.apache.logging.log4j", name = "log4j-core", version = Dependency.log4j2)

    testImplementation(group = "org.assertj", name = "assertj-core", version = Dependency.assertj)
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = Dependency.junit)
    testImplementation(group = "org.slf4j", name = "slf4j-api", version = Dependency.slf4j)

    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = Dependency.junit)
    testRuntimeOnly(group = "org.apache.logging.log4j", name = "log4j-slf4j-impl", version = Dependency.log4j2)
}
