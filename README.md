
# Time Retrive

## 概述

> 时间提取工具，主要是对文本中的时间字符串进行识别，并转换成Java中的Calendar对象，识别过程如下：

    `Reading articles → Regex Matching → Normalization → Calendar objects`

> 下面是根据参考时间`05-06-2015`提取的时间:

    mon 2:35 == 05-04-2015 02:35 AM
    4pm == 05-06-2015 04:00 PM
    6 in the morning == 05-06-2015 06:00 AM
    friday 1pm == 05-08-2015 01:00 PM
    sat 7 in the evening == 05-09-2015 07:00 PM
    next month == 06-01-2015 00:00 AM
    17:00 == 05-06-2015 05:00 PM
    January 5 at 7pm == 01-05-2015 07:00 PM
    1979-05-27 05:00 == 05-27-1979 05:00 AM

> 这里的时间匹配顺序如下：

1. longer relative time regex
2. shorter relative time regex
3. longer absolute time regex
4. shorter absolute time regex

> 当获取到匹配的时间字符串时，使用`SimpleDateFormat`格式化绝对时间字符串，得到Java的`Calendar`对象；同时，考虑到相对时间，最后计算的是相对时间字符串的绝对时间，例如：字符串"sat 7 in the evening"相对`05-09-2015`的绝对时间是"05-09-2015 07:00 PM"。

## 如何从文本中提取时间

`根据给出的文本和参考时间来提取时间：`

```
    $ java info.hb.time.retrive.demo.Example <article_path> <reference_time>
```

**其中，`reference_time`必须是`MM-DD-YYYY`格式，即参考时间。**

`测试自带的训练数据：`

```
    $ java info.hb.time.retrive.demo.Example training
```

## 性能

> 对于测试数据`appointments.txt`，测试可获得1168个时间表达式，运行时间如下：

    real    0m4.309s
    user    0m6.825s
    sys     0m0.269s

## 注意事项

> 需要加多线程测试，DateFormatter有线程安全问题。

## 其他时间提取工具

> 可以参考FudanNLP自然语言处理工具包里面的时间提取工具。
