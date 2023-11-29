


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
    version = "20.9.0"
    npmVersion = "10.2.0"
    download = true
}


tasks{
   findByName("jar")?.dependsOn("ngBuild")
   findByName("angInst")?.dependsOn("npmInst")
   findByName("ngBuild")?.dependsOn("angInst")

    register<Exec>("npmInst"){
      val command = if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
        listOf("npm.cmd", "install")
      } else {
        listOf("npm","install")
      }
      commandLine(command)
    }

    register<Exec>("angInst"){
      val command = if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
        listOf("npm.cmd", "install", "-g", "@angular/cli")
      } else {
        listOf("npm","install", "-g","@angular/cli")
      }
      commandLine(command)
    }

    register<Exec>("ngBuild"){
        val command = if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
            listOf("npm.cmd", "run","build")
        } else {
            listOf("npm","run","build")
        }
        commandLine(command)
    }

  jar{
      from("build/admin-ui"){
        into("static")
      }
  }
}



