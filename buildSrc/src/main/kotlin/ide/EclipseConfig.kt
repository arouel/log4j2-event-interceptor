package ide

import net.ltgt.gradle.apt.apt
import net.ltgt.gradle.apt.factorypath
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.plugins.ide.eclipse.GenerateEclipseJdt
import org.gradle.plugins.ide.eclipse.model.AbstractClasspathEntry
import org.gradle.plugins.ide.eclipse.model.Classpath
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.eclipse.model.SourceFolder

object EclipseConfig {

    fun apply(project: Project) {
        with(project) {
            plugins.apply("org.gradle.java")
            plugins.apply("org.gradle.eclipse")
            plugins.apply("net.ltgt.apt-eclipse")
            configure<EclipseModel> {
                jdt {
                    apt {
                        // whether annotation processing is enabled in Eclipse
                        isAptEnabled = true

                        // where Eclipse will output the generated sources; value is interpreted as per project.file()
                        setGenSrcDir(file(".apt_generated"))

                        // whether annotation processing is enabled in the editor
                        isReconcileEnabled = true
                    }
                }

                classpath {
                    // customize generation of .classpath files
                    defaultOutputDir = file("build/classes-main-ide")

                    val jmhCompile = configurations.findByName("jmhCompile")
                    if (jmhCompile != null) {
                        plusConfigurations.add(jmhCompile)
                    }

                    file {
                        // ignore warnings for generated code
                        whenMerged.add(addAptGeneratedEntry())
                        whenMerged.add(addAptGeneratedTestsEntry())
                    }
                }

                factorypath {
                    plusConfigurations = listOf(
                            configurations.getByName("annotationProcessor"),
                            configurations.getByName("testAnnotationProcessor")
                    )
                }
            }
            val eclipseJdtPrepare = tasks.register("eclipseJdtPrepare", Copy::class) {
                from(rootProject.file("config/eclipse"))
                into(project.file(".settings/"))
                include("*.prefs")
            }
            tasks.named<GenerateEclipseJdt>(EclipsePlugin.ECLIPSE_JDT_TASK_NAME) {
                dependsOn(eclipseJdtPrepare)
            }
        }
    }

    private fun addAptGeneratedEntry(): Action<Classpath> {
        return Action<Classpath> {
            val name = ".apt_generated"
            val entry = this.entries.find {
                it is AbstractClasspathEntry &&
                        it.path.contains(name)
            }
            if (entry == null) {
                val e = SourceFolder(name, null)
                e.entryAttributes["ignore_optional_problems"] = "true"
                this.entries.add(e)
            }
        }
    }

    private fun addAptGeneratedTestsEntry(): Action<Classpath> {
        return Action<Classpath> {
            val name = ".apt_generated_tests"
            val entry = this.entries.find {
                it is AbstractClasspathEntry &&
                        it.path.contains(name)
            }
            if (entry == null) {
                val e = SourceFolder(name, "bin/test")
                e.entryAttributes["ignore_optional_problems"] = "true"
                e.entryAttributes["test"] = "true"
                e.entryAttributes["optional"] = "true"
                this.entries.add(e)
            }
        }
    }
}
