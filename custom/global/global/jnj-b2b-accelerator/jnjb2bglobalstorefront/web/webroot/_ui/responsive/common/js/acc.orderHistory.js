var orderNumber;
var surgeonName;
var updateSurgeionLink;
var disputeline=true;
var selectAllBaseFlag=false;

//Added for maintaining left nav consistency
//ACC.globalActions.menuNdbodyHeight();

$('#selectall-orderhistory').on('change',function(){
	if($(this).prop("checked")) {
		selectAllBaseFlag=true;
		$("#selectAllAccount").change();
		$('#accountsSubmit,.accountsSubmit').click();
		
	}
	else{
		selectAllBaseFlag=false;
		$("#selectAllAccount").prop('checked',false);
	}
});
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
ACC.orderHistory = {

	bindAll: function()
	{
		ACC.orderHistory.submitShowAndSortRequest();
		//ACC.orderHistory.sortOrderHistoryResults();
		ACC.orderHistory.bindsearchSortForm();
		ACC.orderHistory.submitAddToCartRequest();
		ACC.orderHistory.orderHistoryResetRequest();
		ACC.orderHistory.submitOrderHistoryForm();
		ACC.orderHistory.showMoreOrders();
		ACC.orderHistory.showMoreEntries();
		ACC.orderHistory.selectAccounts();
		/*ACC.orderHistory.updatePONumber();
		ACC.orderHistory.updateSurgeon();*/
		ACC.orderHistory.surgeonInformation();
		ACC.orderHistory.selectAccountAutoComplete();
		ACC.orderHistory.disputeOrderInquiryPopup();
		ACC.orderHistory.disputeItemInquiryPopup();
		ACC.orderHistory.disputeInvoiceItemInquiryPopup();
		//ACC.orderHistory.viewShipmentInfo();
		ACC.orderHistory.setDefault();
		ACC.orderHistory.orderHistoryPageSelectAll();
		 
	},
	submitShowAndSortRequest: function()
	{
		/**
		 * On change of 'Show in groups' drop down submit request along with current sort selection. 
		 */
		/*$('#orderHistShowDdown').change(function()
		{
			var pageSize=$(this).val();
			var sortCode = $('.sortby').find(":selected").val();
			ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
		});	*/
		
		$('#orderHistShowDdown').change(function()
				  {
				   var pageSize=$(this).val();
				   var sortCode = $('#sortbyOrdertemplateHeader').val();
				   ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
				  }); 
				//alert($('#sortbyOrdertemplateHeader').val());
		
		/**
		 * On change of 'Sort By' drop down submit request along with current Page size (Show in groups) selection.
		 */
		/*$('#orderHistSortby').change(function()
		{
			var sortCode=$(this).val();
			var pageSize = $('.showDdown').find(":selected").val();
			ACC.orderHistory.sortOrderHistor yResults(pageSize,sortCode);
		});*/
		
		$("#gtordertemplateHeader1").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn1').val();
			if (ascDescTemplateCounter == "Order Number - decreasing" ){
				//alert("desc");
				var sortCode="Order Number - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Number - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
		
		});
				
		$("#gtordertemplateHeader2").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn2').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order poNumber - decreasing" ){
				//alert("desc");
				
				var sortCode="Order poNumber - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order poNumber - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
		
		$("#gtordertemplateHeader3").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn3').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order Type - decreasing" ){
				//alert("desc");
				var sortCode="Order Type - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Type - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
		
		$("#gtordertemplateHeader4").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn4').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order ShipTo - decreasing" ){
				//alert("desc");
				var sortCode="Order ShipTo - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order ShipTo - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
		
		$("#gtordertemplateHeader5").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn5').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order Date - Newest to Oldest" ){
				//alert("desc");
				var sortCode="Order Date - Newest to Oldest";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Date - oldest to newest";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
		});
		
		$("#gtordertemplateHeader6").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn6').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order Channel - decreasing" ){
				//alert("desc");
				var sortCode="Order Channel - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Channel - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
		
		$("#gtordertemplateHeader7").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn7').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order Status - decreasing" ){
				//alert("desc");
				var sortCode="Order Status - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Status - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
		//total
		$("#gtordertemplateHeader8").on("click", function(){
			var ascDescTemplateCounter = $('#ascDescCounter_hcolumn8').val();
			//alert(ascDescTemplateCounter);
			if (ascDescTemplateCounter == "Order Total - decreasing" ){
				//alert("desc");
				var sortCode="Order Total - decreasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			else{
				//alert("asc");
				var sortCode="Order Total - increasing";
				var pageSize = $('.showDdown').find(":selected").val();
				$('#sortbyOrdertemplateHeader').val(sortCode);
				ACC.orderHistory.sortOrderHistoryResults(pageSize,sortCode);
			}
			});
	},
	
	/**
	 * Submits request for order history based on page size and sort code.
	 */
	sortOrderHistoryResults: function(pageSize, sortCode)
	{
		var orderHistoryForm = $('#orderHistoryForm');
		var showMoreCounter = orderHistoryForm.find('input[name="showMoreCounter"]').val();
		var showMore = orderHistoryForm.find('input[name="showMore"]').val();
		var startDate = orderHistoryForm.find('input[name="startDate"]').val();
		var endDate = orderHistoryForm.find('input[name="endDate"]').val();
		var searchRequest = orderHistoryForm.find('input[name="searchRequest"]').val();
		if(showMore == null || showMore == '')
		{
			showMore=false;
		}
		
		var selectedAccounts=orderHistoryForm.find('input[name="accounts"]').val();
		var selectedAccounts =selectedAccounts.split(",");
		
		var url = ACC.config.contextPath +'/order-history';
		var newForm = jQuery('<form>', {
	        'action': url,
	        'id' : 'ordHistoryForm',
	        'method' : 'post'
	    }).append(jQuery('<input>', {
	        'name': 'sortCode',
	         'id': 'sortCode',
	         'value': sortCode,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'pageSize',
	         'id': 'pageSize',
	         'value': pageSize,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'accounts',
	         'id': 'accounts',
	         'value': selectedAccounts,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'showMoreCounter',
	         'id': 'showMoreCounter',
	         'value': parseInt(showMoreCounter),
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'showMore',
	         'id': 'showMore',
	         'value': showMore,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'startDate',
	         'id': 'startDate',
	         'value': startDate,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'endDate',
	         'id': 'endDate',
	         'value': endDate,
	        'type': 'hidden'
	    })).append(jQuery('<input>', {
	        'name': 'searchRequest',
	         'id': 'searchRequest',
	         'value': searchRequest,
	        'type': 'hidden'
	    })).append(jQuery('<input>', { // Account GLN input created 
	    	'name' : 'CSRFToken',
	    	 'value' : ACC.config.CSRFToken,
	    	 'type' : 'hidden'
	    }));
		
		$(".orderHistoryPage").append(newForm);
		$("#ordHistoryForm").submit();
	},
	
	/**
	 * Submits Search Sort Form from Order Detail page on change.
	 */
	bindsearchSortForm: function()
	{
		$('.searchSortForm').change(function()
				{
					this.submit();
				});
	},
	
	/**
	 * On click of RESET from Order History page, submits the Order History Page Form.
	 */
	orderHistoryResetRequest: function()
	{
		$(".orderHistoryReset").click(function()
				{
					var orderHistoryForm = $('#orderHistoryForm');
					$('<input>').attr({
					    type: 'hidden',
					    id: 'resetSelection',
					    name: 'resetSelection',
					    value: true
					}).appendTo(orderHistoryForm);

					orderHistoryForm.off("submit.validate"); // disable validation to be able to refresh screen regardless possible errors in form
					orderHistoryForm.submit();
				});
	},
	
	/**
	 * Submits Add to Cart request from Order Detail page ONLY when all the products are Saleable.
	 */
	finalSubmitAddToCart: function()
	{
		var invalidProducts = [];
		//$(".orderDetailAddToCart").click(function() {
		loadingCircleShow("show");
		var allProductsSaleable = true;
		$(".saleableInd").each
		(
				function()
				{
					if ($(this).val() == 'false')
					{
						invalidProducts.push($(this).attr("id"));
					}
				}
		);
		invalidProducts = getUniqueElements(invalidProducts);
		/**
		 * If All products on Order Detail page are valid, then only submit 'Add To Cart' request, otherwise display the error message.
		 */
		var orderCode = $(".orderDetailAddToCart").attr("orderCode"); //$(this).attr("orderCode");
			var ajaxurl =ACC.config.contextPath+"/order-history/addToCart?orderCode="+orderCode;
			$("#ajaxCallOverlay").show(); 
			$("#modal-ui-dialog").show();
			$.ajax({
			  type: "POST",
			  url: ajaxurl,
			  success: function(data) 
			  {
				  loadingCircleShow("hide");
				  window.location.reload();
			  },
			  error: function(){
				  $("#ajaxCallOverlay").hide();
				  $("#modal-ui-dialog").hide();
			  }
			});
	//});
	},
	 submitAddToCartRequest: function(e) {
		 $(".orderDetailAddToCart").click(function(e) {
			 loadingCircleShow("show");
			setTimeout(function(){
			 /* 
			var selectedProdCodes = [];
			//to submit this order Code is needed
			//var orderCode = $(this).attr("orderCode"); 
			
			$(".orderHistoryList").each( function(i,v) {
				selectedProdCodes.push($(this).attr('id')); 
			});
			
			jQuery.ajax({   
				url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
				async: false,
				success : function(data) {
					var nonContractProductInCart = data.nonContractProductInCart;
					var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
					console.log("nonContractProductInCart : "+ nonContractProductInCart +" and nonContractProductInSelectedList :"+ nonContractProductInSelectedList);
					 if (nonContractProductInCart === nonContractProductInSelectedList) {
						 ACC.orderHistory.finalSubmitAddToCart();
					} else {
						ACC.orderHistory.confirmOrderHistoryProduct(nonContractProductInCart,nonContractProductInSelectedList);
					} 
				}
			}); 
		*/

				
				
				//setTimeout(function(){loadingCircleShow("show");},100);
				var prodQnt=1;//AAOL-6377
				var prodCodes=[];//AAOL-6377
				invalidProducts = [];
				validProductsAndQty = [];
				var allProductsSaleable = true;
				var selectedProdCodes = [];
				var combinedProdCodes =[];
				var validProdCodes =[];
				var productAdded = false;
				var invalidProduct= false;
				var orderCode = $(".orderDetailAddToCart").attr("orderCode"); 		
				/*setTimeout(function(){	*/
						$(".saleableIndicator").each
						(
								function()
								{
									if ($(this).val() == 'false')
									{
										invalidProducts.push($(this).attr("id"));
										combinedProdCodes.push($(this).attr("id")+":"+$(this).attr("qty"));
										prodCodes.push($(this).attr("id"));//AAOL-6377
									}	
									else{	
										validProductsAndQty.push($(this).attr("id")+":"+$(this).attr("qty"));
										selectedProdCodes.push($(this).attr("id"));
										validProdCodes.push($(this).attr("id")+":"+$(this).attr("qty"));
									}
									
								}
								
						);
						var invalidProductString = combinedProdCodes.join(',');
						var validProdString = validProdCodes.join(','); 
						/*AAOL-6377 Start*/
						var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(combinedProdCodes);
						if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
							console.log("useer entry prodIds " +combinedProdCodes );
							console.log("useer entry prodIds " +selectedProdCodes );
							ACC.addToCartHome.checkReplacementProductPopup(combinedProdCodes,prodCodes,prodQnt,null);//AAOL-6377 here there is no active products, thats why we are passing null in place all selected products.
						}
						/*AAOL-6377 END*/
						/*if(invalidProducts.length != 0){
							
							$(".errror-details-tmplt").show();
						}*/
				 		var teamplateAddToCartForm = jQuery('<form>', {
							'action' : ACC.config.contextPath + "/cart/add",
							'id' : 'teamplateAddToCartForm',
							'method' : 'POST',
							'name':'addToCartForm'
						}).append(jQuery('<input>', { // Account GLN input created 
					    	'name' : 'CSRFToken',
					    	 'value' : ACC.config.CSRFToken,
					    	 'type' : 'hidden' 
					    }));
						
						
				 		var numberOfProdSelected = 0;
				 		$(validProductsAndQty).each(function(index,element){
				 			
				 			$('<input>').attr({
							    type: 'hidden',
							    id: index,
							    name: 'productCodeAndQty',
							    value: element
							}).appendTo(teamplateAddToCartForm);
							numberOfProdSelected++;
				 		});
				 		if(numberOfProdSelected > 0)
						{
				 			
				 			jQuery.ajax({   
								url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
								async: false,
								success : function(data) {
									var nonContractProductInCart = data.nonContractProductInCart;
									var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
									if (nonContractProductInCart === nonContractProductInSelectedList) {
//										ACC.orderTemplate.templateDetailsNonContractProductAddToCart(teamplateAddToCartForm);
										ACC.orderHistory.ValidateNonContractProductAddToCart(validProdString);
										
									} else {
//										ACC.orderTemplate.confirmOrderTemplate(selectedProdCodes,teamplateAddToCartForm,nonContractProductInCart,nonContractProductInSelectedList);
									}
								}
							});
				 			
				 			
						}
				 		if(invalidProducts.length != 0){
				 			ACC.orderHistory.ValidateNonContractProductAddToCart(invalidProductString);
				 			if (numberOfProdSelected>0){
								productAdded =true;
							}
							if(productAdded){
								loadingCircleShow("hide");
								setTimeout(function(){
									window.location.href = ACC.config.contextPath +'/order-history/order/'+orderCode;
								}, 7500);
								 $("#errorMultiCartTmplt").show();
								
								/* if(invalidProduct){
									
								 }*/
								
							}
							else{
                                                        	location.reload();
                                                	}		

				 		}else{
				 			location.reload();
				 		}
				 		
					},10);	

					});
	},
	
	ValidateNonContractProductAddToCart : function(prodIds){
		//loadingCircleShow("show");
			var orderCode = $(".orderDetailAddToCart").attr("orderCode"); 	
			var dataObj = new Object();
			var allValid = true;
			var productAdded = false;
			var errorMsg = "";
			var productIds=prodIds;
			var invalidProduct= false;
			/*var teamplateAddToCartForm = jQuery('<form>', {
				'action' : "#",
				'id' : 'teamplateAddToCartForm',
				'method' : 'POST',
				'name':'addToCartForm'
			});*/
//			dataObj.source = "templateDetailAddToCartId";
			dataObj.prodIds = prodIds;
					jQuery.ajax({
						type: "POST",
						url:  ACC.config.contextPath+"/order-history/addToCart",
						data: dataObj,
						 async: false,
						success: function (data) {
							/*console.log(data);*/
							$.each(data.cartModData,function(){
								$.each(this, function(product,msg) {
									if(msg.indexOf("SUCCESS") == -1){
										errorMsg = errorMsg +"|"+ msg;
										allValid = false;
									}
									else
									{
										productAdded =true;
									}
								});
							});
							if(!allValid)
							{	
								invalidProduct= true;
								setTimeout(function(){
										loadingCircleShow("hide");},500);
										$("#errorMultiCartTmplt").show();
										
									$(".error-content div").remove(); 
									errorMsg = errorMsg.substring(1, errorMsg.length);
									var msgArray = errorMsg.split('|');
									$.each(msgArray,function(index,element)
									{
										
										$("#error-detail-popup").find(".error-content").append("<div style='margin-bottom:10px'>"+element+"</div>");

									})
									var popupOpned=false;
									$("#errorMultiCartTmplt").on('click',function(){
										$("#error-detail-popup").modal('show');
										//clearTimeout(stopHidePopup);
									})
									
									
								}
							if (productAdded){
								setTimeout(function(){
									//loadingCircleShow("hide");
									
								}, 500);
								setTimeout(function(){
									loadingCircleShow("hide");
									window.location.href = ACC.config.contextPath +'/order-history/order/'+orderCode;
								}, 7500);
								if(!allValid){
									$("#errorMultiCartTmplt").show();
								}
								 
								
								/* if(invalidProduct){
									
								 }*/
								
							}
							if(invalidProduct){
								//location.reload();
								 loadingCircleShow("hide");
							}
						
						}
					
					});
					
			
		},
	 
	/**
	 * Submits request/form on click of SEARCH from Order History page.
	 */
		submitOrderHistoryForm: function()
		{
			$('#searchByInput').on('keypress', function(e)
					{
				 		if (e.which == 13) 
				 		{
				 			searchOrders();
				 		}
					});
			
			function searchOrders(){
				if($("#orderHistoryForm").valid()){
					loadingCircleShow("show");
					var orderHistoryForm = $("#orderHistoryForm");
					orderHistoryForm.find('input[name="searchRequest"]').val(true);
					orderHistoryForm.find('input[name="showMoreCounter"]').val(0);
					orderHistoryForm.submit();
				} 
			}
			
			/*** On click of Search, set the 'searchRequest' attribute in the form and reset 'showMoreCounter' back to 0 ***/
			$("#ordHistorySearch").click(function(e) {
				searchOrders();
			});
		},
	
	/**
	 * Submit requests on click of 'Show More' from Order History page.
	 */
	showMoreOrders: function(e)
	{
		$(".showMoreOrders").click(function()
		{
			var orderHistoryForm = $('#orderHistoryForm');
			var searchRequest = orderHistoryForm.find('input[name="searchRequest"]').val();
			var showMoreCounter = orderHistoryForm.find('input[name="showMoreCounter"]').val();
			var pageSize =  $('.showDdown').find(":selected").val();
			var counterInteger =  parseInt(showMoreCounter);
			counterInteger++;
			
			/*** If "Show More" is clicked:
			 * 1. After search request - Then submit this request with the same form having all values binded to it from previous search request.
			 * 2. Otherwise - Submit request with new form carrying only show more flag and counter values.  
			 ***/
			if(searchRequest == 'true')
			{
				$('<input>').attr({
				    type: 'hidden',
				    id: 'showMore',
				    name: 'showMore',
				    value: true
				}).appendTo(orderHistoryForm);
				
				orderHistoryForm.find('input[name="showMoreCounter"]').val(counterInteger);
				orderHistoryForm.submit();
			}
			else
			{
				var sortCode = $('.sortby').find(":selected").val();
				var selectedAccounts=orderHistoryForm.find('input[name="accounts"]').val();
				var selectedAccounts =selectedAccounts.split(",");
				
				var newForm = jQuery('<form>', {
			        'action': ACC.config.contextPath +'/order-history',
			        'id' : 'emptyOHForm',
			        'method' : 'post'
			    }).append(jQuery('<input>', {
			        'name': 'showMore',
			         'id': 'showMore',
			         'value': true,
			        'type': 'hidden'
			    })).append(jQuery('<input>', {
			        'name': 'showMoreCounter',
			         'id': 'showMoreCounter',
			         'value': counterInteger,
			        'type': 'hidden'
			    })).append(jQuery('<input>', {
			        'name': 'pageSize',
			         'id': 'pageSize',
			         'value': pageSize,
			        'type': 'hidden'
			    })).append(jQuery('<input>', {
			        'name': 'accounts',
			         'id': 'accounts',
			         'value': selectedAccounts,
			        'type': 'hidden'
			    })).append(jQuery('<input>', {
			        'name': 'sortCode',
			         'id': 'sortCode',
			         'value': sortCode,
			        'type': 'hidden'
			    })).append(jQuery('<input>', { // Account GLN input created 
			    	'name' : 'CSRFToken',
			    	 'value' : ACC.config.CSRFToken,
			    	 'type' : 'hidden' 
			    }));
				$(".orderHistoryPage").append(newForm);
				$("#emptyOHForm").submit();
			}
		})
	},
	
	/**
	 * Handles 'Show More' request from Order Detail page.
	 */
	showMoreEntries: function()
	{
		$(".showMoreEntries").click(function()
		{
			var form = $('#searchSortForm1');
			
			$('<input>').attr({
			    type: 'hidden',
			    id: 'showMore',
			    name: 'showMore',
			    value: true
			}).appendTo(form);
			
			if(typeof form.find('input[name="pageSize"]').val() == "undefined")
			{
				$('<input>').attr({
				    type: 'hidden',
				    id: 'pageSize',
				    name: 'pageSize',
				    value: 10
				}).appendTo(form);
			}
			
			var showMoreCounter = $('#showMoreCounter').val();
			var counterInteger =  parseInt(showMoreCounter);
			counterInteger++;
			$('#showMoreCounter').val(counterInteger);
			$('#showMore').val(true);
			
			form.submit();
		})
	},
	
	selectAccounts: function()
	{
		$("#selectAccount").click(function(e) {
			loadingCircleShow("show");
		
			$('.modal-backdrop').show();
			var selectedAccounts = [];
			$('input[name=accounts]').each(function(){
				selectedAccounts.push(this.value);
			});
			orderHistoryAccountSelectionAjax(true,"","");
			checkBoxSort();
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
				  //console.log(" isOrderHistorySelectAllChecked : "+isOrderHistorySelectAllChecked);
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
								ACC.orderHistory.orderHistSelectAll();
							} else {
								$(".accountSelection").prop("checked", false);
								$(".orderHistorySelectAll").prop("checked", false);  
								$("#isSelectAllAccount").val(false); 
								$("#hddnTempAccountList").val("");
								$(".selectedAccountsText").html("0");
							}
							/*if($(this).prop("checked")) {
								$(".accountSelection").prop("checked", true);
								$(".orderHistorySelectAll").prop("checked", true);
								$("#isSelectAllAccount").val(true); 
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
							} else {
								$(".accountSelection").prop("checked", false);
								$(".orderHistorySelectAll").prop("checked", false);  
								$("#isSelectAllAccount").val(false); 
								$("#hddnTempAccountList").val("");
								$(".selectedAccountsText").html("0");
							}*/
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
								orderHistoryAccountSelectionAjax(false, "","");
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
									
									orderHistoryAccountSelectionAjax(false, "", $.trim($("#orderHisChangeAccountNoAjax").val()));
								}
							});
							/** Account selection cancel action **/
							$(".accountSelectionCancel").click(function(){
								$("#hddnTempAccountList").val("");
								$.colorbox.close();
							});
							ACC.orderHistory.submitSelectedAccounts();
						},
						onClosed: function () {
						/*}
					});*/
			    },
			  dataType: 'html',
			  async: false
			});
		}
	},
	
	
	accountsSelectionCheck: function() {
		$("#selectAllAccount").click(function() {
			if($("#selectAllAccount").is(':checked')) {
				$(".accountSelection").each(function() {
					$('.accountSelection').attr('checked', 'checked');
				});
				$(".orderHistorySelectAll").prop('checked',true);  // report Page select all
				$("#isSelectAllAccount").val(true);//yet to set 
			} else {
				$(".accountSelection").each(function(){
					$('.accountSelection').removeAttr('checked');
				 });
			}
		})
		
		$("#searchAccount").on('keyup', function() {
			var valThis = $(this).val().toLowerCase();
			if (valThis == "") { // On blank, show all results
					$(".accountSelection").parent().show();
					$(".changeAccountUnit").parent().parent().show();
				} else { // Filter condition
					$('.changeAccountUnit').each(function() {
						var text = $(this).text().toLowerCase();
						// Hide or show if value matches
						if(text.indexOf(valThis) >= 0){
							$(this).parent().parent().show();
							$("#accountSelection_" + $(this).attr("id")).parent().show();
						} else {
							$(this).parent().parent().hide();
							$("#accountSelection_" + $(this).attr("id")).parent().hide();
						}
					});
				}
 			}
		);
	},
	selectAccountAutoComplete: function()
	{
	 	$("#searchAccount").on('keyup', function()
				{
					text = $(this).val();
					$("li[class|='accountId']").each(function(){
						accNameId = $(this).find("label").html().toLowerCase();
						if(accNameId.indexOf($.trim(text)) == -1)
							{
								$(this).hide();
							}
					})
				});
	},
	
	submitSelectedAccounts: function(e)
	{
		$("#accountsSubmit,.accountsSubmit").click(function() {
			var accSelLength = $(".accountSelection:checked").length;
			ACC.orderHistory.sendSelectedAccounts(accSelLength);
			/*if ($(".accountSelection:checked").length == '0'){
				$('#select-accnt-error-order-history').css('display','block');
			
				return false;
			}
			var newForm = jQuery('<form>', {
				'action': ACC.config.contextPath +'/order-history',
		        'id' : 'ordHistoryForm',
		        'method' : 'post'
		    });
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
			}*/
		});
	},
	
	/**
	 * Submits Ajax request for Purchase Order update with the updated value submitted by the user.
	 */
	submitPoNumberUpdate: function(orderNumber)
	{
		$("#submitPoNumber").click(function()
				{
			
		
			var errorMsg = $('#thirdPartyErrorMsg').val();
					if($.trim($("#poNumber").val())==""){
						$("#poNumber").parent().parent().find('div.registerErrorPO').html("<label class='error'>" + errorMsg + "</label>");	
//						$.colorbox.resize();
						$(".registerErrorPO").show();
						return false;
					} else {
						$(".registerErrorPO").hide();
//						$.colorbox.resize();
					}
					var updatePONumberRequestUrl = ACC.config.contextPath+"/order-history/updatePurchaseOrderRequest";
					var updatedPoNumber = $("#poNumber").val() ;
					$.ajax({
						  type: "POST",
						  url: updatePONumberRequestUrl,
						  data: { orderNumber: orderNumber, purchaseOrderNumber : updatedPoNumber} ,
						  success: function(data) 
						  	{
							  if(data)
							  {
								 $("#updatePoNumberPopup").modal("hide");
								 $(".modal-backdrop").hide();
								 $("#success-dialogPopup").modal("show");
								//var completeMsg = "Purcahse Order Number updated successfully."; 
//								$.colorbox.close();
								///alert(completeMsg);
								 $("#dialog-ok").click(function(){
										window.location.reload();
								 });					
							  }
							  else
							  {	
								  var errorMsg = CONTACT_CUSTOMER
								  poUpdateFailSpan = $('#poUpdateFail');
								  poUpdateFailSpan.find("label").html(errorMsg);
								  poUpdateFailSpan.show();
//								  $.colorbox.resize();
							  }
						    }
						});
				});
	},
	
	


	
	/*submitSurgeonUpdate: function(orderNumber)
	{
		$('.surgeonDetail').click(function()
				{
					inputText = $('input[name=surgeonName]');
					inputText.val($(this).attr("id"));
					surgeonId = $(this).attr("surgeonId");
					inputText.attr("surgeonId",surgeonId);
				}),
				
		$("#submitSurgeonUpdate").click(function()
				{
			
					if($.trim($("#surgeonIdInput").val())==""){
						$("#globalErrorSuergeon").show();
						$.colorbox.resize();
						return false;
					} else {
						$("#globalErrorSuergeon").hide();
						$.colorbox.resize();
					}
					var updateSurgeonRequestUrl = ACC.config.contextPath+"/order-history/updateSurgeonRequest";
					var surgeonInput = $("#surgeonIdInput");
					var selectedSurgeonId =  surgeonInput.attr("surgeonId");
					$.ajax({
						  type: "POST",
						  url: updateSurgeonRequestUrl,
						  data: { orderNumber: orderNumber, selectedSurgeonId : selectedSurgeonId} ,
						  success: function(data) 
						  	{
							  if(data != null || data != '')
							  {
								  var completeMsg = "Surgeon has been updated successfully."; 
									alert(completeMsg);
									$.colorbox.close();
							  }
							  else
							  {	
								  var errorMsg = "Your update could not be completed at this time. Please try again later or contact customer service for assistance.";
								  surgeonUpdateFailSpan = $('#surgeonUpdateFail');
								  surgeonUpdateFailSpan.find("label").html(errorMsg);
								  surgeonUpdateFailSpan.show();
								  $.colorbox.resize();
							  }
						    }
						});
				});
	},*/
	
	/**
	 * Defines type-ahead search criteria for Surgeon Update Pop up.
	 */

		surgeonUpdateAutoComplete : function() {
		$("#searchSurgeon").on('keyup', function() {
			text = $(this).val();
			$("li[class|='surgeonId']").each(function() {
				surgeonDetail = $(this).find('a').html().toLowerCase();
				
				if (surgeonDetail.indexOf($.trim(text)) == -1) {
					$("#noResultText").hide();
					$(this).hide();
				} else {
					$("#noResultText").hide();

					$(this).show();
				}

			})
			var totalCount = $("li[class|='surgeonId']:visible").length;
			if (totalCount == 0) {
				$("#noResultText").show();
			}

		});
	},
	
	
	
	
	
	disputeOrderInquiryPopup: function()
	{
		  ACC.orderHistory.disputeOrderInquiryFormCheck();
			
		  
		  /**
			 * Allows ONLY backspace, tab, delete, arrows, numbers and keypad numbers in the Quantity text boxes.
			 */
			$('.prodQty').on('keyup keypress', function()
					{
				        var value = $(this).val();
				        pttern = /^\d{1,6}$/g;
				        if(value.match(pttern))
				        {
							return true;
				        }
						else
						{
							$(this).val(value.substring(0,value.length-1));
						}
				
				    });
			
			$('#postalCode').on('keyup keypress', function()
					{
				        var value = $(this).val();
				        pttern = /^\d{1,6}$/g;
				        if(value.match(pttern))
				        {
							return true;
				        }
						else
						{
							$(this).val(value.substring(0,value.length-1));
						}
				
				    });
			
			
		  $('#productShipmentYes').click(function()
					{
			  				$('#poNumber').prop('readonly',false);
							$('#newPONumber').show();
							$('#productShipmentNo').removeAttr("checked")
							$('#productShipmentYes').attr("checked","checked")
					});
							
			$('#productShipmentNo').click(function()
					{
							$('#poNumber').prop('readonly',true);
							$('#productShipmentNo').attr("checked","checked")
							$('#productShipmentYes').removeAttr("checked")
							$('#newPONumber').hide(); 
							
							
							
					});
			
			
			
			//checking the 
			$('.disputeOrder .toggle-link').click(function(){
				checkAccordian($(this).attr('href'));
			});
			
			function checkAccordian(id){
				$(id).find('.inputTxt').prop('disabled',false);
				$(id).find('.inputchkbox').prop('disabled',false);
			}
			
			/**
			 * On submit of Order dispute, perform validation for all selected segments i.e. if none of the requried field is being blank. 
			 */
			$('.submitDisputeOrder').click(function()
				{
					var inquiryFormValidation = true;
					var productEntered = true;
					if($('#collapse1').hasClass("in"))
					{
						//For product lines
						/*** Set flag if none of the product has been enetered. ***/
						for(i=0;i<4;i++)
						{
							if(!$('#prodCode_'+currViewScreenName+'_'+i).val())
							{
								productEntered = false;
							}
							break;
						}
						
						if(!productEntered)
						{
							$('.productCodeValidationError').show();
							$('.emptyQtyError').hide()
							inquiryFormValidation=false;
						}
						else
						{
							if($('.productCodeValidationError').is(':visible'))
							{
								$('.productCodeValidationError').hide();
							}
							
							$('.prodCode').each
							(
								function()
								{
									if($(this).val())
									{
										var currentIndex = $(this).attr("index");
										if(!$('#prodQty_'+currViewScreenName+'_'+currentIndex).val())
										{
											$('#emptyQtyError_'+currViewScreenName+'_'+currentIndex).show();
											inquiryFormValidation = false;
										}
										else if($('#emptyQtyError_'+currViewScreenName+'_'+currentIndex).is(':visible'))
										{
											$('#emptyQtyError_'+currViewScreenName+'_'+currentIndex).hide();
										}
									}
								}
							)
						}
							
						if($('#productShipmentYes').is(":checked") && !$('#poNumber').val())
						{
							$('.poNumberValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.poNumberValidationError').is(':visible'))
						{
							$('.poNumberValidationError').hide();
						}
						
						
					}
					else{
						$('#collapse1').find('.inputTxt').prop('disabled',true);
						$('#collapse1').find('.inputchkbox').prop('disabled',true);
					}
					
					if($('#collapse2').hasClass("in"))
					{
						var disputeOrderInquiryForm = $('#disputeOrderInquiryForm');
						if($("#manual").is(":checked")){
							var manualVal = $("#manual").val();
							$('<input>').attr({
							    type: 'hidden',
							    id: 'disputedFees',
							    name: 'disputedFees',
							    value: manualVal
							}).appendTo(disputeOrderInquiryForm);
						}
						if($("#minimum").is(":checked")){
							var minimumVal = $("#minimum").val();
							$('<input>').attr({
							    type: 'hidden',
							    id: 'disputedFees',
							    name: 'disputedFees',
							    value: minimumVal
							}).appendTo(disputeOrderInquiryForm);
						}
						if($("#expedited").is(":checked")){
							var expeditedVal = $("#expedited").val();
							$('<input>').attr({
							    type: 'hidden',
							    id: 'disputedFees',
							    name: 'disputedFees',
							    value: expeditedVal
							}).appendTo(disputeOrderInquiryForm);
						}
						if($("#shipping").is(":checked")){
							var shippingVal = $("#shipping").val();
							$('<input>').attr({
							    type: 'hidden',
							    id: 'disputedFees',
							    name: 'disputedFees',
							    value: shippingVal
							}).appendTo(disputeOrderInquiryForm);
						}
						if($('.disputeFee').find('input[type="checkbox"]:checked').length == 0)
						{
							$('.disputeFeeValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.disputeFeeValidationError').is(':visible'))
						{
							$('.disputeFeeValidationError').hide();
						}
						
						
						
						//if(form-elemnt-error)
					}
					else{
						$('#collapse2').find('.inputTxt').prop('disabled',true);
						$('#collapse2').find('.inputchkbox').prop('disabled',true);
					}	
					
					if($('#collapse3').hasClass("in"))
					{
						if(!$('#correctPoNumber').val())
						{
							$('.correctPONumValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.correctPONumValidationError').is(':visible'))
						{
							$('.correctPONumValidationError').hide();
						}
					}
					else{
						$('#collapse3').find('.inputTxt').prop('disabled',true);
						$('#collapse3').find('.inputchkbox').prop('disabled',true);
					}
					
					
					if($('#collapse4').hasClass("in"))
					{
						if(!$('#shipCountry').val())
						{
							$('.correctCountryValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.correctCountryValidationError').is(':visible'))
						{
							$('.correctCountryValidationError').hide();
						}
							
						
						if(!$('#addressLine1').val())
						{
							$('.addLine1ValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.addLine1ValidationError').is(':visible'))
						{
							$('.addLine1ValidationError').hide();
						}
						
						
						if(!$('#town').val())
						{
							$('.cityValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.cityValidationError').is(':visible'))
						{
							$('.cityValidationError').hide();
						}
						
						if(!$('#shipStates').val())
						{
							$('.stateValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.stateValidationError').is(':visible'))
						{
							$('.stateValidationError').hide();
						}
						
						
						if(!$('#postalCode').val())
						{
							$('.zipCodeValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.zipCodeValidationError').is(':visible'))
						{
							$('.zipCodeValidationError').hide();
						}
						
						var incorrectAddFlag=true;;
						$('#collapse4 .form-elemnt-error').each(function(i){
							if($(this).is(":visible")){
								incorrectAddFlag=false;
								
							}
						});
						
						if(incorrectAddFlag){$('#incorrect-add-err').hide();}
						else{$('#incorrect-add-err').show();}
					}
					else{
						$('#collapse4').find('.inputTxt').prop('disabled',true);
						$('#collapse4').find('.inputchkbox').prop('disabled',true);
					}
					
					if($('#collapse5').hasClass("in"))
					{
						if(!$('#taxExemptCertificate').val())
						{
							$('.taxCertiValidationError').show();
							inquiryFormValidation = false;
						}
						else if($('.taxCertiValidationError').is(':visible'))
						{
							$('.taxCertiValidationError').hide();
						}
						
					}
					else{
						$('#collapse5').find('.inputTxt').prop('disabled',true);
						$('#collapse5').find('.inputchkbox').prop('disabled',true);
					}
					
					if(!$('#collapse1').hasClass("in") && !$('#collapse2').hasClass("in") &&
							!$('#collapse3').hasClass("in") && !$('#collapse4').hasClass("in") && 
							!$('#collapse5').hasClass("in"))
					{
						inquiryFormValidation = false;
					}
						
					if(inquiryFormValidation)
					{
						var disputeOrderInquiryForm = $('#disputeOrderInquiryForm');
						$('.prodCode').each(function(){
							if($(this).val() != null && $(this).val())
								{
									productCode = $(this).val();
									currentIndex = $(this).attr("index");
									
									lotNumbers = $('#lotNumbers_'+currViewScreenName+'_'+currentIndex).val();
									lotNumbers = lotNumbers.replace(/\,/g, '^');
									qty = $('#prodQty_'+currViewScreenName+'_'+currentIndex).val();
									
									$('<input>').attr({
									    type: 'hidden',
									    id: 'unorderedProductsInfo',
									    name: 'unorderedProductsInfo',
									    value: productCode + ":" + lotNumbers + "~" + qty
									}).appendTo(disputeOrderInquiryForm);
								}
						});
						
						disputeOrderInquiryForm.submit();	 
					}
					ACC.globalActions.menuNdbodyHeight();
				});
		  
			
	},
	
	
	disputeOrderInquiryFormCheck: function()
	{
		//To display the states for the company information page - the ship to country section
		$("#shipCountry").bind('change', function() {
			
			var countryElement = this;
			if( $(this).val() != null)
			{
				var country = countryElement.value;
				var selectedCountry = $(this).val();
				var dataObj = new Object();
				dataObj.country = selectedCountry; 
				
				jQuery.ajax(
						{
							type: 'POST',
							url: ACC.config.contextPath +'/register/getStates',
							data: dataObj,
							success: function (data)
									{
										var stateOptions="<option value=''>Select State</option>";
										for(var i=0;i<data.length;i++){
											stateOptions=stateOptions+ "<option value="+data[i].isocode+">"+data[i].name+"</option>";
					}
									
					$("#shipStates").html(stateOptions).selectpicker('refresh');
				},
				error: function (e) 
					{
					
					}
				});
			}
		});
	},
	
	disputeInvoiceItemInquiryPopup: function(){
		
		
		/*** If Replacement required is checked out, display replacement PO Number layer.  ***/
		$('#replacementRequiredYes').click(function()
		{
			$('#replacementRequiredNo').removeAttr("checked")
			$('#replacementRequiredYes').attr("checked","checked")
			if($('#replacementRequiredYes').is(":checked"))
				{
					$('.replacedPo').show();
				}
			
		});
		
		/*** If Replacement required is checked out, display replacement PO Number layer.  ***/
		$('#replacementRequiredNo').click(function()
		{
			$('#replacementRequiredYes').removeAttr("checked")
			$('#replacementRequiredNo').attr("checked","checked")
			if($('#replacementRequiredNo').is(":checked"))
				{
					$('.replacedPo').hide();
				}
			
		});
		
		$('#productShipmentYesDL').click(function()
				{
						$('#poNumber').prop('readonly',false);
						$('#newPONumber').show();
						$('#productShipmentNo').removeAttr("checked")
						$('#productShipmentYes').attr("checked","checked")
				});
						
		$('#productShipmentNoDL').click(function()
				{
						$('#poNumber').prop('readonly',true);
						$('#productShipmentNo').attr("checked","checked")
						$('#productShipmentYes').removeAttr("checked")
						$('#newPONumber').hide(); 
		
				});
		
									/*** Enable submit button until either of the dispute check box is selected ***/
									$('#collapse1, #collapse2, #collapse3').click(function()
									{
										$('.naSubmitDisputeItem').removeAttr("disabled");
									});
									
									
									
									/**
									 * Allows ONLY backspace, tab, delete, arrows, numbers and keypad numbers in the Quantity text boxes.
									 */
									$('.correctPrice , .prodQtyReceived , .prodQtyReceivedOver ')
										.on('keyup keypress', function()
											{
										        var value = $(this).val();
										        pttern = /^\d{1,6}$/g;
										        if(value.match(pttern))
										        {
													return true;
										        }
												else
												{
													$(this).val(value.substring(0,value.length-1));
												}
										
										    });
									
									/*accordian click*/
									$('.disputeItemInvoice .toggle-link').on('click',function(){
										$('.invoicedisputeerror').hide();
										$('.dispute-an-item-table th,.dispute-an-item-table td').removeClass('normal-cell').addClass('disable-cell');
										$('.invoice-dispute-checkbox input[type="checkbox"]').prop('checked',false);
										$('.dispute-an-item-table .invoiceDisputeTxtField,.dispute-an-item-table .invoiceDisputeQtyTxtField').prop('disabled',true);
										
									});
									
									/*** On click of submit dispute Item, validate all required fields and if no vlaidation errors submit the corresponding form ***/
									$('.naSubmitDisputeItem').click(function()
											{
											var productEntered = true;
										    var formValidationFlag =true;
										    var formSubmitFlag =false;
											var disputeItemInquiryForm = $('#disputeItemInquiryForm');
											var prodCodes =[];
											var length = 0;
											var prodsChecked = 0;
											var prodsShrtChecked = 0;
											var prodsOverChecked = 0;
											
											$('.prodCode').each
											(
												function()
												{
													if($(this).attr("data"))
													{
														
														length=length+1;
													}
												}
													)
											if($('#collapse1').hasClass("in"))
											{
												
												//For product lines
												/*** Set flag if none of the product has been enetered. ***/
												for(i=0;i<length;i++)
												{
													if(!$('#prodCode_'+currViewScreenName+'_'+i).attr("data"))
													{
														productEntered = false;
														
													}
													break;
												}
											
												if(!productEntered)
												{
													
													$('.productCodeValidationError').show();
													$('.emptyQtyError').hide();
													$('.emptyDispPriceError').hide();
													$('.emptyCorrectPriceError').hide();
													$('.emptyInvoiceNumberError').hide();
													formValidationFlag=false;
												
												}
												else
												{
													
													if($('.productCodeValidationError').is(':visible'))
													{
														$('.productCodeValidationError').hide();
													}
													
													$('.prodCode').each
													(
														function()
														{
															if($(this).attr("data"))
															{
																var currentIndex = $(this).attr("index");
																if((!$('#correctPrice_'+currViewScreenName+'_'+currentIndex).val())&&($('#prodCheck_'+currViewScreenName+'_'+currentIndex).is(':checked')))
																{
																	$('#emptyCorrectPriceError_'+currViewScreenName+'_'+currentIndex).show();
																	formValidationFlag = false;
																}
																else if($('#emptyCorrectPriceError_'+currViewScreenName+'_'+currentIndex).is(':visible'))
																{
																	$('#emptyCorrectPriceError_'+currViewScreenName+'_'+currentIndex).hide();
																}
															}
														}
														
													)
												}
													
											
											}
											
											if($('#collapse2').hasClass("in"))
											{
												
												//For product lines
												/*** Set flag if none of the product has been enetered. ***/
												for(i=0;i<length;i++)
												{
													if(!$('#prodCodeShort_'+currViewScreenName+'_'+i).attr("data"))
													{
														productEntered = false;
														
													}
													break;
												}
												if(!productEntered)
												{
													
													$('.productCodeValidationError').show();
													$('.emptyQtyreceivedError').hide();
													$('.emptyQtyorderedError').hide();
													$('.emptyReplacementError').hide();
													$('.emptyinvoiceNumbershortError').hide();
													formValidationFlag=false;
												
												}
												else
												{
													
													if($('.productCodeValidationError').is(':visible'))
													{
														$('.productCodeValidationError').hide();
													}
													
													$('.prodCodeShort').each
													(
														function()
														{
															if($(this).attr("data"))
															{
																
																
																var currentIndex = $(this).attr("index");
																
																if(!$('#prodQtyReceived_'+currViewScreenName+'_'+currentIndex).val()&&($('#prodCheckShort_'+currViewScreenName+'_'+currentIndex).is(':checked')))
																{
																	$('#emptyQtyreceivedError_'+currViewScreenName+'_'+currentIndex).show();
																	formValidationFlag = false;
																}
																else if($('#emptyQtyreceivedError_'+currViewScreenName+'_'+currentIndex).is(':visible'))
																{
																	$('#emptyQtyreceivedError_'+currViewScreenName+'_'+currentIndex).hide();
																}
														
																/*if(!$('#replacement_'+currentIndex).val()&&($('#prodCheckShort_'+currentIndex).is(':checked')))
																{
																	$('#emptyReplacementError_'+currentIndex).show();
																	formValidationFlag = false;
																}
																else if($('#emptyReplacementError_'+currentIndex).is(':visible'))
																{
																	$('#emptyReplacementError_'+currentIndex).hide();
																}*/
															
															}
														}
													)
												}
													
											
											}
											
											
											if($('#collapse3').hasClass("in"))
											{
												
												//For product lines
												/*** Set flag if none of the product has been enetered. ***/
												for(i=0;i<length;i++)
												{
													if(!$('#prodCodeOver_'+currViewScreenName+'_'+i).attr("data"))
													{
														productEntered = false;
														
													}
													break;
												}
												
												if(!productEntered)
												{
													
													$('.productCodeValidationError').show();
													$('.emptyQtyReceivedOverError').hide();
													$('.emptyQtyErrorOrderedOver').hide();
													$('.emptylotNumbersError').hide();
													$('.emptyinvoiveNumberOverError').hide();
													formValidationFlag=false;
												
												}
												else
												{
													
													if($('.productCodeValidationError').is(':visible'))
													{
														$('.productCodeValidationError').hide();
													}
													
													$('.prodCodeOver').each
													(
														function()
														{
															if($(this).attr("data"))
															{
																
																var currentIndex = $(this).attr("index");
																
																if(!$('#prodQtyReceivedOver_'+currViewScreenName+'_'+currentIndex).val()&&($('#prodCheckOver_'+currViewScreenName+'_'+currentIndex).is(':checked')))
																{
																	$('#emptyQtyReceivedOverError_'+currViewScreenName+'_'+currentIndex).show();
																	formValidationFlag = false;
																}
																else if($('#emptyQtyReceivedOverError_'+currViewScreenName+'_'+currentIndex).is(':visible'))
																{
																	$('#emptyQtyReceivedOverError_'+currViewScreenName+'_'+currentIndex).hide();
																}
															
																if(!$('#lotNumbers_'+currViewScreenName+'_'+currentIndex).val()&&($('#prodCheckOver_'+currViewScreenName+'_'+currentIndex).is(':checked')))
																{
																	$('#emptylotNumbersError_'+currViewScreenName+'_'+currentIndex).show();
																	formValidationFlag = false;
																}
																else if($('#emptylotNumbersError_'+currViewScreenName+'_'+currentIndex).is(':visible'))
																{
																	$('#emptylotNumbersError_'+currViewScreenName+'_'+currentIndex).hide();
																}
																
																if($('#productShipmentYesDL').is(":checked") && !$('#poNumber').val())
																{
																	$('.poNumberValidationError').show();
																	inquiryFormValidation = false;
																}
																else if($('.poNumberValidationError').is(':visible'))
																{
																	$('.poNumberValidationError').hide();
																}
																
															}
														}
													)
												}
													
											
											}
											
												
												
											
											if(formValidationFlag)
											{
													
													for(var i = 0 ; i < length ; i++){
														var currentIndex = $("#prodCode_"+currViewScreenName+'_'+i).attr("index");

														if($("#prodCode_"+currViewScreenName+'_'+currentIndex).attr("data") != null && $("#prodCode_"+currViewScreenName+'_'+currentIndex).attr("data") )
														{
															var prodChk=$('#prodCheck_'+currViewScreenName+'_'+currentIndex).is(':checked');
															if(prodChk)
																{
															productCode = $("#prodCode_"+currViewScreenName+'_'+currentIndex).attr("data");
															prodCodes.push(productCode);
															qty = $('#prodQty_'+currViewScreenName+'_'+currentIndex).attr("data");
															dispPrice = $('#disputedPrice_'+currViewScreenName+'_'+currentIndex).attr("data");
															correctPrice_ = $('#correctPrice_'+currViewScreenName+'_'+currentIndex).val();
															contractNumber= $('#contactNumber_'+currViewScreenName+'_'+currentIndex).attr("data");
															invoiceNumber= $('#invoiceNumber_'+currViewScreenName+'_'+currentIndex).attr("data");
															$('<input>').attr({
															    type: 'hidden',
															    id: 'unorderedPricingProductsInfo',
															    name: 'unorderedPricingProductsInfo',
															    value: productCode + ":" + qty + "~" + dispPrice + "~" + correctPrice_ + "~" + contractNumber + "~" + invoiceNumber
															}).appendTo(disputeItemInquiryForm);
															formSubmitFlag=true;
															//disputeItemInquiryForm.submit();
														}
														}
													}
													for(var i = 0 ; i < length ; i++){
														var currentIndex = $("#prodCode_"+currViewScreenName+'_'+i).attr("index");
														if($("#prodCodeShort_"+currViewScreenName+'_'+currentIndex).attr("data") != null && $("#prodCodeShort_"+currViewScreenName+'_'+currentIndex).attr("data") )
														{
															var prodChkShort=$('#prodCheckShort_'+currViewScreenName+'_'+currentIndex).is(':checked');
															if(prodChkShort)
																{
															productCodeShort = $("#prodCodeShort_"+currViewScreenName+'_'+currentIndex).attr("data");
															prodCodes.push(productCodeShort);
															qtyReceived = $('#prodQtyReceived_'+currViewScreenName+'_'+currentIndex).val();
															qtyOrdered = $('#prodQtyOrdered_'+currViewScreenName+'_'+currentIndex).attr("data");
															replacement = $('#replacement_'+currViewScreenName+'_'+currentIndex).val();
															invoiceNumber= $('#invoiceNumberShort_'+currViewScreenName+'_'+currentIndex).attr("data");
															
															$('<input>').attr({
															    type: 'hidden',
															    id: 'unorderedShortShippedProductsInfo',
															    name: 'unorderedShortShippedProductsInfo',
															    value: productCodeShort + ":" + qtyReceived + "~" + qtyOrdered + "~" + replacement + "~" + invoiceNumber 
															}).appendTo(disputeItemInquiryForm);
															formSubmitFlag=true;
															//disputeItemInquiryForm.submit();
														}
														}
													}
													for(var i = 0 ; i < length ; i++){
														var currentIndex = $("#prodCode_"+currViewScreenName+'_'+i).attr("index");
														if($("#prodCodeOver_"+currViewScreenName+'_'+currentIndex).attr("data") != null && $("#prodCodeOver_"+currViewScreenName+'_'+currentIndex).attr("data") )
														{
															
															var prodChkOver=$('#prodCheckOver_'+currViewScreenName+'_'+currentIndex).is(':checked');
															if(prodChkOver)
															{
															productCodeOver = $("#prodCodeOver_"+currViewScreenName+'_'+currentIndex).attr("data");
															prodCodes.push(productCodeOver);
															qtyReceivedOver = $('#prodQtyReceivedOver_'+currViewScreenName+'_'+currentIndex).val();
															qtyOrderedOver = $('#prodQtyOrderedOver_'+currViewScreenName+'_'+currentIndex).attr("data");
															lotnumbers = $('#lotNumbers_'+currViewScreenName+'_'+currentIndex).val();
															invoiceNumberOver= $('#invoiceNumberOver_'+currViewScreenName+'_'+currentIndex).attr("data");
															
															$('<input>').attr({
															    type: 'hidden',
															    id: 'unorderedOverShippedProductsInfo',
															    name: 'unorderedOverShippedProductsInfo',
															    value: productCodeOver + ":" + qtyReceivedOver + "~" + qtyOrderedOver + "~" + lotnumbers + "~" + invoiceNumberOver 
															}).appendTo(disputeItemInquiryForm);
															formSubmitFlag=true;
															//disputeItemInquiryForm.submit();
														}
														}
														
														

													}
													
											}
											
										
											           if(formSubmitFlag==true && disputeline==true)
											        	{
											        	   disputeline=false;
											        	  
											        	   if(prodCodes!=null)
											        		   {
											        		  
											        	   var fieldsString = prodCodes.join(" \n ");
											        	  
											        	   
											        	   var result =  false;
											        	  
												        	 $('#dispute-success-info').text(PRODUCT_SUBMITTED +fieldsString);
												        	 $('#dispute-error-panel').hide();
												        	   $('#dispute-info-panel').show();
												        	 $('#dispute-success-popup').modal('show');
												        	 $('#dispute-ok').show();
												        	 $('#dispute-cancel').show();
												        	 $('#dispute-ok').click(function(){
												        		 disputeItemInquiryForm.submit();
												        	 });
												        	 
												        	 $('#dispute-cancel').click(function(){
												        		 $('#dispute-success-info').text(PRODUCT_RESUBMITTED);
												        		 $('#dispute-ok').hide();
												        		 $('#dispute-cancel').hide();
												        	 });
												        	 
											        		   }
											        	   else
											        	   {
											        	   alert(PRODUCT_NONE);
											        	   }
											        	}
											           else if(formValidationFlag ==true && formSubmitFlag==false)
											        	   {
											        	  
											        	   $('#dispute-error').text(PRODUCT_INQUIRY);
											        	   $('#dispute-info-panel').hide();
											        	   $('#dispute-error-panel').show();
											        	   $('#dispute-success-popup').modal('show');
											        	   
											        	   $('#dispute-ok').hide();
											        	   $('#dispute-cancel').hide();
											        	   }
											         
													});
									

},
	
	
	disputeItemInquiryPopup: function()
	{

			/*** If Replacement required is checked out, display replacement PO Number layer.  ***/
			$('#replacementRequiredYes').click(function()
			{
				$('#replacementRequiredNo').removeAttr("checked")
				$('#replacementRequiredYes').attr("checked","checked")
				if($('#replacementRequiredYes').is(":checked"))
					{
						$('.replacedPo').show();
					}
				
			});
			
			/*** If Replacement required is checked out, display replacement PO Number layer.  ***/
			$('#replacementRequiredNo').click(function()
			{
				$('#replacementRequiredYes').removeAttr("checked")
				$('#replacementRequiredNo').attr("checked","checked")
				if($('#replacementRequiredNo').is(":checked"))
					{
						$('.replacedPo').hide();
					}
				
			});
			
			
			/*** Enable submit button until either of the dispute check box is selected ***/
			$('#collapse11, #collapse12, #collapse13').click(function()
					{
						$('.submitDisputeItem').removeAttr("disabled");
					});
			
			
			/**
			 * Allows ONLY backspace, tab, delete, arrows, numbers and keypad numbers in the Quantity text boxes.
			 */
			$('#shortShippedOrderedQuantity, #shortShippedReceivedQuantity, #overShippedOrderedQuantity, #overShippedReceivedQuantity,#expectedPrice')
				.on('keyup keypress', function()
					{
				        var value = $(this).val();
				        pttern = /^\d{1,6}$/g;
				        if(value.match(pttern))
				        {
							return true;
				        }
						else
						{
							$(this).val(value.substring(0,value.length-1));
						}
				
				    });
			
			$('#productShipmentYesDL').click(function()
					{
							$('#poNumber').prop('readonly',false);
							$('#newPONumber').show();
							$('#productShipmentNo').removeAttr("checked")
							$('#productShipmentYes').attr("checked","checked")
					});
							
			$('#productShipmentNoDL').click(function()
					{
							$('#poNumber').prop('readonly',true);
							$('#productShipmentNo').attr("checked","checked")
							$('#productShipmentYes').removeAttr("checked")
							$('#newPONumber').hide(); 
			
					});
			
			$("#collapse11").find(".inputTxt").prop("disabled",false);
			$("#collapse12").find(".inputTxt").prop("disabled",false);
			$("#collapse13").find(".inputTxt").prop("disabled",false);
				
			/*** On click of submit dispute Item, validate all required fields and if no vlaidation errors submit the corresponding form ***/
			$(document).on('click','.submitDisputeItem',function()
					{
						var inquiryFormValidation = true;
						
						if($('#collapse11').hasClass("in"))
						{
							
							if(!$('#totalDisputed').val())
							{
								$('.disputedAmountValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.disputedAmountValidationError').is(':visible'))
							{
								$('.disputedAmountValidationError').hide();
							}
								
							
							if(!$('#expectedPrice').val())
							{
								$('.expectedAmtValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.expectedAmtValidationError').is(':visible'))
							{
								$('.expectedAmtValidationError').hide();
							}
						}else{
							$("#collapse11").find(".inputTxt").prop("disabled",true);
						}
						
						if($('#collapse12').hasClass("in"))
						{
							if(!$('#shortShippedProductCode').val())
							{
								$('.shortShippedProductCodeValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.shortShippedProductCodeValidationError').is(':visible'))
							{
								$('.shortShippedProductCodeValidationError').hide();
							}
								
							
							if(!$('#shortShippedOrderedQuantity').val())
							{
								$('.shortShippedOrdQtyValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.shortShippedOrdQtyValidationError').is(':visible'))
							{
								$('.shortShippedOrdQtyValidationError').hide();
							}
							
							if(!$('#shortShippedReceivedQuantity').val())
							{
								$('.shortShippedRecQtyValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.shortShippedRecQtyValidationError').is(':visible'))
							{
								$('.shortShippedRecQtyValidationError').hide();
							}
						}else{
							$("#collapse12").find(".inputTxt").prop("disabled",true);
						}
						
						
						if($('#collapse13').hasClass("in"))
						{
							if(!$('#overShippedProductCode').val())
							{
								$('.overShippedProdCodeValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.overShippedProdCodeValidationError').is(':visible'))
							{
								$('.overShippedProdCodeValidationError').hide();
							}
								
							
							if(!$('#overShippedOrderedQuantity').val())
							{
								$('.overShippedOrdQtyValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.overShippedOrdQtyValidationError').is(':visible'))
							{
								$('.overShippedOrdQtyValidationError').hide();
							}
							
							if(!$('#overShippedReceivedQuantity').val())
							{
								$('.overShippedRecQtyValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.overShippedRecQtyValidationError').is(':visible'))
							{
								$('.overShippedRecQtyValidationError').hide();
							}
							
							
							if($('#productShipmentYesDL').is(":checked") && !$('#poNumber').val())
							{
								$('.poNumberValidationError').show();
								inquiryFormValidation = false;
							}
							else if($('.poNumberValidationError').is(':visible'))
							{
								$('.poNumberValidationError').hide();
							}
							
							
						}else{
							$("#collapse13").find(".inputTxt").prop("disabled",true);
						}
						
						if(!$('#collapse11').hasClass("in") && !$('#collapse12').hasClass("in") &&
								!$('#collapse13').hasClass("in"))
						{
							inquiryFormValidation = false;
						}
						
						if(inquiryFormValidation)
						{
							$(".disputeModalUniversalError").attr("style","visibility:hidden;");
							$('#disputeItemInquiryForm').ajaxSubmit(
								{
									success : function(data)
									{
										 if(data = 'true')
										{
											$('#dispute-success-info').text(INQUIRY_SUBMITTED);
											$('#dispute-success-popup').modal('show');
											
											
										}
										else
										{
											$('#dispute-success-info').text(INQUIRY_NOTSUBMITTED);
											$('#dispute-success-popup').modal('show');
											
										}
									}
								});
						} else {
							$(".disputeModalUniversalError").attr("style","visibility:visible;");
						}
					});
	    
		
		
	},
	
	
	
	surgeonInformation: function()
	{
		$('#surgeonInformation').click(function()
			{
				var orderCode = $(this).attr("orderCode");
				var ajaxurl = ACC.config.contextPath+"/order-history/order/surgeonInformation";
				$.ajax({
				  type: "GET",
				  url: ajaxurl,
				  data: {orderCode: orderCode},
				  success: function(data) 
				  {
					  $.colorbox({
							html: data,
							height: '460px',
							width: 'auto',
							escKey:false,
							overlayClose: false,
							onComplete: function()
							{
								
							},
							onClosed: function () {
							}
						});
				    },
				    dataType: 'html',
					async: true
				});
			});
	},
	
	confirmOrderHistoryProduct :function(nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');
		$(document).off('click', '#cancel-btn-addtocart').on('click', '#cancel-btn-addtocart',function(e) {
			ACC.orderHistory.callbackOrderHistoryProduct(false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
		
		$(document).off('click', '#accept-btn-addtocart').on('click', '#accept-btn-addtocart',function(e) {
			ACC.orderHistory.callbackOrderHistoryProduct(true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	/**
	 *  @param value
	 *	if value = true : submitPlaceorderfile()
	 *	else value = false : removeNonContractProductPlaceOrderFile()
	 */
	callbackOrderHistoryProduct : function (value,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	 ACC.orderHistory.finalSubmitAddToCart();
	    	loadingCircleShow("hide");
	    } else {
	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.orderHistory.removeNonContractProductOrderHistoryProduct();
	    	}
	    	else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product 
	    		console.log("nothing to perform becasue dont want to add this non contract product in the contract product");
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	/**
	 *  Remove product which is already non contract
	 */
	removeNonContractProductOrderHistoryProduct : function(){
		jQuery.ajax({  
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {	
				 ACC.orderHistory.finalSubmitAddToCart();
				loadingCircleShow("hide");
			} 
		});
	},
	setDefault : function(){
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
	},
	orderHistSelectAll : function(){
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
			    }).append(jQuery('<input>', { // Account GLN input created 
			    	'name' : 'CSRFToken',
			    	 'value' : ACC.config.CSRFToken,
			    	 'type' : 'hidden' 
			    }));
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
				checkBoxSort();
			}
		});
	},
	orderHistoryPageSelectAll : function(){
        $(".orderHistorySelectAll").change(function(){
               loadingCircleShow("show");
               if($(this).prop("checked")) {
                     loadingCircleShow("show");
                     setTimeout(function(){
          loadingCircleShow("hide");},500);
          //$('.dataTables_wrapper').hide();
          ACC.orderHistory.addAccountsToForm();
          ACC.orderHistory.setDefault();
      } else {
			    	  ACC.orderHistory.removeAccountsFromForm();
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
 },
 addAccountsToForm : function(){
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
                  sessionStorage.setItem('accounts_DefaultValue', $("#accounts").val());
                  sessionStorage.setItem('totAccountsSelected_DefaultValue', $("#totAccountsSelected").val());
                  $("#accounts").val(accountsArray);
                  $("#totAccountsSelected").val(accountsArray.length);
                  $("#totNoOfAccountsSelected").html(accountsArray.length);
            }
     });
},
removeAccountsFromForm : function(){
     $("#isSelectAllAccount").val(false);
     $("#accounts").val(sessionStorage.getItem('accounts_DefaultValue'));
     $("#totAccountsSelected").val(sessionStorage.getItem('totAccountsSelected_DefaultValue'));
},

	sendSelectedAccounts : function (accSelLength){
		if (accSelLength === 0){
			$('#select-accnt-error-order-history').css('display','block');
			return false;
		}
		var newForm = jQuery('<form>', {
			'action': ACC.config.contextPath +'/order-history',
	        'id' : 'ordHistoryForm',
	        'method' : 'post'
	    }).append(jQuery('<input>', { // Account GLN input created 
	    	'name' : 'CSRFToken',
	    	 'value' : ACC.config.CSRFToken,
	    	 'type' : 'hidden' 
	    }));
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
};
	
function validateDisputeItemInquiryForm()
{
	if($('#pricingDisputeChk').is(":checked"))
	{
		if(!$('#totalDisputed').val())
			{
				alert("empty");
				return false;
			}
	}
	
	return false;
}


function getUniqueElements(inputArray)
{
	var outputArray = [];
	for (var i = 0; i < inputArray.length; i++)
	{
		if ((jQuery.inArray(inputArray[i], outputArray)) == -1)
		{
			outputArray.push(inputArray[i]);
		}
	}
	return outputArray;
}




/*function showSurgeonAjaxCall(e, orderNumber,surgeonName){
	var dataObj = new Object();
	dataObj.loadMoreCounter = $("#loadMoreCounter").val();
	dataObj.searchPattern = $("#searchSurgeon").val();
	$("#surgeonIdInput").val(surgeonName);
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/surgeonData',
		data: dataObj,
		type: 'POST',
		content: 'text/html',
		success : function(data) {
			
			$('#surgeonPopupHolder').html(data);
			$('#searchSurgeon').val('');
			$('#selectSurgeonPopup').modal('hide');
			$(".modal-backdrop").hide();
			$('#selectSurgeonPopup').modal('show');
			$("#surgeonIdInput").val(surgeonName);
			if(searchSelectedFlag == true){
				$("#searchSurgeon").prop("disabled",false);
				$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
					
					"cursor":"pointer"
					
				});
			}
			selectSurgeonPopup(); 
		}
	});	
}*/


/*function selectSurgeonPopup(orderNumber)
{
	$('.surgeonDetail').click(function()
			{
				inputText = $('input[name=surgeonName]');
				inputText.val($(this).attr("id"));
				surgeonId = $(this).attr("surgeonId");
				inputText.attr("surgeonId",surgeonId);
			}),
			
			$("#submitSurgeonUpdate").click(function()
					{
						if($.trim($("#surgeonIdInput").val())==""){
							$("#globalErrorSuergeon").show();
							$.colorbox.resize();
							return false;
						} else {
							$("#globalErrorSuergeon").hide();
							$.colorbox.resize();
						}
						var updateSurgeonRequestUrl = ACC.config.contextPath+"/order-history/updateSurgeonRequest";
						var surgeonInput = $("#surgeonIdInput");
						var selectedSurgeonId =  surgeonInput.attr("surgeonId");
						
						$.ajax({
							  type: "POST",
							  url: updateSurgeonRequestUrl,
							  data: { orderNumber: orderNumber, selectedSurgeonId : selectedSurgeonId} ,
							  success: function(data) 
							  	{
								  if(data != null && data != '')
								  {
									  var completeMsg = "Surgeon has been updated successfully.";										  
									  updateSurgeionLink.attr("surgeonName",data)
									  alert(completeMsg);									  
									  $.colorbox.close();
								  }
								  else
								  {	
									  var errorMsg = "Your update could not be completed at this time. Please try again later or contact customer service for assistance.";
									  surgeonUpdateFailSpan = $('#surgeonUpdateFail');
									  surgeonUpdateFailSpan.find("label").html(errorMsg);
									  surgeonUpdateFailSpan.show();
									  $.colorbox.resize();
								  }
							    }
							});
					});
}*/


function getDoc(frame) 
{
	var doc = null;
	// IE8 cascading access check
	try 
	{
		if (frame.contentWindow) 
		{
			doc = frame.contentWindow.document;
		}
	} catch(err) 
	{
		//
	}
	
	if (doc) 
	{ 
		// successful getting content
		return doc;
	}
	
	try 
	{ 
		// simply checking may throw in ie8 under ssl or mismatched protocol
		doc = frame.contentDocument ? frame.contentDocument : frame.document;
	} 
	catch(err) 
	{
		// last attempt
		doc = frame.document;
	}
	return doc;
}

function updatePONumber()
{
	$(".updatePoNumber").click(function(e)
			{
				var ajaxurl = ACC.config.contextPath+"/order-history/updatePurchaseOrder";
				orderNumber = $(this).attr("orderNumber");
				
				$.ajax({
				  type: "GET",
				  url: ajaxurl,
				  success: function(data) {
					/*  $.colorbox({
						html: data,
						height: 'auto',
						width: '492px',
						overlayClose: false,
						escKey:false,
						onComplete: function()
						{
							ACC.orderHistory.submitPoNumberUpdate(orderNumber);
						},
						onClosed: function () {
						}
					});*/
					  $('#updatePoNumberPopup').modal('show');
					  ACC.orderHistory.submitPoNumberUpdate(orderNumber);
				  },
				  dataType: 'html'
				});
			});
}

/*function updateSurgeon()
{
	$(".updateSurgeon").click(function(e)
		{
			orderNumber = $(this).attr("orderNumber");
			surgeonName = $(this).attr("surgeonName");
			updateSurgeionLink =$(this); 
			showSurgeonAjaxCall(e, orderNumber,surgeonName);
		});
}*/
	var currViewScreenName;
$(document).ready(function() {
	
	currViewScreenName = $("#currViewScreenName").val();
	
	$('.dispute-popup-link').click(function(){
		$('#orderCode').val('');
		$('#orderCodeInvalidMsg').hide();
	});
	
	$("#closedispute").click(function(){
		window.location.reload(true);
	});
	
	/*$('.inputTxt').on('change keyup paste',function(){
		if($(this).val() == "" || $(this).val().length==0){
			$(this).parent().find('.error').show();
			$(this).parent().find('.registerError').show();
			
		}
		else{
			$(this).parent().find('.error').hide();
			$(this).parent().find('.registerError').hide();
			
		}
		
	});
	
	$('.productcode').on('change keyup paste',function(){
		if($(this).val() == "" || $(this).val().length==0){
			$('#productcode-error').show();
			
		}
		else{
			$('#productcode-error').hide();
			
		}

	});
	$('.productcodeLast').on('change keyup paste',function(){
		if($(this).val() == "" || $(this).val().length==0){
			$('#emptyQtyError').show();
			$(this).parent().next().show();
		}
		else{
			$('#emptyQtyError').hide();
			$(this).parent().next().hide();
		}

	});
	*/
	
	
	
	$('#disputeOrderPopup').click(function()
			{
				var ordercode = $('#orderCode').val();
				var ajaxurl = ACC.config.contextPath+"/order-history/orderValidation?orderCode="+ordercode;
				
				$.ajax({
					  type: "GET",
					  url: ajaxurl,
					  success: function(data) {
						  if(data){
							  window.location.href=ACC.config.contextPath+"/order-history/order/disputeOrder?orderCode="+ordercode;
						  }else{
							  $("#orderCodeInvalidMsg").show();
							  return false;
						  }
					  },error: function(){
						  $("#orderCodeInvalidMsg").show();
						  return false;
					  }
				
			});
			});		
	
	
	
	$('#select-accnt-error').hide();

	
	/**
	 * 
	 */
	updatePONumber();
//	updateSurgeon();
	
	disputeOrderSubmitFlag = $("#disputeFlag").val();
	if(disputeOrderSubmitFlag != null && disputeOrderSubmitFlag  != '')
	{
		if(disputeOrderSubmitFlag = 'true')
		{
			$('#dispute-success-info').text(INQUIRY_SUBMITTED);
			$('#dispute-success-popup').modal('show');
			
		}
		else
		{
			$('#dispute-success-info').text(INQUIRY_NOTSUBMITTED);
			$('#dispute-success-popup').modal('show');
		}
	}
	
	ACC.orderHistory.bindAll();
	$('#rgaId').click(function(e) {
		$('#errorMessage').hide();
	});
	$('#cmodId').click(function(e) {
		$('#errorMessage').hide();
	});
	
	
	
});


$('#searchby').on('change',function(e){
    txtval = '';
    $('#searchByInput').val('');
    
});
$('input.ordHisPlaceholder:text').each(function(){
	 txtval = $(this).val();
	 $(this).focus(function(){
	     if($(this).val() == txtval){
	         $(this).val('')
	                  $(this).css('color' , '#646464');
	     }
	 });
	 $(this).blur(function(){
	     if($(this).val() == ""){
	         $(this).val(txtval);
	                  $(this).css('color' , '#ccc');
	     }
	 });
});
$('.showOrderDetail').click(function(e){
	var newForm = jQuery('<form>', {
        'action': $(this).attr("data"),
        'id' : 'orderDetailsRedirect',
        'method' : 'post'
    }).append(jQuery('<input>', { // Account GLN input created 
    	'name' : 'CSRFToken',
   	 'value' : ACC.config.CSRFToken,
   	 'type' : 'hidden' 
   }));
	$(".orderHistoryPage").append(newForm);
	$("#orderDetailsRedirect").submit();
});

$('.invoiceDetailPage').click(function(e){
	
	var newForm = jQuery('<form>', {
        'action': $(this).attr("data"),
        'id' : 'invoiceDetailsPost',
        'method' : 'post'
    }).append(jQuery('<input>', { // Account GLN input created 
    	'name' : 'CSRFToken',
   	 'value' : ACC.config.CSRFToken,
   	 'type' : 'hidden' 
   }));
	$(".orderDetailsPage").append(newForm);
	$("#invoiceDetailsPost").submit();
});
