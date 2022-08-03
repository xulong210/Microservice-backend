package cn.xu.eduservice.controller;

import cn.xu.commonutils.R;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin
public class EduLoginController {

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    /**
     * 用户信息
     * @return
     */
    @GetMapping("/info")
    public R info(){

        //https://edu-xulo.oss-cn-shanghai.aliyuncs.com/2022/02/06/b1d64b82-6bcd-429f-aa5a-63fbc870bb67avatar.png
        return R.ok().data("roles","admin").data("name","admin").data("avatar","");
    }

    /**
     * 用户退出
     * @return
     */
    @PostMapping("logout")
    public R logout(){

        return R.ok();
    }

}
