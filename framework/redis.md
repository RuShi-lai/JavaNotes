#### NoSQL数据库发展历程

##### 	web发展历程

web1.0时代简介

- web 1.0是以编辑为特征，网站提供给用户的内容是网站编辑进行编辑处理后提供的，用户阅读网站提供的内容
- 这个过程是网站到用户的**单向行为**
- web1.0时代的代表站点为新浪，搜狐，网易三大门户

web2.0时代简介

- 更注重用户的**交互**作用，用户既是网站内容的消费者（浏览者），也是网站内容的制造者
- 加强了网站与用户之间的**互动**，网站内容基于用户提供（微博、天涯社区、自媒体） 大数据推荐
- 网站的诸多功能也由用户参与建设，实现了网站与用户双向的交流与参与
- 用户在web2.0网站系统内拥有自己的数据，并完全基于WEB，所有功能都能通过浏览器完成。

##### 什么是NoSQL

- NoSQL最常见的解释是"non-relational"， 很多人也说它是"Not Only SQL"
- NoSQL仅仅是一个概念，泛指非关系型的数据库
- 区别于关系数据库，它们不保证关系数据的ACID特性

##### NoSQL特点

应用场景：

- 高并发的读写 10w/s
- 高可扩展性 不限制语言、lua脚本增强

不适用场景：

- 需要事务支持
- 基于sql的结构化查询存储，处理复杂的关系，需要即席查询（用户自定义查询条件的查询）

##### NoSQL数据库

memcache

- 很早出现的NoSql数据库
- 数据都在内存中，一般不持久化
- 支持简单的key-value模式
- 一般是作为缓存数据库辅助持久化的数据库

redis介绍

- 几乎覆盖了Memcached的绝大部分功能
- 数据都在内存中，支持持久化，主要用作备份恢复
- 除了支持简单的key-value模式，还支持多种数据结构的存储，比如 list、set、hash、zset等。
- 一般是作为缓存数据库辅助持久化的数据库
- 现在市面上用得非常多的一款内存数据库

mongoDB介绍

- 高性能、开源、模式自由(schema free)的文档型数据库
- 数据都在内存中， 如果内存不足，把不常用的数据保存到硬盘
- 虽然是key-value模式，但是对value（尤其是json）提供了丰富的查询功能
- 支持二进制数据及大型对象
- 可以根据数据的特点替代RDBMS，成为独立的数据库。或者配合RDBMS，存储特定的数据。

列式存储HBase介绍

HBase是**Hadoop**项目中的数据库。它用于需要对大量的数据进行随机、实时读写操作的场景中。HBase的目标就是处理数据量非常庞大的表，可以用普通的计算机处理超过10亿行数据，还可处理有数百万列元素的数据表

#### Redis介绍

##### Redis基本介绍

- Redis是当前比较热门的NoSQL系统之一
- 它是一个开源的、使用ANSI C语言编写的**key-value**存储系统（区别于MySQL的二维表格形式存储）
- 和Memcache类似，但很大程度补偿了Memcache的不足，Redis数据都是缓存在计算机**内存**中，不同的是，Memcache只能将数据缓存到内存中，无法自动定期写入硬盘，这就表示，一断电或重启，内存清空，数据丢失

##### Redis应用场景

- 取最新的n个数据:   将最新的5000条评论ID放在Redis的List集合中，并将超出集合部分从数据库获取
- 取topN操作：以某个条件为权重，比如按顶的次数排序，可以使用Redis的sorted set，将要排序的值设置成sorted set的score，将具体的数据设置成相应的value，每次只需要执行一条ZADD命令即可。
- 设置过期时间：sorted set的score值设置成过期时间的时间戳，那么就可以简单地通过过期时间排序，定时清除过期数据了，不仅是清除Redis中的过期数据，你完全可以把Redis里这个过期时间当成是对数据库中数据的索引，用Redis来找出哪些数据需要过期删除，然后再精准地从数据库中删除相应的记录。
- 计数器应用：命令都是原子性的，你可以轻松地利用INCR，DECR命令来构建计数器系统
- unique操作：不断地将数据往set中扔就行了，set意为集合，所以会自动排重
- 缓存：将数据直接存放到内存中，性能优于Memcached，数据结构更多样化
- 实时系统：set功能，你可以知道一个终端用户是否进行了某个操作，可以找到其操作的集合并进行分析统计对比等。没有做不到，只有想不到。

##### Redis特点

- 高效性 （内存）
  - Redis读取的速度是30w次/s，写的速度是10w次/s
- 原子性 (主逻辑线程是单线程)
  - Redis的所有操作都是原子性的，同时Redis还支持对几个操作全并后的原子性执行。 pipline
- 支持多种数据结构
  - string（字符串） a->b 配置 color--> red
  - list（列表） a->list 消息队列 msg--->["hello","ydlclass","itlils"]
  - hash（哈希） a->map 购物车 1----->["1"=>"剃须刀"，“2”=>“电脑”]
  - set（集合） a->set 去重 quchong-->["北京"，“山西”，“河北“]
  - zset(有序集合) a->sorted set 排行榜 top10->[”xx拿了金牌,10“,"跑路了,9.5"]
- 稳定性：持久化，主从复制（集群）
- 其他特性：支持过期时间，支持事务，消息订阅。

#### Redis单机环境

##### redis服务端启动

```
redis-server.exe redis.windows.conf
```

##### redis客户端启动

```
redis-cli.exe -h localhost -p 6379
```

#### Redis数据类型

redis当中一共支持五种数据类型，分别是：

- string字符串
- list列表
- set集合
- hash表
- zset有序集合

##### 对string操作

| 序号   | 命令及描述                                    | 示例                                       |
| ---- | ---------------------------------------- | ---------------------------------------- |
| 1    | SET key value 设置指定 key 的值                | 示例：SET hello world                       |
| 2    | GET key 获取指定 key 的值。                     | 示例：GET hello                             |
| 4    | GETSET key value 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。 | 示例：GETSET hello world2                   |
| 5    | MGET key1 [key2..] 获取所有(一个或多个)给定 key 的值。 | 示例：MGET hello world                      |
| 6    | SETEX key seconds value 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。 | 示例：SETEX hello 10 world3                 |
| 7    | SETNX key value 只有在 key 不存在时设置 key 的值。   | 示例：SETNX ydlclass redisvalue             |
| 9    | STRLEN key 返回 key 所储存的字符串值的长度。           | 示例：STRLEN ydlclass                       |
| 10   | MSET key value [key value ] 同时设置一个或多个 key-value 对。 | 示例：MSET ydlclass2 ydlclassvalue2 ydlclass3 ydlclassvalue3 |
| 12   | MSETNX key value key value 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 | 示例：MSETNX ydlclass4 ydlclassvalue4 ydlclass5 ydlclassvalue5 |
| 13   | PSETEX key milliseconds value这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。 | 示例：PSETEX ydlclass6 6000 ydlclass6value  |
| 14   | INCR key 将 key 中储存的数字值增一。                | 示例： set ydlclass7 1 INCR ydlclass7 GET ydlclass7 |
| 15   | INCRBY key increment 将 key 所储存的值加上给定的增量值（increment） | 示例：INCRBY ydlclass7 2 get ydlclass7      |
| 16   | INCRBYFLOAT key increment 将 key 所储存的值加上给定的浮点增量值（increment） | 示例：INCRBYFLOAT ydlclass7 0.8             |
| 17   | DECR key 将 key 中储存的数字值减一。                | 示例： set ydlclass8 1 DECR ydlclass8 GET ydlclass8 |
| 18   | DECRBY key decrement key 所储存的值减去给定的减量值（decrement） | 示例：DECRBY ydlclass8 3                    |
| 19   | APPEND key value 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。 | 示例：APPEND ydlclass8 hello                |

##### 对hash的操作

| 序号   | 命令及描述                                    | 示例                                       |
| ---- | ---------------------------------------- | ---------------------------------------- |
| 1    | HSET key field value 将哈希表 key 中的字段 field 的值设为 value 。 | 示例：HSET key1 field1 value1               |
| 2    | HSETNX key field value 只有在字段 field 不存在时，设置哈希表字段的值。 | 示例：HSETNX key1 field2 value2             |
| 3    | HMSET key field1 value1 [field2 value2 ] 同时将多个 field-value (域-值)对设置到哈希表 key 中。 | 示例：HMSET key1 field3 value3 field4 value4 |
| 4    | HEXISTS key field 查看哈希表 key 中，指定的字段是否存在。 | 示例： HEXISTS key1 field4 HEXISTS key1 field6 |
| 5    | HGET key field 获取存储在哈希表中指定字段的值。          | 示例：HGET key1 field4                      |
| 6    | HGETALL key 获取在哈希表中指定 key 的所有字段和值        | 示例：HGETALL key1                          |
| 7    | HKEYS key 获取所有哈希表中的字段                    | 示例：HKEYS key1                            |
| 8    | HLEN key 获取哈希表中字段的数量                     | 示例：HLEN key1                             |
| 9    | HMGET key field1 [field2] 获取所有给定字段的值     | 示例：HMGET key1 field3 field4              |
| 10   | HINCRBY key field increment 为哈希表 key 中的指定字段的整数值加上增量 increment 。 | 示例： HSET key2 field1 1 HINCRBY key2 field1 1 HGET key2 field1 |
| 11   | HINCRBYFLOAT key field increment 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。 | 示例：HINCRBYFLOAT key2 field1 0.8          |
| 12   | HVALS key 获取哈希表中所有值                      | 示例：HVALS key1                            |
| 13   | HDEL key field1 [field2] 删除一个或多个哈希表字段    | 示例： HDEL key1 field3 HVALS key1          |

##### 对list操作

| **号** | **命令及描述**                                | **示例**                                   |
| ----- | ---------------------------------------- | ---------------------------------------- |
| 1     | **LPUSH key value1 [value2]** 将一个或多个值插入到列表头部 | 示例：LPUSH list1 value1 value2             |
| 2     | **LRANGE key start stop** 查看list当中所有的数据  | 示例：LRANGE list1 0 -1                     |
| 3     | LPUSHX key value 将一个值插入到已存在的列表头部         | 示例：LPUSHX list1 value3 LINDEX list1 0    |
| 4     | RPUSH key value1 [value2] 在列表中添加一个或多个值到尾部 | 示例： RPUSH list1 value4 value5 LRANGE list1 0 -1 |
| 5     | RPUSHX key value 为已存在的列表添加单个值到尾部         | 示例：RPUSHX list1 value6                   |
| 6     | LINSERT key BEFORE\|AFTER pivot value 在列表的元素前或者后插入元素 | 示例：LINSERT list1 BEFORE value3 beforevalue3 |
| 7     | LINDEX key index 通过索引获取列表中的元素            | 示例：LINDEX list1 0                        |
| 8     | LSET key index value 通过索引设置列表元素的值        | 示例：LSET list1 0 hello                    |
| 9     | LLEN key 获取列表长度                          | 示例：LLEN list1                            |
| 10    | LPOP key 移出并获取列表的第一个元素                   | 示例：LPOP list1                            |
| 11    | RPOP key 移除列表的最后一个元素，返回值为移除的元素。          | 示例：RPOP list1                            |
| 12    | BLPOP key1 [key2 ] timeout 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | 示例：BLPOP list1 2000                      |
| 13    | BRPOP key1 [key2 ] timeout 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | 示例：BRPOP list1 2000                      |
| 14    | RPOPLPUSH source destination 移除列表的最后一个元素，并将该元素添加到另一个列表并返回 | 示例：RPOPLPUSH list1 list2                 |
| 15    | BRPOPLPUSH source destination timeout 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 | 示例：BRPOPLPUSH list1 list2 2000           |
| 16    | LTRIM key start stop 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 | 示例：LTRIM list1 0 2                       |
| 17    | DEL key1 key2 删除指定key的列表                 | 示例：DEL list2                             |

##### 对set的操作

| **序号** | **命令及描述**                                | **示例**                                   |
| ------ | ---------------------------------------- | ---------------------------------------- |
| 1      | SADD key member1 [member2] 向集合添加一个或多个成员  | 示例：SADD set1 setvalue1 setvalue2         |
| 2      | SMEMBERS key 返回集合中的所有成员                  | 示例：SMEMBERS set1                         |
| 3      | SCARD key 获取集合的成员数                       | 示例：SCARD set1                            |
| 4      | SDIFF key1 [key2] 返回给定所有集合的差集            | 示例： SADD set2 setvalue2 setvalue3 SDIFF set1 set2 |
| 5      | SDIFFSTORE destination key1 [key2] 返回给定所有集合的差集并存储在 destination 中 | 示例：SDIFFSTORE set3 set1 set2             |
| 6      | SINTER key1 [key2] 返回给定所有集合的交集           | 示例：SINTER set1 set2                      |
| 7      | SINTERSTORE destination key1 [key2] 返回给定所有集合的交集并存储在 destination 中 | 示例：SINTERSTORE set4 set1 set2            |
| 8      | SISMEMBER key member 判断 member 元素是否是集合 key 的成员 | 示例：SISMEMBER set1 setvalue1              |
| 9      | SMOVE source destination member 将 member 元素从 source 集合移动到 destination 集合 | 示例：SMOVE set1 set2 setvalue1             |
| 10     | SPOP key 移除并返回集合中的一个随机元素                 | 示例：SPOP set2                             |
| 11     | SRANDMEMBER key [count] 返回集合中一个或多个随机数    | 示例：SRANDMEMBER set2 2                    |
| 12     | SREM key member1 [member2] 移除集合中一个或多个成员  | 示例：SREM set2 setvalue1                   |
| 13     | SUNION key1 [key2]] 返回所有给定集合的并集          | 示例：SUNION set1 set2                      |
| 14     | SUNIONSTORE destination key1 [key2] 所有给定集合的并集存储在 destination 集合中 | 示例：SUNIONSTORE set5 set1 set2            |

##### 对key的操作

| 序号   | 命令及描述                                    | 示例                     |
| ---- | ---------------------------------------- | ---------------------- |
| 1    | DEL key 该命令用于在 key 存在时删除 key。            | 示例：del ydlclass5       |
| 2    | DUMP key 序列化给定 key ，并返回被序列化的值。           | 示例：DUMP key1           |
| 3    | EXISTS key 检查给定 key 是否存在。                | 示例：exists ydlclass     |
| 4    | EXPIRE key seconds 为给定 key 设置过期时间，以秒计。   | 示例：expire ydlclass 5   |
| 6    | PEXPIRE key milliseconds 设置 key 的过期时间以毫秒计。 | 示例：PEXPIRE set3 3000   |
| 8    | KEYS pattern 查找所有符合给定模式( pattern)的 key 。 | 示例：keys *              |
| 10   | PERSIST key 移除 key 的过期时间，key 将持久保持。      | 示例：persist set2        |
| 11   | PTTL key 以毫秒为单位返回 key 的剩余的过期时间。          | 示例：pttl set2           |
| 12   | TTL key 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。 | 示例：ttl set2            |
| 13   | RANDOMKEY 从当前数据库中随机返回一个 key 。            | 示例： randomkey          |
| 14   | RENAME key newkey 修改 key 的名称             | 示例：rename set5 set8    |
| 15   | RENAMENX key newkey 仅当 newkey 不存在时，将 key 改名为 newkey 。 | 示例：renamenx set8 set10 |
| 16   | TYPE key 返回 key 所储存的值的类型。                | 示例：type set10          |

对zset操作

| **#** | **命令及描述**                                | **示例**                                   |
| ----- | ---------------------------------------- | ---------------------------------------- |
| 1     | ZADD key score1 member1 [score2 member2] 向有序集合添加一个或多个成员，或者更新已存在成员的分数 | 向ZSet中添加页面的PV值 ZADD pv_zset 120 page1.html 100 page2.html 140 page3.html |
| 2     | ZCARD key 获取有序集合的成员数                     | 获取所有的统计PV页面数量 ZCARD pv_zset              |
| 3     | ZCOUNT key min max 计算在有序集合中指定区间分数的成员数    | 获取PV在120-140在之间的页面数量 ZCOUNT pv_zset 120 140 |
| 4     | ZINCRBY key increment member 有序集合中对指定成员的分数加上增量 increment | 给page1.html的PV值+1 ZINCRBY pv_zset 1 page1.html |
| 5     | ZINTERSTORE destination numkeys key [key ...] 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中 | 创建两个保存PV的ZSET： ZADD pv_zset1 10 page1.html 20 page2.html ZADD pv_zset2 5 page1.html 10 page2.html ZINTERSTORE pv_zset_result 2 pv_zset1 pv_zset2 |
| 7     | ZRANGE key start stop [WITHSCORES] 通过索引区间返回有序集合指定区间内的成员 | 获取所有的元素，并可以返回每个key对一个的score ZRANGE pv_zset_result 0 -1 WITHSCORES |
| 9     | ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT] 通过分数返回有序集合指定区间内的成员 | 获取ZSET中120-140之间的所有元素 ZRANGEBYSCORE pv_zset 120 140 |
| 10    | ZRANK key member 返回有序集合中指定成员的索引          | 获取page1.html的pv排名（升序） ZRANK pv_zset page3.html |
| 11    | ZREM key member [member ...] 移除有序集合中的一个或多个成员 | 移除page1.html ZREM pv_zset page1.html     |
| 15    | ZREVRANGE key start stop [WITHSCORES] 返回有序集中指定区间内的成员，通过索引，分数从高到低 | 按照PV降序获取页面 ZREVRANGE pv_zset 0 -1        |
| 17    | ZREVRANK key member 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序 | 获取page2.html的pv排名（降序） ZREVRANK pv_zset page2.html |
| 18    | ZSCORE key member 返回有序集中，成员的分数值          | 获取page3.html的分数值 ZSCORE pv_zset page3.html |

##### 对bitmaps操作

- 计算机最小的存储单位是位bit，Bitmaps是针对位的操作的，相较于String、Hash、Set等存储方式更加节省空间
- Bitmaps不是一种数据结构，操作是基于String结构的，一个String最大可以存储512M，那么一个Bitmaps则可以设置2^32个位
- Bitmaps单独提供了一套命令，所以在Redis中使用Bitmaps和使用字符串的方法不太相同。可以**把Bitmaps想象成一个以位为单位的数组**，数组的每个单元**只能存储0和1**，数组的下标在Bitmaps中叫做偏移量offset
- BitMaps 命令说明：**将每个独立用户是否访问过网站存放在Bitmaps中， 将访问的用户记做1， 没有访问的用户记做0， 用偏移量作为用户的id** 。


设置值

```text
SETBIT key offset value  
```

**setbit**命令设置的vlaue只能是**0**或**1**两个值

![image-20220222171326788](https://www.ydlclass.com/doc21xnv/assets/image-20220222171326788-dcfd2234.png)

```text
 setbit unique:users:2022-04-05 0  1  
 setbit unique:users:2022-04-05 5 1  
 setbit unique:users:2022-04-05 11 1  
 setbit unique:users:2022-04-05 15 1  
 setbit unique:users:2022-04-05 19 1 
```

获取值

```text
GETBIT key offset
```

获取Bitmaps指定范围值为1的个数

```text
BITCOUNT key [start end] 
```

BitMap间的运算

```text
BITOP operation destkey key [key, …] 
bitop是一个复合操作， 它可以做多个Bitmaps的and（交集） 、 or（并集） 、 not（非） 、 xor（异或） 操作并将结果保存在destkey中。
```

##### 对HyperLogLog结构的操作

HyperLogLog使用语法主要有pfadd和pfcount，顾名思义，一个是来添加数据，一个是来统计的

假如两个页面很相近，现在想统计这两个页面的用户访问量呢？这里就可以用**pfmerge**合并统计了，

 HyperLogLog为什么适合做大量数据的统计

- Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定的、并且是很小的。

- 在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。

- #### 但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

#### JavaAPI操作

##### pom依赖

```pom
 <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.9.0</version>
    </dependency>
```

##### 连接及关闭redis客户端

实现步骤：

1. 创建JedisPoolConfig配置对象，指定最大空闲连接为10个、最大等待时间为3000毫秒、最大连接数为50、最小空闲连接5个

2. 创建JedisPool

3. 使用@Test注解，编写测试用例，查看Redis中所有的key

   ​ a) 从Redis连接池获取Redis连接

   ​ b) 调用keys方法获取所有的key

   ​ c) 遍历打印所有key

   ​

```java
public class RedisTest {
    private JedisPool jedisPool;
    @BeforeTest
    public void beforeTest() {
        // JedisPoolConfig配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 指定最大空闲连接为10个
        config.setMaxIdle(10);
        // 最小空闲连接5个
        config.setMinIdle(5);
        // 最大等待时间为3000毫秒
        config.setMaxWaitMillis(3000);
        // 最大连接数为50
        config.setMaxTotal(50);
        jedisPool = new JedisPool(config, "192.168.200.131", 6379);
    }
    @Test
    public void keysTest() {
        // 从Redis连接池获取Redis连接
        Jedis jedis = jedisPool.getResource();
        // 调用keys方法获取所有的key
        Set<String> keySet = jedis.keys("*");

        for (String key : keySet) {
            System.out.println(key);
        }
    }
    @AfterTest
    public void afterTest() {
        // 关闭连接池
        jedisPool.close();
    }
}
```

##### 操作string类型数据

```java
@Test
    public void stringTest() {
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 1.添加一个string类型数据，key为pv，用于保存pv的值，初始值为0
        jedis.set("pv", "0");
        // 2.查询该key对应的数据
        System.out.println("pv:" + jedis.get("pv"));
        // 3.修改pv为1000
        jedis.set("pv", "1000");
        // 4.实现整形数据原子自增操作 +1
        jedis.incr("pv");
        // 5.实现整形该数据原子自增操作 +1000
        jedis.incrBy("pv", 1000);
        System.out.println(jedis.get("pv"));
        // 将jedis对象放回到连接池
        jedis.close();
    }
```

##### 操作hash类型数据

```java
@Test
    public void testHash(){
        //从池子里哪一个连接
        Jedis jedis = jedisPool.getResource();
        //1. 往Hash结构中添加以下商品库存 goods
        //   a)   iphone13 => 10000
        //   b)   macbookpro => 9000
        jedis.hset("goods", "iphone13", "10000");
        jedis.hset("goods", "macbookpro", "9000");
        //2. 获取Hash中所有的商品
        Set<String> goods = jedis.hkeys("goods");
        for (String good : goods) {
            System.out.println(good);
        }
        //3. 新增3000个macbookpro库存
        //String hget = jedis.hget("goods", "macbookpro");
        //int stock=Integer.parseInt(hget)+3000;
        //jedis.hset("goods", "macbookpro", stock+"");
        jedis.hincrBy("goods", "macbookpro", 3000);
        String hget = jedis.hget("goods", "macbookpro");
        System.out.println(hget);
        //4. 删除整个Hash的数据
        jedis.del("goods");
    }
```

##### 操作list类型数据

```java
@Test
    public void listTest() {
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();
        // 1.	向list的左边插入以下三个手机号码：18511310001、18912301231、18123123312
        jedis.lpush("tel_list", "18511310001", "18912301231", "18123123312");
        // 2.	从右边移除一个手机号码
        jedis.rpop("tel_list");
        // 3.	获取list所有的值
        List<String> telList = jedis.lrange("tel_list", 0, -1);
        for (String tel : telList) {
            System.out.println(tel);
        }
        jedis.close();
    }
```

操作set类型数据

```java
@Test
    public void setTest(){
        // 获取Jedis连接
        Jedis jedis = jedisPool.getResource();

        // 求UV就是求独立有多少个（不重复）
        // 1.	往一个set中添加页面 page1 的uv，用户user1访问一次该页面
        jedis.sadd("uv", "user1");
        // jedis.sadd("uv", "user3");
        // jedis.sadd("uv", "user1");
        // 2.	user2访问一次该页面
        jedis.sadd("uv", "user2");

        // 3.	user1再次访问一次该页面
        jedis.sadd("uv", "user1");

        // 4.	最后获取 page1的uv值
        System.out.println("uv:" + jedis.scard("uv"));

        jedis.close();
    }
```

#### Redis持久化

在redis当中，提供了两种数据持久化的方式，分别为**RDB**以及**AOF**，且Redis默认开启的数据持久化方式为RDB方式。

##### RDB持久化方案

- Redis会定期保存数据快照至一个rbd文件中，并在启动时自动加载rdb文件，恢复之前保存的数据。

- 在配置文件中配置Redis进行快照保存的时机：save [seconds]\[ change ]在seconds秒内如果发生了changes次数据修改，则进行一次RDB快照保存
- 可以通过SAVE或者BGSAVE命令手动触发RDB快照保存。SAVE 和 BGSAVE 两个命令都会调用 rdbSave 函数，但它们调用的方式各有不同：
  - SAVE 直接调用 rdbSave ，阻塞 Redis 主进程，直到保存完成为止。在主进程阻塞期间，服务器不能处理客户端的任何请求。
  - BGSAVE 则 fork 出一个子进程，子进程负责调用 rdbSave ，并在保存完成之后向主进程发送信号，通知保存已完成。 Redis 服务器在BGSAVE 执行期间仍然可以继续处理客户端的请求。

RDB方案优点

1. 对性能影响最小。如前文所述，Redis在保存RDB快照时会fork出子进程进行，几乎不影响Redis处理客户端请求的效率。
2. 每次快照会生成一个完整的数据快照文件，所以可以辅以其他手段保存多个时间点的快照（例如把每天0点的快照备份至其他存储媒介中），作为非常可靠的灾难恢复手段。
3. 使用RDB文件进行数据恢复比使用AOF要快很多

RDB方案缺点

1. 快照是定期生成的，所以在Redis crash(菪机）时或多或少会丢失一部分数据
2. 如果数据集非常大且CPU不够强（比如单核CPU），Redis在fork子进程时可能会消耗相对较长的时间，影响Redis对外提供服务的能力

##### AOF持久化方案

采用AOF持久方式时，Redis会把每一个写请求都记录在一个日志文件里。在Redis重启时，会把AOF文件中记录的所有写操作顺序执行一遍，确保数据恢复到最新。

aof默认关闭，需要在配置文件590行左右 appendonly  yes 

AOF提供了三种fsync配置：always/everysec/no，通过配置项[appendfsync]指定：

1. **appendfsync no**：不进行fsync，将flush文件的时机交给OS决定，速度最快
2. **appendfsync always**：每写入一条日志就进行一次fsync操作，数据安全性最高，但速度最慢
3. **appendfsync everysec**：折中的做法，交由后台线程每秒fsync一次

AOF rewrite

随着AOF不断地记录写操作日志，因为所有的写操作都会记录，所以必定会出现一些无用的日志。大量无用的日志会让AOF文件过大，也会让数据恢复的时间过长。不过Redis提供了AOF rewrite功能，可以重写AOF文件，只保留能够把数据恢复到最新状态的最小写操作集。

![](https://www.ydlclass.com/doc21xnv/assets/image-20220301093017307-cc8b332a.png)

AOF rewrite可以通过BGREWRITEAOF命令触发，也可以配置Redis定期自动进行：

```text
auto-aof-rewrite-percentage 100  
auto-aof-rewrite-min-size 64mb  
```

- Redis在每次AOF rewrite时，会记录完成rewrite后的AOF日志大小，当AOF日志大小在该基础上增长了100%后，自动进行AOF rewrite 32m--->10万-->64M--->40M--100万--80--》50M-->>100m
- auto-aof-rewrite-min-size最开始的AOF文件必须要触发这个文件才触发，后面的每次重写就不会根据这个变量了。该变量仅初始化启动Redis有效。

 AOF优点

1. 最安全，在启用appendfsync为always时，任何已写入的数据都不会丢失，使用在启用appendfsync everysec也至多只会丢失1秒的数据
2. AOF文件在发生断电等问题时也不会损坏，即使出现了某条日志只写入了一半的情况，也可以使用redis-check-aof工具轻松修复
3. AOF文件易读，可修改，在进行某些错误的数据清除操作后，只要AOF文件没有rewrite，就可以把AOF文件备份出来，把错误的命令删除，然后恢复数据。

AOF的缺点

1. AOF文件通常比RDB文件更大

2. 性能消耗比RDB高

3. #### 数据恢复速度比RDB慢

#### Redis事务

##### 事务简介

Redis 事务的本质是一组命令的集合。事务支持一次执行多个命令，一个事务中所有命令都会被序列化。在事务执行过程，会按照顺序串行化执行队列中的命令，其他客户端提交的命令请求不会插入到事务执行命令序列中。

- **Redis事务没有隔离级别的概念**

批量操作在发送 EXEC 命令前被放入队列缓存，并不会被实际执行，也就不存在事务内的查询要看到事务里的更新，事务外查询不能看到。

- **Redis不保证原子性**

Redis中，单条命令是原子性执行的，但事务不保证原子性，且没有回滚。事务中任意命令执行失败，其余的命令仍会被执行。

一个事务从开始到执行会经历以下三个阶段：

- 第一阶段：开始事务

- 第二阶段：命令入队

- 第三阶段：执行事务

  ​

Redis事务相关命令：

- MULTI：开启事务，redis会将后续的命令逐个放入队列中，然后使用EXEC命令来原子化执行这个命令队列


- EXEC：执行事务中的所有操作命令


- DISCARD：取消事务，放弃执行事务块中的所有命令


- WATCH：监视一个或多个key，如果事务在执行前，这个key（或多个key）被其他命令修改，则事务被中断，不会执行事务中的任何命令


- UNWATCH：取消WATCH对所有key的监视

##### 事务演练

事务失败处理：语法错误（编译器错误），在开启事务后，修改k1值为11，k2值为22，但k2语法错误，最终导致事务提交失败，k1、k2保留原值。

```text
192.168.200.131:6379> set key1 v1
OK
192.168.200.131:6379> set key2 v2
OK
192.168.200.131:6379> multi
OK
192.168.200.131:6379(TX)> set key1 11
QUEUED
192.168.200.131:6379(TX)> sets key2 22
(error) ERR unknown command `sets`, with args beginning with: `key2`, `22`, 
192.168.200.131:6379(TX)> exec
(error) EXECABORT Transaction discarded because of previous errors.
192.168.200.131:6379> get key1
"v1"
192.168.200.131:6379> get key2
"v2"
```

Redis类型错误（运行时错误），在开启事务后，修改k1值为11，k2值为22，但将k2的类型作为List，在运行时检测类型错误，最终导致事务提交失败，此时事务并没有回滚，而是跳过错误命令继续执行， 结果k1值改变、k2保留原值。

```text
192.168.200.131:6379> set key1 v1
OK
192.168.200.131:6379> set key2 v2
OK
192.168.200.131:6379> multi
OK
192.168.200.131:6379(TX)> set key1 11
QUEUED
192.168.200.131:6379(TX)> lpush key2 22
QUEUED
192.168.200.131:6379(TX)> exec
1) OK
2) (error) WRONGTYPE Operation against a key holding the wrong kind of value
192.168.200.131:6379> get key1
"11"
192.168.200.131:6379> get key2
"v2"
192.168.200.131:6379>
```

##### 为什么Redis不支持事务回滚？

多数事务失败是由语法错误或者数据结构类型错误导致的，语法错误说明在命令入队前就进行检测的，而类型错误是在执行时检测的，Redis为提升性能而采用这种简单的事务，这是不同于关系型数据库的，特别要注意区分。Redis之所以保持这样简易的事务，完全是为了保证高并发下的核心问题——**性能**

#### 数据删除和淘汰策略

##### 过期数据

redis中数据特征：Redis是一种内存级数据库，所有数据均存放在内存中，内存中的数据可以通过TTL指令获取其状态

TTL返回的值有三种情况：正数，-1，-2

- **正数**：代表该数据在内存中还能存活的时间
- **-1**：永久有效的数据
- -**2** ：已经过期的数据 或被删除的数据 或 未定义的数据

**删除策略就是针对已过期数据的处理策略**，已过期的数据是真的就立即删除了吗？其实也不是，我们会有多种删除策略，是分情况的，在不同的场景下使用不同的删除方式会有不同效果，这也正是我们要将的数据的删除策略的问题

实效性数据的存储结构

![](https://www.ydlclass.com/doc21xnv/assets/image-20220301155949303-d7b1315e.png)

过期数据是一块独立的存储空间，Hash结构，field是内存地址，value是过期时间，保存了所有key的过期描述，在最终进行过期处理的时候，对该空间的数据进行检测， 当时间到期之后通过field找到内存该地址处的数据，然后进行相关操作

##### 数据删除策略

- 1.定时删除
- 2.惰性删除
- 3.定期删除

定时删除

创建一个定时器，当key设置有过期时间，且过期时间到达时，由定时器任务立即执行对键的删除操作

- **优点**：节约内存，到时就删除，快速释放掉不必要的内存占用
- **缺点**：CPU压力很大，无论CPU此时负载量多高，均占用CPU，会影响redis服务器响应时间和指令吞吐量
- **总结**：用处理器性能换取存储空间（拿时间换空间）

惰性删除

数据到达过期时间，不做处理。等下次访问该数据时，我们需要判断

1. 如果未过期，返回数据
2. 发现已过期，删除，返回不存在

- **优点**：节约CPU性能，发现必须删除的时候才删除
- **缺点**：内存压力很大，出现长期占用内存的数据
- **总结**：用存储空间换取处理器性能（拿空间换时间）

定期删除

redis的定期删除方案：

- Redis启动服务器初始化时，读取配置server.hz的值，默认为10

- 每秒钟执行server.hz次**serverCron()**-------->**databasesCron()**--------->**activeExpireCycle()**

- **activeExpireCycle()**对每个expires[*]逐一进行检测，每次执行耗时：250ms/server.hz

- 对某个expires[*]检测时，随机挑选W个key检测

  ```text
  如果key超时，删除key
    如果一轮中删除的key的数量>W*25%，循环该过程
    如果一轮中删除的key的数量≤W*25%，检查下一个expires[*]，0-15循环
    W取值=ACTIVE_EXPIRE_CYCLE_LOOKUPS_PER_LOOP属性值
  ```


- 参数current_db用于记录**activeExpireCycle()** 进入哪个expires[*] 执行
- 如果activeExpireCycle()执行时间到期，下次从current_db继续向下执行

总的来说：定期删除就是周期性轮询redis库中的时效性数据，采用随机抽取的策略，利用过期数据占比的方式控制删除频度

- **特点1**：CPU性能占用设置有峰值，检测频度可自定义设置
- **特点2**：内存压力不是很大，长期占用内存的冷数据会被持续清理
- **总结**：周期性抽查存储空间（随机抽查，重点抽查）

删除策略对比

定时删除

```text
节约内存，无占用,
不分时段占用CPU资源，频度高,
拿时间换空间
```

惰性删除

```text
内存占用严重
延时执行，CPU利用率高
拿空间换时间
```

定期删除

```text
内存定期随机清理
每秒花费固定的CPU资源维护内存
随机抽查，重点抽查
```

##### 数据淘汰策略

当新数据进入redis时，如果内存不足怎么办？在执行每一个命令前，会调用**freeMemoryIfNeeded()**检测内存是否充足。如果内存不满足新 加入数据的最低存储要求，redis要临时删除一些数据为当前指令清理存储空间。清理数据的策略称为逐出算法。

注意：逐出数据的过程不是100%能够清理出足够的可使用的内存空间，如果不成功则反复执行。当对所有数据尝试完毕， 如不能达到内存清理的要求，将出现错误信息如下

```sh
(error) OOM command not allowed when used memory >'maxmemory'
```

策略配置

```properties
最大可使用内存，即占用物理内存的比例，默认值为0，表示不限制。生产环境中根据需求设定，通常设置在50%以上
maxmemory ?mb
每次选取待删除数据的个数，采用随机获取数据的方式作为待检测删除数据
maxmemory-samples count
对数据进行删除的选择策略
maxmemory-policy policy
```

policy种类

**第一类**：检测易失数据（可能会过期的数据集server.db[i].expires ） 同一个库

```properties
volatile-lru：挑选最近最少使用的数据淘汰      least recently used
volatile-lfu：挑选最近使用次数最少的数据淘汰   least frequently used
volatile-ttl：挑选将要过期的数据淘汰
volatile-random：任意选择数据淘汰
```

**第二类**：检测全库数据（所有数据集server.db[i].dict ）

```properties
allkeys-lru：挑选最近最少使用的数据淘汰
allkeLyRs-lfu：：挑选最近使用次数最少的数据淘汰
allkeys-random：任意选择数据淘汰，相当于随机
```

**第三类**：放弃数据驱逐

```properties
no-enviction（驱逐）：禁止驱逐数据(redis4.0中默认策略)，会引发OOM(Out Of Memory)
```

#### Redis的主从复制架构

##### 主从复制介绍

三高架构：高并发、高性能、高可用

**可用性**：一年中应用服务正常运行的时间占全年时间的百分比

主从复制概念

为了避免单点Redis服务器故障，准备多台服务器，互相连通。将数据复制多个副本保存在不同的服务器上，连接在一起，并保证数据是同步的。即使有其中一台服务器宕机，其他服务器依然可以继续提供服务，实现Redis的高可用，同时实现数据冗余备份。

![image-20220301173728213](https://www.ydlclass.com/doc21xnv/assets/image-20220301173728213-faff35ea.png)

- 提供数据方：**master**   主服务器，主节点，主库主客户端


- 接收数据方：**slave**   从服务器，从节点，从库，从客户端


- 需要解决的问题：数据同步（master的数据复制到slave中）

**概念：主从复制即将master中的数据即时、有效的复制到slave中**

master和slave职责不同

master:     写数据，执行写操作时，将出现变化的数据自动同步到slave，读数据（可忽略）

slave：读数据、写禁止

主从复制的作用

- 读写分离：master写、slave读，提高服务器的读写负载能力
- 负载均衡：基于主从结构，配合读写分离，由slave分担master负载，并根据需求的变化，改变slave的数量，通过多个从节点分担数据读取负载，大大提高Redis服务器并发量与数据吞吐量
- 故障恢复：当master出现问题时，由slave提供服务，实现快速的故障恢复
- 数据冗余：实现数据热备份，是持久化之外的一种数据冗余方式
- 高可用基石：基于主从复制，构建哨兵模式与集群，实现Redis的高可用方案

##### 主从复制工作流程

主从复制过程大体可以分为3个阶段

- 建立连接阶段（即准备阶段）
- 数据同步阶段
- 命令传播阶段（反复同步）

![](https://www.ydlclass.com/doc21xnv/assets/image-20220301175309628-8e0b266c.png)

主从复制的工作流程

阶段1：建立slave到master的连接，使master能够识别slave，并保存slave端口号

![](https://www.ydlclass.com/doc21xnv/assets/image-20220301180213179-69b85b61.png)

阶段2：数据同步

- 在slave初次连接master后，复制master中的所有数据到slave

- 将slave的数据库状态更新成master当前的数据库状态

  ![](https://www.ydlclass.com/doc21xnv/assets/image-20220301230944836-c1284320.png)

  master说明

  1：如果master数据量巨大，数据同步阶段应避开流量高峰期，避免造成master阻塞，影响业务正常执行

  2：复制缓冲区大小设定不合理，会导致数据溢出。如进行全量复制周期太长，进行部分复制时发现数据已经存在丢失的情况，必须进行第二次全量复制，致使slave陷入死循环状态。

  3：master单机内存占用主机内存的比例不应过大，建议使用50%-70%的内存，留下30%-50%的内存用于执 行bgsave命令和创建复制缓冲区

  slave说明

  1：为避免slave进行全量复制、部分复制时服务器响应阻塞或数据不同步，建议关闭此期间的对外服务

  2：数据同步阶段，master发送给slave信息可以理解master是slave的一个客户端，主动向slave发送命令

  3：多个slave同时对master请求数据同步，master发送的RDB文件增多，会对带宽造成巨大冲击，如果master带宽不足，因此数据同步需要根据业务需求，适量错峰

  4：slave过多时，建议调整拓扑结构，由一主多从结构变为树状结构，中间的节点既是master，也是 slave。注意使用树状结构时，由于层级深度，导致深度越高的slave与最顶层master间数据同步延迟较大，数据一致性变差，应谨慎选择

命令传播

- 当master数据库状态被修改后，导致主从服务器数据库状态不一致，此时需要让主从数据同步到一致的状态，同步的动作称为**命令传播**
- master将接收到的数据变更命令发送给slave，slave接收命令后执行命令	

命令传播阶段部分复制

命令传播阶段出现了断网现象：

- 网络闪断闪连：忽略
- 短时间网络中断：部分复制
- 长时间网络中断：全量复制

部分复制的三个核心要素

1. 服务器的运行 id（run id）
2. 主服务器的复制积压缓冲区
3. 主从服务器的复制偏移量

工作原理

- 通过offset区分不同的slave当前数据传播的差异
- master记录已发送的信息对应的offset
- slave记录已接收的信息对应的offset

全量复制和分布复制

![](https://www.ydlclass.com/doc21xnv/assets/image-20220302004108225-246adaf9.png)

心跳机制

进入命令传播阶段候，master与slave间需要进行信息交换，使用心跳机制进行维护，实现双方连接保持在线

master心跳：

- 内部指令：PING
- 周期：由repl-ping-slave-period决定，默认10秒
- 作用：判断slave是否在线
- 查询：INFO replication 获取slave最后一次连接时间间隔，lag项维持在0或1视为正常

slave心跳任务

- 内部指令：REPLCONF ACK
- 周期：1秒
- 作用1：汇报slave自己的复制偏移量，获取最新的数据变更指令
- 作用2：判断master是否在线

注意事项

- 当slave多数掉线，或延迟过高时，master为保障数据稳定性，将拒绝所有信息同步
- slave数量由slave发送REPLCONF ACK命令做确认
- slave延迟由slave发送REPLCONF ACK命令做确认

![](https://www.ydlclass.com/doc21xnv/assets/image-20220302005829154-38f96bf0.png)

##### 搭建主从架构

```sh
复制两套redis redis6380 redis 6381
cp redis-6.2.6/ redis6380
cp redis-6.2.6/ redis6381

分别修改配置文件：
port 6380
pidfile /var/run/redis_6380.pid
logfile "/export/server/redis6380/log/redis.log"
dir /export/server/redis6380/data
port 6381
pidfile /var/run/redis_6381.pid
logfile "/export/server/redis6381/log/redis.log"
dir /export/server/redis6381/data

启动：
./bin/redis-server redis.conf slaveof 192.168.200.131 6379

登陆6380
./bin/redis-cli -h 192.168.200.131 -p 6380
info
```

5测试

​ 1 主节点写数据，从节点获取数据

![image-20220302013937793](data:image/png;base64,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)

![image-20220302013945167](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARkAAAA9CAYAAACZWh2DAAAJiElEQVR4nO2dz08b1xbHv2P1P8jOhqpPtLs0hoCNEQlSFUWKsiLGciiLVkFq0ieFKAmgCJBXEUEImqCA9EIrETWLBCwwWaFI1VMlEgtiQxL3dZeiVz0Y/xnjtxjPMDMe23cGDybw/UgssK/vnHvP3HPPPfeXdOrUqQJOOMHhNcxGdzHT/gNSPl+9xSlBUXox83YEbfkkBq7cR85jGY96fdSbw9SH0jyG5L/iQKofvVPbpu+Cw2uY62lAdvo07i4fXT19Vm8B6o0Sm8dcTwPklcSRa1CK0oqh1QVcCUiQV66ha2obOGIyniTqoo93O8gDaItcQlDJ6gZNUXrxfTSAgryEX5MAjvBrIZ1UT0brBQAc+Z7gsKEnc7QwGjcjhcwDdN1a1D2rkCSVyaGYXl46FE/Yyok1MoSQw4HdFCHEU2hkCCGeQiNDCPEUGhlCiKfQyBBCPIVGhhDiKTQyhBBPoZEhhHgKjQwhxFNoZAg5RihKLx5u5LA43FpvUXRsN0hqOz8DklR2X48xDQAUChu2e12Me4SA2uyfEJEPQMmeDjsZreUAAHnlWsmOV1FEy1sq2x5e/vMSHn5wl84J1epPic0jPdRh+qyS3kT1ISxfFb05kU9Uv17UM1Ex1aCitOLuSg5vEkAyJZf9kRKbR/rJVeR/+hrnI2dwPnIGM9kIbr/9BVFF0dMFh9cwG0njZvg0zkfOoDP8AFv+OGZXEwga0okiKp/27HRmFP5Uvy5jV8cNs4GxKUfnj0tAdMFVTyBaXv2ogOyE/tyBFHDlyZ94GHOeThTR+vMt39Cfp5bjGl7Crhzi+hBFRG/C8gnq14k+jpqX8ClgMjKxuQWEN/vR1TOOnTI/UJRezAxGUMg8MPVYyzcnsIUI4vdC+me5qcvo6hnXexefbxG/pmTA34kLZ50LKyIfoPZeI9EAstOnK3oksa7I/lZ5jXevkMkD/sglx4ZQtLyxueJZJDef65+9n0xgVS6g7dv9hiKaThTR+rPi821j+sWmTTmc5ac10vWVMVvZRfUmLJ+gfkXrWdNnoOcp1h/3CstXCaV5DIsbObze/MP0t76xhrvNpQa93PfB4TW83vwD6cwoQpKEQM9TU35uDaOdfE47OJORSd0KVlfu2Sb4AeR3P1q++Ig9wcYpSQ1o+NKRnOLyAWi52Al/Pml+uWzY2ZUB/+doMn36FRr8AOSdmm2JN5ZXUXpxLgTkN1+Z849fV7fyF+URTecE0fqzo+UfjQB28fe7A+QX/0YdjpTpZET1JiqfiH6d1nNu6jI6pzcghUfLGktRjAdSaR7ZqlwoGaqpntYCuuXynlZu6rLuPWcLBcgr10zenhu9K0ovZhLARPu+J3hzZQ9tg69MBq4azltR8RCdkox82/hbwGNuagygUNjD3l+OnyyEorTiQiQAyDtomvtPRWv+fvIZthDB7bdqpeluMzYxY+jVDkJJeTUj/d+sniY4vIY3g8Cj6Q0AjfjirIN0h4DmYeRTPx/sfJnk78gWCkA+jX+/M3/lRG+i8gnp10U9+5ZvoPPHJeT9ccxaQgROaLnYCT82kZxUn+3zbWP6fhJ5BEydcMu970reSd3T6upz9WwRfL5F3DV45upzn2ELAYQvhir80pKPmwe/yQL+6P0Sd+1OuPKhOUpsXk2TfeZ5QE0Kj+LceuWxuM+3iLsdQb1XSGdG0ZadKIkBuEWkvNHHOcxG0hho/wHLKO/liaarFUY3Of3kKvz5JCYms9V/WAGtvrssL64REb2JyudGv6L17Pswjnh7P17m942YF+jGN/u7OS6ldeqBpgN5U4eBq5aUuhXETDaAK0/+1HubESTwKFP+/CttRqBQ2KiZl2CP6g5bY0a+D+NIZgF/9Lre82gxgtnoLh6FT6uB2tCIq3Gnlerl7cPDjRziu/2mRlfq5Ymmqy2+D+Po7Qjuu+ebnZjLuAs4iyGuN1H5nOnXfT1LUgO6+517FO9/SyNviGMqSiuGEnH4sYk3+pBRrRcpPFoSt7kTdjdsdoIW69H+tJiPE1yf8Zu6FUTK8L96RKDN2BbFsedgpOw0d21RY0NtNt/s7MpAyChv0XUuyuTDIm63f8TQ6gK6B39BNOlO1orlLQ43Q0OjaoDT0KDMcQXBdIc0w/p+MoHVyAK6u/qA5UUPniCmN1H5RPW7LKoPSz0bh16Pwi7f6S8/V6fWe57idY/6UUFewkC7cSq+WC+yetSmLR61J7uDyrVy+x3kUzvp4tfR7ZeR+c3ssmrBLb9B2eXQXOD1jXnX41zdjQx9U5JHU2MAyP+vOBNSDADq/1t+71K+auXVhpvWWQ+rWyyazql87lHrqzTgL06l2SVxvYnKJ6ZfV/Ucm1eHXvkkBlx2morSiqFv1VlaY4DWOpR0PiwqTsA0fuVYJqt8FyKB0tk5F9TEyASH15Ae6sDWT+bFS04MDKAGwgKSBEnqwLm4e3mWF5ImNxTYj49svVB7Ce3lkgJX8b3hWUrzGOIh2L5c1eQTLe/yQlINGs7tu9gt9+6j2y/j5cJzx+lE5TsIsTm1104eJC5TZXZJRG+i8jnRr5N61t71QuZBxdhSNSoZVSt28lXP97sDxYn0fAy6UprHkBQ4sNyK6SBxu5WUGsYVlSWrWjP2rlz0ca5sMNhuhWa1RioqnzGvQJUVnNayAOVX/FaTz0l5RVdMi6YTkc+tfgF7HTvRByB2X5GI3kTlK5fWdsWvQD3rQwWbO5DcUOmWAevK6XJp7Vcvl95u4GYVuzUfVRcJIKGujxLNj7cVEFIHKhnc6OMcbofkY7Ot4dMvASGfItr6HJuJkp1d2fMlCocJjQwh9aA4q2VdIV9yM+QxgMMlQupEtZshjws0MoQQT+FwiRDiKTQyhBBPoZEhhHgKjQwhxFNoZAghnkIjQwjxFBoZQoin0MgQQjzF9koU7SR0/XwSy/kfweE1oTNLRPMjhBxf6MkQQjzFZGT2Tw0rHjmo3Uzg8nqQWudHCPn0YEsnhHiKqw2SweE1zEZ3MdP+M74wnpx1zHaPEkIOjmtPRpI6cCfzFA0vvtbvx8mHRnhPMCHExIGGS8ZzSPX7cVzcIU0IOb64NjKFwobhAioV+7uHCSEnGQZ+CSGeQiNDCPGUmhmZSjfuEUJOLjWzBpVuNiSEnFxcrZOx3rYH2N8YSAghvK2AEOIpdDsIIZ5CI0MI8RQaGUKIp9DIEEI8hUaGEOIpNDKEEE+hkSGEeMpn9RaAkJPI8+fVV8b39fUdgiTeQ0+GEOIp/wcC1FDgUZXBZAAAAABJRU5ErkJggg==)

​ 2从节点不能写数据

![image-20220302014003296](https://www.ydlclass.com/doc21xnv/assets/image-20220302014003296-0487766a.png)

