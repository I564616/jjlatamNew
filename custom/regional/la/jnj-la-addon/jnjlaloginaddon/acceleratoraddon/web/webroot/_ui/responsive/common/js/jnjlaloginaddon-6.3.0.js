

var recoverpasswordP3 = "#recoverpasswordP3", recPassError = "div.recPassError", tableNoData = "#tableNoData",
    panelCollapsed = "panel-collapsed" , recoverpasswordFormP3 = "#recoverpasswordFormP3";

$('#ordersTablehomelatam').DataTable({
    "aaSorting":[],
    "aoColumnDefs": [{
        'bSortable': false,
        'aTargets': [
            'no-sort'
        ]
    }],
    "language": {
        "emptyTable": $(tableNoData)},
        'pagingType': "simple",
        "bPaginate": false,
        "bFilter": false,
        "bInfo": false
    }
);

function applyDataTableToSortingTable(useAjaxTable, wrapper){
    var emptyTableValue = $(tableNoData).val();
    if (emptyTableValue === undefined || emptyTableValue.trim() === "") {
        emptyTableValue = "No data found.";
    }
    var params = {
        "aaSorting": [  ],
        "aoColumnDefs" : [{'bSortable' : false, 'aTargets':['no-sort']}],
        "language": {
            "emptyTable": emptyTableValue,
            "info": "  <b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,
            "oPaginate": {"sNext": "   >","sPrevious":"<"},
            "lengthMenu":DATA_SHOW_MENU
        },
        'pagingType': "simple"
    };

    if (useAjaxTable) {
        prepareParamsToAjaxTable(params);
    }

    if (wrapper) {
        $('.lasorting-table', wrapper).DataTable(params);
    } else {
        $('.lasorting-table').DataTable(params);
    }
}

function prepareParamsToAjaxTable(params){
    params.destroy = true;
    params.lengthMenu = [[-1, -2, -3, -4], [10, 25, 50, 100]];
}

function initAjaxTable(ajaxUrl, wrapper, sortColumn, table, sendInitialAjax){
    var ajaxTable = new AjaxTable(ajaxUrl, wrapper, sortColumn, table, function(){
        applyDataTableToSortingTable(true, wrapper);
    });
    ajaxTable.init(sendInitialAjax);
    return ajaxTable;
}

function prepareOrderHistoryAjaxTables(){
    var url = "order-history";
    initAjaxTable(url, "#datatab-desktop-wrapper", "order", null, true);
    initAjaxTable(url, "#datatab-tablet-wrapper", "orderNumber", "tablet", true);
    initAjaxTable(url, "#datatab-mobile-wrapper", "orderNumber", "mobile", true);
}

function prepareInvoiceHistoryAjaxTables(){
    var url = "invoice-history/search";
    // creates 3 instances of AjaxTable that will handle each table
    var ajaxTableDesktop = initAjaxTable(url, "#datatab-desktop-wrapper", "creationDate", null, false);
    var ajaxTableTablet = initAjaxTable(url, "#datatab-tablet-wrapper", "creationDate", "tablet", false);
    var ajaxTableMobile = initAjaxTable(url, "#datatab-mobile-wrapper", "creationDate", "mobile", false);

    // creates 1 instance of AjaxTableForm, which manages all instance of AjaxTable
    var ajaxTables = [ajaxTableDesktop, ajaxTableTablet, ajaxTableMobile];
    var ajaxTableForm = new AjaxTableForm(ajaxTables);
    ajaxTableForm.init();
    $("#ajaxTableFormSearchButton").hover(function(){
        callCheckDates();
    });
}

if (location.pathname.match("/order-history$")){
    $(document).ready(function(){
        prepareOrderHistoryAjaxTables();
    });
} else if (location.pathname.match("/invoice-history$")){
    $(document).ready(function(){
        prepareInvoiceHistoryAjaxTables();
    });
} else {
    applyDataTableToSortingTable(false);
}

var table=0;


$('.dateselectordropdown').each(function(){

    $(this).insertAfter(".dropShipmentTable:eq("+table+") .dataTable thead").css("display","block");
    $(this).insertAfter(".header-content-table:eq("+table+").dataTable thead").css("display","block");
    table=table+1;
});

var nowDate = new Date();
var nowDatePattern = $("#hidden-sessionLang").val();
if (undefined !== nowDatePattern && null != nowDatePattern){
    nowDatePattern = nowDatePattern.toLowerCase();
}

var today = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate(), 0, 0, 0, 0);
$('.requestDeliveryDataPicker').datepicker({
    format:nowDatePattern,startDate: today
});

$(".date-picker").on('changeDate', function(ev){
    $(this).datepicker('hide');
});


$('.quickClose').click(function(){
    $('.prod-number-la').val('');
    $('.prod-quanity').val('');
    $('.error-msg-red').hide();
    $('#quickaddcart-popup').find('.modal-dialog').css({width:'500px',
         height:'auto',
       'max-height':'100%'});
});

var permissions = getPermissions();
$( window ).on( "load", function(){
    configChangeAccountPopup();
    prepareLinksByPermissions(permissions);
    prepareFooterText();
    prepareLatamNavbarIcons();
});

$("#selectact").click(function (){
    setTimeout(configChangeAccountPopup,300);
});

var singleAccount = $(".accountListPopUp").size() === 1;
if (singleAccount){
    $('#selectaccountpopup').removeAttr('data-firstLogin');
}

function configChangeAccountPopup(){
    $("#changeAccountNoAjax").keypress(function (event) {
        enterPressToClickEvent(event, '#searchSelectAccountBtnHeader');
    });
    var changeAccountPlaceholder = $("#changeAccountSearch").val();
    $("#changeAccountNoAjax").attr("placeholder", changeAccountPlaceholder);
    var noData = $(tableNoData).val();
    $('center p').text(noData);
    $("#searchSelectAccountBtnHeader").click(function(){
        setTimeout(configChangeAccountPopup,300);
    });
}

$('#datePicker1').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});
$('#datePicker2').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker2").removeClass("hasDatepicker");
});

$("#datePicker1").change(function(){
	 $(".datepicker").css("display","none");
});

$("#datePicker2").change(function(){
	$(".datepicker").css("display","none");
});
$('#date-picker-head').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});

$("#date-picker-head").change(function(){
	 $(".datepicker").css("display","none");
});


$('#date-picker-11').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});

$("#date-picker-11").change(function(){
	 $(".datepicker").css("display","none");
});


$('#openOrdersStartDate').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});
$("#openOrdersStartDate").change(function(){
	 $(".datepicker").css("display","none");
});

$('#datepickerform0').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});


$("#datepickerform0").change(function(){
	 $(".datepicker").css("display","none");
});



$('#datepickerform1').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});


$("#datepickerform1").change(function(){
	 $(".datepicker").css("display","none");
});


$('#fromDateLaudo').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});


$("#fromDateLaudo").change(function(){
	 $(".datepicker").css("display","none");
});
$('#toDateLaudo').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});


$("#toDateLaudo").change(function(){
	 $(".datepicker").css("display","none");
});

$('#openOrdersEndtDate').on('click', function(){
    $("#ui-datepicker-div").remove();
	 $("#datePicker1").removeClass("hasDatepicker");
});


$("#openOrdersEndtDate").change(function(){
	 $(".datepicker").css("display","none");
});




function prepareLinksByPermissions(permissions) {

    if (permissions !== undefined && permissions != null && permissions.length !== 0){
        for (var i=0; i<permissions.length; i++){
            permissions[i] = permissions[i].trim();
        }
        if ($.inArray('placeOrderGroup',permissions) === -1 && $.inArray('placeOrderBuyerGroup',permissions) === -1 ){
            //home page place order block
            $("#placeOrderBlock").addClass("hide");
            //minicart button
            $("#cart-btn-holder").addClass("hide");
            //order history add to cart
            $('[id^="orderDetailAddToCartDiv"]').addClass("btnclsinactive");
            $('[id^="orderDetailAddToCart"]').attr("href", "javascript:void(0)");
            $('[id^="orderDetailAddToCart"]').addClass("linkinactive");
            //template page add to cart
            $('.laAddToCartOrderTemplate').addClass("btnclsinactive");
            $('.laAddToCartOrderTemplate').unbind();
            //catalog page add to cart
            $('.laAddToCart').addClass("btnclsinactive");
            $('.laAddToCart').unbind();

        }
        if ($.inArray('orderHistoryGroup',permissions) === -1 ){
            $("#orderHistoryBlock").addClass("hide");
        }
        if ($.inArray('catalogGroup',permissions) === -1 && $.inArray('viewCatalogGroup',permissions) === -1){
            $("#global-search-txt-holder").addClass("hide");
        }
    }

}

function prepareFooterText(){
    var currentCountry = getCurrentCountry();
    $("#countryLabel").text(currentCountry);
    var currentCountryContactNumber = getCurrentCountryContactNumber();
    $("#countryContactNumberLink").text(currentCountryContactNumber);
}

// Handles Enter key press event for input field in the site
$("#prodCode").keypress(function (event) {
    enterPressToClickEvent(event, '#addToCartForm_2');
});

function prepareLatamNavbarIcons(){
    $(".BIDsBarComponent").addClass('fa-hand-o-up ');
    $(".ServicesBarComponent").addClass('fa-list-alt ');
    $(".SelloutBarComponent").addClass('fa-file-text-o ');
    $(".UploadedFilesBarComponent").addClass('fa-file-archive-o ');
    $(".ContractsBarComponent").addClass('fa-pencil');
    $(".CLSBarComponent").addClass('fa-suitcase ');
    $(".InvoiceHistoryBarComponent").addClass('fa-file-pdf-o ');
}

function removeLinkFromMenuItem(element){
    element.attr("href","#");
}

$("#j_username").attr("tabindex", 1);
$("#j_password").attr("tabindex", 2);

$(document).on('click', '.toggle-link-la', function(){
    var $this = $(this);

    if($this.hasClass(panelCollapsed)) {
        $this.removeClass(panelCollapsed);
        $this.find('span.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus');
    }
    else {
        $this.addClass(panelCollapsed);
        $this.find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus');
    }

    setTimeout(menuNdbodyHeight,500);
});

function enterPressToClickEvent(event, buttonId){
    if (event.which === '13') {
        $(buttonId).click();
    }
}

function getPermissions(){
    if(sessionStorage.getItem("permissions") != null){
        return sessionStorage.getItem("permissions").split(",");
    }

    var permissions = [];
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: ACC.config.contextPath + '/p/la/permissions',
        success: function (data) {
            if (typeof data !== 'undefined' && data != null && data.length > 0) {
                permissions = data;
                sessionStorage.setItem("permissions", permissions);
            }
        }
    });
    return permissions;
}

function getCurrentCountry(){
    if(sessionStorage.getItem("currentCountry") != null){
        return sessionStorage.getItem("currentCountry");
    }

    var currentCountry = "";
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: ACC.config.contextPath + '/p/la/currentCountry',
        success: function (data) {
            currentCountry = data;
            sessionStorage.setItem("currentCountry", currentCountry);
        }
    });
    return currentCountry;
}

function getCurrentCountryContactNumber(){
    if(sessionStorage.getItem("contactNumber") != null){
        return sessionStorage.getItem("contactNumber");
    }

    var contactNumber = "";
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: ACC.config.contextPath + '/p/la/contactNumber',
        success: function (data) {
            contactNumber = data;
            sessionStorage.setItem("contactNumber", contactNumber);
        }
    });
    return contactNumber;
}


function resetPasswordValidation() {
    jQuery(recoverpasswordFormP3).validate({
        rules: {
            checkpass: {
                required: true,
                complexity: true,
                minlength: 8
            },
            checkpass2: {
                equalTo: "#checkpass"
            }
        },
        messages: {
            checkpass: {
                complexity: $("#profileChangePasswordMismatchComplexity").val()
            },
            checkpass2:{
                equalTo: $("#profileChangePasswordMismatch").val()
            }
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent().parent().parent().parent().find(recPassError));
        },
        onfocusout: false,
        focusCleanup: false
    });
}
/**************************************
 * This function sets the click function for password reset step
 */
function resetPasswordButtonAction(userEmail) {
    $('#passnext3').click(function () {
        if ($(recoverpasswordFormP3).valid()) {
            var dataObj = new Object();
            dataObj.email = $("#useremail").length !== 0 ? $("#useremail").val() : userEmail;
            dataObj.password = $("#checkpass").val();
            jQuery.ajax({
                type: 'POST',
                url: ACC.config.contextPath + '/passwordReset/latamRevisionVerification',
                data: dataObj,
                success: function (data) {
                    if (data === "true") {
                        $(recoverpasswordP3).hide();
                        $('#resetPasswordpopup').modal('hide');
                        $('#recoverpasswordP4').show();
                        $('#resetPasswordsuccesspopup').modal('show');
                    } else {
                        $(recoverpasswordP3).find(recPassError).append("<label class=\"error\">" + data  + "</label>");
                    }
                },
                error: function () {
                    $(recoverpasswordP3).find(recPassError).append("<label class=\"error\">" + $("#checkpass2").attr("data-msg-required") + "</label>");
                }
            });
        }
    });
}

function getMessage(code){
    var message = "";
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: ACC.config.contextPath + '/p/la/message?code=' + code,
        success: function (data) {
            message = data;
        }
    });
    return message;
}



var $password = '',
$resetPasswordConfirmation = '',
resetPwd = "#reset_password",
resetConfirmPassword = "#reset_confirm_password",
pwdStrengthMsgHld = "#strengthAddAfter",
waitForPopup = 100000,
waitingTimePopup = 0,
fieldError = 'field-error',
fieldErrorValid = "field-error-valid",
resetPwdWidget = "#resetPwd",
recoverpasswordP3recPassError = "#recoverpasswordP3 .recPassError";
var checkPwdResetPopup = setInterval(function() {
    if ($('#resetPasswordpopup').hasClass('in')) {
        initFunctions();
        clearInterval(checkPwdResetPopup);
    } else if (waitingTimePopup >= waitForPopup) {
        clearInterval(checkPwdResetPopup);
    }
    waitingTimePopup += 500;
}, 500);

function initFunctions() {
    initializeTooltip();
    pwdInit();
    disableResetPwdButton();
    bindResetPAssword();
}

function initializeTooltip() {
    $(resetPwd).on('mouseover', function(){
        $(resetPwdWidget).show();
    });
    $(resetPwd).on('mouseout', function(){
        $(resetPwdWidget).hide();
    });
}

function pwdInit() {
    $password = $(resetPwd);
    $password.strengthify({
        "$addAfter": $(pwdStrengthMsgHld)
    });

    bindResetPasswordOnBlur();
}

function getFieldHolder(elem) {
    return $(elem).parents('.field-holder');
}

function bindResetPasswordOnBlur() {
     $('[type=password]').on('keyup', function() {
		  if ($(this).hasClass('reset-password-confirmation')) {
            $(this).addClass('binded');
            handleResetConfirmPasswordValidation();
         } else {
            handleResetPasswordValidation();
        }
     });
}

function handleResetPasswordValidation() {
	   $password = $(resetPwd);
    var $fieldHolder = getFieldHolder($password);
    var currentValue = $password.val();
	  if (currentValue === "") {
        togglePasswordValidationVisibility(true, false, $fieldHolder);
        $(recoverpasswordP3recPassError).empty();
        disableResetPwdButton();
        return false;
    }
    if (isPasswordWeak()) {
        togglePasswordValidationVisibility(false, true, $fieldHolder);
        $(recoverpasswordP3recPassError).empty();
        disableResetPwdButton();
        return false;
    }
    togglePasswordValidationVisibility(false, false, $fieldHolder);
    if(handleResetConfirmPasswordValidation()) {
        enableResetPwdButton();
        return true;
    }
    return true;
}

function handleResetConfirmPasswordValidation() {
	    $resetPasswordConfirmation = $(""+resetConfirmPassword+".binded");
    var $fieldHolder = getFieldHolder($resetPasswordConfirmation);
    var currentValue = $resetPasswordConfirmation.val();
    if (currentValue === "") {
        togglePasswordValidationVisibility(true, false, $fieldHolder);
        $(recoverpasswordP3recPassError).empty();
        return false;
    }
    if (isPasswordWeak()) {
        togglePasswordValidationVisibility(false, false, $fieldHolder);
        $(recoverpasswordP3recPassError).empty();
        return false;
    }
    if (!isConfirmationPasswordEquals()) {
        disableResetPwdButton();
        $(recoverpasswordP3recPassError).empty();
        togglePasswordValidationVisibility(false, true, $fieldHolder);
        return false;
    }
    togglePasswordValidationVisibility(false, false, $fieldHolder);
    enableResetPwdButton();
    return true;
}

function disableResetPwdButton() {
	$(".emailvalidate").attr("disabled", "disabled");
}

function enableResetPwdButton() {
	$(".emailvalidate").removeAttr("disabled");
}

function togglePasswordValidationVisibility(displayPassword, displayValidPassword, $fieldHolder) {
	if (displayPassword) {
	    $fieldHolder.addClass(fieldError);
	    $fieldHolder.removeClass(fieldErrorValid);
	} else if (displayValidPassword) {
	    $fieldHolder.removeClass(fieldError);
	    $fieldHolder.addClass(fieldErrorValid);
	} else {
	    $fieldHolder.removeClass(fieldError);
	    $fieldHolder.removeClass(fieldErrorValid);
	}
}

function isPasswordWeak() {
	$password = $(resetPwd);
	var minPasswordStrong = 4;
	return getPasswordComplexity($password.val()) < minPasswordStrong;
}

function isConfirmationPasswordEquals() {
	$password = $(resetPwd);
	$resetPasswordConfirmation = $(resetConfirmPassword);
	return $password.val() === $resetPasswordConfirmation.val();
}

function bindResetPAssword(userEmail) {
	$('#resetYourPasswordBtn').click(function() {
	    if ($(recoverpasswordFormP3).valid()) {
	        createLoader();
	        var requestBody = new Object();
	        requestBody.email = $(userEmailIdSelector).length !== 0 ? $(userEmailIdSelector).val() : userEmail;
	        requestBody.email = userEmail ? userEmail : requestBody.email;
	        requestBody.password = $("#reset_password").val();
	        requestBody.token = $("#token").val();
	        jQuery.ajax({
	            type: 'POST',
	            url: ACC.config.contextPath + '/passwordReset/latamRevisionVerification',
	            data: requestBody,
	            success: emailResetSuccessHandler,
	            error: emailResetErrorHandler,
	            complete: function() {
	                removeLoader();
	            }
	        });
	    }
	});
}
$(window).load(function () {
    $( "#contractpage a" ).on("click",function() {
        if($(this).attr("aria-expanded")==="true"){
            $(this).find(".bi")
            .removeClass("bi bi-plus-lg")
            .addClass("bi bi-dash-lg")
        }
        else if($(this).attr("aria-expanded")==="false"){
            $(this).find(".bi")
            .removeClass("bi bi-dash-lg")
            .addClass("bi bi-plus-lg")
        }
    });

});
$( "#help-page a" ).on("click",function() {
    if($(this).attr("aria-expanded")==="true"){
        $(this).children(".bi")
        .removeClass("bi bi-plus")
        .addClass("bi bi-dash")
    }
    else if($(this).attr("aria-expanded")==="false"){
        $(this).children(".bi")
        .removeClass("bi bi-dash")
        .addClass("bi bi-plus")
    }
    });
    $( "#contractpage a" ).on("click",function() {
        if($(this).attr("aria-expanded")==="true"){
            $(this).find(".bi")
            .removeClass("bi bi-plus-lg")
            .addClass("bi bi-dash-lg")
        }
        else if($(this).attr("aria-expanded")==="false"){
            $(this).find(".bi")
            .removeClass("bi bi-dash-lg")
            .addClass("bi bi-plus-lg")
        }
        });

    $( "#searchResults a" ).on("click",function() {
        if($(this).attr("aria-expanded")==="true"){
            $(this).find(".bi")
            .removeClass("bi bi-plus-lg")
            .addClass("bi bi-dash-lg")
        }
        else if($(this).attr("aria-expanded")==="false"){
            $(this).find(".bi")
            .removeClass("bi bi-dash-lg")
            .addClass("bi bi-plus-lg")
        }
        });
        $( "#invoiceDetailsPage a" ).on("click",function() {
            if($(this).attr("aria-expanded")==="true"){
                $(this).children(".bi")
                .removeClass("bi bi-plus")
                .addClass("bi bi-dash")
            }
            else if($(this).attr("aria-expanded")==="false"){
                $(this).children(".bi")
                .removeClass("bi bi-dash")
                .addClass("bi bi-plus")
            }
            });
function toggleIconPlus(toggle){
$( "td a." + toggle ).on("click",function() {
    	if($(this).attr("aria-expanded")==="true"){
        	$(this).children(".bi")
        	.removeClass("bi bi-plus-lg")
        	.addClass("bi bi-dash-lg")
        }
    	else if($(this).attr("aria-expanded")==="false"){
    		$(this).children(".bi")
        	.removeClass("bi bi-dash-lg")
        	.addClass("bi bi-plus-lg")
    	}
    	});


};

function validateEmailInputData() {
    let allowChars = eval(document.getElementById("regex-chars").value);
    $('.email-validate').bind('input', function() {
        var selectedTxt = this.selectionStart, inpVal = $(this).val();
        if (allowChars.test(inpVal)) {
            $(this).val(inpVal.replace(allowChars, ''));
            selectedTxt--;
        }
        this.setSelectionRange(selectedTxt, selectedTxt);
    });
}

$(document).on('keypress paste', '.email-validate', function(e) {
    validateEmailInputData();
});

$(window).load(function() {
const ipadacctoggle = "ipadacctoggle";
const mobileacctoggle = "mobileacctoggle";
const mobileacctoggle2 = "mobileacctoggle2";
toggleIconPlus(ipadacctoggle);
toggleIconPlus(mobileacctoggle);
toggleIconPlus(mobileacctoggle2);
$("#Orderhistorypage #datatab-desktop_wrapper #datatab-desktop").removeClass("table-striped");
$("#Orderhistorypage #datatab-tablet_wrapper #datatab-tablet").removeClass("table-striped");
$("#Orderhistorypage #datatab-mobile_wrapper #datatab-mobile").removeClass("table-striped");

$("#Orderhistorypage #datatab-desktop_wrapper #datatab-desktop_paginate").addClass("p-0 m-0");
$("#Orderhistorypage #datatab-desktop_wrapper #datatab-desktop_info").addClass("p-0 m-0");

$("#Orderhistorypage #datatab-mobile_wrapper  #datatab-mobile_paginate").addClass("p-0 m-0");
$("#Orderhistorypage #datatab-mobile_wrapper  #datatab-mobile_info").addClass("p-0 m-0 ml-4");

$("#Orderhistorypage #datatab-tablet_wrapper #datatab-tablet_paginate").addClass("p-0 m-0");
$("#Orderhistorypage #datatab-tablet_wrapper #datatab-tablet_info").addClass("p-0 m-0");

})
