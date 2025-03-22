plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.hiltAndroid)
}

android {
    namespace = "br.com.velhatech.firebase.auth"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
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