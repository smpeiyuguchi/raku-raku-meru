package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 *アプリ全体の認証.
	 *
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/fonts/**");
	}
	
	/**
	 * 認証パスの設定.
	 *
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/to-register", "/register-user", "/tologin", "/login").permitAll()
			.antMatchers("/admin/**").hasRole("ADMIN") //管理者権限
			.anyRequest().authenticated(); //ユーザー権限
		
		http.formLogin() // ログインに関する設定
			.loginPage("/tologin") // ログインページのパス
			.loginProcessingUrl("/login") // ログインボタンのパス
			.failureUrl("/tologin?error=true") // ログイン失敗時のパス
			.defaultSuccessUrl("/", true) // ログイン成功時のパス
			.usernameParameter("email").passwordParameter("password"); // ログイン時のパラメータ（ログインページのnameと一致）

		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout**")) // ログアウトする時のパス
		.logoutSuccessUrl("/tologin") // ログイン画面に遷移
		.deleteCookies("JSESSIONID") // CookieのセッションIDを削除
		.invalidateHttpSession(true); // セッションを無効
	}
	
	/**
	 * 権限認証の設定.
	 *
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncorder() {
		return new BCryptPasswordEncoder();
	}
	

}
