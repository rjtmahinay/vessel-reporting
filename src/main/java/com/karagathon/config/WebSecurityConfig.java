package com.karagathon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable();
		 http.headers().frameOptions().disable();
//         .antMatchers("*/upload/**").permitAll()
//         .antMatchers("**/").permitAll()
//         .anyRequest().authenticated();
//         .and().logout().logoutUrl("/logout").permitAll()
//         .and()
//         .formLogin().loginPage("/").permitAll();
	}
	
	
}
