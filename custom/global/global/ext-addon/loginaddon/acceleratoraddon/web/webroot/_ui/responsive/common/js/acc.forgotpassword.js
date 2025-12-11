/*****************************************************
 * Global Variables required for select functionality.
 */
var selected1;
var selected2;
var selected3;

/*****************************************************
 * Binding forgot password
 */
ACC.forgotpassword = {
		bindAll: function () {
			this.bindForgotPasswordLink($('.password-forgotten'));
		},
		bindForgotPasswordLink: function (link) {
			link.click(function () {
				passwordResetForUser();
		});
		}
	}
/**************************************
 * DOCUMENT READY
 **************************************/
$(document).ready(function () {
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
	if($("#passwordExpiredForUser").length!=0 && $("#passwordExpiredForUser").val()!='') {
		alert("i am here");
		passwordResetForUser();
	}
});
/**************************************
	 * This condition checks for the expired password flag.
	 *//*
	if($("#passwordExpiryFlag").length!=0 && $("#passwordExpiryFlag").val()!='') {
		jQuery.ajax({
			type: 'GET',
			url: ACC.config.contextPath + '/passwordExpired',
			success: function (data) {
				$.colorbox({
					html: data,
					width: '565px',
					overlayClose: false,
					closeButton:false,
					onComplete: function () {
						*//** Hiding the close button on the colorbox **//*
						$('#cboxClose').attr("style", "display:none;");
						$.get($("#hddnLogoutURL").val());
						$("#recoverpasswordP3").show();
						$(".success.reset").hide();
						resetPasswordValidation();
						resetPasswordButtonAction($("#passwordExpiryFlag").val());
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
				});
			}
		});
	}
});*/
/**************************************
 * This function validates the password reset step
 */
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
			error.appendTo(element.parent().parent().parent().parent().find('div.recPassError'));
		},
		onfocusout: false,
		focusCleanup: false
	});
}
/**************************************
 * This function sets the click function for password reset step
 */
function resetPasswordButtonAction(userEmail) {
	$('#passnext3').click(function () {
		if ($('#recoverpasswordFormP3').valid()) {
			var dataObj = new Object();
			dataObj.email = $("#useremail").length != 0 ? $("#useremail").val() : userEmail;
			dataObj.password = $("#checkpass").val();
			jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath + '/passwordReset/revisionVerification',
				data: dataObj,
				success: function (data) {
					if (data == "true") {
						$('#recoverpasswordP3').hide();
						$('#recoverpasswordP4').show();
						$.colorbox.resize();
					} else {
						$("#recoverpasswordP3").find("div.recPassError").append("<label class=\"error\">" + data  + "</label>");
						$.colorbox.resize();
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
	for(i=1;i<document.getElementById("secq" + id).length;i++) {
		if(thisObj[thisObj.selectedIndex].innerHTML == document.getElementById("secq" + id)[i].innerHTML) {
			document.getElementById("secq" + id).options.remove(i);
			break;
		}
	}
}

function passwordResetForUser()
{
	$.get(ACC.config.contextPath + '/passwordReset').done(function(data) {
		selected1=null;
		selected2=null;
		selected3=null;
		$.colorbox({
			html: data,
			height: '305px',
			width: '565px',
			overlayClose: false,
			closeButton: false,
			onOpen: function () {
				$('#validEmail').remove();
			},
			onComplete: function () {
				$('#dataforgetpassword').show();
				$("#useremail").focus();
				/*****************************************************************************/
				/** EMAIL VERIFICATION STEP **/
				jQuery("#recoverpasswordForm").validate({ // VALIDATION
					rules: {
						useremail: {
							required: true
						}
					},
					errorPlacement: function (error, element) {
						error.appendTo(element.parent().parent().find('div.recPassError'));
					},
					onfocusout: false,
					focusCleanup: false
				});
				// EMAIL NEXT BUTTON ACTION
				$('#passnext1').click(function () {
					if ($('#recoverpasswordForm').valid()) {
						jQuery.ajax({
							url: ACC.config.contextPath + '/passwordReset/emailVerification?email=' + $("#useremail").val(),
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
									$('#recoverpasswordP1').hide();
									$('#recoverpasswordP2').show();

									$.colorbox.resize();
								} else if (data.status == "attack") {
									$("#useremail").parent().parent().find("div.recPassError").append("<label class=\"error\">" + $("#blockedUser").attr("data-msg-required") + "</label>");
								} else if (data.status == "disabled") {
									$("#useremail").parent().parent().find("div.recPassError").append("<label class=\"error\">" + $("#accountDisabled").attr("data-msg-required") + "</label>");
								} else {
									$("#useremail").parent().parent().find("div.recPassError").append("<label class=\"error\">" + $("#useremailError").val() + "</label>");
								}
							},
							error: function () {
								$("#useremail").parent().parent().find("div.recPassError").append("<label class=\"error\">" + $("#useremail").attr("data-msg-required") + "</label>");
							}
						});
					}
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
							questions:$("#secq1").attr("data-msg-required")
						},
						secq2:{
							questions:$("#secq2").attr("data-msg-required")
						},
						secq3:{
							questions:$("#secq3").attr("data-msg-required")
						}
					},
					errorPlacement: function (error, element) {
						if(element.parent().find('div.recPassError').html() !="") {
							if(element.parent().find('div.recPassError').find('.error').find("br").size() == 0){
								//element.parent().find('div.recPassError').find('.error').append("<br/>" + error.html());
								element.parent().find('div.recPassError').find('.error').html($('#emptyQuestionAnswer').val());
							}
						} else {
							error.appendTo(element.parent().find('div.recPassError'));
						}
					},
					onfocusout: false,
					focusCleanup: false
				});
				// SECRET QUESTION NEXT BUTTON ACTION
				$('#passnext2').click(function () {
					$("#recoverpasswordFormP2").find("div.recPassError").empty();
					if ($('#recoverpasswordFormP2').valid()) {
						var questionsAndAnswers = "";
						questionsAndAnswers += $("#secq1").val();
						questionsAndAnswers += "_" + $('[name="text"]').val();
						questionsAndAnswers += "#" + $("#secq2").val();
						questionsAndAnswers += "_" + $('[name="text2"]').val();
						questionsAndAnswers += "#" + $("#secq3").val();
						questionsAndAnswers += "_" + $('[name="text3"]').val();
						var dataObj = new Object();
						dataObj.email = $("#useremail").val();
						dataObj.questionsAndAnswers = questionsAndAnswers;
						jQuery.ajax({
							type: "POST",
							url: ACC.config.contextPath + '/passwordReset/questionsVerification',
							data: dataObj,
							success: function (data) {
								if (data.status == 'valid') {
									$("#checkpass").focus();
									$('#recoverpasswordP2').hide();
									$('#recoverpasswordP3').show();
									$.colorbox.resize();
									$("#checkpass").keyup(function(){
										$('#checkpass').pstrength();
									});
								} else if (data.status != 'invalid' && data.attack=='attack') {
									$($("#recoverpasswordP2").find("div.recPassError")[$("#recoverpasswordP2").find("div.recPassError").length-1]).append("<label class=\"error\">" + $("#blockedUser").attr("data-msg-required") + "</label>");
								} else if (data.status == 'invalid' && data.attack!='attack'){
									$($("#recoverpasswordP2").find("div.recPassError")[$("#recoverpasswordP2").find("div.recPassError").length-1]).append("<label class=\"error\">" + $("#secretquestionsError").attr("data-msg-required") + "</label>");
								}
							},
							error: function () {
								$("#recoverpasswordP2").find("div.recPassError").append("<label class=\"error\">" + $("#secq1").attr("data-msg-required") + "</label>");
							}
						});
					} else {
						if ($('.lightBoxTemplateWidth').css('height') != "540px") {
							$('.lightBoxTemplateWidth').css({'height' : '540px'});
							$.colorbox.resize();
						}
					}
				});
				/*****************************************************************************/
				/** PASSWORD RESET STEP **/
				resetPasswordValidation(); // Validation
				resetPasswordButtonAction(); // Reset Password Button Action
				/*****************************************************************************/
				/** SUCCESS STEP **/
				$('#pass4').click(function () {
					$.colorbox.close();
				});
			},
			onClosed: function () {
			}
		});
	});
}