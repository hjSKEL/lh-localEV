/**
 * 회원정보
 */
let customerUpdatePopupJs = function () {
    'use strict';

    let cbFunc;
    let data;

    function _init() {
        _initEvent();
    }

    function _initEvent(carModelList) {
        //고객관리코드
        $('#Popup_CustomerUpdate_cutGrdCode').empty();
        let cutGrdCodes = parent.commonCodeJs.getCodesByParentCode('MEMB00');
        for (let i = 0, length = cutGrdCodes.length; i < length; ++i) {
            $('#Popup_CustomerUpdate_cutGrdCode').append('<option value="' + cutGrdCodes[i].code + '">' + cutGrdCodes[i].codeName + '</option>');
        }
        //고객관리코드
        $('#Popup_CustomerUpdate_cutManageCode').empty();
        let cutManageCodes = parent.commonCodeJs.getCodesByParentCode('MEMK00');
        for (let i = 0, length = cutManageCodes.length; i < length; ++i) {
            $('#Popup_CustomerUpdate_cutManageCode').append('<option value="' + cutManageCodes[i].code + '">' + cutManageCodes[i].codeName + '</option>');
        }
        $('#Popup_CustomerUpdate_btnUpdate').unbind('click');
        $('#Popup_CustomerUpdate_btnUpdate').click(function () {
            if (_validate()) {
                _customerUpdate();
            }
        });

        $('#Popup_CustomerUpdate_carModelId').change(function () {
            let text = $('#Popup_CustomerUpdate_carModelId option:selected').text();
            if ($('#Popup_CustomerUpdate_carModelId').val().substr(-2) === '99') {
                $('#Popup_CustomerUpdate_carModelId').css('width', '50%');
                $('#Popup_CustomerUpdate_carName').show();
                $('#Popup_CustomerUpdate_carName').val(null);
            } else {
                $('#Popup_CustomerUpdate_carModelId').css('width', '100%');
                $('#Popup_CustomerUpdate_carName').hide();
                $('#Popup_CustomerUpdate_carName').val(text);
            }
        });
    }

    function _show(callbackFunc, json, carModelList) {
        $('#Popup_CustomerUpdate_carModelId').empty();
        for (let i = 0, length = carModelList.length; i < length; ++i) {
            $('#Popup_CustomerUpdate_carModelId').append('<option value="' + carModelList[i].carModelId + '">' + carModelList[i].carName + '</option>');
        }
        cbFunc = callbackFunc;
        data = json;
        _display();
    }

    function _display() {
        $('#Popup_CustomerUpdate_customerId').html(null).html(data.customerId);
        $('#Popup_CustomerUpdate_custName').html(null).html(data.custName + (data.certYn === 'Y' ? '' : _msg.unverified));
        $('#Popup_CustomerUpdate_loginId').html(null).html(data.loginId);
        $('#Popup_CustomerUpdate_mblPhoneNo').html(null).html(formmatUtilsJs.phoneFormat(data.mblPhoneNo));
        $('#Popup_CustomerUpdate_companyName').html(null).html(data.companyName);
        $('#Popup_CustomerUpdate_corporateDept').html(null).html(data.corporateDept);
        $('#Popup_CustomerUpdate_carModelId').val(null).val(data.carModel.carModelId);
        $('#Popup_CustomerUpdate_carName').val(null).val(data.carName);
        if (data.carModel.carModelId.substr(-2) === '99') {
            $('#Popup_CustomerUpdate_carModelId').css('width', '50%');
            $('#Popup_CustomerUpdate_carName').show();
        } else {
            $('#Popup_CustomerUpdate_carModelId').css('width', '100%');
            $('#Popup_CustomerUpdate_carName').hide();
        }
        $('#Popup_CustomerUpdate_carNumber').val(null).val(data.carNumber);
        $('#Popup_CustomerUpdate_cutGrdCode').val(null).val(data.customerMgt.cutGrdCode);
        $('#Popup_CustomerUpdate_cutManageCode').val(null).val(data.customerMgt.cutManageCode);
        $('#Popup_CustomerUpdate_mgrDemYn').val(null).val(data.customerMgt.mgrDemYn);
        $('#Popup_CustomerUpdate_smsRctYn').val(null).val(data.smsRctYn);
        $('#Popup_CustomerUpdate_emailRctYn').val(null).val(data.emailRctYn);
    }

    function _validate() {
        if (!data.customerId || !data.custName || !data.loginId || !data.mblPhoneNo) {
            toastr.warning(_msg.noResult, _msg.customerUpdate);
            return false;
        }
        let carName = $('#Popup_CustomerUpdate_carName').val();
        if (!carName || carName === '') {
            toastr.warning(_msg.checkCarModel, _msg.customerUpdate);
            return false;
        }
        data.carModel.carModelId = $('#Popup_CustomerUpdate_carModelId').val();
        data.carName = $('#Popup_CustomerUpdate_carName').val().trim();

        let carNumber = $('#Popup_CustomerUpdate_carNumber').val();
        if (carNumber) {
            data.carNumber = carNumber;
        }

        let cutGrdCode = $('#Popup_CustomerUpdate_cutGrdCode').val();
        if (!cutGrdCode || cutGrdCode === '') {
            toastr.warning(_msg.checkGrade, _msg.customerUpdate);
            return false;
        }
        data.customerMgt.cutGrdCode = $('#Popup_CustomerUpdate_cutGrdCode').val();

        let cutManageCode = $('#Popup_CustomerUpdate_cutManageCode').val();
        if (!cutManageCode || cutManageCode === '') {
            toastr.warning(_msg.checkUseGrade, _msg.customerUpdate);
            return false;
        }
        data.customerMgt.cutManageCode = $('#Popup_CustomerUpdate_cutManageCode').val();

        let mgrDemYn = $('#Popup_CustomerUpdate_mgrDemYn').val();
        if (!mgrDemYn || mgrDemYn === '') {
            toastr.warning(_msg.checkDemotion, _msg.customerUpdate);
            return false;
        }
        data.customerMgt.mgrDemYn = $('#Popup_CustomerUpdate_mgrDemYn').val();

        let smsRctYn = $('#Popup_CustomerUpdate_smsRctYn').val();
        if (!smsRctYn || smsRctYn === '') {
            toastr.warning(_msg.checkSms, _msg.customerUpdate);
            return false;
        }
        data.smsRctYn = $('#Popup_CustomerUpdate_smsRctYn').val();

        let emailRctYn = $('#Popup_CustomerUpdate_emailRctYn').val();
        if (!emailRctYn || emailRctYn === '') {
            toastr.warning(_msg.checkEmail, _msg.customerUpdate);
            return false;
        }
        data.emailRctYn = $('#Popup_CustomerUpdate_emailRctYn').val();

        return true;
    }

    function _customerUpdate() {
        swal({
            title: _msg.customerMgmt,
            text: _msg.confirmChange,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: _msg.modify,
            cancelButtonText: _msg.cancel
        }, function () {
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + '/ws/customer/' + data.customerId,
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (jsonData) {
                    if (jsonData.status === 'SUCCESS') {
                        toastr.success(_commonMsg.successModify, _msg.customerMgmt);
                        $('#Popup_CustomerUpdate').modal('toggle');
                        cbFunc();
                    } else {
                        toastr.error(_commonMsg.failModify, _msg.customerMgmt);
                    }
                },
                error: function (xhRequest, ErrorText, thrownError) {
                    toastr.error(_commonMsg.failModify, _msg.customerMgmt);
                }
            });
        });
    }

    return {
        init: _init,
        show: _show,
    };
}();
