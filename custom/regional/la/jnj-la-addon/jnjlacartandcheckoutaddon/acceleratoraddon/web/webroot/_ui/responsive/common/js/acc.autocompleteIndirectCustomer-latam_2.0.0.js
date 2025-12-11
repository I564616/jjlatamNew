$(window).load(function(){
	
		$( ".indirectCustomer" ).autocomplete({
		  open: function(event, ui) { $(".ui-menu").css("z-index", 10000); },
		  close: function(event, ui) { $(".ui-menu").css("z-index", -1); },
		  source: 
			  function(request, response) {
		        var results = $.ui.autocomplete.filter(getIndirectCustomerOrPayerData("indirectCustomer", request.term), request.term);
		        response(results);
		    },
		  minLength: 3,
		  select: selectAutoCompleteValue
		});
	    
   	  	$( ".indirectPayer" ).autocomplete({
		   open: function(event, ui) { $(".ui-menu").css("z-index", 10000); },
		   close: function(event, ui) { $(".ui-menu").css("z-index", -1); },
		   source: 
			   function(request, response) {
		   		 var results = $.ui.autocomplete.filter(getIndirectCustomerOrPayerData("indirectPayer", request.term), request.term);
		   		 response(results);
		    },
		    minLength: 3,
		   select: selectAutoCompleteValue
    	}); 	
   	  	
   	 if( getCurrentCountry() == "Argentina"){
 		adjustIndirectLineHeight();
     }
 });

function getIndirectCustomerOrPayerData(indirectType, term){
	
	var loadUrl = ACC.config.contextPath + "/cart/getIndirectCustomerOrPayerData";
	var queryString = "indirectType=" + indirectType + "&term=" + term;
	var indirectDataList = null;
	jQuery.ajax({
		type: "GET",
		url:  loadUrl,
		data: queryString,
		async: false,
		success: function (dataResult) {
			indirectDataList = dataResult;
		}
	});
	
	return indirectDataList;
}


function adjustIndirectLineHeight(){
	
	$('[id^="indirectCustomerName"]').each(function(){
		var icId= this.id;
		var index = icId.substring(20, icId.legth);
		
		var icNameHeight = $('#indirectCustomerName'+index).height();
		var ipNameHeight = $('#indirectPayerName'+index).height();
		
		if(icNameHeight > ipNameHeight){
			$('#indirectPayerName'+index).height(icNameHeight);
		}else{
			$('#indirectCustomerName'+index).height(ipNameHeight);
		}
	});
}


function selectAutoCompleteValue(event, ui){
	event.preventDefault();
	// Split the string in a arrays and grabs the first which is the IC/IP
	value = ui.item.value.split('-')[0];
	value = value.trim();
	$("#"+event.target.id).val(value);
	$("#"+event.target.id).blur();
}

function indirectStringToList(indirectString){
	if (!indirectString)
		return [];
	indirectString = indirectString.replace('[','');
	indirectString = indirectString.replace(']','');
	indirectString = indirectString.split('_').join(' - ');
	indirectString = indirectString.split(",");
	return indirectString;
}

// ############ CART PAGE FUNCTIONS - BEGIN ############################
function onChangeIndirectCustomerCart(entryNumber) {
	var indirectCustomer = $('#indirectCustomerId'+entryNumber).val();
	$.ajaxSetup({
		cache : false
	});
	
	var loadUrl = ACC.config.contextPath + "/cart/getIndirectCustomerName";
	var queryString = "cartEntryId=" + entryNumber + "&indirectCustomer=" + indirectCustomer;
	
	jQuery.ajax({
		type: "GET",
		url:  loadUrl,
		data: queryString,
		success: function (customerName) {
			setCustomerName(entryNumber, customerName);
		}
	});
}

function setCustomerName(entryNumber, customerName){
	if(entryNumber >= 0){
		$('#indirectCustomerName'+entryNumber).text(customerName);
		checkIndirectCustomerFulfillment();
	}else{
		location.reload();
	}
	
	var icNameHeight = $('#indirectCustomerName'+entryNumber).height();
	var ipNameHeight = $('#indirectPayerName'+entryNumber).height();
	
	if(icNameHeight > ipNameHeight){
		$('#indirectPayerName'+entryNumber).height(icNameHeight);
	}else{
		$('#indirectCustomerName'+entryNumber).height(ipNameHeight);
	}
}

function onChangeIndirectPayerCart(entryNumber) {
	var indirectPayer = $('#indirectPayerId'+entryNumber).val();
	$.ajaxSetup({
		cache : false
	});
	
	var loadUrl = ACC.config.contextPath + "/cart/getIndirectPayerName";
	var queryString = "cartEntryId=" + entryNumber + "&indirectPayer=" + indirectPayer;
	
	jQuery.ajax({
		type: "GET",
		url:  loadUrl,
		data: queryString,
		success: function (customerName) {
			setPayerName(entryNumber, customerName);
		}
	});
}

function setPayerName(entryNumber, customerName){
	if(entryNumber >= 0){
		$('#indirectPayerName'+entryNumber).text(customerName);
	}else{
		location.reload();
	}
}



// ############ CART PAGE FUNCTIONS - END ############################
