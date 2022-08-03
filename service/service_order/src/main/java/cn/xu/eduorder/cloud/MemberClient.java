package cn.xu.eduorder.cloud;

import cn.xu.commonutils.vo.UMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-user",fallback = MemberFeignClient.class)
public interface MemberClient {

    @GetMapping("/eduuser/member/getInfoUC/{id}")
    UMember getInfo(@PathVariable("id") String id);

}
