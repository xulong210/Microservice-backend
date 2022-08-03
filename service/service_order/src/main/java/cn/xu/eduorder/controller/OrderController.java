package cn.xu.eduorder.controller;


import cn.xu.commonutils.R;
import cn.xu.eduorder.entity.Order;
import cn.xu.eduorder.entity.vo.CourseVo;
import cn.xu.eduorder.service.OrderService;
import cn.xu.jwtUtils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //1.生产订单方法
    @PostMapping("createOrder")
    public R createOrder(@RequestBody CourseVo courseVo, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            return R.error().code(28004).message("请登录!");
        }
        String orderId = orderService.createOrder(courseVo,memberId);
        return R.ok().data("orderId",orderId);
    }


    //2.根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderId);
        Order order = orderService.getOne(queryWrapper);
        return R.ok().data("order",order);
    }

    //3.判断用户是否购买
    @GetMapping("isBuy/{courseId}/{memberId}")
    public R isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);
        int count = orderService.count(queryWrapper);
        return R.ok().data("isBuy",count);
    }





}

