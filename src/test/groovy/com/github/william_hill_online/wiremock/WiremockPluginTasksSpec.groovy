package com.github.william_hill_online.wiremock

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class WiremockPluginTasksSpec extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    def 'startWiremock task should start a wiremock instance'() {
        given:
        buildFile << """
                    buildscript {
                        repositories {
                            mavenLocal()
                            jcenter()
                        }
                        dependencies {
                            classpath "com.github.william_hill_online:wiremock-gradle-plugin:0.4.1"
                        }
                    }
                    
                    apply plugin: com.github.william_hill_online.wiremock.GradleWiremockPlugin
                    
                    wiremock {
                        dir "test/mappings"
                        params "--port=9090"
                    }
                    
                    task integrationTests() {
                        runWithWiremock = true
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("integrationTests")
                .build()

        then:
        result.getOutput().contains("Starting WireMock with following params: [--root-dir=test/mappings, --port=9090]")
        result.task(":integrationTests").getOutcome() == TaskOutcome.SUCCESS
    }
}
