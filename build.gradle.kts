plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

taboolib {
	description {
		contributors {
			name("Mical").description("Maintainer")
		}
	}
	install("common")
	install("common-5")
	install("module-database")
	install("module-ui")
	install("module-configuration")
	install("module-chat")
	install("platform-bukkit")
	classifier = null
	version = "6.0.12-14"

	relocate("com.mcstarrysky.starrysky", "me.mical.serverguider.taboolib.module.starrysky")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    taboo("org.tabooproject.taboolib:module-parrotx:1.4.19")
	taboo("com.mcstarrysky.taboolib:module-starrysky:1.0.11-6")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}