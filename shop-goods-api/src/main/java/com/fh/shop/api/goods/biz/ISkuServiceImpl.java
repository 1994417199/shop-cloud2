package com.fh.shop.api.goods.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.goods.mapper.ISkuMapper;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.api.goods.vo.SkuVo;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@EnableScheduling
@Service("skuService")
public class ISkuServiceImpl implements ISkuService {
    @Autowired
    private ISkuMapper skuMapper;



    @Override
    public ServerResponse findRecommedNewProductList() {
        String skuList1 = RedisUtil.get("skuList");
        if(StringUtils.isNotEmpty(skuList1)){
            //将json转换为java对象
            List<SkuVo> skuVos = JSON.parseArray(skuList1, SkuVo.class);
            return ServerResponse.success(skuVos);
        }
        List<Sku> skuList = skuMapper.findRecommedNewProductList();
        List<SkuVo> skuVoList = skuList.stream().map(x -> {
            SkuVo skuVo = new SkuVo();
            skuVo.setId(x.getId());
            skuVo.setSkuName(x.getSkuName());
            skuVo.setPrice(x.getPrice().toString());
            skuVo.setImage(x.getImage());
            return skuVo;
        }).collect(Collectors.toList());
        //将java装换为json
        String s = JSON.toJSONString(skuVoList);
        //放入到redis
        RedisUtil.setEx("skuList",20,s);
        return ServerResponse.success(skuVoList);
    }

    @Override
    public ServerResponse findSku(Long id) {
        Sku sku = skuMapper.selectById(id);
        return ServerResponse.success(sku);
    }


}
