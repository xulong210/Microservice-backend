package cn.xu.oss.controller;

import cn.xu.commonutils.R;
import cn.xu.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("upload")
    public R uploadOssFile(MultipartFile file){
        //返回上传到oss的url
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
