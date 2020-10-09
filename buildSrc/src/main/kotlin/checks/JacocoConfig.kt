package checks

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import version.Dependency

object JacocoConfig {
    fun apply(project: Project) {
        with(project) {
            plugins.apply("org.gradle.jacoco")

            configure<JacocoPluginExtension> {
                toolVersion = Dependency.jacoco
            }

            tasks.withType(JacocoReport::class).configureEach {
                reports {
                    html.destination = file("$buildDir/jacocoHtml")
                }
            }

            tasks.withType(JacocoCoverageVerification::class).configureEach {
                violationRules {
                    rule {
                        element = "CLASS"
                        excludes = listOf(
                            "*_*Factory",
                            "*Module"
                        )
                        limit {
                            minimum = 0.99999.toBigDecimal()
                        }
                    }
                }
            }

            tasks.named<DefaultTask>("check") {
                dependsOn(tasks.named("jacocoTestCoverageVerification"))
            }
        }
    }
}
