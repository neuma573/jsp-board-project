package com.lguplus.medialog.project.base.menu;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuDao {

	public List<Menu> allMenu();
	public List<Menu> getMenuByRole(String role);
	
	@MapKey("url")
	public Map<String, Menu> getMenuAuthByRole(String role);
	
}
