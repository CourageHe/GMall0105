package com.couragehe.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.couragehe.gmall.bean.PmsProductImage;
import com.couragehe.gmall.bean.PmsProductInfo;
import com.couragehe.gmall.bean.PmsProductSaleAttr;
import com.couragehe.gmall.manage.util.PmsUploadUtils;
import com.couragehe.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
//允许跨域注解
@CrossOrigin(origins = "*" )
public class SpuController {

    @Reference
    SpuService spuService;

    /**
     * spu数据列表的查询
     * @param catalog3Id
     * @return
     */
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> getSpuList(String catalog3Id){
        List<PmsProductInfo> pmsProductInfos= spuService.getSpuList(catalog3Id);
        return pmsProductInfos;
    }

    /**
     * spu的商品信息列表的查询寻功能
     * @param spuId
     * @return
     */
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs= spuService.getSpuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        //将图片或者音视频上传到分布式的文件存储系统
        String imgUrl = PmsUploadUtils.uploadImage(multipartFile);

        //将图片的存储路径返回给页面
        return imgUrl;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        String  success = spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }


    /**
     * spu商品的图片列表
     * @param spuId
     * @return
     */
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> getSpuImageList(String spuId){
        List<PmsProductImage> pmsProductImages= spuService.getSpuImageList(spuId);
        return pmsProductImages;
    }
}

