#### juc的概述

juc：java.util.current工具包的简称，一个处理线程的工具包，jdk1.5出现

进程：程序的一次执行过程，资源分配的最小单位

线程：系统分配处理器时间资源的基本单元，程序执行的最小单元

##### 线程基本状态

NEW---RUNNABLE---BLOCKED----WARITING-----TIME_WAITING-------TERMINAL

##### wait/sleep的区别

- sleep是Thread的静态方法，wait是Object的方法，任何实例都可以调用
- sleep不会释放和占用锁，wait会释放锁（调用前提当前线程占用锁）
- 都可以被interrup中断

##### 管程(monitor)

java中叫锁，os中叫监视器，一种同步操作，保证同一个时间，只有一个线程访问被保护数据或代码

jvm同步基于进入和推出，使用管程对象

##### 用户线程、守护线程

- 用户线程：用户自定义线程    ->   主线程结束了，用户线程还在运行，jvm存活
- 守护线程：比如垃圾回收机制   -> 没有用户线程，都是守护线程，jvm结束

#### Lock接口

##### 多线程编程步骤

- 创建资源类，在资源类中创建属性和操作方法
- 在资源类操作方法
  - 判断
  - 干活
  - 通知
- 创建多个线程，调用资源类的操作方法
- 放置虚假唤醒的问题（把条件判断放入while语句中）

##### 什么是Lock接口

可重入锁：线程可以重复获得	同一把锁

##### Lock和synchronized区别

- Lock是一个接口，syschronized是关键字
- sychronized发送异常，会自动释放锁，不会死锁，Lock没用unclock会造成死锁，建议放finally代码块中
- Lock可以让等待锁的线程响应中断，而synchronized不行
- 通过Lock可以知道有没有成果获取锁，synchronized不行
- 竞争资源非常激烈时，Lock读操作远优于synchronized

##### 虚假唤醒

- if语句中使用wait，在哪里睡就在哪里醒。
- 解决方法：把条件判断放入while语句中。

#### 线程间定制化通信

启动三个线程，按照如下要求

- AA打印5次、BB打印10次、CC打印15次
- 进行10轮

给每个线程定义一个标志位

![](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\T1L8`NJVB@}{ZJQ7MZ6T16I.png)

Condition 接口描述了可能会与锁有关联的条件变量在用法上与使用 Object.wait 访问的隐式监视器类似，但提供了更强大的功能。需要特别指出的是，单个 Lock 可能与多个 Condition 对象关联。

#### 集合线程不安全

ArrayList中add没有添加synchronized关键字，线程不安全

##### 解决方案

- 用Vector实现，不常用
- 用Collections.synchronizedList(new ArrayList<>())，不常用
- 用CopyOnWriterArrayList    ----读的时候支持并发读，写时（复制一个和之前集合大小相同的内容，然后写入内容，写完将新的内容和之前合并）

HashSet中的add没用synchronized关键字，线程不安全

解决方案：CopyOnWriterArraySet

HashMap线程不安全

解决方案：ConCurrentHashMap

#### 多线程锁

##### 锁的对象

- synchronized放在普通同步方法上，锁的当前实例对象
- synchronized放在静态同步方法，锁的是Class对象
- Class锁和对象锁不是同一把锁

##### 公平锁和非公平锁

- 非公平锁：造成线程饿死，但执行效率高
- 公平锁：阳光普照，效率相对低

##### 可重入锁

- 又叫做递归锁，加锁后可以再加锁（同一个钥匙）
- synchronized(隐式)和Lock(显示)都是可重入锁
- 自己递归锁有里面有一个没释放自己没有关系仍然可用，然而别人拿不到。

##### 死锁

两个或者两个以上的进程在执行过程中，因为争夺资源而造成一种互相等待的现象

产生死锁的原因

- 进程互斥
- 请求保持
- 不可抢夺
- 循环等待

验证是否死锁：jps ---类型 ps -ef              jstack：jvm自带栈跟踪工具

#### Callable接口

call()方法，可用使线程返回结果

##### callable和runnable接口

- call()方法可引发异常，run()方法不能引发异常
- Runnable接口有一个实现类 FutureTask构造可以传递Callable

##### FutureTask(未来任务)

- 在主线程不受影响的前提下，开启一个线程做别的事情
- 最终再汇总，汇总只需要一次

#### juc辅助类

##### countDownLatch(减少计数)

countDownLatch可用设置一个计数器，通过countDown方法进行减一，使用await方法等待计数器等于0，然后继续执行await方法之后的语句。

##### CyclicBarrier(循环栅栏)

允许一组线程互相等待，知道到达某个公共屏障点

循环阻塞，构造方法第一个参数为目标障碍数，每执行CyclicBarrier一次障碍数加1，如果达到目标障碍数，才会执行CyclicBarrier.await()之后语句，理解为CyclicBarrier障碍数加一，第二个参数为实现runnable类

##### semaphore(信号灯)

计数信号量，在许可前会阻塞每一个acquire()，然后再获取改许可，每个release()添加一个许可，从而可能释放一个正在阻塞的获得者

#### 读写锁

##### 乐观锁和悲观锁

- 乐观锁：支持并发操作，通过version控制
- 悲观锁：不支持并发操作

##### 表锁和行锁

表锁不会造成死锁，而行锁会。

##### 读锁和写锁

- 读锁：共享锁（针对同一份数据，多个读操作可以同时进行而不会互相影响），发生死锁
- 写锁：独占锁（当操作没有完成之前，它会阻断其它写操作或读操作），发生死锁

##### 独写锁 

一个资源可用被多个读线程访问，或者可用被一个写线程访问，但是不能同时存在独写线程，独写互斥，读读共享

```java
private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
readWriteLock.writeLock().lock();
readWriteLock.writeLock().unlock();
readWriteLock.readLock().lock();
readWriteLock.readLock().unlock();
```

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\Y4[X]L1%LOH9WK35LZ`7J0B.png)

锁降级：将写入锁降级为读锁（读锁不能升级为写锁）

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\3G_SDM~AA~`WN8}X0G5JKS1.png)

#### 阻塞队列

一个共享队列，从队列一端输入，一端输出

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\}%SG}SDHVGTWK7OO9@X4WQC.png)

队空获取元素被阻塞，队满放元素被阻塞。

##### 阻塞队列的分类

- ArrayBlockingQueue：数组结构组成有界阻塞队列
- LinkedBlockingQueue：链表结构组成有界阻塞队列
- DelayQueue：优先队列实现的延迟无界阻塞队列
- SynchronousQueue：单个元素队列
- LinkedTransferQueue：由链表组成的无界阻塞队列
- LinkedBlockingDeque：由链表组成的双向阻塞队列

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\UF1DN$$Y9XTPC3FZ7F@T]7T.png)

#### 线程池

线程的一种使用模式，线程过多会带来调度开销，进而影响缓存局部性和整体性能。而线程池维护多个线程，等待着监督管理着分配可并发执行的任务，避免了在处理短时间任务时创建与销毁线程的代价，不仅可用保证内核充分利用，还能防止过分调度。

##### 特点

- 降低资源消耗
- 提高响应速度
- 提高线程可管理型

##### 使用方式

- Executors.newFixedThreadPool(int i)：一池N线程
- Executors.newSigleThreadExecutor()：一池一线程，一个任务一个任务执行
- Executors.newCachedThreadPool：线程池根据需求创建线程，可扩容

##### 线程池七个参数

new ThreadPoolExecutor(int corePoolSize,int  maximumPoolSize, long keepAliveTime TimeUnit unit, BlockingQueue<Runnable> workQueuqe, ThreadFactory threadFactory, RejectExecutionHandle handle)

常驻线程数量、最大线程数、线程存活时间、时间单位、阻塞队列、线程工厂、拒绝策略

##### 线程池工作流程

当执行excute()才创建线程

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\`AYZI}O%@Q~T9KC{605T7I1.png)

##### 拒绝策略

- AbortPolocy(默认)：直接抛出RejectExecutionException阻止系统正常允许
- CallerRunsPolicy：不会抛弃任务，也不会抛出异常，而是将某些任务回退到调用者
- DiscardOldestPolicy：抛弃队列中等待最久线程，然后把当前任务加入队列
- DiscardPolicy：默默丢弃无法处理任务，不予处理也不抛出异常

![img](file:///C:\Users\如是\AppData\Roaming\Tencent\Users\1973661189\QQ\WinTemp\RichOle\F13}U86{PCO3@8MN[ZP9P1K.png)

#### 分支合并框架

Fork：把一个复杂任务进行拆分

Join：把分拆任务进行合并

异步回调：CompletableFuture<参数> 参数为Void 没有返回值，get执行

```
       CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName() + " completableFuture1");
        });
        completableFuture.get()

        CompletableFuture<Integer> completableFuture1 =CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName() + " completableFuture2");
            return 1024;
        });
        completableFuture1.whenComplete((t,u)->{
            System.out.println(t); //返回值
            System.out.println(u); //异常
         }).get();
```

