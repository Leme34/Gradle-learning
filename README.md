### [Gradle](https://gradle.org)（核心是Java，部分Groovy）
- **基本概念**
    1. 	[Distribution](https://services.gradle.org/distributions/)  src（仅源代码）、bin（可运行）、all（除了前者还附带文档、example）
    1. 	Wrapper
		
        - Gradle Wrapper其实就是一个脚本文件，使用 gradle wrapper 创建一组Wrapper文件
		    
        ```
        gradlew (Unix Shell 脚本)
        gradlew.bat (Windows批处理文件)
        gradle/wrapper/gradle-wrapper.jar (Wrapper JAR文件)
        gradle/wrapper/gradle-wrapper.properties (Wrapper属性文件)
        ```
        然后，我们就可以使用gradlew了
        - 设置 Wrapper 版本
            1. gradle wrapper --gradle-version 5.4  
            1. 直接编辑gradle/wrapper/gradle-wrapper.properties文件来修改版本
        - Wrapper 存放位置  
            每次使用gradlew命令，都会下载对应版本的Gradle文件，存放在目录$USER_HOME/.gradle/wrapper/dists 中
        - gradle-wrapper.properties
            ```
            # 下载的url
            distributionUrl=https\://services.gradle.org/distributions/gradle-5.4-all.zip
            # 解压后的文件存放目录
            distributionBase=GRADLE_USER_HOME
            distributionPath=wrapper/dists
            # 下载的gradle-5.4-all.zip所存放的位置
            zipStoreBase=GRADLE_USER_HOME
            zipStorePath=wrapper/dists
            ```
            其中 zipStoreBase   和 distributionBase 有两种取值：GRADLE_USER_HOME 和 PROJECT
            - GRADLE_USER_HOME：环境变量的目录
            - PROJECT：工程的当前目录，即gradlew所在的目录
    1. 构建过程（演示）
        - maven：maven启动一个JVM -> 加载jar -> 执行任务 
        - gradle：默认启用Daemon，分为Client JVM 和 [Daemon JVM](https://docs.gradle.org/current/userguide/gradle_daemon.html)，每次构建都会启动一个Client JVM 连接到 Daemon JVM（Daemon JVM会一直存在无需每次加载），避免昂贵的引导过程以及利用缓存来实现这一点，将有关项目的数据保存在内存中，从而加快构建速度


- **Groovy**
   1. MOP特性
   1. 闭包


- **build.gradle（相当于maven的pom.xml）**
   1. build.gradle HelloWorld
   2. [Gradle Build LifeCycle](https://docs.gradle.org/current/userguide/build_lifecycle.html)
        - Initialization（creates a Project instance for each of projects need to be built）
        - Configuration（run all build.gradle scripts）
        - Execution（execute the tasks created and configured during the configuration phase）
   3. Project对象（树形）
        - parent、childProjects属性
        - allprojects
        - beforeEvaluate、afterEvaluate
        - repositories  
    
        ```
        直接写的方法一般都是 Project 类中的
        ```
    
   1. 最小执行单元 task  
      - configure task
      - dependsOn
   1. [插件](https://docs.gradle.org/current/userguide/plugins.html#sec:using_plugins)
      - groovy类插件（封装）
      - Script plugins
      - Binary Plugins
        ```
        apply plugin: 'java'
        ```
      - java编写的插件（固定名为 buildSrc 的模块）  
        >             若发现子目录存在名为 buildSrc 的 gradle Moudle 会先编译打jar包，
        >     并自动在当前（外层）项目的 buildscript 添加这个jar的 classpath 依赖
        >     从而可以引用java编写的插件
      - buildSrc插件 -> Binary Plugins
        buildSrc插件用来插件开发阶段，通过足够的测试，应该变为 Binary Plugins 发布到仓库中，其他项目通过 dependencies 依赖使用
   1. 引用包的范围
      - 现在我想在 build.gradle 使用引用进来的 jar，该怎么做？
      - classpath的概念   
         build.gradle的classpath 与 Execution phase 的 classpath 是分别独立的，因此需要在 Configuration phase 自己定义一套依赖
        ```
        buildscript {
            repositories {
                mavenCentral()
            }
            dependencies {
                // 把jar包放入 Configuration phase 的 classpath 中
                classpath group: 'org.apache.commons', name: 'commons-lang3', version: '3.9'
            }
        }
        ```
    1. apply plugin: xxx 本质上是gradle通过识别以上4种插件，找到对应的Plugin<T>，实例化并调用其中的 apply 方法
