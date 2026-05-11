/**
 * 시스템관리 - 메뉴관리
 */
var systemMenuJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   	    _searchOnClick();
   	};

   	function _initEvent(){
   		//
   		$("#btnSearch").click(function(){
   			//
   			_searchOnClick();
   		});
		$("#btnForm").click(function(){
			//
			_registerOnClick();
		});
		//검색조건 Enter키로 검색기능
		$("#menuName").keypress(function(){
		  if(event.keyCode == 13)
		     {
			  _searchOnClick();
		     }
		});
   	};

	function _registerOnClick(){
		//
		self.location= _ctx + "/system/menu";
	};

   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, systemMenuJs.search);

   		data.searchCond.menuName = "";

   		var menuName = $("#menuName").val();
        //메뉴명
        if (menuName && menuName !== "") {
        	data.searchCond.menuName = encodeURI(menuName);
        }

   		_search();
   	};

   	function _search(){
   		//
   		$("#tBodyList").empty();
   		var html = '<tr style="text-align:center;">';
   		html += '<td colspan="8">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		var paging = pageInfoJs.getPaging();
   		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

        param += "&menuName=" + data.searchCond.menuName;

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/system/menu" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayMenu(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	};

   	function _displayMenu(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		var html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="8">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		var result = jsonData.result;
   		var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr style="text-align:center;" onclick="systemMenuJs.searchDetail(' + result[i].menuId + ')">';
   			html += '<td>' + (noIndex + i) + '</td>';
   			html += '<td class="exCon">' + result[i].menuId + '</td>';
   			html += '<td class="exCon">' + (result[i].highMenuId ? result[i].highMenuId : '-') + '</td>';
   			html += '<td class="exCon">' + result[i].menuName + '</td>';
   			html += '<td class="exCon">' + (result[i].menuNameEn ? result[i].menuNameEn : '-') + '</td>';
   			html += '<td class="exCon">' + result[i].menuUrl + '</td>';
   			html += '<td class="exCon">' + result[i].ordPriority + '</td>';
   			html += '<td class="exCon">' + result[i].menuDesc + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	};

    function _searchDetail(menuId){
        //
        self.location= _ctx + "/system/menu/detail?menuId=" + menuId;
    };

   	return {
   		init : _init,
   		search : _search,
   		searchDetail : _searchDetail
   	};
}();
