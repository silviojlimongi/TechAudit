plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize") //

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.techaudit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.techaudit"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //es contenedor avanzado para mostrar grandes conjuntos de datos que se pueden desplazar en una direccion especifica
    implementation("androidx.cardview:cardview:1.0.0")
    // es el maquillaje que le da el toque moderno y profesional a los elementos
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //manejo de recuersos visuales


    // room database - definir la instrucciones para ques se descarga la configuracion de la base de datos
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Para Corutinas
    ksp("androidx.room:room-compiler:$roomVersion")      // El procesador
}
