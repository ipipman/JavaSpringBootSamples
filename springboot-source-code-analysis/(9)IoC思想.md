### IoC思想



<img src="https://tva1.sinaimg.cn/large/008i3skNly1guohvq3supj61cq0u0gpc02.jpg" alt="image-20210921195432177" width="700" align="left" />



- 松耦合
  - 降低强耦合关系，对象与对象之间的依赖不用new，而是通过Spring容器@Autowired注入完成
- 灵活性
  - 不需要初始化使用类的构造方法，而是通过Spring容器完成初始化工作
- 可维护
  - 通过Spring xml或注解 的方式，可以清晰的了解类与类之间的依赖关系，提升可读性

