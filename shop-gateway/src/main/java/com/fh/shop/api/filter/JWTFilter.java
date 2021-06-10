package com.fh.shop.api.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import com.fh.shop.vo.MemberVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.ResponseContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
@Slf4j
@Component
public class JWTFilter extends ZuulFilter {

    @Value("${fh.shop.checkUrls}")
    private List<String> checkUrls;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {

        //获取当前访问的url
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //处理options请求
        String requestMethod = request.getMethod();
        if(requestMethod.equalsIgnoreCase("options")){
            //不进行处理 拦截
            currentContext.setSendZuulResponse(false);
            return null;
        }

        StringBuffer requestURL = request.getRequestURL();
        boolean isCheck = false;
        for (String checkUrl : checkUrls) {
            if(requestURL.indexOf(checkUrl)>0){
                isCheck = true;
            }
        }
        if(!isCheck){
            //默认相当于放行
            return null;
        }
        //验证
        //判断是否有头信息
        String header = request.getHeader("x-auth");
        if(StringUtils.isEmpty(header)){
            //不仅拦截，而且还给前台提示
//            throw new ShopException(ResponseEnum.HEAD_INFO_IS_NOT);
            return buildResponse(ResponseEnum.HEAD_INFO_IS_NOT);
        }

        //判断头信息是否完整
        String[] headArr = header.split("\\.");
        if(headArr.length!=2){
            return buildResponse(ResponseEnum.HEAD_INFO_IS_NOT_FULL);
        }
        //验签
        String memberVoJsonBase64 = headArr[0];
        String signBase64 = headArr[1];
        //进行base64解码，怎么把字节数组变为字符串
        String memberVoJson = new String(Base64.getDecoder().decode(memberVoJsonBase64), "utf-8");
        String sign = new String(Base64.getDecoder().decode(signBase64), "utf-8");
        String newSign = Md5Util.sign(memberVoJson,Constants.SECRET);
        if(!newSign.equals(sign)){
            return buildResponse(ResponseEnum.TOKEN_IS_FAIL);
        }
        //将json转化为java对象
        MemberVo memberVo = JSON.parseObject(memberVoJson, MemberVo.class);
        Long id = memberVo.getId();
        //判断是否过期
        if(!RedisUtil.exist(KeyUtil.buildMemberKey(id))){
            return buildResponse(ResponseEnum.TYPE_INFO_IS_NULL);
        }
        //续命
        RedisUtil.expire(KeyUtil.buildMemberKey(id),Constants.TOKEN_EXPIRE);
        //将memberVo存到request
        request.setAttribute(Constants.CURR_MEMBER,memberVo);
        //将传给后台微服务的信息放到请求头中
        currentContext.addZuulRequestHeader(Constants.CURR_MEMBER,URLEncoder.encode(memberVoJson,"utf-8"));
        return null;
    }

    private Object buildResponse(ResponseEnum responseEnum) {

        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json;charset=utf-8");//解决中文乱码问题
        currentContext.setSendZuulResponse(false);//拦截了，不会在进行路由转发
        //提示json信息
        ServerResponse error = ServerResponse.error(responseEnum);
        String res = JSON.toJSONString(error);
        currentContext.setResponseBody(res);
        return null;
    }
}
