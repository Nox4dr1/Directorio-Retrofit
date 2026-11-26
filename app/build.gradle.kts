import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Datos secretos (como la API key) viven en local.properties, que git ignora.
val localProperties = Properties().apply {
    val archivo = rootProject.file("local.properties")
    if (archivo.exists()) archivo.inputStream().use { load(it) }
}

android {
    namespace = "com.upb.directorio"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.upb.directorio"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        // La API key llega a BuildConfig.API_KEY sin escribirla en el código.
        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY", "")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Consumo de API REST: Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Registro de peticiones en Logcat (solo para depurar)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Corrutinas: funciones suspend y viewModelScope
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    // ViewModel dentro de Compose: viewModel()
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
}