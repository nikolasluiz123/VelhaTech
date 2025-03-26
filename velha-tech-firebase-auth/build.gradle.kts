import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
}

val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

val webClientId: String = localProperties.getProperty("WEB_CLIENT_ID", "\"default_value\"")

android {
    namespace = "br.com.velha.tech.firebase.auth"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "WEB_CLIENT_ID", "\"$webClientId\"")
        }

        debug {
            buildConfigField("String", "WEB_CLIENT_ID", "\"$webClientId\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":velha-tech-core"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.google.play.auth)
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.play.services)
    implementation(libs.google.id)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}