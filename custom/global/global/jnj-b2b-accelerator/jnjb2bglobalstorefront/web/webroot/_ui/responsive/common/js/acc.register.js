$('.jjEmployeeYes').hide();
ACC.registration = {
		bindAll: function ()
		{
			
			
			
			/*$(document).on('focusout','#pref-mobi-holder',function(){
				
				$('.prefer-mobi-number').prop('readonly',true);
				
			});*/
			$("body").click(function(e) {
				/*alert(e.target.id)*/
		      if(e.target.id !== 'pref-mobi-holder'){
		       $('.prefer-mobi-number').prop('readonly',true);
		      }      
		    }); 
			
			
			$(document).on('click','#prefmob-edit-link,#prefer-mobi-number-code,#prefer-mobi-number',function(){
				
				$('.prefer-mobi-number').prop('readonly',false);
				$('.prefer-mobi-number:eq(0)').focus();
			});
			
			// To display the Sales and Use Tax note.
		$(".cellReg #no").on("click", function() {
			$(".cellReg").find(".selNo").show();
		})
		$(".cellReg #yes").on("click", function() {
			$(".cellReg").find(".selNo").hide();
		})	
		
		$("#registerForm").validate();
		
		$("#emailAddress").blur(function(){
			$("#registerForm").validate();
		});
		
		$("#supNumber").blur(function(){
			$("#registerForm").validate();
		});
		
		
		$("#reenterEmailAddress").blur(function(){
			$("#reenterEmailAddress").validate();
		});
		$("#supervisorEmail").blur(function(){
			$("#registerForm").validate();
		});
		
		// String Validation
		$(".alpha-only").on("keydown", function(event){
			  // Allow controls such as backspace
			  var arr = [8,9,16,17,20,32,35,36,37,38,39,40,45,46];

			  // Allow letters
			  for(var i = 65; i <= 90; i++){
			    arr.push(i);
			  }

			  // Prevent default if not in array
			  if(jQuery.inArray(event.which, arr) === -1){
			    event.preventDefault();
			  }
			});
		
		// Address allow /- Validation
		$(".address-only").on("keydown", function(event){
			
			  // Allow controls such as backspace
			  var arr = [8,9,16,17,20,32,35,36,37,38,39,40,45,46,96,97,98,99,100,101,102,103,104,105,188,189,190,191];

			  // Allow letters
			  for(var i = 65; i <= 90; i++){
			    arr.push(i);
			  }
			  
			  // Allow letters
			  for(var i = 48; i <= 57; i++){
			    arr.push(i);
			  }

			  // Prevent default if not in array
			  if(jQuery.inArray(event.which, arr) === -1){
			    event.preventDefault();
			  }
			});
		
		// Number Validation
		$(document).on("keyup",".numeric-only", function(event){
			this.value = this.value.replace(/[^0-9]/g, '');
			// Allow controls such as backspace
			  var arr = [8,16,17,20,32,35,36,37,38,39,40,45,46];

			  // Allow letters
			  for(var i = 48; i <= 57; i++){
			    arr.push(i);
			  }

			  // Prevent default if not in array
			  if(jQuery.inArray(event.which, arr) === -1){
			    event.preventDefault();
			  }
	        
			});
		
		
		$(document).on("keyup",".qtyUpdateTextBox", function(event){
			this.value = this.value.replace(/[^0-9]/g, '');
			// Allow controls such as backspace
			  var arr = [8,16,17,20,32,35,36,37,38,39,40,45,46];

			  // Allow letters
			  for(var i = 48; i <= 57; i++){
			    arr.push(i);
			  }

			  // Prevent default if not in array
			  if(jQuery.inArray(event.which, arr) === -1){
			    event.preventDefault();
			  }
	        
			});
		
		// Number Validation  5506
		$(document).on("keyup",".phone-only", function(event){
			this.value = this.value.replace(/[^+0-9]/g, '');
			/*
			// Allow controls such as backspace
			  var arr = [8,9,16,17,20,32,35,36,37,38,39,40,45,46,189];

			  // Allow letters
			  for(var i = 48; i <= 57; i++){
			    arr.push(i);
			  }

			  // Prevent default if not in array
			  if(jQuery.inArray(event.which, arr) === -1){
			    event.preventDefault();
			  }*/
	        
			});
		
		$('.numbersonly').on('keyup keypress', function()
				{
			        var value = $(this).val();
			        pttern = /^\d{1,25}$/g;
			        if(value.match(pttern))
			        {
						return true;
			        }
					else
					{
						$(this).val(value.substring(0,value.length-1));
					}
			
			    });
		
		$('.numbersAndcomma').on('keyup keypress', function()
				{
			        var value = $(this).val();
			        pttern = /^[0-9,]+$/;
			        if(value.match(pttern))
			        {
						return true;
			        }
					else
					{
						$(this).val(value.substring(0,value.length-1));
					}
			
			    });
		
		$('.alphanumeric').on('keyup', function(e)
				{
			e.preventDefault();
			//alert();
			        var value = $(this).val();
			        pttern = /^[a-z0-9]+$/i;
			       
			        if(value.match(pttern))
			        {
						return true;
			        }
					else
					{
						$(this).val(value.substring(0,value.length-1));
					}
			
			    });
		
		
		
			// This is the method for the User Profile Activation
			$("#btncreacc").click(
					function(){
						var form = $("#userActivationForm");
						ACC.registration.validateSecurityStep(form,function(){
		                	$("#phone").val($("#regPhoneNumberPrefix").val() +"|"+ $("#regPhoneNumber").val());
		                	if($("#regMobileNumberPrefix").val() !== "" && $("#regMobileNumber").val()!=="" ){
		                		$("#mobile").val($("#regMobileNumberPrefix").val() +"|"+ $("#regMobileNumber").val());	
		                	}
		                	if($("#regFaxNumberPrefix").val() !== "" && $("#regFaxNumber").val()!== "" ){
		                	$("#fax").val($("#regFaxNumberPrefix").val() +"|"+ $("#regFaxNumber").val());
		                	}
							form.submit();
						});
					});
			
			$("#jjEmployee").click(
					
					function() {
						$('#myCompany,#accNumbersRadio').prop('checked',false);
						$.ajax({
							type : "POST",
							url : ACC.config.contextPath +"/register/divisions",
							success : function(data) {

								var options = "<option value=''>Select</option>";
								$.each(data,function(index,element){
									options = options + "<option value=\"" + index + "\">"
									+ element + "</option>";
								})
								
								$("#divis").html(options);
								$("#divis").selectpicker('refresh');
								$('.jjEmployeeYes').show(); 
							},
							error : function(e) {
							}
						});
					});

			$("#myCompany").click(function(){
				$('.jjEmployeeYes').hide();
				$('#productInformationError').hide();
				
				$('#custAccount').prop('checked',true);
				var unknownAccountFlag = $("#unknownAccount");
				if (unknownAccountFlag.val() === "true" || unknownAccountFlag.val() === true )
					unknownAccountFlag.val(false);
				else
					unknownAccountFlag.val(true);
			});
			$("#accNumbersRadio").click(function(){
				$('.jjEmployeeYes').hide();
				$('#productInformationError').hide();
				$('#custAccount').prop('checked',true);
			});
			
			//To display the states for the company information page - the bill to country section
			$("#billCountry1").bind('change', function() {
				 
				
				var countryElement = this;
				if(null != countryElement && '' != countryElement && null!= countryElement.value && 'US'== countryElement.value){
					var country = countryElement.value;
				 
					var dataObj = new Object();
				dataObj.country = country; 
				jQuery.ajax({
					type: 'POST',
					url: ACC.config.contextPath +'/register/getStates',
					data: dataObj,
					success: function (data) {
						var options=SELECT
						var phoneOption="";
						for(var i=0;i<data.length;i++){
							options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
							phoneOption=phoneOption+ "<option value="+data[i].phoneCode+">"+data[i].phoneCode+"</option>";
						}
						
						/*$("#billState").html(options);
						$(".noUsBillState").hide();
						$("#noUsBillState").attr("disabled",true);
						$(".billState").show();
						$("#billState").attr("disabled",false);	*/
						$("#UsBillStatediv").removeClass("hide");
						$("#noUsBillStatediv").addClass("hide");
						$("#noUsBillState").attr("disabled",true);
						$("#billState").attr("disabled",false);
						
					},
					error: function (e) {
						
					}
				});}
				else if(null != countryElement && '' != countryElement && null!= countryElement.value && ''== countryElement.value)
				{
					/*var options="<option value=''>Select State</option>";
					$("#billState").html(options);
					$(".noUsBillState").hide();
					$("#noUsBillState").attr("disabled",true);
					$(".billState").show();
					$("#billState").attr("disabled",false);*/
					$("#UsBillStatediv").removeClass("hide");
					$("#noUsBillStatediv").addClass("hide");
					$("#noUsBillState").attr("disabled",true);
					$("#billState").attr("disabled",false);
					
				}
				else
					{  /* $("#noUsBillState").removeAttr("class");*///added by amol
						/*$(".billState").hide();
						$("#billState").attr("disabled",true);
						$(".noUsBillState").show();
						$(".noUsBillState").css("display","inline-block");
						$("#noUsBillState").attr("disabled",false);*/
					$("#UsBillStatediv").addClass("hide");
					$("#noUsBillStatediv").removeClass("hide");
					$("#noUsBillState").attr("disabled",false);
					$("#billState").attr("disabled",true);
						
					}
				
					
			});
				//To display the states for the company information page - the ship to country section
			$("#shipToCountry").bind('change', function() {
				 
				var countryElement = this;
				if(null != countryElement && '' != countryElement && null!= countryElement.value && 'US'== countryElement.value){
					
					var country = countryElement.value;
					 
					var dataObj = new Object();
				dataObj.country = country; 
				jQuery.ajax({
					type: 'POST',
					url: ACC.config.contextPath +'/register/getStates',
					data: dataObj,
					success: function (data) {
						var options=SELECT
						var phoneOption="";
						for(var i=0;i<data.length;i++){
							options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
							phoneOption=phoneOption+ "<option value="+data[i].phoneCode+">"+data[i].phoneCode+"</option>";
						}
						
						/*$("#shipState").html(options);
						$(".shipState").show();
						$("#shipState").attr("disabled",false);
						$(".noUsShipState").hide();
						$("#noUsShipState").attr("disabled",true);*/
						$("#UsShipStatediv").removeClass("hide");
						$("#noUsShipStatediv").addClass("hide");
						$("#noUsShipState").attr("disabled",true);
						$("#shipState").attr("disabled",false);
						
					},
					error: function (e) {
					}
				});}
				else if(null != countryElement && '' != countryElement && null!= countryElement.value && ''== countryElement.value)
				{
					var options=SELECT
					$("#shipState").html(options);
					$(".shipState").show();
					$("#shipState").attr("disabled",false);
					$(".noUsShipState").hide();
					$("#noUsShipState").attr("disabled",true);
					
					
					
				}
				else
				{   /*$("#noUsShipState").removeAttr("class"); //added by amol
					$(".noUsShipState").show();
					$(".noUsShipState").css("display","inline-block");
					$("#noUsShipState").attr("disabled",false);
					$(".shipState").hide();
					$("#shipState").attr("disabled",true);*/
					
					$("#UsShipStatediv").addClass("hide");
					$("#noUsShipStatediv").removeClass("hide");
					$("#noUsShipState").attr("disabled",false);
					$("#shipState").attr("disabled",true);
				}
			});
			//To display the states for the user information page.
			
				$("#regCountry").bind('change', function() {
				var countryElement = this;
				if(null != countryElement && '' != countryElement && null!= countryElement.value && 'US' == countryElement.value){
					var country = countryElement.value;
						
					var dataObj = new Object();
			 
				dataObj.country = country; 
				jQuery.ajax({
					type: 'POST',
					url: ACC.config.contextPath +'/register/getStates',
					data: dataObj,
					success: function (data) {
						var options=SELECT
						var phoneOption="";
						for(var i=0;i<data.length;i++){
							options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
							phoneOption=phoneOption+ "<option value="+data[i].phoneCode+">"+data[i].phoneCode+"</option>";
						}
						
						/*$("#regState").html(options);
						$(".regState").show();
						$("#regState").attr("disabled",false);
						$(".noUsRegState").hide();
						$("#noUsRegState").attr("disabled",true);*/
						$("#UsRegStatediv").removeClass("hide");
						$("#noUsRegStatediv").addClass("hide");
						$("#noUsRegState").attr("disabled",true);
						$("#regState").attr("disabled",false);
					},
					error: function (e) {
					}
				});
				}
				else if(null != countryElement && '' != countryElement && null!= countryElement.value && ''== countryElement.value)
				{
					
					
					$("#UsRegStatediv").removeClass("hide");
					$("#noUsRegStatediv").addClass("hide");
					$("#noUsRegState").attr("disabled",true);
					$("#regState").attr("disabled",false);
				}
				else
				{   
					 
					$("#UsRegStatediv").addClass("hide");
					$("#noUsRegStatediv").removeClass("hide");
					$("#noUsRegState").attr("disabled",false);
					$("#regState").attr("disabled",true);
				
				}
			});

			$("#custAccount").click(function(){
				$('#productInformationError').hide();
				//if(!$("#wwid").closest("li").hasClass("hide")) $("#wwid").closest("li").addClass("hide");
				
				$('.jjEmployeeYes').hide();
				$('#accNumbersRadio').prop('checked',true);
			});
				
			$("#jjEmployee").click(function() { 
				$('.jjEmployeeYes').show();
				$('#productInformationError').hide();
			 var data = new Object();
				data.isUnknownAccount = false;
				data.isMDD = $("#medicalDevices").is(':checked');
				data.isConsumer = $("#consumerProd").is(':checked');
				typeofProfileSelected = $(".jjProfile:checked").attr("id");
				data.typeOfProfile = typeofProfileSelected;
				switch (typeofProfileSelected) {
				case "custAccount":
					accountsOption = $("li.gln input.yesAcc:checked").attr("id");
					if (data.isMDD) {
						data.accounts = $("input#accNumbers").val();
					} else if (data.isConsumer) {
						if (accountsOption == "accNumbersRadio") {
							data.accounts = $("input#accNumbers").val();
						} else {
							data.isUnknownAccount = true;
						}

					}
					// $("li.gln input.yesAcc:checked").attr("id");

					break;
				case "jjEmployee":
					data.wwid = $("#wwid").val();
					data.divison = $("#divis").val();
					data.empAccount = $(".jjEmployeeYes .accounts").val();
					break;
				default:
					break;
				}
			});

			$("#jjCustomer").click(function() { 
				$('#productInformationError').hide();
				$('.jjEmployeeYes').hide();
				$('.profile-accordian:eq(1)').show();
			
			});

			$("#medicalDevices, #custAccount, #jjCustomer, #jjEmployee, #myCompany").click(function() {
				if(($("#medicalDevices").is(':checked') && $("#custAccount").is(':checked'))) addRequireClass();
				else if(($("#accNumbersRadio").is(':checked') && $("#custAccount").is(':checked'))) addRequireClass();
				else removeRequireClass();
			});

			function removeRequireClass(){
				if($("#accNumbers").hasClass("required")) $("#accNumbers").removeClass("required");
				if($("#accNumbers").hasClass("numberComma")) $("#accNumbers").removeClass("numberComma");	
			}

			function addRequireClass(){
				if(!$("#accNumbers").hasClass("required")) $("#accNumbers").addClass("required");
				if(!$("#accNumbers").hasClass("numberComma")) $("#accNumbers").addClass("numberComma");
			}
			
			/*--------------------copy shipping address to billing address----------------------*/
			$("#billToLocation").on("click", function(){
				if($(this).prop("checked") === true){
					copyAddress();
					$(this).parent().parent().parent().find("input[type='text'], select").each(function(){
						$(this).prop("readonly", true);
						//$(this).attr("disabled",true);
						/*if($(this).hasClass('selectpicker')){
							$(this).selectpicker('refresh')
						}*/
						
					});
				} else {
					$(this).parent().parent().parent().find("input[type='text'], select").each(function(){
						$(this).prop("readonly", false);
						if($(this).prop("tagname") == "SELECT"){
							$(this).prop('selectedIndex', 0);
							//$(this).attr("disabled",false);
							
						} else {
							$(this).val("");
							//$(this).attr("disabled",false);
						}

						$(this).val('');
						if($(this).hasClass('selectpicker')){
							$(this).selectpicker('refresh')
						}
					});

				}
			});

			function copyAddress(){
				//AAOL-3347
				$('#shipToCountry,#shipCountry').val($("#billCountry1").val()).change();				
				$("#shipAddress1").val($("#billAddress1").val());
				$("#signup-ship-To-add2").val($("#signup-Bill-To-add2").val());
				$("#shipCity").val($("#billCity").val());
				$("#shipToZipCode").val($("#billToZipCode").val());
				if("US"==$('#shipToCountry').val())
				{
					var country = $('#shipToCountry').val();
					var dataObj = new Object();
					dataObj.country = country; 
					jQuery.ajax({
						type: 'POST',
						url: ACC.config.contextPath +'/register/getStates',
						data: dataObj,
						success: function (data) {
							var options="<option value=''>Select State</option>";
							var phoneOption="";
							for(var i=0;i<data.length;i++){
								options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
								phoneOption=phoneOption+ "<option value="+data[i].phoneCode+">"+data[i].phoneCode+"</option>";
							}
							
							$("#shipState").html(options);
							$("#shipState").val($("#billState").val());
							$("#shipState").show();
							$(".noUsShipState").hide();
							$("#noUsShipState").attr("disabled",true);
							
						},
						error: function (e) {
						}
					})
				}
				else if("US" != $('#shipToCountry').val() && $('#shipToCountry').val() != '')
				{
					/*$(".noUsShipState").show();
					$(".shipState").hide();
					$('#noUsShipState').val($("#noUsBillState").val());*/
					
					$("#UsShipStatediv").addClass("hide");
					$("#noUsShipStatediv").removeClass("hide");
					$("#noUsShipState").val($("#noUsBillState").val());
				}else{
					
					$("#UsShipStatediv").removeClass("hide");
					$("#noUsShipStatediv").addClass("hide");
					$("#noUsShipState").attr("disabled",true);
					$("#shipState").attr("disabled",false);
					$("#shipState").val($("#billState").val());
				}
			}
			/*-------------------- End copy shipping address to billing address----------------------*/
			/*--------------------Switch validation condition if second step is not required------*/
			$("#custAccount, #jjCustomer, #jjEmployee").on("click",switchProfileValidation);

			function switchProfileValidation(){
				if($("#jjCustomer").is(':checked')){
					$(".profileRegStep2 .switchClass").each(function() {
					  $(this).addClass("required");
					});
				}	
				else{
					$(".profileRegStep2 .switchClass").each(function() {
					  $(this).removeClass("required");
					});
				}
			}
			/*--------------------End Switch validation condition if second step is not required------*/
			/*----------US Phone number format------------*/
			
			pttern = /^\d{1,6}$/g;
			$(document).on("blur","#regFaxNumber, #regMobileNumber, #regPhoneNumber,#supNumber,#preferredMobileNumber",function() { 
				
				if($(this).val() != ""){
		   		var phoneVal = $(this).val().split("-").join("");
				phoneVal = phoneVal.match(new RegExp(".{1,4}$|.{1,3}", "g")).join("-");
				$(this).val(phoneVal);
				}
			});
			/*----------End US Phone number format------------*/
			// Added required field on profile registration
			$('.profileReg #registerForm .actions ul').append('<li class="marTop5"><span class="redStar marLeft50">(*) - '+REQUIRED_FIELDS+' </span></li>');
			$('#userActivationForm .profileRegBlock:first').prepend('<div class="marRight20 right"><span class="redStar marLeft50">(*) - '+REQUIRED_FIELDS+' </span></div>');
			$("#jumpToNewPage").html(ACC.config.contextPath + "/login");
			$("input#medicalDevices").on("change", function(){
				if(!$(this).is(":checked") && !$("input#consumerProd").is(":checked")) {
					$(this).prop("checked", true);
				}
			});
			
			if($("#medicalDevices").is(":checked")) {
				$("#myCompany").closest("p").hide(); 
				$("#accNumbersRadio").hide();
				$(".consProductNo p.smallFont").fadeIn();
			}
			
			$("input#consumerProd").on("change", function(){ 
				if(!$(this).is(":checked") && !$("input#medicalDevices").is(":checked")) {
					$(this).prop("checked", true);
				}
				if($(this).is(":checked")){
					
					$(".consProductNo p.smallFont").hide();
					$("#myCompany").closest("p").fadeIn(); 
				
					$("#accNumbersRadio").fadeIn();
					
				}else{
					$("#myCompany").closest("p").hide(); 
					$("#accNumbersRadio").prop('checked',true).hide();
					$(".consProductNo p.smallFont").fadeIn();
				}
			});
			$("finishButtonText").on("click", function() {
				return validationRegistrationForm();
			})
			 $("#finishButtonText").hide();
		}
};	 


/** AAOL #5371 this method is used to generate PDF or excel for FINANCIAL INVOICE REPORT **/
$("#userManagementExcel,#userManagementPdf").click(function(){
 	//alert('HI ' + 'userManagementExcel' + 'userManagementPdf');
 	var downloadType = $("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
 	//alert(ACC.config.contextPath +'/resources/usermanagement/downloadReport' + downloadType)
 	$("#searchUserForm").attr("action", ACC.config.contextPath +'/resources/usermanagement/downloadReport');
 	$("#searchUserForm").submit();
 	$("#searchUserForm").attr("action", $("#originalFormAction").val());
 });
 
if(document.URL.includes('help')){
	
$("#accordionFaq").click(function() {
$('#jnj-body-content').height('auto');
});

$("#accordionContactUs").click(function() {
$('#jnj-body-content').height('auto');
});

$("#accordionUsefulLinks").click(function() {
$('#jnj-body-content').height('auto');
});

}

$(document).ready(function(){
	
	 ACC.registration.bindAll();
	 $("#reenterEmailAddress").on("keyup", function(){
		 
		 clearErrorMessages("#emailAddressValidateError");
				
	});
	 
	 if($("#userActivationForm").length!=0){
		 $("#regPhoneNumberPrefix, #regMobileNumberPrefix, #regFaxNumberPrefix").change(function(){
	     	if($(this).val()==""){
	     		$(this).val("+1");
	     	}
	     });
		 
	 }
	 
	 $('#signuppage .clickontext:eq(0)').click(function(){
		checkCustomRaio(); 
	 }); 
	 
	 $('.customRadio').click(function(){
		 checkCustomRaio();
	});
	
	 function checkCustomRaio(){
		 var customRadio=false;
		 $('.customRadio').each(function(){
			if($(this).prop('checked')==true){
				customRadio=true;
			}
		});
		 if(customRadio==true){
			$('.profile-accordian:eq(1)').hide();
			$("#tab1NextButtonText").attr("href","#collapse3");
			 
		 }else{
			 $('.profile-accordian:eq(1)').show();
			 $("#tab1NextButtonText").attr("href","#collapse8");
		 }
	 }
	 if($('#viewOnly').length>0){
		 fetchEmailPreferences('viewOnly');
	 }
	 $('#viewOnly').click(function(){
		
			var keepItems = $("#viewOnly").is(':checked');
			
			if(keepItems){
				var pref='viewOnly';
				 fetchEmailPreferences(pref);
			}
			

		
	});
	 
	 $('#placeOrder').click(function(){
			
			var keepItems = $("#placeOrder").is(':checked');
			
			if(keepItems){
				 $('#shippedOrderEmail').val('weekly');
				 $('#backorderEmailType').val('weekly');
				   $('#inoviceEmailPreference').val('weekly');
				   $('#deliveryNoteEmailPreference').val('weekly');
				var pref='placeOrder';
				 fetchEmailPreferences(pref);
			}
		

		
	});
	 $('#noCharge').click(function(){
			
			var keepItems = $("#noCharge").is(':checked');
			 
			if(keepItems){
				 $('#shippedOrderEmail').val('weekly');
				var pref='noCharge';
				 fetchEmailPreferences(pref);
			}
			

		
	});
	
	
	 function fetchEmailPreferences(index){
	
		  var dataObj = new Object();
		  dataObj.emailPreference=index;
		  dataObj.CSRFToken=ACC.config.CSRFToken;
			$.ajax({
					type : "POST",
					url : ACC.config.contextPath+ '/register/emailPreferences',
					data : dataObj,
					success : function(data) {  
					
					 $('.emailPreferenceAccordian').html(data);
					}
				});
	 }
	
	//AAOL-2429 and AAOL-2433 changes
		$('#noChargeSection').parent().css("display","none");
		
		$('#custAccount').click(function(){

			 $('#noChargeSection').parent().css("display","none");
		 });
			
		$('#jjCustomer').click(function(){

			 $('#noChargeSection').parent().css("display","none");
		 });
		 
		$('#jjEmployee').click(function(){

			 $('#noChargeSection').parent().css("display","block");
		 });
		 
		  $('.emailPreferenceLink').click(function(){
			if($("#placeOrder").is(':checked')) {
				$("#check7").parent().css("display","none");
				$("#check9").parent().css("display","none");
				$("#check10").parent().css("display","none");
				
				if( $("#medicalDevices").is(':checked')){
					$("#check9").parent().css("display","block");
					$("#check10").parent().css("display","block");
				 }
				if( $("#consumerProd").is(':checked')){
					$("#check7").parent().css("display","block");
				 }
			} 
			//AAOL 4911 email preference checked for triggering the emails when order placed
	         emiltriggeringMailChecked();
	 });	
		  
/*5506*/
		  $(document).on("keyup","#mobileNumberPrefix,#preferredMobileNumber",function(){
				
				if( ($("#mobileNumberPrefix").val() != '' ) && ($("#preferredMobileNumber").val() != '') ){
					//alert("--");
					$(".enableCheckboxClass").attr('disabled',false);
				}
				else if( ($("#mobileNumberPrefix").val() == '' ) || ($("#preferredMobileNumber").val() == '') ){
					$(".enableCheckboxClass").attr('disabled',true);
					$(".enableCheckboxClass").prop('checked',false);
				}
			}); 
		  
	
});
//START AAOL 4911 email preference checked for triggering the emails when order placed
function emiltriggeringMailChecked() {
	if($("#placeOrder").is(':checked') || $("#noCharge").is(':checked')) {
		emailPrefrenceChecked(15,16,17,18);
	} 
}
function emailPrefrenceChecked(a,b,c,d) {
		//alert(a+">> "+b)
			/* var jjProfileValue;
			 $('.jjProfile').each(function(){
				 if($(this).prop('checked')){
					 jjProfileValue=$(this).val();
				 }
			 });
			 
			 $("input[value='emailPreference"+a+"']").parent().hide();
			 $("input[value='emailPreference"+b+"']").parent().hide();
			 
			 //alert('email >> '+jjProfileValue.trim(''))
			 if(jjProfileValue.trim('')=='custAccount' || jjProfileValue.trim('')=='jjCustomer'){
				 $("input[value='emailPreference"+a+"']").parent().show()
			 }
			 if(jjProfileValue.trim('')=='jjEmployee'){
				 $("input[value='emailPreference"+b+"']").parent().show()
			 }*/
			//End AAOL 4911- Showing email prefereneces based on type of product	
	/*5506*/
	 var jjProfileValue;
	 $('.jjProfile').each(function(){
		 if($(this).prop('checked')){
			 jjProfileValue=$(this).val();
		 }
	 });
	 $("div[class='emailPreference"+a+"']").hide();
	 $("div[class='emailPreference"+b+"']").hide();
	 $("div[class='emailPreference"+c+"']").hide();
	 $("div[class='emailPreference"+d+"']").hide();
	 if(jjProfileValue.trim('')=='custAccount' || jjProfileValue.trim('')=='jjCustomer'){
		
		 $("div[class='emailPreference"+a+"']").show();
	 }
	 else if(jjProfileValue.trim('')=='jjEmployee'){
		
		 $("div[class='emailPreference"+b+"']").show();
		 $("div[class='emailPreference"+c+"']").show();
		 $("div[class='emailPreference"+d+"']").show();
	 }
	 
		/*5506*/
	
	  }
// END AAOL 4911 email preference checked for triggering the emails when order placed

var productInfor,companyInfo,yourInfo,personalInfo,emailPreferences=false;
/*Each Accordian Validation Start*/
function productInformationValidate(){

	 $("#tab1NextButtonText").attr("href","#");
	// $("#signuppage .profile-accordian-header:eq(0)").find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus');
	 //$('#signuppage .toggle-link:eq(0)').removeClass('panel-collapsed');
		
	 var registrationError = "<div >";
		 registrationError +="<ul>";
		 
		 var Status=true;
		 
			var data = new Object();
			data.isUnknownAccount = false;
			data.isMDD = $("#medicalDevices").is(':checked');
			data.isConsumer = $("#consumerProd").is(':checked');
			typeofProfileSelected = $("input.jjProfile:checked").attr("id");
			data.typeOfProfile = typeofProfileSelected;
			if(typeofProfileSelected == "custAccount" && $("#accNumbers").val() != "") {
				accountsOption = $("li.gln input.yesAcc:checked").attr("id");
				if (data.isMDD) {
					data.accounts = $("input#accNumbers").val();
				} else if (data.isConsumer) {
					if (accountsOption == "accNumbersRadio") {
						data.accounts = $("input#accNumbers").val();
					} else {
						data.isUnknownAccount = true;
					}

				}
				// $("li.gln input.yesAcc:checked").attr("id");

			}else if(typeofProfileSelected == "custAccount" && $("#accNumbers").val() == ""&&$('#accNumbersRadio').is(':checked')){
				registrationError += "<li>"+VALID_ACCOUNT+"</li>";
				Status=false
				 $('#productInformationError').show();	 
					registrationError += "</ul></div>";
					$("#productInformationError").html(registrationError);
					return false;
			}
			if(typeofProfileSelected=="jjEmployee"){
				data.wwid = $("#wwid").val();
				data.divison = $("#divis").val();
				data.empAccount = $(".jjEmployeeYes .accounts").val();
				
				if(data.wwid=="" || data.divison==""){
					registrationError += "<li>"+REQUIRED_WWID_DIV+"</li>"; 
					 Status=false
					 $('#productInformationError').show();	 
						registrationError += "</ul></div>";
						$("#productInformationError").html(registrationError);
						return false;
				}
			 
			}
			$.ajax({
				type : "POST",
				url : ACC.config.contextPath +"/register/validateProfile",
				async: false,
				data : data,
				success : function(data) { 
					if(data.status=="error"){
						registrationError += "<li>"+data.message+"</li>"; 
						 Status=false;
					}
				},
				error : function(e) {
				}
			});
			
			 
	if(!Status){
		$('#productInformationError').show();	 
		registrationError += "</ul></div>";
		$("#productInformationError").html(registrationError);
		 $("#tab1NextButtonText").attr("href","#");
		return false;
	}else{
		
		productInfor=true;
		 $('#productInformationError').hide();
		
		 var customRadio=false;
		 $('.customRadio').each(function(){
			if($(this).prop('checked')==true){
				customRadio=true;
			}
		
		});
		
		 if(customRadio==true){
			$('.profile-accordian:eq(1)').hide();
			 nextBtnClick(2);
			$("#tab1NextButtonText").attr("href","#collapse3");
			 
		 }else{
			 nextBtnClick(1);
			 $("#tab1NextButtonText").attr("href","#collapse8");
		 }
	}
}


function clearErrorMessages(id){
		$(id).html(""); 

	}


function companyInformationValidate(){
	 
	 var registrationError = "<div >";
	 registrationError +="<ul>";
	 
	 var Status=true;
	 
	 clearErrorMessages("#accountNameError");
	 if($.trim($("#accountName").val())===""){
		 $("#accountNameError").html(REQUIRED_ACCOUNT);
		 Status=false;
	 }
	 
	 clearErrorMessages("#bussTypeError");
	if($.trim($("#bussType").val())===""){
		 $("#bussTypeError").html(REQUIRED_BUSINESS);
		 Status=false;
	 }
	clearErrorMessages("#billCountryError");
	if($.trim($("#billCountry1").val())===""){
		$("#billCountryError").html(REQUIRED_COUNTRY);
		 Status=false;
	 }
	clearErrorMessages("#billCityError");
	if($.trim($("#billCity").val())===""){
		$("#billCityError").html(REQUIRED_CITY);
		 Status=false;
	 }
	
	// State Need to validate for us
	
	clearErrorMessages("#billAddress1Error");
	if($.trim($("#billAddress1").val())===""){
		$("#billAddress1Error").html(REQUIRED_ADDRESS1);
		 Status=false;
	 }
	
	clearErrorMessages("#billStateError");
	if($.trim($("#noUsBillState").val())==="" && !$("#noUsBillState").prop("disabled")){
		 $("#billStateError").html(REQUIRED_STATE);
		 Status=false;
	 }else if($.trim($("#billState").val())==="" && !$("#billState").prop("disabled")){
		 $("#billStateError").html(REQUIRED_STATE);
		 Status=false;
	 }
	
	clearErrorMessages("#billToZipCodeError");
	if($.trim($("#billToZipCode").val())==="" ){
		$("#billToZipCodeError").html(REQUIRED_ZIPCODE);
		 Status=false;
	 }
	
	clearErrorMessages("#shipToCountryError");
	if($.trim($("#shipToCountry").val())===""){
		$("#shipToCountryError").html(REQUIRED_COUNTRY);
		 Status=false;
	 }
	
	clearErrorMessages("#shipAddress1Error");
	if($.trim($("#shipAddress1").val())===""){
		$("#shipAddress1Error").html(REQUIRED_ADDRESS1);
		 Status=false;
	 }
	
	
	clearErrorMessages("#shipStateError");
	if($.trim($("#noUsShipState").val())==="" && !$("#noUsShipState").prop("disabled")){
		 $("#shipStateError").html(REQUIRED_STATE);
		 Status=false;
	 }else if($.trim($("#shipState").val())==="" && !$("#shipState").prop("disabled")){
		 $("#shipStateError").html(REQUIRED_STATE);
		 Status=false;
	 }
	
	clearErrorMessages("#shipCityError");
	if($.trim($("#shipCity").val())===""){
		$("#shipCityError").html(REQUIRED_CITY);
		 Status=false;
	 }
	
	clearErrorMessages("#shipToZipCodeError");
	if($.trim($("#shipToZipCode").val())===""){
		$("#shipToZipCodeError").html(REQUIRED_ZIPCODE);
		 Status=false;
	 }
	
	clearErrorMessages("#estAmountError");
	if($.trim($("#estAmount").val())===""){
		$("#estAmountError").html(REQUIRED_OPENING_ORDER);
		 Status=false;
	 }
	
	clearErrorMessages("#estimatedAmountPerYearError");
	if($.trim($("#estimatedAmountPerYear").val())===""){
		$("#estimatedAmountPerYearError").html(REQUIRED_EST_AMOUNT);
		 Status=false;
	 }
	
	
	var array = $.map($('input[name="medicalProductsPurchase"]:checked'), function(c){return c.value; })
	if(array.length===0){
		 registrationError += "<li>"+REQUIRED_PRODUCT+" </li>"; 
		 Status=false;
	}
	if(!Status){
		
		$('#companyInformationError').show();
		$('.errorProfileInfo').show();
		registrationError += "</ul></div>";
		$("#companyInformationError").html(registrationError);
		$("#tab2NextButtonText").attr("href","#");
		return false;
	}else{
		companyInfo=true;
		$('.errorProfileInfo').html("");	
	 $('#companyInformationError').hide();
	  $('.errorProfileInfo').hide();
	 nextBtnClick(2);
	$("#tab2NextButtonText").attr("href","#collapse3");
		$(this).scrollTop(0);     
	}
	
}

function yourInformationValidate(){
	
	var registrationError = "<div >";
		 registrationError +="<ul>";
		 
		 var Status=true;
		 clearErrorMessages("#firstNameError");
		 
//			var prefixRegex = /^(([0-9](-)?)*)+$/;
		 	var prefixRegex =/^(\+?\d{1,3})$/;
			var prefix = $("#supervisorPhonePrefix").val();
			 if(prefix.match(prefixRegex)){
					clearErrorMessages("#supNumberError");
				}else{
					clearErrorMessages("#supNumberError");
					$("#supNumberError").html(VALID_PREFIX);
					Status = false;
				}
		 
	 if($.trim($("#firstName").val())===""){
		 $("#firstNameError").html(REQUIRED_FIRST_NAME);
		 Status=false; 
	 }
	 clearErrorMessages("#signup-lnameError");
	 if($.trim($("#signup-lname").val())===""){
		 $("#signup-lnameError").html(REQUIRED_LAST_NAME);
		 Status=false; 
	 }
	 clearErrorMessages("#organizationNameError");
	 if($.trim($("#organizationName").val())===""){
		 $("#organizationNameError").html(REQUIRED_ORG_NAME);
		 Status=false; 
	 }
	 clearErrorMessages("#departmentError");
	 if($.trim($("#department").val())===""){
		 $("#departmentError").html(REQUIRED_DEPARTMENT);
		 Status=false; 
	 }
	 clearErrorMessages("#emailAddressError");
	 if($.trim($("#emailAddress").val())===""){
		 $("#emailAddressError").html(REQUIRED_BUSINESS_EMAIL);
		 Status=false; 
	 }
	 clearErrorMessages("#reenterEmailAddressError");
	 clearErrorMessages("#emailAddressValidateError");
	 if($.trim($("#reenterEmailAddress").val())===""){
		 $("#reenterEmailAddressError").html(RE_ENTER_EMAIL);
		 Status=false; 
	 }else if($.trim($("#reenterEmailAddress").val()) != $.trim($("#emailAddress").val())){
		 $("#emailAddressValidateError").html(EMAIL_MISMATCH);
		 Status=false; 
	 }
	 
	 clearErrorMessages("#supervisorNameError");
	 if($.trim($("#supervisorName").val())===""){
		 $("#supervisorNameError").html(REQUIRED_SUPER_NAME);
		 Status=false; 
	 }
	
	/* clearErrorMessages("#supNumberError");
	 if($.trim($("#supPhoneNumberPrefix").val())===""){
		 $("#supNumberError").html("Please enter Supervisor Phone Number Prefix");
		 Status=false; 
	 }*/

	 if($.trim($("#supNumber").val())===""){
		 $("#supNumberError").html(REQUIRED_SUPER_PHONE);
		 Status=false; 
	 }
	 clearErrorMessages("#supervisorEmailError");
	 if($.trim($("#supervisorEmail").val())===""){
		 $("#supervisorEmailError").html(REQUIRED_SUPER_EMAIL);
		 Status=false; 
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
					console.log(data);
					if(data.isUserValid=="false"){
						registrationError += "<li>"+data.errorMsg+"</li>"; 
						 Status=false;
					}
				},
				error: function (e) {
					
				}
			});
		
	 if(!Status){
			$('#yourInformationError').show();
			 $('.errorYourInfo').show();
			registrationError += "</ul></div>";
			$("#yourInformationError").html(registrationError);
			$("#tab3NextButtonText").attr("href","#");
			return false;
		}else{
			yourInfo=true;
		 $('.errorYourInfo').html("");
		 $('#yourInformationError').hide();
		 $('.errorYourInfo').hide();
		 nextBtnClick(3);
		 $("#tab3NextButtonText").attr("href","#collapse4");
		}
}
function personalInformationValidate(){

	var registrationError = "<div >";
		 registrationError +="<ul>";
		 
		 
		 //var prefixRegex = /^(([0-9](-)?)*)+$/;
		 var prefixRegex =/^(\+?\d{1,3})$/;
		
			var prefix = $("#regPhoneNumberPrefix").val();
		
			 if(prefix.match(prefixRegex)){
					clearErrorMessages("#regPhoneNumberError");
				}else{
					clearErrorMessages("#regPhoneNumberError");
					$("#regPhoneNumberError").html(VALID_PREFIX);
					Status = false;
				}
		 
		 var Status=true;
	 
		 /*clearErrorMessages("#regPhoneNumberError");
	 if($.trim($("#regPhoneNumberPrefix").val())===""){
		 //$("#supervisorEmailError").html("Please Enter Bill One Address");
		 //registrationError += "<li>Please Enter Your Phone Number Prefix</li>";
		 $("#regPhoneNumberError").html("Please Enter Your Phone Number Prefix!");
		 Status=false; 
	 }*/
	
	 if($.trim($("#regPhoneNumber").val())===""){
		 $("#regPhoneNumberError").html(REQUIRED_PHONE);
		 Status=false; 
	 }
	 clearErrorMessages("#regCountryError");
	 if($.trim($("#regCountry").val())===""){
		  $("#regCountryError").html(REQUIRED_COUNTRY);
		 Status=false;
	 }
	 clearErrorMessages("#regAddress1Error");
	 if($.trim($("#regAddress1").val())===""){
		 $("#regAddress1Error").html(REQUIRED_ADDRESS1);
		 Status=false;
	 }
	 clearErrorMessages("#regCityError");
	 if($.trim($("#regCity").val())==="" ){
		 $("#regCityError").html(REQUIRED_CITY);
		 Status=false;
	 }
	 clearErrorMessages("#regStateError");
	 if($.trim($("#noUsRegState").val())==="" && !$("#noUsRegState").prop("disabled")){
		 $("#regStateError").html(REQUIRED_STATE);
		 Status=false;
	 }else if($.trim($("#regState").val())==="" && !$("#regState").prop("disabled")){
		 $("#regStateError").html(REQUIRED_STATE);
		 Status=false;
	 }
	 
	 // State Need to validate for us
	 clearErrorMessages("#zipError");
	 if($.trim($("#zip").val())==="" ){
		 $("#zipError").html(REQUIRED_ZIPCODE);
		 Status=false;
	 }
	 if(!Status){
			$('#personalInformationError').show();
			 $('.errorPersonalInfo').show();
			registrationError += "</ul></div>";
			$("#personalInformationError").html(registrationError);
			$("#tab4NextButtonText").attr("href","#");
			return false;
		}else{
			
		 personalInfo=true;
		 $('.errorPersonalInfo').html("");
		 $('#personalInformationError').hide();
		 $('.errorPersonalInfo').hide();
		 /*AAOL-4910*/
		 
		// alert($("#regMobileNumberPrefix").val()+$("#regMobileNumber").val())
		 var regMbPrevariable = $("#regMobileNumberPrefix").val();
		 var regMbNumbervariable =  $("#regMobileNumber").val()
		 
		
		 $('#mobileNumberPrefix').val(regMbPrevariable);
		 $('#preferredMobileNumber').val(regMbNumbervariable);
	
		 
  if(regMbPrevariable!='' && regMbNumbervariable!='' ){
	 $(".enableCheckboxClass").attr('disabled',false);	 
		 }
		
		 /*AAOL-4910*/
		 nextBtnClick(4);
		 $("#tab4NextButtonText").attr("href","#collapse5");
		}
	 //AAOL 4911 email preference checked for triggering the emails when order placed 
	 emiltriggeringMailChecked();
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
			 $("#tab5NextButtonText").attr("href","#collapse6");
			}
}


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
	
	if (strength >= 2 && $("#password").val().length>=8) {
		
		return true;
	} else {
		return false;
	}
};


function createpasswordValidate(){
	
	var password = $.trim($("#password").val());
	var reenterPwd = $.trim($("#checkpass2").val());
	
	var registrationError = "<div >";
		 registrationError +="<ul>";
		var complexityCheckstatus = complexityCheck();
		 var Status=true;
		 if(password===""){
			 registrationError += SIGNUP_PASSWORD;
			 Status=false; 
		 }
		 
		 if(reenterPwd ===""){
			 registrationError += REENTER_PASSWORD;
			 Status=false; 
		 }
		 if(!Status){
				$('#createpasswordError').show();	 
				registrationError += "</ul></div>";
				$("#createpasswordError").html(registrationError);
				$("#tab6NextButtonText").attr("href","#");
				return false;
			}else if (!complexityCheckstatus){
				$('#createpasswordError').show();	 
				registrationError += "<li>"+PASSWORD_COMPLEXITY+"</li></ul></div>";
				$("#createpasswordError").html(registrationError);
				
			}else if(reenterPwd == password){
				createpassword=true;
			 $('#createpasswordError').hide();
			 nextBtnClick(6);
			 $("#tab6NextButtonText").attr("href","#collapse7");
			}else{
				$('#createpasswordError').show();	 
				registrationError += "<li>"+PASSWORD_MISMATCH+"</li></ul></div>";
				$("#createpasswordError").html(registrationError);
			} 
}
var captchCount=0;
function secretQuestionValidate(){	
	var registrationError = "<div >";
		 registrationError +="<ul>";
		 var Status=true;
		 var count = 0;
		 $(".secQusCls").each(function(v){
			 if($(this).val()===""){
				 count++;
				 Status=false;
			 } 
		 });
		
		 if ($('button[title="Nothing selected"]').length > 0)
		 {
			 registrationError +="<li>"+SECURITY_QUESTION+"</li>";
		 }
		 else if(count <= 3 && count > 0){
			 registrationError += SECURITY_QUESTION;
		 }
		 if(captchCount==0){
			 if(!Status || $('button[title="Nothing selected"]').length > 0){
					$('#signupsecretquestError').show();
					registrationError += "</ul></div>";
					$("#signupsecretquestError").html(registrationError);
					return false;
				}else{
				 $('#signupsecretquestError').hide();
				 nextBtnClick(7);
				
				 var customRadioFlag=false;
				 $('.customRadio').each(function(){
					if($(this).prop('checked')==true){
						customRadioFlag=true;
					}
				});
				
				 if(customRadioFlag==true){
					 if(productInfor && yourInfo && personalInfo && emailPreferences && createpassword){
						 $("#finishButtonText").show();
					 }else{
						 $('#finalsubmissionError').show();
						 registrationError += "<li>"+ACCORDION_FIELDS+"</li>"; 
							registrationError += "</ul></div>";
							$("#finalsubmissionError").html(registrationError);
							return false;
					 }
				 }
				 else{
					 if(productInfor && companyInfo && yourInfo && personalInfo && emailPreferences && createpassword){
						 $("#finishButtonText").show();
					 }else{
						 $('#finalsubmissionError').show();
						 registrationError += "<li>"+ACCORDION_FIELDS+"</li>"; 
							registrationError += "</ul></div>";
							$("#finalsubmissionError").html(registrationError);
							return false;
					 }
					 
				 }
				 /*if($('#recaptcha_content').length>0){
					var RecaptchaOptions = {

        				theme : 'theme_name'
        			};
       
        		    Recaptcha.create(ACC.config.RecaptchaPublicKey, "recaptcha_content", RecaptchaOptions);
				 }   */

	        		
				}
		 }
		 
		 captchCount=captchCount+1;
}

function validationRegistrationForm(){
	 $('#finalsubmissionError').hide();
	$('.registerErrorCaptcha').html("");
	var registrationError = "<div >";
		 registrationError +="<ul>";
	var Status=true;

		if(!$("#terms-conditions").prop('checked')){
			 registrationError += "<li>"+ACCEPT_TERMS+"</li>"; 
			Status=false;
		}
		if(!$("#privacy-policy").prop('checked')){
			 registrationError += "<li>"+ACCEPT_POLICY+"</li>"; 
			Status=false;
		}
	
		if(!Status){
			$('#finalsubmissionError').show();
			registrationError += "</ul></div>";
			$("#finalsubmissionError").html(registrationError);
			return false;
		}
		
	
		var dataObj = new Object();
		dataObj.captchaResponse = $("#captchaResponse").val();
		
	$.ajax({
		type : "GET",
		url : ACC.config.contextPath +"/register/validateCaptchaResponse",
		data: dataObj,
		async: false,
		success: function(data){
			  if(data){
				  Status=true;
		      }
		      else{
		    	  Status=false
				  $('.registerErrorCaptcha').html("<p class='error'>"+INVALID_CAPTCHA+"</p>");
		    	  grecaptcha.reset();
		      }
		    },
		error : function(e) {
		}
	});
if(!Status){
	$('#finalsubmissionError').show();
	registrationError += "</ul></div>";
	$("#finalsubmissionError").html(registrationError);
	return false;
}
	return Status;
}

$('#signuppage .profile-accordian-header .toggle-link').click(function(){
	$('#signuppage .profile-accordian-header').each(function(){
	   $(this).find('.toggle-link').addClass('panel-collapsed');
   });
	if($(this).parent().next().hasClass('in')==true){
		 $(this).removeClass('panel-collapsed');
	}
	
});

function nextBtnClick(index){
	  var nxtAccordianIndex=index;
	 
		  $('#signuppage .profile-accordian-header').each(function(){
			   $(this).find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
			   $(this).find('.toggle-link').addClass('panel-collapsed');
		   });
			$('#signuppage .profile-accordian-header:eq('+nxtAccordianIndex+')').find('span.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus');
			$('#signuppage .toggle-link:eq('+nxtAccordianIndex+')').removeClass('panel-collapsed');
		
	 
}
	 
//security question related


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


/*Each Accordian Validation Start*/