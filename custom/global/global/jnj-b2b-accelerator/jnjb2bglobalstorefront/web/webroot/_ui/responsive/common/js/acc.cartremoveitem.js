ACC.cartremoveitem = {
	
	bindAll: function() {
		if(typeof cartRemoveItem != 'undefined' && cartRemoveItem) {
			this.bindCartRemoveProduct();
		}
	},
	bindCartRemoveProduct: function() {
		$(document).on("click",".submitRemoveProduct", function(event){
			var deviceName=$("#currViewScreenName").val();
			// fix for unique ids
			var prodid= $(this).attr('id').split("_")
			prodid=prodid[1];
			var currId = '#updateCartForm'+prodid+'_'+deviceName;
			var productCode = $(currId).get(0).productCode.value;
			var initialCartQuantity = $(currId).get(0).initialQuantity.value;
			ACC.cartremoveitem.trackRemoveFromCart(productCode, initialCartQuantity);
			$(currId).get(0).quantity.value = 0;
			$(currId).get(0).initialQuantity.value = 0;
			if(deviceName=='mobile'){
				 $(currId).append(jQuery('<input>', { //  Account UID input created
	 					'name' : 'CSRFToken',
	 					'value' : ACC.config.CSRFToken,
	 					'type' : 'hidden'
	 				})).get(0).submit();
			}else{
				$(currId).get(0).submit();
			}
			
			
				
		});
		
		var dataObject = new Object();
		
		$( ".updateQuantityProduct" ).hide(); 
		jQuery('.qtyUpdateTextBox').on('input', function() {
			
			$( ".updateQuantityProduct" ).show();  // update anchor tag link
			
			
		});
		

		$('.updateQuantityProduct').click(function() {
			var deviceName=$("#currViewScreenName").val();
			// fix for unique ids
			var prodid= $(this).attr('id').split("_")
			prodid=prodid[1];
			var currId = '#updateCartForm'+prodid+'_'+deviceName;
			var productCode = $(currId).get(0).productCode.value;
			var initialCartQuantity = $(currId).get(0).initialQuantity.value;
			var newCartQuantity = $(currId).get(0).quantity.value;
			
			/*AAOL-4068/AAOL-6550*/
			if(QTY_CHECK!=""){
				if(newCartQuantity>Number(QTY_CHECK)){
					$('#newQuantitypopup').modal('show');	
					
				}else{
					ACC.cartremoveitem.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
					$(currId).get(0).submit();
				}
			}else{
				ACC.cartremoveitem.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
				$(currId).get(0).submit();
			}
			
			
			$(document).off('click', '#back-btn');
			$(document).on('click', '#back-btn',function(e) {
				$('#newQuantitypopup').modal('hide');
				return false;	
			});
			
			$(document).off('click', '#continue-btn');
			$(document).on('click', '#continue-btn',function(e) {
				$('#newQuantitypopup').modal('hide');
				ACC.cartremoveitem.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
				$(currId).get(0).submit();
			});
			
		});
	},
	trackRemoveFromCart: function(productCode, initialCartQuantity) {
		window.mediator.publish('trackRemoveFromCart',{
			productCode: productCode,
			initialCartQuantity: initialCartQuantity
		});
	},
	trackUpdateCart: function(productCode, initialCartQuantity, newCartQuantity) {
		window.mediator.publish('trackUpdateCart',{
			productCode: productCode,
			initialCartQuantity: initialCartQuantity,
			newCartQuantity: newCartQuantity
		});
	}
};

$(document).ready(function() {
	ACC.cartremoveitem.bindAll();
});

//Changes for Bonus Item
function hideFreeGood(prodCode, thisObject, orderedQuantity) {
	 var id= document.getElementById('freeGoodQuantity'+prodCode);
	 
	 if(id != undefined) {
		var totalQuantity = parseInt(thisObject.value, 10) + parseInt(id.value, 10);
		if(totalQuantity>orderedQuantity){
			thisObject.value = orderedQuantity;			
		}
		else{
			thisObject.value = totalQuantity;			
		}
		
	 }
	 var freeGoodsDiv = document.getElementById('freeGood'+prodCode);
	 if(freeGoodsDiv != undefined) {
		 freeGoodsDiv.remove();
	 }
}
