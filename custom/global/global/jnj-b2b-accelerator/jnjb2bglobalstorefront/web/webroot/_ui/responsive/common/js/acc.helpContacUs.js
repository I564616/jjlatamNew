var flag=true;
ACC.helpContactUs = {

	bindAll: function()
	{
		
		ACC.helpContactUs.selectSubject();
		ACC.helpContactUs.bindcontactUspopupLink($('.contactuspopup_hn'))

	},                                                
	selectSubject: function()
	{
		//Comment by surabhi not using anywhere
		/*$(".contactUsFormSubmit").click(function(e){
			alert(ss)
			if($("#contactUsForm").valid()){
				$("#contactUsForm").find(".contactUsGlobalError").find(".error").hide();
			} else {
				$("#contactUsForm").find(".contactUsGlobalError").find(".error").show();
			}
			if($("#contactUsMessageBox").val()!='')
			{
				if($("#contactUsOrderNumber").val()=='1' && $("#contactUsOrderNumber").val()!='')
				{
					$("#contactUsForm").ajaxSubmit({type: 'POST', url: ACC.config.contextPath + "/help/contactUs"});
				}
				else if($("#contactUsSubject").val()!='')
				{
					$("#contactUsForm").ajaxSubmit({type: 'POST', url: ACC.config.contextPath + "/help/contactUs"});
				}				
			}
		});*/
		
	},
	bindcontactUspopupLink : function(link) {
		link.click(function() {
				//AAOL-4914 Adding Captcha thru recaptcha_ajax call in recaptcha_content div
				/*if($('#recaptcha_content').length>0){
					var RecaptchaOptions = {
						theme : 'theme_name'
					};
			
					Recaptcha.create(ACC.config.RecaptchaPublicKey, "recaptcha_content", RecaptchaOptions);

				}	*/


				
				//End AAOL-4914
				$.get(ACC.config.contextPath + "/help/contactUs").done(function(data) {
					$('#contactuspopupNew').remove();	
					$('#contactuspopupholder').html(data);
					$('#contactuspopupNew').modal('show');
					$('#contactuspopupNew').on('shown.bs.modal', function () {checkContactUs()});
				    $('#contactuspopupNew .contactUsField').keyup(function(){checkContactUs();});
				    $('#contactuspopupNew #contactUsSubjectLb').change(function(){checkContactUs();});
				    $('#contactuspopupNew #contactus-agree').click(function(){checkContactUs();});
				    $('#contactuspopupNew #contactUsFormSubmitLb').prop('disabled',true);
				    function checkContactUs(){
				        var mandatoryFieldFlag=true;
				       
				        //checkbox
				        if($('#contactuspopupNew #contactus-agree').prop('checked')==false){
				                        mandatoryFieldFlag=false;
				        }
				        if(mandatoryFieldFlag==true){
				        	  $('#contactuspopupNew #contactUsFormSubmitLb').prop('disabled',false);
				              $('#contactuspopupNew #contactUsFormSubmitLb').removeClass('btn-disabled-style');
				        }
				        else{
				        	 $('#contactuspopupNew #contactUsFormSubmitLb').prop('disabled',true);
				        	 $('#contactuspopupNew #contactUsFormSubmitLb').addClass('btn-disabled-style');
				        }
				    }
				    
					$("#contactuspopupNew .contactUsFormPage2Lb").hide();
					$("#contactuspopupNew .contactUsSubmitBtn").click(function(){
						
						
						var dataObj = new Object();
						dataObj.captchaResponse = $("#captchaResponse").val();
						
						jQuery.ajax({
							type: "GET",
							url: ACC.config.contextPath + "/help/validateCaptchaResponse",
							data: dataObj,
							success: function (data) {
								if (data)
									{ 		
										if($("#contactuspopupNew .contactUsFormLb").valid()){
													 $('#contactuspopupNew #contactUsFormSubmitLb').removeClass('btn-disabled-style');
													$("#contactuspopupNew .contactUsFormLb").find(".contactUsGlobalError").find(".error").hide();
													$("#contactuspopupNew .contactUsFormLb").ajaxSubmit({
														success : function(data)
														{	
															$("#contactuspopupNew .contactUsSubmitBtn").hide();
															$("#contactuspopupNew .contactUsFormLb").hide();
															$("#contactuspopupNew .contactUsFormPage2Lb").show();
														}
													});
												}else{
													$("#contactuspopupNew .contactUsFormLb").find(".contactUsGlobalError").find(".error").show();
													// $('#contactUsFormSubmitLb').addClass('btn-disabled-style');
												}
												ACC.globalActions.menuNdbodyHeight();
		                            	  }else
	                        				{
	        	                				$('.contactUsErrorCaptcha').html( $("#contactUsCaptchaId").val());
	        	                				$('.contactUsErrorCaptcha').show();
	        	                				grecaptcha.reset();
	        	                				//Recaptcha.reload();
	                                       }
	                                  },
	                                  error : function(e) {
	                                  }
			              });
	
						//end 4914
					});
					
					$("#contactuspopupNew .backToContactUsForm").click(function(e){
						e.preventDefault();
						/*if($('#recaptcha_content').length>0){
							var RecaptchaOptions = {
								theme : 'theme_name'
							};
					
							Recaptcha.create(ACC.config.RecaptchaPublicKey, "recaptcha_content", RecaptchaOptions);

						}	*/


						
						
						$("#contactuspopupNew .contactUsFormLb").show();
						$("#contactuspopupNew .contactUsFormPage2Lb").hide();
						$("#contactuspopupNew #contactUsMessage").val("");
						$("#contactuspopupNew #contactUsSubjectLb").val("");
						$("#contactuspopupNew .contactUsSubmitBtn").show();
						$("#contactuspopupNew #contactUsOrderNumber").val("");
						$("#contactuspopupNew #contactUsProductNumber").val("");
						$("#contactuspopupNew .contactUsOrderNumberLb").css("display","none");
						$("#contactuspopupNew .contactUsProductNumberLb").css("display","none");
						$("#contactuspopupNew #contactUsFormSubmitLb").addClass('btn-disabled-style');
						$("#contactuspopupNew #contactus-agree").prop('checked', false);
						$("#contactuspopupNew #contactUsMessage").css('height',"150px");
						
					});
					
					
					$("#contactuspopupNew #contactUsSubjectLb").change(function(){
						
							if($(this).val() == "1") {
								$("#contactuspopupNew .contactUsOrderNumberLb").css("display","block");
								$("#contactuspopupNew .contactUsProductNumberLb").css("display","none");
								
							} else if($(this).val() == "4"){
								$("#contactuspopupNew .contactUsProductNumberLb").css("display","block");
								$("#contactuspopupNew .contactUsOrderNumberLb").css("display","none");
								
							}else{
								$("#contactuspopupNew .contactUsOrderNumberLb").css("display","none");
								$("#contactuspopupNew .contactUsProductNumberLb").css("display","none");
								
							}
						});	
					
					$('.privacypolicypopupFromContactUs').on('click',function(e) {$.get(ACC.config.contextPath+ "/privacyPolicy").done(
												function(data) {
													$('#contactuspopupNew').modal('hide');
													$('#privacypopupNew').remove();	
													$('#privacypolicypopuopholder').html(data);
													$('#privacypopupNew').modal('show');
													$('#privacypopupNew').css("z-index","2000");
												});
							});
				});
		});
		
	}
};

$(document).ready(function() {
	//AAOL-4729
	$('.privacypolicypopupFromContactUs').on('click',function(e) {$.get(ACC.config.contextPath+ "/privacyPolicy").done(
			function(data) {
				$('#contactuspopupNew').modal('hide');
				$('#privacypopupNew').remove();	
				$('#privacypolicypopuopholder').html(data);
				$('#privacypopupNew').modal('show');
				$('#privacypopupNew').css("z-index","2000");
			});
});
	ACC.helpContactUs.bindAll();
	
	//AAOL-4914 Adding Captcha thru recaptcha_ajax call in recaptcha_content div

	
	/*if($('#recaptcha_content').length>0){
		var RecaptchaOptions = {
				theme : 'theme_name'
			};
			
			Recaptcha.create(ACC.config.RecaptchaPublicKey, "recaptcha_content", RecaptchaOptions);
		
	}
	
		$(document).on('shown.bs.modal', '.modal', function (event) {
			if($('#recaptcha_content').length>0){
				var RecaptchaOptions = {
						theme : 'theme_name'
					};
					
					Recaptcha.create(ACC.config.RecaptchaPublicKey, "recaptcha_content", RecaptchaOptions);
				
			}
		});*/
	//End AAOL-4914
	$('#contactHelp-agree').prop('checked',false);
	checkHelpContactUs();
	//AAOL-3624
	$('#contactHelp-agree').click(function(){checkHelpContactUs();});
    $('#helpContactUsFormSubmitLb').prop('disabled',true);
    function checkHelpContactUs(){
        var mandatoryFieldFlag=true;
       
        //checkbox
        if($('#contactHelp-agree').prop('checked')==false){
                        mandatoryFieldFlag=false;
        }
        if(mandatoryFieldFlag==true){
        	  $('#helpContactUsFormSubmitLb').prop('disabled',false);
              $('#helpContactUsFormSubmitLb').removeClass('btn-disabled-style');
        }
        else{ 
        	 $('#helpContactUsFormSubmitLb').prop('disabled',true);
        	 $('#helpContactUsFormSubmitLb').addClass('btn-disabled-style');
        }
    }
	/*AAOL-6476*/
	$("#contactuspopup #contactUsSubjectLb").change(function(){
		
		if($(this).val() == "1") {
			$("#contactuspopup .contactUsOrderNumberLb").css("display","block");
			$("#contactuspopup .contactUsProductNumberLb").css("display","none");
			
		} else if($(this).val() == "4"){
			$("#contactuspopup .contactUsProductNumberLb").css("display","block");
			$("#contactuspopup .contactUsOrderNumberLb").css("display","none");
			
		}else{
			$("#contactuspopup .contactUsOrderNumberLb").css("display","none");
			$("#contactuspopup .contactUsProductNumberLb").css("display","none");
			
		}
	});	
	
});

