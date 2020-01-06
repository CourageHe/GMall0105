package com.couragehe.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.couragehe.gmall.bean.PmsBaseAttrInfo;
import com.couragehe.gmall.bean.PmsBaseAttrValue;
import com.couragehe.gmall.bean.PmsBaseSaleAttr;
import com.couragehe.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;


    /**
     * 查找所有spu的属性
     *
     * @param catalog3Id
     * @return
     */
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id) {
        List<PmsBaseAttrInfo> baseAttrInfos = attrService.getAttrInfoList(catalog3Id);
        return baseAttrInfos;
    }

    /**
     * 保存新的spu属性值
     *
     * @param pmsBaseAttrInfo
     * @return
     */
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

    /**
     * 查询基本属性列表
     *
     * @return
     */
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> getBaseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.getBaseSaleAttrList();
        return pmsBaseSaleAttrs;
    }
}
