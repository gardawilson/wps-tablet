import java.util.Properties

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

    // Pilih environment: -PbuildEnv=prod  (default: dev).
    // URL & DB_NAME diambil dari env/<buildEnv>.properties dan MENIMPA local.properties.
    // Rahasia (DB_USER/DB_PASS) & sdk.dir tetap dari local.properties.
    val buildEnv = (project.findProperty("buildEnv") as String?) ?: "dev"
    val envProperties = Properties().apply {
        val file = rootProject.file("env/$buildEnv.properties")
        if (file.exists()) {
            load(file.inputStream())
        } else {
            logger.warn("build.gradle.kts: env/$buildEnv.properties tidak ada - pakai nilai local.properties")
        }
    }
    val appConfig = Properties().apply {
        putAll(localProperties)
        putAll(envProperties)
    }
    logger.lifecycle("WPS Tablet build env = $buildEnv | API = ${appConfig["BASE_URL_API"]} | DB = ${appConfig["DB_NAME"]}")

    // Kredensial signing dibaca dari keystore.properties (gitignored).
    // Buat dari keystore.properties.example. Lihat deploy.sh.
    val keystoreProperties = Properties().apply {
        val file = rootProject.file("keystore.properties")
        if (file.exists()) {
            load(file.inputStream())
        }
    }

    signingConfigs {
        if (keystoreProperties.getProperty("storeFile") != null) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 29
        targetSdk = 34
        versionCode = 12
        versionName = "1.1.84"
        multiDexEnabled = true  // Ditambahkan untuk mendukung jCIFS

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BUILD_ENV", "\"$buildEnv\"")
        buildConfigField("String", "DB_IP", "\"${appConfig["DB_IP"]}\"")
        buildConfigField("String", "DB_PORT", "\"${appConfig["DB_PORT"]}\"")
        buildConfigField("String", "DB_USER", "\"${appConfig["DB_USER"]}\"")
        buildConfigField("String", "DB_PASS", "\"${appConfig["DB_PASS"]}\"")
        buildConfigField("String", "DB_NAME", "\"${appConfig["DB_NAME"]}\"")
        buildConfigField(
            "String",
            "BASE_REPORT_MICROSERVICE",
            "\"${appConfig.getProperty("BASE_REPORT_MICROSERVICE", "http://192.168.10.100:5006/")}\""
        )
        buildConfigField(
            "String",
            "BASE_URL_API",
            "\"${appConfig.getProperty("BASE_URL_API", "http://192.168.10.100:5002")}\""
        )
        buildConfigField(
            "String",
            "DEVICE_SERVICE_BASE",
            "\"${appConfig.getProperty("DEVICE_SERVICE_BASE", "http://192.168.11.79:3000/")}\""
        )
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Pakai signing release bila keystore.properties tersedia.
            signingConfig = signingConfigs.findByName("release")
        }
        // "WPS Tablet" (applicationId & label produksi, TANPA UI Debug Launcher,
        // cek update aktif) tapi bisa di-attach debugger.
        create("staging") {
            initWith(getByName("release"))
            isDebuggable = true
            isMinifyEnabled = false
            // tidak ada applicationIdSuffix -> tetap com.example.myapplication
            matchingFallbacks += listOf("release", "debug")
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
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

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
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")

    // Room (persistent queue)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // WorkManager (retry background job)
    implementation(libs.workmanager)

}
