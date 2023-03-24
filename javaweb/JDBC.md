#### JDBC概述

1. JDBC（Java DataBase Connectivity），即Java数据库连接技术。它是一套用于访问【关系型数据库】的应用程序API，由一组用Java语言编写的【类和接口】组成。

2. JDBC是一种规范，它由Sum公司（Oracle）提供了一套完整的**接口**。JDBC规范提供的接口存在于java.sql包中

3. 不同的数据库厂商只需要【按照JDBC规范】提供的api接口进行各自的实现，程序员只需要【面向接口和规范】编程，不需要关心具体的实现。
4. Mysql提供的【JDBC实现】称为Mysql Connector,不同的数据库版本需要使用不同的Connector。实际开发时根据数据库版本、JDK版本、选择不同的Connector。


#### 获取连接

| 接口                | 作用                    |
| ----------------- | --------------------- |
| Driver            | 驱动接口                  |
| DriverManager     | 工具类，用于管理驱动，可以获取数据库的链接 |
| Connection        | 表示Java与数据库建立的连接对象（接口） |
| PreparedStatement | 发送SQL语句的工具            |
| ResultSet         | 结果集，用于获取查询语句的结果       |

##### 	驱动

- java.sql.Driver 接口是所有【驱动程序】需要实现的接口。这个接口是提供给数据库厂商使用的，不同数据库厂商提供不同的实现。
- 在程序中不需要直接去访问实现了 Driver 接口的类，而是由驱动程序管理器类(java.sql.DriverManager)去调用这些Driver实现。

加载和注册驱动

加载驱动：我们需要将数据的的驱动实现类加载到JVM中，实现这个目的我们可以使用 Class 类的静态方法 forName()，向其传递要加载的驱动的类名Class.forName(“com.mysql.cj.jdbc.Driver”)。				DriverManager 类是驱动程序管理器类，负责管理驱动程序。

```java
//加载驱动
Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
//创建驱动
Driver driver = (Driver) clazz.newInstance();
//注册驱动
DriverManager.registerDriver(driver);
```

##### 	URL

统一资源定位符，又叫做网页地址，是互联网上标准的资源的地址。URL 用于标识一个被注册的驱动程序，从而建立到数据库的连接。

URL的标准由三部分组成，各部分间用冒号分隔：

- 协议：java的连接URL中的协议总是jdbc 。
- 子协议：子协议用于标识一个数据库驱动程序。
- 子名称：一种标识【数据库】的方法。子名称作用是为了【定位数据库】。其包含【主机名】(对应服务端的ip地址)，【端口号】，【数据库名】。

编写：jdbc:mysql://主机名称:mysql服务端口号/数据库名称?参数=值&参数=值serverTimezone=Asia/Shanghai。

通常一个高版本的mysql的url还会包含以下三个参数：

```reStructuredText
useUnicode=true&characterEncoding=utf8&useSSL=false
```

useUnicode=true&characterEncoding=UTF-8的作用是：指定字符的编码、解码格式。						比如：若mysql数据库用到 是GBK编码方式，而项目数据用的是UTF-8编码方式。这时如果添加了 "useUnicode =true&characterEncoding=UTF-8"，则在存取数据时根据mysql和项目的编码方式将数据进行相应的格式转化。即：

- 存数据：数据库在存放项目数据的时候会先用UTF-8格式将数据解码成字节码，然后再将解码后的字节码重新使用GBK编码，并存放到数据库中。
- 取数据：在数据库中取数据的时候，数据库会先将数据库中的数据按GBK格式解码成字节码，然后再将解码后的字节码重新按UTF-8格式编码数据，最后再将数据返回给客户端。

MySQL5.7之后要加上useSSL=false，mysql5.7以及之前的版本则不用进行添加useSSL=false，会默认为false。

- useSSL=true：就是一般通过证书或者令牌进行安全验证
- useSSL=false：就是通过账号密码进行连接

##### 获取连接

```java
@Test
public void testConnection1() throws Exception{
    //1.数据库连接的4个基本要素：
    String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    String user = "root";
    String password = "root";

  /*8.0之后名字改了  com.mysql.cj.jdbc.Driver
    String driverName = "com.mysql.cj.jdbc.Driver";
    //2.实例化Driver
    Class clazz = Class.forName(driverName);
    Driver driver = (Driver) clazz.newInstance();
    //3.注册驱动
    DriverManager.registerDriver(driver);*/
   
     //2.加载驱动 （①实例化Driver ②注册驱动）
    Class.forName(driverName);  
  
    //3.获取连接
    Connection conn = DriverManager.getConnection(url, user, password);
    System.out.println(conn);
}
```

```java
public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
```

#### 常用api

##### 	操作和访问数据库

一个数据库连接就是一个Socket连接，数据库连接被用于向数据库服务器发送命令和 SQL 语句，并接受数据库服务器返回的结果。

在 java.sql 包中有 3 个接口分别定义了对数据库的调用的不同方式：

- Statement：用于执行静态 SQL 语句并返回它所生成结果的对象。
- PreparedStatement：语句被预编译并存储在此对象中，可以使用此对象多次高效地执行该语句。
- CallableStatement：用于执行 SQL 存储过程

  ##### Statement

通过调用 Connection 对象的 createStatement() 方法创建该对象。该对象用于执行静态的 SQL 语句，并且返回执行结果。

使用Statement操作数据表存在弊端：

- 问题一：存在拼串操作，繁琐
- 问题二：存在SQL注入问题

![](https://www.ydlclass.com/doc21xnv/assets/image-20220818153245059-eba3d248.png)

```java
        statement = conn.createStatement();
        resultSet = statement.executeQuery(sql);
        //使用遍历获取数据
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String pwd = resultSet.getString("password");
            String birthday = resultSet.getString("birthday");
            System.out.println("id="+id);
            System.out.println("username=" + username);
            System.out.println("password=" + pwd);
            System.out.println("birthday=" + birthday);
        }
```

```java
 public static void closeAll(Connection connection, Statement statement, ResultSet rs){
       if(connection != null){
           try {
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       if(statement != null){
           try {
               statement.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       if( rs != null ){
           try {
               rs.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
```

sql注入问题

SQL 注入：是利用某些系统没有对用户输入的数据进行充分的检查，而在用户输入数据中注入非法的 SQL 语句段或命令（如：SELECT user, password FROM user_table WHERE user='a' OR 1 = ' AND password = ' OR '1' = '1') ，从而利用系统的 SQL 引擎完成恶意行为的做法。

##### 	PreparedStatement

![](https://www.ydlclass.com/doc21xnv/assets/image-20220811153104418-9713c012.png)

通常我们发送一条SQL语句给MySQL服务器时，MySQL服务器每次都需要对这条SQL语句进行校验、解析等操作，SQL语句如果每次都需要进行校验、解析等操作，未免太过于浪费性能了，因此产生了SQL语句的预编译。

预编译：就是将一些灵活的参数值以占位符`?`的形式给代替掉，我们把参数值给抽取出来，把SQL语句进行模板化。让MySQL服务器执行相同的SQL语句时，不需要在校验、解析SQL语句上面花费重复的时间。

PreparedStatement

- 通过调用 Connection 对象的 【preparedStatement(String sql)】方法获取 PreparedStatement对象
- PreparedStatement 接口是 Statement 的子接口，它表示一条【预编译】过的 SQL 语句
- PreparedStatement 对象所代表的 SQL 语句中的参数用问号(?)来表示，调用 PreparedStatement 对象的 setXxx() 方法来设置这些参数。

```java
@Test
public void testQuery3()  {

    // 1、定义资源
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement statement = null;
    String sql = "select * from user where id = ?";

    try {
        // 获取连接
        connection = DBUtil.getConnection();
        // 获取使用预编译的statement
        statement = connection.prepareStatement(sql);
        statement.setInt(1,1);
        // 获取结果集
        resultSet = statement.executeQuery();
        // 封装结果
        List<User> users = new ArrayList<>();
        while (resultSet.next()){
            User user = new User();
            int id = resultSet.getInt(1);
            String username = resultSet.getString(2);
            String password = resultSet.getString(3);
            Date date = resultSet.getDate(4);
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setDate(date);
            users.add(user);
        }
        System.out.println(users);
    } catch (SQLException e){
        e.printStackTrace();
    } finally {
        // 关闭资源
        DBUtil.closeAll(connection,statement,resultSet);
    }

}
```

当使用不同的PreparedStatement对象来执行相同的SQL语句时，还是会出现编译两次的现象，这是因为驱动没有缓存编译后的函数key，会二次编译。如果希望缓存编译后函数的key，那么就要设置`cachePrepStmts参数为true`，如上url的参数。url添加了参数之后才能保证mysql驱动先把SQL语句发送给服务器进行预编译，然后在执行executeQuery()时只是把参数发送给服务器。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220818200720649-b7a5ccc8.png)

防止sql注入

使用PreparedStatement 可以防止 SQL 注入，其根本原因是因为mysql已经对使用了占位符的sql语句进行了预编译，执行计划中的条件已经确定，不能额外在添加其他条件，从而避免了sql注入。

preparedStatement优点

- sql的可读性更强，参数更加灵活更加面向对象，不再是简单的拼接字符串。
- sql会进行预编译，性能高，可以进行重复利用。
- sql的预编译同样可以防止sql注入。

#### 	数据库事务

事务处理可以用来维护数据库的完整性，保证成批的 SQL 语句要么全部执行，要么全部不执行。在jdbc中的事务是使用connection的commit方法和rollback方法来管理的。

在jdbc中事务的默认提交时机，如下：

- 当一个连接对象被创建时，默认情况下是【自动提交事务】，每次执行一个 SQL 语句时，如果执行成功，就会向数据库自动提交，此操作不能回滚。
- 关闭数据库连接，数据就会【自动提交】。如果多个操作（多条sql语句），每个操作使用的是自己单独的连接（connection），则无法保证事务。【同一个事务】的【多个操作】必须在【同一个连接】下。

在jdbc中使用使用的基本步骤：

1. 调用 Connection 对象的 setAutoCommit(false) 以取消自动提交事务
2. 在所有的 SQL 语句都成功执行后，调用 commit()方法提交事务
3. 在出现异常时，调用 rollback()方法回滚事务
4. 若此时 Connection 没有被关闭，还可能被重复使用，则需要恢复其自动提交状态 setAutoCommit(true)

```java
@Override
public int transfer(int id,int money,Connection connection) {
    // 1、定义资源
    //        Connection connection = null;
    PreparedStatement statement = null;
    String sql = "update `user` set balance = balance + ? where id =?";

    try {
        // 获取statement
        statement = connection.prepareStatement(sql);
        statement.setInt(1,money);
        statement.setInt(2,id);
        // 获取结果集
        return statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
    } finally {
        // 关闭资源
        DBUtil.closeAll(null, statement, null);
    }
}

@Override
public Boolean transferAccount(int formId, int toId, int money) {

    Connection connection = DBUtil.getConnection();

    try {
        connection.setAutoCommit(false);
        userDao.transfer(formId,money,connection);
        //            int i = 1/0;
        userDao.transfer(toId,-money,connection);
        connection.commit();
    }catch (SQLException e){
        e.printStackTrace();
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } finally {
        DBUtil.closeAll(connection,null,null);
    }

    return null;
}
```

所有的方法里传递一个connection对象，不是一个好的选择，我们知道一个业务通常是在一个线程中执行的，那么我们多个方法的共享变量能不能方法线程中呢，我们学习过的threadlocal就可以解决这个问题：

```java
public class DBUtil {

    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static Connection getConnection(){
        // 首先从threadLocal中获取
        Connection conn = threadLocal.get();
        // 如果没有就创建
        if(conn == null) {
            try {
                // 1、定义要素
                Properties properties = new Properties();
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties"));

                // 2、加载驱动，实例化驱动
                Class<?> driverName = Class.forName(properties.getProperty("driverName"));
                Constructor<?> constructor = driverName.getConstructor();
                Driver driver = (Driver) constructor.newInstance();

                // 3、注册驱动
                DriverManager.registerDriver(driver);
                //4、 获取连接
                conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
                if (conn == null) {
                    throw new RuntimeException("连接获取异常！");
                }
                // 创建完成以后，加入threadLocal
                threadLocal.set(conn);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("链接获取异常！");
            }
        }
        return conn;

    }
}

public class Transaction {
    /**
     * 开启事务
     */
    public static void begin() {
        Connection connection = DBUtil.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交的方法
     */
    public static void commit() {
        Connection connection = DBUtil.getConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        Connection connection = DBUtil.getConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void close() {
        Connection connection = DBUtil.getConnection();
        try {
            connection.setAutoCommit(true);
            connection.close();
            // 关闭连接之后再删除线程中的连接
            DBUtil.threadLocal.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

@Override
public Boolean transferAccount(int formId, int toId, int money) {

    Transaction.begin();
    try {

        userDao.transfer(formId,money);
        userDao.transfer(toId,-money);

        Transaction.commit();
        return true;
    } catch (Exception e){
        e.printStackTrace();
        Transaction.rollback();
    } finally {
        Transaction.close();
    }
    return false;
}

// 关闭所有的资源
public static void closeAll(Connection connection, Statement statement, ResultSet resultSet){
    if(connection != null) {
        try {
            connection.close();
            // 当连接关闭一定要清除threadLocal内保存的连接
            DBUtil.threadLocal.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}        
```

#### 数据库连接池

##### 	连接池概述

传统的jdbc开发形式存在的问题:

- 普通的JDBC数据库连接使用【DriverManager】来获取，每次向数据库建立连接的时候都要将 【Connection】加载到内存中，再验证用户名和密码（保守估计需要花费0.05s～1s的时间）。
- 需要【数据库连接】的时候，就向数据库申请一个，执行完成后再【断开连接】。这样的方式将会消耗大量的资源和时间。数据库的连接资源并没有得到很好的重复利用。若同时有几百人甚至几千人在线，频繁的进行数据库连接操作将占用很多的系统资源，严重的甚至会造成服务器的崩溃。
- 对于每一次数据库连接，使用完后都得断开。否则，如果程序出现异常而未能关闭，将会导致数据库系统中的内存泄漏，最终将导致重启数据库。（回忆：何为Java的内存泄漏？）
- 这种开发方式不能控制【被创建的连接对象数】，系统资源会被毫无顾及的分配出去，如连接过多，也可能导致内存泄漏，服务器崩溃。

数据库连接池技术

- 数据库连接池的基本思想：就是为数据库连接建立一个“缓冲池”。预先在缓冲池中放入一定数量的连接，当需要建立数据库连接时，只需从“缓冲池”中取出一个，使用完毕之后再放回去。
- 数据库连接池负责分配、管理和释放数据库连接，它允许应用程序【重复使用一个现有的数据库连接】，而不是重新建立一个。
- 数据库连接池在初始化时将【创建一定数量】的数据库连接放到连接池中。无论这些连接是否被使用，连接池都将一直保证至少拥有一定量的连接数量。连接池的【最大数据库连接数】限定了这个连接池能占有的最大连接数，当应用程序向连接池请求的连接数超过最大连接数量时，这些请求将被加入到等待队列中。

优点

- 资源重用：由于数据库连接得以重用，避免了频繁创建，释放连接引起的大量性能开销。在减少系统消耗的基础上，另一方面也增加了系统运行环境的平稳性。
-   更快的系统反应速度：数据库连接池在初始化过程中，往往已经创建了若干数据库连接置于连接池中备用。 此时连接的初始化工作均已完成。对于业务请求处理而言，直接利用现有可用连接，避免了数据库连接初始化和释放过程的时间开销，从而减少了系统的响应时间。
- 新的资源分配手段：对于多应用共享同一数据库的系统而言，可在应用层通过数据库连接池的配置，实现某一应用最大可用数据库连接数的限制，避免某一应用独占所有的数据库资源。
- 统一的连接管理，避免数据库连接泄漏：在较为完善的数据库连接池实现中，可根据预先的占用超时设定，强制回收被占用连接，从而避免了常规数据库连接操作中可能出现的资源泄漏

数据库连接池

【DataSource】通常被称为【数据源】，它包含【连接池】和【连接池管理组件】两个部分，习惯上也经常把 DataSource 称为连接池。																																																																						【DataSource】用来取代DriverManager来获取Connection，获取速度快，同时可以大幅度提高数据库访问速度。DataSource同样是jdbc的规范，针对不通的连接池技术，我们可以有不同的实现。

- 数据源和数据库连接不同，数据源无需创建多个，它是产生数据库连接的工厂，通常情况下，一个应用只需要一个数据源，当然也会有多数据源的情况。
- 当数据库访问结束后，程序还是像以前一样关闭数据库连接：conn.close(); 但conn.close()并没有关闭数据库的物理连接，它仅仅把数据库连接释放，归还给了数据库连接池。

##### 	连接池技术

Druid（德鲁伊）

```java
@Test
public void testConn() throws SQLException {
    // 1、创建数据源
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ydlclass?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false");
    dataSource.setUsername("root");
    dataSource.setPassword("root");

    Connection connection = dataSource.getConnection();
    System.out.println(connection);

}

public class TestDruid {
    public static void main(String[] args) throws Exception {
        Properties pro = new Properties();		 pro.load(TestDruid.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        Connection conn = ds.getConnection();
        System.out.println(conn);
    }
}

druid.driverClassName=com.mysql.cj.jdbc.Driver
druid.url=jdbc:mysql://127.0.0.1:3306/ydlclass?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false
druid.username=root
druid.password=root
druid.initialSize=10
druid.minIdle=20
druid.maxActive=50
druid.maxWait=500
    
# 1、初始化时建立物理连接的个数 默认10
# 2、最小连接池数量  默认20
# 2、最大连接池数量  默认50
# 3、获取连接时最大等待时间，单位毫秒。
```

关闭连接：connection.close()            实际        recycle()  ---->   dataSource.recycle(this);

Hikari

HiKariCP是数据库连接池的一个后起之秀，日语中“光”的意思，号称历史上最快的数据库连接池，可以完美地PK掉其他连接池，是一个高性能的`JDBC`连接池，在后边学习的springboot中默认集成了该连接池

```java
public class TestHiKari {

    @Test
    public void getConnection() throws SQLException {
        // 创建数据源
        HikariDataSource dataSource = new HikariDataSource();
        // 配置数据源
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ydlclass?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.getConnection();
    }

    @Test
    public void getConnection2() throws SQLException {
        // 创建数据源
        // 默认不是classpath，/
        this.getClass().getResourceAsStream("");
        // 默认就是classpath
        this.getClass().getClassLoader().getResourceAsStream("");
        HikariConfig hikariConfig = new HikariConfig("/hikari.properties");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        System.out.println(dataSource.getConnection());
    }
}

jdbcUrl=jdbc:mysql://localhost:3306/ydlclass?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
username=root
password=root
driverClassName=com.mysql.cj.jdbc.Driver

idleTimeout=600000
connectionTimeout=30000
minimumIdle=10
maximumPoolSize=60

# 1、保持连接的最大时长，比如连接多了，最小连接数不够用，就会继续创建，比如又创建了10个，如果这时没有了业务，超过该设置的时间，新创建的就会被关闭
# 2、连接的超时时间
# 3、连接池最少的连接数
# 4、连接池最大的连接数
```

```java
public class DBUtil {

    private final static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 创建数据源
    private final static DataSource dataSource;
    // 初始化
    static {
        HikariConfig hikariConfig = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection(){
        // 首先从threadLocal中获取
        Connection conn = threadLocal.get();
        // 如果没有就创建
        if(conn == null) {
            try{
                conn = dataSource.getConnection();
                if (conn == null) {
                    throw new RuntimeException("连接获取异常！");
                }
                // 创建完成以后，加入threadLocal
                threadLocal.set(conn);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("链接获取异常！");
            }
        }
        return conn;

    }
    
        // 关闭所有的资源
    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet){
        if(connection != null) {
            try {
                connection.close();
                DBUtil.threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
```

#### BaseDao

```java
public class BaseDaoImpl<T> implements BaseDao<T> {

    // 写一个通用的insert
    public int insert(T t) {
        // 1、定义资源
        Connection connection = null;
        PreparedStatement statement = null;
        // sql需要拼接，需要获取传入的实例的字段
        // insert into user (username,password,birthday) values (?,?,?)
        Class<?> target = t.getClass();
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(target.getSimpleName().toLowerCase(Locale.ROOT)).append(" (");
        // insert into user
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].getName());
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        // insert into user (username,password,birthday
        sb.append(") values (");
        for (int i = 0; i < fields.length; i++) {
            sb.append("?");
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        sb.append(");");
        System.out.println(sb);

        try {
            // 获取连接
            connection = DBUtil.getConnection();
            // 获取statement
            statement = connection.prepareStatement(sb.toString());
            // 替换占位符
            for (int i = 1; i <= fields.length; i++) {
                fields[i - 1].setAccessible(true);
                statement.setObject(i, fields[i - 1].get(t));
            }

            return statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        } finally {
            // 关闭资源
            DBUtil.closeAll(connection, statement, null);
        }

    }

    // 写一个通用的delete,可以根据任意一个字段删除
    public int delete(String fieldName, String value, Class target) {
        // 1、定义资源
        Connection connection = null;
        PreparedStatement statement = null;
        // sql需要拼接，需要获取传入的实例的字段
        // delete from user where id = 1
        StringBuilder sb = new StringBuilder("delete from ");
        // 我们做了简单处理，复杂逻辑不去判断
        sb.append(target.getSimpleName().toLowerCase(Locale.ROOT)).append(" where ")
                .append(fieldName).append(" = ?");
        Field[] fields = target.getDeclaredFields();
        // 判断传入的字段名字是否正确
        boolean match = Arrays.stream(fields).anyMatch(item ->
                item.getName().toLowerCase(Locale.ROOT)
                        .equals(fieldName.toLowerCase(Locale.ROOT)));
        if(!match) throw new RuntimeException("您传入的字段非法！");

        System.out.println(sb);

        try {
            // 获取连接
            connection = DBUtil.getConnection();
            // 获取statement
            statement = connection.prepareStatement(sb.toString());
            // 只支持一个占位符
            statement.setObject(1, value);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            // 关闭资源
            DBUtil.closeAll(connection, statement, null);
        }

    }


    // 写一个通用的queryById
    public T queryById(int id,Class target) {
        // 1、定义资源
        Connection connection = null;
        PreparedStatement statement = null;
        // sql需要拼接，需要获取传入的实例的字段
        // insert into user (username,password,birthday) values (?,?,?)
        StringBuilder sb = new StringBuilder("select ");
        // insert into user
        Field[] fields = target.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].getName());
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        sb.append(" from ").append(target.getSimpleName().toLowerCase(Locale.ROOT))
                .append(" where id = ?");
        System.out.println(sb);

        try {
            // 获取连接
            connection = DBUtil.getConnection();
            // 获取statement
            statement = connection.prepareStatement(sb.toString());

            statement.setObject(1,id);

            // 获取结果集
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                // 创建一个对象
                Constructor constructor = target.getConstructor();
                Object instance = constructor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(instance,resultSet.getObject(fields[i].getName()));
                }
                return (T)instance;
            }
            return null;
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭资源
            DBUtil.closeAll(connection, statement, null);
        }

    }

    public ArrayList<T> queryAll(Class target) {
        // 1、定义资源
        Connection connection = null;
        PreparedStatement statement = null;
        // sql需要拼接，需要获取传入的实例的字段
        // insert into user (username,password,birthday) values (?,?,?)
        StringBuilder sb = new StringBuilder("select ");
        // insert into user
        Field[] fields = target.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].getName());
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        sb.append(" from ").append(target.getSimpleName().toLowerCase(Locale.ROOT));
        System.out.println(sb);

        try {
            // 获取连接
            connection = DBUtil.getConnection();
            // 获取statement
            statement = connection.prepareStatement(sb.toString());

            // 获取结果集
            ResultSet resultSet = statement.executeQuery();

            ArrayList<T> list = new ArrayList<>();

            while (resultSet.next()){
                // 创建一个对象
                Constructor constructor = target.getConstructor();
                Object instance = constructor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(instance,resultSet.getObject(fields[i].getName()));
                }
                list.add((T)instance);
            }
            return list;
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭资源
            DBUtil.closeAll(connection, statement, null);
        }

    }


    // 写一个通用的update
    public int update(T t) {
        // 1、定义资源
        Connection connection = null;
        PreparedStatement statement = null;
        // sql需要拼接，需要获取传入的实例的字段
        // update user set username=?,password=? where id = ?
        Class<?> target = t.getClass();
        StringBuilder sb = new StringBuilder("update ");
        sb.append(target.getSimpleName().toLowerCase(Locale.ROOT)).append(" set ");
        // update user set
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].getName()).append(" = ?");
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        // update user set username=?,password=?
        sb.append(" where ").append(fields[0].getName()).append(" = ?");
        System.out.println(sb);

        try {
            // 获取连接
            connection = DBUtil.getConnection();
            // 获取statement
            statement = connection.prepareStatement(sb.toString());
            // 替换占位符
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            fields[0].setAccessible(true);
            statement.setObject(fields.length,fields[0].get(t));

            return statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        } finally {
            // 关闭资源
            DBUtil.closeAll(connection, statement, null);
        }
    }

}
```

