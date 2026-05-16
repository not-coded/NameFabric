plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.20.6" /* [SC] DO NOT EDIT */

tasks.register("chiseledBuild") {
    group = "project"
    dependsOn(stonecutter.tasks.named("buildAndCollect"))
}

tasks.register("chiseledModrinth") {
    group = "project"
    dependsOn(stonecutter.tasks.named("modrinth"))
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter.parameters {
    filters.include("**/*.java")
    filters.exclude("**/*.json")
}

stonecutter.tasks {
    order("modrinth")
}
