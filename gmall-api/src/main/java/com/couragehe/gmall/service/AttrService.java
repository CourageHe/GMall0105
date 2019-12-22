package com.couragehe.gmall.service;

import com.couragehe.gmall.bean.PmsBaseAttrInfo;
import com.couragehe.gmall.bean.PmsBaseAttrValue;
import com.couragehe.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

public interface AttrService {
    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> getBaseSaleAttrList();
}
