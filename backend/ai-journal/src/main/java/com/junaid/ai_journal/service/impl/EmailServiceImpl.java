//package com.junaid.ai_journal.service.impl;
//
//import com.junaid.ai_journal.payload.dto.JournalReport;
//import com.junaid.ai_journal.service.EmailService;
//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//
//@Service
//@RequiredArgsConstructor
//public class EmailServiceImpl implements EmailService {
//
//    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
//    private final JavaMailSender javaMailSender;
//    private final SpringTemplateEngine templateEngine;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    @Override
//    public void sendEmail(String to, String userName ,JournalReport report) {
//        try{
//            logger.info("Attempting to send mail to: {}", to);
//
//            // Thymeleaf context
//            Context context = new Context();
//            context.setVariable("userName", userName);
//            context.setVariable("report", report);
//
//            //Process the template
//            String htmlContent = templateEngine.process("report-email.html", context);
//
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setTo(to);
//            helper.setSubject("Your AI Journal Weekly Report");
//            helper.setText(htmlContent, true);
//
//            javaMailSender.send(mimeMessage);
//
//            logger.info("Email sent successfully to {}", to);
//        }catch (Exception e){
//            logger.info("Failed to send Email to {}", to);
//            e.printStackTrace();
//        }
//    }
//}


package com.junaid.ai_journal.service.impl;

import com.junaid.ai_journal.payload.dto.JournalReport;
import com.junaid.ai_journal.service.EmailService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.sendgrid.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.sendgrid.api-key}")
    private String sendgridApiKey;

    @Value("${spring.mail.from}") // your verified sender
    private String fromEmail;


    @Override
    public void sendEmail(String to, String userName, JournalReport report) {
        try {
            logger.info("Attempting to send mail to: {}", to);

            // Thymeleaf context
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("report", report);

            // Process the template
            String htmlContent = templateEngine.process("report-email.html", context);

            // Build SendGrid email
            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, "Your AI Journal Weekly Report", toEmail, content);

            SendGrid sg = new SendGrid(sendgridApiKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            logger.info("SendGrid Response: {}", response.getStatusCode());

        } catch (IOException e) {
            logger.error("Failed to send Email to {}", to, e);
        }
    }
}
