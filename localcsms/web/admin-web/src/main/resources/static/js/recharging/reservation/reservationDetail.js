/**
 * 예약 관리 - 상세 페이지
 *
 * URL 파라미터:
 *   - cpId  : 충전소 ID (필수)
 *   - csId  : 충전기 ID (필수)
 *   - rsvId : 예약 ID   (선택 - 있으면 상세 섹션 표시)
 */
let reservationDetailJs = function () {
    "use strict";

    let params = {};   // URL 파라미터

    function _init() {
        params = _getUrlParams();

        _initForm();
        _initEvent();

        if (params.rsvId) {
            // 상세 보기: 등록 폼 숨김, 상세 정보 표시
            $("#sectionRegister").hide();
            _loadDetail(params.rsvId);
        } else {
            // 신규 등록: 상세 섹션 숨김, 등록 폼만 표시
            $("#sectionDetail").hide();
        }

        _searchHistory();
    }

    // ── URL 파라미터 파싱 ───────────────────────────────────────────────────────
    function _getUrlParams() {
        let urlParams = new URLSearchParams(window.location.search);
        return {
            rsvId: urlParams.get('rsvId'),
            cpId: urlParams.get('cpId') || '',
            csId: urlParams.get('csId') || ''
        };
    }

    // ── 등록 폼 초기화 ──────────────────────────────────────────────────────────
    function _initForm() {
        if (params.cpId) {
            $("#regCpId").val(params.cpId).prop('readonly', true);
        }
        if (params.csId) {
            $("#regCsId").val(params.csId).prop('readonly', true);
        }

        // 만료일 최솟값: 현재 시각
        let now = new Date();
        let minDt = now.toISOString().slice(0, 16);
        $("#regExpiredDate").attr('min', minDt);
    }

    // ── 이벤트 바인딩 ───────────────────────────────────────────────────────────
    function _initEvent() {
        // 목록 버튼
        $("#btnList, #btnListFromRegister").click(function () {
            window.location.href = _ctx + "/reservation/list";
        });

        // 예약 취소 버튼
        $("#btnCancel").click(function () {
            if (!confirm(_msg.confirmCancelReservation)) return;
            _cancelReservation(params.rsvId);
        });

        // 예약 등록 폼 제출
        $("#registerForm").submit(function (e) {
            e.preventDefault();
            _registerReservation();
        });
    }

    // ── ① 예약 상세 조회 ────────────────────────────────────────────────────────
    function _loadDetail(rsvId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/reservation/detail/" + rsvId,
            dataType: 'json',
            success: function (data) {
                _renderDetail(data);
                $("#sectionDetail").show();
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _renderDetail(r) {
        let statusBadge = _statusBadge(r.status);
        let expiredDate = r.expiredDate ? new Date(r.expiredDate).toLocaleString('ko-KR') : '-';
        let regDate = (r.writer && r.writer.registrationDate)
            ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';

        $("#detailRsvId").text(r.rsvId);
        $("#detailStatus").html(statusBadge);
        $("#detailCpId").text(r.cpId || '-');
        $("#detailCsId").text(r.csId || '-');
        $("#detailCutCardNo").text(r.cutCardNo || '-');
        $("#detailParentCardNo").text(r.parentCardNo || '-');
        $("#detailExpiredDate").text(expiredDate);
        $("#detailRegDate").text(regDate);

        // 예약 상태일 때만 취소 버튼 표시
        if (r.status === 'RSVT01') {
            $("#btnCancel").show();
        }
    }

    // ── ② 예약 취소 ─────────────────────────────────────────────────────────────
    function _cancelReservation(rsvId) {
        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/reservation/cancel",
            contentType: 'application/json',
            data: JSON.stringify({ rsvId: rsvId }),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.reservationCancelled);
                    _loadDetail(rsvId);      // 상세 갱신
                    _searchHistory();         // 이력 갱신
                } else {
                    toastr.error(_msg.reservationCancelFail);
                }
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    // ── ③ 예약 등록 ─────────────────────────────────────────────────────────────
    function _registerReservation() {
        let cpId = $("#regCpId").val().trim();
        let csId = $("#regCsId").val().trim();
        if (!cpId || cpId.length !== 6) {
            toastr.warning(_msg.cpIdDigit6);
            return;
        }
        if (!csId || csId.length !== 2) {
            toastr.warning(_msg.csIdDigit2);
            return;
        }
        let expiredDateVal = $("#regExpiredDate").val();
        if (!expiredDateVal) {
            toastr.warning(_msg.inputExpiredDate);
            return;
        }
        let cutCardNo = $("#regCutCardNo").val().trim();
        if (!cutCardNo) {
            toastr.warning(_msg.inputCardNo);
            return;
        }

        let payload = {
            cpId: cpId,
            csId: csId,
            expiredDate: new Date(expiredDateVal),
            cutCardNo: cutCardNo,
            parentCardNo: $("#regParentCardNo").val().trim() || null
        };

        $("#btnReserve").prop('disabled', true).text(_msg.processing);

        $.ajax({
            type: 'POST',
            url: _ctx + "/ws/reservation",
            contentType: 'application/json',
            data: JSON.stringify(payload),
            dataType: 'json',
            success: function (jsonData) {
                if (jsonData.status === 'SUCCESS') {
                    toastr.success(_msg.reservationRegistered);
                    _showAlert(true, _msg.reservationRegisterComplete, _msg.reservationRegisterSuccess);
                    $("#registerForm")[0].reset();
                    _initForm();
                    _searchHistory();   // 이력 갱신
                } else {
                    _showAlert(false, _msg.reservationRegisterFail, _msg.reservationRegisterFailRetry);
                }
            },
            error: function (xhRequest) {
                _showAlert(false, _msg.error, _commonMsg.requestError);
            },
            complete: function () {
                $("#btnReserve").prop('disabled', false).text(_msg.reserveRegister);
            }
        });
    }

    function _showAlert(isSuccess, title, message) {
        let alertEl = $("#alertRegister");
        alertEl.removeClass("alert-success alert-danger")
            .addClass(isSuccess ? "alert-success" : "alert-danger")
            .show();
        $("#alertTitle").text(title);
        $("#alertMessage").text(message);
    }

    // ── ④ 예약 이력 조회 ────────────────────────────────────────────────────────
    function _searchHistory() {
        let cpId = $("#regCpId").val().trim() || params.cpId;
        let csId = $("#regCsId").val().trim() || params.csId;
        if (!cpId || !csId) return;

        pageInfoHistoryJs.init('pageInfoHistoryJs', 'pagingUlHistory', 10, 20, reservationDetailJs.loadHistory);
        _loadHistory();
    }

    function _loadHistory() {
        let cpId = $("#regCpId").val().trim() || params.cpId;
        let csId = $("#regCsId").val().trim() || params.csId;

        $("#tBodyHistory").empty().append(
            '<tr style="text-align:center;"><td colspan="9">' + _commonMsg.searching + '</td></tr>'
        );

        let paging = pageInfoHistoryJs.getPaging();
        let param = "?pageNumber=" + (paging.pageNumber - 1)
            + "&pageItemSize=" + paging.pageItemSize
            + "&cpId=" + cpId
            + "&csId=" + csId;

        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/reservation/list" + param,
            dataType: 'json',
            success: function (jsonData) {
                _renderHistory(jsonData);
            },
            error: function (xhRequest) {
                parent.layerJs.fn_exception(xhRequest);
            }
        });
    }

    function _renderHistory(jsonData) {
        pageInfoHistoryJs.setTotalCount(jsonData.criteria.totalItemCount);
        $("#historyTotalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
        $("#tBodyHistory").empty();

        if (jsonData.criteria.totalItemCount === 0) {
            $("#tBodyHistory").append(
                '<tr style="text-align:center;"><td colspan="9">' + _msg.noReservationHistory + '</td></tr>'
            );
            return;
        }

        let result = jsonData.result;
        let paging = pageInfoHistoryJs.getPaging();
        let noIndex = (paging.pageNumber - 1) * paging.pageItemSize + 1;
        let html = '';

        for (let i = 0, len = result.length; i < len; i++) {
            let r = result[i];
            let isCurrentRsv = params.rsvId && String(r.rsvId) === String(params.rsvId);
            let statusBadge = _statusBadge(r.status);
            let expiredDate = r.expiredDate ? new Date(r.expiredDate).toLocaleString('ko-KR') : '-';
            let regDate = (r.writer && r.writer.registrationDate)
                ? new Date(r.writer.registrationDate).toLocaleString('ko-KR') : '-';

            html += '<tr' + (isCurrentRsv ? ' class="warning"' : '') + '>';
            html += '<td>' + (i + noIndex) + '</td>';
            html += '<td style="font-family:monospace;">' + r.rsvId + '</td>';
            html += '<td>' + (r.cpId || '-') + '</td>';
            html += '<td>' + (r.csId || '-') + '</td>';
            html += '<td>' + (r.cutCardNo || '-') + '</td>';
            html += '<td>' + (r.parentCardNo || '-') + '</td>';
            html += '<td>' + expiredDate + '</td>';
            html += '<td>' + statusBadge + '</td>';
            html += '<td>' + regDate + '</td>';
            html += '</tr>';
        }

        $("#tBodyHistory").append(html);
    }

    // ── 공통: 상태 배지 ──────────────────────────────────────────────────────────
    function _statusBadge(status) {
        if (status === 'RSVT01') return '<span class="label label-primary"' + _msg.statusReserved + '</span>';
        if (status === 'RSVT02') return '<span class="label label-danger"' + _msg.statusCancelled + '</span>';
        return '<span class="label label-default">' + (status || '-') + '</span>';
    }

    return {
        init: _init,
        loadHistory: _loadHistory
    };
}();
