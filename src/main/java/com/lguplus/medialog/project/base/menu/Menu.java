package com.lguplus.medialog.project.base.menu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * mapping table : RCS_MENU
 * table comment : 메뉴 관리
 */
@Getter
@Setter
public class Menu {
	private Integer menuId; // [PK] 메뉴 ID
	private String menuCd; // 메뉴 CD
	private String menuNm; // 메뉴명
	private String useYn; // 사용여부
	private Integer lvl; // 단계
	private Integer ord; // 순서
	private String url; // URL
	private Integer upperMenuId; // 상위 메뉴 ID
	private LocalDateTime regDt; // 등록일시
	private String allowYn; // (대상 role로) 접근허용여부
	private List<Menu> children = new ArrayList<>();
	
	public void addChild(Menu menu) {
		children.add(menu);
	}
	public boolean isAllowed() {
		return "Y".equals(allowYn);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Menu [menuId=");
		builder.append(menuId);
		builder.append(", menuCd=");
		builder.append(menuCd);
		builder.append(", menuNm=");
		builder.append(menuNm);
		builder.append(", lvl=");
		builder.append(lvl);
		builder.append(", ord=");
		builder.append(ord);
		builder.append(", url=");
		builder.append(url);
		builder.append(", upperMenuId=");
		builder.append(upperMenuId);
		builder.append(", allowYn=");
		builder.append(allowYn);
		builder.append(", childrenSize=");
		builder.append(children.size());
		builder.append("]");
		return builder.toString();
	}


}
