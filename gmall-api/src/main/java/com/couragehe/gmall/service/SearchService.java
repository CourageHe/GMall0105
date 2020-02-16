package com.couragehe.gmall.service;

import com.couragehe.gmall.bean.PmsSearchParam;
import com.couragehe.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
