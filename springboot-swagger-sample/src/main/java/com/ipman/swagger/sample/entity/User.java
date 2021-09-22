package com.ipman.swagger.sample.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Created by ipipman on 2021/9/22.
 *
 * @version V1.0
 * @Package com.ipman.swagger.sample.entity
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/9/22 6:37 下午
 */
@Getter
@Setter
@ToString
@ApiModel(value = "用户实体类", description = "用户信息描述类")
public class User {

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;
}