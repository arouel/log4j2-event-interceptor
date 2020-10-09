package ide

import groovy.util.XmlParser
import net.ltgt.gradle.apt.addAptDependencies
import net.ltgt.gradle.apt.addGeneratedSourcesDirs
import net.ltgt.gradle.apt.apt
import net.ltgt.gradle.apt.configureAnnotationProcessing
import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.gradle.plugins.ide.idea.model.IdeaModel

object IdeaConfig {
    fun apply(project: Project) {
        with(project.rootProject) {
            plugins.apply("org.gradle.java")
            plugins.apply("org.gradle.idea")
            plugins.apply("net.ltgt.apt-idea")
            configure<IdeaModel> {
                project {
                    jdkName = "1.8"
                    languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_1_8)
                    vcs = "Git"
                    configureAnnotationProcessing = true

                    ipr {
                        withXml {
                            with(asNode()) {
                                append(codeFormatSettings)
                                append(inspectionSettings)
                                append(checkstyleSettings)
                            }
                        }
                    }
                }

                workspace {
                    iws {
                        withXml {
                            with(asNode()) {
                                append(insightWorkspaceSettings)
                                append(compilerSettings)
                            }
                        }
                    }
                }
            }
            tasks.named<DefaultTask>("idea") {
                dependsOn(tasks.named("cleanIdea"))
            }
        }
        with(project) {
            plugins.apply("org.gradle.java")
            plugins.apply("org.gradle.idea")
            plugins.apply("net.ltgt.apt-idea")
            configure<IdeaModel> {

                module {
                    apt {
                        addGeneratedSourcesDirs = true
                        addAptDependencies = true
                    }

                    outputDir = file("build/classes-main-ide")
                    testOutputDir = file("build/classes-test-ide")
                    testSourceDirs = testSourceDirs.plus(file("src/test/scala"))
                    testSourceDirs = testSourceDirs.plus(file("src/it/java"))
                    testSourceDirs = testSourceDirs.plus(file("src/jmh/java"))
                    testResourceDirs = testResourceDirs.plus(file("src/it/resources"))

                    configurations.findByName("jmhCompile")?.let {
                        scopes["TEST"]?.get("plus")?.add(it)
                    }
                    inheritOutputDirs = false
                }
            }
        }
    }

    private val codeFormatSettings = XmlParser().parseText(
        """
                  <component name="ProjectCodeStyleConfiguration">
                    <option name="USE_PER_PROJECT_SETTINGS" value="true" />
                    <code_scheme name="Project" version="173">
                      <JavaCodeStyleSettings>
                        <option name="SPACE_AFTER_CLOSING_ANGLE_BRACKET_IN_TYPE_ARGUMENT" value="true" />
                        <option name="CLASS_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999" />
                        <option name="NAMES_COUNT_TO_USE_IMPORT_ON_DEMAND" value="999" />
                        <option name="PACKAGES_TO_USE_IMPORT_ON_DEMAND">
                          <value>
                            <package name="org.mockito.Matchers" withSubpackages="false" static="false" />
                            <package name="org.mockito.Mockito" withSubpackages="false" static="false" />
                            <package name="org.assertj.core.api.Assertions" withSubpackages="false" static="false" />
                            <package name="com.google.common.base.Preconditions" withSubpackages="false" static="false" />
                            <package name="com.google.common.base.Verify" withSubpackages="false" static="false" />
                          </value>
                        </option>
                        <option name="IMPORT_LAYOUT_TABLE">
                          <value>
                            <package name="" withSubpackages="true" static="true" />
                            <emptyLine />
                            <package name="java" withSubpackages="true" static="false" />
                            <emptyLine />
                            <package name="" withSubpackages="true" static="false" />
                            <emptyLine />
                          </value>
                        </option>
                        <option name="ENABLE_JAVADOC_FORMATTING" value="false" />
                      </JavaCodeStyleSettings>
                      <codeStyleSettings language="JAVA">
                        <option name="RIGHT_MARGIN" value="200" />
                        <option name="BLANK_LINES_AROUND_FIELD" value="1" />
                        <option name="BLANK_LINES_AROUND_FIELD_IN_INTERFACE" value="1" />
                        <option name="ALIGN_MULTILINE_PARAMETERS" value="false" />
                        <option name="SPACE_WITHIN_ARRAY_INITIALIZER_BRACES" value="true" />
                        <option name="KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE" value="true" />
                        <option name="KEEP_SIMPLE_CLASSES_IN_ONE_LINE" value="true" />
                        <indentOptions>
                          <option name="INDENT_SIZE" value="4" />
                          <option name="TAB_SIZE" value="4" />
                          <option name="CONTINUATION_INDENT_SIZE" value="8" />
                        </indentOptions>
                        <arrangement>
                          <rules>
                            <section>
                              <rule>
                                <match>
                                  <AND>
                                    <FIELD>true</FIELD>
                                    <STATIC>true</STATIC>
                                  </AND>
                                </match>
                                <order>BY_NAME</order>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <AND>
                                    <INITIALIZER_BLOCK>true</INITIALIZER_BLOCK>
                                    <STATIC>true</STATIC>
                                  </AND>
                                </match>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <FIELD>true</FIELD>
                                </match>
                                <order>BY_NAME</order>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <INITIALIZER_BLOCK>true</INITIALIZER_BLOCK>
                                </match>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <CONSTRUCTOR>true</CONSTRUCTOR>
                                </match>
                                <order>BY_NAME</order>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <AND>
                                    <METHOD>true</METHOD>
                                    <STATIC>true</STATIC>
                                  </AND>
                                </match>
                                <order>BY_NAME</order>
                              </rule>
                            </section>
                            <section>
                              <rule>
                                <match>
                                  <METHOD>true</METHOD>
                                </match>
                                <order>BY_NAME</order>
                              </rule>
                            </section>
                          </rules>
                        </arrangement>
                      </codeStyleSettings>
                    </code_scheme>
                  </component>
        """.trimIndent()
    )

    private val inspectionSettings = XmlParser().parseText(
        """
                  <component name="InspectionProjectProfileManager">
                    <profile version="1.0">
                      <option name="myName" value="Project Default" />
                      <inspection_tool class="unused" enabled="true" level="WARNING" enabled_by_default="true" test_entries="false">
                        <option name="LOCAL_VARIABLE" value="true" />
                        <option name="FIELD" value="true" />
                        <option name="METHOD" value="true" />
                        <option name="CLASS" value="true" />
                        <option name="PARAMETER" value="true" />
                        <option name="REPORT_PARAMETER_FOR_PUBLIC_METHODS" value="true" />
                        <option name="ADD_MAINS_TO_ENTRIES" value="true" />
                        <option name="ADD_APPLET_TO_ENTRIES" value="true" />
                        <option name="ADD_SERVLET_TO_ENTRIES" value="true" />
                        <option name="ADD_NONJAVA_TO_ENTRIES" value="true" />
                      </inspection_tool>
                    </profile>
                    <version value="1.0" />
                   </component>
        """.trimIndent()
    )

    private val checkstyleSettings = XmlParser().parseText(
        """
                  <component name="CheckStyle-IDEA">
                    <option name="configuration">
                      <map>
                        <entry key="active-configuration" value="LOCAL_FILE:${'$'}PRJ_DIR$/config/checks/checkstyle.xml:Datameer" />
                        <entry key="checkstyle-version" value="8.12" />
                        <entry key="copy-libs" value="false" />
                        <entry key="location-0" value="BUNDLED:(bundled):Sun Checks" />
                        <entry key="location-1" value="BUNDLED:(bundled):Google Checks" />
                        <entry key="location-2" value="LOCAL_FILE:\${'$'}PRJ_DIR$/config/checks/checkstyle.xml:Datameer" />
                        <entry key="scan-before-checkin" value="false" />
                        <entry key="scanscope" value="JavaOnly" />
                        <entry key="suppress-errors" value="false" />
                      </map>
                    </option>
                  </component>
        """.trimIndent()
    )

    private val insightWorkspaceSettings = XmlParser().parseText(
        """
                  <component name="CodeInsightWorkspaceSettings">
                    <option name="optimizeImportsOnTheFly" value="true" />
                  </component>
        """.trimIndent()
    )

    private val compilerSettings = XmlParser().parseText(
        """
                  <component name="CompilerWorkspaceConfiguration">
                    <option name="MAKE_PROJECT_ON_SAVE" value="true" />
                    <option name="PARALLEL_COMPILATION" value="true" />
                  </component>
        """.trimIndent()
    )
}
