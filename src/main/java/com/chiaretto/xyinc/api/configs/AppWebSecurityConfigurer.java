package com.chiaretto.xyinc.api.configs;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class AppWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		    .authorizeRequests()
			.anyRequest().authenticated()
			.and().httpBasic()
			.and().exceptionHandling().accessDeniedHandler((request, response, e) -> 
		    {
		        response.setContentType("application/json;charset=UTF-8");
		        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		    })
			.and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
	      .withUser("user").password("{noop}user123").roles("USER")
	      .and()
	      .withUser("admin").password("{noop}admin123").roles("USER", "ADMIN");
	}
}
