/**
 * 상담 분류 검색
 */
var consultCategorySearchPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
    var data = {};
    
	function _init(callbackFunc) {
		//
		cbFunc = callbackFunc;
		_initEvent();
		_searchOnClick();
	};
	function _initEvent(){
		//
		$('#Popup_ConsultCategory_btnSearch').unbind('click');
		$("#Popup_ConsultCategory_btnSearch").click(function(){
			_searchOnClick();
		});
		
		// 검색조건 Enter키로 검색기능
		$("#Popup_ConsultCategory_searchKey").keypress(function() {
			if (event.keyCode == 13) {
				_searchOnClick();
			}
		});
	}
	
	function _searchOnClick(){
		//
		Popup_ConsultCategory_pageInfoJs.init('Popup_ConsultCategory_pageInfoJs', 'Popup_ConsultCategory_pagingUl', 10, 10, consultCategorySearchPopupJs.search);
		
		data.Popup_ConsultCategory_searchKey = $("#Popup_ConsultCategory_searchKey").val();
		_search();
	};
	
	function _search(){
		//
		$("#Popup_ConsultCategory_tBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="4">' + _commonMsg.searching + '</td>';
		$("#Popup_ConsultCategory_tBodyList").append(html);
		
		var paging = Popup_ConsultCategory_pageInfoJs.getPaging();
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		param += "&level=3";
		param += "&deleteYn=N"
		if(data.Popup_ConsultCategory_searchKey){
			param += "&deleteYn=N&&minorCategoryName=" + encodeURI(data.Popup_ConsultCategory_searchKey);
		}
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/consultCode/search" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayConsultCategoryList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayConsultCategoryList(jsonData){
		//
		Popup_ConsultCategory_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_ConsultCategory_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="4">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_ConsultCategory_tBodyList").append(html);
			return ;
		}
		
		var result = jsonData.result;
		var noIndex = (Popup_ConsultCategory_pageInfoJs.getPaging().pageNumber - 1) * Popup_ConsultCategory_pageInfoJs.getPaging().pageItemSize + 1;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="consultCategorySearchPopupJs.selectSearch(\'' + result[i].consultCodeId + '\',\'' + result[i].majorCategoryName + '\',\'' + result[i].middleCategoryName + '\',\'' + result[i].minorCategoryName + '\')">';
			html += '<td>' + result[i].consultCodeId + '</td>'
			html += '<td>' + result[i].majorCategoryName + '</td>';
			html += '<td>' + result[i].middleCategoryName + '</td>'
			html += '<td>' + result[i].minorCategoryName + '</td>'
			html += '</tr>'
			$("#Popup_ConsultCategory_tBodyList").append(html);
		}
	};
	
	function _selectSearch(consultCodeId, majorCategoryName, middleCategoryName, minorCategoryName){
		$('#Popup_ConsultCategory').modal('toggle');
		cbFunc({consultCodeId:consultCodeId, 
			majorCategoryName:majorCategoryName, 
			middleCategoryName:middleCategoryName, 
			minorCategoryName:minorCategoryName}
		);
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
