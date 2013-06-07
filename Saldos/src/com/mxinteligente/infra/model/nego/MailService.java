package com.mxinteligente.infra.model.nego;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService {
	
	@Autowired(required=true)
	private JavaMailSender mailSender;
	
    @Autowired
    private SimpleMailMessage alertMailMessage;
 
	 
	public void sendMail(String from, String to, String subject, String content) {
 
	   MimeMessage message = mailSender.createMimeMessage();
 
	   try{
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
 
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		//helper.setText(String.format(
		//	simpleMailMessage.getText(), dear, content));
		
		helper.setText(content,true);
 
		///FileSystemResource file = new FileSystemResource("C:\\log.txt");
		//helper.addAttachment(file.getFilename(), file);
 
	     }catch (MessagingException e) {
		throw new MailParseException(e);
	     }
	     mailSender.send(message);
         }
	
	
	 public void sendAlertMail(String alert) {
	        
	        SimpleMailMessage mailMessage = new SimpleMailMessage(alertMailMessage);
	        mailMessage.setText(alert);
	        mailSender.send(mailMessage);
	        
	    }


}
