#Gradle
---

What is Gradle？

- 依赖管理工具
- 基于Groovy语言 面向JAVA应用为主

**张哥原话：**

##What is Gradle？

一种公开的叫法是Gradle是一种**构建系统**。

####什么是构建系统呢？

以Android举例，我们以前开发Android都是用的Eclipse，现在用Android Studio。写完代码直接在IDE上编译、运行，生成apk包并且安装到手机上。这一系列的程序可以算是Android程序构建的一个过程。

在Gradle之前你们可能以为这一系列的过程是IDE的功劳。其实不然，之前是Google有个叫 ADT(Android Development Tools)的东西，在Eclipse和AS上都有。但是自从AS正式版问世之后，这个由Gradle来支持。

但是不要以为构建系统是编译、运行、打包这一系列流程。

>举个例子：之前开发引用第三方库的时候大都是下载jar包，然后拷贝到libs里。这种方式管理起来很麻烦，很混乱，比如jar包升级了，那么我们必须得重新下载，重新拷贝过来。

Gradle横空出世，把这一系列都包括了。不仅可以编译、运行、打包，还很方便的管理依赖。

现在大家都是常见的一句代码：

```
compile 'com.android.support-v4:21'
```

类似这样的依赖方式，是不是很方便，而且很直观，升级的话直接改下版本号就可以了。还有一点，就是我们现在开发、运行都在AS上。但是假如没有AS、Eclipse这类IDE, 我们怎么办？难道就没法开发Android了么？

有了gradle这类构建系统可以完全抛弃IDE，你甚至可以在 EditPlus、Sublime这样的编辑器上写代码。只不过没有那么多的智能提示，智能跳转而已。写好代码，就完全可以通过简单的 gradle脚本编译、打包，然后运行在你手机上。

所以讲到这里，简单来说，构建系统就是，前提是你有了源代码之后，其他的交给构建系统，你就可以编译、运行、打包，然后还可以方便的管理依赖的一系列工具。只不过Gradle这个构建系统他支持Java、Android、iOS、Ruby等多种语言与平台。

我们是做Android的，所以平时用的只会跟Android相关，所以你们会误以为Gradle只是针对Android的，其实不然。

>我们程序的编译运行其实是gradle在执行

>确切的说ADT不是一个构建系统，但是是包括构建工具的

Gradle是一个构建工具，但是他是基于Groovy语言的

>就好比Android是基于Java语言的一样

>Gradle里的任何语法都是基于Groovy的，Groovy是一种编程语言，跟Java语法差不多，但是比Java多不少语言的新特性

##Gradle与Android Studio的关系

Gradle是可以单独单独存在的，跟任何平台，Android、iOS、Java Web、Ruby等无关。那肯定也跟IDE无关的。

但是AS正是一推出就默认是用的Gradle构建工具来构建的，抛弃了原来的ADT，所以默认就集成了跟Gradle工具。但是是怎么集成的呢？

Google为了让Android支持Gradle构建，单独开发了支持可以在AS上使用的插件，叫 android gradle plugin。所以我们在AS上所使用的都是gradle的android插件。

```
	dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```

>**project下**的build.gradle。并不是module下的

这个就是依赖gradle 插件的代码，后面的版本号代表的是 android gradle plugin的版本,而不是Gradle的版本。

这个版本号是google命名的，但是Gradle官方本身也有个自己的版本。

>https://gradle.org/
>
>这个是gradle官方网站，他上面有所他们开发的gradle最新的到哪个版本了


为什么插件版本跟gradle版本不会一致呢？

因为gradle官方的版本肯定是第一步，比如他们最新出了3.0版本。但是google还来不及针对最新的版本开发，google也得花时间适配啊，测试啊什么的，肯定比gradle官方的版本要晚。

所以android gradle插件的版本号跟gradle官方的版本号没有任何关系，一个是google命令的，一个是gradle官方命令的

####查看gradle版本

那怎么知道我当前的这个 android gradle插件版本对应的gradle版本是什么呢？

- **命令行查看**

很简单，到你的android当前项目目录下。输入 ./gradllew -v

win系统可能直接输入 gradlew -v 就好了。就可以看到你们当前的gradle版本是什么。

>Mac用户提示权限不够 命令行输入  **chmod 755 gradlew**

命令行输出如下：

```
------------------------------------------------------------
Gradle 2.10
------------------------------------------------------------

Build time:   2015-12-21 21:15:04 UTC
Build number: none
Revision:     276bdcded730f53aa8c11b479986aafa58e124a6

Groovy:       2.4.4
Ant:          Apache Ant(TM) version 1.9.3 compiled on December 23 2013
JVM:          1.7.0_79 (Oracle Corporation 24.79-b02)
OS:           Mac OS X 10.11.5 x86_64

```
- **gradle-wrapper.properties文件**

打开项目，**gradle -> wrapper ->gradle-wrapper.properties **

```
#Mon Dec 28 10:00:20 PST 2015
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-2.10-all.zip

```

得到的gradle版本应该与命令行输出的一致的。

####gradlew

gradlew 是 gradle wrapper的缩写。（wrapper是包装的意思，意思是gradle的一层包装）

举个例子：假设我们项目中有2个android项目，一个是比较老的项目，一个是最新的项目。老项目因为一直没人维护，还在用gradle 1.0的版本；新项目用的是gradle 2.0的版本。但是两个项目按理说肯定是都要能构建成功的，所以google开发了一个叫gradle wrapper的东西，你们可以理解为在每个项目里都内置了一个小型的gradle。

>每个项目只有**一个**gradle wrapper，每个module都有一个build.gradle配置文件。
>
>gradle wrapper是google给每个android项目单独定制的
>
>目的就是为了可以让不同的项目同时运行，因为很多时候不同的项目依赖不同的gradle版本很正常，可能很多历史遗留项目一直没更新。

**总结：不同的项目可以使用不同的gradle版本，但是每个项目都有一个小型的gradle，叫做gradle wrapper，即gradle包装。**

>至于怎么知道google开发的android gradle插件到哪里了呢？
>
>google有个官网，明确标记了每个android gradle plugin的版本更新以及具体改进了什么
>
>http://tools.android.com/tech-docs/new-build-system

##如何运行别人家的demo

- **修改编译SDK版本信息等**

我们下载一个demo，先打开每个module下的gradle文件。

即app目录下的build.gradle以及各个library下的build.gradle。

首先查看compileSdkVersion  buildToolsVersion，因为有些时候你本地下载的版本跟这些不一致，那肯定会失败。

- **检查gradlew wrapper**

```
#Mon Dec 28 10:00:20 PST 2015
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-2.10-all.zip

```
修改为能正常运行的项目的版本号即可。

##gradle实用命令

1. gradlew clean   顾名思义，就是clean下project的意思，清除编译缓存之类的
2. gradlew assembleRelease   打release包
3. gradlew assembleDebug  打debug包

gradle安装成功的话，输入这三个命令，然后打包成功会在你的  app/build/outputs/apk 下找到生成的apk包

**PS：**

[MAC下命令行查看gradle版本失败的解决方案](http://stackoverflow.com/questions/27094492/how-to-run-gradle-from-the-command-line-on-mac-bash)

[gradle信息修改](http://www.cnblogs.com/raomengyang/p/5063743.html)





