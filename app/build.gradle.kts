import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            load(file.inputStream())
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.87"
        multiDexEnabled = true  // Ditambahkan untuk mendukung jCIFS

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DB_IP", "\"${localProperties["DB_IP"]}\"")
        buildConfigField("String", "DB_PORT", "\"${localProperties["DB_PORT"]}\"")
        buildConfigField("String", "DB_USER", "\"${localProperties["DB_USER"]}\"")
        buildConfigField("String", "DB_PASS", "\"${localProperties["DB_PASS"]}\"")
        buildConfigField("String", "DB_NAME", "\"${localProperties["DB_NAME"]}\"")

    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/*.kotlin_module",
                "META-INF/BC1024KE.SF",
                "META-INF/BC1024KE.DSA",
                "META-INF/BC2048KE.SF",
                "META-INF/BC2048KE.DSA"
            )
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Tambahkan multidex
    implementation("androidx.multidex:multidex:2.0.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.jtds)
    implementation(libs.jbcrypt)
    implementation(libs.itext7.core)
    implementation(libs.smbj)
    implementation(libs.bouncycastle)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    implementation(libs.zxing.core)
    implementation(libs.zxing.android.embedded)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation(libs.paging.runtime)
    implementation(libs.recyclerview)

    implementation(libs.java.websocket)

    implementation(libs.swiperefresh)

    implementation(libs.viewpager2)
    implementation(libs.cardview)

}