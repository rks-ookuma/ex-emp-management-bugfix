package jp.co.sample.emp_management.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.co.sample.emp_management.service.AdministratorService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AdministratorService administratorService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**").permitAll().antMatchers("/employee/**")
				.hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/")
				.loginProcessingUrl("/login").usernameParameter("mailAddress").passwordParameter("password").permitAll()
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
				.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		passwordEncoder().encode("password");
		auth.userDetailsService(administratorService);
		System.out.println("WebSecurityConfigのconfig");

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
