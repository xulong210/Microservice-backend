package cn.xu.video.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    String uploadVideo(MultipartFile file);

    void deleteById(String id);

    void deleteByIds(List<String> videoIdList);
}
