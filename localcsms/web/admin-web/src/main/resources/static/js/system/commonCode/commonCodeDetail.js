/**
 * 공통코드 관리
 */
var commonCodeDetailJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

	function _init() {
		//
		_initEvent();
		data.searchCond.code = (queryString.code)? queryString.code : "";
		$("#searchWord").val(data.searchCond.code);
		_searchOnClick();
	};

	function _initEvent(){
		//
		$("#btnList").click(function (){
			_goList();
		})
	};

	function _searchOnClick(){
		//
		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, commonCodeDetailJs.search);

		_search();
	};

	function _search(){
		//
		$("#tBodyList").empty();
		var html = '<tr style="text-align:center;">';
		html += '<td colspan="6">' + _commonMsg.searching + '</td>';
		$("#tBodyList").append(html);

		var paging = pageInfoJs.getPaging();
		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		param += "&highCode=" + data.searchCond.code;

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/system/commonCodeList" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_displayCommonCode(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
	};

	function _displayCommonCode(jsonData){
		//console.log(jsonData);
		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
		$("#tBodyList").empty();
		var html = '';
		if(jsonData.criteria.totalItemCount == 0){
			html = '<tr style="text-align:center;">';
			html += '<td colspan="6">' + _commonMsg.noData + '</td>';
			html += '</tr>';
			$("#tBodyList").append(html);
			return ;
		}
		var result = jsonData.result;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;">';
			html += '<td class="exCon">' + i + '</td>';
			html += '<td class="exCon">' + result[i].code + '</td>';
			html += '<td class="exCon">' + result[i].codeName + '</td>';
			html += '<td class="exCon">' + (result[i].codeNameEn ? result[i].codeNameEn : '') + '</td>';
			html += '<td class="exCon">' + result[i].ordPriority + '</td>';
			html += '<td class="exCon">' + (result[i].codeDescription ? result[i].codeDescription : '') + '</td>';
			html += '</tr>';
			$("#tBodyList").append(html);
		}
	};

	function _goList(){
		self.location= _ctx + "/system/commonCode/list";
	}

	return {
		init : _init,
		search : _search
	};
}();
