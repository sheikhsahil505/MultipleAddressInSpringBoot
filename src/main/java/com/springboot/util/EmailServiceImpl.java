package com.springboot.util;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public boolean sendEmail(String subject,String message,String to){
        boolean f = false;

        String from="sahils.inexture@gmail.com";

        String host="smtp.gmail.com";
        Properties properties=System.getProperties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return  new PasswordAuthentication("sahils.inexture@gmail.com","clamvneakexkmmne");
            }

        });
          session.setDebug(true);
        MimeMessage mimeMessage= new MimeMessage(session);

try{

      mimeMessage.setFrom(from);

      mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));

    mimeMessage.setSubject(subject);

    mimeMessage.setText(message);

    Transport.send(mimeMessage);
    f=true;
}catch (Exception e){
    e.printStackTrace();
}


        return f;
    }

}
