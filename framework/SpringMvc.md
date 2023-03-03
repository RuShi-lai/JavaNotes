#### SpringMVC

##### 	概述

【Spring Web MVC】是最初建立在 Servlet API 之上的 Web 框架，从一开始就包含在【Spring Framework】中。正式名称【Spring Web MVC】来自其源模块的名称 ( spring-webmvc)，但它更常被称为【Spring MVC】。

##### 	MVC发展历程

![](https://www.ydlclass.com/doc21xnv/assets/image-20211225215516299-2d75ddfe.png)

- M 代表 模型（Model）： 完成具体的业务，进行数据的查询。
- V 代表 视图（View） ：就是用来做展示的，比如我们学过的JSP技术，用来展示模型中的数据。
- C 代表 控制器（controller)：搜集页面传来的原始数据，或者调用模型获得数据交给视图层处理，Servlet 扮演的就是这样的角色。

##### 	SpringMVC架构

![](https://www.ydlclass.com/doc21xnv/assets/image-20211225220443021-a4bb5036.png)

传统的模型层被拆分为了业务层（Service）和数据访问层（DAO,Data Access Object）。同时，在 Service层下可以通过 Spring 的声明式事务操作数据访问层。

#### 构建SpringMVC

##### 	pom文件

```xml
<dependencies>
        <!--servlet api-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <!--springmvc的依赖，会自动传递spring的其他依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.18.RELEASE</version>
        </dependency>
    </dependencies>
```

#####  	配置web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--配置一个ContextLoaderListener，他会在servlet容器启动时帮我们初始化spring容器-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!--指定启动spring容器的配置文件-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/app-context.xml</param-value>
    </context-param>

    <!--注册DispatcherServlet，这是springmvc的核心-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>WEB-INF/app-context.xml</param-value>
        </init-param>
        <!--加载时先启动-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!--/ 匹配所有的请求；（不包括.jsp）-->
    <!--/* 匹配所有的请求；（包括.jsp）-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

##### 	编写配置文件

- app-context.xml (其实就是个spring和springmvc共享的配置文件) ，我们可以建立在/WEB-INF/目录下
- 在视图解析器中我们把所有的视图都存放在/WEB-INF/目录下，这样可以保证视图安全，因为这个目录下的文件，客户端不能直接访问，必须通过请求转发。


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 处理映射器 -->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
    <!-- 处理器适配器 -->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
        <!--前缀-->
        <property name="prefix" value="/WEB-INF/page/"/>
        <!--后缀-->
        <property name="suffix" value=".jsp"/>
    </bean>
</beans>
```

##### 	编写controller

```java
public class FirstController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // ModelAndView 封装了模型和视图
        ModelAndView mv = new ModelAndView();
        // 模型里封装数据
        mv.addObject("hellomvc","Hello springMVC!");
        // 封装跳转的视图名字
        mv.setViewName("hellomvc");
        // 不是有个视图解析器吗？
        // 这玩意可以自动给你加个前缀后缀，可以将hellomvc拼装成/jsp/hellomvc.jsp
        return mv;
    }
}
```

##### 	注入容器

```xml
 <bean id="/hellomvc" class="cn.itnanls.controller.FirstController"/>
```

##### 	注解实现

配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"      
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"      
       xmlns:context="http://www.springframework.org/schema/context"      
       xmlns:mvc="http://www.springframework.org/schema/mvc"      
       xsi:schemaLocation="http://www.springframework.org/schema/beans       
       http://www.springframework.org/schema/beans/spring-beans.xsd       
       http://www.springframework.org/schema/context       
       https://www.springframework.org/schema/context/spring-context.xsd       
       http://www.springframework.org/schema/mvc       
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!-- 自动扫包 -->
    <context:component-scan base-package="com.ydlclass"/>
    <!-- 让Spring MVC不处理静态资源，负责静态资源也会走我们的前端控制器、试图解析器 -->
    <mvc:default-servlet-handler />
    <!--  让springmvc自带的注解生效  -->
    <mvc:annotation-driven />
    
     <!-- 处理映射器 -->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
    <!-- 处理器适配器 -->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"         id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/page/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>
</beans>
```

编写controller

```java
@Controller
public class AnnotationController {
    @RequestMapping("/hello")
    public ModelAndView testAnnotation(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("hello","hello annotationMvc");
        modelAndView.setViewName("hello");
        return modelAndView;
    }
}
```

#### 初始SpringMVC

##### 	组件说明

- DispatcherServlet(中央控制器，前端控制器)：用户请求到达前端控制器（dispatcherServlet），他是整个流程控制的中心，由它负责调用其它组件处理用户的请求，dispatcherServlet的存在降低了组件之间的耦合性。
- handler(处理器)：后端控制器，在DispatcherServlet的控制下Handler对【具体的用户请求】进行处理，由于Handler涉及到【具体的用户业务请求】，所以一般情况需要程序员【根据业务需求开发Handler】。这玩意就是你写的controller。
- View（视图）：通过【页面标签或页面模版技术】将模型数据通过页面展示给用户，需要由程序员根据业务需求开发具体的页面。目前我们接触过得视图技术就是jsp，当然还有Freemarker，Thymeleaf等。
- HandlerMapping（处理器映射器）：HandlerMapping负责根据【用户请求url】找到【Handler】即处理器，springmvc提供了不同的【处理器映射器】实现，如配置文件方式，实现接口方式，注解方式等。
- HandlAdapter（处理器适配器）：HandlerAdapter负责调用具体的处理器，这是适配器模式的应用，通过扩展适配器可以对更多类型的处理器进行执行。我们写的controller中的方法，将来就是会由处理器适配器调用。
- ViewResolver（视图解析器）：View Resolver负责将处理结果生成View视图，View Resolver首先根据【逻辑视图名】解析成【物理视图名】即具体的页面地址，再生成View视图对象，最后对View进行渲染将处理结果通过页面展示给用户。

执行流程

![](https://www.ydlclass.com/doc21xnv/assets/image-20211226000022362-184919d4.png)

拦截器

```java
public interface HandlerInterceptor {
    //处理器执行之前
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }
	//处理器执行之后
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
	//完成之后
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
```

##### 	三个上下文

ServletContext：对于一个web应用，其部署在web容器中，web容器提供其一个全局的上下文环境，这个上下文就是我们的ServletContext，其为后面的spring IoC容器提供一个宿主环境。

Spring：

- 在web.xml的配置中，我们需要提供一个监听器【ContextLoaderListener】。在web容器启动时，会触发【容器初始化】事件，此时contextLoaderListener会监听到这个事件，其contextInitialized方法会被调用。

- 在这个方法中，spring会初始化一个【上下文】，这个上下文被称为【根上下文】，即【WebApplicationContext】，这是一个接口类，其实际的实现类是XmlWebApplicationContext。这个就是spring的IoC容器，其对应的Bean定义的配置由web.xml中的【context-param】配置指定，默认配置文件为【/WEB-INF/applicationContext.xml】。

- 在这个IoC容器初始化完毕后，spring以【WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE】为属性Key，将其存储到ServletContext中，便于将来获取；

  ```xml
  <listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  <context-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>/WEB-INF/app-context.xml</param-value>
  </context-param>
  ```

SpringMVC上下文

- contextLoaderListener监听器初始化完毕后，开始初始化web.xml中配置的Servlet，这个servlet可以配置多个，通常只配置一个，以最常见的DispatcherServlet为例，这个servlet实际上是一个【标准的前端控制器】，用以转发、匹配、处理每个servlet请求。
- DispatcherServlet在初始化的时候会建立自己的IoC上下文，用以持有【spring mvc相关的bean】。在建立DispatcherServlet自己的IoC上下文时，会利用【WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE】先从ServletContext中获取之前的【根上下文】作为自己上下文的【parent上下文】。有了这个parent上下文之后，再初始化自己持有的上下文，这个上下文本质上也是XmlWebApplicationContext，默认读取的配置文件是【/WEB-INF/springmvc-servlet.xml】，当然我们也可以使用init-param标签的【contextConfigLocation属性】进行配置。
- DispatcherServlet初始化自己上下文的工作在其【initStrategies】方法中可以看到，大概的工作就是初始化处理器映射、视图解析等。这个servlet自己持有的上下文默认实现类也是xmlWebApplicationContext。初始化完毕后，spring以【"org.springframework.web.servlet.FrameworkServlet.CONTEXT"+Servlet名称】为Key，也将其存到ServletContext中，以便后续使用。这样每个servlet就持有自己的上下文，即拥有自己独立的bean空间，同时各个servlet还可以共享相同的bean，即根上下文(第2步中初始化的上下文)定义的那些bean。

注：springMVC容器只负责创建Controller对象，不会创建service和dao，并且他是一个子容器。而spring的容器只负责Service和dao对象，是一个父容器。子容器可以看见父容器的对象，而父容器看不见子容器的对象，这样各司其职。

#### 核心技术

##### 	视图和模型拆分

- Model会在调用handler时通过参数的形式传入

- View可以简化为字符串形式返回

  ```java
  @RequestMapping("/test1")
  public String testAnnotation(Model model){
      model.addAttribute("hello","hello annotationMvc as string");
      return "annotation";
  }
  ```

##### 	重定向和转发

- 返回视图字符串加前缀redirect就可以进行重定向：return "redirect:https://www.baidu.com"
- 返回视图字符串加前缀forward就可以进行请求转发，而不走视图解析器：return"forward:/a/b"

##### 	RequestMapping和衍生类注解

@RequestMapping

- 这个注解很关键，他不仅仅是一个方法级的注解，还是一个类级注解。

- 如果放在类上，相当于给每个方法默认都加上一个前缀url。

- 六个属性

  - value： 指定请求的实际地址，指定的地址可以是URI Template 模式（后面将会说明）
  - method： 指定请求的method类型， GET、POST、PUT、DELETE等；
  - consumes：指定处理中的请求的内容类型（Content-Type），例如application/json；
  - produces：指定返回响应的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回
  - params： 指定request中必须包含某些参数值处理器才会继续执行。
  - headers： 指定request中必须包含某些指定的header值处理器才会继续执行。

- ```java
  @RequestMapping(value = "add",method = RequestMethod.POST,
                  consumes = "application/json",produces = "text/plain",
                  headers = "name",params = {"age","times"}
                 )
  @ResponseBody
  public String add(Model model){
      model.addAttribute("user","add user");
      return "user";
  }
  ```


- 衍生注解：@GetMapping("getOne")->@PostMapping("insert")->@PutMapping("update")->@DeleteMapping("delete")

##### 	URI模式匹配

@RequestMapping可以支持【URL模式匹配】，为此，spring提供了两种选择（两个类）：

- PathPattern` —`PathPattern是 Web 应用程序的推荐解决方案，也是 Spring WebFlux 中的唯一选择，比较新。
- AntPathMatcher — 使用【字符串模式与字符串路径】匹配。这是Spring提供的原始解决方案，用于选择类路径、文件系统和其他位置上的资源。

示例

- `"/resources/ima?e.png"` - 匹配路径段中的一个字符
- `"/resources/*.png"` - 匹配路径段中的零个或多个字符
- `"/resources/**"` - 匹配多个路径段
- `"/projects/{project}/versions"` - 匹配路径段并将其【捕获为变量】
- `"/projects/{project:[a-z]+}/versions"` - 使用正则表达式匹配并【捕获变量】

捕获的 URI 变量可以使用`@PathVariable`注解

```java
@GetMapping("/owners/{ownerId}/pets/{petId}")
public Pet findPet(@PathVariable Long ownerId, @PathVariable Long petId) {
    // ...
}
```

路由匹配优先级

| 匹配方式                       | 优先级   |
| -------------------------- | ----- |
| 全路径匹配，例如：配置路由/a/b/c        | 第一优先级 |
| 带有{}路径的匹配，例如：/a/{b}/c      | 第二优先级 |
| 正则匹配，例如：/a/{regex:\d{3}}/c | 第三优先级 |
| 带有`*`路径的匹配，例如：/a/b/*       | 第四优先级 |
| 带有`**`路径的匹配，例如：/a/b/**     | 第五优先级 |
| 仅仅是双通配符：/**                | 最低优先级 |

- 完全匹配者，优先级最高
- 都是前缀匹配（/a/**）, 匹配路由越长，优先级越高
- 前缀匹配优先级，比非前缀的低
- 需要匹配的数量越少，优先级越高，this.uriVars + this.singleWildcards + (2 * this.doubleWildcards);
- 路劲越短优先级越高
- *越少优先级越高
- {}越少优先级越高

##### 	牛逼的传参

- 使用springmvc不需要用getparameter()一个一个获取参数，方法的形参就是传入的参数
- @RequestParam注解将【请求参数】（即查询参数或表单数据）绑定到控制器中的方法参数。required`标志设置】为 `false来指定方法参数是可选的
- 使用@RequestHeader注解将请求的首部信息绑定到控制器中的方法参数中


```java
Host localhost:8080 
Accept text/html,application/xhtml+xml,application/xml;q=0.9 
Accept-Language fr,en-gb;q=0.7,en;q=0.3 
Accept-Encoding gzip,deflate 
Accept-Charset ISO -8859-1,utf-8;q=0.7,*;q=0.7 
Keep-Alive 300
@GetMapping("/demo")
public void handle(
        @RequestHeader("Accept-Encoding") String encoding, 
        @RequestHeader("Keep-Alive") long keepAlive) { 
    //...
}
```

- 使用@CookieValue注解将请求中的 cookie 的值绑定到控制器中的方法参数。

- 如果您需要访问全局管理的预先存在的会话属性，并且可能存在或可能不存在，您可以`@SessionAttribute`在方法参数上使用注解

- 使用`@RequestAttribute`注解来访问先前创建的存在与请求中的属性（例如，由 Servlet`Filter` 或`HandlerInterceptor`）创建或在请求转发中添加的数据 

- @ModelAttribute

  您可以使用`@ModelAttribute`注解在方法参数上来访问【模型中的属性】，或者在不存在的情况下对其进行实例化。模型的属性会覆盖来自 HTTP Servlet 请求参数的值，其名称与字段名称匹配，这称为数据绑定

  @ModelAttribute 和 @RequestMapping 注解同时应用在方法上时，有以下作用：

  1. 方法的【返回值】会存入到 Model 对象中，key为 ModelAttribute 的 value 属性值。

  2. 方法的返回值不再是方法的访问路径，访问路径会变为 @RequestMapping 的 value 值，例如：@RequestMapping(value = "/index") 跳转的页面是 index.jsp 页面。

  3. ```java
     @Controller
     public class ModelAttributeController {
         // @ModelAttribute和@RequestMapping同时放在方法上
         @RequestMapping(value = "/index")
         @ModelAttribute("name")
         public String model(@RequestParam(required = false) String name) {
             return name;
         }
     }
     ```

- @SessionAttributes注解应用到Controller上面，可以将Model中的属性同步到session当中：

  ```java
  @Controller
  @RequestMapping("/Demo.do")
  @SessionAttributes(value={"attr1","attr2"})
  public class Demo {
      
      @RequestMapping(params="method=index")
      public ModelAndView index() {
          ModelAndView mav = new ModelAndView("index.jsp");
          mav.addObject("attr1", "attr1Value");
          mav.addObject("attr2", "attr2Value");
          return mav;
      }
      
      @RequestMapping(params="method=index2")
      public ModelAndView index2(@ModelAttribute("attr1")String attr1, @ModelAttribute("attr2")String attr2) {
          ModelAndView mav = new ModelAndView("success.jsp");
          return mav;
      }
  }
  ```


数组传递

```java
@GetMapping("/array")
public String testArray(@RequestParam("array") String[] array) throws Exception {
    System.out.println(Arrays.toString(array));
    return "array";
}

//请求
http://localhost:8080/app/hellomvc?array=1,2,3,4
http://localhost:8080/app/hellomvc?array=1&array=3
```

复杂参数传递

- VO（View Object）：视图对象，用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来。
- DTO（Data Transfer Object）：数据传输对象，这个概念来源于J2EE的设计模式，原来的目的是为了EJB的分布式应用提供粗粒度的数据实体，以减少分布式调用的次数，从而提高分布式调用的性能和降低网络负载，但在这里，我泛指用于展示层与服务层之间的数据传输对象。
- DO（Domain Object）：领域对象，就是从现实世界中抽象出来的有形或无形的业务实体。
- PO（Persistent Object）：持久化对象，它跟持久层（通常是关系型数据库）的数据结构形成一一对应的映射关系，如果持久层是关系型数据库，那么，数据表中的每个字段（或若干个）就对应PO的一个（或若干个）属性。

![](https://www.ydlclass.com/doc21xnv/assets/image-20211229113542929-65d730e2.png)

流程：

- 用户发出请求（可能是填写表单），表单的数据在展示层被匹配为VO；
- 展示层把VO转换为服务层对应方法所要求的DTO，传送给服务层；
- 服务层首先根据DTO的数据构造（或重建）一个DO，调用DO的业务方法完成具体业务；
- 服务层把DO转换为持久层对应的PO（可以使用ORM工具，也可以不用），调用持久层的持久化方法，把PO传递给它，完成持久化操作；
- 数据传输顺序：VO ===> DTO ===> DO ===> PO

##### 	设定字符集

web.xml配置

```xml
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

##### 	返回json数据（序列化）

步骤

- 将我们的对象转化为json字符串。
- 将返回的内容直接写入响应体，不走视图解析器。
- 然后将Content-Type设置为`application/json`即可。

需要引入fastjson

```java
// produces指定了响应的Content-Type
@RequestMapping(value = "getUsers",produces = {"application/json;charset=utf-8"})
@ResponseBody  // 将返回的结果直接写入响应体，不走视图解析器
public String getUsers(){
    List<User> users =  new ArrayList<User>(){{
        add(new User("Tom","2222"));
        add(new User("jerry","333"));
    }};
    return JSONArray.toJSONString(users);
}
```

或者向容器注入一个专门处理消息转换的bean：当不走视图解析器时，如果发现【返回值是一个对象】，就会自动将返回值转化为json字符序列：

```xml
<mvc:annotation-driven >
        <mvc:message-converters>
            <bean id="fastjson" class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
                <property name="supportedMediaTypes">
                    <list>
                        <!-- 这里顺序不能反，一定先写text/html,不然ie下会出现下载提示 -->
    				<value>text/html;charset=UTF-8</value>
					<value>application/json;charset=UTF-8</value>
                    </list>
                </property>
            </bean>
        </mvc:message-converters>
</mvc:annotation-driven>
```

用jackson处理

```xml
dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

```java
public class CustomObjectMapper extends ObjectMapper {

    public CustomObjectMapper() {
        super();
        //去掉默认的时间戳格式
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //设置为东八区
        setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置日期转换yyyy-MM-dd HH:mm:ss
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置输入:禁止把POJO中值为null的字段映射到json字符串中
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // 空值不序列化
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时，属性不存在的兼容处理
        getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化枚举是以toString()来输出，默认false，即默认以name()来输出
        configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    }
} //对序列化进行额外配置
```

配置文件

```xml
<mvc:annotation-driven>

    <mvc:message-converters>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
            <!-- 自定义Jackson的objectMapper -->
            <property name="objectMapper" ref="customObjectMapper" />
            <property name="supportedMediaTypes">
                <list>
                    <value>text/plain;charset=UTF-8</value>
                    <value>application/json;charset=UTF-8</value>
                </list>
            </property>
        </bean>
    </mvc:message-converters>

</mvc:annotation-driven>
<!--注入我们写的对jackson的配置的bean-->
<bean name="customObjectMapper" class="com.ydlclass.CustomObjectMapper"/>
```

##### 	获取请求json数据

在前端发送的数据中可能会如如下情况，Contetn-Type是application/json，请求体中是json格式数据

@RequestBody注解可以【直接获取请求体的数据】。

```java
@PostMapping("insertUser")
public String insertUser(@RequestBody User user) {
    System.out.println(user);
    return "user";
}
```

##### 	数据转化

前端传递过来一个日期字符串，但是后端需要使用Date类型进行接收，这时就需要一个类型转化器进行转化。

```java
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd hh,mm,ss");
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

```xml
<!-- 开启mvc的注解 -->
<mvc:annotation-driven conversion-service="conversionService" />

<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean id="stringToDateConverter" class="cn.itnanls.convertors.StringToDateConverter"/>
        </set>
    </property>
</bean>
```

- 或者使用注解@DateTimeFormat，同时配合jackson提供的@JsonFormat注解几乎可以满足我们的所有需求。

- @DateTimeFormat：当从requestParam中获取string参数并需要转化为Date类型时，会根据此注解的参数pattern的格式进行转化。

- @JsonFormat：当从请求体中获取json字符序列，需要反序列化为对象时，时间类型会按照这个注解的属性内容进行处理。

  ```java
  // 对象和json互相转化的过程当中按照此转化方式转哈
  @JsonFormat(
              pattern = "yyyy年MM月dd日",
              timezone = "GMT-8"
      )
  // 从requestParam中获取参数并且转化
  @DateTimeFormat(pattern = "yyyy年MM月dd日")
  private Date birthday;
  ```

##### 	数据校验

JSR303

| **Constraint**                | **详细信息**                     |
| ----------------------------- | ---------------------------- |
| `@Null`                       | 被注解的元素必须为 `null`             |
| `@NotNull`                    | 被注解的元素必须不为 `null`            |
| `@AssertTrue`                 | 被注解的元素必须为 `true`             |
| `@AssertFalse`                | 被注解的元素必须为 `false`            |
| `@Min(value)`                 | 被注解的元素必须是一个数字，其值必须大于等于指定的最小值 |
| `@Max(value)`                 | 被注解的元素必须是一个数字，其值必须小于等于指定的最大值 |
| `@DecimalMin(value)`          | 被注解的元素必须是一个数字，其值必须大于等于指定的最小值 |
| `@DecimalMax(value)`          | 被注解的元素必须是一个数字，其值必须小于等于指定的最大值 |
| `@Size(max, min)`             | 被注解的元素的大小必须在指定的范围内           |
| `@Digits (integer, fraction)` | 被注解的元素必须是一个数字，其值必须在可接受的范围内   |
| `@Past`                       | 被注解的元素必须是一个过去的日期             |
| `@Future`                     | 被注解的元素必须是一个将来的日期             |
| `@Pattern(value)`             | 被注解的元素必须符合指定的正则表达式           |

Hibernate Validator 扩展注解

| **Constraint** | **详细信息**            |
| -------------- | ------------------- |
| `@Email`       | 被注解的元素必须是电子邮箱地址     |
| `@Length`      | 被注解的字符串的大小必须在指定的范围内 |
| `@NotEmpty`    | 被注解的字符串的必须非空        |
| `@Range`       | 被注解的元素必须在合适的范围内     |

Spring MVC

Spring MVC 可以对表单参数进行校验，并将结果保存到对应的【BindingResult】或 【Errors 】对象中。

```xml
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.9.Final</version>
</dependency>
```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "用户名不能为空")
    private String password;
    @Min(value = 0, message = "年龄不能小于{value}")
    @Max(value = 120,message = "年龄不能大于{value}")
    private int age;
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT-8"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "生日不能大于今天")
    private Date birthday;
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
    private String phone;
    @Email
    private String email;
}
```

```xml
<bean id="localValidator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
    <property name="providerClass" value="org.hibernate.validator.HibernateValidator"/>
</bean>
<!--注册注解驱动-->
<mvc:annotation-driven validator="localValidator"/>
```

controller使用@Validated标识验证的对象，紧跟着的BindingResult获取错误信息

```java
@PostMapping("insert")
public String insert(@Validated UserVO user, BindingResult br) {
    List<ObjectError> allErrors = br.getAllErrors();
    Iterator<ObjectError> iterator = allErrors.iterator();
    // 打印以下错误结果
    while (iterator.hasNext()){
        ObjectError error = iterator.next();
        log.error("user数据校验错误:{}",error.getDefaultMessage());
    }
    if(allErrors.size() > 0){
        return "error";
    }
    System.out.println(user);
    return "user";
}
```

##### 	视图解析器详解

默认视图解析器

```xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
    <!-- 前缀 -->
    <property name="prefix" value="/WEB-INF/page/" />
    <!-- 后缀 -->
    <property name="suffix" value=".jsp" />
    <!--<property name="order" value="10"/>-->
</bean>
```

- 视图解析器：处理jsp页面的映射渲染

- 想添加新的视图解析器，则需要给旧的新增一个order属性，或者直接删除原有的视图解析器

- order表示视图解析的【优先级】，数字越小优先级越大（即：0为优先级最高，所以优先进行处理视图），InternalResourceViewResolver在项目中的优先级一般要设置为最低，也就是order要最大。不然它会影响其他视图解析器。


- 当处理器返回逻辑视图时（也就是return “string”），要经过**视图解析器链**，如果前面的解析器能处理，就不会继续往下传播。如果不能处理就要沿着解析器链继续寻找，直到找到合适的视图解析器。

  ![](https://www.ydlclass.com/doc21xnv/assets/image-20211230114025288-521ba23f.png)

  ​

  配置一个新的Tymeleaf视图解析器，order设置的低一些，这样两个视图解析器都可以生效：

  ```xml
  <!--thymeleaf的视图解析器-->
  <bean id="templateResolver"
        class="org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver">
      <property name="prefix" value="/WEB-INF/templates/" />
      <property name="suffix" value=".html" />
      <property name="templateMode" value="HTML" />
      <property name="cacheable" value="true" />
  </bean>
  <!--thymeleaf的模板引擎配置-->
  <bean id="templateEngine"
        class="org.thymeleaf.spring4.SpringTemplateEngine">
      <property name="templateResolver" ref="templateResolver" />
      <property name="enableSpringELCompiler" value="true" />
  </bean>
  <bean id="viewResolver" class="org.thymeleaf.spring4.view.ThymeleafViewResolver">
      <property name="order" value="1"/>
      <property name="characterEncoding" value="UTF-8"/>
      <property name="templateEngine" ref="templateEngine"/>
  </bean>
  ```

```xml
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.0.14.RELEASE</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf-spring4 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring4</artifactId>
    <version>3.0.14.RELEASE</version>
</dependency>
```

> 模板中需要添加对应的命名空间

```xml
<html xmlns:th="http://www.thymeleaf.org" >
```

##### 	全局捕获异常

在Java中，对于异常的处理一般有两种方式：

- 一种是当前方法捕获处理（try-catch），这种处理方式会造成业务代码和异常处理代码的耦合。

- 另一种是自己不处理，而是抛给调用者处理（throws），调用者再抛给它的调用者，也就是一直向上抛，指导传递给浏览器。

  ![](https://www.ydlclass.com/doc21xnv/assets/image-20211229185603473-112d8134.png)

自己可以写个HandlerExceptionResolver

```java
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", ex.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}

<bean id="globalExecptionResovler" class="com.lagou.exception.GlobalExecptionResovler"></bean>
```

@ControllerAdvice

该注解同样能实现异常的全局统一处理，而且实现起来更加简单优雅，当然使用这个注解有一下三个功能：

- 处理全局异常

- 预设全局数据

- 请求参数预处理

  ```java
  @Slf4j
  @ControllerAdvice
  public class GlobalExceptionResolverController  {

      @ExceptionHandler(ArithmeticException.class)
      public String processArithmeticException(ArithmeticException ex){
          log.error("发生了数学类的异常：",ex);
          return "error";
      }

      @ExceptionHandler(BusinessException.class)
      public String processBusinessException(BusinessException ex){
          log.error("发生了业务相关的异常：",ex);
          return "error";
      }

      @ExceptionHandler(Exception.class)
      public String processException(Exception ex){
          log.error("发生了其他的异常：",ex);
          return "error";
      }
  }
  ```

##### 	处理资源

当我们使用了springmvc后，所有的请求都会交给springmvc进行管理，当然也包括静态资源，比如`/static/js/index.js`，这样的请求如果走了中央处理器，必然会抛出异常，因为没有与之对应的controller，这样我们可以使用一下配置进行处理：

```xml
<mvc:resources mapping="/js/**" location="/static/js/"/>
<mvc:resources mapping="/css/**" location="/static/css/"/>
<mvc:resources mapping="/img/**" location="/static/img/"/>
```

经过这样的配置后，我们直接配置了请求url和路径的映射关系，就不会再走我们的前端控制器了。

##### 	拦截器

1. SpringMVC提供的拦截器类似于JavaWeb中的过滤器，只不过**SpringMVC拦截器只拦截被前端控制器拦截的请求**，而过滤器拦截从前端发送的【任意】请求。
2. 熟练掌握`SpringMVC`拦截器对于我们开发非常有帮助，在没使用权限框架(`shiro，spring security`)之前，一般使用拦截器进行认证和授权操作。
3. SpringMVC拦截器有许多应用场景，比如：登录认证拦截器，字符过滤拦截器，日志操作拦截器等等。

自定义拦截器

1. 自定义的`Interceptor`类要实现了Spring的HandlerInterceptor接口。

2. 继承实现了`HandlerInterceptor`接口的类，比如Spring已经提供的实现了HandlerInterceptor接口的抽象类HandlerInterceptorAdapter。

   @Component
   public class MyInterceptor implements HandlerInterceptor {

   ```java
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       System.out.println("Interceptor 前置");
       return true;
   }
   @Override
   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       System.out.println("Interceptor 处理中");
   }

   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       System.out.println("Interceptor 后置");
   }
   ```
   拦截流程

![](https://www.ydlclass.com/doc21xnv/assets/image-20220106175037525-c07ae95f.png)

拦截器规则

我们可以配置多个拦截器，每个拦截器中都有三个方法。下面将总结多个拦截器中的方法执行规律。

1. preHandle：Controller方法处理请求前执行，根据拦截器定义的顺序，正向执行。
2. postHandle：Controller方法处理请求后执行，根据拦截器定义的顺序，逆向执行。需要所有的preHandle方法都返回true时才会调用。
3. afterCompletion：View视图渲染后处理方法：根据拦截器定义的顺序，逆向执行。preHandle返回true也会调用

登录拦截器

编写一个登录拦截器，这个拦截器可以实现认证操作。就是当我们还没有登录的时候，如果发送请求访问我们系统资源时，拦截器不放行，请求失败。只有登录成功后，拦截器放行，请求成功。登录拦截器只要在preHandle()方法中编写认证逻辑即可，因为是在请求执行前拦截。代码实现如下：

```java
public class LoginInterceptor implements HandlerInterceptor {
    
    /**
        在执行Controller方法前拦截，判断用户是否已经登录，
        登录了就放行，还没登录就重定向到登录页面
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        User user = session.getAttribute("user");
        if (user == null){
            //还没登录，重定向到登录页面
            response.sendRedirect("/toLogin");
        }else {
            //已经登录，放行
            return true;
        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {}
}
```

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <!--
            mvc:mapping：拦截的路径
            /**：是指所有文件夹及其子孙文件夹
            /*：是指所有文件夹，但不包含子孙文件夹
            /：Web项目的根目录
        -->
        <mvc:mapping path="/**"/>
        <!--
                mvc:exclude-mapping：不拦截的路径,不拦截登录路径
                /toLogin：跳转到登录页面
                /login：登录操作
            -->
        <mvc:exclude-mapping path="/toLogin"/>
        <mvc:exclude-mapping path="/login"/>
        <!--class属性就是我们自定义的拦截器-->
        <bean id="loginInterceptor" class="com.ydlclass.interceptor.LoginInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

##### 	全局配置类

springmvc有一个可用作用于做全局配置的接口，这个接口是`WebMvcConfigurer`，在这个接口中有很多默认方法，每一个默认方法都可以进行一项全局配置，这些配置可以和我们配置文件的配置一一对应：这些配置在全局的xml中也可以进行配置：

xml配置

```xml
<!--处理静态资源-->
<mvc:resources mapping="/js/**" location="/static/js/"/>
<mvc:resources mapping="/css/**" location="/static/css/"/>
<mvc:resources mapping="/./image/**" location="/static/./image/"/>

<!--配置页面跳转-->
<mvc:view-controller path="/toGoods" view-name="goods"/>
<mvc:view-controller path="/toUpload" view-name="upload"/>
<mvc:view-controller path="/websocket" view-name="websocket"/>

<mvc:cors>
    <mvc:mapping path="/goods/**" allowed-methods="*"/>
</mvc:cors>
```

常用WebMvcConfigurer的配置

```java
@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
    
    // 拦截器进行配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(List.of("/toLogin","/login"))
                .order(1);
    }

    // 资源的配置
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/static/css/");
    }

    // 跨域的全局配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","PUT","DELETE")
                .maxAge(3600);
    }

    // 页面跳转的配置
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
    }
    
}
```

#### 跨域

##### 	同源策略

同源策略（Sameoriginpolicy）是一种约定，它是浏览器最核心也最基本的安全功能。同源策略会阻止一个域的javascript脚本和另外一个域的内容进行交互。所谓同源（即指在同一个域）就是两个页面具有相同的协议（protocol），主机（host）和端口号（port）。

##### 	什么是跨域

当一个请求url的协议、域名、端口三者之间任意一个与当前页面url不同时，就会产生跨域。

非同源限制

- 无法读取非同源网页的 Cookie、LocalStorage 和 IndexedDB。
- 无法接触非同源网页的 DOM
- 无法向非同源地址发送 AJAX 请求

##### 	两种请求

- 全称是"跨域资源共享"(Cross-origin resource sharing)；
- 浏览器将CORS请求分成两类：简单请求（simple request）和非简单请求（not-so-simple request）。
- 只要同时满足以下两大条件，就属于简单请求：

（1) 请求方法是以下三种方法之一：

- HEAD
- GET
- POST

（2）HTTP的头信息不超出以下几种字段：

- Accept
- Accept-Language
- Content-Language
- Last-Event-ID
- Content-Type：只限于三个值`application/x-www-form-urlencoded`、`multipart/form-data`、`text/plain`


- 这是为了兼容表单（form），因为历史上表单一直可以发出跨域请求。AJAX 的跨域设计就是，只要表单可以发，AJAX 就可以直接发。
- 凡是不同时满足上面两个条件，就属于非简单请求。
- 浏览器对这两种请求的处理，是不一样的。

简单请求

浏览器直接发出CORS请求。具体来说，就是在头信息之中，增加一个`Origin`字段。用来说明，本次请求来自哪个源（协议 + 域名 + 端口）Origin: http://api.bob.com

如果`Origin`指定的源，不在许可范围内，服务器会返回一个正常的HTTP回应。浏览器发现，这个回应的头信息没有包含`Access-Control-Allow-Origin`字段（详见下文），就知道出错了，从而抛出一个错误，被`XMLHttpRequest`的`onerror`回调函数捕获。注意，这种错误无法通过状态码识别，因为HTTP回应的状态码有可能是200。

如果`Origin`指定的域名在许可范围内，服务器返回的响应，会多出几个头信息字段。

```json
Access-Control-Allow-Origin: http://api.bob.com
Access-Control-Allow-Credentials: true
Access-Control-Expose-Headers: FooBar
Content-Type: text/html; charset=utf-8
```

Access-Control-Expose-Headers

- 该字段可选。CORS请求时，`XMLHttpRequest`对象的`getResponseHeader()`方法只能拿到6个基本字段：`Cache-Control`、`Content-Language`、`Content-Type`、`Expires`、`Last-Modified`、`Pragma`。如果想拿到其他字段，就必须在`Access-Control-Expose-Headers`里面指定。上面的例子指定，`getResponseHeader('FooBar')`可以返回`FooBar`字段的值。
- CORS请求默认不发送Cookie和HTTP认证信息。如果要把Cookie发到服务器，一方面要服务器同意，指定`Access-Control-Allow-Credentials` true字段。另一方面，开发者必须在AJAX请求中打开`withCredentials`属性。 var xhr = new XMLHttpRequest();xhr.withCredentials = true;

非简单请求

- 非简单请求是那种对服务器有特殊要求的请求，比如请求方法是`PUT`或`DELETE`，或者`Content-Type`字段的类型是`application/json`。OPTIONS
- 非简单请求的CORS请求，会在正式通信之前，增加一次HTTP查询请求，称为"预检"请求（preflight）。
- 浏览器先询问服务器，当前网页所在的域名是否在服务器的许可名单之中，以及可以使用哪些HTTP动词和头信息字段。只有得到肯定答复，浏览器才会发出正式的`XMLHttpRequest`请求，否则就报错。


下面是一段浏览器的JavaScript脚本。

```javascript
var url = 'http://api.ydlclass.com/cors';
var xhr = new XMLHttpRequest();
xhr.open('PUT', url, true);
xhr.setRequestHeader('X-Custom-Header', 'value');
xhr.send();
```

上面代码中，HTTP请求的方法是`PUT`，并且发送一个自定义头信息`X-Custom-Header`。

浏览器发现，这是一个非简单请求，就【自动】发出一个"预检"请求，要求服务器确认可以这样请求。下面是这个"预检"请求的HTTP头信息。

```
OPTIONS /cors HTTP/1.1
Origin: http://api.bob.com
Access-Control-Request-Method: PUT
Access-Control-Request-Headers: X-Custom-Header
Host: api.ydlclass.com
Accept-Language: en-US
Connection: keep-alive
User-Agent: Mozilla/5.0...
```

"预检"请求用的请求方法是`OPTIONS`，表示这个请求是用来询问的。头信息里面，关键字段是`Origin`，表示请求来自哪个源。

除了`Origin`字段，"预检"请求的头信息包括两个特殊字段。

- （1）Access-Control-Request-Method该字段是必须的，用来列出浏览器的CORS请求会用到哪些HTTP方法，上例是`PUT`。
- （2）Access-Control-Request-Headers该字段是一个逗号分隔的字符串，指定浏览器CORS请求会额外发送的头信息字段，上例是`X-Custom-Header`。

预检请求的响应

```json
 HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 01:15:39 GMT
Server: Apache/2.0.61 (Unix)
Access-Control-Allow-Origin: http://api.bob.com
Access-Control-Allow-Methods: GET, POST, PUT
Access-Control-Allow-Headers: X-Custom-Header
Content-Type: text/html; charset=utf-8
Content-Encoding: gzip
Content-Length: 0
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
Content-Type: text/plain
```

如果服务器否定了"预检"请求，会返回一个正常的HTTP回应，但是没有任何CORS相关的头信息字段。这时，浏览器就会认定，服务器不同意预检请求，因此触发一个错误，被`XMLHttpRequest`对象的`onerror`回调函数捕获。控制台会打印出如下的报错信息。

```json
XMLHttpRequest cannot load http://api.ydlclass.com.
Origin http://api.bob.com is not allowed by Access-Control-Allow-Origin.
```

Access-Control-Max-Age该字段可选，用来指定本次预检请求的有效期，单位为秒。上面结果中，有效期是20天（1728000秒），即允许缓存该条回应1728000秒（即20天），在此期间，不用发出另一条预检请求。

##### 解决方案

用过滤器进行统一的处理

```java
public class CORSFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
    }
}
```

对api为前缀的请求都进行处理：

```xml
<!-- CORS Filter -->
<filter>
    <filter-name>CORSFilter</filter-name>
    <filter-class>com.ydlclass.filter.CORSFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>CORSFilter</filter-name>
    <url-pattern>/api/*</url-pattern>
</filter-mapping>
```

更简单的方案

Controller 上使用 `@CrossOrigin` 注解就可以实现跨域，这个注解是一个类级别也是方法级别的注解

```java
@CrossOrigin(maxAge = 3600)
@RestController 
@RequestMapping("goods")
public class GoodsController{
}
```

全局匹配类

```java
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("PUT", "DELETE")
            .allowedHeaders("header1", "header2", "header3")
            .exposedHeaders("header1", "header2")
            .allowCredentials(false).maxAge(3600);
    }
}
```

```xml
<mvc:cors>
  <mvc:mapping path="/api/**"
        allowed-origins="*"
        allowed-methods="GET, PUT"
        allowed-headers="header1, header2, header3"
        exposed-headers="header1, header2" allow-credentials="false"
        max-age="123" />

    <mvc:mapping path="/resources/**"
        allowed-origins="http://domain1.com" />
</mvc:cors>
```

#### restful

rest风格的软件架构模式后为Web API的标准

##### 	rest架构的主要原则

- 网络上的所有事物都被抽象为资源。
- 每个资源都有一个唯一的资源标识符。
- 同一个资源具有多种表现形式他可能是xml，也可能是json等。
- 对资源的各种操作不会改变资源标识符。
- 所有的操作都是无状态的。
- 符合REST原则的架构方式即可称为RESTful。

##### 	什么是restful

- 一种常见的rest的应用,是遵守了rest风格的web服务，rest式的web服务是一种ROA(The Resource-Oriented Architecture)(面向资源的架构).
- 将互联网的资源抽象成资源，将获取资源的方式定义为方法，从此请求再也不止get和post了：


| 客户端请求     | 原来风格URL地址           | RESTful风格URL地址 |
| --------- | ------------------- | -------------- |
| 查询所有用户    | /user/findAll       | GET /user      |
| 查询编号为1的用户 | /user/findById?id=1 | GET /user/1    |
| 新增一个用户    | /user/save          | POST /user     |
| 修改编号为1的用户 | /user/update        | PUT /user/1    |
| 删除编号为1的用户 | /user/delete?id=1   | DELETE /user/1 |

Spring MVC 对 RESTful应用提供了以下支持

- 利用@RequestMapping 指定要处理请求的URI模板和HTTP请求的动作类型
- 利用@PathVariable讲URI请求模板中的变量映射到处理方法参数上
- 利用Ajax,在客户端发出PUT、DELETE动作的请求

##### 	数据过滤

我们想获取所有用户，使用如下url即可`/user`。但是真是场景下，我们可能需要需要一些条件进行过滤：

例如：我们需要查询名字叫张三的前10条数据，使用以下场景即可：

```url
/user?name=jerry&pageSize=10&page=1
```

##### 	RequestMapping中指定方法

```java
@RequestMapping(value = "/{id}", method = RequestMethod.GET)
@RequestMapping(value = "/add", method = RequestMethod.POST)
@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)   
@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//更好替换项
@GetMapping("/user/{id}")
@PostMapping("/user")
@DeleteMapping("/user/{id}")
@PutMapping("/user/{id}")
```

```javascript
$.ajax( {
    type : "GET",
    url : "http://localhost:8080/springmvc/user/rest/1",
    dataType : "json",
    success : function(data) {
        console.log("get请求！---------------------")
        console.log(data)
    }
});

 const instance = axios.create({
        baseURL: 'http://127.0.0.1:8088/app/'
    });
    // 为给定 ID 的 user 创建请求
    instance.get('goods')
        .then(function (response) {
        console.log(response);
    }).catch(function (error) {
        console.log(error);
    });

```

#### 文件上传和下载

##### 	文件上传

【MultipartResolver】用于处理文件上传。当收到请求时，DispatcherServlet 的 checkMultipart() 方法会调用 MultipartResolver 的 isMultipart() 方法判断请求中【是否包含文件】。如果请求数据中包含文件，则调用 MultipartResolver 的 resolveMultipart() 方法对请求的数据进行解析，然后将文件数据解析成 MultipartFile 并封装在 MultipartHttpServletRequest (继承了 HttpServletRequest) 对象中，最后传递给 Controller。

MultipartResolver 默认不开启，需要手动开启。

文件上传对前端表单有如下要求：为了能上传文件，必须将表单的【method设置为POST】，并将enctype设置为【multipart/form-data】。只有在这样的情况下，浏览器才会把用户选择的文件以二进制数据发送给服务器。

表单中的enctype

- application/x-www-form-urlencoded：默认方式，只处理表单域中的 value 属性值，采用这种编码方式的表单会将表单域中的值处理成 URL 编码方式。
- multipart/form-data：这种编码方式会以二进制流的方式来处理表单数据，这种编码方式会把文件域指定文件的内容也封装到请求参数中，不会对字符编码。

开源的Commons FileUpload组件，成为Servlet/JSP程序员上传文件的最佳选择。

```xml
<!--文件上传-->
<dependency>
   <groupId>commons-fileupload</groupId>
   <artifactId>commons-fileupload</artifactId>
   <version>1.3.3</version>
</dependency>
```

```xml
<!--文件上传配置-->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
   <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
   <property name="defaultEncoding" value="utf-8"/>
   <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
   <property name="maxUploadSize" value="10485760"/>
   <property name="maxInMemorySize" value="40960"/>
</bean>
```

CommonsMultipartFile 的常用方法：

- String getOriginalFilename()：获取上传文件的原名
- InputStream getInputStream()：获取文件流
- void transferTo(File dest)：将上传文件保存到一个目录文件中

前端

```xml
<form action="/upload" enctype="multipart/form-data" method="post">
 <input type="file" name="file"/>
 <input type="submit" value="upload">
</form>
```

```java
@PostMapping("/upload")
@ResponseBody
public R upload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws Exception{
    //获取文件名 : file.getOriginalFilename();
    String uploadFileName = file.getOriginalFilename();
    System.out.println("上传文件名 : "+uploadFileName);
    //上传路径保存设置
    String path = "D:/upload";
    //如果路径不存在，创建一个
    File realPath = new File(path);
    if (!realPath.exists()){
        realPath.mkdir();
    }
    System.out.println("上传文件保存地址："+realPath);
    //就问香不香，就和你写读流一样
    file.transferTo(new File(path+"/"+uploadFileName));
    return R.success();
}
```

##### 文件下载

```java
@GetMapping("/download2")
public ResponseEntity<byte[]> download2(){
    try {
        String fileName = "楠老师.jpg";
        byte[] bytes = FileUtils.readFileToByteArray(new File("D:/upload/"+fileName));
        HttpHeaders headers=new HttpHeaders();
        // Content-Disposition就是当用户想把请求所得的内容存为一个文件的时候提供一个默认的文件名。
         //inline ：将文件内容直接显示在页面 
        //attachment：弹出对话框让用户下载具体例子：
        headers.set("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
     
        headers.set("charsetEncoding","utf-8");
        headers.set("content-type","multipart/form-data");
        ResponseEntity<byte[]> entity=new ResponseEntity<>(bytes,headers, HttpStatus.OK);
        return entity;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
```

#### WebSocket

WebSocket 协议提供了一种标准化方式，可通过单个 TCP 连接在客户端和服务器之间建立全双工、双向通信通道。它是与 HTTP 不同的 TCP 协议，但旨在通过 HTTP 工作，使用端口 80 和 443。

WebSocket 交互以 HTTP 请求开始，HTTP请求中包含`Upgrade: websocket `时，会切换到 WebSocket 协议。以下示例显示了这样的交互：

尽管 WebSocket 被设计为与 HTTP 兼容并从 HTTP 请求开始，但这两种协议会产生不同的架构和应用程序编程模型。

在 HTTP 和 REST 中，一个应用程序被建模为多个 URL。为了与应用程序交互，客户端访问这些 URL，请求-响应样式。服务器根据 HTTP URL、方法和请求头将请求路由到适当的处理程序。而在 WebSocket中，通常只有一个 URL 用于初始连接。随后，所有应用程序消息都在同一个 TCP 连接上流动。

> 我们现在有一个需要，就是页面用实时显示当前的库存信息：

**短轮询:**

最简单的一种方式，就是你用JS写个死循环（setInterval），不停的去请求服务器中的库存量是多少，然后刷新到这个页面当中，这其实就是所谓的短轮询。

这种方式有明显的坏处，那就是你很浪费服务器和客户端的资源。客户端还好点，现在PC机配置高了，你不停的请求还不至于把用户的电脑整死，但是服务器就很蛋疼了。如果有1000个人停留在某个商品详情页面，那就是说会有1000个客户端不停的去请求服务器获取库存量，这显然是不合理的。

**长轮询：**

长轮询这个时候就出现了，其实长轮询和短轮询最大的区别是，短轮询去服务端查询的时候，不管库存量有没有变化，服务器就立即返回结果了。而长轮询则不是，在长轮询中，服务器如果检测到库存量没有变化的话，将会把当前请求挂起一段时间（这个时间也叫作超时时间，一般是几十秒）。在这个时间里，服务器会去检测库存量有没有变化，检测到变化就立即返回，否则就一直等到超时为止。

而对于客户端来说，不管是长轮询还是短轮询，客户端的动作都是一样的，就是不停的去请求，不同的是服务端，短轮询情况下服务端每次请求不管有没有变化都会立即返回结果，而长轮询情况下，如果有变化才会立即返回结果，而没有变化的话，则不会再立即给客户端返回结果，直到超时为止。

这样一来，客户端的请求次数将会大量减少（这也就意味着节省了网络流量，毕竟每次发请求，都会占用客户端的上传流量和服务端的下载流量），而且也解决了服务端一直疲于接受请求的窘境。

但是长轮询也是有坏处的，因为把请求挂起同样会导致资源的浪费，假设还是1000个人停留在某个商品详情页面，那就很有可能服务器这边挂着1000个线程，在不停检测库存量，这依然是有问题的。

何时使用 WebSocket

WebSockets 可以使网页具有动态性和交互性。但是，在许多情况下，Ajax 和 HTTP 长轮询的组合可以提供简单有效的解决方案。

例如，新闻、邮件和社交提要需要动态更新，但每隔几分钟更新一次可能也完全没问题。另一方面，协作、游戏和金融应用程序需要更接近实时。

延迟本身并不是决定因素。如果消息量相对较低（例如监控网络故障），HTTP轮询可以提供有效的解决方案。低延迟、高频率和高容量的组合是使用 WebSocket 的最佳案例。

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-websocket</artifactId>
    <version>5.2.18.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-messaging</artifactId>
    <version>5.2.18.RELEASE</version>
</dependency>  
```

```java
public class MessageHandler extends TextWebSocketHandler {

    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    //用来保存连接进来session
    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    /**
     * 关闭连接进入这个方法处理，将session从 list中删除
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("{} 连接已经关闭，现从list中删除 ,状态信息{}", session, status);
    }

    /**
     * 三次握手成功，进入这个方法处理，将session 加入list 中
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("用户{}连接成功.... ",session);
    }

    /**
     * 处理客户发送的信息，将客户发送的信息转给其他用户
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("收到来自客户端的信息: {}",message.getPayload());
        session.sendMessage(new TextMessage("当前时间："+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) +",收到来自客户端的信息!"));
        for(WebSocketSession wss : sessions)
            if(!wss.getId().equals(session.getId())){
                wss.sendMessage(message);
            }
    }
}
```

```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MessageHandler(), "/message")
        .addInterceptors(new HttpSessionHandshakeInterceptor())
        .setAllowedOrigins("*"); //允许跨域访问
    }
}
```

#### SSM整合

##### 	完整依赖

```xml
?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>ssm-study</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>spring-mvc-study</artifactId>
    <packaging>war</packaging>

    <dependencies>
        <!-- 测试相关 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <!-- springmvc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.6.RELEASE</version>
        </dependency>
        <!-- servlet -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.2</version>
            <scope>provided</scope>
        </dependency>

        <!--文件上传-->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.4</version>
        </dependency>
        <!-- jackson -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.11.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.11.2</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.2</version>
        </dependency>

        <!-- mybatis 相关 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
        <!-- 数据库连接驱动 相关 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <!-- 提供了对JDBC操作的完整封装 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.1.10.RELEASE</version>
        </dependency>
        <!-- 织入 相关 -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.4</version>
        </dependency>
        <!-- spring，mybatis整合包 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.2</version>
        </dependency>
        <!-- 集成德鲁伊使用 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.18</version>
        </dependency>
		<!-- 日志 -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.18</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.30</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.8</source> <!-- 源代码使用的JDK版本 -->
                    <target>1.8</target> <!-- 需要生成的目标class文件的编译版本 -->
                    <encoding>utf-8</encoding><!-- 字符集编码 -->
                </configuration>
            </plugin>
        </plugins>
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
</project>
```

##### 	springmvc配置

springmvc-servlet.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!-- 自动扫包 -->
    <context:component-scan base-package="cn.itnanls"/>
    <!-- 让Spring MVC不处理静态资源 -->
    <mvc:default-servlet-handler />
    <!--  让springmvc自带的注解生效  -->
    <mvc:annotation-driven >


    </mvc:annotation-driven>
    <bean id="fastjson" class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
        <property name="supportedMediaTypes">
            <list>
                <value>text/html;charset=UTF-8</value>
                <value>application/json;charset=UTF-8</value>
            </list>
        </property>
    </bean>

    <!--文件上传配置-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
        <property name="defaultEncoding" value="utf-8"/>
        <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
        <property name="maxUploadSize" value="10485760"/>
        <property name="maxInMemorySize" value="40960"/>
    </bean>

    <!-- 视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"         id="internalResourceViewResolver">
        <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/page/" />
        <!-- 后缀 -->
        <property name="suffix" value=".jsp" />
    </bean>
</beans>
```

##### 	spring配置

```java
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- 加载外部的数据库信息 classpath:不叫会报错具体原因下边解释-->
    <context:property-placeholder location="classpath:db.properties"/>
    <!-- 加入springmvc的配置 -->
    <import resource="classpath:springmvc-servlet.xml"/>

    <!-- Mapper 扫描器 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 扫描 cn.wmyskxz.mapper 包下的组件 -->
        <property name="basePackage" value="cn.itnanls.dao"/>
    </bean>

    <!--配置数据源：数据源有非常多，可以使用第三方的，也可使使用Spring的-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--配置SqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--关联Mybatis-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <property name="mapperLocations" value="classpath:mappers/*.xml"/>
    </bean>

    <!--注册sqlSessionTemplate , 关联sqlSessionFactory-->
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
        <!--利用构造器注入-->
        <constructor-arg index="0" ref="sqlSessionFactory"/>
    </bean>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>


    <!--配置事务通知-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--配置哪些方法使用什么样的事务,配置事务的传播特性-->
            <tx:method name="add*" propagation="REQUIRED"/>
            <tx:method name="delete*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <tx:method name="search*" propagation="SUPPORTS" read-only="true"/>
            <tx:method name="get*" propagation="SUPPORTS" read-only="true"/>
            <tx:method name="find*" propagation="SUPPORTS" read-only="true"/>
            <tx:method name="*" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
    <!--配置aop织入事务-->
    <aop:config>
        <aop:pointcut id="txPointcut" expression="execution(* com.ydlclass.service.impl.*.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
    </aop:config>

</beans>
```

##### web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--注册DispatcherServlet，这是springmvc的核心，就是个servlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application.xml</param-value>
        </init-param>
        <!--加载时先启动-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <!--/ 匹配所有的请求；（不包括.jsp）-->
    <!--/* 匹配所有的请求；（包括.jsp）-->
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    
</web-app>
```