package cn.xu.eduservice.cloudClient;

import cn.xu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-video",fallback = VideoFeignClient.class)
public interface VideoClient {

    //定义调用方法的路径
    //根据视频id删除阿里云视频
    @DeleteMapping("/eduvideo/video/deleteVideo/{id}")
    R deleteVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvideo/video/deleteBatch")
    R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}
