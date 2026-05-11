/**
 * 충전관리 - 충전기 제어 (개발자용 - 27개 OCPP 1.6 명령)
 */
let devControlJs = function () {
    "use strict";

    let data = {
        searchCond: {}
    };

    let idxArr = [];

    function _init() {
        _initData();
        _initEvent();
        if (queryString.cpId && queryString.csId) {
            $("#btnSearch").trigger("click");
        }
    }

    function _initData() {
        if (queryString.cpId && queryString.csId) {
            $("#sWord").val(queryString.cpId + queryString.csId);
        }
    }

    function _toUTC(dateStr) {
        if (!dateStr) return dateStr;
        var t = new Date(dateStr);
        return t.getUTCFullYear() + '-' +
            String(t.getUTCMonth() + 1).padStart(2, '0') + '-' +
            String(t.getUTCDate()).padStart(2, '0') + 'T' +
            String(t.getUTCHours()).padStart(2, '0') + ':' +
            String(t.getUTCMinutes()).padStart(2, '0') + ':' +
            String(t.getUTCSeconds()).padStart(2, '0') + 'Z';
    }

    function _initEvent() {
        $("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
        $('#date1').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });

        // 명령 목록 드롭다운
        var commands = ocpp16CommandDevJs.types();
        for (var i = 0; i < commands.length; i++) {
            $("#ocppCommandType").append(
                '<option value="' + commands[i].value + '">' + commands[i].name + '</option>'
            );
        }

        // 충전기 검색
        $("#btnSearch").click(function () {
            $("#date1").val(dateUtilsJs.currentDate("YYYY-MM-DD"));
            _searchEnvInfoOnClick();
        });
        $("#sWord").keypress(function (event) {
            if (event.keyCode === 13) $("#btnSearch").trigger("click");
        });

        // 이력 검색
        $("#btnLogSearch").click(function () {
            _searchLogClick();
        });
        $("#pagingNum, #csCableChn").change(function () {
            _searchLogClick();
        });
        $("#date1").change(function () {
            var date1 = $("#date1").val();
            if (data.searchCond.fromDate !== date1) _searchLogClick();
        });

        // 제조사
        var makerTypes = parent.commonCodeJs.getCodesByParentCode('CHMK00');
        $("#makerType").append('<option value="">' + _msg.selectPlaceholder + '</option>');
        for (var i = 0; i < makerTypes.length; i++) {
            $("#makerType").append('<option value="' + makerTypes[i].code + '">' + makerTypes[i].codeName + '</option>');
        }

        // 명령 전환
        $("#ocppCommandType").change(function () {
            _changeOcppCommand(this);
        });

        // 명령 전송
        $("#btnCommand").click(function () {
            _sendCommandOnClick();
        });

        // 스케줄 기간 추가
        $("#btnAddChargingSchedulePeriod").click(function () {
            _addChargingSchedulePeriod();
        });

        // RemoteStart 스마트충전 토글
        $("#RemoteStartTransactionValue3").change(function () {
            var isO = $(this).val() === 'O';
            $("#RemoteStartSmartCharging").toggle(isO);
            if (!isO) $("#RemoteStartSmartCharging").hide();
        });

        // GetConfiguration 전체선택
        $("#getConfigSelectAll").change(function () {
            $('input[name="GetConfigurationCheckBox"]').prop('checked', $(this).prop('checked'));
        });

        // SendLocalList idTag 추가/삭제
        $("#btnAddLocalListIdTag").click(function () {
            var idTag = $("#SendLocalListNewIdTag").val().trim();
            if (idTag) {
                $("#SendLocalListValue3").append('<option value="' + idTag + '">' + idTag + '</option>');
                $("#SendLocalListNewIdTag").val('');
            }
        });
        $("#btnRemoveLocalListIdTag").click(function () {
            $("#SendLocalListValue3 option:selected").remove();
        });

        // 결과 복사
        $("#btnCopyResult").click(function () {
            var text = $("#resultBox").text();
            if (text) {
                navigator.clipboard.writeText(text).then(function () {
                    toastr.success(_msg.copied, _msg.copyResult);
                });
            }
        });
    }

    /* ── 충전기 검색 ── */
    function _searchEnvInfoOnClick() {
        var sWord = $("#sWord").val().replace(/-/g, '').trim();
        if (!sWord || sWord === '') {
            toastr.warning(_commonMsg.searchInputReq, _msg.title);
            return;
        }
        data.searchCond.csId = sWord;
        if (data.searchCond.csId.length !== 8) {
            $("#csUniqIdtr").html("");
            toastr.warning(_msg.csIdLength8, _msg.csIdLabel);
            return;
        }
        data.searchCond.cpId = data.searchCond.csId.substring(0, 6);
        data.searchCond.csId = data.searchCond.csId.substring(6);
        $("#sWord").val(sWord);
        _searchEnvInfo();
        _searchLogClick();
    }

    function _searchEnvInfo() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/chargingStation/" + data.searchCond.cpId + "/" + data.searchCond.csId,
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData) {
                    _displayEnvInfo(jsonData);
                } else {
                    toastr.warning(_msg.chargerNotFound, _msg.title);
                }
            },
            error: function () {}
        });
    }

    function _displayEnvInfo(jsonData) {
        if (!jsonData) return;
        $("#csUniqIdtr").html(jsonData.csUniqId + "(" + jsonData.cpId + "-" + jsonData.csId + ")");
        $("#makerType").val(jsonData.makerType);
        $("#fmwVer").val(jsonData.fwVer);
        $("#cpNametr").html(jsonData.cpName);
        $("#protocolType").val(jsonData.ocppVersion);
    }

    /* ── 명령 전환 ── */
    function _changeOcppCommand(target) {
        $('div[name=OCPPCommand]').hide();
        var selectedValue = target.value;
        if (selectedValue) $("#" + selectedValue).show();
    }

    /* ── 명령 전송 ── */
    function _sendCommandOnClick() {
        var ocppCommandType = $("#ocppCommandType").val();
        if (!ocppCommandType) {
            toastr.warning(_msg.selectCommand, _msg.title);
            return;
        }
        if (!data.searchCond.cpId) {
            toastr.warning(_msg.searchChargerFirst, _msg.title);
            return;
        }

        var params = [];
        switch (ocppCommandType) {
            case 'Reset':
                params[0] = $("#ResetValue").val();
                break;
            case 'UnlockConnector':
                params[0] = $("#UnlockConnectorValue").val().trim();
                break;
            case 'RemoteStopTransaction':
                params[0] = Number($("#RemoteStopTransactionValue").val());
                break;
            case 'CancelReservation':
                params[0] = $("#CancelReservationValue").val().trim();
                break;
            case 'CertificateSigned':
                params[0] = $("#CertificateSignedValue").val().trim();
                break;
            case 'GetInstalledCertificateIds':
                params[0] = $("#GetInstalledCertificateIdsValue").val().trim();
                break;
            case 'ChangeAvailability':
                params[0] = $("#ChangeAvailabilityValue1").val().trim();
                params[1] = $("#ChangeAvailabilityValue2").val();
                break;
            case 'ChangeConfiguration':
                params[0] = $("#ChangeConfigurationValue1").val().trim();
                params[1] = $("#ChangeConfigurationValue2").val().trim();
                break;
            case 'TriggerMessage':
            case 'ExtendedTriggerMessage':
                params[0] = $("#" + ocppCommandType + "Value1").val();
                params[1] = $("#" + ocppCommandType + "Value2").val().trim();
                break;
            case 'InstallCertificate':
                params[0] = $("#InstallCertificateValue1").val();
                params[1] = $("#InstallCertificateValue2").val().trim();
                break;
            case 'SetChargeLimit':
                params[0] = $("#SetChargeLimitValue1").val().trim();
                params[1] = $("#SetChargeLimitValue2").val().trim();
                break;
            case 'RemoteStartTransaction':
                params[0] = $("#RemoteStartTransactionValue1").val().replace(/-/g, '').trim();
                params[1] = $("#RemoteStartTransactionValue2").val().trim();
                if ($("#RemoteStartTransactionValue3").val() === "O") {
                    params[2]  = $("#SetChargingProfileValue1").val().trim();
                    params[3]  = $("#SetChargingProfileValue2").val().trim();
                    params[4]  = $("#SetChargingProfileValue3").val().trim();
                    params[5]  = $("#SetChargingProfileValue4").val();
                    params[6]  = $("#SetChargingProfileValue5").val();
                    params[7]  = $("#SetChargingProfileValue6").val().trim();
                    params[8]  = $("#SetChargingProfileValue7").val();
                    params[9]  = _toUTC($("#SetChargingProfileValue8").val().trim());
                    params[10] = _toUTC($("#SetChargingProfileValue9").val().trim());
                    params[11] = $("#SetChargingProfileValue10").val();
                    params[12] = $("#SetChargingProfileValue11").val().trim();
                    params[13] = _toUTC($("#SetChargingProfileValue12").val().trim());
                    params[14] = $("#SetChargingProfileValue13").val().trim();
                    params[15] = _readSchedulePeriods("SetChargingProfileValue14");
                }
                break;
            case 'DataTransfer':
                params[0] = $("#DataTransferValue1").val().trim();
                params[1] = $("#DataTransferValue2").val().trim();
                params[2] = $("#DataTransferValue3").val().trim();
                break;
            case 'GetCompositeSchedule':
                params[0] = $("#GetCompositeScheduleValue1").val().trim();
                params[1] = $("#GetCompositeScheduleValue2").val().trim();
                params[2] = $("#GetCompositeScheduleValue3").val();
                break;
            case 'SetNetworkProfile':
                params[0] = $("#SetNetworkProfileValue1").val().trim();
                params[1] = $("#SetNetworkProfileValue2").val().trim();
                params[2] = $("#SetNetworkProfileValue3").val().trim();
                break;
            case 'ClearChargingProfile':
                params[0] = $("#ClearChargingProfileValue1").val().trim();
                params[1] = $("#ClearChargingProfileValue2").val().trim();
                params[2] = $("#ClearChargingProfileValue3").val().trim();
                params[3] = $("#ClearChargingProfileValue4").val();
                break;
            case 'DeleteCertificate':
                params[0] = $("#DeleteCertificateValue1").val();
                params[1] = $("#DeleteCertificateValue2").val().trim();
                params[2] = $("#DeleteCertificateValue3").val().trim();
                params[3] = $("#DeleteCertificateValue4").val().trim();
                break;
            case 'UpdateFirmware':
                params[0] = $("#UpdateFirmwareValue1").val().trim();
                params[1] = $("#UpdateFirmwareValue2").val().trim();
                params[2] = $("#UpdateFirmwareValue3").val().trim();
                params[3] = $("#UpdateFirmwareValue4").val().trim();
                break;
            case 'GetDiagnostics':
                params[0] = $("#GetDiagnosticsValue1").val().trim();
                params[1] = $("#GetDiagnosticsValue2").val().trim();
                params[2] = $("#GetDiagnosticsValue3").val().trim();
                params[3] = $("#GetDiagnosticsValue4").val().trim();
                params[4] = $("#GetDiagnosticsValue5").val().trim();
                break;
            case 'ReserveNow':
                params[0] = $("#ReserveNowValue1").val().trim();
                params[1] = $("#ReserveNowValue2").val().trim();
                params[2] = $("#ReserveNowValue3").val().trim();
                params[3] = $("#ReserveNowValue4").val().trim();
                params[4] = $("#ReserveNowValue5").val().trim();
                break;
            case 'GetConfiguration':
                $('input[name="GetConfigurationCheckBox"]:checked').each(function () {
                    params.push($(this).attr('meta-value'));
                });
                break;
            case 'SendLocalList':
                params[0] = $("#SendLocalListValue1").val().trim();
                params[1] = $("#SendLocalListValue2").val();
                var opts = $("#SendLocalListValue3 option");
                var idTags = [];
                opts.each(function () { idTags.push($(this).val()); });
                params[2] = idTags;
                break;
            case 'SetChargingProfile':
                params[0]  = $("#SetChargingProfileValue1").val().trim();
                params[1]  = $("#SetChargingProfileValue2").val().trim();
                params[2]  = $("#SetChargingProfileValue3").val().trim();
                params[3]  = $("#SetChargingProfileValue4").val();
                params[4]  = $("#SetChargingProfileValue5").val();
                params[5]  = $("#SetChargingProfileValue6").val().trim();
                params[6]  = $("#SetChargingProfileValue7").val();
                params[7]  = _toUTC($("#SetChargingProfileValue8").val().trim());
                params[8]  = _toUTC($("#SetChargingProfileValue9").val().trim());
                params[9]  = $("#SetChargingProfileValue10").val();
                params[10] = $("#SetChargingProfileValue11").val().trim();
                params[11] = _toUTC($("#SetChargingProfileValue12").val().trim());
                params[12] = $("#SetChargingProfileValue13").val().trim();
                params[13] = _readSchedulePeriods("SetChargingProfileValue14");
                break;
            case 'GetLog':
                params[0] = $("#GetLogValue1").val();
                params[1] = $("#GetLogValue2").val().trim();
                params[2] = $("#GetLogValue3").val().trim();
                params[3] = $("#GetLogValue4").val().trim();
                params[4] = $("#GetLogValue5").val().trim();
                params[5] = $("#GetLogValue6").val().trim();
                params[6] = $("#GetLogValue7").val().trim();
                break;
            case 'SignedUpdateFirmware':
                params[0] = $("#SignedUpdateFirmwareValue1").val().trim();
                params[1] = $("#SignedUpdateFirmwareValue2").val().trim();
                params[2] = $("#SignedUpdateFirmwareValue3").val().trim();
                params[3] = $("#SignedUpdateFirmwareValue4").val().trim();
                params[4] = $("#SignedUpdateFirmwareValue5").val().trim();
                params[5] = $("#SignedUpdateFirmwareValue6").val().trim();
                params[6] = $("#SignedUpdateFirmwareValue7").val().trim();
                params[7] = $("#SignedUpdateFirmwareValue8").val().trim();
                break;
            case 'GetLocalListVersion':
            case 'ClearCache':
                break;
        }

        var valueStr = ocpp16CommandDevJs.makeParam(ocppCommandType, params);
        _sendCommand(ocppCommandType, valueStr);
    }

    function _readSchedulePeriods(tbodyId) {
        var result = [];
        var rows = $("#" + tbodyId).children();
        rows.each(function () {
            var cols = $(this).children();
            result.push({
                startPeriod: Number(cols.eq(0).find('input').val()),
                numberPhases: Number(cols.eq(1).find('input').val()),
                limit: Number(cols.eq(2).find('input').val())
            });
        });
        return result;
    }

    function _sendCommand(ocppCommandType, valueStr) {
        var param = {
            param1: ocppCommandType,
            param2: valueStr
        };

        swal({
            title: _msg.title,
            text: _msg.confirmSendCommand.replace('{0}', ocppCommandType),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnSend,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: true
        }, function () {
            $("#btnCommand").prop("disabled", true);
            $.ajax({
                type: 'PUT',
                url: _ctx + '/ws/cmd/ocpp16/' + data.searchCond.cpId + "-" + data.searchCond.csId,
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(param),
                success: function (jsonData) {
                    if (jsonData.status === 'SUCCESS') {
                        toastr.success(_commonMsg.successSend, _msg.title);
                        _showResult({ status: "SUCCESS", command: ocppCommandType, payload: valueStr }, true);
                    } else {
                        toastr.error(_commonMsg.failSend, _msg.title);
                        _showResult({ status: "FAIL", command: ocppCommandType, payload: valueStr }, false);
                    }
                },
                error: function (xhRequest) {
                    parent.layerJs.fn_exception(xhRequest);
                    toastr.error(_commonMsg.failSend, _msg.title);
                    _showResult({ status: "ERROR", command: ocppCommandType, error: xhRequest.statusText }, false);
                },
                complete: function () {
                    $("#btnCommand").prop("disabled", false);
                }
            });
        });
    }

    /* ── 결과 표시 ── */
    function _showResult(data, success) {
        var box = $("#resultBox");
        box.text(JSON.stringify(data, null, 2));
        box.show();
        box.css("border-color", success ? "#1ab394" : "#ed5565");
        box.css("background-color", success ? "#f8fff8" : "#fff8f8");
        $("#resultArea").show();
    }

    /* ── 스케줄 기간 관리 ── */
    function _addChargingSchedulePeriod() {
        var html = '<tr>';
        html += '<td><input type="number" min="0" placeholder="0" class="form-control input-sm" /></td>';
        html += '<td><input type="number" min="1" placeholder="3" class="form-control input-sm" /></td>';
        html += '<td><input type="number" min="0" step="0.1" placeholder="0.1" class="form-control input-sm" /></td>';
        html += '<td><button type="button" class="btn btn-xs btn-danger" onclick="devControlJs.removeChargingSchedulePeriod(this);">' + _msg.btnDelete + '</button></td>';
        html += '</tr>';
        $("#SetChargingProfileValue14").append(html);
    }

    function _removeChargingSchedulePeriod(that) {
        $(that).closest('tr').remove();
    }

    /* ── 이력 검색 (기존 OCPP16Control과 동일) ── */
    function _searchLogClick() {
        if (!data.searchCond.cpId) return;
        idxArr = [0];
        pageInfo2Js.init('pageInfo2Js', 'pagingUl', $("#pagingNum").val(), devControlJs.searchLog);

        data.searchCond.fromDate = "";
        data.searchCond.toDate = "";
        data.searchCond.csCableChn = "";
        data.searchCond.connectorId = "";
        data.searchCond.idx = "";

        if ($("#date1").val()) {
            data.searchCond.fromDate = $("#date1").val();
            data.searchCond.toDate = data.searchCond.fromDate;
        }
        data.searchCond.csCableChn = $("#csCableChn").val();
        data.searchCond.connectorId = data.searchCond.csCableChn;

        _searchKevitLog();
    }

    function _searchLog() {
        _searchKevitLog();
    }

    function _searchKevitLog() {
        $("#KEVITtBodyList").empty();
        $("#KEVITtBodyList").append('<tr style="text-align:center;"><td colspan="18">' + _commonMsg.searching + '</td></tr>');

        var paging = pageInfo2Js.getPaging();
        var param = "?pageNumber=0&pageItemSize=" + paging.pageItemSize;
        param += "&evseId=" + data.searchCond.csCableChn;
        param += "&cpId=" + data.searchCond.cpId;
        param += "&csId=" + data.searchCond.csId;
        if (paging.pageNumber - 1 === 0 && data.searchCond.fromDate !== '') {
            param += "&fromDate=" + formmatUtilsJs.removeDash(data.searchCond.fromDate);
            param += "&toDate=" + formmatUtilsJs.removeDash(data.searchCond.toDate);
        } else {
            param += "&csStatusId=" + idxArr[paging.pageNumber - 1];
        }

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/charger/logList" + param,
            dataType: 'json',
            success: function (jsonData) {
                _displayKevitLog(jsonData);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _displayKevitLog(jsonData) {
        pageInfo2Js.setCount(jsonData.result.length);
        $("#KEVITtBodyList").empty();
        if (jsonData.result.length === 0) {
            $("#KEVITtBodyList").append('<tr style="text-align:center;"><td colspan="18">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        var result = jsonData.result;
        var noIndex = (pageInfo2Js.getPaging().pageNumber - 1) * pageInfo2Js.getPaging().pageItemSize + 1;

        for (var i = 0; i < result.length; i++) {
            var paging = pageInfo2Js.getPaging();
            var idx = paging.pageNumber;
            if (!idxArr[idx]) idxArr[idx] = result[result.length - 1].csStatusId;

            var html = '<tr>';
            html += '<td>' + (i + noIndex) + '</td>';
            var cdt = new Date(result[i].updateDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            cdt = new Date(result[i].infoCollDate);
            html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            html += '<td>' + (result[i].rechargingId ? result[i].rechargingId : "") + '</td>';
            html += '<td>' + result[i].cpId + "-" + result[i].csId + '</td>';
            html += '<td>' + result[i].evseId + '</td>';
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csStatCode) + '</td>';
            html += '<td>' + parent.commonCodeJs.getChargerCableStatusDesc(result[i].csCableStatus) + '</td>';
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].csErrorStatus) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].cuEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].caEleEnerge) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChAmont) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].instChSum) + '</td>';
            html += '<td>' + formmatUtilsJs.commaFormat(result[i].chSum) + '</td>';
            if (result[i].chStartDate) {
                cdt = new Date(result[i].chStartDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            if (result[i].chEndDate) {
                cdt = new Date(result[i].chEndDate);
                html += '<td>' + formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(cdt), 'YYYY-MM-DD HH:MM:SS') + '</td>';
            } else {
                html += '<td></td>';
            }
            html += '<td>' + parent.commonCodeJs.getCodeNameBySubCode(result[i].eventCode) + '</td>';
            html += '<td>' + (result[i].cutCardNo ? result[i].cutCardNo : "") + '</td>';
            html += '</tr>';
            $("#KEVITtBodyList").append(html);
        }
    }

    return {
        init: _init,
        searchLog: _searchLog,
        removeChargingSchedulePeriod: _removeChargingSchedulePeriod
    };
}();
