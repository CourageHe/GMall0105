package com.couragehe.gmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @PackageName:com.couragehe.gmall.bean
 * @ClassName:PmsSearchParam
 * @Description:
 * @Autor:CourageHe
 * @Date: 2020/1/8 20:29
 */
public class PmsSearchParam  implements Serializable {
    String catalog3Id;
    String keyword;
    List<PmsSkuAttrValue> skuAttrValueList;

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return skuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        this.skuAttrValueList = skuAttrValueList;
    }
}
