package com.leyou.sms.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtil {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_PREFIX_SMS = "sms:verify:phone:";

    /**
     * 发送短信
     *
     * @param phoneNumbers  收信人号码
     * @param signName      签名
     * @param templateCode  模板code
     * @param templateParam 发送的数据(JSON格式)
     */
    public CommonResponse sendSms(String phoneNumbers, String signName,
                                  String templateCode, String templateParam) {
        String key = REDIS_PREFIX_SMS + phoneNumbers;
        // 限流，同一手机号每次短信发送至少间隔1分钟
        if (redisTemplate.opsForValue().get(key) != null) {
            log.warn("[短信微服务] 同一手机号短信发送频率过高，手机号码：[{}]", phoneNumbers);
            return null;
        }

        try {
            DefaultProfile profile = DefaultProfile
                    .getProfile("default", smsProperties.getAccessKeyID(), smsProperties.getAccessKeySecret());
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("PhoneNumbers", phoneNumbers);
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", templateParam);

            CommonResponse response = client.getCommonResponse(request);
            log.info("[短信微服务] 短信发送。发送结果：{}", response.getData());

            // 记录本次发送手机号
            redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 1L, TimeUnit.MINUTES);

            return response;
        } catch (ClientException e) {
            log.error("[短信微服务] 发送短信失败， phone = [{}]", phoneNumbers, e);
        }
        return null;
    }
}
