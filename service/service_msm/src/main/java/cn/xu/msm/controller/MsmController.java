package cn.xu.msm.controller;

import cn.xu.commonutils.R;
import cn.xu.msm.service.MsmService;
import cn.xu.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //1.从redis 中能取到就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //2.取不到 让tx云发
        code = RandomUtil.getFourBitRandom();
        boolean isSend = msmService.sendPhone(phone,code);
        if(isSend){
            redisTemplate.opsForValue().set(phone,code,1, TimeUnit.MINUTES);
            return R.ok();
        }
        return R.error().message("短信发送失败");
    }
}
