var consolidatedEmail = ".ConsolidatedEmail";
var consolidatedRadio = ".ConsolidatedRadio";
var frequencyWeekly = "#frequencyWeekly";
var weeklyClassHeight = ".weeklyClassHeight";
var frequencyMonthly = "#frequencyMonthly";
var displayCalender= ".displayCalender";
var weeklyDaysSection = ".weeklyDaysSection";
var daysLiSpan = ".days li span";

function validateSaveButton(checkBox, saveId){
	var checked = $('#'+checkBox.id).is(":checked");
	if(checked){
		$('#'+saveId).prop('disabled',false);
	}else{
		$('#'+saveId).prop('disabled',true);
	}
}

$("#changePasswordSubmit").click(function(){
	$("#success1").hide();
	$("#success2").hide();
	$("#error1").hide();
	$("#error2").hide();
	$("#newPassword-error").hide();
	$("#checkNewPassword-error").hide();
	$("#laProfileChangePassword").submit();
});

$("#profilSaveupdate,#emailPreferencesPageSubmit,#securityQuestionSubmit").click(function(){
    dataCollectOnSubmitEvent();
	$("#success1").hide();
	$("#success2").hide();
	$("#error1").hide();
	$("#error2").hide();
	$("#newPassword-error").hide();
	$("#checkNewPassword-error").hide();
    sessionStorage.setItem("registrationComplete", "true");

});

$(document).ready(function() {
clickHandlerConsolidateEmail();
clearingWeekDayMonthDate();
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

    var requiredFieldMessage = $("errorMessageRequiredField").val();
	$('[id^="questionList"]').attr("data-msg-required", requiredFieldMessage)
	$('[id^="profile.secret.answer"]').attr("data-msg-required", requiredFieldMessage)
	$("#profilePhoneNumber").attr("data-msg-required", requiredFieldMessage);

	if($('.lapwdwidgetdiv').length>0){
		if($('#thepwddiv').length>0){
			var profilePWdStrngth = new LatamPasswordWidget('thepwddiv','newPassword');
			profilePWdStrngth.MakePWDWidget();
		}
	}


	jQuery.extend(jQuery.validator.messages, {
	    required: $("#registerRequiredField").val(),
	});


	$("#myprofilecheck").validate({
		rules: {
			phone: {
                required: true,
                maxlength: 15,
                normalizer: function(value) {
                    var convertPh = value.split('-').join('');
                    convertPh = convertPh.toString();
                    return convertPh;
                    }
            },
			mobile: {
				required: true,
				maxlength: 15,
				normalizer: function(value) {

					var convertmb = value.split('-').join('');
					convertmb = convertmb.toString();

					return convertmb;
                }
            }
        }
	});


    $(".numbersOnlyInput").on("keypress", function(event){
        return validateNumbers(event);
    });


	$(".accp-PhoneNumber").on('blur',function(){
		if($(this).val() != ""){
	   		var phoneVal = $(this).val().split("-").join("");
			phoneVal = phoneVal.match(new RegExp(".{1,4}$|.{1,3}", "g")).join("-");
			$(this).val(phoneVal);
		}
	});

    validateRegistration();
    
    
    console.log("my profile js file");
    $( "#profilepage #accordion .profile-accordian-header a" ).on("click",function() {
    	if($(this).attr("aria-expanded")==="true"){
        	$(this).children(".bi")
        	.removeClass("bi bi-plus")
        	.addClass("bi bi-dash")
        }
    	else if($(this).attr("aria-expanded")==="false"){
    		$(this).children(".bi")
        	.removeClass("bi bi-dash")
        	.addClass("bi bi-plus")
    	}
    	});
    
    
    
});

function validateRegistration() {
    var registrationFlag = isRegistrationComplete();
    if (registrationFlag == "true"){
        $("#registrationCompleteLabel").hide();
    } else if (registrationFlag != "notLogged" && registrationFlag != "resetPassword") {
        if ($("#isProfilePage").val()=="true"){
            $("#securityQuestionsAnchor").click();
        }
    } else if ($("#isProfilePage").val()=="true"){
        $("#securityQuestionsAnchor").click();
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

function validateNumbers(event){
     return !(event.which != 8 && event.which != 0 &&
                    (event.which < 48 || event.which > 57) && event.which != 46);
}

function removeNonNumbers(componentId){
    var value = $(componentId).val();
    value = value.replace(/[^\d]/g, '');
    $(componentId).val(value);
}

function isRegistrationComplete(){
    if(sessionStorage.getItem("registrationComplete")){
    	return sessionStorage.getItem("registrationComplete");
    }

    var registrationComplete = "notLogged";
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: ACC.config.contextPath + '/p/la/isRegistrationComplete',
        success: function (data) {
            registrationComplete = data;
            sessionStorage.setItem("registrationComplete", registrationComplete);
        }
    })
    return registrationComplete;
}

function getDateMask(){
    var countryCode = $("#countryCode").val();
    if (countryCode == 'pt' || countryCode == 'es') {
        return 'dd/mm/yyyy'
    }
    return 'mm/dd/yyyy'
}

function addFormat(params){
    params.format = getDateMask();
    return params;
}

function clickHandlerConsolidateEmail(){
    $(consolidatedRadio).prop('checked') ? $(consolidatedEmail).show() : $(consolidatedEmail).hide();

    $("#emailPreferencesForm input").click(function() {
        $(consolidatedRadio).prop("checked") ? $(consolidatedEmail).show() : $(consolidatedEmail).hide();
    });
    $(".frequencyClick").click(function() {
        $(frequencyWeekly).prop("checked") ? $(weeklyClassHeight).show() : $(weeklyClassHeight).hide();
        $(frequencyMonthly).prop("checked") ? $(displayCalender).show() : $(displayCalender).hide();
    });
    $(displayCalender+" .days li").click(function() {
        $(this).children('span').hasClass("active") ? $(this).children('span').removeClass("active") : $(this).children('span').addClass("active");
    });
}

function clearingWeekDayMonthDate(){
    $(weeklyDaysSection+" input").click(function(){
        $('.weekErrorMsg').hide();
        $(daysLiSpan).removeClass('active');
    });
    $(daysLiSpan).click(function(){
        $(weeklyDaysSection+" input").each(function () { $(this).prop('checked', false); });
        $('.calendarErrorMsg').hide();
    });
}

function dataCollectOnSubmitEvent() {
    if($("#frequencyDaily").prop('checked')){
       $(daysLiSpan).removeClass('active');
       $(weeklyDaysSection+" input").each(function () { $(this).prop('checked', false); });
    }
    var selectedOrdertypeList = $('.OrderTypeClass option:selected').toArray().map(item => item.value);
    var selectedDates = $(".days li").find(".active").toArray().map(item => item.innerText);
    $(frequencyMonthly).prop('checked') ? $("#CalenderDate").val(selectedDates) : $("#CalenderDate").val('notSelected');
    !$(frequencyWeekly).prop('checked') ? $('.noSelect').prop('name', 'weekDay') : $('.noSelect').prop('name', '');
    $("#orderList").val(selectedOrdertypeList);
}

function validateEmailPrefrenceForm(){
    if($(frequencyWeekly).prop('checked') && !$('.weekDay').is(':checked')){
        $('.weekErrorMsg').show();
        return false;
    }
    else if( $(frequencyMonthly).prop('checked') && !$(daysLiSpan).hasClass('active')){
        $('.calendarErrorMsg').show();
        return false;
    }
    else{
    return true;
    }
}

 $( document ).ready(function() {
  bodyheighOnAccordianClick();
  $(frequencyWeekly).prop("checked") ? $(weeklyClassHeight).show() : $(weeklyClassHeight).hide();
  $(frequencyMonthly).prop("checked") ? $(displayCalender).show() : $(displayCalender).hide();
  var monthDays = $("#varMonthdays").attr("data-monthDay");
  var orderTypeSaveValue = $("#varOrderTypes").attr("data-ordertypedefault");
  var weekDay = $("#varWeekDay").attr("data-weekDay");
     weekDaySequence();
     weekDefaultVals(weekDay);
     if (monthDays || orderTypeSaveValue) {
           monthDefaultVals();
          }
     orderLastValueValidation();
     $(document).on('change', '.OrderTypeClass select', function() {
         orderLastValueValidation();
     });

});

function weekDefaultVals(weekDay){
$(weeklyDaysSection+" label").each(function(){
  var currentDay = $(this).attr("for").toUpperCase();
  currentDay === weekDay ? $(this).next().next().find("input").prop("checked", true):'';

  });
}

function monthDefaultVals(){

    $(displayCalender+" .calender ul li span")
    .each(eachFuncMonth);

}
 function eachFuncMonth(){
	 var monthsDays = $("#varMonthdays").attr("data-monthDay");
     var monthDaysArr= monthsDays.slice(1, -1).split(", ");
     var arrayLength = monthDaysArr.length;
		for (var i = 0; i < arrayLength; i++) {
		  var currentValue = monthDaysArr[i].toString();
				 let currentCalDay = $(this).text();
				 currentCalDay === currentValue ? $(this).addClass("active"):'';
		}
  }

function orderLastValueValidation(){

$(".OrderTypeClass .selected").length===0 ? $('.OrderTypeClass .dropdown-menu.inner li[data-original-index="7"]').find(".text").trigger("click"):"";

}

function weekDaySequence(){
$(weeklyDaysSection+" #MONDAY").css( {"order": "0"});
$(weeklyDaysSection+" #TUESDAY").css( {"order": "0"});
$(weeklyDaysSection+" #WEDNESDAY").css( {"order": "1"});
$(weeklyDaysSection+" #THURSDAY").css( {"order": "3"});
$(weeklyDaysSection+" #FRIDAY").css( {"order": "4"});
$(weeklyDaysSection+" #SATURDAY").css( {"order": "5"});
$(weeklyDaysSection+" #SUNDAY").css( {"order": "6"});

$(weeklyDaysSection+" .text-center").css( {"width": "71px"});

}

function bodyheighOnAccordianClick(){
    $("#profilepage .profile-accordian-header a").click(function(){
        $("#jnj-body-content").height($("#accordion").height() + 500);
    });
}

$("#registerForm .profile-accordian-header a" ).on("click",function() {
    setTimeout(function(){
        changeAccordianIcon();
    },100);
});

function changeAccordianIcon(){
    $("#registerForm .profile-accordian-header a").each(function(){
        if($(this).hasClass("panel-collapsed")){
            $(this).children("i").addClass("bi bi-plus").removeClass("bi bi-dash");
        }else{
            $(this).children("i").addClass("bi bi-dash").removeClass("bi bi-plus");
        }
    })
}
