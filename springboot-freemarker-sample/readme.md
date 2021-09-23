### SpringBoot 集成Freemarker用列

#### 什么是Freemarker？

FreeMarker是一款模板引擎：即基于模板和数据源生成输出文本（html网页，配置文件，电子邮件，源代码）的通用工具。它是一个java类库。

FreeMarker最初被设计用来在MVC模式的Web开发框架中生成HTML页面，它没有被绑定到Servlet或HTML或任意Web相关的东西上。也可以用于非Web应用环境中。

模板编写使用FreeMarker Template Language(FTL)。使用方式类似JSP的EL表达式。模板中专注于如何展示数据，模板之外可以专注于要展示什么数据。



<img src="https://tva1.sinaimg.cn/large/008i3skNly1guqqjr2yghj62180u0ad302.jpg" alt="image-20210923182546179" align="left" width="500" />





#### 在SpringBoot工程中引入Freemarker依赖

```java
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-freemarker</artifactId>
  </dependency>
```



#### 在spring.properties中编写Freemarker配置

```java
# HttpServletRequest的属性是否可以覆盖controller中model的同名项
spring.freemarker.allow-request-override=false
# HttpSession的属性是否可以覆盖controller中model的同名项
spring.freemarker.allow-session-override=false
# 是否开启缓存
spring.freemarker.cache=false
# 模板文件编码
spring.freemarker.charset=UTF-8
# 是否检查模板位置
spring.freemarker.check-template-location=true
# Content-Type的值
spring.freemarker.content-type=text/html
# 是否将HttpServletRequest中的属性添加到Model中
spring.freemarker.expose-request-attributes=false
# 是否将HttpSession中的属性添加到Model中
spring.freemarker.expose-session-attributes=false
# 模板文件后缀
spring.freemarker.suffix=.ftl
# 模板文件位置
spring.freemarker.template-loader-path=classpath:/templates/
```



#### 编写.ftl 模版文件

```java
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>demo列表</title>
</head>
<body>
<table border="1">
    <tr>
        <td>编号</td>
        <td>名称</td>
    </tr>
    <#list students as student>
        <tr>
            <td>${student.idNo}</td>
            <td>${student.name}</td>
        </tr>
    </#list>
</table>
</body>
</html>
```



#### 编写@Controller类，测试Freemarker的ModelAndView功能

```java
@Controller
public class Demo {

    /**
     * 测试Freemarker对象与列表渲染
     */
    @GetMapping("/demo1")
    public ModelAndView demo1() {
        ModelAndView model = new ModelAndView("demo1");
        List<Student> list = new ArrayList<>();
        Student s1 = new Student();
        s1.setIdNo("No1");
        s1.setName("ipman");
        list.add(s1);
        Student s2 = new Student();
        s2.setIdNo("No2");
        s2.setName("ipipman");
        list.add(s2);
        model.addObject("students", list);
        return model;
    }

}
```



#### 编写@RestController，通过Freemarker的工具类获取渲染后的HTML代码

```java
@RestController
public class Demo1 {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 根据Freemarker模版渲染引擎，渲染HTML代码
     */
    @GetMapping("/demo2")
    public String demo2() throws IOException, TemplateException {
        List<Student> list = new ArrayList<>();
        Student s1 = new Student();
        s1.setIdNo("No1");
        s1.setName("ipman");
        list.add(s1);
        Student s2 = new Student();
        s2.setIdNo("No2");
        s2.setName("ipipman");
        list.add(s2);
        Map<String, Object> param = new HashMap<>();
        param.put("students", list);
        // 根据Freemarker模版渲染引擎，渲染HTML代码
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("demo1.ftl");
        return "html=[" + FreeMarkerTemplateUtils.processTemplateIntoString(template, param) + "]";
    }
}
```

