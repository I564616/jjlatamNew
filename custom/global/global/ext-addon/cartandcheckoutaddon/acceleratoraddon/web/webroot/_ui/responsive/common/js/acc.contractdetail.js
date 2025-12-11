//contract detail page
var contractDetailTable=$('#ContractDetailsPage .contract-detail-desktab').dataTable({
				"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":"No data found."},'pagingType': "simple",
				"iDisplayLength ":3
});
	
var contractDetailTableMobi=$('#ContractDetailsPage .contract-detail-mobi').dataTable({
		"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":"No data found."},'pagingType': "simple",
		"iDisplayLength ":3
});

ACC.contractdetail = {
		bindAll: function()	{
			ACC.contractdetail.addToCartRequest();
			ACC.contractdetail.registerEvent();
			ACC.contractdetail.selectContractEntryList();
		},
		/**
		 * Function to handle on click of 'ADD SELECTION TO CART' or 'ADD TO CART' Contract Detail pages.
		 */
		addToCartRequest: function(e) {
			$(".addContractToCart").click(function() {
				ACC.contractdetail.submitAddToCartForm();
			})
		},
		/*
		 * Registering Event to select all check box for desktop or mobile
		 */
		registerEvent : function(){
			$('#contract-select-all').click(function(){
				ACC.contractdetail.headerCheckClicked(this,contractDetailTable);
			});
			
			$('#contract-select-all-mobi').click(function(){
				ACC.contractdetail.headerCheckClicked(this,contractDetailTableMobi);
			});
			
			$('#contractPopuppage .clsBtn').click(function(){
				loadingCircleShow("hide");  //contract pop up close event
			});
			
			$(".contractDetailPdf").click(function(){
				//contractNumber
				var originalAction = $("#addContractToCartForm").attr("action");
				var url = "/my-account/contract/downloadDetailData?downloadType=PDF";
				var form = "#addContractToCartForm";
				ACC.contractCommon.downloadFile(originalAction,url,"PDF",getShowCount(),form);
			});
			
			$(".contractDetailExcel").click(function(){
				var originalAction = $("#addContractToCartForm").attr("action");
				var url = "/my-account/contract/downloadDetailData?downloadType=EXCEL";
				var form = "#addContractToCartForm";
				ACC.contractCommon.downloadFile(originalAction,url,"EXCEL",getShowCount(),form);
			});
		},
		/*
		 * Handle common function to check or uncheck from desktop or mobile
		 */
		headerCheckClicked : function(headCheckObj,tableObj){
			if($(headCheckObj).prop('checked')==true){
				tableObj.find('.contract-tcell-chckbox').each(function(){
					$(this).prop('checked',true);
				});
			}
			else{
				tableObj.find('.contract-tcell-chckbox').each(function(){
					$(this).prop('checked',false);
				});
			}
		},
		selectContractEntryList: function() {
			//for desktop checkbox
			$(contractDetailTable).find('.contract-tcell-chckbox').click(function(){
				if($(this).prop('checked')==false){
					$('#contract-select-all').prop('checked',false);
				}
				var headCheckFlag=true;
				$(contractDetailTable).find('.contract-tcell-chckbox').each(function(){
					if($(this).prop('checked')==false){
						headCheckFlag=false;
					}	
				});
				if(headCheckFlag==true){
					$('#contract-select-all').prop('checked',true);
				}
				else{
					$('#contract-select-all').prop('checked',false);
				}
			});	
			
			//for mobile checkbox
			$(contractDetailTableMobi).find('.contract-tcell-chckbox').click(function(){
				if($(this).prop('checked')==false){
					$('#contract-select-all-mobi').prop('checked',false);
				}
				var headCheckFlag=true;
				$(contractDetailTableMobi).find('.contract-tcell-chckbox').each(function(){
					if($(this).prop('checked')==false){
						headCheckFlag=false;
					}	
				});
				if(headCheckFlag==true){
					$('#contract-select-all-mobi').prop('checked',true);
				}
				else{
					$('#contract-select-all-mobi').prop('checked',false);
				}
			});
			
		//CP024 Changes: Adding Opacity to Inactive entries and Removing the Check box
		var contractHeaderActive = $("#contractHeaderActive").val();
		var contractEntryCount= $("#totalEntriesInContract").val();
		var inactiveEntryCount = 0;
		$(".contractDetailRow").each(function(){
			var rowId = $(this).attr('id');
			var rowStatus = rowId.split("_")[1]; 
			if(contractHeaderActive=="false" || rowStatus=="false"){
				inactiveEntryCount= parseInt(inactiveEntryCount)+1;
				$(this).addClass("opaque");
				$(this).find(".contract-tcell-chckbox").css("visibility","hidden");
				$(this).find(".contract-tcell-chckbox").removeClass("contract-tcell-chckbox");
			}
		});
		// if($("#contractAddToCartEnable").val()=="false" || (contractHeaderActive=="false" || parseInt(contractEntryCount)==inactiveEntryCount)){
		 if((contractHeaderActive=="false" || parseInt(contractEntryCount)==inactiveEntryCount)){
			$(".addContractToCart").addClass("disabledButton");
			$(".addContractToCart").prop('disabled', true);
		} else {
			$(".addContractToCart").removeClass("disabledButton");
			$(".addContractToCart").prop('disabled', false);
		} 
	},
	/**
	 * Checks and submits Ajax call for selected product for Add to Cart From contract detail page.
	 */
	submitAddToCartForm : function(){
		//ACC.contractdetail.showModalSpinner();
		var numberOfProdSelected = 0;
		var form = $('#addContractToCartForm');
		var contractNum = $("#hddnContract-num").val();
		form.eCCContractNum = contractNum;
		console.log("contractNum : " + contractNum);
		// Iterating checked product
		var selectedProdCodes = [];
		var basePriceFlag = true;
		$(".contract-tcell-chckbox").each( function() {
			if($(this).is(':checked')) {
				selectedProdCodes.push($(this).attr('id'))
				numberOfProdSelected++;
			}
		});
		
		// Setting selected products in hidden input and appending to form
		if(selectedProdCodes.length != 0)  {
			$("#selected-product-codes").val(selectedProdCodes).appendTo(form);
		}
		
		
		//check isContractProduct alone or not
		if(numberOfProdSelected > 0) {
			jQuery.ajax({  ///jnjb2bglobalstorefront/en/mdd-deCMSsite/my-account/contract/isContractProduct
                url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes+'&eCCContractNum='+contractNum,
                async: false,
				success : function(data) {
					var nonContractProductInCart = data.nonContractProductInCart;
					var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
					var datas = {"multiContractCount":data.multiContractCount , "multiProductCount":data.multiProductCount}; 
					if (nonContractProductInCart === nonContractProductInSelectedList && data.multiContractCount=== 0) {
						ACC.contractdetail.submitProductCart(form);
					} else {
						ACC.contractdetail.confirmDialog(form,nonContractProductInCart,nonContractProductInSelectedList,datas);
					}
				}
			});
		}else{
			loadingCircleShow("hide");  // not need to show progress window
		}
	},
	submitProductCart :function(form){
		// ajax call to add product from contract 
			var allValid = true;
			var productAdded = false;
			var errorMsg = "";
			form.ajaxSubmit({
				success : function(data) {
					$.each(data.cartData,function(){
						$.each(this, function(product,msg) {
							if(msg.indexOf("SUCCESS") == -1 && product.indexOf("cartModification") === -1 ) {
								errorMsg = errorMsg +"|"+ msg;
								allValid = false;										
							} else {
								productAdded =true;
							}
						});
					});
					if(!allValid) {
						$("#errorDetailsAddToCart").find("ul li").remove();
						$("#errorTemplateCart").show();
						$(".error_details ul li").remove(); //clearing previous error message
						errorMsg = errorMsg.substring(1, errorMsg.length);
						var msgArray = errorMsg.split('|');
						$.each(msgArray,function(index,element) {
							$(".error_details").find("ul").append("<li>"+element+"</li>");	
						})
						$(document).off('click', '#errorTemplateCart').on('click', '#errorTemplateCart',function(e) {
							//$("#colorbox").hide();
							$(".error_details").toggle();
						});
						loadingCircleShow("hide");
					}else{
						$("#errorTemplateCart").hide();
					}

					if(productAdded){
						console.log(data);
						window.location.href = ACC.config.contextPath +'/cart';
						loadingCircleShow("hide");
					}
				}
			});
		//}
	},
	confirmDialog :function(form,nonContractProductInCart,nonContractProductInSelectedList,datas){
		if(datas.multiContractCount > 0){
			$("#contractTitle").text($("#multiContractMsgTitle").val());
			$("#popInfoText").text($("#multiContractMsgInfoText").val());
			$("#contractMessage").text($("#multiContractMessageText").val());
		}
		else if(nonContractProductInCart && !nonContractProductInSelectedList){ 
    		$("#contractTitle").text($("#contractMsgTitle").val());
			$("#contractMsgInfoText").text($("#contractMsgInfoText").val());
			$("#contractMessage").text($("#contractMessageText").val());
    	}else{
    		$("#contractTitle").text($("#nonContractMsgTitle").val());
			$("#contractMsgInfoText").text($("#nonContractMsgInfoText").val());
			$("#contractMessage").text($("#nonContractMessageText").val());
    	}

		$('#contractpopup').modal('show');
		
		//$('#cancel-btn-addtocart1').click( function() {
		$(document).off('click', '#cancel-btn-addtocart1').on('click', '#cancel-btn-addtocart1',function(e) {
           ACC.contractdetail.callback(form,false,nonContractProductInCart,nonContractProductInSelectedList,datas);
		});
		//$('#accept-btn-addtocart1').click( function() {
		$(document).off('click', '#accept-btn-addtocart1').on('click', '#accept-btn-addtocart1',function(e) {
           ACC.contractdetail.callback(form,true,nonContractProductInCart,nonContractProductInSelectedList,datas);
		}); 
	},
	callback : function (form,value,nonContractProductInCart,nonContractProductInSelectedList,datas) {
		if (value) {
			if(datas.multiContractCount > 0){
				//need to clear cart clear before adding this contract product
				ACC.contractdetail.clearCartProduct(form);
			}else{
				ACC.contractdetail.submitProductCart(form);
			}
	    } else {
	    	//need to call remove product which is already non contract
	    	if(datas.multiContractCount > 0){
	    		//nothing to perform becasue dont want to add this non contract product in the contract product 
	    		loadingCircleShow("hide");
	    	}else if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.contractdetail.removeNonContractProduct(form)
	    	}else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product 
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	removeNonContractProduct : function(form){
		jQuery.ajax({  
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct?contractNum='+form.eCCContractNum,
			async: false,
			success : function(data) {	
				 ACC.contractdetail.submitProductCart(form);
			} 
		});
	},
	clearCartProduct : function(form){
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/clearCart',
			type : 'GET' , 
			success : function(data) {
				//after success then need to add selected product
				ACC.contractdetail.submitProductCart(form);
			}
		});
	}
};

$(document).ready(function() {
	ACC.contractdetail.bindAll();
});
