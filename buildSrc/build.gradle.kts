plugins {
    id("org.jmailen.kotlinter").version("2.3.1")
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("ide-config") {
            id = "ide-config"
            implementationClass = "ide.IdeConfig"
        }
        register("checks-config") {
            id = "checks-config"
            implementationClass = "checks.ChecksConfig"
        }
        register("code-checks-config") {
            id = "code-checks-config"
            implementationClass = "checks.CodeChecksConfig"
        }
        register("dev-config") {
            id = "dev-config"
            implementationClass = "dev.JavaConfig"
        }
    }
}

dependencies {
    implementation("com.google.guava:guava:29.0-jre")
    implementation("net.ltgt.gradle:gradle-apt-plugin:0.21")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:1.2.1")
    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.5.1")
    implementation("gradle.plugin.io.morethan.jmhreport:gradle-jmh-report:0.9.0")
    gradleApi()
}

val userHome = System.getProperty("user.home")
repositories {
    mavenCentral()
    maven {
        url = uri("file:///$userHome/git/maven-repository/releases")
    }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}
