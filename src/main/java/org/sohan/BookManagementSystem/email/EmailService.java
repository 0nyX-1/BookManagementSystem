package org.sohan.BookManagementSystem.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine springTemplateEngine;

    @Async
    public void sendEmail(String to,
                          String userName,
                          EmailTemplateName emailTemplateName,
                          String confirmationUrl,
                          String activationCode,
                          String subject) throws MessagingException {
        String templateName;
        if(emailTemplateName==null){
            templateName = "confirm-email";
        }else{
            templateName = emailTemplateName.getName();
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage
        ,MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());
        Map<String,Object> propertiesForTemplate = new HashMap<String,Object>();
        propertiesForTemplate .put("userName",userName);
        propertiesForTemplate.put("confirmationUrl",confirmationUrl);
        propertiesForTemplate.put("activationCode",activationCode);

        Context context = new Context();
        context.setVariables(propertiesForTemplate);

        mimeMessageHelper.setFrom("sohanlamichane@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = springTemplateEngine.process(templateName,context);

        mimeMessageHelper.setText(template,true);
        javaMailSender.send(mimeMessage);

    }
}
