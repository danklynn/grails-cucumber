includeTargets << new File("${cucumberPluginDir}/scripts/_CucumberSetup.groovy")

setDefaultTarget("cucumber")
target(cucumber: "Runs cucumber against all the features in the 'features' directory.") {
    depends(setup, packageApp)

    def pathToFeatures = "${basedir}/features"
    if (!new File(pathToFeatures).isDirectory()) {
        mkdir(dir: pathToFeatures)
        mkdir(dir: "${pathToFeatures}/step_definitions")
    }

    def runTests = {
        //todo: move step definitions into the test/cucumber folder and use spring to find them:  objectFactory: 'SpringFactory'
        "run-cucumber"(args: "--color --format pretty --format junit --out target/junit-report --guess \"${pathToFeatures}\"", clonevm: true, failonerror: false) {
            classpath {
                path(refid:"jruby.classpath")
            }
        }
    }

    myArgs = argsMap["params"]
    if (myArgs && myArgs[0] =~ "^run-app") {
        withServer(runTests)
    } else {
        runTests()
    }
}

target(prepareToRun: "preps the appfor running") {
    depends(cleanTestReports, configureProxy)
}

def withServer(Closure c) {
    prepareToRun()
    def server
    def previousRunMode = System.getProperty('grails.run.mode', '')
    System.setProperty('grails.run.mode', "cucumber")

    try {
        runApp()
        c()
    }
    catch (Exception ex) {
        ex.printStackTrace()
        throw ex
    }
    finally {
        stopServer()
        System.setProperty('grails.run.mode', previousRunMode)
    }
}
