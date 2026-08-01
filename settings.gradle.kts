// Can not be configured by Conventions-Plugin.
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    val versionMyJavaConventionPlugin = providers.gradleProperty("version_myJavaConventionPlugin")
    val versionSpringDependencyManagementPlugin = providers.gradleProperty("version_springDependencyManagementPlugin")

    plugins {
        id("de.freese.gradle.conventions").version(versionMyJavaConventionPlugin).apply(false)
        id("io.spring.dependency-management").version(versionSpringDependencyManagementPlugin).apply(false)
    }
}

// Without rootProject.name the Name of the Projekt-Directory is used.
// rootProject.name = "jsync2"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

include("jsync2-core")
include("jsync2-console")
include("jsync2-swing")
include("jsync2-test")
