/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.dto;

import java.io.Serializable;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 1. 21.
 */
public class DbResultSetDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7732945580025829464L;
	
	private String resultStr01;
	
	private String resultStr02;
	
	private String resultStr03;
	
	private String resultStr04;
	
	private String resultStr05;
	
	private int resultInt01;
	
	private int resultInt02;
	
	private int resultInt03;
	
	private int resultInt04;
	
	private int resultInt05;

	public String getResultStr01() {
		return resultStr01;
	}

	public void setResultStr01(String resultStr01) {
		this.resultStr01 = resultStr01;
	}

	public String getResultStr02() {
		return resultStr02;
	}

	public void setResultStr02(String resultStr02) {
		this.resultStr02 = resultStr02;
	}

	public String getResultStr03() {
		return resultStr03;
	}

	public void setResultStr03(String resultStr03) {
		this.resultStr03 = resultStr03;
	}

	public String getResultStr04() {
		return resultStr04;
	}

	public void setResultStr04(String resultStr04) {
		this.resultStr04 = resultStr04;
	}

	public String getResultStr05() {
		return resultStr05;
	}

	public void setResultStr05(String resultStr05) {
		this.resultStr05 = resultStr05;
	}

	public int getResultInt01() {
		return resultInt01;
	}

	public void setResultInt01(int resultInt01) {
		this.resultInt01 = resultInt01;
	}

	public int getResultInt02() {
		return resultInt02;
	}

	public void setResultInt02(int resultInt02) {
		this.resultInt02 = resultInt02;
	}

	public int getResultInt03() {
		return resultInt03;
	}

	public void setResultInt03(int resultInt03) {
		this.resultInt03 = resultInt03;
	}

	public int getResultInt04() {
		return resultInt04;
	}

	public void setResultInt04(int resultInt04) {
		this.resultInt04 = resultInt04;
	}

	public int getResultInt05() {
		return resultInt05;
	}

	public void setResultInt05(int resultInt05) {
		this.resultInt05 = resultInt05;
	}
	
}
