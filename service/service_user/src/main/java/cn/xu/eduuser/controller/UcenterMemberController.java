package cn.xu.eduuser.controller;


import cn.xu.commonutils.R;
import cn.xu.commonutils.vo.UMember;
import cn.xu.eduuser.entity.UcenterMember;
import cn.xu.eduuser.entity.vo.RegisterVo;
import cn.xu.eduuser.service.UcenterMemberService;
import cn.xu.jwtUtils.JwtUtils;
import cn.xu.servicebase.exceptionHandler.MyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/eduuser/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        //实现登录 , 返回token值
        String token = "";
        try {
            token = ucenterMemberService.login(ucenterMember);
        }catch (MyException e){
            return R.error().data("msg",e.getMsg());
        }

        return R.ok().data("token",token);
    }


    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getUserInfo")
    public R getMemberInfo(HttpServletRequest request){
        //根据request对象获取头信息 返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库 根据用户id查询用户信息
        UcenterMember userInfo = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",userInfo);
    }

    //提供给订单模块的远程调用 用于存储订单信息(需要查询用户信息)
    @GetMapping("getInfoUC/{id}")
    public UMember getInfo(@PathVariable("id") String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UMember uMember = new UMember();
        BeanUtils.copyProperties(member,uMember);
        return uMember;
    }

    //查询某一天的注册人数 提供给数据统计模块远程调用
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = ucenterMemberService.countRegister(day);

        return R.ok().data("countRegister",count);
    }




}

