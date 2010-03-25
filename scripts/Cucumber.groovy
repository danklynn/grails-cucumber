includeTargets << new File("${cucumberPluginDir}/scripts/_CucumberSetup.groovy")

target(cucumber: "Runs cucumber against all the features in the 'features' directory.") {
    def dependencies = [setup, compile, packagePlugins]
    //if (testOptions.clean) dependencies = [clean] + dependencies
    depends(*dependencies)

    def pathToFeatures = "${basedir}/features"
    if (!new File(pathToFeatures).isDirectory()) {
        mkdir(dir: pathToFeatures)
        mkdir(dir: "${pathToFeatures}/step_definitions")
    }

    //todo: move step definitions into the test/cucumber folder and use spring to find them:  objectFactory: 'SpringFactory'
    "cucumber"(args: "--color --format pretty --format junit --out target/junit-report --guess ${pathToFeatures.replaceAll(' ', '\\\\ ')}") {
        classpath {
            path(refid:"jruby.classpath")
        }
    }
}

setDefaultTarget("cucumber")
