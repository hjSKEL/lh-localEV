/**
 * 조직관리 - 직원관리
 */
let employeeListJs = function(){
    "use strict";

    let data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   		if(queryString.pageItemSize){
   			pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, employeeListJs.search);
			pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);

			data.searchCond.emplName = queryString.emplName;
			data.searchCond.companyName = queryString.companyName;

			$("#searchType").val(queryString.searchType);

			switch(queryString.searchType){
				case "EMPLOYEE_NAME":
					$("#sWord").val(queryString.emplName);
					break;
				case "COMPANY_NAME":
					$("#sWord").val(queryString.companyName);
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
			if(event.keyCode === 13) {
			  	_searchOnClick();
			}
		});
   	}

   	function _searchResetClick() {
        $("#sWord").val("");
        _searchOnClick();
    }

	function _registerOnClick(){
		//
		self.location= _ctx + "/organization/employee";
	}

   	function _searchOnClick(){
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, employeeListJs.search);


   		data.searchCond.emplName = "";
   		data.searchCond.companyName = "";

   		let searchType = $("#searchType").val();
   		let searchKey = $("#sWord").val().trim();
   		switch(searchType){
	   		case "EMPLOYEE_NAME":
	   			data.searchCond.emplName = searchKey;
	   			break;
	   		case "COMPANY_NAME":
	   			data.searchCond.companyName = encodeURI(searchKey);
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
   		html += '<td colspan="9">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		let paging = pageInfoJs.getPaging();
   		let param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

   		param += "&emplName=" + data.searchCond.emplName;
   		param += "&companyName=" + data.searchCond.companyName;

   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/organization/employee" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayCompany(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	}
   	let result;
   	function _displayCompany(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		let html = '';
   		if(jsonData.criteria.totalItemCount === 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="9">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		result = jsonData.result;
   		let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
   		let role = parent.UserRole;
   		for(let i=0, length = result.length ; i < length ; ++i){
   			html = '<tr>';
   			html += '<td class="footable-visible footable-first-column">' + (i + noIndex) + '</td>';
			html += '<td><a href="#" onclick="employeeListJs.searchDetail(\'' + i + '\',\'' + result[i].emplStatus + '\')"> ' + result[i].emplName + '</a></td>';
   			html += '<td class="footable-visible">' + result[i].companyName + '</td>';
   			html += '<td class="footable-visible">' + (result[i].loginId ? result[i].loginId : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].mblPhoneNo ? formmatUtilsJs.phoneFormat(result[i].mblPhoneNo) : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].roleType ? result[i].roleType : '-') + '</td>';
   			if(result[i].emplStatus === '1') {
				if(result[i].pwFailCount < 5) {
					html += '<td class="footable-visible">' + _msg.statusNormal + '</td>';
				} else {
					html += '<td class="footable-visible">' + _msg.statusLocked + '</td>';
				}
			}
			if(result[i].emplStatus === '2') {
				html += '<td class="footable-visible">' + _msg.statusResigned + '</td>';
			}
   			html += '<td class="footable-visible footable-last-column">' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
			if(result[i].lastLoginDate){
				html += '<td class="footable-visible footable-last-column">' + dateUtilsJs.formatDate(new Date(result[i].lastLoginDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
			}else{
				html += '<td class="footable-visible footable-last-column">-</td>';
			}
   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	}

    function _searchDetail(index,status){
        //
    	let paging = pageInfoJs.getPaging();
    	let param = "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize="+ paging.pageItemSize;
    	param += "&emplName=" + data.searchCond.emplName;
		param += "&companyName=" + data.searchCond.companyName;
		param += "&searchType=" + $("#searchType").val();
   		param += "&sWord=" + decodeURI($("#sWord").val());
   		param += "&status=" + status;

        self.location= _ctx + "/organization/employee/detail?employeeId=" + result[index].employeeId + param;
    }

   	return {
   		init : _init,
   		search : _search,
   		searchDetail : _searchDetail
   	};
}();
