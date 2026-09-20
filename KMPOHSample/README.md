# KMPOHSample: Kotlin Multiplatform + OpenHarmony 跨平台示例项目

欢迎使用 **KMPOHSample**！这是一个使用 **Kotlin Multiplatform (KMP)** 与 **Compose Multiplatform** 技术构建的跨平台应用示例。

通过本套代码，你可以编写一套 Kotlin / Compose UI 业务逻辑，并将其同时运行在 **Android**、**iOS** 以及 **OpenHarmony (华为鸿蒙)** 三大主流移动操作系统上！

---

## 目录
- [1. 架构总览与工作原理](#1-架构总览与工作原理)
- [2. 项目目录结构](#2-项目目录结构)
- [3. 核心功能与代码说明](#3-核心功能与代码说明)
- [4. 环境准备与依赖](#4-环境准备与依赖)
- [5. 编译与运行指南](#5-编译与运行指南)
- [6. 常见问题排查 (FAQ)](#6-常见问题排查-faq)

---

## 1. 架构总览与工作原理

在多平台开发中，我们希望**业务逻辑和 UI 代码最大化复用**，同时保留各平台的底层原生集成能力：

```
                       ┌───────────────────────┐
                       │      commonMain       │
                       │ (业务逻辑 & Compose UI) │
                       └───────────┬───────────┘
                                   │
         ┌─────────────────────────┼─────────────────────────┐
         │                         │                         │
         ▼                         ▼                         ▼
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│   androidMain   │       │     iosMain     │       │    ohosMain     │
│  (Android 接入)  │       │   (iOS UIKit)   │       │  (OpenHarmony)  │
└────────┬────────┘       └────────┬────────┘       └────────┬────────┘
         │                         │                         │
         ▼                         ▼                         ▼
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│ Android APK 原生 │       │   iOS Framework │       │ HarmonyOS N-API │
│   MainActivity  │       │  ViewController │       │   Shared Lib    │
└─────────────────┘       └─────────────────┘       └─────────────────┘
```

- **统一的 UI 树**：在 `commonMain` 中使用标准 Compose Multiplatform 编写页面与组件。
- **Android 端**：基于 Jetpack Compose 与 ComponentActivity 渲染。
- **iOS 端**：编译为静态 Native Framework，由 `MainViewController.kt` 通过 `ComposeUIViewController` 托管到 iOS UIKit 中。
- **OpenHarmony 端**：编译为 Native 动态链接库 (`libkn.so`)，通过 N-API 与 ArkUI 框架绑定，由 `MainArkUIViewController.kt` 承载绘制。

---

## 2. 项目目录结构

```text
KMPOHSample/
├── composeApp/                 # 跨平台核心模块（包含全部 KMP 源码）
│   ├── src/
│   │   ├── commonMain/         # 全平台共享代码（Compose UI 界面、共享逻辑、通用资源）
│   │   │   ├── kotlin/         # App.kt (通用主入口界面), Platform.kt, Greeting.kt
│   │   │   └── composeResources/# 跨平台图片、多语言文本资源
│   │   ├── androidMain/        # Android 平台专属代码 (MainActivity.kt, AndroidManifest.xml)
│   │   ├── iosMain/            # iOS 平台专属代码 (MainViewController.kt 缩放适配与控制器包装)
│   │   └── ohosMain/           # OpenHarmony 专属代码 (MainArkUIViewController.kt, CInterop 资源互调)
│   └── build.gradle.kts        # KMP 多平台构建配置、Hierarchy 层次结构、依赖配置
├── harmonyApp/                 # 华为鸿蒙 DevEco Studio 原生工程（ArkTS / C++ N-API）
├── iosApp/                     # iOS Xcode 原生工程（SwiftUI / UIKit）
├── runscript/                  # 鸿蒙一键编译与部署脚本 (runOhosApp-Mac.sh / Win.bat)
└── gradle.properties           # 全局构建参数（含 CInterop Commonization、渲染后端配置）
```

---

## 3. 核心功能与代码说明

### 3.1 跨平台主界面 (`commonMain/kotlin/.../App.kt`)
- **功能用途**：应用的核心 UI 交互界面，展示欢迎词、动态打招呼内容、多语言文本资源展示、动态切换动画以及屏幕像素密度读取。
- **核心组件**：
  - `App()`：`@Composable` 根界面。响应用户点击交互并根据状态显示动画与图片。
  - `Greeting().greet()`：演示跨平台获取系统平台名称。

### 3.2 iOS 界面托管与自适应缩放 (`iosMain/kotlin/.../MainViewController.kt`)
- **功能用途**：将跨平台 Compose UI 封装为 iOS 标准的 `UIViewController`，并提供 iPad/宽屏自适应缩放功能。
- **核心函数**：
  - `fun MainViewController(): UIViewController`
    - **说明**：iOS 端原生调用的控制器入口。
    - **实现**：通过 `ComposeUIViewController` 将 `IosAdaptiveContent { App() }` 包装为 UIKit 控制器供 Swift/Objective-C 调用。
  - `private fun IosAdaptiveContent(content: @Composable () -> Unit)`
    - **说明**：以 375dp (iPhone 标准基准宽度) 为参考标准，对宽屏 iOS 设备动态计算并缩放全局像素密度 (`Density`)，实现 dp 布局与 sp 字号同步缩放，无需修改共通 UI 代码。

### 3.3 OpenHarmony N-API 接入 (`ohosMain/kotlin/.../MainArkUIViewController.kt`)
- **功能用途**：将 Compose 渲染输出桥接至鸿蒙 ArkTS Native N-API 运行时。
- **核心函数**：
  - `@CName("MainArkUIViewController") fun MainArkUIViewController(env: napi_env): napi_value`
    - **参数**：`env: napi_env`（ArkTS 引擎上下文环境指针）
    - **返回值**：`napi_value`（导出的 JS/ArkTS 可访问的对象/组件引用）
    - **说明**：初始化协程主线程循环处理器，并通过 `ComposeArkUIViewController` 启动 Compose 鸿蒙渲染循环。

---

## 4. 环境准备与依赖

1. **JDK 版本**：JDK 17 或 JDK 21（推荐使用 Android Studio 自带的 JBR 21）。
2. **Android 开发**：
   - Android Studio (推荐 Ladybug / Jellyfish 或更新版本)
   - Android SDK 35/36
3. **iOS 开发**：
   - macOS 操作系统
   - Xcode 15+ 并配置 CocoaPods / Command Line Tools
4. **OpenHarmony 开发**：
   - DevEco Studio 5.0+
   - OpenHarmony SDK (API 12+)

---

## 5. 编译与运行指南

### 5.1 Android 端
在 Android Studio 中打开项目，选择 `composeApp` 的运行配置，直接连接真机或模拟器点击运行（Run）。

也可以使用命令行打包 APK：
```bash
./gradlew :composeApp:assembleDebug
```

### 5.2 iOS 端
通过 Xcode 打开 `iosApp/iosApp.xcodeproj`：
- 在 Xcode 顶部选择目标模拟器（如 iPhone 16 Pro）或连接真机。
- 点击 **Run (Cmd + R)**，Xcode 会自动调用 Gradle 脚本编译 `ComposeApp.framework` 并完成打包运行。

命令行单独编译 iOS 目标：
```bash
# 模拟器 Arm64 架构
./gradlew :composeApp:compileKotlinIosSimulatorArm64

# 真机 Arm64 架构
./gradlew :composeApp:compileKotlinIosArm64
```

### 5.3 OpenHarmony (鸿蒙) 端
运行项目内置的一键构建与同步脚本：
- **macOS**：
  ```bash
  chmod +x ./runscript/runOhosApp-Mac.sh
  ./runscript/runOhosApp-Mac.sh
  ```
- **Windows**：
  ```bat
  .\runscript\runOhosApp-Win.bat
  ```
脚本会自动构建 Native 共享库 `libkn.so` 并复制到 `harmonyApp/entry/libs/` 下，随后可在 DevEco Studio 中打开 `harmonyApp` 进行调试与签名安装。

---

## 6. 常见问题排查 (FAQ)

### Q: 为什么在 IDE 中打开 `MainViewController.kt` 时 Compose 引用会全部标红？
**A**: 在早期 KMP 配置中，若在 `build.gradle.kts` 中手动声明 `sourceSets.create("iosMain")` 并手动编写 `.dependsOn()`，会打破 Kotlin 的默认层次模板（Default Hierarchy Template），导致 IDE 无法把针对 Apple/UIKit 的 Compose 依赖下发至 `iosMain` 模块。
**解决方案**：
本项目已更新至官方推荐的 `applyDefaultHierarchyTemplate` 声明式模板体系，并开启了 `kotlin.mpp.enableCInteropCommonization=true`。如果你在更新代码后仍看到标红，请点击 Android Studio 右上角的 **File -> Sync Project with Gradle Files** 重新同步即可。
