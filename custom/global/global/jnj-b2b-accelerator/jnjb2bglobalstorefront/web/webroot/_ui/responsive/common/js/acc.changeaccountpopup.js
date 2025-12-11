$(document)
.ready(
		function() {
			
			var currAccScreenName = $("#currAccScreenName").val();
			$(".changeAccount").click(function() {

				var dataObj = new Object();
				createChangeAccountAjaxCall(dataObj, "");
			});

			$(document).keypress(function(e) {
				if (e.which == 13) {
					searchAccountAjax();
				}
			});
			
			//Changes for Change Account Popup on login
			$('#changeAccountNoAjax').typeahead({

				source: function (query, process) {
					return $.get(ACC.config.contextPath +'/home/autocompleteAccount',  {
						term : $('#changeAccountNoAjax').val() 
					}, function (data) {
						return process(data);
					});
				},
				minLength: 3,
				autoFocus: false
				//menuClass:
			});
			
						
			function searchAccountAjax() {
				if (null != $("#changeAccountNoAjax").val()
						&& $.trim($("#changeAccountNoAjax").val()) != "") {
					$("#lodCircleID").show();
					var dataObj = new Object();
					dataObj.searchTerm = $.trim($(
							"#changeAccountNoAjax").val());
					createChangeAccountAjaxCall(dataObj, "");
				}
				
			}

			var mobiaccountSelected=false;
			
			searchBtnClick();
			function searchBtnClick() {
				$(document).on('click','#searchSelectAccountBtnHeader', function(e) {
					loadingCircleShow('show');
					
					searchAccountAjax();
				});
				
				$('#select-accnt-errorMobile').hide();
				var anchrClicked = false;
				$('#select-accnt-error').hide();
				$(document).on('click',
						'#selectaccountpage .anchorcls', function(e) {
					// e.preventDefault();
					anchrClicked = true;
					$('#select-accnt-error').hide();
					$('#select-accnt-errorMobile').hide();
					$('.anchorcls').each(function() {
						$(this).removeClass('anchor-selected');
					});
					$(this).addClass('anchor-selected');
//					changeAcntList();
				});
				
				$(document).on('click', '.changeAccountUnit',function(e) {
					e.preventDefault();
					// $('#change-select-btn').click(function(){
					//calling newSession of GTM container
					var googleAccNumber = $(this).find("div.code").text().trim();
					if (null !== googleAccNumber && "" != googleAccNumber) {
						if(typeof newSession !== "undefined") {
							newSession(googleAccNumber);
						} 
					}
					var changeAccountForm = jQuery(
							'<form>',
							{
								'action' : ACC.config.contextPath
								+ '/home/changeAccount',
								'id' : 'changeAccountForm',
								'method' : 'POST'
							})
							.append(
									jQuery(
											'<input>',
											{ // Account UID input created
												'name' : 'uid','value' : $(
														$(this).find("#accountUID:input[type='hidden']"))
																		.val(),
																		'type' : 'hidden'
							}))
							.append(
									jQuery(
											'<input>',
											{  // Account Name input created
												'name' : 'accountName','value' : $(
														$(this).find("#accountName:input[type='hidden']"))
																		.val(),'type' : 'hidden'
											}))
							.append(
									jQuery(
											'<input>',
											{ // Account GLN input created
												'name' : 'accountGLN',
												'value' : $(
														$(this).find("#accountGLN:input[type='hidden']"))
																		.val(),
																		'type' : 'hidden'
											}))
								.append(
										jQuery(
												'<input>',
												{ 
													'name' : 'CSRFToken',
													'value' : ACC.config.CSRFToken,
													'type' : 'hidden'
												}));

					$("#changeAccountForm").remove();
					$("#changeAccountPopup").append(
							changeAccountForm);
					/** Submitting the dummy form * */




					if (anchrClicked && currAccScreenName=="desktop") {
						$("#changeAccountForm").submit();
					}
					else if(currAccScreenName=="mobile" || currAccScreenName=="tablet"){
						mobiaccountSelected=true;
					}
					else {
						$('#select-accnt-error').show();
						$('#select-accnt-errorMobile').show();
					}
				});
				
				
				
				
				$(document).on('click', '.change-accnt-link',function(e) {
					loadingCircleShow("show");
					$('.loadcircle').show();
					$('.modal-backdrop').show();
//					changeAcntList();
				});
			}
			
			$(document).on('click', '#change-select-btn',function(e) {
				if((mobiaccountSelected && currAccScreenName=="mobile") || (mobiaccountSelected && currAccScreenName=="tablet") ){
					$("#changeAccountForm").submit();
				}
				else {
					$('#select-accnt-error').show();
					$('#select-accnt-errorMobile').show();
				}
			});
			
			//AAAOL-5857
				$(document).on('click','#clearSelectAccountBtn',function(e){
				$("#changeAccountNoAjax").val("");
				var dataObj = new Object();
				dataObj.searchTerm = "";
				createChangeAccountAjaxCall(dataObj, "");
			});

			function changeAccountOnCompleteAction() {
				$("#changeAccountNoAjax").keyup(function(e) {
					var key = e.which;
					if (key == 13) {// the enter key code
						$("#searchSelectAccountBtn").click();
					}
				});

				/** change account functionality on click of link * */

			}

			function createChangeAccountAjaxCall(dataObj, pageNumber) {

				if (null != pageNumber && pageNumber != "") {
					dataObj.showMoreCounter = pageNumber;
					dataObj.showMore = "true";
				} else {
					dataObj.showMoreCounter = "1";
					dataObj.showMore = "false";
				}
				dataObj.showMode = "Page";
				dataObj.page = "0";
				var escapeKeyHide = false;
				var overlayHide = false;

				if (isFirstTimeModal) {
					dataObj.addCurrentB2BUnit = "true";
					escapeKeyHide = false;
					overlayHide = false;
				}

				
				function getAccounts() {
				}
				jQuery
				.ajax({
					type : "POST",
					data : dataObj,
					url : ACC.config.contextPath
					+ ACC.config.currentLocale
					+ '/home/getAccounts',
					success : function(data) {
						loadingCircleShow("hide");
						$("#lodCircleID").hide();
						//$('.modal-backdrop').hide();
						$('#selectaccountpage').html(data);

						$('#select-accnt-error').hide();
						$('#select-accnt-errorMobile').hide();
						loadingCircleShow("show");
						$('#selectaccountpopup').modal('hide');
						loadingCircleShow("show");
						$('.modal-backdrop').hide();
						
						$('#selectaccountpopup').modal('show');
						
						$('#changeAccountNoAjax').val(dataObj.searchTerm);
						
						$('.clsBtn').click(function() {
							$('.modal-backdrop').hide();
						});
						//Changes for Change account popup AAOL -4795 
						
						$('#changeAccountNoAjax').typeahead({

							source: function (query, process) {
								return $.get(ACC.config.contextPath +'/home/autocompleteAccount',  {
									term : $('#changeAccountNoAjax').val() 
								}, function (data) {
									return process(data);
								});
							},
							minLength: 3,
							autoFocus: false
							//menuClass:
						});
												
						
						setTimeout(function() {
							//searchBtnClick();
						}, 3000);

						/** Creating the markup for the popup * */
						
						var totalPages = $(
						"#accountNumberOfPages").val();
						$("#changeAccountPopupContainer")
						.show();
						// $.colorbox.resize();
						$("#changeAccountNoAjax")
						.change(
								function() {
									if ((null == $(
											"#accountSearchTerm")
											.val() || $
											.trim($(
													"#accountSearchTerm")
													.val()) == "")
													&& $(this)
													.val() != null
													&& $
													.trim($(
															this)
															.val()) != "") {
										$(
										"#clearSelectAccountBtn")
										.attr(
												"disabled",
												false);
										$(
										"#clearSelectAccountBtn")
										.removeClass(
										"tertiarybtn");
										$(
										"#clearSelectAccountBtn")
										.addClass(
										"primarybtn");
									} else if ((null == $(
									"#accountSearchTerm")
									.val() || $
									.trim($(
											"#accountSearchTerm")
											.val()) == "")
											&& ($(this)
													.val() == null || $
													.trim($(
															this)
															.val()) == "")) {
										$(
										"#clearSelectAccountBtn")
										.attr(
												"disabled",
												true);
										$(
										"#clearSelectAccountBtn")
										.removeClass(
										"primarybtn");
										$(
										"#clearSelectAccountBtn")
										.addClass(
										"tertiarybtn");
									}
								});
					
						if (isFirstTimeModal) {
							/**
							 * Hiding the close button on the
							 * colorbox *
							 */
							$('#cboxClose').attr("style",
							"display:none;");
							// changeAccountOnCompleteAction(true);
						} else {
							// changeAccountOnCompleteAction(false);
						}
						if (totalPages > 1) {
							$("a.loadMoreAccounts")
							.attr("style",
							"display:block;text-align:center");
							$("a.loadMoreAccounts")
							.click(
									function() {
										var searchTerm = $(
												"#accountSearchTerm")
												.val();
										var loadMoreObject = new Object()
										if (null != searchTerm
												&& searchTerm != "") {
											loadMoreObject.searchTerm = searchTerm;
										}
										createChangeAccountAjaxCall(
												loadMoreObject,
												parseInt(
														$(
																"#currentPage")
																.val(),
																10) + 1);
									});
							//$.colorbox.resize();
						} else {
							$("a.loadMoreAccounts")
							.attr("style",
							"display:none;text-align:center");
						}
						/** Clear button click action * */
						$(".clearSelectAccountBtn").click(
								function() {
									// createChangeAccountAjaxCall(new
									// Object(), "");
								});

						
					},

					error : function(x, e) {
						/**
						 * jjepic-313 push to login page when
						 * session times out *
						 */
						var status = "" + x.status;
						if (status.startswith("9")
								|| x.status == 901) {
							window.location.href = acc.config.contextPath
							+ "/";
						}
					}

				});
			}

			
		

var isFirstTimeModal = ($(".accountListPopUp").size() > 0 && $(
'#updatedLegalPolicy').val() != "false");

			
			var currAccScreenName = $("#currAccScreenName").val();

			if ($(".accountListPopUp").size() > 0
					&& $('#updatedLegalPolicy').val() != "false") {

				/** Color box for the change account Popup * */
				
				/** Hiding the close button on the colorbox * */
				$('#cboxClose').attr("style", "display:none;");
				$("#changeAccountNoAjax")
				.change(
						function() {
							if ((null == $("#accountSearchTerm")
									.val() || $
									.trim($(
											"#accountSearchTerm")
											.val()) == "")
											&& $(this).val() != null
											&& $.trim($(this).val()) != "") {
								$("#clearSelectAccountBtn")
								.attr("disabled", false);
								$("#clearSelectAccountBtn")
								.removeClass(
								"tertiarybtn");
								$("#clearSelectAccountBtn")
								.addClass("primarybtn");
							} else if ((null == $(
							"#accountSearchTerm").val() || $
							.trim($(
							"#accountSearchTerm")
							.val()) == "")
							&& ($(this).val() == null || $
									.trim($(this).val()) == "")) {
								$("#clearSelectAccountBtn")
								.attr("disabled", true);
								$("#clearSelectAccountBtn")
								.removeClass(
								"primarybtn");
								$("#clearSelectAccountBtn")
								.addClass("tertiarybtn");
							}
						});
				
				if ($("#accountNumberOfPages").val() > 1) {
					$("a.loadMoreAccounts").attr("style",
					"display:block;text-align:center");
					$("a.loadMoreAccounts")
					.click(
							function() {
								var searchTerm = $(
										"#accountSearchTerm")
										.val();
								var loadMoreObject = new Object();
								if (null != searchTerm
										&& searchTerm != "") {
									loadMoreObject.searchTerm = searchTerm;
								}
								createChangeAccountAjaxCall(
										loadMoreObject,
										parseInt($(
												"#currentPage")
												.val(), 10) + 1);
							});
					//$.colorbox.resize();
				} else {
					$("a.loadMoreAccounts").attr("style",
					"display:none;text-align:center");
				}
				// changeAccountOnCompleteAction(true);
				/** Clear button click action * */
				$(".clearSelectAccountBtn").click(function() {
					$("#changeAccountNoAjax").val("");
//					
				});
				
			}
			

		});

/*Changes for AAOL - 4796*/
$(document)
.on(
		'click',
		'.accSearch .dropdown-item',
		function() {
			
			var currAccScreenName = $("#currAccScreenName").val();
			
			
			var index=$('.accSearch .dropdown-item').index(this);
			$('#select-accnt-error').hide();
			$('#select-accnt-errorMobile').hide();
			$('.anchorcls').each(function() {
				$(this).removeClass('anchor-selected');
			});
			/**
			 * Creating dummy form for
			 * submission for account change
			 * request *
			 */
			var changeAccountForm = jQuery(
					'<form>',
					{
						'action' : ACC.config.contextPath
						+ '/home/changeAccountAutoSuggest',
						'id' : 'changeAccountForm1',
						'method' : 'POST'
					})
					.append(
							jQuery(
									'<input>',
									{'name' : 'uid',
										'value' : $(this).text().trim(''),
										'type' : 'hidden'}))
										.append(
												jQuery(
														'<input>',
														{
															'name' : 'CSRFToken',
															'value' : ACC.config.CSRFToken,
															'type' : 'hidden'
														}));

			$("#changeAccountForm1").remove();
			$("#changeAccountPopup").append(
					changeAccountForm);
			$("#changeAccountForm1").submit();
			/** Submitting the dummy form * */

		});

function loadingCircleShow(option)
{
	
	if(option=="show"){
		 $("#laodingcircle").show();
	}else if(option=="hide"){
		
	 $("#laodingcircle").hide();
	}
	
}
