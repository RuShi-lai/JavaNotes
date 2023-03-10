#### CSS简介

- CSS 指层叠样式表 (Cascading Style Sheets)
- 样式定义如何显示HTML 元素
- 样式通常存储在*样式表*中
- 把样式添加到 HTML 中，是为了*解决内容与表现分离的问题*
- 外部样式表可以极大提高工作效率
- 外部样式表通常存储在 *CSS 文件*中
- 多个样式定义可*层叠*为一

#### 定义CSS样式的方法

##### 	行内式（内联样式）

```html
<p style="background: red"> I  love  java!</p>
```

##### 	内页式（嵌入样式）

```html
<!DOCTYPE html>
<html lang="en">
  <head>
     <style type="text/css">
          p{ 
              background: green;
          }
     </style>
 </head>
 <body>
     <p> I  love  java!</p>
</body>
</html>
```

##### 	外部样式

```html
<head>
      <meta charset="UTF-8">
      <title>外部样式</title>
      <link rel="stylesheet" href="style.css">
</head>
```

#### CSS选择器

| 选择器                        | 示例              | 示例说明                          | CSS  |
| -------------------------- | --------------- | ----------------------------- | ---- |
| .class （记住）                | .intro          | 选择所有class="intro"的元素，class选择器 | 1    |
| #id （ 记住）                  | #firstname      | 选择所有id="firstname"的元素，id选择器   | 1    |
| * （记住）                     | *               | 选择所有元素                        | 2    |
| element（ 记住）               | p               | 选择所有p元素，元素选择器                 | 1    |
| [element,element]（ 记住）     | div,p           | 选择所有div元素和p元素                 | 1    |
| element element（ 记住）       | div p           | 选择div元素内的所有p元素                | 1    |
| element>element（ 记住）       | div>p           | 选择所有父级是 div元素的 p元素            | 2    |
| element+element（ 记住）       | div+p           | 选择所有紧接着div元素之后的p元素            | 2    |
| [*attribute*]（ 记住）         | [target]        | 选择所有带有target属性元素              | 2    |
| [*attribute*=*value*]（ 记住） | [target=-blank] | 选择所有使用target="-blank"的元素      | 2    |

#### CSS三大特性

##### 	CSS层叠性

所谓层叠性是指多种CSS样式的叠加。

是浏览器处理冲突的一个能力,如果一个属性通过两个相同选择器设置到同一个元素上，那么这个时候一个属性就会将另一个属性层叠掉

比如先给某个标签指定了内部文字颜色为红色，接着又指定了颜色为蓝色，此时出现一个标签指定了相同样式不同值的情况，这就是样式冲突。

一般情况下，如果出现样式冲突，则会按照CSS书写的顺序，以最后的样式为准。

1. 样式冲突，遵循的原则是就近原则。那个样式离着结构近，就执行那个样式。
2. 样式不冲突，不会层叠

##### 	CSS继承性

书写CSS样式表时，子标签会继承父标签的`某些样式`，如文本颜色和字号。想要设置一个可继承的属性，只需将它应用于父元素即可。

##### 	CSS优先级

定义CSS样式时，经常出现两个或更多规则应用在同一元素上，这时就会出现优先级的问题。

- 继承样式的权重为0。即在嵌套结构中，不管父元素样式的权重多大，被子元素继承时，他的权重都为0，也就是说子元素定义的样式会覆盖继承来的样式。
- 行内样式优先。应用style属性的元素，其行内样式的权重非常高，可以理解为远大于100。总之，他拥有比上面提高的选择器都大的优先级。
- 权重相同时，CSS遵循就近原则。也就是说靠近元素的样式具有最大的优先级，或者说排在最后的样式优先级最大。

| 元素                                 | 贡献值                    |
| ---------------------------------- | ---------------------- |
| 继承、*                               | 0，0，0，0                |
| 每个标签                               | 0，0，0，1                |
| 类、伪类                               | 0，0，1，0                |
| ID                                 | 0，1，0，0                |
| 行内样式                               | 1，0，0，0                |
| !important                         | 无穷大                    |
| max-height、max-width覆盖width、height | 大于无穷大                  |
| min-height、min-width               | 大于max-height、max-width |

计算规则

1. 遇到有贡献值的就进行**累加**，例如： div ul li ---> 0,0,0,3 .nav ul li ---> 0,0,1,2 a:hover ---> 0,0,1,1 .nav a ---> 0,0,1,1 #nav p ---> 0,1,0,1
2. **数位没有进位：** 0,0,0,5+0,0,0,5 = 0,0,0,10而不是0,0,1,0，所以**不会存在10个div能赶上一个类选择器的情况** 。
3. **权重不会继承**，所以父元素的权重再高也和子元素没有关系
4. 如果有`!important`那么相加的那些无论多高就不管用，如果有`max-height/max-width`那么`!important`不管用，如果同时有`min-height/min-width`和 `max-height/max-width`，那么`max-height/max-width`的不管用

#### 常用单位

- px（像素）：px是绝对单位，一个像素代表一个点。如100px*100px的正方形，则是由宽度100个点，长度100个点组成的平面

- em：em是相对单位，它的参考对象是它的父级的字号，如父级字号是16px，如果设置元素的字号大小为2em的话，元素的字号大小则为32px

- rem：rem由页面的根元素html的字号决定，浏览器一般默认的字号为16px，如设置元素的字号大小为1rem，则元素的字号大小为16px

  使用rem的好处是：当我们改变了浏览器的字号设置时，页面的字号也会随之发生变化，这个设置会非常方便老年人浏览网页

- 百分比相对与父元素的比例

#### 元素常用属性

##### 	字体font

- **大小 ：font-size**

  x-large (特大) xx-small;(极小) 一般中文用不到，

  还可以使用单位来表示：ps em rem

- **样式：font-style**

  方便 italic(斜体) normal(正常)

- **行高：line-height**

  normal(正常) 单位：PX、EM

- **粗细：font-weight**

  bold(粗体) lighter(细体) normal(正常)

  还可使用数值100-900，定义由粗到细的字符。400 等同于 normal，而 700 等同于 bold。

- **修饰：text-decoration**

  underline(下划线) overline(上划线) line-through(删除线)

- **常用字体： (font-family)**

  "Courier New", Courier, monospace, "Times New Roman", Times, serif, Arial, Helvetica, sans-serif, Verdana

##### 	背景属性

背景颜色

1. 三基色比例表示， #RRGGBB，前两位是红色，中间2位是绿色，后两位是蓝色，最小是0，最大是F。
2. rgba表示方式，rgb(255,0,0)或者rgb(100%,0,0)表示红色，可以传第四个参数代表透明度
3. 英文单词  background-color: #FFFFFF background-color: rgb(255,0,0,.5) background-color: red

图片：{background-image: url();}

重复：{background-repeat: no-repeat;}

滚动：background-attachment:  fixed(固定) scroll(滚动)

位置：background-position：left(左) top(垂直)

简写：background: #444444 url(2.jpg) no-repeat fixed top left;

