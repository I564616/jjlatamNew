ACC.laCartRemoveItem = {

	bindCartRemoveProduct: function() {
	    $('.laUpdateQuantityProduct').click(function() {
            var prodid = $(this).attr('id').split("_")[1];
            var currId = '#updateCartForm'+prodid;
            var productCode = $(currId).get(0).productCode.value;
            var initialCartQuantity = $(currId).get(0).initialQuantity.value;
            var newCartQuantity = $(currId).get(0).quantity.value;

            ACC.laCartRemoveItem.trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
            $(currId).get(0).submit();
        });

	    $('.laSubmitRemoveProduct').click(function(){
            // fix for unique ids
            var prodid= $(this).attr('id').split("_")[1];
            var currId = '#updateCartForm'+prodid;
            var productCode = $(currId).get(0).productCode.value;
            var initialCartQuantity = $(currId).get(0).initialQuantity.value;
            ACC.laCartRemoveItem.trackRemoveFromCart(productCode, initialCartQuantity);
            $(currId).get(0).quantity.value = 0;
            $(currId).get(0).initialQuantity.value = 0;
            $(currId).get(0).submit();
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
	ACC.laCartRemoveItem.bindCartRemoveProduct();
});
