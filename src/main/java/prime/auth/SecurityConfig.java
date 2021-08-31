package prime.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration								// 이 클래스를 스프링 설정 빈으로 등록
@EnableWebSecurity							// 이 클래스에 스프링 시큐리티 기능을 활성화해줌
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http : 시큐리티 설정 객체
		// url에 대한 허용 여부 설정
		// authorizeRequests() 						: 요청에 대한 인가 설정
		//		antMatchers(url) 					: 요청 url을 지정하는 메소드
		//			permitAll() 					: 해당 요청 url이 모두에게 접근허용 됨
		//				hasRole(권한) 				: 특정 권한(1개)을 가진 사용자만 접근을 허용함
		//				hasAnyRole(권한1, 권한2) 		: 특정 권한(복수개)을 가진 사용자만 접근을 허용함
		//				anthenticated()				: 인증된 사용자만 접근 허용
		//		anyRequest()						: 매칭한 url 경로 이외의 요청에 대한 설정 
		http.authorizeRequests().antMatchers("/").permitAll()
								.antMatchers("/css/**", "/js/**", "/upload/**").permitAll() // CSS JS 모두에게
								.antMatchers("/en/**", "/ko/**", "/global/**", "/images/**", "/font/**").permitAll() // static 페이지 모두에게
								.antMatchers("/board/list", "/board/list.html", "/board/read/**").permitAll() // board/list 일부 페이지 모두에게
								.antMatchers("/admin/**").hasRole("ADMIN") // 관리자
			.anyRequest().authenticated();
		
		// Security를 사용하면 default 로그인 화면을 만들어줌
		// 기본 로그인 페이지 url			 			: /login
		// 로그인 페이지 				 				: /auth/login
		http.formLogin()
			.loginPage("/auth/login")
			.loginProcessingUrl("/auth/login")
			.failureUrl("/auth/login?error")
			.usernameParameter("username")
			.passwordParameter("password")
			.permitAll(); 	

		// 기본 로그아웃 페이지 url						: /logout
		// 로그아웃 처리 요청 url						: /auth/logout
		http.logout()
			.logoutUrl("/auth/logout")
			.logoutSuccessUrl("/board/list")
//			.logoutSuccessUrl("/auth/login?logout")
			.permitAll();		
		
		// csrf 방지 기능 비활성화
		http.csrf().disable();
	}
	
	// 인증 설정
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// AuthenticationManagerBuilder			 	: 스프링 시큐리티의 인증에 대한 지원을 설정하는 객체
		//		inMemoryAuthentication()		 	: 인메모리 방식으로 인증 설정을 하는 객체
		//			withUser(아이디)					: 인증 등록에 사용될 아이디
		// 			password(비밀번호)					: 인증 등록에 사용될 비민번호
		//			roles(권한)						: 해당 사용자의 권한을 등록
		auth.inMemoryAuthentication()
				.withUser("admin").password(passwordEncoder().encode("1234")).roles("ADMIN");
	}
	
	// 암호화 방식 빈 등록
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
