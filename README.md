# incremental-annotation-processor
Testing for incremental DI

# setup your box for JDK 7 gradle builds:

Incap needs JDK 7 (as gradle does) add the path to your JDK 7 folder in your `~/gradle/gradle.properties` file:
`java7Home=/Library/Java/JavaVirtualMachines/1.7.0_79.jdk/Contents/Home`


# How to build a version of gradle to test incremental annotation processing

1. clone gradle
1. run gradle configuration step then kill gradle : `./gradlew clean build`. The goal is to resolve all dependencies.
1. use the script `tools/create-command.rb`. If it fails, run gradle configuration step again.
1. use the script `tools/install-gradle.rb`
1. use the incremental compiler option in your build
1. compile using: `~/gradle-source-build/bin/gradle :app:compileJava -d`

We are currently using this gradle fork to work: https://www.github.com/gstevey/gradle
