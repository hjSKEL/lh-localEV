/**
 * 고객관리
 */
var customerListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };
    
   	function _init() {
   		_initEvent();
   		
		if (queryString.pageItemSize) {
			pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerListJs.search);
			pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);

			data.searchCond.custName = queryString.custName;
			data.searchCond.customerId = queryString.condCustomerId;
			data.searchCond.mblPhoneNo = queryString.mblPhoneNo;
			data.searchCond.cutCardNo = queryString.cutCardNo;

			$("#searchType").val(queryString.searchType);
			data.searchCond.order = queryString.order;
			$(".sortBtn").each(function(){
				let $btn = $(this);
				if (String(queryString.order) === String($btn.data("asc"))) {
					$btn.data("state", "asc").text("▲");
				}
			});
			switch(queryString.searchType){
				case "A":
					$("#sWord").val(queryString.condCustomerId);
					break;
				case "B":
					$("#sWord").val(queryString.custName);
					break;
				case "C":
					$("#sWord").val(queryString.mblPhoneNo);
					break;
				case "D":
					$("#sWord").val(queryString.cutCardNo);
					break;
				default :
			}

			_search();
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
		$("#sWord").keypress(function(event){
		  if(event.keyCode == 13)
		     {
			  _searchOnClick();
		     }
		});
		
		$("#cutGrdCode").change(function(){
			_searchOnClick();
		});
		$(".sortBtn").click(function(){
			let $btn = $(this);
			let toAsc = $btn.data("state") !== "asc";
			$btn.data("state", toAsc ? "asc" : "desc").text(toAsc ? "▲" : "▼");
			data.searchCond.order = toAsc ? $btn.data("asc") : $btn.data("desc");
			_searchOnClick();
		});

		$("#saveExcelcs").click(function () {
            _downloadExcel();
        });
   	}

   	function _searchResetClick() {
        $(".sortBtn").data("state", "desc").text("▼");
        data.searchCond.order = "01";
        $("#sWord").val("");
        $("#searchType").val("B");
        _searchOnClick();
    }
   	
   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerListJs.search);
   		
   		data.searchCond.custName = "";
   		data.searchCond.customerId = "";
   		data.searchCond.mblPhoneNo = "";
   		data.searchCond.cutCardNo = "";
   		data.searchCond.order = data.searchCond.order || "01";

   		let searchType = $("#searchType").val();
   		let searchKey = $("#sWord").val().replace(/-/g, '').trim();
   		switch(searchType){
	   		case "A":
	   			data.searchCond.customerId = searchKey;
	   			break;
	   		case "B":
	   			data.searchCond.custName = encodeURI(searchKey);
	   			break;
	   		case "C":
				if(searchKey && searchKey.length != 11) {
					toastr.warning(_msg.phoneDigit11, _msg.phoneNumber);
                	return;
				} else {
					data.searchCond.mblPhoneNo = searchKey;
				}
	   			break;
			case "D":
				if(searchKey && searchKey.length != 16) {
					toastr.warning(_msg.cardDigit16, _msg.cardNumber);
                	return;
				} else {
					data.searchCond.cutCardNo = searchKey;
				}
	   			break;
	   		default :
   		}
   		$("#sWord").val(searchKey);
   		
   		_search();
   	}

   	function _search(){
   		//
   		$("#tBodyList").empty();
   		let html = '<tr style="text-align:center;">';
   		html += '<td colspan="14">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		let paging = pageInfoJs.getPaging();
   		let param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

   		param += "&custName=" + data.searchCond.custName;
   		param += "&customerId=" + data.searchCond.customerId;
   		param += "&mblPhoneNo=" + data.searchCond.mblPhoneNo;
   		param += "&cutCardNo=" + data.searchCond.cutCardNo;
   		param += "&order=" + data.searchCond.order;

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/customer/search" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayCustomer(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	}

   	var result;
   	function _displayCustomer(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		var html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">'

   			html += '<td colspan="10">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		result = jsonData.result;
   		var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr>';
   			html += '<td class="footable-visible footable-first-column">' + (i + noIndex) + '</td>';
   			html += '<td class="footable-visible">' + (result[i].complexName ? result[i].complexName : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].dong ? result[i].dong : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].ho ? result[i].ho : '-') + '</td>';
   			html += '<td><a href="#" onclick="customerListJs.searchDetail(' + i + ')">' + (result[i].custName ? result[i].custName : '-') + '</a></td>';
  			html += '<td class="footable-visible">' + (result[i].mblPhoneNo ? formmatUtilsJs.phoneFormat(result[i].mblPhoneNo) : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].customerMgt.cutCardNo ? formmatUtilsJs.cardFormat(result[i].customerMgt.cutCardNo) : '-') + '</td>';
   			html += '<td class="footable-visible">' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].customerMgt.deleteYn === 'Y' ? 'Y' : '') + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	}

	function _registerOnClick(){
		//
		let paging = pageInfoJs.getPaging();
		let param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
		param += "&custName=" + data.searchCond.custName;
		param += "&condCustomerId=" + data.searchCond.customerId;
		param += "&mblPhoneNo=" + data.searchCond.mblPhoneNo;
   		param += "&cutCardNo=" + data.searchCond.cutCardNo;
		param += "&order=" + data.searchCond.order;
		param += "&searchType=" + $("#searchType").val();

		self.location= _ctx + "/customer" + param;
	}

    function _searchDetail(index){
        //
    	let param = "?customerId=" + result[index].customerId;
   		let paging = pageInfoJs.getPaging();
   		param += "&pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;
   		param += "&custName=" + data.searchCond.custName;
   		param += "&condCustomerId=" + data.searchCond.customerId;
   		param += "&mblPhoneNo=" + data.searchCond.mblPhoneNo;
   		param += "&cutCardNo=" + data.searchCond.cutCardNo;
   		param += "&order=" + data.searchCond.order;
		param += "&searchType=" + $("#searchType").val();
   		
        self.location= _ctx + "/customer/detail" + param;
    }

    function _downloadExcel() {
        //
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&custName=" + data.searchCond.custName;
   		param += "&condCustomerId=" + data.searchCond.customerId;
   		param += "&mblPhoneNo=" + data.searchCond.mblPhoneNo;
   		param += "&cutCardNo=" + data.searchCond.cutCardNo;
   		param += "&order=" + data.searchCond.order;
		$.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/download" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if(jsonData.status == 'SUCCESS'){
                	parent.layerJs.fn_download("/ws/download/DWCU001?tokenId=" + jsonData.result);
                }else{
                	alert(_commonMsg.excelFail + " : "+jsonData.result);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            	alert(_commonMsg.commError);
            }
        });
		
    }
    
   	return {
   		init : _init,
   		search : _search,
   		searchDetail : _searchDetail
   	};
}();
