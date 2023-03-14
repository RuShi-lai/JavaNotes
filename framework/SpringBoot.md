#### 概述

##### 	SpringBoot概念

SpringBoot提供了一种快速使用Spring的方式，基于**约定优于配置**的思想，可以让开发人员不必在配置与逻辑业务之间进行思维的切换，全身心的投入到逻辑业务的代码编写中，从而大大提高了开发的效率。

##### 	Spring功能

1. Spring Boot的自动配置是一个运行时（更准确地说，是应用程序启动时）的过程，考虑了众多因素，才决定Spring配置应该用哪个，不该用哪个。该过程是SpringBoot自动完成的。

2. 起步依赖本质上是一个Maven项目对象模型（Project Object Model，POM），定义了对其他库的**传递依赖**，这些东西加在一起即支持某项功能。 **依赖太多** **版本冲突**。简单的说，起步依赖就是将具备某种功能的坐标打包到一起，并提供一些默认的功能。

   起步依赖分析

- 在spring-boot-starter-parent中定义了各种技术的版本信息，组合了一套最优搭配的技术版本。
- 在各种starter中，定义了完成该功能需要的坐标合集，其中大部分版本信息来自于父工程。
- 我们的工程继承parent，引入starter后，通过**依赖传递**，就可以简单方便获得需要的jar包，并且不会存在版本冲突等问题

#### 配置文件

##### 分类

SpringBoot是基于约定的，所以很多配置都有默认值，但如果想使用自己的配置替换默认配置的话，就可以使用application.properties或者application.yml（application.yaml）进行配置。

1. 默认配置文件名称：application
2. 在同一级目录下优先级为：properties>**yml** > yaml

##### yaml基本语法

概念：一种直观的能够被电脑识别的的数据数据序列化格式，并且容易被人类阅读，容易和脚本语言交互的，可以被支持YAML库的不同的编程语言程序导入。

特点：

- 大小写敏感
- 数据值前边必须有空格，作为分隔符
- 使用缩进表示层级关系
- 缩进时不允许使用Tab键，只允许使用空格（各个系统 Tab对应的空格数目可能不同，导致层次混乱）。
- 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
- ''#" 表示注释，从这个字符一直到行尾，都会被解析器忽略。

##### SpringBoot配置yml数据格式

```java
//对象：键值对集合
person:  
   name: itlils
# 行内写法
person: {name: itlils}
//数组：一组按次序排列的值
address:
  - beijing
  - shanghai
# 行内写法
address: [beijing,shanghai]
//纯量：单个的、不可再分的值
msg1: 'hello \n world'  # 单引忽略转义字符
msg2: "hello \n world"  # 双引识别转义字符
//参数引用
name: itlils 
person:
  name: ${itlils} # 引用上边定义的name值
```

##### SpringBoot获取数据

```java
@Value
 #获取普通配置
    @Value("${name}")
    private String name;
    #获取对象属性
    @Value("${person.name}")
    private String name2;
   	#获取数组
    @Value("${address[0]}")
    private String address1;
  	#获取纯量
    @Value("${msg1}")
    private String msg1;
Evironment
  @Autowired
 private Environment env;
 System.out.println(env.getProperty("person.name"));
 System.out.println(env.getProperty("address[0]"));
```

用yml配置文件创建一个配置类

1. 类注解@ConfigurationProperties(prefix ="xxx")   -> xxx为yml所需创建对象的对象名
2. 需要加上@Componet，创建对象让容器创建

SpringBoot配置-profile

profile：用来完成不同环境下，配置动态切换功能的

dev：开发环境    test：测试环境   pro：生产环境

```java
---
server:
  port: 8081
spring:
  profiles: dev
---
server:
  port: 8082
spring:
  profiles: pro
---
server:
  port: 8083
spring:
  profiles: test
---
spring:
  profiles:
    active: dev
```

##### 项目内部配置文件加载顺序

加载顺序为下文的排列顺序，高优先级配置的属性会生效

- file:./config/：当前项目下的/config目录下
- file:./ ：当前项目的根目录
- classpath:/config/：classpath的/config目录
- classpath:/ ：classpath的根目录

高级配置文件只覆盖低级配置文件的重复项。低级配置文件的独有项任然有效。

#### 整合框架

##### 	整合Junit

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

##### 	整合Mybatis

```xml
<dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.0</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <!--<scope>runtime</scope>-->
        </dependency>
    </dependencies>
```

- 定义表和实体类
- 编写datasource和mybatis相关配置            mybatis:  mapper-locations: classpath:mapper/*
- 纯注解开发新建接口：  @Select("select * from t_user")    public List<User> findAll();

##### 整合redis

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
    </dependencies>
```



```java
@SpringBootTest
//测试类
class SpringbootRedisApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedis1(){
        redisTemplate.opsForValue().set("a","b");
        redisTemplate.boundValueOps("c").set("d");
    }
    @Test
    public void testRedis2(){
        Object a = redisTemplate.opsForValue().get("a");
        System.out.println(a);
        Object c = redisTemplate.boundValueOps("c").get();
        System.out.println(c);
    }
}
```

#### condition

##### @Conditional 

Condition是Spring4.0后引入的条件化配置接口，通过实现Condition接口可以完成有条件的加载相应的Bean

@Conditional要配和Condition的实现类（ClassCondition）进行使用

```java
@SpringBootApplication
public class SpringbootConditionApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootConditionApplication.class, args);
        //只要引入redis起步依赖，有redisTemplate对象，没有引入，容器中没有这个对象
        Object redisTemplate = context.getBean("redisTemplate");
        System.out.println(redisTemplate);
        //通过名字拿bean对象
        User user1 = (User)context.getBean("user");
        System.out.println(user1);
        //通过类型拿bean对象
        User user2 = (User)context.getBean(User.class);
        System.out.println(user2);
        User user = (User)context.getBean("user2");
        System.out.println(user);
    }
}
```

@Conditional(ClassCondition.class)   ClassCondition(实现Condition )里的条件满足，new这个bean对象

```java
try {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes("com.ydl.springbootcondition.condition.ConditionOnclass");
            System.out.println(annotationAttributes);
            String[] values = (String[]) annotationAttributes.get("value");
            for (String value : values) {
                Class.forName(value);
            }
            return  true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }//从注解中拿值
```

系统已经写好了：@ConditionlOnBean   @ConditionlOnClass 

​                            @ConditionlOnProperty(name ="ydlclass",havingValue = "itlils")

​                           @ConditionalOnMissingBean：判断环境中没有对应Bean才初始化Bean

springboot中的autoconfig工程里把常用的对象的配置类都有了，只要工程中，引入了相关起步依赖，这些对象在我们本项目的容器中就有了。

##### @Enable

SpringBoot不能直接获取在其它工程定义的bean

解决方法：1.使用@ComponentScan扫描com.ydlclass.config包

​		  2.可以使用@Import注解，加载类。这些类都会被Spring创建，并放入IOC容器

​	          3.可以对Import注解进行封装。

```java
//一启动只会扫描本包以及子包
 1 扫描第三方jar包的配置类，麻烦，需知道全限定名称
@ComponentScan("com.ydl.springbootenableother.config")
2 或者import引入,麻烦得知道第三方jar包配置类名称
@Import(UserConfig.class)
3 第三方写好注解直接用
@EnableUser  //开启第三方配置类
```

##### @Import

@Enable底层依赖于@Import注解导入一些类，使用@Import导入的类会被Spring加载到IOC容器中。而@Import提供4中用法：

```java
 1：导入Bean
@Import(User.class) 
 2:导入配置类
@Import(UserConfig.class)
3：导入ImportSelector实现类，一般用于加载配置文件中的类
@Import(MyImportSelector.class)
 4:导入 ImportBeanDefinitionRegistrar 实现类
@Import(MyImportBeanDefinitionRegistrar.class)
  AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(User.class).getBeanDefinition();
  //告诉容器注册User对象，并起名
  registry.registerBeanDefinition("user",beanDefinition);
```

@EnableAutoConfiguration中使用的是第三种方式：@Import(AutoConfigurationImportSelector.class)

- 配置文件位置：META-INF/spring.factories，该配置文件中定义了大量的配置类，当 SpringBoot 应用启动时，会自动加载这些配置类，初始化Bean
- 并不是所有的Bean都会被初始化，在配置类中使用Condition来加载满足条件的Bean

#### 事件监听

Java中的事件监听机制定义了以下几个角色：

①事件：Event，继承 java.util.EventObject 类的对象

②事件源：Source ，任意对象Object

③监听器：Listener，实现 java.util.EventListener 接口 的对象

接听器接口

- ApplicationContextInitializer
- SpringApplicationRunListener
- CommandLineRunner
- ApplicationRunner

自定义监听器的启动时机：MyApplicationRunner和MyCommandLineRunner都是当项目启动后执行，使用@Component放入容器即可使用

MyApplicationContextInitializer的使用要在resource文件夹下添加META-INF/spring.factories

MySpringApplicationRunListener的使用要添加**构造器**

#### SpringBoot启动流程

##### 初始化

1. 配置启动引导类（判断是否有启动主类）

2. 判断是否是Web环境

3. 获取初始化类、监听器类

   ![](https://www.ydlclass.com/doc21xnv/assets/1571369439416-7b4c1bdb.png)

运行

1. 启动计时器

2. 执行监听器

3. 准备环境

4. 打印banner：可以resource下粘贴自定义的banner

5. 创建context

   ![](https://www.ydlclass.com/doc21xnv/assets/1571373793325-e72ac485.png)

   ​

   ​

   ​