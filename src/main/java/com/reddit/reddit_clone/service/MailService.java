package com.reddit.reddit_clone.service;

import com.reddit.reddit_clone.exception. SpringRedditException;
import com.reddit.reddit_clone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailContentBuilder mailContentBuilder;
    private static final Logger log = LoggerFactory.getLogger(MailService.class);


    @Async
    void sendMail(NotificationEmail notificationEmail)
    {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("shyam123@emaiol.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));

        };

        try
        {
            mailSender.send(messagePreparator);
            log.info("Activation email sent");

        }
        catch(MailException me)
        {
             throw new SpringRedditException("Exception occured when sending mail to "+notificationEmail.getRecipient());
        }
    }
}
