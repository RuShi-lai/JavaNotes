##### 什么是HTML

HTML 是用来描述网页的一种语言。

- HTML 指的是超文本标记语言 (**H**yper **T**ext **M**arkup **L**anguage)
- HTML 不是一种编程语言，而是一种*标记语言* (markup language)
- 标记语言是一套*标记标签* (markup tag)
- HTML 使用*标记标签*来描述网页

##### HTML标签

HTML 文档 = 网页

- HTML 文档*描述网页*
- HTML 文档*包含 HTML 标签*和纯文本
- HTML 文档也被称为*网页*

Web 浏览器的作用是读取 HTML 文档，并以网页的形式显示出它们。浏览器不会显示 HTML 标签，而是使用标签来解释页面的内容

###### HTML头部

<head> 元素包含了所有的头部标签元素。在 <head>元素中你可以插入脚本（scripts）, 样式文件（CSS），及各种meta信息。

###### HTML  >title> 元素

<title> 元素:

- 定义了浏览器工具栏的标题
- 当网页添加到收藏夹时，显示在收藏夹中的标题
- 显示在搜索引擎结果页面的标题

###### HTML >base>元素

<base> 标签描述了基本的链接地址/链接目标，该标签作为HTML文档中所有的链接标签的默认链接，可以不加:

```html
<head>
<base href="http://www.runoob.com/images/" target="_blank">
</head>
```

###### HTML >link>元素

link> 标签定义了文档与外部资源之间的关系。通常用于链接到样式表:

```html
<head>
<link rel="stylesheet" type="text/css" href="mystyle.css">
</head>
```

###### HTML >style>元素

<style> 标签定义了HTML文档的样式文件引用地址.

在<style> 元素中你也可以直接添加样式来渲染 HTML 文档:

```html
<head>
<style type="text/css">
    body {background-color:yellow}
    p {color:blue}
</style>
</head>
```

###### HTML >meta>元素

<meta> 标签提供了元数据.元数据也不显示在页面上，但会被浏览器解析。

META 元素通常用于指定网页的描述，关键词，文件的最后修改时间，作者，和其他元数据。

```html
<meta name="keywords" content="HTML, CSS, XML, XHTML, JavaScript">
```

###### 基本标签

```html
#标题  <h1></h1>  ~ <h6></h6>
#段落 <p></p>
#换行 <br>
#水平线 <hr>
```

###### 链接

```html
普通的链接：<a href="http://www.example.com/">链接文本</a>
图像链接： <a href="http://www.example.com/"><img src="URL" alt="替换文本"></a>
邮件链接： <a href="mailto:webmaster@example.com">发送e-mail</a>
书签：
<a id="tips">提示部分</a>
<a href="#tips">跳到提示部分</a>
```

###### 图片

```html
<img loading="lazy" src="URL" alt="替换文本" height="42" width="42">
```

###### 样式/区块

```html
<style type="text/css">
h1 {color:red;}
p {color:blue;}
</style>
<div>文档中的块级元素</div>
<span>文档中的内联元素</span>
```

###### 列表

```html
#无序列表
<ul>    
    <li>项目</li>    
    <li>项目</li> 
</ul>

#有序列表
<ol>    
    <li>第一项</li>    
    <li>第二项</li> 
</ol>

#自定义列表
<dl>  
    <dt>项目 1</dt>    
    <dd>描述项目 1</dd>  
    <dt>项目 2</dt>    
    <dd>描述项目 2</dd> 
</dl>
```

###### 表格

```html
<table border="1">  
    <thead>
        <tr>     
            <th>表格标题</th>     
            <th>表格标题</th>   
        </tr>
    </thead>
    <tbody>
       <tr>     
         <td>表格数据</td>     
         <td>表格数据</td>   
       </tr> 
    </tbody>
</table>
```

###### 表单

```html
<form> 
    <input type="text" name="email" size="40" maxlength="50"> 
    <input type="password"> 
    <input type="checkbox" checked="checked"> 
    <input type="radio" checked="checked"> 
    <input type="submit" value="Send"> 
    <input type="reset"> <input type="hidden"> 
    
    <select> 
        <option>苹果</option> 
        <option selected="selected">香蕉</option> 
        <option>樱桃</option> 
    </select> 
    
    <textarea name="comment" rows="60" cols="20"></textarea>  
</form>
```

###### 实体

```html
&lt; 等同于 < 
&gt; 等同于 > 
&nbsp; 等同于空格
```

