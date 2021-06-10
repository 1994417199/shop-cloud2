package com.fh.shop.api.cate.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class Cate implements Serializable {
    @ApiModelProperty(value = "分类id",example = "0")
    private Long id;

    @ApiModelProperty("分类名")
    private String cateName;
    @ApiModelProperty(value = "父id",example = "0")
    private Long fatherId;
    @ApiModelProperty(value = "类型id",example = "0")
    private Long typeId;
    @ApiModelProperty("类型名")
    private String typeName;
}
