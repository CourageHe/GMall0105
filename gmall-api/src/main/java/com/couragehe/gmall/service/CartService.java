package com.couragehe.gmall.service;

import com.couragehe.gmall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(String memberId);

    void addCart(OmsCartItem omsCartItem);

    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    List<OmsCartItem> cartList(String userId);

    void checkCart(OmsCartItem omsCartItem);
}
