/**
 * 고객카드조회
 */
var customerCardPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
	function _init(callbackFunc, presetCutCardNo) {
		//console.log("......");
		cbFunc = callbackFunc;
		if (presetCutCardNo) {
			$("#Popup_CustomerCard_searchKey").val(presetCutCardNo);
		}
		_initEvent();
		_searchOnClick();
	};
	
	function _initEvent(){
		//
		$('#Popup_CustomerCard_btnSearch').unbind('click');
		$("#Popup_CustomerCard_btnSearch").click(function(){
			//
			_searchOnClick();
		});
	};
	
	function _searchOnClick(){
		//
		Popup_CustomerCard_pageInfoJs.init('Popup_CustomerCard_pageInfoJs', 'Popup_CustomerCard_pagingUl', 10, 10, customerCardPopupJs.search);
		
		_search();
	};
	
	function _search(){
		//
		$("#Popup_CustomerCard_tBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="3">' + _commonMsg.searching + '</td>';
		$("#Popup_CustomerCard_tBodyList").append(html);

		var paging = Popup_CustomerCard_pageInfoJs.getPaging();
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		
		//미발송카드만 조회
		param += "&stopYn=N";
		var searchKey = $("#Popup_CustomerCard_searchKey").val();
		param += "&cutCardNo=" + (searchKey ? searchKey : "");
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/customer/card" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCustomerCard(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayCustomerCard(jsonData){
		//console.log(jsonData);
		Popup_CustomerCard_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_CustomerCard_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="3">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_CustomerCard_tBodyList").append(html);
			return ;
		}
		
		var result = jsonData.result;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="customerCardPopupJs.selectSearch(\'' + result[i].cutCardNo + '\')">';
			html += '<td>' + (i+1) + '</td>';
			html += '<td>' + result[i].cutCardNo + '</td>';
			html += '<td>' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
			$("#Popup_CustomerCard_tBodyList").append(html);
		}
	};
	
	function _selectSearch(cutCardNo){
		$('#Popup_CustomerCard').modal('toggle');
		cbFunc({cutCardNo:cutCardNo});
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
