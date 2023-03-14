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

  ##### ElasticSearch特点

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

  #####  retry_on_conflict 参数

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
##### 	结合spring-boot

pom文件和application.yml

```pom
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>2.0.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <version>2.0.6.RELEASE</version>
</dependency>

spring:
  application:
    name: service-search
heima:
  elasticsearch:
    hostlist: 127.0.0.1:9200 #多个结点中间用逗号分隔
```

配置类

```java
@Configuration
public class ElasticSearchConfig {
    @Value("${heima.elasticsearch.hostlist}")
    private String hostlist;
    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient(){
        String[] split = hostlist.split(",");
        HttpHost[] httpHostsArray = new HttpHost[split.length];
        for (int i = 0; i < httpHostsArray.length; i++) {
            String item = split[i];
            httpHostsArray[i] = new  HttpHost
            (item.split(":")[0],Integer.parseInt(item.split(":")[1]),"http");
        }
        return  new RestHighLevelClient(RestClient.builder(httpHostsArray));
    }
}
```

测试类

```java
    @Autowired
    RestHighLevelClient restHighLevelClient;
public void testGet() throws IOException {
    GetRequest getRequest = new GetRequest("test_posts","1");

    // ===================可选字段==========================
    String[] includes = {"user", "message"};
    String[] excludes = Strings.EMPTY_ARRAY;
    FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
    getRequest.fetchSourceContext(fetchSourceContext);

    //同步查询
    GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);

    //异步执行
   /* ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
        //成功时
        @Override
        public void onResponse(GetResponse getResponse) {
            System.out.println(getResponse.getId());
            System.out.println(getResponse.getSourceAsString());
        }

        //失败时
        @Override
        public void onFailure(Exception e) {
            e.printStackTrace();
        }
    };
    restHighLevelClient.getAsync(getRequest,RequestOptions.DEFAULT,listener);
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }*/

    if(getResponse.isExists()) {
        System.out.println(getResponse.getId());
        System.out.println(getResponse.getSourceAsString()); //String形式
        System.out.println(getResponse.getSourceAsBytes());//Byte形式
        System.out.println(getResponse.getSourceAsMap());//Map形式
    }
}
```

测试文档新增

```java
@Test
public void testAdd() throws IOException {
    //构建请求
    IndexRequest request = new IndexRequest("test_posts");
    request.id("5");

    //=======================构建文档数据===================
    //方法一
    String jsonString = "{\"user\":\"tomas\",\n" +
            "  \"postDate\":\"2019-07-18\",\n" +
            "  \"message\":\"trying out es1\"}";
    request.source(jsonString, XContentType.JSON);

 /*   //方法二
    HashMap<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("user","tomas");
    jsonMap.put("postDate","2019-07-18");
    jsonMap.put("message","trying out es1");
    request.source(jsonMap);

    //方法三
    XContentBuilder builder = XContentFactory.jsonBuilder();
    builder.startObject();
    {
        builder.field("user","tomas");
        builder.timeField("postDate","2019-07-18");
        builder.field("message","trying out es1");
    }
    builder.endObject();
    request.source(builder);

    //方法四
    request.source("user","tomas",
            "postDate","2019-07-18",
            "message","trying out es1");*/


    //可选参数
  /*  //设置超时时间
    request.timeout("1s");
    request.timeout(TimeValue.timeValueSeconds(1));

    //手动维护版本号
    request.version(4);
    request.versionType(VersionType.EXTERNAL);*/

    //执行(同步执行）
    IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);

    //异步执行
    /*ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>(){

        @Override
        public void onResponse(IndexResponse indexResponse) {
            System.out.println(indexResponse.getIndex());
            System.out.println(indexResponse.getId());
            System.out.println(indexResponse.getResult());
        }

        @Override
        public void onFailure(Exception e) {
            e.printStackTrace();
        }
    };
    restHighLevelClient.indexAsync(request,RequestOptions.DEFAULT,listener);
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }*/
    //获取结果
    System.out.println(indexResponse.getIndex());
    System.out.println(indexResponse.getId());
    System.out.println(indexResponse.getResult());

    if(indexResponse.getResult() == DocWriteResponse.Result.CREATED){
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println("CREATE" + result);
    }else if(indexResponse.getResult() == DocWriteResponse.Result.UPDATED){
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println("UPDATE" + result);
    }

    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
    if(shardInfo.getTotal() != shardInfo.getSuccessful()){
        System.out.println("处理成功的分片数少于总分片！");
    }
    if(shardInfo.getFailed() > 0){
        for(ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()){
            String reason = failure.reason();
            System.out.println(reason);
        }
    }
}
```

测试文档修改

```java
@Test
public void testUpdate() throws IOException {
    //创建请求
    UpdateRequest request = new UpdateRequest("test_posts","3");
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("user","tomas Lee");
    request.doc(jsonMap);
    //====可选参数=============
    request.timeout("1s");
    request.retryOnConflict(3); // 重试次数
    //执行
    UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
    //获取结果
    updateResponse.getId();
    updateResponse.getIndex();

    //判断结果
    if(updateResponse.getResult() == DocWriteResponse.Result.CREATED){
        DocWriteResponse.Result result = updateResponse.getResult();
        System.out.println("CREATE:"+result);
    }else if(updateResponse.getResult() == DocWriteResponse.Result.UPDATED){
        DocWriteResponse.Result result = updateResponse.getResult();
        System.out.println("UPDATE:"+result);
    }else if(updateResponse.getResult() == DocWriteResponse.Result.DELETED){
        DocWriteResponse.Result result = updateResponse.getResult();
        System.out.println("DELETE:"+result);
    }else if(updateResponse.getResult() == DocWriteResponse.Result.NOOP){
        DocWriteResponse.Result result = updateResponse.getResult();
        System.out.println("NOOP:"+result);
    }
}
```

删除测试文档

```java
@Test
public void testDelete() throws IOException {
    DeleteRequest request = new DeleteRequest("test_posts","3");
    DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
    deleteResponse.getId();
    deleteResponse.getIndex();
    DocWriteResponse.Result result = deleteResponse.getResult();
    System.out.println(result);
}
```

批处理测试文档

```java
@Test
public void testBulk() throws IOException {
    BulkRequest request = new BulkRequest();
 /*   request.add(new IndexRequest("post").id("1").source(XContentType.JSON,"field","1"));
    request.add(new IndexRequest("post").id("2").source(XContentType.JSON,"field","2"));*/
    request.add(new UpdateRequest("post","1").doc(XContentType.JSON,"field","3"));
    request.add(new DeleteRequest("post").id("2"));

    BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

    for (BulkItemResponse bulkItemResponse : bulkResponse) {
        DocWriteResponse response = bulkItemResponse.getResponse();
        switch (bulkItemResponse.getOpType()){
            case INDEX:
                IndexResponse indexResponse = (IndexResponse) response;
                System.out.println("INDEX:"+indexResponse.getResult());
                break;
            case CREATE:
                IndexResponse createResponse = (IndexResponse) response;
                System.out.println("Create:"+createResponse.getResult());
                break;
            case UPDATE:
                UpdateResponse updateResponse = (UpdateResponse) response;
                System.out.println("UPDATE:"+updateResponse.getResult());
                break;
            case DELETE:
                DeleteResponse deleteResponse = (DeleteResponse) response;
                System.out.println("DELETE:"+ deleteResponse.getResult());
                break;
        }
    }
```

#### es内部机制

##### 	es分布式基础

es对复杂分布式机制的透明隐藏特性

- 分布式机制：分布式数据存储及共享。
- 分片机制：数据存储到哪个分片，副本数据写入。
- 集群发现机制：cluster discovery。新启动es实例，自动加入集群。
- shard负载均衡：大量数据写入及查询，es会将数据平均分配。
- shard副本：新增副本数，分片重分配

es垂直扩容和水平扩容

- 垂直：使用更加强大的服务器替代老服务器。但单机存储及运算能力有上线。且成本直线上升。如10t服务器1万。单个10T服务器可能20万。
- 水平扩容：采购更多服务器，加入集群。大数据。
- 生产环境下一般使用水平扩容

新增或减少es实例时，es集群会将数据重新分配 

rebalance：服务器负载过轻、过重，es将某些分片转移到此机器或者移走。

master节点：

1. 管理es集群的元数据，创建删除索引，维护索引的元数据，节点的增加或减少
2. 默认情况选择一台机器为master一个节点上可以有多个分片

   ##### 分片shard、副本replica机制

3. 每个index包含一个或多个shard
4. 每个shard都是一个最小工作单元，承载部分数据，lucene实例，完整的建立索引和处理请求的能力
5. 增减节点时，shard会自动在nodes中负载均衡
6. primary shard和replica shard，每个document肯定只存在于某一个primary shard以及其对应的replica shard中，不可能存在于多个primary shard
7. replica shard是primary shard的副本，负责容错，以及承担读请求负载
8. primary shard的数量在创建索引的时候就固定了，replica shard的数量可以随时修改
9. primary shard的默认数量是1，replica默认是1，默认共有2个shard，1个primary shard，1个replica shard
10. 注意：es7以前primary shard的默认数量是5，replica默认是1，默认有10个shard，5个primary shard，5个replica shard
11. primary shard不能和自己的replica shard放在同一个节点上（否则节点宕机，primary shard和副本都丢失，起不到容错的作用），但是可以和其他primary shard的replica shard放在同一个节点上

单node创建index

1. 单node环境下，创建一个index，有3个primary shard，3个replica shard 
2. 集群status是yellow 
3. 这个时候，只会将3个primary shard分配到仅有的一个node上去，另外3个replica shard是无法分配的 
4. 集群可以正常工作，但是一旦出现节点宕机，数据全部丢失，而且集群不可用，无法承接任何请求

横向扩容

- 分片自动负载均衡，分片向空闲机器转移。
- 每个节点存储更少分片，系统资源给与每个分片的资源更多，整体集群性能提高。
- 扩容极限：节点数大于整体分片数，则必有空闲机器。
- 超出扩容极限时，可以增加副本数，如设置副本数为2，总共3*3=9个分片。9台机器同时运行，存储和搜索性能更强。容错性更好。
- 容错性：只要一个索引的所有主分片在，集群就就可以运行。

情景：master宕机，所有主分片不是active，集群状态为red

1. 容错第一步：重新选举master节点，承担master相关功能

2. 容错第二部：新master将丢失的主分片的副本提升为主分片，集群为yellow，现在少副本分片

3. #### 容错第三步：重启node，新master会感知到新节点的加入，将缺失的副本copy到node上node降级为从节点，集群状态为green

#### 文档存储机制

##### 	数据路由

- 数据路由：一个文档，最终会落在主分片的一个分片上，到底应该在哪一个分片
- 路由算法：shard = hash(routing) % number_of_primary_shards
- routing值，默认是_id，也可以手动指定，相同的routing值，每次过来，从hash函数中，产出的hash值一定是相同的
- 手动指定routing：PUT /test_index/_doc/15?routing=num。手动指定已有数据的一个属性为路由值，好处是可以定制一类文档数据存储到一个分片中。缺点是设计不好，会造成数据倾斜。
- 涉及到以往数据的查询搜索，所以一旦建立索引，主分片数不可变，路由算法取模出错，结果没找到， 间接导致数据丢失。


##### 	文档增删改机制

1. 客户端选择一个node发送请求过去，这个node就是coordinating node（协调节点）

2. coordinating node，对document进行路由，将请求转发给对应的node（有primary shard）

3. 实际的node上的primary shard处理请求，然后将数据同步到replica node。

4. coordinating node，如果发现primary node和所有replica node都搞定之后，就返回响应结果给客户端。

   ![](https://img2018.cnblogs.com/blog/616011/201901/616011-20190118103922952-1763308960.png)

   ##### 文档查询机制

5. 客户端发送请求到任意一个node，成为coordinate node

6. coordinate node对document进行路由，将请求转发到对应的node，此时会使用round-robin随机轮询算法，在primary shard以及其所有replica中随机选择一个，让读请求负载均衡

7. 接收请求的node返回document给coordinate node

8. coordinate node返回document给客户端

9. 特殊情况：document如果还在建立索引过程中，可能只有primary shard有，任何一个replica shard都没有，此时可能会导致无法读取到document，但是document完成索引建立之后，primary shard和replica shard就都有了。

   ![](https://img2018.cnblogs.com/blog/616011/201901/616011-20190118135030380-27647037.png)

   ​

   ##### bulk api奇特json格式

```json
POST /_bulk
{ "delete": { "_index": "test_index",  "_id": "5" }} \n
{ "create": { "_index": "test_index",  "_id": "14" }}\n
{ "test_field": "test14" }\n
{ "update": { "_index": "test_index",  "_id": "2"} }\n
{ "doc" : {"test_field" : "bulk test"} }\n
```

- 不用将其转换为json对象，不会出现内存中的相同数据的拷贝，直接按照换行符切割json
- 对每两个一组的json，读取meta，进行document路由
- 直接将对应的json发送到node上去

#### Mapping映射入门

##### 	mapping映射

自动或手动为index中的_doc建立的一种数据结构和相关配置

动态映射：dynamic mapping，自动为我们建立index，以及对应的mapping，mapping中包含了每个field对应的数据类型，以及如何分词等设置。

```json
PUT /website/_doc/1
{
  "post_date": "2019-01-01",
  "title": "my first article",
  "content": "this is my first article in this website",
  "author_id": 11400
}

GET  /website/_mapping/
{
  "website" : {
    "mappings" : {
      "properties" : {
        "author_id" : {
          "type" : "long"
        },
        "content" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "post_date" : {
          "type" : "date"
        },
        "title" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    }
  }
}
```

##### 	精确匹配和全文搜索对比分析

exact value：2019-01-01，exact value，搜索的时候，必须输入2019-01-01，才能搜索出来

full test

- 缩写 vs. 全称：cn vs. china
- 格式转化：like liked likes
- 大小写：Tom vs tom
- 同义词：like vs love

重建倒排索引

- normalization正规化，建立倒排索引的时候，会执行一个操作，也就是说对拆分出的各个单词进行相应的处理，以提升后面搜索的时候能够搜索到相关联的文档的概率

- 时态的转换，单复数的转换，同义词的转换，大小写的转换


##### 	分词器

- 分词器：切分词语，normalization（提升recall召回率）。
- 给你一段句子，然后将这段句子拆分成一个一个的单个的单词，同时对每个单词进行normalization（时态转换，单复数转换）。
- recall，召回率：搜索的时候，增加能够搜索到的结果的数量。

组成部分

- character filter：在一段文本进行分词之前，先进行预处理，比如说最常见的就是，过滤html标签（`hello` --> hello），& --> and（I&you --> I and you）

- tokenizer：分词，hello you and me --> hello, you, and, me

- token filter：lowercase，stop word，synonymom，dogs --> dog，liked --> like，Tom --> tom，a/the/an --> 干掉，mother --> mom，small --> little


stop word 停用词： 了 的 呢。

内置分词器

- standard analyzer标准分词器：set, the, shape, to, semi, transparent, by, calling, set_trans, 5（默认的是standard）
- simple analyzer简单分词器：set, the, shape, to, semi, transparent, by, calling, set, trans
- whitespace analyzer：Set, the, shape, to, semi-transparent, by, calling, set_trans(5)

  ##### query string 根据字段分词

query string必须以和index建立时相同的analyzer进行分词

```json
#测试分词器
GET /_analyze
{
  "analyzer": "standard",
  "text": "Text to analyze 80"
}

"tokens" : [
    {
      "token" : "text",
      "start_offset" : 0,
      "end_offset" : 4,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {....},{.........},{..........}
  ]

#token 实际存储的term 关键字
#position 在此词条在原文本中的位置
#start_offset/end_offset字符在原始字符串中的位置
```

##### 	mapping核心数据类型以及dynamic mapping

核心数据类型

- string ：text and keyword
- byte、short、integer、long、float、double
- boolean、date

查看mapping： GET /index/_mapping

##### 	手动管理mapping

先创建索引、再创建映射

```json
PUT book/_doc/1

PUT book/_mapping
{
	"properties": {
           "name": {
                  "type": "text"
            },
           "description": {
              "type": "text",
              "analyzer":"english",
              "search_analyzer":"english"
           },
           "pic":{
             "type":"text",
             "index":false
           },
           "studymodel":{
             "type":"text"
           }
    }
```

index属性指定是否索引。默认为index=true，即要进行索引，只有进行索引才可以从索引库搜索到。但是也有一些内容不需要索引，比如：商品图片地址只被用来展示图片，不进行搜索图片，此时可以将index设置为fal

keyword：keyword字段为关键字字段，通常搜索keyword是按照整体搜索，所以创建keyword字段的索引时是不进行分词的，比如：邮政编码、手机号码、身份证等。keyword字段通常用于过虑、排序、聚合等。

date：通过format设置日期格式   "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd"

数值类型

尽量选择范围小的类型，提高搜索效率

对于浮点数尽量用比例因子，比如一个价格字段，单位为元，我们将比例因子设置为100这在ES中会按 分 存储，映射如下：

```json
"price": {
        "type": "scaled_float",
        "scaling_factor": 100
  },
#使用比例因子的好处是整型比浮点型更易压缩，节省磁盘空间。
```

不能修改映射：已有数据按照映射早已分词存储好。如果修改，那这些存量数据怎么办。

```json
PUT /book/_mapping/
{
  "properties" : {
    "new_field" : {
      "type" :    "text",
     "index":    "false"
    }
  }
}
```

删除索引来删除映射

##### 	复杂数据类型

multivalue field：{ "tags": [ "tag1", "tag2" ]}

empty field：null，[]，[null]

object field：	

```json
PUT /company/_doc/1
{
  "address": {
    "country": "china",
    "province": "guangdong",
    "city": "guangzhou"
  },
  "name": "jack",
  "age": 27,
  "join_date": "2019-01-01"
}
#底层存储形式
{
    "name":            [jack],
    "age":          [27],
    "join_date":      [2017-01-01],
    "address.country":         [china],
    "address.province":   [guangdong],
    "address.city":  [guangzhou]
}


{
    "authors": [
        { "age": 26, "name": "Jack White"},
        { "age": 55, "name": "Tom Jones"},
        { "age": 39, "name": "Kitty Smith"}
    ]
}
{
    "authors.age":    [26, 55, 39],
    "authors.name":   [jack, white, tom, jones, kitty, smith]
}
```



