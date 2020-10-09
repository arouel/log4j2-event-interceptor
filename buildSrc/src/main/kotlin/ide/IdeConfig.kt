package ide

import org.gradle.api.Plugin
import org.gradle.api.Project

class IdeConfig : Plugin<Project> {

    override fun apply(project: Project) {
        EclipseConfig.apply(project)
        IdeaConfig.apply(project)
    }
}
