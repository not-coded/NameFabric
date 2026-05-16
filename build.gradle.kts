plugins {
	id("dev.kikugie.loom-back-compat") version "0.3"
	id("com.modrinth.minotaur") version "2.+"
}

val modId: String = sc.properties["mod.id"]
val modGroup: String = sc.properties["mod.group"]
val modVersion: String = sc.properties["mod.version"]
val modVersionName: String = sc.properties["mod.version_name"]
val minecraftVersion: String = sc.properties["deps.minecraft"]
val fabricLoaderVersion: String = sc.properties["deps.fabric_loader"]
val fabricApiVersion: String = sc.properties["deps.fabric_api"]
val javaTargetVersion: String = sc.properties["deps.java"]
val minecraftTarget: String = sc.properties["mod.target"]

version = "$modVersion+$modVersionName"
group = modGroup
base.archivesName = modId

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	loomx.applyMojangMappings()

	modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
	modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

loom {
	fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json") // Useful for interface injection

	decompilerOptions.named("vineflower") {
		options.put("mark-corresponding-synthetics", "1") // Adds names to lambdas - useful for mixins
	}

	runConfigs.all {
		ideConfigGenerated(true)
		vmArgs("-Dmixin.debug.export=true") // Exports transformed classes for debugging
		runDir = "../../run" // Shares the run directory between versions
	}
}

tasks.named<ProcessResources>("processResources") {
	val expandProps = mapOf(
		"version" to project.version,
		"minecraftVersion" to minecraftTarget,
		"javaVersion" to javaTargetVersion
	)

	filesMatching("fabric.mod.json") {
		expand(expandProps)
	}

	inputs.properties(expandProps)
}

java {
	withSourcesJar()

	val javaVersion = JavaVersion.toVersion(javaTargetVersion)

	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

tasks.register<Copy>("buildAndCollect") {
	group = "build"
	from(loomx.modJar.map { it.archiveFile })
	into(rootProject.layout.buildDirectory.file("libs"))
	dependsOn("build")
}


modrinth {
	token.set(System.getenv("MODRINTH_TOKEN"))
	projectId.set("namefabric")
	versionNumber.set(version.toString())
	versionName.set("v$version")
	versionType.set("release")
	uploadFile.set(loomx.modJar)
	gameVersions.addAll(sc.properties.rawOrNull("publishing", "target")?.asList().orEmpty().map { it.toString() })
	loaders.addAll("fabric", "quilt")
	//featured = true

	dependencies {
		required.project("fabric-api")
	}

	changelog = rootProject.file("CHANGES.md").readText()
}
