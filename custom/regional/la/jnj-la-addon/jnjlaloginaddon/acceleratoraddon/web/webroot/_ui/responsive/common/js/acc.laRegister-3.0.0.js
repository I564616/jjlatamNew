$('.toggle-link').click(function() {
   let idClicked = this.id;
   let idNumber = idClicked.slice(idClicked.length - 1);
   $('#accordioncollapse' + idNumber).hasClass('panel-collapsed') ? $('#collapse' + idNumber).slideDown().addClass('in') : $('#collapse' + idNumber).slideUp().removeClass('in');
   $('#signuppage .profile-accordian-body').each(function() {
      (this.id.slice(this.id.length - 1) !== idNumber) && $('#' + this.id).slideUp().removeClass('in');
   });
});


$(".liveChatpopup_hn").click(function() {
    openLauncher();
});

$(document).on("change", "#product", function() {
    let selectedText = $(this).find("option:selected").text();
    if(selectedText === "MEDTECH") {

        $("[data-id='hcp']").empty();
        $("#hcp").empty();
       
        $("[data-id='hcp']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">
ETHICON</div></div> </div>`);

      $("#hcp").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
      
   
    }
    if(selectedText === "CONSIGNACIONES") {
        $("[data-id='hcp']").empty();
       $("#hcp").empty();
        $("[data-id='hcp']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">CONSIGNACIONES</div></div> </div>`);
$("#hcp").append(`<option text="CONSIGNACIONES" value="CONSIGNACIONES">CONSIGNACIONES</option>`);


    }
});

$(document).on("change", "#productphrmdd", function() {
    let selectedText = $(this).find("option:selected").text();
    if(selectedText === "MEDTECH") {

        $("[data-id='hcpphrmdd']").empty();
        $("#hcpphrmdd").empty();
       
        $("[data-id='hcpphrmdd']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">
ETHICON</div></div> </div>`);

      $("#hcpphrmdd").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
      
   
    }
	if(selectedText === "INNOVATIVE MEDICINE") {
        $("[data-id='hcpphrmdd']").empty();
       $("#hcpphrmdd").empty();
        $("[data-id='hcpphrmdd']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">INNOVATIVE MEDICINE</div></div> </div>`);
      $("#hcpphrmdd").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);


    }
	
    if(selectedText === "CONSIGNACIONES") {
        $("[data-id='hcpphrmdd']").empty();
       $("#hcpphrmdd").empty();
        $("[data-id='hcpphrmdd']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">CONSIGNACIONES</div></div> </div>`);
      $("#hcpphrmdd").append(`<option text="CONSIGNACIONES" value="CONSIGNACIONES">CONSIGNACIONES</option>`);

    
    }
});

$(document).on("change", "#productbrphrmdd", function() {
    let selectedText = $(this).find("option:selected").text();
    if(selectedText === "MEDTECH") {

        $("[data-id='hcpbrphrmdd']").empty();
        $("#hcpbrphrmdd").empty();
       
        $("[data-id='hcpbrphrmdd']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">
ETHICON</div></div> </div>`);

      $("#hcpbrphrmdd").append(`<option text="ETHICON" value="ETHICON">ETHICON</option> <option text="DEPUY" value="DEPUY">DEPUY</option>`); 
	  $("#hcpbrphrmdd").selectpicker('refresh');
	  enableDropdown('franchise-dropdown-br-mdd-phr')
    }
	if(selectedText === "INNOVATIVE MEDICINE") {
        $("[data-id='hcpbrphrmdd']").empty();
       $("#hcpbrphrmdd").empty();
        $("[data-id='hcpbrphrmdd']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">INNOVATIVE MEDICINE</div></div> </div>`);
      $("#hcpbrphrmdd").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);
	  disableDropdown('franchise-dropdown-br-mdd-phr');
	  
    }
	
});

$(document).on("change", "#productphrmddothers", function() {
let selectedText = $(this).find("option:selected").text();
    if(selectedText === "MEDTECH") {

        $("[data-id='hcpphrmddothers']").empty();
        $("#hcpphrmddothers").empty();
       
        $("[data-id='hcpphrmddothers']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">
ETHICON</div></div> </div>`);

      $("#hcpphrmddothers").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
	     
    }
	if(selectedText === "INNOVATIVE MEDICINE") {
        $("[data-id='hcpphrmddothers']").empty();
       $("#hcpphrmddothers").empty();
        $("[data-id='hcpphrmddothers']").append(`<div class="filter-option"><div class="filter-option-inner"><div class="filter-option-inner-inner">INNOVATIVE MEDICINE</div></div> </div>`);
      $("#hcpphrmddothers").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);
    }
	disableDropdown('franchise-dropdown-mdd-phr-others');
   
});

const disableDropdown = (className) => {
  const style = document.createElement('style');
  style.innerHTML = `
    .${className} {
      pointer-events: none !important;
    }
  `;
  document.head.appendChild(style);
};

const enableDropdown = (className) => {
	const style = document.createElement('style');
	style.innerHTML = `
	  .${className} {
		pointer-events: auto !important;
	  }
	`;
	document.head.appendChild(style);
  };
  
disableDropdown('country-dropdown');
disableDropdown('franchise-dropdown-co-mdd');
disableDropdown('franchise-dropdown-co-mdd-phr');




$(document).on("change", "#cx_webchat_form_subject_others", function() {
    let selectedVal = $(this).val();
    if(selectedVal === "MEDICAL") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
    }
    if(selectedVal === "PHARMA") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);
    }
    FreezeSingleOption('Franchise');
});
$(document).on("change", "#cx_webchat_form_br_mx_pe", function() {
    let selectedVal = $(this).val();
    if(selectedVal === "MEDICAL") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>
                                                    <option text="DEPUY" value="DEPUY">DEPUY</option>`);
    }
    if(selectedVal === "PHARMA") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);
    }
    FreezeSingleOption('Franchise');
});

$(document).on("change", "#cx_webchat_form_subject_co", function() {
    let selectedVal = $(this).val();
    if(selectedVal === "MEDICAL") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
    }
    if(selectedVal === "PHARMA") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="PHARMA" value="PHARMA">PHARMA</option>`);
    }
	 if(selectedVal === "CONSIGNACIONES") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="CONSIGNACIONES" value="CONSIGNACIONES">CONSIGNACIONES</option>`);
    }
    FreezeSingleOption('Franchise');
});

$(document).on("change", "#cx_webchat_form_subject_mdd_co", function() {
    let selectedVal = $(this).val();
    if(selectedVal === "MEDICAL") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="ETHICON" value="ETHICON">ETHICON</option>`);
    }
    if(selectedVal === "CONSIGNACIONES") {
        $("#cx_webchat_form_customselect").empty();
        $("#cx_webchat_form_customselect").append(`<option text="CONSIGNACIONES" value="CONSIGNACIONES">CONSIGNACIONES</option>`);
    }
    FreezeSingleOption('Franchise');
});

function FreezeSingleOption(selectName) {
    var selectElement = $(".cx-form-inputs select[name='" + selectName + "']");
    if(selectElement.children('option').length === 1) {
        selectElement.css('pointer-events', 'none');
    } else {
        selectElement.css('pointer-events', 'auto');
    }
}

function companyInformationValidate(){
	 
	 var registrationError = "<div >";
	 registrationError +="<ul>";
	 
	 var Status=true;
	 
	
	 if($.trim($("#customerCode").val())===""){
		 $("#customerCodeError").css("display","block");
		 Status=false;
	 }
	 else{
		 $("#customerCodeError").css("display","none");
	 }
	
	if($.trim($("#soldTo").val())===""){
		$("#soldToError").css("display","block");
		Status=false;
	 }
	else{
		$("#soldToError").css("display","none");
	}
	
	if($.trim($("#jnjAccountManager").val())===""){
		$("#jnjAccountManagerError").css("display","block");
		Status=false;
	 }
	else{
		$("#jnjAccountManagerError").css("display","none");
	}
	
	if($.trim($("#shipToCountry").val())===""){
		$("#shipToCountryError").css("display","block");
		Status=false;
	 }
	else{
		$("#shipToCountryError").css("display","none");
	}
		
	
	if($.trim($("#shipAddress1").val())===""){
		$("#shipAddress1Error").css("display","block");
		Status=false;
	 }
	else{
		$("#shipAddress1Error").css("display","none");
	}
		
	if($.trim($("#shipState").val())==="" && !$("#shipState").prop("disabled")){	
		 $("#shipStateError").css("display","block");
		 Status=false;
	 }
	else{
		 $("#shipStateError").css("display","none");	
	}
	
	if($.trim($("#shipCity").val())===""){
		$("#shipCityError").css("display","block");
		Status=false;
	 }
	else{
		$("#shipCityError").css("display","none");
	}
	
	if($.trim($("#shipToZipCode").val())===""){
		$("#shipToZipCodeError").css("display","block");
	    Status=false;
	 }
	else{
		$("#shipToZipCodeError").css("display","none");
	}	
	
	
	if(!Status){		
		$('#companyInformationError').show();
		registrationError += "</ul></div>";		
		$("#companyInformationError").html(registrationError);		
		return false;
	}else{
		companyInfo=true;
		$('.errorProfileInfo').html("");	
		$('#companyInformationError').hide();
	    $('.errorProfileInfo').hide();
	    nextBtnClick(2);
		$('#accordioncollapse3').click();
	}
	
}

function yourInformationValidate(){
	
	var registrationError = "<div >";
		 registrationError +="<ul>";
		 
		 var Status=true;
	 if($.trim($("#firstName").val())===""){
		 $("#firstNameError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#firstNameError").css("display","none");
	 }
	 
	 if($.trim($("#signup-lname").val())===""){
		 $("#signup-lnameError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#signup-lnameError").css("display","none");
	 }
	 
	 if($.trim($("#emailAddress").val())===""){
		 $("#emailAddressError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#emailAddressError").css("display","none");
	 }
	 

	 if(($("#radioCTY").is(":checked")===true) && ($.trim($("#emailAddress").val())!=="")) {
	        if ($("#emailAddress").val().toLowerCase().includes("@its.jnj.com")!==true){
	           $("#sectemailAddressError").css("display","block");
	            Status=false
	        }else {
	         $("#sectemailAddressError").css("display","none");
	        }

	    }

	     if($("#radioCTN").is(":checked")===true)  {
	          $("#sectemailAddressError").css("display","none");
			 $("#radioSmdd, #radioSphr").prop("checked", false);
	    }
	     if (($("#radioSmdd").is(":checked")!==true) &&  ($("#radioSphr").is(":checked")!==true) && ($("#radioCTN").is(":checked")!==true)) {
	         $("#commuser").css("display","block");
	         Status=false;
	      }
	      else  {
	        $("#commuser").css("display","none");

	      }
	 
	 if($.trim($("#reenterEmailAddress").val())===""){
		 $("#reenterEmailAddressError").css("display","block");
		 Status=false; 
	 }else if($.trim($("#reenterEmailAddress").val()) != $.trim($("#emailAddress").val())){
		 $("#emailAddressValidateError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#reenterEmailAddressError").css("display","none");
		 $("#emailAddressValidateError").css("display","none");
	 }
	 if($.trim($("#regPhoneNumberPrefix").val())==="" && $.trim($("#regPhoneNumber").val()) != ""){
		 $("#regCountryCodeError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#regCountryCodeError").css("display","none");
	 } 
	 
	 if($.trim($("#regPhoneNumber").val())===""){
		 $("#regPhoneNumberError").css("display","block");
		 Status=false; 
	 }
	 else{
		 $("#regPhoneNumberError").css("display","none");
	 }
	 
	 var emailAddress = $("#emailAddress").val();
		var dataObj = new Object();
		dataObj.emailAddress = emailAddress;
			jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath +'/register/validateUser',
				async: false,
				data: dataObj,
				success: function (data) {
					if(data.isUserValid=="false"){
						registrationError += "<li>"+data.errorMsg+"</li>"; 
						 Status=false;
					}
				},
				error: function (e) {
					
				}
			});
			
			
			function complexityCheck() {
			 	var strength = 0;
			 	
				if ($.trim($("#password").val()).match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
					strength++;
				}
				if ($.trim($("#password").val()).match(/([a-z])/)) {
					strength++;
				}
				if ($.trim($("#password").val()).match(/([A-Z])/)) {
					strength++;
				}
				if ($.trim($("#password").val()).match(/([0-9])/)) {
					strength++;
				}

				if (strength >= 2 && $.trim($("#password").val()).length >=8) {

					return true;
				} else {
					return false;
				}
			}
	
		var password = $.trim($("#password").val());
		var reenterPwd = $.trim($("#reenter-password").val());
		
		
		
			var complexityCheckstatus = complexityCheck();
			 if(password===""){
				 $("#reenter-passwordError").css("display","none");
				 $("#createpasswordError1").css("display","none");
				 $("#createpasswordError2").css("display","none");
				 $("#createpasswordError").css("display","block");
				 Status=false; 
			 }
			 else if(reenterPwd ===""){
				 $("#createpasswordError").css("display","none");
				 $("#createpasswordError1").css("display","none");
				 $("#createpasswordError2").css("display","none");
				 $("#reenter-passwordError").css("display","block");
				 Status=false; 
			 }
			 else if(reenterPwd != password){
				 $("#reenter-passwordError").css("display","none");
				 $("#createpasswordError").css("display","none");
				 $("#createpasswordError1").css("display","none");
				 $("#createpasswordError2").css("display","block");
				 Status=false;
				}				
			 else if (!complexityCheckstatus){
				 $("#reenter-passwordError").css("display","none");
				 $("#createpasswordError").css("display","none");
				 $("#createpasswordError2").css("display","none");
				 $("#createpasswordError1").css("display","block");
				 Status=false; 
			 }
			 else{
				 $("#reenter-passwordError").css("display","none");
				 $("#createpasswordError").css("display","none");
				 $("#createpasswordError1").css("display","none");
				 $("#createpasswordError2").css("display","none");
			 }
			 
			 if(!Status){
					$('#yourInformationError').show();
					registrationError += "</ul></div>";
					$("#yourInformationError").html(registrationError);
					return false;
				}else{
					 yourInfo=true;
					 $('.errorYourInfo').html("");
					 $('#yourInformationError').hide();
					 $('.errorYourInfo').hide();
					 createpassword=true;
					 $("#createpasswordError1").css("display","none");
					 $("#createpasswordError2").css("display","none");
					 $("#reenter-passwordError").css("display","none");
					 $("#createpasswordError").css("display","none");
					 nextBtnClick(3);
                     $('#accordioncollapse5').click();
                }
            }
function emailPreferencesValidate(){
	
	var registrationError = "<div >";
		 registrationError +="<ul>";
		 
		 var Status=true;
		 
		 if(!Status){
				$('#emailPreferencesError').show();	 
				registrationError += "</ul></div>";
				$("#emailPreferencesError").html(registrationError);
				return false;
			}else{
				emailPreferences=true;
			 $('#emailPreferencesError').hide();
			
			 nextBtnClick(5);
			 $('#accordioncollapse7').click();
			}
}

$('.select-security-ques').change(function(){
	  var selectedQuest=$(this).val();
	  var selectedArr=[];
	  var arrIndex=0;
	  
	  $('.select-security-ques').each(function(){
	   $('.select-security-ques option').prop('disabled',false);
	   selectedArr[arrIndex]=$(this).val();	   
	   arrIndex=arrIndex+1;
	  });	 
	
	  $('.select-security-ques').each(function(){	
		   for(var i=0;i<selectedArr.length;i++){
			  if(selectedArr[i] == $(this).val()){
				  $(this).find('option[value="'+selectedArr[i]+'"]').addClass('selectedVal');
				  $(this).find('option[value="'+selectedArr[i]+'"]').prop('disabled',false);
			  }	
			  else{
				  $(this).find('option[value="'+selectedArr[i]+'"]').removeClass('selectedVal');
			  }
		   }  
		}); 
	   $('.select-security-ques').each(function(){	
		   for(var i=0;i<selectedArr.length;i++){
			   if(!$(this).find('option[value="'+selectedArr[i]+'"]').hasClass('selectedVal')){
				   $(this).find('option[value="'+selectedArr[i]+'"]').prop('disabled',true);
			   }
			  
		   }  
		}); 
	   
	 

	  $('.select-security-ques').selectpicker('refresh');
});

$(document).on('click', '.noTextInput', function(){
    toggleFinishRegistrationElements();
});

function toggleFinishRegistrationElements(){
    $("#signupsecretquestError").toggle(!hasSecretAnswers());
    $("#finalsubmissionError").toggle(!hasMainInformation());
    $("#finishButtonText").toggle(hasMainInformation() && hasSecretAnswers() && $("#terms-conditions").prop('checked') && $("#privacy-policy").prop('checked'));
}

// set default value for some globals
var companyInfo = false;
var yourInfo = false;
var emailPreferences = false;
var createpassword = false;
function hasMainInformation(){
    return companyInfo && yourInfo && emailPreferences && createpassword
}

function hasSecretAnswers(){
    var answers = 0;
    // get all elements with id starting with profile.secret.answer
    $("[id^=profile\\.secret\\.answer]").each(function(){
        if($.trim($(this).val()) != ""){
            answers++
        }
    });
    return answers >= 3;
}

var captchaLoaded = false;
function secretQuestionValidate(){
    toggleFinishRegistrationElements();
    if (hasSecretAnswers() && hasMainInformation()) {
		$('#accordioncollapse8').click();
		$('#accordioncollapse3').click();
		$('#accordioncollapse5').click();
		$('#accordioncollapse7').click();
		$("#accordioncollapse9").click();
        if (!captchaLoaded){
            nextBtnClick(7);
            $.getScript('https://www.google.com/recaptcha/api.js', function(){
                captchaLoaded = true;
                Recaptcha.create("6Le8aucSAAAAAHg-8At0to9flqyrhG2_ctqLbdAT", "recaptcha_content", { theme : 'clean'});
            });
        }
    }
}
$("#radioCTN").change(function() {

	   if($(this).is(":checked")) {

	        $("#sectordiv").hide();

	   }

	});

	 

	$("#radioCTY").change(function() {

	   if($(this).is(":checked")) {

	        $("#sectordiv").show();

	   }

	});
	
    $(".usefullinks_hn").click(function() {
		window.open("https://www.jnj.com/",'_blank').focus();	
	});
	//File Upload Malware Scan function
	async function uploadAndScanFile(file) {
		let cloudmersiveApiKey = $("#cloudmersiveAPIKey").val();
		let cloudmersiveAPIURL = $("#cloudmersiveAPIURL").val();
		const apiKey = cloudmersiveApiKey;
		const formData = new FormData();
		formData.append('inputFile', file);
	
		try {
			// Upload the file to Cloudmersive for scanning
			const response = await fetch(cloudmersiveAPIURL, {
				method: 'POST',
				headers: {
					'Apikey': apiKey
				},
				body: formData
			});
			const result = await response.json();
			const finalResult = result.CleanResult;
	
			return finalResult;
		} catch (error) {
			console.log('Error', error);
			// Handle the error appropriately
		}
	}

$("#shipToCountry").bind('change', function() {
	var countryElement = this;
	if(null != countryElement && '' != countryElement && null!= countryElement.value){
		
		var country = countryElement.value;		 
		var dataObj = new Object();
	dataObj.country = country; 
	jQuery.ajax({
		type: 'POST',
		url: ACC.config.contextPath +'/register/getStates',
		data: dataObj,
		success: function (data) {
			var options="<option value=''>"+$("#shipState").data("none-selected-text")+"</option>";
			for(var i=0;i<data.length;i++){
				options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
				
			}
						
			$("#shipState").attr("disabled",false);
			$("#shipState").children().remove();
			$("#shipState").append(options);
			$("#shipState").selectpicker("refresh");
		},
		error: function (e) {
		}
	});}
	
if(null != countryElement && '' != countryElement && null!= countryElement.value){
		
		var country = countryElement.value;		 
		var dataObj = new Object();
	dataObj.country = country; 
	jQuery.ajax({
		type: 'POST',
		url: ACC.config.contextPath +'/register/getCodes',
		data: dataObj,
		success: function (data) {
			
			var phoneOption="<option value=''>"+$("#regPhoneNumberPrefix").data("none-selected-text")+"</option>";

			for(var i=0;i<data.length;i++){
				phoneOption=phoneOption+ "<option value="+data[i]+">"+data[i]+"</option>";				
			}
			
			$("#regPhoneNumberPrefix").attr("disabled",false);
			$("#regPhoneNumberPrefix").children().remove();
			$("#regPhoneNumberPrefix").append(phoneOption);
			$("#regPhoneNumberPrefix").selectpicker("refresh");
			
			$("#regMobileNumberPrefix").attr("disabled",false);
			$("#regMobileNumberPrefix").children().remove();
			$("#regMobileNumberPrefix").append(phoneOption);
			$("#regMobileNumberPrefix").selectpicker("refresh");
		},
		error: function (e) {
		}
	});}

});