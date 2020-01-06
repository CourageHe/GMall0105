package com.couragehe.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.couragehe.gmall.bean.PmsProductSaleAttr;
import com.couragehe.gmall.bean.PmsSkuInfo;
import com.couragehe.gmall.bean.PmsSkuSaleAttrValue;
import com.couragehe.gmall.service.SkuService;
import com.couragehe.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class ItemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap modelMap, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();//获得的是nginx的ip
        //request.getHeader("") //nginx负载均衡
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId, remoteAddr);
        //sku对象
        modelMap.put("skuInfo", pmsSkuInfo);
        //销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(), pmsSkuInfo.getId());
        modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);

        //查询当前sku的spu的其他sku的集合的hash表
        Map<String, String> skuSaleAttrHash = new HashMap<String, String>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> pmsSkuAttrValueList = skuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
                if (!k.equals("")) {
                    k = k + "|";
                }
                k += pmsSkuAttrValue.getSaleAttrValueId();//239|245
            }
            skuSaleAttrHash.put(k, v);
        }
        //将sku的销售属性hash表放到页面
        String skuSaleAttrHashStr = JSON.toJSONString(skuSaleAttrHash);
        modelMap.put("skuSaleAttrHashStr", skuSaleAttrHashStr);
        return "item";
    }

    /**
     * 返回字符串 会首先前往templte下寻找对应名字的html文件
     *
     * @return
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap) {
        //thymeleaf页面传值
        modelMap.put("hello", "hello Thymelead!!!");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("循环数据" + i);
        }
        modelMap.put("list", list);
        modelMap.put("checknum", 1);
        return "index";
    }
}
