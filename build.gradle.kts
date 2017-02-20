buildscript {

    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", "1.1.0-rc-91"))
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
    compile(kotlinModule("stdlib", "1.1.0-rc-91"))
    testCompile("com.github.elect86:kotlintest:c4b7b397a0d182d1adaf61f71a9423c228dc0106")
    compile("com.github.elect86:glm:caaf5141fc6a914dda1ed9d6a4443fc33b6d2238")
    compile("net.java.dev.jna", "jna", "4.3.0")
}

allprojects {
    repositories {
        maven { setUrl("https://jitpack.io") }
    }
}