/**
 * 고장 상세
 */
let breakdownReceiptPopupJs = function(){
    'use strict';
    
	function _init(id) {
		console.log(id);
		$.ajax({
			type: 'GET',
			url: _ctx + '/ws/breakdown/receipt/detail/' + id,
			dataType: 'json',
			success: function (jsonData, textStatus, jqXHR) {
				_displayBreakDownInfo(jsonData);
			},
			error: function (xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}



	function _displayBreakDownInfo(jsonData) {
		if (!jsonData) {
			return;
		}

		$('#Popup_BreakdownReceipt_cpName').html(null).html(jsonData.cpName);
		$('#Popup_BreakdownReceipt_csUniqId').html(null).html(jsonData.csUniqId);
		$('#Popup_BreakdownReceipt_urgencyLevel').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('URG000', jsonData.breakdownInfo.urgencyLevel));
		$('#Popup_BreakdownReceipt_csCatCode').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('CHRA00', jsonData.breakdownInfo.csCatCode));
		$('#Popup_BreakdownReceipt_category').html(null).html(jsonData.majorCategoryName + '/' + jsonData.middleCategoryName + '/'+ jsonData.minorCategoryName);
		$('#Popup_BreakdownReceipt_stationErrorCode').html(null).html(jsonData.breakdownInfo.stationErrorCode);
		$('#Popup_BreakdownReceipt_callType').html(null).html(jsonData.inboundType);
		$('#Popup_BreakdownReceipt_breakdownContent').html(null).html(jsonData.breakdownInfo.breakdownContent);
		let regDt = new Date(jsonData.writer.registrationDate);
		$('#Popup_BreakdownReceipt_breakdownRegDate').html(null).html(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(regDt), 'YYYY-MM-DD HH:MM'));
	}
	
	return {
		init : _init,
	};
}();
