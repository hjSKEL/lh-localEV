/**
 * 충전소 정보 조회
 */
var chargingPointPopupJs = function(){
    "use strict";
    
    var data = {
    	param1 : undefined,
    	param2 : undefined,
    	param3 : undefined
    };
    
	function _init() {
		//
	};
	
	function _search(cpId){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargePoint/" + cpId,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCharger(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayCharger(jsonData){
		if(!jsonData){
			return ;
		}
		
		//충전소ID
		$("#Popup_ChargingPointInfo_tbCpId").html(jsonData.cpId);
		//충전소명
		$("#Popup_ChargingPointInfo_tbCpName").html(jsonData.cpName);
		//급속충전기대수
		$("#Popup_ChargingPointInfo_highCsCount").html(jsonData.highCsCount);
		//완속충전기대수
		$("#Popup_ChargingPointInfo_lowCsCount").html(jsonData.lowCsCount);
		//충전소 위치
		$("#Popup_ChargingPointInfo_cpLocation").html(jsonData.cpLocation);
		//전력량
		$("#Popup_ChargingPointInfo_electSupplyCapability").html(jsonData.electSupplyCapability);
		//사용 가능 여부
		$("#Popup_ChargingPointInfo_cpUseYn").html(jsonData.cpUseYn);
		//삭제여부
		$("#Popup_ChargingPointInfo_deleteYn").html(jsonData.deleteYn);
		//삭제일
		$("#Popup_ChargingPointInfo_deleteDate").html(jsonData.deleteDate);
		// 메모
		$("#Popup_ChargingPointInfo_memo").html(jsonData.memo);
	};
	
	return {
		init : _init,
		search : _search
	};
}();
