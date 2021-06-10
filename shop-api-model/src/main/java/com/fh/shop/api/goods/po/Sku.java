package com.fh.shop.api.goods.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel
public class Sku implements Serializable {
    @ApiModelProperty(value = "skuId",example = "0")
    private Long id;
    @ApiModelProperty(value = "spuId",example = "0")
    private Long spuId;
    @ApiModelProperty("skuName")
    private String skuName;
    @ApiModelProperty(value = "价格",example = "0")
    private BigDecimal price;
    @ApiModelProperty(value = "库存",example = "0")
    private Integer stock;
    @ApiModelProperty("规格信息")
    private String specInfo;
    @ApiModelProperty(value = "颜色ID",example = "0")
    private Long colorId;
    @ApiModelProperty("图片")
    private String Image;
    @ApiModelProperty(value = "状态",example = "0")
    private Integer status;
    @ApiModelProperty(value = "推荐否",example = "0")
    private Integer recommend;
    @ApiModelProperty(value = "新品否",example = "0")
    private Integer newProduct;
    @ApiModelProperty(value = "销量",example = "0")
    private Integer sales;


}
