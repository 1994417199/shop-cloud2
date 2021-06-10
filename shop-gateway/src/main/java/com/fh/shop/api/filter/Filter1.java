package com.fh.shop.api.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
@Slf4j
public class Filter1 extends ZuulFilter {

    /**
    * pre:所有的客户端请求在访问真正的微服务之前执行
     * router
     *post：访问微服务之后执行
     * error
     * @return
     * */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 执行顺序，值越小优先级越高
     */

    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否生效
     * true
     * false
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     *业务逻辑的处理
     * 永远返回null
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        log.info("-----------pre-------------");
        return null;
    }
}
