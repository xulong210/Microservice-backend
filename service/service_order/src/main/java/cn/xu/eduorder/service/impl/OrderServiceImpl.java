package cn.xu.eduorder.service.impl;

import cn.xu.commonutils.vo.UMember;
import cn.xu.eduorder.cloud.MemberClient;
import cn.xu.eduorder.entity.Order;
import cn.xu.eduorder.entity.vo.CourseVo;
import cn.xu.eduorder.mapper.OrderMapper;
import cn.xu.eduorder.service.OrderService;
import cn.xu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xulong
 * @since 2022-04-19
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private MemberClient memberClient;

    //生产订单方法
    @Override
    public String createOrder(CourseVo courseVo, String memberId) {
        //通过远程调用 根据用户id获取用户信息
        UMember member = memberClient.getInfo(memberId);
        Order order = new Order();
        BeanUtils.copyProperties(courseVo,order);
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setMemberId(memberId);
        order.setNickname(member.getNickname());
        order.setMobile(member.getMobile());
        order.setStatus(0);
        order.setPayType(1);
        this.baseMapper.insert(order);

        return order.getOrderNo();
    }
}
