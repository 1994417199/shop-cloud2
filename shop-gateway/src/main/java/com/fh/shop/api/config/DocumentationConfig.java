package com.fh.shop.api.config;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

//@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
//        resources.add(swaggerResource("搜索服务接口", "/shop-cate-api/v2/api-docs", "1.0"));// /api/search/是网关路由，/v2/api-docs是swagger中的
//        resources.add(swaggerResource("用户服务接口", "/shop-goods-api/v2/api-docs", "1.0"));
//        resources.add(swaggerResource("用户服务接口", "/shop-cart-api/v2/api-docs", "1.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}