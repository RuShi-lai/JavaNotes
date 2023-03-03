#### Mysql数据库

##### 	数据库概念

- 数据库是【按照数据结构来组织、存储和管理数据的仓库】。是一个长期存储在计算机内的、有组织的、可共享的、统一管理的大量数据的集合。

##### 	Mysql

- MySQL是一种关系型数据库管理系统，关系数据库将数据保存在不同的表中，而不是将所有数据放在一个大仓库内，这样就增加了速度并提高了灵活性。

##### 	基本概念

- MySQL是一种关系型数据库管理系统，关系数据库将数据保存在不同的表中，而不是将所有数据放在一个大仓库内，这样就增加了速度并提高了灵活性。
- 表（TABLE）是数据库中用来存储数据的对象，是有结构的数据的集合，是整个数据库系统的基础。


#### SQL

##### 	分类

- DCL(Data Control Language)：数据控制语言，用来定义访问权限和安全级别。
- DDL(Data Definition Language)：数据定义语言，用来定义数据库对象：库、表、列等。功能：创建、删除、修改库和表结构。
- DML(Data Manipulation Language)：数据操作语言，用来定义数据库记录：增、删、改表记录。
- DQL(Data Query Language)：数据查询语言，用来查询记录。也是本章学习的重点

##### 	DCL

```sql
create user 用户名@IP地址 identified by ‘密码’;
set password for zn@'%' = 'newpwd';
-- 语法：`grant 权限1，…，权限n on 数据库.* to 用户名@IP地址;
grant all on `ydlclass`.`user` to 'ydl'@'%';
-- 语法：revoke 权限1，…，权限n on 数据库.* from 用户名@ ip地址;
revoke all on `ydlclass`.`user` from 'ydl'@'%';
-- 语法：show grants for 用户名@ip地址;
show grants for 'ydl'@'%';
-- 语法：drop user 用户名@ip地址;
drop user 'ydl'@'%';
```

##### 	DDL

常用数据类型

> 整型

| MySQL数据类型 | 含义（有符号）                        |
| --------- | ------------------------------ |
| tinyint   | 1字节，范围（-128~127）               |
| smallint  | 2字节，范围（-32768~32767）           |
| mediumint | 3字节，范围（-8388608~8388607）       |
| int       | 4字节，范围（-2147483648~2147483647） |
| bigint    | 8字节，范围（+-9.22*10的18次方）         |

> 浮点型

| MySQL数据类型     | 含义                                     |
| ------------- | -------------------------------------- |
| float(m, d)   | 4字节，单精度浮点型，m总长度，d小数位                   |
| double(m, d)  | 8字节，双精度浮点型，m总长度，d小数位                   |
| decimal(m, d) | decimal是存储为字符串的浮点数，对应我们java的Bigdecimal |

> 字符串数据类型

| MySQL数据类型  | 含义                        |
| ---------- | ------------------------- |
| char(n)    | 固定长度，最多255个字符             |
| varchar(n) | 可变长度，最大容量65535个字节         |
| tinytext   | 可变长度，最大容量255个字节           |
| text       | 可变长度，最大容量65535个字节         |
| mediumtext | 可变长度，最大容量2的24次方-1个字节 16MB |
| longtext   | 可变长度，最大容量2的32次方-1个字节 4GB  |

**（1）char和varchar的区别：**

- char类型是【定长】的类型，即当定义的是char(10)，输入的是"abc"这三个字符时，它们占的空间一样是10个字符，包括7个空字节。当输入的字符长度超过指定的数时，char会截取超出的字符。而且，当存储char值时，MySQL是自动删除输入字符串末尾的空格。
- char是适合存储很短的、一般固定长度的字符串。例如，char非常适合存储密码的MD5值，因为这是一个定长的值。对于非常短的列，char比varchar在存储空间上也更有效率。
- varchar(n)类型用于存储【可变长】的，长度最大为n个字符的可变长度字符数据。比如varchar(10), 然后输入abc三个字符，那么实际存储大小为3个字节。除此之外，varchar还需要使用1或2个额外字节记录字符串的长度，如果列的最大长度小于等于255字节（是定义的最长长度，不是实际长度），则使用1个字节表示长度，否则使用2个字节来表示。n表示的是最大的
- char类型每次修改的数据长度相同，效率更高，varchar类型每次修改的数据长度不同，效率更低。

**（2）varchar和text**

- text不能设置默认值，varchar可以，这个我们在后边再看。
- text类型，包括（MEDIUMTEXT，LONGTEXT）也受单表 65535 最大行宽度限制，所以他支持溢出存储，只会存放前 768 字节在数据页中，而剩余的数据则会存储在溢出段中。虽然 text 字段会把超过 768 字节的大部分数据溢出存放到硬盘其他空间，看上去是会更加增加磁盘压力。但从处理形态上来讲 varchar 大于 768 字节后，实质上存储和 text 差别不是太大了。因为超长的 varchar 也是会用到溢出存储，读取该行也是要去读硬盘然后加载到内存，基本认为是一样的。
- 根据存储的实现：可以考虑用 varchar 替代 text，因为 varchar 存储更弹性，存储数据少的话性能更高。
- 如果存储的数据大于64K，就必须使用到 mediumtext，longtext，因为 varchar 已经存不下了。
- 如果 varchar(255+) 之后，和 text 在存储机制是一样的，性能也相差无几。

> 日期和时间数据类型

| MySQL数据类型 | 含义                              |
| --------- | ------------------------------- |
| date      | 3字节，日期，格式：2014-09-18            |
| time      | 3字节，时间，格式：08:42:30              |
| datetime  | 8字节，日期时间，格式：2014-09-18 08:42:30 |
| timestamp | 4字节，自动存储记录修改的时间                 |
| year      | 1字节，年份                          |

建表约束

| 约束名称        | 描述                    |
| ----------- | --------------------- |
| NOT NULL    | 非空约束                  |
| UNIQUE      | 唯一约束，取值不允许重复,         |
| PRIMARY KEY | 主键约束（主关键字），自带非空、唯一、索引 |
| DEFAULT     | 默认值（缺省值）              |
| FOREIGN KEY | 外键约束（外关键字）            |

FOREIGN KEY约束

外键维护的表与表之间的关系，他规定了当前列的数据必须来源于一张其他表的某一列中的主键：

外键会产生的效果

1、删除表时，如果不删除引用外键的表，被引用的表不能直接删除

2、外键的值必须来源于引用的表的主键字段

FOREIGN KEY [column list] REFERENCES [primary key table] ([column list]);

对表的修改

```sql
desc authors;
alter table author add (hobby varchar(20),address varchar(50));
alter table author modify address varchar(100);
alter table author change address addr varchar(60);
alter table author drop addr;
alter table author rename authors;
drop table if exists 表名;
```

##### 	DML

```sql
--在数据库中所有的字符串类型，必须使用单引号。
insert into `authors` (aut_name,gander,country,brithday,hobby) 
values ('罗曼罗兰','女','漂亮国','1969-1-14','旅游');
update `authors` set aut_name = '吴军',country='中国';
delete from author where auth_id = 1;
```

##### 	DQL

```sql
select `id`,`name`,`age` from `student`;
--1、null加任何值都等于null，，需要用到ifnull()函数。SELECT IFNULL(sal,0) from 表名; 如果薪资列为空，则输出0；
--2、将字符串做加减乘除运算，会把字符串当作0。
select id,name,sal+1000 from employee；
select `id` `编号`,`name` `名字`,ifnull(`age`,0) as `age` from `student` as s;
--（_代表匹配任意一个字符，％代表匹配0～n个任意字符）
select * from student where name like '张_'; 
select * from student where name like '张%';
select * from 表名 order by 列名1 asc, 列名2 desc;
```

聚合函数

```sql
select count(列名) from 表名;
select max(列名) from 表名;
select min(列名) from 表名;
select sum(列名) from 表名;
select avg(列名) from 表名;
```

分组查询

```sql
select gander,avg(age) avg_age,sum(age) sum_age from student
GROUP BY gander HAVING  gander = '男'
```

limit字句

```sql
--如果一个参数：说明从开始查找三条记录
SELECT id,name,age,gander FROM student limit 3;
--如果两个参数：说明从第三行起（不算），向后查三条记录
SELECT id,name,age,gander FROM student limit 3,3;
```

#### 多表查询

##### 	笛卡尔积（多表查询）

![](https://www.ydlclass.com/doc21xnv/assets/image-20220420181307754-1be234a8.png)

对于没有【条件约束】的两张表进行关联查询，如`select * from t1,t2`，就是从t1中一条条的选取数据，然后全量匹配t2的所有数据，形成一个大的集合，集合的数据量是两表数据量的乘积，我们称之为【笛卡尔积】

多表连接的方式有四种，内连接、外链接（左外连接，右外连接），全连接

##### 	内连接	

使用逗号分割两张表进行查询（employee e,dept e），mysql经过优化默认就等效与内链接，内连接使用关键字 【inner join】 或 【join】 来连接两张表。内连接中，【驱动表】是系统优化后自动选取的，会将执行计划中【扫描次数少】的表选做【驱动表】。

```sql
SELECT * from teacher t ,course c where c.t_id = t.id
SELECT * from teacher t join course c on c.t_id = t.id
SELECT * from teacher t inner join course c on c.t_id = t.id
```

##### 	外连接

内连接和外连接的区别：

- 对于【内连接】中的两个表，若【驱动表】中的记录在【被驱动表】中找不到与之匹配的记录，则该记录不会被加入到最后的结果集中。
- 对于【外连接】中的两个表，即使【驱动表】中的记录在【被驱动表】中找不到与之匹配的记录，也要将该记录加入到最后的结果集中，针对不同的【驱动表的选择】，又可以将外连接分为【左外连接】和【右外连接】。

所以我们可以得出以下结论：

- 对于左外连接查询的结果会包含左表的所有数据
- 对于右外连接查询的结果会包含右表的所有数据

```sql
--左外
SELECT * from course c left join  teacher t on  c.t_id = t.id
--右外
SELECT * from course c right join teacher t on c.t_id = t.id
```

##### 	全连接（mysql不支持）

```sql
--全连接
SELECT * from teacher t full join course c on c.t_id = t.id
--mysql等效替代
SELECT * from teacher t right outer join course c on c.t_id = t.id
union
SELECT * from teacher t left outer join course c on c.t_id = t.id
```

##### 	子查询

- 标量子查询：结果集只有一行一列 （又称为单行子查询）
- 列子查询： 结果集只有一列多行
- 行子查询： 结果集只有一行多列
- 表子查询： 结果集一般为多行多列

where/having 型子查询

```sql
--标量子查询
select * from student where age > (
	select age from student where name = '连宇栋'
);
--列子查询
select * from student where id in(
	select distinct s_id from scores where score > 90
)
-- 查询男生且是年龄大的学生信息
select * from student 
where (gander,age) = (
	select gander,max(age) from student  
	GROUP BY gander having gander = '男'
)
```

总结

- where 型子查询，如果是 where 列 =（内层 sql） 则内层 sql 返回的必须是单行单列，单个值。
- where 型子查询，如果是 where 列 in（内层 sql） 则内层 sql 返回的必须是单列，可以多行。

from型子查询

查询结果集在结构上可以当成表看，那就可以当成临时表对他进行再次查询，所以他支持的就是表子查询：

```sql
--查询数学成绩前5
select * from (
select s.id,s.name sname,r.score,c.name cname from student s 
left join scores r on s.id = r.s_id  
left join course c on r.c_id = c.id 
where c.name = '数学' 
order by  r.score desc
limit 5) t order by  t.score 
```

select型子查询

select关键字后的子查询仅仅支持标量子查询。

```sql
--查询代课数
select t.id,t.name,(
	select count(*) from course c where c.t_id = t.id
) as `代课的数量` from teacher t;
```

exists型子查询

表示判断子查询是否有返回值（true/false），有则返回true，没有返回false，这类子查询使用的不是很多。

```sql
--查询有课的老师
select * from teacher t where exists (
	select * from course c where c.t_id = t.id
);
```

#### Mysql常用函数

##### 	聚合函数

- COUNT(col) ： 统计查询结果的行数
- MIN(col)： 查询指定列的最小值
- MAX(col)： 查询指定列的最大值
- SUM(col)： 求和，返回指定列的总和
- AVG(col)： 求平均值，返回指定列数据的平均值

##### 	数值型函数

- CEILING(x)： 返回大于x的最小整数值，向上取整
- FLOOR(x)： 返回小于x的最大整数值，向下取整
- ROUND(x,y)： 返回参数x的四舍五入的有y位小数的值 四舍五入
- TRUNCATE(x,y)： 返回数字x截短为y位小数的结果
- PI()： 返回pi的值（圆周率）
- RAND()： 返回０到１内的随机值,可以通过提供一个参数(种子)使RAND()随机数生成器生成一个指定的值
- MOD(x,y) 返回 x 被 y 除后的余数

##### 	字符串函数

- LENGTH(s)： 计算字符串长度函数，返回字符串的字节长度
- CONCAT(s1,s2...,sn)： 合并字符串函数，返回结果为连接参数产生的字符串，参数可以是一个或多个
- LOWER(str)： 将字符串中的字母转换为小写
- UPPER(str)： 将字符串中的字母转换为大写
- LEFT(str,x)： 返回字符串str中最左边的x个字符
- RIGHT(str,x)： 返回字符串str中最右边的x个字符
- TRIM(str)： 删除字符串左右两侧的空格
-  REPLACE(s，s1，s2) 使用字符串 s2 替换字符串 s 中所有的字符串 s1
- SUBSTRING(s，n，len) 带有 len 参数的格式，从字符串 s 返回一个长度同 len 字符相同的子字符串，起始于位置 n
- REVERSE(str)： 返回颠倒字符串str的结果

##### 时间和日期函数

> 获取时间和日期

- 【CURDATE】 和 CURRENT_DATE】 两个函数作用相同，返回当前系统的【日期值】
- 【CURTIME 和 CURRENT_TIME】 两个函数作用相同，返回当前系统的【时间值】
- 【NOW】 和 【SYSDATE】 两个函数作用相同，返回当前系统的【日期和时间值】

> 时间戳或日期转换函数：

- 【UNIX_TIMESTAMP】 获取UNIX时间戳函数，返回一个以 UNIX 时间戳为基础的无符号整数
- 【FROM_UNIXTIME】 将 UNIX 时间戳转换为时间格式，与UNIX_TIMESTAMP互为反函数

> 据日期获取年月日的数值

- 【MONTH】 获取指定日期中的月份
- 【MONTHNAME】 获取指定日期中的月份英文名称
- 【DAYNAME】 获取指定曰期对应的星期几的英文名称
- 【DAYOFWEEK】 获取指定日期对应的一周的索引位置值
- 【WEEK】 获取指定日期是一年中的第几周，返回值的范围是否为 0〜52 或 1〜53
- 【DAYOFYEAR】 获取指定曰期是一年中的第几天，返回值范围是1~366
- 【DAYOFMONTH】 获取指定日期是一个月中是第几天，返回值范围是1~31
- 【YEAR】 获取年份，返回值范围是 1970〜2069

> 时间日期的计算

- 【DATE_ADD】 和 【ADDDATE】 两个函数功能相同，都是向日期添加指定的时间间隔
- 【DATE_SUB】 和【 SUBDATE】 两个函数功能相同，都是向日期减去指定的时间间隔
- 【ADDTIME】 时间加法运算，在原始时间上添加指定的时间
- 【SUBTIME】 时间减法运算，在原始时间上减去指定的时间
- 【DATEDIFF】 获取两个日期之间间隔，返回参数 1 减去参数 2 的值
- 【DATE_FORMAT】 格式化指定的日期，根据参数返回指定格式的值**

##### 加密函数

```sql
SELECT MD5('abc');
结果：900150983cd24fb0d6963f7d28e17f72
```

##### 流程控制函数

- IF(test,t,f)： 如果test是真，返回t；否则返回f

- IFNULL(arg1,arg2)： 如果arg1不是空，返回arg1，否则返回arg2

- NULLIF(arg1,arg2)： 如果【arg1=arg2】返回NULL，否则返回arg1

- CASE [test] WHEN[val1] THEN [result]...ELSE [default] END：

  如果test和valN相等，则返回resultN，否则返回default

  ```sql
  --输出学生各科的成绩，以及评级，60以下是D,60-70是C，71-80：是B ，80以上是A
  SELECT
  	*,
  CASE
  		WHEN score < 60 THEN 'D' 
  		WHEN score >= 60 and score < 70 THEN 'C' 
  		WHEN score >= 70 and score < 80 THEN 'B' 
  		WHEN score >= 80 and score <= 100 THEN 'A' 
  	END AS "评级"
  FROM
  	mystudent
  ```

#### 数据库设计

##### 	规范

- 第一范式：要求有主键，并且要求每一个字段原子性不可再分
- 第二范式：要求所有非主键字段完全依赖主键，不能产生部分依赖
- 第三范式：所有非主键字段和主键字段之间不能产生传递依赖

第一范式

数据库表中不能出现重复记录，每个字段是原子性的不能再分

| 学生编号 | 学生姓名 | 联系方式                                     |
| ---- | ---- | ---------------------------------------- |
| 1001 | 白杰   | [bj@qq.com](mailto:bj@qq.com),18565987896 |
| 1002 | 杨春旺  | [ycw@qq.com](mailto:ycw@qq.com),13659874598 |
| 1003 | 张志伟  | [zzw@qq.com](mailto:zzw@qq.com),12598745698 |

解决方案

| 学生编号 | 学生姓名 | 邮箱地址                            | 联系电话        |
| ---- | ---- | ------------------------------- | ----------- |
| 1001 | 白杰   | [bj@qq.com](mailto:bj@qq.com)   | 18565987896 |
| 1002 | 杨春旺  | [ycw@qq.com](mailto:ycw@qq.com) | 13659874598 |
| 1003 | 张志伟  | [zzw@qq.com](mailto:zzw@qq.com) | 12598745698 |

关于第一范式，每一行必须唯一，也就是每个表必须有主键，这是数据库设计的最基本要求，主要采用数值型或定长字符串表示，**关于列不可再分，应该根据具体的情况来决定**。如联系方式，为了开发上的便利可能就采用一个字段。

第二范式

第二范式是建立在第一范式基础上的，另外要求所有非主键字段完全依赖主键，不能产生**部分依赖**

其中学生编号和课程编号为联合主键

| 学生编号 | 性别   | 学生姓名 | 课程编号 | 课程名称  | 教室   | 成绩   |
| ---- | ---- | ---- | ---- | ----- | ---- | ---- |
| 1001 | 男    | 白杰   | 2001 | java  | 3004 | 89   |
| 1002 | 男    | 杨春旺  | 2002 | mysql | 3003 | 88   |
| 1003 | 女    | 刘慧慧  | 2003 | html  | 3005 | 90   |
| 1001 | 男    | 白杰   | 2002 | mysql | 3003 | 77   |
| 1001 | 男    | 白杰   | 2003 | html  | 3005 | 89   |
| 1003 | 女    | 刘慧慧  | 2001 | java  | 3004 | 90   |

以上虽然确定了主键，但此表会出现大量的数据冗余，出现冗余的原因在于，学生信息部分依赖了主键的一个字段学生编号，和课程id没有毛线关系。同时课程的信息只是依赖课程id，和学生id没有毛线关系。只有成绩一个字段完全依赖主键的两个部分，这就是第二范式**部分依赖**。

**解决方案：**

学生表：学生编号为主键

| 学生编号 | 性别   | 学生姓名 |
| ---- | ---- | ---- |
| 1001 | 男    | 白杰   |
| 1002 | 男    | 杨春旺  |
| 1003 | 女    | 刘慧慧  |

课程表：课程编号为主键

| 课程编号 | 课程名称  | 教室   |
| ---- | ----- | ---- |
| 2001 | java  | 3003 |
| 2002 | mysql | 3003 |
| 2003 | html  | 3005 |

成绩表：学生编号和课程编号为联合主键

| 学生编号 | 课程编号 | 成绩   |
| ---- | ---- | ---- |
| 1001 | 2001 | 89   |
| 1002 | 2002 | 88   |
| 1003 | 2003 | 90   |
| 1001 | 2002 | 77   |
| 1001 | 2003 | 89   |
| 1003 | 2001 | 90   |

如果一个表是单一主键，那么它就是复合第二范式，部分依赖和主键有关系

以上是典型的“多对多”设计

第三范式

建立在第二范式基础上的，非主键字段不能传递依赖于主键字段（不要产生传递依赖）

其中学生编号是主键

| 学生编号 | 学生姓名 | 专业编号 | 专业名称 |
| ---- | ---- | ---- | ---- |
| 1001 | 白杰   | 2001 | 计算机  |
| 1002 | 杨春旺  | 2002 | 自动化  |
| 1003 | 张志伟  | 2001 | 计算机  |

何为传递依赖？

专业编号依赖学生编号，因为该学生学的就是这个专业啊。但是专业名称和学生其实没多大关系，专业名称依赖于专业编号。这就叫传递依赖，就是某一个字段不直接依赖主键，而是依赖 依赖主键的另一个字段。

解决方法：

学生表，学生编号为主键：

学生编号为主键：

| 学生编号 | 学生姓名 | 专业编号 |
| ---- | ---- | ---- |
| 1001 | 白杰   | 2001 |
| 1002 | 杨春旺  | 2002 |
| 1003 | 张志伟  | 2001 |

专业表，专业编号为主键：

| 专业编号 | 专业名称 |
| ---- | ---- |
| 2001 | 计算机  |
| 2002 | 自动化  |

以上设计是典型的一对多的设计，一存储在一张表中，多存储在一张表中，**在多的那张表中添加外键指向一的一方**

##### 常见表关系

一对一 用的不多

一个表和另一张表存在的关系是一对一，此种设计不常用，因为此种关系经常会将多张表合并为一张表。

举例：

学生信息表可以分为基本信息表和详细信息表。

可能有这种需求，需要给个某个账户对学生表的操作，但是有些私密信息又不能暴露，就可以拆分。

`第一种方案：分两张表存储，共享主键第二种方案：分两张表存储，外键唯一`

一对多

第三范式的例子

两张表 外键建在多的一方

`分两张表存储，在多的一方添加外键，这个外键字段引用一的一方中的主键字段`

多对多

第二范式的例子

`分三张表存储，在学生表中存储学生信息，在课程表中存储课程信息，在成绩表中存储学生和课程的关系信息`