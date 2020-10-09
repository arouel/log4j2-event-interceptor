package checks

import net.ltgt.gradle.errorprone.errorprone
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import version.Dependency

object ErrorProneConfig {
    fun apply(project: Project) {
        with(project) {
            plugins.apply("net.ltgt.errorprone")

            val errorproneVersion = Dependency.errorprone
            val errorproneJavacVersion = "9+181-r4173-1"
            dependencies.add("errorprone", "com.google.errorprone:error_prone_core:$errorproneVersion")
            dependencies.add("errorproneJavac", "com.google.errorprone:javac:$errorproneJavacVersion")

            tasks.withType(JavaCompile::class).configureEach {
                options.errorprone.disable("EmptyBlockTag", "MissingSummary")
                options.errorprone.disableWarningsInGeneratedCode.set(true)
                if (name.equals("compileJmhJava")) {
                    options.errorprone.isEnabled.set(false)
                }
            }
        }
    }
}
