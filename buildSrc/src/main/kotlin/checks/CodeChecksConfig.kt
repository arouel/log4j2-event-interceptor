package checks

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodeChecksConfig : Plugin<Project> {

    override fun apply(project: Project) {
        CheckstyleConfig.apply(project)
        ErrorProneConfig.apply(project)
        SpotBugsConfig.apply(project)
    }
}
