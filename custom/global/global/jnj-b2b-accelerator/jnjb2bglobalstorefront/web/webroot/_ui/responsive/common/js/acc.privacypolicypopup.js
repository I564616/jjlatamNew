var ACC5 = {};
ACC5.privacypolicypop = {
	bindAll : function() {
		this.bindusefullinkspopupLink($('.usefullinks_hn'));
		this.bindprivacypolicypopupLink($('.privacypolicypopup_hn'));
		this.bindlegalnoticepopupLink($('.legalnoticepopup_hn'));
		this.bindtermconditionpopupLink($('.termconditionpopup_hn'));
		this.bindcookiepolicypopupLink($('.cookiepolicypopup_hn'));
	},
	bindprivacypolicypopupLink : function(link) {
		
		 var anchorHrefValue = $('#legal').attr('href');
	        	if(anchorHrefValue)
	        		{
	        		if($("#pdfPopupGenration").val()=="false"){
	        				$.get(ACC.config.contextPath + "/privacyPolicy").done(
	        						function(data) {
	        							$.colorbox({
	        								html : data,
	        								height : '510px',
	        								width : '600px',
	        								overlayClose : false,
	        								onComplete : function() {
	        									$("#closePop").click(function() {
	        										$.colorbox.close();
	        									});
	        								}
	        							});
	        						});
	        		}else{
	        			jQuery.ajax({
	        				url: ACC.config.contextPath + '/GTPrivacyPolicy',
	        				type : "GET",
	        				success: function (data) {
	        					window.open(data);
	        				}
	        				
	        			});
	        		}
	        		}
		link.click(function() {
    		if($("#pdfPopupGenration").val()=="false"){
			$.get(ACC.config.contextPath + "/privacyPolicy").done(
					function(data) {
							
						$('#privacypopupNew').remove();	
						$('#privacypolicypopuopholder').html(data);
						$('#privacypopupNew').modal('show');
						$('#privacypopupNew').css("z-index","2000");
		
					});
    		}else{
    			jQuery.ajax({
    				url: ACC.config.contextPath + '/GTPrivacyPolicy',
    				type : "GET",
    				success: function (data) {
    					window.open(data);
    				}
    				
    			});
    				
    		}
			
		});
		
	},

	bindcookiepolicypopupLink: function(link)
	{
		$("#cookiepopup").modal('show');
		link.click(function()
		{
			jQuery.ajax({
				url: ACC.config.contextPath + '/GTCookiePolicy',
				type : "GET",
				success: function (data) {
					window.open(data);
				}
				
			});
		});
	},
			
		bindusefullinkspopupLink : function(link) {
		
		 var anchorHrefValue = $('#legal').attr('href');
	        	if(anchorHrefValue)
	        		{
	        		if($("#pdfPopupGenration").val()=="false"){
	        				$.get(ACC.config.contextPath + "/usefulLinks").done(
	        						function(data) {
	        							$.colorbox({
	        								html : data,
	        								height : '510px',
	        								width : '600px',
	        								overlayClose : false,
	        								onComplete : function() {
	        									$("#closePop").click(function() {
	        										$.colorbox.close();
	        									});
	        								}
	        							});
	        						});
	        		}else{
	        			jQuery.ajax({
	        				url: ACC.config.contextPath + '/GTUsefulLinks',
	        				type : "GET",
	        				success: function (data) {
	        					window.open(data);
	        				}
	        				
	        			});
	        		}
	        		}
		link.click(function() {
   		if($("#pdfPopupGenration").val()=="false"){
			$.get(ACC.config.contextPath + "/usefulLinks").done(
					function(data) {
							
						$('#usefulLinkspopupNew').remove();	
						$('#usefulLinkspopupholder').html(data);
						$('#usefulLinkspopupNew').modal('show');
						$('#usefulLinkspopupNew').css("z-index","2000");
		
					});
   		}else{
   			jQuery.ajax({
   				url: ACC.config.contextPath + '/GTUsefulLinks',
   				type : "GET",
   				success: function (data) {
   					window.open(data);
   				}
   				
   			});
   				
   		}
			
		});
		
	},bindlegalnoticepopupLink: function(link)
	{
		
		link.click(function()
		{ 
			if($("#pdfPopupGenration").val()=="false"){
			$.get(ACC.config.contextPath + "/legalNotice").done(
					function(data) {
							
						$('#legalnoticepopupNew').remove();	
						$('#legalnoticepopupholder').html(data);
						$('#legalnoticepopupNew').modal('show');
		
					});
			}else{
				jQuery.ajax({
    				url: ACC.config.contextPath + '/GTLegalNotice',
    				type : "GET",
    				success: function (data) {
    					window.open(data);
    				}
    				
    			});
			}
		});
		
		
	},bindtermconditionpopupLink: function(link)
	{
		link.click(function(){
			if($("#pdfPopupGenration").val()=="false"){
			$.get(ACC.config.contextPath + "/termsAndConditions").done(
					function(data) {
							
						$('#tncpopupNew').remove();	
						$('#termsandconditionsholder').html(data);
						$('#tncpopupNew').modal('show');
		
					});
			}else{
				jQuery.ajax({
					url: ACC.config.contextPath + '/GTTermsAndCondition',
					type : "GET",
					success: function (data) {
						window.open(data);
					}
				});
			}
			
		});
		}
	
};

function getUpdatePolicyPopup() {
	if ($('#updatedLegalPolicy').val() == "false") {
		
				$.get(ACC.config.contextPath + "/updatedPrivacyPolicy").done(
						function(data) {
							
							$('#firstloginpopup').remove();	
							$('#updateprivacypopupholder').html(data);
							ACC5.privacypolicypop.bindAll();
							$('#firstloginpopup').modal('show');
							$(".first-login-cancel").attr("href",$("a.usr-action-link-signout").attr("href"));
							$("#privacyPolicyUpdateBtnYes").on('click',function(e) {
										e.preventDefault();
										
										var updateLegalPrivacyVersion = ACC.config.contextPath
												+ "/home/updateLegalPrivacyPolicy";
										var homePageUrl = ACC.config.contextPath
												+ "/home";
										$("#agree-check").click(function() {
											$("#privacyPolicyBlockValidation").css("display","none");	
										});

										
										if ($("#agree-check").is(':checked')) {
											$.ajax({
														type : "POST",
														url : updateLegalPrivacyVersion,
														success : function(response) {
															if (response) {
																window.location.href = homePageUrl
																		+ "?firstTimeLogin=true";
																
															} else {
																window.location.href = homePageUrl;
																// Currently
																// navigating
																// to
																// Home
																// Page
																// in
																// case
																// of
																// error.
															}
														}
													});
										} else {
											$("#privacyPolicyBlockValidation").css("display","block");
													$("#checkPrivacyPolicyBlock .registerError")
													.removeClass(
															"hidden");
											return false;
										}
									});	
							
							
							
							
							
							
							$('.privacypolicypopupFromUpdate').on('click',function(e) {$.get(ACC.config.contextPath+ "/privacyPolicy").done(
														function(data) {
															$('#privacypopupNew').remove();	
															$('#privacypolicypopuopholder').html(data);
															$('#privacypopupNew').modal('show');
															$('#privacypopupNew').css("z-index","2000");
														});
									});
							
							
							
						
							$(".first-login-cancel").addClass("marginRight");
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							/*$
									.colorbox({
										html : data,
										height : 'auto',
										width : '500px',
										escKey : false,
										overlayClose : false,
										onComplete : function() {
											$('#cboxClose').attr("style",
													"display:none;");

											// Enabling/Disabling 'Yes' Button.
											$("#privacyPolicyAgreement")
													.on(
															'change',
															function() {
																if ($(
																		"#privacyPolicyAgreement")
																		.is(
																				':checked')
																		&& ($(
																				"#checkPrivacyPolicyBlock .registerError")
																				.attr(
																						"class")
																				.indexOf(
																						"hidden") == -1)) {
																	$(
																			"#checkPrivacyPolicyBlock .registerError")
																			.addClass(
																					"hidden");
																}
															});

											// Enabling Version Update
											// Functionality on click of 'Yes'
											// Button
											$("#privacyPolicyUpdateBtnYes")
													.on(
															'click',
															function(e) {
																e
																		.preventDefault();
																var updateLegalPrivacyVersion = ACC.config.contextPath
																		+ "/home/updateLegalPrivacyPolicy";
																var homePageUrl = ACC.config.contextPath
																		+ "/home";
																if ($(
																		"#privacyPolicyAgreement")
																		.is(
																				':checked')) {
																	$
																			.ajax({
																				type : "POST",
																				url : updateLegalPrivacyVersion,
																				success : function(
																						response) {
																					if (response) {
																						window.location.href = homePageUrl
																								+ "?firstTimeLogin=true";
																					} else {
																						window.location.href = homePageUrl;
																						// Currently
																						// navigating
																						// to
																						// Home
																						// Page
																						// in
																						// case
																						// of
																						// error.
																					}
																				}
																			});
																} else {
																	$(
																			"#checkPrivacyPolicyBlock .registerError")
																			.removeClass(
																					"hidden");
																	return false;
																}
															});
											// Enabling 'No' Button href
											$("#privacyPolicyUpdateBtnNo")
													.attr(
															"href",
															$("a.icon-logout")
																	.attr(
																			"href"));
											$("#privacyPolicyUpdateBtnNo")
													.addClass("marginRight");
											// Enabling Privacy Policy Link
											$('.privacypolicypopupFromUpdate')
													.on(
															'click',
															function(e) {
																$
																		.get(
																				ACC.config.contextPath
																						+ "/privacyPolicy")
																		.done(
																				function(
																						data) {
																					$
																							.colorbox({
																								html : data,
																								height : '510px',
																								width : '600px',
																								overlayClose : false,
																								onComplete : function() {
																									$(
																											'#cboxClose')
																											.attr(
																													"style",
																													"display:block;");
																									$(
																											'#closePop')
																											.remove();
																								},
																								onClosed : function() {
																									getUpdatePolicyPopup();
																								}
																							});
																				});
															});
										}
									});*/
						});
	}
}
$(document).ready(function() {
	ACC5.privacypolicypop.bindAll();
	getUpdatePolicyPopup();
	
});