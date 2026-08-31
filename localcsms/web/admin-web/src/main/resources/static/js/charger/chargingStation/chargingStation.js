/**
 * 충전기 등록
 */
let chargingStationJs = function () {
    "use strict";

    let data = {};
    let searchData = {};
    let standDate = dateUtilsJs.currentDate();

    function _init() {
        _initEvent();
        if (cpId && csId) {
            _search(cpId, csId);
        } else {
            //요금제조회
            $.ajax({
                type: 'GET',
                url: _ctx + "/ws/product/search?pageNumber=0&pageItemSize=999&standDate=" + standDate,
                dataType: 'json',
                success: function (jsonData, textStatus, jqXHR) {
                    if (jsonData.criteria.totalItemCount !== 0) {
                        let defaultValue = '';
                        $("#prodType").append('<option value="">' + _msg.selectProduct + '</option>');
                        for (let i = 0, length = jsonData.result.length; i < length; ++i) {
                            $("#prodType").append('<option value="' + jsonData.result[i].id + '">' + jsonData.result[i].name + '</option>');
                            if (i === 0) {
                                defaultValue = jsonData.result[i].productType;
                            }
                        }
                        $("#prodType").val(defaultValue);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                }
            });
        }
    }

    function _initEvent() {
        //충전소 검색 팝업
        $("#btnSearchChargePoint").click(function () {
            $("#Popup_ChargePoint").modal();
            chargePointSearchPopupJs.init(_selectedChargePoint);
        });
        
        if(csId == null || csId == '') {
			$("#cpName").click(function () {
            	$("#Popup_ChargePoint").modal();
            	chargePointSearchPopupJs.init(_selectedChargePoint);
        	});
		}

        $("#btnRegister").click(function () {
            _registerOnClick();
        });
        $("#btnList").click(function () {
            _listOnClick();
        });
        $("#btnUpdate").click(function () {
            _updateOnClick();
        });
        $("#btnDelete").click(function () {
            _deleteOnClick();
        });
        $("#btnBreakdownList").click(function () {
            _moveBreakdownListOnClick();
        });

        // 서비스 방식(일반/배터리교환) 전환 시 배터리 입력 영역 토글
        $("#csServiceType").change(function () {
            _toggleBatterySwap();
        });
        _toggleBatterySwap();

        let csKindTypes = parent.commonCodeJs.getCodesByParentCode('CHKT00');
        $("#csKindType").append('<option value="">' + _msg.selectCsKindType + '</option>');
        for (let i = 0, length = csKindTypes.length; i < length; ++i) {
            $("#csKindType").append('<option value="' + csKindTypes[i].code + '">' + csKindTypes[i].codeName + '</option>');
        }
        
        //소속
        $("#companyName").val(parent.roleCompanyName);
        $("#companyId").val(parent.roleCompanyId);

        //제조사
        let makerTypes = parent.commonCodeJs.getCodesByParentCode('CHMK00');
        $("#makerType").append('<option value="">' + _msg.selectMaker + '</option>');
        let html = '';
        for (let i = 0, size = makerTypes.length; i < size; ++i) {
            if (makerTypes[i].code === "UL") {
                html = '<option selected="selected" value="' + makerTypes[i].code + '">';
            } else {
                html = '<option value="' + makerTypes[i].code + '">';
            }
            html += makerTypes[i].codeName;
            html += '</option>';
            $("#makerType").append(html);
        }

        //급속/완속 종벌
        let csCatCodes = parent.commonCodeJs.getCodesByParentCode('CHRA00');
        $("#csCatCode").append('<option value="">' + _msg.selectConnector + '</option>');
        for (let i = 0, length = csCatCodes.length; i < length; ++i) {
            $("#csCatCode").append('<option value="' + csCatCodes[i].code + '">' + csCatCodes[i].codeName + '</option>');
        }

        // V2X 유형 (충전 / 충전+방전). 기본값 V2XT01.
        let v2xTypes = parent.commonCodeJs.getCodesByParentCode('V2XT00');
        for (let i = 0, length = v2xTypes.length; i < length; ++i) {
            let selectedAttr = v2xTypes[i].code === 'V2XT01' ? ' selected="selected"' : '';
            $("#v2xType").append('<option value="' + v2xTypes[i].code + '"' + selectedAttr + '>' + v2xTypes[i].codeName + '</option>');
        }

        //설치년월
        $("#insYearMon").datepicker({
            todayBtn: "linked",
            autoclose: true,
            format: "yyyy-mm",
            minViewMode: 1
        });
        $("#insYearMon").val(dateUtilsJs.currentDate('YYYY-MM-DD').substring(0, 7));
    }

    function _toggleBatterySwap() {
        if ($("#csServiceType").val() === 'CSST02') {
            $("#batterySwapBox").show();
        } else {
            $("#batterySwapBox").hide();
        }
    }

    function _search(cpId, csId) {
        //
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargingStation/" + cpId + "/" + csId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                searchData = jsonData;
                _displayCSInfo(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });

        pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 5, chargingStationJs.searchBreadkDown);
        _searchBreadkDown();
    }

    function _displayCSInfo(jsonData) {
        if (!jsonData) {
            return;
        }
        data = jsonData;
        $("#cpCsId").val(jsonData.cpId + '-' + jsonData.csId + "(" + jsonData.csUniqId + ")");
        $("#csUniqId").val(jsonData.csUniqId);
        $("#cpId").val(jsonData.cpId);
        $("#csId").val(jsonData.csId);
        $("#cpName").val(jsonData.cpName);
        $("#makerType").val(jsonData.makerType);
        $("#csKindType").val(jsonData.csKindType);
        let prodType = jsonData.prodType;
        //요금제조회
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/product/search?pageNumber=0&pageItemSize=999&standDate=" + standDate,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                if (jsonData.criteria.totalItemCount !== 0) {
					$("#prodType").append('<option value="">' + _msg.selectProduct + '</option>');
                    for (let i = 0, length = jsonData.result.length; i < length; ++i) {
                        $("#prodType").append('<option value="' + jsonData.result[i].id + '">' + jsonData.result[i].name + '</option>');
                    }
                }
                $("#prodType").val(data.prodType);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
        $("#csChanelCount").val(jsonData.csChanelCount);
        $("#csCatCode").val(jsonData.csCatCode);
        $("#useYn").val(jsonData.useYn);
        $("#csPassword").val(jsonData.csPassword);
        $("#lastCsPassword").val(jsonData.lastCsPassword || '');
        $("#electronicSupplyCapability").val(jsonData.electSupplyCapability);
        $("#serialNo").val(jsonData.serialNo);
        $("#subsidyType").val(jsonData.subsidyType);
        $("#ssdContents").val(jsonData.ssdContents);
        $("#csInstallCo").val(jsonData.csInstallCo);
        if (jsonData.insYearMon) {
            $("#insYearMon").val(jsonData.insYearMon.substring(0, 4) + "-" + jsonData.insYearMon.substring(4, 6));
        }
        $("#ocppVersion").val(jsonData.ocppVersion);
        if (jsonData.v2xType) {
            $("#v2xType").val(jsonData.v2xType);
        }
        $("#fwVer").html(jsonData.fwVer);
        $("#modelName").html(jsonData.modelName);
        $("#serialNumber").html(jsonData.serialNumber);
        if(jsonData.lastBootDate){
        	$("#lastBootDate").html(dateUtilsJs.formatDate(new Date(jsonData.lastBootDate), "YYYY-MM-DD HH:MM:SS"));
        }
        $("#brkdownYn").val(jsonData.brkdownYn);

        // 서비스 방식 및 배터리 교환형 확장정보
        $("#csServiceType").val(jsonData.csServiceType || 'CSST01');
        _toggleBatterySwap();
        let bsi = jsonData.batterySwapInfo;
        if (bsi) {
            $("#operationalStatus").val(bsi.operationalStatus);
            $("#totalSlotCount").val(bsi.totalSlotCount);
            $("#reservedSlotCount").val(bsi.reservedSlotCount);
            $("#supportedBatteryModel").val(bsi.supportedBatteryModel || '');
            $("#defaultIdToken").val(bsi.defaultIdToken || '');
            $("#defaultIdTokenType").val(bsi.defaultIdTokenType || '');
            $("#swapTimeoutSec").val(bsi.swapTimeoutSec);
            $("#minSoHThreshold").val(bsi.minSoHThreshold != null ? bsi.minSoHThreshold : '');
            $("#minSoCThreshold").val(bsi.minSoCThreshold != null ? bsi.minSoCThreshold : '');
        }
    }

    function _validate() {
        //충전소
        let cpId = $("#cpId").val().trim();
        if (cpId === '') {
            swal(_commonMsg.validationCheck, _msg.selectChargePoint, "warning");
            return false;
        }
        data.cpId = cpId;
        //충전기
        let csId = $("#csId").val().trim();
        if (csId === '') {
            swal(_commonMsg.validationCheck, _msg.inputCsId, "warning");
            return false;
        }
        data.csId = csId;
        //충전기고유ID
        data.csUniqId = data.cpId + data.csId
        //제조사
        let makerType = $("#makerType").val();
        if (makerType === '' || makerType === null) {
            swal(_commonMsg.validationCheck, _msg.selectMaker, "warning");
            return false;
        }
        data.makerType = makerType;
        //충전기유형
        let csKindType = $("#csKindType").val();
        if (csKindType === '' || csKindType === null) {
            swal(_commonMsg.validationCheck, _msg.selectCsKindType, "warning");
            return false;
        }
        data.csKindType = csKindType;
        //요금제
        let prodType = $("#prodType").val();
        if(prodType === '' || prodType === null){
            swal(_commonMsg.validationCheck, _msg.selectProduct, "warning");
            return false;
        }
        data.prodType = prodType;
        //충전장비
        data.csChanelCount = $("#csChanelCount").val();
        //급속/완속 커넥터
        let csCatCode = $("#csCatCode").val();
        if(csCatCode === '' || csCatCode === null){
            swal(_commonMsg.validationCheck, _msg.selectConnector, "warning");
            return false;
        }
        data.csCatCode = csCatCode;
        //사용유무
        let useYn = $("#useYn").val();
        if(useYn === '' || useYn === null) {
			swal(_commonMsg.validationCheck, _msg.selectUseYn, "warning");
			return false;
		}
        data.useYn = useYn;
        //충전기모델명
        data.modelTypeCode = $("#modelTypeCode").val();
        //충전기용량
        let electronicSupplyCapability = $("#electronicSupplyCapability").val();
        if(electronicSupplyCapability == 0 || electronicSupplyCapability === null){
			swal(_commonMsg.validationCheck, _msg.selectCapacity, "warning");
			return;
		}
        data.electSupplyCapability = electronicSupplyCapability;
        //설치년월
        let insYearMon = $("#insYearMon").val();
        if (!insYearMon) {
            swal(_commonMsg.validationCheck, _msg.selectMonth, "warning");
            return false;
        }
        if (insYearMon.substring(0, 1) !== "2") {
            swal(_commonMsg.validationCheck, _msg.yearMustBe2000s, "warning");
            return false;
        }
        data.insYearMon = insYearMon.replace('-', '');
        //패스워드
        let csPassword = $("#csPassword").val();
        if(csPassword === '' || csPassword === null){
			swal(_commonMsg.validationCheck, _msg.inputPassword, "warning");	
			return false;
		}
		
		data.ocppVersion = $("#ocppVersion").val();
		data.v2xType = $("#v2xType").val() || 'V2XT01';
        data.csPassword = csPassword;
        
        data.csInstallCo = $("#csInstallCo").val();
        data.brkdownYn = $("#brkdownYn").val();

        // 서비스 방식 및 배터리 교환형 확장정보
        let csServiceType = $("#csServiceType").val();
        data.csServiceType = csServiceType;
        if (csServiceType === 'CSST02') {
            let totalSlotCount = $("#totalSlotCount").val();
            if (!totalSlotCount || parseInt(totalSlotCount, 10) <= 0) {
                swal(_commonMsg.validationCheck, _msg.inputTotalSlot, "warning");
                return false;
            }
            let minSoH = $("#minSoHThreshold").val();
            let minSoC = $("#minSoCThreshold").val();
            data.batterySwapInfo = {
                operationalStatus: $("#operationalStatus").val(),
                totalSlotCount: parseInt(totalSlotCount, 10),
                reservedSlotCount: parseInt($("#reservedSlotCount").val() || '0', 10),
                supportedBatteryModel: $("#supportedBatteryModel").val(),
                defaultIdToken: $("#defaultIdToken").val(),
                defaultIdTokenType: $("#defaultIdTokenType").val(),
                swapTimeoutSec: parseInt($("#swapTimeoutSec").val() || '600', 10),
                minSoHThreshold: minSoH !== '' ? minSoH : null,
                minSoCThreshold: minSoC !== '' ? minSoC : null
            };
        } else {
            data.batterySwapInfo = null;
        }
        return true;
    }

    function _registerOnClick() {
        if (!_validate()) {
            return;
        }
        $.ajax({
            type: 'POST',
            method: 'POST',
            url: _ctx + "/ws/charger/chargingStation",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_commonMsg.successRegister, _msg.title);
                    _moveDetail(jsonData.result.cpId, jsonData.result.csId);
                } else {
                    toastr.error(_commonMsg.failRegister, _msg.title);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
                toastr.error(_commonMsg.failRegister, _msg.title);
            }
        });
    }

    function _updateOnClick() {
        if (!_validate()) {
            return;
        }
        swal({
            title: _msg.title,
            text: _msg.confirmChange,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnModify,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function () {
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + "/ws/charger/chargingStation/" + cpId + "/" + csId,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (jsonData) {
                    if (jsonData.status === 'SUCCESS') {
                        toastr.success(_commonMsg.successModify, _msg.title);
                        _moveDetail(cpId, csId);
                    } else {
                        toastr.error(_commonMsg.failModify + jsonData.result, _msg.title);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failModify, _msg.title);
                }
            });
        });
    }

    function _deleteOnClick() {
        swal({
            title: _msg.title,
            text: _msg.confirmDelete,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnDelete,
            cancelButtonText: _msg.btnCancel,
        }, function () {
            $.ajax({
                type: 'GET',
                method: 'GET',
                url: _ctx + "/ws/charger/status/chargerList/" + searchData.cpId + "-" + searchData.csId ,
                contentType: "application/json",
                dataType: 'json',
                success: function (jsonData) {
                    let charging = false;
                    for (let i = 0, length = jsonData.result.length; i < length; i++) {
                        if (jsonData.result[i].csStatCode === 'CHRS04') {
                            charging = true;
                            break;
                        }
                    }
                    if (charging) {
                        toastr.error(_msg.cannotDeleteInUse, _msg.title);
                    } else {
                        _deleteOn();
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    //
                	parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failDelete, _msg.title);
                }
            });
        });
    }

    function _deleteOn() {
        $.ajax({
            type: 'DELETE',
            method: 'DELETE',
            url: _ctx + "/ws/charger/chargingStation/" + searchData.cpId + "/" + searchData.csId,
            contentType: "application/json",
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    swal({
                        title: _msg.title,
                        text: _msg.deleteComplete,
                        type: "success",
                        showCancelButton: false,
                        confirmButtonText: _msg.btnConfirm,
                    }, function () {
                        _listOnClick();
                    });
                } else {
                    toastr.error(jsonData.result, _msg.title);
                }
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
                toastr.error(_commonMsg.failDelete, _msg.title);
            }
        });
    }

    function _selectedChargePoint(param) {
        $("#cpId").val(param.cpId);
        $("#cpName").val(param.cpName);
        console.log(param);
        if(param.useYn == 'N'){
        	$("#useYn").val("N");
        	$("#useYn").attr("disabled","disabled");
        }else{
        	$("#useYn").val("Y");
        	$("#useYn").removeAttr("disabled");
        }
        _maxCsId(param.cpId);
    }

    function _maxCsId(value) {
        $.ajax({
            type: 'GET',
            method: 'GET',
            url: _ctx + "/ws/charger/maxCpId/" + value,
            contentType: "application/json",
            dataType: 'json',
            success: function (jsonData) {
                $('#csId').val(jsonData.result);
                $("#csUniqId").val(value + "-" + jsonData.result);
            },
            error: function (xhRequest, ErrorText, thrownError) {
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }
    
    function _listOnClick() {
        //
        let param = '?pageNumber=' + queryString.pageNumber;
        param += "&pageItemSize=" + queryString.pageItemSize;
        param += "&makerType=" + queryString.makerType;
        param += "&csKindType=" + queryString.csKindType;
        param += "&csServiceType=" + (queryString.csServiceType || '');
        param += "&sType=" + queryString.sType;
        param += "&searchKey=" + queryString.searchKey;

        if (queryString.pageItemSize) {
            self.location = _ctx + "/charger/chargingStation/list" + param;
        } else {
            self.location = _ctx + "/charger/chargingStation/list";
        }
    }
    
    function _moveDetail(cpId, csId) {
        //
        let param = '&pageNumber=' + queryString.pageNumber;
        param += "&pageItemSize=" + queryString.pageItemSize;
        param += "&makerType=" + queryString.makerType;
        param += "&csKindType=" + queryString.csKindType;
        param += "&csServiceType=" + (queryString.csServiceType || '');
        param += "&sType=" + queryString.sType;
        param += "&searchKey=" + queryString.searchKey;
        if (queryString.pageItemSize) {
            self.location = _ctx + "/charger/chargingStation/detail?cpId=" + cpId + "&csId=" + csId + param;
        } else {
            self.location = _ctx + "/charger/chargingStation/detail?cpId=" + cpId + "&csId=" + csId;
        }
    }

    function _searchBreadkDown() {
        //
        $("#tBodyList").empty();
        let html = '<tr style="text-align:center;">';
        html += '<td colspan="9">' + _commonMsg.searching + '</td>';
        $("#tBodyList").append(html);

        let paging = pageInfoJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        param += "&cpId=" + cpId;
        param += "&csId=" + csId;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/breakdown/list" + param,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayBreakDown(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            	parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayBreakDown(jsonData) {
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
        //$("#btnBreakdownList").show();
        let result = jsonData.result;
        let noIndex = (pageInfoJs.getPaging().pageNumber - 1) * pageInfoJs.getPaging().pageItemSize + 1;
        for (let i = 0, length = result.length; i < length; ++i) {
            html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td><a href="#" onclick="chargingStationJs.moveBreakDownDetail(\'' + result[i].id + '\')">' + result[i].id + '</a></td>';
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
    
    function _moveBreakDownDetail(id) {
        //
        let param = "?id=" + id;
        param += "&cpId=" + cpId;
        param += "&csId=" + csId;
        param += "&breakdownStatus=";
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;
        
        parent.layerJs.fn_moveMenu('20000204', _msg.menuBreakdown, '/evAdmin/charger/breakdown/detail' + param, 'THIS', true);
    }
    
    function _moveBreakdownListOnClick() {
		let param = '?cpId=' + cpId;
        param += "&csId=" + csId;
        let paging = pageInfoJs.getPaging();
        param += "&pageNumber=" + (paging.pageNumber - 1) + "&pageItemSize=" + paging.pageItemSize;

		parent.layerJs.fn_moveMenu('20000204', _msg.menuBreakdown, '/evAdmin/charger/breakdown/list' + param, 'THIS', true);
	}


    return {
        init: _init,
        selectedChargePoint: _selectedChargePoint,
        searchBreadkDown: _searchBreadkDown,
        moveBreakDownDetail: _moveBreakDownDetail,
    };
}();