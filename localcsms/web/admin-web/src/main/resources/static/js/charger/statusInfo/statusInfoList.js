/**
 * 충전기 상태 정보
 */
let statusInfoListJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    let err = {
        makerType: [],
        csKindType: [],
        csErrorStatus: [],
    };

    function _init() {
        _initEvent();
        _searchChargerStatusClick();
    }

    function _initEvent() {
        $("#btnSearch").click(function () {
            _searchChargerStatusClick();
        });
        $("#btnReset").click(function () {
            _searchResetClick();
        });
        //검색조건 Enter키로 검색기능
        $("#searchKey").keypress(function (event) {
            if (event.keyCode == 13) {
                _searchChargerStatusClick();
            }
        });
        $("#saveExcelcs").click(function () {
            _downloadExcel();
        });
    }

    function _searchResetClick() {
    	//
        $("#selChaStatus").val("");
        $("#searchKey").val("");
        _searchChargerStatusClick();
    }

    function _searchChargerStatusClick() {
        //
        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, statusInfoListJs.search);
        data.searchCond.standardDate = "";
        data.searchCond.status = "";
        data.searchCond.csErrorStatus = "";
        data.searchCond.cpId = "";
        data.searchCond.cpName = "";

        let searchType = $("#searchType").val();
        let searchKey = $("#searchKey").val().trim();
        switch (searchType) {
            case 'cpId':
                if (searchKey && searchKey.length != 6) {
					toastr.warning(_msg.cpIdLength6, _msg.cpIdLabel);
					return;
                } else {
					data.searchCond.cpId = searchKey;
				}
				break;
            case 'csUniqId':
                data.searchCond.csUniqId = searchKey;
                break;
            case 'cpName':
                data.searchCond.cpName = encodeURI(searchKey);
                break;
        }
        $("#searchKey").val(searchKey);

        let status = $("#selChaStatus").val();
        switch (status) {
        	case 'A':
        		data.searchCond.status = "CHRS03";
        		break;
            case 'B':
                data.searchCond.status = "CHRS08";
                break;
            case 'C':
                data.searchCond.status = "CHRS04";
                break;
            case 'D':
                data.searchCond.status = "CHRS09";
                break;
            case 'E':
                data.searchCond.status = "CHRS01,CHRS02,CHRS06";
                break;
            default:
        }

        _search();
    }

    function _search() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="18">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&standardDate=" + data.searchCond.standardDate;
        param += "&status=" + data.searchCond.status;
        param += "&csErrorStatus=" + data.searchCond.csErrorStatus;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&cpName=" + data.searchCond.cpName;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/status" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayChargerStatus(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayChargerStatus(jsonData) {
        pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

        $("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyList").empty();
        let html = '';
        if (jsonData.criteria.totalItemCount == 0) {
            html = '<tr style="text-align:center;">';
            html += '<td colspan="18">' + _commonMsg.noData + '</td>';
            html += '</tr>';
            $("#tBodyList").append(html);
            return;
        }
        err = {
            makerType: [],
            csKindType: [],
            csErrorStatus: [],
        };
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            let colorType = 'A'; //A: 정상, B: 30분이상 통신X, C: 15분이상 통신X, D: 사용중지
            if (result[i].infoCollDate) {
                let cdt = new Date(result[i].infoCollDate);
                let curTime = new Date().getTime();
                let chaTime = cdt.getTime();
                if (curTime - chaTime > 1000 * 60 * 30) {
                    colorType = 'B';
                } else if (curTime - chaTime > 1000 * 60 * 16) {
                    colorType = 'C';
                }
            }
//            if (result[i].chargePoint.cpUseYn == 'N') {
//                colorType = 'D';
//            }
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td>' + result[i].cpName + '</td>';
            html += '<td>' + result[i].cpId + '</td>';
            //html += '<td><a href=javascript:;" data-toggle="collapse" data-target="#ex' + i + '" class="accordion-toggle" onclick="statusInfoListJs.searchStatusInfoDetail(\'' + result[i].cpId + '\',\'' + result[i].csId + '\',\'' + i + '\')">' + result[i].cpName + '</a></td>';
            html += '<td><a href="#" onclick="statusInfoListJs.searchChargerDetail(\'' + result[i].cpId + '\',\'' + result[i].csId + '\',\'' + result[i].evseId + '\')">' + result[i].cpId + '-' + result[i].csId + '</a></td>';
            html += '<td> ' + result[i].evseId + ' </td>';
            if (result[i].csStatCode) {
                html += '<td> ' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csStatCode) + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].csCableStatus) {
                html += '<td> ' + parent.commonCodeJs.getChargerCableStatusDesc(result[i].csCableStatus) + ' </td>';
            } else {
                html += '<td> </td>';
            }
            if (result[i].csErrorStatus) {
                err.makerType.push(result[i].makerType);
                err.csKindType.push(result[i].csKindType);
                err.csErrorStatus.push(result[i].csErrorStatus);
                html += '<td class="' + result[i].makerType + result[i].csKindType + result[i].csErrorStatus + '">' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csErrorStatus) + '</td>';
            } else {
                html += '<td> </td>';
            }
            let cdt = "-";
            if (result[i].infoCollDate) {
                cdt = new Date(result[i].infoCollDate);
                cdt = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM');
            }
            if (colorType == 'A') {
                html += '<td>' + cdt + '</td>';
            } else if (colorType == 'B') {
                html += '<td style="background-color: #f78080;">' + cdt + '</td>';
            } else if (colorType == 'C') {
                html += '<td style="background-color: #FFDCDC;">' + cdt + '</td>';
            } else if (colorType == 'D') {
                html += '<td style="background-color: rgba(85,85,85,0.5);">' + cdt + '</td>';
            }
            if (result[i].cuEleEnerge) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].cuEleEnerge) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].caEleEnerge) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].caEleEnerge) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].instChAmont) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].instChAmont) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].instChCost) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].instChCost) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].instChSum) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].instChSum) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].chSum) {
                html += '<td style="text-align: right;padding-right: 10px;"> ' + formmatUtilsJs.commaFormat(result[i].chSum) + ' </td>';
            } else {
                html += '<td style="text-align: right;padding-right: 10px;">0</td>';
            }
            if (result[i].rechargingId) {
                html += '<td>' + result[i].rechargingId + '</td>';
            } else {
                html += '<td></td>';
            }
            if (result[i].eventCode) {
                html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].eventCode) + '</td>';
            } else {
                html += '<td></td>';
            }
            html += '</tr>';
            $("#tBodyList").append(html);
        }

        let makerType = new Set(err.makerType);
        let csKindType = new Set(err.csKindType);
        let csErrorStatus = new Set(err.csErrorStatus);

        err.makerType = [...makerType];
        err.csKindType = [...csKindType];
        err.csErrorStatus = [...csErrorStatus];
        _csAlarmSearch();
    }

    function _downloadExcel() {
        toastr.info(_msg.pleaseWait, _msg.excelDownload);
        let param = "?standardDate=" + data.searchCond.standardDate;
        param += "&status=" + data.searchCond.status;
        param += "&csErrorStatus=" + data.searchCond.csErrorStatus;
        param += "&regionId=" + data.searchCond.regionId;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&csUniqId=" + data.searchCond.csUniqId;
        param += "&cpName=" + data.searchCond.cpName;
        param += "&roadName=" + data.searchCond.roadName;
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/statusInfo/download" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if (jsonData.status == 'SUCCESS') {
                    parent.layerJs.fn_download("/ws/download/DWCH003?tokenId=" + jsonData.result);
                } else {
                    alert(_commonMsg.excelFail + " : " + jsonData.result);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
                alert(_commonMsg.commError);
            }
        });

    }

    function _searchChargerDetail(cpId, csId, ch) {
        $("#Popup_StationInfo").modal();
        chargerPopupJs.search(cpId, csId, ch);
    }
    
    function _searchBreakdownDetail(cpId, csId){
		
		let param = "?cpId=" + cpId + "&csId=" + csId;

		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/breakdown/receipt/list" + param,
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				_searchBreakdownInfo(jsonData);
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
			}
		});
	}
	
	function _searchBreakdownInfo(jsonData) {
		if(!jsonData.result){
			toastr.error(_msg.noBreakdownHistory, _msg.breakdownHistory);
			return ;
		}
		$("#Popup_BreakdownInfo").modal();
        breakdownPopupJs.search(jsonData.result[0].id);
	}

    function _searchStatusInfoDetail(cpId, csId, i) {
        if ($("#toggle_ChargerRegistrationDate" + i).html() === '') {
            $.ajax({
                type: 'GET',
                url: _ctx + "/ws/charger/" + cpId + "/" + csId + "/info",
                dataType: 'json',
                success: function (jsonData, textStatus, jqXHR) {
                    _displayCharger(jsonData, i);
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                }
            });
        }
    }

    function _displayCharger(jsonData, i) {
        $("#toggle_RoadName" + i).html(null);
        $("#toggle_ChargerRegistrationDate" + i).html(null);
        $("#toggle_UseYn" + i).html(null);
        $("#toggle_PlaceName" + i).html(null);
        $("#toggle_TelecomNo" + i).html(null);
        $("#toggle_BrkdownYn" + i).html(null);
        if (!jsonData) {
            return;
        }
        $("#toggle_RoadName" + i).html(jsonData.chargePoint.roadName + " " + jsonData.chargePoint.roadDetName);
        let chargerRegistrationDate = formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(jsonData.writer.registrationDate)), 'YYYY-MM-DD HH:MM');
        $("#toggle_ChargerRegistrationDate" + i).html(chargerRegistrationDate);
        $("#toggle_UseYn" + i).html(jsonData.useYn == 'Y' ? _msg.inUse : _msg.notInUse);
        $("#toggle_PlaceName" + i).html(jsonData.chargePoint.placeName);
        $("#toggle_TelecomNo" + i).html(formmatUtilsJs.phoneFormat(jsonData.telecomNo));
        $("#toggle_BrkdownYn" + i).html(jsonData.brkdownYn == 'Y' ? _msg.broken : _msg.normal);
    }

    function _searchAlarmDetail(makerType, errCode, csKindType) {
        $("#Popup_CsAlarmInfo").modal();
        csAlarmInfoPopupJs.init();
        csAlarmInfoPopupJs.search(makerType, errCode, csKindType);
    }

    function _moveCsControl(csUniqId, csCableChn) {
        let param = "?csUniqId=" + csUniqId + "&csCableChn=" + csCableChn;
        if (parent.UserRole == 'ADMIN') {
            parent.layerJs.fn_moveMenu('20000019', _msg.menuCsControl, '/evAdmin/charger/chargingStation/control' + param, 'THIS', true);
        } else if (parent.UserRole == 'OPERATION') {
            parent.layerJs.fn_moveMenu('30100106', _msg.menuCsControl, '/evAdmin/charger/chargingStation/control' + param, 'THIS', true);
        }
    }

    function _registerBrkdown(cpName, cpId, csId) {
        let param = "?cpId=" + cpId + "&cpName=" + cpName + "&csId=" + csId;
        if (parent.UserRole == 'ADMIN') {
            parent.layerJs.fn_moveMenu('20000503', _msg.menuBreakdownReceipt, '/evAdmin/breakdown/receipt' + param, 'THIS', true);
        } else if (parent.UserRole == 'OPERATION') {
            parent.layerJs.fn_moveMenu('30100201', _msg.menuBreakdownReceipt, '/evAdmin/breakdown/receipt' + param, 'THIS', true);
        }
    }

    function _csAlarmSearch() {
        let param = "?makerTypeCodes=" + err.makerType;
        param += '&errorCodes=' + err.csErrorStatus;
        param += '&csKindTypes=' + err.csKindType;

        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/charger/csAlarm/list' + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if (jsonData.length == 0) return;
                let result = jsonData;
                for (let i = 0, length = result.length; i < length; ++i) {
                    let count = $('.' + result[i].makerTypeCode + result[i].csKindType + result[i].errorCode).length;
                    for (let j = 0; j < count; ++j) {
                        let html = '<a href="#" onclick="statusInfoListJs.searchAlarmDetail(\'' + result[i].makerTypeCode + '\', \'' + parent.commonCodeJs.getCodeNameBySubCode(result[i].errorCode) + '\', \'' + result[i].csKindType + '\')">' + result[i].manufacturerCode + '(' + parent.commonCodeJs.getCodeNameBySubCode(result[i].errorCode) + ')</a>'
                        $('.' + result[i].makerTypeCode + result[i].csKindType + result[i].errorCode).eq(j).html(html);
                    }
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }
    
    return {
        init: _init,
        search: _search,
        searchChargerDetail: _searchChargerDetail,
        searchBreakdownDetail: _searchBreakdownDetail,
        searchStatusInfoDetail: _searchStatusInfoDetail,
        searchAlarmDetail: _searchAlarmDetail,
        registerBrkdown: _registerBrkdown,
        moveCsControl: _moveCsControl
    };
}();