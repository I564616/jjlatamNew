jQuery("#laProfileChangePassword").validate({
    rules: {
        newPassword: {
            required: true,
            complexityCheck: true,
            minlength: 8
            },
        checkNewPassword: {
            required: true,
            equalTo: "#newPassword"
        }
    },
    messages: {
        checkNewPassword: {
            equalTo: $("#profileChangePasswordMismatch").val()
        },
        newPassword: {
            complexityCheck: $("#profileChangePasswordMismatchComplexity").val()
        }
    },
     errorPlacement: function(error, element) {
        $(".recPassError").append(error);
    },
    onfocusout: false,
    focusCleanup: false
});

if($('.lapwdresetwidgetdiv').length>0 && $('#laResetPwd').length>0){
    var profilePWdStrngth = new LatamPasswordWidget('laResetPwd','newPassword');
    profilePWdStrngth.MakePWDWidget();
}

function passwordStrengthScore(){
    var siteLanguage = window.location.href.split("store/")[1].split("/")[0]
    if(document.getElementById("newPassword_strength_str").innerHTML == "weak"){
        switch(siteLanguage) {
            case "es":
                document.getElementById("newPassword_strength_str").innerHTML = "Débil";
                break;
            case "en":
                document.getElementById("newPassword_strength_str").innerHTML = "Weak";
                break;
            case "pt":
                document.getElementById("newPassword_strength_str").innerHTML = "Fraca";
                break;
        }
    }else if(document.getElementById("newPassword_strength_str").innerHTML == "medium"){
        switch(siteLanguage) {
            case "es":
                document.getElementById("newPassword_strength_str").innerHTML = "Mediana";
                break;
            case "en":
                document.getElementById("newPassword_strength_str").innerHTML = "Medium";
                break;
            case "pt":
                 document.getElementById("newPassword_strength_str").innerHTML = "Média";
                 break;
            }
    }else if(document.getElementById("newPassword_strength_str").innerHTML == "good"){
        switch(siteLanguage) {
            case "es":
                document.getElementById("newPassword_strength_str").innerHTML = "Fuerte";
                break;
            case "en":
                document.getElementById("newPassword_strength_str").innerHTML = "Strong";
                break;
            case "pt":
                 document.getElementById("newPassword_strength_str").innerHTML = "Forte";
                 break;
            }
    }
}

function LatamPasswordWidget(divid,pwdname)
{
	this.maindivobj = document.getElementById(divid);
	this.pwdobjname = pwdname;

	this.MakePWDWidget=_MakePWDWidget;

	this.showing_pwd=1;
	this.txtShow = $('#laPasswordShow').val();
	this.txtMask = $('#laPasswordMask').val();
	this.txtGenerate = $('#laPasswordGenerate').val();
	this.txtWeak = $('#laPasswordWeak').val();
	this.txtMedium = $('#laPasswordMedium').val();
	this.txtGood = $('#laPasswordGood').val();

	this.enableShowMask=true;
	this.enableGenerate=true;
	this.enableShowStrength=true;
	this.enableShowStrengthStr=true;

}

ACC.firstTimeResetPassword.resetPasswordClicked = function(userEmail) {
    if ($('#laProfileChangePassword').valid()) {
        var dataObj = {};
        dataObj.email = $("#useremail").val();
        dataObj.password = $("#newPassword").val();
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
                $("#recoverpasswordP1").find("div.recPassError").append("<label class=\"error\">" + $("#checkNewPassword").attr("data-msg-required") + "</label>");
            }
        });
    }
}

$("#newPassword").keyup(function(){
    var infoarea = $(this).attr('id');
    ACC.firstTimeResetPassword.runPassword($(this).val(), infoarea, options);
    $('#newPassword').pstrength();
    passwordStrengthScore();
});

$('#resetPassword').prop('disabled',true);