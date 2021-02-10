import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.jomof"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.ow2.asm:asm:9.0")
    implementation("org.ow2.asm:asm-tree:9.0")
    implementation("org.ow2.asm:asm-analysis:9.0")
    implementation("org.ow2.asm:asm-util:9.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}