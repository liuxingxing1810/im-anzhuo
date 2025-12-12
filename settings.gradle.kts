pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AuroraWave"
include(
    ":app",
    ":core:design",
    ":core:common",
    ":core:network",
    ":core:data",
    ":feature:messages",
    ":feature:connections",
    ":feature:discover",
    ":feature:profile",
    ":feature:settings",
    ":feature:auth"
)
