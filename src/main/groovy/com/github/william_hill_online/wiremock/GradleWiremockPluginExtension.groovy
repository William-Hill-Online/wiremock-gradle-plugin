package com.github.william_hill_online.wiremock

class GradleWiremockPluginExtension {

    public static final String DEFAULT_DIR = "target/classes"
    def dir = DEFAULT_DIR
    def params

    protected String[] getAllParams() {
        if (params == null || params.isEmpty()) {
            return ["--root-dir=$dir"]
        }
        return "--root-dir=$dir $params".split(" ")
    }
}
