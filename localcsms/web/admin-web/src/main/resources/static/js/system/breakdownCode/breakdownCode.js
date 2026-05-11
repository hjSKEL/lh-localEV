/**
 * 시스템관리 - 메뉴관리
 */
var systemBreakdownCodeJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };

   	function _init() {
   		_initEvent();
   	    _searchBreakDownCodeRoot();
   	};

   	function _initEvent(){
   		//
		$("#btnForm").click(function(){
			//
			_registerOnClick();
		});
		$("#btnBreakDownCodeUpdate").click(function(){
			_updateBreakDownCodeOnClick();
		});
		$("#btnBreakDownCodeSubRegister").click(function(){
			_registerBreakDownCodeSubOnClick();
		});
		$("#btnBreakDownCodeRegister").click(function(){
			_registerBreakDownCodeOnClick();
		});

		$("#btnBreakDownCodeUpdate").hide();
		$("#btnBreakDownCodeSubRegister").hide();
		$("#middleCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
   	};

   	function _updateBreakDownCodeOnClick () {


   		swal({
            title: _msg.breakdownCodeMgmt,
            text: _msg.confirmChangeBreakdownCode,
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
    			url : _ctx + "/ws/breakdownCode",
    			contentType:"application/json",
    			dataType : 'json' ,
    			data : JSON.stringify(data),
    			success : function(jsonData) {
    				if(jsonData.status == 'SUCCESS'){
    					_searchBreakDownCodeRoot();
    					toastr.success(_commonMsg.successModify, _msg.breakdownCodeMgmt);
    				}else{
    					toastr.error(_commonMsg.failModify, _msg.breakdownCodeMgmt);
    				}
    			},
    			error : function(xhRequest, ErrorText, thrownError) {
    				//
    				toastr.error(_commonMsg.failModify, _msg.breakdownCodeMgmt);
    			}
    		});
        });
   	}

   	function _registerBreakDownCodeOnClick () {
   		if(!_validate()){
			return ;
		}

		$.ajax({
			type : 'POST' ,
			method : 'POST',
			url : _ctx + "/ws/breakdownCode",
			contentType:"application/json",
			dataType : 'json' ,
			data : JSON.stringify(data),
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					toastr.success(_commonMsg.successRegister, _msg.breakdownCodeMgmt);
					_searchBreakDownCodeRoot();
				}else{
					toastr.error(_commonMsg.failRegister, _msg.breakdownCodeMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				toastr.error(_commonMsg.failRegister, _msg.breakdownCodeMgmt);
			}
		});
   	}


   	function _registerBreakDownCodeSubOnClick () {
   		//
   		let level = Number($("#level").val()) + 1;
   		$("#level").val(level);
   		$("#btnBreakDownCodeUpdate").hide();
   		$("#btnBreakDownCodeSubRegister").hide();
   		$("#btnBreakDownCodeRegister").show();

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

		data.breakdownCodeId = $("#breakdownCodeId").html();
		data.majorCategoryName = $("#majorCategoryName").val();
		data.middleCategoryName = $("#middleCategoryName").val();
		data.minorCategoryName = $("#minorCategoryName").val();
		data.minorDetailCategoryName = $("#minorDetailCategoryName").val();
		data.deleteYn = $("#deleteYn").val();
		data.level = $("#level").val();
		return true;
	};


   	function _searchBreakDownCodeRoot () {
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/breakdownCode/list?param1=&param2=0000000",
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayBreakDownCode(jsonData);
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

	var majorBreakDownCodeInfo = '';
	function _majorBreakDownCodeInfo (index) {
		_clearInputData();
		$("#breakdownCodeId").html(majorBreakDownCodeInfo[index].breakdownCodeId);
		$("#majorCategoryName").val(majorBreakDownCodeInfo[index].majorCategoryName);
		$("#deleteYn").val(majorBreakDownCodeInfo[index].deleteYn);

		$("#btnBreakDownCodeRegister").hide();
		$("#btnBreakDownCodeSubRegister").show();
		$("#middleCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(1);
	}

   	function _displayBreakDownCode(jsonData){
   		//console.log(jsonData);
   		$("#breakDownCodeRoot").empty();
   		var html = '';
   		var result = jsonData;
   		majorBreakDownCodeInfo = result;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<li class="dd-item" data-id="' + result[i].breakdownCodeId + '">';
   			html += '<button id="collapse_' + result[i].breakdownCodeId + '" data-action="collapse" onclick="systemBreakdownCodeJs.hideTwoDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" style="display: none;">Collapse</button>';
   			html += '<button id="expand_' + result[i].breakdownCodeId + '" data-action="expand" onclick="systemBreakdownCodeJs.displayTwoDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" >Expand</button>';
   			html += '<div class="dd-handle" onclick="systemBreakdownCodeJs.majorBreakDownCodeInfo(' + i + ');">';
   			html += '<span class="pull-right">' + result[i].breakdownCodeId + '</span>';
   			html += '<span class="label label-info"></span> (' + result[i].breakdownCodeId.toString().substring(0,2) + ') '+ result[i].majorCategoryName;
   			html += '</div>';
   			html += '<ol class="dd-list" id="' + result[i].breakdownCodeId + '_sub"></ol>';
   			html += '</li>';
   			$("#breakDownCodeRoot").append(html);
   		}
   	};

	var middleBreakDownCodeInfo = '';
	function _middleBreakDownCodeInfo (index) {
		_clearInputData();
		$("#breakdownCodeId").html(middleBreakDownCodeInfo[index].breakdownCodeId);
		$("#majorCategoryName").val(middleBreakDownCodeInfo[index].majorCategoryName);
		$("#middleCategoryName").val(middleBreakDownCodeInfo[index].middleCategoryName);
		$("#deleteYn").val(middleBreakDownCodeInfo[index].deleteYn);

		$("#btnBreakDownCodeRegister").hide();
		$("#btnBreakDownCodeSubRegister").show();
		$("#majorCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(2);
	}

	function _clearInputData () {
		$("#breakdownCodeId").html('');
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
		$("#btnBreakDownCodeRegister").show();
		$("#btnBreakDownCodeUpdate").show();
		$("#btnBreakDownCodeSubRegister").hide();

	}

	function lpad(str, padLen, padStr) {
	    str += ""; // 문자로
	    padStr += ""; // 문자로
	    while (str.length < padLen)
	        str = padStr + str;
	    str = str.length >= padLen ? str.substring(0, padLen) : str;
	    return str;
	}

   	function _displayTwoDepth(majorBreakDownCode) {
   		majorBreakDownCode = lpad(majorBreakDownCode, 9, '0');
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/breakdownCode/list?param1=" + majorBreakDownCode.toString().substring(0,2) + "&param2=00000",
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				$("#"+majorBreakDownCode + "_sub").empty();
   				var html = '';
   		   		var result = jsonData;
   		   		middleBreakDownCodeInfo = result;
   		   		for(var i=0, length = result.length ; i < length ; ++i){
   		   			if (i == 0) {
   		   				continue;
   		   			}
   		   			html = '<li class="dd-item" data-id="' + result[i].breakdownCodeId + '">';
   		   			html += '<button id="collapse_' + result[i].breakdownCodeId + '" data-action="collapse" onclick="systemBreakdownCodeJs.hideThreeDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" style="display: none;">Collapse</button>';
   		   			html += '<button id="expand_' + result[i].breakdownCodeId + '" data-action="expand" onclick="systemBreakdownCodeJs.displayThreeDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" >Expand</button>';
   		   			html += '<div class="dd-handle" onclick="systemBreakdownCodeJs.middleBreakDownCodeInfo(' + i + ');">';
   		   			html += '<span class="pull-right">' + result[i].breakdownCodeId + '</span>';
   		   			html += '<span class="label label-info"></span> (' + result[i].breakdownCodeId.toString().substring(2,4) + ') ' + result[i].middleCategoryName;
   		   			html += '</div>';
   		   			html += '<ol class="dd-list" id="' + result[i].breakdownCodeId + '_sub"></ol>';
   		   			html += '</li>';
   		   			$("#"+majorBreakDownCode + "_sub").append(html);
   		   		}
   		   		$("#expand_"+ majorBreakDownCode).css('display' , 'none');
   		   		$("#collapse_"+ majorBreakDownCode).css('display' , 'block');
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   			}
   		});
   	}

   	function _hideTwoDepth(majorBreakDownCode) {
   		majorBreakDownCode = lpad(majorBreakDownCode, 9, '0');
   		$("#"+majorBreakDownCode + "_sub").empty();
   		$("#expand_"+ majorBreakDownCode).css('display' , 'block');
   		$("#collapse_"+ majorBreakDownCode).css('display' , 'none');
   	}

	var minorBreakDownCodeInfo = '';
	function _minorBreakDownCodeInfo (index) {
		_clearInputData();
		$("#breakdownCodeId").html(minorBreakDownCodeInfo[index].breakdownCodeId);
		$("#majorCategoryName").val(minorBreakDownCodeInfo[index].majorCategoryName);
		$("#middleCategoryName").val(minorBreakDownCodeInfo[index].middleCategoryName);
		$("#minorCategoryName").val(minorBreakDownCodeInfo[index].minorCategoryName);
		$("#deleteYn").val(minorBreakDownCodeInfo[index].deleteYn);

		$("#btnBreakDownCodeRegister").hide();
		$("#btnBreakDownCodeSubRegister").show();
		$("#majorCategoryName").attr("readonly",true);
		$("#middleCategoryName").attr("readonly",true);
		$("#minorDetailCategoryName").attr("readonly",true);
		$("#level").val(3);
	}

	function _displayThreeDepth(middleBreakDownCode) {
		middleBreakDownCode = lpad(middleBreakDownCode, 9, '0');
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/breakdownCode/list?param1=" + middleBreakDownCode.toString().substring(0,4) + "&param2=000",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				$("#"+middleBreakDownCode + "_sub").empty();
				var html = '';
				var result = jsonData;
				minorBreakDownCodeInfo = result;
				for(var i=0, length = result.length ; i < length ; ++i){
					if (i == 0) {
						continue;
					}
					html = '<li class="dd-item" data-id="' + result[i].breakdownCodeId + '">';
					html += '<button id="collapse_' + result[i].breakdownCodeId + '" data-action="collapse" onclick="systemBreakdownCodeJs.hideFourDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" style="display: none;">Collapse</button>';
					html += '<button id="expand_' + result[i].breakdownCodeId + '" data-action="expand" onclick="systemBreakdownCodeJs.displayFourDepth(' + Number(result[i].breakdownCodeId) + ')" type="button" >Expand</button>';
					html += '<div class="dd-handle" onclick="systemBreakdownCodeJs.minorBreakDownCodeInfo(' + i + ');">';
					html += '<span class="pull-right">' + result[i].breakdownCodeId + '</span>';
					html += '<span class="label label-info"></span> (' + result[i].breakdownCodeId.toString().substring(4,6) + ') ' + result[i].minorCategoryName;
					html += '</div>';
					html += '<ol class="dd-list" id="' + result[i].breakdownCodeId + '_sub"></ol>';
					html += '</li>';
					$("#"+middleBreakDownCode + "_sub").append(html);
				}
				$("#expand_"+ middleBreakDownCode).css('display' , 'none');
   		   		$("#collapse_"+ middleBreakDownCode).css('display' , 'block');
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});
   	}

   	function _hideThreeDepth(middleBreakDownCode) {
   		middleBreakDownCode = lpad(middleBreakDownCode, 9, '0');
   		$("#"+middleBreakDownCode + "_sub").empty();
   		$("#expand_"+ middleBreakDownCode).css('display' , 'block');
   		$("#collapse_"+ middleBreakDownCode).css('display' , 'none');
   	}

	var minorDetailBreakDownCodeInfo = '';
	function _minorDetailBreakDownCodeInfo (index) {
		_clearInputData();
		$("#breakdownCodeId").html(minorDetailBreakDownCodeInfo[index].breakdownCodeId);
		$("#majorCategoryName").val(minorDetailBreakDownCodeInfo[index].majorCategoryName);
		$("#middleCategoryName").val(minorDetailBreakDownCodeInfo[index].middleCategoryName);
		$("#minorCategoryName").val(minorDetailBreakDownCodeInfo[index].minorCategoryName);
		$("#minorDetailCategoryName").val(minorDetailBreakDownCodeInfo[index].minorDetailCategoryName);
		$("#deleteYn").val(minorDetailBreakDownCodeInfo[index].deleteYn);

		$("#btnBreakDownCodeRegister").hide();
		$("#majorCategoryName").attr("readonly",true);
		$("#middleCategoryName").attr("readonly",true);
		$("#minorCategoryName").attr("readonly",true);
		$("#level").val(4);
	}

	function _displayFourDepth(minorBreakDownCode) {
		minorBreakDownCode = lpad(minorBreakDownCode, 9, '0');
		$.ajax({
			type: 'GET' ,
			url : _ctx + "/ws/breakdownCode/list?param1=" + minorBreakDownCode.toString().substring(0,6) + "&param2=",
			dataType : 'json' ,
			success : function(jsonData, textStatus, jqXHR) {
				$("#"+minorBreakDownCode + "_sub").empty();
				var html = '';
				var result = jsonData;
				minorDetailBreakDownCodeInfo = result;
				for(var i=0, length = result.length ; i < length ; ++i){
					if (i == 0) {
						continue;
					}
					html = '<li class="dd-item" data-id="' + result[i].breakdownCodeId + '">';
//					html += '<button data-action="collapse" type="button" style="display: none;">Collapse</button>';
//					html += '<button data-action="expand" onclick="systemBreakdownCodeJs.displayTwoDepth(' + result[i].breakdownCodeId + ')" type="button" >Expand</button>';
					html += '<div class="dd-handle" onclick="systemBreakdownCodeJs.minorDetailBreakDownCodeInfo(' + i + ');">';
					html += '<span class="pull-right">' + result[i].breakdownCodeId + '</span>';
					html += '<span class="label label-info"></span> ('  + result[i].breakdownCodeId.toString().substring(6,9) + ') ' + result[i].minorDetailCategoryName;
					html += '</div>';
					html += '<ol class="dd-list" id="' + result[i].breakdownCodeId + '_sub"></ol>';
					html += '</li>';
					$("#"+minorBreakDownCode + "_sub").append(html);
				}
				$("#expand_"+ minorBreakDownCode).css('display' , 'none');
   		   		$("#collapse_"+ minorBreakDownCode).css('display' , 'block');
			} ,
			error : function(xhRequest, ErrorText, thrownError) {
				//
			}
		});

	}

   	function _hideFourDepth(minorBreakDownCode) {
   		minorBreakDownCode = lpad(minorBreakDownCode, 9, '0');
   		$("#"+minorBreakDownCode + "_sub").empty();
   		$("#expand_"+ minorBreakDownCode).css('display' , 'block');
   		$("#collapse_"+ minorBreakDownCode).css('display' , 'none');
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
   		displayFourDepth : _displayFourDepth,
   		hideFourDepth : _hideFourDepth,
   		majorBreakDownCodeInfo : _majorBreakDownCodeInfo,
   		middleBreakDownCodeInfo : _middleBreakDownCodeInfo,
   		minorBreakDownCodeInfo : _minorBreakDownCodeInfo,
   		minorDetailBreakDownCodeInfo : _minorDetailBreakDownCodeInfo,
   		searchDetail : _searchDetail
   	};
}();
