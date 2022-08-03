package cn.xu.eduuser.service;

import cn.xu.eduuser.entity.UcenterMember;
import cn.xu.eduuser.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author xulong
 * @since 2022-02-25
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openId);

    Integer countRegister(String day);
}
