@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

group = "com.github.ahbandegan.Easify-Android"
version = libs.versions.easify.get()

kotlin {
    jvmToolchain(17)
    withSourcesJar(publish = true)

    jvm()

    android {
        namespace = "ir.amirhesambandegan.easify_state"
        compileSdk = 37
        minSdk = 24
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()

    js {
        browser {
            testTask {
                enabled = false
            }
        }
    }
    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":easify-state-core"))
            api(libs.kotlinx.coroutines.core)
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.ui)
            api(libs.jetbrains.lifecycle.runtime.compose)
        }
        androidMain.dependencies {
            api(libs.androidx.core.ktx)
            api(libs.kotlinx.coroutines.android)
            api(libs.androidx.lifecycle.viewmodel.ktx)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

// Library modules don't declare a browser executable binary; commonTest runs on JVM/Android/Native.
tasks.matching {
    it.name == "checkComposeUiTestConfigurationForJs" ||
        it.name == "checkComposeUiTestConfigurationForWasmJs"
}.configureEach {
    enabled = false
}
