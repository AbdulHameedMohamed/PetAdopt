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

rootProject.name = "PetAdopt"
include(":app")
include(":logging")
include(":common")
include(":features:animals")
include(":features:search")
include(":features:onboarding")
include(":features:sharing")
