#### maven概述

maven：规范化的，统一的便捷的构建工具

##### 好处

- 统一管理jar包，自动导入jar及其依赖，这样是很初学者唯一能感受出来的好处，确实牛逼啊。
- 项目移植之后甚至不需要安装开发工具，只需要maven加命令就能跑起来，降低学习成本。
- 使我们的项目流水线成为可能，因为使用简单的命令我们就能完成项目的编译，打包，发布等工作，就让程序操作程序成为了可能，大名鼎鼎的jekins技能做到这一点

##### 核心配置文件

conf中的setting.xml配置

```xml
<localRepository>D:/repository</localRepository>   //配置路径
<mirrors>
    <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>
    </mirror>
<mirrors>                                      //阿里云镜像
  <profile>    
    <id>jdk-1.8</id>    
     <activation>    
     	<activeByDefault>true</activeByDefault>    
      	<jdk>1.8</jdk>    
      </activation>    
    <properties>    
        <maven.compiler.source>1.8</maven.compiler.source>    
        <maven.compiler.target>1.8</maven.compiler.target>    
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>    
    </properties>    
</profile>                                   //全局jdk版本
```

##### maven生命周期

内置生命周期：默认（default），清洁（clean）和站点（site）

- 清洁（clean） 为执行以下工作做必要的清理。就是我们经常做的，删除target文件夹。
- 默认（default） 真正进行项目编译打包等工作的阶段
- 站点（site） 生成项目报告，站点，发布站点

默认的生命周期

1. 验证（validate） - 验证项目是否正确，所有必要的信息可用。
2. **编译**（compile） - 编译项目的源代码。
3. 测试（test） - 使用合适的单元测试框架测试编译的源代码。这些测试不应该要求代码被打包或部署。
4. **打包**（package）- 采用编译的代码，并以其可分配格式（如JAR）进行打包。
5. 验证（verify） - 对集成测试的结果执行任何检查，以确保满足质量标准。
6. **安装**（install） - 将软件包安装到本地存储库中，用作本地其他项目的依赖项。
7. 部署（deploy） - 在构建环境中完成，将最终的包复制到远程存储库以与其他开发人员和项目共享（私服）。

##### maven版本规范

1. groudId：团体、组织的标识符。团体标识的约定是，它以创建这个项目的组织名称的逆向域名开头。一般对应着JAVA的包的结构，例如org.apache。
2. artifactId：单独项目的唯一标识符。比如我们的tomcat, commons等。不要在artifactId中包含点号(.)。
3. version：版本
4. packaging：项目的类型，默认是jar

版本特殊字符

- SNAPSHOT：不稳定版本
- LATEST：特定构件的最新发布，这个发布可能是一个发布版
- RELEASE：最后一个发布版本

#### Maven依赖

jar包下载流程

![](https://www.ydlclass.com/doc21xnv/assets/image-20210119141859117.0e02f679.png)

##### 依赖范围

classpath：编译好的class文件所在的路径，类加载器就是去对应的classpath中加载class二进制文件	

web项目打包，src目录下的配置文件会和class文件一样，自动copy到应用的 WEB-INF/classes目录下

web项目的classpath是 WEB-INF/classes

maven工程会将`src/main/java` 和 `src/main/resources` 文件夹下的文件全部打包在classpath中。运行时他们两个的文件夹下的文件会被放在一个文件夹下。

maven 项目不同的阶段引入到classpath中的依赖是不同的，例如，

- `编译时`，maven 会将与编译相关的依赖引入classpath中
- `测试时`，maven会将测试相关的的依赖引入到classpath中
- `运行时`，maven会将与运行相关的依赖引入classpath中

依赖范围用来控制这三种classpath的关系

scope标签是依赖范围的标签：默认配置compile,可选配置还有test、provided、runtime、system、import。

- compile：默认依赖范围，对于编译、测试、运行三种`classpath`都有效
- test：只对测试classpath才有效
- provided：只对编译和测试的classpath有效，对运行的classpath无效（例子 servlet)
- runtime：只对测试和运行的classpath有效，对编译的classpath无效（例子 JDBC驱动）

##### 依赖的传递

- 最短路径优先原则：如果A依赖于B，B依赖于C，在B和C 中同时有log4j的依赖，并且这两个版本不一致，那么A会根据最短路径原则，在A中会传递过来B的log4j版本。
- 路径相同先声明原则：如果我们的工程同时依赖于B和A，B和C没有依赖关系，并且都有D的依赖，且版本不一致，那么会引入在pom.xml中先声明依赖的log4j版本。

##### 依赖的排除

```xml
 <dependency>
        <groupId>com.xinzi</groupId>
        <artifactId>B</artifactId>
        <version>1.5.3</version>
        <exclusions>
            <exclusion>
                <artifactId>com.xinzhi</artifactId>
                <groupId>D</groupId>
            </exlcusion>
        </exclusions>
    </dependency> 
```

##### 聚合和继承

聚合模块（父模块）的打包方式必须为pom，否则无法完成构建

聚合多个项目时，如果这些被聚合的项目中需要引入相同的Jar，那么可以将这些Jar写入父pom中，各个子项目继承该pom即可。，父模块的打包方式必须为pom，否则无法构建项目。

#### POM文件

##### dependencies和dependencyManagement区别

dependencies

- 即使在子项目中不写该依赖项，那么子项目仍然会从父项目中继承该依赖项（全部继承）。
- 继承下来就会被编译，如果子项目根本不用这个依赖会增加子工程的负担。

dependencyManagement：通常会在父工程中定义，目的是统一各个子模块的依赖版本，有不用实际依赖

- 只是声明依赖，并不实现引入
- 子项目**需要显示的声明需要用的依赖**。如果不在子项目中声明依赖，是不会从父项目中继承下来的；
- 只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom;另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本

##### 构建配置

关于资源的处理  src中配置文件导入

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```

添加本地jar包

```xml
<!-- geelynote maven的核心插件之-complier插件默认只支持编译Java 1.4，因此需要加上支持高版本jre的配置，在pom.xml里面加上 增加编译插 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <encoding>UTF-8</encoding>
        <compilerArguments>
            <!-- 本地jar，支付宝jar包放到  src/main/webapp/WEB-INF/lib 文件夹下，
           如果没有配置，本地没问题，但是线上会找不到sdk类
           为什么要引入，因为支付宝jar包再中央仓库没有，再比如oracle连接驱动的jar
        -->
            <extdirs>${project.basedir}/src/main/webapp/WEB-INF/lib</extdirs>
        </compilerArguments>
    </configuration>
</plugin>
```

#### Maven仓库

- 本地仓库
- 远程仓库，远程仓库又分成3种：
  - 中央仓库
  - 私服
  - 其它公共库

maven项目使用的仓库一共有如下几种方式：

1. 中央仓库，这是默认的仓库
2. 镜像仓库，通过 sttings.xml 中的 settings.mirrors.mirror 配置
3. 全局profile仓库，通过 settings.xml 中的 settings.repositories.repository 配置
4. 项目仓库，通过 pom.xml 中的 project.repositories.repository 配置
5. 项目profile仓库，通过 pom.xml 中的 project.profiles.profile.repositories.repository 配置
6. 本地仓库

搜索顺序如下：

local_repo > settings_profile_repo > pom_profile_repo > pom_repositories > settings_mirror > central

#### Maven插件

##### maven-compiler-plugin

```xml
<plugin>                                       
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
        <source>1.8</source> <!-- 源代码使用的JDK版本 -->
        <target>1.8</target> <!-- 需要生成的目标class文件的编译版本 -->
        <encoding>UTF-8</encoding><!-- 字符集编码 -->
    </configuration>
</plugin>
```

##### tomcat插件

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <uriEncoding>UTF-8</uriEncoding>
        <path>/xinzhi</path>
        <finalName>xinzhi</finalName>
    </configuration>
</plugin>
```

##### war打包插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <configuration>
        <warName>test</warName>
        <webResources>
            <resource>
                <directory>src/main/webapp/WEB-INF</directory>
                <filtering>true</filtering>
                <targetPath>WEB-INF</targetPath>
                <includes>
                    <include>web.xml</include>
                </includes>
            </resource>
        </webResources>
    </configuration>
</plugin>
```

jar打包

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.3.0</version>
    <executions>
        <execution>
            <id>make-assembly</id>
            <!-- 绑定到package生命周期 -->
            <phase>package</phase>
            <goals>
                <!-- 只运行一次 -->
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <mainClass>com.xinzi.Test</mainClass> <!-- 你的主类名 -->
            </manifest>
        </archive>

        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
</plugin>
```