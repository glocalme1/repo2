package com.glocalme.spring;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;


@Configuration
public class BeanConfig {
   
   @Bean
   public VelocityEngine velocityEngine() throws VelocityException, IOException{
   	VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
   	Properties props = new Properties();
   	props.put("resource.loader", "class");
   	props.put("class.resource.loader.class",  "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
   	factory.setVelocityProperties(props);
   	return factory.createVelocityEngine();
   }
   
   @Bean
	public JavaMailSenderImpl javaMailSenderImpl(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		//Set gmail email id
		mailSender.setUsername("glocalmetest@gmail.com");
		//Set gmail email password
		mailSender.setPassword("glocalmenyp");
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.debug", "true");
		return mailSender;
	}
   
   
}