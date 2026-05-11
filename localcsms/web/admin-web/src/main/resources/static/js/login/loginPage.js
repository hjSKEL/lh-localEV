'use strict';

var pageAuthSignIn = {
  initValidation : function() {
    $('.js-validation-signin').validate({
      errorClass: 'invalid-feedback animated fadeIn',
      errorElement: 'div',
      errorPlacement: (error, el) => {
        $(el).addClass('is-invalid');
        $(el).parents('.form-group').append(error);
      },
      highlight: (el) => {
        $(el).parents('.form-group').find('.is-invalid').removeClass('is-invalid').addClass('is-invalid');
      },
      success: (el) => {
        $(el).parents('.form-group').find('.is-invalid').removeClass('is-invalid');
        $(el).remove();
      },
      rules: {
        'login-username': {
          required: true,
          minlength: 3
        },
        'login-password': {
          required: true,
          minlength: 3
        }
      },
      messages: {
        'login-username': {
          required: '아이디를 입력해 주세요.',
          minlength: '아이디는 최소 3자리입니다.'
        },
        'login-password': {
          required: '비밀번호를 입력해 주세요.',
          minlength: '비밀번호는 최소 5자리입니다.'
        }
      }
    });
  },
  initEvent : function() {
	  $('#findIdPin').click(function() {
	  	ajax.getPage(_ctx+"/login/find/idpin", null, function(data){
	  		$("#main-container").empty();
	  		$("#main-container").append(data);
	  	});
	  });
  },
  
  init : function() {
    this.initValidation();
    this.initEvent();
  }
}



