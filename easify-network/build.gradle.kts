plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "ir.amirhesambandegan.easify_network"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.ahbandegan.Easify-Android"
                artifactId = "easify-network"
                version = libs.versions.easify.get()
            }
        }
    }
}

dependencies {
    api(project(":easify-network-core"))
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.activity)
    api(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.ktor.client.cio)
    api(libs.androidx.compose.material.icons.core)
}
