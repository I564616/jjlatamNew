$(document).ready(function () {
if($("#passwordExpireToken").length!=0 && $("#passwordExpireToken").val()!='') {
	jQuery.ajax({
		type: 'GET',
		url: ACC.config.contextPath + '/login/passwordExpiredEmail?passwordExpireToken='+$("#passwordExpireToken").val()+'&email='+$("#email").val() ,
		success: function (data) {
			$('#resetPass').html(data);
			$("#recoverpasswordP3").show();
			$('#resetPasswordpopup').modal('show');
	
			$.get($("#hddnLogoutURL").val());
			
			$(".success.reset").hide();
			resetPasswordValidation();
			resetPasswordButtonActionUserMangement($("#email").val());
		
			$("#checkpass").keyup(function(){
				$('#checkpass').pstrength();
			});
			$('#pass4').click(function () {
				$('#resetPasswordpopup').modal('hide');
			});
			
			
			/*$.colorbox({
				html: data,
				hieght: '565px',
				width: '565px ',
				overlayClose: false,
				onComplete: function () {
					$.get($("#hddnLogoutURL").val());
					$("#recoverpasswordP3").show();
					$(".success.reset").hide();
					resetPasswordValidation();
					resetPasswordButtonActionUserMangement($("#email").val());
					$.colorbox.resize();
					$("#checkpass").keyup(function(){
						$('#checkpass').pstrength();
					});
					$('#pass4').click(function () {
						$.colorbox.close();
					});
				},
				onClosed: function(){
					window.location.href = ACC.config.contextPath + '/login';
				}
			});*/
		}
	});
}

function resetPasswordButtonActionUserMangement(userEmail) {
	$('#passnext3').click(function () {
		if ($('#recoverpasswordFormP3').valid()) {
			var dataObj = new Object();
			dataObj.email = $("#useremail").length != 0 ? $("#useremail").val() : userEmail;
			dataObj.password = $("#checkpass").val();
			dataObj.passwordExpireToken =  $("#passwordExpireToken").val();
			jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath + '/passwordReset/passwordToken',
				data: dataObj,
				success: function (data) {
					if (data == "true") {
						$('#recoverpasswordP3').hide();
						$('#resetPasswordpopup').modal('hide');
						$('#recoverpasswordP4').show();
						$('#resetPasswordsuccesspopup').modal('show');
						
					} else {
						$("#recoverpasswordP3").find("div.recPassError").append("<label class=\"error\">" + data  + "</label>");
						//$.colorbox.resize();
					}
				},
				error: function () {
					$("#recoverpasswordP3").find("div.recPassError").append("<label class=\"error\">" + $("#checkpass2").attr("data-msg-required") + "</label>");
				}
			});
		} else {
			if($("#recoverpasswordP3")) {
			}
		}
	});
}
});