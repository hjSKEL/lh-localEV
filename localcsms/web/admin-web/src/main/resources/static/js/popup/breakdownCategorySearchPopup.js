/**
 * 고장분류 검색
 */
var breakdownCategorySearchPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
    var data = {level : 3};
    
	function _init(callbackFunc, level) {
		//
		cbFunc = callbackFunc;
		_initEvent();
		if(level){
			data.level = level;
		}
		if(data.level == 4){
			$("#Popup_BreakdownCategory_tTitle").html('<th>' + _msg.code + '</th><th>' + _msg.majorCat + '</th><th>' + _msg.middleCat + '</th><th>' + _msg.minorCat + '</th><th>' + _msg.detailCat + '</th>');
		}
		_searchOnClick();
	};
	function _initEvent(){
		//
		$('#Popup_BreakdownCategory_btnSearch').unbind('click');
		$("#Popup_BreakdownCategory_btnSearch").click(function(){
			_searchOnClick();
		});
		$("#Popup_BreakdownCategory_searchKey").keypress(function(){
			if(event.keyCode === 13)
			{
				_searchOnClick();
			}
		});
		
	}
	
	function _searchOnClick(){
		//
		Popup_BreakdownCategory_pageInfoJs.init('Popup_BreakdownCategory_pageInfoJs', 'Popup_BreakdownCategory_pagingUl', 10, 10, breakdownCategorySearchPopupJs.search);
		
		data.Popup_BreakdownCategory_searchType = $("#Popup_BreakdownCategory_searchType").val();
		data.Popup_BreakdownCategory_searchKey = $("#Popup_BreakdownCategory_searchKey").val().trim().replace(/ /g,"");
		_search();
	};
	
	function _search(){
		//
		$("#Popup_BreakdownCategory_tBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="' + (data.level + 1)  + '">' + _commonMsg.searching + '</td>';
		$("#Popup_BreakdownCategory_tBodyList").append(html);
		
		var paging = Popup_BreakdownCategory_pageInfoJs.getPaging();
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		param += "&level=" + data.level;
		param += "&deleteYn=N";
		switch(data.Popup_BreakdownCategory_searchType) {
			case 'majorCategoryName':
				param += "&majorCategoryName=" + encodeURI(data.Popup_BreakdownCategory_searchKey);
				break;
			case 'middleCategoryName':
				param += "&middleCategoryName=" + encodeURI(data.Popup_BreakdownCategory_searchKey);
				break;
			case 'minorCategoryName':
				param += "&minorCategoryName=" + encodeURI(data.Popup_BreakdownCategory_searchKey);
				break;
			case 'minorDetailCategoryName':
				param += "&minorDetailCategoryName=" + encodeURI(data.Popup_BreakdownCategory_searchKey);
				break;
		}
/*		if(data.Popup_BreakdownCategory_searchKey){
			param += "&deleteYn=N&&minorCategoryName=" + encodeURI(data.Popup_BreakdownCategory_searchKey);
		}*/
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/breakdownCode/search" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayBreakdownCategoryList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displayBreakdownCategoryList(jsonData){
		//
		Popup_BreakdownCategory_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		$("#Popup_BreakdownCategory_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="' + (data.level + 1)  + '">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_BreakdownCategory_tBodyList").append(html);
			return ;
		}
		
		var result = jsonData.result;
		var noIndex = (Popup_BreakdownCategory_pageInfoJs.getPaging().pageNumber - 1) * Popup_BreakdownCategory_pageInfoJs.getPaging().pageItemSize + 1;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="breakdownCategorySearchPopupJs.selectSearch(\'' + result[i].breakdownCodeId + '\',\'' + result[i].majorCategoryName + '\',\'' + result[i].middleCategoryName + '\',\'' + result[i].minorCategoryName + '\',\'' + result[i].minorDetailCategoryName + '\')">';
			html += '<td>' + result[i].breakdownCodeId + '</td>'
			html += '<td>' + result[i].majorCategoryName + '</td>';
			html += '<td>' + result[i].middleCategoryName + '</td>'
			html += '<td>' + result[i].minorCategoryName + '</td>'
			if(data.level == 4){
				html += '<td>' + result[i].minorDetailCategoryName + '</td>'
			}
			html += '</tr>'
			$("#Popup_BreakdownCategory_tBodyList").append(html);
		}
	};
	
	function _selectSearch(breakdownCodeId, majorCategoryName, middleCategoryName, minorCategoryName,minorDetailCategoryName){
		$('#Popup_BreakdownCategory').modal('toggle');
		cbFunc({breakdownCodeId:breakdownCodeId, 
			majorCategoryName:majorCategoryName, 
			middleCategoryName:middleCategoryName,
			minorCategoryName:minorCategoryName,
			minorDetailCategoryName:minorDetailCategoryName}
		);
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
