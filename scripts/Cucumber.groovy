includeTargets << new File("${cucumberPluginDir}/scripts/_CucumberSetup.groovy")

target(cucumber: "Runs cucumber against all the features in the 'features' directory.") {
    def dependencies = [setup, packageApp]
    //if (testOptions.clean) dependencies = [clean] + dependencies
    depends(*dependencies)

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
    def completed = false
    def previousRunMode
    def testingInProcessServer = false

    ftArgs = argsMap["params"]
    if (ftArgs && ftArgs[0] =~ "^http(s)?://") {
        testingBaseURL = ftArgs[0]

        // Shift the args
        ftArgs.remove(0)
    } else {
        // Default to internally hosted app
        testingBaseURL = "http://localhost:$serverPort$serverContextPath"
        if (!testingBaseURL.endsWith('/')) testingBaseURL += '/'
        testingInProcessServer = true
    }

    previousRunMode = System.getProperty('grails.run.mode', '')
    System.setProperty('grails.run.mode', "functional-test")

    try {
        if (testingInProcessServer) {
//            def savedOut = System.out
//            def savedErr = System.err
//            try {
//                new File(reportsDir, "bootstrap-out.txt").withOutputStream {outStream ->
//                    System.out = new PrintStream(outStream)
//                    new File(reportsDir, "bootstrap-err.txt").withOutputStream {errStream ->
//                        System.err = new PrintStream(errStream)

                        if (argsMap["dev-mode"]) {
                            println "Running tests in dev mode"
                            server = configureHttpServer()
                        }
                        else {
                            server = configureHttpServer()
                        }
                        // start it
                        server.start()
//                    }
//                }
//            } finally {
//                System.out = savedOut
//                System.err = savedErr
//            }
        }

        System.out.println "Functional tests running with base url: ${testingBaseURL}"
        c()
        completed = true
    }
    catch (Exception ex) {
        ex.printStackTrace()
        throw ex
    }
    finally {
        if (testingInProcessServer && server) {
            stopWarServer()
        }
        System.setProperty('grails.run.mode', previousRunMode)
        if (completed) {
            //processResults()
        }
    }
}


setDefaultTarget("cucumber")
