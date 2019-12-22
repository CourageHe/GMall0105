package com.couragehe.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.couragehe.gmall.bean.PmsSkuAttrValue;
import com.couragehe.gmall.bean.PmsSkuImage;
import com.couragehe.gmall.bean.PmsSkuInfo;
import com.couragehe.gmall.bean.PmsSkuSaleAttrValue;
import com.couragehe.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.couragehe.gmall.manage.mapper.PmsSkuImageMapper;
import com.couragehe.gmall.manage.mapper.PmsSkuInfoMapper;
import com.couragehe.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.couragehe.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //保存sku信息
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);

        //保存sku图片元信息
        List<PmsSkuImage>pmsSkuImages = pmsSkuInfo.getSkuImageList();
        for(PmsSkuImage pmsSkuImage: pmsSkuImages){
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        //保存sku平台信息
        List<PmsSkuSaleAttrValue>pmsSkuSaleAttrValues = pmsSkuInfo.getSkuSaleAttrValueList();
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue: pmsSkuSaleAttrValues){
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        //保存sku销售信息
        List<PmsSkuAttrValue>pmsSkuAttrValues = pmsSkuInfo.getSkuAttrValueList();
        for(PmsSkuAttrValue pmsSkuAttrValue: pmsSkuAttrValues){
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        return "success";
    }
}
