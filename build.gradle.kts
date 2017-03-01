buildscript {

    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", "1.1.0"))
    }
}

apply {
    plugin("kotlin")
    plugin("maven")
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
//    flatDir {
//        dirs '../local_lib'
//    }
}

dependencies {
    compile(kotlinModule("stdlib", "1.1.0"))
    testCompile("com.github.elect86:kotlintest:d8878d6da0944ec6bcbcdad6a1540bba021d768d")
    compile("com.github.elect86:glm:05859e02c3529f7a95b4b8cefbeb16f9b5e0b515")
    compile("net.java.dev.jna", "jna", "4.3.0")
}

allprojects {
    repositories {
        maven { setUrl("https://jitpack.io") }
    }
}