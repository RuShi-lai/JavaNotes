#### 缓存池

Java 基本类型的包装类的大部分都实现了缓存池技术，对应的缓冲池如下：

- Boolean 直接返回 true / false
- Byte/ Short / Integer / Long 创建了数值范围在 [-128，127] 的相应类型的缓存数据
- Character 创建了数值在 [0,127] 的缓存数据

如果超出对应范围仍然会去创建新的对象。

new Integer(123) 与 Integer.valueOf(123) 的区别在于：

- new Integer(123) 每次都会新建一个对象；
- Integer.valueOf(123) 会使用缓存池中的对象，多次调用会取得同一个对象的引用。

```java
Integer x = new Integer(123);
Integer y = new Integer(123);
System.out.println(x == y);    // false
Integer z = Integer.valueOf(123);
Integer k = Integer.valueOf(123);
System.out.println(z == k);   // true
```

valueOf() 方法的实现比较简单，就是先**判断值是否在缓存池**中，如果在的话就直接返回缓存池的内容。

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

编译器会在自动装箱过程调用 valueOf() 方法，因此多个 Integer 实例使用自动装箱来创建并且值相同，那么就会引用相同的对象。

```java
Integer m = 123;
Integer n = 123;
System.out.println(m == n); // true
```

不适用于 Integer 对象，首先 i，j 进行自动拆箱，然后数值相加，得到数值 80，进行自动装箱后，和 m 引用相同的对象。Integer 对象无法与数值直接进行比较，自动拆箱后转为数值 80，显然 80 == 80，输出 true。

```java
Integer i = 40;
Integer j = 40;
Integer m = 80;
System.out.println(m == i+j);  // true
System.out.println(80 == i+j);  // true
```

#### 基本运算

##### 	== 和 equals()

- == 判断两个对象的地址是不是相等。即判断两个对象是不是同一个对象。
  - 基本数据类型：== 比较的是值
  - 引用数据类型：== 比较的是内存地址
- equals() 判断两个对象是否相等。但它一般有两种使用情况：
  - 情况1：类没有重写 equals() 方法。等价于“==”。
  - 情况2：类重写了 equals() 方法。一般用来**比较两个对象的内容**，若它们的内容相等，则返回 true (即，认为这两个对象相等)。

注意：

- String 中的 **equals 方法是被重写过**的，因为 object 的 equals 方法是比较的对象的内存地址， 而 String 的 equals 方法比较的是对象的值。
- 当创建 String 类型的对象时，虚拟机会在**常量池**中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。 如果没有就在常量池中重新创建一个 String 对象。

```
public class EqualsDemo {
    public static void main(String[] args) {
        String a = new String("ab"); // a 为一个引用
        String b = new String("ab"); // b为另一个引用,对象的内容一样
        String aa = "ab"; // 放在常量池中
        String bb = "ab"; // 从常量池中查找
        if (aa == bb) // true
            System.out.println("aa==bb");
        if (a == b) // false，非同一对象
            System.out.println("a==b");
        if (a.equals(b)) // true
            System.out.println("aEQb");
        if (42 == 42.0) { // true
            System.out.println("true");
        }
    }
}
```

##### 	参数传递

Java 的参数是以**值传递**的形式传入方法中，而不是引用传递。

以下代码中 Dog dog 的 dog 是一个指针，存储的是对象的地址。 在将一个参数传入一个方法时，本质上是**将对象的地址以值的方式传递到形参中**。 因此在方法中使指针引用其它对象，那么这两个指针此时指向的是完全不同的对象，在一方改变其所指向对象的内容时对另一方没有影响。

```java
public class Dog {

    String name;

    Dog(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getObjectAddress() {
        return super.toString();
    }
}
```

```java
public class PassByValueExample {
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        func(dog);
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        System.out.println(dog.getName());          // A
    }

    private static void func(Dog dog) {
        System.out.println(dog.getObjectAddress()); // Dog@4554617c
        dog = new Dog("B");
        System.out.println(dog.getObjectAddress()); // Dog@74a14482
        System.out.println(dog.getName());          // B
    }
}
```

如果在方法中改变对象的字段值会改变原对象该字段值，因为改变的是同一个地址指向的内容。

```java
class PassByValueExample {
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        func(dog);
        System.out.println(dog.getName());          // B
    }

    private static void func(Dog dog) {
        dog.setName("B");
    }
}
```

##### 	float和double

Java 不能隐式执行向下转型，因为这会使得精度降低。

1.1 字面量属于 double 类型，不能直接将 1.1 直接赋值给 float 变量，因为这是向下转型。

```java
// float f = 1.1;
```

1.1f 字面量才是 float 类型。

```java
float f = 1.1f;
```

##### 	隐式类型转换

因为字面量 1 是 int 类型，它比 short 类型精度要高，因此不能隐式地将 int 类型下转型为 short 类型。

```java
short s1 = 1;
// s1 = s1 + 1;
```

但是使用 += 或者 ++ 运算符可以执行隐式类型转换。

```java
s1 += 1;
// s1++;
```

上面的语句相当于将 s1 + 1 的计算结果进行了向下转型：

```java
s1 = (short) (s1 + 1);
```

#### final&static

##### 	final关键字

**1. 数据**

声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量。

- 对于基本类型，final 使数值不变；
- 对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。

**2. 方法**

**声明方法不能被子类重写**。

private 方法隐式地被指定为 final，如果在子类中定义的方法和基类中的一个 private 方法签名相同，此时子类的方法不是重写基类方法，而是在子类中定义了一个新的方法。

**3. 类**

**声明类不允许被继承**。

##### 	static关键字

**1. 静态变量**

- 静态变量：又称为类变量，也就是说这个变量属于类的，**类所有的实例都共享静态变量**，可以直接通过类名来访问它。**静态变量在内存中只存在一份**。
- 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

**2. 静态方法**

- 静态方法在**类加载的时候就存在**了，它**不依赖于任何实例**。所以静态方法必须有实现，也就是说它不能是抽象方法。
- 只能访问所属类的静态字段和静态方法，方法中不能有 this 和 super 关键字。


**3. 静态语句块**

静态语句块在类初始化时运行一次。

**4. 静态内部类**

- 非静态内部类依赖于外部类的实例，而静态内部类不需要。
- 静态内部类不能访问外部类的非静态的变量和方法。


```
public class OuterClass {

    class InnerClass { // 非静态内部类
    }

    static class StaticInnerClass { // 静态内部类
    }

    public static void main(String[] args) {
        // InnerClass innerClass = new InnerClass(); 
        // 'OuterClass.this' cannot be referenced from a static context
        OuterClass outerClass = new OuterClass();
        InnerClass innerClass = outerClass.new InnerClass();
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }
}
```

**5. 静态导包**

在使用静态变量和方法时不用再指明 ClassName，从而简化代码，但可读性大大降低。

```
import static com.xxx.ClassName.*
```

**6. 初始化顺序**

静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序

存在继承的情况下，初始化顺序为：

- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）

### Java常见类

#### 	Object

Object类是类层次结构的根类。每个类都使用 Object 作为超类。每个类都直接或者间接的继承自Object类。

Object 中常用方法有：

```java
public int hashCode() //返回该对象的哈希码值。
// 注意：哈希值是根据哈希算法计算出来的一个值，这个值和地址值有关，但是不是实际地址值。
public final Class getClass() //返回此 Object 的运行时类
public String toString() //返回该对象的字符串表示。
protected Object clone() //创建并返回此对象的一个副本。可重写该方法
protected void finalize() 
// 当垃圾回收器确定不存在对该对象的更多引用时，由对象的垃圾回收器调用此方法。用于垃圾回收，但是什么时候回收不确定。
```

##### equals()方法

```java
//Ⅰ 自反性
x.equals(x); // true
//Ⅱ 对称性
x.equals(y) == y.equals(x); // true
//传递性
if (x.equals(y) && y.equals(z))
    x.equals(z); // true;
//Ⅳ 一致性          多次调用 equals() 方法结果不变
x.equals(y) == x.equals(y); // true
//Ⅴ 与 null 的比较  对任何不是 null 的对象 x 调用 x.equals(null) 结果都为 false
x.equals(null); // false;
```

**2. 等价与相等**

- 对于基本类型，== 判断两个值是否相等，基本类型没有 equals() 方法。
- 对于引用类型，== 判断两个变量是否引用同一个对象，而 equals() 判断引用的对象是否等价。

```java
Integer x = new Integer(1);
Integer y = new Integer(1);
System.out.println(x.equals(y)); // true
System.out.println(x == y);      // false
```

**3. 实现**

- 检查是否为同一个对象的引用，如果是直接返回 true；
- 检查是否是同一个类型，如果不是，直接返回 false；
- 将 Object 对象进行转型；
- 判断每个关键域是否相等。

```java
 @Override
    public boolean equals(Object o) {
        if (this == o) return true;  //检查是否为同一个对象的引用，如果是直接返回 true；
        if (o == null || getClass() != o.getClass()){
            //检查是否是同一个类型，如果不是，直接返回 false
            return false;
        }

        // 将 Object 对象进行转型
        EqualExample that = (EqualExample) o;

        // 判断每个关键域是否相等。
        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }
```

##### hashCode()方法

hashCode() 返回散列值，而 equals() 是用来判断两个对象是否等价。 **等价的两个对象散列值一定相同，但是散列值相同的两个对象不一定等价**。

**在覆盖 equals() 方法时应当总是覆盖 hashCode() 方法，保证等价的两个对象散列值也相等**。

下面的代码中，新建了两个等价的对象，并将它们添加到 HashSet 中。 我们希望将这两个对象当成一样的，只在集合中添加一个对象，但是因为 EqualExample 没有实现 hasCode() 方法， 因此这两个对象的散列值是不同的，最终导致集合添加了两个等价的对象。

```java
EqualExample e1 = new EqualExample(1, 1, 1);
EqualExample e2 = new EqualExample(1, 1, 1);
System.out.println(e1.equals(e2)); // true
HashSet<EqualExample> set = new HashSet<>();
set.add(e1);
set.add(e2);
System.out.println(set.size());   // 2
```

理想的散列函数应当具有均匀性，即不相等的对象应当均匀分布到所有可能的散列值上。 这就要求了散列函数要把**所有域的值都考虑进来**。 可以将每个域都当成 R 进制的某一位，然后组成一个 R 进制的整数。 R 一般取 31，因为它是一个奇素数，如果是偶数的话，当出现乘法溢出，信息就会丢失，因为与 2 相乘相当于向左移一位。

一个数与 31 相乘可以转换成移位和减法：`31*x == (x<<5)-x`，编译器会自动进行这个优化。

```
@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + z;
    return result;
}
```

##### toString()方法

默认返回类名@4554617c 这种形式，其中 @ 后面的数值为散列码的无符号十六进制表示。

##### clone()方法

