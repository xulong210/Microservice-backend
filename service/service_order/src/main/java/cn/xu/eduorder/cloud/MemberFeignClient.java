package cn.xu.eduorder.cloud;

import cn.xu.commonutils.vo.UMember;
import org.springframework.stereotype.Component;

@Component
public class MemberFeignClient implements MemberClient {
    @Override
    public UMember getInfo(String id) {
        return null;
    }
}
