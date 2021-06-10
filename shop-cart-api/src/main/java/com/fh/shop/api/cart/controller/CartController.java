package com.fh.shop.api.cart.controller;

import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.common.BaseController;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(tags = "购物车接口")
public class CartController extends BaseController {

    @Resource
    private ICartService cartService;
    @Autowired
    private HttpServletRequest request;


    @PostMapping("/addCartItem")
    @ApiOperation("增加购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId",value = "商品id",required = true,dataType = "java.lang.long"),
            @ApiImplicitParam(name = "count",value = "商品数量",required = true,dataType = "java.lang.long"),
            @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,dataType = "java.lang.String",paramType = "header")
    })
    public ServerResponse addCartItem(Long skuId, Long count){
        com.fh.shop.api.member.vo.MemberVo memberVo = buildMemberVo(request);
        Long id = memberVo.getId();
        return cartService.addCartItem(id,skuId,count);
    }

    @GetMapping("/findCart")
    @ApiOperation("查询购物车接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,dataType = "java,lang.long",paramType = "header")
    })
    public ServerResponse findCart(){
        com.fh.shop.api.member.vo.MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.findCart(memberId);
    }

    @GetMapping("/findCartCount")
    @ApiOperation("查询购物车商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",required = true,dataType = "java,lang.String",paramType = "header")
    })
    public ServerResponse findCartCount(){
        com.fh.shop.api.member.vo.MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.findCartCount(memberId);
    }

    @DeleteMapping("/deleteCartItem")
    @ApiOperation("删除购物车中的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.long",required = true,paramType = "header")
    })
    public ServerResponse deleteCartItem(Long skuId){
        com.fh.shop.api.member.vo.MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteCartItem(memberId,skuId);
    }

    @DeleteMapping("/batchDeleteCartItem")
    @ApiOperation("删除购物车中的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true,paramType = "header")
    })
    public ServerResponse batchDeleteCartItem(String ids){
        com.fh.shop.api.member.vo.MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.batchDeleteCartItem(memberId,ids);
    }


}
