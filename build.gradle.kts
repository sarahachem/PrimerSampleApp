import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://www.jitpack.io"))
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha12")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    }
}

plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://www.jitpack.io")) }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Enable Kotlin backend compilation
            useIR = true
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = false
            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
            freeCompilerArgs += "-Xopt-in=kotlin.Experimental"

            freeCompilerArgs += "-Xjvm-default=all"

            //ExperimentalUnsignedTypes is used for ecg parsing
            freeCompilerArgs += "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes"

            jvmTarget = "11"
        }
    }
}