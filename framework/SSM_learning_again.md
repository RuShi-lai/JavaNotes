#### 初识SSM框架

SSM：标准的模型-视图-控制器(Model-View-Controller)模式。系统分为视图层(View)、控制层(Controller)、业务逻辑层(Service)、数据访问层(DAO)。Spring MVC负责请求转发和视图管理、Spring实现业务对象管理、MyBatis  完成数据持久化。

##### 	Spring

IOC:  

- 传统：A a = new A() ——B b = new B(a) ——C c = new (b)  创建C需要依次创建A、B。
- IOC：创建A，发现其依赖B，创建B，发现其依赖于C，创建C(由容器管理，不知道先构建谁，要什么先构建什么)，对象依赖的其它对象可以通过被动的方式传递进来，而不是这个对象自己创建或查找依赖对象。

AOP

- 分离应用的业务层与系统级服务（例如事务管理）开发，将那些与业务无关，却为业务模块所公用的逻辑封装起来，减少重复代码，降低耦合。

##### 	MyBaits

基于Java持久层框架，支持定制化SQL，存储过程和高级映射，可以使用简单xm或l注释来配置原生信息，将接口和Java的POJO映射成数据库中的记录。

##### 	SpringMVC

将Web进行层与层解耦

#### Spring

​	