package jp.co.sample.emp_management.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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

	/**
	 * セキュリティ設定を無視する全体に関わる設定をすることができる. 具体的にはstaticファイルの中身に対してのセキュリティ無視設定。
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/fonts/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/toInsert", "/insert").permitAll() // ログインしてなくても見れるURLの設定
				.anyRequest().authenticated(); // それ以外のURLは全て認可（ログイン）が必要

		http.formLogin() // ログインに関する設定
				.loginPage("/") // ログイン画面を表示させるパス、ログインしてないときに認可が必要なページを見る時とかもここにとぶ
				.loginProcessingUrl("/login") // ログインする処理のパス、ログインボタンのとび先にここを設定する
				.failureUrl("/?error=true") // ログインが失敗したときにとぶパス（"/"のログインページ＋失敗メッセージを出すための?error=true
				.defaultSuccessUrl("/employee/showList", false) // 第一引数：ログイン成功時にとぶ先
																// 第二引数をfalseにしておくと、ログインしてなくて見れずログインページにとばされた続きに遷移する
				.usernameParameter("mailAddress") // ここに来たusernameとpasswordでSpringが自動でログイン処理をする
				.passwordParameter("password");

		http.logout() // ログアウトに関する設定
				.logoutRequestMatcher(new AntPathRequestMatcher("logout")) // このパスが来たらログアウトを自動でしてくれる
				.logoutSuccessUrl("/") // ログアウト後に遷移させるパスを設定⇒今回はログアウト後はログイン画面を表示
				.deleteCookies("JSESSIONID") // ログアウト後にCookieに保存されているセッションIDを破棄
				.invalidateHttpSession(true); // trueにしておけば、ログアウト後にセッションを無効にする
	}
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/css/**", "/js/**").permitAll().antMatchers("/employee/**")
//				.hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/")
//				.loginProcessingUrl("/login").usernameParameter("mailAddress").passwordParameter("password").permitAll()
//				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
//				.permitAll();
//	}

	/**
	 * 認証に関する設定. 認証ユーザを取得するServiceの設定やパスワードのハッシュ化は何を使っているかを設定する。
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(administratorService) // 認証するサービスクラスの設定
				.passwordEncoder(new BCryptPasswordEncoder()); // パスワードのハッシュ化の設定
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		passwordEncoder().encode("password");
//		auth.userDetailsService(administratorService);
//		System.out.println("WebSecurityConfigのconfig");
//
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
