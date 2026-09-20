plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}

android {
    namespace = "ir.amirhesambandegan.easify_storage"
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
                artifactId = "easify-storage"
                version = "3.0.0"
            }
        }
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)

    api(project(":easify-security"))
    api(libs.datastore.preferences)
    
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    api(libs.gson)
}


