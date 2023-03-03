#### MySQL架构

##### 	mysql的系统架构

数据库和数据库实例

- 数据库：按照数据结构来组织、存储和管理数据的仓库，通常由数据库管理系统进行管理。
- 数据库管理软件（RDBMS）：就是我们说的数据库管理系统软件，他强调软件。
- 数据库实例：启动数据库软件，在内存中运行一个独立进程，用来操作数据，这个正在运行的进程就是一个数据库实例，理论上可以在一台电脑上启动多个数据库实例，当然要监听在不同的端口

mysql架构

**（1）MySQL向外提供的交互接口（Connectors）**

Connectors组件，是MySQL向外提供的交互组件，如java,.net,php等语言可以通过该组件来操作SQL语句，实现与SQL的交互。通过客户端/服务器通信协议与MySQL建立连接。MySQL 客户端与服务端的通信方式是 “ 半双工 ”。对于每一个 MySQL 的连接，时刻都有一个线程状态来标识这个连接正在做什么。

**（2）管理服务组件和工具组件(Management Service & Utilities)**

提供MySQL的各项服务组件和管理工具，如备份(Backup)，恢复(Recovery)，安全管理(Security)等功能。

**（3）连接池组件(Connection Pool)**

负责监听客户端向MySQL Server端的各种请求，接收请求，转发请求到目标模块。每个成功连接MySQL Server的客户请求都会被创建或分配一个线程，该线程负责客户端与MySQL Server端的通信，接收客户端发送的命令，传递服务端的结果信息等。

**（4）SQL接口组件(SQL Interface)**

接收用户SQL命令，如DML,DDL和存储过程等，并将最终结果返回给用户。

**（5）查询分析器组件(Parser)**

首先分析SQL命令语法的合法性，并进行抽象语法树解析，如果sql有语法错误，会抛出异常信息。

**（6）优化器组件（Optimizer）**

对SQL命令按照标准流程进行优化分析，mysql会按照它认为的最优方式进行优化，选用成本最小的执行计划。

**（7）缓存主件（Caches & Buffers）**

缓存和缓冲组件，这里边的内容我们后边会详细的讲解。

**（8）MySQL存储引擎**

MySQL属于关系型数据库，而关系型数据库的存储是以表的形式进行的，对于表的创建，数据的存储，检索，更新等都是由MySQL存储引擎完成的。

MySQL存储引擎在MySQL中扮演着重要角色。研究过SQL Server和Oracle的读者可能很清楚，这两种数据库的存储引擎只有一个，而MySQL的存储引擎种类比较多，如MyIsam存储引擎，InnoDB存储引擎和Memory存储引擎。

因为mysql本身就是开源的，他允许第三方基于MySQL骨架，开发适合自己业务需求的存储引擎。从MySQL存储引擎种类上来说，可以分为官方存储引擎和第三方存储引擎，比较常用的存储引擎包括InnoDB存储引擎，MyIsam存储引擎和Momery存储引擎。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220424195802351-3a69069f.png)

##### 	目录结构

mysql启动的时候，会从【安装目录】加载软件数据，在运行过程中，会从【数据目录】中读取数据。这两个目录我们不要放在一起，避免重新安装软件导致数据丢失。

mysql安装目录：默认C:\Program Files\MySQL

- `bin`目录：用于放置一些可执行的工具文件，如mysql.exe、mysqld.exe、mysqlshow.exe等。
- `include`目录：用于放置一些头文件，如：mysql.h、mysql_ername.h等。
- `lib`目录：用于放置一系列库文件。

数据文件目录：

默认数据目录：C:\ProgramData\MySQL\MySQL Server 8.0，注意ProgramData是一个隐藏目录，需要设置为【显示隐藏文件】：

`data`目录：用于放置一些日志文件以及数据库。data目录保存的是我们的真是的数据，每个数据库一个文件夹，每张表又是一个独立的文件，不同的存储引擎的文件存储方式不同。

bin目录工具汇总

> MySQL服务器端工具

- mysqld：SQL后台保护程序(MySQL服务器进程)。该程序必须运行之后。客户端才能通过连接服务器端程序访问和操作数据库。
- mysqld_safe：MySQL服务脚本。mysql_safe增加了一些安全特性，如当出现错误时重启服务器，向错误日志文件写入运行时间信息。
- mysql.server：MySQL服务启动服本。调用mysqld_safe来启动MySQL服务。
- mysql_multi：服务器启动脚本，可以启动或停止系统上安装的多个服务。
- myiasmchk：用来描述、检查、优化和维护MyISAM表的实用工具。
- mysqlbu：MySQL缺陷报告脚本。它可以用来向MySQL邮件系统发送缺陷报告。
- mysql_install_db：用于默认权限创建MySQ授权表。通常只是在系统上首次安装MySQL时执行一次。

> MySQL客户端工具

- mysql：交互式输入SQL语句或从文件以批处理模式执行SQL语句来操作数据库管理系统，就是我们的客户端。
- mysqldump：将MySQL数据库转储到一个文件，可以用来备份数据库。
- mysqladmin：用来检索版本、进程、以及服务器的状态信息。
- mysqlbinlog：用于从二进制日志读取语句。在二进制日志文件中包含执行的语句，可用来帮助系统从崩溃中恢复。
- mysqlcheck：检查、修复、分析以及优化表的表维护。
- mysqlhotcopy：当服务器在运行时，快速备份MyISAM或ISAM表的工具。
- mysql import：使用load data infile将文本文件导入相关表的客户程序。
- perror：显示系统或MySQL错误代码含义的工具。
- myisampack：压缩MyISAP表，产生更小的只读表。
- mysaqlaccess：检查访问主机名、用户名和数据库组合的权限。

```sh
mysql -h 124.220.197.17 -P 3306 -uroot -p  --连接本机或远程mysql
```

```sql
-- 备份一个表
mysqldump -u root -p ydlclass ydl_user  > ~/dump.txt
-- 备份一个数据库
mysqldump -u root -p ydlclass  > ~/dump.txt
-- 备份所有数据库
mysqldump -u root -p   --all-databases > dump.txt
--恢复数据
mysql -u root -p ydl < ~/dump.txt
```

##### 	字符集和排序规则

【show collation】命令可以查看mysql支持的所有的排序规则和字符集，如下所示部分：

举个例子：

- utf8-polish-ci，表示utf-8的字符集的波兰语的比较规则，ci代表忽略大小写。
- utf8-general-ci，就是通用的忽略大小写的utf8字符集比较规则。
- utf8mb4_0900_ai_ci中的0900指的是Unicode 9.0的规范，后边的后缀代表不区分重音也不区分大小写，他是utf8mb4字符集一个新的通用排序归则。

| 后缀   | 英文                 | 描述             |
| ---- | ------------------ | -------------- |
| _ai  | accent insensitive | 不区分重音（è，é，ê和ë） |
| _as  | accent sensitive   | 区分重音           |
| _ci  | case insensitive   | 不区分大小写         |
| _cs  | case sensitive     | 区分大小写          |
| _bin | binary             | 以二进制的形式进行比较    |

utf8和utf8mb4的区别：

- utf8mb3(utf-8)：使用1~3个字节表示字符，utf8默认就是utf8mb3。
- utf8mb4：使用1~4个字节表示字符，他是utf8的超集，甚至可以存储很多【emoji表情😀😃😄😁😆】，mysql8.0已经默认字符集设置为utf8mb4。

mysql提供两个变量，进行全局设置。【character_set_server】和【collate_server】对全局的字符集和排序规则进行设置。这两个设置可以在配置文件中修改。

```sql
-- 指定数据库
create database 数据库名 character set 字符集  collate 比较规则
create table 表名(
	列名 列类型 
) character set 字符集 collate 比较规则
create table 表名(
	列名 列类型 [character set 字符集] [collate 比较规则]
)
```

##### mysql修改配置文件的方法

#### I/O和存储

执行sql成本

- I/O成本：我们经常使用的MyIsam和InnoDB存储引擎都是将数据存储在磁盘上，当查询表中的记录时，需要先将数据加载到内存中，然后进行操作，这个从磁盘到内存的加载过程损耗的时间成为I/O成本。
- CPU成本：读取记录以及检测记录是否满足对应的搜索条件、对结果集进行排序等这些操作所消耗的时间称之为CPU成本。

##### I/O成本

磁盘结构

![](https://www.ydlclass.com/doc21xnv/assets/image-20201122150129276-837e618f.png)

盘片、片面和磁头：硬盘中一般会有多个【盘片】组成，每个盘片包含【两个面】，每个盘面都对应的有一个【读/写磁头】。受到硬盘整体体积和生产成本的限制，盘片数量都受到限制，一般都在5片以内。盘片的编号【自下向上】从0开始，如最下边的盘片有0面和1面，再上一个盘片就编号为2面和3面。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220424195930213-55095177.png)

扇区和磁道：下图显示的是一个盘面，盘面中一圈圈灰色同心圆为一条条磁道，从圆心向外画直线，可以将磁道划分为若干个弧段，每个磁道上一个弧段被称之为一个扇区（图践绿色部分）。扇区是磁盘的最小组成单元，通常是512字节。（由于不断提高磁盘的大小，部分厂商设定每个扇区的大小是4096字节）；

![](https://www.ydlclass.com/doc21xnv/assets/image-20220424195949755-9516f689.png)

磁头和柱面：硬盘通常由重叠的一组盘片构成，每个盘面都被划分为数目相等的磁道，并从外缘的“0”开始编号，具有相同编号的磁道形成一个圆柱，称之为磁盘的柱面。磁盘的柱面数与一个盘面上的磁道数是相等的。由于每个盘面都有自己的磁头，因此，盘面数等于总的磁头数。 如下图

![](https://www.ydlclass.com/doc21xnv/assets/image-20220425153537739-a5e991d6.png)

磁盘容量计算

- 存储容量 ＝ 磁头数 × 磁道(柱面)数 × 每道扇区数 × 每扇区字节数

- 有些【古老硬盘】每个磁道的扇区数一样，外圈的密度小，内圈的密度大，每圈可存储的数据量是一样的。现在的硬盘数据的密度都一致，这样磁道的周长越长，扇区就越多，存储的数据量就越大


磁盘读取响应时间

1. 寻道时间：磁头从开始移动到数据所在磁道所需要的时间，寻道时间越短，I/O操作越快，目前磁盘的平均寻道时间一般在3－15ms，一般都在10ms左右。
2. 旋转延迟：盘片旋转将请求数据所在扇区移至读写磁头下方所需要的时间，旋转延迟取决于磁盘转速。普通硬盘一般都是7200rpm，大概一圈8ms，慢的5400rpm。
3. 数据传输时间：完成传输所请求的数据所需要的时间。 小结一下：从上面的指标来看、其实最重要的、或者说、我们最关心的应该只有两个：寻道时间；旋转延迟。

交换单位

【块】是操作系统中最小的逻辑存储单位，他是虚拟出来的一个单位。操作系统与磁盘打交道的最小单位是磁盘块。每个块可以包括2、4、8、16、32、64…2的n次方个扇区。

为什么使用磁盘块

- 读取方便：由于扇区的容量比较小，数目众多，在寻址时比较困难，所以操作系统就将相邻的扇区组合在一起，形成一个块，再对块进行整体的操作。
- 分离对底层的依赖：操作系统忽略对底层物理存储结构的设计。通过虚拟出来磁盘块的概念，在系统中认为块是最小的单位。

扇区、块/簇、page的关系

1. 扇区： 硬盘的最小读写单元
2. 块/簇： 是操作系统针对硬盘读写的最小单元
3. page： 是内存与操作系统之间操作的最小单元
4. 扇区 <= 块/簇 <= page


局部性原理和磁盘预读

- 盘的存取速度往往是主存的【十万分之一】，因此为了提高效率，要【尽量减少磁盘I/O】。也是因为这个原因，磁盘往往不是严格的【按需读取】，而是每次都会预读，即使只需要一个字节，磁盘也会从这个位置开始，顺序向后读取一定长度的数据放入内存。这样做的理论依据是计算机科学中著名的局部性原理。

- 局部性原理：当一个数据被用到时，其附近的数据也通常会马上被使用，程序运行期间所需要的数据通常比较集中。

- 由于磁盘【顺序读取】的效率很高（不需要寻道时间，只需很少的旋转时间），因此对于具有局部性的程序来说，预读可以提高I/O效率。
- 预读的长度一般为【页（page）】的整倍数（在许多操作系统中，页得大小通常为4k）。当程序要读取的数据不在主存中时，会触发一个缺页异常，此时系统会向磁盘发出读盘信号，磁盘会找到数据的起始位置并向后连续读取一页或几页载入内存中，然后异常返回，程序继续运行。

##### 数据存储

数据是存储在文件系统中的，不同的存储存储引擎会有不同的文件格式和组织形式

innodb数据存储

对于innodb而言，数据是存储在表空间（文件空间file space）内的，表空间是一个抽象的概念，他对应着硬盘上的一个或多个文件，如下图

![](https://www.ydlclass.com/doc21xnv/assets/image-20220427104757135-00dc235b.png)

表空间存储数据的单位是【页】，我们可以这样类比，一个表空间就是个大大的本子，本子里是一页页的数据（innodb是以页为单位进行数据存储的），常用页面类型有很多，不同类型的页面可以存放【不同类型的数据】

- file header：记录页面的一些通用信息，比如当前页的校验和、页号、上页号、下页号、所属表空间等。
- file trailer：主要的工作是检验页是否完整。
- 表空间中的每一个页，都有一个页号（File_PAGE_OFFSET），我们可以通过这个页号在表空间快速定位到指定的页面。这个页号由4个字节组成，也就是32位，所有最多能存放2的32次方页，如果按照一页16k计算，一个表空间最大支持【64TB】的数据。整体的排列中页是连续的，但是页有上下指针，不连续的页也能组成链表。



#### MySQL临时表

##### 临时表简介

用户自己创建的临时表用于【保存临时数据】，以及MySQL内部在执行【复杂SQL】时，需要借助临时表进行【分组、排序、去重】等操作，临时表具有一下几个特点：

- 临时表不能通过`show tables`查看，在服务器重启之后，所有的临时表将全部被销毁。
- 临时表是每个进程独享的，当前进程（客户端）创建的临时表，其他进程（客户端）是查不到临时表里面的数据的，所以不同客户端可以创建同名的临时表。

##### 临时表分类

外部临时表

```sql
--通过create temporary table语句创建的临时表为外部临时表
create temporary table temp_table(
	id int,
	name varchar(10)
) ENGINE = InnoDB;
insert into temp_table values (1,'1');
select * from temp_table ;
-- 删除临时表
DROP TEMPORARY TABLE table_name;
```

内部临时表

用来存储某些操作的【中间结果】，这些操作可能包括在【优化阶段】或者【执行阶段】，这种内部表对用户来说是不可见的。通常在执行复杂SQL语句时，比如group by，distinct，union等语句时，MySQL内部将使用【自动生成的临时表】，以辅助SQL的执行

groupby执行流程

```sql
select age,count(*) from student group by age order by age
```

流程

- 创建一个内部临时表，有两列，一列是age，一列是count(*)。
- 全表扫描【原始表】（聚簇索引会在后边的内容讲解），每扫描一条数据进行一次判断，第一种情况，这条数据的年龄在临时表中不存在，则将年龄填入，count列填1。第二种情况，该条数据在临时表中存在，则将count列加1。临时表的存在是为了辅助计算。
- 对临时表的数据按照年龄进行排序。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220512173136197-67e95f7b.png)

内部临时表创建时机

- 使用GROUP BY分组，且分组字段没有索引时。
- 使用DISTINCT查询。
- 使用UNION进行结果合并，辅助去重。


临时表还可以分为【内存临时表】和【磁盘临时表】。内存临时表使用memery引擎（Memory引擎不支持BOLB和TEXT类型），磁盘临时表默认使用innodb引擎。在以下几种情况下，会创建磁盘临时表：

- 数据表中包含BLOB/TEXT列；
- 在 GROUP BY 或者 DSTINCT 的列中有超过 512字符的字符类型列；
- 在SELECT、UNION、UNION ALL查询中，存在最大长度超过512的列（对于字符串类型是512个字符，对于二进制类型则是512字节）；

#### MySQL事务

##### 事务简介

- 首先给大家举一个例子：我们有如下的销售业务，一个销售业务可能包含很多步骤，比如记录订单、添加积分、管理库存、扣减金额等等，每一个操作都可能对应一条或多条sql语句，但是这个业务却是不可分割的，不能下了订单，不扣减库存。此时我们就需要事务来统一管理这个业务当中的一系列sql语句了。
- 在 MySQL 中只有使用了 Innodb 数据库引擎的数据库或表才支持事务。
- 事务处理可以用来维护数据库的完整性，保证成批的 SQL 语句要么全部执行，要么全部不执行。

##### 事务分类

显式事务和隐式事务

- mysql的事务可以分为【显式事务】和【隐式事务】。

- 默认的事务是隐式事务，由变量【autocommit】控制。隐式事务的环境下，我们每执行一条sql都会【自动开启和关闭】事务，变量如下：SHOW VARIABLES LIKE 'autocommit';

- 显式事务由我们【自己控制】事务的【开启，提交，回滚】等操作  

  start transaction、commit、rollback

只读事务和读写事务

- 用read only开启只读事务，开启只读事务模式之后，事务执行期间任何【insert】或者【update】语句都是不允许的    start transaction  read only


保存点

- 使用savepoint 关键字在事务执行中新建【保存点】，之后可以使用rollback向任意保存点回滚。

  savepoint a 、rollback to a

- Mysql是不支持嵌套事务的，开启一个事务的情况下，若再开启一个事务，会隐式的提交上一个事务：


##### 事务的四大特性

- 原子性（Atomicity）：一个事务（transaction）中的所有操作，**要么全部完成，要么全部不完成**，不会结束在中间某个环节。如果事务在执行过程中发生错误，会被回滚（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样，这个很好理解。
- 一致性（Consistency）：在事务【开始之前和结束以后】，数据库的完整性没有被破坏，数据库状态应该与业务规则保持一致。**举一个例子**：A向B转账，不可能A扣了钱，B却没有收到，也不可能A和B的总金额，在事务前后发生变化，产生数据不一致。其他的三个特性都在为他服务。
- 隔离性（Isolation）：数据库【允许多个并发事务同时对其数据进行读取和修改】，隔离性可以防止多个事务在并发修改共享数据时产生【数据不一致】的现象，这里要联想到我们学习过的多线程。事务隔离级别分为不同等级，包括读未提交（Read uncommitted）、读提交（read committed）、可重复读（repeatable read）和串行化（Serializable），后续会详细讲。
- 持久性（Durability）：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。

##### 事务的隔离级别

|                        |      |       |      |                             |
| ---------------------- | ---- | ----- | ---- | --------------------------- |
| 隔离级别                   | 脏读   | 不可重复读 | 幻读   | 解决方案                        |
| Read uncommitted（读未提交） | √    | √     | √    |                             |
| Read committed（读已提交）   | ×    | √     | √    | undo log                    |
| Repeatable read（可重复读）  | ×    | ×     | √    | MVCC版本控制+间隙锁（mysql的rr不存在幻读） |
| Serializable（串行化）      | ×    | ×     | ×    |                             |

```
-- 查看全局和当前事务的隔离级别
SELECT @@global.transaction_isolation
show variables like 'transaction_isolation';

-- 设置下一个事务的隔离级别
SET transaction isolation level read uncommitted;
SET transaction isolation level read committed;
set transaction isolation level repeatable read;
SET transaction isolation level serializable;
-- 设置当前会话的隔离级别
SET session transaction isolation level read uncommitted;
SET session transaction isolation level read committed;
set session transaction isolation level repeatable read;
SET session transaction isolation level serializable;
-- 设置全局事务的隔离级别
SET GLOBAL transaction isolation level read uncommitted;
SET GLOBAL transaction isolation level read committed;
set GLOBAL transaction isolation level repeatable read;
SET GLOBAL transaction isolation level serializable;
其中，SESSION 和 GLOBAL 关键字用来指定修改的事务隔离级别的范围：
SESSION：表示修改的事务隔离级别将应用于当前 session（当前 cmd 窗口）内的所有事务；
GLOBAL：表示修改的事务隔离级别将应用于所有 session（全局）中的所有事务，且当前已经存在的 session 不受影响；
如果省略 SESSION 和 GLOBAL，表示修改的事务隔离级别将应用于当前 session 内的下一个还未开始的事务。
```

读未提交（read uncommitted）

- 一个事务可以读取其他【未提交的事务】修改的数据，这种隔离级别最低，一般情况下，数据库隔离级别都要高于该级别，该隔离级别下，可能会存在脏读、不可重复度，幻读的问题。

- 脏读：指的是一个事务读到了其他事务未提交的数据，未提交意味着这些数据可能会回滚，读到的数据不一定准确。


读已提交（read committed）

- 当前事务只能读到别的事物已经提交的数据，该隔离级别可能会产生不可重复读和幻读。

- 不可重复读：【一个事务】（A事务）修改了【另一个未提交事务】（B事务）读取过的数据。那么B事务【再次读取】，会发现两次读取的数据不一致。也就是说在一个原子性的操作中一个事务两次读取相同的数据，却不一致，一行数据不能重复被读取。主要是【update】语句，会导致不可重复读。

可重复读（repeatable read）

- 同一个事务中发出同一个SELECT语句【两次或更多次】，那么产生的结果数据集总是相同的，在RR隔离级别中可能出现幻读。

- 幻读：一个事务按照某些条件进行查询，事务提交前，有另一个事务插入了满足条件的其他数据，再次使用相同条件查询，却发现多了一些数据，就像出现了幻觉一样。幻读主要针对针对delete和insert语句。

- 不可重复读强调的是两次读取的数据【内容不同】，幻读前调的是两次读取的【行数不同】。


串行化（serializable）

- 事务A和事务B，事务A在操作数据库时，事务B只能排队等待
- 这种隔离级别很少使用，吞吐量太低，用户体验差
- 这种级别可以避免“幻读”，每一次读取的都是数据库中真实存在数据，事务A与事务B串行，而不并发。