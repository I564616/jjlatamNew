
/*Report type change*/

/*$('#reportType').on('change', function() {
	var reportsUrl = this.value.trim();
	if(reportsUrl!= ""){

		localStorage.setItem('selectAllFlag',false)
		localStorage.setItem('currentAccountid',$("#accountid").val())
		window.location.href=reportsUrl;

	}
});
*/
/* Switch to other page making unchecked selectallacount checkbox ADDED by jayashre */
/*$(document).ready(function (){
	alert($("#hddnAccountsString").val()+"#hddnAccountsString");
	if($("#hddnAccountsString").val().length == $("#accountid").val().length){
		$(".selectAllAccount").prop("checked",false);
	}
});*/

//Add by surabhi
$('.sub-menu-item').click(function(){
		localStorage.setItem('selectAllFlag',false)
		localStorage.setItem('currentAccountid',$("#accountid").val())
});


//Add by Arvind
$("#openOrdersStartDate").change(function(){
	 $(".datepicker").css("display","none");
});
$("#openOrdersEndtDate").change(function(){
	 $(".datepicker").css("display","none");
});
$('#openOrdersEndtDate').on('click', function(){
    $("#ui-datepicker-div").remove();
	
});

$('#openOrdersStartDate').on('click', function(){
    $("#ui-datepicker-div").remove();
	 
});



/* Report type on change */
$('#reportTypes').on('change', function() {
	var reportUrl = this.value.trim();
	localStorage.setItem('selectAllFlag',false)
	localStorage.setItem('currentAccountid',$("#accountid").val())
	window.location.href =ACC.config.contextPath+reportUrl;

});

/*Report Category on change*/
$('#reportCategory').on('change',function(){
	var reportCategoryUrl = this.value.trim();
	localStorage.setItem('selectAllFlag',false)
	localStorage.setItem('currentAccountid',$("#accountid").val())
	window.location.href =ACC.config.contextPath+reportCategoryUrl;

});

/* set default start and end date for report page */
$(".date-picker").datepicker({
	format: DATE_FORMAT_TO_DISPLAY.toLowerCase()
});

var today = new Date();
var month = today.getMonth() - 1,
	year = today.getFullYear();
if (month < 0) {
	month = 11;
	year -= 1;
}
var oneMonthAgo = new Date(year, month, today.getDate());
if(localStorage.getItem('selectAllFlag')=='true'){
	$(".selectAllAccount").prop('checked',true);
}
else{
	$(".selectAllAccount").prop('checked',false);
	if($('#invoice-startDate').val()=="" || $('#invoice-startDate').val()==undefined)
	{
		$('#invoice-startDate').datepicker('update', new Date(oneMonthAgo));
	}
	if($('#invoice-endDate').val()=="" || $('#invoice-endDate').val()==undefined)
	{
		$('#invoice-endDate').datepicker('update', new Date());
	}
	/*if()
	$('#reports-startDate').datepicker('setDate', oneMonthAgo);
	$('#reports-endDate').datepicker('setDate', new Date());*/
}
$('.reset').click(function(){

	localStorage.setItem('selectAllFlag',false)

});
// checkbox on page
var currentAccountid;

$(".selectAllAccount").change(function(){

	if($(this).is(":checked")) {
		/* select all flag as checked true or false according to the condition of checkobx*/
		localStorage.setItem('selectAllFlag',true);

		var newObj = new Object();
		newObj.showMoreCounter = "1";
		newObj.showMore = "false";
		newObj.showMode = "Page";
		newObj.page = "0";
		newObj.fetchAll = true;
		jQuery.ajax({
			url: ACC.config.contextPath + '/reports/accountSelection',
			type : "POST",
			data : newObj,
			success: function (data) {
				loadingCircleShow("hide");
				var accounts=data.split(":");
				var acc=accounts[2].split('"');
				var acco=acc[1].split(",");
				$("#hddnAccountsString").val(acco);
				$("#ucnNo").val(acco);
				$('#changeAcntPopup').modal('hide');
				$("#selectedAccountsText").html($("#allText").val() + " (" + $("#hddnAccountsString").val().split(",").length + " " + $("#selectedText").val() + ")");
				checkBoxSort();
			}
		});

	} else {

		/* select all flag as checked true or false according to the condition of checkobx*/
		localStorage.setItem('selectAllFlag',false);
		//Added by surabhi
		if($("#accountid").val().length != 0){
			localStorage.setItem('currentAccountid',$("#accountid").val());

		}
		//end
		$("#hddnAccountsString").val($("#accountid").val());
		$("#selectedAccountsText").html(localStorage.getItem('currentAccountid'));
		$("#ucnNo").val($("#hddnAccountsString").val());
	}

});

/* Generate backorder submit button*/
$("#backOrderReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}

	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if($("#backOrderReportForm").valid()) {
		loadingCircleShow("show");
		$("#backOrderReportForm").submit();
	}
});

/*consingment inventory submit report*/

$("#consInventoryReportSubmit").click(function(){
	//alert("ss");
	//alert($("#hddnAccountsString").val()+"button");
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if($("#consignmentInventoryReportForm").valid()) {
		$("#consignmentInventoryReportForm").submit();
	}
	else {
		$("#consignmentInventoryReportForm").show();
	}

});

/*Generate inventory Report button*/
$("#inventoryReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if($("#inventoryReportForm").valid()) {
		$("#inventoryReportForm").submit();
	} else {
		$("#inventoryReportForm").show();
	}

});

/*AAOL #2419 Generate invoic report button */
$("#financialReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());


	if($("#financialReportForm").valid()) {
		$("#financialReportForm").submit();
	}
});

/*single product report generate submit button*/
$("#singlePurchaseReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if($("#singlePurchaseReportForm").valid()) {
		$("#singlePurchaseReportForm").submit();
	}
});


/** single product report generate submit button **/
$("#multiPurchaseReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if ($("#multiPurchaseReportForm").valid()) {
		$("#multiPurchaseReportForm").submit();
	}
});

/**AAOL-4603: OA Delivery List - Search button click action **/
$("#oadlReportSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if ($("#dlReportAnalysisForm").valid()) {
		$("#dlReportAnalysisForm").submit();
	}
});

/** Backorder Screen Validation Start **/

//AAOL-2410: Sales Report
$("#salesReportPageSubmit").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if ($("#salesReportAnalysisForm").valid()) {
		$("#salesReportAnalysisForm").submit();
	}
});

//AAOL-2410: invoic past due Report
$("#invoicePastDueReport").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if ($("#invoiceDueReportForm").valid()) {
		$("#invoiceDueReportForm").submit();
		console.log("form submitteed");
	}
});

//AAOL-2410: invoic clearing Report
$("#invoiceClearingReport").click(function(){
	if($(".selectAllAccount").prop('checked')==true){
		localStorage.setItem('selectAllFlag',true);
		localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
	}
	else{
		localStorage.setItem('selectAllFlag',false)
		if(localStorage.getItem('currentAccountid') !='undefined'){
			localStorage.setItem('currentAccountid',localStorage.getItem('currentAccountid'));
		}else{
			localStorage.setItem('currentAccountid',$("#accountid").val());
		}
	}
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	if ($("#invoiceClearingForm").valid()) {
		$("#invoiceClearingForm").submit();
		console.log("form submitteed");
	}
});

/**
 * Account selection Link click action
 */
$("#accountSelectionLink").click(function(){
	var flag = false;
	/*if($(".selectAllAccount").prop("checked") == true){
		alert("--")
		$("#selectAllAccount").prop("checked",true);
	}*/
	//alert($("#hddnAccountsString").val()+"on click");
	if($("#inventoryReportForm").length == 0) {
		$("#hddnTempAccountList").val($("#hddnAccountsString").val());
	} else {
		$("#hddnTempAccountList").val("," + $("#ucnNo").val());
		flag = true;
	}
	var dataObj = new Object();
	accountSelectionAjaxCall(dataObj, "", "", flag);
});

/**
 * Change accound popup
 * @param dataObj
 * @param pageNumber
 * @param searchmode
 * @param inventoryFlag
 */
var invAccntLength='';
function accountSelectionAjaxCall(dataObj, pageNumber, searchmode, inventoryFlag){


	if(null!=pageNumber && pageNumber!="") {
		dataObj.showMoreCounter = pageNumber;
		dataObj.showMore = "true";
	}else{
		dataObj.showMoreCounter = "1";
		dataObj.showMore = "false";
	}
	dataObj.showMode = "Page";
	dataObj.page = "0";
	var param = "";

	if(typeof $("#accountSelectionLink").attr("data") != 'undefined' && $("#accountSelectionLink").attr("data") != "") {
		dataObj.ucnFlag = $("#accountSelectionLink").attr("data");
		param = $("#accountSelectionLink").attr("data");
	} else {
		param = "";
	}
	/**
	 * Getting all available account.
	 */
	jQuery.ajax({
		url: ACC.config.contextPath + '/reports/accountSelection',
		type : "POST",
		data : dataObj,
		success: function (data) {
			/*
			 * Show Change account up
			 */
			var reportPopup=$(data);

			var accntList=[];

			$('#changeAccountPopupContainer').html(reportPopup);
			$('.modal-backdrop').hide();
			$('#changeAcntPopup').modal('show');
			// End here

			if(!$('#totalAccountsInModal').hasClass('valuePicked')){
				$('#totalAccountsInModal').val($('#invAccountList').val().split(',').length)
				invAccntLength=$('#totalAccountsInModal').val();
				$('#totalAccountsInModal').addClass('valuePicked')
			}

			var totalPages = $("#accountNumberOfPages").val();

			/* If form lenght is not equal to 0 */
			if($("#hddnAccountsString").val().split(",").length == invAccntLength) {
				/* set select all account check box in the popup*/
				console.log("select all account in popup checkbox checked");
				$("#selectAllAccount").attr("checked", true);
			}

			// TO DO
			if($("#hddnAccountsString").val().split(",").length == 1000) {
				$("#selectAllAccount").attr("checked", true);
			}

			if($("#isFinancialSummary").val()=="true")
			{
				$("#selectAllDiv").hide();
			}

			if(searchmode == "true" && $("#hddnTempAccountList").val() != null && $("#hddnTempAccountList").val()!="") {
				var tempAccounts = $("#hddnTempAccountList").val();
				$(tempAccounts).each(function(){
					$("#changeAccountPopupContainer").find("input.accountSelection[value='" + this + "']").attr("checked", true);
				});
			}else{
				if(inventoryFlag) {
					$($("#ucnNo").val().split(",")).each(function(){
						$("#changeAccountPopupContainer").find("input.accountSelection[value='" + this + "']").attr("checked", true);
					});
				}else{
					if($("#hddnCurrentAccount").val()!="" && $("#hddnAccountsString").val()==$("#hddnCurrentAccount").val()) {
						$("#changeAccountPopupContainer").find("input.accountSelection[value='" + $("#hddnCurrentAccount").val() + "']").attr("checked", true);
					}else{
						if($('#selectAllAccount').prop('checked')){
							$($("#hddnTempAccountList").val().split(",")).each(function(){
								$("#changeAccountPopupContainer").find("input.accountSelection[value='"+this+"']").attr("checked", true);
							});
						}
						else{
							if($("#hddnTempAccountList").val()!="" && $("#hddnTempAccountList").val()!=null){
								$($("#hddnTempAccountList").val().split(",")).each(function(){
									$("#changeAccountPopupContainer").find("input.accountSelection[value='"+this+"']").attr("checked", true);
								});
							}else{
								//$("#hddnTempAccountList").val($('#selectedAccountsText').text().trim(''));
								$("#changeAccountPopupContainer").find("input.accountSelection[value='"+$('#selectedAccountsText').text().trim('')+"']").attr("checked", true);
							}
						}
					}
				}
			}

			/*when checking/Unchecking  select all account check box from Popup (#selectAllAcount id for popup)  */
			$("#selectAllAccount").change(function(){
				console.log("when i am selecting Popup select all account check box");
				if($(this).is(":checked")) {
					var newObj = new Object();
					newObj.showMoreCounter = "1";
					newObj.showMore = "false";
					newObj.showMode = "Page";
					newObj.page = "0";
					newObj.fetchAll = true;
					jQuery.ajax({
						url: ACC.config.contextPath + '/reports/accountSelection',
						type : "POST",
						data : newObj,
						success: function (data) {
							console.log(data);
							var accounts=data.split(":");
							var acc=accounts[2].split('"');
							var acco=acc[1].split(",");
							console.log(acco+"all selected accounts");
							$("#hddnAccountsString").val(acco);
							$("#ucnNo").val(acco);
							//$('#changeAcntPopup').modal('hide');
							$(".selectAllAccount").prop("checked", true);
							$(".accountSelection").prop("checked", true);
							$("#selectedAccountsText").html($("#allText").val() + " (" + $("#hddnAccountsString").val().split(",").length + " " + $("#selectedText").val() + ")");

						}
					});

				} else {
				    $("#changeAccountPopupContainer .modalbody .accountsMargin").each(function(){
                        $(this).find("input").prop("checked",false);
                    });
					$(".accountSelection").attr("checked", false);/* unchecked alll accountSelection from popup  */
					$("#hddnTempAccountList").val("");
					$(".selectedAccountsText").html("0");
					$("#ucnNo").html("0");
					$(".selectAllAccount").prop("checked", false);
					$("#hddnAccountsString").val($("#accountid").val());
					$("#selectedAccountsText").html($("#hddnAccountsString").val());
				}
			});
			/** Single account select check-box click action on popup **/
			var currentAccountList="";
			var currentValueIndex='';
			var currentValue='';



			$(document).on('change','.accountSelection',function(){
				/*unselect any one account from popup*/
				if(!$(this).is(":checked")) {
					currentAccountList=$("#hddnTempAccountList").val();
					currentValue=$(this).val().trim('');
					currentValueIndex=currentAccountList.indexOf(currentValue);

					if($("#hddnTempAccountList").val().indexOf($(this).val())!=-1){
						/*if templist is not empty othewise else*/
//						if($("#hddnTempAccountList").val().split(",").length > 2){
//
//
//							if(currentAccountList.slice(currentValueIndex,currentValueIndex+(currentValue.length)+1).indexOf(',')>0){
//								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val()+',',""));
//							}
//							else{
//								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val(),""));
//							}
//
//
//							alert('uncheck if >> '+$("#hddnTempAccountList").val())
//						}else{
//							$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val(),""));
//							alert('uncheck else >> '+$("#hddnTempAccountList").val())
//						}

						if(currentAccountList.slice(currentValueIndex,currentValueIndex+(currentValue.length)+1).indexOf(',')>0){
							$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val()+',',""));
						}
						else{
							if(currentAccountList.indexOf(',')>0){
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace(','+$(this).val(),""));
							}
							else{
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val(),""));
							}
						}
					}

					$(".accountSelectionAll").prop("checked", false);
					$(".selectAllAccount").prop("checked", false);

				}else if (invAccntLength== $(".accountSelection:checked").length ) {

					// call when all account select from the list it will select all account
					if($("#hddnTempAccountList").val().indexOf($(this).val())==-1) {
						$("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
					}



					$(".accountSelectionAll").prop("checked", true);
					$(".selectAllAccount").prop("checked", true);
				} else {
					if($("#hddnTempAccountList").val() == "") {
						$("#hddnTempAccountList").val($("#hddnTempAccountList").val() +$(this).val());
						console.log($("#hddnTempAccountList").val()+"when empty");
					}else{
						if($("#hddnTempAccountList").val()!="" && $("#hddnTempAccountList").val().indexOf($(this).val())==-1){
							$("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
							console.log($("#hddnTempAccountList").val()+"contain list");
						}
					}
				}

				if($("#isFinancialSummary").val()=="true")
				{
					//alert("at financial summary");
					 var $box = $(this);
					  if ($box.is(":checked")) {
					    // the name of the box is retrieved using the .attr() method
					    // as it is assumed and expected to be immutable
					    var group = "input:checkbox[name='" + $box.attr("name") + "']";
					    // the checked state of the group/box on the other hand will change
					    // and the current value is retrieved using .prop() method
					    $(group).prop("checked", false);
					    $box.prop("checked", true);
					    $("#hddnTempAccountList").val($(this).val());accountid
					  }
				}
				else
				{
					checkBoxSort();
				}
			});
			/** Clear button click action **/
			$(".clearSelectAccountBtn").click(function(){
				accountSelectionAjaxCall(new Object(), "", "", false);
			});
			/** Account selection cancel action **/
			$(".accountSelectionCancel").click(function(){

				$("#hddnTempAccountList").val("");
				$('#changeAcntPopup').modal('hide');

			});
			$("#selectAccountNoAjax").keypress(function(e){
				var key = e.which;
				if(key == 13){// the enter key code
					$('#changeAcntPopup').modal('hide');
					$("#searchSelectAccountBtn").click();
				}
			});
			/** Search action **/
			$("#searchSelectAccountBtn").click(function(){

				if(null!=$("#selectAccountNoAjax").val() && $.trim($("#selectAccountNoAjax").val())!="") {
					var dataObj = new Object();
					dataObj.searchTerm = $.trim($("#selectAccountNoAjax").val());
					accountSelectionAjaxCall(dataObj, "", "true", false);
				}
			});
			/** Account selection okay action **/

			$(".accountSelectionOk,.accountSelectionOk").click(function(){

				var accountListHidden;
				if($(".selectAllAccount").prop('checked')==true){
					accountListHidden = $("#hddnAccountsString").val();
				}
				else{
					accountListHidden = $("#hddnTempAccountList").val(); //hddnTempAccountList usually , hddnAccountsString on select all
				}

				if($("#isFinancialSummary").val()=="true")
				{
				//alert("fetching payer ID for account ID>"+accountListHidden);
				fetchPayerID(accountListHidden);
				}

				$("#hddnTempAccountList").val("");
				if(param==""){

					/** each selected account loop **/
					var count;
					if($.trim(accountListHidden) == ""){
						count=0;
					}else{
						count = accountListHidden.split(",").length;
					}
					if(count!=0){
						if($(".selectAllAccount").prop('checked')==true){
						accountList = accountListHidden.substring(0, accountListHidden.length); // for comma - 1 else 0
						}
						else{
							accountList = accountListHidden.substring(1, accountListHidden.length);
						}
						/** Accounts string created and set in hidden input **/
						$("#hddnAccountsString").val(accountListHidden);
						$("#ucnNo").val(accountListHidden);

						/** Showing statement based on selection **/
						if(count == invAccntLength) {
							$("#selectedAccountsText").html($("#allText").val() + " (" + count + " " + $("#selectedText").val() + ")");
						} else if (count==1) {
							$("#selectedAccountsText").html(accountListHidden);
						} else {
							$("#selectedAccountsText").html($("#multipleText").val() + " (" + count + " " + $("#selectedText").val() + ")");
						}
						$('#changeAcntPopup').modal('hide');

					}
					if(count==0){
						$("#Errormassage").html(ACCOUNT_CHANGE);
					}

					if($(".cutReportPage").length!=0 && count!=0) {
						getShippingAddress(accountList);
					}
				} else {
					var accountList = [];
					/** each selected account loop **/
					if(accountListHidden!=""){
						if (accountListHidden.indexOf(",")==0) {
							accountList = accountListHidden.substring(1, accountListHidden.length).split(",");
						}else{
							accountList = accountListHidden.split(",");
						}
					}
					$("#ucnNo").val(accountList);
				}
				//localtorage of selectAllflag
				if($('#selectAllAccount').prop('checked')){
					localStorage.setItem('selectAllFlag',true)
				}
				else{
					localStorage.setItem('selectAllFlag',false)
				}
			});

			/** Color box for the account selection pop-up **/
			checkBoxSort();
		}
	});
}


/** Search button click action **/
if(null!=$("productCode")){
	$("productCode").val("");
}
/** this method is used to generate PDF or excel for BACKORDER **/
$("#backOrderReportExcel,#backOrderReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#backOrderReportForm").attr("action", ACC.config.contextPath +'/reports/backorder/downloadReport');
	$("#backOrderReportForm").submit();
	$("#backOrderReportForm").attr("action", $("#originalFormAction").val());
});

/** AAOL #2419 this method is used to generate PDF or excel for FINANCIAL INVOICE REPORT **/
$("#financialReportExcel,#financialReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#financialReportForm").attr("action", ACC.config.contextPath +'/reports/financial/downloadReport');
	$("#financialReportForm").submit();
	$("#financialReportForm").attr("action", $("#originalFormAction").val());
});

/** AAOL #2421 this method is used to generate PDF or excel for FINANCIAL SUMMARY REPORT **/
$("#financialSummaryReportExcel,#financialSummaryReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#financialSummaryReportForm").attr("action", ACC.config.contextPath +'/reports/financialSummary/downloadReport');
	$("#financialSummaryReportForm").submit();
	$("#financialSummaryReportForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for InvoiceDue **/
$("#InvoiceDueReportExcel,#InvoiceDueReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#invoiceDueReportForm").attr("action", ACC.config.contextPath +'/reports/financial/invoicedownloadReport');
	$("#invoiceDueReportForm").submit();
	$("#invoiceDueReportForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for Cut Report**/
$("#cutOrderReportExcel,#cutOrderReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#cutOrderReportForm").attr("action", ACC.config.contextPath +'/reports/cutorder/downloadReport');
	$("#cutOrderReportForm").submit();
	$("#cutOrderReportForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for Multi PA report **/
$(".multiPurchaseReportBlock .excel, .multiPurchaseReportBlock .pdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#multiPurchaseReportForm").attr("action", ACC.config.contextPath +'/reports/purchaseAnalysis/multi/downloadReport');
	$("#multiPurchaseReportForm").submit();
	$("#multiPurchaseReportForm").attr("action", $("#originalFormAction").val());
});

/** AAOL-4603: this method is used to generate PDF or excel for OA Delivery List report **/
$(".oadlReportBlock .excel, .oadlReportBlock .pdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#dlReportAnalysisForm").attr("action", ACC.config.contextPath +'/reports/orderAnalysis/dl/downloadReport');
	$("#dlReportAnalysisForm").submit();
	$("#dlReportAnalysisForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for INVENTORY **/
$("#inventoryReportForm .excel, #inventoryReportForm .pdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#inventoryReportForm").attr("action", ACC.config.contextPath +'/reports/inventory/downloadReport');
	$("#inventoryReportForm").submit();
	$("#inventoryReportForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for singleproduct **/
$(".singlePurchaseReportBlock .excel, .singlePurchaseReportBlock .pdf").click(function(){

	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#singlePurchaseReportForm").attr("action", ACC.config.contextPath +'/reports/purchaseAnalysis/single/downloadReport');
	$("#singlePurchaseReportForm").submit();
	$("#singlePurchaseReportForm").attr("action", $("#originalFormAction").val());
});

/** AAOL-2410 this method is used to generate PDF or excel for SalesReport **/
$(".salesReportBlock .excel, .salesReportBlock .pdf").click(function(){

	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#salesReportAnalysisForm").attr("action", ACC.config.contextPath +'/reports/salesReport/downloadReport');
	$("#salesReportAnalysisForm").submit();
	$("#salesReportAnalysisForm").attr("action", $("#originalFormAction").val());
});


/** this method is used to generate PDF or excel for consignment Report**/
$("#consignmentReportPdf,#consignmentReportExcel").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!= -1 ? "EXCEL" : "PDF");
	$("#consignmentInventoryReportForm").attr("action", ACC.config.contextPath +'/reports/inventoryAnalysis/consignmentInventory/downloadReport');
	$("#consignmentInventoryReportForm").submit();
	$("#consignmentInventoryReportForm").attr("action", $("#originalFormAction").val());
});

/** this method is used to generate PDF or excel for InvoiceClearing AAOL-2426 **/
$("#InvoiceClearingReportExcel,#InvoiceClearingReportPdf").click(function(){
	$("#downloadType").val($(this).attr("class").indexOf("excel")!=-1 ? "EXCEL" : "PDF");
	$("#invoiceClearingForm").attr("action", ACC.config.contextPath +'/reports/financial/invoiceClearingdownloadReport');
	$("#invoiceClearingForm").submit();
	$("#invoiceClearingForm").attr("action", $("#originalFormAction").val());
});

/** Search button click action for DEFECT  JJEPIC - 913 -Valli
 **/
$("#disputeOrderReportSubmit").click(function(){
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
if($("#disputeOrderReportForm").valid()) {
	$("#ajaxCallOverlay").fadeTo(0, 0.6);
	$("#modal-ui-dialog").fadeTo(0, 0.6);
	$("#disputeOrderReportForm").submit();
}
else
	{
	$("#errMsg").show();
	}
});
/** Search button click action **/

var today = new Date();
var threemonthBack = today.getMonth() - 3,
	year = today.getFullYear();
if (month < 0) {
	month = 11;
	year -= 1;
}
var threeMonthAgo = new Date(year, threemonthBack, today.getDate());
/*AAOL-5794* start*/
if($("#selectAllReports").length>0){
	if($("#selectAllReports").val().trim('')!='false'){
		$("#allreports").prop('checked',true);
	}
	else{
		$("#allreports").prop('checked',false);
	}
	startDateDisEn(false);
}


$("#allreports").click(function(){
	 startDateDisEn(true);
});

 function startDateDisEn(flag){
	 if ($("#allreports").is(':checked')) {
		 $("#reports-fromDate").val('');
			$("#reports-toDate").val('');
		 document.getElementById("reports-toDate").disabled = true;
		document.getElementById("reports-fromDate").disabled = true;
		$(".redStar").hide();
	 }else{
		 if(null!=document.getElementById("reports-toDate") && null!=document.getElementById("reports-fromDate")){
			 document.getElementById("reports-toDate").disabled = false;
				document.getElementById("reports-fromDate").disabled = false;

				if(flag){
					$("#reports-fromDate").datepicker('update',threeMonthAgo);

					$("#reports-toDate").datepicker('setDate',today);
				}

				$(".redStar").show();
		 }
	 }
 }

 /*AAOL-5794-end*/

//Account selection Sorting
function checkBoxSort(){
		var cbl = $('.accountsMargin');
	    if (cbl.length) {
	    	var cbElements= $('.accountsMargin').filter(function() {
	            return $(this).find('input:checked').length;
	        });
	    	  cbElements.each(function() {
	          	$(this).parent().prepend($(this));
	          });
	    }
	}

/**************************************
 * DOCUMENT READY
 **************************************/
$(document).ready(function () {
	//alert($("#productCode").val())
	//AAOL-5153 - remove extra spaces
	if($("#productCode")!=undefined && $("#productCode").length>0){
	    $("#productCode").val($("#productCode").val().trim(''));}
	if($("#deliveryNo")!=undefined && $("#deliveryNo").length>0){
		$("#deliveryNo").val($("#deliveryNo").val().trim(''));}
	if($("#salesDocNo")!=undefined && $("#salesDocNo").length>0){
		$("#salesDocNo").val($("#salesDocNo").val().trim(''));}
	if($("#invoiceNo")!=undefined && $("#invoiceNo").length>0){
		$("#invoiceNo").val($("#invoiceNo").val().trim(''));}
	if($("#customerPONo")!=undefined && $("#customerPONo").length>0){
		$("#customerPONo").val($("#customerPONo").val().trim(''));}
	if($("#custPONum")!=undefined && $("#custPONum").length>0){
		$("#custPONum").val($("#custPONum").val().trim(''));}
	if($("#deliveryNum")!=undefined && $("#deliveryNum").length>0){
		$("#deliveryNum").val($("#deliveryNum").val().trim(''));}


	if($(".cutReportPage").length!=0) {
		//alert($("#hddnAccountsString").val());
		getShippingAddress($("#hddnAccountsString").val());
	}

		$(document).on("click",".showSingleReport",function(){

		localStorage.setItem('selectAllFlag',false)


		var singleReportForm = jQuery('<form>', {
			'action' : ACC.config.contextPath + '/reports/orderAnalysis/single',
			'id' : 'singleReportForm',
			'name': 'jnjGlobalSinglePurchaseAnalysisReportForm',
			'method' : 'POST'
		}).append(jQuery('<input>', { //  Account UID input created
			'name' : 'accountIds',
			'value' : $("#hddnAccountsString").val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { //  Account Name input created
			'name' : 'startDate',
			'value' : $("#reports-startDate").val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { //  Account GLN input created
			'name' : 'endDate',
			'value' : $("#reports-endDate").val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { //  Account Name input created
			'name' : 'orderedFrom',
			'value' : $("#orderedFrom").val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { //  Account Name input created
			'name' : 'productCode',
			'value' : $(this).parent().parent().find(".hddnPrdCode").val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { // Account GLN input created
	    	'name' : 'CSRFToken',
	    	 'value' : ACC.config.CSRFToken,
	    	 'type' : 'hidden'
		}));
		$(".sectionBlock").append(singleReportForm);
		/** Submitting the dummy form **/
		$("#singleReportForm").submit();
	});
	/** Method allows only numeric values in the prodCode field **/
	$("#inventoryReportForm #prodCode").keydown(function (e) {
		// Allow: backspace, delete, tab, escape and enter
		if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
		// Allow: Ctrl+A
		(e.keyCode == 65 && e.ctrlKey === true) ||
		// Allow: home, end, left, right
		(e.keyCode >= 35 && e.keyCode <= 39)) {
			// let it happen, don't do anything
			return;
		}
		// Ensure that it is a number and alphbets and stop the keypress
		 var key = e.keyCode;
			if (!((key == 8) || (key == 32)
			|| (key == 46)
			|| (key >= 35 && key <= 40)
			|| (key >= 65 && key <= 90)
			|| (key >= 48 && key <= 57) || (key >= 96 && key <= 105))) {
			e.preventDefault();
		}
	});
	/** Prevent pasting in prodCode field **/
	/*$("#inventoryReportForm #prodCode").bind("paste", function(e) {
		e.preventDefault();
    }); */ //commenting for jjepic-712

	/** Adding the preselected accounts text if any **/
	if($("#selectedAccountsText").text()==""){
		$("#selectedAccountsText").text($("#hddnAccountsSelectedValue").val());
	}
	/** condition added for back-order total value **/
	if($(".backOrderPage .reportBody").length!=0) {
		var total = 0.00;
		$(".extendedPriceField").each(function(){
			total += parseFloat(this.value);
		});
		$(".backOrderPage .total").append('$' + total.toFixed(2));
	}
	//Fix for Show hyperlink issue in 2nd page of search result page
	$(document).on('click',".showEntries",function(){


		var hddnAccountNumber = $(this).parent().find(".hddnAccountNumber").val();
		var dataObj = new Object();
		dataObj.accountId = $("#hddnAccountsString").val();
		dataObj.productCode = $("#productCode").val();
		dataObj.orderedFrom = $("#orderedFrom").selected().val();
		dataObj.lotNumber = $("#lotNumber").val();
		var dates = $($(this).parent().parent().find("span")[0]).text().split(" ");
		dataObj.periodFrom = dates[0];
		dataObj.periodTo = dates[2];
		$.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/reports/purchaseAnalysis/single/entries',
			data: dataObj,
			success: function (data) {
				var reportPopup1=$(data);
				$('#orderproductReportPopupAdd').html(reportPopup1);
				$('.modal-backdrop').hide();
				$('#OrderedProductpopup').modal('show');
			},
			error: function (e) {
			}
		});
	});
	/** START : Date setup for reports **/
	/** Changes for JJEPIC-733 **/
	$(".orderReport #startDate").datepicker({
		maxDate: 0,
		minDate: "-2y",
		inline: true,
		onSelect: function(selected) {
			var fromDateInit = new Date(selected);
			var dateToSet = new Date();
			dateToSet.setMonth(fromDateInit.getMonth());
			dateToSet.setFullYear(fromDateInit.getFullYear());
			dateToSet.setDate(fromDateInit.getDate());
			$(".orderReport #endDate").datepicker("option","minDate", dateToSet);
    	}
	});
	if($(".orderReport #startDate").val()==""){
		$(".orderReport #startDate").datepicker("setDate", "-1m");
	} else {
		$(".orderReport #startDate").datepicker("setDate", new Date($(".orderReport #startDate").val()));
	}
	var fromDateInit = new Date($(".orderReport #startDate").val());
	var dateToSet = new Date();
	dateToSet.setMonth(fromDateInit.getMonth());
	dateToSet.setFullYear(fromDateInit.getFullYear());
	dateToSet.setDate(fromDateInit.getDate());
	$(".orderReport #endDate").datepicker({
		maxDate: 0,
		minDate: dateToSet,
		inline: true,
		onSelect: function(selected) {
			var fromDateInit = new Date(selected);
			var dateToSet = new Date();
			dateToSet.setMonth(fromDateInit.getMonth());
			dateToSet.setFullYear(fromDateInit.getFullYear());
			dateToSet.setDate(fromDateInit.getDate());
			$(".orderReport #startDate").datepicker("option","maxDate", dateToSet);
		}
	});
	if($(".orderReport #endDate").val()==""){
		$(".orderReport #endDate").datepicker("setDate", new Date());
	} else {
		$(".orderReport #endDate").datepicker("setDate", new Date($(".orderReport #endDate").val()));
	}
	/** END : Date setup for reports **/
	/** Start: for cut report date **/
	/**Code changes for JJEPIC-733**/

	var date = new Date();
	  var today = new Date(date.getFullYear()-1, date.getMonth(), date.getDate());
	  var end = new Date(date.getFullYear(), date.getMonth(), date.getDate());
	  var dateDefault = new Date(date.getFullYear(), date.getMonth()-1, date.getDate());
	 $('#cutReportPoStartDate')
     .datepicker({
    	 startDate: today,
    	 endDate:end,
    	 useCurrent: true,
    	 autoclose: true,
    	 defaultdate: dateDefault,


     })  .on('changeDate', function(selected) {
    	  var minDate = new Date(selected.date.valueOf());
          $('#cutReportPoEndDate').datepicker('setStartDate', minDate);
     });


	 $('#cutReportPoEndDate')
     .datepicker({

    	 endDate:end,
    	 autoclose: true,


     })

     if($("#cutReportPoStartDate").val()==""){
 		$("#cutReportPoStartDate").datepicker("update", dateDefault);

 	}else{
 		$("#cutReportPoEndDate").datepicker("update", end);
 	}
	 if($("#cutReportPoEndDate").val()==""){
	 		$("#cutReportPoEndDate").datepicker("setDate", end);
	 	}

	/** End: for cut report date **/
	/*2410 - 4982 */
	$("#salesDocNo").keypress(function (e) {

	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	         return false;
	    }
	   });
	 $("#invoiceNo").keypress(function (e) {

	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	         return false;
	    }
	   });
 $("#deliveryNo").keypress(function (e) {

     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
         return false;
    }
   });


	 if($('#reports-startDate').val() == '' || $('#reports-startDate') == null || $('#reports-startDate') == undefined){
		$('#reports-startDate').datepicker('update', oneMonthAgo);

		/* $('#reports-startDate').datepicker({
	        dateFormat  : 'dd-mm-yy',
	        setDate : '-2m'
	    });*/
	 }

	 if($('#reports-endDate').val() == '' || $('#reports-endDate') == null || $('#reports-endDate') == undefined){
			$('#reports-endDate').datepicker('update', new Date());
	 }

	 if($('#invoiceStartDate').val() == '' || $('#invoiceStartDate') == null || $('#invoiceStartDate') == undefined){
		 $('#invoiceStartDate').datepicker('update', oneMonthAgo);
	 }
	 if($('#invoiceEndDate').val() == '' || $('#invoiceEndDate') == null || $('#invoiceEndDate') == undefined){
			$('#invoiceEndDate').datepicker('update', new Date());
	 }

	});


$("#cutsortBy").on('change',function(){

	if($(this).val()=='sort1'){
		$('#orderDate').addClass('sorting_asc').click();
	}
	else if($(this).val()=='sort2'){
		$('#poNumber').addClass('sorting_asc').click();
	}
	else if($(this).val()=='sort3'){
		$('#orderNumber').addClass('sorting_asc').click();
	}else if($(this).val()=='sort4'){
		$('#orderDate').addClass('sorting_asc').click();
	}else if($(this).val()=='sort5'){
		$('#cutReason').addClass('sorting_asc').click();
	}
});

$(".showViewDetails").click(function() {
	if($(this).parent().parent().next().css('display') == "none"){
	var dataObj = new Object();
	var orderEntryDiv = $(this);
	var order = $(this).closest("div.reportRow");
	dataObj.orderNumber = order.find(".orderNumber span a").html();

	$.ajax({
		type: 'POST',
		url: ACC.config.contextPath +'/reports/viewDetails',
		data: dataObj,
		success: function (data) {

					var divShow = orderEntryDiv.parent().parent().next();
					if (orderEntryDiv.parent().parent().next().hasClass('viewDetails'))
						{
						$(divShow).find(".reportBody").remove();
						$(divShow).append(data);
						}
					$(".cutReportPage .viewDetails .reportBody .reportRow span").each(function(){
						if($.trim($(this).html())==""){
							$(this).attr("style","display:inline-block");
						}
					});
		},
		error: function (e) {
		}
	})
	}
});


function getShippingAddress(accountList){
	//TODO :write method for getting the dropdown for the shipping addresses
	accountList = accountList.replace("_",",");
	var data = new Object();
	data.accounts = accountList;
	$.ajax({
		type: 'POST',
		url: ACC.config.contextPath +'/reports/shipToDropdown',
		data: data,
		success: function (data) {
			var options="<option value='All'>"+$('#allField').val()+"</option>";
			var phoneOption="";
			for(var i=0;i<data.length;i++){
				if($("#shiptohdn").val()==data[i].id)
						{
						options=options+ "<option selected value="+data[i].id+">"+data[i].line1 + "," +data[i].postalCode+"</option>";
						}
					else
						{
					options=options+ "<option value="+data[i].id+">"+data[i].line1 + "," +data[i].postalCode+"</option>";
						}
			}
			$("#shipTo").html(options);
			$(".selectshiptopicker").selectpicker('refresh');
		},
		error: function (e) {
		}
	})
}

$("#cutOrderReportSubmit").click(function(){
	$("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text());
	$("#cutOrderReportForm").submit();
});

$(document).ready(function(){

	$("#operatingCompany").bind('change', function() {
		var categoryElement = this;
		if(this.value === "All")
			{
			$(".franchiseCode").hide();
			}
		else
			{
			$(".franchiseCode").show();
			}
		if(null != categoryElement && '' != categoryElement && null!= categoryElement.value){
			var category = categoryElement.value;

			var dataObj = new Object();
		dataObj.operatingCompany = category;
		jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/reports/franchiseCode',
			data: dataObj,
			success: function (data) {

				var options=ALL
				$.each(data,function(index,element){
					options = options + "<option value=" + index + ">"
					+ element + "</option>";
				})
				$("#franchise").html(options);

			},
			error: function (e) {
			}
		});}
	});

	if($("#operatingCompany").val() != "All")
		{$(".franchiseCode").show();}
	else{$(".franchiseCode").hide();}

	//Backorder report
	var reportLabel=$('#allreport-label').html();
	$('#allreport-label').remove();
	$(reportLabel).insertAfter($('#allreports'));

	//Inventory report
	var delivLabel=$('#delivered-label').html();
	$('#delivered-label').remove();
	$(delivLabel).insertAfter($('#stocks'));
});

	//STARTS the story 2421
$('#accountAgingPayer').change( function(e) {
	//alert("enter into div accountAgingPayer");
       var payer= $("#accountAgingPayer").val();
       $("#accountAgingPayerID").val($("#accountAgingPayer").val());
       console.log( $("#accountAgingPayerID").val()+" value for the accountAgingPayerID");
      // alert(payer);
       if(payer!= undefined && payer!= ""){
             fetchAccountAgingReport(payer);
       }else{
    	   alert("invalid payer value");
       }
});

 function fetchAccountAgingReport(payer){
	//alert("enter into the ajex call accountAgingPayer");
	 var accountId=$("#hddnAccountsString").val();
		console.log('vlue of payer at fetchAccountAgingReport>'+payer);
       var url = ACC.config.contextPath+"/reports/financialAnalysis/accountAging?CSRFToken="+ACC.config.CSRFToken+"&payer="+payer+"&accountid="+accountId;
       jQuery.ajax({
              type: "POST",
              url: url,
              async: false
       }).done(function(responseData) {
    	   $("#accountAgingDiv").html(responseData);
       });
	   setTimeout(ACC.globalActions.menuNdbodyHeight,100);
}


 $('#balanceSummaryPayer').change( function(e) {
	//alert("enter into div balanceSummaryPayer");
       var payer= $("#balanceSummaryPayer").val();
       $("#balanceSummaryPayerID").val($("#balanceSummaryPayer").val());
       console.log( $("#balanceSummaryPayerID").val()+" value for the balanceSummaryPayerID");
      // alert(payer);
       if(payer!= undefined && payer!= ""){
             fetchBalanceSummaryReport(payer);
       }else{
    	   alert("invalid payer value");
       }
});

 function fetchBalanceSummaryReport(payer){
	//alert("enter into the ajex call balanceSummaryPayer");
	 var accountId=$("#hddnAccountsString").val();
		console.log('vlue of payer at fetchBalanceSummaryReport>'+payer);
       var url = ACC.config.contextPath+"/reports/financialAnalysis/balanceSummary?CSRFToken="+ACC.config.CSRFToken+"&payer="+payer+"&accountid="+accountId;
       jQuery.ajax({
              type: "POST",
              url: url,
              async: false
       }).done(function(responseData) {
    	   $("#balanceSummaryDiv").html(responseData);
       });
	   setTimeout(ACC.globalActions.menuNdbodyHeight,100);
}


 $('#paymentSummaryPayer').change( function(e) {
		//alert("enter into div paymentSummaryPayer");
	       var payer= $("#paymentSummaryPayer").val();
	      // alert(payer);
	       $("#paymentSummaryPayerID").val($("#paymentSummaryPayer").val());
	       console.log( $("#paymentSummaryPayerID").val()+" value for the paymentSummaryPayerID");
	       if(payer!= undefined && payer!= ""){
	             fetchPaymentSummaryPayerReport(payer);
	       }else{
	    	   alert("invalid payer value");
	       }
	});

function fetchPaymentSummaryPayerReport(payer){
		//alert("enter into the ajex call paymentSummaryPayer");
	var accountId=$("#hddnAccountsString").val();
			console.log('vlue of payer at fetchPaymentSummaryPayerReport>'+payer);
	       var url = ACC.config.contextPath+"/reports/financialAnalysis/paymentSummary?CSRFToken="+ACC.config.CSRFToken+"&payer="+payer+"&accountid="+accountId;
	       jQuery.ajax({
	              type: "POST",
	              url: url,
	              async: false
	       }).done(function(responseData) {
	    	   $("#paymentSummaryDiv").html(responseData);
	       });
		   setTimeout(ACC.globalActions.menuNdbodyHeight,100);
}


 $('#creditSummaryPayer').change( function(e) {
	//alert("enter into div creditSummaryPayer");
       var payer= $("#creditSummaryPayer").val();
      // alert(payer);
       $("#creditSummaryPayerID").val($("#creditSummaryPayer").val());
       console.log( $("#creditSummaryPayerID").val()+" value for the creditSummaryPayerID");
       if(payer!= undefined && payer!= ""){
             fetchCreditSummaryPayerReport(payer);
       }else{
    	   alert("invalid payer value");
       }
});

 function fetchCreditSummaryPayerReport(payer){
	//alert("enter into the ajex call creditSummaryPayer");
		console.log('vlue of payer at fetchCreditSummaryPayerReport>'+payer);
		var accountId=$("#hddnAccountsString").val();
       var url = ACC.config.contextPath+"/reports/financialAnalysis/creditSummary?CSRFToken="+ACC.config.CSRFToken+"&payer="+payer+"&accountid="+accountId;
       jQuery.ajax({
              type: "POST",
              url: url,
              async: false
       }).done(function(responseData) {
    	   $("#creditSummaryDiv").html(responseData);
       });
	   setTimeout(ACC.globalActions.menuNdbodyHeight,100);
}

 function fetchPayerID(accountid){
	       var url = ACC.config.contextPath+"/reports/financialAnalysis/getPayer?CSRFToken="+ACC.config.CSRFToken+"&accountid="+accountid;
	       jQuery.ajax({
	              type: "GET",
	              url: url,
	              async: false
	       }).done(function(responseData) {
	    	   if(responseData!="")
	    	   {
	    		   responseData="Select,"+responseData;
	    		   var items=responseData.split(",");
	    		   $('#accountAgingPayer').find('option').remove().end();
	    		   $('#balanceSummaryPayer').find('option').remove().end();
	    		   $('#paymentSummaryPayer').find('option').remove().end();
	    		   $('#creditSummaryPayer').find('option').remove().end();
		    	   $.each(items, function (i, item) {
		    		    $('#accountAgingPayer').append($('<option>', {
		    		        value: item,
		    		        text : item
		    		    }));
		    		    $('#balanceSummaryPayer').append($('<option>', {
		    		        value: item,
		    		        text : item
		    		    }));
		    		    $('#paymentSummaryPayer').append($('<option>', {
		    		        value: item,
		    		        text : item
		    		    }));
		    		    $('#creditSummaryPayer').append($('<option>', {
		    		        value: item,
		    		        text : item
		    		    }));
		    		});
		    	   $('.selectpicker').selectpicker('refresh');
		    	   $("#accountAgingDiv").text("");
		    	   $("#paymentSummaryDiv").text("");
		    	   $("#balanceSummaryDiv").text("");
		    	   $("#creditSummaryDiv").text("");
	    	   }
	    });
 }
//ENDS the story 2421

//AFFG-26067 openOrders Report
var autocompletArray = [];
var closeButtonArray = [];
var orderNumberID = "#orderNumber", openOrdersOrderTypeID = "#openOrdersOrderType", openOrdersQuickSelectionID = "#openOrdersQuickSelection", ProductCodeID = "#ProductCode",
openOrdersStartDateID = "#openOrdersStartDate", openOrdersEndtDateID = "#openOrdersEndtDate", selectTemplateNameID = "#selectTemplateName",
openOrdersColumnListID = "#openOrdersColumnList", openOrdersReportTableHeadingClass = ".openOrdersReportTable table thead th", selectColumnListID = "#selectColumnList";

var tableHeightAlighement;
var outLineToggelSwithch;
var bottomBorderClass = ".bottomBorder", openOrdersReportTableID = "#openOrdersReportTable";
var leftSideExpandClass = 'col-lg-3 col-md-2', leftSideCollapseClass = 'col-lg-1 col-md-1';
var rightSideExpandClass = 'col-lg-11 col-md-11', rightSideCollapseClass = 'col-lg-9 col-md-10';

$(document).ready(function() {
    dataInitilization();
    setDateOnQuickSelect();
    openOrdersResetButtonClick();
    openOrdersGenerateButtonClick();
    saveTemplateButtonClick();
    setValueForMultiSelect();
    displayTableOnGenerateReport();
    showColumnsBasedOnSelect();
    addRemoveColumnOption();
    removeButtonFromList();
    setTheFormObjectData();
    paginationHandler();
    resetButtonHandler();
    $("#hddnAccountsString").val($("#hddnAccountsSelectedValue").val());
    tableHeightAlighement = $(openOrdersReportTableID).height();
    paginationDropdown();
});

function paginationHandler() {
    $(".openOrdersReportTable #datatab-desktop_paginate").click(function() {
        hideTableDataBasedOnHeading();
    });
}

function resetAllData() {
    $(openOrdersOrderTypeID).selectpicker('val', 'all');
    $(selectColumnListID).selectpicker("val", "default");
    $(openOrdersColumnListID).val($(selectColumnListID).val());
    $(openOrdersQuickSelectionID).selectpicker('val', '7');
    $("#shipTO").val("");
    $(ProductCodeID).val("");
    $(orderNumberID).val("");
    var dateObj = new Date();
    dateObj.setDate(dateObj.getDate() - parseInt(7));
    $(openOrdersStartDateID).datepicker("setDate", dateObj);
    $(openOrdersEndtDateID).datepicker("setDate", new Date());
    $(selectTemplateNameID).selectpicker("val", "default");
    $("#selectedAccountsText").text($("#accountid").val());
    $(".selectAllAccount").prop("checked", false);
    $("#hddnAccountsString").val($("#accountid").val());
}

function setTheTemplateValue(selectedValue) {
    let selectValId;
    $("#getTempDataList input").each(function() {
        if ($(this).attr("templatename") === selectedValue){
            selectValId = $(this).attr("id");
        }
    });
    if (selectValId !== undefined) {
        let getAccoutnId = $("#" + selectValId).attr("accountids");
        let selectQuickDay = $("#" + selectValId).attr("quickselection");
        let getOrderType = $("#" + selectValId).attr("ordertype");
        let getOrderNumber = $("#" + selectValId).attr("ordernumber");
        let getProductCode = $("#" + selectValId).attr("productcode");
        let getShipTo = $("#" + selectValId).attr("shipto");
        let listOfColumns = $("#" + selectValId).attr("reportcolumns");
        $(openOrdersQuickSelectionID).val(selectQuickDay).change();
        $(openOrdersQuickSelectionID).selectpicker('refresh');
        $(openOrdersOrderTypeID).selectpicker('val', getOrderType);
        $(openOrdersOrderTypeID).selectpicker('refresh');
        $(orderNumberID).val(getOrderNumber);
        $(ProductCodeID).val(getProductCode);
        $("#shipTO").val(getShipTo);
        if (listOfColumns.split(",").length === 1) {
            $(selectColumnListID).selectpicker('val', listOfColumns);
            $(selectColumnListID).selectpicker('refresh');
        } else {
            $(selectColumnListID).selectpicker('val', listOfColumns.split(","));
            $(selectColumnListID).selectpicker('refresh');
        }
        if (getAccoutnId === '[allAccountSelected]' && $(".selectAllAccount").prop("checked") === false) {
            $(".selectAllAccount").click();
        } else {
            if (getAccoutnId.split(",").length === 1 && getAccoutnId !== '[allAccountSelected]') {
                $("#selectedAccountsText").text(getAccoutnId.substr(1, getAccoutnId.length - 2));
                $("#hddnAccountsString").val(getAccoutnId.substr(1, getAccoutnId.length - 2));
                $(".selectAllAccount").prop("checked", false);
            } else {
                $("#selectedAccountsText").text(`Multiple (${getAccoutnId.split(",").length} selected)`);
                $("#hddnAccountsString").val(getAccoutnId.substr(1, getAccoutnId.length - 2));
                $(".selectAllAccount").prop("checked", false);
            }
        }
    }
}

function resetButtonHandler(){
    if($(selectTemplateNameID).val() === "default"){
        $(".OpenOrderRepurtButton .deletTempDiv").css('display','none');
        $(".OpenOrderRepurtButton .resetBtnDiv").css('display','');
    }else{
        $(".OpenOrderRepurtButton .deletTempDiv").css('display','inline-block');
        $(".OpenOrderRepurtButton .resetBtnDiv").css('display','none');
    }
}

$(selectTemplateNameID).change(function(e) {
    resetButtonHandler();
    let selectedValue = $(this).val();
    localStorage.setItem("currentTemp", selectedValue);
    if (selectedValue === "default") {
        resetAllData();
    } else {
        setTheTemplateValue(selectedValue);
    }
});

function setTheFormObjectData() {
    let retrievedFormData = localStorage.getItem('formObjectData');
    let getFormObject = JSON.parse(retrievedFormData);
    if (getFormObject !== null) {
        $(selectTemplateNameID).selectpicker('val', getFormObject.templateName);
        $(openOrdersQuickSelectionID).val(getFormObject.quickSelection).change();
        $(openOrdersQuickSelectionID).selectpicker('refresh');
        $(openOrdersOrderTypeID).selectpicker('val', getFormObject.orderType);
        $(openOrdersOrderTypeID).selectpicker('refresh');
        $(orderNumberID).val(getFormObject.orderNumber);
        $(ProductCodeID).val(getFormObject.productCode);
        $("#shipTO").val(getFormObject.shipTo);
        $(openOrdersStartDateID).val(getFormObject.startDate);
        $(openOrdersEndtDateID).val(getFormObject.endDate);
        let getColumnList = getFormObject.reportColumns;
        $(selectColumnListID).selectpicker('val', getColumnList);
        getColumnList.push("default");
        $(selectColumnListID).selectpicker("val", getColumnList);
        $(openOrdersColumnListID).val($(selectColumnListID).val());
        $(selectColumnListID).selectpicker('refresh');
        if (getFormObject.accountIds === 'allAccountSelected' && getFormObject.accountIds.split(",").length === 1 && $(".selectAllAccount").prop("checked") === false) {
            $(".selectAllAccount").click();
        } else {
            if (getFormObject.accountIds.split(",").length === 1) {
                $("#selectedAccountsText").text(getFormObject.accountIds);
                $("#hddnAccountsString").val(getFormObject.accountIds);
            } else {
                $("#selectedAccountsText").text(`Multiple (${getFormObject.accountIds.split(",").length} selected)`);
                $("#hddnAccountsString").val(getFormObject.accountIds);
            }
            $(".selectAllAccount").prop("checked", false);
        }
        localStorage.removeItem('formObjectData');
    }
}

function openOrdersGenerateButtonClick() {
    $("#runReport").click(function(e) {
        let formObjectData = new Object();
        formObjectData.quickSelection = $(openOrdersQuickSelectionID).val();
        formObjectData.fromDate = $(openOrdersStartDateID).val();
        formObjectData.toDate = $(openOrdersEndtDateID).val();
        formObjectData.orderType = $(openOrdersOrderTypeID).val();
        formObjectData.orderNumber = $(orderNumberID).val();
        formObjectData.productCode = $(ProductCodeID).val();
        formObjectData.shipTo = $("#shipTO").val();
        formObjectData.reportColumns = $(selectColumnListID).val().length === 0 ? "default" : $(selectColumnListID).val();
        formObjectData.templateName = $(selectTemplateNameID).val();
        formObjectData.startDate = $(openOrdersStartDateID).val();
        formObjectData.endDate = $(openOrdersEndtDateID).val();
        if ($(".selectAllAccount").prop("checked")) {
            formObjectData.accountIds = "allAccountSelected";
        } else {
            formObjectData.accountIds = $("#hddnAccountsString").val();
        }
        localStorage.setItem('formObjectData', JSON.stringify(formObjectData));
        localStorage.setItem('buttonClicked', 'submitted');
        localStorage.setItem("columnSelect", $(selectColumnListID).val());
        if ($("#hddnAccountsString").val() === '') {
            $("#hddnAccountsString").val($("#accountid").val());
        }
    });
}

function dataInitilization() {
    if ($('.selectAllAccount').is(':checked') && localStorage.getItem("selectAllFlag")) {
        $('.selectAllAccount').prop('checked', false);
    }
    var quickSelectDate = $(openOrdersQuickSelectionID).val();
    var dateObj = new Date();
    dateObj.setDate(dateObj.getDate() - parseInt(quickSelectDate));
    $(openOrdersStartDateID).datepicker("setDate", dateObj);
    $(openOrdersEndtDateID).datepicker("setDate", new Date());
    $(openOrdersStartDateID).datepicker(addFormat({
        format: 'mm/dd/yyyy',
        autoclose: true,
        endDate: '0'
    }));
    $(openOrdersEndtDateID).datepicker(addFormat({
        format: 'mm/dd/yyyy',
        autoclose: true,
        endDate: '0'
    }));
    $(openOrdersStartDateID).change(function(){
       $('.datepicker-dropdown').css("display",'none')
    });
    $(openOrdersEndtDateID).change(function(){
       $('.datepicker-dropdown').css("display",'none')
    });
}

function setDateOnQuickSelect() {
    $(openOrdersQuickSelectionID).change(function() {
        var quickSelectDate = $(openOrdersQuickSelectionID).val();
        var dateObj = new Date();
        dateObj.setDate(dateObj.getDate() - parseInt(quickSelectDate));
        $(openOrdersStartDateID).datepicker("setDate", dateObj);
        $(openOrdersEndtDateID).datepicker("setDate", new Date());
    });
}

function openOrdersResetButtonClick() {
    $(".OpenOrderRepurtButton .resetButton").click(function() {
        resetAllData();
    });
    $(".OpenOrderRepurtButton .deletTempBtn").click(function() {
        let delTemp = new Object();
        delTemp.templateName = $(selectTemplateNameID).val();
        if (delTemp.templateName !== '') {
            jQuery.ajax({
                url: ACC.config.contextPath + '/reports/openordersreport/deletetemplate',
                type: "POST",
                data: delTemp,
                success: function(data) {
                    location.reload();
                }
            });
            location.reload();
        }
    });
}

function setValueForMultiSelect() {
    $("#hddnAccountsSelectedValue").val($("#selectedAccountsText").text().trim());
    $("#hiddenCurrentAccount").val($("#selectedAccountsText").text().trim());
    $(selectColumnListID).change(function() {
        $(openOrdersColumnListID).val($(selectColumnListID).val());
    });
}

function saveTemplateButtonClick() {
    $(".templatebutton").click(function() {
        $('#openOrdersTemplate').modal('show');
        $("#saveTemplateButton").click(function() {
            if ($("#saveTemplate").val() === '') {
                $(".tempError").css("display", "block");
            } else {
                $(".tempError").css("display", "none");
                makeAjaxCallForSavetemp();
            }
        });
    });
}

function makeAjaxCallForSavetemp() {
    if ($("#hddnAccountsString").val() === '') {
        $("#hddnAccountsString").val($("#accountid").val());
    }
    let dataForAjaxCall = new Object();
    dataForAjaxCall.quickSelection = $(openOrdersQuickSelectionID).val();
    dataForAjaxCall.fromDate = $(openOrdersStartDateID).val();
    dataForAjaxCall.toDate = $(openOrdersEndtDateID).val();
    dataForAjaxCall.orderType = $(openOrdersOrderTypeID).val();
    dataForAjaxCall.orderNumber = $(orderNumberID).val();
    dataForAjaxCall.productCode = $(ProductCodeID).val();
    dataForAjaxCall.shipTo = $("#shipTO").val();
    dataForAjaxCall.reportColumns = $(openOrdersColumnListID).val() === "" ? "default" : $(openOrdersColumnListID).val();
    dataForAjaxCall.templateName = $("#saveTemplate").val();
    if ($(".selectAllAccount").prop("checked")) {
        dataForAjaxCall.accountIds = "allAccountSelected";
    } else {
        dataForAjaxCall.accountIds = $("#hddnAccountsString").val();
    }
    jQuery.ajax({
        url: ACC.config.contextPath + '/reports/openordersreport/savetemplate',
        type: "POST",
        data: dataForAjaxCall,
        success: function(data) {
            if (data === "error") {
                $(".tempErrorDuplicate").css("display", "");
            } else {
                $('#openOrdersTemplate').modal('hide');
                location.reload();
            }
        }
    });
}

function displayTableOnGenerateReport() {
    if (localStorage.getItem('buttonClicked') === 'submitted') {
        $(openOrdersReportTableID).css('display', 'flex');
        localStorage.removeItem('buttonClicked');
    } else if (localStorage.getItem('buttonClicked') === 'reset') {
        $(openOrdersReportTableID).css('display', 'none');
        localStorage.removeItem('buttonClicked');
    }
}

function showColumnsBasedOnSelect() {
    var selectColumnArray;
    var getSelectedColumn = localStorage.getItem("columnSelect");
    if (getSelectedColumn !== null) {
        selectColumnArray = getSelectedColumn.split(',');
    } else {
        selectColumnArray = [];
    }
    localStorage.removeItem("columnSelect");
    $(openOrdersReportTableHeadingClass).each(function() {
        if ($(this).hasClass("default") || selectColumnArray.includes($(this).text().trim())) {
            $(this).addClass("showColumn");
        } else {
            var columnID = $(this).prop('class').split(' ')[1];
            $("." + columnID).css('display', 'none');
            $(this).addClass("hideColumn");
        }
    });
}

function addRemoveColumnOption() {
    $(openOrdersReportTableHeadingClass).each(function() {
        if ($(this).hasClass("showColumn")) {
            closeButtonArray.push($(this).text().trim());
        } else if ($(this).hasClass("hideColumn")) {
            autocompletArray.push($(this).text().trim());
        }
    });
    $(".columnButtons label").each(function() {
        var buttonList = $(this).text().trim();
        if (!closeButtonArray.includes(buttonList)) {
            $(this).css('display', 'none');
        }
    });
    autocomplet(autocompletArray);
}

function autocomplet(autocompletArray) {
    $(function() {
        $("#columnSearch").autocomplete({
            source: autocompletArray,
            change: function(e, ui) {
                $("#columnSearch").val('');
            },
            select: function(e, ui) {
                var selectedText = ui.item.value;
                addButtonAndShowCloumn(selectedText);
                autocompletArray.remove(selectedText);
            }
        });
    });
}

function addButtonAndShowCloumn(selectedText) {
    $(".columnButtons label").each(function() {
        var buttonText = $(this).text().trim();
        if (buttonText === selectedText) {
            $(this).css('display', '');
        }
    });
    $(openOrdersReportTableHeadingClass).each(function() {
        if ($(this).text().trim() === selectedText) {
            var getColumnNumber = $(this).prop('class').split(' ')[1];
            $('.' + getColumnNumber).css('display', '');
            $('thead tr .' + getColumnNumber).removeClass("hideColumn");
            $('thead tr .' + getColumnNumber).addClass("showColumn");
        }
    });
}

function removeButtonFromList() {
    $('.columnButtons .columeCloseIcon').click(function() {
        var selectedVal = $(this).parent().text().trim();
        $(this).parent().css('display', 'none');
        removeColumeFromTable(selectedVal);
    });
}

function removeColumeFromTable(selectedVal) {
    $(openOrdersReportTableHeadingClass).each(function() {
        if ($(this).text().trim() === selectedVal) {
            var columnId = $(this).prop('class').split(' ')[1];
            $('.' + columnId).css("display", "none");
            $(this).addClass("hideColumn");
            $(this).removeClass("showColumn");
        }
    });
    autocompletArray.push(selectedVal);
    autocomplet(autocompletArray);
}

$(bottomBorderClass).click(function(){
    if(outLineToggelSwithch){
       expandSideMenu();
       outLineToggelSwithch = false;
    }else{
        collapseSideMenu();
        outLineToggelSwithch = true;
    }
});

function collapseSideMenu(){
    $(openOrdersReportTableID).children().each(function(){
        if($(this).hasClass(leftSideExpandClass)){
            $(this).find('p, input, .columnButtons').hide();
            $(this).removeClass(leftSideExpandClass);
            $(this).addClass(leftSideCollapseClass);
            $(this).css("padding","7px");
        }
        if($(this).hasClass(rightSideCollapseClass)){
            $(this).removeClass(rightSideCollapseClass);
            $(this).addClass(rightSideExpandClass);
        }
    });
}

function expandSideMenu(){
    $(openOrdersReportTableID).children().each(function(){
        if($(this).hasClass(leftSideCollapseClass)){
            $(this).find('p, input, .columnButtons').show();
            $(this).removeClass(leftSideCollapseClass);
            $(this).addClass(leftSideExpandClass);
            $(this).css("padding","7px");
        }
        if($(this).hasClass(rightSideExpandClass)){
            $(this).removeClass(rightSideExpandClass);
            $(this).addClass(rightSideCollapseClass);
        }
    });
}

function paginationDropdown(){
    $("#openOrdersReportTable #datatab-desktop_length select").change(function(){
        hideTableDataBasedOnHeading();
        setTimeout(function(){
            var bodyContentHeight = $("#jnj-body-content").height();
            $("#jnj-usr-details-menu").height(bodyContentHeight + 270);
        },500);
    })
}

function hideTableDataBasedOnHeading(){
    $(".ajaxTableWrapperContent thead tr th").each(function() {
        if ($(this).hasClass("hideColumn")) {
            let thisIdName = $(this).attr('id');
            $("tr #" + thisIdName).css("display", "none");
        } else {
            let thisIdName = $(this).attr('id');
            $("tr #" + thisIdName).css("display", "");
        }
    });
}

