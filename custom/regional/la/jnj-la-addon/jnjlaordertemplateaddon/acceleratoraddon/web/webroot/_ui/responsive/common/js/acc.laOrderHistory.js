$(window).load(function(){

var endValuedata = $('#endValueData').val();
$('#ordersTable tr').each(function() {
    $(this).children('td').find("#showMoreInfoLink").children('p').slice(endValuedata).hide();
});

	$("#selectAccountlatam").click(function(e) {
        $('.modal-backdrop').show();
        var selectedAccounts = [];
        $('input[name=accounts]').each(function(){
            selectedAccounts.push(this.value);
        });
        orderHistoryAccountSelectionAjax(true,"","");
	});

	function orderHistoryAccountSelectionAjax(isFirstClick, pageNumber, searchTerm){

		var ajaxurl = ACC.config.contextPath+"/order-history/selectAccount";
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

		loadingCircleShow("show");

		$.ajax({
		  type: "POST",
		  url: ajaxurl,
		  data: dataObj,
		  success: function(data) {

			 loadingCircleShow("hide");

			 $('#order-history-popup-holder').html(data);
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
					if($("#accounts").val().split(",").length >= 999) {
						$("#selectAllAccount").attr("checked", true);
					}
					$(".selectedAccountsText").html($("#hddnTempAccountList").val().split(",").length);
					if($("#hddnTempAccountList").val().indexOf(",")==0){
						preselectedAccounts = $("#hddnTempAccountList").val().substring(1, $("#hddnTempAccountList").val().length);
					} else {
						preselectedAccounts = $("#hddnTempAccountList").val();
					}
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
							orderHistSelectAll();
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
                            /** Merged from Hotfix **/
                            if(searchTerm==null || searchTerm ==""){
                            $("#selectAllAccount").prop('checked',true); // select all in pop up
                            $(".orderHistorySelectAll").prop('checked',true);  // report/orderhist Page select all
                            $("#isSelectAllAccount").val(true);//yet to set
                            }
                            $(".accountSelection").each(function(){
                                if($(this).prop('checked')!=true){
                                    $("#selectAllAccount").prop('checked',false);
                                    $(".orderHistorySelectAll").prop('checked',false);  // report Page select all
                                    $("#isSelectAllAccount").val(false);//yet to set clear
                                }
                            });



                        if(!$(this).is(":checked")) {
                            if($("#hddnTempAccountList").val().indexOf("," + $(this).val())!=-1){
                                $("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace("," + $(this).val(),""));
                            }else if ($("#hddnTempAccountList").val().indexOf($(this).val())!=-1){
                                $("#hddnTempAccountList").val($("#hddnTempAccountList").val().replace($(this).val(),""));
                            }
                            /*$(".accountSelectionAll").attr("checked", false);*/
                        } else if ($(".accountSelection").length == $(".accountSelection:checked").length) {
                            if($("#hddnTempAccountList").val().indexOf($(this).val())==-1) {
                                $("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
                            }
                            /*$(".accountSelectionAll").attr("checked", true);*/
                        } else {
                            if($("#hddnTempAccountList").val().indexOf($(this).val())==-1) {
                                $("#hddnTempAccountList").val($("#hddnTempAccountList").val() + "," + $(this).val());
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
						$("#orderHisChangeAccountNoAjax").change(function(){
							if((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
								&& $(this).val()!=null && $.trim($(this).val())!=""){
								$("#clearSelectAccountBtnOrderHistory").attr("disabled", false);
								$("#clearSelectAccountBtnOrderHistory").removeClass("tertiarybtn");
								$("#clearSelectAccountBtnOrderHistory").addClass("primarybtn");
							} else if ((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
									&& ($(this).val()==null || $.trim($(this).val())=="")){
								$("#clearSelectAccountBtnOrderHistory").attr("disabled", true);
								$("#clearSelectAccountBtnOrderHistory").removeClass("primarybtn");
								$("#clearSelectAccountBtnOrderHistory").addClass("tertiarybtn");
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
						$(".clearSelectAccountBtnOrderHistory").click(function(){
							orderHistoryAccountSelectionAjax(false, "","");
							$("#orderHisChangeAccountNoAjax").val("");
						});
						$("#orderHisChangeAccountNoAjax").keypress(function(e){
							var key = e.which;
							if(key == 13){
								$("#orderHisSearchSelectAccountBtn").click();
							}
						});
						/** Search button action **/
						$("#orderHisSearchSelectAccountBtn").click(function(){

							if(null!=$("#orderHisChangeAccountNoAjax").val() && $.trim($("#orderHisChangeAccountNoAjax").val())!="") {

								orderHistoryAccountSelectionAjax(false, "", $.trim($("#orderHisChangeAccountNoAjax").val()));
							}
						});
						/** Account selection cancel action **/
						$(".accountSelectionCancel").click(function(){
							$("#hddnTempAccountList").val("");
							$.colorbox.close();
						});
						$(".accountsSubmitlatam").click(function() {
                            var accSelLength = $(".accountSelection:checked").length;
                            sendSelectedAccounts(accSelLength);
                        });

                        checkBoxSort();
					},
					onClosed: function () {

		    },
		  dataType: 'html',
		  async: false
		});
	}
	
	$(".orderHistorySelectAll").change(function(){
		if($(this).prop("checked")) {
			var accSelLength = 1; // temp for byepass
			orderHistSelectAll();
			sendSelectedAccounts(accSelLength);
			setDefault();
		} else {
			var orderHistoryForm = $('#orderHistoryForm');
			$('<input>').attr({
			    type: 'hidden',
			    id: 'resetSelection',
			    name: 'resetSelection',
			    value: true
			}).appendTo(orderHistoryForm);
			orderHistoryForm.submit();
			
		}
	});

	
	function orderHistSelectAll(){
	$(".accountSelection").prop("checked", true);
	$(".orderHistorySelectAll").prop("checked", true);
	$("#isSelectAllAccount").val(true);//yet to set
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
		async : false,
		success: function (data) {
			var accounts=data.split(":");
			var acc=accounts[2].split('"');
			var acco=acc[1].split(",");
			$("#hddnTempAccountList").val(acco);
			var newForm = jQuery('<form>', {
				'action': ACC.config.contextPath +'/order-history',
		        'id' : 'ordHistoryForm',
		        'method' : 'post'
		    });
			var accountList = $("#hddnTempAccountList").val();
			var accountsArray = [];
			if(accountList.indexOf(",")!=-1) {
				accountsArray = accountList.split(",");
				if(accountsArray[0]==""){
					accountsArray.shift();
				}
			} else {
				accountsArray.push(accountList);
			}
			var numberOfAccountsSelected = 0;
			$(accountsArray).each(function() {
				if($.trim(this)!=""){
					$('<input>').attr({
					    type: 'hidden',
					    id: 'accountId',
					    name: 'accounts',
					    value: this
					}).appendTo(newForm);
					numberOfAccountsSelected++;
				}
			});
			$(".selectedAccountsText").html(numberOfAccountsSelected);
		}
	});
}

	function setDefault(){
	//to display in the header of order history when single or multiple accounts selected
	var totalAccountSel = $("#totAccountsSelected").val();
	if(totalAccountSel !== undefined && totalAccountSel !== ""){
		if(parseInt(totalAccountSel) > 1){
			$("#defaultAccountSel").attr("style", "display:none;");
			$("#singleAccountSel").attr("style", "display:none;");
			$("#multipleAccountSel").attr("style", "display:inline-block;");
		}else{
			$("#multipleAccountSel").attr("style", "display:none;");
			$("#defaultAccountSel").attr("style", "display:none;");
			$("#singleAccountSel").attr("style", "display:inline-block;");
		}
	}
	//to make default check if all accounts selected while loading page
	var isSelectFlag = $("#isSelectAllAccount").val();
	if(isSelectFlag === true || isSelectFlag === "true"){
		$(".orderHistorySelectAll").prop("checked", true);
	}
}

	function sendSelectedAccounts (accSelLength){
	if (accSelLength === 0){
		$('#select-accnt-error-order-history').css('display','block');
		return false;
	}
	var newForm = jQuery('<form>', {
		'action': ACC.config.contextPath +'/order-history',
        'id' : 'ordHistoryForm',
        'method' : 'post'
    });
	newForm.append(jQuery('<input>', {
        'name' : 'CSRFToken',
        'value' : ACC.config.CSRFToken,
        'type' : 'hidden'}));
	var isSelectAll = $("#isSelectAllAccount").val();
	var accountList = $("#hddnTempAccountList").val();
	var accountsArray = [];
	if(accountList.indexOf(",")!=-1) {
		accountsArray = accountList.split(",");
		if(accountsArray[0]==""){
			accountsArray.shift();
		}
	} else {
		accountsArray.push(accountList);
	}
	$('<input>').attr({
	    type: 'hidden',
	    id: 'isSelectAllAccount',
	    name: 'selectAllAccount',
	    value: isSelectAll
	}).appendTo(newForm);
	var numberOfAccountsSelected = 0;
	$(accountsArray).each(function() {
		if($.trim(this)!=""){
			$('<input>').attr({
			    type: 'hidden',
			    id: 'accountId',
			    name: 'accounts',
			    value: this
			}).appendTo(newForm);
			numberOfAccountsSelected++;
			
		}
		
	});
	if(numberOfAccountsSelected > 0) {
		$("#Orderhistorypage").append(newForm);
		newForm.submit();
	}

}

});