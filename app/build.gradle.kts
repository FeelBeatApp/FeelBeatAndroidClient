plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.github.feelbeatapp.androidclient"
    compileSdk = 35
    ndkVersion = "28.0.12433566"

    defaultConfig {
        applicationId = "com.github.feelbeatapp.androidclient"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "SOCKET_URI", "\"ws://10.0.2.2\"")
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"0368b2bddb504887b517fc4e8fca9cc5\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI", "\"feelbeat://callback\"")
            buildConfigField("String", "SPOTIFY_SCOPE", "\"user-read-private user-read-email\"")
            buildConfigField(
                "String",
                "SPOTIFY_AUTHORIZE_URI",
                "\"https://accounts.spotify.com/authorize\"",
            )
            buildConfigField(
                "String",
                "SPOTIFY_TOKEN_URI",
                "\"https://accounts.spotify.com/api/token\"",
            )
            buildConfigField(
                "String",
                "SPOTIFY_REFRESH_URI",
                "\"https://accounts.spotify.com/api/token\"",
            )
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            buildConfigField("String", "SOCKET_URI", "\"ws://10.0.2.2\"")
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"0368b2bddb504887b517fc4e8fca9cc5\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI", "\"feelbeat://callback\"")
            buildConfigField("String", "SPOTIFY_SCOPE", "\"user-read-private user-read-email\"")
            buildConfigField(
                "String",
                "SPOTIFY_AUTHORIZE_URI",
                "\"https://accounts.spotify.com/authorize\"",
            )
            buildConfigField(
                "String",
                "SPOTIFY_TOKEN_URI",
                "\"https://accounts.spotify.com/api/token\"",
            )
            buildConfigField(
                "String",
                "SPOTIFY_REFRESH_URI",
                "\"https://accounts.spotify.com/api/token\"",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.window.size)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.browser)
    implementation(libs.kotlinx.serialization)
    implementation(libs.security.crypto)

    // Async image
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // Coil
    implementation(libs.coil.kt.coil.compose)

    // ktor
    implementation(libs.io.ktor.client)
    implementation(libs.io.ktor.content.negotiation)
    implementation(libs.io.ktor.json.serialization)
    implementation(libs.fuzzywuzzy)

    // hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.android.mockk)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
