var filledFields = [];
$("#laQuickAddToCart").click(function() {
    var numberOfProdSelected = 0;
    var selectedProdCodes = [];
    var prodIds;
    var count = 1;
    filledFields = [];

    $("span.prod").each(function() {
        if ($(this).find("input").val() != "") {
            filledFields.push(count);
        }
        count++;
    });

    if (filledFields.length == 0) {
        $("#productId1").focus();
        return false;
    }

    if ($("#productQuantityForm").valid()) {

        var prodId_Qtys = $("#productId" + filledFields[0]).val() + ":" + $("#quantity" + filledFields[0]).val();
        prodIds = $("#productId" + filledFields[0]).val();
        selectedProdCodes.push($("#productId" + filledFields[0]).val());
        numberOfProdSelected++;

        for (var int = 1; int < filledFields.length; int++) {
            if ($("#productId" + filledFields[int]).val() != "") {
                numberOfProdSelected++;
                prodId_Qtys += "," + $("#productId" + filledFields[int]).val() + ":" + $("#quantity" + filledFields[int]).val();
                prodIds += "," + $("#productId" + filledFields[int]).val();
                selectedProdCodes.push($("#productId" + filledFields[int]).val());
            }
        }

        if (numberOfProdSelected > 0) {
            jQuery.ajax({
                url: ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts=' + selectedProdCodes,
                async: false,
                success: function(data) {
                    var nonContractProductInCart = data.nonContractProductInCart;
                    var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
                    if (nonContractProductInCart === nonContractProductInSelectedList) {
                        ACC.addToCartHome.quickAddToCartNonContractProductAddToCart(prodId_Qtys);
                    } else {
                        ACC.addToCartHome.confirmDialogQuickAddToCart(prodIds, prodId_Qtys, nonContractProductInCart, nonContractProductInSelectedList);
                    }
                }
            });
        }
    }
});

ACC.addToCartHome.quickAddToCartNonContractProductAddToCart = function(prodId_Qtys) {

    var dataObj = {};
    var allValid = true;
    var productAdded = false;
    dataObj.prodId_Qtys = prodId_Qtys;

    jQuery.ajax({
        type: "POST",
        url: ACC.config.contextPath + '/home/addTocart',
        data: dataObj,
        success: function(data) {
            var productCount = 0;
            var currentField = 0;
            $('#items-msg').text(data.totalUnitCount);
            $.each(data.cartModifications, function(i, data) {
                $.each(this, function(key, value) {
                    if ((isNaN(value) || value === true || value === false) && key != 'entry') { //
                        if (key === 'statusCode' && /restri/i.test(value)) {
                            currentField = filledFields[productCount];
                            productCount++;

                            showHideErrors("#errorResCatDiv_", currentField);

                            ACC.addToCartHome.showErrorIncart('#quickaddcart-popup', 'click');
                            allValid = false;
                        }
                        else if (key === 'statusCode' && /inv/i.test(value)) {
                            currentField = filledFields[productCount];
                            productCount++;

                            showHideErrors("#errorDiv_", currentField);

                            ACC.addToCartHome.showErrorIncart('#quickaddcart-popup', 'click');
                            allValid = false;
                        }
                        else if (key === 'statusCode' &&
                            (value === "success" || value === "update" ||
                                value == 'basket.page.message.minQtyAdded' || value == 'basket.page.message.update')) {
                            currentField = filledFields[productCount];
                            productCount++;
                            productAdded = true;

                            $('#productId' + currentField).val('');
                            $('#quantity' + currentField).val('');

                            showHideErrors("#successDiv_", currentField);
                        }
                    }
                });

            });
            if (allValid && productAdded) {
                window.location.href = ACC.config.contextPath + '/cart';
            }
        }
    });

};

ACC.addToCartHome.callbackQuickAddToCart = function(prodIds, prodId_Qtys, value, nonContractProductInCart, nonContractProductInSelectedList) {

    $('#quickaddcart-popup .modalcls').show();
    $('#quickaddcart-popup').show();

    if (value) {
        ACC.addToCartHome.quickAddToCartNonContractProductAddToCart(prodId_Qtys);
    } else {
        if (nonContractProductInCart && !nonContractProductInSelectedList) {
            ACC.addToCartHome.removeNonContractProductQuickAddToCart(prodId_Qtys)
        } else {
            loadingCircleShow("hide");
        }
    }

};

$('#productQuantityForm').on('click', '#select-account-close, #la-select-accnt-close', function() {
    location.reload();
});

function showHideErrors(divIdToManipulate, currentField) {

    $('#errorDiv_' + currentField).addClass("hide");
    $('#errorResCatDiv_' + currentField).addClass("hide");
    $('#successDiv_' + currentField).addClass("hide");
    $('#errorDiv_' + currentField).hide();
    $('#errorResCatDiv_' + currentField).hide();
    $('#successDiv_' + currentField).hide();
    $(divIdToManipulate + currentField).removeClass("hide");
    $(divIdToManipulate + currentField).show();

}