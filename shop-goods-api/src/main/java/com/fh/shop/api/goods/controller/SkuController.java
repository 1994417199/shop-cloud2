package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.ISkuService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Api(tags = "商品接口")
@Component
@Slf4j
public class SkuController {

    @Value("${server.port}")
    private String port;

    @Resource(name = "skuService")
    private ISkuService skuService;



    private static final long SECOND = 1000;

    @ApiOperation("查询sku")
    @GetMapping("/sku/recommend/newProduct")
    public ServerResponse query(){
        log.info("端口号：",port);
        return skuService.findRecommedNewProductList();
    }

    @GetMapping("/sku/findSku")
    public ServerResponse findSku(@RequestParam("id") Long id){
        return skuService.findSku(id);
    }

}
