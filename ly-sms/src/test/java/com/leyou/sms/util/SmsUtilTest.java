package com.leyou.sms.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendSms() {
        Map<String, String> msg = new HashMap<>();
        msg.put("phoneNumber", "18282015397");
        msg.put("code", "456789");
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
    }
}