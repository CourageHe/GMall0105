package com.couragehe.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.couragehe.gmall.annotations.LoginRequired;
import com.couragehe.gmall.util.CookieUtil;
import com.couragehe.gmall.util.HttpclientUtil;
import com.couragehe.gmall.util.IpAddressUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.util.Map;

/**
 * @PackageName:com.couragehe.gmall.interceptors
 * @ClassName:AuthInterceptor
 * @Description:认证拦截器
 * @Autor:CourageHe
 * @Date: 2020/1/16 20:19
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
        //拦截代码

        //判断被拦截的请求的的访问的方法的注解（是否是需要拦截的）
        HandlerMethod hm = (HandlerMethod)handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        //是否拦截
        if(methodAnnotation == null){
            return true;
        }


        //新老token的四种状态处理
        String token = "";

        //没oldToken则以前没有登录或刚刚登陆
        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }

        //有新token则以前登录过 或token过期
        String newToken= request.getParameter("token");
        if(StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        //是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();//获得该请求是否必须登录成功


        //调用认证中心进行验证

        String success = "fail";
        Map<String,String> successMap = null;
        if(StringUtils.isNotBlank(token)){
            String successJson =  HttpclientUtil.doGet("http://passport.gmall.com/verify?token="+token+"&currentIp="+ IpAddressUtil.getIpAddress(request));
            successMap =   JSON.parseObject(successJson, Map.class);
            success = successMap.get("state");
        }
        if(loginSuccess){
            //必须登录的成功才能使用

            //如果验证不通过 则重定向至登录
            if(!success.equals("success")){
                //重定向回passport登录
                StringBuffer requestURL = request.getRequestURL();
                try {
                    response.sendRedirect("http://passport.gmall.com/index?originUrl="+requestURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;

            }
            //如果验证通过 则传值member等，并更新/保存token的Cookie
                //验证通过，覆盖cookie中的token,
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }



//            if(StringUtils.isBlank(token)){
//                //踢回认证中心
//            }else{
//
//            }
        }else{
            //没有登录也能用，但必须验证
            if(success.equals("success")){
                //需要将token携带用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
            }
        }
        System.out.println("进入拦截器的拦截方法");

        //拦截代码

        return true;
    }

}
