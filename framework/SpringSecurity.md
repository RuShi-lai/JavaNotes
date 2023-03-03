##### SpringSecurity核心功能

1. 认证：验证当前访问的用户是不是本系统中的用户。确定是哪一个具体的用户。
2. 鉴权：经过认证，判断当前登陆用户有没有权限来执行某个操作。
3. security自带登录和退出页面

#### 认证

##### 	web登录流程

![img](https://www.ydlclass.com/doc21xnv/assets/image-20220820185333664.af9918b3.png)

springsecurity通过一些过滤器、拦截器，实现登陆鉴权，内置关于springsecurity的16的过滤器。

核心过滤器：

- **UsernamePasswordAuthenticationFilter**：处理我们登陆页面输入的用户名和密码是否正确的过滤器。
- **ExceptionTranslationFilter**：处理前面的几个过滤器中，有了问题，抛出错误，不让用户登录。
- **FilterSecurityInterceptor**：经行一个权限校验的拦截器。

security运行机制

![](https://www.ydlclass.com/doc21xnv/assets/image-20220821175458416.57c8e429.png)

自定义登录

![](https://www.ydlclass.com/doc21xnv/assets/image-20220822014000886.f37aa999.png)

##### JWT简介

JSON Web Token（JWT）是一个非常轻巧的规范。这个规范允许我们使用JWT在用户和服务器之间传递安全可靠的信息。无状态。实际上是一个字符串，由头部、荷载、与签名组成

1. 头部(Header)：用于描述关于该JWT的最基本的信息，例如其类型以及签名所用的算法等。这也可以被表示成一个JSON对象。{"typ":"JWT","alg":"HS256"}
2. 荷载(playload)：载荷就是存放有效信息的地方。{"sub":"1234567890","name":"itlils","admin":true,"age":18}
3. 签证(signature) = header (base64后的) + payload (base64后的) +secret   base64加盐
4. 将这三部分用.连接成一个完整的字符串,构成了最终的jwt:


JJWT：提供端到端的JWT创建和验证的Java库

```java
//创建token   
public static void main(String[] args) {
                JwtBuilder jwtBuilder = Jwts.builder()
                        .setId("666")//设置id
                        .setSubject("testJwt")//主题
                        .setIssuedAt(new Date())//签发日期
                        .signWith(SignatureAlgorithm.HS256, "itlils"); //HS256算法  itlils盐
                String jwt = jwtBuilder.compact();
                System.out.println(jwt);
        }
//解析token 创建了token ，在web应用中这个操作是由服务端进行然后发给客户端，客户端在下次向服务端发送请求时需要携带这个token（这就好像是拿着一张门票一样），那服务端接到这个token 应该解析出token中的信息（例如用户id）,根据这些信息查询数据库返回相应的结果。
Claims claims = Jwts.parser().setSigningKey("itlils").parseClaimsJws(jwt).getBody();
System.out.println(claims);
//设置过期时间
JWts.builder().setExpiration(date)//过期时间
//自定义claims
 JWts.builder().claim("userId","123")
```

如果要测试，需要往用户表中写入用户数据，并且如果你想让用户的密码是明文存储，需要在密码前加{noop}

例如{noop}ydlclass

##### 密码加密存储

默认使用的PasswordEncoder要求数据库中的密码格式为：{id}password 。它会根据id去判断密码的加密方式

一般使用SpringSecurity为我们提供的BCryptPasswordEncoder。

##### 认证过滤器

OncePerRequestFilter 只走一次，请求前

#### 授权

##### 作用

​        不同用户可以使用不同功能   

注解去指定访问对应的资源所需的权限开启配置

```
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PreAuthorize("hasAuthority('sayhello')")
```

##### 从数据库中查询信息

RBAC权限模型（Role-Based Access Control）即：基于角色的权限控制。这是目前最常被开发者使用也是相对易用、通用权限模型。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220822050940245.854bb6a9.png)

#### 自定义失败处理

ExceptionTranslationFilter捕获，它会判断是认证失败和授权失败。

- 认证失败：它会封装AuthenticationException，然后调用**AuthenticationEntryPoint**的commence方法处理
- 授权失败：它会封装AccessDeniedException，然后调用**AccessDeniedHandler**的handle方法处理

#### 跨域

##### 什么是跨域

1. 什么是跨域问题：**浏览器**的**同源策略**，导致不能向其他域名发送**异步**请求。

2. ##### 同源策略：具有相同的协议（protocol），主机（host）和端口号（port）

##### CORS

​	CORS:跨域资源共享

​        本质：请求头增加一个参数，开启跨域请求。

##### CSRF

​	CSRF是指跨站请求伪造（Cross-site request forgery)

![](https://www.ydlclass.com/doc21xnv/assets/image-20220822081610164.2581bc4b.png)

​ SpringSecurity去防止CSRF攻击的方式就是通过csrf_token。后端会生成一个csrf_token，前端发起请求的时候需要携带这个csrf_token,后端会有过滤器进行校验，如果没有携带或者是伪造的就不允许访问。

