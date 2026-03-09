plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.postgresql:postgresql:42.7.2")
}

tasks.test {
    useJUnitPlatform()
}

// Указываем кодировку для компиляции
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Указываем кодировку для тестов
tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

// Указываем кодировку для запуска (если будешь запускать через Gradle)
tasks.withType<JavaExec> {
    systemProperty("file.encoding", "UTF-8")
}