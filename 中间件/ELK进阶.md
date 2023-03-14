前置知识：

1. 一个索引数据量太大，分片会存储在多个es node中
2. 一个文档只会存储于一个主分片上

主分片不可变，取模会出错

插入时使用ik_max_word  查询使用ik_small

time_out：搜索时，请求必定要跨所有的主分片，数据量太大，搜索完毕要1h，用户等不及，丢失业务，设置time_out 指定每个shard只能在给定时间查询数据，能有几条返回几条，保住业务。

match_alll 全部匹配  match 全文检索  

步骤一：search phase得到id，score等少量信息，汇总到协调节点排序

步骤二：fetch phase 协调节点构建mget，到各个shard上获取数据，给客户端返回。

分桶就是按group by 后面的数据分

父层聚合可以使用子层聚合结果排序

#### 索引Index入门

##### 	索引管理

```json
#创建索引
PUT /my_index
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "field1":{
        "type": "text"
      },
      "field2":{
        "type": "text"
      }
    }
  },
  "aliases": {
    "default_index": {}
  } 
}

#插数据
POST /my_index/_doc/1
{
	"field1":"java",
	"field2":"js"
}
#查询索引
GET /my_index/_mapping
GET /my_index/_setting

#修改索引
PUT /my_index/_settings
{
    "index" : {
        "number_of_replicas" : 2
    }
}

#删除索引
DELETE /my_index
```

##### 	定制分词器

默认分词器standard：（character filter，tokenizer，token filter）组件

- standard tokenizer：以单词边界进行切分
- standard token filter：什么都不做
- lowercase token filter：将所有字母转换为小写
- stop token filer（默认被禁用）：移除停用词，比如a the it等等

```json
#启动分词器
PUT /my_index
{
  "settings": {
    "analysis": {
      "analyzer": {
        "es_std": {
          "type": "standard",
          "stopwords": "_english_"
        }
      }
    }
  }
}

#测试分词器
GET /my_index/_analyze
{
  "analyzer": "standard", 
  "text": "a dog is in the house"
}

#定制自定义分词器
PUT /my_index
{
  "settings": {
    "analysis": {
      "char_filter": {
        "&_to_and": {
          "type": "mapping",
          "mappings": ["&=> and"]
        }
      },
      "filter": {
        "my_stopwords": {
          "type": "stop",
          "stopwords": ["the", "a"]
        }
      },
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "char_filter": ["html_strip", "&_to_and"],
          "tokenizer": "standard",
          "filter": ["lowercase", "my_stopwords"]
        }
      }
    }
  }
}

#设置字段使用自定义分词器
PUT /my_index/_mapping/
{
  "properties": {
    "content": {
      "type": "text",
      "analyzer": "my_analyzer"
    }
  }
}
```

##### 	type弃用

index中用来区分类似的数据的，类似的数据，但是可能有不同的fields，而且有不同的属性来控制索引建立、分词器. field的value，在底层的lucene中建立索引的时候，全部是opaque bytes类型，不区分类型的。 lucene是没有type的概念的，在document中，实际上将type作为一个document的field来存储，即_type，es通过_type来进行type的过滤和筛选。

存储机制

```json
{
   "goods": {
      "mappings": {
         "electronic_goods": {
            "properties": {
               "name": {
                  "type": "string",
               },
               "price": {
                  "type": "double"
               },
               "service_period": {
                  "type": "string"
                   }			
                }
         },
         "fresh_goods": {
            "properties": {
               "name": {
                  "type": "string",
               },
               "price": {
                  "type": "double"
               },
               "eat_period": {
              		"type": "string"
               }
                }
         }
      }
   }
}
PUT /goods/electronic_goods/1
{
  "name": "小米空调",
  "price": 1999.0,
  "service_period": "one year"
}
PUT /goods/fresh_goods/1
{
  "name": "澳洲龙虾",
  "price": 199.0,
  "eat_period": "one week"
}
#底层存储
{
   "goods": {
      "mappings": {
        "_type": {
          "type": "string",
          "index": "false"
        },
        "name": {
          "type": "string"
        }
        "price": {
          "type": "double"
        }
        "service_period": {
          "type": "string"
        },
        "eat_period": {
          "type": "string"
        }
      }
   }
}
#底层数据存储
{
  "_type": "electronic_goods",
  "name": "小米空调",
  "price": 1999.0,
  "service_period": "one year",
  "eat_period": ""
}
{
  "_type": "fresh_goods",
  "name": "澳洲龙虾",
  "price": 199.0,
  "service_period": "",
  "eat_period": "one week"
}
```

同一索引下，不同type的数据存储其他type的field 大量空值，造成资源浪费。所以，不同类型数据，要放到不同的索引中。es9中，将会彻底删除type。

##### 	定制dynamic mapping

dynamic字段

- true：遇到陌生字段，就进行dynamic mapping
- false：新检测到的字段将被忽略。这些字段将不会被索引，因此将无法搜索，但仍将出现在返回点击的源字段中。这些字段不会添加到映射中，必须显式添加新字段。
- strict：遇到陌生字段，就报错

```json
PUT /my_index
{
    "mappings": {
      "dynamic": "strict",
       "properties": {
        "title": {
          "type": "text"
        },
        "address": {
          "type": "object",
          "dynamic": "true"
        }
	    }
    }
}
```

es会根据传入的值，推断类型：null(没字段添加)、boolean、integer(long)、object、array、string(date text double  long  )、float point number(float)

date_detection 日期探测：默认会按照一定格式识别date，比如yyyy-MM-dd。但是如果某个field先过来一个2017-01-01的值，就会被自动dynamic mapping成date

```json
PUT /my_index
{
    "mappings": {
      "date_detection": false,
      properties:{......},
      }
 }
  
  #自定义日期格式
  PUT my_index
{
  "mappings": {
    "dynamic_date_formats": ["MM/dd/yyyy"]
  }
}
```

numeric_detection 数字探测：某些应用程序或语言有时可能将数字呈现为字符串。通常正确的解决方案是显式地映射这些字段，但是可以启用数字检测（默认情况下禁用）来自动完成这些操作。

```json
PUT my_index
{
  "mappings": {
    "numeric_detection": true
  }
}
```

定制自己的dynamic mapping template

```json
PUT /my_index
{
    "mappings": {
            "dynamic_templates": [
                { 
                  "en": {
                      "match":              "*_en", 
                      "match_mapping_type": "string",
                      "mapping": {
                          "type":           "text",
                          "analyzer":       "english"
                      }
                }                  
            }
        ]
	}
}
```

搜索

```json
#结构化搜索
{
    "strings_as_keywords": {
        "match_mapping_type": "string",
        "mapping": {
            "type": "keyword"
        }
    }
}
#仅搜索
{
    "strings_as_text": {
        "match_mapping_type": "string",
        "mapping": {
            "type": "text"
        }
    }
}
#norms 不关心评分
{
    "strings_as_keywords": {
        "match_mapping_type": "string",
        "mapping": {
            "type": "text",
            "norms": false,
            "fields": {
                "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                }
            }
        }
    }
}
```

##### 	零停机重建索引

引入：插入一个字符串2019-01-01被视为date其实为string类型，而又不能修改字段类型。需要reindex，重新建立一个索引，将旧索引的数据查询出来，再导入新索引。终端java应用，已经在使用old_index在操作了，难道还要去停止java应用，修改使用的index为new_index，才重新启动java应用吗？这个过程中，就会导致java应用停机，可用性降低。

解决方法

```json
#给java应用一个别名，这个别名是指向旧索引的，java应用先用着，java应用先用prod_index alias来操作，此时实际指向的是旧的my_index
PUT /my_index/_alias/prod_index
#新建一个index，调整其title的类型为string
PUT /my_index_new
{
  "mappings": {
    "properties": {
		"title": {
         "type": "text"
        }
    }
  }
}
#使用scroll api将数据批量查询出来
GET /my_index/_search?scroll=1m
{
    "query": {
        "match_all": {}
    },    
    "size":  1
}
#采用bulk api将scoll查出来的一批数据，批量写入新索引
POST /_bulk
{ "index":  { "_index": "my_index_new", "_id": "1" }}
{ "title":    "2019-09-10" }
#反复循环8~9，查询一批又一批的数据出来，采取bulk api将每一批数据批量写入新索引
#将prod_index alias切换到my_index_new上去，java应用会直接通过index别名使用新的索引中的数据，java应用程序不需要停机，零提交，高可用
POST /_aliases
{
    "actions": [
        { "remove": { "index": "my_index", "alias": "prod_index" }},
        { "add":    { "index": "my_index_new", "alias": "prod_index" }}
    ]
}
#直接通过prod_index别名来查询，是否ok
GET /prod_index/_search
```

#### IK分词器

##### 	ik使用

- ik_max_word: 会将文本做最细粒度的拆分，比如会将“中华人民共和国人民大会堂”拆分为“中华人民共和国，中华人民，中华，华人，人民共和国，人民大会堂，人民大会，大会堂”，会穷尽各种可能的组合；
- ik_smart: 会做最粗粒度的拆分，比如会将“中华人民共和国人民大会堂”拆分为“中华人民共和国，人民大会堂”。

使用：存储时，使用ik_max_word，搜索时，使用ik_smart

```json
PUT /my_index 
{
  "mappings": {
      "properties": {
        "text": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_smart"
        }
      }
  }
}
```

##### 	ik配置文件

- IKAnalyzer.cfg.xml：用来配置自定义词库
- main.dic：ik原生内置的中文词库，总共有27万多条，只要是这些单词，都会被分在一起
- preposition.dic: 介词
- quantifier.dic：放了一些单位相关的词，量词
- suffix.dic：放了一些后缀
- surname.dic：中国的姓氏
- stopword.dic：英文停用词
- ik原生最重要的两个配置文件
  - main.dic：包含了原生的中文词语，会按照这个里面的词语去分词
  - stopword.dic：包含了英文的停用词

自定义词库 

自己补充自己的最新的词语，到ik的词库里面 IKAnalyzer.cfg.xml：ext_dict，创建mydict.dic。补充自己的词语，然后需要重启es，才能生效

#### Java实现索引管理

```java
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class TestIndex {
    @Autowired
    RestHighLevelClient client;

    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("itheima_book");
        //setting
        createIndexRequest.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","1"));
        //mapping

        //设置mapping方法
        createIndexRequest.mapping("_doc"," {\n" +
                " \t\"properties\": {\n" +
                "            \"name\":{\n" +
                "             \"type\":\"keyword\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\"\n" +
                "           },\n" +
                "            \"price\":{\n" +
                "             \"type\":\"long\"\n" +
                "           },\n" +
                "           \"pic\":{\n" +
                "             \"type\":\"text\",\n" +
                "             \"index\":false\n" +
                "           }\n" +
                " \t}\n" +
                "}", XContentType.JSON);

     /*   Map<String,Object> message = new HashMap<>();
        Map<String,Object> properties = new HashMap<>();
        Map<String,Object> field1 = new HashMap<>();
        field1.put("type","text");
        Map<String,Object> field2 = new HashMap<>();
        field2.put("type","text");
        properties.put("field1",field1);
        properties.put("field2",field2);
        message.put("properties",properties);
        createIndexRequest.mapping(message);

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
             {
                 builder.startObject("field1");
                 {
                     builder.field("type","text");
                 }
                 builder.endObject();
                 builder.startObject("field2");
                 {
                     builder.field("type","text");
                 }
                 builder.endObject();
              }
              builder.endObject();
        }
        builder.endObject();
        createIndexRequest.mapping(builder);
*/
        createIndexRequest.alias(new Alias("itheima_book_new"));
        //============================可选参数=======================================
        //主节点超时时间
        createIndexRequest.masterNodeTimeout(TimeValue.timeValueSeconds(3));
        //超时时间
        createIndexRequest.timeout(TimeValue.timeValueSeconds(3));
        //设置构造索引api返回响应之前等待活动分片的数量
        createIndexRequest.waitForActiveShards(ActiveShardCount.ALL);
        //createIndexRequest.waitForActiveShards(ActiveShardCount.from(2));

        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
        String index = createIndexResponse.index();


        System.out.println("acknowledge: " + acknowledged);
        System.out.println("shardsAcknowledged: " + shardsAcknowledged);
    }


    @Test
    public void testCreateIndexAsync() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("itheima_book1");
        //setting
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "1"));
        //mapping

        //设置mapping方法
        createIndexRequest.mapping("_doc", " {\n" +
                " \t\"properties\": {\n" +
                "            \"name\":{\n" +
                "             \"type\":\"keyword\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\"\n" +
                "           },\n" +
                "            \"price\":{\n" +
                "             \"type\":\"long\"\n" +
                "           },\n" +
                "           \"pic\":{\n" +
                "             \"type\":\"text\",\n" +
                "             \"index\":false\n" +
                "           }\n" +
                " \t}\n" +
                "}", XContentType.JSON);

        createIndexRequest.alias(new Alias("itheima_book_new"));
        //============================可选参数=======================================
        //主节点超时时间
        createIndexRequest.masterNodeTimeout(TimeValue.timeValueSeconds(3));
        //超时时间
        createIndexRequest.timeout(TimeValue.timeValueSeconds(3));
        //设置构造索引api返回响应之前等待活动分片的数量
        createIndexRequest.waitForActiveShards(ActiveShardCount.ALL);

        IndicesClient indices = client.indices();

        ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                boolean acknowledged = createIndexResponse.isAcknowledged();
                boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
                String index = createIndexResponse.index();
                System.out.println("acknowledge: " + acknowledged);
                System.out.println("shardsAcknowledged: " + shardsAcknowledged);

            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };
        client.indices().createAsync(createIndexRequest,RequestOptions.DEFAULT,listener);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("itheima_book1");
 /*       ActionListener<AcknowledgedResponse> acknowledgedResponseActionListener = new ActionListener<AcknowledgedResponse>() {
            @Override
            public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
                boolean acknowledged = delete.isAcknowledged();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };*/
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println("acknowledge:  " + acknowledged);
    }


    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("itheima_book1");
        request.local(false);//从主节点返回本地索引信息的状态
        request.humanReadable(true); //适合人类格式返回
        request.includeDefaults(false); //是否返回索引的所有的默认配置

        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("exist: " + exists);
    }

    //关闭索引
    @Test
    public void testCloseIndex() throws IOException {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest("itheima_book");
        CloseIndexResponse close = client.indices().close(closeIndexRequest, RequestOptions.DEFAULT);
        boolean acknowledged = close.isAcknowledged();
        System.out.println("acknowledge: " + acknowledged);
    }

    //打开索引
    @Test
    public void testOpenIndex() throws IOException {
        OpenIndexRequest request = new OpenIndexRequest("itheima_book");
        OpenIndexResponse open = client.indices().open(request, RequestOptions.DEFAULT);
        boolean acknowledged = open.isAcknowledged();
        System.out.println("acknowledge: " + acknowledged);

    }
}
```

#### search搜索入门

##### 	搜索语法入门

GET /book/_search

```
{
  "took" : 969,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 3,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "book",
        "_type" : "_doc",
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "name" : "Bootstrap开发",
          "description" : "Bootstrap是由Twitter推出的一个前台页面开发css框架，是一个非常流行的开发框架，此框架集成了多种页面效果。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长css页面开发的程序人员）轻松的实现一个css，不受浏览器限制的精美界面css效果。",
          "studymodel" : "201002",
          "price" : 38.6,
          "timestamp" : "2019-08-25 19:11:35",
          "pic" : "group1/M00/00/00/wKhlQFs6RCeAY0pHAAJx5ZjNDEM428.jpg",
          "tags" : [
            "bootstrap",
            "dev"
          ]
        }
      }
      },
    ]
  }
}
```

- took：耗费了几毫秒
- timed_out：是否超时，这里是没有
- _shards：到几个分片搜索，成功几个，跳过几个，失败几个。
- hits.total：查询结果的数量，3个document
- hits.max_score：score的含义，就是document对于一个search的相关度的匹配分数，越相关，就越匹配，分数也高
- hits.hits：包含了匹配搜索的document的所有详细数据

传参：GET /book/_search?q=name:java&sort=price:desc

timeout：GET /book/_search?timeout=10ms(返回10ms内查询到的数据)

##### 	multi-index 多索引搜索

/index1,index2/_search：同时搜索两个index下的数据

##### 	分页搜索

参数：size，from

GET /book/_search?size=10&from=0

deep paging

- 根据相关度评分倒排序，所以分页过深，协调节点会将大量数据聚合分析。

- deep paging：每次搜索的关键词不同，所有的评分在每个shard中完全不一样的分数分布，可能某个shard中的第一条数据都比另一个shard中第1000个数据评分兜底，每个分片取10完全错误。

- 正确：每个节点都会将10000条数据给协调节点，协调节点会在内存中再排序，再拿出9990-10000的10条数据，返回客户端

- 性能问题：
  - 消耗网络带宽，因为所搜过深的话，各 shard 要把数据传递给 coordinate node，这个过程是有大量数据传递的，消耗网络。
  - 消耗内存，各 shard 要把数据传送给 coordinate node，这个传递回来的数据，是被 coordinate node 保存在内存中的，这样会大量消耗内存。
  - 消耗cup，coordinate node 要把传回来的数据进行排序，这个排序过程很消耗cpu。 所以：鉴于deep paging的性能问题，所有应尽量减少使用。

  ​


##### 	query string基础语法

基础语法：

GET /book/\_search?q=+name:java            name含有java                                                                                                                   GET /book/\_search?q=-name:java             name不含java                                                                                                                                                                                                                                                                                                               GET /book/_search?q=java   直接可以搜索所有的field，任意一个field包含指定的关键字就可以搜索出来。我们在进行中搜索的时候，难道是对document中的每一个field都进行一次搜索吗？不是的。

es中\_all元数据。建立索引的时候，插入一条docunment，es会将所有的field值经行全量分词，把这些分词，放到\_all field中。在搜索的时候，没有指定field，就在_all搜索。

```json
{
    name:jack
    email:123@qq.com
    address:beijing
}
all : jack,123@qq.com,beijing
```

##### 	query DSL入门

DSL:Domain Specified Language，特定领域的语言

```json
#查询全部
GET /book/_search
{
  "query": { "match_all": {} }
}
#排序 GET /book/_search?sort=price:desc
GET /book/_search 
{
    "query" : {
        "match" : {
            "name" : " java"
        }
    },
    "sort": [
        { "price": "desc" }
    ]
}
#分页查询
GET  /book/_search 
{
  "query": { "match_all": {} },
  "from": 0,
  "size": 1
}
#指定返回字段
GET /book/_search 
{
  "query": { "match_all": {} },
  "_source": ["name", "studymodel"]
}
```

组合多个搜索条件

title必须包含elasticsearch，content可以包含elasticsearch也可以不包含，author_id必须不为111

```json
GET /website/_doc/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "elasticsearch"
          }
        }
      ],
      "should": [
        {
          "match": {
            "content": "elasticsearch"
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "author_id": 111
          }
        }
      ]
    }
  }
}
```

##### 	DSL语法练习

```json
#multi_match
GET /book/_search
{
  "query": {
    "multi_match": {
      "query": "java程序员",
      "fields": ["name", "description"]
    }
  }
}
#range query 范围查询
GET /book/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 80,
		"lte": 90
      }
    }
  }
}
#term query 字段为keyword时，存储和搜索都不分词
GET /book/_search
{
  "query": {
    "term": {
      "description": "java程序员"
    }
  }
}
#terms query
GET /book/_search
{
    "query": { "terms": { "tag": [ "search", "full_text", "nosql" ] }}
}
#exist query 查询有某些字段值的文档
GET /_search
{
    "query": {
        "exists": {
            "field": "join_date"
        }
    }
}
#Fuzzy query：返回包含与搜索词类似的词的文档，该词由Levenshtein编辑距离度量。
#更改角色（box→fox）
#删除字符（aple→apple）
#插入字符（sick→sic）
#调换两个相邻字符（ACT→CAT）
GET /book/_search
{
    "query": {
        "fuzzy": {
            "description": {
                "value": "jave"
            }
        }
    }
}
#IDs
GET /book/_search
{
    "query": {
        "ids" : {
            "values" : ["1", "4", "100"]
        }
    }
}
#prefix 前缀查询
GET /book/_search
{
    "query": {
        "prefix": {
            "description": {
                "value": "spring"
            }
        }
    }
}
#regexp query 正则查询
GET /book/_search
{
    "query": {
        "regexp": {
            "description": {
                "value": "j.*a",
                "flags" : "ALL",
                "max_determinized_states": 10000,
                "rewrite": "constant_score"
            }
        }
    }
}

```

##### 	Filter

用户查询description中有"java程序员"，并且价格大于80小于90的数据

```json
GET /book/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "description": "java程序员"
          }
        }
      ],
      "filter": {
        "range": {
          "price": {
            "gte": 80,
		     "lte": 90
          }
        }
      }
    }
  }
}
```

filter和query对比

filter：仅仅只是按照搜索条件过滤出需要的数据而已，不计算任何相关度分数，对相关度没有任何影响。		query：会去计算每个document相对于搜索条件的相关度，并按照相关度进行排序。

filter：不需要计算相关度分数，不需要按照相关度分数进行排序，同时还有内置的自动cache最常使用filter的数据query：相反，要计算相关度分数，按照分数进行排序，而且无法cache结果

##### 	定位错误语法

```json
GET /book/_validate/query?explain
{
  "query": {
    "mach": {
      "description": "java程序员"
    }
  }
}

{
  "valid" : false,
  "error" : "org.elasticsearch.common.ParsingException: no [query] registered for [mach]"
}
{
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "failed" : 0
  },
  "valid" : true,
  "explanations" : [
    {
      "index" : "book",
      "valid" : true,
      "explanation" : "description:java description:程序员"
    }
  ]
}
```

##### 	定制排序信息

默认排序规则按照\_score降序排序某些情况下，可能没有有用的_score，比如说filter，引入const\_score

```json
GET /book/_search 
{
  "query": {
    "constant_score": {
      "filter" : {
            "term" : {
                "studymodel" : "201001"
            }
        }
    }
  },
  "sort": [
    {
      "price": {
        "order": "asc"
      }
    }
  ]
}
```

Text字段排序

如果对一个text field进行排序，结果往往不准确，因为分词后是多个单词，再排序就不是我们想要的结果了。通常解决方案是，将一个text field建立两次索引，一个分词，用来进行搜索；一个不分词，用来进行排序。

```json
PUT /website 
{
  "mappings": {
  "properties": {
    "title": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword"
        }        
      }      
    },
    "content": {
      "type": "text"
    },
    "post_date": {
      "type": "date"
    },
    "author_id": {
      "type": "long"
    }
  }
 }
}

GET /website/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "title.keyword": {
        "order": "desc"
      }
    }
  ]
}
```

##### 	scroll分批查询

scoll搜索会在第一次搜索的时候，保存一个当时的视图快照，之后只会基于该旧的视图快照提供数据搜索，如果这个期间数据变更，是不会让用户看到的																每次发送scroll请求，我们还需要指定一个scoll参数，指定一个时间窗口，每次搜索请求只要在这个时间窗口内能完成就可以了。

```json
GET /book/_search?scroll=1m
{
  "query": {
    "match_all": {}
  },
  "size": 3
}


{
  "_scroll_id" : "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAMOkWTURBNDUtcjZTVUdKMFp5cXloVElOQQ==",
  "took" : 3,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 3,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
     
    ]
  }
}

#获得的结果会有一个scoll_id，下一次再发送scoll请求的时候，必须带上这个scoll_id
GET /_search/scroll
{
    "scroll": "1m", 
    "scroll_id" : "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAMOkWTURBNDUtcjZTVUdKMFp5cXloVElOQQ=="
}
```

与分页区别

- 分页给用户看的 deep paging

- #### scroll是用户系统内部操作，如下载批量数据，数据转移。零停机改变索引映射。

#### Java Api实现搜索

```java
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class TestSearch {
    @Autowired
    RestHighLevelClient client;

    //搜索全部记录
    @Test
    public void testSearchAll() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        //获取某些字段
        //searchSourceBuilder.fetchSource(new String[]{"name"},new String[]{});

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testPage() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0); //下标
        searchSourceBuilder.size(2);
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("id: " + id);
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testSearchIds() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds("1","3","100"));
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("id: " + id);
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testSearchMatch() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","java程序员"));
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("id: " + id);
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testSearchMuliMatch() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("java程序员","name","description"));
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("id: " + id);
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }

    @Test
    public void testSearchBoolAndFilterAndSort() throws IOException {
        SearchRequest request = new SearchRequest("book");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("java程序员", "name", "description");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("studymodel", "201001");
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.should(matchQueryBuilder);
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(50).lte(90));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("=====================================================");
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            float score = searchHit.getScore();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            Double price = (Double) sourceAsMap.get("price");
            System.out.println("id: " + id);
            System.out.println("name: " + name);
            System.out.println("description: " + description);
            System.out.println("price: " + price);
            System.out.println("=====================================================");
        }
    }
}
```

#### 评分机制详解

##### 	评分机制

TF/IDF算法：TF词频(Term Frequency)，IDF逆向文件频率(Inverse Document Frequency)

**Term frequency**：搜索文本中的各个词条在field文本中出现了多少次，出现次数越多，就越相关。

**Inverse document frequency**：搜索文本中的各个词条在整个索引的所有文档中出现了多少次，出现的次数越多，就越不相

##### 	Doc value

- 搜索的时候，要依靠倒排索引；排序的时候，需要依靠正排索引，看到每个document的每个field，然后进行排序，所谓的正排索引，其实就是doc values
- 在建立索引的时候，一方面会建立倒排索引，以供搜索用；一方面会建立正排索引，也就是doc values，以供排序，聚合，过滤等操作使用

- doc values是被保存在磁盘上的，此时如果内存足够，os会自动将其缓存在内存中，性能还是会很高；如果内存不足够，os会将其写入磁盘上


##### 	query phase

1. 搜索请求发送到某一个coordinate node，构构建一个priority queue，长度以paging操作from和size为准，默认为10
2. coordinate node将请求转发到所有shard，每个shard本地搜索，并构建一个本地的priority queue
3. 各个shard将自己的priority queue返回给coordinate node，并构建一个全局的priority queue

##### 	fetch phbase

1. coordinate node构建完priority queue之后，就发送mget请求去所有shard上获取对应的document
2. 各个shard将document返回给coordinate node
3. coordinate node将合并后的document结果返回给client客户端

#### 聚合入门

##### 	聚合示例

```json
#计算每个studymodel下的商品数量
GET /book/_search
{
  "size": 0, 
  "query": {
    "match_all": {}
  }, 
  "aggs": {
    "group_by_model": {
      "terms": { "field": "studymodel" }
    }
  }
}
#对text字段聚合   计算每个tags下的商品数量
PUT /book/_mapping/
{
  "properties": {
    "tags": {
      "type": "text",
      "fielddata": true
    }
  }
}
GET /book/_search
{
  "size": 0, 
  "query": {
    "match_all": {}
  }, 
  "aggs": {
    "group_by_tags": {
      "terms": { "field": "tags" }
    }
  }
}
#先分组，再算每组的平均值，计算每个tag下的商品的平均价格、照平均价格降序排序
GET /book/_search
{
    "size": 0,
    "aggs" : {
        "group_by_tags" : {
            "terms" : { 
              "field" : "tags" 
            },
           "order": {
                "avg_price": "desc"
              },
            "aggs" : {
                "avg_price" : {
                    "avg" : { "field" : "price" }
                }
            }
        }
    }
}
#按照指定的价格范围区间进行分组，然后在每组内再按照tag进行分组，最后再计算每组的平均价格
GET /book/_search
{
  "size": 0,
  "aggs": {
    "group_by_price": {
      "range": {
        "field": "price",
        "ranges": [
          {
            "from": 0,
            "to": 40
          },
          {
            "from": 40,
            "to": 60
          },
          {
            "from": 60,
            "to": 80
          }
        ]
      },
      "aggs": {
        "group_by_tags": {
          "terms": {
            "field": "tags"
          },
          "aggs": {
            "average_price": {
              "avg": {
                "field": "price"
              }
            }
          }
        }
      }
    }
  }
}
```

##### 	bucket和metric

- bucket：一个数据分组
- metric：对一个数据分组执行的统计，就是对一个bucket执行的某种聚合分析的操作，比如说求平均值，求最大值，求最小值


-   "size" : 0,只获取聚合结果，不要执行聚合的原始数据

-    terms：根据字段的值进行分组

- aggs：固定语法，要对一份数据执行分组聚合操作

- 对每个aggs需要起一个名字

  ​

```json
#按照日期分组聚合
GET /tvs/_search
{
   "size" : 0,
   "aggs": {
      "sales": {
         "date_histogram": {
            "field": "sold_date",
            "interval": "month", 
            "format": "yyyy-MM-dd",
            "min_doc_count" : 0, 
            "extended_bounds" : { 
                "min" : "2019-01-01",
                "max" : "2020-12-31"
            }
         }
      }
   }
}
#单个品牌与所有品牌销量对比
GET /tvs/_search 
{
  "size": 0, 
  "query": {
    "term": {
      "brand": {
        "value": "小米"
      }
    }
  },
  "aggs": {
    "single_brand_avg_price": {
      "avg": {
        "field": "price"
      }
    },
    "all": {
      "global": {},
      "aggs": {
        "all_brand_avg_price": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}
```

#### Java API实现

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestAggs {
    @Autowired
    RestHighLevelClient client;

    //按颜色分类
    @Test
    public void testAggs() throws IOException {
        //构建请求
        SearchRequest request = new SearchRequest("tvs");
        //请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group_by_color").field("color");
        //term聚合下面添加子聚合
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_price").field("price");
        termsAggregationBuilder.subAggregation(avgAggregationBuilder);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms group_by_color = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = group_by_color.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Aggregations aggregations1 = bucket.getAggregations();
            Avg avg_price = aggregations1.get("avg_price");
            double value = avg_price.getValue();
            System.out.println("key: " + key);
            System.out.println("docCount: " + docCount);
            System.out.println("avg_price: " + value);
            System.out.println("================================================");
        }
    }
    @Test
    public void testAggsAndMore() throws IOException {
        //构建请求
        SearchRequest request = new SearchRequest("tvs");
        //请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group_by_color").field("color");
        //term聚合下面添加子聚合
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_price").field("price");
        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("min_price").field("price");
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("max_price").field("price");
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("sum_price").field("price");
        termsAggregationBuilder.subAggregation(avgAggregationBuilder);
        termsAggregationBuilder.subAggregation(maxAggregationBuilder);
        termsAggregationBuilder.subAggregation(minAggregationBuilder);
        termsAggregationBuilder.subAggregation(sumAggregationBuilder);
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms group_by_color = aggregations.get("group_by_color");
        List<? extends Terms.Bucket> buckets = group_by_color.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Aggregations aggregations1 = bucket.getAggregations();
            Avg avg_price = aggregations1.get("avg_price");
            double avg_priceValue = avg_price.getValue();
            Max max_price = aggregations1.get("max_price");
            double max_priceValue = max_price.getValue();
            Min min_price = aggregations1.get("min_price");
            double min_priceValue = min_price.getValue();
            Sum sum_price = aggregations1.get("sum_price");
            double sum_priceValue = sum_price.getValue();
            System.out.println("key: " + key);
            System.out.println("docCount: " + docCount);
            System.out.println("avg_price: " + avg_priceValue);
            System.out.println("max_price: " + max_priceValue );
            System.out.println("min_price: " + min_priceValue );
            System.out.println("sum_price: " + sum_priceValue);
            System.out.println("================================================");
        }
    }
    @Test
    public void testAggsAndHistogram() throws IOException {
        //构建请求
        SearchRequest request = new SearchRequest("tvs");
        //请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram("by_histogram").field("price").interval(2000);

        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("income").field("price");
        histogramAggregationBuilder.subAggregation(sumAggregationBuilder);
        searchSourceBuilder.aggregation(histogramAggregationBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Histogram by_histogram = aggregations.get("by_histogram");
        List<? extends Histogram.Bucket> buckets = by_histogram.getBuckets();
        for (Histogram.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Aggregations aggregations1 = bucket.getAggregations();
            Sum sum_price = aggregations1.get("income");
            double sum_priceValue = sum_price.getValue();
            System.out.println("key: " + key);
            System.out.println("docCount: " + docCount);
            System.out.println("sumPriceValue: "+ sum_priceValue);
            System.out.println("================================================");
        }
    }
    @Test
    public void testAggsDateHistogram() throws IOException {
        //构建请求
        SearchRequest request = new SearchRequest("tvs");
        //请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("date_histogram").field("sold_date")
                .calendarInterval(DateHistogramInterval.QUARTER)
                .format("yyyy-MM-dd")
                .minDocCount(0)
                .extendedBounds(new ExtendedBounds("2019-01-01", "2020-12-31"));
        SumAggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("income").field("price");
        dateHistogramAggregationBuilder.subAggregation(sumAggregationBuilder);
        searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);
        request.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        ParsedDateHistogram date_histogram = aggregations.get("date_histogram");
        List<? extends Histogram.Bucket> buckets = date_histogram.getBuckets();
        for (Histogram.Bucket bucket : buckets) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            Aggregations aggregations1 = bucket.getAggregations();
            Sum income = aggregations1.get("income");
            double value = income.getValue();
            System.out.println("key: " + key);
            System.out.println("docCount: " + docCount);
            System.out.println("value: " + value);
            System.out.println("================================================");
        }
    }
}
```

#### es7 sql新特性

##### 	快速入门

```json
POST /_sql?format=txt
{
    "query": "SELECT * FROM tvs "
}
```

启动：elasticsearch-sql-cli.bat

##### 	sql翻译	

```json
POST /_sql/translate
{
    "query": "SELECT * FROM tvs "
}

{
  "size" : 1000,
  "_source" : false,
  "stored_fields" : "_none_",
  "docvalue_fields" : [
    {
      "field" : "brand"
    },
    {
      "field" : "color"
    },
    {
      "field" : "price"
    },
    {
      "field" : "sold_date",
      "format" : "epoch_millis"
    }
  ],
  "sort" : [
    {
      "_doc" : {
        "order" : "asc"
      }
    }
  ]
}
```

##### 	与其它dsl结合

```json
POST /_sql?format=txt
{
    "query": "SELECT * FROM tvs",
    "filter": {
        "range": {
            "price": {
                "gte" : 1200,
                "lte" : 2000
            }
        }
    }
}
```

##### java实现

前提： es拥有白金版功能，kibana中管理 -> 许可管理 开启白金版试用

依赖

```xml
 <dependency>
        <groupId>org.elasticsearch.plugin</groupId>
        <artifactId>x-pack-sql-jdbc</artifactId>
        <version>7.9.0</version>
    </dependency>
    
    <repositories>
        <repository>
            <id>elastic.co</id>
            <url>https://artifacts.elastic.co/maven</url>
        </repository>
    </repositories>
```

```java
public static void main(String[] args) {
        try  {
            Connection connection = DriverManager.getConnection("jdbc:es://http://localhost:9200");
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(
                    "select * from tvs");
            while(results.next()){
                System.out.println(results.getString(1));
                System.out.println(results.getString(2));
                System.out.println(results.getString(3));
                System.out.println(results.getString(4));
                System.out.println("============================");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
```

#### LogStach

![](https://www.ydlclass.com/doc21xnv/assets/1573291947262-0ca3aea7.png)

##### 	基本构成

logstash：一个数据抽取工具，将数据从一个地方转移到另一个地方

配置文件 config下 自己建一个 xx.conf

```conf
input {
    #输入插件
}
filter {
    #过滤匹配插件
}
output {
    #输出插件
}

```

启动：命令行下logstash.bat -f ../config/test1.conf

##### 	输入插件

```json
#标准输入
input{
    stdin{
       
    }
}
output {
    stdout{
        codec=>rubydebug    
    }
}

#文件输入
input {
    file {
        path => ["/var/*/*"]
        start_position => "beginning"
    }
}
output {
    stdout{
        codec=>rubydebug    
    }
}
```

##### 	过滤插件

```json
filter {
    grok {
        match => ["message", "%{HTTPDATE:timestamp}"]
    }
    date {
        match => ["timestamp", "dd/MMM/yyyy:HH:mm:ss Z"]
    }
   mutate {   #正则表达式替换匹配字段
        gsub => ["filed_name_1", "/" , "_"]
    }
    mutate {   #分隔符分割字符串为数组
        split => ["filed_name_2", "|"]
    }
     mutate {  #重命名字段
        rename => { "old_field" => "new_field" }
    }
    mutate {  #删除字段
        remove_field  =>  ["timestamp"]
    }
    geoip {  #GeoIP 地址查询归类
        source => "ip_field"
    }
}
```

##### 	输出插件

```json
#标准输出
output {
    stdout {
        codec => rubydebug
    }
}
#file
output {
    file {
        path => "/data/log/%{+yyyy-MM-dd}/%{host}_%{+HH}.log"
    }
}
#elastic
output {
    elasticsearch {
        host => ["192.168.1.1:9200","172.16.213.77:9200"]
        index => "logstash-%{+YYYY.MM.dd}"       
    }
}
```

