/**
 * 고객 보유 차량 (세대별 등록 차량) 관리 — TB_CUEV001 / CustomerVehicleResource.
 *
 * 회원 상세 페이지 (customer.html) 의 "보유 차량" 섹션에 바인딩.
 * REST: /ws/customer/vehicle/* (PK: VIN_NO)
 */
var customerVehicleJs = (function () {

    var customerId = null;
    var editingVinNo = null;  // null = 등록 모드, 값 있음 = 수정 모드

    function _init(custId) {
        customerId = custId;
        $("#btnVehicleAdd").off("click").on("click", _onClickAdd);
        $("#btnVehicleSave").off("click").on("click", _onClickSave);
        $("#btnVehicleCancel").off("click").on("click", _onClickCancel);
        $("#btnVehicleDelete").off("click").on("click", _onClickDelete);
        $("#btnVehicleCarNoChecker").off("click").on("click", _onClickCheckCarNo);
        $("#btnVehicleVinNoChecker").off("click").on("click", _onClickCheckVinNo);
        // ESC/배경클릭/닫기버튼 등 어떤 경로로 팝업이 닫히든 편집 상태를 초기화
        $("#vehicleFormArea").off("hidden.bs.modal").on("hidden.bs.modal", function () {
            editingVinNo = null;
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
            $tbody.append('<tr><td colspan="5" style="text-align:center;">-</td></tr>');
            return;
        }
        for (var i = 0; i < list.length; i++) {
            var v = list[i];
            var regDt = v.writer && v.writer.registrationDate
                ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(v.writer.registrationDate)), 'YYYY-MM-DD')
                : '';
            var updDt = v.writer && v.writer.updateDate
                ? formmatUtilsJs.dateFormmat(dateUtilsJs.date2String(new Date(v.writer.updateDate)), 'YYYY-MM-DD')
                : '';
            var html = '<tr>';
            html += '<td style="text-align:center;">' + (v.carNo || '') + '</td>';
            html += '<td style="text-align:center;">' + (v.carName || '') + '</td>';
            html += '<td style="text-align:center;">' + regDt + '</td>';
            html += '<td style="text-align:center;">' + updDt + '</td>';
            html += '<td style="text-align:center;">'
                + '<button class="btn btn-primary btn-xs" onclick="customerVehicleJs.openDetail(\'' + _escape(v.vinNo) + '\')">차량상세</button>'
                + '</td>';
            html += '</tr>';
            $tbody.append(html);
        }
    }

    // 차량 팝업의 세대주명/세대정보는 별도 조회 없이 customer.html 자체 폼(#custName/#cxNum/#dong/#ho)의 현재 값을 그대로 표시.
    function _fillCustomerInfo() {
        $("#vehicleCustName").text($("#custName").val() || '-');
        var cx = $("#cxNum").val(), dong = $("#dong").val(), ho = $("#ho").val();
        var addr = (cx ? cx + '단지 ' : '') + (dong ? dong + '동 ' : '') + (ho ? ho + '호' : '');
        $("#vehicleCustAddr").text(addr || '-');
    }

    function _setAddMode() {
        $("#vehicleInfoTitle").text("차량 추가");
        $("#vfCarNo,#vfVinNo").prop("readonly", false);
        $("#btnVehicleVinNoChecker").attr("disabled", false);
        $("#btnVehicleDelete").hide();
        $("#btnVehicleSave span").text("등록");
        $("#btnVehicleCancel span").text("취소");
    }

    function _onClickCheckCarNo() {
        var carNo = $("#vfCarNo").val().trim();
        if (!carNo) {
            swal("확인", "차량번호는 필수입니다.", "warning");
            return;
        }
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/vehicle/checkCarNo/" + encodeURIComponent(carNo),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success(res.message || "사용 가능한 차량번호입니다.");
                } else {
                    swal("확인", (res && res.message) || "이미 등록된 차량번호입니다.", "warning");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _onClickCheckVinNo() {
        var vinNo = $("#vfVinNo").val().trim();
        if (!vinNo) {
            swal("확인", "차대번호(VIN)는 필수입니다.", "warning");
            return;
        }
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/vehicle/checkVinNo/" + encodeURIComponent(vinNo),
            dataType: 'json',
            success: function (res) {
                if (res && res.status === 'SUCCESS') {
                    toastr.success(res.message || "사용 가능한 차대번호입니다.");
                } else {
                    swal("확인", (res && res.message) || "이미 등록된 차대번호입니다.", "warning");
                }
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _setDetailMode() {
        $("#vehicleInfoTitle").text("차량상세");
        $("#vfVinNo").prop("readonly", true);  // PK 변경 불가
        $("#btnVehicleVinNoChecker").attr("disabled", true);
        $("#btnVehicleDelete").show();
        $("#btnVehicleSave span").text("수정");
        $("#btnVehicleSave").attr("class", "btn btn-warning btn-sm");
        $("#btnVehicleCancel span").text("닫기");
    }

    function _onClickAdd() {
        editingVinNo = null;
        _clearForm();
        _setAddMode();
        _fillCustomerInfo();
        $("#vehicleFormArea").modal();
    }

    function _onClickCancel() {
        $("#vehicleFormArea").modal('hide');
    }

    function _onClickSave() {
        var carNo = $("#vfCarNo").val().trim();
        if (!carNo) {
            swal("확인", "차량번호는 필수입니다.", "warning");
            return;
        }
        var vinNo = $("#vfVinNo").val().trim();
        if (!vinNo) {
            swal("확인", "차대번호(VIN)는 필수입니다.", "warning");
            return;
        }
        var payload = {
            customerId: customerId,
            carName: $("#vfCarName").val().trim() || null,
            carNo: carNo,
            vinNo: vinNo
        };
        if (editingVinNo) {
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
            url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(editingVinNo),
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

    function _openDetail(vinNo) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(vinNo),
            dataType: 'json',
            success: function (v) {
                if (!v) {
                    swal("오류", "차량 정보를 찾을 수 없습니다.", "error");
                    return;
                }
                editingVinNo = vinNo;
                _setDetailMode();
                $("#vfVinNo").val(v.vinNo || '');
                $("#vfCarName").val(v.carName || '');
                $("#vfCarNo").val(v.carNo || '');
                _fillCustomerInfo();
                $("#vehicleFormArea").modal();
            },
            error: function (xhr) { parent.layerJs.fn_exception(xhr); }
        });
    }

    function _onClickDelete() {
        if (!editingVinNo) return;
        var vinNo = editingVinNo;
        swal({
            title: "삭제 확인",
            text: "정말 삭제하시겠습니까? (" + vinNo + ")",
            type: "warning",
            showCancelButton: true,
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }, function (isConfirm) {
            if (!isConfirm) return;
            $.ajax({
                type: 'DELETE',
                url: _ctx + "/ws/customer/vehicle/" + encodeURIComponent(vinNo),
                dataType: 'json',
                success: function (res) {
                    if (res && res.status === 'SUCCESS') {
                        toastr.success("차량이 삭제되었습니다.");
                        $("#vehicleFormArea").modal('hide');
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
        $("#vfCarName").val('');
        $("#vfCarNo").val('');
        $("#vfVinNo").val('');
    }

    function _escape(s) {
        return (s || '').replace(/'/g, "\\'");
    }

    return {
        init: _init,
        openDetail: _openDetail
    };
})();
