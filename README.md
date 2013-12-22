SaveSSD plugin for Gradle
=========================

Gradle plugin that helps to save your SSD write cycles by linking project build directory into /tmp on *nix.

How it works
------------
It works on nix systems (Linux, BSD, Mac), assuming, that your */tmp* directory is mounted as *tmpfs* and resides in computer's RAM (on Windows it just does nothing).
Before project build **SaveSSD** checks, if *project.buildDir* directory exists in project. If it exists and not empty, it does nothing, else creates */tmp/gradle/your-project-path* dir and creates *project.buildDir* symlink to it. So, your project structure remanes same as before, but temporary files, that are often rewritten, are kept in memory.

Plugin can be enabled or disabled through project properties (in *gradle.properties* file), so different team members can enable or disable it individually. By default it does nothing until you set *saveSSD=true*.

### Pros ###
+ Extend SSD lifetime by reducing number of writes to it
+ Speeds up build of small projects, because build files are kept in memory, so it can be usefull for HDD owners too

### Cons ###
- Your project */build* dir contents will be lost on system reboot, relogin, etc.
- Increased memory usage

How to use
-----
1. **gradle.build**

    ```groovy
    buildscript {
        repositories {
            maven {
                url 'http://dl.bintray.com/haunted-soft/maven'
            }
        }
        dependencies {
            classpath 'com.haunted.gradle-plugins:save-ssd:+@jar'
        }
    }
    
    apply plugin:'save-ssd'
    ```
    
2.  **gradle.properties**
    
    ```
    saveSSD = true
    ```

3. Perform *clean* task or delete */build* directory manually, than perform build (or other tasks) in your project as usual.
 
   ```shell
   gradle clean
   gradle build
   ```

   You also can run *saveSsd* task manually - it will run ignoring *saveSSD=true* check but it will do nothing while */build* directory exists and not empty.
Example
-------
You can see a live code sample at [Android MarketBin](https://github.com/populov/MarketBin/tree/master/sample) project.
