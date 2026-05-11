/**
 * 충전소 검색
 */
var chargerSearchPopupJs = function(){
    "use strict";
    
    var cbFunc = undefined;
    
    var data = {
    		searchKey : undefined,
    		cpId : undefined,
    		result : []
    		
    };
    
	function _init(callbackFunc) {
		//
		cbFunc = callbackFunc;
		_initEvent();
//		zipCodeJs.searchZipCode(undefined, _disZipDoSiCode);
		_searchOnClick();
	};
	function _initEvent(){
		//
		$('#Popup_ChargerSearch_zipCodeSi').unbind('change');
		$("#Popup_ChargerSearch_zipCodeSi").change(function(){
			var value = $("#Popup_ChargerSearch_zipCodeSi").val();
			if(value.length > 0){
//				zipCodeJs.searchZipCode(value.substring(0,2), _disZipSiGuCode);
			}
		});
		$('#Popup_ChargerSearch_btnSearch').unbind('click');
		$("#Popup_ChargerSearch_btnSearch").click(function(){
			_searchOnClick();
		});
		$('#Popup_ChargerSearch_btnSelectedCharger').unbind('click');
		$("#Popup_ChargerSearch_btnSelectedCharger").click(function(){
			_selectedChargerOnClick();
		});
	}
	
	function _searchOnClick(){
		//
		Popup_ChargerSearch_pageInfoJs.init('Popup_ChargerSearch_pageInfoJs', 'Popup_ChargerSearch_pagingUl', 10, 10, chargerSearchPopupJs.search);
		data.searchKey = $("#Popup_ChargerSearch_searchKey").val().trim();
		data.supplyCustRegYn = $("#Popup_ChargerSearch_sltSupplyCustRegYn").val();
		
		var zipCodeSi = $("#Popup_ChargerSearch_zipCodeSi").val();
		var zipCodeGu = $("#Popup_ChargerSearch_zipCodeGu").val();
		zipCodeSi = (!zipCodeSi) ? '' : zipCodeSi.substring(0,2);
		if(zipCodeGu && zipCodeGu.length  > 0){
			zipCodeSi = zipCodeGu.substring(0,4);
		}
		data.cpId = zipCodeSi;
		$("#Popup_ChargerSearch_searchKey").val(data.searchKey);
		_search();
	};
	
	function _search(){
		//
		var paging = Popup_ChargerSearch_pageInfoJs.getPaging();
		var zipCodeSi = '';
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		if(data.searchKey){
			param += "&cpName=" + encodeURI(data.searchKey);
		}
		if(data.cpId){
			param += "&cpId=" + data.cpId; 
		}
		if(data.supplyCustRegYn){
			param += "&supplyCustRegYn=" + data.supplyCustRegYn; 
		}
		
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/charger/finder" + param,
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
		Popup_ChargerSearch_pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);
		
		$("#Popup_ChargerSearch_tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="4">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#Popup_ChargerSearch_tBodyList").append(html);
			
			data.result = [];
			return ;
		}
		
		var result = jsonData.result;
		data.result = result;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;cursor:pointer;" data-index="' + i + '">';
			html += '<td><input type="checkbox" name="selected"/></td>';
			html += '<td>' + result[i].cpName + '</td>';
			html += '<td>' + result[i].cpId + '-' + result[i].csId + '</td>';
			html += '<td>' + result[i].roadName + '</td>';
			html += '</tr>'
			$("#Popup_ChargerSearch_tBodyList").append(html);
		}
	};
	
	function _disZipDoSiCode(result){
		//console.log(result);
		$("#Popup_ChargerSearch_zipCodeSi").empty();
		$("#Popup_ChargerSearch_zipCodeSi").append('<option value="" selected >' + _commonMsg.cityDo + '</option>');
		for(var i = 0, length = result.length; i < length ; ++i){
			$("#Popup_ChargerSearch_zipCodeSi").append('<option value="' + result[i].zipCodeId + '">' + result[i].zipCodeSi + '</option>');
		}
		$("#Popup_ChargerSearch_zipCodeGu").empty();
		$("#Popup_ChargerSearch_zipCodeGu").append('<option value="" selected >' + _commonMsg.cityGun + '</option>');
	};
	
	function _disZipSiGuCode(result){
		//console.log(result);
		$("#Popup_ChargerSearch_zipCodeGu").empty();
		$("#Popup_ChargerSearch_zipCodeGu").append('<option value="" selected >' + _commonMsg.cityGun + '</option>');
		for(var i = 0, length = result.length; i < length ; ++i){
			if(!result[i].zipCodeGu) continue;
			
			$("#Popup_ChargerSearch_zipCodeGu").append('<option value="' + result[i].zipCodeId + '">' + result[i].zipCodeGu + '</option>');
		}
	};
	
	function _selectedChargerOnClick(){
		var result = new Array();
		$("input[name=selected]:checked").each(function () {
	        var id = $(this).parent().parent().attr('data-index');
	        result.push(data.result[id]);
	    });
		_selectSearch(result);
	};
	
	function _selectSearch(result){
		$('#Popup_ChargerSearch').modal('toggle');
		cbFunc(result);
	};
	
	return {
		init : _init,
		search : _search,
		selectSearch : _selectSearch
	};
}();
