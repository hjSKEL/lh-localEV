/**
 * 고객 보유 차량 (EVCCID) 관리 — TB_CUEV001 / CustomerVehicleResource.
 *
 * 회원 상세 페이지 (customer.html) 의 "보유 차량" 섹션에 바인딩.
 * REST: /ws/customer/vehicle/*
 */
var customerVehicleJs = (function () {

    var customerId = null;
    var editingEvccId = null;  // null = 등록 모드, 값 있음 = 수정 모드

    function _init(custId) {
        customerId = custId;
        $("#btnVehicleAdd").off("click").on("click", _onClickAdd);
        $("#btnVehicleSave").off("click").on("click", _onClickSave);
        $("#btnVehicleCancel").off("click").on("click", _onClickCancel);
        // ESC/배경클릭/닫기버튼 등 어떤 경로로 팝업이 닫히든 편집 상태를 초기화
        $("#vehicleFormArea").off("hidden.bs.modal").on("hidden.bs.modal", function () {
            editingEvccId = null;
            _clearForm();
        });
        _loadList();
    }

    function _loadList() {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/vehicle/byCustomer/" + encodeURIComponent(customerId),
            dataType: 'json',
            success: function (list) {
                _renderList(list || []);
            },
            error: function (xhr) {
                parent.layerJs.fn_exception(xhr);
            }
        });
    }

    function _renderList(list) {
        var $tbody = $("#vehicleTbody").empty();
        if (list.length === 0) {
            $tbody.append('<tr><td colspan="7" style="text-align:center;">-</td></tr>');
            return;
        }
        for (var i = 0; i < list.length; i++) {
            var v = list[i];
            var regDt = v.writer && v.writer.registrationDate
                ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(v.writer.registrationDate)), 'YYYY-MM-DD')
                : '';
            var html = '<tr>';
            html += '<td>' + (v.evccId || '') + '</td>';
            html += '<td>' + (v.carName || '') + '</td>';
            html += '<td>' + (v.carNo || '') + '</td>';
            html += '<td>' + (v.carModelId || '') + '</td>';
            html += '<td>' + (v.vinNo || '') + '</td>';
            html += '<td>' + regDt + '</td>';
            html += '<td>'
                + '<button class="btn btn-warning btn-xs" onclick="customerVehicleJs.edit(\'' + _escape(v.evccId) + '\')">수정</button> '
                + '<button class="btn btn-danger btn-xs" onclick="customerVehicleJs.remove(\'' + _escape(v.evccId) + '\')">삭제</button>'
                + '</td>';
            html += '</tr>';
            $tbody.append(html);
        }
    }

    function _onClickAdd() {
        editingEvccId = null;
        _clearForm();
        $("#vfEvccId").prop("readonly", false);
        $("#vehicleFormArea").modal();
    }

    function _onClickCancel() {
        $("#vehicleFormArea").modal('hide');
    }

    function _onClickSave() {
        var evccId = $("#vfEvccId").val().trim();
        if (!evccId) {
            swal("확인", "EVCCID는 필수입니다.", "warning");
            return;
        }
        var payload = {
            evccId: evccId,
            customerId: customerId,
            carName: $("#vfCarName").val().trim() || null,
            carNo: $("#vfCarNo").val().trim() || null,
            carModelId: $("#vfCarModelId").val().trim() || null,
            vinNo: $("#vfVinNo").val().trim() || null,
            v2xYn: $("#vfV2xYn").val() || 'N'
        };
        if (editingEvccId) {
            _doUpdate(payload);
        } else {
            _doRegister(payload);
        }
    }

    function _doRegister(payload) {
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/customer/vehicle",
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success("차량이 등록되었습니다.");
                    _onClickCancel();
                    _loadList();
                } else {
                    swal("오류", (res && res.message) || "등록 실패", "error");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _doUpdate(payload) {
        $.ajax({
            type: 'PUT',
            url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(editingEvccId),
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success("차량 정보가 수정되었습니다.");
                    _onClickCancel();
                    _loadList();
                } else {
                    swal("오류", (res && res.message) || "수정 실패", "error");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _edit(evccId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(evccId),
            dataType: 'json',
            success: function (v) {
                if (!v) {
                    swal("오류", "차량 정보를 찾을 수 없습니다.", "error");
                    return;
                }
                editingEvccId = evccId;
                $("#vfEvccId").val(v.evccId || '').prop("readonly", true);  // PK 변경 불가
                $("#vfCarName").val(v.carName || '');
                $("#vfCarNo").val(v.carNo || '');
                $("#vfCarModelId").val(v.carModelId || '');
                $("#vfVinNo").val(v.vinNo || '');
                $("#vfV2xYn").val(v.v2xYn || 'N');
                $("#vehicleFormArea").modal();
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _remove(evccId) {
        swal({
            title: "삭제 확인",
            text: "정말 삭제하시겠습니까? (" + evccId + ")",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }, function (isConfirm) {
            if (!isConfirm) return;
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(evccId),
                dataType: 'json',
                success: function (res) {
                    if (res && res.status === 'SUCCESS') {
                        toastr.success("차량이 삭제되었습니다.");
                        _loadList();
                    } else {
                        swal("오류", (res && res.message) || "삭제 실패", "error");
                    }
                },
                error: function (xhr) { parent.layerJs.fn_exception(xhr); }
            });
        });
    }

    function _clearForm() {
        $("#vfEvccId").val('');
        $("#vfCarName").val('');
        $("#vfCarNo").val('');
        $("#vfCarModelId").val('');
        $("#vfVinNo").val('');
        $("#vfV2xYn").val('N');
    }

    function _escape(s) {
        return (s || '').replace(/'/g, "\\'");
    }

    return {
        init: _init,
        edit: _edit,
        remove: _remove
    };
})();
