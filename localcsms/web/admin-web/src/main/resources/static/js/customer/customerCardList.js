/**
 * 고객카드 관리
 */
var customerCardListJs = function(){
    "use strict";

    var data = {
    	searchCond : {}
    };
    
   	function _init() {
   		_initEvent();
   	    _searchOnClick();
   	};

   	function _initEvent(){
   		//
   		$("#btnSearch").click(function(){
   			//
   			_searchOnClick();
   		});
   		//검색조건 Enter키로 검색기능
		$("#sWord").keypress(function(event){
		  if(event.keyCode == 13)
		     {
			  _searchOnClick();
		     }
		});
		
		$("#status").change(function(){
			_searchOnClick();
		});
		
		$("#btnReset").click(function () {
            _searchResetClick();
        });
		//고객카드등록 팝업
//		$("#btnCreateCard").click(function(){
//			$("#Popup_CustomerCardCreate").modal();
//			customerCardCreateJs.init();
//		});
   		
		var errStatusList = parent.commonCodeJs.getCodesByParentCode('MEML00');
		$("#status").append('<option value="" selected>' + _msg.cardStatusAll + '</option>');
		for(var i =0, length = errStatusList.length; i < length ; ++i){
			$("#status").append('<option value="' + errStatusList[i].code + '">' + errStatusList[i].codeName + '</option>');
		}
   	};
   	
   	function _searchResetClick() {
        $("#sWord").val("");
        $("#status").val("");
        $("#searchType").val("ID");
        _searchOnClick();
    }
   	
   	function _searchOnClick(){
   		//
   		pageInfoJs.init('pageInfoJs', 'pagingUl', 10, 20, customerCardListJs.search);
   		data.searchCond.customerId = "";
   		data.searchCond.customerName = "";
   		data.searchCond.cutCardNo = "";
   		var searchType = $("#searchType").val();
   		let searchKey = $("#sWord").val().replace(/-/g, '').trim();
   		switch(searchType){
	   		case "ID":
	   			data.searchCond.customerId = searchKey;
	   			break;
	   		case "NAME":
	   			data.searchCond.customerName = searchKey;
	   			break;
	   		case "CARDNO":
				if(searchKey && searchKey.length != 16) {
					toastr.warning(_msg.cardDigit16, _msg.cardNumber);
					return;
				} else {
					data.searchCond.cutCardNo = searchKey;
				}
	   			break;
	   		default :
   		}
   		data.searchCond.custStatCode = $("#status").val();
   		
   		$("#sWord").val(searchKey);
   		
   		_search();
   	};

   	function _search(){
   		//
   		$("#tBodyList").empty();
   		var html = '<tr style="text-align:center;">';
   		html += '<td colspan="9">' + _commonMsg.searching + '</td>';
   		$("#tBodyList").append(html);

   		var paging = pageInfoJs.getPaging();
   		var param = "?pageNumber=" +(paging.pageNumber - 1) + "&pageItemSize=" +paging.pageItemSize;

   		param += "&customerName=" + data.searchCond.customerName;
   		param += "&customerId=" + data.searchCond.customerId;
   		param += "&cutCardNo=" + data.searchCond.cutCardNo;
   		param += "&custStatCode=" + data.searchCond.custStatCode;
   		
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/customer/card" + param,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				_displayCustomerCard(jsonData);
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
   	};
   	var result;
   	function _displayCustomerCard(jsonData){
   		pageInfoJs.setTotalCount(jsonData.criteria.totalItemCount);

   		$("#totalCount").html(_commonMsg.totalCount.replace('{0}', jsonData.criteria.totalItemCount));
   		$("#tBodyList").empty();
   		var html = '';
   		if(jsonData.criteria.totalItemCount == 0){
   			html = '<tr style="text-align:center;">';
   			html += '<td colspan="9">' + _commonMsg.noData + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   			return ;
   		}
   		result = jsonData.result;
   		for(var i=0, length = result.length ; i < length ; ++i){
   			html = '<tr class="footable-even" style="display: table-row;">';
   			html += '<td class="footable-visible">' + formmatUtilsJs.cardFormat(result[i].cutCardNo) + '</td>';
   			html += '<td class="footable-visible">' + parent.commonCodeJs.getCodeNameBySubCode(result[i].custStatCode)  + '</td>';
   			if (result[i].customerId && result[i].customerId.trim()) {
   				html += '<td class="footable-visible">' + result[i].customerId + '</td>';
   			} else {
   				html += '<td class="footable-visible" style="text-align: center;"><button id="btnCustomerSelect_'+ i  +'" onclick="customerCardListJs.searchCustomerOnClick('+ result[i].cutCardNo +')" class="btn btn-primary">' + _msg.selectCustomer + '</button></td>';
   			}
   			html += '<td class="footable-visible">' + (result[i].customerName ? result[i].customerName : '-') + '</td>';
   			html += '<td class="footable-visible">' + dateUtilsJs.formatDate(new Date(result[i].writer.registrationDate), "YYYY-MM-DD HH:MM") + '</td>';
   			html += '<td class="footable-visible">' + (result[i].lossName ? result[i].lossName + '(' + result[i].lossId + ')' : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].lossDate ? dateUtilsJs.formatDate(new Date(result[i].lossDate), "YYYY-MM-DD HH:MM") : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].deleteName ? result[i].deleteName + '(' + result[i].deleteId + ')' : '-') + '</td>';
   			html += '<td class="footable-visible">' + (result[i].delDate ? dateUtilsJs.formatDate(new Date(result[i].delDate), "YYYY-MM-DD HH:MM") : '-') + '</td>';
   			html += '</tr>';
   			$("#tBodyList").append(html);
   		}
   	};

   	function _searchDetail(index){
   		//
   		self.location= _ctx + "/customer/detail/" + result[index].customerId;
   	};

   	var selectedCardNo;
    function _searchCustomerOnClick(cutCardNo){
        //
    	selectedCardNo = cutCardNo;
    	$("#Popup_Customer").modal();
		customerPopupJs.init(_selectedCustomer);
    };
    function _lostCardOnClick(cutCardNo){
    	//
    	swal({
            title: _msg.customerCardMgmt,
            text: _msg.confirmLostCard,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: false
        }, function () {
        	_chagneCustStatCode(cutCardNo, "MEML04");
        	_reCustomerCardOnClick(cutCardNo);
        });
    };
    
    function _chagneCustStatCode (cutCardNo, custStatCode) {
    	$.ajax({
			type : 'PUT' ,
			method : 'PUT',
			url : _ctx + "/ws/customer/card/changeCustStatCode/" + cutCardNo + "/status/" + custStatCode,
			contentType:"application/json",
			dataType : 'json' ,
			data : {},
			success : function(jsonData) {
				if(jsonData.status == 'SUCCESS'){
					toastr.success(_commonMsg.successApply, _msg.customerCardMgmt);
					_listOnClick();
				}else{
					toastr.error(_commonMsg.failApply, _msg.customerCardMgmt);
				}
			},
			error : function(xhRequest, ErrorText, thrownError) {
				//
				parent.layerJs.fn_exception(xhRequest);
				toastr.error(_commonMsg.failApply, _msg.customerCardMgmt);
			}
		});
    }
    
    function _deleteCardOnClick(cutCardNo){
    	swal({
            title: _msg.customerCardMgmt,
            text: _msg.confirmDeleteCard,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: false
        }, function () {
        	_chagneCustStatCode(cutCardNo, "MEML05");
        	_reCustomerCardOnClick(cutCardNo);
        });
    };
    function _reCustomerCardOnClick(cutCardNo){
      	swal({
            title: _msg.customerCardMgmt,
            text: _msg.confirmReissueCard,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.confirm,
            cancelButtonText: _msg.cancel,
            closeOnConfirm: false
        }, function () {
        	_chagneCustStatCode(cutCardNo, "MEML06");
        });
    };
    
    
	function _selectedCustomer(obj){
		//고객정합성 체크
		_isReadyCardMapping(obj.id);
	};
	
	function _isReadyCardMapping(customerId) {
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/customer/isReadyCardMapping/" + customerId,
   			dataType : 'json' ,
   			success : function(jsonData, textStatus, jqXHR) {
   				
   				if (!jsonData) {
   					swal(_commonMsg.validationCheck, _msg.customerAlreadyHasCard, "warning");
   					return false;
   				}
   				//카드 매핑
   				$.ajax({
   					type : 'PUT' ,
   					method : 'PUT',
   					url : _ctx + "/ws/customer/card/mappingCard/" + selectedCardNo,
   					contentType:"application/json",
   					dataType : 'json' ,
   					data : JSON.stringify({customerId : customerId}),
   					success : function(jsonData) {
   						if(jsonData.status == 'SUCCESS'){
   							toastr.success(_msg.successCardRegister, _msg.customerCardMgmt);
   							_listOnClick();
   						}else{
   							toastr.error(_msg.failCardRegister, _msg.customerCardMgmt);
   						}
   					},
   					error : function(xhRequest, ErrorText, thrownError) {
   						//
   						parent.layerJs.fn_exception(xhRequest);
   						toastr.error(_msg.failCardRegister, _msg.customerCardMgmt);
   					}
   				});
   				
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
	}

	function _listOnClick(){
		self.location= _ctx + "/customerCard/list";
	};
	
   	return {
   		init : _init,
   		search : _search,
   		searchCustomerOnClick : _searchCustomerOnClick,
   		lostCardOnClick : _lostCardOnClick,
   		deleteCardOnClick : _deleteCardOnClick,
   		reCustomerCardOnClick : _reCustomerCardOnClick
   	};
}();
