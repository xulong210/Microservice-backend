package cn.xu.oss.service.impl;

import cn.xu.oss.service.OssService;
import cn.xu.oss.utils.ConstantProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantProperties.END_POINT;
        String accessKeyId = ConstantProperties.KEY_ID;
        String accessKeySecret = ConstantProperties.KEY_SECRET;
        String bucketName = ConstantProperties.BUCKET_NAME;
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName; // 2021/10/2/ewqwwq01.jpg
            // 参数一：bucket名称 参数二：文件路径或文件名称 参数三：输入流
            ossClient.putObject(bucketName,fileName, inputStream);
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

            ossClient.shutdown();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
}
