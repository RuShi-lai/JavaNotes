#### ORM概述

##### 	为什么使用

ORM（Object-Relational Mapping） 表示对象关系映射。在面向对象的软件开发中，通过ORM，就可以把对象映射到关系型数据库中。只要有一套程序能够做到建立对象与数据库的关联，操作对象就可以直接操作数据库数据，就可以说这套程序实现了ORM对象关系映射

##### 	常见ORM

Mybatis（ibatis）、Hibernate、Jpa

#### JPA与Hibernate

##### 	Hibernate

Hibernate是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，它将POJO与数据库表建立映射关系，是一个全自动的orm框架，hibernate可以自动生成SQL语句，自动执行，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库

##### 	JPA概述

JAVA的全称是Java Persistence API， 即Java 持久化API，是SUN公司推出的一套基于ORM的规范，内部是由一系列的接口和抽象类构成。

##### 	JPA与Hibernate

JPA和Hibernate的关系就像JDBC和JDBC驱动的关系，JPA是规范，Hibernate除了作为ORM框架之外，它也是一种JPA实现。JPA怎么取代Hibernate呢？JDBC规范可以驱动底层数据库吗？答案是否定的，也就是说，如果使用JPA规范进行数据库操作，底层需要hibernate作为其实现类完成数据持久化工作。

#### JPA入门	

配置数据源

导入依赖：spring-boot-starter-data-jpa  ->  mysql-connector-java

##### 	创建实体类

需要在实体类上加注解 

1. @Table(name = "user")  -> 对应数据库哪张表    

2. @Entity表示这个类是一个实体类

3. 主键字段加 @Id   策略：@GeneratedValue(strategy = GenerationType.IDENTITY) //

4. 字段对应数据库中数据   @Column(name = "id")//数据库中的id,对应属性中的id

   ```java
   spring:
     jpa:
       show-sql: true #显示sql
       hibernate:
         ddl-auto: create #自动生成表
   #ddl-auto属性用于设置自动表定义，可以实现自动在数据库中为我们创建一个表，表的结构会根据我们定义的实体类决定，它有4种
   create 启动时删数据库中的表，然后创建，退出时不删除数据表
   create-drop 启动时删数据库中的表，然后创建，退出时删除数据表 如果表不存在报错
   update 如果启动时表格式不一致则更新表，原有数据保留
   validate 项目启动表结构进行校验 如果不一致则报错
   ```

启动测试类完成表创建

##### 访问表

自定义接口JpaRepository

```java
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {}
//两个泛型，前者是具体操作的对象实体，也就是对应的表，后者是ID的类型
```

```java
@SpringBootTest
class YdljpaApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Test
    public void testQuery(){userRepository.findById(1).ifPresent(System.out::println);}
       @Test
    public void testAdd(){
        User user = new User();
        //user.setId(2);
        user.setUsername("lyg");
        user.setPassword("666");
        User saveUser = userRepository.save(user); //新增  返回的实体当中带着实体ID
        System.out.println(saveUser);
    }
    @Test
    public void testDel(){ userRepository.deleteById(2);  }
    @Test
    public void testPageable(){
        userRepository.findAll(PageRequest.of(0,2)).forEach(System.out::println);}
    //根据user的用户名来查询
    @Test
    public void testFind(){
        userRepository.findAllByUsername("lyg").forEach(System.out::println);}
    @Test
    public void testFind2(){
    userRepository.findAllByUsernameAndPassword("lyg","666").forEach(System.out::println);}
    @Test
    public void testFind3(){
        userRepository.findAllByUsernameLike("l%").forEach(System.out::println); }
    @Test
    public void testFind4(){
       userRepository.findAllUser().forEach(System.out::println);}
    @Test
    public void testFind5(){
        userRepository.findAllUserByUsername().forEach(System.out::println);}
    @Test
    public void testFind6(){
        userRepository.findAllUser2().forEach(System.out::println);}
    @Test
    public void testFind7(){
        userRepository.findAllUser3().forEach(System.out::println); }
    @Test
    public void testFind8(){
        userRepository.findAllUser4().forEach(System.out::println);}
    @Test
    public void testFind9(){int row = userRepository.updateUsernameById("luoqi", 1);
        System.out.println(row);}
    @Test
    public void testFind10(){
        int row = userRepository.updateUsernameById2("lq", 1);
        System.out.println(row);}
    @Autowired
    AccountRepository accountRepository;
    @Test
    public void testFind11(){
        Account account = new Account();
        account.setUsername("lq");
        account.setPassword("666");

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setPhone("135188");
        accountDetail.setRealName("luoqi");
        accountDetail.setAddress("cn");
        accountDetail.setEmail("197@qq.com");

        account.setDetail(accountDetail);
        Account save = accountRepository.save(account);

        System.out.println("插入之后account id为：" + save.getId() + "||| accountDetail id" +save.getDetail().getId());
    }
    @Test
    public void testFind12(){
       accountRepository.deleteById(1);
    }

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Test
    public void testFind13(){
        Author author = new Author();
        author.setName("lq");
        Author author1 = authorRepository.save(author);

        Article article1 = new Article();
        article1.setTitle("1");
        article1.setContent("123");
        article1.setAuthor(author1);
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("2");
        article2.setContent("123");
        article2.setAuthor(author1);
        articleRepository.save(article2);
    }

    @Test
    public void testFind14(){
        articleRepository.deleteById(2L);
    }
    @Test
    public void testFind15(){
        Optional<Author> byId = authorRepository.findById(1L);
        if (byId.isPresent()){
            Author author = byId.get();
            List<Article> articleList = author.getArticleList();
            System.out.println(articleList);
        }
    }
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Test
    public void testFind16() {
        Authority authority = new Authority();
        authority.setId(1);
        authority.setName("ROLE_ADMIN");
        authorityRepository.save(authority);
    }

    @Test
    public void testFind17() {
        Users users = new Users();
        users.setUsername("itlils");
        users.setPassword("123456");
        Authority authority = authorityRepository.findById(1).get();
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(authority);
        users.setAuthorityList(authorityList);
        usersRepository.save(users);
    }
    @Test
    public void testFind18() {
        usersRepository.deleteById(1L);
    }
}

```

#### 方法命名规则查询

按照Spring Data JPA 定义的规则，查询方法以findBy开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：条件属性首字母需大写。框架在进行方法名解析时，会先把方法名多余的前缀截取掉，然后对剩下部分进行解析。

| Keyword           | Sample                                  | JPQL                                     |
| ----------------- | --------------------------------------- | ---------------------------------------- |
| And               | findByLastnameAndFirstname              | … where x.lastname = ?1 and x.firstname = ?2 |
| Or                | findByLastnameOrFirstname               | … where x.lastname = ?1 or x.firstname = ?2 |
| Is,Equals         | findByFirstnameIs,findByFirstnameEquals | … where x.firstname = ?1                 |
| Between           | findByStartDateBetween                  | … where x.startDate between ?1 and ?2    |
| LessThan          | findByAgeLessThan                       | … where x.age < ?1                       |
| LessThanEqual     | findByAgeLessThanEqual                  | … where x.age ⇐ ?1                       |
| GreaterThan       | findByAgeGreaterThan                    | … where x.age > ?1                       |
| GreaterThanEqual  | findByAgeGreaterThanEqual               | … where x.age >= ?1                      |
| After             | findByStartDateAfter                    | … where x.startDate > ?1                 |
| Before            | findByStartDateBefore                   | … where x.startDate < ?1                 |
| IsNull            | findByAgeIsNull                         | … where x.age is null                    |
| IsNotNull,NotNull | findByAge(Is)NotNull                    | … where x.age not null                   |
| Like              | findByFirstnameLike                     | … where x.firstname like ?1              |
| NotLike           | findByFirstnameNotLike                  | … where x.firstname not like ?1          |
| StartingWith      | findByFirstnameStartingWith             | … where x.firstname like ?1 (parameter bound with appended %) |
| EndingWith        | findByFirstnameEndingWith               | … where x.firstname like ?1 (parameter bound with prepended %) |
| Containing        | findByFirstnameContaining               | … where x.firstname like ?1 (parameter bound wrapped in %) |
| OrderBy           | findByAgeOrderByLastnameDesc            | … where x.age = ?1 order by x.lastname desc |
| Not               | findByLastnameNot                       | … where x.lastname <> ?1                 |
| In                | findByAgeIn(Collection ages)            | … where x.age in ?1                      |
| NotIn             | findByAgeNotIn(Collection age)          | … where x.age not in ?1                  |
| TRUE              | findByActiveTrue()                      | … where x.active = true                  |
| FALSE             | findByActiveFalse()                     | … where x.active = false                 |
| IgnoreCase        | findByFirstnameIgnoreCase               | … where UPPER(x.firstame) = UPPER(?1)    |

#### 使用JPQL查询

@Query 注解的使用非常简单，只需在方法上面标注该注解，同时提供一个JPQL查询语句即可，注意：

- 大多数情况下将*替换为别名
- 表名改为类名
- 字段名改为属性名
- 搭配注解@Query进行使用

```java
@Query("select 表别名 from 表名(实际为类名) 别名 where 别名.属性='itlils'")
public List<User> findUsers(); ////从实体类，查询，而不是表
@Query("select u from User u where u.username='itlils'")//从实体类，查询，而不是表。where不是列名，而是属性名
 List<User> findAllUserByUsername();
 @Query("select u.id from User u")//从实体类，查询，而不是表
    List<Integer> findAllUser2();
  @Query("select count(u) from User u")//从实体类，查询，而不是表
    List<Integer> findAllUser4();
 //修改数据 一定加上@Modifying 注解
    @Transactional
    @Modifying
    @Query("update User set username=?1 where id=?2")
    int updateUsernameById2(String username,Integer id);
```

使用原生sql

```java
@Query(value = "update user u set u.username=?1 where u.id=?2",nativeQuery = true)
```

#### 一对一关系

```java
@JoinColumn(name = "detail_id")
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //设置关联操作为ALL
AccountDetail detail;
```

关联级别cascade也是有多个,一般设置为all就行

- ALL：所有操作都进行关联操作
- PERSIST：插入操作时才进行关联操作
- REMOVE：删除操作时才进行关联操作
- MERGE：修改操作时才进行关联操作

#### 一对多关系

```java
@OneToMany:
   	作用：建立一对多的关系映射
    属性：
    	targetEntityClass：指定多的多方的类的字节码
    	mappedBy：指定从表实体类中引用主表对象的名称。
    	cascade：指定要使用的级联操作
    	fetch：指定是否采用延迟加载
    	orphanRemoval：是否使用孤儿删除
```

Author(一)和Article(多)表

1. JPA使用@OneToMany和@ManyToOne来标识一对多的双向关联。一端(Author)使用@OneToMany,多端(Article)使用@ManyToOne。

2. 一对多的双向关系由多端(Article)来维护。就是说多端(Article)为关系维护端，负责关系的增删改查。一端(Author)则为关系被维护端，不能维护关系。

3. 一端(Author)使用@OneToMany注释的mappedBy="author"属性表明Author是关系被维护端。

4. 多端(Article)使用@ManyToOne和@JoinColumn来注释属性 author,@ManyToOne表明Article是多端，@JoinColumn设置在article表中的关联字段(外键)。


```java
@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name="author_id")//设置在article表中的关联字段(外键)
    private Author author;//所属作者
    
    
 @OneToMany(mappedBy = "author",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="author"中的author是Article中的author属性
    private List<Article> articleList;//文章列表
```

1. 懒加载问题：fetch=FetchType.EAGER
2. 栈溢出（循环遍历）：	循环tostring 破坏一方的tostring即可。文章@ToString(exclude = {"author"})

#### 多对多关系

Jpa使用@ManyToMany来注解多对多的关系，由一个关联表来维护。这个关联表的表名默认是：主表名+下划线+从表名。(主表是指关系维护端对应的表,从表指关系被维护端对应的表)。这个关联表只有两个外键字段，分别指向主表ID和从表ID。字段的名称默认为：主表名+下划线+主表中的主键列名，从表名+下划线+从表中的主键列名。

注意

1. 多对多关系中一般不设置级联保存、级联删除、级联更新等操作。

2. 可以随意指定一方为关系维护端，在这个例子中，我指定 User 为关系维护端，所以生成的关联表名称为： user_authority，关联表的字段为：user_id 和 authority_id。

3. 多对多关系的绑定由关系维护端来完成，即由 User.setAuthorities(authorities) 来绑定多对多的关系。关系被维护端不能绑定关系，即Authority不能绑定关系。

4. 多对多关系的解除由关系维护端来完成，即由Authority.getUser().remove(user)来解除多对多的关系。关系被维护端不能解除关系，即Authority不能解除关系。

5. 如果 User 和 Authority 已经绑定了多对多的关系，那么不能直接删除 Authority，需要由 User 解除关系后，才能删除 Authority。但是可以直接删除 User，因为 User 是关系维护端，删除 User 时，会先解除 User 和 Authority 的关系，再删除 Authority。

   ```java
    @ManyToMany
       @JoinTable(name = "user_authority",joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "authority_id"))
       //1、关系维护端，负责多对多关系的绑定和解除
       //2、@JoinTable注解的name属性指定关联表的名字，joinColumns指定外键的名字，关联到关系维护端(User)
       //3、inverseJoinColumns指定外键的名字，要关联的关系被维护端(Authority)
       //4、其实可以不使用@JoinTable注解，默认生成的关联表名称为主表表名+下划线+从表表名，
       //即表名为user_authority
       //关联到主表的外键名：主表名+下划线+主表中的主键列名,即user_id
       //关联到从表的外键名：主表中用于关联的属性名+下划线+从表的主键列名,即authority_id
       //主表就是关系维护端对应的表，从表就是关系被维护端对应的表
       private List<Authority> authorityList;

       @ManyToMany(mappedBy = "authorityList")
       private List<Users> userList;
   ```