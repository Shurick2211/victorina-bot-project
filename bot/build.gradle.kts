import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.18-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    `maven-publish`
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

springBoot {
    buildInfo()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Shurick2211/victorina-bot-project") // Replace with your GitHub repository URL

            credentials {
                username =  System.getenv("USERNAME_GITHUB_PACKAGES")
                password =  System.getenv("TOKEN_GITHUB_PACKAGES")
            }
        }
    }
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
   // runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
    runtimeOnly(project(":admin-ui","default"))
    implementation(project(":message-services","default"))


}

tasks{
    processResources {
        filesMatching("**/application.properties") {
            expand( project.properties )
        }
    }

    bootRun{
       systemProperties = System.getProperties() as Map<String,String>
   }


    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }


}




