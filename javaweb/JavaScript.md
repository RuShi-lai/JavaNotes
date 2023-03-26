### 基础语法

#### 	基本情况

介绍

JavaScript是一门解释性的脚本语言，主要用来给HTML网页增加动态功能。通常的js是运行在浏览器环境下的，可以帮助我们去控制页面，实现丰富的功能。会有dom和bom的api去操作html文档和浏览器的一些功能。

js哪里可以执行

```html
<!--放在html标签中 -->
<body> 
//中间放页面标签  
//放在body的结束标签之前
<script type="text/javascript">
    document.write('<h1>123</h1>')
</script>
</body>

<!--引入外部的js -->
<body> 
//中间放页面标签  
//放在body的结束标签之前   
<script src="./index.js"></script>
</body>
```

#### 	数据类型

弱类型自动推断类型、null是有值但为空、undefined是只是被申明，未赋值

- 数字（number）
- 字符串 （string）
- 布尔型 （boolean）
- 空（null）
- 未定义（ undefined）

#### 	定义变量

弱类型不需要声明这个变量的类型，统一用var

```javascript
var num = 10;
var money = 1.2;
//字符串单引号和双引号都行，和java对比
var str = 'str';
var str2 = "str2";
var nul = null;
var flag = true;
//压根就没有定义叫undefined
//数组和对象
var arr = [];
var obj = {};
```

var有一些弊端，今天就说一个，如果前边定义了一个变量，后边再次定义，就会覆盖，这样会有问题														let 和 const 定义的变量不能不覆盖，不能重复定义。

#### 	数组(array)

##### 定义方式	

​	js中初始化数组不需要初始化长度

```javascript
//使用方法调用
var arr = Array();//Array 是个函数，猜一猜他的返回值就是个空数组

//使用new关键字
var arr = new Array();    //js里函数可以当类使用

//使用json数组的方式，字面量，推荐
var arr = [];
```

##### 赋值方式

```javascript
// 定义后赋值
arr[0] = 123;
arr[1] = 234;

//定义的时候赋值
var arr = [123,234];
```

#### 	方法

- js对参数要求极其灵活，不需要声明数据类型																																								
- 参数可以没有，可以有一个或多个																																																						
- 返回值要么没有，要么只有一个

```javascript
//参数是你要处理的数据，
//返回值就是处理的结果
//返回值可以给
function plus(num1,num2){
    return num1 + num2;
}
plus(23,34)
57
```

#### 	对象

##### 定义空对象

```javascript
//使用方法调用
var obj = Object();

//使用new关键字
var obj = new Object();

//使用json对象的方式，推荐
var obj = {}

//自定义对象类型
//function定义的函数，既能直接调用，也可以像类一样使用new关键字来生成。也就是函数既可以当做普通函数，也能当做构造函数。
//注意，要想给new出来的对象添加属性或方法，必须使用this关键字，不写不行。
//命名规范和java一样，首字母大写，驼峰式命名。
function User(name){
    this.name = name;
}
var user = new User('wusanshui');
console.log(user.name)
```

##### 给对象添加属性和方法

```javascript
//定义对象后
obj.name = 'zhangsan';
obj.age = 18;
onj.eat = function(){
	console.log(" I am eating! ")
}

//定义类的时候
//直接用json对象写一个对象出来
var user = {
    name: 'zhangsan',
    age: 10,
    eat: function(){
        console.log("i am eating！");
    }
}

//定义类的时候
function User(name){
    this.name = name;
    this.age = 18;
    this.eat = function(){
    	console.log("I am eating!")
    }
}
var user = new User('wusanshui');
//new 出来的对象自然而然就拥有这些属性和方法

```

##### 获取对象的属性和方法

- 使用. -> user.eat()    user.name
- 使用[] -> user\['eat']()  user['name']

#### 循环和判断

```javascript
//if语句：如果是 0 ‘’ null undefined false 都是false、{} [] 非零的数字 字符串 都是真
if(flag){
   alert(true)
}else{
   alert(false)
}

//switch语句同java

//循环数组
let cars = [ '兰博基尼','CRV','卡宴',"奔驰是傻逼",'bwm' ];
for (var i=0;i<cars.length;i++)
{ 
    console.log(cars[i]);
}

//遍历对象属性
var options = {
    name: 'zhangsan',
    age: 10
}
for(var attr in options){
    console.log(attr)
    //正确
    console.log(options[attr])
    //错误
    console.log(options.attr)
}
```



### 常见内置对象

#### 	Array对象

```javascript
//方法
concat（）　　　　 表示把几个数组合并成一个数组
join（）　　　　　 设置分隔符连接数组元素为一个字符串
pop（）　　　　　 移除数组最后一个元素
shift（）　　　　　 移除数组中第一个元素
slice（start，end） 返回数组中的一段
splice（）　　　　 可以用来删除，可以用来插入，也可以用来替换
push（）　　　　 往数组中新添加一个元素，返回最新长度
sort（）　　　　　 对数组进行排序
reverse（）　　　　反转数组的顺序
toLocaleString()　　 把数组转换为本地字符串

//属性
length　　　　　　 表示取得当前数组长度 （常用）
constructor　　　　 引用数组对象的构造函数
prototype　　　　　通过增加属性和方法扩展数组定义
```

#### 	Grobal对象

```javascript
escape（）　　　　 对字符串编码
eval（） 　　　　 把字符串解析为JavaScript代码并执行
isNaN()　　　　　　判断一个值是否是NaN
parseInt（）　　　 解析一个字符串并返回一个整数
parseFloat（）　　 解析一个字符串并返回一个浮点数
number（） 　　　 把对象的值转换为数字
string（）　　　　 把对象的值转换为字符串
```

#### 	String对象

```javascript
charAt()　　　　　 　　　　 返回指定索引的位置的字符
indexOf() 　　　 　　　　 从前向后检索字符串，看是否含有指定字符串
lastIndexOf()　　　 　　　　从后向前检索字符串，看是否含有指定字符串
concat()　　 　　　　　　　连接两个或多个字符串
match()　　 　　　　　　 使用正则表达式模式对字符串执行查找，并将包含查找结果最为结果返回
replace()　　　　　　　　　替换一个与正则表达式匹配的子串
search()　　　　　　　　　 检索字符串中与正则表达式匹配的子串。如果没有找到匹配，则返回 -1。
slice（start，end） 　　　 根据下表截取子串
substring（start，end）　 根据下表截取子串
split()　　　　　　　　　　 根据指定分隔符将字符串分割成多个子串，并返回素组
substr(start，length)　　　 根据长度截取字符串 
toUpperCase()　　　　　　 返回一个字符串，该字符串中的所有字母都被转化为大写字母。
toLowerCase()　　　　　　 返回一个字符串，该字符串中的所有字母都被转化为小写字母
```

#### 	Math对象

```javascript
ceil()　　　　向上取整。
floor()　　　 向下取整。
round()　　　四舍五入。
random()　　取随机数。
```

​	Date对象

```
getDate函数：　　　　　　返回日期的“日”部分，值为1～31。
getDay函数：　　　　　　 返回星期，值为0～6，0表示星期日。
getHours函数：　　　　　 返回日期的“小时”部分，值为0～23。
getMinutes函数：　　　　 返回日期的“分钟”部分，值为0～59。
getMonth函数：　　　　　 返回日期的“月”部分，值为0～11。
getSeconds函数：　　　　 返回日期的“秒”部分，值为0～59。
getTime函数：　　　　　　返回系统时间。
getTimezoneOffset函数：　返回此地区的时差(当地时间与GMT格林威治标准时间的地区时差)，单位为分钟。
getYear函数：　　　　　　返回日期的“年”部分。返回值以1900年为基数，如1999年为99。
parse函数：　　　　　　　返回从1970年1月1日零时整算起的毫秒数(当地时间)
setDate函数：　　　　　　设定日期的“日”部分，值为0～31。
setHours函数：　　　　　 设定日期的“小时”部分，值为0～23。
setMinutes函数：　　　　 设定日期的“分钟”部分，值为0～59。
setMonth函数：　　　　　 设定日期的“月”部分，值为0～11。其中0表示1月，...，11表示12月。
setSeconds函数：　　　　 设定日期的“秒”部分，值为0～59。
setTime函数：　　　　　　设定时间。时间数值为1970年1月1日零时整算起的毫秒数。
setYear函数：　　　　　　设定日期的“年”部分。
toGMTString函数：　　　　转换日期成为字符串，为GMT格林威治标准时间。
setLocaleString函数：　　 转换日期成为字符串，为当地时间。
UTC函数：　　　　　　　 返回从1970年1月1日零时整算起的毫秒数(GMT)。
```

### DOM编写

#### 	概述

DOM (Document Object Model) 即文档对象模型中, 每个东西都是 **节点** :

- 文档本身就是一个文档对象
- 所有 HTML 元素都是元素节点
- 所有 HTML 属性都是属性节点
- 元素内的文本是文本节点
- 注释是注释节点，就不用

```html
<div class='test1' id='a'>itnanls</div>
div整体是一个元素节点
class=‘test1’ 是一个属性节点
itnanls是个文本节点，注意中间没有东西空字符也是一个文本节点
```

