plugins {
    id("fabric-loom") version("1.6-SNAPSHOT")
    id("maven-publish")
}

version = "1.0.0"
group = "com.goober"

repositories {
    maven {
        name = "Meteor Dev"
        url = uri("https://maven.meteordev.org/releases")
    }
    maven {
        name = "Meteor Dev Snapshots"
        url = uri("https://maven.meteordev.org/snapshots")
    }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.11")
    mappings("net.fabricmc:yarn:1.21.11+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("meteordevelopment:meteor-client:1.21.11-86")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

java {
    withSourcesJar()
}
