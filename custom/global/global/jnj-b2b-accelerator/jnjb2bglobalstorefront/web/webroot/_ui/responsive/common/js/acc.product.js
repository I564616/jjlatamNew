ACC.product = {

	/**
	 * Binds all JQuery functions of the js on page load.
	 */
	bindAll: function() {
		ACC.product.getPrice();
		ACC.product.disableSelection();
		ACC.product.bindsearchSortForm();
		ACC.product.quantityValidation();
		ACC.product.addToCartRequest();
		ACC.product.productSelection();
		ACC.product.exportCatalog();
		ACC.product.selectAllProducts();
	},
	selectAllProducts : function()
	{
		$('#selectAllProductCheckboxCol #select-all').click(function(){

			var propChecked=$(this).prop('checked');	
			if(propChecked){
				$('.catalog-prod-chck').prop('checked',true);
			}
			else{
				$('.catalog-prod-chck').prop('checked',false);
			}
			
			$('.catalog-qty').each(function(){
				if($(this).val()<1 && propChecked){
					$(this).val(1);
				}
				if($(this).val()==1 && !propChecked){
					$(this).val(0);
				}
			});
		});
		
		var selectAllChecked=false;
		
		$(document).on('change','#ordersTable_length .selectpicker',function(){
			checkSelectAll();
		});
		
		$(document).on("click","#ordersTable_next",function(){ 
			checkSelectAll();	
		});
		$(document).on("click","#ordersTable_previous",function(){ 
			checkSelectAll();	
		});
		
		$(document).on('change','.catalog-qty',function(){

			if($(this).val()>0){
				$('.catalog-prod-chck:eq('+$('.catalog-qty').index(this)+')').prop('checked',true);
			}
			else{
				$('.catalog-prod-chck:eq('+$('.catalog-qty').index(this)+')').prop('checked',false);
			}
		});
		
		$(document).on('click','.catalog-prod-chck',function(){
			var propChecked=$(this).prop('checked');	
			if($('.catalog-qty:eq('+$('.catalog-prod-chck').index(this)+')').val()<1 && propChecked){
				$('.catalog-qty:eq('+$('.catalog-prod-chck').index(this)+')').val(1);
			}
			if($('.catalog-qty:eq('+$('.catalog-prod-chck').index(this)+')').val()==1 && !propChecked){
				$('.catalog-qty:eq('+$('.catalog-prod-chck').index(this)+')').val(0);
			}
			checkSelectAll();
		});
		
		function checkSelectAll(){
			
			$('#selectAllProductCheckboxCol #select-all').prop('checked',true);
			$('.catalog-prod-chck').each(function(){
				if(!$(this).prop('checked')){
					$('#selectAllProductCheckboxCol #select-all').prop('checked',false);
				}
			});
		}
		/*AAOL-6377 start*/
		var prodId_Qtys='';
		var productCodes='';
		var prodCodes=[]
		$('.add-all-item-btn').click(function(){
			var quanitycheck=false;
			prodCodes=[];
			/*loadingCircleShow("show");*/
			 var prodId_Qty='';
			 var prodQnt='';
			
			 $('.catalog-prod-chck').each(function(){
				if($(this).prop('checked')){
					var prdIndex=$('.catalog-prod-chck').index(this);
					prodId_Qtys=prodId_Qtys+","+$('.productCodePost:eq('+prdIndex+')').val()+":"+$('.catalog-qty:eq('+prdIndex+')').val();
					productCodes+=","+$('.productCodePost:eq('+prdIndex+')').val();
					prodId_Qty=$('.productCodePost:eq('+prdIndex+')').val()+":"+$('.catalog-qty:eq('+prdIndex+')').val();
					prodCodes.push(prodId_Qty);
					if(QTY_CHECK!=""){
						if($('.catalog-qty:eq('+prdIndex+')').val()>Number(QTY_CHECK)){
							quanitycheck=true;
						}
					}					
					
				}
			});
		 
			var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(prodCodes);
			if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
				console.log("useer entry prodIds " +prodCodes );
				ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,productCodes,prodQnt,prodCodes);//AAOL-6377 prodCodes contains all absolute and active seleceted items in the search page.  
			}
			else{
			if(quanitycheck){
				loadingCircleShow("hide");
				$('#newQuantitypopup').modal('show');
			}else{
				var error="";
				var success="";
				var dataObj = new Object();
				dataObj.source = "addToCartForm_1";
				dataObj.prodId_Qtys = prodId_Qtys;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/addTocart' ,
					data: dataObj,
					success: function (data) {
					//	console.log(data);
						location.reload();
						loadingCircleShow("hide");
						
					}
				});	
				
			}
			}
			/*AAOL-6377 end*/
			$(document).off('click', '#back-btn');
			$(document).on('click', '#back-btn',function(e) {
				$('#newQuantitypopup').modal('hide');
				loadingCircleShow("hide");
				return false;	
			});
			$(document).off('click', '#continue-btn');
			$(document).on('click', '#continue-btn',function(e) {
				$('#newQuantitypopup').modal('hide');
				loadingCircleShow("show");
				var error="";
				var success="";
				var dataObj = new Object();
				dataObj.source = "addToCartForm_1";
				dataObj.prodId_Qtys = prodId_Qtys;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/addTocart' ,
					data: dataObj,
					success: function (data) {
					//	console.log(data);
						location.reload();
						loadingCircleShow("hide");
					}
				});
			});
		});
		
	},
	disableSelection : function()
	{
		$(".selprod").each(
				function() {
					
						var currentIndex = $(this).attr("index");
						var qtyInput = $('#qty_'+currentIndex);
						
						if($(this).attr('disableProductRow') == 'true')
						{
							$(this).attr('disabled','disabled');
							qtyInput.attr('disabled','disabled');
						}
				});
		
		
		
		$('.searchSelAll').click(function()
		{  
			 if($("#selall").is(':checked'))
			 {
				$(".selprod").each(function()
					{
						if($(this).attr("disableProductRow") == 'false')
						{
							$(this).attr('checked', 'checked');
							//$('.disabledProd .selprod').removeAttr('checked');	
						}
						else
						{
							$(this).attr('disabled','disabled');
							$(this).removeAttr('checked');
						}
				  });
			 }
			
			 if(!$("#selall").is(':checked'))
			 {
				$(".prodRow ").each(function(){
					$('.selprod').removeAttr('checked');
				  });
			 }
		});
	},

	/**
	 *	Handles 'Get Price' action from the front-end. Performs Ajax call to retieve price for the corresponding product and toggles 
	 *	the <span> layer to display updated price. 
	 */
	getPrice: function()
	{
		$('.getPrice').click(function(e)
				{
			
			loadingCircleShow("show");
					e.preventDefault();
					var currentIndex = $(this).attr("index");
					var productPrice = $(this).attr("productPrice");
					var productCode = $(this).attr("productCode");
					
					var loadUrl = ACC.config.contextPath+"/search/getPrice?productCode="+productCode;
					$.ajax({
					  type: "GET",
					  url: loadUrl,
					  
					  /**
					   * If get price out-bound call is, display returned price if NOT 0.0 else displaye its list price.
					   */
					  success: function(data) 
						  {
						  	if(data == 'error'){
							  	if(productPrice == '')
					  			{
					  				$('#priceRule_'+currentIndex).html("-").show();
					  				
					  			}
							  	else
						  		{
							  		$('#priceRule_'+currentIndex).html(productPrice).show();
						  		}
						   }
						  	else{
							  	if(data == '$0.0' || data == ''||data == '0.0')
						  		{
							  		if(productPrice == '')
							  			{
							  				$('#priceRule_'+currentIndex).html("-").show();
							  			}	
							  		else
							  			{
							  				$('#priceRule_'+currentIndex).html(productPrice).show();
							  			}
						  		}
							  	else
							  	{
							  		$('#priceRule_'+currentIndex).html(data).show();
							  	}	
						  	}
						  	$('#getPrice_'+currentIndex).hide();
							 loadingCircleShow("hide");
						  },
					 /**
					  * If the call fails, display the product price.
					  */
					  error : function(data)
						  {
						  	if(productPrice == '')
				  			{
				  				$('#priceRule_'+currentIndex).html("-").show();
				  				
				  			}
						  	else
					  		{
						  		$('#priceRule_'+currentIndex).html(productPrice).show();
					  		}
						  	$('#getPrice_'+currentIndex).hide();
						  	$("#ajaxCallOverlay").hide();
							$("#modal-ui-dialog").hide();
						  }
					});
				});
		
		$('.getPricemobile').click(function(e)
				{
			        loadingCircleShow("show");
					e.preventDefault();
					var currentIndex = $(this).attr("index");
					var productPriceMobile = $(this).attr("productPriceMobile");
					var productCode = $(this).attr("productCode");
					
					var loadUrl = ACC.config.contextPath+"/search/getPrice?productCode="+productCode;
					$.ajax({
					  type: "GET",
					  url: loadUrl,
					  
					  /**
					   * If get price out-bound call is, display returned price if NOT 0.0 else displaye its list price.
					   */
					  success: function(data) 
						  {
						  	if(data == 'error'){
							  	if(productPriceMobile == '')
					  			{
					  				$('#MobilePriceRule_'+currentIndex).html("-").show();
					  				
					  			}
							  	else
						  		{
							  		$('#MobilePriceRule_'+currentIndex).html(productPriceMobile).show();
						  		}
						   }
						  	else{
							  	if(data == '$0.0' || data == ''||data == '0.0')
						  		{
							  		if(productPriceMobile == '')
							  			{
							  				$('#MobilePriceRule_'+currentIndex).html("-").show();
							  			}	
							  		else
							  			{
							  				$('#MobilePriceRule_'+currentIndex).html(productPriceMobile).show();
							  			}
						  		}
							  	else
							  	{
							  		$('#MobilePriceRule_'+currentIndex).html(data).show();
							  	}	
						  	}
						  	$('#MobileGetgetPrice_'+currentIndex).hide();
							 loadingCircleShow("hide");
						  },
					 /**
					  * If the call fails, display the product price.
					  */
					  error : function(data)
						  {
						  	if(productPriceMobile == '')
				  			{
				  				$('#priceRule_'+currentIndex).html("-").show();
				  				
				  			}
						  	else
					  		{
						  		$('#priceRule_'+currentIndex).html(productPriceMobile).show();
					  		}
						  	$('#getPrice_'+currentIndex).hide();
						  	$("#ajaxCallOverlay").hide();
							$("#modal-ui-dialog").hide();
						  }
					});
				});
	},
	
	/**
	 * Allows ONLY backspace, tab, delete, arrows, numbers and keypad numbers in the Quantity text box.
	 */
	quantityValidation: function(e)
	{
		$('.column3Input , .qty').on('keyup keypress', function()
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
	},
	
	/**
	 * Auto selects/un-selects the product check box on entering value in the quantity box. 
	 */
	productSelection: function(e)
	{
		$('input.column3Input').on('keyup', function(){
			if ($(this).val() > 0) {
				$(this).parent().parent().find("input[type='checkbox']").attr('checked', 'checked');
			} else {
				$(this).parent().parent().find("input[type='checkbox']").removeAttr('checked');
				$("#selall").removeAttr('checked');
			}
		});
	},
	
	bindsearchSortForm: function()
	{
		$('.searchSortForm').change(function()
				{
					this.submit();
				});
	},
	
	/**
	 * Function to handle on click of 'ADD SELECTION TO CART' or 'ADD TO CART' from Product Search, PLP, PDP pages.
	 */
	addToCartRequest: function(e)
	{
		$("#addToCart").click(function() {
			var productCode = "";
			var currentIndex = "";

			var quantity = "";
			$(".selprod").each(function() {
				if ($(this).is(':checked')) {
					 productCode = ($(this).attr("id"));
					 currentIndex = $(this).attr("index");
					 quantity = $('#qty_'+currentIndex).val()
					}
				});
		
	
		loadingCircleShow("show");
		$("#errorQuickCart").hide();	
		var numberOfProdSelected = 0;
		var selectedProdCodes = [];
		var prodId_Qtys =productCode+":"+ quantity;
		selectedProdCodes.push(prodId_Qtys); 
		// AAOL-6378 
		var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(selectedProdCodes);
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
			console.log("useer entry prodIds " +selectedProdCodes );
			ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,productCode,quantity,null);//AAOL-6377 here there is no active products, thats why we are passing null in place all selected products.
		}
		//AAOL-2406 end
		else
		{
			
		//check isContractProduct alone or not
			jQuery.ajax({   
				url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
				async: false,
				success : function(data) {
					var nonContractProductInCart = data.nonContractProductInCart;
					var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
					if (nonContractProductInCart === nonContractProductInSelectedList) {
						ACC.product.submitProductCart(prodId_Qtys);
					} else {
						ACC.product.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
					}
				}
			});
		}
		});
	
		
		$(document).on('click',".addToCartKitProduct",function() {
			var productCode = "";
			var currentIndex = "";
			var quantity = "";
			
			 productCode = ($(this).attr("id"));
			 currentIndex = $(this).attr("index");
			 quantity=$('#kitCompQty_'+currentIndex).val()
			loadingCircleShow("show");
		$("#errorQuickCart").hide();	
		var numberOfProdSelected = 0;
		var selectedProdCodes = [];
		var prodId_Qtys =productCode+":"+ quantity;
		selectedProdCodes.push(productCode); 
			
		//check isContractProduct alone or not
			jQuery.ajax({   
				url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
				async: false,
				success : function(data) {
					var nonContractProductInCart = data.nonContractProductInCart;
					var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
					if (nonContractProductInCart === nonContractProductInSelectedList) {
						ACC.product.submitProductCart(prodId_Qtys);
					} else {
						ACC.product.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
					}
				}
			});
		});
	//Changes for fixing Add to cart defect for product at 11th position onward
	//		$(".addToCart").click(function(){
	$(document).on("click",".addToCart",function(){
			var prodCode = $(this).attr("id");
			var currentIndex = $(this).attr("index");
			var currViewScreenName = $("#currViewScreenName").val();
			console.log("currViewScreenName : "+ currViewScreenName);
			var qtyValue = $('#qty_'+currViewScreenName+'_'+currentIndex).val()
			//AAOL-6377 start
			if(qtyValue==0){
				qtyValue=1;
			}
			//AAOL-end
			loadingCircleShow("show");
			$("#errorQuickCart").hide();	
			var numberOfProdSelected = 0;
			var selectedProdCodes = [];
			var prodId_Qtys =prodCode+":"+ qtyValue;
			selectedProdCodes.push(prodId_Qtys); //AAOL-6377
			//AAOL-2406 start
			var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(selectedProdCodes);
				if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
					console.log("useer entry prodIds " +selectedProdCodes );
					ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,prodCode,qtyValue,null);//AAOL-6377 here there is no active products, thats why we are passing null in place all selected products.
			}//AAOL-2406 end
				else if(QTY_CHECK!=""){
					if(qtyValue>Number(QTY_CHECK)){
						 quanitycheck();
							
					}
				}					
				else
				{
					addTocartAjax();
				}
				/*AAOL-4068/AAOL-6550*/
				function quanitycheck(){
					loadingCircleShow("hide");
					$('#newQuantitypopup').modal('show');
				}
				$(document).off('click', '#back-btn');
				$(document).on('click', '#back-btn',function(e) {
					$('#newQuantitypopup').modal('hide');
					loadingCircleShow("hide");
					return false;	
				});
				$(document).off('click', '#continue-btn');
				$(document).on('click', '#continue-btn',function(e) {
					$('#newQuantitypopup').modal('hide');
					loadingCircleShow("show");
					addTocartAjax();
					
				});
				function addTocartAjax(){				
					jQuery.ajax({   
						url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
						async: false,
						success : function(data) {
							var nonContractProductInCart = data.nonContractProductInCart;
							var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
							if (nonContractProductInCart === nonContractProductInSelectedList) {
								ACC.product.submitProductCart(prodId_Qtys);
							} else {
								ACC.product.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
							}
						}
					});
				}
			});
		
		/*AAOL-5157 - add to cart the selected product by pressing enter in the qty box*/
		$(".addtocartqty").keypress(function(e){
			var qtyBoxIndex=$(".addtocartqty").index(this);
			if(e.which==13)
			{ $(".addToCart:eq("+qtyBoxIndex+")").click(); }
			});
	},
	
	
	/**
	 * Checks and submits Ajax call for selected product for Add to Cart.
	 */
	submitAddToCartForm : function()
	{
		var addToCartForm = $('#prodAddToCartForm');
		var numberOfProdSelected = 0;
		$(".selprod").each(
				function() {
					if ($(this).is(':checked')) {
						var prodCode = ($(this).attr("id"));
						var currentIndex = $(this).attr("index");

						var qtyValue = $('#qty_'+currentIndex).val()
						
						$('<input>').attr({
						    type: 'hidden',
						    id: prodCode,
						    name: 'productCodeAndQty',
						    value: prodCode+':'+qtyValue
						}).appendTo(addToCartForm);
						numberOfProdSelected++;
					}
				});
		
		if(numberOfProdSelected > 0)
			{
				addToCartForm.ajaxSubmit({
					success : function(data)
					{ loadingCircleShow("hide");
						window.location.reload();
						$("#prodAddToCartForm").empty();
					},
					error : function(data)
					{
						$("#divisonError").css("display","block");
					}
				});
			}
	},
	
	resetproductSelection: function(e)
	{
		$(".selprod").each(
				function() {
					if ($(this).is(':checked')) {
					
						$(this).removeAttr('checked');
						var currentIndex = $(this).attr("index");
						$('#qty_'+currentIndex).val(0);
					}
						
				});
		$("#selall").removeAttr('checked');
		 
	},
	
	
	addToCart : function(cartResult, statusText, xhr, formElement) {
		var productCode = $('[name=productCodePost]', formElement).val();
		var quantityField = $('[name=qty]', formElement).val();
		var quantity = 1;
		if (quantityField != undefined) {
			quantity = quantityField;
		}
		$('#cart_popup').hide();
		$('#cart_popup').html(cartResult.cartPopupHtml);

		$('#add_to_cart_close').click(function(e) {
			e.preventDefault();
			$('#cart_popup').hide();
		});

		$('#cart_popup').fadeIn();
		if (typeof timeoutId != 'undefined') {
			clearTimeout(timeoutId);
		}
		timeoutId = setTimeout(function() {
		}, 50);
	
		
		var updateMiniCartComponet = $('#lineInput').val();
		$("#lineItems").html(updateMiniCartComponet);
		$("#cart_header").addClass("shoppingcart");
		$("#cart_header").removeClass("shoppingcartDisabled");
		$("#cart_header .cart_content").attr("id","cart_content");
		$('#miniCartHrefId').attr("href",$("#hddnCartUrl").val());
		setTimeout(function(){$('#cart_popup').fadeOut();}, 5);
		if(quantityField == 0)
		{
			var infoMsgLayer = $('.marTop20');
			infoMsgLayer.show("slow");
			infoMsgLayer.delay(7000).fadeOut("slow");
		}
	},
	
	submitProductCart :function(prodId_Qtys){
		var errorMsg = "";
		var dataObj = new Object();
		dataObj.source = "addToCartForm_1";
		var allValid = true;
		var productAdded = false;
		dataObj.prodId_Qtys = prodId_Qtys;
		jQuery.ajax({
			type: "POST",
			url:  ACC.config.contextPath +'/home/addTocart' ,
			data: dataObj,
			success: function (data) {
				
			
				/*console.log(data);
				window.location.href = ACC.config.contextPath +'/cart';*/
				 location.reload();
				loadingCircleShow("hide");
				
			}
		});
},
confirmDialog :function(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList){
	$('#contractpopup').modal('show');
	
	//$('#cancel-btn-addtocart0').click( function(e) {
	$(document).off('click', '#cancel-btn-addtocart').on('click', '#cancel-btn-addtocart',function(e) {
		ACC.product.callback(prodId_Qtys,false,nonContractProductInCart,nonContractProductInSelectedList);
		e.preventDefault();
	});
	
	//$('#accept-btn-addtocart0').click( function(e) {
	$(document).off('click', '#accept-btn-addtocart').on('click', '#accept-btn-addtocart',function(e) {
		ACC.product.callback(prodId_Qtys,true,nonContractProductInCart,nonContractProductInSelectedList);
		e.preventDefault();
	});
},
callback : function (prodId_Qtys,value,nonContractProductInCart,nonContractProductInSelectedList) {
    if (value) {
    	ACC.product.submitProductCart(prodId_Qtys);
    	loadingCircleShow("hide");
    } else {
    	//need to call remove product which is already non contract
    	if(nonContractProductInCart && !nonContractProductInSelectedList){
    		ACC.product.removeNonContractProduct(prodId_Qtys)
    	}
    	else{
    		//nothing to perform becasue dont want to add this non contract product in the contract product 
    		loadingCircleShow("hide");
    	}
    }
},
removeNonContractProduct : function(prodId_Qtys){
	jQuery.ajax({  
		url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
		async: false,
		success : function(data) {	
			ACC.product.submitProductCart(prodId_Qtys);
			loadingCircleShow("hide");
		} 
	});
},

	exportCatalog: function()
	{ 
		$('#exportFullCatalog').click(function()
				{
			$.colorbox({
				html : $(".downloadCatalogWrapper").html(),
				height: 'auto',
				width: '565px',
				overlayClose: true,					
				onComplete: function(){
					$('.downloadSubmitButton').click(function(){
						$.ajax({
							type : "POST",
							url : ACC.config.contextPath + "/c/catalog-Download/" + $("#categoryCode").val(),
							success : function(data) {
								parent.$.colorbox.close();
							},
							error : function(e) {
								console.log("an error occured while trying to process the records.")
							}
						});
					});
			}
		});
	});
	}

};

$(document).ready(function() {
	ACC.product.bindAll();
});

