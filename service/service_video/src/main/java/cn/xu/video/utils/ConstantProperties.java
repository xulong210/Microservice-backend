package cn.xu.video.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "aliyun.video.file")
public class ConstantProperties implements InitializingBean {

    //读取配置文件
    @Value("${keyid}")
    private String keyId;
    @Value("${keysecret}")
    private String keySecret;

    public static String KEY_ID;
    public static String KEY_SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
    }
}
