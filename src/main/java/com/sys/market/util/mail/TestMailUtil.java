package com.sys.market.util.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class TestMailUtil implements MailUtil{

    @Override
    public void sendMail(String to, String subject, String text) {
        // 테스트 환경에서는 메일을 보내지 않도록한다.
    }
}
