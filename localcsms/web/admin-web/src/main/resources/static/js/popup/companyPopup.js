/**
 * 회사검색 팝업
 */
var companyPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
	function _init(callbackFunc) {
		cbFunc = callbackFunc;
		_initEvent();
		_searchOnClick();
	};
	
	function _initEvent(){
		//
		$('#Popup_Company_btnSearch').unbind('click');
		$("#Popup_Company_btnSearch").click(function(){
			//
			_searchOnClick();
		});
		$("#Popup_Company_searchKey").keypress(function(){
			if(event.keyCode === 13)
			{
				_searchOnClick();
			}
		});
	};
	
	function _searchOnClick(){
		//
		Popup_Company_pageInfoJs.init('Popup_Company_pageInfoJs', 'Popup_Company_pagingUl', 10, 10, companyPopupJs.search);
		
		_search();
	};
	
	function _search(){
		//
		$("#Popup_Company_tBodyList").empty();
		let html = '<tr style="text-align:center;">';
		html += '<td colspan="5">' + _commonMsg.searching + '</td>';
		$("#Popup_Company_tBodyList").append(html);

		let paging = Popup_Company_pageInfoJs.getPaging();
		let param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		
		let searchKey = $("#Popup_Company_searchKey").val().trim();
		param += "&companyName=" + (searchKey ? searchKey : "");
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/organization/company/search" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCompany(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayCompany(jsonData){
		Popup_Company_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_Company_tBodyList").empty();
		let html = '';
		if(jsonData.criteria.totalItemCount === 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="5">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_Company_tBodyList").append(html);
			return ;
		}
		
		let result = jsonData.result;
		let noIndex = (Popup_Company_pageInfoJs.getPaging().pageNumber - 1) * Popup_Company_pageInfoJs.getPaging().pageItemSize + 1;
		for(let i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="companyPopupJs.selectSearch(\'' + result[i].companyId + '\',\'' + result[i].companyName + '\')">';
			html += '<td>' + (i + noIndex) + '</td>';
			html += '<td>' + result[i].companyName+ '</td>'
			html += '<td>' + result[i].bizRegNo + '</td>';
			html += '<td>' + result[i].ceoName + '</td>';
			html += '<td>' + result[i].roadName + '</td>';
			$("#Popup_Company_tBodyList").append(html);
		}
	};
	
	function _selectSearch(companyId, companyName){
		$('#Popup_Company').modal('toggle');
		cbFunc({companyId:companyId, companyName:companyName});
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
