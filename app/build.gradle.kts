plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "aj.dev.event"
        minSdk = 19
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        dataBinding = true
    }
}

dependencies {
    val hilt = "2.41"
    val retrofit = "2.9.0"
    val gson = "2.9.0"
    val okhttp19 = "3.12.12"
    val navigation = "2.4.2"
    val ktx = "2.4.1"
    val multidexVersion = "2.0.1"
    val glide = "4.13.1"
    val mockk = "1.12.4"

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.android.gms:play-services-auth:20.2.0")

    // dependency navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation")

    // dependency livedata and viewmodel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$ktx")

    // dependency injection
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")

    // network
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")

    implementation("com.squareup.okhttp3:okhttp") {
        version { strictly(okhttp19) }
    }

    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp19")

    // parser json
    implementation("com.google.code.gson:gson:$gson")

    // image
    implementation("com.github.bumptech.glide:glide:$glide")
    kapt("com.github.bumptech.glide:compiler:$glide")

    implementation("androidx.multidex:multidex:$multidexVersion")

    // tests
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation("io.mockk:mockk:$mockk")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}