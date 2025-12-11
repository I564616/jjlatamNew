$(document).ready(function(){

	setTimeout(function(){
		$("#invalid-error-msg").hide();
	},10000)
});

$(".laAddToCartOrderTemplate").click(function(e){
	
	var numberOfProductLines = parseInt($("#numberOfProductLines").val());	
	loadingCircleShow("show");
	$("#errorQuickCart").hide();	
	var numberOfProdSelected = 0;
	var selectedProdCodes = [];
	var prodId_Qtys = [];
	
	for (var int=0;int<numberOfProductLines;int++) {
		if($("#productId"+int).val() != "" && $("#productId"+int).val() != undefined){
			prodId_Qtys += $("#productId"+int).val()+":"+ $("#quantity"+int).val() + ",";
			selectedProdCodes.push($("#productId"+int).val());
			numberOfProdSelected++;			
		}
	}

	if(numberOfProdSelected > 0) {
		jQuery.ajax({   
			url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
			async: false,
			success : function(data) {
				var nonContractProductInCart = data.nonContractProductInCart;
				var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
				if (nonContractProductInCart == nonContractProductInSelectedList) {
					submitAddToCartFromTemplate(prodId_Qtys);
				} else {
					
					confirmLaDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
				}
			}
		});
	}
});

function submitAddToCartFromTemplate(prodId_Qtys) {

	var dataObj = {};
	dataObj.source = "addToCartForm_1";
	dataObj.prodId_Qtys = prodId_Qtys;
	dataObj.isTemplateAdd = true;
	jQuery.ajax({
		type: "POST",
		url:  ACC.config.contextPath +'/cart/laAddToCartOrderTemplate' ,
		data: dataObj,
		success: function (data) {
			window.location.href = ACC.config.contextPath +'/cart';
			loadingCircleShow("hide");			
		}
	});
	
}

function confirmLaDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList){

	$('#contractpopup').modal('show');
	loadingCircleShow("hide");	
	
	$(document).off('click', '#cancel-btn-addtocart').on('click', '#cancel-btn-addtocart',function(e) {		
		ACC.addToCart.callback(prodId_Qtys,false,nonContractProductInCart,nonContractProductInSelectedList);
		e.preventDefault();
	});
	
	$(document).off('click', '#accept-btn-addtocart').on('click', '#accept-btn-addtocart',function(e) {		
		ACC.addToCart.callback(prodId_Qtys,true,nonContractProductInCart,nonContractProductInSelectedList);
		e.preventDefault();
	});
}
