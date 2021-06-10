package com.fh.shop.api.member.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.BaseController;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.common.Constants;
import com.fh.shop.api.member.common.KeyUtil;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api")
@Api(tags = "会员接口")
public class MemberController extends BaseController{


    @Resource(name = "memberService")
    private IMemberService memberService;

    @Autowired
    private HttpServletRequest request;



    @ApiOperation("登录")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName",value = "会员名",dataType = "java,lang,String",required = true),
            @ApiImplicitParam(name = "password",value = "密码",dataType = "java,lang,String",required = true)
    })
    public ServerResponse login(String memberName, String password){
        return memberService.login(memberName,password);
    }


    @ApiOperation("查询会员信息")
    @GetMapping("/member/findMember")
    @ApiImplicitParam(name = "x-auth",value = "头信息",dataType = "java.lang.String",required = true,paramType = "header")
    public ServerResponse findMember() throws UnsupportedEncodingException {
//        MemberVo memberVo = (MemberVo) request.getAttribute(Constants.CURR_MEMBER);
        MemberVo memberVo = buildMemberVo(request);
        return ServerResponse.success(memberVo);

    }

    @ApiOperation("登录超时")
    @GetMapping("/member/loginOut")
    public ServerResponse loginOut() throws UnsupportedEncodingException {
        String memberVoJson = URLDecoder.decode(request.getHeader(Constants.CURR_MEMBER),"utf-8");
        MemberVo memberVo = JSON.parseObject(memberVoJson,MemberVo.class);
        RedisUtil.delete(KeyUtil.buildMemberKey(memberVo.getId()));
        return ServerResponse.success();
    }



}
