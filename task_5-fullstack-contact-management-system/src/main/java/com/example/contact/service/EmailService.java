package com.example.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendAutoReply(String to, String name) {
        if (mailSender == null) {
            System.out.println("[SIMULATED EMAIL] Auto-reply email would be sent to: " + to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("We received your enquiry!");
            message.setText("Dear " + name + ",\n\n"
                    + "Thank you for reaching out to us. We have received your message and our team will get back to you shortly.\n\n"
                    + "Best Regards,\nAdmin Team");
            
            mailSender.send(message);
            System.out.println("Auto-reply email sent to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email (Check SMTP config): " + e.getMessage());
        }
    }
}
