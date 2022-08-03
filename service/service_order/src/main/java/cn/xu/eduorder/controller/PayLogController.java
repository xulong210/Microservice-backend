package cn.xu.eduorder.controller;


import cn.xu.commonutils.R;
import cn.xu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/eduorder/payLog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        System.out.println("二维码集合"+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("查询订单状态"+map);
        if(map == null){
            return R.error().message("支付出错");
        }
        if(map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中");
    }

}

