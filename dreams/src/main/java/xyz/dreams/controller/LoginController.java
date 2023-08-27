package xyz.dreams.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import xyz.dreams.dto.MemberDTO;
import xyz.dreams.exception.LoginAuthFailException;
import xyz.dreams.service.MemberService;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

	// 정적 필드 대신 inject 어노테이션 사용 가능
	private final MemberService memberService;

	// 1. 로그인 화면
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String login() {
		return "login/login";
	}

	// 2. 로그인 성공 시 세션 생성 실패시 LoginAuthInterceptor
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String login(@ModelAttribute MemberDTO member, HttpSession session) throws LoginAuthFailException {
		MemberDTO authMember = memberService.loginAuth(member);
		session.setAttribute("member", authMember);
		return "mainpage/index";
	}

	@RequestMapping(value = "/search_id", method = RequestMethod.GET)
	public String search_id(HttpServletRequest request, Model model, MemberDTO searchDTO) {
		return "login/search_result_id";
	}

	@RequestMapping(value = "/search_pw", method = RequestMethod.GET)
	public String search_pw(HttpServletRequest request, Model model, MemberDTO searchDTO) {
		return "login/search_result_pw";
	}

}