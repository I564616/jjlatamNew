ACC.cartPage = {

	bindAll: function() {
		ACC.cartPage.validateProposedItem();
	},
	validateProposedItem: function()
	{
		$(".placeOrderBtnConsignment").click(function(e) {
			ACC.cartPage.confirmDialog();
		});
		
		$('#placeOrderPopuppage .clsBtn').click(function(){
			loadingCircleShow("hide");  // pop up close event
		});
		
	},
	simulateOrderFirstSAPCallMethod : function(){
		var ajaxurl = ACC.config.contextPath+"/cart/simulateOrderFirstSAPCall?CSRFToken="+ACC.config.CSRFToken;
		//var ajaxurl = ACC.config.contextPath+"/cart/validate";  
		var dataToSend = JSON.stringify({isfirstSAPCall:true,isSecondSAPCall:false,isRefreshCall:false});
		console.log("dataToSend : "+dataToSend);
		$.ajax({
		  type: "POST",
		  async: false,
		  url: ajaxurl,
		  headers: {
				'Content-Type': 'application/json'
			},
		  data: dataToSend, 
		  success: function(data) {
			 $('#proposed-line-item-holder').html(data);
			 $('#proposedItemOrder-popup').removeClass("hidden");
			 $('#proposedItemOrder-popup').modal('hide');
			 if($('.proposedListData tr').children().length==0){
				 $('#proposedItemOrder-popup').modal('hide');
				  $('#proposedItemOrder-popup').addClass("hidden");
				 console.log("no record found so closeORCancel invoking");
				 ACC.cartPage.closeORCancel();
			 }
			 else
			 {
				 $('#proposed-line-item-holder').show();
			 }
			 
			 $('.clsBtn').click(function(){
				 $('.modal-backdrop').hide();
			 });
					 
			 $("input.proposedItemSelection").each(function(){
				$(this).prop('checked',false);
			 });
			 
			 $('.select-accnt-close').click(function(){
				 console.log("select-accnt-close clicked closeORCancel invoking");
				 ACC.cartPage.closeORCancel();
			 });
			 $('#proposedItemOrder-popup').modal({backdrop: 'static',keyboard: false,show:true});
			 	
			 //here need to handle the select all check based on order history page select all checkbox
			 $("#proposedItemSelectAll").change(function(){
				var id;
				if($(this).prop("checked")) {
					/** AAOL - 6368 **/
					$('.propProd-radio').removeAttr('disabled');
					$("input.proposedItemSelection").each(function(){
						$(this).prop('checked',true);
						id = $(this).attr("data-name");
						//$("#proposedItemQty_"+id).attr("disabled", false);
					});
				} else {
					$('.propProd-radio').prop('disabled',true);
					
					$("input.proposedItemSelection").each(function(){
						$(this).prop('checked',false);
						id = $(this).attr("data-name");
						$("#proposedItemQty_"+id).attr("disabled", true);
					});
				}
			 });
				/** AAOL - 6368 **/		 
			 /** Single account select check-box click action **/
			 var rowIndex;
			 $(".proposedItemSelection").change(function(){
				console.log(" proposedItemSelection check box selected");
				var id;
        /** AAOL - 6368 **/
				rowIndex=$('.proposedItemSelection').index(this)+1;
				$("#proposedItemSelectAll").prop('checked',true);
				
				//alert($('.propProd-radio[name="row'+rowIndex+'"]').prop('disabled')+" >> "+rowIndex);
				/** AAOL - 6368 **/
				if($(this).prop('checked')==true){
					$('.propProd-radio[name="row-'+rowIndex+'"]').each(function(){$(this).removeAttr('disabled');});
					
				}else{
					$('.propProd-radio[name="row-'+rowIndex+'"]').each(function(){$(this).prop('checked',false).prop('disabled',true);});
					$('.prod-number[data-prodnumber="prodnumber-'+rowIndex+'"]').each(function(){$(this).val(1).prop('disabled',true) });
					
				}
				
				$(".proposedItemSelection").each(function(){
					/** AAOL - 6368 **/
					id = $(this).attr("data-name");
					if($(this).prop('checked')!=true){
						$("#proposedItemSelectAll").prop('checked',false);
            /** AAOL - 6368 **/
						//$("#proposedItemQty_"+id).attr("disabled", true);
						//$('.propProd-radio[name="row'+rowIndex+'"]').each(function(){$(this).prop('checked',false).prop('disabled',true);});
					}else{
          /** AAOL - 6368 **/
						//$("#proposedItemQty_"+id).attr("disabled", false);
						//$('.propProd-radio[name="row'+rowIndex+'"]').each(function(){alert('check');$(this).removeAttr('disabled').prop('disabled',false);});
					}
				});
			 });
			 /** AAOL - 6368 **/
			 var propRadioIndex;
			 var radioRowIndex;
			 $('.propProd-radio').click(function(){
				 propRadioIndex=$('.propProd-radio').index(this);
				 radioRowIndex=$(this).attr('name').split('-')[1];
				 
				 $('.prod-number[data-prodnumber="prodnumber-'+radioRowIndex+'"]').each(function(){
					$(this).prop('disabled',true) 
				 });
				 
				if($(this).prop('checked')==true){
					$('.prod-number:eq('+propRadioIndex+')').removeAttr('disabled');
				}
			 });
			/* $(".proposedItemSelection").change(function(){
				console.log(" proposedItemSelection check box selected");
				var id;
				$("#proposedItemSelectAll").prop('checked',true);
				$(".proposedItemSelection").each(function(){
					id = $(this).attr("data-name");
					if($(this).prop('checked')!=true){
						$("#proposedItemSelectAll").prop('checked',false);
						$("#proposedItemQty_"+id).attr("disabled", true);
					}else{
						$("#proposedItemQty_"+id).attr("disabled", false);
					}
				});
			 });*/
			  ACC.cartPage.submitSelectedProposed();
			 },
			  onClosed: function () {
		    },
		  dataType: 'html',
		  async: false
		});
	},
	submitSelectedProposed : function(){
		$("#submit-ProposedProd").click(function() {
			var accSelLength = $(".proposedItemSelection:checked").length;
			/** AAOL - 6368 **/
			//alert($(".proposedItemSelection:checked").length+ " >> "+$(".propProd-radio:checked").length);
			if($(".proposedItemSelection:checked").length>0 && $(".propProd-radio:checked").length==0 || $(".proposedItemSelection:checked").length==0){
				$('#obsolote-popup-error').show();
				return false;
			}
			else{
				$('#obsolote-popup-error').hide();
				$('#proposedItemOrder-popup').modal('hide');
				
			}
			ACC.cartPage.sendSelectedProducts(accSelLength);
		});
	},
	sendSelectedProducts : function(accSelLength){
		var propProdId_Qtys,removeProdId_Qtys=undefined;
		var orderItemQty,propItemQty,oriPropItemQty,idx=0;
		var hyLineItemNo,orderItemNo;
		//var removeProdCodes = [];
		var jnjAddToCartCartFormList = [];
		var ajaxResponse = [];
		/*if (accSelLength === 0){
			$('#select-accnt-error-order-history').css('display','block');
			return false;
		}*/
		/** AAOL - 6368 **/
		var proposeItemRowIndex;
		$(".proposedItemSelection").each(function(){
			var propItemNo=[];
			//var propProdId_Qtys;
			id = $(this).attr("data-name"); // using for popup index
			console.log("id : "+id);
			proposeItemRowIndex=$(".proposedItemSelection").index(this)+1;
			if($(this).prop('checked') === true){
				var jnjAddToCartCartForm = new Object();	
				hyLineItemNo = id; // this is using for hybris line litem no
				orderItemNo = $("#orderItemCd_"+id).text();
				orderItemQty = $("#orderItemQty_"+id).text();
        /** AAOL - 6368 **/
				$('.propProd-radio[name="row-'+proposeItemRowIndex+'"]').each(function(i){
					if($(this).prop('checked')){
						propItemNo[0]=$(this).val();
					}
				});
				
				$('.prod-number[data-prodnumber="prodnumber-'+proposeItemRowIndex+'"]').each(function(){
					if(!$(this).prop('disabled')){
						propItemQty = $(this).val();
					}
					
				});
				oriPropItemQty = $("#origProposItemQty_"+id).val();
				console.log("orderItemNo : "+orderItemNo +" and orderItemQty : "+ orderItemQty +" and propItemNo : "+ propItemNo +" and propItemQty : "+ propItemQty + " and oriPropItemQty : "+oriPropItemQty);
				
				if(parseInt(orderItemQty) === parseInt(oriPropItemQty)){
					// need to clear the cart because this is BackOrder product so yet to update proposed product based on user choose
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(removeProdId_Qtys)){
						removeProdId_Qtys = removeProdId_Qtys+","+hyLineItemNo+":"+ "0";  // in controller using hyLineItemNo only updating the QTY not in product id
					}else{
						removeProdId_Qtys = hyLineItemNo+":"+ "0";
					}
				}else{
					// need to update the original item qty which is available in SAP-original requested
					var updateOrderItemQty = parseInt(orderItemQty) - parseInt(oriPropItemQty); 
					  // update the available qty which is received from the SAP. user requested qty-Sap propo qty
					if( parseInt(updateOrderItemQty) >0){ // if proposed qty is greater than original Qty then  check is gt 0 then add to cart
						if(ACC.formCheckFunc.isNotNullAndNotEmpty(propProdId_Qtys)){
							removeProdId_Qtys = removeProdId_Qtys+","+hyLineItemNo+":"+ updateOrderItemQty;
						}else{
							removeProdId_Qtys = hyLineItemNo+":"+ updateOrderItemQty;
						}
					}
				}
				if( parseInt(propItemQty) >0){ // if proposed qty is greater than 0 then add to cart
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(propProdId_Qtys) ){
						propProdId_Qtys = propProdId_Qtys+","+propItemNo+":"+ propItemQty;
						jnjAddToCartCartForm.hybrisLineItemNo= hyLineItemNo;
						jnjAddToCartCartForm.proposedItemNo= propItemNo;
						jnjAddToCartCartForm.originalItemNo= orderItemNo;
					}else{
						propProdId_Qtys = propItemNo+":"+ propItemQty;
						jnjAddToCartCartForm.hybrisLineItemNo= hyLineItemNo;
						jnjAddToCartCartForm.proposedItemNo= propItemNo;
						jnjAddToCartCartForm.originalItemNo= orderItemNo;
					}
				}
				//collecting for proposed and original item list to update in product level
				jnjAddToCartCartFormList.push(jnjAddToCartCartForm); 
			} 
			idx = idx+1;
		});
		 
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(propProdId_Qtys)){
			ajaxResponse  = ACC.cartPage.submitProposedProductCart(propProdId_Qtys); // calling the submitProductCart method its available in acc.addToCartHome.js
		}
		
		/*
		 * reset the line item no to make update based on the new entry
		 */
		ACC.cartPage.resetEntryNo(jnjAddToCartCartFormList,ajaxResponse );
		
		/*
		 * calling remove backorder product from cart
		 */
		ACC.cartPage.removeBackorderProduct(removeProdId_Qtys);
		
		/*
		 * calling to update proposed and original item no in product level
		 */
		ACC.cartPage.updateProductLineItem(jnjAddToCartCartFormList);
		
		/*
		 * invoking SAP call 2nd time to send about the user action
		 */
		ACC.cartPage.simulateOrderSecondSAPCallMethod(propProdId_Qtys);
		
	},
	submitProposedProductCart : function(propProdId_Qtys){
		console.log("step0 submitProposedProductCart : "+propProdId_Qtys );
		var dataObj = new Object();
		var userList = [];
		dataObj.source = "addToCartForm_1";
		dataObj.prodId_Qtys = propProdId_Qtys;
		jQuery.ajax({
			type: "POST",
			url:  ACC.config.contextPath +'/home/addTocart' ,
			data: dataObj,
			async: false,
			success: function (data) {
				$.each(data.cartModifications,function(i,data){
					 $.each(this, function(key,value) {
						 if(key == 'entry' ){ 
							 userList.push({"entryNo":value.entryNumber,"productCode":value.product.code});	
						 }
					 });
				});
			}
		});
		return userList;
	},
	removeBackorderProduct : function(removeProdId_Qtys){
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(removeProdId_Qtys)){
			console.log("step 1 removeBackorderProduct : "+removeProdId_Qtys );
			jQuery.ajax({
				url : ACC.config.contextPath + '/cart/removeCartItem?entryQuantityList='+removeProdId_Qtys,
				async: false,
				success : function(data) {
					 console.log("removeBackorderProduct data : "+ data);
				}
			});
		}
	},
	updateProductLineItem : function(jnjAddToCartCartFormList){
		var dataToSend = JSON.stringify({jnjAddToCartCartFormList:jnjAddToCartCartFormList});
		console.log('step 2 updateProductLineItem  send af stringify : '+ dataToSend);
		var url = ACC.config.contextPath+"/cart/updateProductLineItem?CSRFToken="+ACC.config.CSRFToken;
		jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false,
			success : function(responseData) {
				var responseDataJson = JSON.stringify(responseData);
				console.log("responseDataJson : "+ responseDataJson);
			}
		});
	},
	 updateConsignmentItem : function(){
		var poNO = $("#customerPONo").val();
		var poDate = $("#poDate").val();
		var endUser = $("#endUser").val();
		var stockUser = $("#stockUser").val();
		var requestDelDate = $("#requestDelDate").val();
		var  comment=  $("#comment").val();
		var dataToSend = JSON.stringify({customerPONo:poNO,poDate:poDate,requestDelDate:requestDelDate,endUser:endUser,stockUser:stockUser,comment:comment});
		console.log('updateConsignmentItem  send af stringify : '+ dataToSend);
		var url = ACC.config.contextPath+"/cart/consigmentFillUpAddToCartUrl?CSRFToken="+ACC.config.CSRFToken;
		jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false
		}).done(function(responseData) { 
			var responseDataJson = JSON.stringify(responseData);
			ACC.cartPage.simulateOrderFirstSAPCallMethod(); // check the proposed item is available
		});
	},
	 updateConsignmentReturnItem : function(){
		var consignmentReturnForm = new Object();	
		var poNO = $("#customerPONo").val();
		var poDate = $("#poDate").val();
		var returnCreatedDate = $("#returnCreatedDate").val();
		var endUser = $("#endUser").val();
		var stockUser = $("#stockUser").val();
		var  comment=  $("#comment").val();
		var dataToSend = JSON.stringify({customerPONo:poNO,poDate:poDate,returnCreatedDate:returnCreatedDate,endUser:endUser,stockUser:stockUser,comment:comment});
		console.log('updateConsignmentReturnItem  send af stringify : '+ dataToSend);
		
		var url = ACC.config.contextPath+"/cart/consigmentReturnAddToCartUrl?CSRFToken="+ACC.config.CSRFToken;
		jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false
		}).done(function(responseData) { 
			var responseDataJson = JSON.stringify(responseData);
			/*ACC.cartPage.simulateOrderFirstSAPCallMethod();*/ // check the proposed item is available
			 ACC.cartPage.closeORCancel();
		});
	},
	updateConsignmentChargeItem : function(){
		var batchDetails=[];
		var batchDetail;
		$(".shoppingcartOrderEntryList").each(function() {
			batchDetail = new Object();
			var prodCode=$(this).find('input[name=productCode]').val();
		    var entry=$(this).find('input[name=entryNumber]').val();
		    if($(this).find('.batchNumber').length > 0)
			{
				var batchNumber=$('#bn_'+prodCode+'_'+entry).val();
			}
			else
			{
				var batchNumber=$('#bn_'+entry).val();
			}
			
			if($(this).find('.serialNumber').length > 0)
			{
				var serialNumber=$('#sn_'+prodCode+'_'+entry).val();
			}
			else
			{
				var serialNumber=$('#sn_'+entry).val();
			}
		   
		    if(prodCode!=""){
		    	batchDetail.productCode=prodCode;
		    	batchDetail.entryNumber=entry;
		    	batchDetail.batchNumber=batchNumber;
		    	batchDetail.serialNumber=serialNumber;
		    console.log("entry no>"+entry+", Product code is>"+prodCode+", batchNo.>"+batchNumber+" & selected serial no. is>"+serialNumber);
		    batchDetails.push(batchDetail);
		    }
		});
		
		var consignmentChargeForm = new Object();
		var poNO = $("#customerPONo").val();
		var poDate = $("#poDate").val();
		var requestDelDate = $("#requestDelDate").val();
		var endUser = $("#endUser").val();
		var stockUser = $("#stockUser").val();
		var  comment=  $("#comment").val();
		consignmentChargeForm.customerPONo=poNO;
		consignmentChargeForm.endUser=endUser;
		consignmentChargeForm.stockUser=stockUser;
		consignmentChargeForm.poDate=poDate;
		consignmentChargeForm.requestDelDate=requestDelDate;
		consignmentChargeForm.comment=comment;
		consignmentChargeForm.batchDetails=batchDetails;
		var dataToSend = JSON.stringify(consignmentChargeForm);
		console.log('updateConsignmentChargeItem  send af stringify : '+ dataToSend);
		var url = ACC.config.contextPath+"/cart/consigmentChargeAddToCartUrl?CSRFToken="+ACC.config.CSRFToken;
		jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false
		}).done(function(responseData) { 
			var responseDataJson = JSON.stringify(responseData);
			//ACC.cartPage.simulateOrderFirstSAPCallMethod(); // check the proposed item is available
			 ACC.cartPage.closeORCancel();
		});
	},
	updateBatchAndSerialDetails : function(){
		console.log("called updateBatchAndSerialDetails");
		var batchDetails=[];
		var batchDetail;
		$(".shoppingcartOrderEntryList").each(function() {
			batchDetail = new Object();
			var prodCode=$(this).find('input[name=productCode]').val();
		    var entry=$(this).find('input[name=entryNumber]').val();
			if($(this).find('.batchNumber').length > 0)
			{
				var batchNumber=$('#bn_'+prodCode+'_'+entry).val();
			}
			else
			{
				var batchNumber=$('#bn_'+entry).val();
			}
			
			if($(this).find('.serialNumber').length > 0)
			{
				var serialNumber=$('#sn_'+prodCode+'_'+entry).val();
			}
			else
			{
				var serialNumber=$('#sn_'+entry).val();
			}
		    if(prodCode!=""){
		    	batchDetail.productCode=prodCode;
		    	batchDetail.entryNumber=entry;
		    	batchDetail.batchNumber=batchNumber;
		    	batchDetail.serialNumber=serialNumber;
		    console.log("entry no>"+entry+", Product code is>"+prodCode+", batchNo.>"+batchNumber+" & selected serial no. is>"+serialNumber);
		    batchDetails.push(batchDetail);
		    }
		});
		
		var consignmentChargeForm = new Object();
		var poNO = $("#customerPONo").val();
		var poDate = $("#poDate").val();
		var requestDelDate = $("#requestDelDate").val();
		var endUser = $("#endUser").val();
		var stockUser = $("#stockUser").val();
		var  comment=  $("#comment").val();
		consignmentChargeForm.customerPONo=poNO;
		consignmentChargeForm.endUser=endUser;
		consignmentChargeForm.stockUser=stockUser;
		consignmentChargeForm.poDate=poDate;
		consignmentChargeForm.requestDelDate=requestDelDate;
		consignmentChargeForm.comment=comment;
		consignmentChargeForm.batchDetails=batchDetails;
		var dataToSend = JSON.stringify(consignmentChargeForm);
		console.log('updateBatchAndSerialDetails  send af stringify : '+ dataToSend);
		var url = ACC.config.contextPath+"/cart/consigmentChargeAddToCartUrl?CSRFToken="+ACC.config.CSRFToken;
		jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false
		})
	},
	confirmDialog :function(){
		$('#placeOrderpopup').modal('show');
		$(document).off('click', '#cancel-btn-placeorder').on('click', '#cancel-btn-placeorder',function(e) {
			ACC.cartPage.popupCallback(false);
		});
		 
		$(document).off('click', '#accept-btn-placeorder').on('click', '#accept-btn-placeorder',function(e) {
			ACC.cartPage.popupCallback(true);
		}); 
	},
	popupCallback : function (value) {
		if (value) {
			var val=$('#termsOfSales').prop("checked");
			//Either TOC is checked or it is not available/Valid for USer Subit the form
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
		} else {
			window.location.href = ACC.config.contextPath +'/cart';
		}
	},
	closeORCancel : function(){
		var propProdId_Qtys = undefined;
		ACC.cartPage.simulateOrderSecondSAPCallMethod(propProdId_Qtys);
	},
	simulateOrderSecondSAPCallMethod:function (propProdId_Qtys){
		console.log("step 4 simulateOrderSecondSAPCallMethod method invoked" + propProdId_Qtys);
		var simulateOrderForm = $('#simulateOrderForm');
		var arrayOfStringsLst="";
		var jnjGTCommonFormIODataList = [];
		var jnjGTProposedOrderResData = new Object();
		var sapSecondCallReq ="";
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(propProdId_Qtys)){
			arrayOfStringsLst = propProdId_Qtys.split(",");
		
	    $.each( arrayOfStringsLst, function(i,v) {
	        $.each( v.split(':'), function(index, value) {
	        	var jnjGTCommonFormIOData = new Object(); 
		         if(index === 0 ){
		        	 if(ACC.formCheckFunc.isNotNullAndNotEmpty(sapSecondCallReq)){
		        		 	sapSecondCallReq = sapSecondCallReq+","+value;  // in controller using hyLineItemNo only updating the QTY not in product id
						}else{
							sapSecondCallReq = value;
						}
		        	//or the below
		        	 $('<input>').attr({ type: 'hidden',  id: 'zzsubstMat', name: 'zzsubstMat',  value: value }).appendTo(simulateOrderForm);
		         }
		         if(index === 1 ){
		        	 if(ACC.formCheckFunc.isNotNullAndNotEmpty(sapSecondCallReq)){
		        		 	sapSecondCallReq = sapSecondCallReq+","+value;  // in controller using hyLineItemNo only updating the QTY not in product id
						}else{
							sapSecondCallReq = value;
						}
		        	//or the below
		        	 $('<input>').attr({ type: 'hidden',  id: 'zzsubstQty', name: 'zzsubstQty',  value: value }).appendTo(simulateOrderForm);
		         }
		         
		         if(ACC.formCheckFunc.isNotNullAndNotEmpty(sapSecondCallReq)){
	        		 	sapSecondCallReq = sapSecondCallReq+","+"X";  // in controller using hyLineItemNo only updating the QTY not in product id
					}else{
						sapSecondCallReq = "X";
					}
		       //or the below
		         $('<input>').attr({ type: 'hidden',  id: 'zzFlag', name: 'zzFlag',  value: "X" }).appendTo(simulateOrderForm);
	        });
	        sapSecondCallReq = sapSecondCallReq+"#";
	         
	      });
		}else{
     		 //sapSecondCallReq =  ""+","+""+","+"X";  // in controller using hyLineItemNo only updating the QTY not in product id
			 //or the below
			 $('<input>').attr({ type: 'hidden',  id: 'zzsubstMat', name: 'zzsubstMat',  value: "" }).appendTo(simulateOrderForm);
			 $('<input>').attr({ type: 'hidden',  id: 'zzsubstQty', name: 'zzsubstQty',  value: "" }).appendTo(simulateOrderForm);
			 $('<input>').attr({ type: 'hidden',  id: 'zzFlag', name: 'zzFlag',  value: "X" }).appendTo(simulateOrderForm);
			 
			//jnjGTCommonFormIODataList.push(jnjGTCommonFormIOData); 
		}
	    // jnjGTProposedOrderResData.jnjGTCommonFormIODataList = jnjGTCommonFormIODataList;
	    //var dataToSend = JSON.stringify({jnjGTProposedOrderResData:jnjGTProposedOrderResData});
 
	    $(simulateOrderForm).submit();
	    //need to call validate to redirect the url address
	}, 
	
	TempsimulateOrderSecondSAPCall:function (propProdId_Qtys){
		console.log("step 4 TempsimulateOrderSecondSAPCall method invoked" + propProdId_Qtys);
		var arrayOfStringsLst="";
		var jnjGTCommonFormIODataList = [];
		var jnjGTProposedOrderResData = new Object();
		 
		if(ACC.formCheckFunc.isNotNullAndNotEmpty(propProdId_Qtys)){
			arrayOfStringsLst = propProdId_Qtys.split(",");
		
	    $.each( arrayOfStringsLst, function(i,v) {
	    	var jnjGTCommonFormIOData = new Object();
	        $.each( v.split(':'), function(index, value) {
		         if(index === 0 ){
		        	 jnjGTCommonFormIOData.zzsubstMat = value;
		         }
		         if(index === 1 ){
		        	 jnjGTCommonFormIOData.zzsubstQty = value; 
		         }
		         jnjGTCommonFormIOData.zzFlag = 'X'; 
	        });
	        jnjGTCommonFormIODataList.push(jnjGTCommonFormIOData); 
	      });
		}else{
			var jnjGTCommonFormIOData = new Object();
			jnjGTCommonFormIOData.zzsubstMat = "";
			jnjGTCommonFormIOData.zzsubstQty = ""; 
			jnjGTCommonFormIOData.zzFlag = 'X';
			jnjGTCommonFormIODataList.push(jnjGTCommonFormIOData); 
		}
	    jnjGTProposedOrderResData.jnjGTCommonFormIODataList = jnjGTCommonFormIODataList;
	    
		var dataToSend = JSON.stringify({isfirstSAPCall:false,isSecondSAPCall:true,isRefreshCall:false,jnjGTProposedOrderResData:jnjGTProposedOrderResData});
		console.log('simulateOrderSecondSAPCallMethod  send af stringify : '+ dataToSend);
		
		// var url = ACC.config.contextPath+"/cart/simulateOrderSecondSAPCall";
		// var url = ACC.config.contextPath+"/cart/validate"; 
		/*jQuery.ajax({
			type: "POST",
			headers: {
				'Content-Type': 'application/json'
			},
			url: url, 
			data: dataToSend,
			async: false,
			success : function(responseData) {
				var responseDataJson = JSON.stringify(responseData);
				console.log("responseDataJson completed SAP 2 call ");
				window.location.href = ACC.config.contextPath+ '/cart/validate';
			}
		});*/ 
	},
	resetEntryNo : function(jnjAddToCartCartFormList,ajaxResponse ){
		console.log("before jnjAddToCartCartFormList :  " + JSON.stringify(jnjAddToCartCartFormList));
		$.each(ajaxResponse,function(i,data){
			$.each(jnjAddToCartCartFormList,function(j,obj){
				if(data.productCode === obj.proposedItemNo){
					obj.hybrisLineItemNo = data.entryNo;
				}
			}); 
		}); 
		console.log("after jnjAddToCartCartFormList :  " + JSON.stringify(jnjAddToCartCartFormList));
	}

};

jQuery(document).ready(function() {
	ACC.cartPage.bindAll();
	var inReturnOrderPage = false;
	var currViewScreenName = $("#currViewScreenName").val();
	/*$(document).on("click",'#surgeonRadio3',function(e){
		$(".surge-inputbox").attr("readonly",false);
	});
	
	$(document).on("click",'#surgeonRadio',function(e){
		$(".surge-inputbox").attr("readonly",true);
	});
	
	var listVal;	
	$(document).on("click",'.surgeonaccountList',function(e){
		e.preventDefault();		
		listVal = $(this).find('.surgeonNameOnly').text().toString();	
		listVal = listVal.toString();
		$("#surgeonIdInput").val(listVal);
	});*/
	/*$(document).on("click",'.submitSurgeonOk',function(e){
		var unAvail = "UNAVAILABLE";
		if($("#surgeonRadio2").prop("checked")){			
			$("#surgeonName").val(unAvail);			
		}
		if($("#surgeonRadio3").prop("checked")){
			$("#surgeonName").val($("#surgeonIdInputradio3").val());
		}		
		if($("#surgeonRadio").prop("checked")){
			$("#surgeonName").val(listVal);	
		}
	});
	*/
	
	
	$('#priceInquiry-nodata-error').hide();	
	
	//refreshSapPrices();
	 $('#selectaddresspopup').on('shown.bs.modal',function(){
		 $('#selectaddresspopup .odd-row').each(function(){
             $(this).removeClass('addressSelected');
		 });
		 $('#selectaddresspopup .even-row').each(function(){
             $(this).removeClass('addressSelected');	                        
		 });
	 });
	 $('#selectaddresspopupBillTO').on('shown.bs.modal',function(){
		 $('#selectaddresspopupBillTO .odd-row').each(function(){
             $(this).removeClass('addressSelected');
		 });
		 $('#selectaddresspopupBillTO .even-row').each(function(){
             $(this).removeClass('addressSelected');	                        
		 });
		 $('#selectbillingAddressErr').hide();
	 });
	 
	 $('#selectaddresspopup .odd-row,#selectaddresspopup .even-row').on('click',function(){
		   
	        $('#selectaddresspopup .odd-row').each(function(){
	                        $(this).removeClass('addressSelected');
	        });
	        $('#selectaddresspopup .even-row').each(function(){
	                        $(this).removeClass('addressSelected');
	                       	                        
	        });
	      
	        $(this).addClass('addressSelected');
	});
	 
	 $('#selectaddresspopupBillTO .odd-row,#selectaddresspopupBillTO .even-row').on('click',function(){
		   
	        $('#selectaddresspopupBillTO .odd-row').each(function(){
	                        $(this).removeClass('addressSelected');
	        });
	        $('#selectaddresspopupBillTO .even-row').each(function(){
	                        $(this).removeClass('addressSelected');
	                       	                        
	        });
	      
	        $(this).addClass('addressSelected');
	});
	 
	 $( "#close-btn-address" ).click(function () {
		  $('#selectAdressErr').hide(); 
		  $('#selectbillingAddressErr').hide();
	 })
	 
	 
	 $( "#submitChangeAddress" ).unbind( "click"); 
		   	   
		 $('#submitChangeAddress').click(function () {
		var id_Address = $("#selectaddresspopup .addressSelected div.list-group-item-text input.shipp").val();
		 if(id_Address!=undefined){
			 $('#selectAdressErr').hide();
			 var input = document.createElement('input');
			    input.type = 'hidden';
			    input.name = 'shippingAddress';
			    input.value = id_Address;
			    $('#changeAddForm').append(input);
			    $("#shippingAddress1").val('');
	     		$('#changeAddForm').submit();
		 }
		 else{
			 	$('#selectAdressErr').show();
		 }
		 	

			});	 
		 
		 
		 
		 $('#submitChangeAddressForBilling').click(function () {
				var id_Address = $("#selectaddresspopupBillTO .addressSelected div.list-group-item-text input.shipp").val();
				 if(id_Address!=undefined){
					 $('#selectbillingAddressErr').hide();
					 var input = document.createElement('input');
					    input.type = 'hidden';
					    input.name = 'billingAddress';
					    input.value = id_Address;
					    $('#changeBillAddForm').append(input);
					    $("#billingAddress1").val('');
			            $('#changeBillAddForm').submit();
				 }
				 else{
					 	$('#selectbillingAddressErr').show();
				 }
				 	

					});	
		 
		 /*change order type start*/
		  $("#changeOrderType").on('change',function(){
			  
			 
			  
			    var orderType=$(this).val();
			    var input = document.createElement('input');
			    input.type = 'hidden';
			    input.name = 'orderType';
			    input.value = orderType;
			    $('#changeOrderTypeForm').append(input);
			    $("#orderType").val(orderType);
	     		$('#changeOrderTypeForm').submit();
		 });
		 
		  
		 /*change order type end*/
		 
	/*	International oder Requested Delivery Date start*/
		  
		  var reqDeldate = new Date();
		  reqDeldate.setDate(reqDeldate.getDate() + 10);
		  
		  
		 $('#requestDeliveryDate').datepicker('setDate', reqDeldate); 
		  
		  /*Requested Delivery Date end*/
		 
		 
		 
	
	/** Display Batch mode alert in case Order-Simulation fails */
	if($("#displayBatchModeAlert").val()=='true'){
		batchModePopUp(true);
	}
	
	 if($("#errorInvoiceMessage").val()=='true'){
	invoicePDFPopUp(true);
       }
	 
	// Make This address Default code Start	for shipping
	 	if($("#makeThisAddrDefaultChk").val()=='true'){
		 $("#shippingAddrDefaultChk").prop('checked',true);
		 }else{
		 $("#shippingAddrDefaultChk").prop('checked',false);
		 } 
	 	 $( "#shippingAddrDefaultChk" ).unbind( "click"); 
		$("#shippingAddrDefaultChk").click(function(e){  
			     var checkStatus=$("#shippingAddrDefaultChk").prop('checked'); 
				 var id_defaultChekAdd = $("#defaultChekAddid").val();  
				    var dataObj = new Object();	 
				    dataObj.shippingAddressId = id_defaultChekAdd; 
				    dataObj.checBoxStatus = checkStatus; 
				   					$.ajax({
				   					type : "POST",
				   					url : ACC.config.contextPath+ '/cart/updateShipToAddressIdMap',
				   					data : dataObj,
				   					success : function(data) {  
				   					}
				   				});

				 
		});
		// Make This shipping  address Default code End   
		
		// Make This billing  address Default code start  
		if($("#makeThisAddrDefaultChangeChkForBilling").val()=='true'){
			 $("#billingAddrDefaultChk").prop('checked',true);
			 }else{
			 $("#billingAddrDefaultChk").prop('checked',false);
			 } 
		 	 $( "#billingAddrDefaultChk" ).unbind( "click"); 
			$("#billingAddrDefaultChk").click(function(e){  
				     var checkStatus=$("#billingAddrDefaultChk").prop('checked'); 
					 var id_defaultChekAdd = $("#defaultCheckforBillingAddid").val();  
					    var dataObj = new Object();	 
					    dataObj.billingAddressId = id_defaultChekAdd; 
					    dataObj.checBoxStatus = checkStatus; 
					   					$.ajax({
					   					type : "POST",
					   					url : ACC.config.contextPath+ '/cart/updateBillToAddressIdMap',
					   					data : dataObj,
					   					success : function(data) {  
					   					}
					   				});

					 
			});
			// Make This billing  address Default code End   
 //Adding for JJEPIC-720
	 
			 $("#UploadIt").click(function(e){
				 e.preventDefault();
				$('#uploadDeliveredOrder').modal('show');
				 $(".invalidFileError").hide();
				 $(".emptyFileError").hide();
				 
			 });
	  // If shipping address not more then one 
	  //AAOL-5386 
	  if($("#shippingAddressCounts").val()<2 || $("#shippingAddressCounts").val()==null || $("#shippingAddressCounts").val()==""){  
		  $("#shippingAddrDefaultChkDiv").hide();
	   }
	  
	  if($("#billingAddressCounts").val()<2 || $("#billingAddressCounts").val()==null || $("#billingAddressCounts").val()==""){ 
		  $("#billingAddrDefaultChkDiv").hide();
	   }
	   
	    if ($("#uploadBrowseFile").val() == "") {
			$("#removeUploadForm").hide();
		} else {
			$("#uploadBrowseFile").css("width", "auto");
			$(".invalidFileError").hide();
			$("#removeUploadForm").show();
		}

		if ($("#attachDocName").val() != "") {
			$("#uploadBrowseFile").css("width", "85px");
			$(".invalidFileError").hide();
			$("#removeUploadForm").show();

		}
		
	

		$("#uploadBrowseFile").change(function() {
			if ($("#uploadBrowseFile").val() == "") {
				$("#removeUploadForm").hide();
			} else {
				$("#uploadBrowseFile").css("width", "auto");
				$(".emptyFileError").hide();
				$("#removeUploadForm").show();
				$("#attachDocNameSpan").hide();

			}
		});
		
		$("#uploadFileCancel").click(function() {
			if ($("#uploadBrowseFile").val() != "") {
				$("#removeUploadForm").hide();
				$("#submitDelivedOrderFileForm").reset();
			} else if ($("#attachDocName").val() != "") {
				$("#uploadBrowseFile").css("width", "85px");
				$("#attachDocNameSpan").show();
				$(".invalidFileError").hide();
				$("#removeUploadForm").show();
			}
		});
		
		
		
		$("#removeDoc").click(function() {
			if ($("#uploadBrowseFile").val() != "") {
				$("#submitDelivedOrderFileForm").reset();
				$("#removeUploadForm").hide();
			} else {
				$("#uploadBrowseFile").css("width", "auto");
				$("#attachDocNameSpan").hide();
				$("#removeUploadForm").hide();
			}
	 //Added JJEPIC-720 Ends
});
		
		var idx = 0;
		$(".shoppingcartOrderEntryList").each( function() {
				var myContractRowId = $(this).attr('id'); 
				//$("#"+myContractRowId).find("p .contractNumber").html();
				var contractNumber = $(this).find("p .contractNumberText").html();
					
				if(contractNumber !== undefined && contractNumber !== null && contractNumber ){
					idx = parseInt(idx)+1;
				} 
				if(idx > 0){  // to enable the contract no in top of the page if atleast one contract product
					$(".contract-product-show").show(); 
					$("#contract_product_msg").text(contractNumber);
				}
			});
		
		
		var idx = 0;
		$(".shoppingcartOrderEntryList").each( function() {
				var myContractRowId = $(this).attr('id'); 
				//$("#"+myContractRowId).find("p .contractNumber").html();
				var contractNumber = $(this).find("p .contractNumberText").html();
					
				if(contractNumber !== undefined && contractNumber !== null && contractNumber ){
					idx = parseInt(idx)+1;
				} 
				if(idx > 0){  // to enable the contract no in top of the page if atleast one contract product
					$(".contract-product-show").show(); 
					$("#contract_product_msg").text(contractNumber);
				}
			});
		
		
		
		/* Return Order Validations*/
		if($('.reasonCodeReturnSelect').val()=='R07' || $('.reasonCodeReturnSelect').val()=='R29')
			
		{
		$("#invoiceStar").css("display","none");
		}
		else
			{
			
				$("#invoiceStar").css("display","inline");
				
			}		
		
	
		
});

if($("#distPurExpOrderDate").length!=0){
	
	var maxDateForDeliveryDate;
	var todaysDate = new Date();
	var futureDate = new Date();
	futureDate.setDate(todaysDate.getDate()+2);
	
	if (futureDate.getDay() == 0) {
		maxDateForDeliveryDate = "3d";
	} else if (futureDate.getDay() == 6) {
		maxDateForDeliveryDate = "4d";
	} else {
		maxDateForDeliveryDate = "2d";
	}

	$("#distPurExpOrderDate").datepicker({
		minDate: maxDateForDeliveryDate,
	    inline: true,
	    beforeShowDay: $.datepicker.noWeekends,
	});
	
	/** JIRA 362 : Below code sets the date to 10 days from current date if the saved date turns out before 10 days range **/ 
	futureDate.setHours(0,0,0,0);
	if(Date.parse($("#distPurExpOrderDate").val()) < futureDate) {
		$("#distPurExpOrderDate").datepicker("setDate", futureDate);
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/updateDeliveryDateForCart?expDeliveryDate='+$("#distPurExpOrderDate").val(),
			async: false,
			success : function(data) {	}
		});
	}
}



$('.cartStep1Saveupdate').click(function(e) {
			$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
			$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
			var surgeryData = $("#surgeryData").val();
			var surgeryMand = false;
			var splStockPartnerRequired = false;
			if(surgeryData != undefined){
				// means surgery info is mandatory
				if(surgeryData == ""){
					$("#surgeryInfoError").show();
					surgeryMand = true;
				}
			}
			
			if($("#dropShip").val() != null && $("#dropShip").val().length!=0)
			{
				$("#stndDistPurOrder")[0].setAttribute("required", "true");
			}
			
			var lotNoMandatory = false;
			if($(".orderEntry").length != 0){
			    $('.orderEntry').each(function() {  
			        if(!jQuery(this).valid()){
			        	lotNoMandatory = true;
			        }
			    });				
			}
			if($(".splStockPartnerForm").length != 0){
			    $('.splStockPartnerForm').each(function() {	    	
			    	
			    	var formIdArray= $(this).attr('id').split("_");
			    	var entryNumber=formIdArray[1];
			    	//Remove existing errors
			    	$("#"+entryNumber+"_Error").remove();
			    	
			        if(!jQuery(this).valid()){
			        	splStockPartnerRequired = true;
			        }
			    });				
			}
						
			if (!jQuery("#cartStep1Check").valid() || lotNoMandatory || surgeryMand || splStockPartnerRequired) {
				$("#ajaxCallOverlay").hide(); // overlay must fade out
				$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
				$('#myprofileedit .success').hide();
			}
			else{


														  /*GTR-1693*  Commented and added new else block completely/

										/* If any error occurs then keep this commented code inside else block till {search:Till 'A'}
										var canValidate = $("#canValidateCart").val() ;
										if(canValidate == 'false'){
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											batchModePopUp(false);
										}else{ //Cart can be validate
											var poNum=$("#purchOrder").val();
											jQuery.ajax({
												global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
												url : ACC.config.contextPath + '/cart/checkPO?poNumber='
														+ encodeURIComponent(poNum),
												success : function(data) {
													if (data) {
														$("#ajaxCallOverlay").hide(); // overlay must fade out
														$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
														showValilatePONumAlert(e)
													} else {
														window.location.href = ACC.config.contextPath
																+ '/cart/validate';
													}
												}
											});
										}//close of else
										*/





			
					var canValidate = $("#canValidateCart").val() ;
				if(canValidate == 'false')
					{
					 
					checkForPOAlert(e);		
					//** 
				
					/*$("#ajaxCallOverlay").hide(); // overlay must fade out
					$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
					batchModePopUp(false);*/
				}else{ //Cart can be validate
					
						
					var poNum=$("#purchOrder").val();
					jQuery.ajax({
						global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
						url : ACC.config.contextPath + '/cart/checkPOValid?poNumber='
								+ encodeURIComponent(poNum),
						success : function(data) {
							if (data) {
								 
								$("#ajaxCallOverlay").hide(); // overlay must fade out
								$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
								showValilatePOOrderNumAlert(e);
								
							} else { 
							
								
								
								/*//Added for GTR-1693
*/								jQuery.ajax({
									global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
									url : ACC.config.contextPath + '/cart/checkPO?poNumber='
											+ $("#purchOrder").val(),
									success : function(data) 
									{							
										if (data) 
										{ 
										
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											showValilatePONumAlert(e);
										} 
										else 
										{   
											
											window.location.href = ACC.config.contextPath+ '/cart/validate';
										}
									}
								});
							}
							
							//Added
								/*window.location.href = ACC.config.contextPath
										+ '/cart/validate';
							}*/
						}
					});
				}//close of else
			}//Till 'A'
		});

$(".validateprice").click(function(e) {
	
		if($('.price-inquiry-table').find('td:first').hasClass('dataTables_empty')){
			$('#priceInquiry-nodata-error').show();
		}
		else
			{
	window.location.href = ACC.config.contextPath+ '/cart/requestQuote';
			}
});
$('.cartvalidateInternational').click(
		function(e) {
			/*if($("#date-picker-1").val() == "" || $("#date-picker-1").val() == undefined){
				$("#reqDateError").html("<label class='error' for='toDate'>Please select a date</label>");
				return false;
			}else{*/
			window.location.href = ACC.config.contextPath+ '/cart/validate';
			/*}*/
			
		});

//for replishment cart validate
$('.replenishValidateSaveupdate').click( function(e) {
	if($("#replenishValidateCartForm").valid()){
		window.location.href = ACC.config.contextPath+ '/cart/validate';	
	}
	
});

//for replishment cart validate
$('.returnCartupdate').click( function(e) {
		window.location.href = ACC.config.contextPath+'/cart/consigmentReturnAddToCartUrl';	
		$("#ConsignmentReturnForm").submit();
})

$('.cartStep1Saveupdate1').click(function(e) {
	ACC.cartPage.simulateOrderFirstSAPCallMethod();
	//window.location.href = ACC.config.contextPath+ '/cart/validate';	
});

/**/
	 
$('.cartDeliveryValidate').click(function(e) {
	var thirdPartyFlag = true;
	var headerFlag = false;
	var stockFlag = false;
	var lotFlag = false;
	/*if(otherFields() && $("#headervalidateCartForm").valid() && $("#specialStockPartForm").valid() &&
			$("#lotNoForm").valid() ){
		console.log("Delivery order validate invoking");
		window.location.href = ACC.config.contextPath+ '/cart/validate';	
	}*/
	
///	flag = $("#headervalidateCartForm").valid() ? true : false
	var surgeryData = $("#surgeryData").val();
	var surgeryMand = false;

	if(surgeryData != undefined){
		// means surgery info is mandatory
		if(surgeryData == ""){
			$("#surgeryInfoError").parent().find('div.registerError').html($("#surgeryInfoError").attr('data-msg-required'));

			$("#surgeryInfoError").show();
			surgeryMand = true;
		}
	}
	if($("#headervalidateCartForm").valid() ){
		headerFlag = true;
	} else{
		headerFlag= false;
	}
	
	
	if(thirdPartyFields()){
		thirdPartyFlag = true;
	} else{
		thirdPartyFlag= false;
	}	
	
	/* if($("#specialStockPartForm").valid() ){
		 stockFlag = true;
	} else{
		stockFlag= false;
	}*/
	 
	 if($('.splStockPartnerForm_'+currViewScreenName).length != 0){
		    $('.splStockPartnerForm_'+currViewScreenName).each(function() {
		    	$(this).removeAttr('novalidate');
		    	if(jQuery(this).valid()){
		    		stockFlag = true;
			}  
	    	else{
	    		stockFlag= false;
			}});				
		}
	 
	 lotFlag = true;
	 
	 if($('.orderEntry_'+currViewScreenName).length>0){
		 $('.orderEntry_'+currViewScreenName).each(function() {
			 $(this).removeAttr('novalidate');
		    	if(!jQuery(this).valid()){
		    		 lotFlag = false;
				}  
		    	
			 
		 });
	}	 
	else{
		lotFlag=true
		}
	/* if($("#lotNoForm").length>0){
		 if($("#lotNoForm").valid()){
			 lotFlag = true;
		}  else{
			lotFlag= false;
			}
	 }
	 else{
		 lotFlag = true;
	 }
	*/
	 if(thirdPartyFlag && headerFlag && stockFlag && lotFlag && !surgeryMand){
		window.location.href = ACC.config.contextPath+ '/cart/validate';	
	}
	
});
var invalidDropShipAccount = false;

function checkForValidDropShipment(){
	
	var shipAccount=$("#dropShip").val();
	if(shipAccount.trim()!=''){
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateDropShipAccount?dropShipAccount=' + $("#dropShip").val(),
		success: function (data) {

			// if error occurred in updating drop ship address
			// it will show error massage in errorMsg div else reflect the new address on delivery address div
			if (data.indexOf("~Error Occurred~")!= -1){
				invalidDropShipAccount = true;
				$('#errorMsgDiv').html("<label class='error'>"+ $("#DropShipError").val() + "</label>");
				//$("#errorMsgDiv").html("<label class='error'>Please select a No Charge Reason!</label>")

				$('#distPurOrder').removeAttr( "data-msg-required");
				$('#distPurOrder').removeAttr( "class");
			}
			else{
				invalidDropShipAccount =false;
				$('#errorMsgDiv').html("");
				//$('#pi-shipToaddress').html(data)
				$('#dropShip').val('');
				$('#distPurOrder').attr( "data-msg-required",  $("#DistPurchaseError").val() );
				$('#distPurOrder').attr( "class", "required" );
				$('#drop-ship-account-list-icon').removeClass('strictHide');									
			}	
		}
	});
}else{
	invalidDropShipAccount =false;
}
	
}

/**********No charge Order AAOL-3392*/

$('.cartStep1Saveupdate1NoCharge').click(function(e){
	
	var poNumber = $("#purchOrder").val();
	
	
	if($('.selectnoChargepicker').find(":selected").val()!='' && $('.selectnoChargepicker').find(":selected").val()!='undefined' && poNumber.trim() != ""){
		$(".registerPOError").html("");	
		$(".registerError").html("");
		$(".errorMsgDiv").html("");
		checkForValidDropShipment();
		jQuery.ajax({
			global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
			url : ACC.config.contextPath + '/cart/checkPOValid?poNumber='
					+ encodeURIComponent(poNumber),
			success : function(data) {
				if (data) {
					
					//$("#ajaxCallOverlay").hide(); // overlay must fade out
					//$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
					if(!invalidDropShipAccount){
					$('#validateOrderDivId-popup').modal('show');
					}
					
				} else { 
				
					
								jQuery.ajax({
						global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
						url : ACC.config.contextPath + '/cart/checkPO?poNumber='
								+ $("#purchOrder").val(),
						success : function(data) 
						{						
							if (data) 
							{ 
								if(!invalidDropShipAccount){
							$('#validateOrderDivId-popup').modal('show');
								}
							} 
							else 
							{   
								if(!invalidDropShipAccount){
								window.location.href = ACC.config.contextPath+ '/cart/validate';
								}
							}
						}
					});
				}
				
							}
		});
	
	}else{
		if(poNumber == ""){
			$(".registerPOError").html(REGIS_ERROR)
			/*$(".registerPOError").html("<label class='error'>"+ $('#regisError').val() + "</label>")*/
			$(".registerPOError").css('display','block');
			
		} 
		
		 if($('.selectnoChargepicker').find(":selected").val()==''){
			$(".registerReError").html(NOCHARGE_REASON)
		/* 	$(".registerReError").html("<label class='error'>Please select a No Charge Reason!</label>")*/

		}
		if(poNumber!=""){
			$(".registerPOError").html("");	
		}
		if($('.selectnoChargepicker').find(":selected").val()!=""){
			$(".registerError").html("");	
		}
	}
	
		
		});
/**********No charge Order AAOL-3392*/
$('.shippingPage').click(
		function(e) {
			
			window.location.href = ACC.config.contextPath+ '/cart/shipping';	
			
		});


$(".continuetopayment").click(function(e) {
	var shipDrodownBodyArr=new Array();
	$('#datatab-desktop select.shipping-body-dropdown').each(function(e) {
		if($.trim($(this).val()).length==0 || $(this).val()=="Select Method")
			{
			 $(this).val("Standard +~~+ Standard");
			}
		
		
		/*jQuery.ajax({
			url : ACC.config.contextPath + '/cart/updateShippingMethod?route='
					+ $(this).val()+'&entryNumber='+$(this).attr('data'),
			async: false,
			success : function(data) {	}
		});*/
		
	}); 


	
	window.location.href = ACC.config.contextPath+ '/cart/paymentContinue';	
});



$(".orderreview").click( function(e) {
	var orderType = $("#orderType").val();
	var errorMsg = $("#paymentErrorMsg").val();
	
	var poNumber = $("#purchOrder").val();
	
	if(orderType !=="ZDEL" ){
		if(poNumber == ""){
			$(".registerError").html(REGIS_ERROR)
			/*$(".registerError").html("<label class='error'>"+ $('#poNumberError').val() + "</label>")*/
		}else{
			window.location.href = ACC.config.contextPath+ '/cart/orderReview';
		}
	}
	else{
		window.location.href = ACC.config.contextPath+ '/cart/orderReview';
	}
	
		});





$("#creditCardLightbox").click(function(){
	
	$(".lightboxBlackOverlay").css("height", $(document).height());
	$(".lightboxContent").css("display","block");
	$(".lightboxBlackOverlay").css("display","block");
	// Fix for JJEPIC-257 Reset form fields (In Case popup has opened before and contains data) and also during regression testing, when the user clicks on the
	// Pay Now button on the IE browser, it's not working properly because Paymetric_Packet is being emptied due to reset() function so we assign that value to
	// one variable and after calling the reset function, assign that value to paymetric_packet again.
	var payload = $("#Paymetric_Packet").val();
	$("#addEditCreditCart form" ).reset();
	$("#Paymetric_Packet").val(payload);
	$("#addEditCreditCart form input" ).css("background","");
	$("#Paymetric_ErrorLogging").html('');
	$("#Paymetric_ErrorLogging").css('border','');
	$("#Paymetric_CreditCardNumber").attr("autocomplete", "off");
	$("#Paymetric_Exp_Month").attr("autocomplete", "off");
	$("#Paymetric_Exp_Year").attr("autocomplete", "off");
	$("#Paymetric_CVV").attr("autocomplete", "off");
});


$('#cartStep1Saveupdate').click(
		function(e) {
			$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
			$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
			var surgeryData = $("#surgeryData").val();
			var surgeryMand = false;
			var splStockPartnerRequired = false;
			if(surgeryData != undefined){
				// means surgery info is mandatory
				if(surgeryData == ""){
					$("#surgeryInfoError").show();
					surgeryMand = true;
				}
			}
			
			if($("#dropShip").val() != null && $("#dropShip").val().length!=0)
			{
				$("#stndDistPurOrder")[0].setAttribute("required", "true");
			}
			
			var lotNoMandatory = false;
			if($(".orderEntry").length != 0){
			    $('.orderEntry').each(function() {  
			        if(!jQuery(this).valid()){
			        	lotNoMandatory = true;
			        }
			    });				
			}
			if($(".splStockPartnerForm").length != 0){
			    $('.splStockPartnerForm').each(function() {	    	
			    	
			    	var formIdArray= $(this).attr('id').split("_");
			    	var entryNumber=formIdArray[1];
			    	//Remove existing errors
			    	$("#"+entryNumber+"_Error").remove();
			    	
			        if(!jQuery(this).valid()){
			        	splStockPartnerRequired = true;
			        }
			    });				
			}
						
			if (!jQuery("#cartStep1Check").valid() || lotNoMandatory || surgeryMand || splStockPartnerRequired) {
				
			
				
				$("#ajaxCallOverlay").hide(); // overlay must fade out
				$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
				$('#myprofileedit .success').hide();
			}
			else{


														  /*GTR-1693*  Commented and added new else block completely/

										/* If any error occurs then keep this commented code inside else block till {search:Till 'A'}
										var canValidate = $("#canValidateCart").val() ;
										if(canValidate == 'false'){
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											batchModePopUp(false);
										}else{ //Cart can be validate
											var poNum=$("#purchOrder").val();
											jQuery.ajax({
												global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
												url : ACC.config.contextPath + '/cart/checkPO?poNumber='
														+ encodeURIComponent(poNum),
												success : function(data) {
													if (data) {
														$("#ajaxCallOverlay").hide(); // overlay must fade out
														$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
														showValilatePONumAlert(e)
													} else {
														window.location.href = ACC.config.contextPath
																+ '/cart/validate';
													}
												}
											});
										}//close of else
										*/





			
					var canValidate = $("#canValidateCart").val() ;
				if(canValidate == 'false')
					{
					 
					checkForPOAlert(e);		
					//** 
				
					/*$("#ajaxCallOverlay").hide(); // overlay must fade out
					$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
					batchModePopUp(false);*/
				}else{/* //Cart can be validate
					
						
					var poNum=$("#purchOrder").val();
					jQuery.ajax({
						global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
						url : ACC.config.contextPath + '/cart/checkPOValid?poNumber='
								+ encodeURIComponent(poNum),
						success : function(data) {
							if (data) {
								 
								$("#ajaxCallOverlay").hide(); // overlay must fade out
								$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
								showValilatePOOrderNumAlert(e);
								
							} else { 
							
								
								
								//Added for GTR-1693
								jQuery.ajax({
									global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
									url : ACC.config.contextPath + '/cart/checkPO?poNumber='
											+ $("#purchOrder").val(),
									success : function(data) 
									{							
										if (data) 
										{ 
										
											$("#ajaxCallOverlay").hide(); // overlay must fade out
											$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
											//showValilatePONumAlert(e);
										} 
										else 
										{   
											
											window.location.href = ACC.config.contextPath+ '/cart/validate';
										}
									}
								});
							}
							
							//Added
								window.location.href = ACC.config.contextPath
										+ '/cart/validate';
							}
						}
					});
				*/}//close of else
			}//Till 'A'
		}); 

$('.shippingMethodSelect').change(function(e) {	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateShippingMethod?route='
				+ $(this).val()+'&entryNumber='+$(this).attr('data'),
		async: false,
		success : function(data) {	}
	});
});



$('.cartStep1Saveupdate').click(function(e) {
	$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
	$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
	var surgeryData = $("#surgeryData").val();
	var surgeryMand = false;
	var splStockPartnerRequired = false;
	if(surgeryData != undefined){
		// means surgery info is mandatory
		if(surgeryData == ""){
			$("#surgeryInfoError").show();
			surgeryMand = true;
		}
	}
	
	if($("#dropShip").val() != null && $("#dropShip").val().length!=0)
	{
		$("#stndDistPurOrder")[0].setAttribute("required", "true");
	}
	
	var lotNoMandatory = false;
	if($(".orderEntry").length != 0){
	    $('.orderEntry').each(function() {  
	        if(!jQuery(this).valid()){
	        	lotNoMandatory = true;
	        }
	    });				
	}
	if($(".splStockPartnerForm").length != 0){
	    $('.splStockPartnerForm').each(function() {	    	
	    	
	    	var formIdArray= $(this).attr('id').split("_");
	    	var entryNumber=formIdArray[1];
	    	//Remove existing errors
	    	$("#"+entryNumber+"_Error").remove();
	    	
	        if(!jQuery(this).valid()){
	        	splStockPartnerRequired = true;
	        }
	    });				
	}
				
	if (!jQuery("#cartStep1Check").valid() || lotNoMandatory || surgeryMand || splStockPartnerRequired) {
		$("#ajaxCallOverlay").hide(); // overlay must fade out
		$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
		$('#myprofileedit .success').hide();
	}
	else{


												  /*GTR-1693*  Commented and added new else block completely/

								/* If any error occurs then keep this commented code inside else block till {search:Till 'A'}
								var canValidate = $("#canValidateCart").val() ;
								if(canValidate == 'false'){
									$("#ajaxCallOverlay").hide(); // overlay must fade out
									$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
									batchModePopUp(false);
								}else{ //Cart can be validate
									var poNum=$("#purchOrder").val();
									jQuery.ajax({
										global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
										url : ACC.config.contextPath + '/cart/checkPO?poNumber='
												+ encodeURIComponent(poNum),
										success : function(data) {
											if (data) {
												$("#ajaxCallOverlay").hide(); // overlay must fade out
												$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
												showValilatePONumAlert(e)
											} else {
												window.location.href = ACC.config.contextPath
														+ '/cart/validate';
											}
										}
									});
								}//close of else
								*/





	
			var canValidate = $("#canValidateCart").val() ;
		if(canValidate == 'false')
			{
			 
			checkForPOAlert(e);		
			//** 
		
			/*$("#ajaxCallOverlay").hide(); // overlay must fade out
			$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
			batchModePopUp(false);*/
		}else{ //Cart can be validate
			
				
			var poNum=$("#purchOrder").val();
			jQuery.ajax({
				global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
				url : ACC.config.contextPath + '/cart/checkPOValid?poNumber='
						+ encodeURIComponent(poNum),
				success : function(data) {
					if (data) {
						 
						$("#ajaxCallOverlay").hide(); // overlay must fade out
						$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
						showValilatePOOrderNumAlert(e);
						
					} else { 
					
						
						
						/*//Added for GTR-1693
*/								jQuery.ajax({
							global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
							url : ACC.config.contextPath + '/cart/checkPO?poNumber='
									+ $("#purchOrder").val(),
							success : function(data) 
							{							
								if (data) 
								{ 
								
									$("#ajaxCallOverlay").hide(); // overlay must fade out
									$("#modal-ui-dialog").hide(); // dialog and spinner must fade out
									showValilatePONumAlert(e);
								} 
								else 
								{   
									
									window.location.href = ACC.config.contextPath+ '/cart/validate';
								}
							}
						});
					}
					
					//Added
						/*window.location.href = ACC.config.contextPath
								+ '/cart/validate';
					}*/
				}
			});

		}//close of else
	}//Till 'A'
});


$('.reasonCodeSelect').change(function(e) {	
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCode?reasonCode='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});


$('.requestDeliveryDate').change(function(e) {	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateDeliveryDateForCart?expDeliveryDate='+$(this).val(),
		async: false,
		success : function(data) {	}
	});
});


$('.showChagneOrderPopup').click(function(e) {
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getOrderType?currentOrderType='+$("#currentOrderType").val(),
		type: 'GET',
		content: 'text/html',
		success : function(data) {
			showChangeOrderTypePopUp(e,data)
		}
	
	});
});

var searchSelectedFlag = false;
$('#selectSurgeon').click(function(e) {
	searchSelectedFlag = false;
	$("#searchSurgeon").val('');
	showCartSurgeonAjaxCall(e);

});

$(document).on('click','#slctSurgeonBtn',function(e) {
	searchSelectedFlag = true;
	showCartSurgeonAjaxCall(e);
});


/**Update Surgeon*/
$(document).on('click','#updateSurgeon',function(e) {
	
	searchSelectedFlag = false;
	orderNumber = $(this).attr("orderNumber");
	surgeonName = $(this).attr("surgeonName");
	updateSurgeionLink =$(this); 
	showSurgeonAjaxCall(e, orderNumber,surgeonName);
});

function showCartSurgeonAjaxCall(e) {
	var dataObj = new Object();
	dataObj.loadMoreCounter = $("#loadMoreCounter").val();
	dataObj.searchPattern = $("#searchSurgeon").val();
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/surgeonData',
		data : dataObj,
		type : 'POST',
		content : 'text/html',
		success : function(data) {
			
			$('#surgeonPopupHolder').html(data);
			$('#searchSurgeon').val('');
			$('#selectSurgeonPopup').modal('hide');
			$(".modal-backdrop").hide();
			$('#selectSurgeonPopup').modal('show');
			$("#globalErrorSuergeon").hide();
			$(".surgeonloadmore").click(function() {
				$("#loadMoreCounter").val($(this).attr("data"))
				showSurgeonAjaxCall(e);
			});
			if(searchSelectedFlag == true){
				
				$("#surgeonRadio").prop("checked",true);
				$("#searchSurgeon,#slctSurgeonBtn").prop("disabled",false);
				/*$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
					
					"cursor":"pointer"
					
				});*/
			}
		}
	});
}

function showSurgeonAjaxCall(e, orderNumber,surgeonName){
	var dataObj = new Object();
	dataObj.loadMoreCounter = $("#loadMoreCounter").val();
	dataObj.searchPattern = $("#searchSurgeon").val();
	$("#surgeonIdInput").val(surgeonName);
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/surgeonData',
		data: dataObj,
		type: 'POST',
		content: 'text/html',
		success : function(data) {
			
			$('#surgeonPopupHolder').html(data);
			$('#searchSurgeon').val('');
			$('#selectSurgeonPopup').modal('hide');
			$(".modal-backdrop").hide();
			$('#selectSurgeonPopup').modal('show');
			$("#surgeonIdInput").val(surgeonName);
			if(searchSelectedFlag == true){
				$("#surgeonRadio").prop("checked",true);
				$("#searchSurgeon,#slctSurgeonBtn").prop("disabled",false);
				/*$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
					
					"cursor":"pointer"
					
				});*/
			}
			
			$(".surgeonloadmore").click(function() {
				$("#loadMoreCounter").val($(this).attr("data"))
				showSurgeonAjaxCall(e);
			})
		}
	});
}

var hospitalId = '';


	$(document).on("click","#surgeonRadio",function(e) {
		$("#registerErrorSurgeon").hide();
		$("#searchSurgeon,#slctSurgeonBtn").prop("disabled",false);
		/*$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
			
			"cursor":"pointer"
			
		});*/
		$(".surge-inputbox").attr("readonly",true);
		document.getElementById("submitSurgeonUpdate").style.pointerEvents = "visible";
		document.getElementById("surgeonIdInputradio3").value = "";
		document.getElementById("surgeonIdInputradio3").readonly = true;
		document.getElementById("surgeonRadio").value = "true";
		document.getElementById("radio2").value = "false";
		document.getElementById("radio3").value = "false";
	});
	
	$(document).on("click",'#radio2',function(e){
		$("#globalErrorSuergeon").hide();
		$("#searchSurgeon").prop("disabled",true);
		/*$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
			
			"cursor":"default"
			
		});*/
		$("#surgeonIdInputradio3").prop('readonly',true);
		document.getElementById("surgeonIdInputradio3").value = "";
		document.getElementById("submitSurgeonUpdate").style.pointerEvents = "visible";
		document.getElementById("radio2").value = "true";
		document.getElementById("radio3").value = "false";
		document.getElementById("surgeonRadio").value = "false";
	});
	
	$(document).on("click",'#radio3',function(e){
		$("#globalErrorSuergeon").hide();
		$("#surgeonIdInput").val("");
		$("#searchSurgeon").prop("disabled",true);
		/*$(".surgeonaccountList .anchorcls, .surgeonaccountList .anchorcls .list-group-item-heading, #selectSurgeonPopup .accountListPopUp:nth-child(even) .anchorcl").css({
			
			"cursor":"default"
			
		});*/
		$("#surgeonIdInputradio3").prop('readonly',false);
		document.getElementById("submitSurgeonUpdate").style.pointerEvents = "visible";
		document.getElementById("radio3").value = "true";
		document.getElementById("radio2").value = "false";
		document.getElementById("surgeonRadio").value = "false";
	});

	$(document).on("click",'.surgeonDetail',function(e){
//		if($("#surgeonRadio").prop("checked") || searchSelectedFlag){
			$("#surgeonRadio").prop("checked",true);
			$("#searchSurgeon,#slctSurgeonBtn").prop("disabled",false);
			inputText = $('input[name=surgeonName]');
			inputText.val($(this).attr("id"));
			surgeonId = $(this).attr("surgeonId");
			hospitalId = $(this).attr("hospitalID");
			inputText.attr("surgeonId", surgeonId);
			$("#searchSurgeon").val('');
			//document.getElementById("radio2").value = "false";
			//document.getElementById("radio3").value = "false";
			
//		}
	});
	
	
		$(document).on("click",'#submitSurgeonUpdate',function(){	
			var errorMsg = $('#thirdPartyErrorMsg').val();
			if($("#radio2").prop("checked")) 
			{
				var surgeonId = "UNAVAILABLE";
				$("#surgeonIdInput").val(surgeonId);

			}
			if($("#radio3").prop("checked")) 
			{	
				if($("#surgeonIdInputradio3").val()==""){
					$("#surgeonIdInputradio3").parent().parent().find('div.registerError').html("<label class='error'>" + errorMsg + "</label>");				
					
					$("#registerErrorSurgeon").show();
					return false;
				}
				var surgeonId = $("#surgeonIdInputradio3").val();
				$("#surgeonIdInput").val(surgeonId);
				
			}
			if ($.trim($("#surgeonIdInput").val()) == ""
				|| $.trim($("#surgeonIdInput").val()) == "") {	
				$("#registerErrorSurgeon").hide();
				$("#globalErrorSuergeon").show();
				
				return false;
			} 
			else {
				$("#globalErrorSuergeon").hide();
				//$('#selectSurgeonPopup').modal('hide');
				//$(".modal-backdrop").hide();
			}
			
			var selectedSurgeonId = $("#surgeonIdInput").attr("surgeonId");
			var selectSurgeonName = null;
			if (selectedSurgeonId === undefined) {
				selectedSurgeonId = null;
				hospitalId = '';
			}
			selectSurgeonName = $("#surgeonIdInput").val();
			var dataObj = new Object();
			dataObj.selectedSurgeonId = selectedSurgeonId;
			dataObj.selectSurgeonName = selectSurgeonName;
			dataObj.hospitalId = hospitalId;
			
			if($('.cartpage-surgeon').length>0){
				var updateSurgeonRequestUrl = ACC.config.contextPath
				+ '/cart/updateSurgeonData';
				$.ajax({
					type : "POST",
					url : updateSurgeonRequestUrl,
					data : dataObj,
					success : function(data) {
						if (data != "Error") {
							$('#selectSurgeonPopup').modal('hide');
							$("#surgeonName").val(data);					
						} else {
							var errorMsg = $("#SurgeonError").val()
							surgeonUpdateFailSpan = $('#surgeonUpdateFail');
							surgeonUpdateFailSpan.html(errorMsg);
							surgeonUpdateFailSpan.show();
						}

					}
				});

			}
			
			 if($('.orderHistory-surgeon').length>0){
				 var updateSurgeonRequestUrl = ACC.config.contextPath
					+ '/order-history/updateSurgeonGTRequest';
				 $.ajax({
						type : "POST",
						url : updateSurgeonRequestUrl,
						data : { orderNumber: orderNumber, selectedSurgeonId : selectedSurgeonId,selectSurgeonName : selectSurgeonName,hospitalId :hospitalId},
						success : function(data) {
							if(data != null && data != ''){
								updateSurgeionLink.attr("surgeonName",data)
								 $(".modal-backdrop").hide();
								 $("#success-dialogPopupSurgeon").modal("show");								
								 $("#dialog-okSurgeon").click(function(){
										window.location.reload();
								 });
							} else {
								var errorMsg = $("#SurgeonError").val()
								surgeonUpdateFailSpan = $('#surgeonUpdateFail');
								surgeonUpdateFailSpan.html(errorMsg);
								surgeonUpdateFailSpan.show();
							}
							
						}
					});
			 }
		
		});
	
	

$('.showPriceOverridePopup').click(function(e) {
	var basePrice= $(this).attr('id').split("_");
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getPriceOverridePopUp?entryNumber='+ basePrice[1],
		type: 'GET',
		content: 'text/html',
		success : function(data) {
			showPriceOverridePopup(e,data)
		}
	
	});
});

function showPriceOverridePopup(e,data) {
	e.preventDefault();
	$.colorbox({
		html : data,
		height : '405px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#priceOverrideCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			jQuery("#priceOverrideForm").validate(
					{

						rules : {
							overridePrice : {
								number : true,
								priceOverRideMax : true,
								priceOverRideMin : true
							}
						},
						messages : {
							number : "only Numbers!"
						},
						errorPlacement : function(error, element) {
							error.appendTo(element.parent().parent().parent()
									.find('div.registerError'));
						},
						onfocusout : false,
						focusCleanup : false
					});
		}
	});
}




function showChangeOrderTypePopUp(e,data) {
	e.preventDefault();
	$.colorbox({
		html : data,
		height : '405px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#changeOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});	
			$("#submitChangeOderType").click(function(e) {				
				if ($('input[name=orderType]:checked').length > 0) {
					$("#changeOrderTypeForm").submit();
				}		
				else{
					$("#changeOrderTypeMessageId").show();
					e.preventDefault();
				}
			});	
		}
	});
	$('#changeOrderType').css('display', 'block');
}


$('#showSurgeryInfoPopup').click(function(e) {
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getSurgeryInfo',
		type: 'GET',
		content: 'text/html',
		success : function(data) {			
			//showSurgeryInfoPopup(e,data)
			$('#surgeryInfoPopupHolder').html(data);
			$('#surgeryInfoPopup').modal('show');
			$('.selectpicker').selectpicker('refresh');
			$('.date-picker').datepicker();
			$('.date-picker').on('changeDate', function(ev){
			    $(this).datepicker('hide');
			});
		}
	
	});
});

$('#showSurgeryInfoPopupReview').click(function(e) {
	e.preventDefault();

	$('#cartSurgeryInfoPopup').modal('show');
	$('.selectpicker').selectpicker('refresh');
	$('.date-picker').datepicker();

});

$('#showSurgeryInfoPopupConfirm').click(function(e) {
	e.preventDefault();

	$('#orderSurgeryInfoPopup').modal('show');
	$('.selectpicker').selectpicker('refresh');
	$('.date-picker').datepicker();

});
/*$('.showSurgeryInfoPopup').click(function(e) {
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getSurgeryInfo',
		type: 'GET',
		content: 'text/html',
		success : function(data) {
			showSurgeryInfoPopup(e,data)
		}
	
	});
});


function showSurgeryInfoPopup(e,data) {
	e.preventDefault();
	$.colorbox({
		html : data,
		height : '505px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#enterCaseDate").datepicker();
			$("#changeOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});			
		}
	});
	$('#enterSurgeryInfo').css('display', 'block');
}*/



/*GTR-1693 changes Starts*    Commented and added same functionality down/

/*function showValilatePONumAlert(e) {
	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderDivId").html(),
		height : '160px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#validateOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
			$("#validateOrderOk").click(function() {
				$("#cartStep1Saveupdate").submit();
			});
		}
	});
	$('.validateOrderDivId').css('display', 'block');
}*/

/*GTR-1693 changes Ends*/


/*GTR-1693 Starts*/
 

function showValilatePONumAlert(e) {
	
	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderDivId").html(),
		height : '180px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {

			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			$("#validateOrderOk").click(function() {
				$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in
														// to 0.6 opacity
				$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner
														// must fade in to 0.6
														// opacity
				$("#cartStep1Saveupdate").submit();
			});
		}
	});
	$('.validateOrderDivId').css('display', 'block');
}

function showValilatePOOrderNumAlert(e) {

	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderNumDivId").html(),
		height : '220px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			
			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
		}
	});
	$('.validateOrderNumDivId').css('display', 'block');
}

function showPONumAlertBeforeBatch(e) {

	e.preventDefault();
	$.colorbox({
		html : $("#validateOrderDivId").html(),
		height : '180px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {

			$("#validateOrderOk").prop('href', 'javascript:;');

			$(".closePopup").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});

			$("#validateOrderOk").click(function() {
				batchModePopUp(false);
			});
		}
	});
	$('.validateOrderDivId').css('display', 'block');
}



    /*GTR-1693 Ends*/

$('#editFreightCostLink').click(function(e) {
	$.colorbox({
		html : $("#editFreightLightBox").html(),
		height : '305px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$("#validateOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
			$("#validateOrderOk").click(function() {
				$("#cartStep1Saveupdate").submit();
			});
		}
	});
});


$('.enterOneTimeShipToAddress').click(function(e) {
    jQuery.ajax({
           url : ACC.config.contextPath + '/cart/enterOntimeShippingAddress',
           type: 'GET',
           content: 'text/html',
           success : function(data) {
                  showEnterOneTimeShippingaddressPopUp(e,data)
           }
    
    });
});

$('.removeOneTimeShipToAddress').click(function(e) {
    jQuery.ajax({
           url : ACC.config.contextPath + '/cart/removeOneTimeShipToAddress',
           type: 'GET',
           content: 'text/html',
           success : function(data) {
        	   $("#deliveryAddressTag .companyName").text(data.companyName!= null ?data.companyName:"");
        	   $("#deliveryAddressTag .lineOne").text(data.line1 != null ?data.line1:"");
        	   $("#deliveryAddressTag .lineTwo").text(data.line2!= null ?data.line2:"");
        	   if(null!=data.country){
        		   $("#deliveryAddressTag .countryName").text(data.country.name!= null ?data.country.name:"");
        	   }
        	   $(".enterOneTimeShipToAddress").show();
        	   $(".removeOneTimeShipToAddress").hide();
        	   
//        	   $('.deliveryAddressTag').css('display', 'block');
                  }
    
    });
    
});




function showEnterOneTimeShippingaddressPopUp(e,data) {
    e.preventDefault();
    $.colorbox({
           html : data,
           height : '440px',
           width : '565px',
           overlayClose : false,
           onComplete : function() {
	        	 
	        			 jQuery("#oneTimeShippingAddressForm").validate({	        				
	        				 errorPlacement: function(error, element) {
	        					error.appendTo( element.parent().parent().find('div.registerError'));	        					
	        				},
	        				onfocusout: false,
	        				focusCleanup: false
	
	        			});
	        	
	        	   
                  $("#changeOrderCancel").click(function(e) {
                        e.preventDefault();
                        $.colorbox.close();
                  });                  
           }
    });
    $('#enterOneTimeShippingAddress').css('display', 'block');
}


$('.surgeryInfoPopup').click(function(e) {
	 surgeryInfoPopup(e);
});


function surgeryInfoPopup(e) {
	e.preventDefault();
	$.colorbox({
		html : $(".surgeryinfo").html(),
		height : '305px',
		width : '565px',
		overlayClose : false,
		onComplete : function() {
			$('.surgeryinfo').css('display', 'none');
		}
	});
}

$('.lotNumberEntry').change(
		function() {
//			var currViewScreenName = $("#currViewScreenName").val();
			var thisObject = $(this);
			var entryNumber  =  $(this).attr('data');
			var idx ="#lotNumber_"+ currViewScreenName + "_"+entryNumber;
			var lotErrorMsg = $("#lotErrorMsg").val();
			var lotNum =  $(idx).val();
			//FIX GTR-1768
			var prodcode =  $("#productCode_"+entryNumber).val();
			
							 
			if($.trim(lotNum) != ""){
				setTimeout(function(){$("#laodingcircle").show();},0);
				jQuery.ajax({
					url : ACC.config.contextPath
							+ '/cart/updateLotNumber?lotNumber='
							+ lotNum
							+ '&pcode='+prodcode
							 + '&entryNumber='+ entryNumber,
							 async:false,							
					success : function(data) {
						//alert("data"+data)
						if (!data) {
							
							$(idx).parent().parent().find('div.registerError').html( lotErrorMsg);
							$('#invalidLotNum_'+entryNumber).show();
							$('#lotNumber_'+ currViewScreenName + "_"+entryNumber).val(''); 
							
							
							$(idx).parent().parent().find('div.registerError').hide().fadeIn(1000);
							setTimeout(function(){
								$(idx).parent().parent().find('div.registerError').slideUp(500, function(){
									$(idx).parent().parent().find('div.registerError').html("");
								});
							},5000);
							$(idx).parent().parent().find('div.registerError').removeAttr( 'style' );
//							thisObject.closest("form").validate().resetForm();
						} else {
							$('#invalidLotNum_'+entryNumber).hide();
						}
						setTimeout(function(){$("#laodingcircle").hide();},100);
					},
					error:function(){
						setTimeout(function(){$("#laodingcircle").hide();},100);
					}
				});
			}
		});


$('.lotNumber').change(
		function() {
			var deviceName=$("#currViewScreenName").val();

			inReturnOrderPage=true;
			var entryNumber  =  $(this).attr('data');
			var lotNum =  $("#lotNumber_"+deviceName+"_"+entryNumber).val();
			//FIX GTR-1768
			var prodcode =  $("#productCode_"+entryNumber).val();
			
							 
			if($.trim(lotNum) != ""){
				setTimeout(function(){$("#laodingcircle").show();},0);
				jQuery.ajax({
					url : ACC.config.contextPath
							+ '/cart/updateLotNumber?lotNumber='
							+ lotNum
							+ '&pcode='+prodcode
							 + '&entryNumber='+ entryNumber,
					success : function(data) {
						if (!data) {
							$('#invalidLotNum_'+deviceName+"_"+entryNumber).show();
							$('#invalidLotNum_'+deviceName+"_"+entryNumber).removeClass("hide");
							$('#lotNumber_'+deviceName+"_"+entryNumber).val(''); 
						} else {
							$('#invalidLotNum_'+deviceName+"_"+entryNumber).hide();
							$('#invalidLotNum_'+deviceName+"_"+entryNumber).addClass("hide");
						}
						setTimeout(function(){$("#laodingcircle").hide();},100);
					},
					error:function(){
						setTimeout(function(){$("#laodingcircle").hide();},100);
					}
				});
			}
		});
				

$('.poNumber').change(function () {
	var poNumb= $(".poNumber").val();
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updatePONumber?poNumber=' + encodeURIComponent(poNumb) +'&entryNumber='+$(this).attr('data'),
		async: false,
		success: function (data) {
			
			}
	});
});
		
$('.invoiceNumber').change(function () {
	inReturnOrderPage=true;
	loadingCircleShow("show");
	var deviceName=$("#currViewScreenName").val();
	$('.invalidInvoiceNum').addClass('hide');
	var entryNumber  =  $(this).attr('data');
	var invNumber =  $("#invoiceNumber_"+deviceName+"_"+entryNumber).val();
	var prodcode =  $("#productCode_"+entryNumber).val();
	
	
	if($.trim(invNumber) != ""){	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateInvoiceNumber?invoiceNumber=' + invNumber+'&entryNumber='+$(this).attr('data'),
		success: function (data) {
			loadingCircleShow("hide");
			if(!data){								
				$('.invalidInvoiceError').show();
				$("#invoiceNumber_"+deviceName+"_"+entryNumber).val('');
				loadingCircleShow("hide");
				}else{
					$('.invalidInvoiceError').hide();
					loadingCircleShow("hide");
				}
			}
	});
	}else{
		loadingCircleShow("hide");
	}
});




/*jQuery("#returnOrderForm").validate({
	rules: {		
		reasonCodeReturn: {
			selectedOthers: true
		}
		
		},
		showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
	errorPlacement: function(error, element) { 
		error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});

jQuery.validator.addMethod("selectedOthers",
	    function (value, element) { if($("#reasonCodeReturn").val() == "other") return false; else return true; }, $("#ReasonCodeError").val());
*/


$('#spinSalesUCN').change(
		function() {
			window.location.href = ACC.config.contextPath
					+ '/cart/updateSalesRepUCN?salesRepUCN='
					+ $("#spinSalesUCN").val() + '&specialStockPartner='
					+ $("#spinSalesUCN option:selected").attr('data');
		});

$('.specialStockPartnerEntry').change(function () {
	
	var errorMessage = $('#stockErrorMsg').val();
	var thisObject = $(this);
	var prodid= $(this).attr('data');
//	prodid=prodid[1];
	var splStockPartner = $('#specialStock_'+currViewScreenName+"_"+prodid).val();
	if($.trim(splStockPartner) != ""){ 
		jQuery.ajax({
			url: ACC.config.contextPath +'/cart/updateSpecialStockPart?entryNumber=' +prodid+'&specialStockPartner='+ splStockPartner,
			async: false,
			success: function (data) {
				if(!data){
					$('#specialStock_'+currViewScreenName+"_"+prodid).val("");
					if($("#" + prodid +"_Error").length==0){
						//thisObject.parent().append("<div id='" + prodid + "_Error' class='error marTop5'><p>Special Stock Partner - '" + spclStckNum + "' Invalid!</p></label>");
						thisObject.parent().append("<div id='" + prodid + "_Error' class='error marTop5'><p>"+ errorMessage +"</p></label>");
						$("#" + prodid +"_Error").hide().fadeIn(1000);
						setTimeout(function(){
							$("#" + prodid +"_Error").slideUp(500, function(){
								$("#" + prodid +"_Error").remove();
							});
						},5000);
						thisObject.closest("form").validate().resetForm();
						$('#specialStock_'+prodid).val('');
					}
					$('#specialStock_'+prodid).focus();				
				}
			}
		});
	}
});

$('#third-party-checkbox').change(function () {	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateThirdPartyFlag?thirdPartyFlag=' + $('#third-party-checkbox').prop("checked"),
		success: function (data) {
			if(null != data && data != ""){
				/*$("#deliveryAddressTag .companyName").text(data.firstName!= null ?data.firstName:"");
				$("#deliveryAddressTag .companyName").append(data.lastName!= null ?" " + data.lastName:"");
				 $("#deliveryAddressTag .lineOne").text(data.line1!= null ?data.line1:"");
				 $("#deliveryAddressTag .lineOne").append(data.line2!= null ?", " + data.line2:"");
				 if(null!=data.country)
				 {
					 $("#deliveryAddressTag .countryName").text(data.country.name!= null ?data.country.name:"");
				 }*/
				$('#shipToAccount').val("");
				$('#distributorPo').val("");
			}
		}
	});	
});

$('#customerPo').change(function () {
	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateCustomerPo?customerPo=' + $('#customerPo').val(),
		async: false,
		success: function (data) {
		
			}
	});
});



$('#cordisHouseAccount').change(function () {
	
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/updateCordisHouseAccount?cordisHouseAccount=' + $('#cordisHouseAccount').val(),
		success: function (data) {
			if(data === true){
				$('#cordisHouseAccount').parent().parent().parent().find('div.registerError').html('');
			}
			else{
				$('#cordisHouseAccount').parent().parent().parent().find('div.registerError').html('<label for="cordisHouseAccount" class="error">'+ $("#cordisError").val()+'</label>');
			}
			}
	});
});

/**********No charge Order AAOL-3392*/


$('.reasonCodeNoChargeSelect').change(function(e) {	
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeNoCharge?reasonCodeNoCharge='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});

$('.selectnoChargepicker').change(function(e) {	
	$(".registerReError").html("");	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeNoCharge?reasonCodeNoCharge='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});
/**********No charge Order AAOL-3392*/


$.validator.addClassRules("returnEntriesRequired", {
	required:{
		depends: function(){
			if( $("#reasonCodeReturn").val()!='R07' && $("#reasonCodeReturn").val()!='R29' )
				{
				return true;
				}			
			else{
				return false;
			}
		}
	}
});

/*$(".submitDelivered").click(function() {	
	if (!jQuery("#cartStep1Check").valid() || ($(".orderEntry").length != 0 && !jQuery(".orderEntry").valid()) ||) {
		$('#myprofileedit .success').hide();
	}
	else{				
		var canValidate = $("#canValidateCart").val() ;
		if(canValidate == 'false'){
			batchModePopUp();
		}
		else{ //Cart can be validate
			jQuery.ajax({
				url : ACC.config.contextPath + '/cart/checkPO?poNumber='
						+ $("#purchOrder").val(),
				success : function(data) {
					if (data) {
						showValilatePONumAlert(e)
					} else {
						window.location.href = ACC.config.contextPath
								+ '/cart/validate';
					}
				}
			});
		}//close of else
	}
});*/
var toalEntries;
var toalResponse;
function refreshSapPrices(){	
	if($("#refreshSapPrice").val()){		
		var entryNumbers = $('input[name="entryNumber"]');
		toalResponse = 0;
		toalEntries = entryNumbers.length;
		entryNumbers.each(function() {
			getSapPrice($(this).val());
		});
	}

}

function getSapPrice(entryNumber) {	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/getSAPPrice?entryNumber='
				+entryNumber,
		async: false,
		type: 'POST',
		success : function(data) {
			$("#basePrice_"+entryNumber).html(data.currencySymbol + data.entryBasePrice);
			$("#totalPrice_"+entryNumber).html(data.currencySymbol + data.entryTotalPrice);
			toalResponse++;
			if(toalEntries == toalResponse){
				alert(ALL_PRICES);
			}
		}
	});
}
function getCartTotal() {	
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/getCartSubTotal',
			async: false,
			type: 'POST',
			success : function(data) {
				//
			}
		});
}



$("#returnOrderUploadError").click(function(e){
	inReturnOrderPage=true;
	$('#newReturnOrderErrorpopup').modal('show');
/*	  $.colorbox({
			html: $("#errorDetailsAddToCart").html(),
			height: '300px',
			width: '492px',
			overlayClose: false,	
			escKey:false
		});	*/
});

$(".addQuoteToCart").click(function(e){
	$("#addQuoteToCartForm").submit();
});

$('#uploadReturnOrderBrowseFile').on("change", function(){
	var val = $(this).val();
	if(val.substring(val.lastIndexOf(".")+1, val.length)!='xls'){
		$(this).prop("value", null);
		alert("Please choose xls file only");
	}
});


$(".addToCartPriceQuote").click(function(e){
	var orderNumber = $('#hybrisOrderNumber').val();
	jQuery.ajax({
		url: ACC.config.contextPath +'/cart/addToCartFromQuote?orderNumber=' + orderNumber,
		async: false,
		success: function (data) {
			window.location.href = ACC.config.contextPath+ '/cart';
		}
	});
});

$('#uploadReturnOrderBrowseFile').on("change", function(){
	var val = $(this).val();
	if(val.substring(val.lastIndexOf(".")+1, val.length)!='xls'){
		$(this).prop("value", null);
		alert(CHOOSE_FILE); 
	}
});

$('.returnOrderFileUpload').on("change", function(){
	inReturnOrderPage=true;
	var val = $(this).val();
	if(val.substring(val.lastIndexOf(".")+1, val.length)!='xls'){
		$(this).prop("value", null);
		
	}
});

/** Start : Upload file validation for Delivered order **/
Array.prototype.contains = function(k) {
	  for(var i=0; i < this.length; i++){
	    if(this[i]==k){
	      return true;
	    }
	  }
	  return false;
	}

//Added JJEPIC 720 Start


/*function TestFileType(fileName,fileTypes) {
	$(".invalidFileError").hide();
	if (!fileName)
		{
//		parent.$.colorbox.close();
		if($("#attachDocName").val() == "")
			removeUploadFile();
		return false;
		}
	dots = fileName.split(".");
	fileType = dots[dots.length - 1];
	if (fileTypes.contains(fileType)) {
		$("#submitDelivedOrderFileForm").submit();
	} else { 
		$(".invalidFileError").show();
		$("#uploadBrowseFile").val("");
		$("#removeUploadForm").hide();
		$("#attachDocNameSpan").val("");
		

	}
}*/

function TestFileType(fileName, fileTypes) {
	$(".invalidFileError").hide();
	if(fileName==""){
		$(".emptyFileError").show();
	}
	if (!fileName)
		{
//		parent.$.colorbox.close();
		if($("#attachDocName").val() == "")
			removeUploadFile();
		return false;
		}
	dots = fileName.split(".");
	fileType = dots[dots.length - 1];
	if (fileTypes.contains(fileType)) {
		$("#submitDelivedOrderFileForm").submit();
	} else { 
		$(".invalidFileError").show();
		$("#uploadBrowseFile").val("");
		$("#removeUploadForm").hide();
		$("#submitDelivedOrderFileForm").reset();

	}
}


<!--/*Added for AAOL-4937*/-->
function TestFileTypeReturn(fileName, fileTypes) {
	if (!fileName)
		{
		parent.$.colorbox.close();
		if($("#attachDocName").val() == "")
			removeUploadFile();
		return false;
		}
	dots = fileName.split(".");
	fileType = dots[dots.length - 1];
	if (fileTypes.contains(fileType)) {
		$("#uploadFileforReturn").submit();
	} else { 
		/*$(".invalidFileError").show();
		$("#uploadBrowseFile").val("");
		$("#removeUploadForm").hide();
		$.colorbox.resize();*/
		

	}
}
<!--/*Added for AAOL-4937*/-->


function removeUploadFile() {
	if ($("#uploadBrowseFile").val() != "") {
		$("#submitDelivedOrderFileForm").reset();
		$("#removeUploadForm").hide();
	} else {
		jQuery.ajax({
			url : ACC.config.contextPath + '/cart/deleteDeliveredOrderFile',
			type : 'POST',
			success : function(data) {
				if (data == "Success") {
					$("#uploadBrowseFile").css("width", "auto");
					$("#attachDocNameSpan").hide();
					$("#removeUploadForm").hide();
					$(".submitDeliveredOrderFile").prop("disabled", false);
				} else if (data == "Failure") {
					$(".removeFileError").show();
				}
			}
		});
	}
}


$('#third-party-checkbox').click(function(){
	if($(this).is(':checked')){
		
		$('.third-party-content').css("display","table");
	}
	else{
		$('.third-party-content').hide()
	}
})

if($('#third-party-checkbox').is(':checked')){
	
	$('.third-party-content').css("display","table");
}
else{
	$('.third-party-content').hide()
}


function thirdPartyFields(){
	var isValid = true;
	var errorMsg = $('#thirdPartyErrorMsg').val();

	if($('#third-party-checkbox').is(':checked')) {
		
		/*if(ACC.formCheckFunc.isNotNullAndNotEmpty($('#shipToAccount').val())
				&& ACC.formCheckFunc.isNotNullAndNotEmpty($('#distributorPo').val())){
			return true;
		}
		else{
			$("#shipToAccount").parent().parent().find('div.registerError').html("<label class='error'>Please enter Drop Ship Account! </label>");
			$("#distributorPo").parent().parent().find('div.registerError').html("<label class='error'>Please enter Distributor PO Number! </label>");
			return false;
		}*/
		
		if(ACC.formCheckFunc.isNotNullAndNotEmpty($('#dropShipDel').val())){
			clearCartErrorMessages("#dropShipDel");
			isValid = true;
		}
		else{
			$("#dropShipDel").parent().parent().find('div.registerError').html("<label class='error'>" + errorMsg + "</label>");

			isValid = false;
		}
		if(ACC.formCheckFunc.isNotNullAndNotEmpty($('#distPurOrderDel').val())){
			clearCartErrorMessages("#distPurOrder");
			isValid = true;
		}
		else{
			$("#distPurOrderDel").parent().parent().find('div.registerError').html("<label class='error'>" + errorMsg + "</label>");

			isValid = false;
		}

	}
	return isValid;
}

function clearCartErrorMessages(id){	
	$(id).parent().parent().find('div.registerError').html("");
}

//Added JJEPIC 720 Ends
/** End : Upload file validation for Delivered order **/

/** Start - Added Spinner on get price quote **/
$(".requestPriceQuoteBtn").click(function(e){
	$("#ajaxCallOverlay").fadeTo(0, 0.6); // overlay must fade in to 0.6 opacity 
	$("#modal-ui-dialog").fadeTo(0, 0.6); // dialog and spinner must fade in to 0.6 opacity
});
/** End - Added Spinner on get price quote **/


/*hiding Update and update all button start*/

$( ".cartUpdateAllbutton" ).hide();
$( ".qtyUpdateTextBox1" ).hide();  // update anchor tag link
/*jQuery('.qtyUpdateTextBox').on('input', function() {
	$( ".cartUpdateAllbutton" ).show();
	
	var id=$(this).attr('id');
	$( "#"+id ).show();
	
});*/


$( ".splitcartUpdateAllbutton" ).hide();
//$( ".qtyUpdateTextBox1" ).hide();
jQuery('.qtyUpdateTextBox').on('input', function() {
	$( ".cartUpdateAllbutton" ).show();
	var dropShipmentId=$(this).parents().closest('.dropShipmentTable').attr('id');
	$( "#"+dropShipmentId+" .splitcartUpdateAllbutton" ).show();
	var id=$(this).attr('id');
	$("#"+id+"_update").show();
});
 
/*hiding Update and update all button end*/


var dataObject1 = new Object();
var dataObject2 = new Object();

$(".qtyUpdateTextBox").change(function(e) {
	var entryNumber = ($(this).attr("entryNumber"));
	var qty = $(this).val();
	if (qty != "") {
		dataObject1[entryNumber]=qty;
		//$( ".qtyUpdateTextBox1" ).show();  // update anchor tag link
	}
	
});





$(document).on("click",".cartUpdateAllbutton", function(event){
	loadingCircleShow("show");
	var UpdateMultipleEntriesInCartForm = $('#UpdateMultipleEntriesInCartForm');
	var entryQty;
	var entryQtycheck=false;
	for (var entryNumber in dataObject1) {
		  if (dataObject1.hasOwnProperty(entryNumber)) {
			  entryQty = entryNumber + ":" + dataObject1[entryNumber];
			  if(QTY_CHECK!=""){
				  if(dataObject1[entryNumber]>Number(QTY_CHECK)){
					  entryQtycheck=true;  
				  } 
			  }			 
			  
			  $('<input>').attr({
					type : 'hidden',
					id : 'entryQuantityList',
					name : 'entryQuantityList',
					value : entryQty
				}).appendTo(UpdateMultipleEntriesInCartForm);
		  }
		}
	
	if(entryQtycheck==true){
		loadingCircleShow("hide");
		$('#newQuantitypopup').modal('show');
		
	}else{
		UpdateMultipleEntriesInCartForm.submit();
	}
	
	$(document).on('click', '#back-btn',function(e) {
		$('#newQuantitypopup').modal('hide');
		loadingCircleShow("hide");
		return false;	
	});
	
	$(document).on('click', '#continue-btn',function(e) {
		$('#newQuantitypopup').modal('hide');		
		UpdateMultipleEntriesInCartForm.submit();
		
	});
	
});





$(".splitcartUpdateAllbutton").click(function(e) {
			var currentClearAllBtnIndex=$(".splitcartUpdateAllbutton").index(this)+1;
			$("#dropShipmentTable-"+currentClearAllBtnIndex+" .qtyUpdateTextBox").each(function(e) {
				var entryNumber = ($(this).attr("entryNumber"));
				var qty = $(this).val();
				if (qty != "") {
					dataObject2[entryNumber]=qty;
				}
				
			});
			loadingCircleShow("show");
			var UpdateMultipleEntriesInCartForm = $('#UpdateMultipleEntriesInCartForm');
			var entryQty;
			for (var entryNumber in dataObject2) {
				  if (dataObject2.hasOwnProperty(entryNumber)) {
					  entryQty = entryNumber + ":" + dataObject2[entryNumber];
					  $('<input>').attr({
							type : 'hidden',
							id : 'entryQuantityList',
							name : 'entryQuantityList',
							value : entryQty
						}).appendTo(UpdateMultipleEntriesInCartForm);
				  }
				}
				
			UpdateMultipleEntriesInCartForm.submit();
		});




/* Changes by Swathi for Shipping Date*/		
$('.date-picker-body').on("changeDate", function(e)
 {
	var entryNo = $(this).attr('data');
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateShippingDateForCart?expectedShipDate='+$(this).val()+'&entryNumber='+entryNo,
		async: false,
		success : function(data) {}
	});
	e.preventDefault();
});
/* Changes by Swathi for Shipping Date*/		

/*************Return Order AAOL-3753-3758****************************/

/*$('.reasonCodeNoChargeSelect').change(function(e) {	
	
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeNoCharge?reasonCodeNoCharge='
				+ $(this).val(),
		async: false,
		success : function(data) {	}
	});
});*/



$('.reasonCodeReturnSelect').change(function(e) {	
	inReturnOrderPage=true;
	$('.registerREError').html('');
	if( $(this).val() == 'R07' || $(this).val() == 'R29')
	{
	$("#invoiceStar").css("display","none");
	}
	else
	{
	$("#invoiceStar").css("display","inline");
	}
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/updateReasonCodeReturn?reasonCodeReturn='
				+ $(this).val(),
		async: false,
		success : function(data) {	
			
			
		}
	});
});

$(".returnEntriesRequired").each(function (item) {
	inReturnOrderPage=true;
	$.validator.addClassRules("returnEntriesRequired", {
		required : {
			depends : function() {
				
				if ($('.reasonCodeReturnSelect').find(":selected").val() != 'R07' && $("#reasonCodeReturn").val() != 'R29') {
					
					return true;
				} else {
					return false;
				}
			}
		}
	});
});

//Fix for AAOL-6466
$(".submitReturn").click(function() {	
	
	$('.invalidLotNum').hide();
	$('.invalidInvoiceError').hide();
			var lotNoMandatory = false;
			if ($(".returnEntriesRequiredForm").length != 0) {
				$('.returnEntriesRequiredForm').each(function() {
					if (!jQuery(this).valid()) {
						lotNoMandatory = true;
					}
				});
			}
			
			if (jQuery("#returnOrderForm").valid()
					&& lotNoMandatory == false) {
		$("#returnOrderForm").submit();		
	}
});	

jQuery("#returnOrderForm").validate({
	rules: {		
		reasonCodeReturn: {
			selectedOthers: true
		}
		
		},
		showErrors: function(errorMap, errorList) {
			if(errorMap != null && errorList.length != 0)
				$(".errorSummary").html('<label class="error">'+ $("#globalError").val() +'</label>');
			else{
				$(".errorSummary").html('<label class="valid"></label>');
			}
			this.defaultShowErrors();
		},
	errorPlacement: function(error, element) { 
		error.appendTo(element.parent().parent().parent().find('div.registerREError'));
		error.appendTo(element.parent().find('div.registerError'));
		//error.appendTo(element.parent().parent().parent().find('div.registerError'));
	},
	onfocusout: false,
	focusCleanup: false
});


/*jQuery.validator.addMethod("selectedOthers",
	    function (value, element) { if($('.reasonCodeReturnSelect').find(":selected").val() =='other') return false; else return true; }, $("#ReasonCodeError").val());*/


jQuery.validator.addMethod("selectedOthers",
	    function (value, element) { if($("#reasonCodeReturn").val() == "other") return false; else return true; }, $("#ReasonCodeError").val());


/*$(".returnEntriesRequiredForm").each(function (item) {
	inReturnOrderPage=true;
	var sid= jQuery(this).attr('id');
	jQuery('#'+sid).validate(
		{
			rules : {
				
			},
			showErrors : function(errorMap, errorList) {
				if (errorMap != null && errorList.length != 0) {
					$(".errorSummary").html(
							'<label class="error">'
									+ $("#returnOrderEntryFieldError").val()
									+ '</label>');
				}
				this.defaultShowErrors();
			},
			errorPlacement : function(error, element) {
				error.appendTo(element.parent().parent().find(
						'div.registerError'));
			},
			onfocusout : false,
			focusCleanup : false
		});
});
*/
$('#uploadReturnOrderFile').click(function(){
	$('#newReturnOrderpopup').modal('show');
});

$('#rgaId').click(function(e) {
	$('#errorMessage').hide();
});

$('.submitReturnOrderFile').click(function(){
	$('#newReturnOrderpopup').modal('hide');
	$('#submitDelivedOrderFileForm').submit();
	
});

/*************Return Order AAOL-3753-3758****************************/

/*$('.return-ordr-cancel-btn,#jnj-menu-list li,#jnj-logo-holder').on('click', function (e) {
	
	jQuery.ajax({
	 type: "GET",
	 url:  ACC.config.contextPath +'/buildorder/clearSessionItem' ,
	 });
	if ($(this).find('a').attr('href') != undefined) {
		$('.leave-page-btn').attr('href', $(this).find('a').attr('href'));
	}
	else if ($(this).attr('href') != undefined) {
		$('.leave-page-btn').attr('href', $(this).attr('href'));
	}

	if ($('#returnOrderPage').val='true') {
		e.preventDefault();
		
		$("#startNewOrderpopup").modal('show');
		return false;
	}

});*/
$('.consignmentFillupCartValidate').click( function(e) {
	if($("#consignmentFillupForm").valid()){
		validatePoNumber($("#customerPONo").val())
		ACC.cartPage.updateConsignmentItem(); 
	}
});

$("#leaveReturnOk").click(function(e) {
	var keepItems = $("#keepItems").is(':checked');
	var deleteItems = $("#deleteItems").is(':checked');
	var dataObj = new Object();
	dataObj.keepItems =keepItems;
	dataObj.deleteItems =deleteItems;
	$('#returnOrderPage').val('false');
	jQuery.ajax({
		type: "POST",
		url:  ACC.config.contextPath +'/home/startNewOrder' ,
		data: dataObj,
		success: function (data) {
			window.location.href = ACC.config.contextPath + '/cart';
	    }
	});
});	

$("#startReturnButton,.startReturnButton").click(function(e) {
	inReturnOrderPage=true;
	$('#returnOrderPage').val('true');
	jQuery.ajax({
		type: "GET",
		url:  ACC.config.contextPath +'/home/startReturn' ,
		success: function (data) {
			if(data)
			{
				$("#newReturnpopup").modal('show');
				$("#newReturnOk").click(function(e) {
					var keepItems = $("#returnkeepItems").is(':checked');
					var deleteItems = $("#returndeleteItems").is(':checked');
					                      
					var dataObj = new Object();
					dataObj.keepItems =keepItems;
					dataObj.deleteItems =deleteItems;
					jQuery.ajax({
						type: "POST",
						url:  ACC.config.contextPath +'/home/startReturn' ,
						data: dataObj,
						success: function (data) {
							window.location.href = ACC.config.contextPath + '/cart';
					    }
					});
				});
			}
			else
			{ 
				window.location.href = ACC.config.contextPath + '/cart';
			}
	    }
	});
});

//Changes for View Surgery Info AAOL-4796 

$('.showSurgeryInfoOrderPopup').click(function(e) {
	
	var orderNumber = $(this).attr("orderNumber");
//	showSurgeryInfoAjaxCall(e, orderNumber);
	var dataObj = new Object();
	dataObj.orderNumber = orderNumber
	jQuery.ajax({
		url : ACC.config.contextPath + '/order-history/surgeryInfoOrderData',
		data:{orderNumber:orderNumber},
		type: 'GET',
		content: 'text/html',
		success : function(data) {			
			//showSurgeryInfoPopup(e,data)
			$('#surgeryPopupHolder').html(data);
			$('#orderHIstorySurgeryInfoPopup').modal('show');
			$('.selectpicker').selectpicker('refresh');
			$('.date-picker').datepicker();
			$('.date-picker').on('changeDate', function(ev){
			    $(this).datepicker('hide');
			});
		}
	
	});

});


/*************Return Order AAOL-3753-3758****************************/

$('.consignmentReturnValidate').click( function(e) {
	var poNumber =$("#customerPONo").val();
	if($("#consignmentReturnForm").valid()){
		validatePoNumber($("#customerPONo").val())
		ACC.cartPage.updateConsignmentReturnItem(); 
	}
	
});
/************AAOL-3769 changes**************/
function validatePoNumber(poNumber){
	var status;
jQuery.ajax({
	global: false, // Added this flag to prevent ajaxstart function to detect this ajax call
	async: false, // important ! do not remove.
	url : ACC.config.contextPath + '/cart/checkPOCinsignment?poNumber='
			+ poNumber,
	success : function(data)
	{	
		status = data;
		if(!data){
			$("#poNotPresent").css("display","block");
		}
	}
});
return status;
}
/************End AAOL-3769 changes**************/



$('#no-btn-placeorder').click( function(e) {	
	window.location.href = ACC.config.contextPath+'/cart';		
});

/* Return Order Validations*/
//Soumitra - AAOL-3784 - Consignment Charge
$('.consignmentChargeCartValidate').click( function(e) {
	if($("#consignmentChargeForm").valid()){
		validatePoNumber($("#customerPONo").val());
		ACC.cartPage.updateConsignmentChargeItem();
	}
});

$('.chargeCopyLine').click( function(e) {
	var id = this.id;
	var prodIds=$("#updateCartForm"+id+"_desktop input[name=productCode]").val();
	var prodQty=$("#quantity"+id).val();
	//var orignalLineQty=$("#quantity"+id).val();
	//orignalLineQty=orignalLineQty-1;
	//$("#quantity"+id).val(orignalLineQty);
	ACC.addToCartHome.ValidateNonContractProductForceNewEntry(prodIds,prodQty,id);
	//$("#quantity"+newEntryNumber+"_update").click();
 	//$("#orderentry-"+id).clone().appendTo("#datatab-desktop");
});

$('.batchNumber').change( function() {
	var serialNumberID=this.id.replace('bn_','sn_');
	if($('#'+serialNumberID).length)         // use this if you are using id to check
	{
		var serialNumberOptions=$('#'+this.value).val().replace('[','').replace(']','');
		$('#'+serialNumberID).find('option').remove().end();
		$.each(serialNumberOptions.split(","), function(index,value) {
			 $('#'+serialNumberID).append('<option value='+value.trim()+'>'+value.trim()+'</option>');
		});
		$('.selectpicker').selectpicker('refresh');
	}
});
