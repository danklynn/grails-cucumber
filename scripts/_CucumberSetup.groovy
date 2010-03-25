includeTargets << grailsScript("Init")
includeTargets << grailsScript("Package")
includeTargets << grailsScript("Bootstrap")
//includeTargets << grailsScript("_GrailsBootstrap")
includeTargets << grailsScript("_GrailsRun")
includeTargets << grailsScript("_GrailsSettings")
includeTargets << grailsScript("_GrailsClean")


target(setup: "The description of the script goes here!") {
    depends( classpath )

    taskdef (name: 'cucumber', classname: 'cuke4duke.ant.CucumberTask')

    def jrubyHome = "${cucumberPluginDir}/lib/.jruby"
    property(name: 'jruby.home', value: jrubyHome)
    path(id: 'jruby.classpath') {
        fileset( dir: "${grailsSettings.grailsHome}/lib/") {
            include name: "**/*.jar"
        }
        
        fileset(dir: "${cucumberPluginDir}") {
            include name: "**/*.jar"
        }
        fileset(dir: "target/classes") {
            include name: "**/*.class"
        }
    }

    if (!new File(jrubyHome).isDirectory()) {
        echo("Installing required JRuby gems...")
        taskdef(name:"gem", classname:"cuke4duke.ant.GemTask")
        gem args: "install cuke4duke --version 0.2.4 --source http://gemcutter.org/"
    }
}