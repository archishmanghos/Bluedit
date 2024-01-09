package com.semicolonised.bluedit.service;

import com.semicolonised.bluedit.exception.BlueditException;
import com.semicolonised.bluedit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailTemplateBuilder mailTemplateBuilder;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator mimeMessagePreparator=mimeMessage -> {
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("archishman@email.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(notificationEmail.getBody());
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Activation Email Sent!");
        } catch (MailException e) {
            throw new BlueditException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
        }
    }
}
