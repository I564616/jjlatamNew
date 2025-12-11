$(window).load(function(){

    $( "#selectindc" ).click(function() {
        $('#selectindirectcustomerpopup').modal({backdrop: 'static', keyboard: false}).modal('show');
        $("#customerName").html("");
        $("#customerNumber").html("");
        $("#changeCustomerNoAjax").val("");
        $("#change-select-customer-btn").hide();
        $("#select-indirect-customer-error").hide();
        $("#changeCustomerNoAjax").attr("autocomplete", "on");
    });

    $( "#selectindp" ).click(function() {
        $('#selectindirectpayerpopup').modal({backdrop: 'static', keyboard: false}).modal('show');
        $("#payerName").html("");
        $("#payerNumber").html("");
        $("#changePayerNoAjax").val("");
        $("#change-select-payer-btn").hide();
        $("#select-indirect-payer-error").hide();
        $("#changePayerNoAjax").attr("autocomplete", "on");
    });

    $("#changeCustomerNoAjax").change(function(){
        var type = "Customer";
        var indirectNumber = $("#changeCustomerNoAjax").val();
        if (indirectNumber.length >= 10)
            indirectNumber = indirectNumber.trim().substring(0,10);
        onClickSearch(indirectNumber, type);
    });

    $("#changePayerNoAjax").change(function(){
        var type = "Payer";
        var indirectNumber = $("#changePayerNoAjax").val();
        if (indirectNumber.length >= 10)
            indirectNumber = indirectNumber.trim().substring(0,10);
        onClickSearch(indirectNumber, type);
    });

    $('#change-select-customer-btn').click(function(){
        var indirectCustomer = $("#customerNumber").html();
  
    	$.ajaxSetup({
    		cache : false
    	});
    	
    	var loadUrl = ACC.config.contextPath + "/cart/getIndirectCustomerName";
    	var queryString = "cartEntryId=-1&indirectCustomer=" + indirectCustomer;
    	var url= loadUrl + "?" + queryString;
    	jQuery.ajax({
    		type: "GET",
    		url:  loadUrl,
    		data: queryString,
    		success: function (data) {
    			location.reload();
    		}
    	}); 
        $('#selectindirectcustomerpopup').modal('hide');
    });

    $('#change-select-payer-btn').click(function(){
        var indirectPayer = $("#payerNumber").html();
    	$.ajaxSetup({
    		cache : false
    	});
 
    	var loadUrl = ACC.config.contextPath + "/cart/getIndirectPayerName";
    	var queryString = "cartEntryId=-1&indirectPayer=" + indirectPayer;
    	var url= loadUrl + "?" + queryString;
    	
    	jQuery.ajax({
    		type: "GET",
    		url:  loadUrl,
    		data: queryString,
    		success: function (data) {
    			location.reload();
    		}
    	});
        $('#selectindirectpayerpopup').modal('hide');
    });

    var indirectName = "";

    function onClickSearch(indirectNumber, searchType) {
        $.ajaxSetup({
            cache : false
        });

        var loadUrl = ACC.config.contextPath + "/cart/getIndirectNameForModal";
        var queryString = "searchType=" + searchType + "&indirectNumber=" + indirectNumber;
        var url= loadUrl + "?" + queryString;
        var request;

        if(window.XMLHttpRequest){
            request=new XMLHttpRequest();
            console.log("XMLHttpRequest")
        }
        else if(window.ActiveXObject){
            request=new ActiveXObject("Microsoft.XMLHTTP");
            console.log("ActiveXObject")
        }

        request.onreadystatechange=getInfo;
        request.open("GET",url,true);
        request.send(null);

        function getInfo(){
            if(request.readyState==4 && searchType === "Customer"){
                indirectName = request.responseText;
                if (indirectName != null && indirectName !== "") {
                    $("#customerNumber").html(indirectNumber);
                    $("#customerName").html(indirectName);
                    $("#change-select-customer-btn").show();
                    $("#select-indirect-customer-error").hide();
                } else {
                    $("#customerNumber").html("");
                    $("#customerName").html("");
                    $("#change-select-customer-btn").hide();
                    $("#select-indirect-customer-error").show();
                }
            }
            if(request.readyState==4 && searchType === "Payer"){
                indirectName = request.responseText;
                if (indirectName != null && indirectName !== "") {
                    $("#payerNumber").html(indirectNumber);
                    $("#payerName").html(indirectName);
                    $("#change-select-payer-btn").show();
                    $("#select-indirect-payer-error").hide();
                } else {
                    $("#payerNumber").html("");
                    $("#payerName").html("");
                    $("#change-select-payer-btn").hide();
                    $("#select-indirect-payer-error").show();
                }
            }
        }
    }
    checkIndirectCustomerFulfillment();
});

function checkIndirectCustomerFulfillment(){
	var indirectCustomerList = $('[id^="indirectCustomerId"]').each(function(){});
	var disableValidateCart = false;
	for(i = 0; i < indirectCustomerList.length; i++){
		if($(indirectCustomerList[i]).val().trim() === "" || ($(indirectCustomerList[i]).val().length) !==  10){
			$(indirectCustomerList[i]).css('border-color', '#CC0000');
			disableValidateCart = true;
		}else{
			$(indirectCustomerList[i]).css('border-color', '');
		}
	}
	$(".cartStep1Saveupdate1Latam").prop("disabled", disableValidateCart);
}