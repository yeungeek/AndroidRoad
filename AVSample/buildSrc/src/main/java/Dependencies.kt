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
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0"

    //Libs
    const val coreKtxVersion = "1.3.1"
    const val appcompatVersion = "1.2.0"
    const val materialVersion = "1.2.1"
    const val constraintlayoutVersion = "2.0.1"

    const val junitVersion = "4.+"
    const val extJunitVersion = "1.1.2"
    const val espressoCoreVersion = "3.3.0"
}

object Libs {
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayoutVersion}"
    const val junit = "junit:junit:${Versions.junitVersion}"
    const val ext_junit = "androidx.test.ext:junit:${Versions.extJunitVersion}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espressoCoreVersion}"
}