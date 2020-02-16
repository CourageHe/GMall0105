package com.couragehe.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.couragehe.gmall.util.HttpclientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackageName:com.couragehe.gmall.passport.controller
 * @ClassName:TestOauth2
 * @Description:测试社交登录
 * @Autor:CourageHe
 * @Date: 2020/2/12 22:53
 */
public class TestOauth2 {
    //App key  2858943378
    //App secret 389e0858fe013287c9158e815c67ee36
    //授权回调页 http://passport.gmall.com/vlogin

    public static String getCode() {
        // 1 获得授权码
        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=2858943378&response_type=code&redirect_uri=http://passport.gmall.com/vlogin");
        System.out.println(s1);

        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程

        // 2 返回授权码到回调地址
        return null;
    }
    public static String getAccess_token() {
        //3.post请求 access_token

        String s3 = "https://api.weibo.com/oauth2/access_token";

        Map<String,String> map = new HashMap<>();
        map.put("client_id","2858943378");
        map.put("client_secret","389e0858fe013287c9158e815c67ee36");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://passport.gmall.com/vlogin");
        map.put("code","35e3cd793c402a13da1a5aaf65c13f64");//授权有效期内可以使用，每新生成依次授权码，说明用户第三方数据进行重新授权，之前的access_token和授权码作废
        String aceessJson = HttpclientUtil.doPost(s3,map);
        Map<String,String> aceessMap = JSON.parseObject(aceessJson,Map.class);
        String access_token = aceessMap.get("access_token");
        System.out.println(access_token);

        return access_token;
    }
    public static Map<String,String> getUser_info() {
        //4.用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00xmx9DIylpTHD168634cc0bobI8YE&uid=7380294679";
        String infoJson = HttpclientUtil.doGet(s4);
        Map<String,String> infoMap = JSON.parseObject(infoJson,Map.class);
        System.out.println(infoJson);

        return infoMap;
    }
    public static void main(String[]args){
        //App key  2858943378
        //App secret 389e0858fe013287c9158e815c67ee36
        //授权回调页 http://passport.gmall.com/vlogin
//        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=2858943378&response_type=code&redirect_uri=http://passport.gmall.com/vlogin");
//        System.out.println(s1);
//
//        //授权码 35e3cd793c402a13da1a5aaf65c13f64
//        String s2 = "http://passport.gmall.com/vlogin?code=35e3cd793c402a13da1a5aaf65c13f64";

        //post请求 access_token

//        String s3 = "https://api.weibo.com/oauth2/access_token";
//
//        Map<String,String> map = new HashMap<>();
//        map.put("client_id","2858943378");
//        map.put("client_secret","389e0858fe013287c9158e815c67ee36");
//        map.put("grant_type","authorization_code");
//        map.put("redirect_uri","http://passport.gmall.com/vlogin");
//        map.put("code","35e3cd793c402a13da1a5aaf65c13f64");//授权有效期内可以使用，每新生成依次授权码，说明用户第三方数据进行重新授权，之前的access_token和授权码作废
//        String aceessJson = HttpclientUtil.doPost(s3,map);
//        Map<String,String> aceessMap = JSON.parseObject(aceessJson,Map.class);
//        String access_token = aceessMap.get("access_token");
//        System.out.println(access_token);

        //access_token 2.00xmx9DIylpTHD168634cc0bobI8YE
        //用access_token查询用户信息
//        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00xmx9DIylpTHD168634cc0bobI8YE&uid=7380294679";
//        String infoJson = HttpclientUtil.doGet(s4);
//        Map<String,String> infoMap = JSON.parseObject(infoJson,Map.class);
//        System.out.println(infoJson);
        getUser_info();

    }
}
