plugins {
    id("org.jetbrains.intellij") version "1.14.1"
    id("org.kordamp.gradle.markdown") version "2.2.0"
    id("io.freefair.lombok") version "6.4.3"
    java
    idea
    id("org.sonarqube") version "4.2.0.3129"
//    application
}

// intellij 版本（编译环境版本）
val intellijVersion = findProperty("intellijVersion") ?: System.getenv("intellijVersion") ?: "2023.1.2"
// intellij 上传插件 Token
val intellijPublishToken = findProperty("intellijPublishToken") ?: System.getenv("intellijPublishToken")

group = "com.tsintergy"
version = "0.1.3"

println(">>> PROJECT INFO : $group --> { intellij-version = IU-$intellijVersion, plugin-version = $version }")

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo1.maven.org/maven2")
    mavenCentral()
}
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    //    implementation(kotlin("stdlib-jdk8"))
    implementation ("cn.hutool:hutool-core:5.8.5")
    // https://mvnrepository.com/artifact/com.google.guava/guava
    // implementation("com.google.guava:guava:29.0-jre")
    // https://mvnrepository.com/artifact/org.freemarker/freemarker
    implementation("org.freemarker:freemarker:2.3.31")
    // https://mvnrepository.com/artifact/org.apache.velocity/velocity
    // implementation("org.apache.velocity:velocity:1.7")
    // https://mvnrepository.com/artifact/org.apache.velocity/velocity-engine-core
    // implementation("org.apache.velocity:velocity-engine-core:2.2")
    // https://mvnrepository.com/artifact/com.ibeetl/beetl
    implementation("com.ibeetl:beetl:3.10.0.RELEASE")
    implementation("joda-time:joda-time:2.10.10")
    implementation("org.yaml:snakeyaml:1.30")

    // implementation("com.google.code.gson:gson:2.8.5")
    // https://mvnrepository.com/artifact/jalopy/jalopy
    // implementation("jalopy:jalopy:1.5rc3")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    pluginName.set(rootProject.name)
    version.set("IU-${intellijVersion}")
    plugins.set(listOf("DatabaseTools", "java"))
    sandboxDir.set("${rootProject.rootDir}/idea-sandbox")
    updateSinceUntilBuild.set(false)
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    dependsOn("buildSyncFiles")
    options.encoding = "utf-8"
    options.compilerArgs = listOf("-Xlint:deprecation", "-Xlint:unchecked")
}

tasks.publishPlugin{
    // 在 gradle.properties 文件中设置 intellijPublishToken 属性存储 Token 信息
    // https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html
    token.set(intellijPublishToken as String?)
}

tasks.patchPluginXml {
    val notes = file("$buildDir/gen-html/changeNotes.html")
    val desc = file("$buildDir/gen-html/description.html")
//    if (notes.exists() && notes.isFile) {
//        changeNotes.set(notes.readText())
//    }
//    if (desc.exists() && desc.isFile) {
//        pluginDescription.set(desc.readText())
//    }
    outputs.upToDateWhen { false }
    dependsOn("markdownToHtml")
    pluginId.set("com.tsintergy.ssc.database.generator")
    //最小支持版本
    sinceBuild.set("223")
    // 最大支持版本
    untilBuild.set("232")
}

tasks.markdownToHtml {
    sourceDir = file("doc/plugin")
    outputDir = file("$buildDir/gen-html")
}

tasks.runPluginVerifier {
    ideVersions.set(listOf("IU-2019.3.5",
        "IU-2020.1.4",
        "IU-2020.2.4",
        "IU-2020.3.4",
        "IU-2021.1.3",
        "IU-2021.2.4",
        "IU-2021.3.3",
        "IU-2022.1.4",
        "IU-2022.2.5",
        "IU-2022.3.3",
        "IU-2023.1.2",
        "IU-2023.2"))

}

/**
 * 生成插件运行时需要同步的模板文件列表
 */
task("buildSyncFiles") {
    outputs.upToDateWhen { false }
    dependsOn("markdownToHtml")
    val sourcePath = "src/main/resources/"

    val dir = arrayOf("config.yml", "templates/")
    val source = File(rootProject.rootDir, sourcePath)
    val start = source.absolutePath.length

    val filesText = fileTree(sourcePath)
        .map { it.absolutePath.substring(start + 1).replace("\\", "/") }
        .filter { path ->
            dir.any { path.startsWith(it) }
        }
        .joinToString("\n")

    file("${sourcePath}/syncFiles.txt").writeText(filesText)
}
