/**
 *
 */
var productListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

	function _init() {
		//
		_initEvent();
		_searchOnClick();
	};

	function _initEvent(){
		//
		$("#btnSearch").click(function(){
			_searchOnClick();
		});
		$("#btnForm").click(function(){
			_searchDetail('');
		});
		$("#btnReset").click(function () {
            _searchResetClick();
        });
		$("#productName").keypress(function (event) {
            if (event.keyCode == 13) {
            	_searchOnClick();
            }
        });

		//정렬(요금제ID/요금제명) 컬럼 헤더 클릭 - 클릭할 때마다 오름차순/내림차순 토글
		$(".sortBtn").click(function () {
			var $btn = $(this);
			var toAsc = $btn.data("state") !== "asc";
			$btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
			data.searchCond.sortOrder = toAsc ? $btn.data("asc") : $btn.data("desc");
			_search();
		});
	};

	function _searchResetClick() {
        $("#productName").val("");
        $(".sortBtn").data("state", "desc").text("▼");
        data.searchCond.sortOrder = "";
        _searchOnClick();
    }

	function _searchOnClick(){
		//
		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, productListJs.search);
		data.searchCond.productName = $("#productName").val().trim();
		$("#productName").val(data.searchCond.productName);
		_search();
	};

	function _search(){
		//
   		$("#tBodyList").empty();
   		var html = '<tr style="text-align:center;">';
   		html += '<td colspan="7">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		var paging = pageInfoJs.getPaging();
   		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
   		param += "&name=" + data.searchCond.productName;
   		param += "&sortOrder=" + (data.searchCond.sortOrder || "");

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/product/search" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayProductList(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
	};

	function _displayProductList(jsonData){
		//
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		var html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="7">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		var result = jsonData.result;
   		let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr>';
   			html += '<td>' + (i + noIndex) + '</td>';
   			html += '<td><a href="javascript:void(0);" onclick="productListJs.searchDetail(\'' + result[i].id + '\')">' + result[i].id + '</a></td>';
   			html += '<td>' + result[i].name + '</td>';
   			html += '<td>' + result[i].writer.regUserName + "(" + result[i].writer.regUserId + ')</td>';
   			var cdt = new Date(result[i].writer.registrationDate);
   			html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM') + '</td>';
   			html += '<td>' + result[i].writer.updUserName + "(" + result[i].writer.updUserId + ')</td>';
   			cdt = new Date(result[i].writer.updateDate);
   			html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM') + '</td>';

   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
	};

	function _searchDetail(id){
		//
		self.location= _ctx + "/product/detail?productId=" + id;
	};

	return {
		init : _init,
		search : _search,
		searchDetail : _searchDetail
	};
}();
