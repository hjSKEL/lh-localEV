/**
 * 충전기 충전정보
 */
let rechargingJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    let _resultCache = [];

    function _init() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        _initEvent();
        _searchRechargingOnClick();

        chargingPointPopupJs.init();
    }

    function _initEvent() {
        //
        $("#btnSearch").click(function () {
            //
            _searchRechargingOnClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchRechargingOnClick();
            }
        });

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

        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });

        // 최대 에너지 한도 변경 모달
        $(document).on('click', '.me-quick', function () {
            var add = parseFloat($(this).data('amt')) || 0;
            var cur = parseFloat($("#me_newMax").val()) || 0;
            $("#me_newMax").val(cur + add);
        });
        $("#meQuickUnlimited").click(function () { $("#me_newMax").val(0); });
        $("#btnMaxEnergySave").click(_submitMaxEnergy);

        let chaStatusCodes = parent.commonCodeJs.getCodesByParentCode("RECS00");
        let html = "";
        for (let i = 0, size = chaStatusCodes.length; i < size; ++i) {
            html = '<label><input type="radio" name="chaStatus" value="' + chaStatusCodes[i].code + '"' + (chaStatusCodes[i].code === 'RECS02' ? 'checked' : '') + '><span>' + chaStatusCodes[i].codeName + '</span></label>';
            $("#chaStatus").append(html);
        }

        $("input:radio[name='chaStatus']").click(function () {
            _searchRechargingOnClick();
        });
        
        $("#dateOrder").change(function () {
			_searchRechargingOnClick();
		});
    }

    function _searchResetClick() {
        $("#date1").val(dateUtilsJs.formatDate(dateUtilsJs.addDay(new Date(), -7), "YYYY-MM-DD"));
        $("#date2").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $("#searchKey").val("");
        $("input:radio[name='chaStatus']:radio[value='RECS02']").prop('checked', true);
        $("#dateOrder").val("B");
        $("#dateType").val("S");
        _searchRechargingOnClick();
    }

    function _searchRechargingOnClick() {
    	//
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, rechargingJs.search);
        data.searchCond.cpId = "";
        data.searchCond.cpName = "";
        data.searchCond.csId = "";
        data.searchCond.csUniqId = "";
        data.searchCond.rechargingId = "";
        data.searchCond.dateOrder = $("#dateOrder").val();
        data.searchCond.dateType = $("#dateType").val();

        let searchKey = $("#searchKey").val().replace(/-/g, '').trim();
        if (searchKey && searchKey.length > 0) {

            switch ($("#sType").val()) {
                case 'CHARGE_POINT':
                    data.searchCond.cpName = encodeURI(searchKey);
                    break;
                case 'CHARGING_STATION':
					if(searchKey.length)
                    data.searchCond.csId = searchKey;
                    break;
                case 'RECHARGING_ID':
                    data.searchCond.rechargingId = searchKey;
                    break;
            }

        }
        $("#searchKey").val(searchKey);
        if (data.searchCond.csId && data.searchCond.csId.length > 0) {
            if (data.searchCond.csId.length !== 8) {
				toastr.warning(_msg.chargerIdDigit8, _msg.chargerId);
                return;
            }
            data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
            data.searchCond.csId = data.searchCond.csId.substring(6);
        }

        data.searchCond.status = $(":input:radio[name=chaStatus]:checked").val();

        data.searchCond.fromDate = formmatUtilsJs.removeDash($("#date1").val()) + "000000";
        data.searchCond.toDate = formmatUtilsJs.removeDash($("#date2").val()) + "235959";
        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="23">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&status=" + data.searchCond.status;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csId=" + data.searchCond.csId;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&rechargingId=" + data.searchCond.rechargingId;
        param += "&dateOrder=" + data.searchCond.dateOrder;
        param += "&dateType=" + data.searchCond.dateType;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/recharging/list" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayRecharging(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _downloadExcel() {
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&status=" + data.searchCond.status;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&csId=" + data.searchCond.csId;
        param += "&fromDate=" + data.searchCond.fromDate;
        param += "&toDate=" + data.searchCond.toDate;
        param += "&rechargingId=" + data.searchCond.rechargingId;
        param += "&dateOrder=" + data.searchCond.dateOrder;
        param += "&dateType=" + data.searchCond.dateType;
        parent.layerJs.fn_download(_ctx + "/ws/recharging/download/list" + param);
        
    }

    function _displayRecharging(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="23">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        let result = jsonData.result;
        _resultCache = result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            if (result[i].chStatCode === 'RECS02') {
            	html += '<td><a href="#" onclick="rechargingJs.searchRechargingDetail(\'' + result[i].rechargingId + '\')">' + result[i].rechargingId + '</a></td>';
            }else{
            	html += '<td>' + result[i].rechargingId + '</td>';
            }
            html += '<td><a href="#" onclick="rechargingJs.popup(' + result[i].cpId + ')">' + result[i].cpName + '</a></td>';
            html += '<td>' + formmatUtilsJs.cardFormat(result[i].cutCardNo) + '</td>';
            html += '<td>' + (result[i].idTagType || '') + '</td>';
            if (result[i].chStartDate) {
                let cdt = new Date(result[i].chStartDate);
                html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].chEndDate) {
                let cdt = new Date(result[i].chEndDate);
                let curTime = new Date().getTime();
                let chaTime = cdt.getTime();
                if (curTime - chaTime > 1000 * 60 * 10 && result[i].chStatCode === 'RECS02') {
                    html += '<td style="background-color:#FFDCDC"> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + ' </td>';
                } else {
                    html += '<td> ' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + ' </td>';
                }
            } else {
                html += '<td> </td>';
            }
            if (result[i].chStartDate && result[i].chEndDate) {
                let chTime = _chargingTime(new Date(result[i].chStartDate), new Date(result[i].chEndDate));
                html += chTime + '</td>';
            } else {
                html += '<td> </td>';
            }
            html += '<td>' + (parent.commonCodeJs.getCodeNameBySubCode(result[i].chStatCode)) + '</td>';
            html += '<td>' + result[i].chUseAmount + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].chUseUnitCost) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].chUseCost) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].paySum) + '</td>';
            html += '<td>' + result[i].cpId + '-' + result[i].csId + '</td>';
            html += '<td>' + result[i].evseId + '</td>';
            html += '<td>' + (result[i].startCaEleEnerge ? result[i].startCaEleEnerge : "0") + '</td>';
            html += '<td>' + (result[i].endCaEleEnerge ? result[i].endCaEleEnerge : "0") + '</td>';
            html += '<td style="text-align:right;">' + (result[i].maxEnergy ? formmatUtilsJs.commaFormat(result[i].maxEnergy) : "0") + '</td>';
            html += '<td>' + _formatDateTime(result[i].pkStartDate) + '</td>';
            html += '<td>' + _formatDateTime(result[i].pkEndDate) + '</td>';
            html += '<td>' + _formatDateTime(result[i].cableStartDate) + '</td>';
            html += '<td>' + _formatDateTime(result[i].cableEndDate) + '</td>';
            html += '<td>' + _actionButtons(i, result[i]) + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
        }
    }

    function _actionButtons(index, row) {
        if (row.chStatCode === 'RECS02') {
            return '<button type="button" class="btn btn-xs btn-info" onclick="rechargingJs.openMaxEnergy(' + index + ')">' + _msg.btnEdit + '</button>';
        }
        return '-';
    }

    function _openMaxEnergy(index) {
        var row = _resultCache[index];
        if (!row) return;
        if (row.chStatCode !== 'RECS02') {
            toastr.warning(_msg.onlyActiveMaxEnergy, _msg.maxEnergyMgmt);
            return;
        }
        $("#me_rechargingId").val(row.rechargingId);
        $("#me_cpName").val(row.cpName || '-');
        $("#me_startTime").val(_formatDateTime(row.chStartDate));
        $("#me_currentMax").val((row.maxEnergy ? formmatUtilsJs.commaFormat(row.maxEnergy) : '0') + ' Wh');
        $("#me_newMax").val(row.maxEnergy || 0);
        $("#Popup_Recharging_MaxEnergy").modal();
    }

    function _submitMaxEnergy() {
        var rechargingId = $("#me_rechargingId").val();
        var maxEnergy = parseFloat($("#me_newMax").val());
        if (!rechargingId) return;
        if (isNaN(maxEnergy) || maxEnergy < 0) {
            toastr.warning(_msg.inputMaxEnergy, _msg.maxEnergyMgmt);
            return;
        }
        swal({
            title: _msg.maxEnergyMgmt,
            text: _msg.confirmMaxEnergy.replace('{0}', formmatUtilsJs.commaFormat(maxEnergy)),
            type: "info",
            showCancelButton: true,
            confirmButtonColor: "#1ab394",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/recharging/" + encodeURIComponent(rechargingId) + "/maxEnergy",
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify({ maxEnergy: maxEnergy }),
                success: function (res) {
                    if (res.status == 'SUCCESS') {
                        toastr.success(_msg.successMaxEnergy, _msg.maxEnergyMgmt);
                        $("#Popup_Recharging_MaxEnergy").modal('hide');
                        rechargingJs.search();
                    } else {
                        toastr.error(res.result || _msg.failMaxEnergy, _msg.maxEnergyMgmt);
                    }
                },
                error: function (xhRequest) {
                    parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_msg.failMaxEnergy, _msg.maxEnergyMgmt);
                }
            });
        });
    }

    function _calcDate(no) {
        //
        let from = dateUtilsJs.addDay(new Date(), -no);
        let to = new Date();
        $('#date2').val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(to), "YYYY-MM-DD"));
        $('#date1').val(formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(from), "YYYY-MM-DD")); //몇일 전
    }

    function _formatDateTime(val) {
        if (!val) return '';
        let dt = new Date(val);
        return formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(dt), 'YYYY-MM-DD HH:MM:SS');
    }

    //충전소요시간
    function _chargingTime(start, end) {
        let result = ''; //50400000 => 14시간	//86400000 => 1일
        let gapTime = end.getTime() - start.getTime();
        let d = parseInt(gapTime / (1000 * 60 * 60 * 24));
        let h = Math.floor((gapTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let m = Math.floor((gapTime % (1000 * 60 * 60)) / (1000 * 60));
        let s = Math.floor((gapTime % (1000 * 60)) / 1000);

        if (gapTime >= 50400000) {
            result += '<td style="background-color:#FFDCDC">';
        } else {
            result += '<td>';
        }

        if (d > 0) {
            result += d + _msg.day;
        }
        if (h > 0) {
            result += h + _msg.hour;
        }
        if (m > 0) {
            result += m + _msg.minute;
        }
        if (s >= 0) {
            result += s + _msg.second;
        }
        return result;
    }

    function _searchRechargingDetail(rechargingId) {
        let param = "?rechargingId=" + rechargingId;
        parent.layerJs.fn_moveMenu('20000007', _msg.exceptionMgmt, _ctx + '/recharging/exception/view' + param, 'THIS', true);
    }

    function _popup(cpId) {
        $("#Popup_ChargingPointInfo").modal();
        chargingPointPopupJs.search(cpId);
    }

    return {
        init: _init,
        search: _search,
        calcDate: _calcDate,
        searchRechargingDetail: _searchRechargingDetail,
        popup: _popup,
        openMaxEnergy: _openMaxEnergy
    };
}();
