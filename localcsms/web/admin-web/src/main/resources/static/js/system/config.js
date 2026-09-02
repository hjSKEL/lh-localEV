/**
 * 시스템설정 조회/저장
 */
let systemConfigJs = function(){
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

        //홈넷적용
        let homeNetCodes = parent.commonCodeJs.getCodesByParentCode('HNET00');
        for(let i = 0, length = homeNetCodes.length; i < length; ++i){
            $('#useHomeNet').append('<option value=\'' + homeNetCodes[i].code + '\'>' + homeNetCodes[i].codeName + '</option>');
        }
    }

    function _search(){
        //
        $.ajax({
            type: 'GET',
            url: _ctx + '/ws/system/config',
            dataType: 'json',
            success: function(jsonData, textStatus, jqXHR){
                _displaySystemConfig(jsonData);
            }, error: function(xhRequest, ErrorText, thrownError){
                //
            }
        });
    }

    function _displaySystemConfig(jsonData){
        if(!jsonData){
            return;
        }
        data = jsonData;
        $('#chargingTimeBasedOnAbnormalCharging').val(jsonData.chargingTimeBasedOnAbnormalCharging);
        $('#abnormalChargeAmount').val(jsonData.abnormalChargeAmount);
        $('#emplacement').val(jsonData.emplacement);
        $('#serverAddress').val(jsonData.serverAddress);
        $('#useHomeNet').val(jsonData.useHomeNet);
        $('#fileStorePath').val(jsonData.fileStorePath);
        $('#customerSupport').val(jsonData.customerSupport);
        $('#dbBackupPath').val(jsonData.dbBackupPath);
        $('#dbBackupCycle').val(jsonData.dbBackupCycle);
        $('#dbBackupRetentionPeriod').val(jsonData.dbBackupRetentionPeriod);
    }

    function _validate(){
        data = {};

        if($('#chargingTimeBasedOnAbnormalCharging').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterChargingTime, 'warning');
            return false;
        }
        if(Number($('#chargingTimeBasedOnAbnormalCharging').val()) < 20){
            swal(_commonMsg.validationCheck, _msg.chargingTimeMin, 'warning');
            return false;
        }
        data.chargingTimeBasedOnAbnormalCharging = $('#chargingTimeBasedOnAbnormalCharging').val();

        if($('#abnormalChargeAmount').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterAbnormalChargeAmount, 'warning');
            return false;
        }
        if(Number($('#abnormalChargeAmount').val()) < 100000){
            swal(_commonMsg.validationCheck, _msg.abnormalChargeAmountMin, 'warning');
            return false;
        }
        data.abnormalChargeAmount = $('#abnormalChargeAmount').val();

        if($('#emplacement').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterEmplacement, 'warning');
            return false;
        }
        data.emplacement = $('#emplacement').val();

        if($('#serverAddress').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterServerAddress, 'warning');
            return false;
        }
        data.serverAddress = $('#serverAddress').val();

        if($('#useHomeNet').val() === ''){
            swal(_commonMsg.validationCheck, _msg.selectUseHomeNet, 'warning');
            return false;
        }
        data.useHomeNet = $('#useHomeNet').val();

        if($('#fileStorePath').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterFileStorePath, 'warning');
            return false;
        }
        data.fileStorePath = $('#fileStorePath').val();
        data.customerSupport = $('#customerSupport').val();

        if($('#dbBackupPath').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterDbBackupPath, 'warning');
            return false;
        }
        data.dbBackupPath = $('#dbBackupPath').val();

        if($('#dbBackupCycle').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterDbBackupCycle, 'warning');
            return false;
        }
        data.dbBackupCycle = $('#dbBackupCycle').val();

        if($('#dbBackupRetentionPeriod').val() === ''){
            swal(_commonMsg.validationCheck, _msg.enterDbBackupRetentionPeriod, 'warning');
            return false;
        }
        data.dbBackupRetentionPeriod = $('#dbBackupRetentionPeriod').val();

        return true;
    }

    function _updateOnClick(){
        //
        swal({
            title: _msg.systemConfigMgmt,
            text: _msg.confirmModifySystemConfig,
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
                url: _ctx + '/ws/system/config',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(data),
                success: function(jsonData){
                    if(jsonData.status == 'SUCCESS'){
                        swal({
                            title: _msg.systemConfigMgmt,
                            text: _commonMsg.successModify,
                            type: 'success',
                            showCancelButton: false
                        }, function(){
                            _search();
                        });
                    }else{
                        toastr.error(_commonMsg.failModify, _msg.systemConfigMgmt);
                    }
                }, error: function(xhRequest, ErrorText, thrownError){
                    toastr.error(_commonMsg.failModify, _msg.systemConfigMgmt);
                }
            });
        });
    }

    return {
        init: _init
    };
}();
