package cn.xu.eduservice.cloudClient;

import cn.xu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoFeignClient implements VideoClient {
    @Override
    public R deleteVideo(String id) {
        return R.error().message("删除视频失败！");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频失败！");
    }
}
