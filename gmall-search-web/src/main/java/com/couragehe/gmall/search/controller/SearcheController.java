package com.couragehe.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.couragehe.gmall.annotations.LoginRequired;
import com.couragehe.gmall.bean.PmsSearchParam;
import com.couragehe.gmall.bean.PmsSearchSkuInfo;
import com.couragehe.gmall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @PackageName:com.couragehe.gmall.search.controller
 * @ClassName:SearcheController
 * @Description:
 * @Autor:CourageHe
 * @Date: 2020/1/8 19:37
 */
@Controller
public class SearcheController {

    @Reference
    SearchService searchService;

    @RequestMapping("index.html")
    @LoginRequired(loginSuccess = false)
    public String index(){
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam,ModelMap modelMap){//三级分类，
        //调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);
        return "list";
    }
}
