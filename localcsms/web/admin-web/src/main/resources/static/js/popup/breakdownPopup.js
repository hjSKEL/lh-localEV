/**
 * 고장 이력 조회
 */
let breakdownPopupJs = function(){
    "use strict";
    
	function _init() {
	}
	
	function _search(id){
		//
		$('#Popup_BreakdownInfo_cpName').html();
		$('#Popup_BreakdownInfo_csUniqId').html();
		$('#Popup_BreakdownInfo_regDate').html();
		$('#Popup_BreakdownInfo_inboundType').html();
		$('#Popup_BreakdownInfo_urgencyLevel').html();
		$('#Popup_BreakdownInfo_csCatCode').html();
		$('#Popup_BreakdownInfo_majorCategoryName').html();
		$('#Popup_BreakdownInfo_middleCategoryName').html();
		$('#Popup_BreakdownInfo_minorCategoryName').html();
		$('#Popup_BreakdownInfo_stationErrorCode').html();
		$('#Popup_BreakdownInfo_breakdownContent').html();
		$('#Popup_BreakdownInfo_makerYn').html();
		$('#Popup_BreakdownInfo_brkdownYn').html();
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/breakdown/receipt/detail/" + id,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayBreakDownInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	function _displayBreakDownInfo(jsonData){
		if(!jsonData){
			return ;
		}
		
		let urgencyLevels = parent.commonCodeJs.getCodesByParentCode("URG000");
        $("#Popup_BreakdownInfo_urgencyLevel").append('<option value="">' + _commonMsg.select + '</option>');
		let html = '';
		for(var i =0, size = urgencyLevels.length; i < size ; ++i){
			html = '<option value="' + urgencyLevels[i].code + '">';
			html += urgencyLevels[i].codeName;
			html += '</option>';
			$("#Popup_BreakdownInfo_urgencyLevel").append(html);	
		}
		
		let csCatCodes = parent.commonCodeJs.getCodesByParentCode("CHRA00");
        $("#Popup_BreakdownInfo_csCatCode").append('<option value="">' + _commonMsg.select + '</option>');
		html = '';
		for(var i =0, size = csCatCodes.length; i < size ; ++i){
			html = '<option value="' + csCatCodes[i].code + '">';
			html += csCatCodes[i].codeName;
			html += '</option>';
			$("#Popup_BreakdownInfo_csCatCode").append(html);	
		}
		$('#Popup_BreakdownInfo_cpName').html(jsonData.cpName);
		$('#Popup_BreakdownInfo_csUniqId').html(jsonData.csUniqId);
		let rgd = new Date(jsonData.writer.registrationDate);
		$("#Popup_BreakdownInfo_regDate").html(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(rgd), 'YYYY-MM-DD HH:MM:SS'));
		let inboundType = '';
		switch (jsonData.inboundType) {
			case 'APP':
				inboundType = _msg.app;
				break;
			case 'TEL':
				inboundType = _msg.tel;
				break;
			case 'WEB':
				inboundType = _msg.web;
				break;
		}
		$('#Popup_BreakdownInfo_inboundType').html(inboundType);
		$('#Popup_BreakdownInfo_urgencyLevel').val(jsonData.breakdownInfo.urgencyLevel);
		$('#Popup_BreakdownInfo_csCatCode').val(jsonData.breakdownInfo.csCatCode);
		$('#Popup_BreakdownInfo_majorCategoryName').val(jsonData.majorCategoryName);
		$('#Popup_BreakdownInfo_middleCategoryName').val(jsonData.middleCategoryName);
		$('#Popup_BreakdownInfo_minorCategoryName').val(jsonData.minorCategoryName);
		$('#Popup_BreakdownInfo_stationErrorCode').html(jsonData.breakdownInfo.stationErrorCode);
		$('#Popup_BreakdownInfo_breakdownContent').html(jsonData.breakdownInfo.breakdownContent);
		
		switch(jsonData.breakdownStatus){
			case 'BDST04':
			case 'BDST07':
				$("#Popup_BreakdownInfo_makerYn").html("Y");
			break;
			default :
				$("#Popup_BreakdownInfo_makerYn").html("N");
		}
		if(jsonData.urgencyInspectionInfo) {
			$("#Popup_BreakdownInfo_inspectionContent").html(jsonData.urgencyInspectionInfo.inspectionContent);
		} else {
			$("#Popup_BreakdownInfo_inspectionContent").html();
		}
		
	}

	return {
		init : _init,
		search : _search,
	};
}();
