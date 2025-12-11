$(document).on('click', '.laAddToCartSearchResult', function(e){
	
	var currentProdId = $(this).attr("id");
	var currentIndex = $(this).attr("index");
	var currentProdQunatity =  $("#quantity"+currentIndex).val();
	loadingCircleShow("show");
	$("#errorQuickCart").hide();	
	var numberOfProdSelected = 0;
	var selectedProdCodes = [];
	
	var prodId_Qtys = currentProdId+":"+currentProdQunatity;
	selectedProdCodes.push(currentProdId);	
	
	if(currentProdId != "" && currentProdId != undefined){
		numberOfProdSelected = 1;		
	}
		
	if(numberOfProdSelected > 0) {
		jQuery.ajax({   
			url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
			async: false,
			success : function(data) {
				var nonContractProductInCart = data.nonContractProductInCart;
				
				var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
				
				if (nonContractProductInCart == nonContractProductInSelectedList) {					
					var dataObj = {};
					dataObj.source = "addToCartForm_1";
					dataObj.prodId_Qtys = prodId_Qtys;
					jQuery.ajax({
						type: "POST",
						url:  ACC.config.contextPath +'/cart/laAddTocart' ,
						data: dataObj,
						success: function (data) {
							loadingCircleShow("hide");
							window.location.reload();
						}
					});
					
				} else {					
					ACC.addToCart.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
				}
			}
		});
	}
});