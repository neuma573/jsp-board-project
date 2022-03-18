package com.lguplus.medialog.project.base.menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired private MenuDao dao;
	
	
	/**
	 * 권한에 해당하는 Menu 객체를 얻는다. 
	 */
	@Cacheable("ui:cache:lnb")
	public Menu getMenuTree(String role) {
		logger.debug("build new menu tree... ({})", role);
		long start = System.currentTimeMillis();
		Menu root = new Menu();
		List<Menu> menus = dao.getMenuByRole(role);
		
		Map<Integer, Menu> map = menus.stream().collect(
				Collectors.toMap(Menu::getMenuId, Function.identity()));
		
		for (Menu menu : menus) {
			Integer prntId = menu.getUpperMenuId();
			if (prntId == null) {
				root.addChild(menu);
			}
			else {
				map.get(prntId).addChild(menu);
			}
		}

		long finish = System.currentTimeMillis();
		double elapsedTime = (finish - start)/1000d;
		logger.debug("build new menu tree... ({}) elapsed={}", role, elapsedTime);
		return root;
	}

	/**
	 * 권한에 해당하는 key=URL인 Menu 맵을 얻는다.
	 */
	@Cacheable("ui:cache:menu")
	public Map<String, Menu> getMenuAuth(String role) {
		Map<String, Menu> map = dao.getMenuAuthByRole(role);
		return map;
	}
	
	/**
	 * 권한에 해당하는 Menu 객체를 캐시에서 삭제한다. 
	 */
	@CacheEvict(value = {"ui:cache:lnb", "ui:cache:menu"})
	public void removeMenuTree(String role) {
		logger.debug("clear menu tree cache. ({})", role);
	}

	/**
	 * 모든 권한에 해당하는 Menu 객체를 캐시에서 삭제한다. 
	 */
	@CacheEvict(value = {"ui:cache:lnb", "ui:cache:menu"}, allEntries = true)
	public void clearMenuTreeCache() {
		logger.debug("clear menu tree cache.");
	}
	
	public Menu findMenuByUrl(Menu menu, String reqUri) {
		for (Menu m : menu.getChildren()) {
			if (m.getUrl() != null && m.getUrl().equals(reqUri)) {
				return m;
			}
			Menu sub = findMenuByUrl(m, reqUri);
			if (sub != null)
				return sub;
		}
		return null;
	}
	
	public Menu findMenuById(Menu menu, Integer id) {
		for (Menu m : menu.getChildren()) {
			if (m.getMenuId().equals(id)) {
				return m;
			}
			Menu sub = findMenuById(m, id);
			if (sub != null)
				return sub;
		}
		return null;
	}
	
	public Menu getTopParentMenu(Menu root, Menu child) {
		Integer pId = child.getUpperMenuId();
		if (pId == null)
			return child;
		
		Menu prnt = findMenuById(root, pId);
		if (prnt != null)
			return getTopParentMenu(root, prnt);
		
		return null;
	}
	
	/**
	 * 현재 메뉴 (reqUri에 해당하는 메뉴)의 최상위 부모 메뉴의 ID를 반환한다.
	 */
	public Integer getTopParentMenuId(Menu root, String reqUri) {
		return getTopParentMenuId(root, reqUri, null);
	}
	public Integer getTopParentMenuId(Menu root, String reqUri, Integer depth1) {
		for (Menu m1 : root.getChildren()) {
			Integer topId = depth1 == null ? m1.getMenuId() : depth1; // 1 depth 메뉴의 ID
			if (m1.getUrl() != null && m1.getUrl().equals(reqUri)) {
				return topId;
			}
			Integer tId = getTopParentMenuId(m1, reqUri, topId);
			if (tId > 0)
				return tId;
		}
		return -1;
	}

}
