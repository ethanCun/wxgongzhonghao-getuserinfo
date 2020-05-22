package com.example.gongzhonghao.web;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@Slf4j
@Controller
@RequestMapping(value = "/wx")
public class WXController {

    //微信公众号授权测试数据: 微信公众号开发文档-》开始开发-》接口测试号申请
    private static final String appId = "wxe7aa1abba3445c85";
    private static final String appSecret = "265c97f290d2eac5acc35c76f26340b8";
    private static final String wxName = "otI3VvxGDuqfXRBYiUYbdVS7fJlw";
    private static final String wxAdmin = "www.myyj.xyz";

    //用户授权之后跳转到这个页面
    @GetMapping(value = "/index")
    public String index(){

        return "index";
    }

    //跳转到用户授权页面 如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=czy
    //code说明 ： code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期
    @GetMapping(value = "/login")
    public String wxgongzhonghao(){

        String ss = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+
                "&redirect_uri=" + URLUtil.encode("https://myyj.xyz:8888/wxzhongzhonghao/wx/index") +
                "&response_type=code&scope=snsapi_userinfo&state=czy#wechat_redirect";

        System.out.println(ss);

        return "redirect:"+ss;
    }

    //通过code换取网页授权access_token
    @ResponseBody
    @GetMapping(value = "/getAccessTokenWithCode")
    public String getAccessTokenWithCode(String code){

        String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

        Map<String, Object> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("code", code);
        map.put("grant_type", "authorization_code");

        String res = HttpUtil.get(baseUrl, map);

        return res;
    }

    //根据openid和access_token获取用户信息
    @ResponseBody
    @GetMapping(value = "/getUserInfo")
    public String getUserInfo(String openid, String access_token){

        String baseUrl = "https://api.weixin.qq.com/sns/userinfo";

        Map<String, Object> params = new HashMap<>();
        params.put("access_token", access_token);
        params.put("openid", openid);
        params.put("lang", "zh_CN");

        return HttpUtil.get(baseUrl, params);
    }
}
