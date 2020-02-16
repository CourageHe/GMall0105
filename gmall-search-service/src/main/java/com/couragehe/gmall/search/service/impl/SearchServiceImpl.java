package com.couragehe.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.couragehe.gmall.bean.PmsSearchParam;
import com.couragehe.gmall.bean.PmsSearchSkuInfo;
import com.couragehe.gmall.bean.PmsSkuAttrValue;
import com.couragehe.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @PackageName:com.couragehe.gmall.search.service.impl
 * @ClassName:SearchServiceImpl
 * @Description: Search服务类
 * @Autor:CourageHe
 * @Date: 2020/1/8 20:51
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        String searchDsl = getSearchDsl(pmsSearchParam);

        //用api执行复杂查询
        List<PmsSearchSkuInfo>  pmsSearchSkuInfos = new ArrayList<PmsSearchSkuInfo>();
        Search search = new Search.Builder(searchDsl).addIndex("gmall0105").addType("PmsSkuInfo").build();
        SearchResult result = null;
        try {
            result = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = result.getHits(PmsSearchSkuInfo.class);

        for(SearchResult.Hit<PmsSearchSkuInfo,Void> hit : hits){
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;

            //高亮部分显示替换
            Map<String,List<String>> highlight = hit.highlight;
            String skuName = highlight.get("skuName").get(0);
            pmsSearchSkuInfo.setSkuName(skuName);

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        System.out.println(pmsSearchSkuInfos.size());
        return pmsSearchSkuInfos;
    }

    private String getSearchDsl(PmsSearchParam pmsSearchParam) {
        List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSearchParam.getSkuAttrValueList();
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();

        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //filter
        if(StringUtils.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        if(pmsSkuAttrValues != null){
            for(PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValues){
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",pmsSkuAttrValue.getValueId());
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //must
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<b>");
        highlightBuilder.postTags("</b>");
        highlightBuilder.field("skuName");
        searchSourceBuilder.highlight(highlightBuilder);

        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //sort
        searchSourceBuilder.sort("id", SortOrder.DESC);


        String searchDsl = searchSourceBuilder.toString();
        System.out.println(searchDsl);
        return searchDsl;

    }
}
