package com.lguplus.medialog.project.base.auth;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lguplus.medialog.project.base.user.User;

@Mapper
public interface AuthUserDao {

	public User getByUsername(String username);
	public String[] getRoles(String username);
	public int increaseFailCount(String username);
	public int setUserStatus(User user);
	public void setLoginInfo(String userId);
	public int addSmsAuthNum(SmsAuthNum num);
	public User getUser(String userId);
	public SmsAuthNum getSmsAuthNum(String userId);
	public int setSmsAuthNum(SmsAuthNum sms);
	public List<SmsAuthNum> getSmsAuthNumByNum(String issNum);
	public String getSmsAuthPhone(SmsAuthNum num);
	
	// extra login field 포함해서 로그인 하는 경우
	public User getByUsernameAndDomain(String userId, String userDomain);
	public String[] getRolesByUsernameAndDomain(String userId, String userDomain);
	
}
