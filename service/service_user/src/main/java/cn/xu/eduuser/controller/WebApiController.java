package cn.xu.eduuser.controller;

import cn.xu.eduuser.entity.UcenterMember;
import cn.xu.eduuser.service.UcenterMemberService;
import cn.xu.eduuser.utils.ConstantWxUtils;
import cn.xu.eduuser.utils.HttpClientUtils;
import cn.xu.jwtUtils.JwtUtils;
import cn.xu.servicebase.exceptionHandler.MyException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
//@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WebApiController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @GetMapping("login")
    public String getWxCode(){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );
        return "redirect:"+url;
    }

    //获取扫描人信息
    //流程：获取code -> 根据code获取accesstoken和openId -> 根据accesstoken和openid请求固定地址
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            //通过httpClient获取accessToken 和 openId
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
            "?appid=%s" +
            "&secret=%s" +
            "&code=%s" +
            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //从accessTokenInfo字符串获取出来两个值 先把字符串转成map
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = (String) map.get("access_token");
            String openId = (String) map.get("openid");

            UcenterMember member = ucenterMemberService.getOpenIdMember(openId);
            if(member == null){
                //根据token和openid获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        accessToken,
                        openId
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap hashMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) hashMap.get("nickname");
                String headimgurl = (String) hashMap.get("headimgurl");

                //添加到数据库
                //1.先判断数据库表中是否存在相同的微信信息 不存在才添加。

                member = new UcenterMember();
                member.setOpenid(openId);
                member.setAvatar(headimgurl);
                member.setNickname(nickname);
                ucenterMemberService.save(member);
            }

            //生成token
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            throw new MyException(20001,"登陆失败");
        }
    }
}
