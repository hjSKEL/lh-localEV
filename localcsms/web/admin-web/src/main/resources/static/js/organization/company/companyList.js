/**
 * 조직관리 - 법인관리
 */
var companyListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   		if (queryString.pageItemSize) {
			pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, companyListJs.search);
			pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);

			data.searchCond.bizRegNo = queryString.bizRegNo;
	   		data.searchCond.companyName = queryString.companyName;

			$("#searchType").val(queryString.searchType);

			switch(queryString.searchType){
				case "COMPANY_NAME":
					$("#sWord").val(queryString.companyName);
					break;
				case "BIZ_REG_NO":
					$("#sWord").val(queryString.bizRegNo);
					break;
				default :
			}
			_search();
		} else {
			_searchOnClick();
		}
   	};

   	function _initEvent(){
   		//

//   		if (parent.roleMainRole !== 'ADMIN' && parent.roleMainRole !== 'OPERATION') {
   		if (parent.UserRole === 'COMPANY_USER') {
   			self.location= _ctx + "/organization/company/detail?companyId=" + parent.roleCompanyId;
   		}

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
		$("#sWord").keypress(function(){
		  if(event.keyCode == 13)
		     {
			  _searchOnClick();
		     }
		});

   	};

   	function _searchResetClick() {
        $("#sWord").val("");
        _searchOnClick();
    }


	function _registerOnClick(){
		//
		self.location= _ctx + "/organization/company";
	};

   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, companyListJs.search);

   		data.searchCond.bizRegNo = "";
   		data.searchCond.companyName = "";

   		var searchType = $("#searchType").val();
   		switch(searchType){
	   		case "BIZ_REG_NO":
	   			data.searchCond.bizRegNo = $("#sWord").val().replace(/-/g, '').trim();
	   			break;
	   		case "COMPANY_NAME":
	   			data.searchCond.companyName = encodeURI($("#sWord").val());
	   			break;
	   		default :
   		}
   		_search();
   	};


   	function _search(){
   		//
   		$("#tBodyList").empty();
   		var html = '<tr style="text-align:center;">';
   		html += '<td colspan="9">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		var paging = pageInfoJs.getPaging();
   		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

   		param += "&bizRegNo=" + data.searchCond.bizRegNo;
   		param += "&companyName=" + data.searchCond.companyName;

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/organization/company/list" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayCompany(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	};
   	var result;
   	function _displayCompany(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		var html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="9">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		result = jsonData.result;
   		var noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr>';
   			html += '<td>' + (i + noIndex) + '</td>';
   			html += '<td><a href="#" onclick="companyListJs.searchDetail(' + i + ')">' + result[i].companyName + '</a></td>';
   			html += '<td style="text-align: center;"><button onclick="companyListJs.sso(\'' + result[i].coUserLoginId + '\')">' + _msg.login + '</button></td>';
   			html += '<td>' + result[i].ceoName + '</td>';
   			html += '<td>' + formmatUtilsJs.bizRegNoFormat(result[i].bizRegNo) + '</td>';
   			html += '<td>' + (result[i].billMethodCode ? parent.commonCodeJs.getCodeNameBySubCode(result[i].billMethodCode) : '-') + '</td>';
   			html += '<td>' + (result[i].pymtReqCode ? result[i].pymtReqCode : '-') + '</td>';
   			html += '<td>' + (result[i].billMethodCode ? _msg.paymentCard : _msg.paymentNotRegistered) + '</td>';
   			html += '<td>' + result[i].custCount + ' ' + _msg.personUnit + '</td>';

   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	};

    function _sso(loginId){
    	//
    	try{
    		parent.sso(loginId);
    	}catch(ex){
    		//
    	}
    };

    function _searchDetail(index){
        //
    	let paging = pageInfoJs.getPaging();
    	let param = "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize="+ paging.pageItemSize;
    	param += "&companyName=" + data.searchCond.companyName;
		param += "&bizRegNo=" + data.searchCond.bizRegNo;
		param += "&searchType=" + $("#searchType").val();
   		param += "&sWord=" + decodeURI($("#sWord").val());

        self.location= _ctx + "/organization/company/detail?companyId=" + result[index].companyId + param;
    };

   	return {
   		init : _init,
   		search : _search,
   		searchDetail : _searchDetail,
   		sso : _sso
   	};
}();
