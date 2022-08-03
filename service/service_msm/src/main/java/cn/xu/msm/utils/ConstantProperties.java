package cn.xu.msm.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "txyun.msm")
public class ConstantProperties implements InitializingBean {


    @Value("${signId}")
    private String signId;

    @Value("${singName}")
    private String signName;

    @Value("${templateId}")
    private String templateId;

    @Value("${templateContext}")
    private String  templateContext;

    @Value("${appId}")
    private String appId;

    @Value("${appKey}")
    private String appKey;

    @Value("${secretId}")
    private String secretId;

    @Value("${secretKey}")
    private String secretKey;

    public static String SIGN_ID;
    public static String SIGN_NAME;
    public static String TEMPLATE_ID;
    public static String TEMPLATE_CONTEXT;
    public static String APP_ID;
    public static String APP_KEY;
    public static String SECRET_ID;
    public static String SECRET_KEY;


    @Override
    public void afterPropertiesSet() throws Exception {
        SIGN_ID = signId;
        SIGN_NAME = signName;
        TEMPLATE_ID = templateId;
        TEMPLATE_CONTEXT = templateContext;
        APP_ID = appId;
        APP_KEY = appKey;
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
    }
}
