/**
 * Account selection Link click action
 */
$("#addAccount").click(function(){
	getAccountSelectionAjax(true, "","");
});

function getAccountSelectionAjax(isFirstClick, pageNumber, searchTerm){
	var dataObj = new Object();
	if(null!=pageNumber && pageNumber!="") {
		dataObj.showMoreCounter = pageNumber;
		dataObj.showMore = "true";
	}else{
		dataObj.showMoreCounter = "1";
		dataObj.showMore = "false";
	}
	if(null!=searchTerm && searchTerm!=""){
		dataObj.searchTerm = searchTerm;
	}
	dataObj.showMode = "Page";
	dataObj.page = "0";
	jQuery.ajax({
		type: 'POST',
		async: false,
		data: dataObj,
		url: ACC.config.contextPath + '/resources/usermanagement/accountsSelection',
		success: function (data) {
			/** Color box for the account selection pop-up **/
			$.colorbox({
				html : data,
				height : 'auto',
				width : '580px',
				onComplete : function() {
					$("#changeAccountPopupContainer").show();
					$.colorbox.resize();
					var preselectedAccounts = "";
					/** If clicked on add accounts link **/
					if(isFirstClick){
						$("#hddnTempAccountList").val($("#hddnAccountsString").val());
						$("#hddnTempAccountNameList").val($("#hddnOriginalTempAccountNameList").val());
						if(null!=$("#hddnAccountsString").val() && $("#hddnAccountsString").val()!="") {
							if($("#hddnAccountsString").val().indexOf(",")==0){
								$(".selectedAccountsText").html($("#hddnAccountsString").val().split(",").length - 1);
							}else{
								$(".selectedAccountsText").html($("#hddnAccountsString").val().split(",").length);
							}
						}else{
							$(".selectedAccountsText").html("0");
						}
					} else { // In case of search or load more
						if(null!=$("#hddnTempAccountList").val() && $("#hddnTempAccountList").val()!="") {
							if($("#hddnTempAccountList").val().indexOf(",")==0){
								$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length - 1);
							}else {
								$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length);
							}
						} else {
							$(".selectedAccountsText").html("0");
						}
					}
					if($("#hddnTempAccountList").val().indexOf(",")==0){
						preselectedAccounts = $("#hddnTempAccountList").val().substring(1, $("#hddnTempAccountList").val().length);
					} else {
						preselectedAccounts = $("#hddnTempAccountList").val();
					}
					$(preselectedAccounts.split(",")).each(function(){
						
						var checkedAcc = $("input.accountSelection[value='" + this + "']");
						checkedAcc.attr("checked", true);
						/*alert("input.accountSelection[value='" + this + "']");*/
						
						var liItem = checkedAcc.closest('li');
						
						while (liItem.prev().is('li')) {
							liItem.insertBefore(liItem.prev());
						}
						
						/*var liItem = $("#changeAccountPopupContainer").find("input.accountSelection[value='" + this + "']").closest("li");
						while (liItem.prev().is('li')) {
							liItem.insertBefore(liItem.prev());*/
							//test
					});
					if($(".accountSelection").length!=0 && $(".accountSelection").length==$(".accountSelection:checked").length){
						$("#selectAllAccount").attr("checked", true);
					} else {
						$("#selectAllAccount").attr("checked", false);
					}
					/** Select all check-box click action **/
					if($("#multiPurchaseReportForm").length==0 && $("#singlePurchaseReportForm").length==0 && $("#backOrderReportForm").length==0 && $("#cutOrderReportForm").length==0 && $("#orderHistoryForm").length==0) {
						$("#selectAllAccount").change(function(){
							if($(this).is(":checked")) {
								$(".accountSelection").each(function(){
									if(!$(this).is(":checked")) {
										$(this).attr("checked", true);
										$(this).trigger("change");
									}
								});
							} else {
								$(".accountSelection").each(function(){
									if($(this).is(":checked")) {
										$(this).attr("checked", false);
										$(this).trigger("change");
									}
								});
							}
							if(null==$("#hddnTempAccountList").val() || $("#hddnTempAccountList").val()==""){
								$(".selectedAccountsText").html("0");
							} else if($("#hddnTempAccountList").val().indexOf(",")==0) {
								$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length - 1);
							}else{
								$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length);
							}
						});
					}
					/** Single account select check-box click action **/
					$(".accountSelection").change(function(){
						if(!$(this).is(":checked")) {
							if($("#hddnTempAccountList").val().indexOf("," + $(this).val())!=-1){
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace("," + $(this).val(),""));
							}else if ($("#hddnTempAccountList").val().indexOf($(this).val())!=-1){
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val(),""));
							}
							if($("#hddnTempAccountNameList").val().indexOf("|" + $(this).val() + "**" + $(this).attr("data").split("_")[1])!=-1){
								$("#hddnTempAccountNameList").val($("#hddnTempAccountNameList").val().replace("|" + $(this).val() + "**" + $(this).attr("data").split("_")[1],""));
							}else if ($("#hddnTempAccountNameList").val().indexOf($(this).val() + "**" + $(this).attr("data").split("_")[1])!=-1){
								$("hddnTempAccountNameList").val($("#hddnTempAccountNameList").val().replace($(this).val() + "**" + $(this).attr("data").split("_")[1],""));
							}
							$(".accountSelectionAll").attr("checked", false);
						} else if ($(".accountSelection").length == $(".accountSelection:checked").length) {
							if($("#hddnTempAccountList").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
							}
							if($("#hddnTempAccountNameList").val().indexOf($(this).val() + "**" + $(this).attr("data").split("_")[1])==-1) {
								$("#hddnTempAccountNameList").val($("#hddnTempAccountNameList").val() + "|" + $(this).val() + "**" + $(this).attr("data").split("_")[1]);
							}
							$(".accountSelectionAll").attr("checked", true);
						} else {
							if($("#hddnTempAccountList").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
							}
							if($("#hddnTempAccountNameList").val().indexOf($(this).val() + "**" + $(this).attr("data").split("_")[1])==-1) {
								$("#hddnTempAccountNameList").val($("#hddnTempAccountNameList").val() + "|" + $(this).val() + "**" + $(this).attr("data").split("_")[1]);
							}
						}
						if(null==$("#hddnTempAccountList").val() || $("#hddnTempAccountList").val()=="") {
							$(".selectedAccountsText").html("0");
						} else if($("#hddnTempAccountList").val().indexOf(",")==0) {
							$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length - 1);
						}else{
							$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length);
						}
					});
					var totalPages = $("#accountNumberOfPages").val();
					$("#changeAccountNoAjax").change(function(){
						if((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="") 
							&& $(this).val()!=null && $.trim($(this).val())!=""){
							$("#clearSelectAccountBtn").attr("disabled", false);
							$("#clearSelectAccountBtn").removeClass("tertiarybtn");
							$("#clearSelectAccountBtn").addClass("primarybtn");
						} else if ((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="") 
								&& ($(this).val()==null || $.trim($(this).val())=="")){
							$("#clearSelectAccountBtn").attr("disabled", true);
							$("#clearSelectAccountBtn").removeClass("primarybtn");
							$("#clearSelectAccountBtn").addClass("tertiarybtn");
						}
					});
					if(totalPages>1){
						$("a.loadMoreAccounts").attr("style", "display:block;text-align:center");
						$("a.loadMoreAccounts").click(function(){
							var searchTerm = $("#accountSearchTerm").val();
							getAccountSelectionAjax(false, parseInt($("#currentPage").val(), 10) + 1, searchTerm);
						});
						$.colorbox.resize();
					} else {
						$("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
					}
					/** Clear button click action **/
					$(".clearSelectAccountBtn").click(function(){
						getAccountSelectionAjax(false, "","");
					});
					$("#selectAccountNoAjax").keypress(function(e){
						var key = e.which;
						if(key == 13){// the enter key code
							$("#searchSelectAccountBtn").click();
						}  
					});
					/** Search button action **/
					$("#searchSelectAccountBtn").click(function(){
						if(null!=$("#selectAccountNoAjax").val() && $.trim($("#selectAccountNoAjax").val())!="") {
							getAccountSelectionAjax(false, "", $.trim($("#selectAccountNoAjax").val()));
						}
					});
					/** Account selection cancel action **/
					$(".accountSelectionCancel").click(function(){
						$("#hddnTempAccountList").val("");
						$("#hddnTempAccountNameList").val("");
						$.colorbox.close();
					});
					/** Account selection okay action **/
					$("#accountSelectionOk").click(function(){
						var accountList = "";
						var accountNames = "";
						/** each selected account loop **/
						var count = 0;
						
						var splitAccounts="";
						var splitAccountNames="";
						var splitAccountNamesArr = [];
						if($("#hddnTempAccountList").val()==""){
							$("#hddnAccountsString").val("");
							$("#selectedAccountstext").html("");
						}else if($("#hddnTempAccountList").val().indexOf(",")==0){
							splitAccounts = $("#hddnTempAccountList").val().substring(1, $("#hddnTempAccountList").val().length);
						}else{
							splitAccounts = $("#hddnTempAccountList").val();
						}
						if($("#hddnTempAccountNameList").val()!="" && $("#hddnTempAccountList").val()!=""){
							if($("#hddnTempAccountNameList").val().indexOf("|")==0) {
								splitAccountNames = $("#hddnTempAccountNameList").val().substring(1, $("#hddnTempAccountNameList").val().length);
							}else{
								splitAccountNames = $("#hddnTempAccountNameList").val();
							}
							splitAccountNamesArr = splitAccountNames.split("|");
						}
						$(splitAccounts.split(",")).each(function(){
							var account = this+"";
							accountList += "," + account;
							if(typeof splitAccountNamesArr[count] !='undefined') {
								var accountName = splitAccountNamesArr[count].split("**")[1];
								accountNames += "," + account+"("+ accountName +")";
							}
							count++;
						});
						if(count!=0){
							accountList = accountList.substring(1, accountList.length);
							accountNames = accountNames.substring(1, accountNames.length);
							/** Accounts string created and set in hidden input **/
							$("#hddnAccountsString").val(accountList);
							$("#selectedAccountstext").html(accountNames);
							/** Showing statement based on selection **/
						}
						$("#hddnOriginalTempAccountNameList").val($("#hddnTempAccountNameList").val());	
						$.colorbox.close();
						
					});
					if($(".accountSelection").length!=0 && $(".accountSelection:checked").length == $(".accountSelection").length){
						$("#selectAllAccount").attr("checked", true);
					} else {
						$("#selectAllAccount").attr("checked", false);
					}
				}
			});
		}
	});
}

//Added for maintaining left nav consistency
//ACC.globalActions.menuNdbodyHeight();

$("#createProfileButton").click(function(){
	
	//newly added for multiple accounts in sprint-2 start
	var numberOfGrpSelected = 0;
	var currSelectedGroups = [];
	var prevSelectedGroups = $("#hddnAccountsString").val();
	$(".multiAccountSelection").each( function() {
		if($(this).is(':checked')) {
			currSelectedGroups.push($(this).attr('value'))
			numberOfGrpSelected++;
		}
	});
	$("#hddnAccountsString").val(currSelectedGroups);
	//newly added for multiple accounts in sprint-2 end
	
	$("#createNewProfileFormError").hide();
	var emailAddress = $("#email").val();
	var validSecflag = true;
	if(validateSectors()){
		$("#sectorMsg").html("<div style='color: #B41601;margin-top:10px'>"+ SELECT_SECTOR +"</div>");
		validSecflag = false;
	}
	
	var dataObj = new Object();
	dataObj.email = emailAddress;
	{
		jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/resources/usermanagement/isUidExists',
			async: false,
			data: dataObj,
			success: function (data) {
				if(!data){
					if($("#createNewProfileForm").valid() && validSecflag){
						$("#createNewProfileForm").submit();
					}else{
						$("#email-error").show();
					}
				}
				else{
					$(".duplicateError").text($("#hiddenMsgValue").val());
					$("#emailLogin-error").text("");
				}
					
			},
			error: function (e) {
				
			}
		});
	}
		
});

$(".profileSector").on('click',function(){
	$("#sectorMsg").html("");
	$("#sectorMsg2").html("");
});


$(".myTrainingResources").hide();
$("#resetPasswordButton").click(function(){
	
	$(".myTrainingResources").hide();

	
	var emailAddress = $("#emailLogin").val();
	var dataObj = new Object();
	dataObj.email = emailAddress;
	{
		jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/resources/usermanagement/resetPassword',
			async: false,
			data: dataObj,
			success: function (data) {
				$.each(data,function(type,content){
					if(type=="SUCCESS"){
						$("#myTrainingResourcesSuccess").show();
						$("#myTrainingResourcesSuccess").find(".training-content").text(content);	
					} else  if (type=="FAIL"){
						$("#myTrainingResourcesFailure").show();
						$("#myTrainingResourcesFailure").find(".training-content").text(content);
					} 
				})		
			},
			error: function (e) {
					
			}
		});
	}
		
});

$(".updateProfileButton").click(function(){
	//newly added for multiple accounts in sprint-2 start
	var numberOfGrpSelected = 0;
	var currSelectedGroups = [];
	var prevSelectedGroups = $("#hddnAccountsString").val();
	$(".multiAccountSelection").each( function() {
		if($(this).is(':checked')) {
			currSelectedGroups.push($(this).attr('value'))
			numberOfGrpSelected++;
		}
	});
	$("#hddnAccountsString").val(currSelectedGroups);
	//newly added for multiple accounts in sprint-2 end
	var emailAddress = $("#emailLogin").val();
	var existingEmail =  $("#existingEmail").val()

	if($("#country").val() == ""){
		$("#countryMsg").html("");
		var msg = ENTER_COUNTRY
		$("#countryMsg").append(msg);
	}
	
	if($("#department").val() == ""){
		$("#deptMsg").html("");
		var msg = ENTER_DEPARTMENT
		$("#deptMsg").append(msg);
	}
	
	var validSecflag = true;
	if(validateSectors()){
		$("#sectorMsg2").html("<div style='color: #B41601;margin-top:10px'>"+ SELECT_SECTOR +"</div>");
		validSecflag = false;
	}else{
		$("#sectorMsg2").html("");
	}
	
	if(emailAddress == existingEmail ){
		if($("#editUserProfileForm").valid() && validSecflag){
			$("#editUserProfileForm").submit();
		}else{
			$("#emailLogin").next().text("");
		}
		return;
	}
 
	var dataObj = new Object();
	dataObj.email = emailAddress;
	{
		jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/resources/usermanagement/isUidExists',
			async: false,
			data: dataObj,
			success: function (data) {
				if(!data){
					if($("#editUserProfileForm").valid() && validSecflag){
						$("#editUserProfileForm").submit();
					}else{
						$(".duplicateError").text(""); //clear existing error
					}
				}
				else{
					$(".duplicateError").text($("#hiddenMsgValue").val());
					$("#emailLogin-error").text("");
				}
			},
			error: function (e) {
				
			}
		});
	}
		
});


$("#department").on('change',function(){
	$("#deptMsg").html("");
});

//To display the states for the company information page - the bill to country section
$("#country").bind('change', function() {
	$("#countryMsg").html("");
//	$("#countryMsg").css("display","none");
	var countryElement = this;
	if(null != countryElement && '' != countryElement && null!= countryElement.value && 'US'== countryElement.value){
		var country = countryElement.value;
		var dataObj = new Object();
	dataObj.country = country; 
	jQuery.ajax({
		type: 'POST',
		url: ACC.config.contextPath +'/register/getStates',
		data: dataObj,
		success: function (data) {
			var options="<option value=''>Select State</option>";
			for(var i=0;i<data.length;i++){
				options=options+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
			}
			$("#state").val("");
			$("#noUsState").val("");
			//$(".noUsState").attr("disabled",true);
			$("#noUsState").attr("disabled",true);
			$("#state").html(options);
			$("div.state").show();
			$("#state").attr("disabled", false);
			$(".noUsState").hide();
			
			$("#mandatoryStar").show();
		},
		error: function (e) {
			
		}
	});}
	else if(null != countryElement && '' != countryElement && null!= countryElement.value && ''== countryElement.value)
	{
		var options="<option value=''>Select State</option>";
		$("#state").html(options);
		$("div.state").show();
		$("#state").attr("disabled", false);
		$(".noUsState").hide();
		$(".noUsState").attr("disabled", true);
		$("#mandatoryStar").hide();
	}
	else
	{	
		$(".noUsState").show();
		$("#noUsState").val("");
		$("div.state").hide();
		$("#state").attr("disabled", true);
		$("#noUsState").attr("disabled",false);
		$("#mandatoryStar").hide();
	}
});

$("#addToNotesButton").click(function(){
	var existingNotes = $("#notes").val();
	var currentNotes = $("#addToNotesText").val();
	var dataObj = new Object();
	dataObj.csrNotes = currentNotes;
	dataObj.existingNotes = existingNotes;
	{
		jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/resources/usermanagement/addToNotes',
			async: false,
			data: dataObj,
			success: function (data) {
				$("#notes").val(data);
				$("#addToNotesText").val('')
			},
			error: function (e) {
			}
		});
	}
		
});

//currently the is no delete option in GT
/*$("#deleteProfileButtonBtm").click(function(){
	 var userId = $("#userId").val();
	 jQuery.ajax({
			type: 'POST',
			url: ACC.config.contextPath +'/resources/usermanagement/deleteUser?uid='+userId,
			async: false,
			success: function (data) {
				window.location.href = ACC.config.contextPath + '/resources/usermanagement';
			},
			error: function (e) {
			}
		});
});*/

$(document).ready(function() {
	if($("#valueSetFlag").val()=="true"){
		$("#hddnTempAccountList").val($("#hddnAccountsString").val());
		$("#valueSetFlag").val("false");
	}
	$("#phonePrefix, #supPhonePrefix, #mobilePrefix, #faxPrefix").change(function(){
		if($(this).val()=="") {
			$(this).val("+1");
		}
	});
	var checkedRadio = $(".accessRadio:checked")
	showCheckedRadioData(checkedRadio);
	var selfAddAccountFalse = $("#selfAddAccountFalse").val();
	if(selfAddAccountFalse == "false")
	{		
		$("#addAccount").addClass("linkDisable");		
	}
	
	$(".userStatusChange").click(function(){
		 var emailAddress = $("#userId").val();
		 var ids =$(this).attr("data");
		 $("#status").val(ids);
		 if(ids =='Disabled')
		 {
			 	 status = true;
		 }else{
			 	status = false;
			}
		 jQuery.ajax({
				type: 'POST',
				url: ACC.config.contextPath +'/resources/usermanagement/enableOrDisableUser?status='+status+'&&emailAddress='+emailAddress,
				async: false,
				success: function (data) {
					window.location.href = ACC.config.contextPath + '/resources/usermanagement/edit?user='+emailAddress;
				},
				error: function (e) {
				}
			});
		
	});
	
	setUserRoles();
	setDefaultFields();
	//clear munual message setting for error create
	$("#email").keyup(function(){
		$(".duplicateError").text("");
	});
	//clear munual message setting for error edit
	$("#emailLogin").keyup(function(){
		$(".duplicateError").text("");
	});
	
$('.userRoleDropDown').change(function() {
		
		if($(this).val() == "placeOrderBuyerGroup" ){
			$("#ConsignmentOrderEntryChkbox").show();
			return false;
		}
		else {
			$("#ConsignmentOrderEntryChkbox").hide();
		}
	});
	$('.userRoleDropDown').trigger("change");
});

function showCheckedRadioData(checkedRadio){
	var radioSelected = $(checkedRadio).attr("id");
	switch(radioSelected){
		case "radio1":

			$("#addAccount").removeClass("linkDisable");
			$("#wwidDiv").hide();
			$("#moreDiv").hide();

		break;

		case "radio2":

			$("#addAccount").removeClass("linkDisable");
			$("#wwidDiv").show();
			$("#moreDiv").hide();

		break;

		case "radio3":

			$("#addAccount").removeClass("linkDisable");
			$("#wwidDiv").hide();
			$("#moreDiv").show();

		break;
	}
}

function setDefaultFields(checkedRadio){
	var count = 0;
	var accountList="";
	var loggedUserAccounts = "";
	var selectedGroups = $("#hddnAccountsString").val();
	loggedUserAccounts = $("#hddnloggedUserAccountList").val();
	if(loggedUserAccounts !== null && loggedUserAccounts !== undefined) {
		$(loggedUserAccounts.split(",")).each(function(){
			var account = $.trim(this+"");
			if(accountList.length >0){
			 accountList += "," + account;
			}else{
				accountList += account;
			}
			count++;
		});
	}
	
	if(count!=0){
		//console.log("selectedGroups : "+selectedGroups);
		//[00080890, 00080889, 00080888] to convert 00080890, 00080889, 00080888
		accountList = accountList.substring(1, accountList.length-1);
		//console.log("loggedUserAccounts : "+loggedUserAccounts +" converted to : "+accountList);
	}
	
	$(".multiAccountSelection").each( function() {
		var accountId = $(this).attr('value');
		var checkId =$(this).attr('id');
		if(selectedGroups !== null  && selectedGroups !== undefined) {
			if($.inArray(accountId, selectedGroups.split(",")) !== -1){
				$("#"+checkId).prop('checked', true);
			}else{
				$("#"+checkId).prop('checked', false);
			}
		}
		
		if(accountList !== null  && accountList !== undefined) {
			if($.inArray(accountId, accountList.split(",")) !== -1){
				/*$(this).addClass("opaque");
				$("#"+checkId).css("visibility","hidden");
				$(this).find(".multiAccountSelection").removeClass("multiAccountSelection");*/
			}else{
				var id = $(this).parent().parent().parent().attr('id'); /*for the parent div id to remove the irrevelent group instead of disable ro shown*/
				var isChecked = $("#"+id+" #"+checkId).prop('checked');
				
				/*console.log("isChecked :" +isChecked);
				if(!isChecked){
					$("#"+id).remove();
				}
				$(this).addClass("opaque");
				$("#"+checkId).attr("disabled", true);
				$(this).find(".multiAccountSelection").removeClass("multiAccountSelection");*/
			}
			
		}
	});
	
	$(".grpAccount").each( function(i,v) {
		if((i%2)===0){
			$(this).addClass('even').removeClass('odd');
		} else{
			$(this).addClass('odd').removeClass('even');
		}
	});
}

function validateRolesAndGroupsFields(){
	var roleFlag = false;
	var accountFlag = false;
	$(".userRoleList").each( function() {
		if($(this).is(':checked')) {
			roleFlag = true;
			return false;
		}
	});
	
	if(!roleFlag){
		$(".roleContent").find('div.registerError').html(USER_ROLE);
	}
	
	$(".multiAccountSelection").each( function() {
		if($(this).is(':checked')) {
			accountFlag = true;
			return false;
		}
	});
	
	if(!accountFlag){
		$(".multipleAccountContent").find('div.registerError').html(USER_ONE_ACCOUNT);
	}
	
	return (roleFlag && accountFlag? true:false);
}

function setUserRoles(){
	var userRoles = $("#hiddenRoleValue").val();
	var userRoleList = [];
	//console.log("userRoles : "+userRoles);
	if(userRoles !== null  && userRoles !== undefined){
		userRoles = userRoles.substring(1, userRoles.length-1);
		userRoles = userRoles.replace(/, /g, ",") // remove space after the comma
		$(".userRoleList").each( function() {
			var roleId = $(this).attr('value');
			var checkId =$(this).attr('id');   
			if($.inArray(roleId, userRoles.split(",")) !== -1){
				$("#"+checkId).prop('checked', true);
			}else{
				$("#"+checkId).prop('checked', false);
			}
		});	
	}
}

function validateSectors(){
	var sectorFlag = false;
	$(".profileSector").each( function() {
		if($(this).is(':checked')) {
			sectorFlag = true;
			return false;
		}
	});
	
	if(!sectorFlag){
		return true;
	}
}

//added for AAOL-2422
function activeFinancialAnalysis()
{
	var selectedGroup = $('#role :selected').val();
	if(selectedGroup =='placeOrderBuyerGroup' || selectedGroup =='placeOrderSalesGroup'){
		
		 $("#financialEnable").attr("disabled", false);
	}
	else
		{
		 $("#financialEnable").attr("disabled", true);
		}

}

//ended for AAOL-2422
