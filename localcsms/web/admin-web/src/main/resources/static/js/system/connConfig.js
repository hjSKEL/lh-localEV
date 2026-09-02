/**
 * 연결설정 조회/저장
 */
let connConfigJs = function(){
    'use strict';

    let data = {};

    function _init(){
        _initEvent();
        _search();
    }

    function _initEvent(){
        //
        $('#btnSave').click(function(){
            _updateOnClick();
        });

        //로컬서버 운영모드
        let opModeCodes = parent.commonCodeJs.getCodesByParentCode('OPMD00');
        for(let i = 0, length = opModeCodes.length; i < length; ++i){
            $('#localOperationTypeGroup').append(
                '<input type="radio" name="localOperationType" class="localOperationTypeRadio" id="localOperationType' + opModeCodes[i].code + '" value="' + opModeCodes[i].code + '"> ' +
                '<label for="localOperationType' + opModeCodes[i].code + '">' + opModeCodes[i].codeName + '</label>&nbsp;&nbsp'
            );
        }
        $(document).on('change', '.localOperationTypeRadio', function(){
            _toggleCsmsAddressByOperationType();
        });
    }

    //로컬모드(OPMD01) 선택시 CPO CSMS 주소 비활성화, CPO모드(OPMD02) 선택시 LH CSMS 주소 비활성화
    function _toggleCsmsAddressByOperationType(){
        let operationType = $('.localOperationTypeRadio:checked').val();
        $('#cpoCsmsAddress').prop('disabled', operationType === 'OPMD01');
        $('#lhCsmsAddress').prop('disabled', operationType === 'OPMD02');
    }

    function _search(){
        //
        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/system/connConfig',
            dataType: 'json',
            success: function(jsonData, textStatus, jqXHR){
                _displayConnConfig(jsonData);
            }, error: function(xhRequest, ErrorText, thrownError){
                //
            }
        });
    }

    function _displayConnConfig(jsonData){
        if(!jsonData){
            return;
        }
        data = jsonData;
        $('#lhCsmsAddress').val(jsonData.lhCsmsAddress);
        $('#cpoCsmsAddress').val(jsonData.cpoCsmsAddress);
        $('#localSystemId').val(jsonData.localSystemId);
        $('#localSystemSN').val(jsonData.localSystemSn);
        $('.localOperationTypeRadio[value="' + jsonData.localOperationType + '"]').prop('checked', true);
        _toggleCsmsAddressByOperationType();
    }

    function _validate(){
        data = {};

        if(!$('#lhCsmsAddress').prop('disabled') && $('#lhCsmsAddress').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterLhCsmsAddress, 'warning');
            return false;
        }
        data.lhCsmsAddress = $('#lhCsmsAddress').val();
        data.cpoCsmsAddress = $('#cpoCsmsAddress').val();

        if($('#localSystemId').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterLocalSystemId, 'warning');
            return false;
        }
        data.localSystemId = $('#localSystemId').val();
        data.localSystemSn = $('#localSystemSN').val();
        data.localOperationType = $('.localOperationTypeRadio:checked').val();

        return true;
    }

    function _updateOnClick(){
        //
        swal({
            title: _msg.connConfigMgmt,
            text: _msg.confirmModifyConnConfig,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: _msg.btnModify,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function(){
            if(!_validate()){
                return;
            }
            $.ajax({
                type: 'PUT',
                method: 'PUT',
                url: _ctx + '/ws/system/connConfig',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(data),
                success: function(jsonData){
                    if(jsonData.status == 'SUCCESS'){
                        swal({
                            title: _msg.connConfigMgmt,
                            text: _commonMsg.successModify,
                            type: 'success',
                            showCancelButton: false
                        }, function(){
                            _search();
                        });
                    }else{
                        toastr.error(_commonMsg.failModify, _msg.connConfigMgmt);
                    }
                }, error: function(xhRequest, ErrorText, thrownError){
                    toastr.error(_commonMsg.failModify, _msg.connConfigMgmt);
                }
            });
        });
    }

    return {
        init: _init
    };
}();
