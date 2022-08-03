package cn.xu.video.service.impl;

import cn.xu.servicebase.exceptionHandler.MyException;
import cn.xu.video.service.VideoService;
import cn.xu.video.utils.ConstantProperties;
import cn.xu.video.utils.InitObject;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    /*
            title:上传之后显示名称
            fileName:上传文件原始名称
            inputStream:上传文件输入流
         */

    @Override
    public String uploadVideo(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(ConstantProperties.KEY_ID, ConstantProperties.KEY_SECRET, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantProperties.KEY_ID, ConstantProperties.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new MyException(20001,"删除视频失败");
        }
    }

    @Override
    public void deleteByIds(List<String> videoIdList) {
        try {
            DefaultAcsClient client = InitObject.initVodClient(ConstantProperties.KEY_ID, ConstantProperties.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();

            //多个视频id的格式：1,2,3,4
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<videoIdList.size();i++){
                sb.append(videoIdList.get(i));
                sb.append(",");
            }
            String ids = sb.substring(0, sb.length() - 1);
            request.setVideoIds(ids);
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new MyException(20001,"删除视频失败");
        }
    }
}
