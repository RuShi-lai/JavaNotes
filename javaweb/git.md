#### Git安装

```sh
> git config --global user.name "jianbin-lai"
> git config --global user.email "1973661189@qq.com"
#检查信息是否写入成功
git config --list 
```

#### 理论基础

##### 	Git是什么

分布式管理系统

##### 	三种状态

**已提交（committed）**、**已修改（modified）** 和 **已暂存（staged）**。

- 已修改表示修改了文件，但还没保存到数据库中。
- 已暂存表示对一个已修改文件的当前版本做了标记，使之包含在下次提交的快照中。
- 已提交表示数据已经安全地保存在本地数据库中。

这会让我们的 Git 项目拥有三个阶段：工作区、暂存区以及 Git 目录。

![](https://www.ydlclass.com/doc21xnv/assets/areas-ef38e741.png)

工作目录、暂存区域以及 Git 仓库.

- 工作区是对项目的某个版本独立提取出来的内容。 这些从 Git 仓库的压缩数据库中提取出来的文件，放在磁盘上供你使用或修改。
- 暂存区是一个文件，保存了下次将要提交的文件列表信息，一般在 Git 仓库目录中。 按照 Git 的术语叫做“索引”，不过一般说法还是叫“暂存区”。
- Git 仓库目录是 Git 用来保存项目的元数据和对象数据库的地方。 这是 Git 中最重要的部分，从其它计算机克隆仓库时，复制的就是这里的数据。

基本的 Git 工作流程如下：

1. 在工作区中修改文件。
2. 将你想要下次提交的更改选择性地暂存，这样只会将更改的部分添加到暂存区。
3. 提交更新，找到暂存区的文件，将快照永久性存储到 Git 目录。

   ##### Git保证完整性

Git 中所有的数据在存储前都计算校验和，然后以校验和来引用。Git 用以计算校验和的机制叫做 SHA-1 散列（hash，哈希）。 这是一个由 40 个十六进制字符（0-9 和 a-f）组成的字符串，基于 Git 中文件的内容或目录结构计算出来。

#### 实战

##### 	初始化Git

用户信息

```sh
  > git config --global user.name "jianbin-lai"
  > git config --global user.email "1973661189@qq.com"
  #检查信息是否写入成功
  git config --list 
```

检查配置信息

```sh
>git config --list
>git config <key> #检查Git某一项配置
```

##### 	基础命令

CRLF和LF的区别

- windows下的换行符是CRLF而Unix的换行符格式是LF。git默认支持LF。
- 上面的报错的意思是会把CRLF（也就是回车换行）转换成Unix格式（LF），这些是转换文件格式的警告，不影响使用。
- 一般commit代码时git会把CRLF转LF，pull代码时LF换CRLF。
- 解决方案：git rm -r --cached           git config core.autocrlf false

```sh
#查看当前状态
>git status
#查看历史操作
>git log
>git log --pretty=oneline
>git log --graph
>git log --decorate --all --graph --oneline
```

##### 	时光回退

- 回滚快照：快照即提交的版本，每个版本我们称之为一个快照。
- HEAD 表示最新提交的快照，而 HEAD~ 表示 HEAD 的上一个快照，HEAD~~表示上上个快照，如果表示上10个快照，则可以用HEAD ~10

```sh
 git  --hard reset head~
```

![](https://www.ydlclass.com/doc21xnv/assets/image-20210316212457416-62bfdc2a.png)

参数选择

- --hard : 回退版本库，暂存区，工作区。（因此我们修改过的代码就没了，需要谨慎使用）reset 不仅移动 HEAD 的指向，将快照回滚动到暂存区域，它还将暂存区域的文件还原到工作目录。

- --mixed: 回退版本库，暂存区。(--mixed为git reset的默认参数，即当任何参数都不加的时候的参数)

- --soft: 回退版本库。

  ![](https://www.ydlclass.com/doc21xnv/assets/image-20210316214437985-2956454f.png)

  ​

回滚指定快照

```sh
git reset --hard  c7c0e3bf6d64404e3e68632c24ca13eac38b02e2
```

- 它就会将忽略移动 HEAD 的指向这一步（因为你只是回滚快照的部分内容，并不是整个快照，所以 HEAD 的指向不应该发生改变），直接将指定快照的指定文件回滚到暂存区域。

- 不仅可以往回滚，还可以往前滚！



- ```sh
  git reflog #查找快照ID
  ```

  ##### 版本对比

暂存区和工作树

```sh
$ git diff
diff --git a/b.txt b/b.txt
index 9ab39d5..4d37a8a 100644
--- a/b.txt
+++ b/b.txt
@@ -2,3 +2,4 @@
 1212
 123123123
 234234234
```

- **第一行：**diff --git a/b.txt b/b.txt 表示对比的是存放在暂存区域和工作目录的b.txt
- **第二行：**index 9ab39d5..4d37a8a 100644 表示对应文件的 ID 分别是 9ab39d5和 4d37a8a，左边暂存区域，后边当前目录。最后的 100644 是指定文件的类型和权限
- **第三行：**--- a/b.txt  --- 表示该文件是旧文件（存放在暂存区域）
- **第四行：**+++ b/b.txt +++ 表示该文件是新文件（存放在工作区域）
- **第五行：**@@ -2,3 +2,4 @@ 以 @@ 开头和结束，中间的“-”表示旧文件，“+”表示新文件，后边的数字表示“开始行号，显示行数”
- 内容：+代表新增的行 -代表少了的行

工作树和最新提交

```sh
>git  diff head
```

两个历史快照

```sh
> git diff 5da78a4 c7c0e3b
```

比较仓库和暂存区

```sh
>git diff --cached c70e3b
```

##### 	删除文件

- 工作目录删除a.txt，使用checkout将暂存区域的文件恢复到工作目录
- rm 命令删除的只是工作目录和暂存区域的文件,版本库还有需要版本库回滚
- 暂存区和工作区内容不一致 git rm -f a.txt 两个地方全部删除或者删除暂存区 git rm --cached

```sh
>git checkout -- a.txt

>git rm a.txt
>git reset --soft HEAD~

>git rm -f a.txt   #暂存区和工作区内容不一致两个地方全部删除
> git rm --cached  #删除暂存区
```

##### 	重命名文件

```sh
>git mv b.txt c.txt #git mv 旧文件名 新文件名
```

##### 	忽略文件

- Git 识别某些格式的文件，然后自主不跟踪它们：在工作目录创建一个名为 .gitignore 的文件。
- Windows 不允许你在文件管理器中创建以点（.）开头的文件。windows需要在命令行窗口创建（.）开头的文件。执行 echo *.temp > .gitignore 命令，创建一个 .gitignore 文件，并让 Git 忽略所有 .temp 后缀的文件


##### 	创建和切换分支

分支是什么

- 假设你的大项目已经上线了（有上百万人在使用），过了一段时间你突然觉得应该添加一些新的功能，但是为了保险起见，你肯定不能在当前项目上直接进行开发，这时候你就有创建分支的需要了。
- 对于其它版本控制系统而言，创建分支常常需要完全创建一个源代码目录的副本，项目越大，耗费的时间就越多；而 Git 由于每一个结点都已经是一个完整的项目，所以只需要创建多一个“指针”（像 master）指向分支开始的位置即可。


![](https://www.ydlclass.com/doc21xnv/assets/image-20210316221252036-21f2b3f7.png)

创建分支

```sh
>git branch feature1
```

 (HEAD -> master, feature01)意思是：目前有两个分支，一个是主分支（master），一个是刚才我们创建的新分支（feature），然后 HEAD 指针仍然指向默认的 master 分支。

切换分支

```sh
>git checkout feature01
```

##### 	合并分支

在主分支合并

```sh
 >git checkout master
 >git merge feature1
```

![](https://www.ydlclass.com/doc21xnv/assets/image-20210315100125472-29dec56b.png)

先创建分支feature1，再提交一次版本r1，切换分支feature并提交版本r2，切回主分支master合并会出错

```sh
<<<<<<< HEAD
123123
=======
123345
>>>>>>> feature
#以“=======”为界，上到“<<<<<<< HEAD”的内容表示当前分支，下到“>>>>>>> feature”表示待合并的 feature1 分支，之间的内容就是冲突的地方。
```

![](https://www.ydlclass.com/doc21xnv/assets/image-20210315100412732-4620ac19.png)

解决方法：手动修改

##### 	删除分支

```sh
>git branch -d 分支名
#如果试图删除未合并的分支，Git 会提示你“该分支未完全合并，如果你确定要删除，请使用 git branch -D 分支名 命令。
```

##### 	变基

当我们开发一个功能时，可能会在本地有无数次commit，而你实际上在你的master分支上只想显示每一个功能测试完成后的一次完整提交记录就好了，其他的提交记录并不想将来全部保留在你的master分支上，那么rebase将会是一个好的选择，他可以在rebase时将本地多次的commit合并成一个commit，还可以修改commit的描述等

```sh
// 合并前两次的commit
git  rebase -i head~~

// 合并此次commit在最新commit的提交
git rebaser -i hash值
```

#### Github

##### 	认识仓库	

1. pull Request 开发者在本地对源代码进行修改之后，想仓库提交请求合并的功能
2. Wiki 该功能通常用作文档手册的编写当中
3. Issues：是将一个任务或问题分配给一个issue进行跟踪和管理，可以当做bug管理系统使用，每一个功能的更正或修改都应该对应一个issue，只要看issues就能看到关于这个更改的所有信息
4. 统计就是仓库各项数据的数据统计，devOPs是持续继承、持续交付的服务，服务：其他码云提供的一些服务。
5. 管理：对仓库的一些修改删除等操作：

```sh
#列出所有的远程仓库
git remote -v

#不管是否存在对应的远程分支，将本地的所有分支都推送到远程主机，这时需要 -all 选项
 git push --all origin 
 
 #git push的时候需要本地先git pull更新到跟服务器版本一致，如果本地版本库比远程服务器上的低，那么一般会提示你git pull更新，如果一定要提交，那么可以使用这个命令。
  git push --force origin 
```

##### 	仓库

源仓库(线上版本库)

- 在项目的开始,项目的发起者构建起一个项目的最原始的仓库,称为`origin。`
- 源仓库的有两个作用：
  - 汇总参与该项目的各个开发者的代码
  - 存放趋于稳定和可发布的代码
- 源仓库应该是受保护的，开发者不应该直接对其进行开发工作。只有项目管理者能对其进行较高权限的操作。

开发者仓库(本地仓库)

- 任何开发者都不会对源仓库进行直接的操作，源仓库建立以后，每个开发者需要做的事情就是把源仓库的“复制”一份，作为自己日常开发的仓库。这个复制是gitlab上面的`fork`。
- 每个开发者所fork的仓库是完全独立的，互不干扰，甚至与源仓库都无关。每个开发者仓库相当于一个源仓库实体的影像，开发者在这个影像中进行编码，提交到自己的仓库中，这样就可以轻易地实现团队成员之间的并行开发工作。而开发工作完成以后，开发者可以向源仓库发送pull request，请求管理员把自己的代码合并到源仓库中，这样就实现了**分布式开发工作**和**集中式的管理**

  ##### 分支划分

master branch：主分支

**master**：主分支从项目一开始便存在，它用于存放经过测试，已经完全稳定代码；在项目开发以后的任何时刻当中，`master`存放的代码应该是可作为产品供用户使用的代码。所以，应该随时保持`master`仓库代码的清洁和稳定，确保入库之前是通过完全测试和代码reivew的。master分支是所有分支中最不活跃的，大概每个月或每两个月更新一次，每一次master更新的时候都应该用`git`打上`tag`，来说明产品有新版本发布。

develop branch：开发分支

**develop**：开发分支，一开始从`master`分支中分离出来，用于开发者存放基本稳定代码。每个开发者的仓库相当于源仓库的一个镜像，每个开发者自己的仓库上也有master和develop。开发者把功能做好以后，是存放到自己的develop中，当测试完以后，可以向管理者发起一个pull request，请求把自己仓库的develop分支合并到源仓库的develop中。所有开发者开发好的功能会在源仓库的develop分支中进行汇总，当develop中的代码经过不断的测试，已经逐渐趋于稳定了，接近产品目标了。这时候，就可以把`develop`分支合并到`master`分支中，发布一个新版本。

注:任何人不应该向`master`直接进行无意义的合并、提交操作。正常情况下，`master`只应该接受`develop`的合并，也就是说，`master`所有代码更新应该源于合并`develop`的代码。

feature branch：功能分支

**feature**：功能性分支，是用于开发项目的功能的分支，是开发者主要战斗阵地。开发者在本地仓库从`develop`分支分出功能分支，在该分支上进行功能的开发，开发完成以后再合并到`develop`分支上，这时候功能性分支已经完成任务，可以删除。功能性分支的命名一般为`feature-*`，|*为需要开发的功能的名称。

##### 	协议选择

git关联远程仓库可以使用https协议或者ssh协议。

HTTPS优缺点

- 优点1: 相比 SSH 协议，可以使用用户名／密码授权是一个很大的优势，这样用户就不必须在使用 Git 之前先在本地生成 SSH 密钥对再把公钥上传到服务器。 对非资深的使用者，或者系统上缺少 SSH 相关程序的使用者，HTTP 协议的可用性是主要的优势。 与 SSH 协议类似，HTTP 协议也非常快和高效
- 优点2: 企业防火墙一般会打开 80 和 443 这两个常见的http和https协议的端口，使用http和https的协议在架设了防火墙的企业里面就可以绕过安全限制正常使用git，非常方便
- 缺点: 使用http/https除了速度慢以外，还有个最大的麻烦是每次推送都必须输入口令. 但是现在操作系统或者其他git工具都提供了 `keychain` 的功能，可以把你的账户密码记录在系统里，例如OSX 的 Keychain 或者 Windows 的凭证管理器。所以也只需要输一次密码就搞定了。

SSH的优缺点

- 优点1: 架设 Git 服务器时常用 SSH 协议作为传输协议。 因为大多数环境下已经支持通过 SSH 访问 —— 即时没有也比较很容易架设。 SSH 协议也是一个验证授权的网络协议；并且，因为其普遍性，架设和使用都很容易。
- 缺点1: SSH服务端一般使用22端口，企业防火墙可能没有打开这个端口。
- 缺点2: SSH 协议的缺点在于你不能通过他实现匿名访问。 即便只要读取数据，使用者也要有通过 SSH 访问你的主机的权限，这使得 SSH 协议不利于开源的项目。 如果你只在公司网络使用，SSH 协议可能是你唯一要用到的协议。 如果你要同时提供匿名只读访问和 SSH 协议，那么你除了为自己推送架设 SSH 服务以外，还得架设一个可以让其他人访问的服务。

总结

- HTTPS利于匿名访问，适合开源项目可以方便被别人克隆和读取(但他没有push权限)；毕竟为了克隆别人一个仓库学习一下你就要生成个ssh-key折腾一番还是比较麻烦，所以github除了支持ssh协议必然提供了https协议的支持。
- 而SSH协议使用公钥认证比较适合内部项目。 当然了现在的代码管理平台例如github、gitliab，两种协议都是支持的，基本上看自己喜好和需求来选择就可以了。

克隆分支

```sh
git clone 仓库地址
```

##### 多人开发

```sh
>>> git clone 仓库地址
>>> git checkout develop
# 切换到`develop`分支
>>> git checkout -b feature-discuss
# 分出一个功能性分支
>>> touch discuss.java
# 假装discuss.java就是我们要开发的功能
>>> git add .
>>> git commit -m 'finish discuss feature'
# 提交更改,多次测试以后
>>> git checkout develop
# 回到develop分支
>>> git merge feature-discuss
# 把做好的功能合并到develop中
>>> git branch -d feature-discuss
# 删除功能性分支
>>> git push origin develop
# 把develop提交到远程仓库中
```