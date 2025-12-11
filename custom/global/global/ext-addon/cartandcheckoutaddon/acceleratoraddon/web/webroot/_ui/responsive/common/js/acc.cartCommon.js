$('.saveorderastemplate').click(function(e) {
	var saveAsTemplatepopup=$("#outerSaveAsTemplate").html()
	$('#SaveAsTemplateHolder').html(saveAsTemplatepopup);
	$('#save-as-tempalte-popup').modal("show");
	$(".createCartTemplate").click(function(e) {
			if($.trim($("#templateName").val())!="") {
				
			
				jQuery.ajax({
					url : ACC.config.contextPath
							+ '/my-account/template/save?templateName='
							+ $("#templateName").val()
							+ '&shared='+$("#share-account").is(":checked") + '&orderId='
							+$("#orderCode").val(),
					success : function(data) {
						
						if (data) {
							$('#save-as-tempalte-popup').modal('hide');
						} else {
							alert(SAVE_TEMPLATE);
						}
					}
				});
			} else {
				
				$("#save-as-tempalte-popup").find(".registerError").html('<label for="templateName" class="error">'+ALL_FIELDS+'</label>');
			}
		});
		
	});

function batchModePopUp(hideExtendedTimeOut) {
	$.colorbox({
		html : $("#extendTimeOutDiv").html(),
		height : 'auto',
		width : '530px',
		overlayClose : false,
		onComplete : function() {
			$("#submitBatchOrder").click(function() {				
				$("#createBatchOrderForm").submit();
			});	
			$("#extendedValidation").click(function() {
			    $("#extendedValidationForm").submit();
			});			
			$("#skipValidatonCancel").click(function() {				
				$.colorbox.close();
			});
			if(hideExtendedTimeOut){
				$("#extendedValidationBlock").hide();
			}
			$.colorbox.resize();
		}
	});
	}
	function invoicePDFPopUp(hideExtendedTimeOut) {
	$.colorbox({
		html : $("#InvoicePDFError").html(),
		height : 'auto',
		width : '530px',
		overlayClose : false,
		onComplete : function() {
			$("#submitBatchOrder").click(function() {				
				$("#createBatchOrderForm").submit();
			});	
			$("#extendedValidation").click(function() {
			    $("#extendedValidationForm").submit();
			});			
			$("#skipValidatonCancel").click(function() {				
				$.colorbox.close();
			});
			if(hideExtendedTimeOut){
				$("#extendedValidationBlock").hide();
			}
			$.colorbox.resize();
		}
	});
	
}