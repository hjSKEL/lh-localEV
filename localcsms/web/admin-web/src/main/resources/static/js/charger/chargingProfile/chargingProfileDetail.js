/**
 * 충전 프로파일 - 상세/등록 페이지
 */
let chargingProfileDetailJs = function () {
    "use strict";

    let data = {
        profileId : 0,
        cpId      : '',
        csId      : '',
        isNew     : true
    };

    function _init() {
        data.profileId = parseInt($("#hidProfileId").val()) || 0;
        data.cpId      = $("#hidCpId").val();
        data.csId      = $("#hidCsId").val();
        data.isNew     = $("#hidIsNew").val() === 'true';

        _initEvent();

        if (!data.isNew && data.profileId > 0) {
            _loadDetail();
        }
    }

    function _initEvent() {
        $("#btnList").click(function () {
            let url = _ctx + "/charger/chargingProfile/list";
            if (data.cpId) url += "?cpId=" + data.cpId;
            if (data.csId) url += (data.cpId ? "&" : "?") + "csId=" + data.csId;
            window.location.href = url;
        });

        $("#btnSave").click(function () {
            _save();
        });

        $("#btnDelete").click(function () {
            _delete();
        });

        // Kind = Recurring 일 때만 반복종류 행 노출
        $("#inputKind").change(function () {
            if ($(this).val() === 'CHKD02') {
                $("#rowRecurrencyKind").show();
            } else {
                $("#rowRecurrencyKind").hide();
                $("#inputRecurrencyKind").val('');
            }
        });
    }

    function _loadDetail() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/chargingProfile/" + data.profileId
                + "?cpId=" + data.cpId + "&csId=" + data.csId,
            dataType: 'json',
            success: function (profile) {
                if (!profile) {
                    _showAlert(false, _commonMsg.searchError, _msg.profileNotFound);
                    return;
                }
                _fillForm(profile);
            },
            error: function () {
                _showAlert(false, _commonMsg.systemError, _commonMsg.queryError);
            }
        });
    }

    function _fillForm(p) {
        $("#infoProfileId").text(p.profileId);
        $("#inputCpId").val(p.cpId);
        $("#inputCsId").val(p.csId);
        $("#inputEvseId").val(p.evseId != null ? p.evseId : 0);
        $("#inputStackLevel").val(p.stackLevel != null ? p.stackLevel : 0);

        if (p.purpose) {
            // GET returns CHPP code directly (toMap conversion in Resource)
            $("#inputPurpose").val(p.purpose);
        }
        if (p.kind) {
            $("#inputKind").val(p.kind);
            if (p.kind === 'CHKD02') {
                $("#rowRecurrencyKind").show();
            }
        }
        if (p.recurrencyKind) {
            $("#inputRecurrencyKind").val(p.recurrencyKind);
        }
        if (p.validFrom) $("#inputValidFrom").val(_toDatetimeLocal(p.validFrom));
        if (p.validTo)   $("#inputValidTo").val(_toDatetimeLocal(p.validTo));
        $("#inputRechargingId").val(p.rechargingId || '');
        if (p.csStatus)  $("#infoCsStatus").text(p.csStatus);

        if (p.scheduleListJson) {
            try {
                let parsed = JSON.parse(p.scheduleListJson);
                $("#inputScheduleJson").val(JSON.stringify(parsed, null, 2));
            } catch (e) {
                $("#inputScheduleJson").val(p.scheduleListJson);
            }
        }

        if (p.writer) {
            let regDt = p.writer.registrationDate
                ? new Date(p.writer.registrationDate).toLocaleString('ko-KR') : '-';
            let updDt = p.writer.updateDate
                ? new Date(p.writer.updateDate).toLocaleString('ko-KR') : '-';
            $("#infoRegDt").text(regDt);
            $("#infoRegId").text(p.writer.regUserId || '-');
            $("#infoUpdDt").text(updDt);
            $("#infoUpdId").text(p.writer.updUserId || '-');
        }
    }

    function _buildPayload() {
        let cpId       = $("#inputCpId").val().trim();
        let csId       = $("#inputCsId").val().trim();
        let purpose    = $("#inputPurpose").val();
        let kind       = $("#inputKind").val();
        let stackLevel = parseInt($("#inputStackLevel").val()) || 0;

        if (!cpId || cpId.length !== 6) {
            _showAlert(false, _commonMsg.inputError, _msg.inputCpId6);
            return null;
        }
        if (!csId || csId.length !== 2) {
            _showAlert(false, _commonMsg.inputError, _msg.inputCsId2);
            return null;
        }
        if (!purpose) {
            _showAlert(false, _commonMsg.inputError, _msg.selectPurpose);
            return null;
        }
        if (!kind) {
            _showAlert(false, _commonMsg.inputError, _msg.selectKind);
            return null;
        }

        // Schedule JSON 유효성 검사
        let scheduleJsonStr = $("#inputScheduleJson").val().trim();
        if (scheduleJsonStr) {
            try {
                JSON.parse(scheduleJsonStr);
            } catch (e) {
                _showAlert(false, _commonMsg.inputError, _msg.invalidScheduleJson + ": " + e.message);
                return null;
            }
        }

        let payload = {
            cpId            : cpId,
            csId            : csId,
            evseId          : parseInt($("#inputEvseId").val()) || 0,
            stackLevel      : stackLevel,
            purpose         : purpose,
            kind            : kind,
            recurrencyKind  : $("#inputRecurrencyKind").val() || null,
            validFrom       : _fromDatetimeLocal($("#inputValidFrom").val()),
            validTo         : _fromDatetimeLocal($("#inputValidTo").val()),
            rechargingId    : $("#inputRechargingId").val().trim() || null,
            scheduleListJson: scheduleJsonStr || null
        };

        return payload;
    }

    function _save() {
        let payload = _buildPayload();
        if (!payload) return;

        let isNew  = data.isNew;
        let method = isNew ? 'POST' : 'PUT';
        let url    = _ctx + "/ws/chargingProfile" + (isNew ? '' : '/' + data.profileId);

        $("#btnSave").prop('disabled', true).text(_msg.saving);

        $.ajax({
            type: method,
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    if (isNew) {
                        let newId = jsonData.result;
                        toastr.success(_msg.profileRegistered.replace('{0}', newId));
                        window.location.href = _ctx + "/charger/chargingProfile/detail?profileId=" + newId
                            + "&cpId=" + payload.cpId + "&csId=" + payload.csId;
                    } else {
                        toastr.success(_msg.profileModified);
                        _loadDetail();
                    }
                } else {
                    _showAlert(false, _msg.saveFail, jsonData.result || _commonMsg.failSave);
                }
            },
            error: function () {
                _showAlert(false, _commonMsg.systemError, _commonMsg.requestError);
            },
            complete: function () {
                $("#btnSave").prop('disabled', false).text(_msg.btnSave);
            }
        });
    }

    function _delete() {
        if (!confirm(_msg.confirmDeleteProfile.replace('{0}', data.profileId))) return;

        $.ajax({
            type: 'DELETE',
            url: _ctx + "/ws/chargingProfile/" + data.profileId
                + "?cpId=" + data.cpId + "&csId=" + data.csId,
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.profileDeleted);
                    window.location.href = _ctx + "/charger/chargingProfile/list"
                        + "?cpId=" + data.cpId + "&csId=" + data.csId;
                } else {
                    _showAlert(false, _msg.deleteFail, _commonMsg.failDelete);
                }
            },
            error: function () {
                _showAlert(false, _commonMsg.error, _commonMsg.requestError);
            }
        });
    }

    function _showAlert(isSuccess, title, message) {
        let alertEl = $("#alertArea");
        alertEl.removeClass("alert-success alert-danger")
            .addClass(isSuccess ? "alert-success" : "alert-danger")
            .show();
        $("#alertTitle").text(title);
        $("#alertMessage").text(message);
    }

    function _toDatetimeLocal(timestamp) {
        if (!timestamp) return '';
        let d = new Date(timestamp);
        let pad = function (n) { return n < 10 ? '0' + n : '' + n; };
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate())
            + 'T' + pad(d.getHours()) + ':' + pad(d.getMinutes());
    }

    function _fromDatetimeLocal(val) {
        if (!val) return null;
        return new Date(val).getTime();
    }

    return {
        init: _init
    };
}();
