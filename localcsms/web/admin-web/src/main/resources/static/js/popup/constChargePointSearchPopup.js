/**
 * 시공 충전소 검색
 */
var chargePointSearchPopupJs = function(){
    "use strict";
    
    let cbFunc = undefined;
    let data = {
    		searchKey : undefined,
    		cpId : undefined
    };
    
	function _init(callbackFunc) {
		//
		cbFunc = callbackFunc;
		_initEvent();
		_searchOnClick();
	}
	
	function _initEvent(){
		//
		$('#Popup_ChargePoint_btnSearch').unbind('click');
		$("#Popup_ChargePoint_btnSearch").click(function(){
			_searchOnClick();
		});
	}
	
	function _searchOnClick(){
		//
		Popup_ChargePoint_pageInfoJs.init('Popup_ChargePoint_pageInfoJs', 'Popup_ChargePoint_pagingUl', 10, 10, chargePointSearchPopupJs.search);

		data.Popup_ChargePoint_searchKey = $("#Popup_ChargePoint_searchKey").val();
		_search();
	}
	
	function _search(){
		//
		$("#Popup_ChargePoint_tBodyList").empty();
		let html = '<tr style="text-align:center;">';
		html += '<td colspan="4">' + _commonMsg.searching + '</td>';
		$("#Popup_ChargePoint_tBodyList").append(html);
		
		let paging = Popup_ChargePoint_pageInfoJs.getPaging();
		let param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		if(data.Popup_ChargePoint_searchKey){
			param += "&cpName=" + encodeURI(data.Popup_ChargePoint_searchKey);
		}

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/construction/chargePoint/list" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displaySpotList(jsonData.result);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	}
	
	function _displaySpotList(jsonData){
		//
		Popup_ChargePoint_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_ChargePoint_tBodyList").empty();
		let html = '';
		if(jsonData.criteria.totalItemCount === 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="4">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_ChargePoint_tBodyList").append(html);
			return ;
		}
		
		let result = jsonData.result;
		let noIndex = (Popup_ChargePoint_pageInfoJs.getPaging().pageNumber - 1) * Popup_ChargePoint_pageInfoJs.getPaging().pageItemSize + 1;
		for(let i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="chargePointSearchPopupJs.selectSearch(\'' + result[i].cpId + '\',\'' + result[i].cpName + '\')">';
			html += '<td>' + (i + noIndex) + '</td>';
			html += '<td>' + result[i].cpId + '</td>'
			html += '<td>' + result[i].cpName + '</td>';
			let tempAddr = (result[i].roadName) ? result[i].roadName : "";
			tempAddr += (result[i].roadDetName) ? " " + result[i].roadDetName : "";
			html += '<td>' + tempAddr + '</td>'
			html += '</tr>'
			$("#Popup_ChargePoint_tBodyList").append(html);
		}
	}
	

	function _selectSearch(cpId, cpName){
		$('#Popup_ChargePoint').modal('toggle');
		cbFunc({cpId:cpId, cpName:cpName});
	}
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
