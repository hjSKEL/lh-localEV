/**
 *
 */
let productDetailJs = function(){
    "use strict";
    let data = {
    		prices : []
    };

    let idCheck = 0;

	function _init() {
		//
		_initEvent();
		if(!queryString.productId || queryString.productId === ''){
			$("#tdProductId").removeAttr("readonly"); ;
			$("#tdProductName").removeAttr("readonly"); ;
			$("#btnProdIdChecker").show();
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			_initProduct();
			return ;
		}
		_search(productId);
	}

	function _initProduct(){
		//
		data = {
	    		prices : []
	    };
	}

	function _initEvent(){
		//
		if(productId=="null" || productId ==""){
			let html = '<tr>';
			html += '<td></td>';
			html += '<td>1</td>';
			html += '<td><input type="text" id="startDate" class="form-control"></td>';
			html += '<td><input type="text" id="endPrice" value="9999-12-31" class="form-control" readonly></td>';
			html += '<td><input type="number" min="0" value="0" max="10000" id="price" class="form-control"></td>';
			html += '<td><input type="number" min="0" value="0" max="10000" id="dischargePrice" class="form-control"></td>';
			html += '<td></td>'
			//html += '<td><button class="btn btn-warning pull" onclick="productDetailJs.cancelPrice(this)">' + _msg.btnCancel + '</button></td>';
			html += '</tr>';
			$("#tBodyList").prepend(html);
		}

        $('#startDate').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $("#startDate").val(dateUtilsJs.formatDate(new Date(), 'YYYY-MM-DD'));

		$("#btnNewPrice").click(function(){
			//
			_registerOnClick();
		});
		$("#btnList").click(function(){
			self.location= _ctx + "/product/list";
		});
		$("#btnAdd").click(function(){
			//
			_addPrice();
		});
		$("#btnProdIdChecker").click(function(){
			//
			_checkProductId();
		});

	}

	function _checkProductId(){
		//
		let checkIdFront = $("#tdProductId").val().trim().substring(0,2);
		let checkIdBack = $("#tdProductId").val().trim().substring(2,4);
		let regFrontId = /^[A-Z]{2}$/;
		let regBackId = /^[0-9]{2}$/;

		if(!regFrontId.test(checkIdFront)){
			swal(_commonMsg.validationCheck, _msg.productIdFrontFormat, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return;
		}

		if(!regBackId.test(checkIdBack)){
			swal(_commonMsg.validationCheck, _msg.productIdBackFormat, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return;
		}

		let tempId = "PO" + $("#tdProductId").val().trim();
		if (!tempId || tempId == 'PO') {
			swal(_commonMsg.validationCheck, _msg.checkProductIdDuplicate, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}

		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/product/exist/" + tempId,
   			dataType : 'json' ,
   			success : function(jsonData) {
   				if(jsonData.status == 'SUCCESS'){
   					swal(_commonMsg.validationCheck, _msg.productIdInUse, "warning");
   					let contents = _msg.idAlreadyExists;
					let color = 'red';
					$('#prodIdCheckLabel').html(contents);
					$('#prodIdCheckLabel').css('color', color);
					idCheck = 0;
   				} else {
					toastr.success(_msg.productIdAvailable, _msg.productMgmt);
					let contents = _msg.idAvailable;
					let color = 'green';
					$('#prodIdCheckLabel').html(contents);
					$('#prodIdCheckLabel').css('color', color);
					idCheck = 1;
				}
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
	}


	function _search(id){
		//
   		$.ajax({
   			type: 'GET' ,
   			url : _ctx + "/ws/product/detail/" + id,
   			dataType : 'json' ,
   			success : function(jsonData) {
   				data = jsonData;
   				_displayProduct();
   			} ,
   			error : function(xhRequest, ErrorText, thrownError) {
   				//
   				parent.layerJs.fn_exception(xhRequest);
   			}
   		});
	}

	function _displayProduct(){
		//
		if(data){
			$("#btnAdd").show();
			$("#btnNewPrice").hide();
			idCheck = 1;
		}
		$("#tdProductId").val(data.id);
		$("#tdProductName").val(data.name);
		$("#tdRegId").html(data.writer.regUserName + "(" + data.writer.regUserId + ")");
		$("#tdRegDate").html(dateUtilsJs.formatDate(new Date(data.writer.registrationDate), "YYYY-MM-DD HH:MM:SS"));
		$("#tdUpdId").html(data.writer.updUserName + "(" + data.writer.updUserId + ")");
		$("#tdUpdDate").html(dateUtilsJs.formatDate(new Date(data.writer.updateDate), "YYYY-MM-DD HH:MM:SS"));

		let html = '';
		let length = data.prices.length - 1;
		let today = new Date();
		let tomorrow = new Date(today);
		tomorrow.setDate(today.getDate() + 1);
		let tomorrowDt = dateUtilsJs.formatDate(tomorrow,'YYYY-MM-DD');

		for(let i=length; i >= 0  ; --i){
			html = '<tr>';
			html += '<td>' + data.prices[i].productType + '</td>';
			html += '<td>' + data.prices[i].seq + '</td>';
			html += '<td>' + formmatUtilsJs.dateFormmat(data.prices[i].startDt, 'YYYY-MM-DD') + '</td>';
			html += '<td>' + formmatUtilsJs.dateFormmat(data.prices[i].endDt, 'YYYY-MM-DD') + '</td>';
			html += '<td>' + data.prices[i].fee + '</td>';
			html += '<td>' + (data.prices[i].dischargeFee != null ? data.prices[i].dischargeFee : 0) + '</td>';
			if(!length==0 && data.prices[i].seq == length + 1
				&& formmatUtilsJs.dateFormmat(data.prices[length].startDt, 'YYYY-MM-DD') >= tomorrowDt ) {
				html += '<td><div style="text-align:center;"><button id="btnDelete" class="btn btn-danger" onclick="productDetailJs.deletePrice(\'' + data.prices[i].id + '\')">' + _msg.btnDelete + '</button></td>';
			} else {
				html += '<td></td>';
			}
			html += '</tr>';
			$("#tBodyList").append(html);
		}
	}

	function _addPrice(){
		//
		let id = "PO" + $("#tdProductId").val().trim();
		if (!id || id == 'PO') {
			swal(_commonMsg.validationCheck, _msg.checkProductIdDuplicate, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}
		if(!idCheck){
			swal(_commonMsg.validationCheck, _msg.checkProductIdDuplicate, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}

		//오금제명
		let pdName = $("#tdProductName").val().trim();
		/*if ( !pdName || pdName == '') {
			swal(_commonMsg.validationCheck, "요금제명을 입력해 주세요.", "warning");
			return;
		}*/

		$("#btnDelete").hide();
		$("#btnAdd").hide();
		$("#btnNewPrice").show();

		let html = '<tr>';
		if(data.prices.length == 0){
			html += '<td>' + id + '</td>';
			html += '<td>1</td>';
			html += '<td><input type="text" id="startDate" class="form-control"></td>';
			html += '<td><input type="text" id="endPrice" value="9999-12-31" class="form-control" readonly></td>';
			html += '<td><input type="number" min="0" value="0" max="10000" id="price" class="form-control"></td>';
			html += '<td><input type="number" min="0" value="0" max="10000" id="dischargePrice" class="form-control"></td>';
		}else{
			let datum = data.prices[data.prices.length -1];
			let prevDch = datum.dischargeFee != null ? datum.dischargeFee : 0;
			html += '<td>' + datum.productType + '</td>';
			html += '<td>' + (datum.seq + 1) + '</td>';
			html += '<td><input type="text" id="startDate" class="form-control"></td>';
			html += '<td><input type="text" value="9999-12-31" class="form-control" readonly></td>';
			html += '<td><input type="number" min="0" value="' + datum.fee + '" max="10000" id="price" class="form-control"></td>';
			html += '<td><input type="number" min="0" value="' + prevDch + '" max="10000" id="dischargePrice" class="form-control"></td>';
		}
		html += '<td><button class="btn btn-warning pull" onclick="productDetailJs.cancelPrice(this)">' + _msg.btnCancel + '</button></td>';
		html += '</tr>';
		$("#tBodyList").prepend(html);

        $('#startDate').datepicker({
            todayBtn: "linked",
            autoClose: true,
            format: "yyyy-mm-dd"
        });
        $("#startDate").val(dateUtilsJs.formatDate(new Date(), 'YYYY-MM-DD'));
	}

	function _cancelPrice(target){
		target.parentElement.parentElement.remove();
		$("#btnNewPrice").hide();
		$("#btnDelete").show();
		$("#btnAdd").show();
	}

	function _deletePrice(id){
		swal({
			title: _msg.productMgmt,
			text: _msg.confirmDeletePrice,
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#ED5565',
			confirmButtonText: _msg.btnDelete,
			cancelButtonText: _msg.btnCancel,
		}, function () {
			$.ajax({
				type : 'DELETE' ,
				method : 'DELETE',
				url : _ctx + '/ws/product/' + id,
				contentType: 'application/json',
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_commonMsg.successProcess, _msg.productMgmt);
						_moveDetail(productId);
					}else{
						toastr.error(_commonMsg.failDelete, _msg.productMgmt);
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_commonMsg.failDelete, _msg.productMgmt);
				}
			});
		});
	}
	function _validate(){
		// 요금제 ID
		let id;
		if(productId === "null"){
			id = "PO" + $("#tdProductId").val().trim();
			if (!id || id == 'PO') {
				swal(_commonMsg.validationCheck, _msg.enterProductId, "warning");
				return false;
			}
			if(!data.id || data.id == ''){
				data.id= id;
			}
		} else {
			id = $("#tdProductId").val().trim();
			if (!id || id == '') {
				swal(_commonMsg.validationCheck, _msg.enterProductId, "warning");
				return false;
			}
			if(!data.id || data.id == ''){
				data.id= id;
			}
		}
		if(!idCheck){
			swal(_commonMsg.validationCheck, _msg.checkProductIdDuplicate, "warning");
			let contents = _msg.duplicateCheckRequired;
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}

		//오금제명
		let pdName = $("#tdProductName").val().trim();
		/*if ( !pdName || pdName == '') {
			swal(_commonMsg.validationCheck, "요금제명을 입력해 주세요.", "warning");
			return false;
		}*/
		data.name = pdName;

		//시작~종료일
		let newStartDt = formmatUtilsJs.removeDash($("#startDate").val());
		if ( !newStartDt || newStartDt == '') {
			swal(_commonMsg.validationCheck, _msg.selectStartDate, "warning");
			return false;
		}

		if(data.prices && data.prices.length > 0){
			let lastPrice = data.prices[data.prices.length -1];
			if(Number(lastPrice.startDt) >= Number(newStartDt)){
				swal(_commonMsg.validationCheck, _msg.startDateAfterPrevious, "warning");
				return false;
			}
		}

		let newFee = $("#price").val();
		if ( !newFee || newFee == '' || newFee < 0 || newFee >= 10000) {
			swal(_commonMsg.validationCheck, _msg.enterValidPrice, "warning");
			return false;
		}

		// 방전 단가 (V2X, default 0). 음수/상한 검증만, 미입력 시 0.
		let newDchFee = $("#dischargePrice").val();
		if (newDchFee === undefined || newDchFee === '') {
			newDchFee = 0;
		}
		if (newDchFee < 0 || newDchFee >= 10000) {
			swal(_commonMsg.validationCheck, _msg.enterValidPrice, "warning");
			return false;
		}

		let price = {
				seq:1,
				productType:id,
				startDt :newStartDt,
				endDt: '99991231',
				fee : newFee,
				dischargeFee : Number(newDchFee)
		};
		if(!data.prices || data.prices.length == 0){
			data.prices = [];
			price.seq = 1;
		}else{
			let maxSeq = data.prices.length;
			price.seq = maxSeq + 1;
		}
		data.prices.push(price);
		return true;
	}

	function _registerOnClick(){

		if(!_validate()){
			return ;
		}
		if(!queryString.productId || queryString.productId === ''){
			_registerProduct();
		}else{
			_modifyProduct();
		}
	}

	function _registerProduct(){
        swal({
            title: _msg.productMgmt,
            text: _msg.confirmRegisterProduct,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnRegister,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function () {
			$.ajax({
				type : 'POST' ,
				method : 'POST',
				url : _ctx + "/ws/product",
				contentType:"application/json",
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_commonMsg.successRegister, _msg.productMgmt);
						swal({
				            title: _msg.productMgmt,
				            text: _commonMsg.registered,
				            type: "success",
				            showCancelButton: false,
				            confirmButtonColor: "#DD6B55",
				            confirmButtonText: _msg.btnConfirm,
        				}, function(){
							_moveDetail(jsonData.result);
						});
					}else{
						toastr.error(_commonMsg.failRegister, _msg.productMgmt);
					}
				},
				error : function() {
					//
					toastr.error(_commonMsg.failRegister, _msg.productMgmt);
				}
			});
        });
	}

	function _modifyProduct(){
        swal({
            title: _msg.productMgmt,
            text: _msg.confirmChangeProduct,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: _msg.btnChange,
            cancelButtonText: _msg.btnCancel,
            closeOnConfirm: false
        }, function () {
			$.ajax({
				type : 'PUT' ,
				method : 'PUT',
				url : _ctx + "/ws/product",
				contentType:"application/json",
				dataType : 'json' ,
				data : JSON.stringify(data),
				success : function(jsonData) {
					if(jsonData.status === 'SUCCESS'){
						toastr.success(_commonMsg.successChange, _msg.productMgmt);
						_moveDetail(jsonData.result);
					}else{
						toastr.error(_commonMsg.failChange, _msg.productMgmt);
					}
				},
				error : function() {
					//
					toastr.error(_commonMsg.failChange, _msg.productMgmt);
				}
			});
        });
	}

	function _moveDetail(id){
		self.location= _ctx + "/product/detail?productId=" + id;
	}

	return {
		init : _init,
		deletePrice : _deletePrice,
		cancelPrice : _cancelPrice,
	};
}();
