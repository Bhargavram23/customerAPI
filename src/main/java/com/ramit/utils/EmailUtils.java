package com.ramit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	  @Autowired
	    private JavaMailSender mailSender;
	  
	  private String resetLink="http://localhost:4200/reset-password";

	public void sendEmail(String toEmail) throws MessagingException  {
		  MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(message, true);
          helper.setTo(toEmail);
          helper.setSubject("Password Reset Request");
          // http://localhost:4200/reset-password?email=rama@gmail.com
          helper.setText(buildEmailContent(resetLink+"?email="+toEmail), true); // true indicates that the content is HTML
          mailSender.send(message);
	}

	private String buildEmailContent(String link) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                "h2 { color: #333; }" +
                "p { font-size: 16px; }" +
                "a { color: #007BFF; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Password Reset Request</h2>" +
                "<p>We received a request to reset your password. Click the link below to reset your password:</p>" +
                "<p><a href='" + link + "'>Reset Password</a></p>" +
                "<p>If you did not request this, please ignore this email. Your password will remain unchanged.</p>" +
                "<p>Thank you!</p>" +
                "</body>" +
                "</html>";

	}
}
