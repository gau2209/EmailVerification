package com.gauu.emailverification.event.lisener;

import com.gauu.emailverification.event.RegistrationCompleEvent;
import com.gauu.emailverification.security.token.VerificationTokenService;
import com.gauu.emailverification.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleEventLisener implements ApplicationListener<RegistrationCompleEvent> {
    private final VerificationTokenService verifiTokenService;
    private final JavaMailSender mailSender;
    private User user;



    @Override
    public void onApplicationEvent(RegistrationCompleEvent event) {
        user = event.getUser(); //get the user
        String vToken = UUID.randomUUID().toString();//generate a token for the user
        this.verifiTokenService.saveVerificationToken(user,vToken);// save the token
        String url = event.getConfirmationCode()+"/registration/verifyEmail?token="+ vToken;// build the verify url
        // send the mail to the user
        try{
            sendVerificationEmail(url);
        }
        catch (MessagingException | UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User theUser)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("anhtuanvo2209@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }


    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Verification Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p><b>You recently requested to reset your password,</b>"+"" +
                "Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

}
