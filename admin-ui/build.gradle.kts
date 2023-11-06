import com.moowork.gradle.node.npm.NpmTask


plugins {
    id("com.moowork.node") version "1.3.1"
    id("java")
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("com.moowork.gradle:gradle-node-plugin:1.3.1")
    }
}



apply(plugin = "com.moowork.node")

node{
    version = "21.1.0"
    npmVersion = "10.2.0"
    download = true
}


tasks{
    findByName("jar")?.dependsOn("ngBuild")

//   register<NpmTask>("npmStart"){
//        dependsOn("bootJar")
//        println("Task - npmStart")
//        setArgs(listOf("ng","serve"))
//    }
//
//    register<NpmTask>("npmRunBuild"){
//        println("Task - npmRunBuild")
//        setArgs(listOf("run","build"))
//    }
//    findByName("npmRunBuild")?.dependsOn("buildFrontend")

    register<Exec>("ngBuild"){
        println("Task - ngBuild")
        val command = if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            listOf("ng.cmd", "build")
        } else {
            listOf("ng", "build")
        }
        commandLine(command)

    }

  jar{
      from("build/admin-ui"){
        into("static")
      }
  }
}



