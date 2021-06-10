package com.fh.shop.api.goods.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SkuVo {
    @ApiModelProperty(value = "skuID",example = "0")
    private Long id;
    @ApiModelProperty(value = "skuName")
    private String skuName;
    @ApiModelProperty(value = "价格")
    private String price;
    @ApiModelProperty(value = "图片")
    private String image;
}
