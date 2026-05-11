/**
 * 충전기 정보 조회
 */
var chargingStationPopupJs = function(){
    "use strict";
    
    var data = {
    	param1 : undefined,
    	param2 : undefined,
    	param3 : undefined
    };
    
	function _init() {
		//
		//제조사
		let makerTypes = parent.commonCodeJs.getCodesByParentCode("CHMK00");
		$("#Popup_ChargingStationInfo_makerTypeCode").empty();
		$("#Popup_ChargingStationInfo_makerTypeCode").append('<option value="">' + _commonMsg.select + '</option>');
        let html = '';
		for(let i =0, size = makerTypes.length; i < size ; ++i){
			html = '<option value="' + makerTypes[i].code + '">';
			html += makerTypes[i].codeName;
			html += '</option>';
			$("#Popup_ChargingStationInfo_makerTypeCode").append(html);	
		}
		
		let maintenanceStatusList = parent.commonCodeJs.getCodesByParentCode("CHMT00");
		$("#Popup_ChargingStationInfo_maintenanceStatus").empty();
        $("#Popup_ChargingStationInfo_maintenanceStatus").append('<option value="">' + _commonMsg.select + '</option>');
		html = '';
		for(let i =0, size = maintenanceStatusList.length; i < size ; ++i){
			html = '<option value="' + maintenanceStatusList[i].code + '">';
			html += maintenanceStatusList[i].codeName;
			html += '</option>';
			$("#Popup_ChargingStationInfo_maintenanceStatus").append(html);	
		}
		
		let csCatCodes = parent.commonCodeJs.getCodesByParentCode("CHRA00");
		$("#Popup_ChargingStationInfo_csCatCode").empty();
		$("#Popup_ChargingStationInfo_csCatCode").append('<option value="">' + _commonMsg.select + '</option>');
		html = '';
		for(let i =0, size = csCatCodes.length; i < size ; ++i){
			html = '<option value="' + csCatCodes[i].code + '">';
			html += csCatCodes[i].codeName;
			html += '</option>';
			$("#Popup_ChargingStationInfo_csCatCode").append(html);	
		}
		
		//
		let modelTypeCodes = parent.commonCodeJs.getCodesByParentCode("CHMD00");
		$("#Popup_ChargingStationInfo_modelTypeCode").empty();
        $("#Popup_ChargingStationInfo_modelTypeCode").append('<option value="">' + _commonMsg.select + '</option>');
		html = '';
		for(let i =0, size = modelTypeCodes.length; i < size ; ++i){
			html = '<option value="' + modelTypeCodes[i].code + '">';
			html += modelTypeCodes[i].codeName;
			html += '</option>';
			$("#Popup_ChargingStationInfo_modelTypeCode").append(html);	
		}
	}
	
	function _search(cpId, csId){
		//
		data.param1 = cpId;
		data.param2 = csId;
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargingStation/" + cpId + "/" + csId,
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
		$("#Popup_ChargingStationInfo_tbChargerId").html(jsonData.csId);
		$("#Popup_ChargingStationInfo_tbCpName").html(jsonData.cpName);
		$("#Popup_ChargingStationInfo_csUniqId").html(jsonData.csUniqId);
		$("#Popup_ChargingStationInfo_csCatCode").val(jsonData.csCatCode);
		$("#Popup_ChargingStationInfo_makerTypeCode").val(jsonData.makerType);
		$("#Popup_ChargingStationInfo_maintenanceStatus").val(jsonData.maintenanceStatus);
		$("#Popup_ChargingStationInfo_modelTypeCode").val(jsonData.modelTypeCode);
		if(jsonData.warrantyExpDate){
			$("#Popup_ChargingStationInfo_warrantyExpDate").html(dateUtilsJs.formatDate(new Date(jsonData.warrantyExpDate), 'YYYY-MM-DD'));
		}else{
			$("#Popup_ChargingStationInfo_warrantyExpDate").html("");
		}
		$("#Popup_ChargingStationInfo_insYearMon").html(jsonData.insYearMon);
		$("#Popup_ChargingStationInfo_electronicSupplyCapability").html(jsonData.electronicSupplyCapability);
		$("#Popup_ChargingStationInfo_canopyInstallYn").val(jsonData.canopyInstallYn);
		$("#Popup_ChargingStationInfo_insuranceJoinDate").html(jsonData.insuranceJoinDate);
		$("#Popup_ChargingStationInfo_insuranceExpiredDate").html(jsonData.insuranceExpiredDate);
		$("#Popup_ChargingStationInfo_multyChargingYn").val(jsonData.multyChargingYn);
		$("#Popup_ChargingStationInfo_insuranceStatus").html(jsonData.insuranceStatus);
		$("#Popup_ChargingStationInfo_transformerInstallYn").val(jsonData.transformerInstallYn);
		$("#Popup_ChargingStationInfo_transformerInOutType").val(jsonData.transformerInOutType);
		$("#Popup_ChargingStationInfo_useYn").html(jsonData.useYn);
		
		$("#Popup_ChargingStationInfo_operType").html(parent.kecoCodeJs.getCodeNameByParentNSubCode("BID", jsonData.bid));
		_getProduct(jsonData.prodType);
	}
	
	function _getProduct(prodType){
		//
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/product/prodType/" + prodType,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				$("#Popup_ChargingStationInfo_useFee").html(jsonData.name);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	return {
		init : _init,
		search : _search
	};
}();
