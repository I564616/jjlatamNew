jQuery(document).ready(function() {		
});

$('.shippingMethodSelect').change(function(e) {	
			jQuery.ajax({
				url : ACC.config.contextPath + '/cart/updateShippingMethod?route='
						+ $(this).val()+'&entryNumber='+$(this).attr('data'),
				success : function(data) {	}
			});
});

$(".checkoutBtn").click(function(e){
	$("#cartValidateForm").submit();
});