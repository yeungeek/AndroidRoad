rootProject.name = "KMPOHSample"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        // mavenLocal()
        maven("https://maven.eazytec-cloud.com/nexus/repository/maven-public/")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        // mavenLocal()
        maven("https://maven.eazytec-cloud.com/nexus/repository/maven-public/")
    }
}

include(":composeApp")