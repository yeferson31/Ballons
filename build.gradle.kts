plugins {
    java
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/" )
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly ("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    implementation ("commons-codec:commons-codec:1.15")
    compileOnly ("com.mojang:authlib:1.5.25")
    compileOnly ("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    compileOnly ("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly(files("/lib/spigot-1.8.8-R.jar"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}