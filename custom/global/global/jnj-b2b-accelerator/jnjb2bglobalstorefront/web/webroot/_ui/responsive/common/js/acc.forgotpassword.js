/*****************************************************
 * Global Variables required for select functionality.
 */
var selected1;
var selected2;
var selected3;

var multimodeSelector = '#chooseMultiMode';
var multimodeEmailSelector = '#multimodeEmail';
var multimodeEnabled = false;

var internalError = '999';
var tooManyAttempts = '429';
var invalidToken = '401';

const passwordExpiredForUserElementId = "#passwordExpiredForUser";

const tokenNotificationPath = '/passwordReset/emailTokenNotification?email=';

if(!!$("#resetFlag").val()) {
	$('#cookiepopup').remove();
	multimodeEnabled = true;
}

/*****************************************************
 * Binding forgot password
 */
ACC.forgotpassword = {
	bindAll: function () {
		this.bindForgotPasswordLink($('.password-forgotten'));
	},
	bindForgotPasswordLink: function (link) {
		link.click(function () {
			passwordResetForUser(false);
		});
	}
};
/**************************************
 * DOCUMENT READY
 **************************************/
$(document).ready(function () {
	
	checkReset();
	
	jQuery.validator.addMethod('complexity', function () {
	 	var strength = 0;
		if ($("#checkpass").val().match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
			strength++;
		}
		if ($("#checkpass").val().match(/([a-z])/)) {
			strength++;
		}
		if ($("#checkpass").val().match(/([A-Z])/)) {
			strength++;
		}
		if ($("#checkpass").val().match(/([0-9])/)) {
			strength++;
		}
		if (strength >= 2) {
			return true;
		} else {
			return false;
		}
    }, $("#complexityInvalid").val());
	ACC.forgotpassword.bindAll();
	jQuery.validator.addMethod('questions', function (value, element) {
		if(value==="0"){
			return false;
		} else {
			return true;
		}
	});
	/**************************************
	 * This condition checks for the expired password flag.
	 */

	if($(passwordExpiredForUserElementId).length!=0 && $(passwordExpiredForUserElementId).val()!='') {
		passwordResetForUser(false);
	}
});
/**************************************
 * This function validates the password reset step
 */

const divWithClassRecPassError = 'div.recPassError';

function resetPasswordValidation() {
	jQuery("#recoverpasswordFormP3").validate({
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
			
			$('#recoverpasswordFormP3 .password-field-group .password-fields').addClass('password-fields-shrink');
			$('#recoverpasswordFormP3 .password-field-group .password-info-resetpassword').addClass('password-info-grow');
			error.appendTo(element.parent().parent().parent().parent().parent().find(divWithClassRecPassError));
		},
		onfocusout: false,
		focusCleanup: false
		
	});
}
/**************************************
 * This function sets the click function for password reset step
 */

const resetPasswordPopupIdSelector = '#resetPasswordpopup';
const htmlForlabelWithClassError = '<label class="error">';
const userEmailIdSelector = '#useremail';
const recoverPasswordP3IdSelector = '#recoverpasswordP3';
function resetPasswordButtonAction(userEmail) {
	$('#passnext3').click(function () {
		resetPasswordValidation();

		if ($('#recoverpasswordFormP3').valid()) {	
			createLoader();
			var requestBody = new Object();
			requestBody.email = $(userEmailIdSelector).length != 0 ? $(userEmailIdSelector).val() : userEmail;
			requestBody.email = userEmail ? userEmail : requestBody.email;
			requestBody.password = $("#checkpass").val();
			requestBody.token = $("#token").val();

			jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath + '/passwordReset/revisionVerification',
				data: requestBody,
				success: emailResetSuccessHandler,
				error: emailResetErrorHandler,
				complete: function () {
					removeLoader();
				}
			});
		}
	});
}

function emailResetSuccessHandler(data) {
	if (multimodeEnabled) {
		var multimodeResponse = JSON.parse(data);

		if(multimodeResponse.response === 'true') {
			$(recoverPasswordP3IdSelector).hide();
			$(resetPasswordPopupIdSelector).modal('hide');
			$('#recoverpasswordP4').show();
			$('#resetPasswordsuccesspopup').modal('show');
		} else {
			if(multimodeResponse.responseCode === internalError) {
				$(recoverPasswordP3IdSelector).find(divWithClassRecPassError).append(htmlForlabelWithClassError + multimodeResponse.responseMessage + "</label>");
			} else if (multimodeResponse.responseCode === tooManyAttempts) {
				showTimeoutError();
			} else {
				showPasswordError(multimodeResponse.responseCode,multimodeResponse.responseMessage);
			}
		}
	} else {									
		if (data == "true") {
			$(recoverPasswordP3IdSelector).hide();
			$(resetPasswordPopupIdSelector).modal('hide');
			$('#recoverpasswordP4').show();
			$('#resetPasswordsuccesspopup').modal('show');
			
		} else {
			$(recoverPasswordP3IdSelector).find(divWithClassRecPassError).append(htmlForlabelWithClassError + data  + "</label>");
		}
	}
} 

const requiredFieldAttribute = 'data-msg-required';

function emailResetErrorHandler() {
	$(recoverPasswordP3IdSelector).find(divWithClassRecPassError).append(htmlForlabelWithClassError + $("#checkpass2").attr(requiredFieldAttribute) + "</label>");
}
/**************************************
 * This method revises the select lists once a value is selected on any one of them. 
 * This will remove the option selected in a particular list from the other lists. 
 * @param thisObj
 */
function reviseLists(thisObj) {
	var idIndex = thisObj.id.split("q")[1];
	switch (idIndex) {
		case "1":
			if (selected1 == null || typeof selected1 == 'undefined' || selected1.substring(0,2) == "0_") {
				removeFromList("2", thisObj);
				removeFromList("3", thisObj);
				selected1 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			} else {
				document.getElementById("secq2").options[document.getElementById("secq2").options.length] = new Option(selected1.split("_")[1], selected1.split("_")[0]);
				document.getElementById("secq3").options[document.getElementById("secq3").options.length] = new Option(selected1.split("_")[1], selected1.split("_")[0]);
				if (thisObj[thisObj.selectedIndex].value != "0") {
					removeFromList("2", thisObj);
					removeFromList("3", thisObj);
				} else {
					selected1 = null;
				}
				selected1 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			}
		break;
		case "2":
			if (selected2 == null || typeof selected2 == 'undefined' || selected2.substring(0,2) == "0_") {
				removeFromList("1", thisObj);
				removeFromList("3", thisObj);
				selected2 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			} else {
				document.getElementById("secq1").options[document.getElementById("secq1").options.length] = new Option(selected2.split("_")[1], selected2.split("_")[0]);
				document.getElementById("secq3").options[document.getElementById("secq3").options.length] = new Option(selected2.split("_")[1], selected2.split("_")[0]);
				if (thisObj[thisObj.selectedIndex].value != "0") {
					removeFromList("1", thisObj);
					removeFromList("3", thisObj);
				} else {
					selected2 = null;
				}
				selected2 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			}
		break;
		case "3":
			if (selected3 == null || typeof selected3 == 'undefined' || selected3.substring(0,2) == "0_") {
				removeFromList("2", thisObj);
				removeFromList("1", thisObj);
				selected3 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			} else {
				document.getElementById("secq2").options[document.getElementById("secq2").options.length] = new Option(selected3.split("_")[1], selected3.split("_")[0]);
				document.getElementById("secq1").options[document.getElementById("secq1").options.length] = new Option(selected3.split("_")[1], selected3.split("_")[0]); 
				if (thisObj[thisObj.selectedIndex].value != "0") {
					removeFromList("2", thisObj);
					removeFromList("1", thisObj);
				} else {
					selected3 = null;
				}
				selected3 = thisObj[thisObj.selectedIndex].value + "_" + thisObj[thisObj.selectedIndex].innerHTML;
			}
		break;
	}
}
/**
 * Method is responsible for removing an option element from a list
 * @param id
 * @param thisObj
 */
function removeFromList(id, thisObj) {
	for(var i=1;i<document.getElementById("secq" + id).length;i++) {
		if(thisObj[thisObj.selectedIndex].innerHTML == document.getElementById("secq" + id)[i].innerHTML) {
			document.getElementById("secq" + id).options.remove(i);
			break;
		}
	}
}

const forgotPasswordPopupIdSelector = '#forgotPopup';
const recoverPasswordP1IdSelector = '#recoverpasswordP1';

function passwordResetForUser(resetValid)
{
	createLoader();

	//AAOL-4915 creation of url if password has expired else for forget password 
	var url = ACC.config.contextPath + '/passwordReset';
	if(!!$(passwordExpiredForUserElementId).val()){
		 url += '?passwordExipred=true';
	}
	$.get(url).done(function(data) {
		var userInfo;
		selected1=null;
		selected2=null;
		selected3=null;

		removeLoader();

		$("#forgotpasswordPopup").html(data);
		if(!resetValid){
			$(forgotPasswordPopupIdSelector).modal('show');
		}
		$('#validEmail').remove();
		jQuery("#recoverpasswordForm").validate({ // VALIDATION
			rules: {
				useremail: {
					required: true
				}
			},
			errorPlacement: function (error, element) {
				error.appendTo(element.parent().parent().find(divWithClassRecPassError));
			},
			onfocusout: false,
			focusCleanup: false
		});
		
		if(resetValid) {
			resetPath($("#email").val(),$("#errorCode").val(),$("#errorMessage").val());
		}
		
		// EMAIL NEXT BUTTON ACTION
		$('#passnext1').click(function () {
			if ($('#recoverpasswordForm').valid()) {
				createLoader();
				jQuery.ajax({
					url: ACC.config.contextPath + '/passwordReset/emailVerification?email=' + $(userEmailIdSelector).val(),
					success: function (data) {
						if (data.status == "valid" && data.questions != null && data.questions != "") {
							$("#secq1").focus();
							var questionsHTMLString = "";
							for (var i = 0; i < data.questions.split("_").length - 1; i++) {
								questionsHTMLString += "<option value = \"" + data.questions.split("_")[i].split("#")[1] + "\">" + data.questions.split("_")[i].split("#")[0] + "</option>";
							}
							$("#secq1").append(questionsHTMLString);
							$("#secq2").append(questionsHTMLString);
							$("#secq3").append(questionsHTMLString);
							$("#secq1").change(function(){
								reviseLists(this);
							});
							$("#secq2").change(function(){
								reviseLists(this);
							});
							$("#secq3").change(function(){
								reviseLists(this);
							});
							$(recoverPasswordP1IdSelector).hide();
							
							$('#recoverpasswordP2').show();										

							
						} else if (data.status == "attack") {
							$(userEmailIdSelector).parent().parent().find(divWithClassRecPassError).html(htmlForlabelWithClassError + $("#blockedUser").attr(requiredFieldAttribute) + "</label>");
						} else if (data.status == "disabled") {
							$(userEmailIdSelector).parent().parent().find(divWithClassRecPassError).html(htmlForlabelWithClassError + $("#accountDisabled").attr(requiredFieldAttribute) + "</label>");
						} else if (data.status === "multimode") {
							multimodeEnabled = true;
							$(recoverPasswordP1IdSelector).hide();
							userInfo = JSON.parse(data.resetInput);
							modifyMultiModeModal(userInfo);
							$(multimodeSelector).show();
						} else {
							$(userEmailIdSelector).parent().parent().find(divWithClassRecPassError).html(htmlForlabelWithClassError + $("#useremailError").val() + "</label>");
						}
					},
					error: function () {
						$(userEmailIdSelector).parent().parent().find(divWithClassRecPassError).html(htmlForlabelWithClassError + $(userEmailIdSelector).attr(requiredFieldAttribute) + "</label>");
					},
					complete: function () {
						removeLoader();
					}
				});
			}
		});

		$('#multimodenext1').click(function () {
			var $multimodeSelection = $(multimodeSelector + " input[type='radio'][name='multimode']:checked")

				if($multimodeSelection.val() === 'EMAIL') {
					emailTokenGeneration(false);
				}
		});
		$('#multimode-email').click(function(){
			$("#multimodenext1").attr("disabled", false);
		});
		
		$('#multimodeEmailResend').click(function(){
			emailTokenGeneration(true);
		});
		
		/*****************************************************************************/
		/** SECRET QUESTION STEP **/
		jQuery("#recoverpasswordFormP2").validate({ // VALIDATION
			rules:{
				secq1:{
					questions:true
				},
				secq2:{
					questions:true
				},
				secq3:{
					questions:true
				},
				text:{
					required:true
				},
				text2:{
					required:true
				},
				text3:{
					required:true
				}
			},
			messages:{
				secq1:{
					questions:$("#secq1").attr(requiredFieldAttribute)
				},
				secq2:{
					questions:$("#secq2").attr(requiredFieldAttribute)
				},
				secq3:{
					questions:$("#secq3").attr(requiredFieldAttribute)
				}
			},
			errorPlacement: function (error, element) {
				if(element.parent().find(divWithClassRecPassError).html() !="") {
					if(element.parent().find(divWithClassRecPassError).find('.error').find("br").size() == 0) {
						element.parent().find(divWithClassRecPassError).find('.error').html($('#emptyQuestionAnswer').val());
					}
				} else {
					error.appendTo(element.parent().find(divWithClassRecPassError));
				}
			},
			onfocusout: false,
			focusCleanup: false
		});
		// SECRET QUESTION NEXT BUTTON ACTION
		$('#passnext2').click(function () {
			$("#recoverpasswordFormP2").find(divWithClassRecPassError).empty();
			if ($('#recoverpasswordFormP2').valid()) {
				var questionsAndAnswers = "";
				questionsAndAnswers += $("#secq1").val();
				questionsAndAnswers += "_" + $('[name="text"]').val();
				questionsAndAnswers += "#" + $("#secq2").val();
				questionsAndAnswers += "_" + $('[name="text2"]').val();
				questionsAndAnswers += "#" + $("#secq3").val();
				questionsAndAnswers += "_" + $('[name="text3"]').val();
				var dataObj = new Object();
				dataObj.email = $(userEmailIdSelector).val();
				dataObj.questionsAndAnswers = questionsAndAnswers;
				jQuery.ajax({
					type: "POST",
					url: ACC.config.contextPath + '/passwordReset/questionsVerification',
					data: dataObj,
					success: function (data) {
						if (data.status == 'valid') {
							$("#checkpass").focus();
							$('#recoverpasswordP2').hide();
							$(forgotPasswordPopupIdSelector).modal('hide');
							
							
							$(resetPasswordPopupIdSelector).modal('show');
							$(recoverPasswordP3IdSelector).show();
							
							$("#checkpass").keyup(function(){
								$('#checkpass').pstrength();
							});
						} else if (data.status != 'invalid' && data.attack=='attack') {
							$($("#recoverpasswordP2").find(divWithClassRecPassError)[$("#recoverpasswordP2").find(divWithClassRecPassError).length-1]).append(htmlForlabelWithClassError + $("#blockedUser").attr(requiredFieldAttribute) + "</label>");
						} else if (data.status == 'invalid' && data.attack!='attack') {
							$($("#recoverpasswordP2").find(divWithClassRecPassError)[$("#recoverpasswordP2").find(divWithClassRecPassError).length-1]).append(htmlForlabelWithClassError + $("#secretquestionsError").attr(requiredFieldAttribute) + "</label>");
						}
					},
					error: function () {
						$("#recoverpasswordP2").find(divWithClassRecPassError).append(htmlForlabelWithClassError + $("#secq1").attr(requiredFieldAttribute) + "</label>");
					}
				});
			} 
		});
		$('[data-toggle="tooltip"]').tooltip(function() {
			title.addClass('tootipcls');
		}); 
		resetPasswordValidation(); 
		resetPasswordButtonAction();	
	});
}

function modifyMultiModeModal(info) {
    const multimodeInput = multimodeSelector + " input[type='radio'][name='multimode']";
    const multimodeLabel = multimodeSelector + "  .multimode-btn label";

    const phoneModes = [
        {
            phoneNumber: info.phone.shift(),
            inputSelector: $(multimodeInput + '[value="CALL"]'),
            labelSelector: $(multimodeLabel + '[for="multimode-call"]')
        },
        {
            phoneNumber: info.text.shift(),
            inputSelector: $(multimodeInput + '[value="TEXT"]'),
            labelSelector: $(multimodeLabel + '[for="multimode-text"]')
        }
    ];

    var formattedPhoneNumber = function formattedPhoneNumber(phoneNumber) {
        var indexOfLastFour = -4;
        var lastFour = phoneNumber.toString().slice(indexOfLastFour);
        return ': ' + "(***) ***-".concat(lastFour);
    };

    phoneModes.forEach(function(mode) {
        var phoneNumber = mode.phoneNumber,
            labelSelector = mode.labelSelector,
            inputSelector = mode.inputSelector;

        if (phoneNumber) {
            var labelVal = inputSelector.val() + formattedPhoneNumber(phoneNumber);
            labelSelector.text(labelVal);
        } else {
            labelSelector.parent().remove();
        }
    });
}

const passwordResetFailedIdSelector = '#passwordResetFailed';
const emailVerificationFailedIdSelector = '#emailVerificationFailed';
function emailTokenGeneration(isResend) {
	createLoader();
	jQuery.ajax({
		type: "POST",
		url: ACC.config.contextPath + tokenNotificationPath + $(userEmailIdSelector).val(),
		success: function (tokenGenSuccess) {
			var tokenGenSuccessJson= JSON.parse(tokenGenSuccess);

			if (tokenGenSuccessJson.response === 'true') {
				if (isResend) {
					if($(multimodeEmailSelector + ' .resendTitleText').val()){
					    $(multimodeEmailSelector + ' .modal-title').text($(multimodeEmailSelector + ' .resendTitleText').val());
					}else{
					    $(multimodeEmailSelector + ' .modal-title').text('Email Has Been Resent!');
					}
				} else {
					$(multimodeSelector).hide();
					$(multimodeEmailSelector).show();
				}
				
			} else {
				$(multimodeSelector).hide();
				if (tokenGenSuccessJson.responseCode === tooManyAttempts) {
					$('#recoverpasswordMulti2').hide();
					showTimeoutError()
				} else if ( tokenGenSuccessJson.responseCode === invalidToken ) {
					$(passwordResetFailedIdSelector).hide();
					$(resetPasswordPopupIdSelector).modal('hide');
					$(forgotPasswordPopupIdSelector).modal('show');
					$(emailVerificationFailedIdSelector).show();
					$('#passResendFailed').click(function () {
						emailFailedTokenResend();
					});
				} else {
					showPasswordError(tokenGenSuccessJson.responseCode,tokenGenSuccessJson.responseMessage);
				}
			}
		},
		error: function () {
			removeLoader();
			$(multimodeSelector).hide();
			$('#passwordResetFailedForbidden').show();
		},
		complete: function () {
			removeLoader();
		}
	});
}

function passwordResetRefreshReturn(){
	window.location.href = ACC.config.contextPath+ "/";
}

const failedUserNameIdSelector = '#failedUserName';
function emailFailedTokenResend() {
	var dataObj = new Object();
	if ($(emailVerificationFailedIdSelector).valid()) {
		createLoader();
		jQuery.ajax({
			type: "POST",
			url: ACC.config.contextPath + tokenNotificationPath + $(failedUserNameIdSelector).val(),
			data: dataObj,
			success: function (data) {
				if(!!data) {
					var response = JSON.parse(data);
					if(response.response === 'true') {
						$(userEmailIdSelector).val($("#email").val());
						$(emailVerificationFailedIdSelector).hide();
						$(multimodeEmailSelector).show();
						if($(multimodeEmailSelector + ' .resendTitleText').val()){
                            $(multimodeEmailSelector + ' .modal-title').text($(multimodeEmailSelector + ' .resendTitleText').val());
                        }else{
                            $(multimodeEmailSelector + ' .modal-title').text('Email Has Been Resent!');
                        }
					} else {
						$("#emailVerificationFailedP1").find(divWithClassRecPassError).append(htmlForlabelWithClassError + $(failedUserNameIdSelector).attr(requiredFieldAttribute) + "</label>");
					}
				}
			},
			error: function () {
				$("#emailVerificationFailedP1").find(divWithClassRecPassError).append(htmlForlabelWithClassError + $(failedUserNameIdSelector).attr(requiredFieldAttribute) + "</label>");
			},
			complete: function () {
				removeLoader();
			}
		});
	}
}

function checkReset() {
	$('#cookiepopup').ready(function() {
		if(!!$("#resetFlag").val()) {
			$('#cookiepopup .clsBtn').click();
			passwordResetForUser(true);
		}
	});
}

function resetPath(emailID,errorCode,errorMessage){
	$(recoverPasswordP1IdSelector).hide();
	if(errorCode !== "") { 
		if (errorCode === tooManyAttempts) {
			showTimeoutError();
		} else if (errorCode === invalidToken ) {
			$(resetPasswordPopupIdSelector).modal('hide');
			$(forgotPasswordPopupIdSelector).modal('show');
			$(emailVerificationFailedIdSelector).show();
			$('#passResendFailed').click(function () {
				emailFailedTokenResend();
			});
		} else {
			showPasswordError(errorCode,errorMessage);

			$(userEmailIdSelector).val(emailID);
		}
	} else {
		$(resetPasswordPopupIdSelector).modal('show');
		$(recoverPasswordP3IdSelector).show();
		
		$("#checkpass").keyup(function(){
			$('#checkpass').pstrength();
		});

		$(userEmailIdSelector).val(emailID);
	}	
}

function showPasswordError(responseCode,responseMessage) {
	$(recoverPasswordP3IdSelector).hide();
	$('#recoverpasswordP2').hide();
	$(resetPasswordPopupIdSelector).modal('hide');
	$('#passwordResetFailErrorMessage').text(' ' + responseCode + (responseMessage === null ? "" :  " - " + responseMessage));
	$(passwordResetFailedIdSelector).show();
	$(forgotPasswordPopupIdSelector).modal('show');

	$('#passResetFail').click(function () {
		$(passwordResetFailedIdSelector).hide();
		emailTokenGeneration(false);
	});
}

function showTimeoutError() {
	showPasswordError(tooManyAttempts,null);
	$('.passwordResetUnlocked').hide();
	$('#passResetFail').remove();
	$('.passwordResetLocked').show();
}

function createLoader() {
	$(document.body).append("<div class='loadingContainer'><div class='loader'></div></div>");
}

function removeLoader() {
	$('.loadingContainer').remove();
}