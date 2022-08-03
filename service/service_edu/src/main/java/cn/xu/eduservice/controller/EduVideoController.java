package cn.xu.eduservice.controller;


import cn.xu.commonutils.R;
import cn.xu.eduservice.cloudClient.VideoClient;
import cn.xu.eduservice.entity.EduVideo;
import cn.xu.eduservice.service.EduVideoService;
import cn.xu.servicebase.exceptionHandler.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author xulong
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/eduservice/video")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VideoClient videoClient;

    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    // 删除小节 同时要删除视频
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断是否有视频id
        if(!StringUtils.isEmpty(videoSourceId)) {
            R result = videoClient.deleteVideo(videoSourceId); //远程调用 实现视频删除
            if(result.getCode() == 20001){
                throw new MyException(200001,"删除视频失败.熔断器");
            }
        }
        eduVideoService.removeById(id);
        return R.ok();
    }

    @GetMapping("getVideoInfo/{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo video = eduVideoService.getById(id);
        return R.ok().data("video",video);
    }

    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

}

