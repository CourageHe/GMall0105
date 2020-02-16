package com.couragehe.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.couragehe.gmall.bean.UmsMember;
import com.couragehe.gmall.service.CartService;
import com.couragehe.gmall.service.UserService;
import com.couragehe.gmall.util.HttpclientUtil;
import com.couragehe.gmall.util.IpAddressUtil;
import com.couragehe.gmall.util.JwtUtil;
import com.couragehe.gmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.rmi.MarshalledObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.couragehe.gmall.passport.controller
 * @ClassName:PassportController
 * @Description:
 * @Autor:CourageHe
 * @Date: 2020/1/16 15:21
 */
@Controller
public class PassportController {


    @Reference
    UserService userService;

    /**
     * 微博登录
     * @param code 校验码
     * @return
     */
    @RequestMapping("vlogin")
    public String vlogin(HttpServletRequest request,String code){

        //授权码换取access_token
        String oauth2_url = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> map = new HashMap<>();
        map.put("client_id","2858943378");
        map.put("client_secret","389e0858fe013287c9158e815c67ee36");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://passport.gmall.com/vlogin");
        map.put("code",code);//授权有效期内可以使用，每新生成依次授权码，说明用户第三方数据进行重新授权，之前的access_token和授权码作废
        String aceessJson = HttpclientUtil.doPost(oauth2_url,map);
        Map<String,Object> aceessMap = JSON.parseObject(aceessJson,Map.class);

        //access_token换取用户信息
        String access_token = String.valueOf(aceessMap.get("access_token"));
        String uid = (String)aceessMap.get("uid");

        String users_show_url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String infoJson = HttpclientUtil.doGet(users_show_url);
        Map<String,String> infoMap = JSON.parseObject(infoJson,Map.class);

        //将用户信息保存至数据库，用户类型设置为微博用户
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceType(2+"");
        umsMember.setSourceUid(uid);
        umsMember.setAccessCode(code);
        umsMember.setAccessToken(access_token);
        //性别
        int gender = infoMap.get("gender").compareTo("m")==0?1:2;
        umsMember.setGender(gender+"");
        umsMember.setCity(infoMap.get("location"));
//        umsMember.setUsername(infoMap.get("screen_name"));
        umsMember.setNickname(infoMap.get("name"));
        umsMember.setIcon(infoMap.get("profile_image_url"));

        //检测是否登录过(仅使用uid作为查询条件)
        UmsMember umsMemberTest = new UmsMember();
        umsMemberTest.setSourceUid(umsMember.getSourceUid());
        UmsMember umsMemberCheck = userService.checkOauthUser(umsMemberTest);
        if(umsMemberCheck == null) {
            userService.addOauthUser(umsMember);
        }
        //经过上述判断 该客户肯定在数据库 可直接读取
        umsMember =  userService.getOauthUser(umsMemberTest);

        //生成jwt的token，并且重定向道首页，携带token
        String token = createTokenByJwt(request,umsMember);

        return "redirect:http://cart.gmall.com/cartList?token="+token;
    }
    /**
     *
     * @param currentIp
     * @param token
     * @return
     */
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String currentIp,String token){
        //通过jwt验证token真假
        Map<String,String > map = new HashMap<>();

        //按照设计的算法对参数进行加密后，生成token
        Map<String,Object> decodeMap = JwtUtil.decode(token,MD5Util.md52("2019gmall0105"),MD5Util.md52(currentIp));
        if(decodeMap != null){
            map.put("state","success");
            map.put("memberId",(String)decodeMap.get("memberId"));
            map.put("nickname",(String)decodeMap.get("nickname"));
        }else{
            //防止返回 NULL 造成空指针异常
            map.put("state","fail");
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(HttpServletRequest request,UmsMember umsMember){
        //调用服务验证用户名和密码
        UmsMember umsMemberLogin = userService.login(umsMember);

        String token = "";
        if(umsMemberLogin != null) {
            //登陆成功
            token = createTokenByJwt(request,umsMemberLogin);
             //将token存入redis一份
            userService.addUserToken(token,umsMemberLogin.getId());

        }else{
            //登陆失败
            //提示登陆密码错误即可
            token = "fail";
        }
        return token;
    }

    @RequestMapping("index")
    public String index(String originUrl, ModelMap modelMap){
        //将返回Url渲染至登录页面，便于直接跳转
        modelMap.put("originUrl",originUrl);
        return "index";
    }

    private String createTokenByJwt(HttpServletRequest request,UmsMember umsMember){
        String token = "";
        //用jwt制作token
        String memberId = umsMember.getId();
        String nickname = umsMember.getNickname();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("memberId",memberId);
        userMap.put("nickname",nickname);

            String ip = request.getHeader("x-forwarded-for");//通过nginx转发的客户端
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();//从request获取ip
            }
            if(StringUtils.isBlank(ip)){
                ip = "127.0.0.1";
            }
//        String ip =  IpAddressUtil.getIpAddress(request);
        //按照设计的算法对参数进行加密后，生成token
        token = JwtUtil.encode(MD5Util.md52("2019gmall0105"),userMap,MD5Util.md52(ip));
        return token;
    }
}
