package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cart.vo.CartItemVo;
import com.fh.shop.api.cart.vo.CartVo;
import com.fh.shop.api.goods.IGoodsFeignService;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.BigdecimalUtil;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("cartService")
@Transactional(rollbackFor = Exception.class)
public class ICartServiceImpl implements ICartService {

    @Autowired
    private IGoodsFeignService goodsFeignService;
    @Value("${count.limit}")
    private int countLimit;

    @Override
    public ServerResponse addCartItem(Long id, Long skuId, Long count) {
        //购买数量超过10
        if(count>countLimit){
            return ServerResponse.error(ResponseEnum.CART_SKU_BUY_TO_MANY);
        }
        //商品是否存在
        //**********************
        ServerResponse<Sku> skuResponse = goodsFeignService.findSku(skuId);
        Sku sku = skuResponse.getData();
        //**********************
        if(sku==null){
            return ServerResponse.error(ResponseEnum.CART_SKU_IS_NULL);
        }
        //商品是否上架
        if(sku.getStatus().equals(Constants.STATUS_DOWN)){
            return ServerResponse.error(ResponseEnum.CART_SKU_IS_DOWN);
        }
        //库存的数量大于购买数量
        if(sku.getStock()<count){
            return ServerResponse.error(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
        }
        //是否有购物车
        String key = KeyUtil.buildCartKey(id);
        boolean exist = RedisUtil.exist(key);
        if(!exist){
            if(count<0){
                return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
            }
            //没有购物车，创建一个，直接把商品放入购物车
            CartVo cartVo = new CartVo();
            CartItemVo cartItemVo = new CartItemVo();
            cartItemVo.setCount(count);
            cartItemVo.setSkuId(skuId);
            cartItemVo.setSkuImage(sku.getImage());
            cartItemVo.setSkuName(sku.getSkuName());
            String price = sku.getPrice()+"";
            cartItemVo.setPrice(price);
            cartItemVo.setSubPrice(BigdecimalUtil.mul(count+"",price).toString());
            cartVo.getCartItemVoList().add(cartItemVo);
            cartVo.setTotalCount(count);
            cartVo.setTotalPrice(cartItemVo.getPrice());
            //更新购物车
            RedisUtil.hset(key,Constants.CART_JSON_FIELD,JSON.toJSONString(cartVo));
            RedisUtil.hset(key,Constants.CART_COUNT_FIELD,cartVo.getTotalCount()+"");

        }else {

            //如果有购物车，购物车中没有这款商品直接加入这款商品
            String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
            CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
            List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
            Optional<CartItemVo> item = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId).findFirst();
            if(item.isPresent()){
                //购物车有这款商品，找到这款商品，更新商品的数量、小计
                CartItemVo cartItemVo = item.get();
                long count1 = cartItemVo.getCount() + count;
                //购买数量超过10
                if(count1>countLimit){
                    return ServerResponse.error(ResponseEnum.CART_SKU_BUY_TO_MANY);
                }
                if(count1 <=0){
                    //从购物车删除当前商品
                    cartItemVoList.removeIf(x -> x.getSkuId().longValue()==skuId.longValue());
                    if(cartItemVoList.size()==0){
                        RedisUtil.delete(key);
                        return ServerResponse.success();
                    }
                    //更新
                    updateCart(key, cartVo);
                    return ServerResponse.success();
                }
                cartItemVo.setCount(count1);
                BigDecimal subPrice = new BigDecimal(cartItemVo.getSubPrice());
                String subPriceStr = subPrice.add(BigdecimalUtil.mul(cartItemVo.getPrice(), count + "")).toString();
                cartItemVo.setSubPrice(subPriceStr);
                updateCart(key, cartVo);


            }else {
                if(count<0){
                    return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
                }
                //购物车中没有这款商品，直接将商品加入购物车
                CartItemVo cartItemVo = new CartItemVo();
                cartItemVo.setCount(count);
                cartItemVo.setSkuId(skuId);
                cartItemVo.setSkuImage(sku.getImage());
                cartItemVo.setSkuName(sku.getSkuName());
                String price = sku.getPrice()+"";
                cartItemVo.setPrice(price);
                cartItemVo.setSubPrice(BigdecimalUtil.mul(count+"",price).toString());
                cartVo.getCartItemVoList().add(cartItemVo);
                updateCart(key, cartVo);
            }
        }
        return ServerResponse.success();
    }

    private void updateCart(String key, CartVo cartVo) {
        //更新购物车
        List<CartItemVo> cartItemVos = cartVo.getCartItemVoList();
        long totalCount=0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemVo itemVo : cartItemVos) {
            totalCount+=itemVo.getCount();
            totalPrice=totalPrice.add(new BigDecimal(itemVo.getSubPrice()));
        }
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        //更新购物车
        RedisUtil.hset(key,Constants.CART_JSON_FIELD,JSON.toJSONString(cartVo));
        RedisUtil.hset(key,Constants.CART_COUNT_FIELD,cartVo.getTotalCount()+"");
    }

    @Override
    public ServerResponse findCart(Long memberId) {
        String cartJson = RedisUtil.hget(KeyUtil.buildCartKey(memberId),Constants.CART_JSON_FIELD);
        CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
        return ServerResponse.success(cartVo);
    }

    @Override
    public ServerResponse findCartCount(Long memberId) {
        String count = RedisUtil.hget(KeyUtil.buildCartKey(memberId), Constants.CART_COUNT_FIELD);
        return ServerResponse.success(count);
    }

    @Override
    public ServerResponse deleteCartItem(Long memberId, Long skuId) {
        //获取会员对应的购物车
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
        CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Optional<CartItemVo> itemVo = cartItemVoList.stream().filter(x -> x.getSkuId().longValue() == skuId.longValue()).findFirst();
        if(!itemVo.isPresent()){
            return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
        }
        cartItemVoList.removeIf(x ->x.getSkuId().longValue()==skuId.longValue());
        if(cartItemVoList.size() == 0){
            RedisUtil.delete(key);
            return ServerResponse.success();
        }
        updateCart(key,cartVo);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse batchDeleteCartItem(Long memberId, String ids) {
        if(StringUtils.isEmpty(ids)){
            return ServerResponse.error(ResponseEnum.CART_BATCH_DELETE_NO_SELECT);
        }
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
        CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Arrays.stream(ids.split(",")).forEach(x -> cartItemVoList.removeIf(y ->y.getSkuId().longValue()==Long.parseLong(x)));
        if(cartItemVoList.size()==0){
            RedisUtil.delete(key);
            return ServerResponse.success();
        }
        //更新购物车
        updateCart(key,cartVo);
        return ServerResponse.success();
    }
}