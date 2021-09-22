package com.ipman.swagger.sample.controller;

import com.ipman.swagger.sample.entity.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by ipipman on 2021/9/22.
 *
 * @version V1.0
 * @Package com.ipman.swagger.sample.controller
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/22 6:29 下午
 */
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
