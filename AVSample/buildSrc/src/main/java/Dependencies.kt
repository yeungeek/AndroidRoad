/**
 * version
 */
object Versions {
    //gradle
    const val gradleToolsVersion = "4.2.1"


    const val kotlinVersion = "1.5.0"
    const val compileSdkVersion = 30
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 21
    const val targetSdkVersion = 28
    const val versionCode = 1
    const val versionName = "1.0"

    //Libs
    const val coreKtxVersion = "1.3.1"
    const val coroutinesAndroidVersion = "1.4.1"
    const val lifecycleKtxVersion = "2.2.0"
    const val appcompatVersion = "1.2.0"
    const val materialVersion = "1.2.1"
    const val constraintlayoutVersion = "2.0.1"

    const val junitVersion = "4.+"
    const val extJunitVersion = "1.1.2"
    const val espressoCoreVersion = "3.3.0"
    const val easyPermission = "3.0.0"

    const val cameraxVersion = "1.0.1"
    const val cameraxViewVersion = "1.0.0-alpha27"

    const val timberVersion = "5.0.1"
}

object Libs {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val coroutines_android ="org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroidVersion}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtxVersion}"

    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayoutVersion}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val ext_junit = "androidx.test.ext:junit:${Versions.extJunitVersion}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espressoCoreVersion}"

    const val easy_permissions = "pub.devrel:easypermissions:${Versions.easyPermission}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    const val camerax = "androidx.camera:camera-camera2:${Versions.cameraxVersion}"
    const val camerax_lifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraxVersion}"
    const val camerax_view = "androidx.camera:camera-view:${Versions.cameraxViewVersion}"
}