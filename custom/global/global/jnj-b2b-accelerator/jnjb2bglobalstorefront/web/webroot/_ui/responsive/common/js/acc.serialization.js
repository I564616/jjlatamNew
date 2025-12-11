$(document).ready(function() {
	
	updateDays($('#verfiedSelectedMonth').val());
	var verifiedSelectValue;
	if($('#serialVerfied').val()=='true'){
		
		
		$('.verify-serial-select').each(function(){
			verifiedSelectValue=$(this).prev().val();
			$(this).val(verifiedSelectValue)
		});
		
	}
	
	$("#expiryYear").change(function() {
		var expYearVal = $(this).val();
		var expMonthVal = $("#expiryMonth").val();
		if (!!expMonthVal && !expMonthVal == "") {
			
			updateDays(expMonthVal);
		}
	});
	// Required to handle leap years
	$("#expiryMonth").change(function() {
		var expMonthVal1 = $(this).val();
		
		updateDays(expMonthVal1);

	});

	function updateDays(month) {
		
		// Assume Feb has 29 days to start. Change to 28 if needed when year is
		// selected.
		var daysPerMonth = [ 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
		
		var yearSelected ;
		if($("#expiryYear").val()==null){
			yearSelected = $("#verfiedSelectedYear").val();
		}
		else{
			yearSelected = $("#expiryYear").val();
		}
		
		
		
		var expiryDay = document.getElementById('expiryDay');

		// If a year is selected and it isn't a leapYear then force Feb to 28
		// days.
		if (Number(month) == 02 && !!yearSelected && !isLeapYear(yearSelected)) {

			daysPerMonth[1] = 28;
		}

		var daysInMonth = daysPerMonth[month - 1];


		$('#expiryDay').empty()
		
		$("#expiryDay").append("<option value=''>Select</option>");	

		for (var i = 1; i <= daysInMonth; i++) {
			$("#expiryDay").append("<option>" + i + "</option>");
		}

		


	}

	function isLeapYear(year) {
		return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
	}
	$("#verifySerialButton").click(function(){

		if($("#verifySerialForm").valid()){
			$("#verifySerialForm").submit();
		}
		
	})
	
	//Changes for AAOL-6197	
	$(document).on('keypress', function(e) {
       if ($("#verifySerialForm").size() > 0) {
           if (e.keyCode == 13)
           {
               if($("#verifySerialForm").valid()){
                   $("#verifySerialForm").submit();
               }
           }
        }
    });
	
	$("#serializationPDF,#serializationExcel").click(function(){
		  
		var downloadType = $("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
		$("#verifySerialForm").attr("action", ACC.config.contextPath +'/serialization/serializationResult');
	    $("#verifySerialForm").submit();
	    $("#verifySerialForm").attr("action", $("#originalFormAction").val()); 
 

	});
	
	$("#resetButton").click(function(){
		$('#external-links').hide();
		$('.serial-result').text('');		
		$('.verify-serial-select').each(function(){
			$(this).val('').prop('disabled',false);
		});
		$('.verify-serial-input').each(function(){
			$(this).val('').prop('disabled',false);
		});
		$('#verifySerialButton').prop('disabled',false);
		//Changes for AAOL-6197
		$('.verifyDetailError').each(function(){
			$(this).find('label').hide();
		});
		$('.serial-result').removeClass("known")
		$('.serial-result').removeClass("unknown")
		
	});
	

	
});
