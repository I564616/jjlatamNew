ACC.addToCart = {
	submitProductCartFromCarousel :function(cartButton){
    	    $('#laodingcircle').show();
    	    var productCode = cartButton.parent().parent().siblings(".imgProductDescrp").children(".productCode").text();
            var prodId_Qtys =productCode+":1";
            var dataObj = new Object();
            dataObj.source = "addToCartForm_1";
            dataObj.prodId_Qtys = prodId_Qtys;
            jQuery.ajax({
                type: "POST",
                url:  ACC.config.contextPath +'/cart/laAddTocart' ,
                data: dataObj,
                success: function (data) {
                    location.reload();
                }
            });
        },
	submitProductCart :function(prodId_Qtys){
			var dataObj = {};
			dataObj.source = "addToCartForm_1";
			dataObj.prodId_Qtys = prodId_Qtys;
			jQuery.ajax({
				type: "POST",
				url:  ACC.config.contextPath +'/cart/laAddTocart' ,
				data: dataObj,
				success: function (data) {
					window.location.href = ACC.config.contextPath +'/cart';
					loadingCircleShow("hide");
				}
			});
	},
	confirmDialog :function(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');
		
		$(document).off('click', '#cancel-btn-addtocart0').on('click', '#cancel-btn-addtocart0',function(e) {
			ACC.addToCart.callback(prodId_Qtys,false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
		
		$(document).off('click', '#accept-btn-addtocart0').on('click', '#accept-btn-addtocart0',function(e) {
			ACC.addToCart.callback(prodId_Qtys,true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	callback : function (prodId_Qtys,value,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	ACC.addToCart.submitProductCart(prodId_Qtys);
	    	loadingCircleShow("hide");
	    } else {
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.addToCart.removeNonContractProduct(prodId_Qtys)
	    	}
	    	else{
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	removeNonContractProduct : function(prodId_Qtys){
		jQuery.ajax({  
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {	
				ACC.addToCart.submitProductCart(prodId_Qtys);
				loadingCircleShow("hide");
			} 
		});
	}
};
$('#complementaryInfoOrder').change(function () {
    $('.registerPOError').html('');
    var compNum=$("#complementaryInfoOrder").val();
    jQuery.ajax({
         url: ACC.config.contextPath +'/cart/updateComplementaryInfo?complementaryInfo=' + encodeURIComponent(compNum),
         async: false,
         success: function (data) {
         }
     });
});


ACC.addToCartHome.ValidateNonContractProductAddToCart = function(prodIds){

    var dataObj = {};
    var allValid = true;
    var productAdded = false;
    var errorMsg = "";
    dataObj.source = "addToCartForm_2";
    dataObj.prodIds = prodIds;
    dataObj.CSRFToken = ACC.config.CSRFToken;

    jQuery.ajax({
        type: "POST",
        url:  ACC.config.contextPath +'/home/multiAddToCart' ,
        data: dataObj,
        async: false,
        success: function (data) {
            $.each(data.cartData,function(){
                $.each(this, function(product,msg) {
                	if( msg.length == 1 ){
                    	msg = "";
                    }
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
                loadingCircleShow("hide");
                $("#errorMultiCart").show();
                errorMsg = errorMsg.substring(1, errorMsg.length);
                var msgArray = errorMsg.split('|');
                $.each(msgArray,function(index,element)
                {
                    $("#error-detail-popup").find(".error-content").append("<div style='margin-bottom:10px'>"+element+"</div>");
                })
            }else{
                $("#errorMultiCart").hide();
            }
            if(productAdded){
                var domSuccessElement = $("#mltiAddToCartForm").find("div.registersucess");
                domSuccessElement.removeClass("hide");
                setTimeout(function(){
                    domSuccessElement.slideUp(500);
                    window.location.href = ACC.config.contextPath +'/cart';
                }, 3000);
            }
        }
    });
}
$(document).ready(function() {
    if ($.inArray('placeOrderGroup',permissions) != -1 || $.inArray('placeOrderBuyerGroup',permissions) != -1){
        $(".laAddToCart").click(function(e){
            var numberOfProductLines = parseInt($("#numberOfProductLines").val(), 10);

            loadingCircleShow("show");
            $("#errorQuickCart").hide();
            var numberOfProdSelected = 0;
            var selectedProdCodes = [];
            var prodId_Qtys =$("#productId0").val()+":"+ $("#quantity0").val();
            selectedProdCodes.push($("#productId0").val())
            for (var int=1;int<=numberOfProductLines;int++) {
                if($("#productId"+int).val() != "" && $("#productId"+int).val() != undefined){
                    prodId_Qtys+=","+$("#productId"+int).val()+":"+ $("#quantity"+int).val();
                    selectedProdCodes.push($("#productId"+int).val());

                }
                numberOfProdSelected++;
            }

            if(numberOfProdSelected > 0) {
                jQuery.ajax({
                    url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
                    async: false,
                    success : function(data) {
                        var nonContractProductInCart = data.nonContractProductInCart;
                        var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
                        if (nonContractProductInCart == nonContractProductInSelectedList) {
                            ACC.addToCart.submitProductCart(prodId_Qtys);
                        } else {
                            ACC.addToCart.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
                        }
                    }
                });
            }
        });
    }

    $(".addToCartLaCarousel").click(function(){
            ACC.addToCart.submitProductCartFromCarousel($(this));
        });




});

ACC.addToCartHome.callbackValidate = function (prodIds,value,nonContractProductInCart,nonContractProductInSelectedList) {
    if (value) {
        ACC.addToCartHome.ValidateNonContractProductAddToCart(prodIds);
    }
    else {
        if(nonContractProductInCart && !nonContractProductInSelectedList){
            ACC.addToCartHome.removeNonContractProductValidate(prodIds)
        }
        else{
            loadingCircleShow("hide");
        }
    }
};


$('.saveorderastemplatelatam').click(function(e) {
	var saveAsTemplatepopup=$("#outerSaveAsTemplate").html()
	$('#SaveAsTemplateHolder').html(saveAsTemplatepopup);
	$('#save-as-tempalte-popup').modal("show");
	$(".createCartTemplatelatam").click(function(e) {
			if($.trim($("#templateName").val())!="") {
				
			
				jQuery.ajax({
					url : ACC.config.contextPath
							+ '/my-account/template/save?templateName='
							+ $("#templateName").val()
							+ '&shared='+$("#share-account").is(":checked") + '&orderId='
							+$("#orderCode").val(),
					success : function(data) {
						
						if (data) {
							$('#save-as-tempalte-popup').modal('hide');
							$('#successMessage_cart').show();
						} else {
							alert('Not able to save template');
						}
					}
				});
			} else {
				
				$("#save-as-tempalte-popup").find(".registerError").html('<label for="templateName" class="error">Please complete all required fields.</label>');
			}
		});
		
	});

$("#error-detail-popup .close").click(function (){
    window.location.reload();
})