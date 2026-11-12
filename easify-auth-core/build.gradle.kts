@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    id("maven-publish")
}

group = "com.github.ahbandegan.Easify-Android"
version = libs.versions.easify.get()

kotlin {
    jvmToolchain(17)
    withSourcesJar(publish = true)

    jvm()

    android {
        namespace = "ir.amirhesambandegan.easify_auth_core"
        compileSdk = 37
        minSdk = 24
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
        nodejs()
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
