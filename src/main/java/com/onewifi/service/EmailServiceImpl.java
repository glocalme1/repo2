package com.onewifi.service;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.onewifi.exception.GlocalMeException;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private JavaMailSenderImpl javamailSender;	
	
	@Override
	public void sendEmail(String toAddress, String subject, String mailTemplate, Map<String,Object> model) throws GlocalMeException {

		try {
			String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, mailTemplate, "UTF-8", model);
			MimeMessage mimeMessage = javamailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);			
			message.setTo(toAddress);
			message.setBcc("rameshs24@gmail.com");
			message.setFrom(new InternetAddress("glocalmetest@gmail.com") );
			message.setSubject(subject);
			mimeMessage.setContent(text, "text/html; charset=utf-8");
			javamailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new GlocalMeException();
		}		
		
	}

}
