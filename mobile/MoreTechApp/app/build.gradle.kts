import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val hostToken: String = gradleLocalProperties(rootDir).getProperty("hostToken", "")
val debugUrl: String = gradleLocalProperties(rootDir).getProperty("debugUrl", "")
val releaseUrl: String = gradleLocalProperties(rootDir).getProperty("releaseUrl", "")

plugins {
    kotlin("kapt")
    id("kotlin-android")
    id("com.android.application")
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"

    defaultConfig {
        applicationId ="ru.smirnov.test.moretechapp"
        minSdk = 24
        targetSdk = 31
        versionCode = 17
        versionName = "0.1 beta"
    }

    buildTypes {

        getByName("debug") {
            buildConfigField("String", "hostToken", hostToken)
            buildConfigField("String", "hostUrl", debugUrl)
        }

        getByName("release") {
            buildConfigField("String", "hostToken", hostToken)
            buildConfigField("String", "hostUrl", releaseUrl)

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    kapt("com.google.dagger:hilt-android-compiler:2.39.1")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")

    implementation("androidx.activity:activity:1.4.0")
    implementation("androidx.fragment:fragment:1.4.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("androidx.camera:camera-camera2:1.1.0-alpha12")
    implementation("androidx.camera:camera-lifecycle:1.1.0-alpha12")
    implementation("androidx.camera:camera-view:1.0.0-alpha32")
    implementation("com.jakewharton:butterknife:8.8.1")
    implementation("com.google.android.material:material:1.6.0-alpha01")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.android.support.constraint:constraint-layout:2.0.4")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.fasterxml.jackson.core:jackson-core:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")
    implementation("com.github.smarteist:autoimageslider:1.4.0")

    implementation("com.google.dagger:hilt-android:2.39.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    testImplementation("junit:junit:4.13.2")
}