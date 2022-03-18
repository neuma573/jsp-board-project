package com.lguplus.medialog.project.base.menu;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lguplus.medialog.project.common.utils.SpringUtils;


@Controller
@RequestMapping("/view")
public class MenuController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired private MenuService svc;

	/**
	 * /view/home 요청하면 tiles 통해서 /WEB-INF/view/home/home.jsp 응답
	 * /view/func 요청하면 tiles 통해서 /WEB-INF/view/func/func.jsp 응답
	 */
	@GetMapping("/{depth1}")
    public String depth1IndexPage(@PathVariable String depth1) {
        return String.format("%s.usual", depth1);
    }
	
	/**
	 * /view/my/home 요청하면 tiles 통해서 /WEB-INF/view/my/home/home.jsp 응답
	 * /view/my/func 요청하면 tiles 통해서 /WEB-INF/view/my/func/func.jsp 응답
	 */
	@GetMapping("/{depth1}/{depth2}")
	public String depth2IndexPage(@PathVariable String depth1, @PathVariable String depth2) {
		return String.format("%s/%s.usual", depth1, depth2);
	}
	
	@GetMapping("/{depth1}/{depth2}/{depth3}")
	public String depth3IndexPage(@PathVariable String depth1, @PathVariable String depth2, @PathVariable String depth3) {
		return String.format("%s/%s/%s.usual", depth1, depth2, depth3);
	}
	
	@ModelAttribute("rootMenu")
	public Menu rootMenu(HttpServletRequest request, Model model) {
		String currUserRole = SpringUtils.getCurrentUserFirstRole();
		logger.debug("add rootMenu to model... ({})", currUserRole);
		
		Menu root = svc.getMenuTree(currUserRole);
		String reqUri = request.getRequestURI();
		model.addAttribute("activeMenuId", svc.getTopParentMenuId(root, reqUri));
		
		return root;
	}
	
	/**
	 * rootMenu ModelAttribute 적용 안하는 핸들러
	 */
	@Controller
	public static class LnbNoMenuController {
		@Autowired private MenuService svc;
		
		@GetMapping("/api/menu/clearcache")
		@ResponseBody
		public boolean clearLnbMenuCache() {
			svc.clearMenuTreeCache();
			return true;
		}

	}
	
}
