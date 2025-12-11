/* Register Form Validation */
//Getting Start date & End getting

ACC.formCheckFunc = {

		bindAll: function() {
			this.bindSearchAutocomplete();


		},
		isNullOrEmpty: function(value) {
			if (value === null || value === "" || value === undefined || value === "undefined"  || value === "null") {
				return true;
			} else {
				return false;
			}
		},
		isNotNullAndNotEmpty: function(value) {
			return !ACC.formCheckFunc.isNullOrEmpty(value);
		},
		bindSearchAutocomplete: function() {
			$('#global-search-txt').typeahead({
			 source: function (query, process) {
			        return $.get(ACC.config.contextPath +'/search/autocompleteSecure',  { term : $('#global-search-txt').val() }, function (data) {

			        			return process(data);
			        		});
			    },
			    minLength: 3,
				autoFocus: false
				//menuClass:
		    });
		}
	};

if($('.AddAccountError').children().length>0){
	$('#collapse5').collapse('show');
	$('#collapse5').prev().find('.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus');
}

jQuery(function($){

	jQuery.validator.addMethod("phoneUS", function(phone_number, element) {
    phone_number = phone_number.replace(/\s+/g, "");
    return this.optional(element) || phone_number.length > 9 && phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
	},SPECIFY_PHONE_NUMBER);

	jQuery.validator.addMethod("postalcode", function (value, element) {
		return this.optional(element) || /^(([0-9](-)?)*)+$/.test(value);
	},INVALID_POSTALCODE);
	//INVALID_PHONE_NUMBER="Please enter a valid phone number"


	jQuery.validator.addMethod("phoneFormateUS", function (value, element) {
    	return this.optional(element) || /^(([0-9](-)?)*)+$/.test(value);
	},INVALID_PHONE_NUMBER);

	jQuery.validator.addMethod("faxFormate", function (value, element) {
    	return this.optional(element) || /^(([0-9](-)?)*)+$/.test(value);
	},INVALID_FAX);

	jQuery.validator.addMethod("mobileFormate", function (value, element) {
    	return this.optional(element) || /^(([0-9](-)?)*)+$/.test(value);
	}, INVALID_MOBILE_NUMBER);

	jQuery.validator.addMethod("numberComma", function(value, element) {
		return this.optional(element) || /^(([0-9](,)?)*)+$/.test(value);
	}, INVALID_ACCOUNT_NUMBER);

	jQuery.validator.addMethod("lettersonly", function(value, element) {
		  return this.optional(element) || /^[a-z\s]+$/i.test(value);
	},LETTER_ONLY);


	jQuery.validator.addMethod("dateValidates", function(value, element) {
		var rawStartDate = $.trim($("#datePicker1").val());
		var startDate = $("#datePicker1").datepicker('getDate');
		var endDate = $("#datePicker2").datepicker('getDate');

		var isValid;
		if (rawStartDate == "" || ACC.formCheckFunc.isNullOrEmpty(startDate)) {
		    isValid = true;
		} else {
			isValid = new Date(startDate) <= new Date(endDate)
		}

		return isValid;
	}, DATE_SELECTION);

	var startDateValue;
	var EndDatevalue;
	jQuery.validator.addMethod("dateReportValidates", function(value, element) {


		if( $("#reports-startDate").length>0){
			startDateValue = $("#reports-startDate").datepicker('getDate');
			EndDatevalue = $("#reports-endDate").datepicker('getDate');
		}

		if( $("#reports-fromDate").length>0){
			startDateValue = $("#reports-fromDate").datepicker('getDate');
			EndDatevalue = $("#reports-toDate").datepicker('getDate');
		}

		if( $("#invoice-startDate").length>0){
			startDateValue = $("#invoice-startDate").datepicker('getDate');
			EndDatevalue = $("#invoice-endDate").datepicker('getDate');
		}

		if( $("#invoiceStartDate").length>0){
			startDateValue = $("#invoiceStartDate").datepicker('getDate');
			EndDatevalue = $("#invoiceEndDate").datepicker('getDate');
		}

		if(ACC.formCheckFunc.isNotNullAndNotEmpty(startDateValue)){
			if(new Date(startDateValue) <= new Date(EndDatevalue)){
				return true;
			}
			else{
				return false;
			}

		}
		else{
			return true;
		}


	}, DATE_SELECTION);

	/*jQuery.validator.addMethod("dateReportEndDateValidates", function(value, element) {
		if( $("#reports-endDate").length>0){
			EndDatevalue = $("#reports-endDate").datepicker('getDate');
		}

		if( $("#reports-fromDate").length>0){
			EndDatevalue = $("#reports-toDate").datepicker('getDate');
		}

		if(ACC.formCheckFunc.isNotNullAndNotEmpty(startDateValue)){
			if(new Date(EndDatevalue) <= new Date()){
				return true;
			}
			else{
				return false;
			}

		}
		else{
			return true;
		}


	}, 'End date should not exceed the current date');*/

	jQuery.validator.addMethod('complexityCheck', function () {
	 	var strength = 0;


		if ($("#newPassword").val().match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
			strength=strength+2;
		}
		if ($("#newPassword").val().match(/([a-z])/)) {
			strength=strength+2;
		}
		if ($("#newPassword").val().match(/([A-Z])/)) {
			strength=strength+2;
		}
		if ($("#newPassword").val().match(/([0-9])/)) {
			strength=strength+2;
		}

		if (strength >= 4) {

			return true;
		} else {
			return false;
		}
    }, $("#complexityInvalid").val());


	/*Hiding success msg for pwd check*/

	$("#emailPreferencesSubmit").click(function(){
		$(".recPassError").text("");
		$("#ProfileChangePassword").validate();
	});


	/* Form Validation - Register Page */
	jQuery("#registerForm").validate({
        rules: {
        	wWID:{
        		alphanumeric: true
        	},
        	supervisorName: {
        		alphabetsSpaces: false
			},
			firstName: {
				alphabetsSpaces: true
			},
			lastName: {
				alphabetsSpaces: true
			},
        	zip: {
				digits:true
			},
			regPhoneNumber: {
				phoneFormateUS: true
			},
			regMobileNumber: {
				phoneFormateUS: true
			},
			regFaxNumber: {
				phoneFormateUS: true
			},
			supNumber: {
				phoneFormateUS: true
			},
			billToZipCode: {
				digits:true
			},
			shipToZipCode: {
				digits:true
			},
			estAmount: {
				required: true
			},
			estimatedAmountPerYear: {
				digits:true
			},
			emailAddress: {
				required: true
			},
			reenterEmailAddress: {
				required: true,
				equalTo: "#emailAddress"
			},
			password: {
				required: true,
				minlength: 8,
				chkStrength: true
				},
			checkpass2: {
				equalTo: "#password"
			}
        },
        messages: {
        	emailAddress: {
        		email: INVALID_EMAIL
        	},
        	checkpass2: {
        		equalTo: PASSWORD_MISMATCH
        	},
        	reenterEmailAddress: {
        		equalTo: EMAIL_MISMATCH,
        		email: INVALID_EMAIL
        	},
        	password: {
        		chkStrength: PASSWORD_COMPLEXITY
        	}
        },
		errorPlacement: function(error, element) {

			if($(element).attr("id") === "password" || $(element).attr("id") === "checkpass2")
				error.appendTo(".passwordLightboxbody3 .registerError");
			else
				error.appendTo( element.parent().parent().find('div.registerError') );
		},
		onfocusout: false,
		focusCleanup: false
	});

	jQuery("#userActivationForm").validate({
		rules: {
			regMobileNumber: {
				phoneFormateUS:true
			},
			regPhoneNumber: {
				phoneFormateUS:true
			},
			regFaxNumber: {
				phoneFormateUS:true
			},
			zip: {
				digits:true
			}
		},
		errorPlacement: function(error, element) {
		    error.appendTo( element.parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false

	});

	jQuery('#btncreacc').click(function(e){
      if(!jQuery("#userActivationForm").valid())
      {
        $('#globalerrorform').show();
      }
      else
      {
        $('#globalerrorform').hide();
      }
   });

	jQuery('#buttoncreacc2').click(function(e){
		if(!jQuery("#completeuserRegForm").valid())
		{
			$('#globalerrorform1').show();

		}
		else
		{
			$('#globalerrorform1').hide();

		}
	});

	jQuery("#recoverpasswordForm").validate({
	rules: {
		useremail: {
			required: true
		}
	},
	errorPlacement: function(error, element) {
		error.appendTo( element.parent().parent().find('div.recPassError') );
	},
	onfocusout: false,
	focusCleanup: false

	});

	jQuery("#recoverpasswordFormP2").validate({
        rules: {

        },
		errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().parent().parent().find('div.recPassError') );
		},
		onfocusout: false,
		focusCleanup: false

	});

	jQuery("#recoverpasswordFormP3").validate({

        rules: {

			checkpass: {
				required: true,
				chkStrength: true
				},

			checkpass2: {
				required: true,
				equalTo: "#checkpass"
			}
        },
		errorPlacement: function(error, element) {

			error.appendTo( element.parent().parent().parent().parent().find('div.recPassError') );
		},
		onfocusout: false,
		focusCleanup: false

	});

	jQuery("#completeuserRegForm").validate({
        rules: {

			choosepwd12: {
				required: true
				},
			retypepwd12: {
				required: true,
				equalTo: "#choosepwd12"
			}
        },
		errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find('div.registerError') );
		},
		onfocusout: false,
		focusCleanup: false

	});


	jQuery("form#quicksearchvalidate").validate({

	groups: {
	productid: "productid quantity"

		 },
	 errorPlacement: function(error, element) {
		error.appendTo( element.parent().parent().find('div.registerError'));

	},


	onfocusout: false,
	focusCleanup: false


	});

	jQuery("#myprofilesecretquescheck").validate({

		 errorPlacement: function(error, element) {
			 if(element.attr("id") =="questionList[0].code"){

				 if($(element).hasClass('selectpicker')){
					 element.parent().parent().parent().find('div.registerError').html(error);
				 }
				 else{
					 element.parent().parent().find('div.registerError').html(error);
				 }

					$("#myProfileSecretQuestError").show();
					$("#myProfileSecretQuestError").find('label.error').show();
			 }else{

				 if($(element).hasClass('selectpicker')){
					 element.parent().parent().parent().find('div.registerError').html(error);
				 }
				 else{
					 element.parent().parent().find('div.registerError').html(error);
				 }


			 }
			$("#myProfileSecretQuestError").show();
			$("#myProfileSecretQuestError").find('label.error').show();
		},

		onfocusout: false,
		focusCleanup: false
	});

	jQuery("#myprofilecheck").validate({

		rules: {
			state: {
				lettersonly: true
			},
			profilePhoneNumber: {
				phoneFormateUS: true
			},profileSupervisorPhone: {
				phoneFormateUS: true
			},
			supervisorPhone: {
				phoneFormateUS: true
			},
			profileSupName: {
				lettersonly: true
			},
			supervisorEmail: {
				email: true,
				required: true
			},
			supervisorName: {
				lettersonly: true
			}
        },
        messages: {
        	supervisorEmail: {
				email: INVALID_EMAIL
			},
			fax: {
				phoneFormateUS: INVALID_FAX
			}
        },
		errorPlacement: function(error, element) {
			if(element.attr("id") == "profilePhoneNumber"){
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			}else if(element.attr("id") == "profileSupervisorPhone"){
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			}else{
			error.appendTo(element.parent().parent().find('div.registerError'));
			}
		},
		onfocusout: false,
		focusCleanup: false


	});
/*----------------------- change security question -----------------------------------*/
	$("#changeQuestionForm").validate({
		 errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false

	});

	$('#changeQuestionPasswordSubmit').click(function(e){
		if(jQuery("#changeQuestionForm").valid())
		{
			$("#changesecuritypsd").click();
			//$('#collapse4').addClass('in');
			/*$(".changeSecurityQuestions").css("display","none");
			$(".changeSecurityQuestions2").css("display","block");	*/
		}

	});

	jQuery("#headervalidateCartForm").validate({
        rules: {

        	surgeonName: {
				required: true
				},
			spinSalesUCN: {
				required: true
				},
        },

		errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find('div.registerError') );
		},
		onfocusout: false,
		focusCleanup: false

	});
	 if($('.splStockPartnerForm_'+currViewScreenName).length != 0){
		    $('.splStockPartnerForm_'+currViewScreenName).each(function(i) {
		    	$(this).removeAttr('novalidate');
		    	jQuery(this).validate({
		            rules: {

		            	sspNumber: {
		    				required: true
		    				},
		            },

		    		errorPlacement: function(error, element) {
		    			/*error.appendTo( element.parent().parent().find('div.registerError') );*/
		    			//$(element).hide();

		    			error.appendTo( element.parent().next('div.registerError') );
		    		},
		    		onfocusout: false,
		    		focusCleanup: false

		    	});
		    });
	 }

	 if($('.orderEntry_'+currViewScreenName).length != 0){
		    $('.orderEntry_'+currViewScreenName).each(function(i) {
		    	$(this).removeAttr('novalidate');
		    	jQuery(this).validate({
		            rules: {

		            	lotNo: {
		    				required: true
		    				}
		            },

		    		errorPlacement: function(error, element) {
		    			/*error.appendTo( element.parent().parent().find('div.registerError') );
		    			element.parent().parent().find('div.registerError').show();*/
		    			error.appendTo( element.parent().next('div.registerError') );
		    			element.parent().next('div.registerError').show;

		    		},
		    		onfocusout: false,
		    		focusCleanup: false

		    	});
		    });

		}

	/*if($(".orderEntry").length != 0){
	    $('.orderEntry').each(function(i) {
	    	$(this).removeAttr('novalidate');
	    	jQuery(this).validate({
	            rules: {

	            	lotNo: {
	    				required: true
	    				}
	            },

	    		errorPlacement: function(error, element) {
	    			error.appendTo( element.parent().parent().find('div.registerError') );
	    			element.parent().parent().find('div.registerError').show();
	    			error.appendTo( element.parent().next('div.registerError') );
	    			element.parent().next('div.registerError').show;

	    		},
	    		onfocusout: false,
	    		focusCleanup: false

	    	});
	    });

	}*/


	var securityFlag=$('.jnj-security-check').attr('data-securityFlag');
	$("#changesecuritypsd").click(function(){
		if(securityFlag=="true"){
			if($(this).hasClass('panel-opened')){
				$("#change-security-sign").removeClass('glyphicon-minus').addClass('glyphicon-plus');
				$(this).removeClass('panel-opened');
			}
			else{
				$("#change-security-sign").removeClass('glyphicon-plus').addClass('glyphicon-minus');
				$(this).addClass('panel-opened');
			}

		}

	});




	if(securityFlag=='true'){
		$("#changesecuritypsd").removeAttr('data-target').attr('data-toggle','collapse').attr('data-parent','#accordian').attr('href','#collapse4');
		$("#changesecuritypsd").click();
		$("#change-security-sign").removeClass('glyphicon-plus').addClass('glyphicon-minus');
		//$('#collapse4').addClass('in');
	}
	else if(securityFlag=='false'){
		$("#changesecuritypsd").removeAttr('data-parent').attr('data-toggle','modal').attr('data-target','#securitycheckpopup').attr('href','#');
		$('#securitycheckpopup').modal('show');
		$("#change-security-sign").removeClass('glyphicon-minus').addClass('glyphicon-plus');
	}
/*-------------------------- End change security question------------------------------*/

/*----------------------- Credit Card -----------------------------------*/
	jQuery("#addEditCreditCardVal").validate({

		 errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find('div.registerError'));

		},
		onfocusout: false,
		focusCleanup: false

	});
/*-------------------------- End Credit Card ------------------------------*/

	/*----------------------- One Time Shipping Address Start -----------------------------------*/
		jQuery("#oneTimeShippingAddressForm").validate({

			 errorPlacement: function(error, element) {
				error.appendTo( element.parent().parent().find('div.registerError'));

			},
			onfocusout: false,
			focusCleanup: false

		});
	/*-------------------------- End One Time Shipping Address ------------------------------*/

/*----------------------- Start Update PO -----------------------------------*/
	jQuery("#updatePOValidation").validate({

		 errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find('div.registerError'));

		},
		onfocusout: false,
		focusCleanup: false

	});
/*-------------------------- End Update PO ------------------------------*/

/*----------------------- profile Changed Password -----------------------------------*/
	jQuery("#ProfileChangePassword").validate({

		rules: {
			newPassword: {
				required: true,
				complexityCheck: true,
				minlength: 8,
				equalTo: "#newPassword"
			},
			checkNewPassword: {
				required: true,
				equalTo: "#newPassword"
			}
        },
        messages: {
        	checkNewPassword: {
        		equalTo: PASSWORD_MISMATCH
        	},
        	newPassword: {
        		complexityCheck: PASSWORD_COMPLEXITY
        	}
        },
		 errorPlacement: function(error, element) {

			 $(error).remove();
			$(error).insertAfter(element);

		},
		onfocusout: false,
		focusCleanup: false

	});
/*-------------------------- End profile Changed Password ------------------------------*/


/*-----------Dispute Order Form validation---------------*/

jQuery("#disputeOrderForm").validate({
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery("#disputeItemForm").validate({
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

/*-----------End Dispute Order Form validation--------------*/
/* Create New Profile Start */
$(".phoneFormat").blur(function() {
   		var phoneVal = $(this).val().split("-").join("");
		phoneVal = phoneVal.match(new RegExp(".{1,4}$|.{1,3}", "g")).join("-");
		$(this).val(phoneVal);
	});

jQuery("#createNewProfileForm").validate({
    rules: {
	    	firstName: {
	    		 lettersonly: true
			},
			lastName: {
				 lettersonly: true
			},
    		email: {
                 email: true
           },
           phone: {
               phoneFormateUS: true

         },
         supervisorPhone: {
               phoneFormateUS: true
         },
    },
        groups: {
               mdd: "mdd consumer"
        },
	    messages:{
	        mdd: {
	        	require_from_group: SELECT_SECTOR
	 },
	 consumer: {
	 require_from_group: SELECT_SECTOR
	 }
	 },
    errorPlacement: function(error, element) {
   	error.appendTo(element.parents().closest('.um-element-col').find('div.registerError'));
    },
    onfocusout: false,
    focusCleanup: false

});


/* Create New Profile End */

/* Edit User Profile Start */
 jQuery("#editUserProfileForm").validate({

	    rules: {
	    	   firstName: {
	    		   lettersonly: true
			   },
			   lastName: {
	    		   lettersonly: true
			   },
	    	   emailLogin: {
	                 email: true
	           },
	           postalCode : {
	                 digits: true
	           }
	    },

	    messages:{
	    	/*roles1: {
	        	   require_from_group: "select at least one Role !"
	           },
	           consumer: {
	        	   require_from_group: "select at least one sector !"
	           } */
	    },
	    errorPlacement: function(error, element) {
	    	//error.appendTo( element.parent().parent().find('div.registerError'));
	    	//error.appendTo( element.next('div.registerError'));
	    	error.appendTo(element.parents().closest('.um-element-col').find('div.registerError'));
	    },
	    onfocusout: false,
	    focusCleanup: false

	});
/* Edit User Profile End */


	/** My profile page message **/
	$("#myPersonalInfo #profilSaveupdate").click(function(e){
		var  profileSupervisorPhone = $("#profileSupervisorPhone").val();
		var  profilePhoneNumber = $("#profilePhoneNumber").val();
		var errorMsg = false;
		var errorSupMsg = false;
		var regexMsg = false;
		var regexSupMsg = false;
		var regex = /^(([0-9](-)?)*)+$/;
		/*if( profilePhoneNumber != undefined && profilePhoneNumber != "" && profilePhoneNumber.match(regex)){
			errorMsg = true;
		}else if(profilePhoneNumber == undefined || profilePhoneNumber == ""){

			errorMsg = false;
		}else if(profilePhoneNumber != undefined && profilePhoneNumber != "" && !profilePhoneNumber.match(regex)){

			regexMsg = true;
		}

		if( profileSupervisorPhone != undefined && profileSupervisorPhone != "" && profileSupervisorPhone.match(regex)){
			errorSupMsg = true;
		}else if(profileSupervisorPhone == undefined || profileSupervisorPhone == ""){

			errorSupMsg = false;
		}else if(profileSupervisorPhone != undefined && profileSupervisorPhone != "" && !profileSupervisorPhone.match(regex)){

			regexSupMsg = true;
		}

		if(!jQuery("#myprofilecheck").valid() && !errorMsg)
		{
			$("#profilePhoneNumberError").html("<label for='profilePhoneNumber' class='error'>Please Enter Phone Number</label>");

			$('.myProfileGlobalError').find(".error").show();
		} else  if(jQuery("#myprofilecheck").valid() && regexMsg){
			$("#profilePhoneNumberError").html("<label for='profilePhoneNumber' class='error'>Please Enter Valid Phone Number</label>");
			$("#profileSupervisorPhoneError").html("<label for='profileSupervisorPhone' class='error'>Please Enter Valid Phone Number</label>");
		}
		else
		{
			$('.myProfileGlobalError').find(".error").hide();
			$('#myprofilecheck').submit();
		}*/

		if(!jQuery("#myprofilecheck").valid())
		{

			$('.myProfileGlobalError').find(".error").show();
		}
		else
		{
			$('.myProfileGlobalError').find(".error").hide();
			$('#myprofilecheck').submit();
		}

	});



	/*My profile validation success message appears */
	jQuery('#myprofileedit #profilSaveupdate').click(function(e){
			if(!jQuery("#myprofilecheck").valid())
			{
				e.preventDefault();
				$('#myprofileedit .success').hide();
				$('.myProfileGlobalError').find(".error").show();
			}
			else
			{
				$('#myprofileedit .success').show();
				$('.myProfileGlobalError').find(".error").hide();
			}
		});
		jQuery("#myprofilepasswrdcheck").validate({
			rules: {
				choosepwd: {
					required: true
					},
				retypepwd: {

					equalTo: "#choosepwd"
				}
			},

			 errorPlacement: function(error, element) {
				error.appendTo( element.parent().parent().find('div.registerError'));

			},

			onfocusout: false,
			focusCleanup: false

		});

});
/*
 * VALIDATE FOR START AND END DATE
 */
/*jQuery("#orderHistoryForm").validate({
	rules: {
			 endDate: {
				dateValidates: true
			}
		},
		showErrors: function(errorMap, errorList) {
			 if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parents().closest('.um-element-col').find('div.registerError'));
		},
	onfocusout: false,
	focusCleanup: false
});*/

//report - multiproduct
jQuery("#multiPurchaseReportForm").validate({
	rules: {
			startDate: {
				dateReportValidates: false
			},
			endDate: {
				dateReportValidates: true
			}
		},
		showErrors: function(errorMap, errorList) {
			 if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
		errorPlacement: function(error, element){
			error.appendTo(element.parent().next('div.registerError'));
		},
	onfocusout: false,
	focusCleanup: false
});


//AAOL-2410: report - saleseport
jQuery("#salesReportAnalysisForm").validate({

		rules: {
			startDate: {
				dateReportValidates: false
			},
			endDate: {
				dateReportValidates: true
			}
		},
		showErrors: function(errorMap, errorList) {
			 if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));
		},
	onfocusout: false,
	focusCleanup: false
});

// Validate Add New Account functionality - START
jQuery("#addNewAccount").validate({
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});


jQuery('#cartStep1Saveupdate').click(function(e){
		if(!jQuery("#addNewAccount").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
});
// Validate  Add New Account functionality - END

// Validate shopping cart functionality - START
jQuery("#cartStep1Check").validate({
	rules: {
		dropShip: {
			alphanumeric: true
		},
		cordisHouseAccount: {
			validCordisAccNumber: true
		}
		},
		/* show up consolidated messages */
		/*Changes done by Sakshi*/
		showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery.validator.addMethod("validCordisAccNumber",
        function(value, element){
				if($('#cordisHouseAccount').parent().parent().parent().find('div.registerError label').css("display") == "block"){
					return false;
				}
				else {
                       return true;
				}
        },
        $('#cordisError').val());

// Select Surgeon Name - START
jQuery("#selectSurgeonNameForm").validate({
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery("#replenishValidateCartForm").validate({
	rules: {
			purchaseOrder: {
				required: true
			}
		},
		/* show up consolidated messages */
		/*Changes done by Sakshi*/
		/*showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},*/
	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery("#consignmentFillupForm").validate({
	rules: {
			customerPONo: {
				required: true
			},

			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},

			poDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},
			requestDelDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			}
			/*
			endUser: {
				required: true
			},
			poDate: {
				required: true
			},
			requestDelDate: {
				required: true
			}*/
		},
		/* show up consolidated messages */
		/*Changes done by Sakshi*/
		/*showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},*/

	onfocusout: false,
	focusCleanup: false,
	onchage:false
});



jQuery("#consignmentReturnForm").validate({
	rules: {
			customerPONo: {
				required: true
			},

			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},

			poDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},
			returnCreatedDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			}
			/*
			endUser: {
				required: true
			},
			poDate: {
				required: true
			},
			requestDelDate: {
				required: true
			}*/
		},
		/* show up consolidated messages */
		/*Changes done by Sakshi*/
		/*showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},*/

	onfocusout: false,
	focusCleanup: false,
	onchage:false
});

//Soumitra AAOL-3784
jQuery("#consignmentChargeForm").validate({
	rules: {
			customerPONo: {
				required: true
			},

			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},

			poDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},
			requestDelDate: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},
			batchNumber: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			},
			serialNumber: {
				required: true
			},
			errorPlacement: function(error, element) {
				error.appendTo(element.parent().parent().parent().find('div.registerError'));
			}
			/*
			endUser: {
				required: true
			},
			poDate: {
				required: true
			},
			requestDelDate: {
				required: true
			}*/
		},
		/* show up consolidated messages */
		/*Changes done by Sakshi*/
		/*showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},*/

	onfocusout: false,
	focusCleanup: false,
	onchage:false
});

jQuery.validator.addMethod("priceOverRideMin",
        function(value, element){
				if($("#isServiceFees").val() == 'true'){
					return true;
				}
				else {
						var itemPriceText  = $("#priceOverrideForm .ttBlock ul li:first-child .itemLeft span.textBlack").text()
						/*overRidePriceThreshold = Number($("#overridePriceThreshold").val());
						maxOverRidePriceThreshold = Number($("#maxOverridePriceThreshold").val());
						minLimit = itemPriceNumber - (itemPriceNumber*overRidePriceThreshold)/100;
                        maxLimit = itemPriceNumber - (itemPriceNumber*maxOverRidePriceThreshold)/100;*/
						itemPriceNumber = Number(itemPriceText.replace(/[^0-9\.]+/g,""));
                      	minLimit = itemPriceNumber - (itemPriceNumber*75)/100,
                      	overRidePrice = $("#priceOverrideForm #overridePrice").val();
                        return minLimit<=overRidePrice;
                        /*if(minLimit <= overRidePrice){
                        	return true;
                        }else if(overRidePrice<=itemPriceNumber && overRidePrice<=minLimit && overRidePrice>=maxLimit){
                        	alert("A price floor has been set for this product at "+ overRidePriceThreshold + "%.  A price discount cannot exceed this value");
                        	return true;
                        }else{
                        	return false;
                        }*/
				}
        },
        PRODUCT_PRICE);
		//"A price discount cannot exceed 90% of the original item price. Please enter a price within the limit.");

jQuery.validator.addMethod("priceOverRideMax",
        function(value, element){
                        var itemPriceText  = $("#priceOverrideForm .ttBlock ul li:first-child .itemLeft span.textBlack").text()
                        itemPriceNumber = Number(itemPriceText.replace(/[^0-9\.]+/g,"")),
                        overRidePrice = $("#priceOverrideForm #overridePrice").val();
                        return overRidePrice<=itemPriceNumber;
        },
        PRODUCT_PRICE_GREATER);

//Price Override - START
jQuery("#priceOverrideForm").validate({

        rules:{
                        overridePrice:{
                                        number:true,
                                        priceOverRideMax:true,
                                        priceOverRideMin:true
                        }
        },
        messages:{
                        number:NUMBERS_ONLY
        },
        errorPlacement: function(error, element) {
                        error.appendTo(element.parent().parent().parent().find('div.registerError'));
        },
        onfocusout: false,
        focusCleanup: false
});


jQuery('#cartStep1Saveupdate').click(function(e){
		if(!jQuery("#cartStep1Check").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
});
// Validate shopping cart functionality - END

// Cut Report - Start //
	jQuery("#cutOrderReportForm").validate({

		rules: {
			fromDate: {
				dateReportValidates: false
			},
			toDate: {
				dateReportValidates: true
			}
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));

		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#cutOrderReportSubmit').click(function(e){
		if(!jQuery("#cutOrderReportForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
	});
// Cut Report - End //

// Backorder Report - Start //
	jQuery("#backOrderReportForm").validate({

		rules: {
			fromDate: {
				dateReportValidates: false
			},
			toDate: {
				dateReportValidates: true
			}
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));
			//$('#backorder-report-date-error').html(error);
		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#backOrderReportSubmit').click(function(e){
		if(!jQuery("#backOrderReportForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
	});
// Backorder Report - End //
// Inventory Report - Start //
	jQuery("#inventoryReportForm").validate({
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#inventoryReportSubmit').click(function(e){
		if(!jQuery("#inventoryReportForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
	});
// Inventory Report - End //
// Single Purchase Report - Start //
	jQuery("#singlePurchaseReportForm").validate({
		rules: {
		startDate: {
			dateReportValidates: false
		},
		endDate: {
			dateReportValidates: true
		}
	},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));
			error.appendTo(element.next('div.registerError'));
			//$('#single-purchase-report-date-error').html(error);
		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#singlePurchaseReportSubmit').click(function(e){
		if(!jQuery("#singlePurchaseReportForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();
		}
	});
// Single Purchase Report - End //



// Sales Purchase Report - End //
	// AAOL-2410: SalesReport - Start //
	/*jQuery("#salesReportAnalysisForm").validate({
		rules: {
			startDate: {
				dateReportValidates: true
			},
			endDate: {
				dateReportValidates: true
			}
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false
	});*/
	jQuery('#salesReportPageSubmit').click(function(e){
		if(!jQuery("#salesReportAnalysisForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();
		}
	});
// SingleReport - End //





// Multi Purchase Report - Start //
	jQuery("#multiPurchaseReportForm").validate({
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#multiPurchaseReportSubmit').click(function(e){
		if(!jQuery("#multiPurchaseReportForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();
		}
	});
// Multi Purchase Report - End //

	//AAOL-4603: OA Delivery List Report - Start //
	jQuery("#dlReportAnalysisForm").validate({
		rules: {
			startDate: {
				dateReportValidates: false
			},
			endDate: {
				dateReportValidates: true
			}
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));

		},
		onfocusout: false,
		focusCleanup: false
	});

	jQuery('#oadlReportSubmit').click(function(e){
		if(!jQuery("#dlReportAnalysisForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();
		}
	});
// AAOL-4603: OA Delivery List Report - End //

//Start - Financial Report screens validation
	jQuery("#financialReportForm,#invoiceClearingForm").validate({
		rules: {
			startDate: {
				dateReportValidates: false
			},
			endDate: {
				dateReportValidates: true
			}
		},
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().next('div.registerError'));

		},
		onfocusout: false,
		focusCleanup: false
	});
//End - Financial Report screens validation

// Add New Account Form validation - Start //
	jQuery("#addAccountExistingForm").validate({
		rules: {

			accountName:{
				required:true
			},
			estAmount: {
				required: true
			},
			estimatedAmountPerYear: {
				digits: true
			},
			globalLocNo: {
				digits: true
			},
			shipCountry: {
				required: true
			},
			bussType: {
				required: true
			},
			billCountry1: {
				required: true
			}
		},
		errorPlacement: function(error, element) {

			if(element.attr("id") =="bussType"){
				error.appendTo( element.parent().parent().parent().find("div.registerError"));
			}else if(element.attr("id") =="shipCountry"){
				error.appendTo( element.parent().parent().parent().find("div.registerError"));
			}else if(element.attr("id") =="billCountry1"){
				error.appendTo( element.parent().parent().parent().find("div.registerError"));
			}
			else if(element.attr("id") =="estAmount"){
				error.appendTo(element.parent().parent().parent().find("div.registerError"));
			}
			else{
				error.appendTo( element.parent().parent().find("div.registerError"));
			}
		},
		onfocusout: false,
		focusCleanup: false
	});
// Add New Account Form validation - End //

	var convert_fn = function(date) {
		var year = date.getFullYear();

		var month = (1 + date.getMonth()).toString();
		month = month.length > 1 ? month : '0' + month;

		var day = date.getDate().toString();
		day = day.length > 1 ? day : '0' + day;

		return month + '/' + day + '/' + year;
	}

	var start = new Date(new Date().setFullYear(new Date().getFullYear() - 2)); // 2 years ago
	start.setHours(0,0,0,0);
	var today = new Date();

	var startDate = start.format(DATE_FORMAT.toLowerCase());
	var todayDate = today.format(DATE_FORMAT.toLowerCase());

	var startDateValue;
	var EndDatevalue;

function addDateValidators(){
	var startDate = start.format(DATE_FORMAT_TO_DISPLAY.toLowerCase());
	var todayDate = today.format(DATE_FORMAT_TO_DISPLAY.toLowerCase());

	jQuery.validator.addMethod("startdateValidation", function(value, element){
	    return validateDateInRange(value, element);
	}, messageFormat(DATE_FROM_INVALID_RANGE, [startDate, todayDate]));

	jQuery.validator.addMethod("enddateValidation", function(value, element){
        return validateDateInRange(value, element);
    }, messageFormat(DATE_TO_INVALID_RANGE, [startDate, todayDate]));
}
addDateValidators();

function validateDateInRange(value, element){
    if(ACC.formCheckFunc.isNullOrEmpty(value)){
        return true;
    }

    var datePickerValue = $(element).datepicker('getDate');
    return datePickerValue >= start && datePickerValue <= today;
}

function messageFormat(message, params){
    for (var i=0; i<params.length; i++) {
        message = message.replace("{" + i + "}", params[i]) // search for {0}, {1}, ... and replace by correct parameter
    }
    return message;
}

jQuery("#orderHistoryForm").validate({
	rules: {
			startDate: {
				dateValidates: true,
				startdateValidation: true
			},
			 endDate: {
				dateValidates: true,
				enddateValidation: true
			}
		},
		errorPlacement: function(error, element) {
			$('#orderHistoryDateError').html(error);
		},
		onfocusout: false,
		focusCleanup: false
	});

// Cut Report - Start //
	jQuery("#mddReturnsForm").validate({
		errorPlacement: function(error, element) {
			error.appendTo(element.parent().parent().parent().find('div.registerError'));
		},
		onfocusout: false,
		focusCleanup: false
	});
	jQuery('#mddReturnsSubmit').click(function(e){
		if(!jQuery("#mddReturnsForm").valid()){
			$('#myprofileedit .success').hide();
		}
		else {
			$('#myprofileedit .success').show();

		}
	});
// Cut Report - End //
// Add New Account Form validation - Start //
jQuery("#addAccountExistingChoiceForm").validate({

	rules: {
		nameOrNumberOfExistingAccount: {
			required: true,
		},
	},
		 errorPlacement: function(error, element) {
			error.appendTo( element.parent().parent().find("div.registerError"));
		},
		onfocusout: false,
		focusCleanup: false
});
// Add New Account Form validation - End //

/*---------Updated Privacy Policy-------------*/
jQuery("#updatePrivacyPolicyForm").validate({

	 errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().find(".registerError"));
	},
	onfocusout: false,
	focusCleanup: false
});
/*---------end Updated Privacy Policy-------------*/


/* validate contact us form start*/
jQuery(".contactUsForm").validate({

	errorPlacement: function(error, element) {
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false

});

jQuery(".contactUsFormSubmit").click(function(e){

	if(jQuery(".contactUsForm").valid()){
		$('.contactUsFormPage1').hide();
		$('.contactUsFormPage2').show();
	}
});

jQuery("#addAccountExistingSubmit").click(function(e){

	if(!jQuery("#addAccountExistingForm").valid()){
		$("#globalErrorAddAccount").show();
	}else{
		$("#globalErrorAddAccount").hide();
		$("#addAccountExistingForm").submit();
	}
});

jQuery(".backToContactUsForm").click(function(e){
	$('.contactUsFormPage1').show();
	$('.contactUsFormPage2').hide();
});

/* validate contact us form end*/

//serialization form
jQuery.validator.addMethod("isNullOrEmptyValidation", function(value, element) {

	if (value === null || value === "" || value === undefined || value === "undefined"  || value === "null") {
		return true;
	} else {
		return false;
	}

},'');

jQuery.validator.addMethod("isEmptyalphanumeric", function(value, element) {
	  return this.optional(element) || /^[a-z0-9 ]+$/i.test(value);
},INVALID_SERIAL_NUMBER);

//Changes for AAOL-6197
jQuery.validator.addMethod("isEmptyalphanumeric_", function(value, element) {
	  return this.optional(element) || /^[a-z0-9_ ]+$/i.test(value);
},INVALID_SERIAL_NUMBER);

jQuery.validator.addMethod("isEmptynumeric", function(value, element) {
	  return this.optional(element) || /^[0-9 ]+$/.test(value);
},INVALID_GTIN);
jQuery("#verifySerialForm").validate({

    rules: {
    		gtin: {
    			isEmptynumeric: true

			},
			serialNumber: {
				isEmptyalphanumeric: true

			},
			batchNumber: {
				isEmptyalphanumeric_: true

           }
    },
    errorPlacement: function(error, element) {
    	console.log(error)
    	error.appendTo(element.next('div.verifyDetailError'));
    },
    onfocusout: false,
    focusCleanup: false
});


//end serialization form

//contact form display field on selection
$(".contactUsSubject").on("click", showInputBox);

function showInputBox(e){
	e.preventDefault();
	var contactUsSubjectVal = $(".contactUsSubject option:selected").val();
	if(contactUsSubjectVal == "1"){ //order issue
		$("#contactUsOrderNumber").attr("disabled", false);
		$("#contactUsOrderNumber").closest("li").css("display","block");
	}else {
		$("#contactUsOrderNumber").attr("disabled", true);
		$("#contactUsOrderNumber").closest("li").css("display","none");
	}
}
// end contact form js
$(document).ready(function () {

	var currViewScreenName = $("#currViewScreenName").val();




	if($("#profilSaveupdate").length!=0){
		$("#profilePhoneNumber, #profileMobileNumber, #profileFaxNumber, #profileSupervisorPhone").trigger("blur");
	}
	if($('.correctAnswer').length>0){
		$('#profile-updated-panel').show();
	}



	/* Datepicker Validation 1.0.1 for jQuery UI Datepicker 1.8.6.
   Requires Jörn Zaefferer's Validation plugin (http://plugins.jquery.com/project/validate).
   Written by Keith Wood (kbwood{at}iinet.com.au).
   Dual licensed under the GPL (http://dev.jquery.com/browser/trunk/jquery/GPL-LICENSE.txt) and
   MIT (http://dev.jquery.com/browser/trunk/jquery/MIT-LICENSE.txt) licenses.
   Please attribute the author if you use it. */

(function($) { // Hide the namespace

/* Add validation methods if validation plugin available. */
/*if ($.fn.validate) {

	$.datepicker._selectDate2 = $.datepicker._selectDate;

	$.extend($.datepicker.regional[''], {
		validateDate: 'Please enter a valid date',
		validateDateMin: 'Please enter a date on or after {0}',
		validateDateMax: 'Please enter a date on or before {0}',
		validateDateMinMax: 'Please enter a date between {0} and {1}',
		validateDateCompare: 'Please enter a date {0} {1}',
		validateDateToday: 'today',
		validateDateOther: 'the other date',
		validateDateEQ: 'equal to',
		validateDateNE: 'not equal to',
		validateDateLT: 'before',
		validateDateGT: 'after',
		validateDateLE: 'not after',
		validateDateGE: 'not before'
	});

	$.extend($.datepicker._defaults, $.datepicker.regional['']);

	$.extend($.datepicker, {

		 Trigger a validation after updating the input field with the selected date.
		   @param  id       (string) the ID of the target field
		   @param  dateStr  (string) the chosen date
		_selectDate: function(id, dateStr) {
			this._selectDate2(id, dateStr);
			var input = $(id);
			var inst = this._getInst(input[0]);
			if (!inst.inline && $.fn.validate)
				input.parents('form').validate().element(input);
		},

		 Correct error placement for validation errors - after (before if R-T-L) any trigger.
		   @param  error    (jQuery) the error message
		   @param  element  (jQuery) the field in error
		errorPlacement: function(error, element) {
			var trigger = element.next('.' + $.datepicker._triggerClass);
			var before = false;
			if (trigger.length == 0) {
				trigger = element.prev('.' + $.datepicker._triggerClass);
				before = (trigger.length > 0);
			}
			error[before ? 'insertBefore' : 'insertAfter'](trigger.length > 0 ? trigger : element);
		},

		 Format a validation error message involving dates.
		   @param  message  (string) the error message
		   @param  params  (Date[]) the dates
		   @return  (string) the formatted message
		errorFormat: function(inst, message, params) {
			var format = $.datepicker._get(inst, 'dateFormat');
			$.each(params, function(i, v) {
				message = message.replace(new RegExp('\\{' + i + '\\}', 'g'),
					$.datepicker.formatDate(format, v) || 'nothing');
			});
			return message;
		}
	});

	var lastElement = null;

	 Validate date field.
	$.validator.addMethod('dpDate', function(value, element, params) {
			lastElement = element;
			var inst = $.datepicker._getInst(element);
			var dateFormat = $.datepicker._get(inst, 'dateFormat');
			try {
				var date = $.datepicker.parseDate(dateFormat, value, $.datepicker._getFormatConfig(inst));
				var minDate = $.datepicker._determineDate(inst, $.datepicker._get(inst, 'minDate'), null);
				var maxDate = $.datepicker._determineDate(inst, $.datepicker._get(inst, 'maxDate'), null);
				var beforeShowDay = $.datepicker._get(inst, 'beforeShowDay');
				return this.optional(element) || !date ||
					((!minDate || date >= minDate) && (!maxDate || date <= maxDate) &&
					(!beforeShowDay || beforeShowDay.apply(element, [date])[0]));
			}
			catch (e) {
				return false;
			}
		}, function(params) {
			var inst = $.datepicker._getInst(lastElement);
			var minDate = $.datepicker._determineDate(inst, $.datepicker._get(inst, 'minDate'), null);
			var maxDate = $.datepicker._determineDate(inst, $.datepicker._get(inst, 'maxDate'), null);
			var messages = $.datepicker._defaults;
			return (minDate && maxDate ?
				$.datepicker.errorFormat(inst, messages.validateDateMinMax, [minDate, maxDate]) :
				(minDate ? $.datepicker.errorFormat(inst, messages.validateDateMin, [minDate]) :
				(maxDate ? $.datepicker.errorFormat(inst, messages.validateDateMax, [maxDate]) :
				messages.validateDate)));
		});

	 And allow as a class rule.
	$.validator.addClassRules('dpDate', {dpDate: true});

	var comparisons = {equal: 'eq', same: 'eq', notEqual: 'ne', notSame: 'ne',
		lessThan: 'lt', before: 'lt', greaterThan: 'gt', after: 'gt',
		notLessThan: 'ge', notBefore: 'ge', notGreaterThan: 'le', notAfter: 'le'};

	 Cross-validate date fields.
	   params should be an array with [0] comparison type eq/ne/lt/gt/le/ge or synonyms,
	   [1] 'today' or date string or Date or other field selector/element/jQuery OR
	   an object with one attribute with name eq/ne/lt/gt/le/ge or synonyms
	   and value 'today' or date string or Date or other field selector/element/jQuery OR
	   a string with eq/ne/lt/gt/le/ge or synonyms followed by 'today' or date string or jQuery selector
	$.validator.addMethod('dpCompareDate', function(value, element, params) {
			if (this.optional(element)) {
				return true;
			}
			params = normaliseParams(params);
			var thisDate = $(element).datepicker('getDate');
			var thatDate = extractOtherDate(element, params[1]);
			if (!thisDate || !thatDate) {
				return true;
			}
			lastElement = element;
			var result = true;
			switch (comparisons[params[0]] || params[0]) {
				case 'eq': result = (thisDate.getTime() == thatDate.getTime()); break;
				case 'ne': result = (thisDate.getTime() != thatDate.getTime()); break;
				case 'lt': result = (thisDate.getTime() < thatDate.getTime()); break;
				case 'gt': result = (thisDate.getTime() > thatDate.getTime()); break;
				case 'le': result = (thisDate.getTime() <= thatDate.getTime()); break;
				case 'ge': result = (thisDate.getTime() >= thatDate.getTime()); break;
				default:   result = true;
			}
			return result;
		},
		function(params) {
			var inst = $.datepicker._getInst(lastElement);
			var messages = $.datepicker._defaults;
			params = normaliseParams(params);
			var thatDate = extractOtherDate(lastElement, params[1], true);
			thatDate = (params[1] == 'today' ? messages.validateDateToday : (thatDate ?
				$.datepicker.formatDate($.datepicker._get(inst, 'dateFormat'), thatDate,
				$.datepicker._getFormatConfig(inst)) : messages.validateDateOther));
			return messages.validateDateCompare.replace(/\{0\}/,
				messages['validateDate' + (comparisons[params[0]] || params[0]).toUpperCase()]).
				replace(/\{1\}/, thatDate);
		});

	 Normalise the comparison parameters to an array.
	   @param  params  (array or object or string) the original parameters
	   @return  (array) the normalised parameters
	function normaliseParams(params) {
		if (typeof params == 'string') {
			params = params.split(' ');
		}
		else if (!$.isArray(params)) {
			var opts = [];
			for (var name in params) {
				opts[0] = name;
				opts[1] = params[name];
			}
			params = opts;
		}
		return params;
	}

	 Determine the comparison date.
	   @param  element  (element) the current datepicker element
	   @param  source   (string or Date or jQuery or element) the source of the other date
	   @param  noOther  (boolean) true to not get the date from another field
	   @return  (Date) the date for comparison
	function extractOtherDate(element, source, noOther) {
		if (source.constructor == Date) {
			return source;
		}
		var inst = $.datepicker._getInst(element);
		var thatDate = null;
		try {
			if (typeof source == 'string' && source != 'today') {
				thatDate = $.datepicker.parseDate($.datepicker._get(inst, 'dateFormat'),
					source, $.datepicker._getFormatConfig(inst));
			}
		}
		catch (e) {
			// Ignore
		}
		thatDate = (thatDate ? thatDate : (source == 'today' ? new Date() :
			(noOther ? null : $(source).datepicker('getDate'))));
		if (thatDate) {
			thatDate.setHours(0, 0, 0, 0);
		}
		return thatDate;
	}
}*/

})(jQuery);

ACC.formCheckFunc.bindAll();


});