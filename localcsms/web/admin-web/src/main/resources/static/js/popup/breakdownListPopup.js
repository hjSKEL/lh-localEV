/**
 * 충전소 검색
 */
var breakdownListPopupJs = function(){
    "use strict";
    
    function _init(){
    	
    }
    
	function _initEvent(){
		//
	}
	
	function _search(cpCsId){
		//
		var paging = Popup_BreakdownList_pageInfoJs.getPaging();
		var param = "?pageItemSize=5";
		let csIds = cpCsId.split("-");
		param += "&cpId=" + csIds[0] + "&csId=" + csIds[1];
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/breakdown/list" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayBreakdownList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayBreakdownList(jsonData){
		//
		Popup_BreakdownList_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_BreakdownList_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="8">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_BreakdownList_tBodyList").append(html);
			
			return ;
		}
        let result = jsonData.result;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + result[i].id + '</td>';
            html += '<td>' + result[i].cpId + '-' + result[i].csId + '</td>';
            if(result[i].receiptDate) {
				let receiptDt = formmatUtilsJs.dateFormmat(result[i].receiptDate + result[i].receiptTime, 'YYYY-MM-DD HH:MM:SS');
				html += '<td>' + receiptDt.substring(0,10) + '</td>';
            	html += '<td>' + receiptDt.substring(11,19) + '</td>';
			} else {
				html += '<td>-</td>';
            	html += '<td>-</td>';
			}
            if(result[i].repairDate) {
				let repairDt = formmatUtilsJs.dateFormmat(result[i].repairDate + result[i].repairTime, 'YYYY-MM-DD HH:MM:SS');
            	html += '<td>' + repairDt.substring(0,10) + '</td>';
            	html += '<td>' + repairDt.substring(11,19) + '</td>';
			} else {
				html += '<td>-</td>';
            	html += '<td>-</td>';
			}
            let breakdownStatus = parent.commonCodeJs.getCodeNameBySubCode(result[i].breakdownStatus);
            html += '<td>' + breakdownStatus + '</td>';
            html += '<td>' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '</tr>';
			$("#Popup_BreakdownList_tBodyList").append(html);
		}
	};
	
	return {
		init : _init,
		search : _search
	};
}();
