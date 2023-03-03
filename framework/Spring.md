#### IOC容器

##### 	概述

- 容器：可以管理对象的生命周期、对象与对象之间的依赖关系


- POJO:（Plain Old Java Object）：从字面上翻译为“纯洁老式的Java对象”，但大家都使用“简单java对象”来称呼它。POJO的内在含义是指：那些没有继承任何类、也没有实现任何接口，更没有被其它框架侵入的java对象。不允许有业务方法，也不能携带connection之类的方法，实际就是普通JavaBeans。


- JavaBean：JavaBean是一种JAVA语言写成的可重用组件。JavaBean符合一定规范编写的Java类，不是一种技术，而是一种规范。大家针对这种规范，总结了很多开发技巧、工具函数。符合这种规范的类，可以被其它的程序员或者框架使用。它的方法命名，构造及行为必须符合特定的约定：

  - 1、所有属性为private。
  - 2、这个类必须有一个公共的缺省构造函数。即是提供无参数的构造器。
  - 3、这个类的属性使用getter和setter来访问，其他方法遵从标准命名规范。
  - 4、这个类应是可序列化的。实现serializable接口。

- SpringBean：SpringBean是受Spring管理的对象，所有能受Spring容器管理的对象都可以成SpringBean。Spring中的bean，是通过配置文件、javaconfig等的设置，由Spring自动实例化，用完后自动销毁的对象。

- EntityBean：Entity Bean是域模型对象，用于实现O/R映射，负责将数据库中的表记录映射为内存中的Entity对象，事实上，创建一个Entity Bean对象相当于新建一条记录，删除一个 Entity Bean会同时从数据库中删除对应记录，修改一个Entity Bean时，容器会自动将Entity Bean的状态和数据库同步。

- SpringBean和JavaBean的区别：

  1、用处不同：传统javabean更多地作为值传递参数，而spring中的bean用处几乎无处不在，任何组件都可以被称为bean。

  2、生命周期不同：传统javabean作为值对象传递，不接受任何容器管理其生命周期；spring中的bean有spring管理其生命周期行为。

maven依赖

```xml
dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.2.18.RELEASE</version>
</dependency>
<!-- SpringIoC(依赖注入)的基础实现 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
    <version>5.2.18.RELEASE</version>
</dependency>
<!--Spring提供在基础IoC功能上的扩展服务，此外还提供许多企业级服务的支持，如邮件服务、任务调度、JNDI定位、EJB集成、远程访问、缓存以及各种视图层框架的封装等 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.2.18.RELEASE</version>
</dependency>
```

org.springframework.beans和org.springframework.context包是Spring框架的IoC容器的基础。其中BeanFactory接口提供了容器的基本功能，而ApplicationContext添加了更多特定于企业的功能。ApplicationContext是BeanFactory的一个完整超集。

思考

- 由Spring IoC容器【管理】的构成【应用程序主干的对象】称为【bean】。
- bean是由Spring IoC容器实例化、组装和管理的对象。
- bean及其之间的依赖关系反映在容器使用的【配置元数据】中，元数据可以是配置文件，也可以使用Java注解或代码申明。
- 容器通过读取【配置元数据】获得关于要实例化、配置和组装哪些对象的指令。 配置元数据以XML、Java注解或Java代码表示。元数据表达了【组成应用程序的对象】以及这些对象之间丰富的【相互依赖关系】。

  ##### 配置元数据

【配置元数据】获得关于要实例化、配置和组装哪些对象的指令。 配置元数据以XML、Java注解或Java代码表示。元数据表达了【组成应用程序的对象】以及这些对象之间丰富的【相互依赖关系】。

```xml
 <bean id="..." class="...">  
        <!-- collaborators and configuration for this bean go here -->
  </bean>
' id '属性是标识单个beanDifination的字符串。
' class '属性定义bean的类型，并使用完全限定的类名。
```

##### 	实例化一个容器

ApplicationContext 的构造函数可以是【xml文件的位置路径】的字符串，它允许容器从各种外部资源（如本地文件系统、Java的 ' CLASSPATH '等）加载配置元数据。

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml")
```

##### 	使用容器

【ApplicationContext】是一个高级工厂的接口，它维护了一个bean的注册列表，保存了容器产生的所有bean对象。 通过使用方法T getBean(String name， Class requiredType) ，您可以检索bean的实例。

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml");
// retrieve configured instance，这里使用bean的标识符活class对象检索bean的实例。
PetStoreService service = context.getBean("petStore"， PetStoreService.class);
// use configured instance
List<String> userList = service.getUsernameList();
```

##### 	Bean的概述

在容器本身中，这些定义好的【bean的元数据（描述bean的数据）】被表示为【BeanDefinition】对象，其中包含但不限于以下元数据：

- 全限定类名：通常是被定义的bean的实际【实现类】。
- Bean的行为配置元素：它声明Bean在容器中应该存在哪些行为（作用范围、生命周期回调等等）。
- bean所需的其他bean的引用（成员变量）：这些引用也称为【协作者】或【依赖项】。

bean的命名

- 每个bean都有【一个或多个】标识符。 这些标识符在spring容器（ioc容器）中必须是唯一的。 bean通常只有一个标识符。 但是，如果需要多个，则可以考虑使用别名。
- 在【基于xml】的配置元数据中，可以使用' id '属性、' name '属性或两者同时使用，来指定bean的标识符。 ' id '属性允许您指定一个id，通常，这些名称是字母数字（'myBean'， 'someService'等），但它们也可以包含特殊字符。 如果想为bean引入其他别名（一个或者多个都可以），还可以在' name '属性中指定它们，由逗号('，')、分号(';')或空格分隔。
- 您甚至不需要为bean提供' name '或' id '。 如果您没有显式地提供' name '或' id '，容器将为该bean生成唯一的名称。 但是，如果您想通过名称引用该bean，则必须通过使用' ref '元素来提供名称。 xml中默认的名字是【类的全限定名称#数字】,如（com.ydlclass.dao.UserDao#1）。
- 命名规则：驼峰式userDao

bean的别名

```xml
<alias name="fromName" alias="toName"/>
```

实例化bean

**beanDifination**本质上是描述了一个bean是如何被创建的。 当被请求时，容器会查看指定bean的定义，并使用由该beanDifination封装的配置元数据来创建（或获取）实际对象。

如果使用基于xml配置的元数据，则要在<bean/>元素的【class】属性中指定实例化的对象的类型。 这个' class '属性（在内部是' BeanDefinition '实例上的' class '属性，一个bean的配置加载到内存会形成一个BeanDefinition事例）通常是强制性的。 你可以通过以下两种方式使用Class属性:

1. 在容器中，如果是通过【反射调用其构造函数】直接创建bean，则要指定bean的类型，这有点类似于使用“new”操作符的Java代码。
2. 这个类同样可以是用于创建对象的“静态”工厂方法的实际类，在这种情况下，容器调用该类上的【静态工厂方法来创建bean】。 调用静态工厂方法返回的对象类型可能是同一个类，也可能完全是另一个类，这要看你的工厂方法的具体实现。

使用构造函数实例化：当您通过构造函数方法创建bean时，所有普通类都可以被Spring使用并与Spring兼容。 也就是说，正在开发的类不需要实现任何特定的接口，也不需要以特定的方式编码。 只需指定bean类就足够了。 但是，这种情况下您可能需要一个默认（无参）构造函数。其实就是spring通过class全限定名使用反射进行构造实例。

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>
```

使用静态工厂方法实例化：在使用【静态工厂方法】创建的bean时，使用【class】属性指定包含【一个静态工厂方法】的类，并使用名为【factory-method】的属性指定工厂方法本身的名称。 我们应该能够调用这个方法并返回一个对象实例。

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}
    public static ClientService createInstance() {
        return clientService;
    }
}
<bean id="clientService" class="examples.ClientService"
  factory-method="createInstance"/>
```

使用工厂方法实例化：容器同样可以使用【实例工厂方法】调用【非静态方法】创建一个新的bean。 要使用这种机制，请将【class】属性保留为空，并在【factory-bean】属性中指定当前容器中包含要调用的实例方法的bean的名称。 使用“factory-method”属性设置工厂方法本身的名称。

```xml
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>
<!-- the bean to be created via the factory bean -->
<bean id="clientService" factory-bean="serviceLocator" factory-method="createClientServiceInstance"/>

public class DefaultServiceLocator {
    private static ClientService clientService = new ClientServiceImpl();
    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

##### 	依赖注入

依赖注入（DI）是一个【过程】（目前可以理解为给成员变量赋值的过程），在此过程中，对象仅通过【构造函数参数】、【工厂方法参数】等来确定它们的依赖项。 然后容器在创建bean时注入这些依赖项。 从根本上说，这个过程与bean本身相反(因此得名“控制反转”)。

DI主要有以下两种方式:

- Constructor-based依赖注入，基于构造器的依赖注入，本质上是使用构造器给成员变量赋值。
- Setter-based依赖注入，基于setter方法的依赖注入，本质上是使用set方法给成员变量赋值

基于构造函数的依赖注入

```java
package x.y;

public class ThingOne {

    public ThingOne(ThingTwo thingTwo,ThingThree thingThree) {
        // ...
    }
}

<beans>
    <bean id="beanOne" class="x.y.ThingOne">
        <!-- 直接写就可以 -->
        <constructor-arg ref="beanTwo"/>
        <constructor-arg ref="beanThree"/>
    </bean>

    <bean id="beanTwo" class="x.y.ThingTwo"/>
    <bean id="beanThree" class="x.y.ThingThree"/>
</beans>
```

构造参数类型匹配

```java
package examples;

public class ExampleBean {

    // Number of years to calculate the Ultimate Answer
    private final int years;

    // The Answer to Life， the Universe， and Everything
    private final String ultimateAnswer;

    public ExampleBean(int years,String ultimateAnswer) {
        this.years = years;
        this.ultimateAnswer = ultimateAnswer;
    }
}

<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg type="int" value="7500000"/>
    <constructor-arg type="java.lang.String" value="42"/>
</bean>
```

按照构造函数参数的下标匹配

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg index="0" value="7500000"/>
    <constructor-arg index="1" value="42"/>
</bean>
```

按照构造函数参数的名字匹配

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <constructor-arg name="years" value="7500000"/>
    <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```

基于setter注入

基于setter的DI是通过容器在【调用无参数构造函数】或【无参数“静态”工厂方法】实例化bean后调用bean上的setter方法来实现的。

基于setterDI

```xml
<bean id="exampleBean" class="examples.ExampleBean">
    <!-- setter injection using the nested ref element -->
    <property name="beanOne">
        <ref bean="anotherExampleBean"/>
    </property>

    <!-- setter injection using the neater ref attribute -->
    <property name="beanTwo" ref="yetAnotherBean"/>
    <property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>

<!-- Spring不是使用构造函数，而是被告知调用一个【static】工厂方法来返回对象的一个实例:-->
<bean id="exampleBean" class="examples.ExampleBean" factory-method="createInstance">
    <constructor-arg ref="anotherExampleBean"/>
    <constructor-arg ref="yetAnotherBean"/>
    <constructor-arg value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

构造还是setter？

- 可以混合使用基于构造【函数和setter】的DI，一般情况下，我们对于【强制性依赖项】使用构造函数，对于【可选依赖项】使用setter方法注入，这是一个很好的经验法则。 注意，在setter方法上使用【@Required】注解可以使属性成为必需依赖项。

- Spring团队更提倡使用构造函数注入，因为它允许你将应用程序组件实现为【不可变的对象】，并确保所需的依赖项不是”空“的，这样会更加的安全可靠。 而且，构造函数注入的组件总是以完全初始化的状态返回给客户端(调用)代码。
- Setter注入主要应该只用于【可选依赖项】，这些依赖项可以在类中分配合理的默认值。 setter注入的一个好处是，setter方法使该类的对象能够在稍后进行重新配置或重新注入。

依赖关系和配置细节

```xml
<!--直接值（原语、字符串）-->
<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <!-- results in a setDriverClassName(String) call -->
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
    <property name="username" value="root"/>
    <property name="password" value="misterkaoli"/>
</bean>

<!--idref  将容器中另一个bean的【id 字符串值-不是引用】传递给·<constructor-arg/> 或<property/> 元素的一种防错误方法
使用' idref '标记可以让容器在部署时【验证所引用的已命名bean是否实际存在】  -->
<bean id="theTargetBean" class="..."/>
<bean id="theClientBean" class="...">
    <property name="targetName">
        <idref bean="theTargetBean"/>
    </property>
</bean>


<!--对其它bean的引用 -->
<bean id="accountService" class="com.something.SimpleAccountService">
    <!-- insert dependencies as required here -->
</bean>
<bean id="accountService" <!-- bean name is the same as the parent bean -->
    class="org.springframework.aop.framework.ProxyFactoryBean">
    <property name="target">
        <ref bean="accountService"/> <!-- notice how we refer to the parent bean -->
    </property>
    <!-- insert other configuration and dependencies as required here -->
</bean>


<!-- 内部bean  在<property/> 或<constructor-arg/> 元素内部的'<bean/> 元素定义了一个内部bean-->
<bean id="outer" class="...">
    <!-- instead of using a reference to a target bean， simply define the target bean inline -->
    <property name="target">
        <bean class="com.example.Person"> <!-- this is the inner bean -->
            <property name="name" value="Fiona Apple"/>
            <property name="age" value="25"/>
        </bean>
    </property>
</bean>


<!--集合 -->
<bean id="moreComplexObject" class="example.ComplexObject">
    <!-- results in a setAdminEmails(java.util.Properties) call -->
    <property name="adminEmails">
        <props>
            <prop key="administrator">administrator@example.org</prop>
            <prop key="support">support@example.org</prop>
            <prop key="development">development@example.org</prop>
        </props>
    </property>
    <!-- results in a setSomeList(java.util.List) call -->
    <property name="someList">
        <list>
            <value>a list element followed by a reference</value>
            <ref bean="myDataSource" />
        </list>
    </property>
    <!-- results in a setSomeMap(java.util.Map) call -->
    <property name="someMap">
        <map>
            <entry key="an entry" value="just some string"/>
            <entry key="a ref" value-ref="myDataSource"/>
        </map>
    </property>
    <!-- results in a setSomeSet(java.util.Set) call -->
    <property name="someSet">
        <set>
            <value>just some string</value>
            <ref bean="myDataSource" />
        </set>
    </property>
</bean>

<!-- null和空字符串-->
<bean class="ExampleBean">
    <property name="email" value=""/>
</bean>
<bean class="ExampleBean">
    <property name="email">
        <null/>
    </property>
</bean>


<!-- 带有p命名空间的xml配置-->
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="classic" class="com.example.ExampleBean">
        <property name="email" value="someone@somewhere.com"/>
    </bean>

    <bean name="p-namespace" class="com.example.ExampleBean"
        p:email="someone@somewhere.com"
        p:spouse-ref = "jane"/>
</beans>


<!-- 带有c命名空间的xml快捷方式-->
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:c="http://www.springframework.org/schema/c"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="beanTwo" class="x.y.ThingTwo"/>
    <bean id="beanThree" class="x.y.ThingThree"/>
    <!-- traditional declaration with optional argument names -->
    <bean id="beanOne" class="x.y.ThingOne">
        <constructor-arg name="thingTwo" ref="beanTwo"/>
        <constructor-arg name="thingThree" ref="beanThree"/>
        <constructor-arg name="email" value="something@somewhere.com"/>
    </bean>
    <!-- c-namespace declaration with argument names -->
    <bean id="beanOne" class="x.y.ThingOne" c:thingTwo-ref="beanTwo"
        c:thingThree-ref="beanThree" c:email="something@somewhere.com"/>
</beans>

<!--复合属性名 -->
<bean id="something" class="things.ThingOne">
    <property name="fred.bob.sammy" value="123" />
</bean>

<!--延迟初始化Bean-->
<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
```

自动装配

Spring容器可以自动装配【协作bean之间的关系】。 自动装配具有以下优点:

- 自动装配可以显著减少指定属性或构造函数参数的需要。
- 自动装配可以随着对象的发展更新配置。 例如，如果您需要向类添加依赖项，则无需修改配置即可自动满足该依赖项。

四种模式

| 运行方式          | 解释                                       |
| ------------- | ---------------------------------------- |
| `no`          | (默认)没有自动装配。 Bean引用必须由【ref】元素定义。 对于较大的部署，不建议更改默认设置，因为【明确指定协作者】可以提供更大的控制和清晰度。 在某种程度上，它记录了系统的结构。 |
| `byName`      | **通过属性名自动装配**。 Spring寻找与需要自动连接的属性同名的bean。 例如，如果一个beanDifination被设置为按名称自动装配，并且它包含一个“master”属性（也就是说，它有一个“setMaster(..)”方法），Spring会寻找一个名为“master”的beanDifination并使用它来设置属性。 |
| `byType`      | 如果容器中恰好有一个**属性类型**的bean，则允许自动连接属性。 如果存在多个，则抛出异常，这表明您不能对该bean使用' byType '自动装配。 如果没有匹配的bean，则不会发生任何事情(没有设置属性)。 |
| `constructor` | 类似于' byType '，但适用于构造函数参数。 如果容器中没有一个构造函数参数类型的bean，则会引发致命错误。 |

autowire-candidate属性被设计成只影响【基于类型】的自动装配。 它不会影响【按名称的显式引用】，即使指定的bean没有被标记为自动连接候选对象，也会解析该引用。 因此，如果名称匹配，按名称自动装配仍然会注入一个bean。

循环依赖

![](https://www.ydlclass.com/doc21xnv/assets/image-20211128102816659-b5c945d9.png)

- 使用描述所有bean的配置元数据创建和初始化【ApplicationContext】。 配置元数据可以由XML、Java代码或注解指定。
- 对于每个bean，其依赖关系都以属性、构造函数参数或静态工厂方法参数的形式表示。 这些依赖项是在实际创建bean时提供给bean的。
- 每个属性或构造函数参数的值将从其指定的格式转换为该属性或构造函数参数的实际类型。 默认情况下，Spring可以将字符串格式提供的值转换为所有内置类型，比如' int '、' long '、' string '、' boolean '等等。
- ​ 使用setter注入的循环依赖是可以解决的，通常是采用三级缓存的方式。
- 构造函数注入，可能会创建不可解析的循环依赖场景。


##### Bean的作用域

| scope       | 描述                                       |
| ----------- | ---------------------------------------- |
| singleto    | 每个bean在ioc容器中都是独一无二的单例形式。                |
| prototype   | 将单个beanDifination定义为，spring容器可以【实例化任意数量】的对象实例。 |
| request     | 将单个beanDifination限定为单个HTTP请求的生命周期。 也就是说，每个HTTP请求都有自己的bean实例，它是在单个beanDifination的后面创建的。 仅在web环境中的Spring【ApplicationContext】的上下文中有效。 |
| session     | 将单个beanDifination定义为HTTP 【Session】的生命周期。 仅在web环境中的Spring 【ApplicationContext】的上下文中有效。 |
| application | 将单个beanDifination定义为【ServletContext】的生命周期。 仅在web环境中的Spring 【ApplicationContext】的上下文中有效。 |
| websocket   | 将单个beanDifination作用域定义为【WebSocket】的生命周期。 仅在web环境中的Spring【ApplicationContext】的上下文中有效。 |

单例作用域：定义一个beanDifination并且它的作用域为单例时，Spring IoC容器会创建由该beanDifination定义的对象的一个实例。 这个实例对象会存储单例bean的缓存中，对该命名bean的所有后续请求和引用都会返回缓存的对象

【Spring的单例bean概念不同于设计模式书中定义的单例模式】。 单例设计模式对对象的作用域进行硬编码，使得每个ClassLoader只创建一个特定类的实例。 Spring单例的作用域最好描述为每个容器和每个bean，这并不影响我们手动创建更多个实例。 单例作用域是Spring中的默认作用域。 

```xml
<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>
```

​原型作用域： 非单例原型作用域导致【每次对特定bean发出请求时都要创建一个新的bean实例】。 也就是说，该bean被注入到另一个bean中，或者您通过容器上的`getBean()`方法调用请求它，都会创建一个新的bean。 作为一条规则，您应该对所有**有状态bean**使用原型作用域，对**无状态bean**使用单例作用域。

Spring并【不管理原型bean的完整生命周期】。 容器实例化、配置和组装一个原型对象，并将其传递给客户端，而不需要进一步记录该原型实例，不会缓存，不会管理他的后续生命周期。 因此，尽管初始化生命周期回调方法在所有对象上都被调用但在原型的情况下，配置的销毁生命周期回调不会被调用

##### 更多bean特性

- 生命周期回调
- ApplicationContextAware`和`BeanNameAware
- 其他Aware 接口

生命周期回调

org.springframework.beans.factory.InitializingBean.InitializingBean的接口允许bean在容器设置了bean上的所有必要属性之后执行【初始化工作】。【InitializingBean】接口指定了一个方法:

```java
void afterPropertiesSet() throws Exception;
```

建议不要使用【InitializingBean】接口，因为这将你的代码与Spring的代码耦合在一起。 我们更推荐使用【@PostConstruct】

```java
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
public class ExampleBean {
    public void init() {
        // do some initialization work
    }
}

//等效
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
  public class AnotherExampleBean implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        // do some initialization work
    }
}
```

销毁回调

org.springframework.beans.factory.DisposableBean接口可以让bean在管理它的容器被销毁时获得回调。 ' DisposableBean '接口指定了一个方法:

```java
void destroy() throws Exception;
```

建议使用【DisposableBean】回调接口，因为我们没有必要将自己的代码与Spring耦合在一起。 另外，我们建议使用【@PreDestroy】注解或指定beanDifination支持的销毁方法。 对于基于xml的配置元数据，您可以在``上使用' destroy-method '属性。 在Java配置中，您可以使用“@Bean”的【destroyMethod】属性

```java
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
  public class ExampleBean {
    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}

//等效
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
public class AnotherExampleBean implements DisposableBean {
    @Override
    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

配置统一的bean的初始化和销毁方法。 这意味着，作为应用程序开发人员，您可以仅仅声明一个名为【init()】的初始化方法即可，而不必为每个beanDifination配置一个【init-method=“init】属性。

```xml
<beans default-init-method="init">

    <bean id="blogService" class="com.something.DefaultBlogService">
        <property name="blogDao" ref="blogDao" />
    </bean>

</beans>
```

ApplicationContextAware` 和 `BeanNameAware

```java
public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

