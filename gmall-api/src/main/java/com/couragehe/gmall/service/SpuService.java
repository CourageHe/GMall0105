package com.couragehe.gmall.service;

import com.couragehe.gmall.bean.PmsProductImage;
import com.couragehe.gmall.bean.PmsProductInfo;
import com.couragehe.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {
    public List<PmsProductInfo> getSpuList(String catalog3Id) ;

    List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId);

    List<PmsProductImage> getSpuImageList(String spuId);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);
}
