package com.lguplus.medialog.project.base.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lguplus.medialog.project.base.auth.AuthUserDao;
import com.lguplus.medialog.project.base.auth.SmsAuthNum;
import com.lguplus.medialog.project.base.auth.UserPassword;
import com.lguplus.medialog.project.base.role.Role;
import com.lguplus.medialog.project.base.role.UserRoleMap;
import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao dao;
	@Autowired
	private PasswordEncoder pwdEncoder;
	@Autowired
	private AuthUserDao authDao;

	public List<User> selectUserList(User user) {
		return this.dao.selectUserList(user);
	}

	public User getUser(User user) {
		List<User> userList = this.selectUserList(user);
		if (userList.size() > 0) {
			return userList.get(0);
		}
		return null;
	}

	public Integer insertUser(User user) {
		int success = this.dao.insertUser(user);
		
//		UserPassword pass = new UserPassword();
//		pass.setUserId(user.getUserId());
//		pass.setCurPwd(user.getUserPwd());
//		dao.insertUserPwdHist(pass);
		
		if (success > 0 && user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				UserRoleMap roleMap = new UserRoleMap(user.getUserId(), role.getRoleId());
				success = this.dao.insertUserRole(roleMap);
			}
		}
		return success;
	}

	public Integer updateUser(User user) {
		int success = this.dao.updateUser(user);
		if (success > 0 && user.getRoles() != null) {
			this.dao.deleteUserRole(user.getUserId());
			for (Role role : user.getRoles()) {
				UserRoleMap roleMap = new UserRoleMap(user.getUserId(), role.getRoleId());
				this.dao.insertUserRole(roleMap);
			}
		}
		return success;
	}

	public Integer deleteUser(List<User> users) {
		return this.dao.deleteUser(users);
	}
	
	public User selectUser(String userId) {
		User user = new User();
		user.setUserId(userId);
		return getUser(user);
	}

	public String selectUserPwd(String userId) {
		return dao.selectUserPwd(userId);
	}
	
	public List<String> selectLastPwds(String userId) {
		return dao.selectLastPwds(userId, Const.USED_PWD_CHECK_NUM - 1);
	}

	public Integer updateUserPwd(UserPassword pass) {
		dao.updateUserPwd(pass);
		return dao.insertUserPwdHist(pass);
	}
	
	public ResultCode changePassword(UserPassword pass) {
		return changePassword(pass, false);
	}

	public ResultCode changePassword(UserPassword pass, boolean self) {
		String userId = pass.getUserId();
		String dbPwd = selectUserPwd(userId);
		
		if (self && pass.getCurPwd() != null && !pwdEncoder.matches(pass.getCurPwd(), dbPwd)) {
			return ResultCode.CE_WRONG_PWD;
		}
		
		String newPwd = pass.getNewPwd();
		List<String> lastPwds = selectLastPwds(userId);
		lastPwds.add(dbPwd);
		boolean reuse = lastPwds.stream().anyMatch(s -> pwdEncoder.matches(newPwd, s));
		
		if (reuse) {
			return ResultCode.CE_USED_PWD;
		}
		
		UserPassword encPass = new UserPassword();
		encPass.setUserId(userId);
		encPass.setCurPwd(dbPwd);
		encPass.setNewPwd(pwdEncoder.encode(newPwd));
		updateUserPwd(encPass);

		return ResultCode.SUCCESS;

	}

	public boolean aleadyExistId(User user) {
		return this.dao.aleadyExistId(user) > 0;
	}
	
	public ResultCode resetPassword(UserPassword pass) {
		String userId = pass.getUserId();
		
		List<SmsAuthNum> nums = authDao.getSmsAuthNumByNum(pass.getIssNum());
		if (nums.size() == 0) {
			return ResultCode.CE_WRONG_AUTHNUM;
		}
		else if (nums.size() > 1) {
			SmsAuthNum anum = nums.stream()
					.filter(s -> s.getUserId().equals(userId))
					.findFirst().orElse(null);
			if (anum == null) {
				return ResultCode.CE_WRONG_AUTHNUM;
			}
		}
		
		return changePassword(pass);
	}
}
