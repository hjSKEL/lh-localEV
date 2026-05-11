/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.authority.entity.shared;

import kr.co.kevit.localcsms.authority.entity.domain.Menu;

import java.io.Serializable;

/**
 * 시스템관리 -  메뉴 DTO
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 4. 1.
 */
public class MenuDto extends Menu implements Serializable  {

	/**  */
	private static final long serialVersionUID = -2362308153594388872L;

	private String highMenuName;

	public String getHighMenuName() {
		return highMenuName;
	}

	public void setHighMenuName(String highMenuName) {
		this.highMenuName = highMenuName;
	}

}
