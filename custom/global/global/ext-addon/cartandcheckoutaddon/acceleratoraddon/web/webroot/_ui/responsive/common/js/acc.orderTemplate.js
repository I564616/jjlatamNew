ACC.orderTemplate = {

	bindAll: function()
	{
		if($("span.templateNumber").text() !== null){
		$("#hddnTemplateNumber").val($("span.templateNumber").text());}
		// ACC.orderTemplate.showMoreTemplate();
		ACC.orderTemplate.bindDownloadPDFLink($('.downloadTemplatePDF'));
		ACC.orderTemplate.bindDownloadExcelLink($('.downloadTemplateExcel'));
		ACC.orderTemplate.bindShowByGroupOf($('#showinGroups'));
		ACC.orderTemplate.bindDownloadDetailPDFLink($('.downloadbuttons .pdf'));
		ACC.orderTemplate.bindDownloadDetailExcelLink($('.downloadbuttons .excel'));
		ACC.orderTemplate.showTemplateDetailGroups();
		//ACC.orderTemplate.showMoreTemplateDetails();
		ACC.orderTemplate.deleteTemplatePopup();
		ACC.orderTemplate.saveTemplateDetails();
		ACC.orderTemplate.enableSaveButton();
		ACC.orderTemplate.submitAddToCartRequest();
		
	},
	
	/**
	 * Submits Add to Cart request from Order Template Detail page ONLY when all the products are Saleable.
	 */
	submitAddToCartRequest: function(e)
	{
		var invalidProducts = [];
		var validProductsAndQty = [];		
		
		$(".templateDetailAddToCart").click(function() 
		{
			invalidProducts = [];
			validProductsAndQty = [];
			var allProductsSaleable = true;
			var selectedProdCodes = [];
			var combinedProdCodes =[];
			var validProdCodes =[];
			var productAdded = false;
			var quantityCheck = 0;
			var invalidProduct= false;
			
					$(".saleableInd").each
					(
							function()
							{
								if ($(this).val() == 'false')
								{
									invalidProducts.push($(this).attr("id")+":"+$(this).attr("qty"));
									combinedProdCodes.push($(this).attr("id")+":"+$(this).attr("qty"));
								}	
								else{
									if(QTY_CHECK!=""){
										if(parseInt($(this).attr("qty")) > Number(QTY_CHECK))
										{
											quantityCheck++;
										
										}
									}									
									validProductsAndQty.push($(this).attr("id")+":"+$(this).attr("qty"));
									selectedProdCodes.push($(this).attr("id"));
									validProdCodes.push($(this).attr("id")+":"+$(this).attr("qty"));
								}
								
							}
							
					);
					if(parseInt(quantityCheck) > 0)
						{
						  loadingCircleShow("hide");
						$('#newQuantitypopup').modal('show');
						
					$(document).off('click', '#back-btn');
					$(document).on('click', '#back-btn',function(e) {
						$('#newQuantitypopup').modal('hide');
						loadingCircleShow("hide");					
						return false;	
					});
					
					$(document).off('click', '#continue-btn');
					$(document).on('click', '#continue-btn',function(e) {
						   loadingCircleShow("show");
						$('#newQuantitypopup').modal('hide');
						 ACC.orderTemplate.addProductToCart(combinedProdCodes,validProdCodes,selectedProdCodes,validProductsAndQty,invalidProducts,productAdded);
						
					});
						}
					else
					{
						 ACC.orderTemplate.addProductToCart(combinedProdCodes,validProdCodes,selectedProdCodes,validProductsAndQty,invalidProducts,productAdded)
			}
					

				});	
		
	},
	addProductToCart : function(combinedProdCodes,validProdCodes,selectedProdCodes,validProductsAndQty,invalidProducts,productAdded){
		var invalidProductString = combinedProdCodes.join(',');
		var validProdString = validProdCodes.join(','); 
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
 		
 		//AAOL-6378
			var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(invalidProducts);
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
				console.log("useer entry prodIds " +invalidProducts );
				ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,invalidProducts,null);//AAOL-6377 here there is no active products, thats why we are passing null in place all selected products.
			}
		
 		//if(numberOfProdSelected > 0)
 		else{
 			
 			jQuery.ajax({   
				url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
				async: false,
				success : function(data) {
					var nonContractProductInCart = data.nonContractProductInCart;
					var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
					if (nonContractProductInCart === nonContractProductInSelectedList) {
						ACC.orderTemplate.ValidateNonContractProductAddToCart(validProdString);
					} else {
						ACC.orderTemplate.confirmOrderTemplate(selectedProdCodes,teamplateAddToCartForm,nonContractProductInCart,nonContractProductInSelectedList);
					}
				}
			});
 			
 			
		}
 		if(invalidProducts.length != 0){
 			ACC.orderTemplate.ValidateNonContractProductAddToCart(invalidProductString);
 			if (numberOfProdSelected>0){
				productAdded =true;
			}
			if(productAdded){
				loadingCircleShow("hide");
				setTimeout(function(){
					window.location.href = ACC.config.contextPath +'/templates/templateDetail/'+$("#hddnTemplateNumber").val();
				}, 7500);
				 $("#errorMultiCartTmplt").show();
				
			}
 		}else{
 			location.reload();
 		}
	},
enableSaveButton:function()
	{
		$("#templateName").on("focus", function(){
			$(this).data("originalValue", this.value);
		}).on("keyup", function(){
			if($(this).data("originalValue")!=this.value) {
				$(".saveTemplate").attr("disabled", false);
				$(".saveTemplate").addClass("primarybtn");
				$(".saveTemplate").removeClass("tertiarybtn");
				$(".saveTemplate").removeClass("linkDisable");
			}else{
				$(".saveTemplate").attr("disabled", true);
				$(".saveTemplate").removeClass("primarybtn");
				$(".saveTemplate").addClass("tertiarybtn");
			}
		});
		$("#templateName").on("change",function(){
			$(".saveTemplate").attr("disabled", false);
			$(".saveTemplate").addClass("primarybtn");
			$(".saveTemplate").removeClass("tertiarybtn");
			$(".saveTemplate").removeClass("linkDisable");
			
		})
	},
	
	saveTemplateDetails:function()
	{
		$(".saveTemplate").change(function() {
			var obj = new Object();
			obj.templateCode=  $("span.templateNumber").text();
			$("#templateEditForm").ajaxSubmit({url: ACC.config.contextPath + "/templates/templateDetail/editTemplate/"+$("span.templateNumber").text(),type: 'post',data:obj });
		});
	},
	// naga
	deleteTemplatePopup:function()
	{
		$(".deleteTemplate").on( "click", function(){
		
			var obj = new Object();
			obj.templateCode=  $("span.templateNumber").text();
			jQuery.ajax({
				url: ACC.config.contextPath + '/templates/templateDetail/deleteTemplate/'+$("span.templateNumber").text(),
				type: 'GET',
				//data: obj,
				success: function (data) {
					
					
					$("#confirmationPopup").modal('show');
					
					$("#Confirmationext").html(data);
					$(".deleteTempYes").on( "click", function(){
								$("#templateEditForm").submit();
							})
							$(".deleteTempNo").on( "click", function(){
								window.location=ACC.config.contextPath + "/templates/templateDetail/"+$("span.templateNumber").text();
							})
				
				}
			});
		});
	},
	
	showTemplateDetailGroups: function()
	{
		$("#groupOfItem").on( "change", function(){
			var detailForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/"+$("span.templateNumber").text(),
				'id' : 'detailForm',
				'method' : 'POST'
			}).append(jQuery('<input>', {
				'name' : 'showinGroups',
				'value' : $(this).val(),
				'type' : 'hidden'
			})).append(jQuery('<input>', { // Account GLN input created 
		    	'name' : 'CSRFToken',
		    	 'value' : ACC.config.CSRFToken,
				'type' : 'hidden'
			}));
			$(".sectionBlock").append(detailForm);
			/** Submitting the dummy form **/
			$("#detailForm").submit();
		
			/*var obj = new Object();
			obj.showinGroups = $(this).val();
			//$.get(ACC.config.contextPath + "/templates/templateDetail/"+$("span.templateNumber").text(),obj);
			window.location= ACC.config.contextPath + "/templates/templateDetail/"+$("span.templateNumber").text()+"?showinGroups="+$(this).val();*/
		});		
	},
	
	showMoreTemplateDetails: function()
	{
		if($("#totalDetailsShown").val()!='0' && $("#totalDetailsShown").val()!='')
		{
			$('.showMoreDetailsDiv').show();
		}
		else
		{
			$('.showMoreDetailsDiv').hide();
		}
		
		$(".showMoreTempDetail").on( "click", function(){
			var pageCount = $('#currentPageCount').val();
			var pageCountInt = parseInt(pageCount);
			$('#currentPageCount').val(pageCountInt+1);
			var showMoreDetailForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/"+$("span.templateNumber").text(),
				'id' : 'showMoreDetailForm',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'showinGroups',
				'value' : $("#groupOfItem").val(),
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'showMode',
				'value' : 'Page',
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'page',
				'value' : $("#currentPageCount").val(),
				'type' : 'hidden'
			})).append(jQuery('<input>', { // Account GLN input created 
		    	'name' : 'CSRFToken',
		    	 'value' : ACC.config.CSRFToken,
		    	 'type' : 'hidden' 
			}));
			$(".sectionBlock").append(showMoreDetailForm);
			/** Submitting the dummy form **/
			$("#showMoreDetailForm").submit();
		});		
	},
		
	showMoreTemplate: function()
	{
		if($(".totalTemplatesShown").val()!='0' && $(".totalTemplatesShown").val()!='')
		{
			$('.showMoreTempDiv').show();
			
		}
		else
		{
			$('.showMoreTempDiv').hide();
		}
		$(".showMoreTemplate").click(function(e){
			$(".totalTemplatesShown").val(parseInt($(".totalTemplatesShown").val())+parseInt($('#showinGroups').val()));
			$("#templates").attr("action", ACC.config.contextPath +'/templates?showinGroups='+$(".totalTemplatesShown").val());
			$("#templates").submit();
		});
	},
	
	bindDownloadDetailPDFLink : function(link) {
		link.click(function() {
			var obj = new Object();
			//obj.page =
			obj.show = "All";
			obj.downloadType="PDF";
			var detailDownloadForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/download/"+$("span.templateNumber").text(),
				'id' : 'detailFormPDFDownload',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'show',
				'value' : obj.show,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'downloadType',
				'value' : obj.downloadType,
				'type' : 'hidden'
			})).append(jQuery('<input>', { // Account GLN input created 
		    	'name' : 'CSRFToken',
		    	 'value' : ACC.config.CSRFToken,
		    	 'type' : 'hidden' 
			}));
			$(".sectionBlock").append(detailDownloadForm);
			/** Submitting the dummy form **/
			detailDownloadForm.submit();
			
		});
	},
	bindDownloadDetailExcelLink : function(link) {
		link.click(function() {
			var obj = new Object();
			//obj.page =
			obj.show = "All";
			obj.downloadType="EXCEL";
			var detailDownloadForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/download/"+$("span.templateNumber").text(),
				'id' : 'detailFormExcelDownload',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'show',
				'value' : obj.show,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'downloadType',
				'value' : obj.downloadType,
				'type' : 'hidden'
			})).append(jQuery('<input>', { // Account GLN input created 
		    	'name' : 'CSRFToken',
		    	 'value' : ACC.config.CSRFToken,
				'type' : 'hidden'
			}));
			$(".sectionBlock").append(detailDownloadForm);
			/** Submitting the dummy form **/
			detailDownloadForm.submit();
		});
	},
	
	bindDownloadPDFLink : function(link) {
		link.click(function() {
			$("#templates").attr("action", ACC.config.contextPath +'/templates/downloadData?downloadType=PDF');
			$("#templates").submit();
			$("#templates").attr("action", ACC.config.contextPath + "/templates");
		});
	},
	bindDownloadExcelLink : function(link) {
		link.click(function() {
			$("#templates").attr("action", ACC.config.contextPath +'/templates/downloadData?downloadType=EXCEL');
			$("#templates").submit();
			$("#templates").attr("action", ACC.config.contextPath + "/templates");
		});
	},
	bindShowByGroupOf : function(link) {
		link.change(function() {
			$('#templates').submit();
		});
	},
	confirmOrderTemplate :function(selectedProdCodes,teamplateAddToCartForm,nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');
		$(document).off('click', '#cancel-btn-addtocart').on('click', '#cancel-btn-addtocart',function(e) {
			ACC.orderTemplate.callbackOrderTemplate(false,teamplateAddToCartForm,selectedProdCodes,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
		
		$(document).off('click', '#accept-btn-addtocart').on('click', '#accept-btn-addtocart',function(e) {
			ACC.orderTemplate.callbackOrderTemplate(true,teamplateAddToCartForm,selectedProdCodes,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	/**
	 *  @param value
	 *	if value = true : submitPlaceorderfile()
	 *	else value = false : removeNonContractProductPlaceOrderFile()
	 */
	callbackOrderTemplate : function (value,teamplateAddToCartForm,selectedProdCodes,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	 ACC.orderTemplate.templateDetailsNonContractProductAddToCart( teamplateAddToCartForm);
	    	loadingCircleShow("hide");
	    } else {
	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.orderTemplate.removeNonContractOrderTemplate(teamplateAddToCartForm);
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
	removeNonContractOrderTemplate : function(teamplateAddToCartForm){
		jQuery.ajax({  
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {	
				 ACC.orderTemplate.templateDetailsNonContractProductAddToCart(teamplateAddToCartForm);
				loadingCircleShow("hide");
			} 
		});
	},
ValidateNonContractProductAddToCart : function(prodIds){
	loadingCircleShow("show");
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
//		dataObj.source = "templateDetailAddToCartId";
		dataObj.prodIds = prodIds;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/cart/addTemplateToCart',
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
						if(invalidProduct){
							//location.reload();
							 loadingCircleShow("hide");
						}
						
						
						/*else{
							loadingCircleShow("hide");
							$("#errorMultiCart").hide();
							location.reload(true)
						}*/
					
					}
				
				});
				
		
	},
	templateDetailsNonContractProductAddToCart : function(teamplateAddToCartForm){
		$(".sectionBlock").append(teamplateAddToCartForm);
			teamplateAddToCartForm.ajaxSubmit({
			success : function(data)
			{
				var productAdded = false;
				$.each(data.cartModData,function(){
					$.each(this, function(products) {
						if(this.quantity!=""){
							productAdded = true;
						}
					});
				});
				if(productAdded){
					location.reload();
					 loadingCircleShow("hide");
				} else {
					var errorMessage = YOUR_SELECTION+' '+invalidProducts+' '+NA_PURCHASE;
					$('#addToCartErrorLayer').html('<p>' +errorMessage + '</p>');
					$('#addToCartErrorLayer').show("slow");
					$('#addToCartErrorLayer').delay(7000).fadeOut("slow");
					 loadingCircleShow("hide");
				}
				$("#productQuantityForm").reset();
			},
			error : function(data)
			{ loadingCircleShow("hide");
				ACC.product.addToCart(data);
			}
		});	
	}
	

	
	
};


$(document).ready(function() {
	ACC.orderTemplate.bindAll();
	if($('#shareWithAccountUsers').next().attr('type')=="hidden"){
		var shareHiddenField=$('#shareWithAccountUsers').next().html();
		$('#shareWithAccountUsers').next().remove();
		
	}
});
