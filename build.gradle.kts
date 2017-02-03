buildscript {

    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", "1.1.0-beta-22"))
    }
}

apply {
    plugin("kotlin")
    plugin("maven")
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib", "1.1.0-beta-22"))
    testCompile("io.kotlintest:kotlintest:1.3.5")
    compile("com.github.elect86:glm:ee12ebece0")
    compile("net.java.dev.jna", "jna", "4.3.0")
}

allprojects {
    repositories {
        maven { setUrl("https://jitpack.io") }
    }
}