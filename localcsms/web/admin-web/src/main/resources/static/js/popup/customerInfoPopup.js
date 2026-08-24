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

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/customer/vehicle/byCustomer/" + customerId,
			dataType : 'json' ,
			success : function(list) {
				// 세대당 차량 여러 대 등록 가능(TB_CUEV001) — 최근 등록 차량(list[0]) 표시
				var v = (list && list.length > 0) ? list[0] : null;
				$('#Popup_CustomerInfo_carName').html(v ? v.carName : '-');
				$('#Popup_CustomerInfo_carNumber').html(v ? v.carNo : '-');
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
