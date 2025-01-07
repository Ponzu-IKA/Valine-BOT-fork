plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "amaiice.valine_bot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.kord.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    //Kord:             Kotlin用DiscordAPI
    implementation("dev.kord:kord-core:0.15.0")
    //KTOML:            Kotlin用Tomlライブラリ
    implementation("com.akuleshov7:ktoml-core:0.5.1")
    implementation("com.akuleshov7:ktoml-file:0.5.1")
    //KotlinDiffUtils:  Kotlin用のDiffが簡単に使えるライブラリ
    implementation("io.github.petertrr:kotlin-multiplatform-diff-jvm:0.7.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}