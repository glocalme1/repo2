package com.glocalme.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.onewifi.beans.Userrole;
import com.onewifi.service.OneWifiService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired	
	private OneWifiService oneWifiService; 
 
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	System.out.println("Authenticate Method is called");
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        System.out.println("UserId:"+ name);
        System.out.println("Password: "+ password);
        
        Userrole userrole = null;
        try {
			userrole = oneWifiService.authenticateUser(name, password);
			if (userrole == null) {
				return null;
			}
			System.out.println(userrole.getRoleName());
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            grantedAuths.add(new SimpleGrantedAuthority(userrole.getRoleName()));
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            return auth;            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}