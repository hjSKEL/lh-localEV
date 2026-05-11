/**
 * 고객 정보 조회
 */
let customerInfoPopupJs = function(){
    "use strict";
    
    let data = {
    };
    
	function _init() {
		//
	}
	
	function _search(customerId){
		//
		$('#Popup_CustomerInfo_customerName').html();
		$('#Popup_CustomerInfo_customerId').html();
		$('#Popup_CustomerInfo_cutCardNo').html();
		$('#Popup_CustomerInfo_custStatCode').html();
		$('#Popup_CustomerInfo_carName').html();
		$('#Popup_CustomerInfo_carNumber').html();
		$('#Popup_CustomerInfo_cpName').html();
		$('#Popup_CustomerInfo_csUniqId').html();
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/customer/detail/" + customerId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCustomerInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
		
		$.ajax({ 
			type: 'GET' ,
			url : _ctx + "/ws/recharging/customer/list?pageItemSize=1&customerId=" + customerId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCustomerRechargingInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	function _displayCustomerInfo(jsonData){
		if(!jsonData){
			return ;
		}
		$('#Popup_CustomerInfo_customerName').html(jsonData.custName);
		$('#Popup_CustomerInfo_customerId').html(jsonData.customerId);
		$('#Popup_CustomerInfo_cutCardNo').html(jsonData.customerMgt.cutCardNo);
		$('#Popup_CustomerInfo_stopYn').html(jsonData.customerMgt.stopYn);
		$('#Popup_CustomerInfo_carName').html(jsonData.carName);
		$('#Popup_CustomerInfo_carNumber').html(jsonData.carNumber);
		
	}
	
	function _displayCustomerRechargingInfo(jsonData){
		if(!jsonData){
			return ;
		}
		$('#Popup_CustomerInfo_cpName').html(jsonData.result[0].cpName);
		$('#Popup_CustomerInfo_csUniqId').html(jsonData.result[0].cpId + "-" + jsonData.result[0].csId);
	}
	
	return {
		init : _init,
		search : _search
	};
}();
