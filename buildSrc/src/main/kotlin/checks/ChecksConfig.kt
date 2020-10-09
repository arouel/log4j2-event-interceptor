package checks

import org.gradle.api.Plugin
import org.gradle.api.Project

class ChecksConfig : Plugin<Project> {

    override fun apply(project: Project) {
        CodeChecksConfig().apply(project)
        JacocoConfig.apply(project)
    }
}
