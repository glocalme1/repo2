package com.glocalme.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.glocalme.security.OneWifiAuthenticationEntryPoint;
import com.glocalme.security.OneWifiAuthenticationFailureHandler;
import com.glocalme.security.OneWifiAuthenticationSuccessHandler;
import com.onewifi.constants.AppConstants;

 
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private OneWifiAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private OneWifiAuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private OneWifiAuthenticationSuccessHandler successHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
				.antMatchers("/retailer/**").hasRole(AppConstants.ROLE_RETAILER)
				//.antMatchers("/partner/**").hasRole(AppConstants.ROLE_RETAILER)
				//.antMatchers("/partials/buy.html").hasRole(AppConstants.ROLE_RETAILER)
				//.anyRequest().authenticated()
				.anyRequest().permitAll();
		
		http.csrf().disable();
		
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.formLogin().successHandler(successHandler);
		http.formLogin().failureHandler(authenticationFailureHandler);
	}

	@Override
    protected void configure(AuthenticationManagerBuilder registry) throws Exception {
		registry.authenticationProvider(customAuthenticationProvider);
		/*
        registry
            .inMemoryAuthentication()
                .withUser("admin")
                    .password("admin")
                    .roles("ADMIN");
        */
    }	

	@Bean(name="myAuthenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
       return super.authenticationManagerBean();
	}
	
	/*
	@Override
    public void configure(WebSecurity builder) throws Exception {
        builder
            .ignoring()
                .antMatchers("/public/");
    }
    */
}
