/**
 * 충전소 검색
 */
var chargePointSearchPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
    var data = {
    		searchKey : undefined,
    		cpId : undefined
    		
    };
    
	function _init(callbackFunc) {
		//
		cbFunc = callbackFunc;
		_initEvent();
		_searchOnClick();
//		zipCodeJs.searchZipCode(undefined, _disZipDoSiCode);
	};
	function _initEvent(){
		//
		$('#Popup_ChargePoint_zipCodeSi').unbind('change');
		$("#Popup_ChargePoint_zipCodeSi").change(function(){
			var value = $("#Popup_ChargePoint_zipCodeSi").val();
			if(value.length > 0){
//				zipCodeJs.searchZipCode(value.substring(0,2), _disZipSiGuCode);
			}
		});
		$('#Popup_ChargePoint_btnSearch').unbind('click');
		$("#Popup_ChargePoint_btnSearch").click(function(){
			_searchOnClick();
		});

		//검색조건 Enter키로 검색기능
		$("#Popup_ChargePoint_searchKey").unbind('keypress');
		$("#Popup_ChargePoint_searchKey").keypress(function(){
			if(event.keyCode == 13)
			{
				_searchOnClick();
			}
		});
		
	}
	
	function _searchOnClick(){
		//
		Popup_ChargePoint_pageInfoJs.init('Popup_ChargePoint_pageInfoJs', 'Popup_ChargePoint_pagingUl', 10, 10, chargePointSearchPopupJs.search);
		
		data.Popup_ChargePoint_searchKey = $("#Popup_ChargePoint_searchKey").val();
		_search();
	};
	
	function _search(){
		//
		$("#Popup_ChargePoint_tBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="5">' + _commonMsg.searching + '</td>';
		$("#Popup_ChargePoint_tBodyList").append(html);
		
		var paging = Popup_ChargePoint_pageInfoJs.getPaging();
		var zipCodeSi = '';
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		if(data.Popup_ChargePoint_searchKey){
			param += "&cpName=" + encodeURI(data.Popup_ChargePoint_searchKey);
		}
		if(data.cpId){
			param += "&cpId=" + data.cpId; 
		}
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/chargePoint" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displaySpotList(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};
	
	function _displaySpotList(jsonData){
		//
		Popup_ChargePoint_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_ChargePoint_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="5">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_ChargePoint_tBodyList").append(html);
			return ;
		}
		
		var result = jsonData.result;
		var noIndex = (Popup_ChargePoint_pageInfoJs.getPaging().pageNumber - 1) * Popup_ChargePoint_pageInfoJs.getPaging().pageItemSize + 1;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" onclick="chargePointSearchPopupJs.selectSearch(\'' + result[i].cpId + '\',\'' + result[i].cpName + '\',\'' + result[i].cpUseYn +  '\')">';
			html += '<td>' + (i + noIndex) + '</td>';
			html += '<td>' + result[i].cpId + '</td>'
			html += '<td>' + result[i].cpName + '</td>';
			html += '<td>' + result[i].cpLocation + '</td>'
			html += '<td>' + (result[i].cpUseYn == 'Y' ? _commonMsg.use : _commonMsg.notUse) + '</td>'
			html += '</tr>'
			$("#Popup_ChargePoint_tBodyList").append(html);
		}
	};
	
	function _disZipDoSiCode(result){
		$("#Popup_ChargePoint_zipCodeSi").empty();
		$("#Popup_ChargePoint_zipCodeSi").append('<option value="" selected >' + _commonMsg.cityDo + '</option>');
		for(var i = 0, length = result.length; i < length ; ++i){
			$("#Popup_ChargePoint_zipCodeSi").append('<option value="' + result[i].zipCodeId + '">' + result[i].zipCodeSi + '</option>');
		}
		$("#Popup_ChargePoint_zipCodeGu").empty();
		$("#Popup_ChargePoint_zipCodeGu").append('<option value="" selected >' + _commonMsg.cityGun + '</option>');
	};
	
	function _disZipSiGuCode(result){
		$("#Popup_ChargePoint_zipCodeGu").empty();
		$("#Popup_ChargePoint_zipCodeGu").append('<option value="" selected >' + _commonMsg.cityGun + '</option>');
		for(var i = 0, length = result.length; i < length ; ++i){
			if(!result[i].zipCodeGu) continue;
			
			$("#Popup_ChargePoint_zipCodeGu").append('<option value="' + result[i].zipCodeId + '">' + result[i].zipCodeGu + '</option>');
		}
	};
	
	function _selectSearch(cpId, cpName, useYn){
		$('#Popup_ChargePoint').modal('toggle');
		cbFunc({cpId:cpId, cpName:cpName, useYn:useYn});
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
