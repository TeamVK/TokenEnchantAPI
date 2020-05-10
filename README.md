# TokenEnchantAPI

This API allows you to write your own Custom Enchantment effect. Your custom enchantment will automatically be searched and loaded into TokenEnchantment when you place your .jar file in "TokenEnchant/enchants" folder.

## Using maven repository
`TokenEnchantAPI` is hosted on TeamVK's public maven repository and you can reference it in your dev environment.

### Gradle
Here is an example of a fragment of the script you can add to your build.gradle.

```
plugins {
    id "com.github.unafraid.gradle.git-repo-plugin" version "2.0.4.1"
    id "java"
    id "maven-publish"
}

// this will allow you to use github() to specify the github hosted maven repository
apply plugin: "com.github.unafraid.gradle.git-repo-plugin"

repositories {
    jcenter()
    mavenCentral()
    github("teamvk", "maven-repository", "origin", "master", "release")
}

dependencies {
    // ... other dependencies
    compile group: 'org.spigotmc', name: 'spigot', version: spigot_version
    compile group: 'com.vk2gpz.tokenenchant', name: 'TokenEnchantAPI', version: '16.3.3'
    // ... other dependencies
}
```

## [API Documentation](javadoc/index.html)

## Sample Custom Enchantments (CEs)
### [Extending EnchantHandler]
### [Extending PotionHandler]

## [Donation](http://PayPal.Me/vk2gpz)
It would be greatly appreciated for your donation to continue providing support for this plugin.
