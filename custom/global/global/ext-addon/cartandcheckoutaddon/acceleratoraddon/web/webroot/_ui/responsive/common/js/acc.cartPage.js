jQuery(document).ready(function() {	
	// refreshSapPrices();
	
	/** Display Batch mode alert in case Order-Simulation fails */
	if($("#displayBatchModeAlert").val()=='true'){
		batchModePopUp(true);
	}
	
	 if($("#errorInvoiceMessage").val()=='true'){
	invoicePDFPopUp(true);
       }
	   
	   	   
 //Adding for JJEPIC-720
	 
			$("#UploadIt").click(function(e){
				 e.preventDefault();
				$('#uploadDeliveredOrder').modal('show');
				 $(".invalidFileError").hide();
				 $(".emptyFileError").hide();
				 
			 });
	   
	   
	   
	    //Added JJEPIC-720 Starts
	 if ($("#uploadBrowseFile").val() == "") {
			$("#removeUploadForm").hide();
		} else {
			$("#uploadBrowseFile").css("width", "auto");			
			$(".invalidFileError").hide();
			$("#removeUploadForm").show();
			
		}

		if ($("#attachDocName").val() != "") {
			$("#uploadBrowseFile").css("width", "85px");			
			$(".invalidFileError").hide();
			$("#removeUploadForm").show();

		}
		
	

		$("#uploadBrowseFile").change(function() {
			if ($("#uploadBrowseFile").val() == "") {
				$("#removeUploadForm").hide();
			} else {
				$("#uploadBrowseFile").css("width", "auto");				
				$(".emptyFileError").hide();
				$("#removeUploadForm").show();
				$("#attachDocNameSpan").hide();

			}
		});
		$("#uploadFileCancel").click(function() {
			if ($("#uploadBrowseFile").val() != "") {
				$("#removeUploadForm").hide();
				$("#submitDelivedOrderFileForm").reset();
			} else if ($("#attachDocName").val() != "") {
				$("#uploadBrowseFile").css("width", "85px");
				$("#attachDocNameSpan").show();				
				$(".invalidFileError").hide();
				$("#removeUploadForm").show();
			}
		});
		
		
		
		$("#removeDoc").click(function() {
			if ($("#uploadBrowseFile").val() != "") {
				$("#submitDelivedOrderFileForm").reset();
				$("#removeUploadForm").hide();
			} else {
				$("#uploadBrowseFile").css("width", "auto");
				$("#attachDocNameSpan").hide();
				$("#removeUploadForm").hide();
			}
			//Added JJEPIC-720 Ends
			});
		
		//loadContractForm: function() {
		var idx = 0;
		$(".shoppingcartOrderEntryList").each( function() {
				var myContractRowId = $(this).attr('id'); 
				//$("#"+myContractRowId).find("p .contractNumber").html();
				var contractNumber = $(this).find("p .contractNumberText").html();
					
				if(contractNumber !== undefined && contractNumber !== null && contractNumber ){
					idx = parseInt(idx)+1;
				} 
				if(idx > 0){  // to enable the contract no in top of the page if atleast one contract product
					$(".contract-product-show").show(); 
					$("#contract_product_msg").text(contractNumber);
				}
			});
		//}
		
		/*3088*/
		$('#searchSelectAddBtn').click(function(e)  {
			$('#selectaddresspopup').removeData();
			var dataObj = new Object();
			createShipToAddressAjaxCall(dataObj, "");
			
		});
		/*3088*/
		$('#searchSelectAddBtn1').click(function(e)  {
			$('#selectaddresspopupBillTO').removeData();
			var dataObj = new Object();
			createBillToAddressAjaxCall(dataObj, "");
		});
});

if($("#distPurExpOrderDate").length!=0){
	
	var maxDateForDeliveryDate;
	var todaysDate = new Date();
	var futureDate = new Date();
	futureDate.setDate(todaysDate.getDate()+2);
	
	if (futureDate.getDay() == 0) {
		maxDateForDeliveryDate = "3d";
	} else if (futureDate.getDay() == 6) {
		maxDateForDeliveryDate = "4d";
	} else {
		maxDateForDeliveryDate = "2d";
	}

	
	
	
	

	
	$("#distPurExpOrderDate").datepicker({
		minDate: maxDateForDeliveryDate,
	    inline: true,
	    beforeShowDay: $.datepicker.noWeekends,
	});
	
	/** JIRA 362 : Below code sets the date to 10 days from current date if the saved date turns out before 10 days range **/ 
	futureDate.setHours(0,0,0,0);
	if(Date.parse($("#distPurExpOrderDate").val()) < futureDate) {
		$("#distPurExpOrderDate").datepicker("setDate", futureDate);
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/updateDeliveryDateForCart?expDeliveryDate='+$("#distPurExpOrderDate").val(),
			async: false,
			success : function(data) {	}
		});
	}
}
$('.cartStep1Saveupdate').click(
		function(e) {
			$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
			$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
			var surgeryData = $("#surgeryData").val();
			var surgeryMand = false;
			var splStockPartnerRequired = false;
			if(surgeryData != undefined){
				// means surgery info is mandatory
				if(surgeryData == ""){
					$("#surgeryInfoError").show();
					surgeryMand = true;
				}
			}
			
			if($("#dropShip").val() != null && $("#dropShip").val().length!=0)
			{
				$("#stndDistPurOrder")[0].setAttribute("required", "true");
			}
			
			var lotNoMandatory = false;
			if($(".orderEntry").length != 0){
			    $('.orderEntry').each(function() {  
			        if(!jQuery(this).valid()){
			        	lotNoMandatory = true;
			        }
			    });				
			}
			if($(".splStockPartnerForm").length != 0){
			    $('.splStockPartnerForm').each(function() {	    	
			    	
			    	var formIdArray= $(this).attr('id').split("_");
			    	var entryNumber=formIdArray[1];
			    	//Remove existing errors
			    	$("#"+entryNumber+"_Error").remove();
			    	
			        if(!jQuery(this).valid()){
			        	splStockPartnerRequired = true;
			        }
			    });				
			}
						
			if (!jQuery("#cartStep1Check").valid() || lotNoMandatory || surgeryMand || splStockPartnerRequired) {
				$("#ajaxCallOverlay").hide(); // overlay must fade out
				$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
				$('#myprofileedit .success').hide();
			}
			else{


														  /*GTR-1693*  Commented and added new else block completely/

										/* If any error occurs then keep this commented code inside else block till {search:Till 'A'}
										var canValidate = $("#canValidateCart").val() ;
										if(canValidate == 'false'){
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											batchModePopUp(false);
										}else{ //Cart can be validate
											var poNum=$("#purchOrder").val();
											jQuery.ajax({
												global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
												url : ACC.config.contextPath + '/cart/checkPO?poNumber='
														+ encodeURIComponent(poNum),
												success : function(data) {
													if (data) {
														$("#ajaxCallOverlay").hide(); // overlay must fade out
														$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
														showValilatePONumAlert(e)
													} else {
														window.location.href = ACC.config.contextPath
																+ '/cart/validate';
													}
												}
											});
										}//close of else
										*/





			
					var canValidate = $("#canValidateCart").val() ;
				if(canValidate == 'false')
					{
					 
					checkForPOAlert(e);		
					//** 
				
					/*$("#ajaxCallOverlay").hide(); // overlay must fade out
					$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
					batchModePopUp(false);*/
				}else{ //Cart can be validate
					
						
					var poNum=$("#purchOrder").val();
					jQuery.ajax({
						global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
						url : ACC.config.contextPath + '/cart/checkPOValid?poNumber='
								+ encodeURIComponent(poNum),
						success : function(data) {
							if (data) {
								 
								$("#ajaxCallOverlay").hide(); // overlay must fade out
								$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
								showValilatePOOrderNumAlert(e);
								
							} else { 
							
								
								
								/*//Added for GTR-1693
*/								jQuery.ajax({
									global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
									url : ACC.config.contextPath + '/cart/checkPO?poNumber='
											+ $("#purchOrder").val(),
									success : function(data) 
									{							
										if (data) 
										{ 
										
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											showValilatePONumAlert(e);
										} 
										else 
										{   
											
											window.location.href = ACC.config.contextPath+ '/cart/validate';
										}
									}
								});
							}
							
							//Added
								/*window.location.href = ACC.config.contextPath
										+ '/cart/validate';
							}*/
						}
					});
				}//close of else
			}//Till 'A'
		}); 

$('.shippingMethodSelect').change(function(e) {	
			jQuery.ajax({
				url : ACC.config.contextPath + '/cart/updateShippingMethod?route='
						+ $(this).val()+'&entryNumber='+$(this).attr('data'),
				async: false,
				success : function(data) {	}
			});
});

$('.reasonCodeSelect').change(function(e) {	
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCode?reasonCode='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});


$('.requestDeliveryDate').change(function(e) {	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateDeliveryDateForCart?expDeliveryDate='+$(this).val(),
		async: false,
		success : function(data) {	}
	});
});


$('.showChagneOrderPopup').click(function(e) {
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getOrderType?currentOrderType='+$("#currentOrderType").val(),
		type: 'GET',
		content: 'text/html',
		success : function(data) {
			showChangeOrderTypePopUp(e,data)
		}
	
	});
});



var hospitalId = '';

	
	$(document).on("click",'#surgeonRadio3',function(e){
		$(".surge-inputbox").attr("readonly",false);
	});
	
	$(document).on("click",'#surgeonRadio',function(e){
		$(".surge-inputbox").attr("readonly",true);
	});
	
	var listVal;	
	


$('.showPriceOverridePopup').click(function(e) {
	var basePrice= $(this).attr('id').split("_");
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getPriceOverridePopUp?entryNumber='+ basePrice[1],
		type: 'GET',
		content: 'text/html',
		success : function(data) {
			showPriceOverridePopup(e,data)
		}
	
	});
});

function showPriceOverridePopup(e,data) {
	e.preventDefault();
	$.colorbox({
		html : data,
		height : '405px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#priceOverrideCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			jQuery("#priceOverrideForm").validate(
					{

						rules : {
							overridePrice : {
								number : true,
								priceOverRideMax : true,
								priceOverRideMin : true
							}
						},
						messages : {
							number : "only Numbers!"
						},
						errorPlacement : function(error, element) {
							error.appendTo(element.parent().parent().parent()
									.find('div.registerError'));
						},
						onfocusout : false,
						focusCleanup : false
					});
		}
	});
}

function showChangeOrderTypePopUp(e,data) {
	e.preventDefault();
	$.colorbox({
		html : data,
		height : '405px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#changeOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});	
			$("#submitChangeOderType").click(function(e) {				
				if ($('input[name=orderType]:checked').length > 0) {
					$("#changeOrderTypeForm").submit();
				}		
				else{
					$("#changeOrderTypeMessageId").show();
					e.preventDefault();
				}
			});	
		}
	});
	$('#changeOrderType').css('display', 'block');
}

$('#third-party-checkbox').click(function(){
	if($(this).is(':checked')){
		
		$('.third-party-content').css("display","table");
	}
	else{
		$('.third-party-content').hide()
	}
})

if($('#third-party-checkbox').is(':checked')){
	
	$('.third-party-content').css("display","table");
}
else{
	$('.third-party-content').hide()
}



$('#showSurgeryInfoPopup').click(function(e) {
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getSurgeryInfo',
		type: 'GET',
		content: 'text/html',
		success : function(data) {			
			//showSurgeryInfoPopup(e,data)
			$('#surgeryInfoPopupHolder').html(data);
			$('#surgeryInfoPopup').modal('show');
			$('.selectpicker').selectpicker('refresh');
			$('.date-picker').datepicker();
			$('.date-picker').on('changeDate', function(ev){
			    $(this).datepicker('hide');
			});
		}
	
	});
});

$('#showSurgeryInfoPopupReview').click(function(e) {
	e.preventDefault();

	$('#cartSurgeryInfoPopup').modal('show');
	$('.selectpicker').selectpicker('refresh');
	$('.date-picker').datepicker();

});

$('#showSurgeryInfoPopupConfirm').click(function(e) {
	e.preventDefault();

	$('#orderSurgeryInfoPopup').modal('show');
	$('.selectpicker').selectpicker('refresh');
	$('.date-picker').datepicker();

});


function showSurgeryInfoPopup(e,data) {
	e.preventDefault();
	
}

function showValilatePONumAlert(e) {
	
	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderDivId").html(),
		height : '180px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {

			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			$("#validateOrderOk").click(function() {
				$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in
														// to 0.6 opacity
				$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner
														// must fade in to 0.6
														// opacity
				$("#cartStep1Saveupdate").submit();
			});
		}
	});
	$('.validateOrderDivId').css('display', 'block');
}

function showValilatePOOrderNumAlert(e) {

	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderNumDivId").html(),
		height : '220px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			
			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
		}
	});
	$('.validateOrderNumDivId').css('display', 'block');
}

function showPONumAlertBeforeBatch(e) {

	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderDivId").html(),
		height : '180px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {

			$("#validateOrderOk").prop('href', 'javascript:;');

			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			$("#validateOrderOk").click(function() {
				batchModePopUp(false);
			});
		}
	});
	$('.validateOrderDivId').css('display', 'block');
}


$('#editFreightCostLink').click(function(e) {
	$.colorbox({
		html : $("#editFreightLightBox").html(),
		height : '305px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#validateOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
			$("#validateOrderOk").click(function() {
				$("#cartStep1Saveupdate").submit();
			});
		}
	});
});


$('.enterOneTimeShipToAddress').click(function(e) {
    jQuery.ajax({
           url : ACC.config.contextPath + '/cart/enterOntimeShippingAddress',
           type: 'GET',
           content: 'text/html',
           success : function(data) {
                  showEnterOneTimeShippingaddressPopUp(e,data)
           }
    
    });
});

$('.removeOneTimeShipToAddress').click(function(e) {
    jQuery.ajax({
           url : ACC.config.contextPath + '/cart/removeOneTimeShipToAddress',
           type: 'GET',
           content: 'text/html',
           success : function(data) {
        	   $("#deliveryAddressTag .companyName").text(data.companyName!= null ?data.companyName:"");
        	   $("#deliveryAddressTag .lineOne").text(data.line1 != null ?data.line1:"");
        	   $("#deliveryAddressTag .lineTwo").text(data.line2!= null ?data.line2:"");
        	   if(null!=data.country){
        		   $("#deliveryAddressTag .countryName").text(data.country.name!= null ?data.country.name:"");
        	   }
        	   $(".enterOneTimeShipToAddress").show();
        	   $(".removeOneTimeShipToAddress").hide();
        	   
           }
    
    });
    
});




function showEnterOneTimeShippingaddressPopUp(e,data) {
    e.preventDefault();
    $.colorbox({
           html : data,
           height : '440px',
           width : '565px',
           overlayClose : false,
           onComplete : function() {
	        	 
	        			 jQuery("#oneTimeShippingAddressForm").validate({	        				
	        				 errorPlacement: function(error, element) {
	        					error.appendTo( element.parent().parent().find('div.registerError'));	        					
	        				},
	        				onfocusout: false,
	        				focusCleanup: false
	
	        			});
	        	
	        	   
                  $("#changeOrderCancel").click(function(e) {
                        e.preventDefault();
                        $.colorbox.close();
                  });                  
           }
    });
    $('#enterOneTimeShippingAddress').css('display', 'block');
}

$('.lotNumber').change(
		function() {
			var entryNumber  =  $(this).attr('data');
			var lotNum =  $("#lotNumber_"+entryNumber).val();
			//FIX GTR-1768
			var prodcode =  $("#productCode_"+entryNumber).val();
										 
			if($.trim(lotNum) != ""){
				jQuery.ajax({
					url : ACC.config.contextPath
							+ '/cart/updateLotNumber?lotNumber='
							+ lotNum
							+ '&pcode='+prodcode
							 + '&entryNumber='+ entryNumber,
					success : function(data) {
						//alert("data"+data)
						if (!data) {
							$('#invalidLotNum_'+entryNumber).show();
							$('#lotNumber_'+entryNumber).val(''); 
						} else {
							$('#invalidLotNum_'+entryNumber).hide();
						}
					}
				});
			}
		});
		

$('.poNumber').change(function () {
	var poNumb= $(".poNumber").val();
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updatePONumber?poNumber=' + encodeURIComponent(poNumb) +'&entryNumber='+$(this).attr('data'),
		async: false,
		success: function (data) {
			
			}
	});
});
		
$('.invoiceNumber').change(function () {
	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateInvoiceNumber?invoiceNumber=' + $(".invoiceNumber").val()+'&entryNumber='+$(this).attr('data'),
		success: function (data) {
			if(!data){								
				$('.invalidInvoiceError').show();
				$('.invoiceNumber').val('');	
				}else{
					$('.invalidInvoiceError').hide();
				}
			}
	});
});

/*$(".submitReturn").click(function() {	
	
	$('.invalidLotNum').hide();
	$('.invalidInvoiceError').hide();
			var lotNoMandatory = false;
			if ($(".returnEntriesRequiredForm").length != 0) {
				$('.returnEntriesRequiredForm').each(function() {
					if (!jQuery(this).valid()) {
						lotNoMandatory = true;
					}
				});
			}
			
			if (jQuery("#returnOrderForm").valid()
					&& lotNoMandatory == false) {
		$("#returnOrderForm").submit();		
	}
});	*/


jQuery("#returnOrderForm").validate({
	rules: {		
		reasonCodeReturn: {
			selectedOthers: true
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
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery.validator.addMethod("selectedOthers",
	    function (value, element) { if($("#reasonCodeReturn").val() == "other") return false; else return true; }, $("#ReasonCodeError").val());



$('#spinSalesUCN').change(
		function() {
			window.location.href = ACC.config.contextPath
					+ '/cart/updateSalesRepUCN?salesRepUCN='
					+ $("#spinSalesUCN").val() + '&specialStockPartner='
					+ $("#spinSalesUCN option:selected").attr('data');
		});

$('.specialStockPartner').change(function () {
	var thisObject = $(this);
	var prodid= thisObject.attr('id').split("_");
	prodid=prodid[1];
	var splStockPartner = $('#specialStock_'+prodid).val();
	if($.trim(splStockPartner) != ""){ 
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateSpecialStockPart?entryNumber=' +prodid+'&specialStockPartner='+ splStockPartner,
			async: false,
			success: function (data) {
				if(!data){
					var spclStckNum = $('#specialStock_'+prodid).val();
					$('#specialStock_'+prodid).val("");
					if($("#" + prodid +"_Error").length==0){
						//thisObject.parent().append("<div id='" + prodid + "_Error' class='error marTop5'><p>Special Stock Partner - '" + spclStckNum + "' Invalid!</p></label>");
						thisObject.parent().append("<div id='" + prodid + "_Error' class='error marTop5'><p>"+STOCKPARTNER_NUMBER+"</p></label>");
						$("#" + prodid +"_Error").hide().fadeIn(1000);
						setTimeout(function(){
							$("#" + prodid +"_Error").slideUp(500, function(){
								$("#" + prodid +"_Error").remove();
							});
						},5000);
						thisObject.closest("form").validate().resetForm();
						$('#specialStock_'+prodid).val('');
					}
					$('#specialStock_'+prodid).focus();				
				}
			}
		});
	}
});

$('#thirdParty').change(function () {	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateThirdPartyFlag?thirdPartyFlag=' + $('#thirdParty').prop("checked"),
		success: function (data) {
			if(null != data && data != ""){
				$("#deliveryAddressTag .companyName").text(data.firstName!= null ?data.firstName:"");
				$("#deliveryAddressTag .companyName").append(data.lastName!= null ?" " + data.lastName:"");
				 $("#deliveryAddressTag .lineOne").text(data.line1!= null ?data.line1:"");
				 $("#deliveryAddressTag .lineOne").append(data.line2!= null ?", " + data.line2:"");
				 if(null!=data.country)
				 {
					 $("#deliveryAddressTag .countryName").text(data.country.name!= null ?data.country.name:"");
				 }
				$('#dropShip').val("");
				$('#distPurOrderDis').val("");
			}
		}
	});	
});

$('#customerPo').change(function () {
	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateCustomerPo?customerPo=' + $('#customerPo').val(),
		async: false,
		success: function (data) {
		
			}
	});
});



$('#cordisHouseAccount').change(function () {
	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateCordisHouseAccount?cordisHouseAccount=' + $('#cordisHouseAccount').val(),
		success: function (data) {
			if(data === true){
				$('#cordisHouseAccount').parent().parent().parent().find('div.registerError').html('');
			}
			else{
				$('#cordisHouseAccount').parent().parent().parent().find('div.registerError').html('<label for="cordisHouseAccount" class="error">'+ $("#cordisError").val()+'</label>');
			}
			}
	});
});

$('.reasonCodeNoChargeSelect').change(function(e) {	
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeNoCharge?reasonCodeNoCharge='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});

$('.reasonCodeReturnSelect').change(function(e) {	
	if( $(this).val() == 'R07' || $(this).val() == 'R29')
	{
	$("#invoiceStar").css("display","none");
	}
	else
	{
	$("#invoiceStar").css("display","inline");
	}
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeReturn?reasonCodeReturn='
				+ $(this).val(),
		async: false,
		success : function(data) {	
			
			
		}
	});
});

$(".returnEntriesRequiredForm").each(function (item) {
	var sid= jQuery(this).attr('id');
	jQuery('#'+sid).validate(
		{
			rules : {
				
			},
			showErrors : function(errorMap, errorList) {
				if (errorMap != null && errorList.length != 0) {
					$(".errorSummary").html(
							'<label class="error">'
									+ $("#returnOrderEntryFieldError").val()
									+ '</label>');
				}
				this.defaultShowErrors();
			},
			errorPlacement : function(error, element) {
				error.appendTo(element.parent().parent().find(
						'div.registerError'));
			},
			onfocusout : false,
			focusCleanup : false
		});
});

$.validator.addClassRules("returnEntriesRequired", {
	required:{
		depends: function(){
			if( $("#reasonCodeReturn").val()!='R07' && $("#reasonCodeReturn").val()!='R29' )
				{
				return true;
				}			
			else{
				return false;
			}
		}
	}
});


var toalEntries;
var toalResponse;
function refreshSapPrices(){	
	if($("#refreshSapPrice").val()){		
		var entryNumbers = $('input[name="entryNumber"]');
		toalResponse = 0;
		toalEntries = entryNumbers.length;
		entryNumbers.each(function() {
			getSapPrice($(this).val());
		});
	}

}

function getSapPrice(entryNumber) {	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getSAPPrice?entryNumber='
				+entryNumber,
		async: false,
		type: 'POST',
		success : function(data) {
			$("#basePrice_"+entryNumber).html(data.currencySymbol + data.entryBasePrice);
			$("#totalPrice_"+entryNumber).html(data.currencySymbol + data.entryTotalPrice);
			toalResponse++;
			if(toalEntries == toalResponse){
				alert(ALL_PRICES);
			}
		}
	});
}
function getCartTotal() {	
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/getCartSubTotal',
			async: false,
			type: 'POST',
			success : function(data) {
				//
			}
		});
}



$("#returnOrderUploadError").click(function(e){
	  $.colorbox({
			html: $("#errorDetailsAddToCart").html(),
			height: '300px',
			width: '492px',
			overlayClose: false,	
			escKey:false
		});	
});

$(".addQuoteToCart").click(function(e){
	$("#addQuoteToCartForm").submit();
});

$('#uploadReturnOrderBrowseFile').on("change", function(){
	var val = $(this).val();
	if(val.substring(val.lastIndexOf(".")+1, val.length)!='xls'){
		$(this).prop("value", null);
		alert(CHOOSE_FILE); 
	}
});

/** Start : Upload file validation for Delivered order **/
Array.prototype.contains = function(k) {
	  for(var i=0; i < this.length; i++){
	    if(this[i]==k){
	      return true;
	    }
	  }
	  return false;
	}

//Added JJEPIC 720 Start	

/*function TestFileType(fileName,fileTypes) {
	$(".invalidFileError").hide();
	if (!fileName) return;
	dots = fileName.split(".");
	fileType = dots[dots.length-1];
	if(fileTypes.contains(fileType)){
		$("#submitDelivedOrderFileForm").submit();
	}
	else{
		$(".invalidFileError").show();
		$("#uploadBrowseFile").val("");
		
	}
}*/



function TestFileType(fileName, fileTypes) {
	$(".invalidFileError").hide();
	if(fileName==""){
		$(".emptyFileError").show();
	}
	if (!fileName)
		{
//		parent.$.colorbox.close();
		if($("#attachDocName").val() == "")
			removeUploadFile();
		return false;
		}
	dots = fileName.split(".");
	fileType = dots[dots.length - 1];
	if (fileTypes.contains(fileType)) {
		$("#submitDelivedOrderFileForm").submit();
	} else { 
		$(".invalidFileError").show();
		$("#uploadBrowseFile").val("");
		$("#removeUploadForm").hide();
		$("#submitDelivedOrderFileForm").reset();

	}
}

function removeUploadFile() {
	if ($("#uploadBrowseFile").val() != "") {
		$("#submitDelivedOrderFileForm").reset();
		$("#removeUploadForm").hide();
	} else {
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/deleteDeliveredOrderFile',
			type : 'POST',
			success : function(data) {
				if (data == "Success") {
					$("#uploadBrowseFile").css("width", "auto");
					$("#attachDocNameSpan").hide();
					$("#removeUploadForm").hide();
					$(".submitDeliveredOrderFile").prop("disabled", false);
				} else if (data == "Failure") {
					$(".removeFileError").show();
				}
			}
		});
	}
}

//Added JJEPIC 720 Ends
/** End : Upload file validation for Delivered order **/

/** Start - Added Spinner on get price quote **/
$(".requestPriceQuoteBtn").click(function(e){
	$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
	$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
});
/** End - Added Spinner on get price quote **/
/*3088*/
$("#selectaddresspopup1").click(function(){
	$('#searchAddressAjax').val('');
	var dataObj=new Object();
	createShipToAddressAjaxCall(dataObj, "");
});

/*3088*/
$("#selectaddresspopup2").click(function(){
	$('#searchBillingAddressAjax').val('');
	var dataObj=new Object();
	createBillToAddressAjaxCall(dataObj, "");
});



var dataObject = new Object();

/*$(".qtyUpdateTextBox").change(function(e) {
	var entryNumber = ($(this).attr("entryNumber"));
	var qty = $(this).val();
	if (qty != "") {
		dataObject[entryNumber]=qty;
	}
});*/

/*$("#cartUpdateAllbutton")
.click(
		function(e) {
			var UpdateMultipleEntriesInCartForm = $('#UpdateMultipleEntriesInCartForm');
			var entryQty;
			for (var entryNumber in dataObject) {
				  if (dataObject.hasOwnProperty(entryNumber)) {
					  entryQty = entryNumber + ":" + dataObject[entryNumber];
					  $('<input>').attr({
							type : 'hidden',
							id : 'entryQuantityList',
							name : 'entryQuantityList',
							value : entryQty
						}).appendTo(UpdateMultipleEntriesInCartForm);
				  }
				}
			
			UpdateMultipleEntriesInCartForm.submit();
		});*/

/*3088*/
function createShipToAddressAjaxCall(dataObj, pageNumber) {
	
	var flag=true;
	if(null!=pageNumber && pageNumber!="") {
		dataObj.showMoreCounter = pageNumber;
		dataObj.showMore = "true";
		
	}else{
		dataObj.showMoreCounter = "1";
		dataObj.showMore = "false";
		
	}
	dataObj.showMode = "Page";
	dataObj.page = "0";
	dataObj.searchTerm = $.trim($("#searchAddressAjax").val());
	var url = ACC.config.contextPath + '/cart/getShippingAjax';
	jQuery.ajax({
		type : "POST",
		data : dataObj,
		url : url,
		success : function(data) {
			
			$('#searchAddresDiv').html(data);
			$('#selectaddresspopup').modal('show');
			
			if($(data).find('.address-txt').length==0){
				$('#no-address-result').show();
			}
			
			$('#searchAddresDiv .odd-row,#searchAddresDiv .even-row').on('click',function(){
				 $('#searchAddresDiv .odd-row').each(function(){
		         	 $(this).removeClass('addressSelected');
		         	
		        });
		        $('#searchAddresDiv .even-row').each(function(){
		           	$(this).removeClass('addressSelected');
		        });
		        $(this).addClass('addressSelected');
		       
			});
		}
	});	
}
/*3088*/
function createBillToAddressAjaxCall(dataObj, pageNumber) {
	
	var flag=true;
	if(null!=pageNumber && pageNumber!="") {
		dataObj.showMoreCounter = pageNumber;
		dataObj.showMore = "true";
		
	}else{
		dataObj.showMoreCounter = "1";
		dataObj.showMore = "false";
		
	}
	dataObj.showMode = "Page";
	dataObj.page = "0";
	dataObj.searchTerm = $.trim($("#searchBillingAddressAjax").val());
	var url = ACC.config.contextPath + '/cart/getBillingAddressSearch';
	jQuery.ajax({
		type : "POST",
		data : dataObj,
		url : url,
		success : function(data) {
			$('#searchAddresDiv1').html(data);
			$('#selectaddresspopupBillTO').modal('show');
			
			if($(data).find('.address-txt').length==0){
				$('#bill-no-address-result').show();
			}
			
			$('#searchAddresDiv1 .odd-row,#searchAddresDiv1 .even-row').on('click',function(){
				 $('#searchAddresDiv1 .odd-row').each(function(){
		         	 $(this).removeClass('addressSelected');
		         	
		        });
		        $('#searchAddresDiv1 .even-row').each(function(){
		           	$(this).removeClass('addressSelected');
		        });
		        $(this).addClass('addressSelected');
		        
			});
		}
	});	
}
