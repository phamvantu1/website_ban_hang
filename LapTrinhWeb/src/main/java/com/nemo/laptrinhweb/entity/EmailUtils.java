package com.nemo.laptrinhweb.entity;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

//public class EmailUtils {
//
//    public static void send(Email email)
//            throws Exception {
//        Properties prop = new Properties();
//
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true");
//
////        prop.put("mail.transport.protocol", "smtps");
////        prop.put("mail.smtps.host", "smtp.gmail.com");
////        prop.put("mail.smtps.port", 465);
////        prop.put("mail.smtps.auth", "true");
////        prop.put("mail.smtps.quitwait", "false");
//
//        Session session = Session.getInstance(prop, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(email.getFrom(), email.getFromPassword());
//            }
//        });
//        try {
//            Message message = new MimeMessage(session);
//
//            message.setFrom(new InternetAddress(email.getFrom()));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
//            message.setSubject(email.getSubject());
//            message.setContent(email.getContent(), "text/html; charset=utf-8");
//
//            Transport.send(message);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//}
public class EmailUtils {

    public static void sendEmail(Email email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email.getFrom(), email.getFromPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
            message.setSubject(email.getSubject());
            message.setContent(email.getContent(), "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
