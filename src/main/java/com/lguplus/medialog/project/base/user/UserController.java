package com.lguplus.medialog.project.base.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lguplus.medialog.project.base.auth.UserPassword;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.common.utils.SpringUtils;
import com.lguplus.medialog.project.config.consts.ResultCode;

@RestController
public class UserController {

	@Autowired
	private UserService service;
	@Autowired
	private PasswordEncoder pwdEncoder;

	/**
	 * 사용자 목록
	 */
	@GetMapping("/api/user/list")
	public RestResult<?> list(User user) {

		List<User> data = this.service.selectUserList(user);

		RestResult<List<User>> result = new RestResult<>();
		result.setData(data);
		result.setSuccess(true);

		return result;
    }

	/**
	 * 사용자 조회
	 */
	@GetMapping("/api/user/info")
	public RestResult<?> info(User user) {

		User data = this.service.getUser(user);

		RestResult<User> result = new RestResult<>();
		result.setData(data);
		result.setSuccess(true);

		return result;
    }

	/**
	 * 사용자 추가
	 */
	@PostMapping("/api/user/add")
	public RestResult<?> add(User user) {

		// 운영자 조회
		User exists = new User();
		exists.setUserId(user.getUserId());
		if (this.service.aleadyExistId(exists)) {
			return new RestResult<Void>(false).setCode(ResultCode.SS_DUP_USER_ID);
		}

		// 운영자 추가
		user.setUserPwd(pwdEncoder.encode(user.getUserPwd()));
		user.setRegUserId(SpringUtils.getCurrentUserName());

		boolean success = this.service.insertUser(user) > 0;

		return new RestResult<Void>(success);
    }

	/**
	 * 사용자 수정
	 */
	@PostMapping("/api/user/edit")
	public RestResult<?> edit(User user) {
		boolean success = this.service.updateUser(user) > 0;
		return new RestResult<Void>(success);
    }

	/**
	 * 사용자 삭제 (레코드 삭제는 안하고 삭제표시)
	 */
	@PostMapping("/api/user/delete")
	public RestResult<?> delete(@RequestBody List<User> users) {
		boolean success = this.service.deleteUser(users) > 0;
		return new RestResult<Void>(success);
	}

	/**
	 * 사용자 비밀번호 변경
	 * 관리자가 남의 비밀번호 수정
	 * 현재 비밀번호가 틀려도 무조건 변경
	 */
	@PostMapping("/api/user/chgpwd")
	public RestResult<?> chgpwd(UserPassword pass) {
		ResultCode rcode = service.changePassword(pass);
		return new RestResult<String>()
				.setSuccess(rcode == ResultCode.SUCCESS)
				.setCode(rcode);
	}
	
	/**
	 * 내 비밀번호 변경
	 */
	@PostMapping("/api/user/self/chgpwd")
	public RestResult<?> chgpwdSelf(UserPassword pass) {
		pass.setUserId(SpringUtils.getCurrentUserName());
		ResultCode rcode = service.changePassword(pass, true);
		return new RestResult<String>()
				.setSuccess(rcode == ResultCode.SUCCESS)
				.setCode(rcode);
	}
	
	/**
	 * 내 전화번호 조회
	 */
	@GetMapping("/api/user/self/phone")
	public RestResult<?> phoneSelf() {
		String userId = SpringUtils.getCurrentUserName();
		String phone = service.selectUser(userId).getPhone();
		return new RestResult<String>().setData(phone);
    }

	/**
	 * 내 정보 조회
	 */
	@GetMapping("/api/user/self/info")
	public RestResult<?> infoSelf() {
		User user = new User();
		user.setUserId(SpringUtils.getCurrentUserName());
		return info(user);
    }

	/**
	 * 내 정보 수정
	 */
	@PostMapping("/api/user/self/edit")
	public RestResult<?> editSelf(User user) {
		String userId = SpringUtils.getCurrentUserName();
		user.setUserId(userId);
		return edit(user);
    }

	@PostMapping("/api/public/user/newpwd")
	public RestResult<?> newpwd(UserPassword pass) {
		ResultCode rcode = service.resetPassword(pass);
		return new RestResult<String>()
				.setSuccess(rcode == ResultCode.SUCCESS)
				.setCode(rcode);
	}
}
