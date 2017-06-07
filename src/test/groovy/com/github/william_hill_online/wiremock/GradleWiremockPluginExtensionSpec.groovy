package com.github.william_hill_online.wiremock

import spock.lang.Shared
import spock.lang.Specification

import static com.github.william_hill_online.wiremock.GradleWiremockPluginExtension.DEFAULT_DIR

class GradleWiremockPluginExtensionSpec extends Specification {

    @Shared def pluginExtension = new GradleWiremockPluginExtension()

    def 'when plugin is not configured default dir should be the unique parameter'() {
        expect:
        ["--root-dir=$DEFAULT_DIR"] == pluginExtension.getAllParams()
    }

    def 'should create an array element for each parameter'() {
        given:
        pluginExtension.dir = "test/mappings"
        pluginExtension.params = "--port=6890 --bind-address=localhost"

        expect:
        ["--root-dir=test/mappings", "--port=6890", "--bind-address=localhost"] == pluginExtension.getAllParams()
    }
}
