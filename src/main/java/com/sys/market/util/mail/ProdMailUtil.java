package com.sys.market.util.mail;

import com.sys.market.advice.exception.CMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class ProdMailUtil implements MailUtil{
    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper;

            messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CMessagingException();
        }
    }
}
