package com.couragehe.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.couragehe.gmall.bean.PmsProductImage;
import com.couragehe.gmall.bean.PmsProductInfo;
import com.couragehe.gmall.bean.PmsProductSaleAttr;
import com.couragehe.gmall.bean.PmsProductSaleAttrValue;
import com.couragehe.gmall.manage.mapper.PmsProductImageMapper;
import com.couragehe.gmall.manage.mapper.PmsProductInfoMapper;
import com.couragehe.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.couragehe.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.couragehe.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductInfo> getSpuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);
        return pmsProductInfos;
    }

    @Override
    public List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId) {

        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        //封装商品销售属性值列表
        for(PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs){
            //attr_id 与 spu_id复合查询

            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            //销售属性id用的是系统的字典表id，不是销售属性表的主键id
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            pmsProductSaleAttrValue.setProductId(spuId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> getSpuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        //保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        //保存商品图片元信息
        List<PmsProductImage> pmsProductImages = pmsProductInfo.getSpuImageList();
        for(PmsProductImage pmsProductImage : pmsProductImages){
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        //保存销售属性信息
        List<PmsProductSaleAttr> PmsProductSaleAttrs = pmsProductInfo.getSpuSaleAttrList();
        for(PmsProductSaleAttr pmsProductSaleAttr : PmsProductSaleAttrs){
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
        }

        //保存销售属性值信息
        for(PmsProductSaleAttr pmsProductSaleAttr : PmsProductSaleAttrs){
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for(PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValues){
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }

        return "success";
    }
}
