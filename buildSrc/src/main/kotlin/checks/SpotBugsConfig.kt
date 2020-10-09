package checks

import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.withType

object SpotBugsConfig {
    fun apply(project: Project) {
        with(project) {
            plugins.apply("com.github.spotbugs")
            configure<SpotBugsExtension> {
                toolVersion.set("4.1.3")
                showProgress.set(false)
                ignoreFailures.set(false)
                includeFilter.set(rootProject.file("config/checks/spotbugs.xml"))
                reportLevel.set(Confidence.LOW)
            }

            tasks.withType(SpotBugsTask::class).configureEach {
                reports.create("html") {
                    isEnabled = false
                }
                reports.create("xml") {
                    isEnabled = true
                }
                dependsOn(tasks["compileJava"])
            }
        }
    }
}
