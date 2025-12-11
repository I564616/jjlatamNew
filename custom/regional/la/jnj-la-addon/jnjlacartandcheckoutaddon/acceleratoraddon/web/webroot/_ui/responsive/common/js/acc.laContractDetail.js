ACC.contractdetail.callback = function(form,accept,nonContractProductInCart,nonContractProductInSelectedList,datas){
    if (accept) {
        if(datas.multiContractCount > 0){
            ACC.contractdetail.clearCartProduct(form);
        }else{
            ACC.contractdetail.removeNonContractProduct(form)
        }
    }
    else {
        loadingCircleShow("hide");
    }
}

ACC.contractdetail.confirmDialog = function(form,nonContractProductInCart,nonContractProductInSelectedList,datas){
    if(datas.multiContractCount > 0){
        $("#contractTitle").text($("#multiContractMsgTitle").val());
        $("#popInfoText").text($("#multiContractMsgInfoText").val());
        $("#contractMessage").text($("#multiContractMessageText").val());
    }
    else if(nonContractProductInCart && !nonContractProductInSelectedList){
        $("#contractTitle").text($("#contractMsgTitle").val());
        $("#popInfoText").text($("#contractMsgInfoText").val());
        $("#contractMessage").text($("#contractMessageText").val());
    }else{
        $("#contractTitle").text($("#nonContractMsgTitle").val());
        $("#popInfoText").text($("#nonContractMsgInfoText").val());
        $("#contractMessage").text($("#nonContractMessageText").val());
    }

    $('#contractpopup').modal('show');

    $(document).off('click', '#cancel-btn-addtocart1').on('click', '#cancel-btn-addtocart1',function(e) {
          ACC.contractdetail.callback(form,false,nonContractProductInCart,nonContractProductInSelectedList,datas);
    });
    $(document).off('click', '#accept-btn-addtocart1').on('click', '#accept-btn-addtocart1',function(e) {
          ACC.contractdetail.callback(form,true,nonContractProductInCart,nonContractProductInSelectedList,datas);
    });
};