$("#radioCpf").change(function() {
   if($(this).is(":checked")) {
        $("#cpfDiv").show();
   }
});
$("#radioCnpj").change(function() {
   if($(this).is(":checked")) {
        $("#cpfDiv").hide();
   }
});
$("#radioTaxId").change(function() {
   if($(this).is(":checked")) {
        $("#cpfDiv").hide();
   }
});
$("#indirectCustomer-agree").change(function() {
    if($(this).is(":checked")) {
        $("#indirectCustomerSend").removeAttr("disabled");
        $("#indirectCustomerSend").removeClass("btn-disabled-style");
    }
    else {
        $("#indirectCustomerSend").attr("disabled", "disabled");
        $("#indirectCustomerSend").addClass("btn-disabled-style");
    }
})
$("#indirectCustomerSend").click(function(){
    $(".addIndirectCustomerGlobalError").hide();
    var form = $("#addIndirectCustomerValidate");
    form.append(jQuery('<input>', {
        'name' : 'CSRFToken',
        'value' : ACC.config.CSRFToken,
        'type' : 'hidden'}));
    form.validate();
    if(form.valid()){
        form.submit();
    }
    else{
        $(".addIndirectCustomerGlobalError").show();
        $(window).scrollTop(0);
    }
});

var formSendSuccessfulResult = $("#isFormSendSuccessful").val();
if (formSendSuccessfulResult == "true"){
    $("#addIndirectCustomerFormPage #successMessage").show();
}
else if (formSendSuccessfulResult == "false"){
    $("#addIndirectCustomerFormPage #errorMessage").show();
}

$("#indirectPayer-agree").change(function() {
    if($(this).is(":checked")) {
        $("#indirectPayerSend").removeAttr("disabled");
        $("#indirectPayerSend").removeClass("btn-disabled-style");
    }
    else {
        $("#indirectPayerSend").attr("disabled", "disabled");
        $("#indirectPayerSend").addClass("btn-disabled-style");
    }
});
$("#indirectPayerSend").click(function(){
    $(".addIndirectPayerGlobalError").hide();
    var form = $("#addIndirectPayerValidate");
    form.append(jQuery('<input>', {
        'name' : 'CSRFToken',
        'value' : ACC.config.CSRFToken,
        'type' : 'hidden'}));
    form.validate();
    if(form.valid()){
        form.submit();
    }
    else{
        $(".addIndirectPayerGlobalError").show();
        $(window).scrollTop(0);
    }
});

var formSendSuccessfulResult = $("#isFormSendSuccessful").val();
if (formSendSuccessfulResult == "true"){
    $("#addIndirectPayerFormPage #successMessage").show();
}
else if (formSendSuccessfulResult == "false"){
    $("#addIndirectPayerFormPage #errorMessage").show();
}

$("#indirectPayerBack").click(function (e) {
	window.location.href = ACC.config.contextPath + '/services';
});