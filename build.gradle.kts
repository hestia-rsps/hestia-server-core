import java.util.Date
import com.jfrog.bintray.gradle.BintrayExtension.*
import org.gradle.api.publish.maven.MavenPom

plugins {
    kotlin("jvm") version "1.3.61"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    maven
    idea
    java
}

group = "world.gregs.hestia"
version = "0.4.9"

val bintrayUser: String? by project
val bintrayKey: String? by project
val versionName = version.toString()

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/hestia-rsps/Hestia")
}

dependencies {
    //Main
    implementation(kotlin("stdlib-jdk8"))
    implementation("world.gregs.hestia:hestia-cache-store:0.0.6")
    implementation("io.netty:netty-all:4.1.44.Final")
    implementation("org.yaml:snakeyaml:1.25")
    implementation("com.displee:rs-cache-library:6.0")

    //Utilities
    implementation("com.google.guava:guava:28.2-jre")

    //Logging
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    //Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.assertj:assertj-core:3.14.0")
    testImplementation("org.mockito:mockito-core:3.2.4")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("io.mockk:mockk:1.9.3")
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.getByName("main").java.srcDirs)
    archiveClassifier.set("sources")
}

artifacts {
    archives(sourcesJar)
}

fun MavenPom.addDependencies() = withXml {
    asNode().appendNode("dependencies").let { depNode ->
        configurations.compile.get().allDependencies.forEach {
            depNode.appendNode("dependency").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}

publishing {
    publications {
        create("production", MavenPublication::class) {
            artifact("$buildDir/outputs/aar/app-release.aar")
            groupId
            artifactId = "hestia-server-core"
            version = versionName
            pom.addDependencies()
        }
    }
}

bintray {
    user = bintrayUser
    key = bintrayKey
    setConfigurations("archives")
    publish = true
    pkg(delegateClosureOf<PackageConfig> {
        repo = "Hestia"
        name = "hestia-server-core"
        userOrg = "hestia-rsps"
        vcsUrl = "https://github.com/hestia-rsps/hestia-server-core.git"
        setLabels("kotlin")
        setLicenses("BSD 3-Clause")
        publicDownloadNumbers = true
        version(delegateClosureOf<VersionConfig> {
            name = versionName
            desc = "Library of shared core networking features required by all Hestia servers"
            released = Date().toString()
            vcsTag = "v$versionName"
        })
    })
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}