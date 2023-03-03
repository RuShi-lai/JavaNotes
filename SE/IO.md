#### 文件的操作

​	文件路径

1. 在Unix/Linux中，路径的分隔采用正斜杠"/"，比如"/home/hutaow"；而在Windows中，路径分隔采用反斜杠""，比如"C:\Windows\System"
2. 在Java当中反斜杠代表的是转义


##### 	File类

在 Java 中，File 类是 java.io 包中唯一代表磁盘文件本身的对象。File 类定义了一些与平台无关的方法来操作文件，File类主要用来获取或处理与磁盘文件相关的信息，像文件名、 文件路径、访问权限和修改日期等，还可以浏览子目录层次结构。File 类表示处理文件和文件系统的相关信息。也就是说，File 类不具有从文件读取信息和向文件写入信息的功能，它仅描述文件本身的属性。

```java
//构造器
File(String pathname)
File(String parent,String child)
File(File parent,String child)   //   用\\来分割
  
//创建和删除
 boolean createNewFile()
 boolean mkdir()
 boolean mkdirs()  //多级目录
 boolean delete()
  
  //判断
 boolean	exists()	判断指定路径的文件或文件夹是否为空
 boolean	isAbsolute()	判断当前路径是否是绝对路径
 boolean	isDirectory()	判断当前的目录是否存在
 boolean	isFile()	判断当前的目录是否是一个文件
 boolean	isHidden()	判断当前路径是否是一隐藏文件
  
  //获取功能修改名字
  File	getAbsoluteFile()	获取文件的绝对路径，返回File对象
  String	getAbsolutePath()	获取文件的绝对路径，返回路径的字符串
  String	getParent()	获取当前路径的父级路径，以字符串形式返回该父级路径
  String	getName()	获取文件或文件夹的名称
  String	getPath()	获取File对象中封装的路径
  long	lastModified()	以毫秒值返回最后修改时间 
  long	length()	返回文件的字节数
  boolean	renameTo(File dest)	将当前File对象所指向的路径修改为指定File所指向的路径
  
  //文件夹列表
  String	list()	得到这个文件夹下的所有文件，返回路径数组
  String[]	list(FilenameFilter filter)	通过过滤器过滤文件，过滤通过文件名过滤，返回路径数组
  File[]	listFiles()	得到这个文件夹下的所有文件，返回文件数组
  File[]	listFiles(FileFilter filter)	通过过滤器过滤文件，过滤通过文件过滤，返回文件数组
  File[]	listFiles(FilenameFilter filter)	通过过滤器过滤文件，过滤通过文件名过滤，返回文件
```

#### IO流的分类

##### 流向分

1. 输入流： 只能从中读取数据，而不能向其写入数据。
2. 输出流：只能向其写入数据，而不能向其读取数据。

##### 操作单元分

1. 字节流：是一个字节一个字节的读取或写入
2. 字符流：是一个字符一个字符的读取或写入，一个字符就是两个字节，主要用来处理字符

##### 角色划分

1. 节点流：直接从/向一个特定的IO设备（如磁盘，网络）读/写数据的流，称为节点流。
2. 处理流：“连接”在已存在的流（节点流或处理流）之上通过对数据的处理为程序提供更为强大的读写功能的流。

##### 分类表

|         |                      |                       |                 |                 |
| ------- | -------------------- | --------------------- | --------------- | --------------- |
| 分类      | 字节输入流                | 字节输出流                 | 字符输入流           | 字符输出流           |
| 抽象基类    | InputStream          | OutputStream          | Reader          | Writer          |
| 访问文件    | FileInputStream      | FileOutputStream      | FileReader      | FileWriter      |
| 访问数组    | ByteArrayInputStream | ByteArrayOutputStream | CharArrayReader | CharArrayWriter |
| 访问字符串   |                      |                       | StringReader    | StringWriter    |
| 缓冲流（处理） | BufferedInputStream  | BufferedOutputStream  | BufferedReader  | BufferedWriter  |
| 操作对象    | ObjectInputStream    | ObjectOutputStream    |                 |                 |

#### 流的案例

##### 	流的使用

1. 将一个流对象插入在一个节点上

2. 用int read()方法读取一个字节

3. 用int read(byte[] buf)读取一个字节数组

4.  定义文件输出流时，有两个参数，第二个如果是true代表追加文件，如果false代表覆盖文件，意思就是如果人家这个文件原来有内容

5. #### AutoCloseable接口，实现了这个接口的类可以在try中定义资源，并会主动释放资源：

   ```java
   public static void main(String[] args) {
       try(InputStream inputStream = new FileInputStream("D:/code/a.txt");
           OutputStream outputStream= new FileOutputStream("D:/code/b.txt",true)) {
           byte[] buf = new byte[3];
           int len;
           while ((len =inputStream.read(buf))  != -1){
               outputStream.write(buf,0,len);
           }
       }  catch (IOException e) {
           e.printStackTrace();
       }
   }
   ```

#### 序列化和反序列化

##### 	对象序列化

- 序列化：将对象写入到IO流中，说的简单一点就是将内存模型的对象变成字节数字，可以进行存储和传输。
- 反序列化：从IO流中恢复对象，将存储在磁盘或者从网络接收的数据恢复成对象模型。
- 使用场景：所有可在网络上传输的对象都必须是可序列化的，否则会出错；所有需要保存到磁盘的Java对象都必须是可序列化的。
- 该对象必须实现Serializable接口，才能被序列化。


#####   	反序列化版本号

1. 反序列化必须拥有class文件，但随着项目的升级，class文件也会升级，序列化保证升级前后的兼容性提供了一个``private static final long serialVersionUID` 的序列化版本号，只要版本号相同，即使更改了序列化属性，对象也可以正确被反序列化回来。
2. 序列化版本号可自由指定，如果不指定，JVM会根据类信息自己计算一个版本号，这样随着class的升级、代码的修改等因素无法正确反序列化；

3. 不指定版本号另一个明显隐患是，不利于jvm间的移植，可能class文件没有更改，但不同jvm可能计算的规则不一样，这样也会导致无法反序列化。
4. 总结
   1. 所有需要网络传输的对象都需要实现序列化接口。
   2. 对象的类名、实例变量（包括基本类型，数组，对其他对象的引用）都会被序列化；方法、类变量、transient实例变量都不会被序列化。
   3. 如果想让某个变量不被序列化，使用transient修饰。
   4. 序列化对象的引用类型成员变量，也必须是可序列化的，否则，会报错。
   5. 反序列化时必须有序列化对象的class文件。
   6. 同一对象序列化多次，只有第一次序列化为二进制流，以后都只是保存序列化编号，不会重复序列化。
   7. 建议所有可序列化的类加上serialVersionUID 版本号，方便项目升级。

深拷贝

1. 对象引用的改变
2. 浅拷贝：实现clonable接口，重写clone方法。

3. 深拷贝：使用对象流先写入byte数组，再读出来。


```java
public void deepCopyTest() throws  CloneNotSupportedException {
    User user = new User(12, "zhagnsna");
    user.setDog(new Dog(2));
    User user1 = user;

    user.getDog().setAge(23);
    System.out.println(user1);
}

public class User implements Serializable,Cloneable {


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
@Test
public void deepCopyTest() throws  CloneNotSupportedException {
    User user = new User(12, "zhagnsna");
    user.setDog(new Dog(2));
    User user1 = (User)user.clone();

    user.getDog().setAge(23);
    System.out.println(user1);
}

@Test
public void deepCopyTest2() throws CloneNotSupportedException, IOException, ClassNotFoundException {
    User user = new User(12, "zhangsan");
    user.setDog(new Dog(2));

    // 将对象写到字节数组当中
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
    objectOutputStream.writeObject(user);
    // 获取字节数组
    byte[] bytes = outputStream.toByteArray();
    // 用输入流读出来
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    Object object = objectInputStream.readObject();
    User user1 = (User) object;

    user.setAge(44);
    user.getDog().setAge(11);
    System.out.println(user);
    System.out.println(user1);

}

```