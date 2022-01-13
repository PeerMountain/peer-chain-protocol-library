import org.ajoberstar.grgit.Grgit

plugins {
    kotlin("jvm") version "1.5.20"
    id("org.ajoberstar.grgit") version "4.0.1"
    `java-library`
    `maven-publish`
}

description = "Attestation API library"
group = "com.kyc3"
java.sourceCompatibility = JavaVersion.VERSION_11

val grgit = Grgit.open(mapOf("dir" to project.projectDir))
val commit = grgit.head().abbreviatedId
version = commit

dependencies {
    implementation("com.kyc3:oracle-definitions:8bfa4f3")

    implementation("org.igniterealtime.smack:smack-tcp:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-core:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-im:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-extensions:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-java7:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-experimental:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-bosh:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-xmlparser-xpp3:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-xmlparser-stax:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-streammanagement:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-websocket:4.5.0-alpha1-SNAPSHOT")
    implementation("org.igniterealtime.smack:smack-websocket-okhttp:4.5.0-alpha1-SNAPSHOT")

    implementation("com.muquit.libsodiumjna:libsodium-jna:1.0.4") {
        exclude("org.slf4j", "slf4j-log4j12")
    }

    implementation("org.web3j:core:5.0.0")
}

val libraryJar by tasks.registering(Jar::class) {
    group = "build"
    description = "Assembles a JAR containing the Attestation Enginge API library."

    from(sourceSets["main"].java) {
        exclude("**/*.md", "**/*.yaml")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.kyc3"
            artifactId = "attestation-engine-api"
            version = version

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://gitlab.amlapi.com/api/v4/projects/72/packages/maven")
            credentials(HttpHeaderCredentials::class.java) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}