var accountSubmitClicked=false;
var accountSelectionObj=new Object();
var initialPopupOpen = true;

if($('#hddnAccountsString').length>0){
	var accountsArray=$('#hddnAccountsString').val().split(',');
	for(var i=0;i<accountsArray.length;i++){
		accountSelectionObj[accountsArray[i]]=accountsArray[i];
	}
}


	$(document).on('hidden.bs.modal', '#changeAcntPopup', function (event) {
		if(!accountSubmitClicked){
	/*		var hiddenTmpAccntArr=$('#hddnTempAccountList1').val().split(',');
			alert("hiddenTmpAccntArr >> "+hiddenTmpAccntArr)
			var checkedAccount=false;
			var checkedList=[];*/
			var accountGroupTextArr=[];
			
			$('.usermanagementSelectedAcc').each(function(){
				if($(this).is(':visible')){
					accountGroupTextArr.push($(this).find('.accountGroupText').text());
				}	
			});
			$('.grpAccountEdit').each(function(){
				if($(this).is(':visible')){
					accountGroupTextArr.push($(this).find('.accountGroupText').text());
				}	
			});
			$('#hddnTempAccountList1').val(accountGroupTextArr);
		}
		accountSubmitClicked=false;
    })
    
    
/** 
 * Account selection Link click action
 */

$("#selectAccountUM").click(function(e) {
	
	var selectedAccounts = [];
	$('input[name=accounts]').each(function(){
		selectedAccounts.push(this.value);
	});
	userManagmentAccountSelectionAjax(true,"","");
	checkBoxSort();
});
$("#selectAccountEditUM").click(function(e) {

	var selectedAccounts = [];
	$('input[name=accounts]').each(function(){
		selectedAccounts.push(this.value);
	});
	editUserManagmentAccountSelectionAjax(true,"","");
	checkBoxSort();
});

/*Soumitra - Adding a method to show popup on click of edit franchise button on create and edit user page AAOL-4913*/
$("#selectFranchiseUM").click(function(e) {
	$('input[name=chkFranchise]').each(function(){ //fetch each checkbox in the popup
		if(this.checked==false)
		{
			$("#franchiseSelectAll").prop("checked",false);
		}
	});
	$("#franchisePopUpDiv").show();
	$("#changeFranchisePopupForUsr").show();
	$("#franchiseError").css("display", "none");
});

$("#franchiseSelectAll").click(function(e) {
	if(this.checked==true)
	{
		$('input[name=chkFranchise]').each(function(){ //fetch each checkbox in the popup
			this.checked=true;
		});
	}
	else
	{
		$('input[name=chkFranchise]').each(function(){ //fetch each checkbox in the popup
			this.checked=false;
		});
	}
});

$(".franchise-tcell-chckbox").click(function(e) {
	var selectAllCheck=true;
	if(this.checked==true)
	{
		$('input[name=chkFranchise]').each(function(){ //fetch each checkbox in the popup
			if(this.checked==false)
			{		
				$("#franchiseSelectAll").checked=false;
				selectAllCheck=false;
			}
		});
		if(selectAllCheck)
		{
			$("#franchiseSelectAll").prop("checked",selectAllCheck);
		}
	}
	else
	{
		$("#franchiseSelectAll").prop("checked",false);
	}
});

/*Soumitra - Adding a method to persist the selected franchise values on click of OK AAOL-4913*/
$("#franchisePopUpOk").click(function(e) {
	var selectedFranchiseComma='';
	var count=0;
	var franchiseDivData="";
	$('input[name=chkFranchise]').each(function(){ //fetch each checkbox in the popup
		if(this.checked==true){
			count=count+1;
			franchiseDivData=franchiseDivData+"<div class='"+(count%2 == 0 ? "even-row" : "odd-row")+"'><div class='display-table-cell'><div class=''><a href='#' class='usm-account-num'>"+this.value+"</a></div></div></div>";
			selectedFranchiseComma=selectedFranchiseComma+","+this.id; //if the chkbox is checked, append the value in selectedFranchiseComma
		}
	});
	selectedFranchiseComma=selectedFranchiseComma.replace(',',''); //replacing extra comma at the beginning of the string
	if(selectedFranchiseComma!=undefined && selectedFranchiseComma!='')
	{
		$('#selectedFranchiseComma').val(selectedFranchiseComma); // Setting to the hidden field "selectedFranchiseComma" which is mapped to the customerForm.allowedFranchise
		$("#errorDiv").text("");
		$("#franchiseDiv").empty().append(franchiseDivData);
		$(".close").click(); //Closing the popup
	}
	else
	{
		/*$("#errorDiv").empty();
		$("#franchiseError").show();
//		$("#errorDiv").append("<span class='glyphicon glyphicon-ban-circle quick-msg' >"+ NO_FRANCHISE_ERROR); //force user to select at least one franchise
		$("#errorDiv").append(NO_FRANCHISE_ERROR);*/
		$("#franchiseError").css("display", "block");
	}
});

/*Soumitra - Adding a method to reset selected franchise on click of Cancel AAOL-4913*/
$("#franchisePopUpCancel").click(function(e) {
	var selectedFranchiseComma=$('#selectedFranchiseComma').val().split(','); //forming an array from the comma separated franchise Codes
	$('input[name=chkFranchise]').each(function(){
		if(selectedFranchiseComma.contains(this.id)){
			this.checked=true; //if the value is present in selectedFranchiseComma, chkbox is checked,
		}
		else
		{
			this.checked=false;
		}
	});
	$(".close").click();//Closing the popup
});

function userManagmentAccountSelectionAjax(isFirstClick, pageNumber, searchTerm){
	
	
	
		var ajaxurl = ACC.config.contextPath+"/resources/usermanagement/selectAccount";
		
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
		$.ajax({
		  type: "POST",
		  url: ajaxurl,
		  data: dataObj, 
		  success: function(data) {
			 
			 $('#add-account-popup-holder').html(data);
			 if($('.contract-detail-desktab').children().length==0){
				 $('.selectAllHeader').hide();
				 $('.contract-detail-desktab').html('<div style="font-size:18px;margin-top:5px;">No data found.</div>');
			 }
			 $('#changeAcntPopup').modal('hide');	
			  $('#changeAcntPopup').modal('show');
			  $('.clsBtn').click(function(){
				  $('.modal-backdrop').hide();
			  });
			  	
			  

			  var isOrderHistorySelectAllChecked = $(".orderHistorySelectAll").prop('checked');
					var preselectedAccounts = "";
					if(isFirstClick){
						if(null!=$("#accounts").val() && $("#accounts").val()!="") {
							$("#hddnTempAccountList").val($("#accounts").val());
						}else{
							$("#hddnTempAccountList").val($("#currentAccount").val());
						}
					}
					/*if($("#accounts").val().split(",").length == $("#totalResultsAccountSelection").val()) {
						$("#selectAllAccount").attr("checked", true);
					}*/
					/*if($("#accounts").val().split(",").length == 1000) {
						$("#selectAllAccount").attr("checked", true);
					}*/
					$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length);
					
					if($("#hddnTempAccountList1").val().indexOf(",")==0){
						preselectedAccounts = $("#hddnTempAccountList1").val().substring(1, $("#hddnTempAccountList1").val().length);
					} else {
						preselectedAccounts = $("#hddnTempAccountList1").val();
					}
					//preselectedAccounts="";
				//	$("#hddnTempAccountList1").val("");
				    $("input.accountSelection").each(function(){
						$(this).prop('checked',false);
					});
					$(preselectedAccounts.split(",")).each(function(){
						$("input.accountSelection[value='" + this + "']").prop("checked", true);
					});
					//here need to handle the select all check based on order history page select all checkbox
					if(isOrderHistorySelectAllChecked){
						var collectSelAccount="";
						$("input.accountSelection").each(function(){
							$(this).prop('checked',true);
							if(collectSelAccount ===""){
								collectSelAccount = $(this).val();
							}else{
								collectSelAccount = collectSelAccount+","+$(this).val();
							}
						});
						$("#hddnTempAccountList").val(collectSelAccount); //set all accounts id onload selection
						$("#selectAllAccount").prop('checked',true);
						$("#isSelectAllAccount").val(true);//yet to set
					}
					$("#selectAllAccount").change(function(){
						
						if(selectAllBaseFlag==true){
							$(this).prop('checked',true);
						}
						
						
						if($(this).prop("checked")) {
							if($(this).hasClass('usm-select-all')){
							$('#hddnTempAccountList1').val($('#hddnloggedUserAccountList').val().slice(1, -1).replace(/ /g,''));
							}
							ACC.orderHistory.orderHistSelectAll();
												} else {
							$(".accountSelection").prop("checked", false);
							$(".orderHistorySelectAll").prop("checked", false);  
							$("#isSelectAllAccount").val(false); 
							$("#hddnTempAccountList").val("");
							$(".selectedAccountsText").html("0");
						}
						
					});
					
				
					/** Single account select check-box click action **/
					$(".accountSelection").change(function(){
						checkBoxSort();
				
							$("#selectAllAccount").prop('checked',true);
							$(".orderHistorySelectAll").prop('checked',true);  // report Page select all
							$("#isSelectAllAccount").val(true);//yet to set
							$(".accountSelection").each(function(){
								if($(this).prop('checked')!=true){
									$("#selectAllAccount").prop('checked',false);
									$(".orderHistorySelectAll").prop('checked',false);  // report Page select all
									$("#isSelectAllAccount").val(false);//yet to set clear
								}
							});
					
						
						
						if(!$(this).is(":checked")) {
							
							if($("#hddnTempAccountList1").val().indexOf("," + $(this).val())!=-1){
							
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val().replace("," + $(this).val(),""));
							}else if ($("#hddnTempAccountList1").val().indexOf($(this).val())!=-1){
							
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val().replace($(this).val(),""));
							}
							/*$(".accountSelectionAll").attr("checked", false);*/
						} else if ($(".accountSelection").length == $(".accountSelection:checked").length) {
							
							if($("#hddnTempAccountList1").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val() + "," + $(this).val());
							}
							/*$(".accountSelectionAll").attr("checked", true);*/
						} else {
							
							if($("#hddnTempAccountList1").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val() + "," + $(this).val());
							}
						}
						if(null==$("#hddnTempAccountList").val() || $("#hddnTempAccountList").val()=="") {
							$(".selectedAccountsText").html("0");
						} else if($("#hddnTempAccountList1").val().indexOf(",")==0) {
							
							$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length - 1);
						}else{
							
							$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length);
						}
							 
					});
						var totalPages = $("#accountNumberOfPages").val();
						$("#orderHisChangeAccountNoAjax").change(function(){
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
								orderHistoryAccountSelectionAjax(false, parseInt($("#currentPage").val(), 10) + 1, searchTerm);
							});
							$.colorbox.resize();
						} else {
							$("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
						}
						/** Clear button click action **/
						$(".clearSelectAccountBtn").click(function(){
							userManagmentAccountSelectionAjax(false, "","");
							$("#orderHisChangeAccountNoAjax").val("");
						});
						$("#orderHisChangeAccountNoAjax").keypress(function(e){
							var key = e.which;
							if(key == 13){// the enter key code
								$("#orderHisSearchSelectAccountBtn").click();
							}  
						});
						/** Search button action **/
						$("#orderHisSearchSelectAccountBtn").click(function(){
							
							if(null!=$("#orderHisChangeAccountNoAjax").val() && $.trim($("#orderHisChangeAccountNoAjax").val())!="") {
								
								userManagmentAccountSelectionAjax(false, "", $.trim($("#orderHisChangeAccountNoAjax").val()));
							}
						});
						/** Account selection cancel action **/
						$(".accountSelectionCancel").click(function(){
							$("#hddnTempAccountList").val("");
							$.colorbox.close();
						});
						submitSelectedAccountsCUM();
					},
					onClosed: function () {
					/*}
				});*/
		    },
		  dataType: 'html',
		  async: false
		});
		
		//Checking all the accounts are checked
		var allSelected=true;
		  $('.accountSelection').each(function(){
		  	if(!$(this).prop('checked')){
		  		allSelected=false;
		  	}
		  });
		  if(allSelected){
		  	$('#selectAllAccount').prop('checked',true);
		  }
		  else{
		  	$('#selectAllAccount').prop('checked',false);
		  }
		
	}

function editUserManagmentAccountSelectionAjax(isFirstClick, pageNumber, searchTerm){
	
	
	
	var ajaxurl = ACC.config.contextPath+"/resources/usermanagement/editUserselectAccount";
	
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
	var user =$("#email").val();
	
	dataObj.showMode = "Page";
	dataObj.page = "0";
	dataObj.user =user;
	$.ajax({
	  type: "POST",
	  url: ajaxurl,
	  data: dataObj, 
	  success: function(data) {
			 $('#add-account-popup-holder').html(data);
			 if($('.contract-detail-desktab').children().length==0){
				 $('.selectAllHeader').hide();
				 $('.contract-detail-desktab').html('<div style="font-size:18px;margin-top:5px;">No data found.</div>');
			 }
			 $('.modal-backdrop').hide();
			 $('#changeAcntPopup').modal('hide');	
			  $('#changeAcntPopup').modal('show');
			 
			  $('.clsBtn').click(function(){
				  $('.modal-backdrop').hide();
			  });
			  var isOrderHistorySelectAllChecked = $(".orderHistorySelectAll").prop('checked');
			  //console.log(" isOrderHistorySelectAllChecked : "+isOrderHistorySelectAllChecked);
					var preselectedAccounts = "";
					if(isFirstClick){
						if(null!=$("#accounts").val() && $("#accounts").val()!="") {
							$("#hddnTempAccountList").val($("#accounts").val());
						}else{
							$("#hddnTempAccountList").val($("#currentAccount").val());
						}
					}
					preselectedAccounts = $("#hddnAccountsString").val();
					$("#hddnTempAccountList1").val(preselectedAccounts);
					
					$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length);
					if($("#hddnTempAccountList1").val().indexOf(",")==0){
						preselectedAccounts = $("#hddnTempAccountList1").val().substring(1, $("#hddnTempAccountList1").val().length);
					} else {
						preselectedAccounts = $("#preselectedAccounts").val();
					}
				  //$("#hddnTempAccountList").val("");
					
					
					$("#selectAllAccount").change(function(){
						
						if(selectAllBaseFlag==true){
							$(this).prop('checked',true);
						}
						
						
						if($(this).prop("checked")) {
							if($(this).hasClass('usm-select-all')){
							$('#hddnTempAccountList1').val($('#hddnloggedUserAccountList').val().slice(1, -1).replace(/ /g,''));
							}
							ACC.orderHistory.orderHistSelectAll();
												} else {
							$(".accountSelection").prop("checked", false);
							$(".orderHistorySelectAll").prop("checked", false);  
							$("#isSelectAllAccount").val(false); 
							$("#hddnTempAccountList").val("");
							$(".selectedAccountsText").html("0");
						}
					});	
					
					
					$("input.accountSelectionUM").each(function(){
						$(this).prop('checked',false);
					});

					if(initialPopupOpen && document.URL.includes("usermanagement")){
					    let thisUserAccount = $("#hddnAccountsStringUM").val();
					    $("#hddnAccountsString").val(thisUserAccount);
					    initialPopupOpen = false;
					}

					var hddnAccountsStringArr=$("#hddnAccountsString").val().split(",");
					for(var i=0;i<hddnAccountsStringArr.length;i++){
						$("input.accountSelectionUM[value='" + hddnAccountsStringArr[i] + "']").prop("checked", true);
					}
						
				
					
					
					if(isOrderHistorySelectAllChecked){
						var collectSelAccount="";
						$("input.accountSelectionUM").each(function(){
							$(this).prop('checked',true);
							if(collectSelAccount ===""){
								collectSelAccount = $(this).val();
							}else{
								collectSelAccount = collectSelAccount+","+$(this).val();
							}
						});
						$("#hddnTempAccountList").val(collectSelAccount); //set all accounts id onload selection
						$("#selectAllAccount").prop('checked',true);
						$("#isSelectAllAccount").val(true);//yet to set
					}
					
						 
					/** Single account select check-box click action **/
					var accountSelectedValue='';
					$(".accountSelectionUM").change(function(){
						checkBoxSort();
						$("#selectAllAccount").prop('checked',true);
						$(".orderHistorySelectAll").prop('checked',true);  // report Page select all
						$("#isSelectAllAccount").val(true);//yet to set
							$(".accountSelectionUM").each(function(){
								if($(this).prop('checked')!=true){
									$("#selectAllAccount").prop('checked',false);
									$(".orderHistorySelectAll").prop('checked',false);  // report Page select all
									$("#isSelectAllAccount").val(false);//yet to set clear
								}
							});
						
						accountSelectedValue=$(this).val().trim('');
						if(!$(this).is(":checked")) {
				
							delete accountSelectionObj[accountSelectedValue];
							if($("#hddnTempAccountList1").val().indexOf("," + $(this).val())!=-1){
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val().replace("," + $(this).val(),""));
							}else if ($("#hddnTempAccountList1").val().indexOf($(this).val())!=-1){
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val().replace($(this).val(),""));
							}
							/*$(".accountSelectionAll").attr("checked", false);*/
							
							
						} else if ($(".accountSelectionUM").length == $(".accountSelectionUM:checked").length) {
								accountSelectionObj[accountSelectedValue]=accountSelectedValue;
							if($("#hddnTempAccountList1").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val() + "," + $(this).val());
							}
							/*$(".accountSelectionAll").attr("checked", true);*/
						} else {
							
							accountSelectionObj[accountSelectedValue]=accountSelectedValue;
							
							
							if($("#hddnTempAccountList1").val().indexOf($(this).val())==-1) {
								$("#hddnTempAccountList1").val($("#hddnTempAccountList1").val() + "," + $(this).val());
							}
						}
						if(null==$("#hddnTempAccountList1").val() || $("#hddnTempAccountList1").val()=="") {
						
							$(".selectedAccountsText").html("0");
						} else if($("#hddnTempAccountList1").val().indexOf(",")==0) {
							
							$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length - 1);
						}else{
							
							$(".selectedAccountsText").html($("#hddnTempAccountList1").val().split(",").length);
						}
							 
					});
						var totalPages = $("#accountNumberOfPages").val();
						$("#orderHisChangeAccountNoAjax").change(function(){
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
								orderHistoryAccountSelectionAjax(false, parseInt($("#currentPage").val(), 10) + 1, searchTerm);
							});
							$.colorbox.resize();
						} else {
							$("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
						}
						/** Clear button click action **/
						$(".clearSelectAccountBtn").click(function(){
							editUserManagmentAccountSelectionAjax(false, "","");
							$("#orderHisChangeAccountNoAjax").val("");
						});
						$("#orderHisChangeAccountNoAjax").keypress(function(e){
							var key = e.which;
							if(key == 13){// the enter key code
								$("#orderHisSearchSelectAccountBtn").click();
							}  
						});
						/** Search button action **/
						$("#orderHisSearchSelectAccountBtn").click(function(){
							
							if(null!=$("#orderHisChangeAccountNoAjax").val() && $.trim($("#orderHisChangeAccountNoAjax").val())!="") {
								
								editUserManagmentAccountSelectionAjax(false, "", $.trim($("#orderHisChangeAccountNoAjax").val()));
							}
						});
						/** Account selection cancel action **/
						$(".accountSelectionCancel").click(function(){
							$("#hddnTempAccountList").val("");
							$.colorbox.close();
						});
						submitSelectedAccountsUM();
					},
					onClosed: function () {
					/*}
				});*/
		    },
		  dataType: 'html',
		  async: false
		});
		var allSelected=true;
	  $('.accountSelection').each(function(){
	  	if(!$(this).prop('checked')){
	  		allSelected=false;
	  	}
	  });
	  if(allSelected){
	  	$('#selectAllAccount').prop('checked',true);
	  }
	  else{
	  	$('#selectAllAccount').prop('checked',false);
	  }
	}

 function submitSelectedAccountsUM(e)
{
	var selectedAccnts=[]; 	
	$("#accountsSubmitUM").click(function() {
		 selectedAccnts=[]; 	
		var accSelLength = $(".accountSelectionUM:checked").length;
	
		
		$('.grpAccountEdit').hide();
		
		$('.multiAccountSelection').prop('checked',false);
		for (var property in accountSelectionObj) {
		    if (accountSelectionObj.hasOwnProperty(property)) {
		    	selectedAccnts.push(accountSelectionObj[property]);	
				$('.grpAccountEdit_'+accountSelectionObj[property]).show();
				$('.multiAccountSelection_'+accountSelectionObj[property]).prop('checked',true);
		    }
		   
			
		}
		$('#hddnAccountsString').val(selectedAccnts);
		sendSelectedAccountsUM(accSelLength);
		
	});
}
 function submitSelectedAccountsCUM(e)
 {
 	$("#accountsSubmitCUM").click(function() {
 		var accSelLength = $(".accountSelection:checked").length;
 		accountSubmitClicked=true;
 		
 		$("#account-holder-usm > div[id]").each(function(){
 			$(this).css('display','none');	
 			$(".check_"+this.id.split("_")[1]).prop("checked",false);
 		});
 		sendSelectedAccountsCUM(accSelLength);
 		  ACC.globalActions.menuNdbodyHeight();
 	});
 }

 function sendSelectedAccountsUM(accSelLength){

	if (accSelLength === 0){
		$('#select-accnt-error-order-history').css('display','block');
		return false;
	}
   var accountList = $("#hddnTempAccountList1").val();
   var accountsArray = [];
	if(accountList.indexOf(",")!=-1) {
		accountsArray = accountList.split(",");
		if(accountsArray[0]==""){
			accountsArray.shift();
		}
	} else {
		accountsArray.push(accountList);
	}
	var  strAccountList = accountList.replace(/^,/,'');
	var accountNames = "";
	$(strAccountList.split(",")).each(function(){
		var account = this+"";
			var accountName = "";
			accountNames += "," + account.replace(/^0+/, '');
		});
	var  strAccountLists = accountNames.replace(/^,/,'');
$("#accnt").html(strAccountLists);
$("#preselectedAccounts").val(strAccountList);
$("#hddnAccountsStringUM").val(accountsArray)
$('#changeAcntPopup').modal('hide');
$('body').removeClass('modal-open');
$('.modal-backdrop').remove();
}
 function sendSelectedAccountsCUM(accSelLength){

		if (accSelLength === 0){
			$('#select-accnt-error-order-history').css('display','block');
			return false;
		}
	   var accountList = $("#hddnTempAccountList1").val();
	   accountList=accountList.replace(/ /g,''); 
	  
	   var accountsArray = [];
		if(accountList.indexOf(",")!=-1) {
			accountsArray = accountList.split(",");
			if(accountsArray[0]==""){
				accountsArray.shift();
			}
		} else {
			accountsArray.push(accountList);
		}
var  strAccountList = accountList.replace(/^,/,'');

var accountNames = "";
$(strAccountList.split(",")).each(function(){
	var account = this+"";
		var accountName = "";
		accountNames += "," + account;
	});
var  strAccountLists = accountNames.replace(/^,/,'');
	$("#hddnAccountsStringUM").val(accountsArray)
	
	for(var i=0;i<= accountsArray.length;i++){

		$(".account_"+accountsArray[i]).show();
		$(".check_"+accountsArray[i]).prop("checked",true);
	}
	
	
    $('#changeAcntPopup').modal('hide');	
	$('body').removeClass('modal-open');
	$('.modal-backdrop').remove();
	}

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

$("#createProfileButtonNA").click(function(){
	
	//newly added for multiple accounts in sprint-2 start
	var numberOfGrpSelected = 0;
	var currSelectedGroups = [];
	var prevSelectedGroups = $("#hddnAccountsString").val();
	$("#emailduplicate").text("");
	$(".multiAccountSelectionNA").each( function() {
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
		$("#sectorMsg").html("<div style='color: #B41601;margin-top:10px'>Please select at least one Sector</div>");
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
					if($("#createNewProfileFormNA").valid() && validSecflag){
						$("#createNewProfileFormNA").submit();
					}else{
						$("#email-error").show();
					}
				}
				else{
					//$("#email").parent().parent().find('div.registerError').html("<label class='error'>"+ $("#hiddenMsgValue").val() +"</label>");
					$("#emailduplicate").text($("#hiddenMsgValue").val());
					$("#emailLogin-error").text("");
				}
					
			},
			error: function (e) {
				
			}
		});
	}
		
});

$("#resetPasswordButtonNA").click(function(){
	
	$("#myTrainingResources").find(".success").first().hide();
	$("#myTrainingResources").find(".registerError").first().hide();
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

$(".updateProfileButtonNA").click(function(){
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
	
	if($("#consDivision").val()=="" && ($("#consumer").is(':checked')))
			{
				$("#consDiverror").text("Please select consumer division.").addClass("error");
				return false;
			}else{
				$("#consDiverror").text("");
			}
	
	$("#hddnAccountsString").val(currSelectedGroups);
	//newly added for multiple accounts in sprint-2 end
	var emailAddress = $("#emailLogin").val();
	var existingEmail =  $("#existingEmail").val()
	var validSecflag = true;
	if(validateSectors()){
		$("#sectorMsg2").html("<div style='color: #B41601;margin-top:10px'>Please select at least one Sector</div>");
		validSecflag = false;
	}else{
		$("#sectorMsg2").html("");
	}

	if(emailAddress == existingEmail ){
		if($("#editUserProfileFormNA").valid() && validSecflag){
			$("#editUserProfileFormNA").submit();
		}else{
			//$("#editUserProfileFormError").show();
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
					if($("#editUserProfileFormNA").valid() && validSecflag){
						$("#editUserProfileFormNA").submit();
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

//To display the states for the company information page - the bill to country section
$("#country").bind('change', function() {
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
			$("#state").selectpicker('refresh');
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


//To display the states for the CA country
$("#countryCA").bind('change', function() {
	var countryElement = this;
	
	if(null != countryElement && '' != countryElement && null!= countryElement.value && 'CA'== countryElement.value){
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
			$("#state").selectpicker('refresh');
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
				//$("#notes").val(data);
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
//$("#TextBoxContainerUM").show();
//Add the cost center function    
$("#btnAddCostCenter").bind("click", function(e) {
       e.preventDefault();
       
       var div = $("<div><input name = 'costCenter' class='form-control margintop' type='text' value = '' />&nbsp;<a href='#' id='btnAdd1' class='remove'  return false;><font color='blue'>Delete</font></a></div>");
       // var div = $("<div class='col-lg-4 col-md-4 col-sm-4 col-xs-12' ></br><input name = 'costCenter' class='form-control margintop' type='text' value = '' />&nbsp;<a href='#' id='btnAdd1' class='remove'  return false;><font color='blue'>Delete</font></a></div>");
       
       // $("#TextBoxContainerUM").append(div);
       $("#TextBoxContainerUM #reg_inputsUM").append(div);
});
//Changes done for Cost Center Onload

//cost center in usermanagement edit page  (final change)
$("#access-radio-2").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-2").is(":checked")) {
		$("#TextBoxContainerUM").show();
	} else{
		
		$("#TextBoxContainerUM").hide();
	}
});
if($("input#consumer").is(":checked"))
{
	$("#TextBoxContainerUM").removeClass( "hide" );
	
}else{
	
	$("#TextBoxContainerUM").addClass( "hide" );
}

$("#consumer").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-2").is(":checked")) {
		$("#TextBoxContainerUM").removeClass( "hide" );
			//$("#TextBoxContainerUM").show();
	} else {
		$("#TextBoxContainerUM").addClass( "hide" );
		//$("#TextBoxContainerUM").hide();
	}
});

//1 
$("#access-radio-1").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-1").is(":checked")) {
		
		$("#TextBoxContainerUM").hide();
	}
});

$("#consumer").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-1").is(":checked")){
		
		
		$("#TextBoxContainerUM").hide();
	}
});

//Radio 3 
$("#access-radio-3").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-3").is(":checked")) {
		
		$("#TextBoxContainerUM").hide();
	}
});

$("#consumer").on("change", function() {
	if ($("#consumer").is(":checked") && $("input#access-radio-3").is(":checked")){

		$("#TextBoxContainerUM").hide();
	}
});
$("body").on("click", ".remove", function(e) {
       e.preventDefault();
       $(this).closest("div").remove();
});
$(document).ready(function() {
	if($("#valueSetFlag").val()=="true"){
		$("#hddnTempAccountList").val($("#hddnAccountsString").val());
		$("#valueSetFlag").val("false");
	}
	if ($("input[id='access-radio-1']").is(':checked')) {
		$("#TextBoxContainerUM").hide();   
		}
	if ($("input[id='access-radio-2']").is(':checked')) {
		$("#TextBoxContainerUM").show();   
		}
	if ($("input[id='access-radio-3']").is(':checked')) {
		$("#TextBoxContainerUM").hide();   
		}
	$("#phonePrefix, #supPhonePrefix, #mobilePrefix, #faxPrefix").change(function(){
		if($(this).val()=="") {
			$(this).val("+1");
		}
	});
	//Add button of CostCenter
	$("#reg_inputsUM").css({
		'max-height': '120px',
		'overflow': 'auto'
	});
	
	$("#reg_inputsUM .table-cell").css('display', 'block');
	
	$("#btnAdd1").bind("click", function (e) {
		e.preventDefault();
		var div = $("<div class='reg_inputsUM' style='display: block;'><input name='costCenter' class='form-control' type='text' value=''/>&nbsp;<a href='#' id='btnAdd1' class='remove' return false;>Delete</font></a></div>");
		$("#TextBoxContainerUM #reg_inputsUM").append(div);
	});

	$("body").on("click", ".remove", function (e) {
		e.preventDefault();
		$(this).closest("div").remove();
	});
	
	// Consumer Product Divion Added
	if($("input#consumer").is(":checked"))
	{
		$("#consDivDropdown").show();
	}
	$("input#consumer").click(function(){
		if($(this).is(":checked"))
		{
			$("#consDivDropdown").show();
		}
		else
		{
			$("#consDivDropdown").hide();
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
		$("#emailduplicate").text("");
		
	});
	//clear munual message setting for error edit
	$("#emailLogin").keyup(function(){
		$(".duplicateError").text("");
	});
	
	//display selected accounts
	
	$('.grpAccountEdit').each(function(){
		$(this).hide();
	});
	
	if($("#hddnAccountsString").length>0){
	var hddnAccountsStringArr=$("#hddnAccountsString").val().split(",");
	for(var i=0;i<hddnAccountsStringArr.length;i++){
		$('.grpAccountEdit_'+hddnAccountsStringArr[i]).show();
	}
	}
});

function showCheckedRadioData(checkedRadio){
	var radioSelected = $(checkedRadio).attr("id");
	switch(radioSelected){
		case "radio1":

			$("#addAccount").removeClass("linkDisable");
			$("#access-content-2").hide();
			$("#moreDiv").hide();

		break;

		case "radio2":

			$("#addAccount").removeClass("linkDisable");
			$("#access-content-2").show();
			$("#moreDiv").hide();

		break;

		case "radio3":

			$("#addAccount").removeClass("linkDisable");
			$("#access-content-2").hide();
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
				//console.log("isChecked :" +isChecked);
				if(!isChecked){
					$("#"+id).remove();
				}
				$(this).addClass("opaque");
				// $("#"+checkId).attr("disabled", true);
				$(this).find(".multiAccountSelection").removeClass("multiAccountSelection");
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
		$(".roleContent").find('div.registerError').html("<label class='error'>Please select at least one Role/Permissions! </label>");
	}
	
	$(".multiAccountSelection").each( function() {
		if($(this).is(':checked')) {
			accountFlag = true;
			return false;
		}
	});
	
	if(!accountFlag){
		$(".multipleAccountContent").find('div.registerError').html("<label class='error'>Please select at least one Account </label>");
	}
		return (roleFlag && accountFlag? true:false);
		
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
jQuery("#createNewProfileFormNA").validate({
    rules: {
	    	
    		email: {
                 email: true 
           },
           phone: {
               phoneFormateUS: true

         },
         supervisorPhone: {
               phoneFormateUS: true
         },
    },  
        groups: {
               mdd: "mdd consumer"
        },    
	    messages:{
	        mdd: {
	        	require_from_group: "select at least one sector !"
	 },
	 consumer: {
	 require_from_group: "select at least one sector !"
	 }
	 },
    errorPlacement: function(error, element) {
    //error.appendTo( element.parent().parent().find('div.registerError'));
    	//alert('s');
    	error.appendTo(element.parents().closest('.um-element-col').find('div.registerError'));
    	//error.appendTo( element.next('div.registerError'));
    },
    onfocusout: false,
    focusCleanup: false

});
jQuery("#editUserProfileFormNA").validate({
	
    rules: {
    	    emailLogin: {
                 email: true 
           },
           postalCode : {
                 digits: true
           }
    },
     
    messages:{
    	/*roles1: {
        	   require_from_group: "select at least one Role !"
           },
           consumer: {
        	   require_from_group: "select at least one sector !"
           } */
    },
    errorPlacement: function(error, element) {
    	//error.appendTo( element.parent().parent().find('div.registerError'));
    	//error.appendTo( element.next('div.registerError'));
    	error.appendTo(element.parents().closest('.um-element-col').find('div.registerError'));
    },
    onfocusout: false,
    focusCleanup: false

}); 
