/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.domain;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ROLE_AUTHORITY
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 13.
 */
public class RoleAuthority implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2660988816618136005L;

	/**
	 * 역할구분
	 * ROLE_TP        VARCHAR(20)            NOT NULL,
	 */
	private UserRoleType roleType;

	/**
	 * 메뉴아이디
	 * MENU_ID        VARCHAR(8)            NOT NULL,
	 */
	private String menuId;
	
	private List<Menu> menu;

	public RoleAuthority() {
	}

	public RoleAuthority(UserRoleType roleType, String menuId) {
		this.roleType = roleType;
		this.menuId = menuId;
	}

	public UserRoleType getRoleType() {
		return roleType;
	}
	
	public void setRoleType(UserRoleType roleType) {
		this.roleType = roleType;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuId() {
		return menuId;
	}
	
}
