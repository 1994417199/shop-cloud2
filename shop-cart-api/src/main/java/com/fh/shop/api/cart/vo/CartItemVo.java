package com.fh.shop.api.cart.vo;

import lombok.Data;


@Data
public class CartItemVo {

    private String skuName;

    private Long skuId;

    private String skuImage;

    private String price;

    private Long count;

    private String subPrice;
}
