package com.fh.shop.api.member.param;

import com.fh.shop.api.member.po.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class MemberParam extends Member implements Serializable {

    @ApiModelProperty("自定义编码")
    private String code;

    @ApiModelProperty("再次输入密码")
    private String confirmPassword;
}
