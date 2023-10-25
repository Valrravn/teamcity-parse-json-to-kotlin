import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.05"

project {

    buildType(Test)

    params {
        param("JsonParam", "")
    }
}

data class MyClass(
    val name: String,
    val value: String
)

 // Gson gson = new Gson();
 // MyType target = new MyType();
 // String json = gson.toJson(target); // serializes target to Json
 // MyClass deserializedParam = gson.fromJson(json, MyClass::class.java); // deserializes json into target2

Gson gson = new Gson()
String serializedJsonString = gson.toJson(MyClass(1,"Test"))
MyClass parsedJson = gson.fromJson(serializedJsonString, MyClass::class.java)

object Test : BuildType({
    name = "Test"

    artifactRules = "example.json"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Create Json"
            id = "Create_Json"
            scriptContent = """
                touch example.json
                echo ${serializedJsonString} > example.json
            """.trimIndent()
        }
        /* script {
            name = "Deserialize Json Test"
            id = "Report_param"
            scriptContent = """
                echo + parsedJson.name
            """.trimIndent()
        } */
    }
})
