package cn.xu.video.controller;

import cn.xu.commonutils.R;
import cn.xu.servicebase.exceptionHandler.MyException;
import cn.xu.video.service.VideoService;
import cn.xu.video.utils.ConstantProperties;
import cn.xu.video.utils.InitObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvideo/video")
//@CrossOrigin
public class VideoController {

    @Autowired
    private VideoService videoService;

    //上传视频
    @PostMapping("upload")
    public R uploadVideo(MultipartFile file){
        String videoId = videoService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id删除
    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){
        videoService.deleteById(id);
        return R.ok();
    }

    //删除阿里云多个视频
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        videoService.deleteByIds(videoIdList);
        return R.ok();
    }

    //根据视频id获取播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            //创建client
            DefaultAcsClient client = InitObject.initVodClient(ConstantProperties.KEY_ID, ConstantProperties.KEY_SECRET);
            //创建response和request
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置id
            request.setVideoId(id);
            //得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new MyException(20001,"获取视频凭证失败");
        }
    }
}
