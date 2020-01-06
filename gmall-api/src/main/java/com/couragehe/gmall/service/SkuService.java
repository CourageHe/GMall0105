package com.couragehe.gmall.service;

import com.couragehe.gmall.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId, String ip);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);
}
