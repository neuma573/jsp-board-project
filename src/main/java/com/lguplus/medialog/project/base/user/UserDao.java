package com.lguplus.medialog.project.base.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lguplus.medialog.project.base.auth.UserPassword;
import com.lguplus.medialog.project.base.role.UserRoleMap;

@Mapper
public interface UserDao {

	public List<User> selectUserList(User user);

	public Integer insertUser(User user);

	public Integer insertUserRole(UserRoleMap roleMap);

	public Integer updateUser(User user);

	public Integer updateUserRole(User user);

	public Integer deleteUser(List<User> users);
	
	public String selectUserPwd(String userId);
	public List<String> selectLastPwds(String userId, Integer limitNum);
	public Integer updateUserPwd(UserPassword pass);
	public Integer insertUserPwdHist(UserPassword pass);

	public Integer aleadyExistId(User user);

	public void deleteUserRole(String userId);
}
