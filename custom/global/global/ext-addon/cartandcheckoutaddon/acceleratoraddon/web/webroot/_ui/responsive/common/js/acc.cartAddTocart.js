jQuery(document)
		.ready(function(){
			
			if($("#dropShip").val()!=''){
				$('#distPurOrder').attr( "data-msg-required", $("#POError").val());
				$('#distPurOrder').attr( "class", "required" );
			}
			
			if($("#creditCard").is(":checked")){
				 $('.selectCard').show();
			}
			
			// Validate shopping cart functionality - START
			jQuery("#addToCartForm").validate({
				rules: {
					productId:{
						required:true,
						//alphanumeric: true
						
						
					},
					qty:{
						digits: true
					}
					},
				errorPlacement: function(error, element) { 
					error.appendTo(element.parent().parent().parent().find('div.registerError'));
				},
				onfocusout: false,
				focusCleanup: false
			});
			
			$("#addToCartCart").click(function(e) {
				e.preventDefault();
				$("#addToCartForm .registerErrorSearch .productValidationErrorMsg").hide();
				if(jQuery("#addToCartCart").valid() && $('#productId').val()!='Product Code or GTN')
				{
					$("#addToCartForm").attr("action",ACC.config.contextPath +"/cart/addToCart");
					
					$("#addToCartForm").find("input[name=qty]").val($("#quantity").val());
					$("#addToCartForm").find("input[name=productId]").val($("#productId").val());
					$("#addToCartForm").submit();
				}
				else
				{
					$("#productId").parent().parent().find('div.registerError').html("<label class='error'>" + $("#invalidProdcutCodeMessage").val() + "</label>");
				}
			});	
	
	$('#purchOrder').change(function () {
		$('.registerPOError').html('');
		var poNum=$("#purchOrder").val();
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updatePurchaseOrderNumber?purchaseOrderNumber=' + encodeURIComponent(poNum),
			async: false,
			success: function (data) {
			}
		});
	});
	$('#attention').change(function () {
		var attenction="";
		attenction=$("#attention").val();
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateAttention?attention='+encodeURIComponent(attenction),
			async: false,
			success: function (data) {
			}
		});
	});
    $('#specialText').change(function () {
		
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateSpecialText?specialText=' + $("#specialText").val(),
			async: false,
			success: function (data) {
			}
		});
	});
	$('#dropShip').change(function () {
		$('#errorMsgDiv').html('');
		var shipAccount=$("#dropShip").val();
		//Block committed to update drop-ship value if user removes value for the field
//		if(shipAccount.trim()!=''){
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateDropShipAccount?dropShipAccount=' + $("#dropShip").val(),
			success: function (data) {
	
				// if error occurred in updating drop ship address
				// it will show error massage in errorMsg div else reflect the new address on delivery address div
				if (data.indexOf("~Error Occurred~")!= -1){
					$('#dropShip').val('');
					$('#errorMsgDiv').html("<label class='error'>"+ $("#DropShipError").val() + "</label>");
					//$("#errorMsgDiv").html("<label class='error'>Please select a No Charge Reason!</label>")

					$('#distPurOrder').removeAttr( "data-msg-required");
					$('#distPurOrder').removeAttr( "class");
				}
				else{
					/*$('#errorMsgDiv').html("");
					//$('#pi-shipToaddress').html(data)
					$('#dropShip').val('');
					$('#distPurOrder').attr( "data-msg-required",  $("#DistPurchaseError").val() );
					$('#distPurOrder').attr( "class", "required" );
					$('#drop-ship-account-list-icon').removeClass('strictHide');*/
					window.location.reload();
				}	
			}
		});
	/*}else{
		$('#dropShip').val('');
	}*/
	});
	$('.distPurOrder').change(function () {
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateDistributorPONumber?distributorPONumber=' + $(".distPurOrder").val(),
			async: false,
			success: function (data) {
			}
		});
	});
	
	/*Delivered Order cart Page*/
	$('#dropShipDel').change(function () {
		
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateDropShipAccount?dropShipAccount=' + $("#dropShipDel").val(),
			success: function (data) {
	
				// if error occurred in updating drop ship address
				// it will show error massage in errorMsg div else reflect the new address on delivery address div
				if (data.indexOf("~Error Occurred~")!= -1){
					
					$('#errorMsgDiv').html('<font color="#B41601">'+ $("#DropShipError").val() + '</font>');
					$("#errorMsgDiv").show();
					$("#errorMsgDiv").hide().fadeIn(1000);
					setTimeout(function(){
						$("#errorMsgDiv").slideUp(500, function(){
							$("#errorMsgDiv").hide();
						});
					},5000);
					$('#dropShipDel').val('');
				}
				else{
					$('#errorMsgDiv').html("");
					$('#pi-shipToaddress-drop').html(data);
					$('#selectDropShipLink').removeClass('strictHide');									
				}	
			}
		});
	});
	
	$('#distPurOrderDel').change(function () {
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateDistributorPONumber?distributorPONumber=' + $("#distPurOrderDel").val(),
			async: false,
			success: function (data) {
			}
		});
	});
	/*Delivered Order cart Page Ends*/
	
	$('.lotCommentInput').change(function () {
		
		// fix for unique ids
		var prodid= $(this).attr('id').split("_")
		prodid=prodid[1];
				
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateLotNumberForEntry?entryNumber=' +prodid+'&newLotComment='+ $('#LotComment_'+prodid).val(),
			async: false,
			success: function (data) {
			}
		});
	});
	
	$('#selectCard').change(function () {
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updatePaymentInfo?selectCard=' + $("#selectCard").val(),
			async: false,
			success: function (data) {
			}
		});
		
	});
	
	$('#purOrder').click(function () {
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updatePaymentInfo?selectCard=' + $("#purOrder").val(),
			async: false,
			success: function (data) {
			}
		});
		
	});
	
	
}
);