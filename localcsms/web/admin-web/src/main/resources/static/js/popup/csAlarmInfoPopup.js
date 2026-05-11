/**
 * 충전기 정보 조회
 */
let csAlarmInfoPopupJs = function(){
    'use strict';
    
	function _init() {
		//
	}
	
	function _search(makerType, errCode, csKindType){
		//
		$('#Popup_CsAlarmInfo_MakerType').html('');
		$('#Popup_CsAlarmInfoInfo_CsKindType').html('');
		$('#Popup_CsAlarmInfoInfo_ErrorCode').html('');
		$('#Popup_CsAlarmInfoInfo_ErrorType').html('');
		$('#Popup_CsAlarmInfoInfo_HMI').html('');
		$('#Popup_CsAlarmInfoInfo_ManuFactCode').html('');
		$('#Popup_CsAlarmInfoInfo_ErrorDesc').html('');
		$('#Popup_CsAlarmInfoInfo_RepairWay').html('');

		let param = "?makerTypeCode=" + makerType;
		param += '&errorCode=' + errCode;
		param += '&csKindType=' + csKindType;
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + '/ws/charger/csAlarm/list' + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCsAlarmInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	function _displayCsAlarmInfo(jsonData){
		if(jsonData.criteria.totalItemCount == 0){
			return;
		}
		let result = jsonData.result[0];
		$('#Popup_CsAlarmInfo_MakerType').html(parent.commonCodeJs.getCodeNameBySubCode(result.makerTypeCode));
		$('#Popup_CsAlarmInfoInfo_CsKindType').html(parent.commonCodeJs.getCodeNameBySubCode(result.csKindType));
		$('#Popup_CsAlarmInfoInfo_ErrorCode').html(result.errorCode);
		$('#Popup_CsAlarmInfoInfo_ErrorType').html(result.errorType);
		$('#Popup_CsAlarmInfoInfo_HMI').html(result.hmiErrMsg);
		$('#Popup_CsAlarmInfoInfo_ManuFactCode').html(result.manufacturerCode);
		$('#Popup_CsAlarmInfoInfo_ErrorDesc').html(result.errorDesc);
		$('#Popup_CsAlarmInfoInfo_RepairWay').html(result.repairWay);
		
	}
	
	return {
		init : _init,
		search : _search
	};
}();
