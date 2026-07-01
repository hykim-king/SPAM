package com.pcwk.ehr;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	
	@RequestMapping("/hello.do")
	public String hello(Model model) {
		System.out.println("/hello.do");
		
		model.addAttribute("message", "스프링MVC");
		// /WEB-INF/views/hello.jsp
		return "hello";
	}
}

/**
 * 이 파일은 프로젝트가 정상적으로 세팅되었는지 확인하기 위한 '테스트용 진입점' 역할을 하고 있습니다.
 * 1. 코드 분석
		@Controller: 이 클래스가 스프링의 컨트롤러임을 나타냅니다. 스프링 컨테이너가 이 클래스를 인식해서 웹 요청을 처리할 수 있게 준비시켜 줍니다.
	
		@RequestMapping("/hello.do"): 사용자가 웹 브라우저에서 http://localhost:8080/ehr/hello.do와 같이 요청을 보내면, 이 hello() 메서드가 실행되도록 연결해 줍니다.

		model.addAttribute("message", "스프링MVC"): 컨트롤러에서 화면(JSP)으로 데이터를 전달하는 보따리(Model)에 "스프링MVC"라는 값을 담아 보내는 것입니다.

		return "hello": 스프링의 뷰 리졸버(View Resolver)라는 녀석이 이 문자열을 보고 /WEB-INF/views/hello.jsp 파일을 찾아 사용자에게 보여줍니다.

	2. 대장의 프로젝트는 지금 어떤 상태일까요?
		이 hello.do가 잘 작동한다면, "웹 서버(Tomcat)와 스프링 프레임워크 간의 통신이 아주 완벽하게 성공했다!"라는 뜻입니다.
 * */
 