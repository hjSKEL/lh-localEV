/**
 * 배터리 교체 충전소 상세 (신규/수정) + 소속 교환충전기 인라인 관리
 */
var batterySwapPointJs = function () {
    "use strict";

    var state = {
        mode: 'create',          // 'create' | 'edit'
        cpId: '',
        stations: [],
        stationMode: 'create',   // for modal
        editingStation: null
    };

    function _init(cpId) {
        _bindDatepickers();
        _bindStatusSelect();
        _bindEvent();

        if (cpId && cpId.length > 0) {
            state.mode = 'edit';
            state.cpId = cpId;
            $("#cpId").prop('readonly', true);
            $("#btnDelete").show();
            $("#stationsBlock").show();
            _load();
        } else {
            state.mode = 'create';
            $("#stationsBlock").hide();
            $("#btnDelete").hide();
        }
    }

    function _bindDatepickers() {
        $("#openedFrom,#closedDate").datepicker({
            todayBtn: "linked",
            autoclose: true,
            clearBtn: true,
            format: "yyyy-mm-dd"
        });
    }

    function _bindStatusSelect() {
        var codes = parent.commonCodeJs.getCodesByParentCode("BSOS00") || [];
        var html = '';
        if (codes.length === 0) {
            html = '<option value="Operative">Operative</option>'
                 + '<option value="Inoperative">Inoperative</option>'
                 + '<option value="Maintenance">Maintenance</option>';
        } else {
            for (var i = 0; i < codes.length; ++i) {
                html += '<option value="' + codes[i].code + '">' + codes[i].codeName + '</option>';
            }
        }
        $("#st_operationalStatus").html(html);
    }

    function _bindEvent() {
        $("#btnList").click(function () { self.location = _ctx + "/batterySwap/point/list"; });
        $("#btnSave").click(_save);
        $("#btnDelete").click(_remove);

        $("#btnAddStation").click(_openStationModalForCreate);
        $("#btnStationSave").click(_saveStation);
        $("#btnStationDelete").click(_deleteStation);
    }

    function _load() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/point/" + encodeURIComponent(state.cpId),
            dataType: 'json',
            success: function (point) {
                if (!point) {
                    toastr.error(_commonMsg.dataNotFound, _msg.pointMgmt);
                    return;
                }
                $("#cpId").val(point.cpId || '');
                $("#cpName").val(point.cpName || '');
                $("#openedFrom").val(_formatDate(point.openedFrom));
                $("#closedDate").val(_formatDate(point.closedDate));
                $("#latitude").val(point.latitude != null ? point.latitude : '');
                $("#longitude").val(point.longitude != null ? point.longitude : '');
                $("#basicAddress").val(point.basicAddress || '');
                $("#detailAddress").val(point.detailAddress || '');
                $("#detailLocation").val(point.detailLocation || '');

                state.stations = point.stations || [];
                _renderStations();
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _renderStations() {
        var $tb = $("#tBodyStations").empty();
        if (!state.stations || state.stations.length === 0) {
            $tb.append('<tr style="text-align:center;"><td colspan="7">' + _commonMsg.noData + '</td></tr>');
            return;
        }
        var html = '';
        for (var i = 0; i < state.stations.length; ++i) {
            var s = state.stations[i];
            var avail = (s.totalSlotCount || 0) - (s.reservedSlotCount || 0);
            var slotTxt = avail + '/' + (s.totalSlotCount || 0);
            if (avail <= 0 && (s.totalSlotCount || 0) > 0) {
                slotTxt = '<span style="color:#dd6b55;">' + slotTxt + '</span>';
            }
            html += '<tr>';
            html += '<td>' + (i + 1) + '</td>';
            html += '<td>' + s.csId + '</td>';
            html += '<td>' + _operStatBadge(s.operationalStatus) + '</td>';
            html += '<td>' + (s.enabled === 'Y' ? '<i class="fa fa-check text-navy"></i>' : '<i class="fa fa-times text-muted"></i>') + '</td>';
            html += '<td>' + slotTxt + '</td>';
            html += '<td>' + (s.supportedBatteryModel || '-') + '</td>';
            html += '<td>'
                  + '<button type="button" class="btn btn-xs btn-info" onclick="batterySwapPointJs.openStation(\'' + s.csId + '\')"><i class="fa fa-pencil"></i></button> '
                  + '<button type="button" class="btn btn-xs btn-danger" onclick="batterySwapPointJs.removeStation(\'' + s.csId + '\')"><i class="fa fa-trash"></i></button>'
                  + '</td>';
            html += '</tr>';
        }
        $tb.append(html);
    }

    function _operStatBadge(code) {
        switch (code) {
            case 'Operative':   return '<span class="label label-primary">Operative</span>';
            case 'Maintenance': return '<span class="label label-warning">Maintenance</span>';
            case 'Inoperative': return '<span class="label label-default">Inoperative</span>';
            default: return code || '-';
        }
    }

    function _save() {
        var cpId = $("#cpId").val().trim();
        var cpName = $("#cpName").val().trim();
        if (!cpId) { toastr.warning(_msg.inputCpId, _msg.pointMgmt); return; }
        if (state.mode === 'create' && cpId.length !== 6) {
            toastr.warning(_msg.cpIdDigit6, _msg.pointMgmt); return;
        }
        if (!cpName) { toastr.warning(_msg.inputCpName, _msg.pointMgmt); return; }

        var lat = $("#latitude").val();
        var lon = $("#longitude").val();
        if ((lat && !lon) || (!lat && lon)) {
            toastr.warning(_msg.invalidCoord, _msg.pointMgmt); return;
        }

        var body = {
            cpId: cpId,
            cpName: cpName,
            latitude: lat || null,
            longitude: lon || null,
            basicAddress: $("#basicAddress").val(),
            detailAddress: $("#detailAddress").val(),
            detailLocation: $("#detailLocation").val()
        };
        var openedFrom = $("#openedFrom").val();
        var closedDate = $("#closedDate").val();
        if (openedFrom) body.openedFrom = new Date(openedFrom + "T00:00:00").getTime();
        if (closedDate) body.closedDate = new Date(closedDate + "T23:59:59").getTime();

        if (state.mode === 'create') {
            _checkCpIdThenPost(cpId, body);
        } else {
            _put(cpId, body);
        }
    }

    function _checkCpIdThenPost(cpId, body) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/batterySwap/point/check/" + encodeURIComponent(cpId),
            dataType: 'json',
            success: function (json) {
                if (json.status !== 'SUCCESS') {
                    toastr.error(_msg.duplicateCpId, _msg.pointMgmt); return;
                }
                _post(body);
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _post(body) {
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/batterySwap/point",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (json) {
                if (json.status === 'SUCCESS') {
                    toastr.success(_msg.successSave, _msg.pointMgmt);
                    self.location = _ctx + "/batterySwap/point/detail?cpId=" + encodeURIComponent(json.result || body.cpId);
                } else {
                    toastr.error(json.result || _msg.failSave, _msg.pointMgmt);
                }
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
                toastr.error(_msg.failSave, _msg.pointMgmt);
            }
        });
    }

    function _put(cpId, body) {
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/batterySwap/point/" + encodeURIComponent(cpId),
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(body),
            success: function (json) {
                if (json.status === 'SUCCESS') {
                    toastr.success(_msg.successSave, _msg.pointMgmt);
                    _load();
                } else {
                    toastr.error(json.result || _msg.failSave, _msg.pointMgmt);
                }
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
                toastr.error(_msg.failSave, _msg.pointMgmt);
            }
        });
    }

    function _remove() {
        swal({
            title: _msg.pointMgmt,
            text: _msg.confirmDelete,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/batterySwap/point/" + encodeURIComponent(state.cpId),
                dataType: 'json',
                success: function (json) {
                    if (json.status === 'SUCCESS') {
                        toastr.success(_msg.successDelete, _msg.pointMgmt);
                        self.location = _ctx + "/batterySwap/point/list";
                    } else {
                        toastr.error(json.result || _msg.failDelete, _msg.pointMgmt);
                    }
                },
                error: function (xhr) {
                    parent.layerJs.fn_exception(xhr);
                    toastr.error(_msg.failDelete, _msg.pointMgmt);
                }
            });
        });
    }

    // ------- Station modal -------
    function _openStationModalForCreate() {
        if (!state.cpId) {
            toastr.warning(_msg.inputCpId, _msg.stationMgmt); return;
        }
        state.stationMode = 'create';
        state.editingStation = null;
        $("#st_cpId").val(state.cpId);
        $("#st_csId").val('').prop('readonly', false);
        $("input:radio[name=st_enabled][value=Y]").prop('checked', true);
        $("#st_operationalStatus").val('Operative');
        $("#st_totalSlotCount").val(0);
        $("#st_reservedSlotCount").val(0);
        $("#st_supportedBatteryModel").val('');
        $("#st_minSoHThreshold").val('');
        $("#st_minSoCForDispatch").val('');
        $("#st_swapTimeoutSec").val(600);
        $("#st_defaultIdToken").val('');
        $("#st_defaultIdTokenType").val('');
        $("#btnStationDelete").hide();
        $("#Popup_Station").modal();
    }

    function _openStation(csId) {
        var found = null;
        for (var i = 0; i < state.stations.length; ++i) {
            if (state.stations[i].csId === csId) { found = state.stations[i]; break; }
        }
        if (!found) return;
        state.stationMode = 'edit';
        state.editingStation = found;
        $("#st_cpId").val(found.cpId);
        $("#st_csId").val(found.csId).prop('readonly', true);
        $("input:radio[name=st_enabled][value=" + (found.enabled || 'Y') + "]").prop('checked', true);
        $("#st_operationalStatus").val(found.operationalStatus || 'Operative');
        $("#st_totalSlotCount").val(found.totalSlotCount || 0);
        $("#st_reservedSlotCount").val(found.reservedSlotCount || 0);
        $("#st_supportedBatteryModel").val(found.supportedBatteryModel || '');
        $("#st_minSoHThreshold").val(found.minSoHThreshold != null ? found.minSoHThreshold : '');
        $("#st_minSoCForDispatch").val(found.minSoCForDispatch != null ? found.minSoCForDispatch : '');
        $("#st_swapTimeoutSec").val(found.swapTimeoutSec || 600);
        $("#st_defaultIdToken").val(found.defaultIdToken || '');
        $("#st_defaultIdTokenType").val(found.defaultIdTokenType || '');
        $("#btnStationDelete").show();
        $("#Popup_Station").modal();
    }

    function _collectStationBody() {
        var csId = $("#st_csId").val().trim();
        if (!csId) { toastr.warning(_msg.inputCsId, _msg.stationMgmt); return null; }
        if (csId.length !== 2) { toastr.warning(_msg.csIdDigit2, _msg.stationMgmt); return null; }

        var total = parseInt($("#st_totalSlotCount").val(), 10);
        var rsvd = parseInt($("#st_reservedSlotCount").val(), 10);
        if (isNaN(total) || isNaN(rsvd) || total < 0 || rsvd < 0 || rsvd > total) {
            toastr.warning(_msg.invalidSlot, _msg.stationMgmt); return null;
        }
        var soh = $("#st_minSoHThreshold").val();
        var soc = $("#st_minSoCForDispatch").val();
        if (soh !== '' && (Number(soh) < 0 || Number(soh) > 100)) {
            toastr.warning(_msg.invalidPercent, _msg.stationMgmt); return null;
        }
        if (soc !== '' && (Number(soc) < 0 || Number(soc) > 100)) {
            toastr.warning(_msg.invalidPercent, _msg.stationMgmt); return null;
        }
        var timeout = parseInt($("#st_swapTimeoutSec").val(), 10);
        if (isNaN(timeout) || timeout <= 0) {
            toastr.warning(_msg.invalidSlot, _msg.stationMgmt); return null;
        }

        return {
            cpId: $("#st_cpId").val(),
            csId: csId,
            enabled: $("input:radio[name=st_enabled]:checked").val(),
            operationalStatus: $("#st_operationalStatus").val(),
            totalSlotCount: total,
            reservedSlotCount: rsvd,
            supportedBatteryModel: $("#st_supportedBatteryModel").val(),
            minSoHThreshold: soh === '' ? null : Number(soh),
            minSoCForDispatch: soc === '' ? null : Number(soc),
            swapTimeoutSec: timeout,
            defaultIdToken: $("#st_defaultIdToken").val(),
            defaultIdTokenType: $("#st_defaultIdTokenType").val()
        };
    }

    function _saveStation() {
        var body = _collectStationBody();
        if (!body) return;
        if (state.stationMode === 'create') {
            $.ajax({
                type: 'POST',
                url: _ctx + "/ws/batterySwap/station",
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(body),
                success: _afterStationSave,
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        } else {
            $.ajax({
                type: 'PUT',
                url: _ctx + "/ws/batterySwap/station/" + encodeURIComponent(body.cpId) + "/" + encodeURIComponent(body.csId),
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify(body),
                success: _afterStationSave,
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        }
    }

    function _afterStationSave(json) {
        if (json.status === 'SUCCESS') {
            toastr.success(_msg.successSaveStation, _msg.stationMgmt);
            $("#Popup_Station").modal('hide');
            _load();
        } else {
            toastr.error(json.result || _msg.failSaveStation, _msg.stationMgmt);
        }
    }

    function _deleteStation() {
        if (!state.editingStation) return;
        var s = state.editingStation;
        swal({
            title: _msg.stationMgmt,
            text: _msg.confirmDeleteStation,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/batterySwap/station/" + encodeURIComponent(s.cpId) + "/" + encodeURIComponent(s.csId),
                dataType: 'json',
                success: function (json) {
                    if (json.status === 'SUCCESS') {
                        toastr.success(_msg.successDeleteStation, _msg.stationMgmt);
                        $("#Popup_Station").modal('hide');
                        _load();
                    } else {
                        toastr.error(json.result || _msg.failDeleteStation, _msg.stationMgmt);
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        });
    }

    function _removeStation(csId) {
        var target = null;
        for (var i = 0; i < state.stations.length; ++i) {
            if (state.stations[i].csId === csId) { target = state.stations[i]; break; }
        }
        if (!target) return;
        swal({
            title: _msg.stationMgmt,
            text: _msg.confirmDeleteStation,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: true
        }, function () {
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/batterySwap/station/" + encodeURIComponent(target.cpId) + "/" + encodeURIComponent(target.csId),
                dataType: 'json',
                success: function (json) {
                    if (json.status === 'SUCCESS') {
                        toastr.success(_msg.successDeleteStation, _msg.stationMgmt);
                        _load();
                    } else {
                        toastr.error(json.result || _msg.failDeleteStation, _msg.stationMgmt);
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        });
    }

    function _formatDate(val) {
        if (!val) return '';
        var dt = new Date(val);
        var y = dt.getFullYear();
        var m = ('0' + (dt.getMonth() + 1)).slice(-2);
        var d = ('0' + dt.getDate()).slice(-2);
        return y + '-' + m + '-' + d;
    }

    return {
        init: _init,
        openStation: _openStation,
        removeStation: _removeStation
    };
}();
