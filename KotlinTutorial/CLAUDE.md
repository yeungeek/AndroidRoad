# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all modules
./gradlew build

# Build only the tour (pure Kotlin) module
./gradlew :tour:build

# Build only the Android app module
./gradlew :app:build

# Run unit tests for the app module
./gradlew :app:test

# Run a single test class
./gradlew :app:test --tests "com.yeungeek.kotlintutorial.ExampleUnitTest"

# Run Android instrumented tests (requires connected device/emulator)
./gradlew :app:connectedAndroidTest

# Assemble debug APK
./gradlew :app:assembleDebug
```

## Architecture

This project is a **Kotlin learning repository** with two Gradle modules:

### `:tour` — Pure Kotlin Tutorial Module
- A `java-library` module with **no Android dependencies**, JVM 11 target.
- Package root: `com.yeungeek.tour`
- Organized into two sub-packages:
  - `beginner/` — foundational concepts: types, control flow, functions, null safety, collections, classes
  - `intermediate/` — advanced concepts: open classes, objects/companion objects, lambdas, extension functions, scope functions, delegated properties
- Each topic is self-contained in its own `.kt` file. New topics follow this one-file-per-concept pattern.

### `:app` — Android Shell Module
- A Jetpack Compose Android app (`com.yeungeek.kotlintutorial`), minSdk 24, targetSdk 36.
- Currently a minimal scaffold: single `MainActivity`, Compose Material3 theme under `ui/theme/`.
- Intended to eventually demonstrate Kotlin concepts in an Android context.

### Module Relationship
The `:tour` module is independent of `:app`. Kotlin concepts are developed and demonstrated in `:tour`; `:app` is the Android host but does not currently depend on `:tour`.
