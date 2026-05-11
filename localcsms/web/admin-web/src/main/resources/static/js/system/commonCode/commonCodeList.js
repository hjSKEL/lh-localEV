/**
 * 공통코드 관리
 */
var commonCodeListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

	function _init() {
		//
		_initEvent();
		_searchChargerStatusClick();
	};

	function _initEvent(){
		//
		$("#btnSearch").click(function (){
			_searchChargerStatusClick();
		})
		//검색조건 Enter키로 검색기능
		$("#searchWord").keypress(function(){
		  if(event.keyCode == 13)
		     {
			  _searchChargerStatusClick();
		     }
		});
	};

	function _searchChargerStatusClick(){
		//
		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, commonCodeListJs.search);
		var type = $("#sType").val();
		var searchWord = $("#searchWord").val();
		searchWord = searchWord && searchWord.trim();
		$("#searchWord").val(searchWord);
		data.searchCond.subCode = '';
		data.searchCond.name    = '';
		if(type == 'code'){
			data.searchCond.subCode = searchWord;
		}else{
			data.searchCond.name = encodeURI(searchWord);
		}

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
		param += "&subCode=" + data.searchCond.subCode + "&codeName=" +data.searchCond.name;

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
		var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
		for(var i=0, length = result.length ; i < length ; ++i){
			html = '<tr style="text-align:center;">';
			html += '<td class="exCon">' + (noIndex + i) + '</td>';
			html += '<td class="exCon"><a href="javascript:void(0);" onclick="commonCodeListJs.searchDetail(\'' + result[i].code + '\')">' + result[i].code + '</a></td>';
			html += '<td class="exCon">' + result[i].codeName + '</td>';
			html += '<td class="exCon">' + (result[i].codeNameEn ? result[i].codeNameEn : '') + '</td>';
			html += '<td class="exCon">' + result[i].ordPriority + '</td>';
			html += '<td class="exCon">' + result[i].codeDescription + '</td>';
			html += '</tr>';
			$("#tBodyList").append(html);
		}
	};

	function _searchDetail(code){
		self.location= _ctx + "/system/commonCode/detail?code=" + code;
	};

	return {
		init : _init,
		search : _search,
		searchDetail : _searchDetail
	};
}();
