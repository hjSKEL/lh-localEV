package kr.co.kevit.localcsms.adminweb.util;

public class Validation {
	
	public static boolean isProductId(String productId) {
		// TODO ...
		if(productId == null || productId.trim().length() == 0) {
			return false;
		}
		if(productId.length() > 6) {
			return false;
		}
		return true;
	}
	
	public static boolean isBreakdownId(String breakdownId) {
		if(breakdownId == null || breakdownId.trim().length() == 0) {
			return false;
		}
		if(breakdownId.length() > 13) {
			return false;
		}
		return true;
	}
	
	public static boolean isCpId(String cpId) {
		if(cpId == null || cpId.trim().length() == 0) {
			return false;
		}
		if(cpId.length() > 6) {
			return false;
		}
		return true;
	}
	
	public static boolean isCsId(String csId) {
		if(csId == null || csId.trim().length() == 0) {
			return false;
		}
		if(csId.length() > 2) {
			return false;
		}
		return true;
	}
	
	public static boolean isCustomerId(String customerId) {
		if(customerId == null || customerId.trim().length() == 0) {
			return false;
		}
		if(customerId.length() > 9) {
			return false;
		}
		return true;
	}
	
	public static boolean isCompanyId(String companyId) {
		if(companyId == null || companyId.trim().length() == 0) {
			return false;
		}
		if(companyId.length() > 9) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmployeeId(String employeeId) {
		if(employeeId == null || employeeId.trim().length() == 0) {
			return false;
		}
		if(employeeId.length() > 9) {
			return false;
		}
		return true;
	}
	
	public static boolean isCarModelId(String carModelId) {
		if(carModelId == null || carModelId.trim().length() == 0) {
			return false;
		}
		if(carModelId.length() > 6) {
			return false;
		}
		return true;
	}
	
	public static boolean isMenuId(String menuId) {
		if(menuId == null || menuId.trim().length() == 0) {
			return false;
		}
		if(menuId.length() > 8) {
			return false;
		}
		return true;
	}
}
