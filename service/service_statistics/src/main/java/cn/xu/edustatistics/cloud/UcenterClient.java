package cn.xu.edustatistics.cloud;

import cn.xu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-user")
public interface UcenterClient {

    @GetMapping("/eduuser/member/countRegister/{day}")
    R countRegister(@PathVariable("day") String day);
}
