package com.onewifi.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.onewifi.exception.GlocalMeException;

@Service
public interface EmailService {

	public void sendEmail(String toAddress, String subject, String mailTemplate, Map<String,Object> model) throws GlocalMeException;
}
