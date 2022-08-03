package cn.xu.eduorder.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "wx.pay")
public class ConstantProperties implements InitializingBean {


    @Value("${appid}")
    private String appId;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerKey;

    @Value("${notifyurl}")
    private String  notifyUrl;

    public static String APP_ID;
    public static String PARTNER;
    public static String PARTNER_KEY;
    public static String NOTIFY_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID = appId;
        PARTNER = partner;
        PARTNER_KEY = partnerKey;
        NOTIFY_URL = notifyUrl;
    }
}
