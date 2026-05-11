/**
 * 충전관리 - 고장관리
 */
let breakdownListJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    function _init() {
        _initEvent();
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -30), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
//        zipCodeJs.searchZipCode(undefined, _disZipDoSiCode);
        if (queryString.pageItemSize) {
			let cpCsId;
            pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, breakdownListJs.search);
            pageInfoJs.setPageNumber(Number(queryString.pageNumber) + 1);
            if(queryString.cpId == "" && queryString.csId == ""){
				cpCsId = "";
				data.searchCond.cpId = "";
            	data.searchCond.csId = "";
			} else {
				data.searchCond.cpId = queryString.cpId;
            	data.searchCond.csId = queryString.csId;
            	cpCsId = queryString.cpId + "-" + queryString.csId;
			}
            if(queryString.breakdownStatus == undefined) {
				data.searchCond.breakdownStatus = "";
			} else {
				data.searchCond.breakdownStatus = queryString.breakdownStatus;
			}
			if(queryString.fromDate == undefined || queryString.fromDate == '') {
				$('#date1').val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -30),"YYYY-MM-DD"));
		        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val());
			} else {
				data.searchCond.fromDate = queryString.fromDate;
				$('#date1').val(formmatUtilsJs.dateFormmat(queryString.fromDate, "YYYY-MM-DD"));
			} if(queryString.toDate == undefined || queryString.toDate == '') {
				$('#date2').val(dateUtilsJs.currentDate("YYYY-MM-DD"));
				data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val());
			} else {
				data.searchCond.toDate = queryString.toDate;
				$('#date2').val(formmatUtilsJs.dateFormmat(queryString.toDate, "YYYY-MM-DD"));
			}
			/*if(queryString.receiptDate == undefined || queryString.receiptDate == '') {
				data.searchCond.receiptDate = "";
				$("#date1").val(formmatUtilsJs.dateFormmat(dateUtilsJs.currentDate(), 'YYYY-MM-DD'));
			} else {
				let receiptDate = formmatUtilsJs.dateFormmat(queryString.receiptDate, 'YYYY-MM-DD');
				data.searchCond.receiptDate = receiptDate.replace(/-/gi, '').trim();
				$("#date1").val(receiptDate);
			}*/
            _search();

            $("#cpCsId").val(cpCsId);
            $("#bdType").val(queryString.breakdownStatus);
            
        } else {
            _searchOnClick();
        }
    }

    function _initEvent() {
        //
        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });

        $("#btnSearch").click(function () {
            //
            _searchOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        $("#btnForm").click(function () {
            //
            _registerOnClick();
        });

        // 검색조건 Enter키로 검색기능
        $("#cpCsId").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchOnClick();
            }
        });
        //고장처리상태
        let bdTypes = parent.commonCodeJs.getCodesByParentCode('BDST00');
        $("#bdType").append('<option value="">' + _msg.statusAll + '</option>');
        let html = '';
        for (let i = 0, size = bdTypes.length; i < size; ++i) {
            html = '<option value="' + bdTypes[i].code + '">';
            html += bdTypes[i].codeName;
            html += '</option>';
            $("#bdType").append(html);
        }
        
        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $('#date2').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        
    }

    function _searchResetClick() {
        $("#cpCsId").val("");
        $("#bdType").val("");
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -30), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        _searchOnClick();
    }
    
    function _registerOnClick() {
        //
        self.location = _ctx + "/charger/breakdown";
    }

    function _searchOnClick() {
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, breakdownListJs.search);
        data.searchCond.cpId = "";
        data.searchCond.csId = "";
        data.searchCond.breakdownStatus = "";
        //data.searchCond.receiptDate = "";
        
        if ($("#date1").val() != null && $("#date1").val() != '') {
        	data.searchCond.receiptDate = $("#date1").val().replace(/-/gi, '').trim();
        }
        
        let searchKey = $("#cpCsId").val().replace(/-/g, '').trim();
        
        data.searchCond.breakdownStatus = $("#bdType").val();
        
        data.searchCond.csId = searchKey;
        
        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            if (data.searchCond.csId.length !== 8) {
				toastr.warning(_msg.csIdInvalid, _msg.csId);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }
        
        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val());
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val());
        
        $("#cpCsId").val(searchKey);
        
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="9">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&breakdownStatus=" + data.searchCond.breakdownStatus;
        //param += "&receiptDate=" + data.searchCond.receiptDate;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/breakdown/list" + param ,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayBreakdown(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayBreakdown(jsonData) {

        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="9">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td><a href="#" onclick="breakdownListJs.searchDetail(\'' + result[i].id + '\')">' + result[i].id + '</a></td>';
            html += '<td>' + result[i].cpId + '-' + result[i].csId + '</td>';
            if(result[i].receiptDate) {
				let receiptDt = formmatUtilsJs.dateFormmat(result[i].receiptDate + result[i].receiptTime, 'YYYY-MM-DD HH:MM:SS');
				html += '<td>' + receiptDt.substring(0,10) + '</td>';
            	html += '<td>' + receiptDt.substring(11,19) + '</td>';
			} else {
				html += '<td>-</td>';
            	html += '<td>-</td>';
			}
            if(result[i].repairDate) {
				let repairDt = formmatUtilsJs.dateFormmat(result[i].repairDate + result[i].repairTime, 'YYYY-MM-DD HH:MM:SS');
            	html += '<td>' + repairDt.substring(0,10) + '</td>';
            	html += '<td>' + repairDt.substring(11,19) + '</td>';
			} else {
				html += '<td>-</td>';
            	html += '<td>-</td>';
			}
            let breakdownStatus = parent.commonCodeJs.getCodeNameBySubCode(result[i].breakdownStatus);
            html += '<td>' + breakdownStatus + '</td>';
            html += '<td>' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }

    }

    function _searchDetail(id) {
        //
        let param = "?id=" + id;
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        param += "&breakdownStatus=" + data.searchCond.breakdownStatus;
        //param += "&receiptDate=" + data.searchCond.receiptDate;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        self.location = _ctx + "/charger/breakdown/detail" + param;
    }

    function _downloadExcel() {
        //
    	let paging = pageInfoJs.getPaging();
    	let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
    	param += "&cpName=" + data.searchCond.cpName;
    	param += "&receiptDate=" + data.searchCond.receiptDate;
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargePoint/download" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if(jsonData.status == 'SUCCESS'){
                	parent.layerJs.fn_download("/ws/download/DWCH001?tokenId=" + jsonData.result);
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
        init: _init,
        search: _search,
        searchDetail: _searchDetail
    };
}();
