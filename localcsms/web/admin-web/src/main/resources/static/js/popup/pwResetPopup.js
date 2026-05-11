/**
 * 고객 패스워드 리셋
 */
let pwResetPopupJs = function () {
    'use strict';

    let cbFunc;
    let data;

    function _init(callbackFunc, json) {
        cbFunc = callbackFunc;
        data = json;
        $('#Popup_PwReset_customerId').html(json.customerId);
        $('#Popup_PwReset_custName').html(json.custName);
        $('#Popup_PwReset_newPw').val(null);
        _initEvent();
    }

    function _initEvent() {
        $('#Popup_PwReset_btnReset').unbind('click');
        $('#Popup_PwReset_btnReset').click(function() {
            let newPw = $('#Popup_PwReset_newPw').val();
            if(!newPw || newPw === '') {
                toastr.warning(_msg.pwEnter, _msg.pwReset);
                return;
            }
            if(newPw.length < 8 || newPw.length > 16) {
                toastr.warning(_msg.pwLength, _msg.pwReset);
                return;
            }
            data.userPwd = newPw;
            swal({
                title: _msg.customerMgmt,
                text: _msg.pwConfirm,
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#DD6B55',
                confirmButtonText: _msg.modify,
                cancelButtonText: _msg.cancel
            }, function () {
                $.ajax({
                    type : 'PUT' ,
                    method : 'PUT',
                    url: _ctx + '/ws/customer/' + data.customerId,
                    contentType:'application/json',
                    dataType: 'json' ,
                    data: JSON.stringify(data),
                    success: function(jsonData) {
                        if (jsonData.status === 'SUCCESS') {
                            toastr.success(_commonMsg.successModify, _msg.customerMgmt);
                            $('#Popup_PwReset').modal('toggle');
                            cbFunc();
                        } else {
                            toastr.error(_commonMsg.failModify, _msg.customerMgmt);
                        }
                    },
                    error : function(xhRequest, ErrorText, thrownError) {
                        toastr.error(_commonMsg.failModify, _msg.customerMgmt);
                    }
                });
            })
        });
    }

    return {
        init: _init,
    };
}();
