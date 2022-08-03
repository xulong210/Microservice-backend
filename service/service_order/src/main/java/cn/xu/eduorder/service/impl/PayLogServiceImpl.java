package cn.xu.eduorder.service.impl;

import cn.xu.eduorder.entity.Order;
import cn.xu.eduorder.entity.PayLog;
import cn.xu.eduorder.mapper.PayLogMapper;
import cn.xu.eduorder.service.OrderService;
import cn.xu.eduorder.service.PayLogService;
import cn.xu.eduorder.utils.ConstantProperties;
import cn.xu.eduorder.utils.HttpClient;
import cn.xu.servicebase.exceptionHandler.MyException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-04-19
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Override
    public Map createNative(String orderNo) {

        try {
            //1.根据订单号查询出信息
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no",orderNo);
            Order orderInfo = orderService.getOne(queryWrapper);

            //2.map设置生成二维码需要的参数
            Map map = new HashMap<>();
            map.put("appid", ConstantProperties.APP_ID);
            map.put("mch_id",ConstantProperties.PARTNER);
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body",orderInfo.getCourseTitle());
            map.put("out_trade_no",orderNo);
            map.put("total_fee",orderInfo.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            map.put("trade_type", "NATIVE");


            //3.发送httpClient请求，传递参数xml格式 微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(map, ConstantProperties.PARTNER_KEY));
            client.setHttps(true);
            client.post();
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);


            //4.得到发送请求返回结果
            Map m = new HashMap<>();
            m.put("out_trade_no", orderNo);
            m.put("course_id", orderInfo.getCourseId());
            m.put("total_fee", orderInfo.getTotalFee());
            m.put("result_code", resultMap.get("result_code"));
            m.put("code_url", resultMap.get("code_url"));
            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return m;
        }catch (Exception e){
            throw new MyException(20001,"生成二维码失败");
        }
    }

    //查询支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", ConstantProperties.APP_ID);
            m.put("mch_id", ConstantProperties.PARTNER);
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantProperties.PARTNER_KEY));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //订单表的支付状态设置为已支付
    @Override
    public void updateOrdersStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        orderService.updateById(order);
        //记录支付日志
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        this.baseMapper.insert(payLog);//插入到支付日志表
    }
}
