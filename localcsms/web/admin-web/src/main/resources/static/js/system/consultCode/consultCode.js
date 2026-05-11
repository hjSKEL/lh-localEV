/**
 * 시스템관리 - 상담분류코드
 */
var systemConsultCodeJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   	    _searchConsultCodeRoot();
   	};

   	function _initEvent(){
   		//
		$("#btnForm").click(function(){
			//
			_registerOnClick();
		});
		$("#btnConsultCodeUpdate").click(function(){
			_updateConsultCodeOnClick();
		});
		$("#btnConsultCodeSubRegister").click(function(){
			_registerConsultCodeSubOnClick();
		});
		$("#btnConsultCodeRegister").click(function(){
			_registerConsultCodeOnClick();
		});

		$("#btnConsultCodeUpdate").hide();
		$("#btnConsultCodeSubRegister").hide();
		$("#middleCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
   	};

   	function _updateConsultCodeOnClick () {


   		swal({
            title: _msg.consultCodeMgmt,
            text: _msg.confirmChangeConsultCode,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnModify,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: true
        }, function () {
        	if(!_validate()){
    			return ;
    		}

    		$.ajax({
    			type : 'PUT' ,
    			method : 'PUT',
    			url : _ctx + "/ws/consultCode",
    			contentType:"application/json",
    			dataType : 'json' ,
    			data : JSON.stringify(data),
    			success : function(jsonData) {
    				if(jsonData.status == 'SUCCESS'){
    					_searchConsultCodeRoot();
    					toastr.success(_commonMsg.successModify, _msg.consultCodeMgmt);
    				}else{
    					toastr.error(_commonMsg.failModify, _msg.consultCodeMgmt);
    				}
    			},
    			error : function(xhRequest, ErrorText, thrownError) {
    				//
    				toastr.error(_commonMsg.failModify, _msg.consultCodeMgmt);
    			}
    		});
        });
   	}

   	function _registerConsultCodeOnClick () {
   		if(!_validate()){
			return ;
		}

		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/consultCode",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					toastr.success(_commonMsg.successRegister, _msg.consultCodeMgmt);
					_searchConsultCodeRoot();
				}else{
					toastr.error(_commonMsg.failRegister, _msg.consultCodeMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				toastr.error(_commonMsg.failRegister, _msg.consultCodeMgmt);
			}
		});
   	}


   	function _registerConsultCodeSubOnClick () {
   		//
		let level = Number($("#level").val());
		$("#level").val(level + 1);

   		$("#btnConsultCodeUpdate").hide();
   		$("#btnConsultCodeSubRegister").hide();
   		$("#btnConsultCodeRegister").show();

   		if ($("#middleCategoryName").val() === '') {
   			$("#majorCategoryName").attr("readonly",true);
   			$("#middleCategoryName").removeAttr("readonly");
   			return;
   		}

   		if ($("#minorCategoryName").val() === '') {
   			$("#middleCategoryName").attr("readonly",true);
   			$("#minorCategoryName").removeAttr("readonly");
   			return;
   		}

   		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").removeAttr("readonly");
   	}

function _validate(){

		data = {};

		data.consultCodeId = $("#consultCodeId").html();
		data.majorCategoryName = $("#majorCategoryName").val();
		data.middleCategoryName = $("#middleCategoryName").val();
		data.minorCategoryName = $("#minorCategoryName").val();
		data.deleteYn = $("#deleteYn").val();
		data.level = $("#level").val();
		return true;
	};


   	function _searchConsultCodeRoot () {
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/consultCode/list?param1=&param2=00000",
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayConsultCode(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   			}
   		});
   	}

	function _registerOnClick(){
		//
		self.location= _ctx + "/system/menu";
	};

	var majorConsultCodeInfo = '';
	function _majorConsultCodeInfo (index) {
		_clearInputData();
		//console.log(majorConsultCodeInfo[index]);
		$("#consultCodeId").html(majorConsultCodeInfo[index].consultCodeId);
		$("#majorCategoryName").val(majorConsultCodeInfo[index].majorCategoryName);
		$("#deleteYn").val(majorConsultCodeInfo[index].deleteYn);

		$("#btnConsultCodeRegister").hide();
		$("#btnConsultCodeSubRegister").show();
		$("#middleCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(1);
	}

   	function _displayConsultCode(jsonData){
   		$("#consultCodeRoot").empty();
   		var html = '';
   		var result = jsonData;
   		majorConsultCodeInfo = result;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<li class="dd-item" data-id="' + result[i].consultCodeId + '">';
   			html += '<button id="collapse_' + result[i].consultCodeId + '" data-action="collapse" onclick="systemConsultCodeJs.hideTwoDepth(' + result[i].consultCodeId + ')" type="button" style="display: none;">Collapse</button>';
   			html += '<button id="expand_' + result[i].consultCodeId + '" data-action="expand" onclick="systemConsultCodeJs.displayTwoDepth(' + result[i].consultCodeId + ')" type="button" >Expand</button>';
   			html += '<div class="dd-handle" onclick="systemConsultCodeJs.majorConsultCodeInfo(' + i + ');">';
   			html += '<span class="pull-right">' + result[i].consultCodeId + '</span>';
   			html += '<span class="label label-info"></span> (' + result[i].consultCodeId.toString().substring(0,2) + ') '+ result[i].majorCategoryName;
   			html += '</div>';
   			html += '<ol class="dd-list" id="' + result[i].consultCodeId + '_sub"></ol>';
   			html += '</li>';
   			$("#consultCodeRoot").append(html);
   		}
   	};

	var middleConsultCodeInfo = '';
	function _middleConsultCodeInfo (index) {
		_clearInputData();
		$("#consultCodeId").html(middleConsultCodeInfo[index].consultCodeId);
		$("#majorCategoryName").val(middleConsultCodeInfo[index].majorCategoryName);
		$("#middleCategoryName").val(middleConsultCodeInfo[index].middleCategoryName);
		$("#deleteYn").val(middleConsultCodeInfo[index].deleteYn);

		$("#btnConsultCodeRegister").hide();
		$("#btnConsultCodeSubRegister").show();
		$("#majorCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(2);
	}

	function _clearInputData () {
		$("#consultCodeId").html('');
		$("#majorCategoryName").show();
		$("#majorCategoryName").removeAttr("readonly");
		$("#majorCategoryName").val('');
		$("#middleCategoryName").show();
		$("#middleCategoryName").removeAttr("readonly");
		$("#middleCategoryName").val('');
		$("#minorCategoryName").show();
		$("#minorCategoryName").removeAttr("readonly");
		$("#minorCategoryName").val('');
		$("#minorDetailCategoryName").removeAttr("readonly");
		$("#minorDetailCategoryName").show();
		$("#minorDetailCategoryName").val('');
		$("#deleteYn").val('N');
		$("#btnConsultCodeRegister").show();
		$("#btnConsultCodeUpdate").show();
		$("#btnConsultCodeSubRegister").hide();

	}

   	function _displayTwoDepth(majorConsultCode) {
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/consultCode/list?param1=" + majorConsultCode.toString().substring(0,2) + "&param2=000",
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				$("#"+majorConsultCode + "_sub").empty();
   				var html = '';
   		   		var result = jsonData;
   		   		middleConsultCodeInfo = result;
   		   		for(var i=0, length = result.length ; i < length ; ++i){
   		   			if (i == 0) {
   		   				continue;
   		   			}
   		   			html = '<li class="dd-item" data-id="' + result[i].consultCodeId + '">';
   		   			html += '<button id="collapse_' + result[i].consultCodeId + '" data-action="collapse" onclick="systemConsultCodeJs.hideThreeDepth(' + result[i].consultCodeId + ')" type="button" style="display: none;">Collapse</button>';
   		   			html += '<button id="expand_' + result[i].consultCodeId + '" data-action="expand" onclick="systemConsultCodeJs.displayThreeDepth(' + result[i].consultCodeId + ')" type="button" >Expand</button>';
   		   			html += '<div class="dd-handle" onclick="systemConsultCodeJs.middleConsultCodeInfo(' + i + ');">';
   		   			html += '<span class="pull-right">' + result[i].consultCodeId + '</span>';
   		   			html += '<span class="label label-info"></span> (' + result[i].consultCodeId.toString().substring(2,4) + ') ' + result[i].middleCategoryName;
   		   			html += '</div>';
   		   			html += '<ol class="dd-list" id="' + result[i].consultCodeId + '_sub"></ol>';
   		   			html += '</li>';
   		   			$("#"+majorConsultCode + "_sub").append(html);
   		   		}
   		   		$("#expand_"+ majorConsultCode).css('display' , 'none');
   		   		$("#collapse_"+ majorConsultCode).css('display' , 'block');
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   			}
   		});
   	}

   	function _hideTwoDepth(majorConsultCode) {
   		$("#"+majorConsultCode + "_sub").empty();
   		$("#expand_"+ majorConsultCode).css('display' , 'block');
   		$("#collapse_"+ majorConsultCode).css('display' , 'none');
   	}

	var minorConsultCodeInfo = '';
	function _minorConsultCodeInfo (index) {
		_clearInputData();
		$("#consultCodeId").html(minorConsultCodeInfo[index].consultCodeId);
		$("#majorCategoryName").val(minorConsultCodeInfo[index].majorCategoryName);
		$("#middleCategoryName").val(minorConsultCodeInfo[index].middleCategoryName);
		$("#minorCategoryName").val(minorConsultCodeInfo[index].minorCategoryName);
		$("#deleteYn").val(minorConsultCodeInfo[index].deleteYn);

		$("#btnConsultCodeRegister").hide();
		$("#btnConsultCodeSubRegister").show();
		$("#majorCategoryName").attr("readonly",true);
		$("#middleCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(3);

		$("#btnConsultCodeSubRegister").hide();
	}

	function _displayThreeDepth(middleConsultCode) {
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/consultCode/list?param1=" + middleConsultCode.toString().substring(0,4) + "&param2=",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				$("#"+middleConsultCode + "_sub").empty();
				var html = '';
				var result = jsonData;
				minorConsultCodeInfo = result;
				for(var i=0, length = result.length ; i < length ; ++i){
					if (i == 0) {
						continue;
					}
					html = '<li class="dd-item" data-id="' + result[i].consultCodeId + '">';
					html += '<div class="dd-handle" onclick="systemConsultCodeJs.minorConsultCodeInfo(' + i + ');">';
					html += '<span class="pull-right">' + result[i].consultCodeId + '</span>';
					html += '<span class="label label-info"></span> (' + result[i].consultCodeId.toString().substring(4,7) + ') ' + result[i].minorCategoryName;
					html += '</div>';
					html += '<ol class="dd-list" id="' + result[i].consultCodeId + '_sub"></ol>';
					html += '</li>';
					$("#"+middleConsultCode + "_sub").append(html);
				}
				$("#expand_"+ middleConsultCode).css('display' , 'none');
   		   		$("#collapse_"+ middleConsultCode).css('display' , 'block');
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});

   	}

   	function _hideThreeDepth(middleConsultCode) {
   		$("#"+middleConsultCode + "_sub").empty();
   		$("#expand_"+ middleConsultCode).css('display' , 'block');
   		$("#collapse_"+ middleConsultCode).css('display' , 'none');
   	}


    function _searchDetail(menuId){
        //
        self.location= _ctx + "/system/menu/detail?menuId=" + menuId;
    };

   	return {
   		init : _init,
   		displayTwoDepth : _displayTwoDepth,
   		hideTwoDepth : _hideTwoDepth,
   		displayThreeDepth : _displayThreeDepth,
   		hideThreeDepth : _hideThreeDepth,
   		majorConsultCodeInfo : _majorConsultCodeInfo,
   		middleConsultCodeInfo : _middleConsultCodeInfo,
   		minorConsultCodeInfo : _minorConsultCodeInfo,
   		searchDetail : _searchDetail
   	};
}();
