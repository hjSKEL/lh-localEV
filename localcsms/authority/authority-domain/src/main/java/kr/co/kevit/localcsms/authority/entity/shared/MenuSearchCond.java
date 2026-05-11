/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

import java.util.List;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 18.
 */
public class MenuSearchCond extends PageCriteria {
	
	private List<String> parentMenuIds;
	
	private List<String> menuIds;

	private String menuName;

	public List<String> getParentMenuIds() {
		return parentMenuIds;
	}

	public void setParentMenuIds(List<String> parentMenuIds) {
		this.parentMenuIds = parentMenuIds;
	}

	public List<String> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
