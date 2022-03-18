package com.lguplus.medialog.project.func;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lguplus.medialog.project.base.auth.etc.AccessMapping;
import com.lguplus.medialog.project.common.dto.RestResult;

@RestController
@RequestMapping("/api/func")
// 이 컨트롤러에 있는 메서드에 대한 접근권한을 설정함
@AccessMapping(menu="/view/func/sub2") // sub2 메뉴에 대한 접근권한 있어야 호출 가능
public class FuncApiController {
	
	// 이 메서드는 클래스의 AccessMapping 설정이 적용됨 
	@GetMapping("/sub2/api1")
	public RestResult<?> api1() {
		return new RestResult<String>().setData("admin만 호출 가능한 API");
	}

	// 이 메서드는 별도 AccessMapping 설정함
	@AccessMapping(menu="/view/home") // home에 접근권한 있으면 호출 가능
	@GetMapping("/sub2/api2")
	public RestResult<?> api2() {
		return new RestResult<String>().setData("user도 호출 가능한 API");
	}

	// 이 메서드는 접근권한 검사 안함
	@AccessMapping(skip=true)
	@GetMapping("/sub2/api3")
	public RestResult<?> api3() {
		return new RestResult<String>().setData("권한 검사 안하는 API");
	}
	
}
