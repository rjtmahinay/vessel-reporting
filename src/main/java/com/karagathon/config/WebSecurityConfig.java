package com.karagathon.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	final DataSource dataSource;

	public WebSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
//		http.headers().frameOptions().disable();
				.authorizeRequests().antMatchers("/resources/**").permitAll()
//         .antMatchers("*/upload/**").permitAll()
				.antMatchers("**/").permitAll().anyRequest().authenticated().and().logout().logoutUrl("/logout")
				.permitAll().and().formLogin().loginPage("/login").permitAll();

	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usersQuery().toString())
				.authoritiesByUsernameQuery(authorityQuery().toString());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private StringBuilder usersQuery() {
		return new StringBuilder().append("SELECT USER_ID, PASSWORD, ENABLED ").append("FROM ").append("USER ")
				.append("WHERE USER_ID = ?");
	}

	private StringBuilder authorityQuery() {
		return new StringBuilder().append("SELECT USER_ID, ROLE ").append("FROM ").append("USER ")
				.append("WHERE USER_ID = ?");
	}
}
