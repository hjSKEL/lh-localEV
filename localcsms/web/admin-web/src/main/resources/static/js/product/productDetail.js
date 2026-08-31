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
			let contents = '* 중복확인 해주세요.';
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
			html += '<td></td>'
			//html += '<td><button class="btn btn-warning pull" onclick="productDetailJs.cancelPrice(this)">' + '취소' + '</button></td>';
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
			swal(_commonMsg.validationCheck, '요금제ID 앞 두자리는 영문대문자입니다.', "warning");
			let contents = '* 중복확인 해주세요.';
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return;
		}

		if(!regBackId.test(checkIdBack)){
			swal(_commonMsg.validationCheck, '요금제ID 뒤 두자리는 숫자입니다.', "warning");
			let contents = '* 중복확인 해주세요.';
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return;
		}

		let tempId = "PO" + $("#tdProductId").val().trim();
		if (!tempId || tempId == 'PO') {
			swal(_commonMsg.validationCheck, '요금제ID 중복확인을 해주세요.', "warning");
			let contents = '* 중복확인 해주세요.';
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
   					swal(_commonMsg.validationCheck, '이미 사용중인 요금제ID 입니다. 변경하세요.', "warning");
   					let contents = '* 이미 등록한 아이디가 존재합니다.';
					let color = 'red';
					$('#prodIdCheckLabel').html(contents);
					$('#prodIdCheckLabel').css('color', color);
					idCheck = 0;
   				} else {
					toastr.success('사용 가능한 요금제ID 입니다.', '요금제관리');
					let contents = '* 사용가능한 아이디입니다.';
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
			html += '<td style="text-align:center;">' + data.prices[i].productType + '</td>';
			html += '<td style="text-align:center;">' + data.prices[i].seq + '</td>';
			html += '<td style="text-align:center;">' + formmatUtilsJs.dateFormmat(data.prices[i].startDt, 'YYYY-MM-DD') + '</td>';
			html += '<td style="text-align:center;">' + formmatUtilsJs.dateFormmat(data.prices[i].endDt, 'YYYY-MM-DD') + '</td>';
			html += '<td style="text-align:center;">' + data.prices[i].fee + '</td>';
			if(data.prices[i].seq == length + 1 && formmatUtilsJs.dateFormmat(data.prices[length].startDt, 'YYYY-MM-DD') >= tomorrowDt ) {
				html += '<td style="text-align:center;"><button id="btnDelete" class="btn btn-danger btn-sm" onclick="productDetailJs.deletePrice(\'' + data.prices[i].id + '\')">' + '삭제' + '</button></td>';
			} else {
				html += '<td style="text-align:center;"></td>';
			}
			html += '</tr>';
			$("#tBodyList").append(html);
		}
	}

	function _addPrice(){
		//
		let id = "PO" + $("#tdProductId").val().trim();
		if (!id || id == 'PO') {
			swal(_commonMsg.validationCheck, '요금제ID 중복확인을 해주세요.', "warning");
			let contents = '* 중복확인 해주세요.';
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}
		if(!idCheck){
			swal(_commonMsg.validationCheck, '요금제ID 중복확인을 해주세요.', "warning");
			let contents = '* 중복확인 해주세요.';
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}

		//요금제명
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
		}else{
			let datum = data.prices[data.prices.length -1];
			html += '<td style="text-align:center;">' + datum.productType + '</td>';
			html += '<td style="text-align:center;">' + (datum.seq + 1) + '</td>';
			html += '<td style="text-align:center;"><input type="text" id="startDate" class="form-control"></td>';
			html += '<td style="text-align:center;"><input type="text" value="9999-12-31" class="form-control" readonly></td>';
			html += '<td style="text-align:center;"><input type="number" min="0" value="' + datum.fee + '" max="10000" id="price" class="form-control"></td>';
		}
		html += '<td style="text-align:center;"><button class="btn btn-danger btn-sm" onclick="productDetailJs.cancelPrice(this)">' + '삭제' + '</button></td>';
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
			title: '요금제관리',
			text: '해당 단가 정보를 삭제하시겠습니까??',
			type: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#ED5565',
			confirmButtonText: '삭제',
			cancelButtonText: '취소',
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
						toastr.success(_commonMsg.successProcess, '요금제관리');
						_moveDetail(productId);
					}else{
						toastr.error(_commonMsg.failDelete, '요금제관리');
					}
				},
				error : function(xhRequest, ErrorText, thrownError) {
					parent.layerJs.fn_exception(xhRequest);
					toastr.error(_commonMsg.failDelete, '요금제관리');
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
				swal(_commonMsg.validationCheck, '요금제ID를 입력해 주세요.', "warning");
				return false;
			}
			if(!data.id || data.id == ''){
				data.id= id;
			}
		} else {
			id = $("#tdProductId").val().trim();
			if (!id || id == '') {
				swal(_commonMsg.validationCheck, '요금제ID를 입력해 주세요.', "warning");
				return false;
			}
			if(!data.id || data.id == ''){
				data.id= id;
			}
		}
		if(!idCheck){
			swal(_commonMsg.validationCheck, '요금제ID 중복확인을 해주세요.', "warning");
			let contents = '* 중복확인 해주세요.';
			let color = 'red';
			$('#prodIdCheckLabel').html(contents);
			$('#prodIdCheckLabel').css('color', color);
			idCheck = 0;
			return ;
		}

		//요금제명
		let pdName = $("#tdProductName").val().trim();
		/*if ( !pdName || pdName == '') {
			swal(_commonMsg.validationCheck, "요금제명을 입력해 주세요.", "warning");
			return false;
		}*/
		data.name = pdName;

		//시작~종료일
		let newStartDt = formmatUtilsJs.removeDash($("#startDate").val());
		if ( !newStartDt || newStartDt == '') {
			swal(_commonMsg.validationCheck, '시작일자를 선택해 주세요.', "warning");
			return false;
		}

		if(data.prices && data.prices.length > 0){
			let lastPrice = data.prices[data.prices.length -1];
			if(Number(lastPrice.startDt) >= Number(newStartDt)){
				swal(_commonMsg.validationCheck, '이전 등록한 시작일자보다 미래로 시작일자를 설정해주세요.', "warning");
				return false;
			}
		}

		let newFee = $("#price").val();
		if ( !newFee || newFee == '' || newFee < 0 || newFee >= 10000) {
			swal(_commonMsg.validationCheck, '유효한 단가(0 ~ 9999)를 입력해 주세요.', "warning");
			return false;
		}

		// 방전 단가 (V2X, default 0). 음수/상한 검증만, 미입력 시 0.
		let newDchFee = $("#dischargePrice").val();
		if (newDchFee === undefined || newDchFee === '') {
			newDchFee = 0;
		}
		if (newDchFee < 0 || newDchFee >= 10000) {
			swal(_commonMsg.validationCheck, '유효한 단가(0 ~ 9999)를 입력해 주세요.', "warning");
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
            title: '요금제관리',
            text: '신규 요금제를 등록하시겠습니까?',
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: '등록',
            cancelButtonText: '취소',
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
						toastr.success(_commonMsg.successRegister, '요금제관리');
						swal({
				            title: '요금제관리',
				            text: _commonMsg.registered,
				            type: "success",
				            showCancelButton: false,
				            confirmButtonColor: "#DD6B55",
				            confirmButtonText: '확인',
        				}, function(){
							_moveDetail(jsonData.result);
						});
					}else{
						toastr.error(_commonMsg.failRegister, '요금제관리');
					}
				},
				error : function() {
					//
					toastr.error(_commonMsg.failRegister, '요금제관리');
				}
			});
        });
	}

	function _modifyProduct(){
        swal({
            title: '요금제관리',
            text: '요금제를 변경하시겠습니까?',
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: '변경',
            cancelButtonText: '취소',
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
						toastr.success(_commonMsg.successChange, '요금제관리');
						_moveDetail(jsonData.result);
					}else{
						toastr.error(_commonMsg.failChange, '요금제관리');
					}
				},
				error : function() {
					//
					toastr.error(_commonMsg.failChange, '요금제관리');
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
