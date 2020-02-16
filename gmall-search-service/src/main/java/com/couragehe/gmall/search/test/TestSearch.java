package com.couragehe.gmall.search.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.couragehe.gmall.bean.PmsSearchSkuInfo;
import com.couragehe.gmall.bean.PmsSkuInfo;
import com.couragehe.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName:com.couragehe.gmall.search
 * @ClassName:TestSearch
 * @Description: 测试Search服务，导入数据，进行测试
 * @Autor:CourageHe
 * @Date: 2020/1/8 10:43
 */
@Controller
public class TestSearch {

    @Reference
    SkuService skuService;//查询mysql

    @Autowired
    JestClient jestClient;

    @RequestMapping("putSearch")
    public void put() throws IOException {
        //查询mysql数据
        List<PmsSkuInfo> pmsSkuInfoList= new ArrayList<PmsSkuInfo>();
        pmsSkuInfoList= skuService.getAllSku();
        //转化为es的数据结构
        List<PmsSearchSkuInfo>  pmsSearchSkuInfos = new ArrayList<PmsSearchSkuInfo>();
        for(PmsSkuInfo pmsSkuInfo : pmsSkuInfoList){
            PmsSearchSkuInfo pmsSearchSkuInfo =  new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        //导入es
        for(PmsSearchSkuInfo pmsSearchSkuInfo :pmsSearchSkuInfos){
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()+"").build();
            jestClient.execute(put);
        }

    }
    @RequestMapping("getSearch")
    public void get() throws IOException {
        //用api执行复杂查询
        List<PmsSearchSkuInfo>  pmsSearchSkuInfos = new ArrayList<PmsSearchSkuInfo>();

       /* String searchDsl = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": [\n" +
                "        {\n" +
                "         \"terms\": \n" +
                "          {\n" +
                "            \"skuAttrValueList.valueId\": [\"81\",\"84\",\"87\"]\n" +
                "          }\n" +
                "        },   \n" +
                "      \n" +
                "       {\n" +
                "         \"term\": \n" +
                "          {\n" +
                "            \"skuAttrValueList.valueId\": \"81\"\n" +
                "          }\n" +
                "      },   \n" +
                "      {\n" +
                "        \"term\": \n" +
                "        {\n" +
                "            \"skuAttrValueList.valueId\": \"84\"\n" +
                "        }\n" +
                "      }\n" +
                "      ]\n" +
                "     \n" +
                "      ,\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"skuName\": \"小米9\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        */
       //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","81");
        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("skuAttrValueList.valueId","84","84","87");
        boolQueryBuilder.filter(termQueryBuilder);
        boolQueryBuilder.filter(termsQueryBuilder);
            //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","小米9");

        boolQueryBuilder.must(termQueryBuilder);
        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //highlight
        searchSourceBuilder.highlight(null);

        String searchDsl = searchSourceBuilder.toString();

        Search search = new Search.Builder(searchDsl).addIndex("gmall0105").addType("PmsSkuInfo").build();
        SearchResult result = jestClient.execute(search);
        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = result.getHits(PmsSearchSkuInfo.class);

        for(SearchResult.Hit<PmsSearchSkuInfo,Void> hit : hits){
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        System.out.println(pmsSearchSkuInfos.size());

    }
}


