package dev

import com.google.common.collect.Maps
import kotlin.math.ceil
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
/* ktlint-disable no-wildcard-imports */
import org.gradle.kotlin.dsl.*

/* ktlint-enable no-wildcard-imports */

class JavaConfig : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            plugins.apply("org.gradle.java")

            configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            val userHome = System.getProperty("user.home")
            repositories {
                mavenCentral()
                maven {
                    url = uri("file:///$userHome/git/maven-repository/releases")
                }
            }

            tasks.withType(Test::class).configureEach {
                useJUnitPlatform()
                maxHeapSize = "1G"
                maxParallelForks = ceil(Runtime.getRuntime().availableProcessors() / 2.0).toInt()
                ignoreFailures = project.hasProperty("ignoreTestFailures")
                logger.info("$path will run with maxParallelForks=$maxParallelForks.")
                systemProperties = Maps.fromProperties(System.getProperties()).toMap() - "user.dir"
                testLogging {
                    if (project.hasProperty("output")) {
                        exceptionFormat = TestExceptionFormat.FULL
                        events = setOf(TestLogEvent.STARTED, TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
                        showStandardStreams = true
                    }
                }
            }

            tasks.withType(JavaCompile::class).configureEach {
                options.compilerArgs.add("-Werror")
                options.compilerArgs.add("-Xlint:auxiliaryclass")
                options.compilerArgs.add("-Xlint:cast")
                options.compilerArgs.add("-Xlint:classfile")
                options.compilerArgs.add("-Xlint:deprecation")
                options.compilerArgs.add("-Xlint:dep-ann")
                options.compilerArgs.add("-Xlint:divzero")
                options.compilerArgs.add("-Xlint:empty")
                options.compilerArgs.add("-Xlint:fallthrough")
                options.compilerArgs.add("-Xlint:finally")
                options.compilerArgs.add("-Xlint:options")
                options.compilerArgs.add("-Xlint:overrides")
                options.compilerArgs.add("-Xlint:path")
                options.compilerArgs.add("-Xlint:processing")
                options.compilerArgs.add("-Xlint:rawtypes")
                options.compilerArgs.add("-Xlint:serial")
                options.compilerArgs.add("-Xlint:static")
                options.compilerArgs.add("-Xlint:try")
                options.compilerArgs.add("-Xlint:unchecked")
                options.compilerArgs.add("-Xlint:varargs")
                options.isFork = true
            }

            tasks.withType(AbstractArchiveTask::class) {
                isPreserveFileTimestamps = false
                isReproducibleFileOrder = true
            }
        }
    }
}
