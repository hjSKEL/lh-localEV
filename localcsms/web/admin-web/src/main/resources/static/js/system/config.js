/**
 * 시스템설정 조회/저장
 */
let systemConfigJs = function(){
    'use strict';

    let data = {};

    //섹션별 편집 대상 필드(수정 버튼 클릭 시 readonly/disabled 해제 범위)
    let sectionFields = {
        Abnormal: ['#chargingTimeBasedOnAbnormalCharging', '#abnormalChargeAmount'],
        Operation: ['#emplacement', '#serverAddress', '#useHomeNet', '#fileStorePath', '#customerSupport'],
        Backup: ['#dbBackupPath', '#dbBackupCycle', '#dbBackupRetentionPeriod']
    };

    function _init(){
        _initEvent();
        _search();
    }

    function _initEvent(){
        //
        $.each(sectionFields, function(section){
            $('#btnModify' + section).click(function(){
                _setEditable(section, true);
            });
            $('#btnSave' + section).click(function(){
                _updateOnClick(section);
            });
        });

        //홈넷적용
        let homeNetCodes = parent.commonCodeJs.getCodesByParentCode('HNET00');
        for(let i = 0, length = homeNetCodes.length; i < length; ++i){
            $('#useHomeNet').append('<option value=\'' + homeNetCodes[i].code + '\'>' + homeNetCodes[i].codeName + '</option>');
        }
    }

    //section의 input/select를 편집 가능/readonly 상태로 전환하고 수정·저장 버튼을 토글
    function _setEditable(section, editable){
        $.each(sectionFields[section], function(i, selector){
            if(selector === '#useHomeNet'){
                $(selector).prop('disabled', !editable);
            }else{
                $(selector).prop('readonly', !editable);
            }
        });
        $('#btnModify' + section).toggle(!editable);
        $('#btnSave' + section).toggle(editable);
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

    //섹션별 정합성 검사 - 저장하려는 섹션의 필드만 검사(다른 섹션은 이미 저장된 값 그대로라 재검사 불필요)
    let validators = {
        Abnormal: function(){
            if($('#chargingTimeBasedOnAbnormalCharging').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterChargingTime, 'warning');
                return false;
            }
            if(Number($('#chargingTimeBasedOnAbnormalCharging').val()) < 20){
                swal(_commonMsg.validationCheck, _msg.chargingTimeMin, 'warning');
                return false;
            }
            if($('#abnormalChargeAmount').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterAbnormalChargeAmount, 'warning');
                return false;
            }
            if(Number($('#abnormalChargeAmount').val()) < 100000){
                swal(_commonMsg.validationCheck, _msg.abnormalChargeAmountMin, 'warning');
                return false;
            }
            return true;
        },
        Operation: function(){
            if($('#emplacement').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterEmplacement, 'warning');
                return false;
            }
            if($('#serverAddress').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterServerAddress, 'warning');
                return false;
            }
            if($('#useHomeNet').val() === ''){
                swal(_commonMsg.validationCheck, _msg.selectUseHomeNet, 'warning');
                return false;
            }
            if($('#fileStorePath').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterFileStorePath, 'warning');
                return false;
            }
            return true;
        },
        Backup: function(){
            if($('#dbBackupPath').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterDbBackupPath, 'warning');
                return false;
            }
            if($('#dbBackupCycle').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterDbBackupCycle, 'warning');
                return false;
            }
            if($('#dbBackupRetentionPeriod').val() === ''){
                swal(_commonMsg.validationCheck, _msg.enterDbBackupRetentionPeriod, 'warning');
                return false;
            }
            return true;
        }
    };

    //저장 API는 설정 전체를 교체하므로, 검사 통과 후 현재 화면의 모든 필드 값을 담아 전송
    function _collectData(){
        data = {
            chargingTimeBasedOnAbnormalCharging: $('#chargingTimeBasedOnAbnormalCharging').val(),
            abnormalChargeAmount: $('#abnormalChargeAmount').val(),
            emplacement: $('#emplacement').val(),
            serverAddress: $('#serverAddress').val(),
            useHomeNet: $('#useHomeNet').val(),
            fileStorePath: $('#fileStorePath').val(),
            customerSupport: $('#customerSupport').val(),
            dbBackupPath: $('#dbBackupPath').val(),
            dbBackupCycle: $('#dbBackupCycle').val(),
            dbBackupRetentionPeriod: $('#dbBackupRetentionPeriod').val()
        };
    }

    function _updateOnClick(section){
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
            if(!validators[section]()){
                return;
            }
            _collectData();
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
                            _setEditable(section, false);
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
