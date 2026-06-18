
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import android.databinding.tool.ext.capitalizeUS

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11) 
        }
    }

    listOf(
        iosX64(),    
        iosArm64(),   
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true    
        }
        iosTarget.compilations.getByName("main") {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xbinary=sanitizer=address")
            }
        }
    }
    
  // 配置OHOS（华为鸿蒙）多架构目标
    listOf(
        ohosArm64(),   // 真机 arm64
        ohosX64()      // 模拟器/开发机 x64
    ).forEach { ohosTarget ->
        ohosTarget.binaries.sharedLib {
            baseName = "kn"
            // Release 链接阶段的 DevirtualizationAnalysis 对这个 sample 的内存占用过高，容易在 OOM 后失败。
            if (buildType == NativeBuildType.RELEASE) {
                optimized = false
            }
            export(libs.compose.multiplatform.export)
            linkerOpts("-lz")
                // 渲染模式
 	             // 背景：当 libkn.so 为旧编译产物时，其 DT_NEEDED 可能缺少以下库（正确构建时
 	             // NativeTasksConfiguration.kt 已通过 -l 选项将它们写入 DT_NEEDED）。
 	             // 在 build.gradle.kts 中统一补全，避免在 CMakeLists.txt 中硬编码。
 	             val rendererBackend = rootProject.findProperty("rendererBackend")?.toString() ?: "fusion-renderer"
                    if (rendererBackend == "fusion-renderer") {
 	                 linkerOpts(
 	                     "-lnative_drawing",    // OH_Drawing_*（字体、绘制）
 	                     "-limage_source",       // OH_ImageSourceNative_*（图像解码）
 	                     "-lpixelmap",           // OH_PixelMap_*
 	                     "-lpixelmap_ndk.z",     // OH_PixelMapNdk_*
 	                     "-lnative_window",      // OH_NativeWindow_*
 	                     "-lace_napi.z",         // N-API
 	                     "-lhilog_ndk.z",        // HiLog 日志
 	                     "-lhitrace_ndk.z",      // HiTrace 性能追踪
 	                     "-luv",                 // libuv 事件循环
 	                     "-lunwind",             // 栈展开
 	                     "-licu",               // ICU 文本处理
 	                 )
 	             }
        }
        ohosTarget.compilations.getByName("main") {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xbinary=sanitizer=address")
            }
            val resource by cinterops.creating {
                defFile(file("src/ohosMain/cinterop/resource.def"))
                includeDirs(file("src/ohosMain/cinterop/include"))
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.collection)
        }
        commonMain.dependencies {
            implementation(compose.runtime)                     
            implementation(compose.foundation)                   
            implementation(compose.material)
            implementation(compose.material3)                    
            implementation(compose.ui)                          
            implementation(compose.components.resources)         
            implementation(compose.components.uiToolingPreview)   
            implementation(libs.kotlinx.coroutines.core)          
            implementation(libs.atomicFu)                       
        }
          // iOS平台共享代码
                val iosMain = sourceSets.create("iosMain").apply {
                    dependsOn(commonMain.get())
                }
                // iOS平台依赖
                iosMain.dependencies {
                }
                // iOS平台变体依赖关系
                listOf("iosX64Main", "iosArm64Main", "iosSimulatorArm64Main").forEach {
                    sourceSets.getByName(it).dependsOn(iosMain)
                }

         // OHOS 共享（对应目录 src/ohosMain/，arm64/x64 共用）
        val ohosMain = sourceSets.create("ohosMain").apply {
            dependsOn(commonMain.get())
        }
        ohosMain.dependencies {
            api(libs.compose.multiplatform.export)
        }
        val ohosArm64Main by getting {
            dependsOn(ohosMain)
        }
        val ohosX64Main by getting {
            dependsOn(ohosMain)
        }
    }
}


android {
    namespace = "com.yeungeek.kmpohsample"                     
    compileSdk = libs.versions.android.compileSdk.get().toInt()  

    defaultConfig {
        applicationId = "com.yeungeek.kmpohsample"                  
        minSdk = libs.versions.android.minSdk.get().toInt()     
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1                                       
        versionName = "1.0"                                  
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false     
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11   
        targetCompatibility = JavaVersion.VERSION_11   
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)   
}



// Harmony App 输出目录（支持命令行 --harmonyAppPath）
val harmonyAppDir: File = run {
    val cliPath = project.findProperty("harmonyAppPath") as String?
    if (cliPath.isNullOrBlank()) {
        // 默认：项目根目录 /harmonyApp
        rootProject.file("harmonyApp")
    } else {
        // 命令行传入的路径
        file(cliPath)
    }
}

// 字符串首字母大写工具函数
fun String.capitalizeUS(): String = this.replaceFirstChar { 
    if (it.isLowerCase()) it.titlecase() else it.toString() 
}


// 为不同类型(debug、release)OHOS构建注册Copy任务并发布到Harmony App目录
arrayOf("debug", "release").forEach { type ->
    tasks.register<Copy>("publish${type.capitalizeUS()}BinariesToHarmonyApp") {
        group = "harmony" // 归类到harmony任务组
        dependsOn(
            "link${type.capitalizeUS()}SharedOhosArm64",
            "link${type.capitalizeUS()}SharedOhosX64",
        )
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
         into(harmonyAppDir) // 输出目标目录
        from("build/bin/ohosArm64/${type}Shared/libkn_api.h") { // 复制头文件
            into("entry/src/main/cpp/include/arm64-v8a/")         // 指定目录
        }
        from(project.file("build/bin/ohosArm64/${type}Shared/libkn.so")) { // 复制共享库文件
            into("/entry/libs/arm64-v8a/")           // 指定目标目录
        }

        from("build/bin/ohosX64/${type}Shared/libkn_api.h") {
	        into("entry/src/main/cpp/include/x86_64/")
	    }
	    from(project.file("build/bin/ohosX64/${type}Shared/libkn.so")) {
	        into("entry/libs/x86_64/")
	    }
	    val composeResourcePackage = "${rootProject.name.lowercase()}.${project.name.lowercase()}.generated.resources"
	    from("src/commonMain/composeResources") {
	        into("entry/src/main/resources/rawfile/composeResources/$composeResourcePackage/")
	    }

    }
}
