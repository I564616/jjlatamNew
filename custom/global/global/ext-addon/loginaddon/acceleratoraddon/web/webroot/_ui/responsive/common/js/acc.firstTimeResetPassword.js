var options = {
				verdicts:	["Very Weak","Weak","Medium","Strong","Very Strong"],
				colors: 	["#f00","#c06", "#f60","#3c0","#3f0"],
				scores: 	[10,15,30,40],
				minchar:	8,
				tooShort:   "Too Short!",
				minCharText: "Minimum length is %d characters"
			};
ACC.firstTimeResetPassword = {
	/**
	 * Binds all JQuery functions of the js on page load.
	 */
	bindAll: function() {
		
		ACC.firstTimeResetPassword.init();
		$('#resetPassword').click(function () {
			ACC.firstTimeResetPassword.resetPasswordClicked();
		});
	},
	init: function(){
		$("#recoverpasswordP1").show();
		$(".success.reset").hide();
		ACC.firstTimeResetPassword.resetPasswordValidation();
		 $("#checkpass").keyup(function(){
			var infoarea = $(this).attr('id');
			/*$("#resetPwd .pstrength-bar").parent().remove();
			$("#resetPwd .pstrength-info, #resetPwd  .pstrength-minchar, #resetPwd  .pstrength-bar").remove();*/
			
			ACC.firstTimeResetPassword.runPassword($(this).val(), infoarea, options);
		}); 
		$("#checkpass").keyup(function(){
			$('#checkpass').pstrength();
		});
		$('#pass4').click(function () {
			ACC.firstTimeResetPassword.resetSuccess();
		});
	},
	 
	/**************************************
	 * This function sets the click function for password reset step
	 */
	resetPasswordClicked : function() {
			var userEmail = $("#useremail").val();
			if ($('#recoverpasswordFormP1').valid()) {
			var dataObj = new Object();
			dataObj.email = userEmail;
			dataObj.password = $("#checkpass").val();
			jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath + '/passwordReset/revisionVerification',
				data: dataObj,
				success: function (data) {
					if (data == "true") {
						$('#recoverpasswordP1').hide();
						$('#recoverpasswordP4').hide();
						$('#success-resetpassowrdpage').show();
					} else {
						$("#recoverpasswordP1").find("div.recPassError").append("<label class=\"error\">" + data  + "</label>");
					}
				},
				error: function () {
					$("#recoverpasswordP1").find("div.recPassError").append("<label class=\"error\">" + $("#checkpass2").attr("data-msg-required") + "</label>");
				}
			});
		} else {
			if($("#recoverpasswordP1")) {
			}
		}
	},
	
	resetSuccess: function() {
		window.location.href = ACC.config.contextPath + '/login';
	},	
	resetPasswordValidation: function() {
		jQuery("#recoverpasswordFormP1").validate({
			rules: {
				checkpass: {
					required: true,
					complexity: true,
					minlength: 8
				},
				checkpass2: {
					equalTo: "#checkpass"
				}
			},
			messages: {
				checkpass: {
					complexity: PASSWORD_COMPLEXITY
				},
				checkpass2:{
					equalTo: PASSWORD_MISMATCH
				}
			},
			errorPlacement: function (error, element) {
				error.appendTo(element.parent().parent().parent().parent().find('div.recPassError'));
			},
			onfocusout: false,
			focusCleanup: false
		});
	},
	runPassword : function (password, infoarea, options){
		// Check password
		nPerc = ACC.firstTimeResetPassword.checkPassword(password, options);
		//console.log("score : " +nPerc);
		if(parseInt(nPerc) < 30){
			$("#resetPassword").prop('disabled', true);
		}else{
			$("#resetPassword").prop('disabled', false);
		}
		
	},
	checkPassword : function(password, options)
	{
		var intScore = 0;
		var strVerdict = options.verdicts[0];
		// PASSWORD LENGTH
		if (password.length < options.minchar)                         // Password too short
		{
			intScore = (intScore - 100)
		}
		else if (password.length >= options.minchar && password.length <= (options.minchar + 2)) // Password Short
		{
			intScore = (intScore + 6)
		}
		else if (password.length >= (options.minchar + 3) && password.length <= (options.minchar + 4))// Password Medium
		{
			intScore = (intScore + 12)
		}
		else if (password.length >= (options.minchar + 5))                    // Password Large
		{
			intScore = (intScore + 18)
		}
		if (password.match(/[a-z]/))                              // [verified] at least one lower case letter
		{
			intScore = (intScore + 1)
		}
		if (password.match(/[A-Z]/))                              // [verified] at least one upper case letter
		{
			intScore = (intScore + 5)
		}
		// NUMBERS
		if (password.match(/\d+/))                                 // [verified] at least one number
		{
			intScore = (intScore + 5)
		}
		if (password.match(/(.*[0-9].*[0-9].*[0-9])/))             // [verified] at least three numbers
		{
			intScore = (intScore + 7)
		}
		// SPECIAL CHAR
		if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~]/))            // [verified] at least one special character
		{
			intScore = (intScore + 5)
		}
		// [verified] at least two special characters
		if (password.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/))
		{
			intScore = (intScore + 7)
		}
		// COMBOS
		if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/))        // [verified] both upper and lower case
		{
			intScore = (intScore + 2)
		}
		if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) // [verified] both letters and numbers
		{
			intScore = (intScore + 3)
		}
	 	// [verified] letters, numbers, and special characters
		if (password.match(/([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/))
		{
			intScore = (intScore + 3)
		}
		return intScore;
	}
};

$(document).ready(function() {
	ACC.firstTimeResetPassword.bindAll();
});