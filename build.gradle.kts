import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

val testcontainersVersion = "1.15.3"
val postgresVersion="42.3.1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.postgresql:postgresql:42.3.1")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.10.4")
	testImplementation("com.ninja-squad:springmockk:3.0.1")

	testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
	testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
	testImplementation("org.testcontainers:postgresql:$testcontainersVersion")

	implementation("org.postgresql:postgresql:$postgresVersion")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

springBoot{
	mainClass.set("demo.DemoApplicationKt")
}

tasks.test{
	useJUnitPlatform()
}