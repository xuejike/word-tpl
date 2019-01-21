# Word-Tpl Word模板引擎[![](https://jitpack.io/v/xuejike/word-tpl.svg)](https://jitpack.io/#xuejike/word-tpl)

如果只是在word中替换变量，那是我们没用充分利用起word的功能，word-tpl实现了在word中使用我们常用的模板引擎来对word进行内容控制，比如if判断，for循环等，让生成报告更加方便简单。word-tpl中word操作采用POI进行编辑，而不是采用xml生成方式生成的，这样生成的word更加标准。
# 功能说明
* [x] 变量替换
* [x] 段落内条件判断
* [x] 段落间条件判断
* [x] 单元格内条件判断
* [ ] 段落内的for循环
* [ ] 段落间的for循环
* [ ] 表格间的for循环
# 使用效果
## word模板内容
![avatar](./word-tpl/Snipaste_2019-01-21_12-55-55.png)
## 转换后的效果
![avatar](./word-tpl/Snipaste_2019-01-21_12-56-19.png)
# 使用方式
## 引入类库
```groovy


repositories {
    maven { url 'https://jitpack.io' }

}

dependencies {
    implementation 'com.github.xuejike.word-tpl:word-tpl-core:bea94c8e07'
}


```
## 生成word文档
```java
 public class TestMain {
     public static void main(String[] args) throws IOException, TplBuildException {
         String tplFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl.docx";
         String outFile = "E:\\project\\word-tpl\\word-tpl\\var-tpl-out.docx";
 
         HashMap<String, Object> map = new HashMap<>();
         map.put("xuejike","薛纪克");
         WordTplUtils wordTplUtils = new WordTplUtils(new FreemarkerWordTplFactory());
         wordTplUtils.buildWord(new File(tplFile),outFile,map);
     }
 }

```
# 设计思想

# 二次开发

## 引入新的模板引擎

## 标签或者语法的扩展
