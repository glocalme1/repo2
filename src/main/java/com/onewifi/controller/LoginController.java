package com.onewifi.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.glocalme.service.dto.CustomerDto;
import com.glocalme.service.dto.UserLogin;
import com.onewifi.beans.Customermaster;
import com.onewifi.constants.AppConstants;
import com.onewifi.service.OneWifiService;

@Controller
@RequestMapping("/login") 
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired	
	private OneWifiService oneWifiService;
	
	final static Logger logger = Logger.getLogger(LoginController.class);	
	
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public UserLogin login(@RequestParam("login") String username,
                   @RequestParam("passwd") String password, HttpServletRequest request) {
		logger.info("Login Requests from "+ username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

		try {
		  Authentication auth = authenticationManager.authenticate(token);
		  if (auth == null) {
			  throw new BadCredentialsException("Not a valid User");
		  } else {
			  Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();
			  String roleName = null;
			  if (authorities != null) {
				  for (SimpleGrantedAuthority roles: authorities) {
					  roleName = roles.getAuthority();
				  }
			  }
			  
			  String imie = "TESTING";
			  

			  if (AppConstants.ROLE_USER.equalsIgnoreCase(roleName)) {
		    	Customermaster customerMaster = null;
				try {
					customerMaster = oneWifiService.fetchCustomerDetails(username);
				} catch (Exception e) {
					e.printStackTrace();
				}
				imie = customerMaster.getImei();
			  }
				
			  SecurityContextHolder.getContext().setAuthentication(auth);
			  SecurityContext securityContext = SecurityContextHolder.getContext();
			  HttpSession session = request.getSession(true);
		      session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		      
			  UserLogin userLogin = new UserLogin(auth.isAuthenticated(), auth.getName(),roleName,imie);		
		      session.setAttribute("LOGGEDIN_USER", userLogin);
		      return userLogin;
		  }

		} catch (BadCredentialsException e) {
		  return new UserLogin(false, null,null);
		}
	}
	
	
	@RequestMapping(value = "/logout",  method = { RequestMethod.GET, RequestMethod.POST })	
	public @ResponseBody Map<String,Object> logout(HttpServletRequest request) {
		System.out.println("Logout - Start");
		Map<String,Object> result = new HashMap<String,Object>();
		String status = AppConstants.RESPONSE_SUCCESS;
		try {
			HttpSession session = request.getSession();
			if (session != null) {
				session.invalidate();
			}
			result.put(AppConstants.RESPONSE_STATUS, status);
		} catch(Exception e) {
			logger.error(e);
			e.printStackTrace();
			result.put(AppConstants.RESPONSE_STATUS, AppConstants.RESPONSE_FAILED);
		}
		System.out.println("Logout - End");		
		return result;
	}
	
	@RequestMapping(value = "/loginstatus", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public UserLogin getStatus(HttpServletRequest request) {
		HttpSession session = request.getSession();
		SecurityContext securityContext = null;
		if (session != null) {
			securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		}
		if (securityContext == null) {
			logger.info("User Not Logged in");
			return new UserLogin(false, null,null);
		} else {
			logger.info("User Logged in");
			logger.info(securityContext.getAuthentication().getName());
			UserLogin userLogin = (UserLogin)session.getAttribute("LOGGEDIN_USER");
			return userLogin;
		}
	}
	
}
