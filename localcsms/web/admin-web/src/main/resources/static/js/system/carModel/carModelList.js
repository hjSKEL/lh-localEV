/**
 * 시스템관리 - 차량모델관리
 */
var carModelListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   		if(queryString.pageItemSize){
   			pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, carModelListJs.search);
			pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);
			data.searchCond.carName = queryString.carName;
			_search();

			$("#carName").val(queryString.carName);
   		} else {
   			_searchOnClick();
   		}
   	}

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
		$("#btnReset").click(function () {
            _searchResetClick();
        });
		//검색조건 Enter키로 검색기능
		$("#carName").keypress(function(){
		  if(event.keyCode == 13)
		     {
			  _searchOnClick();
		     }
		});
   	}

	function _registerOnClick(){
		//
		self.location= _ctx + "/system/carModel";
	}

	function _searchResetClick() {
        $("#carName").val("");
        _searchOnClick();
    }

   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, carModelListJs.search);

   		data.searchCond.carName = "";

   		var carName = $("#carName").val();
        //메뉴명
        if (carName && carName !== "") {
        	data.searchCond.carName = encodeURI(carName);
        }

   		_search();
   	}

   	function _search(){
   		//
   		$("#tBodyList").empty();
   		var html = '<tr style="text-align:center;">';
   		html += '<td colspan="7">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		var paging = pageInfoJs.getPaging();
   		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

        param += "&carName=" + data.searchCond.carName;

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/system/carModel" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayMenu(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   			}
   		});
   	}
   	var result;
   	function _displayMenu(jsonData){
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
   		result = jsonData.result;
   		var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr>';
   			html += '<td>' + (noIndex + i) + '</td>';
   			html += '<td>' + result[i].carModelId + '</td>';
   			html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].carModelId.substr(0,4)) + '</td>';
   			html += '<td><a href="#" onclick="carModelListJs.searchDetail(' + i + ')">' + (result[i].carName ? result[i].carName : '-') + '</a></td>';
   			html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].linkCode) + '</td>';
   			html += '<td>' + result[i].drivingDistance + '</td>';
   			html += '<td>' + result[i].kmKwh + '</td>';
   			html += '<td>' + result[i].batteryCapacity + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	}

    function _searchDetail(index){
        //
    	let paging = pageInfoJs.getPaging();
    	let param = "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize="+ paging.pageItemSize;
    	param += "&carName=" + data.searchCond.carName;

        self.location= _ctx + "/system/carModel/detail?carModelId=" + result[index].carModelId + param;
    }

   	return {
   		init : _init,
   		search : _search,
   		searchDetail : _searchDetail
   	};
}();
