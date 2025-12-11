


$("#fromDateLaudo").datepicker();
$("#fromDateLaudo").on('changeDate', function(ev){
var dateString = ((ev.date.getMonth() > 8) ? (ev.date.getMonth() + 1) : ('0' + (ev.date.getMonth() + 1))) + '/' + ((ev.date.getDate() > 9) ? ev.date.getDate() : ('0' + ev.date.getDate())) + '/' + ev.date.getFullYear();
$("#fromDateLaudo").val(dateString);
    $(this).datepicker('hide');
});

$("#toDateLaudo").datepicker();
$("#toDateLaudo").on('changeDate', function(ev){
var dateString = ((ev.date.getMonth() > 8) ? (ev.date.getMonth() + 1) : ('0' + (ev.date.getMonth() + 1))) + '/' + ((ev.date.getDate() > 9) ? ev.date.getDate() : ('0' + ev.date.getDate())) + '/' + ev.date.getFullYear();
$("#toDateLaudo").val(dateString);
    $(this).datepicker('hide');
});

$("#popUpExpirationDate").datepicker();
$("#popUpExpirationDate").on('changeDate', function(ev){
var dateString = ((ev.date.getMonth() > 8) ? (ev.date.getMonth() + 1) : ('0' + (ev.date.getMonth() + 1))) + '/' + ((ev.date.getDate() > 9) ? ev.date.getDate() : ('0' + ev.date.getDate())) + '/' + ev.date.getFullYear();
$("#popUpExpirationDate").val(dateString);
    $(this).datepicker('hide');
});

$("#datepickerform0").datepicker();
$("#datepickerform0").on('changeDate', function(ev){
var dateString = ((ev.date.getMonth() > 8) ? (ev.date.getMonth() + 1) : ('0' + (ev.date.getMonth() + 1))) + '/' + ((ev.date.getDate() > 9) ? ev.date.getDate() : ('0' + ev.date.getDate())) + '/' + ev.date.getFullYear();
$("#datepickerform0").val(dateString);
	$("#datepickerform0-error").hide();
	$(this).attr("aria-invalid")==false;
	$(this).removeClass("error");
	$(this).addClass("valid");
    $(this).datepicker('hide');
});

$("#firstFakeButton1").click(function(){
    $("#browseSingleFileLaudo").click();

});


$("#firstFakeButton2").click(function(){
    $("#browseCsvFileLaudo").click();

});

$("#firstFakeButton3").click(function(){
    $("#browseMultiFileLaudo").click();

});

$("#consignment_back").click(function(){
	window.location.href = ACC.config.contextPath + "/services";
});



$(document).on('click','#select-account-close, #la-select-accnt-close',function(){
	$("#manageLaudoUploadLightBox").modal('hide');
	loadingCircleShow("show");
	location.reload();
});


$(document).ready(function(){
	$("#downloadExcelCrossReference1").click(function(){
				var originalAction = $("#crossReferenceForm").attr("action");
				$("#crossReferenceForm").attr("action", ACC.config.contextPath +"/services/crossReferenceTable/downloadCrossReferenceData?downloadType=EXCEL");
				$("#crossReferenceForm").submit();
				$("#crossReferenceForm").attr("action", originalAction);
			});

			$("#downloadPdfCrossReference1").click(function(){
				var originalAction = $("#crossReferenceForm").attr("action");
				$("#crossReferenceForm").attr("action",ACC.config.contextPath +"/services/crossReferenceTable/downloadCrossReferenceData?downloadType=PDF");
				$("#crossReferenceForm").submit();
				$("#crossReferenceForm").attr("action", originalAction);
			});

});



$("#consignmentIssue-agree").change(function() {
    if($(this).is(":checked")) {
        $("#consignmentIssueSend").removeAttr("disabled");
        $("#consignmentIssueSend").removeClass("btn-disabled-style");
    }
    else {
        $("#consignmentIssueSend").attr("disabled", "disabled");
        $("#consignmentIssueSend").addClass("btn-disabled-style");
    }
});

$("#consignmentIssueSend").click(function(){
	$("#consignmentIssuePage #successMessage").hide();
	$('#itemerrorpanel').hide();
	$('#itemerrorpanel2').hide();
	$('#group_0').find(".cellError").remove();

	var form = $("#consignmentIssueValidate");
    form.append(jQuery('<input>', {
        'name' : 'CSRFToken',
        'value' : ACC.config.CSRFToken,
        'type' : 'hidden'}));
   form.validate();
    if(form.valid()){
    	var qtdInputs = $(".column2").find("input");
    	$(qtdInputs).css("border","");
    	var validity = true;
    	var validity2=true;
    	for(var i=0; i<qtdInputs.length; i++) {
    		if(qtdInputs[i].value != '' && isNaN(qtdInputs[i].value)) {
    			$('#itemerrorpanel').hide();
    			$('#itemerrorpanel2').hide();
    			$(qtdInputs[i]).css("border","1px solid #D70817");
    			validity = false;
    		}
    	}
    	if(!validity) {
    		$('#group_0').append($('#nanError').html());
    		return false;
    	}
    	var rows = $("#group_0").find(".consignmentIssueRow");
    	for (var i=0; i<rows.length; i++) {
    	if(rows.length == 1){
    		var inputs = $(rows[i]).find("input");
    		for (var j=0; j<inputs.length; j++) {
    			if($(inputs[j]).val()=="" && j != 4) {
    				validity = false;
    				break;
    			}
    		}
    		if (!validity) {
    			break
    		}
    	}
    	else
    		{

    		var inputs = $(rows[i]).find("input");
    		for (var j=0; j<inputs.length; j++) {
    			if($(inputs[j]).val()=="" && j != 4) {
    				validity2 = false;
    				break;
    			}
    		}
    		if (!validity2) {
    			break
    		}

    		}
    	}
    	if (!validity) {
    		$('#itemerrorpanel').show();
    		return false;
    	}
    	if (!validity2) {
    		$('#itemerrorpanel2').show();
    		return false;
    	}
        form.submit();
    }

});

$("#consignmentIssueValidate").validate({
	rules: {
		contactEmail: {
			required: true,
			email: true
		},
		contactPhone: {
			required: true
		}
	},
	messages:{
		contactEmail: {
			required: $("#errorMessageConsignment").val(),
			email: $("#emailInvalidError").val()
		},
		contactPhone: {
			required: $("#errorMessageConsignment").val()
		},
		date: $("#errorMessageConsignment").val()
	},
	errorPlacement: function (error, element) {
	    if(element.is("select")) {
	        error.appendTo(element.parent().parent().parent().find('div.registerError'));
	    } else {
		    error.appendTo(element.parent().parent().find('div.registerError'));
	    }
	},
	onfocusout: false,
	focusCleanup: false

});

var formSendSuccessfulResult = $("#isFormSendSuccessful").val();
if (formSendSuccessfulResult == "true"){
    $("#consignmentIssuePage #successMessage").show();
}
else if (formSendSuccessfulResult == "false"){
    $("#consignmentIssuePage #errorMessage").show();
}

$('a.addMoreLines').on('click', function (e) {
	bindAddMoreLinesFunction(this, e)
});
$('a.removeLineslatam').on('click', function (e) {
	bindRemoveLinesFunction(this, e)
});

var countAddLineArr = [0]
$('.removeLineslatam').hide();
function bindAddMoreLinesFunction(thisObj, event) {
	$('#itemerrorpanel').hide();
	$('#itemerrorpanel2').hide();
	$(thisObj).parent().find(".tableErrorForms").remove();
	var thisRow = $(thisObj).parent().parent().parent();
	var currentGroupCount = parseInt(thisRow.parent().attr("id").split("_")[1]);
	countAddLineArr[currentGroupCount] = countAddLineArr[currentGroupCount] + 1;
	var rowClass;

	if (countAddLineArr[currentGroupCount] >= 0) {
		$('#removeLines_' + currentGroupCount).show();
	}
	event.preventDefault();
	if (countAddLineArr[currentGroupCount] % 2 == 0) {
		rowClass = "even";
	} else {
		rowClass = "odd";
	}

	var rowObject = $("<tbody class=\"consignmentIssueRow additionalRow " + rowClass + 	"\"><tr><td><div class=\"column1\"><input name=\"item["+countAddLineArr[currentGroupCount]+"]\" type=\"text\"  class=\"form-control autowidth\" /></div></td><td><div class=\"column2\"><input name=\"qty[" + countAddLineArr[currentGroupCount] + "]\" type=\"text\" class=\"form-control autowidth\" /></div></td><td><div class=\"column3\"><input name=\"uom[" + countAddLineArr[currentGroupCount] + "]\" type=\"text\" class=\"form-control autowidth\" /></div></td><td><div class=\"column4\"><input name=\"batchNumber[" + countAddLineArr[currentGroupCount] + "]\" type=\"text\" class=\"form-control autowidth\" /></div></td></tr></tbody>");
	$("#consignmentTable").append(rowObject);
}

function bindRemoveLinesFunction(thisObj, event) {
	$('#itemerrorpanel').hide();
	$('#itemerrorpanel2').hide();
	$(thisObj).parent().find(".tableErrorForms").remove();
	var currentGroupCount = parseInt($(thisObj).parent().parent().parent().parent().attr("id").split("_")[1]);
	countAddLineArr[currentGroupCount] = countAddLineArr[currentGroupCount] - 1;
	event.preventDefault();
	if (countAddLineArr[currentGroupCount] >= 0) {
		$(".additionalRow:last-child").remove();
		if (countAddLineArr[currentGroupCount] == 0) {
			$('#removeLines_' + currentGroupCount).hide();
		}
	} else {
		$('#removeLines_' + currentGroupCount).hide();
		return false;
	}
}


$(document).on('click','#select-accnt-close',function(){

		$("#manageLaudoUploadLightBox").modal('hide');
});

$(function(){
    prepareClsUploadDeliveryDates()
});

function prepareClsUploadDeliveryDates(){
    var mainContext = '#cls-upload-delivery-dates'
    var formContext = '#upload-delivery-date'
    var uploadButtons = formContext + ' [role=button]'
    var fileElement = $("input[type=file]", formContext)
    var submitButton = $("input[type=submit]", formContext)
    var fileName = $(".fileName", formContext)

    $(mainContext).on('click', uploadButtons, function(){
        $(fileElement).click();
    });

    $(fileElement).change(function(){
        var name = fileElement[0].files[0] ? fileElement[0].files[0].name : null
        var validName = name && name.match(/.xls$/)

        if (name && !validName) {
            alert($("#homeUploadFileWrongFormat").val())
        }
        
		processFile();
		async function processFile() {
    try {
		var input = document.getElementById("clsuploadEdiFile");
        const file = input.value; // Replace with the actual file
        const resultFinals = await uploadAndScanFile(file);
		if(validName && resultFinals==true){
        fileName.html(name)
        $(submitButton).removeClass('hidden')
        $(fileName).removeClass('hidden')
        }else if(resultFinals==false){
		$('#malware-detail-popup').modal('show');
        $(submitButton).addClass('hidden')
        $(fileName).addClass('hidden')
	}
	} catch (error) {
        console.error('Error processing file:', error);
    }
}

});

}