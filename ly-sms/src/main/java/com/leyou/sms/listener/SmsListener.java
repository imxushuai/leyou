package com.leyou.sms.listener;

import com.leyou.common.util.JsonUtils;
import com.leyou.common.util.LeyouConstants;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.util.SmsAppConstants;
import com.leyou.sms.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private SmsProperties smsProperties;


    /**
     * 短信验证码
     *
     * @param msg 数据
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SmsAppConstants.QUEUE_VERIFY_CODE, durable = "true"),
            exchange = @Exchange(name = LeyouConstants.EXCHANGE_SMS),
            key = LeyouConstants.ROUTING_KEY_VERIFY_CODE_SMS
    ))
    public void verifyCode(Map<String, String> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phoneNumber = msg.remove("phoneNumber");
        if (StringUtils.isBlank(phoneNumber)) {
            return;
        }
        // 发送短信
        smsUtil.sendSms(phoneNumber, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate(), JsonUtils.serialize(msg));
    }

}
