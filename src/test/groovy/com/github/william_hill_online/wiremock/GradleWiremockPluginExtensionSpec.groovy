package com.github.william_hill_online.wiremock

import spock.lang.Specification

class GradleWiremockPluginExtensionSpec extends Specification {

    def pluginExtension = new GradleWiremockPluginExtension()

    def 'timeout can be supplied as a number in seconds'() {
        given:
        pluginExtension.dir = "test/mappings"
        pluginExtension.params = "--port 6890"

        expect:
        ["--root-dir=test/mappings", "--port", "6890"] == pluginExtension.getAllParams()
    }
}
