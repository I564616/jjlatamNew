
jQuery(document).ready(function() {
	/** Below condition to check if the device is an IPAD **/
	try{
		if(navigator.userAgent.match(/iPad/i)!=null) {
			$(".captureSignature").fadeIn(1000);
		}
	}catch (exception){
		$(".captureSignature").hide();
	}	
	if($("#sapCreateOrderFail").val()=='true'){
		batchModePopUp(true);
	}
	$(".captureSignature").click(function(){
		$.get(ACC.config.contextPath + '/checkout/single/signature').done(function(data) {
			$.colorbox({
				html : data,
				height : 'auto',
				width : '740px',
				overlayClose : false,
				onComplete : function() {
					$("#signatureTermsRequested").val("true");
					$('#cboxClose').attr("style", "display:none;");
					$("#createSignature").jSignature({width:398+'px',height:204+'px'});
					var originalURL = document.getElementsByClassName("jSignature")[0].toDataURL();
					var $imgvalue = $("#createSignature");
					jQuery("form[name='signatureForm']").validate({
						errorPlacement: function(error, element) {
						    error.appendTo( element.parent().find('div.registerErrorTablet'));
						},
						onfocusout: false,
						focusCleanup: false
					});
					$('#submitSignature').click(function(e) {
						if(originalURL!= $('#createSignature').jSignature('getData') && $("form[name='signatureForm']").valid()){
							$("#canvasError").hide();
							var dataObj = new Object();
							dataObj.imageCode = $('#createSignature').jSignature('getData');
							dataObj.name=$("#signaturePad").find("#fname").val() + " " + $("#signaturePad").find("#lname").val();
							jQuery.ajax({
								type: 'POST',
								url: ACC.config.contextPath + '/checkout/single/signature',
								data: dataObj,
								success: function(data) {
									$.colorbox.close();
								}
							});
						} else if (originalURL== $('#createSignature').jSignature('getData')) {
							$("form[name='signatureForm']").valid();
							$("#canvasError").show();
							$("#canvasError > label").show();
						} else {
							$("form[name='signatureForm']").valid();
							$("#canvasError").hide();
						}
					});
					$('#ClearSignature').click(function(e) {
						$('#createSignature').jSignature('clear');
					});
					ACC4.termconditionpop.bindprivacypolicypopupLink($(".formInfoContainer .termconditionpopup_hn"));
				},
				onClosed: function(){
					$("#signatureTermsRequested").val("");
				}
			});
		});
	});
});


$(".placeOrderBtn").click(function(e){
	var val=$('#termsOfSales').prop("checked");
		//Either TOC is checked or it is not available/Valid for User Submit the form
	if(val === undefined || val)
	{
		$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
		$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
		//Changes added to display circle show on click of Place Order button
		loadingCircleShow("show");
		$("#cartCheckoutForm").submit();
	}
	//If TOC is valid and not checked show Error message and stay on checkout page
	else
	{
		$('.termsOfSalesError').show();
	}	
});


$(".placeConsignmentReturnOrderBtn").click(function(e){
	
	/* Commented the code as per comments in regression bug AAOL-6029 
	 var purchOrdNum=  $("#purchOrder").val();
	jQuery.ajax({
		global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
		url : ACC.config.contextPath + '/cart/checkPO?poNumber='
				+ $("#purchOrder").val(),
		success : function(data) 
		{		
			
			if (data) 
			{ */
				
				var val=$('#termsOfSales').prop("checked");
				//Either TOC is checked or it is not available/Valid for USer Subit the form
			if(val === undefined || val)
			{
				$("#poDuplicate").css("display","block");
				$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
				$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
				$("#cartCheckoutForm").submit();
			}
			//If TOC is valid and not checked show Error message and stay on checkout page
			else
			{
				$('.termsOfSalesError').show();
			}	
			/* Commented the code as per comments in regression bug AAOL-6029 
			 } 
			else 
			{   
				if(!invalidDropShipAccount){
			
				$("#poNotPresent").css("display","block");
				return false;
				}
			}
		}
	});*/
	
});



$(".requestPriceQuoteBtn").click(function(e){
	$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
	$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
});