package prime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
	
	// 메인 페이지
	@GetMapping("/")
	public String home() throws Exception {
		
		return "index";
	}
	
	// 비회원 페이지
	@GetMapping("/guest")
	public String guest() throws Exception {
		
		return "guest/index";
	}
	
	// 관리자 페이지
	@GetMapping("/admin")
	public String admin() throws Exception {
		
		return "admin/index";
	}
	
	// 로그인 페이지
	@GetMapping("/auth/login")
	public String login(Model model, String logout, String error) throws Exception {
		if (error != null) {
			model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		if (logout != null) {
			model.addAttribute("logout", "로그아웃 되었습니다.");
		}
//		log.info("error > " + error);
		
		return "auth/login";
	}

}
