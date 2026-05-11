/**
 * 충전기 정보 조회
 */
let chargerPopupJs = function(){
    "use strict";
    
    let data = {
    	param1 : undefined,
    	param2 : undefined,
    	param3 : undefined
    };
    
    let cpCsId = '';
    
	function _init() {
	}
	
	function _search(cpId, csId, ch){
		//
		$('#Popup_StationInfo_cpCsId').html();
		$('#Popup_StationInfo_cpName').html();
		$('#Popup_StationInfo_csUniqId').html();
		$('#Popup_StationInfo_csCableChn').html();
		$('#Popup_StationInfo_roadName').html();
		$('#Popup_StationInfo_placeName').html();
		$('#Popup_StationInfo_phoneNo').html();
		$('#Popup_StationInfo_chargerRegistrationDate').html();
		$('#Popup_StationInfo_csCatCode').html();
		$('#Popup_StationInfo_brkdownYn').html();
		$('#Popup_StationInfo_csStatCode').html();
		$('#Popup_StationInfo_useYn').html();
		$('#Popup_StationInfo_telecomNo').html();
		$('#Popup_StationInfo_reservationType').html();
		$('#Popup_StationInfo_fmwVersion').html();
		$('#Popup_StationInfo_serialNo').html();
		data.param1 = cpId;
		data.param2 = csId;
		data.param3 = ch;

		let url = _ctx + "/ws/charger/chargingStation/" + cpId + "/" + csId;
		if(parent.UserRole != 'ADMIN' && parent.UserRole != 'OPERATION') {
			url = _ctx + "/ws/chargingStation4ext/" + cpId + "/" + csId + "/info";
		}
		
		$.ajax({
			type: 'GET' ,
			url : url,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCharger(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	function _displayCharger(jsonData){
		if(!jsonData){
			return ;
		}
		let chargerStatusInfo = null;
		if(jsonData.chargerStatusInfo) {
			for(let i = 0; i < jsonData.chargerStatusInfo.length; i++) {
				if(data.param3 == jsonData.chargerStatusInfo[i].csCableChn) {
					chargerStatusInfo = jsonData.chargerStatusInfo[i];
				}
			}
		}
		$('#Popup_StationInfo_cpCsId').html(jsonData.cpId +  '-' + jsonData.csId);
		$('#Popup_StationInfo_cpName').html(jsonData.cpName);
		$('#Popup_StationInfo_csUniqId').html(jsonData.csUniqId);
		$('#Popup_StationInfo_csCableChn').html(jsonData.csChanelCount);
		let chargerRegistrationDate = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(jsonData.writer.registrationDate)), 'YYYY-MM-DD HH:MM');
		$('#Popup_StationInfo_chargerRegistrationDate').html(chargerRegistrationDate);
		if(jsonData.lastBootDate){
			let lastBootDate = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(jsonData.lastBootDate)), 'YYYY-MM-DD HH:MM');
			$('#Popup_StationInfo_lastBootDate').html(chargerRegistrationDate);
		}else{
			$('#Popup_StationInfo_lastBootDate').html("-");
		}
		$("#Popup_StationInfo_csCatCode").html(parent.commonCodeJs.getCodeNameBySubCode(jsonData.csCatCode));
		$('#Popup_StationInfo_brkdownYn').html('<button onclick="chargerPopupJs.registerBrkdown(\'' + jsonData.cpId + '\',\'' + jsonData.csId + '\')">' + _msg.receipt + '</button>');
		$('#Popup_StationInfo_useYn').html(jsonData.useYn == 'Y' ? _commonMsg.use : _commonMsg.notUse);
		$('#Popup_StationInfo_fmwVersion').html(jsonData.fwVer);
		$('#Popup_StationInfo_serialNo').html(jsonData.serialNumber);
		$('#Popup_StationInfo_csKindType').html(parent.commonCodeJs.getCodeNameBySubCode(jsonData.csKindType));
		

	}
	
	function _sendSmsControll(command){
		//
		data.param3 = command;
		
		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/charger/control",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					toastr.success(_msg.smsSent, _msg.smsSend);
				}else{
					toastr.error(_msg.smsFail, _msg.smsFailTitle);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				toastr.error(_commonMsg.commErrorDetail, _commonMsg.commError);
			}
		});
	}

	function _registerBrkdown(cpId, csId) {
		let param = "?cpId=" + cpId + "&csId=" + csId;
		parent.layerJs.fn_moveMenu('20000204', _msg.breakdownMgmt, '/evAdmin/charger/breakdown/detail' + param, 'THIS', true);
		//parent.layerJs.fn_moveMenu('20000503','고장접수 관리','/evAdmin/breakdown/receipt' + param,'THIS', true);
	}
	
	return {
		init : _init,
		search : _search,
		sendSmsControll : _sendSmsControll,
		registerBrkdown : _registerBrkdown
	};
}();
