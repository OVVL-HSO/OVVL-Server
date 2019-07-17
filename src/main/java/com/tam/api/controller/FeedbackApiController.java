package com.tam.api.controller;

import com.tam.api.FeedbackApi;
import com.tam.model.FeedbackResource;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@EnableAutoConfiguration
@RestController
public class FeedbackApiController implements FeedbackApi {

    private JavaMailSender emailSender;

    @Autowired
    public FeedbackApiController(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public ResponseEntity<Void> feedback(@ApiParam(value = "Feedback"  )  @Valid @RequestBody FeedbackResource body) {
        if (body.getContactMail() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SimpleMailMessage message = configureSupportMail(body);
        emailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private SimpleMailMessage configureSupportMail(FeedbackResource body) {
        SimpleMailMessage message = new SimpleMailMessage();
        String receiver = Optional.ofNullable(System.getenv("SUPPORT_MAIL_RECEIVER")).orElseThrow(
                () -> new NullPointerException("SUPPORT_MAIL_RECEIVER is not set in the environment"));
        message.setTo(receiver);
        message.setSubject(body.getType().toString());
        message.setText("\n" + body.getMessage() + "\n\n\n" + body.getContactMail() + "\n\n" + body.getDate());
        return message;
    }
}
