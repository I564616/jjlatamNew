$(document).ready(function()
{
	var payCreditcard = $("#payCreditcard").val();
	if(payCreditcard != "" && payCreditcard == "SUCCESS"){
		$(".paymenttwo").find("a").removeClass("collapsed");
		$(".paymentone").find("a").addClass("collapsed");
		$(".paymenttwo").find("i").removeClass("fa-circle-o");
		$(".paymenttwo").find("i").addClass("fa-circle");
		$(".paymentone").find("i").removeClass("fa-circle");
		$(".paymentone").find("i").addClass("fa-circle-o");
		$("#paymentType2").css("display","block");
	}
	
	/* light box for credit card*/
	$("#editCreditCardDetails").click(function(){
		/*$(".lightboxBlackOverlay").css("height", $(document).height());
		$(".lightboxContent").css("display","block");
		$(".lightboxBlackOverlay").css("display","block");*/
		// Fix for JJEPIC-257 Reset form fields (In Case popup has opened before and contains data) and also during regression testing, when the user clicks on the
		// Pay Now button on the IE browser, it's not working properly because Paymetric_Packet is being emptied due to reset() function so we assign that value to
		// one variable and after calling the reset function, assign that value to paymetric_packet again.
		var payload = $("#Paymetric_Packet").val();
		$("#addEditCreditCart form" ).reset();
		$("#Paymetric_Packet").val(payload);
		$("#addEditCreditCart form input" ).css("background","");
		$("#Paymetric_ErrorLogging").html('');
		$("#Paymetric_ErrorLogging").css('border','');
		$("#Paymetric_CreditCardNumber").attr("autocomplete", "off");
		$("#Paymetric_Exp_Month").attr("autocomplete", "off");
		$("#Paymetric_Exp_Year").attr("autocomplete", "off");
		$("#Paymetric_CVV").attr("autocomplete", "off");
	});
	
	$('#remember').change(function () {
		if($(this).is(":checked")){
			jQuery.ajax({
				url: ACC.config.contextPath +'/cart/saveValueInSession',
				success: function () {
					
				}
			});
		}
	});
	
});



