#### 了解MyBatis

##### 	作用

- MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。

##### 	持久化

​ 持久化是将程序数据在持久状态和瞬时状态间转换的机制。通俗的讲，就是瞬时数据（比如内存中的数据，是不能永久保存的）持久化为持久数据（比如持久化至数据库中，能够长久保存）。

1. 程序产生的数据首先都是在内存。
2. 内存是不可靠的，他丫的一断电数据就没了。
3. 那可靠的存储地方是哪里？硬盘、U盘、光盘等。
4. 我们的程序在运行时说的持久化通常就是指将内存的数据存在硬盘。

##### 	持久层

- 业务是需要操作数据的
- 数据是在磁盘上的
- 具体业务调用具体的数据库操作，耦合度太高，复用性太差
- 将操作数据库的代码统一抽离出来，自然就形成了介于业务层和数据库中间的独立的层

##### 	ORM

ORM，即Object-Relational Mapping（对象关系映射），它的作用是在关系型数据库和业务实体对象之间作一个映射，这样，我们在具体的操作业务对象的时候，就不需要再去和复杂的SQL语句打交道，只需简单的操作对象的属性和方法。

#### 小知识

##### 	DTD

DTD(Document Type Definition)即文档类型定义，是一种XML约束模式语言，是XML文件的验证机制

```xml
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
```

1、DTD声明始终以!DOCTYPE开头，空一格后跟着文档根元素的名称。

2、根元素名：configuration。所以每一个标签库定义文件都是以taglib为根元素的，否则就不会验证通过。

3、PUBLIC "-//mybatis.org//DTD Config 3.0//EN，这是一个公共DTD的名称（私有的使用SYSTEM表示）。这个东西命名是有些讲究的。首先它是以"-"开头的，表示这个DTD不是一个标准组织制定的。（如果是ISO标准化组织批准的，以“ISO”开头）。接着就是双斜杠“//”，跟着的是DTD所有者的名字，很明显这个DTD是MyBatis公司定的。接着又是双斜杠“//”，然后跟着的是DTD描述的文档类型，可以看出这份DTD描述的是DTD Config 3.0的格式。再跟着的就是“//”和ISO 639语言标识符。

4、绿色的字"http://mybatis.org/dtd/mybatis-3-config.dtd"，表示这个DTD的位置。

##### 	XSD

文档结构描述XML Schema Definition 缩写，这种文件同样可以用来定义我们xml文件的结构！

```XML
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd
">
```

第一行的xmlns代表了一个xml文件中的一个命名空间，通常是一个唯一的字符串，一般使用一个url，因为不会重复嘛。

xmlns:xsi 定义了一个命名空间前缀 xsi 对应的唯一字符串 http://www.w3.org/2001/XMLSchema-instance。但这个 xmlns:xsi 在不同的 xml 文档中似乎都会出现。 这是因为， xsi 已经成为了一个业界默认的用于 XSD(（XML Schema Definition) 文件的命名空间。 而 XSD 文件（也常常称为 Schema 文件）是用来定义 xml 文档结构的。剩余两行的目的在于为我们的命名空间指定对应的xsd文件。

##### lombok

1. javac对源代码进行分析，生成了一棵抽象语法树（AST）
2. 运行过程中调用实现了“JSR 269 API”的Lombok程序
3. 此时Lombok就对第一步骤得到的AST进行处理，找到@Data注解所在类对应的语法树（AST），然后修改该语法树（AST），增加getter和setter方法定义的相应树节点
4. javac使用修改后的抽象语法树（AST）生成字节码文件，即给class增加新的节点（代码块）

- @AllArgsConstructor：生成全参构造器。
- @NoArgsConstructor：生成无参构造器。
- @Getter/@Setter： 作用类上，生成所有成员变量的getter/setter方法；作用于成员变量上，生成该成员变量的getter/setter方法。可以设定访问权限及是否懒加载等。
- @Data：作用于类上，是以下注解的集合：@ToString @EqualsAndHashCode @Getter @Setter @RequiredArgsConstructor
- @Log：作用于类上，生成日志变量。针对不同的日志实现产品，有不同的注解。

##### 	日志配置

mybatis-config.xml配置  里面的setting标签内设置

```xml
<settings>
   <setting name="logImpl" value="SLF4J"/>
</settings>
```

#### CRUD来一套

##### 	基本流程

```java
// 1、创建一个SqlSessionFactory的 建造者 ，用于创建SqlSessionFactory
// SqlSessionFactoryBuilder中有大量的重载的build方法，可以根据不同的入参，进行构建
// 极大的提高了灵活性，此处使用【创建者设计模式】
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
// 2、使用builder构建一个sqlSessionFactory，此处我们基于一个xml配置文件
// 此过程会进行xml文件的解析，过程相对比较复杂
SqlSessionFactory sqlSessionFactory = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis-config.xml"));
// 3、通过sqlSessionFactory获取另一个session，此处使用【工厂设计模式】
SqlSession sqlSession = sqlSessionFactory.openSession();
```

- 创建一个SqlSessionFactory的 建造者 ，用于创建SqlSessionFactory
- 使用builder构建一个sqlSessionFactory，此处我们基于一个xml配置文件
- 通过sqlSessionFactory获取另一个session，此处使用【工厂设计模式】
- 一个sqlsession就是一个会话，可以使用sqlsession对数据库进行操

SqlSessionFactory

1. ​ 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。
2. ​ SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式

sqlsession

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 

##### ResultType和ResultMap

1. 对于查询结构需要返回的简单pojo，结果都可以映射到一致的hashMap上，换句话来说就是数据库列名可以精确匹配到pojo属性的。一般都用resultType。其实这里有一个隐含的构建机制。映射到resultType的结果都是MyBatis在幕后自动创建了一个resultMap来处理的。简而言之，只要resultType能干的事情resultMap都能干

2. resultMap更擅长来处理复杂映射的结果集。比如一对一、一对多的复杂关系。如果你不但要查询一个班级的情况，附带需要查询班级所在的学校，班级学生的详细情况，甚至是班级男女学生概况。就必须使用resultMap来描述这些映射关系了。

   ```xml
   <resultMap id="userMap" type="com.ydlclass.entity.User">
       <id column="id" property="id"/>
       <result column="user_name" property="username"/>
       <result column="pass" property="password"/>
   </resultMap>
   ```

   动态代理实现

   ```java
   //定义一个接口
   public interface UserMapper {
       List<User> selectAll();
   }
   //修改映射文件，让命名空间改为接口的权限定名，id改为方法的名字
   <mapper namespace="com.ydlclass.mapper.UserMapper">
       <select id="selectAll" resultType="com.ydlclass.entity.User">
           select * from user
       </select>
   </mapper>
     
     try (SqlSession sqlSession = sqlSessionFactory.openSession();){
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       List<User> list = mapper.selectAll();
       LOGGER.debug("result is [{}]",list);
   }
   ```

   sqlSession.getMapper(UserMapper.class)帮我们生成一个代理对象，该对象实现了这个接口的方法，具体的数据库操作比如建立连接，创建statment等重复性的工作交给框架来处理，唯一需要额外补充的就是sql语句了，xml文件就是在补充这个描述信息，比如具体的sql，返回值的类型等，框架会根据命名空间自动匹配对应的接口，根据id自动匹配接口的方法，不需要我们再做额外的操作。


##### 	select

```xml
User selectUserById(int id);

<select id="selectUserById" resultType="com.ydlclass.entity.User"  parameterType="int">
    select id,username,password from user where id = #{id}
</select>
```

- resultType：指定返回类型，查询是有结果的，结果啥类型，你得告诉我
- parameterType：指定参数类型，查询是有参数的，参数啥类型，你得告诉我
- id：指定对应的方法映射关系，就是你得告诉我你这sql对应的是哪个方法
- \#{id}：sql中的变量，要保证大括号的变量必须在User对象里有
- \#{}：占位符，其实就是咱们的【**PreparedStatement**】处理这个变量，mybatis会将它替换成`?`

\#{ } 和  \${ } : 

- \#{} 的作用主要是替换预编译语句(PrepareStatement)中的占位符
- ${} 的作用是直接进行字符串替换


##### insert

```xml
int addUser(User user);

<insert id="addUser" parameterType="com.ydlclass.entity.User">
    insert into user (id,username,password) values (#{id},#{username},#{password})
</insert>
```

注意insert需要开启事务

- 在openSession方法传入true，就变成自动提交了

- 手动提交 sqsession.commit()  -> sqsession.close()

当对象字段名与数据库表中列名不一致时需要加@Param注解：数据库中列名为id username password、 对象字段为userId   name  pwd

```java
int insertUser(@Param("id") int userId, @Param("username") String name,@Param("password") String pws);
```

##### 	update

```java
int updateUser(User user);

<update id="updateUser" parameterType="com.ydlclass.entity.User">
    update user set username=#{username},password=#{password} where id = #{id}
</update>
```

注意，需要开启事务

##### 	delete

```java
int deleteUser(int id);

<delete id="deleteUser" parameterType="int">
  delete from user where id = #{id}
</delete>
```

##### 	模糊查询

```xml
//java拼串
string name = “%IT%”;
list<name> names = mapper.getUserByName(name);
<select id=”getUsersByName”>
	select * from user where name like #{name}
</select>

//配置文件中
string name = “IT”;
list<User> users = mapper.getUserByName(name);
<select id=”getUsersByName”>
    select * from user where name like "%"#{name}"%"
</select>
  
 <select id=”getUsersByName”>
    select * from user where name like "%${name}%"
</select>
```

##### map使用

```xml
List<User> getUsersByParams(Map<String,String> map);
  
<select id="getUsersByParams" parameterType="java.util.HashMap">
    select id,username,password from user where username = #{name}
</select>
  
   UserMapper mapper = session.getMapper(UserMapper.class);
    Map<String,String> map = new HashMap<String, String>();
    map.put("name","磊磊哥");
    List<User> users = mapper.getUsersByParams(map);
```

#### 使用注解开发

利用注解开发不需要mapper.xml映射文件,直接在方法名上加@Select、@Update、@Insert、@Delete写sql

#### 架构源码

##### 	架构讲解

![](https://www.ydlclass.com/doc21xnv/assets/image-20211104152708780.70319dea.png)

**API接口层**：提供给外部使用的接口API，开发人员通过这些本地API来操纵数据库。接口层一接收到调用请求就会调用数据处理层来完成具体的数据处理。

**数据处理层**：负责具体的SQL查找、SQL解析、SQL执行和执行结果映射处理等。它主要的目的是根据调用的请求完成一次数据库操作。

**基础支撑层**：负责最基础的功能支撑，包括连接管理、事务管理、配置加载和缓存处理，这些都是共用的东西，将他们抽取出来作为最基础的组件。为上层的数据处理层提供最基础的支撑。

##### 	核心成员

1. **Configuration：**MyBatis所有的配置信息都保存在Configuration对象之中，配置文件中的大部分配置都会存储到该类中
2. **SqlSession：**作为MyBatis工作的主要顶层API，表示和数据库交互时的会话，完成必要数据库增删改查功能
3. **Executor：**MyBatis执行器，是MyBatis 调度的核心，负责SQL语句的生成和查询缓存的维护
4. **StatementHandler：**封装了JDBC Statement操作，负责对JDBC statement 的操作，如设置参数等
5. **ParameterHandler：**负责对用户传递的参数转换成JDBC Statement 所对应的数据类型
6. **ResultSetHandler：**负责将JDBC返回的ResultSet结果集对象转换成List类型的集合
7. **TypeHandler：**负责java数据类型和jdbc数据类型(也可以说是数据表列类型)之间的映射和转换
8. **MappedStatement：**MappedStatement维护一条<select|update|delete|insert>节点的封装
9. **SqlSource：**负责根据用户传递的parameterObject，动态地生成SQL语句，将信息封装到BoundSql对象中，并返回
10. **BoundSql：**表示动态生成的SQL语句以及相应的参数信息

![](https://www.ydlclass.com/doc21xnv/assets/63651-20180924164811172-1839433605.fe1e078c.jpg)

​ xml的解析过程就是将xml文件转化为Configuration对象，它在启动的时候执行，也就意味着修改配置文件就要重启

##### 别名系统

核心配置文件加入

```xml
<typeAliases>
    <typeAlias type="com.ydlclass.entity.User"  alias="user"/>
</typeAliases>
```

`type` 填写 实体类的全类名，`alias` 可以不填，不填的话，默认是类名，不区分大小写，`alias` 填了的话就以 alias里的值为准。

```xml
<typeAiases>
    <package name=""/>
</typeAliases>
```

`package` 标签 为某个包下的所有类起别名； `name` 属性填写包名。 别名默认是类名，不区分大小

```xml
@Alias` 注解   加在实体类上，为某个类起别名；例：`@Alias("User")
```

#### 动态SQL

##### 	if元素

```xml
<select id="findUserById" resultType="com.ydlclass.entity.User">
    select id,username,password from user
    where 1 =1
    <if test="id != null">
        AND id = #{id}
    </if>
    <if test="username != null and username != ''">
        AND username = #{username}
    </if>
    <if test="password != null and password != ''">
        AND password = #{password}
    </if>
</select>
```

##### 	where元素

```xml
<select id="findUserById" resultType="com.ydlclass.entity.User">
    select id,username,password from user
    <where>
        <if test="id != null">
            AND id = #{id}
        </if>
        <if test="username != null and username != ''">
            AND username = #{username}
        </if>
        <if test="password != null and password != ''">
            AND password = #{password}
        </if>
    </where>
</select>
```

##### 	trim元素

有时候我们要去掉一些特殊的SQL语法，比如常见的and、or，此时可以使用trim元素。trim元素意味着我们需要去掉一些特殊的字符串，prefix代表的是语句的前缀，而prefixOverrides代表的是你需要去掉的那种字符串，suffix表示语句的后缀，suffixOverrides代表去掉的后缀字符串

```xml
<select id="select" resultType="com.ydlclass.entity.User">
    SELECT * FROM user
    <trim prefix="WHERE" prefixOverrides="AND">
        <if test="username != null and username != ''">
            AND username LIKE concat('%', #{username}, '%')
        </if>
        <if test="id != null">
            AND id = #{id}
        </if>
    </trim>
</select>
```

##### 	choose、when、otherwise

```xml
<!-- 有name的时候使用name搜索，没有的时候使用id搜索 -->
<select id="select" resultType="com.ydlclass.entity.User">
    SELECT * FROM user
    WHERE 1=1
    <choose>
        <when test="name != null and name != ''">
            AND username LIKE concat('%', #{username}, '%')
        </when>
        <when test="id != null">
            AND id = #{id}
        </when>
    </choose>
</select>
```

##### 	set元素

在update语句中，如果我们只想更新某几个字段的值，这个时候可以使用set元素配合if元素来完成。**注意：set元素遇到,会自动把,去掉**。

```xml
<update id="update">
    UPDATE user
    <set>
        <if test="username != null and username != ''">
            username = #{username},
        </if>
        <if test="password != null and password != ''">
            password = #{password}
        </if>
    </set>
    WHERE id = #{id}
</update>
```

​	foreach元素

foreach元素是一个循环语句，它的作用是遍历集合，可以支持数组、List、Set接口。

```xml
<select id="select" resultType="com.ydlclass.entity.User">
    SELECT * FROM user
    WHERE id IN
    <foreach collection="ids" open="(" close=")" separator="," item="id">
        #{id}
    </foreach>
</select>
```

- collection配置的是传递进来的参数名称。
- item配置的是循环中当前的元素。
- index配置的是当前元素在集合的位置下标。
- open和 close配置的是以什么符号将这些集合元素包装起来。
- separator是各个元素的间隔符。

##### foreach批量插入

```xml
<insert id="batchInsert" parameterType="list">
    insert into `user`( user_name, pass)
    values
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.password})
    </foreach>
</insert>
```

##### SQL片段

```xml
#提取sql片段
<sql id="if-title-author">
   <if test="title != null">
      title = #{title}
   </if>
   <if test="author != null">
      and author = #{author}
   </if>
</sql>

<select id="queryBlogIf" parameterType="map" resultType="blog">
  select * from blog
   <where>
       <!-- 引用 sql 片段，如果refid 指定的不在本文件中，那么需要在前面加上 namespace -->
       <include refid="if-title-author"></include>
       <!-- 在这里还可以引用其他的 sql 片段 -->
   </where>
</select>
```

#### 数据关系处理

```xml
//一对一用association  一对多用 collection
//级联
<resultMap id="employeeMap" type="employee">
        <id column="id" property="id" />
        <result column="name" property="name"/>
        <association property="dept" column="did" javaType="dept"
                     select="com.ydlclass.mapper.DeptMapper.select"> //根据did查到dept表java类型时dept(javaType 返回类型为dept)  去DeptMapper.xml查询   
            <id column="id" property="id"/>
            <result column="name" property="name"/>
        </association>
    </resultMap>

//结果嵌套
    <resultMap id="employeeMap2" type="com.ydlclass.entity.Employee">
    <id column="eid" property="id"/>
    <result column="ename" property="name"/>
        <association property="dept" javaType="com.ydlclass.entity.Dept">
            <id column="did" property="id"/>
            <result column="dname" property="name"/>
        </association>
    </resultMap>
    <select id="select2" resultMap="employeeMap2" >
        select d.id did, d.name dname, e.id eid, e.name ename
        from dept d
        left join employee e on d.id = e.did
    </select>


 <resultMap id="deptMap" type="dept">
        <id column="id" property="id"/>
        <result column="name" property="name"/>
        <collection property="employees" javaType="list" ofType="employee"  
                    column="id" select="com.ydlclass.mapper.EmployeeMapper.selectByDid">
          //ofType为传入参数
            <id column="eid" property="id"/>
            <result column="ename" property="name"/>
        </collection>
    </resultMap>

<resultMap id="deptMap2" type="dept">
        <id column="did" property="id"/>
        <result column="dname" property="name"/>
        <collection property="employees" javaType="list" ofType="employee">
            <id column="eid" property="id"/>
            <result column="ename" property="name"/>

        </collection>
    </resultMap>
```

##### 按查询嵌套

这种方式是一种级联查询的方式，会产生多个sql语句，第一个sql的查询语句结果会触发第二个查询语句。

##### 结果查询

结果嵌套是使用复杂查询，在根据结果的字段进行对象的封装，本质只会发送一个sql。

##### 懒加载

- 按需加载，我们需要什么的时候再去进行什么操作。而且先从单表查询，需要时再从关联表去关联查询，能大大提高数据库性能，因为查询单表要比关联查询多张表速度要快。

- 在mybatis中，resultMap可以实现高级映射(使用association、collection实现一对一及一对多映射)，association、collection具备延迟加载功能。


```xml
<settings>
  <!-- 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 -->
  <setting name="lazyLoadingEnabled" value="true"/>
  <!-- 开启时，任一方法的调用都会加载该对象的所有延迟加载属性。 否则，每个延迟加载属性会按需加载 -->
  <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

##### MyBatis获取主键自增

使用xml

直接在标签属性上添加 useGeneratedKeys（是否是自增长，必须设置 true） 和 keyProperty（实体类主键属性名称） 、keyColumn（数据库主键字段名称）

```xml
<insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
    insert into `user`(id, username, password)
    values (#{id}, #{username}, #{password})
</insert>
```

注解

```java
@Insert("INSERT INTO user(name,age) VALUES(#{user.name},#{user.age})")
@Options(useGeneratedKeys=true, keyProperty="user.id",keyColumn="id"  )
public int insert(@Param("user")User user);
```

全局设置

```xml
<setting name="useGeneratedKeys" value="true"/>
```

#### MyBatis缓存

##### 	为什么用缓存

- 如果缓存中有数据，就不用从数据库获取，大大提高系统性能。
- MyBatis提供一级缓存和二级缓存

##### 	一级缓存

一级缓存时sqlsession级别的缓存

- 在操作数据库时，需要构造sqlsession对象，在对象中有一个数据结构（HashMap）用于存储缓存数据

- 不同的sqlsession之间的缓存区域是互相不影响的。

  ![](https://www.ydlclass.com/doc21xnv/assets/yijihuancun.84f2500b.png)

  - 第一次发起查询sql查询用户id为1的用户，先去找缓存中是否有id为1的用户，如果没有，再去数据库查询用户信息。得到用户信息，将用户信息存储到一级缓存中。
  - 如果sqlsession执行了commit操作（插入，更新，删除），会清空sqlsession中的一级缓存，避免脏读
  - 第二次发起查询id为1的用户，缓存中如果找到了，直接从缓存中获取用户信息
  - MyBatis默认支持并开启一级缓存。

一级缓存失效

1. sqlSession不同
2. 当sqlSession对象相同的时候，查询的条件不同，原因是第一次查询时候，一级缓存中没有第二次查询所需要的数据
3. 当sqlSession对象相同，两次查询之间进行了插入的操作
4. 当sqlSession对象相同，手动清除了一级缓存中的数据

一级缓存的生命周期

1. MyBatis在开启一个数据库会话时，会创建一个新的SqlSession对象，SqlSession对象中会有一个新的Executor对象，Executor对象中持有一个新的PerpetualCache对象；当会话结束时，SqlSession对象及其内部的Executor对象还有PerpetualCache对象也一并释放掉。
2. 如果SqlSession调用了close()方法，会释放掉一级缓存PerpetualCache对象，一级缓存将不可用。
3. 如果SqlSession调用了clearCache()，会清空PerpetualCache对象中的数据，但是该对象仍可使用。
4. SqlSession中执行了任何一个update操作(update()、delete()、insert()) ，都会清空PerpetualCache对象的数据，但是该对象可以继续使用。

##### 二级缓存

二级缓存时mapper级别的缓存

- 多个sqlsession去操作同一个mapper的sql语句，多个sqlsession可以共用二级缓存，所得到的数据会存在二级缓存区域
- 二级缓存是跨sqlsession的
- 二级缓存相比一级缓存的范围更大（按namespace来划分），多个sqlsession可以共享一个二级缓存

需要手动开启二级缓存

```xml
<settings>
    <!--开启二级缓存-->
    <setting name="cacheEnabled" value="true"/>       
</settings>
<!-- 需要将映射的javabean类实现序列化 -->

<!--开启本Mapper的namespace下的二级缓存-->
<cache eviction="LRU" flushInterval="100000"/>
```

eviction：回收策略（缓存满的淘汰机制）

1. LRU（Least Recently Used），最近最少使用的，最长时间不用的对象
2. FIFO（First In First Out），先进先出，按对象进入缓存的顺序来移除他们
3. SOFT，软引用，移除基于垃圾回收器状态和软引用规则的对象，当内存不足，会触发JVM的GC，如果GC后，内存还是不足，就会把软引用的包裹的对象给干掉，也就是只有内存不足，JVM才会回收该对象。
4. WEAK，弱引用，更积极的移除基于垃圾收集器状态和弱引用规则的对象。弱引用的特点是不管内存是否足够，只要发生GC，都会被回收。
5. flushInterval：刷新时间间隔(ms)  不配置它，那么当SQL被执行的时候才会去刷新缓存。
6. size :  引用数目代表缓存最多可以存储多少个对象，不宜设置过大。设置过大会导致内存溢出
7. readOnly 只读    意味着缓存数据只能读取而不能修改，这样设置的好处是我们可以快速读取缓存，缺点是我们没有

操作过程

- ​	sqlsession1查询用户id为1的信息，查询到之后，会将查询数据存储到二级缓存中。
- ​	如果sqlsession3去执行相同mapper下sql，执行commit提交，会清空该mapper下的二级缓存区域的数据
- ​	sqlsession2查询用户id为1的信息， 去缓存找是否存在缓存，如果存在直接从缓存中取数据

禁用二级缓存

```xml
<select id="getStudentById" parameterType="java.lang.Integer" resultType="Student" useCache="false">
```

flushCache刷新缓存（清空缓存）可以避免脏读

```
<select id="getStudentById" parameterType="java.lang.Integer" resultType="Student" flushCache="true">
```

应用场景：结果实时性要求不高的情况下

二级缓存对细粒度的数据级别的缓存实现不好，比如如下需求：对商品信息进行缓存，由于商品信息查询访问量大，但是要求用户每次都能查询最新的商品信息，此时如果使用MyBatis的二级缓存就无法实现当一个商品变化时只刷新该商品的缓存信息而不刷新其它商品的信息，因为MyBatis的二级缓存区域以mapper为单位划分，当一个商品信息变化会将所有商品信息的缓存数据全部清空。解决此类问题需要在业务层根据需求对数据有针对性缓存。

#### 配置文件

##### 	配置顺序

```
1.properties是一个配置属性的元素
2.settings设置，mybatis最为复杂的配置也是最重要的，会改变mybatis运行时候的行为
3.typeAliases别名（在TypeAliasRegistry中可以看到mybatis提供了许多的系统别名）
4.typeHandlers 类型处理器（比如在预处理语句中设置一个参数或者从结果集中获取一个参数时候，都会用到类型处理器，在TypeHandlerRegistry中定义了很多的类型处理器）
5.objectFactory 对象工厂（myabtis在构建一个结果返回的时候，会使用一个ObjectFactory去构建pojo)
6.plugins 插件
7.environments 环境变量
  environment 环境变量
   transactionManager 事务管理器
   dataSource 数据源
   databaseIdProvider 数据库厂商标识
8.mappers 映射器
```

##### 	environments

environments可以为mybatis配置多环境运行，将SQL映射到多个不同的数据库上，必须指定其中一个为默认运行环境（通过default指定），如果想切换环境修改default的值即可。

##### 	简单参数

| cacheEnabled             | 该配置影响的所有映射器中配置的缓存的全局开关。                  | true \| false                            | true           |
| ------------------------ | ---------------------------------------- | ---------------------------------------- | -------------- |
| lazyLoadingEnabled       | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态。 | true \| false                            | false          |
| useColumnLabel           | 使用列标签代替列名。不同的驱动在这方面会有不同的表现，具体可参考相关驱动文档或通过测试这两种不同的模式来观察所用驱动的结果。 | true \| false                            | true           |
| useGeneratedKeys         | 允许 JDBC 支持自动生成主键，需要驱动兼容。如果设置为 true 则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍可正常工作（比如 Derby）。 | true \| false                            | False          |
| defaultStatementTimeout  | 设置超时时间，它决定驱动等待数据库响应的秒数。                  | Any positive integer                     | Not Set (null) |
| mapUnderscoreToCamelCase | 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。 | true \| false                            | False          |
| logPrefix                | 指定 MyBatis 增加到日志名称的前缀。                   | Any String                               | Not set        |
| logImpl                  | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。          | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | Not set        |