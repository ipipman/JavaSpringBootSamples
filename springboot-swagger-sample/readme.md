### SpringBoot集成Swagger用例



#### 什么是 Swagger 2

**Swagger 2** 是一个开源软件框架，可以帮助开发人员设计、构建、记录和使用 **RESTful Web** 服务，它将代码和文档融为一体，可以完美解决文档编写繁琐、维护不方便等问题。使得开发人员可以将大部分精力集中到业务中，而不是繁杂琐碎的文档中。



 #### 在Maven工程中引入Swagger和Swagger UI

```java
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
  </dependency>

  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
  </dependency>
```



#### 配置Swagger Bean

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 注入SwaggerDocketBean
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ipman.swagger.sample.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 配置Swagger文档说明
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API测试文档")
                .description("DEMO项目的接口测试文档")
                .termsOfServiceUrl("http://www.hangge.com")
                .version("1.0")
                .contact(new Contact("ipman",
                        "http://www.ipman.com",
                        "ipipman@163.com"))
                .build();
    }
}
```



#### 编写Controller，配置Swagger接口文档说明

```java
@RestController
@Api(tags = "用户数据接口") // @Api 注解标注在类上用来描述整个 Controller 信息。
public class UserController {

    /**
     * @ApiOperation 注解标注在方法上，用来描述一个方法的基本信息。
     */
    @ApiOperation(value = "修改用户", notes = "传入用户信息进行更新修改")
    @PutMapping("/user")
    public String updateUser(@RequestBody User user){
        return user.toString();
    }

    /**
     * @ApiImplicitParam 注解标注在方法上，用来描述方法的参数。其中 paramType 是指方法参数的类型，有如下可选值：
     * path：参数获取方式为 @PathVariable
     * query：参数获取方式为 @RequestParam
     * header：参数获取方式为 @RequestHeader
     * body
     * form
     */
    @ApiOperation(value ="查询用户", notes = "根据 id 查询用户")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户 id", required = true)
    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable Integer id){
        return "查找的用户id是：" + id;
    }

    /**
     * 如果有多个参数，可以将多个参数的 @ApiImplicitParam 注解放到 @ApiImplicitParams 中
     */
    @ApiOperation(value = "新增用户", notes = "根据传入的用户名和密码添加一个新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username",
                    value = "用户名", required = true, defaultValue = "test"),
            @ApiImplicitParam(paramType = "query", name = "password",
                    value = "密码", required = true, defaultValue = "123")
    })
    @PostMapping("/user")
    public String addUser(@RequestParam String username, @RequestParam String password) {
        return "新增用户：" + username + " " + password;
    }

    /**
     * @ApiResponse 是对响应结果的描述。code 表示响应码，message 为相应的描述信息。如果有多个 @ApiResponse，则放在一个 @ApiResponses 中
     */
    @ApiOperation(value = "删除用户", notes = "根据 id 删除用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功！"),
            @ApiResponse(code = 500, message = "删除失败！")
    })
    @DeleteMapping("/user/{id}")
    public Integer deleteUserById(@PathVariable Integer id) {
        return id;
    }

    /**
     * @ApiIgnore 注解表示不对某个接口生成文档。
     */
    @ApiIgnore
    @GetMapping("/user/test")
    public String test() {
        return "这是一个测试接口，不需要在api文档中显示。";
    }
}
```



#### 启动项目，访问Swagger-UI：http://127.0.0.1:8080/swagger-ui.html#/%E7%94%A8%E6%88%B7%E6%95%B0%E6%8D%AE%E6%8E%A5%E5%8F%A3

<img src="/Users/huangyan110110114/Library/Application Support/typora-user-images/image-20210922203731622.png" alt="image-20210922203731622" width="1200" align="left" />

