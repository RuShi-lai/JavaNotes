

生产环境下一般使用水平扩容

rebalance：服务器负载过轻、过重，es将某些分片转移到此机器或者移走。

master节点：

1. 管理es集群的元数据，创建删除索引，维护索引的元数据，节点的增加或减少
2. 默认情况选择一台机器为master

   一个节点上可以有多个分片

情景：master宕机，所有主分片不是active，集群状态为red

容错第一步：重新选举master节点，承担master相关功能

容错第二部：新master将丢失的主分片的副本提升为主分片，集群为yellow，现在少副本分片

容错第三步：重启node，新master会感知到新节点的加入，将缺失的副本copy到node上node降级为从节点，集群状态为green

前置知识：

1. 一个索引数据量太大，分片会存储在多个es node中
2. 一个文档只会存储于一个主分片上

主分片不可变，取模会出错

插入时使用ik_max_word  查询使用ik_small

time_out：搜索时，请求必定要跨所有的主分片，数据量太大，搜索完毕要1h，用户等不及，丢失业务，设置time_out 指定每个shard只能在给定时间查询数据，能有几条返回几条，保住业务。

deep paging：每次搜索的关键词不同，所有的评分在每个shard中完全不一样的分数分布，可能某个shard中的第一条数据都比另一个shard中第1000个数据评分兜底，每个分片取10完全错误。

正确：每个节点都会将10000条数据给协调节点，协调节点会在内存中再排序，再拿出9990-10000的10条数据，返回客户端

match_alll 全部匹配  match 全文检索  

步骤一：search phase得到id，score等少量信息，汇总到协调节点排序

步骤二：fetch phase 协调节点构建mget，到各个shard上获取数据，给客户端返回。

分桶就是按group by 后面的数据分

父层聚合可以使用子层聚合结果排序

#### ELK简介

​	ELK是包含但不限于Elasticsearch（简称es）、Logstash、Kibana 三个开源软件的组成的一个整体，分别取其首字母组成ELK。ELK是用于数据抽取（Logstash）、搜索分析（Elasticsearch）、数据展现（Kibana）的一整套解决方案，所以也称作ELK stack。

![](https://www.ydlclass.com/doc21xnv/assets/1567691051440-beb65285.png)

#### Elastic Stack

##### 		特色

- 处理方式灵活：elasticsearch是目前最流行的准实时全文检索引擎，具有高速检索大数据的能力。
- 配置简单：安装elk的每个组件，仅需配置每个组件的一个配置文件即可。修改处不多，因为大量参数已经默认配在系统中，修改想要修改的选项即可。
- 接口简单：采用json形式RESTFUL API接受数据并响应，无关语言。
- 性能高效：elasticsearch基于优秀的全文搜索技术Lucene，采用倒排索引，可以轻易地在百亿级别数据量下，搜索出想要的内容，并且是秒级响应。
- 灵活扩展：elasticsearch和logstash都可以根据集群规模线性拓展，elasticsearch内部自动实现集群协作。
- 数据展现华丽：kibana作为前端展现工具，图表华丽，配置简单。

  ##### 组件介绍

**Elasticsearch**

Elasticsearch 是使用java开发，基于Lucene、分布式、通过Restful方式进行交互的近实时搜索平台框架。它的特点有：分布式，零配置，自动发现，索引自动分片，索引副本机制，restful风格接口，多数据源，自动搜索负载等。

**Logstash**

Logstash 基于java开发，是一个数据抽取转化工具。一般工作方式为c/s架构，client端安装在需要收集信息的主机上，server端负责将收到的各节点日志进行过滤、修改等操作在一并发往elasticsearch或其他组件上去。

**Kibana**

Kibana 基于nodejs，也是一个开源和免费的可视化工具。Kibana可以为 Logstash 和 ElasticSearch 提供的日志分析友好的 Web 界面，可以汇总、分析和搜索重要数据日志。

#### Elasticsearch是什么

##### 			搜索是什么

概念：用户输入想要的关键词，返回含有该关键词的所有信息。

应用场景

1. 互联网搜索：谷歌、百度、各种新闻首页
2. ​ 站内搜索（垂直搜索）：企业OA查询订单、人员、部门，电商网站内部搜索商品（淘宝、京东）场景。

   ##### 数据库搜索弊端

站内搜索（垂直搜索）：数据量小，简单搜索，可以使用数据库。

问题：

-  存储问题。电商网站商品上亿条时，涉及到单表数据过大必须拆分表，数据库磁盘占用过大必须分库（mycat）。
-  性能问题：解决上面问题后，查询“笔记本电脑”等关键词时，上亿条数据的商品名字段逐行扫描，性能跟不上。
-  不能分词。如搜索“笔记本电脑”，只能搜索完全和关键词一样的数据，那么数据量小时，搜索“笔记电脑”，“电脑”数据要不要给用户。

互联网搜索，肯定不会使用数据库搜索。数据量太大。PB级。

##### 		全文检索、倒排索引、Lucene

倒排索引：源于实际应用中需要根据属性的值来查找记录。根据存储时，经行分词建立term索引库。索引表中的每一项都包括一个属性值和具有该属性值的各记录的地址。由于不是由记录来确定属性值，而是由属性值来确定记录的位置，因而称为倒排索引(inverted index)

Lucene：一个jar包，里面封装了全文检索的引擎、搜索的算法代码。开发时，引入lucene的jar包，通过api开发搜索相关业务。底层会在磁盘建立索引库。LUCENE不支持分布式：引发问题：数据如何交互、数据如何分布、数据如何备份

##### 		ElasticSearch的功能

分布式的搜索引擎和数据分析引擎

- 搜索：互联网搜索、电商网站站内搜索、OA系统查询
- 数据分析：电商网站查询近一周哪些品类的图书销售前十；新闻网站，最近3天阅读量最高的十个关键词，舆情分析。

全文检索，结构化检索，数据分析

- 全文检索：搜索商品名称包含java的图书select * from books where book_name like "%java%"。
- 结构化检索：搜索商品分类为spring的图书都有哪些，select * from books where category_id='spring'
- 数据分析：分析每一个分类下有多少种图书，select category_id,count(*) from books group by category_id

对海量数据进行近实时的处理

- 分布式：ES自动可以将海量数据分散到多台服务器上去存储和检索,经行并行查询，提高搜索效率。相对的，Lucene是单机应用。
- 近实时：数据库上亿条数据查询，搜索一次耗时几个小时，是批处理（batch-processing）。而es只需秒级即可查询海量数据，所以叫近实时。秒级。

##### 	ElasticSearch特点

- 可拓展性：大型分布式集群（数百台服务器）技术，处理PB级数据，大公司可以使用。小公司数据量小，也可以部署在单机。大数据领域使用广泛。
- 技术整合：将全文检索、数据分析、分布式相关技术整合在一起：lucene（全文检索），商用的数据分析软件（BI软件），分布式数据库（mycat）
- 部署简单：开箱即用，很多默认配置不需关心，解压完成直接运行即可。拓展时，只需多部署几个实例即可，负载均衡、分片迁移集群内部自己实施。
- 接口简单：使用restful api经行交互，跨语言。
- 功能强大：Elasticsearch作为传统数据库的一个补充，提供了数据库所不不能提供的很多功能，如全文检索，同义词处理，相关度排名。

  ##### ElasticSearch核心概念

lucene和elasticsearch的关系

- Lucene：最先进、功能最强大的搜索库，直接基于lucene开发，非常复杂，api复杂
- Elasticsearch：基于lucene，封装了许多lucene底层功能，提供简单易用的restful api接口和许多语言的客户端，如java的高级客户端和底层客户端。

核心概念

- NRT	（近实时）：写入数据时，过1秒才会被搜索到，因为内部在分词、录入索引。es搜索时：搜索和分析数据需要秒级出结果。
- Cluster（集群）：包含一个或多个启动着es实例的机器群。通常一台机器起一个es实例。同一网络下，集群名一样的多个es实例自动组成集群，自动均衡分片等行为。默认集群名为“elasticsearch”
- Node（节点）：每个es实例称为一个节点。节点名自动分配，也可以手动配置。
- Index（索引）：包含一堆有相似结构的文档数据。
- Document（文档）：es中的最小数据单元。一个document就像数据库中的一条记录。通常以json格式显示。多个document存储于一个索引（Index）中。
- Field（字段）：就像数据库中的列（Columns），定义每个document应该有的字段。
- Type（类型）：每个索引里都可以有一个或多个type，type是index中的一个逻辑数据分类，一个type下的document，都有相同的field。
- shard（分片）：index数据过大时，将index里面的数据，分为多个shard，分布式的存储在各个服务器上面。可以支持海量数据和高并发，提升性能和吞吐量，充分利用多台机器的cpu。减轻单节点压力、充分利用机器性能、方便集群扩张。
- replica（副本）：在分布式环境下，任何一台机器都会随时宕机，如果宕机，index的一个分片没有，导致此index不能搜索。所以，为了保证数据的安全，我们会将每个index的分片经行备份，存储在另外的机器上。保证少数机器宕机es集群仍可以搜索。能正常提供查询和插入的分片我们叫做主分片（primary shard），其余的我们就管他们叫做备份的分片（replica shard）。副本（为了容错、高可用、继续提高吞吐量）

索引创建规则：

- 仅限小写字母
- 不能包含\、/、 *、?、"、<、>、|、#以及空格符等特殊符号
- 从7.0版本开始不再包含冒号
- 不能以-、_或+开头
- 不能超过255个字节（注意它是字节，因此多字节字符将计入255个限制）

  ##### Elasticsearch核心概念 vs. 数据库核心概念

| **关系型数据库（比如Mysql）** | **非关系型数据库（Elasticsearch）** |
| ------------------- | -------------------------- |
| 数据库Database         | 索引Index                    |
| 表Table              | 索引Index（原为Type）            |
| 数据行Row              | 文档Document                 |
| 数据列Column           | 字段Field                    |
| 约束 Schema           | 映射Mapping                  |

##### 		集群状态

Green 所有分片可用。Yellow所有主分片可用。Red主分片不可用，集群不可用

##### 		服务端口

- elasticsearch对外http服务端口：9200
- 集群间通信端口：9300
- Kibana对外http服务端口：5601

#### es快速入门

##### 		文档(document)的数据格式

- 应用系统的数据结构都是面向对象的，具有复杂的数据结构
- 对象存储到数据库，需要将关联的复杂对象属性插到另一张表，查询时再拼接起来。
- es面向文档，文档中存储的数据结构，与对象一致。所以一个对象可以直接存成一个文档。
- es的document用json数据格式来表达

  ##### 简单的集群管理

- 检查集群的健康状态：GET /_cat/health?v

- 查看集群有哪些索引：GET /_cat/indices?v

- 简单索引操作:  PUT /demo_index?pretty     

  ​                        DELETE /demo_index?pretty


##### 		商品的CRUD操作

- 创建索引： PUT /book
- 新增文档：PUT /book/_doc/1 {"name" : "java开发","price": 38.6}
- 检索文档：GET /book/_doc/1
- 更新文档：POST /book/_update/1 { "\_doc": { "name": "go开发"}}   #部分更新
- 删除文档：DELETE /book/_doc/1

#### 文档document入门

##### 		默认字段解析

- _index：说明此文档属于那个索引
- _type：相同索引中区分文档的字段，es9删除
- _id：文档的唯一标识（类比主键）
- _seq_no：序列号 
- _primary_term：文档所在主分片的编号
- _source：插入文档的内容
- _version：版本

  ##### 生成文档id

- 手动生成：PUT /test_index/_doc/1{...}

- 自动生成：POST /index/_doc

- 自动id特点：长度为20个字符，URL安全，base64编码，GUID，分布式生成不冲突

  ​

  ##### _source字段

\_source：插入数据时的所有字段和值。在get获取数据时，在_source字段中原样返回。

定制返回字段：GET /book/_doc/1?__source_includes=name,price

##### 		文档的替换和删除

全量替换

- 返回结果的版本号\_version在不断上升
- 实质：旧文档的内容不会立即删除，只是标记为deleted。适当的时机，集群会将这些文档删除

强制创建

* 防止覆盖原有数据，我们在新增时，设置为强制创建，不会覆盖原有文档。
* PUT /book/ \_doc/1/_create{ "test\_field" : "test"}  #版本冲突


删除

* DELETE /book/_doc/id
* 实质：旧文档的内容不会立即删除，只是标记为deleted。适当的时机，集群会将这些文档删除。


##### 		局部替换

局部更新步骤

1. es内部获取旧文档
2. 将传来的文档field更新到旧数据（内存）
3. 将旧文档标记为delete
4. 创建新文档

局部更新的优点

1. 减少网络请求次数

2. 减少网络开销

3. 减少并发冲突

   ```json
   post /index/_doc/id/_update 
   {
      "doc": {
         "field"："value"
      }
   }
   ```


##### 	使用脚本更新

使用内置脚本：搜索所有文档，将num字段乘以2输出

```json
PUT /test_index/_doc/7
{
  "num": 5
}
GET /test_index/_search
{
  "script_fields": {
    "my_doubled_field": {
      "script": {
       "lang": "expression",
        "source": "doc['num'] * multiplier",
        "params": {
          "multiplier": 2
        }
      }
    }
  }
}
```

##### 	es并发问题

电商平台场景

1. 读取商品信息（显示库存）
2. 用户下单
3. 更新库存信息（更新库存）

高并发问题：总是有一个线程是先到的，线程一、二并发买商品，库存由10变9，数据不对

##### 		乐观锁和悲观锁

假设商品件数为10

- 悲观锁：一旦有一个线程进入获得锁，另一个线程需要等待锁。不同锁机制：行级锁、标记锁、独写锁
- 乐观锁：没有锁的概念，一般使用版本来控制。同时并发，线程一先返回version1，件数9，版本号与库存一致则将版本号加一。当线程二返回时，版本号不一致重新读取。

总结

es后台主从同步是异步多线程的，多个请求是乱序的

es内部的主从同步也是基于版本号，当是如果线程二先到，线程一后到，判断线程一版本太旧了丢弃

- 悲观锁
  - 优点：方便，对程序来讲透明的，不需要额外操作
  - 缺点：并发能力弱
- 乐观锁
  - 优点：并发能力高，不给线程加锁，支持多线程操作

  - 缺点：麻烦，每次要对比版本号，在高并发情况下重复次数会很多

    ​

    手动维护版本号


- 背景：已有数据是在数据库中，有自己手动维护的版本号的情况下，可以使用external version控制。
- 要求：修改时external version要大于当前文档的_version
- 对比：基于_version时，修改的文档version等于当前文档的版本号。
- 使用?version=1&version_type=external

#####  	retry_on_conflict 参数

指定重试次数，常与_version结合使用

```json
 POST /test_index/_doc/5/_update？retry_on_conflict=3&version=22&version_type=external
{
  "doc": {
    "test_field": "ydl1"
  }
}
```

##### 	批量查询mget

```json
GET /_mget
{
   "docs" : [
      {
         "_index" : "test_index",
         "_type" :  "_doc",
         "_id" :    1
      },
      {
         "_index" : "test_index",
         "_type" :  "_doc",
         "_id" :    7
      }
   ]
}

GET /test_index/_mget
{
   "docs" : [
      {
         "_id" :    2
      },
      {
         "_id" :    3
      }
   ]
}

post /test_index/_doc/_search
{
    "query": {
        "ids" : {
            "values" : ["1", "7"]
        }
    }
}
```

##### 	批量增删改

Bulk 操作解释将文档的增删改查一些列操作，通过一次请求全都做完。减少网络传输次数。

```json
#语法
POST /_bulk
{"action": {"metadata"}}
{"data"}

##删除5，新增14，修改2。 
POST /_bulk
{ "delete": { "_index": "test_index",  "_id": "5" }} 
{ "create": { "_index": "test_index",  "_id": "14" }}
{ "test_field": "test14" }
{ "update": { "_index": "test_index",  "_id": "2"} }
{ "doc" : {"test_field" : "bulk test"} }
```

- 功能：
  - delete：删除一个文档，只要1个json串就可以了
  - create：相当于强制创建 PUT /index/type/id/_create
  - index：普通的put操作，可以是创建文档，也可以是全量替换文档
  - update：执行的是局部更新partial update操作
- 格式：每个json不能换行。相邻json必须换行。
- 隔离：每个操作互不影响。操作失败的行会返回其失败信息。
- 实际用法：bulk请求一次不要太大，否则一下积压到内存中，性能会下降。所以，一次请求几千个操作、大小在几M正好

##### es是什么

- 一个分布式的文档数据存储系统distributed document store。es看做一个分布式nosql数据库。如redis\mongoDB\hbase。
- 文档数据：es可以存储和操作json文档类型的数据，而且这也是es的核心数据结构。 存储系统：es可以对json文档类型的数据进行存储，查询，创建，更新，删除，等等操作。

应用场景

- 大数据。es的分布式特点，水平扩容承载大数据。
- 数据结构灵活。列随时变化。使用关系型数据库将会建立大量的关联表，增加系统复杂度。
- 数据操作简单。就是查询，不涉及事务。

举例

- 电商页面、传统论坛页面等。面向的对象比较复杂，但是作为终端，没有太复杂的功能（事务），只涉及简单的增删改查crud。
- 这个时候选用ES这种NoSQL型的数据存储，比传统的复杂的事务强大的关系型数据库，更加合适一些。无论是性能，还是吞吐量，可能都会更好。

#### Java API实现文档管理

##### 	依赖

```pom
<dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>elasticsearch-rest-high-level-client</artifactId>
        <version>7.9.0</version>
        <exclusions>  //带的es版本号可能比较低
            <exclusion>
                <groupId>org.elasticsearch</groupId>
                <artifactId>elasticsearch</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.elasticsearch</groupId>
        <artifactId>elasticsearch</artifactId>
        <version>7.9.0</version>
    </dependency>
```

##### 	步骤

1. ​ 获取连接客户端
2. ​ 构建请求
3. ​ 执行

```java
//获取连接客户端
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    //构建请求
    GetRequest getRequest = new GetRequest("book", "1");
    // 执行
    GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
    // 获取结果
    if (getResponse.isExists()) {
        long version = getResponse.getVersion();
        String sourceAsString = getResponse.getSourceAsString();//检索文档(String形式)
        System.out.println(sourceAsString);
    }
```