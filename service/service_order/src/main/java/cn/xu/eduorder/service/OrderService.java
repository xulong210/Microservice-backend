package cn.xu.eduorder.service;

import cn.xu.eduorder.entity.Order;
import cn.xu.eduorder.entity.vo.CourseVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author xulong
 * @since 2022-04-19
 */
public interface OrderService extends IService<Order> {

    String createOrder(CourseVo courseVo, String memberId);
}
