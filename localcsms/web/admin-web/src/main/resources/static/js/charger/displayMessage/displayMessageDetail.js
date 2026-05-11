/**
 * 충전기 디스플레이 메시지 - 상세/등록 페이지
 */
let displayMessageDetailJs = function () {
    "use strict";

    let data = {
        messageId : 0,
        cpId      : '',
        csId      : '',
        isNew     : true
    };

    function _init() {
        data.messageId = parseInt($("#hidMessageId").val()) || 0;
        data.cpId      = $("#hidCpId").val();
        data.csId      = $("#hidCsId").val();
        data.isNew     = $("#hidIsNew").val() === 'true';

        _initEvent();

        if (!data.isNew && data.messageId > 0) {
            _loadDetail();
        } else {
            // 신규 등록 - 빈 내용 행 1개 추가
            _addContentRow('', 'UTF8', '');
        }
    }

    function _initEvent() {
        $("#btnList").click(function () {
            let url = _ctx + "/charger/displayMessage/list";
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

        $("#btnAddContent").click(function () {
            _addContentRow('', 'UTF8', '');
        });
    }

    function _loadDetail() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/displayMessage/" + data.messageId
                + "?cpId=" + data.cpId + "&csId=" + data.csId,
            dataType: 'json',
            success: function (msg) {
                if (!msg) {
                    _showAlert(false, _msg.queryError, _msg.messageNotFound);
                    return;
                }
                _fillForm(msg);
            },
            error: function (xhRequest) {
                _showAlert(false, _commonMsg.error, _commonMsg.queryError);
            }
        });
    }

    function _fillForm(msg) {
        $("#infoMessageId").text(msg.messageId);
        $("#inputCpId").val(msg.cpId);
        $("#inputCsId").val(msg.csId);
        $("#inputPriority").val(msg.priority);
        $("#inputStatus").val(msg.status);
        if (msg.startDate) $("#inputStartDate").val(_toDatetimeLocal(msg.startDate));
        if (msg.endDate)   $("#inputEndDate").val(_toDatetimeLocal(msg.endDate));
        $("#inputRcId").val(msg.rcId || '');
        $("#inputDisplayName").val(msg.displayName || '');
        $("#inputDisplayInstance").val(msg.displayInstance || '');
        if (msg.displayEvseId != null) $("#inputDisplayEvseId").val(msg.displayEvseId);
        if (msg.displayConnId != null) $("#inputDisplayConnId").val(msg.displayConnId);
        if (msg.csStatus) $("#infoCsStatus").text(msg.csStatus);

        if (msg.writer) {
            let regDt = msg.writer.registrationDate
                ? new Date(msg.writer.registrationDate).toLocaleString('ko-KR') : '-';
            let updDt = msg.writer.updateDate
                ? new Date(msg.writer.updateDate).toLocaleString('ko-KR') : '-';
            $("#infoRegDt").text(regDt);
            $("#infoRegId").text(msg.writer.regUserId || '-');
            $("#infoUpdDt").text(updDt);
            $("#infoUpdId").text(msg.writer.updUserId || '-');
        }

        $("#tBodyContents").empty();
        if (msg.contents && msg.contents.length > 0) {
            for (let i = 0; i < msg.contents.length; i++) {
                let c = msg.contents[i];
                _addContentRow(c.msgLanguage, c.msgFormat, c.msgContent);
            }
        } else {
            _addContentRow('', 'UTF8', '');
        }
    }

    function _addContentRow(lang, format, content) {
        let formatOptions = ['ASCII', 'HTML', 'URI', 'UTF8', 'QRCODE'].map(function (f) {
            return '<option value="' + f + '"' + (f === format ? ' selected' : '') + '>' + f + '</option>';
        }).join('');

        let row = '<tr>'
            + '<td><input type="text" class="form-control input-sm content-lang" maxlength="2" value="'
            + _escape(lang) + '" placeholder="ko" style="width:60px; text-align:center;"></td>'
            + '<td><select class="form-control input-sm content-format">' + formatOptions + '</select></td>'
            + '<td><input type="text" class="form-control input-sm content-text" maxlength="512" value="'
            + _escape(content) + '" placeholder="' + _msg.messagePlaceholder + '"></td>'
            + '<td><button type="button" class="btn btn-xs btn-danger btnRemoveContent">'
            + '<i class="fa fa-minus"></i></button></td>'
            + '</tr>';

        $("#tBodyContents").append(row);

        $("#tBodyContents").off("click", ".btnRemoveContent").on("click", ".btnRemoveContent", function () {
            $(this).closest("tr").remove();
        });
    }

    function _buildPayload() {
        let cpId = $("#inputCpId").val().trim();
        let csId = $("#inputCsId").val().trim();
        let priority = $("#inputPriority").val();

        if (!cpId || cpId.length !== 6) {
            _showAlert(false, _msg.inputError, _msg.inputCpId6);
            return null;
        }
        if (!csId || csId.length !== 2) {
            _showAlert(false, _msg.inputError, _msg.inputCsId2);
            return null;
        }
        if (!priority) {
            _showAlert(false, _msg.inputError, _msg.selectPriority);
            return null;
        }

        let contents = [];
        $("#tBodyContents tr").each(function () {
            let lang    = $(this).find(".content-lang").val().trim();
            let format  = $(this).find(".content-format").val();
            let content = $(this).find(".content-text").val().trim();
            if (lang && content) {
                contents.push({ msgLanguage: lang, msgFormat: format, msgContent: content });
            }
        });

        let payload = {
            cpId     : cpId,
            csId     : csId,
            priority : priority,
            status   : $("#inputStatus").val() || null,
            startDate: _fromDatetimeLocal($("#inputStartDate").val()),
            endDate  : _fromDatetimeLocal($("#inputEndDate").val()),
            rcId     : $("#inputRcId").val().trim() || null,
            displayName    : $("#inputDisplayName").val().trim() || null,
            displayInstance: $("#inputDisplayInstance").val().trim() || null,
            displayEvseId  : parseInt($("#inputDisplayEvseId").val()) || null,
            displayConnId  : parseInt($("#inputDisplayConnId").val()) || null,
            contents : contents
        };

        return payload;
    }

    function _save() {
        let payload = _buildPayload();
        if (!payload) return;

        let isNew   = data.isNew;
        let method  = isNew ? 'POST' : 'PUT';
        let url     = _ctx + "/ws/displayMessage" + (isNew ? '' : '/' + data.messageId);

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
                        toastr.success(_msg.messageRegistered.replace('{0}', newId));
                        // 등록 후 상세 페이지로 이동
                        window.location.href = _ctx + "/charger/displayMessage/detail?messageId=" + newId
                            + "&cpId=" + payload.cpId + "&csId=" + payload.csId;
                    } else {
                        toastr.success(_msg.messageModified);
                        _loadDetail();
                    }
                } else {
                    _showAlert(false, _msg.saveFail, jsonData.result || _commonMsg.failRegister);
                }
            },
            error: function () {
                _showAlert(false, _commonMsg.error, _commonMsg.requestError);
            },
            complete: function () {
                $("#btnSave").prop('disabled', false).text(_msg.btnSave);
            }
        });
    }

    function _delete() {
        if (!confirm(_msg.confirmDeleteMessage.replace('{0}', data.messageId))) return;

        $.ajax({
            type: 'DELETE',
            url: _ctx + "/ws/displayMessage/" + data.messageId
                + "?cpId=" + data.cpId + "&csId=" + data.csId,
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.messageDeleted);
                    window.location.href = _ctx + "/charger/displayMessage/list"
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

    function _escape(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }

    return {
        init: _init
    };
}();
