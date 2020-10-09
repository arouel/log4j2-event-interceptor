package checks

import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

object CheckstyleConfig {

    fun apply(project: Project) {
        with(project) {
            plugins.apply("org.gradle.checkstyle")

            configure<CheckstyleExtension> {
                toolVersion = "8.30"
                configFile = rootProject.file("config/checks/checkstyle.xml")
                isShowViolations = true
                isIgnoreFailures = false
            }

            tasks.withType(Checkstyle::class).configureEach {
                reports {
                    xml.isEnabled = true
                    html.isEnabled = true
                }
            }
        }
    }
}
