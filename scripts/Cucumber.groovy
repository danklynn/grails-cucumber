includeTargets << new File("${cucumberPluginDir}/scripts/_CucumberSetup.groovy")

target(cucumber: "Runs cucumber against all the features in the 'features' directory.") {
    depends(setup, packageApp)

    def pathToFeatures = "${basedir}/features"
    if (!new File(pathToFeatures).isDirectory()) {
        mkdir(dir: pathToFeatures)
        mkdir(dir: "${pathToFeatures}/step_definitions")
    }

    withServer {
        //todo: move step definitions into the test/cucumber folder and use spring to find them:  objectFactory: 'SpringFactory'
        "run-cucumber"(args: "--color --format pretty --format junit --out target/junit-report --guess \"${pathToFeatures}\"", clonevm: true, failonerror: false) {
            classpath {
                path(refid:"jruby.classpath")
            }
        }
    }
}

def withServer(Closure c) {
    def server
    def previousRunMode = System.getProperty('grails.run.mode', '')
    System.setProperty('grails.run.mode', "cucumber")

    try {
        server = configureHttpServer()
        server.start()

        c()
    }
    catch (Exception ex) {
        ex.printStackTrace()
        throw ex
    }
    finally {
        if (server) {
            stopWarServer()
        }
        System.setProperty('grails.run.mode', previousRunMode)
    }
}


setDefaultTarget("cucumber")
