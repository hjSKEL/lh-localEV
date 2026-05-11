/**
 * 상담내역 상세
 */
let consultDetailPopupJs = function () {
    'use strict';

    let carModelList;
    function _init(obj, list) {
        $('#Popup_CosultDetail_startDt').html(null).html(formmatUtilsJs.dateFormmat(obj.startDate + obj.startTime, 'YYYY-MM-DD HH:MM'));
        $('#Popup_CosultDetail_endDt').html(null).html(formmatUtilsJs.dateFormmat(obj.endDate + obj.endTime, 'YYYY-MM-DD HH:MM'));
        $('#Popup_CosultDetail_consultType').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('CSCS00', obj.consultType));
        $('#Popup_CosultDetail_callType').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('CSCA00', obj.callType));
        $('#Popup_CosultDetail_category').html(null).html(obj.majorCategoryName + '/' + obj.middleCategoryName + '/'+ obj.minorCategoryName);
        $('#Popup_CosultDetail_cpName').html(null);
        if(obj.chargePointCsm) {
            $('#Popup_CosultDetail_cpName').html(obj.chargePointCsm.cpName);
        }
        $('#Popup_CosultDetail_csUniqId').html(null).html(obj.csUniqId);
        $('#Popup_CosultDetail_question').html(null).html(obj.question);
        $('#Popup_CosultDetail_answer').html(null).html(obj.answer);
        $('#Popup_CosultDetail_note').html(null).html(obj.note);

        _searchEmployee(obj.writer.regUserId);

        carModelList = list;
        if(obj.breakdownId) {
            _search(obj.breakdownId);
            $('#Popup_CosultDetail_bd').show();
        } else {
            $('#Popup_CosultDetail_bd').hide();
        }
    }

    function _searchEmployee(employeeId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/organization/employee/" + employeeId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                $('#Popup_CosultDetail_employeeName').html(null).html(jsonData.emplName + '(' + jsonData.employeeId + ')');
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            }
        });
    }

    function _search(bdId) {
        $.ajax({
            type: 'GET',
            url: _ctx + "/ws/breakdown/receipt/detail/" + bdId,
            dataType: 'json',
            success: function (jsonData, textStatus, jqXHR) {
                _displayBreakDownInfo(jsonData);
            },
            error: function (xhRequest, ErrorText, thrownError) {
                //
            }
        });
    }

    function _displayBreakDownInfo(obj) {
        $('#Popup_CosultDetail_cpName').html(null).html(obj.cpName);
        $('#Popup_CosultDetail_csUniqId').html(null).html(obj.csUniqId);
        $('#Popup_CosultDetail_urgencyLevel').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('URG000', obj.breakdownInfo.urgencyLevel));
        $('#Popup_CosultDetail_csCatCode').html(null).html(parent.commonCodeJs.getCodeNameByPCodeNSubCode('CHRA00', obj.breakdownInfo.csCatCode));
        $('#Popup_CosultDetail_bdCategory').html(null).html(obj.majorCategoryName + '/' + obj.middleCategoryName + '/'+ obj.minorCategoryName);
        $('#Popup_CosultDetail_stationErrorCode').html(null).html(obj.breakdownInfo.stationErrorCode);
        $('#Popup_CosultDetail_breakdownContent').html(null).html(obj.breakdownInfo.breakdownContent);

        for(let i = 0, length = carModelList.length; i < length; i++) {
            if(carModelList[i].carModelId === obj.breakdownInfo.carModelId) {
                $('#Popup_CosultDetail_carModelId').html(null).html(carModelList[i].carName);
            }
        }
    }

    return {
        init: _init,
    };
}();
