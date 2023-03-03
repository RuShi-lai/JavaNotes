#### API概念

 API（Application Programming Interface）应用程序接口，是一些预先定义的接口。

#### 时间API

##### 	一些概念

```java
Mon Aug 09 15:46:13 GMT+08:00 2021
```

1. GMT：格林尼治标准时间 ，格林尼治标准时间的正午是指当太阳横穿格林尼治子午线时（也就是在格林尼治时）的时间。
2. +08:00 就是北京时间，这是时间区域的标示，用以区别时间，以英国格林威治标准时间为基准，台湾,香港,中国为往东8个时区
3. 时间戳：时间戳是指格林威治时间1970年01月01日00时00分00秒(北京时间1970年01月01日08时00分00秒)起至现在的总毫秒数。时间戳在全世界都是固定的。

##### Date	

```java
Date date= new Date(); //构造方法   Date(long date) 分配一个时间（ms）返回时间戳
boolean after(Date date)   boolean before(Date date)    
int getTime()  //返回自1970年1月1日以来，由此 Date对象表示的00:00:00 GMT的毫秒数
```

Calendar

```
add(int field, int amount)根据日历的规则，将指定的时间量添加或减去给定的日历字段。
after(Object when)返回 Calendar是否 Calendar指定时间之后的时间 Object 。
before(Object when)返回此 Calendar是否 Calendar指定的时间之前指定的时间 Object 。
Date	getTime()返回一个 Date表示此物体 Calendar的时间值（毫秒从偏移 Epoch “）。
long	getTimeInMillis()以毫秒为单位返回此日历的时间值。
TimeZone	getTimeZone()获取时区。
int	getWeeksInWeekYear()返回由这个 Calendar表示的星期内的星期 Calendar 。
int	getWeekYear()返回这个 Calendar 。   
void	setTime(Date date)使用给定的 Date设置此日历的时间。    
void	setTimeInMillis(long millis)从给定的长值设置此日历的当前时间。   
void	setTimeZone(TimeZone value)以给定的时区值设置时区。  
Instant	toInstant()将此对象转换为Instant 。
```

##### 时区TimeZone

静态方法获取：TimeZone.getDefault()

##### ZoneId

ZoneId是指区域ID，可以这样表示 Europe/London Asia/Shanghai America/New_York Japan也可以这样 GMT+8:00 也行

##### SimpleDateFormat

```java
public static void main(String[] args) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH小时mm分ss秒");
    String format = simpleDateFormat.format(new Date());
  //Date parse(String source) 从给定字符串的开始解析文本以生成日期
    Date parse = simpleDateFormat.parse(format); 
}
```

#### JDK8时间类

##### 	Instant（替代Date)

```java
Instant instant = Instant.now() ;//比北京时间少8小时需要加上默认时区
instant.atZone(ZoneId.SystemDefault())
long epochMilli = instant.toEpochMilli(); //时间戳转毫秒
long epochSecond = instant.getEpochSecond(); //时间戳转秒
String text = "2020-06-10T08:46:55.967Z";
Instant parseInstant = Instant.parse(text);//表达式转instant
```

##### 	Duration(持续时间用于Instant计算)

```java
   Instant instant = Instant.now();// 当前的时间加五天
        Instant plus = instant.plus(Duration.ofDays(5));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(plus,ZoneId.systemDefault());
        Duration.ofDays(3);   Duration.ofHours(3); Duration.ofMinutes(3);
        Duration.ofSeconds(3);  Duration.ofMillis(3);
// 次方法时通用的，第一个参数是数字，第二个是单位，ChronoUnit是一个枚举类，就是枚举了一堆的时间单位：年月日时分秒。
        Duration.of(3, ChronoUnit.YEARS);
```

##### LocalDate

```java
        LocalDate today = LocalDate.now();
        int year = today.getYear(); //获取年
        int monthValue = today.getMonthValue(); //获取月份
        int dayOfMonth = today.getDayOfMonth(); //当月第几号
        int dayOfYear = today.getDayOfYear();  //这一年的第几天
        DayOfWeek dayOfWeek = today.getDayOfWeek();  //周几 枚举类
        int value = today.getDayOfWeek().getValue();// 用1-7表示周几
        LocalDate birthday = LocalDate.of(1991,7, 16); //指定日期
	    Boolean  flag =today.isLeapYear()   //是否闰年
```

##### DateTimeFormatter

```java
LocalDate today = LocalDate.now();
DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
String format = today.format(dateTimeFormatter);
```

##### 类型相互转换

```
    //instant和Date
        Instant now = Instant.now();
        Date date = Date.from(now);
        Instant instant = date.toInstant();

        //instant和LocalDateTime
      LocalDateTime localDateTime =LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
        Instant instant1 = LocalDateTime.now().toInstant(ZoneOffset.UTC);

        //Date和LocalDateTime
        Date date1 = new Date();
        Instant instant2 = date.toInstant();
        LocalDateTime localDateTimeOfInstant = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime localDateTime1 = LocalDateTime.now();
        Instant toInstant = localDateTime1.atZone(ZoneId.systemDefault()).toInstant();
        Date dateFromInstant = Date.from(toInstant);
```

#### 数学类

##### 	Math

Math.random()返回0-99随机数

```java
Math.random()//返回0-99随机数
Math.floor(x)//返回小于x的第一个整数所对应的浮点数(值是整的，类型是浮点型)
Math.ceil(x)//返回大于x的第一个整数所对应的浮点数(值是整的，类型是浮点型)
```

##### 	Random

```java
       Random random = new Random(5); //随机数生成器
    /*  1.protected int next(int bits)：生成下一个伪随机数。
        2.boolean nextBoolean()：返回下一个伪随机数，它是取自此随机数生成器序列的均匀分布的boolean值。
        3.void nextBytes(byte[] bytes)：生成随机字节并将其置于用户提供的 byte 数组中。
        4.double nextDouble()：返回下一个伪随机数，它是取自此随机数生成器序列的、在0.0和1.0之间均匀分布的 double值。
        5.float nextFloat()：返回下一个伪随机数，它是取自此随机数生成器序列的、在0.0和1.0之间均匀分布float值。
        6.double nextGaussian()：返回下一个伪随机数，它是取自此随机数生成器序列的、呈高斯（“正态”）分布的double值，其平均值是0.0 标准差是1.0。
        7.int nextInt()：返回下一个伪随机数，它是此随机数生成器的序列中均匀分布的 int 值。
        8.int nextInt(int n)：返回一个伪随机数，它是取自此随机数生成器序列的、在（包括和指定值（不包括）之间均匀分布的int值。
        9.long nextLong()：返回下一个伪随机数，它是取自此随机数生成器序列的均匀分布的 long 值*/
```

#### 工具类

##### 	Arrays

```java
int[] nums = {1,3,9,2,5,6};
Arrays.toString(nums)
Arrays.sort(nums);
int i = Arrays.binarySearch(nums, 5); // 二分查找 i是下标
copyOf(int[] original, int newLength)
copyOfRange(int[] original, int from, int to)
  Arrays.equals(nums,nums2)
```

##### 	System类

```java
  public static void gc()  //用于垃圾回收
  //终止正在运行的java虚拟机。参数用作状态码，根据惯例，非0表示异常终止
  public static void exit(int status)
  //System.out.println(System.currentTimeMillis());
  //返回从1970年1月1日到现在时间的毫秒数（协调时间）
  public static native long currentTimeMillis();
  public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
  //src - 源数组。 
  //srcPos - 源数组中的起始位置。 
  //dest - 目标数组。 
  //destPos - 目的地数据中的起始位置。 
  //length - 要复制的数组元素的数量。
```