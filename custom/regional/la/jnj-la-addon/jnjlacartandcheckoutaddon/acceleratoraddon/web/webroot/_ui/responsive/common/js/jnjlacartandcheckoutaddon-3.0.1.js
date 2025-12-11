var orderCodeDataPinputClass = ".orderCodeData  p  input";

$('.showMoreInfo').hover(function(e) {
	var entryNum = $(this).attr('data');
	$("#entryMoreInfoDiv" + entryNum).show();

	let showMoreInfoLink = $(this).find('#showMoreInfoLink');

});

$('.showMoreInfo').mouseout(function(e) {
	var entryNum = $(this).attr('data');
	$("#entryMoreInfoDiv" + entryNum).fadeOut();
});

/* ATP popup js */
$(document).ready(function(){

	var quantityindex = 0;
	$(".deliveryDate-holder").each(function(){
		var quantitylength = $(this).find(".schedule-quantity").length
		if(quantitylength == 0){
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#b41601');
		}
		else if(quantitylength == 1 ){
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#828282');
		}
		else {
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#ffcc00');
		}
		quantityindex=quantityindex+1;
	});
	
	var headcheckflag = false;
	$(".addContractToCart").prop('disabled',true);		
	checkStatus();
	function checkStatus(){ 			
		headcheckflag = false;			
		$('.contract-tcell-chckbox').each(function(){
			if($(this).prop('checked')== true){					
				headcheckflag = true;
			}	
		});		
		if(headcheckflag == true){
			$(".addContractToCart").prop('disabled',false);
		}
		else{
			$(".addContractToCart").prop('disabled',true);
		}
	}		
	
	 $('#ContractDetailsPage').on("click","#contract-select-all,.contract-tcell-chckbox", function(){
         checkStatus();
	 });
	
	
	var quantityindex = 0;
	$(".deliveryDate-holder").each(function(){
		var quantitylength = $(this).find(".schedule-quantity").length
		if(quantitylength == 0){
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#b41601');
		}
		else if(quantitylength == 1 ){
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#828282');
		}
		else {
			$('.laQtyUpdateTextBox:eq('+quantityindex+')').css('color','#ffcc00');
		}
		quantityindex=quantityindex+1;
	})
		
})

$("#laSubmitChangeAddress").unbind( "click");
$('#laSubmitChangeAddress').click(function () {
    var id_Address = $(".addressSelected div.list-group-item-text input.shipp").val();
    if(id_Address!=undefined){
        $('#selectAdressErr').hide();

        var input = $('<input>', {
            'name' : 'shippingAddress',
            'value' : id_Address,
            'type' : 'hidden'
        });

        var CSRFToken = $('<input>', { //  Account GLN input created
            'name' : 'CSRFToken',
            'value' : ACC.config.CSRFToken,
            'type' : 'hidden'
        })

        $('#changeAddForm').append(input);
        $('#changeAddForm').append(CSRFToken);
        $("#shippingAddress1").val('');
        $('#changeAddForm').submit();
    }
    else{
        $('#selectAdressErr').show();
    }
});

$(".laShowProductDetails").click(function(e) {
    e.preventDefault();
    jQuery.ajax({
        url : ACC.config.contextPath +'/**/p/productDetailPopUp?productCode='
        + $(this).attr('data'),
        type: 'GET',
        content: 'text/html',
        success : function(data) {
            $('#product-details-popupholder').html(data);
            $('#product-detail-popup').modal('show');
        }
    });
});


$('.laQtyUpdateTextBox').on('input', function(){
    $(".cartUpdateAllbutton").show();
});

$('.laQtyUpdateTextBox').keyup(function() {
	   if (this.value.match(/[^0-9 ]/g)) {
	    this.value = this.value.replace((/[^0-9 ]/g), '');
	   }
});

$(".laQtyUpdateTextBox").change(function(e) {
	var entryNumber = ($(this).attr("entryNumber"));
	var qty = $(this).val();
	if (qty != "") {
		dataObject1[entryNumber]=qty;
	}
});

$(document).on("click",".cartUpdateAllbutton", function(event){
	loadingCircleShow("show");
	var updateMultipleEntriesInCartForm = $('#UpdateMultipleEntriesInCartForm');
	for (var entryNumber in dataObject1) {
		  if (dataObject1.hasOwnProperty(entryNumber)) {
			  var entryQty = entryNumber + ":" + dataObject1[entryNumber];
			  $('<input>').attr({
					type : 'hidden',
					id : 'entryQuantityList',
					name : 'entryQuantityList',
					value : entryQty
				}).appendTo(updateMultipleEntriesInCartForm);
		  }
    }

    updateMultipleEntriesInCartForm.submit();
});

$(document).ready(function(){
	var numberOfItemscart = parseInt($("#numberOfItemscart").val());
	if(numberOfItemscart >=2)
	{
		$(".saveorderastemplatelatam").removeAttr("disabled");
	}
});

$('.cartStep1Saveupdate1Latam').click(
function(e) {
    loadingCircleShow("show");
    window.location.href = ACC.config.contextPath+ '/cart/validate';
});


let cifobflag = $("#freightType").val();
let confirmationpagefreighttype= $("#orderconfirmationfreightType").val();
let countryspecificcode = $("#countryspecificcode").val();	

if(countryspecificcode=="BR"){
if (cifobflag=="CIF") {
    document.getElementById('cif').checked = true;
    $('#freightypediv').css('display', 'block');
} else if (cifobflag=="FOB") {
    document.getElementById('fob').checked = true;
}
}

$('.shippingPageLatam').click(
    function(e) {
        loadingCircleShow("show"); 
        window.location.href = ACC.config.contextPath+ '/cart/shipping';
        
});

$('.shippingPageLatamFreight').click(function(e) {
e.preventDefault();
const selectedValue = document.querySelector('input[name="shipping"]:checked').value;
const dataObj = new Object();
dataObj.freightType = selectedValue;
jQuery.ajax({
    type: 'POST',
    url: ACC.config.contextPath + '/cart/shipping',
    async: false,
    data: dataObj,
    success: function (_data) {
        window.location.href = ACC.config.contextPath+ '/cart/shipping';
    },
    error: function (_err) {
        
    }
});
});

$(document).ready(function(){
	const urlparam= window.location.href;
    const ORDER_CONFIRMATION_INDEX = 2;

    function getOrderConfirmation(url) {
    const parts = url.split('/');
    return parts[parts.length - ORDER_CONFIRMATION_INDEX];
    }
    const pagename = getOrderConfirmation(urlparam);
	if(pagename=="orderConfirmation" && confirmationpagefreighttype=="FOB") { 
	$("#freight-fee-details").modal('show');
	}else{
    $("#freight-fee-details").modal('hide'); 	
	}
    });

    $('#freight-fee-details .clsBtn').click(function(){
	$('#freight-fee-details').modal('hide');  
	$('.modal-backdrop').css('display', 'none');
	});


$(".placeOrderBtnLatam").click(function(e){
    loadingCircleShow("show");
    var val=$('#termsOfSales').prop("checked");
    //Either TOC is checked or it is not available/Valid for User Submit the form
	if(val === undefined || val)
	{
		$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity
		$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
		$("#cartCheckoutForm").submit();
	}
	//If TOC is valid and not checked show Error message and stay on checkout page
	else
	{
		$('.termsOfSalesError').show();
	}
});


$( document ).ready(function() {
    disableOnload();
    singleSelectFunc();
    selectAllFunc();

});

function disableOnload() {
    $("#datatab-desktop .orderCodeData > p > input").each(function() {
        $(this).prop("checked", false);
        $(this).attr("disabled", true);
        $("#substituteAdd").attr("disabled", true);
    });
}

function singleSelectFunc() {
    $("#datatab-desktop tbody > tr > td input[type='checkbox']").change(function() {
        var checkedBoxCount = $("tbody input[type=checkbox]:checked").length;
        if(checkedBoxCount === 0){
            if($("#headercheck_").prop("checked")){
                $("#headercheck_").prop("checked", false);
            }
        }
        var count = 0;
        $("#jnjLaProductReplacementForm table .substituteTr").each(function() {
            if ($(this).find("td .singleSelect").prop('checked')) {
                count = count + 1;
            } else {
                count = count - 1;
            }
        });
        if (count >= 0) {
            $("#substituteAdd").attr("disabled", false);
        } else {
            $("#substituteAdd").attr("disabled", true);
        }
        if ($(this).prop("checked")) {
            $(this).parent().parent().find(".orderCodeData  p  input").each(function() {
                $(this).attr("disabled", false);
            });
            $(this).parent().parent().find(".orderCodeData  p  input").first().prop("checked", true);
        } else {
            $(this).parent().parent().find(".orderCodeData  p  input").each(function() {
                $(this).prop("checked", false);
                $(this).attr("disabled", true);
            });
        }
    });
}

function selectAllFunc() {
    $("#datatab-desktop thead th .noWrapLabel input").change(function() {
        if ($(this).prop("checked")) {
            $("#datatab-desktop  tbody tr .singleSelect").each(function() {
                if (!$(this).is(":checked")) {
                    setTimeout(() => {
                        $(this).trigger("click");
                    }, "500");
                }
            });
        }
        if (!$(this).prop("checked")) {
            $("#datatab-desktop  tbody tr .singleSelect").each(function() {
                if ($(this).is(":checked")) {
                    $(this).trigger("click");
                }
            });
        }
    });
}

$(function() {
	$("#substituteAdd").click(function() {
	    var replacementProductCode;
		$('.substituteTr').each(function() {
            if ($(this).find('.singleSelect').prop("checked")) {
                $(this).find('.orderCodeData p').each(function() {
                    var radioChecked = $(this).find('input:radio:checked');
                    if (radioChecked.length) {
                        replacementProductCode = $(this).find('input:radio:checked').val();
                    }
                    $(this).next().val(replacementProductCode);
                });
            } else{
            	var replaceProdIsOriginalProd = $(this).find(".productCode").html();
                $(this).find("#replacementProductCode").val(replaceProdIsOriginalProd);                
            }
        });
		$("#jnjLaProductReplacementForm").submit();
	});
});
