package com.lguplus.medialog.project.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lguplus.medialog.project.common.dto.RestResult;

@Controller
public class HomeController {

	@RequestMapping("/view/public/home/pub1")
    public String pub1(Model model) {
		model.addAttribute("pageType", "기본 tiles template 적용 페이지로 호출한 경우");
        return "home/pub";
    }
	
	@RequestMapping("/view/public/home/pub2")
	public String pub2(Model model) {
		model.addAttribute("pageType", "독립 페이지로 호출한 경우");
		return "home/pub.empty";
	}
	
	@RequestMapping("/api/public/home/pub")
	@ResponseBody
	public RestResult<?> pubApi() {
		return new RestResult<String>().setData("public API");
	}
	
	/**
	 * ROLE_ADMIN 권한자만 접근 가능하도록 SecurityConfig에서 설정되어 있음
	 */
	@RequestMapping("/view/home/adminOnly")
	public String adminOnly() {
		return "home/adminOnly";
	}
	
	/**
	 * ROLE_ADMIN 권한자만 접근 가능하도록 SecurityConfig에서 설정되어 있음
	 */
	@RequestMapping("/api/home/adminOnly")
	@ResponseBody
	public RestResult<?> adminOnlyApi() {
		return new RestResult<String>().setData("adminOnly API");
	}
	
}
