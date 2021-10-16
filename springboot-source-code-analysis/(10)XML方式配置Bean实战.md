### XML方式配置Bean实战



#### 配置方式

##### 无参构造

> 定义一个普通对象

```java
public class Student {

    private String name;
    private Integer age;
    private List<String> classList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", classList=" + String.join(",", classList) +
                '}';
    }
}
```

> 无参构造器xml定义

```java
    <bean id="student" class="com.example.springboot.source.code.analysis.ioc.xml.Student">
        <property name="name" value="zhangsan"/>
        <property name="age" value="13"/>
        <property name="classList">
            <list>
                <value>math</value>
                <value>english</value>
            </list>
        </property>
    </bean>

```



##### 有参构造

>定义个有构造器的普通类

```java
public class Student {

    private String name;
    private Integer age;
    private List<String> classList;

    /**
     * 使用构造器注入
     *
     * @param name
     * @param age
     */
    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", classList=" + String.join(",", classList) +
                '}';
    }
}
```

> 定义有参数构造器xml配置

```java
    <bean id="student" class="com.example.springboot.source.code.analysis.ioc.xml.Student">
        <!--   有参构造器     -->
        <constructor-arg index="0" value="zhangsan"/>
        <constructor-arg index="1" value="13"/>
        <property name="name" value="zhangsan"/>
        <property name="age" value="13"/>
        <property name="classList">
            <list>
                <value>math</value>
                <value>english</value>
            </list>
        </property>
    </bean>
```



##### 静态工厂方法 和 实例工厂方法

> 定义一个普通的抽象类 和 继承类

```java
public abstract class Animal {

    abstract String getName();
}
```

```java
public class Cat extends Animal{
    @Override
    String getName() {
        return "Cat";
    }
}
```

```java
public class Dog extends Animal{
    @Override
    String getName() {
        return "Dog";
    }
}
```



> 定义一个抽象工厂类

```java
public class AnimalFactory {

    // 静态工厂
    public static Animal getAnimal(String type) {
        if ("Dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }

    // 实例工厂
    public Animal getAnimal1(String type) {
        if ("Dog".equals(type)) {
            return new Dog();
        } else {
            return new Cat();
        }
    }
}
```



> 定义一个服务类

```
public class HelloService {

    private Student student;

    private Animal animal;

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String hello() {
        //return student.toString();
        return animal.getName();
    }

}
```



> 静态工厂方法 和 实例工厂方法 XML 配置

```java
    <bean id="helloService" class="com.example.springboot.source.code.analysis.ioc.xml.HelloService">
        <property name="student" ref="student"/>
        <property name="animal" ref="cat"/>
    </bean>

    <!-- 静态工厂类  -->
    <bean id="dog" class="com.example.springboot.source.code.analysis.ioc.xml.AnimalFactory" factory-method="getAnimal">
        <constructor-arg value="Dog"/>
    </bean>

    <!-- 实例工厂类  -->
    <bean name="animalFactory" class="com.example.springboot.source.code.analysis.ioc.xml.AnimalFactory"/>
    <bean id="cat" factory-bean="animalFactory" factory-method="getAnimal1">
        <constructor-arg value="Cat"/>
    </bean>

```

