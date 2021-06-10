package com.fh.shop.api.cart.biz;

import com.fh.shop.common.ServerResponse;

public interface ICartService {
    ServerResponse addCartItem(Long id, Long skuId, Long count);

    ServerResponse findCart(Long memberId);

    ServerResponse findCartCount(Long memberId);

    ServerResponse deleteCartItem(Long memberId, Long skuId);

    ServerResponse batchDeleteCartItem(Long memberId, String ids);
}
