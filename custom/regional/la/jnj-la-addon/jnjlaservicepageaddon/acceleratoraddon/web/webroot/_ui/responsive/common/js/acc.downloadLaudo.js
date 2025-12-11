$(document).ready(function(){
	$(".jnjId").focus();
	$(".anchorClicker").click(function(){
		window.location = $(this).parent().find("a").attr("href");
	});
	/**
	 * Binding the enter key press action on lote number field
	 */
	$(".loteNumber").keypress(function(e) {
		if(e.which == 13) {
			$("#downloadLaudoSearchButton").click();
	    }
	});
	/**
	 * Binding the enter key press action on jnj id field
	 */
	$(".jnjId").keypress(function(e) {
		if(e.which == 13) {
			$("#downloadLaudoSearchButton").click();
	    }
	});
	/**
	 * Binding the DownloadLaudoSerach button to submit Form.
	 */
	$('#downloadLaudoSearchButton').click(function(e){
		e.preventDefault();
		$('#defaultalert').hide();
		var productCode = $('#downloadJnjId').val();
		$('#downloadLaudoNoSearchInfo').hide();
		if(($('#isBatchMandatory').val()) === "true") {
			if(!!productCode && !!$('#downloadLoteNumber').val()){
					$('#downloadSearchBy').val('productAndLaudo');
					$('#downloadSearchFlag').val(true);
					$('#downloadLaudoForm').submit();
				    } else {
						$(".mandatoryError").html("<label class='error'>" + $("#hddnMandatoryError").val() + "</label>");
					}
			} else if(!!productCode){
		        $('#downloadSearchBy').val('productAndLaudo');
				$('#downloadSearchFlag').val(true);
				$('#downloadLaudoForm').submit();
		    } else {
				$(".mandatoryError").html("<label class='error'>" + $("#hddnMandatoryError").val() + "</label>");
			}
    });	
});