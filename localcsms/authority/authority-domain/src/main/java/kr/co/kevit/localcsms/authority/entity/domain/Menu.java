/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.util.ArrayList;
import java.util.List;

/**
 * MNULIST 메뉴 도메인 - 한개의 메뉴는 여러개의 메뉴 그룹에 속 할 수 있기 때문에 메뉴와 메뉴 그룹의 관계를 느슨하게 갖음 따라서,
 * 그룹 타입을 속성으로 넣지 않았음.
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class Menu {

	public static final String DEFAULT_PARENT_MENU_ID = "000000";

	/**
	 * 메뉴아이디
	 * MENU_ID        CHAR(8)            NOT NULL,
	 */
	private String menuId;

	/**
	 * 상위메뉴아이디
	 * HI_MNU_ID        CHAR(8)
	 */
	private String highMenuId;

	/**
	 * 메뉴명
	 * MNU_NM        VARCHAR(500)
	 */
	private String menuName;

	/**
	 * 메뉴명(영문)
	 * MNU_NM_EN     VARCHAR(500)
	 */
	private String menuNameEn;

	/**
	 * 메뉴URL
	 * MNU_URL        VARCHAR(128)
	 */
	private String menuUrl;

	/**
	 * 우선순위
	 * ORD_PRI        INT
	 */
	private int ordPriority;

	/**
	 * 메뉴설명
	 * MNU_DES        VARCHAR(500)
	 */
	private String menuDesc;

	/**
	 * 연결유형
	 * LINK_TP        VARCHAR(10)
	 */
	private String linkType;

	/** 하위 메뉴 */
	private List<Menu> child;

	/** 등록정보 */
	private Writer writer;
	/**
	 * 메뉴 추가
	 * 
	 * @param menu
	 */
	public void addMenu(Menu menu) {
		if (this.child == null) {
			this.child = new ArrayList<>();
		}
		this.child.add(menu);
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getHighMenuId() {
		return highMenuId;
	}

	public void setHighMenuId(String highMenuId) {
		this.highMenuId = highMenuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuNameEn() {
		return menuNameEn;
	}

	public void setMenuNameEn(String menuNameEn) {
		this.menuNameEn = menuNameEn;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public int getOrdPriority() {
		return ordPriority;
	}

	public void setOrdPriority(int ordPriority) {
		this.ordPriority = ordPriority;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public List<Menu> getChild() {
		return child;
	}

	public void setChild(List<Menu> child) {
		this.child = child;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}
}
