var flagUpload=false;
var toggleBtnClicked=false;
jQuery(document).ready(function(){

			/*Start - Showing error details button on reload UAT-556*/
			if($('#errorDetailMSG').length>0){
				var errorMsglength=$('#errorDetailMSG').val().length;
				var errorMsg=$('#errorDetailMSG').val().trim('');
				var errorMsgSliced=errorMsg.slice(1,errorMsglength-1).trim('');
				var errorMsgList=errorMsgSliced.split(',');
				if(errorMsgSliced.length>0){
					$('#errorMultiCart').show();
					for(var i=0;i<errorMsgList.length;i++){
						$('#error-detail-popup .error-content').append(errorMsgList[i].split('=')[1]+"<br/>");
					}
				}
				else{
					$('#errorMultiCart').hide();
				}
			}
			/*End  - Showing error details button on reload  UAT-556*/

			ACC.addToCartHome.bindAll();
			dataObj = new Object();
			$(document).on("click",".qtyUpdateTextBox1", function(event){
			console.log("test id :"+$(this).attr('id'))
			var deviceName=$("#currViewScreenName").val();
			var quantity = $('[name=quantity]').val();
            if(quantity.trim()== ""){

		            }
            else{
		            	var productCode = $(this).attr('entryNum');
				        var sub = '#updateCartForm'+productCode+'_'+deviceName;
		        	/*AAOL-4068/AAOL-6550*/
				        if(QTY_CHECK!=""){
				        	if(quantity>Number(QTY_CHECK)){
					        	   $('#newQuantitypopup').modal('show');
					        	   $(document).off('click', '#back-btn');
									$(document).on('click', '#back-btn',function(e) {
										$('#newQuantitypopup').modal('hide');
										loadingCircleShow("hide");
										return false;
									});

									$(document).off('click', '#continue-btn');
									$(document).on('click', '#continue-btn',function(e) {
										   loadingCircleShow("show");
										$('#newQuantitypopup').modal('hide');
										if(deviceName=='mobile'){
							            	 $(sub).append(jQuery('<input>', {
								 					'name' : 'CSRFToken',
								 					'value' : ACC.config.CSRFToken,
								 					'type' : 'hidden'
								 				})).submit();
							            }else{
							            	$(sub).submit();
							            }

									});

					           }else{
			        	   if(deviceName=='mobile'){
			            	 $(sub).append(jQuery('<input>', { //  Account UID input created
					 					'name' : 'CSRFToken',
					 					'value' : ACC.config.CSRFToken,
					 					'type' : 'hidden'
					 				})).submit();
				            }else{
				            	$(sub).submit();
				            }

			            	//


					           }
				        }else{
				        	if(deviceName=='mobile'){
				        		$(sub).append(jQuery('<input>', { //  Account UID input created
				        			'name' : 'CSRFToken',
				        			'value' : ACC.config.CSRFToken,
				        			'type' : 'hidden'
				        		})).submit();
				        	}else{
				        		$(sub).submit();
				        	}

				        	}

		            }
				});

			$(".addToCartBtnHide").hide();
			 if($('#ordersTable').length) {
				 $(".addToCartBtnHide").show();
			    }

			var numberOfProductLines = parseInt($("#numberOfProductLines").val(), 10);
			$("#addToOrder").click(function(e){
				loadingCircleShow("show");
				var dataObj = new Object();
				   var error1='Please enter a valid product code';
					var error = '<label for="productId0" class="error">Please enter a valid product code and quantity.</label>';
					var error = 'Please enter a valid product code and quantity.';
					$("#productQuantityForm").find("div.registersucess").text("");
					$("#productQuantityForm").find("div.registerError").text("");
					if($.trim($("#qty").val())=="" && $.trim($("#productCode").val())==""){
	                   $("#productQuantityForm").find("div.registerError").text(error);
	                   $("#productQuantityForm").trigger("reset");
	                   loadingCircleShow("hide");
	                 return ;
					}else if($.trim($("#qty").val())!="" && $.trim($("#productCode").val())==""){
						 $("#productQuantityForm").find("div.registerError").text(error1);
						 $("#productQuantityForm").trigger("reset");
						 loadingCircleShow("hide");
		                 return ;
					}



				$("#errorQuickCart").hide();
	/* Modified by Vijay Start*/
				dataObj.productCode=$("#productCode").val();
				dataObj.qty=$("#qty").val();
				var allValid = true;
				var productAdded = false;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/buildorder/productValidate',
					data: dataObj,
					success: function (data) {
							if(data){
										jQuery.ajax({
											type: "POST",
											url:  ACC.config.contextPath +'/buildorder/addItem',
											data: dataObj,
											success: function (data) {
												 $("#productQuantityForm").trigger("reset");
												    location.reload();
													//by naga
													$("#productCode").val("");
													 $("#qty").val("");
												    loadingCircleShow("hide");
											}
										});// addItem Ajax End
								}else{  // Fixed for GTUX_015
									 $("#productQuantityForm").find("div.registerError").text("Enter product code "+ $("#productCode").val() +"is invalid."+error1);
									 $("#productCode").val("");
									 $("#qty").val("");
									 loadingCircleShow("hide");
									 return ;
								}
						}// productValidate Sucess End


			});// productValidateAjai call End

			});// Add to Order End

		/*Modified by Vijay End*/

			$(".removeProd").click(function(e){
				var dataObj = new Object();
				loadingCircleShow("show");
				var prdCode = $(this).attr('name');
				dataObj.productCode=prdCode;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/buildorder/deleteItem' ,
					data: dataObj,
					success: function (data) {
						 location.reload();
						 loadingCircleShow("hide");
					}
			});
			});


			$(".addtoCart").click(function(e){
				loadingCircleShow("show");
				$("#errorQuickCart").hide();
				var numberOfProdSelected = 0;
				var selectedProdCodes = [];
				var prodId_Qtys =$("#productId0").val()+":"+ $("#quantity0").val();
				selectedProdCodes.push($("#productId0").val());
				for (var int=1;int<=numberOfProductLines;int++) {
					if($("#productId"+int).val() != "" && $("#productId"+int).val() != undefined){
						prodId_Qtys+=","+$("#productId"+int).val()+":"+ $("#quantity"+int).val();
						selectedProdCodes.push($("#productId"+int).val());

					}
					numberOfProdSelected++;
				}

				//check isContractProduct alone or not
				if(numberOfProdSelected > 0) {
					jQuery.ajax({
						url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
						async: false,
						success : function(data) {
							var nonContractProductInCart = data.nonContractProductInCart;
							var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
							if (nonContractProductInCart === nonContractProductInSelectedList) {
								ACC.addToCartHome.submitProductCart(prodId_Qtys);
							} else {
								ACC.addToCartHome.confirmDialog(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
							}
						}
					});
				}
			});

			$("#errorQuickCart").click(function(e){
				  $.colorbox({
						html: $("#errorDetailsAddToCart").html(),
						height: 'auto',
						width: '492px',
						overlayClose: false,
						escKey:false
					});
			});

/*	Quick Add to Cart Home pop-up Starts*/

			$(".quickAddtoCartClick").click( function(e) {
				if($('#errorDiv').attr("class") == "hide"){
					$('#quickaddcart-popup .modalcls').show();
					$('#quickaddcart-popup').modal('show');
				}else{
					$('#errorDiv').addClass("hide");
					$('#quickaddcart-popup .modalcls').show();
					$('#quickaddcart-popup').modal('show');
				}
			});


			$("#contractCancelBtn").click(function(e) {
				$('#quickaddcart-popup').modal('hide');
				clearDataQuickaddtoCart();
			});

	$("#addToCartForm_1").click(function(e) {
		$(".errMsgHolder").hide();
		$("#quickaddcart-popup .modalcls").css("width","500px")
		var numberOfProdSelected = 0;
		var selectedProdCodes = [];
		var quantitylimit=false;
		var prodIds;
		var quanitycheck=false;//AAOL-6377
			var count=0;
			$("span.prod").each(function(i){
				if($(this).find("input").val()!=""){
					count++;
				}

			});


			if(count==0){
				$("#productId1").focus();
				$("#errorMsgHolder_1").hide();
				$("#errorDiv_1").hide();
				$("#msg_1").html("");
				$('#quickaddcart-popup .modalcls').width(500);
				return false;
			}

			if(jQuery("#productQuantityForm").valid()){
				if($("#productId1").val() != ""){
				var prodId_Qtys =$("#productId1").val()+":"+ $("#quantity1").val()+":1";
				if(QTY_CHECK!=""){
					if($("#quantity1").val()>Number(QTY_CHECK)){
						quantitylimit=true;
					}
				}

				selectedProdCodes.push($("#productId1").val());
				prodIds=$("#productId1").val()+",";
				numberOfProdSelected++;
				for (var int=2;int<=numberOfProductLines;int++) {
					if($("#productId"+int).val() != ""){
						numberOfProdSelected++;
						prodId_Qtys+=","+$("#productId"+int).val()+":"+ $("#quantity"+int).val()+":"+int;
						if(QTY_CHECK!=""){
							if($("#quantity"+int).val()>QTY_CHECK){
								quantitylimit=true;
							}
						}

						selectedProdCodes.push($("#productId"+int).val());
						prodIds=$("#productId"+int).val()+",";
					}else{
						$("#errorMsgHolder_"+int).hide();
						$("#errorDiv_"+int).hide();
						$("#msg_"+int).html("");
					}
				}

				//Changes for AAOL-4068/AAOL-6550
				if(quantitylimit==true){
					 quanitycheck();

				}else{
					addTocartAjax();
				}
				function quanitycheck(){
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
					addTocartAjax();

				});


				/*AAOL-6377 start*/
				var prdIdQty='';
				var prdCodes='';
				var prodQnt=1;
				var prodCodes=[];
				$('#quickaddcart-popup .quicart-row-gap').each(function(){
					if($(this).find('.prod-number').val().trim()!=''){
						prdCodes+=","+$(this).find('.prod-number').val();
						if($(this).find('.prod-quanity').val().trim('')!=''){
							prdIdQty=$(this).find('.prod-number').val()+":"+$(this).find('.prod-quanity').val();
						}
						else{
							prdIdQty=$(this).find('.prod-number').val()+":"+prodQnt;
						}
						prodCodes.push(prdIdQty);
						if(QTY_CHECK!=""){
							if($(this).find('.prod-quanity').val()>Number(QTY_CHECK)){
								quanitycheck=true;
							}
						}

					}
				});

				var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(prodCodes);
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
					console.log("useer entry prodIds " +prodCodes );
					ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,prdCodes,prodQnt,prodCodes);//AAOL-6377 prodCodes contains all absolute and active seleceted items in the search page.
				}
				/*AAOL-6377 end*/

					function addTocartAjax(){
					if(numberOfProdSelected > 0) {
						jQuery.ajax({
							url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
							async: false,
							success : function(data) {
								var nonContractProductInCart = data.nonContractProductInCart;
								var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
								if (nonContractProductInCart === nonContractProductInSelectedList) {
									ACC.addToCartHome.quickAddToCartNonContractProductAddToCart(prodId_Qtys);
								} else {
									ACC.addToCartHome.confirmDialogQuickAddToCart(prodIds,prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList);
								}
							}
						});
					}
					}
				}
					else if($("#productId1").val() == "" && numberOfProdSelected == 0){
						$(".errMsgHolder").hide();
						$(".error-msg-red").addClass("hide");
						$("#productId1").focus();
					}else{
						$("#errorMsgHolder_1").hide();
						$("#errorDiv_1").hide();
						$("#msg_1").html("");
					}
				}


		});

		var arrrMax;


		$('.quickClose').click(function(){
			clearDataQuickaddtoCart();
		});

		function clearDataQuickaddtoCart(){
			$('.prod-number').val('');
			$('.prod-quanity').val('');
			$('.error-msg-red').hide();
			$('.errorMsgHolder_1').css('display','none');
			$('#quickaddcart-popup').find('.modal-dialog').css({width:'500px',
				 height:'auto',
			   'max-height':'100%'});
		}


		/*Quick add to cart Home End*/



			$("#addToCartForm_2").click(function(e) {
				console.log("calling obsolete popup");
				$('#priceInquiry-nodata-error').hide();
				loadingCircleShow("show");
				var numberOfProdSelected = 0;
				var selectedProdCodes = [];

				//$("#errorMultiCart").hide();
				var prodIds =$.trim($("#prodCode").val());
				/*AAOL-6377*/
				var prodQty=1;
				if($.trim($("#prodQty").val())!=""){
					prodQty =$.trim($("#prodQty").val());
				}

				var regex = new RegExp("^[0-9]+$|^$");
			    var key = prodQty;

				if(prodIds===""){
					$("#prodCode").focus();
					loadingCircleShow("hide");
					$('#noProduct').show();
					return false;

					if (!regex.test(key)) {
					       $('#noQty').show();
					       loadingCircleShow("hide");
					       return false;
					    }

				}else{
				var prodIdList=prodIds.split(",");
				$('#noProduct').hide();
				 $('#noQty').hide();

				if (!regex.test(key)) {
				       $('#noQty').show();
				       loadingCircleShow("hide");
				       return false;
				    }

				for (var int=0;int<=prodIdList.length;int++) {
					if($.trim(prodIdList[int]) !== ""){
						selectedProdCodes.push(prodIdList[int].toUpperCase()+":"+prodQty);/*AAOL-6377*/
						numberOfProdSelected++;
				}
				}

				}
				if(numberOfProdSelected > 0) {
					//if( true ){
						//AAOL-2406 start
						var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(selectedProdCodes);
							if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
								console.log("useer entry prodIds " +prodIds );
								ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,prodIds,prodQty,null);/*AAOL-6377*/
							}
							/*AAOL-4068/AAOL-6550*/
							if(QTY_CHECK!=""){
								if(prodQty>Number(QTY_CHECK)){
									 loadingCircleShow("hide");
									 quanitycheck();

								}else{
									addTocartAjax1();
								}
							}else{
								addTocartAjax1();
							}

							function quanitycheck(){
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
								   loadingCircleShow("show");
								$('#newQuantitypopup').modal('hide');
								addTocartAjax1();

							});
						//AAOL-2406 end
					//}else{



				function addTocartAjax1(){
					jQuery.ajax({
						url : ACC.config.contextPath + '/my-account/contract/isNonContractProduct?selectedProducts='+selectedProdCodes,
						async: false,
						success : function(data) {
							var nonContractProductInCart = data.nonContractProductInCart;
							var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
							if (nonContractProductInCart === nonContractProductInSelectedList) {
								ACC.addToCartHome.ValidateNonContractProductAddToCartWithQty(prodIds,prodQty);
							} else {
								ACC.addToCartHome.confirmDialogValidate(prodIds,nonContractProductInCart,nonContractProductInSelectedList);
							}
						}
					});
					//}
			}
				}
			});
}); //Document.ready function ends
			$("#errorMultiCart").click(function(e){
				e.preventDefault();
				$('#error-detail-popup .modal-body').html( );
				$('#error-detail-popup').modal('show');
			});
			$("#addToCartForm_3").click(function(e) {
				$("#errorTemplateCart").hide();
				$(".uploadMessagesSuccessOT").hide();
				var templateCode =$("#selTemplate").val();
				var dataObj = new Object();
				var allValid = true;
				var productAdded = false;
				var errorMsg = "";
				dataObj.templateCode = templateCode;
						jQuery.ajax({
							type: "POST",
							url:  ACC.config.contextPath +'/home/addToCartFromTemplate' ,
							data: dataObj,
							success: function (data) {
								$.each(data.cartData,function(){
									$.each(this, function(product,msg) {
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
									$("#errorDetailsAddToCart").find("ul li").remove();
									$("#errorTemplateCart").show();
									errorMsg = errorMsg.substring(1, errorMsg.length);
									var msgArray = errorMsg.split('|');
									$.each(msgArray,function(index,element)
									{
										$("#errorDetailsAddToCart").find("ul").append("<li>"+element+"</li>");
									})
								} else {
									$("#errorTemplateCart").hide();
								}
								if(productAdded){
									$('#cart_popup').hide();
									$('#cart_popup').html(data.cartPopupHtml);

									$('#add_to_cart_close').click(function(e) {
										e.preventDefault();
										$('#cart_popup').hide();
									});
									$('#cart_popup').fadeIn();
									setTimeout(function(){$('#cart_popup').fadeOut();}, 5000);

									var updateMiniCartComponet = $('#lineInput').val();
									$("#lineItems").html(updateMiniCartComponet);
									$("#cart_header").addClass("shoppingcart");
									$("#cart_header").removeClass("shoppingcartDisabled");
									$("#cart_header .cart_content").attr("id","cart_content");
									$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
									ACC.cartpopup.bindCartPop();
									$("#mltiAddToCartForm").trigger("reset");

								}
							}
						});
			});

			$("#errorTemplateCart").click(function(e){
				  $.colorbox({
						html: $("#errorDetailsAddToCart").html(),
						height: 'auto',
						width: '492px',
						overlayClose: false,
						escKey:false
					});
			});





			$("#startNewButton").click(function(e) {
				jQuery.ajax({
					type: "GET",
					url:  ACC.config.contextPath +'/home/startNewOrder' ,
					success: function (data) {
						if(data)
						{
							$("#startNewOrderpopup").modal('show');
							$("#newOrderOk").click(function(e) {
								var keepItems = $("#keepItems").is(':checked');
								var deleteItems = $("#deleteItems").is(':checked');
								var dataObj = new Object();
								dataObj.keepItems =keepItems;
								dataObj.deleteItems =deleteItems;
								jQuery.ajax({
									type: "POST",
									url:  ACC.config.contextPath +'/home/startNewOrder' ,
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
			$("#orderFromTemplate").click(function(e) {
				window.location.href = ACC.config.contextPath + '/templates';
			});

			$(".requestPriceQuote").click(function(e) {
				jQuery.ajax({
					type: "GET",
					url:  ACC.config.contextPath +'/home/requestPriceQuote' ,
					success: function (data) {
						if(data)
						{
							$("#newQuotepopup").modal('show');
							$("#newQuoteOk").click(function(e) {
								var keepItems = $("#keepItems").is(':checked');
								var deleteItems = $("#deleteItems").is(':checked');
								var dataObj = new Object();
								dataObj.keepItems =keepItems;
								dataObj.deleteItems =deleteItems;
								jQuery.ajax({
									type: "POST",
									url:  ACC.config.contextPath +'/home/requestPriceQuote' ,
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

/*$("#productQuantityForm").validate({
				rules:{
					productId1:{
						required:{
							depends:function(){
								return $("#quantity1").val();
							}
						}
					},
					productId2:{
						required:{
							depends:function(){
								return $("#quantity2").val();
							}
						}
					},
					productId3:{
						required:{
							depends:function(){
								return $("#quantity3").val();
							}
						}
					}
				},
				messages:{
					quantity1:{
						digits:"Quantity in Numbers Only!"
					},
					quantity2:{
						digits:"Quantity in Numbers Only!"
					},
					quantity3:{
						digits:"Quantity in Numbers Only!"
					}
				},
				errorPlacement: function(error, element) {
					error.appendTo($('#productQuantityForm .registerError'));
				},
				onfocusout: false,
				focusCleanup: false
			});*/


			$(".moreBroadCastMessage").click(function(e) {
				var broadCastId =$(this).parent().find("input").val();
				var dataObj = new Object();
				dataObj.broadCastId = broadCastId;
						jQuery.ajax({
							type: "POST",
							url:  ACC.config.contextPath +'/home/getBroadCastContent' ,
							data: dataObj,
							async: false,
							success: function (data) {
								$.each(data,function(type,content){
								if(type=='ALERT'){
									$("#alert").show();
									$("#info").hide();
									$("#success").hide();
									$("#alert").find("p").html(content);
								} else if(type=='NOTIFICATION'){
									$("#info").show();
									$("#alert").hide();
									$("#success").hide();
									$("#info").find("p").html(content);
								} else {
									$("#success").show();
									$("#info").hide();
									$("#alert").hide();
									$("#success").find("p").html(content);
								}
							})
							}
						});
			});
			/*$(".moreBroadCastMessage").click(function(e){
				  $.colorbox({
						html: $("#broadCastPopUp").html(),
						height: 'auto',
						width: '492px',
						overlayClose: false,
						escKey:false
					});
			});
			*/
			/*$("#placeorderfile").click(function(e) {
				loadingCircleShow("show");
				$('#uploadFileFormHome').ajaxSubmit({contentType:"text/html",accept:"text/html",
					success:function(data){
						loadingCircleShow("hide");
						data = JSON.parse(data);
						$(".uploadfilehome").MultiFile("reset");
						$("#uploadMessagesInstruction").hide();
						$(".buttonContainer .MultiFile-list").hide();
				    	$("#placeorderfileSpn").hide();
				    	var responseDataVal = undefined;
				    	var containsError= false;
						var count = 0;
							for(var key in data) {
							count= count+1;
							}
							$.each(data, function(i, field){
								responseDataVal = field.toLowerCase();
					        });
							if ("empty" in data && count==1){
								$(".uploadMessagesEmpty").show();
								return false;
							}
							else if ("success" in data && "showMinQtyMsg" in data  && count == 2 && data.showMinQtyMsg == "false"){

								$(".uploadMessagesSuccess").show();

								$("#downLoadCartSpn").show();
								$("#lineItems").html(" "+data["success"]);
								$("#cart_header").addClass("shoppingcart");
								$("#cart_header").removeClass("shoppingcartDisabled");
								$("#cart_header .cart_content").attr("id","cart_content");
								$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
								ACC.cartpopup.bindCartPop();
								return false;
							}


							else if ("success" in data && "showMinQtyMsg" in data && count==2 && data.showMinQtyMsg == "true" ){
								var infoMsgLayer = $('.uploadMinMessagesSuccess');
								infoMsgLayer.show("slow");
								$(".uploadMessagesSuccess").show();
								infoMsgLayer.delay(7000).fadeOut("slow");

								$("#downLoadCartSpn").show();
								$("#lineItems").html(" "+data["success"]);
								$("#cart_header").addClass("shoppingcart");
								$("#cart_header").removeClass("shoppingcartDisabled");
								$("#cart_header .cart_content").attr("id","cart_content");
								$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
								ACC.cartpopup.bindCartPop();
								return false;
							}else if(responseDataVal !== undefined && responseDataVal=== 'success' ){
								var infoMsgLayer = $('.uploadMinMessagesSuccess');
								//infoMsgLayer.show("slow");
								//$(".uploadMessagesSuccess").show();
								//infoMsgLayer.delay(7000).fadeOut("slow");
								$("#uploadMessagesInstruction").hide();
								$("#downLoadCartSpn").show();
								$("#lineItems").html(" "+data[responseDataVal.toLowerCase()]);
								$("#cart_header").addClass("shoppingcart");
								$("#cart_header").removeClass("shoppingcartDisabled");
								$("#cart_header .cart_content").attr("id","cart_content");
								$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
								ACC.cartpopup.bindCartPop();
								return false;
							}
						 else {
							 $("#uploadMessagesInstruction").hide();
							$(".uploadMessagesError").show();
							$(".uploadMessagesEmpty").hide();
							if ("success" in data)
							{
								$("#lineItems").html(" "+data["success"]);
								$("#cart_header").addClass("shoppingcart");
								$("#cart_header").removeClass("shoppingcartDisabled");
								$("#cart_header .cart_content").attr("id","cart_content");
								$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
								ACC.cartpopup.bindCartPop();
								return false;
							}
							var markup = "<div class=\"lightboxtemplate\">"
								markup += "<H2>ERROR DETAILS</H2>";
								markup += "<div class=\"sectionBlock body popupButtonWrapper uploadFile\">"
								markup += "<div class=\"globalError\">";
								markup +="<p>";
								markup +="One or more product codes entered are invalid or ineligible for purchase.";
								markup +="</p>";
								markup +="</div>";
								markup +="<div class=\"scroll\">";
								markup +="<ul>";
								for(var key in data) {
									if(key != "success" && key != "showMinQtyMsg")
									{
										markup += "<li>";
										markup += data[key];
										markup += "</li>";
									}
								}
								markup += "</ul></div></div></div>";
							$("#errorDetailsSpn").show().click(function(){
								$("#addtocartmsgpopup").modal("show");
								$("#addtocartmsgpopup .modal-body").html(markup);
							});
						}
					}
				, error:function(data){
					loadingCircleShow("hide");
					}
				});
			});*/
			$("#placeorderfile").click(function(e) {
				var data = new FormData();
				 var IDs = $(".MultiFile-wrap input") // find MultiFile-wrap ->input attribute
				  .map(function() { return this.id; }) // convert to set of IDs
				  .get(); // convert to instance of Array (optional)

				 $.each(IDs, function( index, value ) {
					 console.log( "index : "+index + " and value :" + value );
					 jQuery.each(jQuery('#'+value)[0].files, function(i, file) {
						data.append('uploadmultifilehome', file);
					});
				});

				 $('#uploadFileFormHome').ajaxSubmit({type: "POST",contentType:"text/html",accept:"text/html",url:ACC.config.contextPath +'/home/getFileUploadQuantity',
						success: function (response) {
							if(response){
								$('#newQuantitypopup').modal('show');
							}else{

								jQuery.ajax({
									url : ACC.config.contextPath + '/home/isNonContractProduct',
									data: data,
									cache: false,
									contentType:false,
									processData: false,
									accept:"text/html",
									type: "POST",
									async: false,
									success : function(data) {
										var nonContractProductInCart = data.nonContractProductInCart;
										var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
										console.log("nonContractProductInCart : "+ nonContractProductInCart +" and nonContractProductInSelectedList :"+ nonContractProductInSelectedList);
										 if (nonContractProductInCart === nonContractProductInSelectedList) {
											 ACC.addToCartHome.submitPlaceorderfile();
										} else {
											ACC.addToCartHome.confirmPlaceOrderFile(nonContractProductInCart,nonContractProductInSelectedList);
										}
									}
								});
							}//end of else
						}//end of success
				 });
				/*AAOL-4068/AAOL-6550*/
				 $(document).off('click', '#back-btn');
				 $(document).on('click', '#back-btn',function(e) {
						$('#newQuantitypopup').modal('hide');
						//loadingCircleShow("hide");
						return false;
					});
				 $(document).off('click', '#continue-btn');
				 $(document).on('click', '#continue-btn',function(e) {
						$('#newQuantitypopup').modal('hide');
						jQuery.ajax({
							url : ACC.config.contextPath + '/home/isNonContractProduct',
							data: data,
							cache: false,
							contentType:false,
							processData: false,
							accept:"text/html",
							type: "POST",
							async: false,
							success : function(data) {
								var nonContractProductInCart = data.nonContractProductInCart;
								var nonContractProductInSelectedList = data.nonContractProductInSelectedList;
								console.log("nonContractProductInCart : "+ nonContractProductInCart +" and nonContractProductInSelectedList :"+ nonContractProductInSelectedList);
								 if (nonContractProductInCart === nonContractProductInSelectedList) {
									 ACC.addToCartHome.submitPlaceorderfile();
								} else {
									ACC.addToCartHome.confirmPlaceOrderFile(nonContractProductInCart,nonContractProductInSelectedList);
								}
							}
						});

					});


				$('#contractPopuppage .clsBtn').click(function(){
					loadingCircleShow("hide");  //contract pop up close event
				});

			});
$(".quickAddtoCartClick").click( function(e) {
	$('#quickaddcart-popup').modal('show');

});


$("#RemoveCartData").click( function(e) {
	loadingCircleShow("show");
	jQuery.ajax({
		url : ACC.config.contextPath + '/cart/clearCart',
		type : 'GET' ,
		success : function(data) {
			window.location.href = ACC.config.contextPath + '/cart';
		}

	});
});

var width = $(window).width();
var winHeight = $(window).innerHeight();
var panelArray =new Array();
$('.orderstatuspage .centered').each(function(){
	 panelArray.push($(this).innerHeight());
})

var panelHeight=Math.max.apply(Math,panelArray);

ACC.addToCartHome = {

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
					console.log(data);
					window.location.href = ACC.config.contextPath +'/cart';
					loadingCircleShow("hide");

				}
			});
	},
	confirmDialog :function(prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');

		//$('#cancel-btn-addtocart0').click( function(e) {
		$(document).off('click', '#cancel-btn-addtocart0').on('click', '#cancel-btn-addtocart0',function(e) {
			ACC.addToCartHome.callback(prodId_Qtys,false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});

		//$('#accept-btn-addtocart0').click( function(e) {
		$(document).off('click', '#accept-btn-addtocart0').on('click', '#accept-btn-addtocart0',function(e) {
			ACC.addToCartHome.callback(prodId_Qtys,true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	callback : function (prodId_Qtys,value,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	ACC.addToCartHome.submitProductCart(prodId_Qtys);
	    	loadingCircleShow("hide");
	    } else {
	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.addToCartHome.removeNonContractProduct(prodId_Qtys)
	    	}
	    	else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product
	    		console.log("nothing to perform becasue dont want to add this non contract product in the contract product");
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	removeNonContractProduct : function(prodId_Qtys){
		jQuery.ajax({
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {
				ACC.addToCartHome.submitProductCart(prodId_Qtys);
				loadingCircleShow("hide");
			}
		});
	},
	submitPlaceorderfile  : function(){
		//$("#placeorderfile").click(function(e) {
			loadingCircleShow("show");
			$('#uploadFileFormHome').ajaxSubmit({contentType:"text/html",accept:"text/html",
			success:function(data){
				loadingCircleShow("hide");
				data = JSON.parse(data);
				$(".uploadfilehome").MultiFile("reset");
				$("#uploadMessagesInstruction").hide();
				$(".buttonContainer .MultiFile-list").hide();
		    	$("#placeorderfileSpn").hide();
		    	var responseDataVal = undefined;
		    	var containsError= false;
				var count = 0;
					for(var key in data) {
					count= count+1;
					}
					/*$.each(data, function(i, field){
						responseDataVal = field.toLowerCase();
			        });*/

					$.each(data, function(key, field){
						if(field === 'SUCCESS') {
							responseDataVal = field.toLowerCase();
						}
						if(key === 'totalCartCount') {
							totalCartCount = field;
						}
			        });
					//console.log("responseDataVal : "+responseDataVal + " and totalCartCount : "+totalCartCount);
					if ("empty" in data && count==1){
						$(".uploadContent").show();
						$(".uploadMessages").show();
						$(".uploadMessagesEmpty").show();
						return false;
					}
					else if("EmptySheet" in data){
						showErrorPopup(data);
						return false;
					}
					else if ("success" in data && "showMinQtyMsg" in data  && count == 2 && data.showMinQtyMsg == "false"){

						$(".uploadMessagesSuccess").show();

						$("#downLoadCartSpn").show();
						$("#lineItems").html(" "+data["success"]);
						$("#cart_header").addClass("shoppingcart");
						$("#cart_header").removeClass("shoppingcartDisabled");
						$("#cart_header .cart_content").attr("id","cart_content");
						$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
						ACC.cartpopup.bindCartPop();
						return false;
					}
					else if ("success" in data && "showMinQtyMsg" in data && count==2 && data.showMinQtyMsg == "true" ){
						var infoMsgLayer = $('.uploadMinMessagesSuccess');
						infoMsgLayer.show("slow");
						$(".uploadMessagesSuccess").show();
						infoMsgLayer.delay(7000).fadeOut("slow");
						$("#downLoadCartSpn").show();
						$("#lineItems").html(" "+data["success"]);
						$("#cart_header").addClass("shoppingcart");
						$("#cart_header").removeClass("shoppingcartDisabled");
						$("#cart_header .cart_content").attr("id","cart_content");
						$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
						ACC.cartpopup.bindCartPop();
						return false;
					}else if(responseDataVal !== undefined && responseDataVal=== 'success' ){
						console.log("responseDataVal : "+responseDataVal);
						console.log("data : "+data);
						window.location.href = ACC.config.contextPath +'/cart';
						loadingCircleShow("hide");
					}
				 else {
					 //modified for file upload error message popup
					 $("#uploadMessagesInstruction").hide();
					$(".uploadMessagesError").show();
					$(".uploadMessagesEmpty").hide();
					if ("success" in data)
					{
						/*$("#lineItems").html(" "+data["success"]);
						$("#cart_header").addClass("shoppingcart");
						$("#cart_header").removeClass("shoppingcartDisabled");
						$("#cart_header .cart_content").attr("id","cart_content");
						$(".cart_content").find("a").attr("href", $("#hddnCartUrl").val());
						ACC.cartpopup.bindCartPop();
						return false;*/
						window.location.href = ACC.config.contextPath +'/cart';
						loadingCircleShow("hide");
					}
					$.each(data, function(key, field){
						if(field === 'SUCCESS') {
							responseDataVal = field.toLowerCase();
						}
						if(key === 'totalCartCount') {
							totalCartCount = field;
						}
			        });

					//Error markup code moved to showErrorPopup()
					showErrorPopup(data);
					showReplacementPopup(data);
					//refresh cart, in case of partial errors
					$('#items-msg').text(totalCartCount);
				}
			}
			, error:function(data){
				loadingCircleShow("hide");
				}
			});
		//});
	},
	confirmPlaceOrderFile :function(nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');
		//$('#cancel-btn-addtocart0').click( function(e) {
		$(document).off('click', '#cancel-btn-addtocart0').on('click', '#cancel-btn-addtocart0',function(e) {
			ACC.addToCartHome.callbackPlaceOrderFile(false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});

		//$('#accept-btn-addtocart0').click( function(e) {
		$(document).off('click', '#accept-btn-addtocart0').on('click', '#accept-btn-addtocart0',function(e) {
			ACC.addToCartHome.callbackPlaceOrderFile(true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	/**
	 *  @param value
	 *	if value = true : submitPlaceorderfile()
	 *	else value = false : removeNonContractProductPlaceOrderFile()
	 */
	callbackPlaceOrderFile : function (value,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	ACC.addToCartHome.submitPlaceorderfile();
	    	loadingCircleShow("hide");
	    } else {
	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.addToCartHome.removeNonContractProductPlaceOrderFile();
	    	}
	    	else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product
	    		console.log("nothing to perform becasue dont want to add this non contract product in the contract product");
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	/**
	 *  Remove product which is already non contract
	 */
	removeNonContractProductPlaceOrderFile : function(){
		jQuery.ajax({
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {
				ACC.addToCartHome.submitPlaceorderfile();
				loadingCircleShow("hide");
			}
		});
	} ,

	ValidateNonContractProductAddToCart : function(prodIds){

		var dataObj = new Object();
		var allValid = true;
		var productAdded = false;
		var errorMsg = "";
		dataObj.source = "addToCartForm_2";
		dataObj.prodIds = prodIds;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/multiAddToCart' ,
					data: dataObj,
					 async: false,
					success: function (data) {
						$.each(data.cartData,function(){
							$.each(this, function(product,msg) {
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
							$(".error-content div").remove();
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
							var success = SUCCESS_ORDER
							var domSuccessElement = $("#mltiAddToCartForm").find("div.registersucess");
							domSuccessElement.html(success);
							domSuccessElement.show();
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
							if ($(".addToCart").length > 0)
							{
								var cartValue= $("#items-msg").text();
								if(prodQty==0)
								{
									cartValue=+cartValue+1;
								}
								else
								{
									cartValue=+cartValue+(+prodQty);
								}
								$("#items-msg").text(cartValue);
								$("#laodingcircle").hide();
							}
							else
							{
							setTimeout(function(){domSuccessElement.slideUp(500);
										window.location.href = ACC.config.contextPath +'/cart';
								$(".modal-backdrop in modal-stack").hide();
								}, 10000);
							}
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
						}
					}
				});

	},
	ValidateNonContractProductAddToCartWithQty : function(prodIds,prodQty){
		var dataObj = new Object();
		var allValid = true;
		var productAdded = false;
		var errorMsg = "";
		dataObj.source = "addToCartForm_2";
		dataObj.prodIds = prodIds;
		dataObj.prodQty = prodQty;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/multiAddToCartWithQty' ,
					data: dataObj,
					 async: false,
					success: function (data) {
						$.each(data.cartData,function(){
							$.each(this, function(product,msg) {
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
							$(".error-content div").remove();
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
							if(prodQty === "")
								{
									var success = SUCCESS_ORDER

							domSuccessElement.html(success);
							domSuccessElement.show();
								}
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
							if ($(".addToCart").length > 0)
							{
								var cartValue= $("#items-msg").text();
								if(prodQty==0)
								{
									cartValue=+cartValue+1;
								}
								else
								{
									cartValue=+cartValue+(+prodQty);
								}
								$("#items-msg").text(cartValue);
								$("#laodingcircle").hide();
							}
							else
							{
							setTimeout(function(){domSuccessElement.slideUp(500);
										window.location.href = ACC.config.contextPath +'/cart';
								$(".modal-backdrop in modal-stack").hide();
								}, 10000);
							}
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
						}
					}
				});

	},
ValidateNonContractProductForceNewEntry : function(prodIds,prodQty,line_number){
		var dataObj = new Object();
		var allValid = true;
		var productAdded = false;
		var errorMsg = "";
		dataObj.source = "addToCartForm_2";
		dataObj.prodIds = prodIds;
		dataObj.prodQty = prodQty;
		dataObj.lineNumber = line_number;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/multiAddToCartWithQty?forceNewEntry='+true ,
					data: dataObj,
					 async: false,
					success: function (data) {
						$.each(data.cartData,function(){
							$.each(this, function(product,msg) {
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
							$(".error-content div").remove();
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
							if(prodQty === "")
								{
									var success = '<label for="productId1" class="success">The quantity of one or more items has been automatically set to the required shipping minimum.</label>';

							domSuccessElement.html(success);
							domSuccessElement.show();
								}
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
							if ($(".addToCart").length > 0)
							{
								var cartValue= $("#items-msg").text();
								if(prodQty==0)
								{
									cartValue=+cartValue+1;
								}
								else
								{
									cartValue=+cartValue+(+prodQty);
								}
								$("#items-msg").text(cartValue);
								$("#laodingcircle").hide();
							}
							else
							{
								setTimeout(function(){domSuccessElement.slideUp(500);
								window.location.href = ACC.config.contextPath +'/cart';
								$(".modal-backdrop in modal-stack").hide();
								}, 10000);
							}
							/*when successful do not redirect in case of cart page, update in case of PLP or SLP- Soumitra*/
						}
					}
				});

	},
	confirmDialogValidate :function(prodIds,nonContractProductInCart,nonContractProductInSelectedList){
		$('#contractpopup').modal('show');

		//$('#cancel-btn-addtocart0').click( function(e) {
		$(document).off('click', '#cancel-btn-addtocart0').on('click', '#cancel-btn-addtocart0',function(e) {
			ACC.addToCartHome.callbackValidate(prodIds,false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});

		//$('#accept-btn-addtocart0').click( function(e) {
		$(document).off('click', '#accept-btn-addtocart0').on('click', '#accept-btn-addtocart0',function(e) {
			ACC.addToCartHome.callbackValidate(prodIds,true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	callbackValidate : function (prodIds,value,nonContractProductInCart,nonContractProductInSelectedList) {
	    if (value) {
	    	ACC.addToCartHome.ValidateNonContractProductAddToCart(prodIds);
	    	loadingCircleShow("hide");
	    } else {
	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.addToCartHome.removeNonContractProductValidate(prodIds)
	    	}
	    	else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product
	    		console.log("nothing to perform becasue dont want to add this non contract product in the contract product");
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	removeNonContractProductValidate : function(prodIds){
		jQuery.ajax({
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {
				ACC.addToCartHome.ValidateNonContractProductAddToCart(prodIds);
				loadingCircleShow("hide");
			}
		});
	},

		quickAddToCartNonContractProductAddToCart : function(prodId_Qtys){

		var errorMsg = "";
		var dataObj = new Object();
		var allValid = true;
		var productAdded = false;
		var responseDataVal = undefined;
		var temp = prodId_Qtys.split(",");
		var prodEntry = [];
		prodId_Qtys = "";
		for(var i = 0 ; i< temp.length ; i++){
			prodEntry.push(temp[i].substr(temp[i].length-1));
			temp[i] = temp[i].substr(0,temp[i].length-2);
			if(ACC.formCheckFunc.isNotNullAndNotEmpty(prodId_Qtys) ){
			prodId_Qtys = prodId_Qtys+","+temp[i];
			}else{
				prodId_Qtys = temp[i];
			}
			//prodId_Qtys = prodId_Qtys+","+temp[i];
		}

		if(prodId_Qtys.substr(0,1) == ","){
			prodId_Qtys = prodId_Qtys.substr(1,prodId_Qtys.length-1);
		}
		dataObj.prodId_Qtys = prodId_Qtys;
				jQuery.ajax({
					type: "POST",
					url:  ACC.config.contextPath +'/home/addTocart' ,
					data: dataObj,
					success: function (data) {
						var errcount=0;
						$('#items-msg').text(data.totalUnitCount);
						$.each(data.cartModifications,function(i,data){
							errcount=prodEntry[i];
							$.each(this, function(key,value) {
								if( (isNaN(value) || value === true || value === false ) && key != 'entry' ){
									$(".errMsgHolder").show();
									 if(key === 'error' && value === true){
										errorMsg = errorMsg +"|"+ value;
											  /* var error = '<label for= "productId'+errcount+'" class="error">'+VALID_CODE+'</label>';*/
										 var error = '<label  class="error">'+VALID_CODE+'</label>';
			                                   $("#errorMsgHolder_"+errcount).show();
			                                   $("#msg_"+errcount).html("");
			                                   $("#msg_"+errcount).html(error);
			                                   $('#errorDiv_'+errcount+' .panel').removeClass('panel-success').addClass('panel-danger');
			                                 $('#errorDiv_'+errcount).removeClass("hide");
			                                 $('#errorDiv_'+errcount).show();
			                                 ACC.addToCartHome.showErrorIncart('#quickaddcart-popup','click');
										allValid = false;

									 }else if(key === 'statusCode' && (value === "success" || value === "update") ){
										productAdded =true;
										/*var success1 = '<label for= "productId'+errcount+'" >Product added to cart.</label>';*/
										var success1 = '<label  >'+ PRODUCT_ADDED + '</label>';
										$("#errorMsgHolder_"+errcount).show();
		                                   $("#msg_"+errcount).html("");
		                                   $("#msg_"+errcount).html(success1);
		                                   $('#errorDiv_'+errcount).prev().find('.prod-number').val('');
		                                   $('#errorDiv_'+errcount).prev().find('.prod-quanity').val('');
		                                   $('#errorDiv_'+errcount+' .panel').removeClass('panel-danger').addClass('panel-success');
		                                   $('#errorDiv_'+errcount).removeClass("hide");
		                                   $('#errorDiv_'+errcount).show();
									 }else if(key === 'statusCode' && (value == 'basket.page.message.minQtyAdded' || value == 'basket.page.message.update') ){
										productAdded =true;
									/*var success1 = '<label for= "productId'+errcount+'" >Product added to cart.</label>';*/
										var success1 = '<label  >'+ PRODUCT_ADDED + '</label>';
									$("#errorMsgHolder_"+errcount).show();
		                               $("#msg_"+errcount).html("");
		                               $("#msg_"+errcount).html(success1);
		                               $('#errorDiv_'+errcount).prev().find('.prod-number').val('');
		                               $('#errorDiv_'+errcount).prev().find('.prod-quanity').val('');
		                               $('#errorDiv_'+errcount+' .panel').removeClass('panel-danger').addClass('panel-success');
		                               $('#errorDiv_'+errcount).removeClass("hide");
		                               $('#errorDiv_'+errcount).show();
									}
								}else{
									/*console.log("key : "+key);
									console.log("value : "+value);*/
								}
							});
							});
						if(allValid == true && productAdded == true )
						{
							window.location.href = ACC.config.contextPath +'/cart';

						}else{
						}
					}
				});

		},
	confirmDialogQuickAddToCart :function(prodIds,prodId_Qtys,nonContractProductInCart,nonContractProductInSelectedList){

		$('#quickaddcart-popup .modalcls').hide();
		$('#quickaddcart-popup').hide();
		$('#contractpopup').modal('show');

		//$('#cancel-btn-addtocart0').click( function(e) {
		$(document).on('click', '#cancel-btn-addtocart0',function(e) {
			$('#contractpopup').modal('hide');
			$('#quickaddcart-popup .modalcls').show();
			$('#quickaddcart-popup').modal('show');

			ACC.addToCartHome.callbackQuickAddToCart(prodIds,prodId_Qtys,false,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});


		//$('#accept-btn-addtocart0').click( function(e) {
		$(document).on('click', '#accept-btn-addtocart0',function(e) {
			$('#contractpopup').modal('hide');
			$('#quickaddcart-popup .modalcls').show();
			$('#quickaddcart-popup').show();
			$('#quickaddcart-popup').modal('show');

			ACC.addToCartHome.callbackQuickAddToCart(prodIds,prodId_Qtys,true,nonContractProductInCart,nonContractProductInSelectedList);
			e.preventDefault();
		});
	},
	callbackQuickAddToCart : function (prodIds,prodId_Qtys,value,nonContractProductInCart,nonContractProductInSelectedList) {

		$('#quickaddcart-popup .modalcls').show();
		$('#quickaddcart-popup').show();
		if (value) {
	    	ACC.addToCartHome.quickAddToCartNonContractProductAddToCart(prodId_Qtys);
	    	loadingCircleShow("hide");
	    } else {


	    	//need to call remove product which is already non contract
	    	if(nonContractProductInCart && !nonContractProductInSelectedList){
	    		ACC.addToCartHome.removeNonContractProductQuickAddToCart(prodId_Qtys)
	    	}
	    	else{
	    		//nothing to perform becasue dont want to add this non contract product in the contract product
	    		console.log("nothing to perform becasue dont want to add this non contract product in the contract product");
	    		loadingCircleShow("hide");
	    	}
	    }
	},
	removeNonContractProductQuickAddToCart : function(prodId_Qtys){
		jQuery.ajax({
			url : ACC.config.contextPath + '/my-account/contract/removeNonContractProduct',
			async: false,
			success : function(data) {
				ACC.addToCartHome.quickAddToCartNonContractProductAddToCart(prodId_Qtys);
				loadingCircleShow("hide");
			}
		});
	},
	/* Upload file functionality  starts here*/
	sizesHeight:function(height) {
		width = $(window).width();
		winHeight = $(window).innerHeight();
		//modified by lokesh dec 1
		if (width > 960) {
			$('.orderstatuspage .centered').each(function(){
				if(flagUpload){
					$(this).innerHeight(panelHeight+height+15);
				}
				else{
					$(this).innerHeight(panelHeight);
				}
			})
		}
		ACC.addToCartHome.uploadBodyHeight();
	 	},
	 	bindAll : function(){
	 		$("#uploadmultifilehome").change(function(){
				flagUpload=true;
				ACC.addToCartHome.sizesHeight($('.MultiFile-list').height());
			});

			$(".filenamebutton").click(function(){
				flagUpload=false;
				$("#filename").text("");
				ACC.addToCartHome.sizesHeight(0);
			});
			$(window).load(function() {
				ACC.addToCartHome.sizesHeight(0);
			});
			$(window).resize(function () {clearTimeout(this.id);this.id = setTimeout(ACC.addToCartHome.mobileSize, 500);});
			ACC.addToCartHome.removeA();
	 	},
	 	uploadBodyHeight :function(){
			  var leftHeight=$('#jnj-usr-details-menu').innerHeight();
			  var rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();

			  var productnameheight= $('.productspanel').innerHeight();
			  var productviewheight= $('.ps-current').innerHeight();

			  if(productviewheight<productnameheight){
			    $('.ps-current').removeAttr('style');
			    $('.ps-current').css('height',productnameheight+'px !important');
			  }

			  if(leftHeight==rightHeight){
				  $('#jnj-body-content').height('auto');
			  }
			  else if(leftHeight<rightHeight || toggleBtnClicked){
				  $('#jnj-usr-details-menu').innerHeight(rightHeight);
			  } else{
				  var bodyContentHeight=leftHeight-($('#jnj-header').innerHeight()+$('#jnj-footer').innerHeight());
			   	$('#jnj-body-content').innerHeight(bodyContentHeight);
			  }
		},
		mobileSize: function(){
			ACC.addToCartHome.sizesHeight(0);
		},
		showErrorIncart: function(poupId,flagVal){
		  var qcpopupwidth;
		  var flagError=false;
		  var arry=[];
		  $(".error-msg-red").each(function(){arry.push($(this).innerWidth())});
		  arrrMax=Math.max.apply(Math,arry);
		  $(".error-msg-red").each(function(){
			   if($(this).hasClass("hide")&&flagError==false){
				   qcpopupwidth=$(".first-col:eq(0)").innerWidth();
			   } else{
				    if(flagError==false){
				    	qcpopupwidth=$(".first-col:eq(0)").innerWidth()+arrrMax+40;
				    	flagError=true;
				    }
			   }
		  });

		  if(qcpopupwidth!=null){
			  if(flagVal=="click"){
				  	$(poupId).find('.modal-dialog').css({width:qcpopupwidth+'px',
						 height:'auto',
					   'max-height':'100%'});
			  } else{
				  $(poupId).find('.modal-dialog').css({width:'500px',height:'auto',  'max-height':'100%'});
			  }
		  }
	 	},
	 	isObsoleteProduct :function(selectedProdCodes){
			console.log("step0 isObsoleteProduct : "+selectedProdCodes );
			//var dataObj = new Object();
			var userList = [];
			var response;
			jQuery.ajax({
				url : ACC.config.contextPath + '/cart/isObsoleteProduct?selectedProducts='+selectedProdCodes,
				//type: "POST",
				async: false,
				success : function(data) {
					console.log("data.obsoleteProductList : "+data);
					response =  data.obsoleteProductList.substring(0,data.obsoleteProductList.lastIndexOf(","));
					console.log("data.obsoleteProductList : "+response);
	 	}
			});
			return response;

	 	},
	 	//AAOL-6377
	 	checkReplacementProductPopup : function (obsoleteProductList,prodIds,prodQty,allProdCodes){
	 		console.log("prodCodes" +allProdCodes);
	 		console.log("step1 checkReplacementProductPopup" );
	 		if(prodQty==0){
	 			prodQty = 1;
	 		}
	 		//test quantity
			/*AAOL-6377*/
			var ajaxurl = ACC.config.contextPath + '/cart/checkReplacementProductWithQty?obsoleteProductList='+obsoleteProductList;
			//var ajaxurl = ACC.config.contextPath + '/cart/checkReplacementProduct?obsoleteProductList='+obsoleteProductList;
			$.ajax({
			 // type: "GET",
			  async: false,
			  url: ajaxurl,
			  headers: {
					'Content-Type': 'application/json'
				},
			 // data: dataToSend,
			  success: function(data) {
				  console.log("data is>"+data);
				 $('#replacement-line-item-holder').html(data);
				 $('#replacementItemOrder-popup').modal('hide');
				 if($('.replacementListData tr').children().length==0){
					 $('#replacementItemOrder-popup').modal('hide');
					//AAOL-6377 start
					 ACC.addToCartHome.closeORCancel(prodIds,prodQty,allProdCodes);
					 console.log("prodCodes" +allProdCodes);
					//AAOL-6377 end
				 }
				 else
				 {
					 $('#replacement-line-item-holder').show();
				 }
				 $('#replacementItemOrder-popup').modal({backdrop: 'static',keyboard: false,show:true});
				 $('.clsBtn').click(function(){
					 $('.modal-backdrop').hide();
				 });
				 $("input.replacementItemSelection").each(function(){
					$(this).prop('checked',false);
				 });
				 $('.select-accnt-close').click(function(){
					//AAOL-6377 start
					 ACC.addToCartHome.closeORCancel(prodIds,prodQty,allProdCodes);
					 console.log("prodCodes" +allProdCodes);
					//AAOL-6377 end
				 });
				 //here need to handle the select all check based on order history page select all checkbox
				 $("#replacementSelectAll").change(function(){
					var id;
					if($(this).prop("checked")) {
						$("input.replacementItemSelection").each(function(){
							$(this).prop('checked',true);
							id = $(this).attr("data-name");
							$(".propProd-radio").removeAttr("disabled");//AAOL-6378
						});
					} else {
						$("input.replacementItemSelection").each(function(){
							$(this).prop('checked',false);
							id = $(this).attr("data-name");
							$("#replacementItemQty_"+id).attr("disabled", true);
							$(".propProd-radio").attr("disabled", true);
						});
					}
				 });
				 /** Single account select check-box click action **/
				 /*AAOL-6378 start*/
				 var rowIndex;
				 var propRadioIndex;
				 var radioRowIndex;
				 $('.propProd-radio').click(function(){
					 propRadioIndex=$('.propProd-radio').index(this);
					 radioRowIndex=$(this).attr('name').split('-')[1];

					 $('#replacementItemQtyTD_'+radioRowIndex+' .prod-number').each(function(){
						$(this).prop('disabled',true)
					 });

					if($(this).prop('checked')==true){
						 $('.replace-item-qty:eq('+propRadioIndex+')').removeAttr('disabled');
					}
				 });

				 $(".replacementItemSelection").change(function(){
					console.log("replacementItemSelection check box selected");
					var id;
					rowIndex=$('.replacementItemSelection').index(this)+1;

					$("#replacementSelectAll").prop('checked',true);

					if($(this).prop('checked')==true){
						$('.propProd-radio[name="row-'+rowIndex+'"]').each(function(){$(this).removeAttr('disabled');});

					}else{
						$('.propProd-radio[name="row-'+rowIndex+'"]').each(function(){$(this).prop('checked',false).prop('disabled',true);});
						$('#replacementItemQtyTD_'+rowIndex+' .replace-item-qty').prop('disabled',true);

					}

					$(".replacementItemSelection").each(function(){
						id = $(this).attr("data-name");
						if($(this).prop('checked')!=true){
							$("#replacementSelectAll").prop('checked',false);
							//$("#replacementItemQty_"+id).attr("disabled", true);
						}else{
							//$("#replacementItemQty_"+id).attr("disabled", false);
						}
					});
				 });
				  //AAOL-6377
				 /*AAOL-6378*/
				  ACC.addToCartHome.submitSelectedReplacement(prodIds,prodQty,allProdCodes);
				  console.log("prodCodes" +allProdCodes);
				 },
				  onClosed: function () {
			    },
			  dataType: 'html'
			});
		},
		//AAOL-6377 start
		closeORCancel : function(prodIds,prodQty,prodCodes){
			console.log("prodCodes" +prodCodes);
			//AAOL-6377 end
			console.log("step2.0 checkReplacementProductPopup" );
			var propProdId_Qtys = undefined;
			console.log("closeORCancel method invoked");
			ACC.addToCartHome.ValidateNonContractProductAddToCartWithQty(prodIds,prodQty);
		},
		//AAOL-6377
		submitSelectedReplacement : function(prodIds,prodQty,prodCodes){
			console.log("prodCodes" +prodCodes);
			$("#submit-replacementProd").click(function() {
				console.log("step2.0 submitSelectedReplacement" );

				if($(".replacementItemSelection:checked").length>0 && $(".propProd-radio:checked").length==0 || $(".replacementItemSelection:checked").length ==0){
					$('#obsolote-popup-error').show();
				}
				else{
					$('#obsolote-popup-error').hide();
					$('#replacementItemOrder-popup').modal('hide');

				}
				var accSelLength = $(".replacementItemSelection:checked").length;
				ACC.addToCartHome.sendSelectedReplacement(accSelLength,prodIds,prodQty,prodCodes);

			});
		},
		removeA : function( ){
			/*// Removes an element from an array.
			// String value: the value to search and remove.
			// return: an array with the removed element; false otherwise.
			Array.prototype.remove = function(orderItemNo) {
			var idx = prodIdList.indexOf(value);
			if (idx != -1) {
			    //
				console.log("testing : "+idx);
				return this.splice(idx, 1); // The second parameter is the number of elements to remove.
			}
			console.log("false ");
			return false;
			}*/
			Array.prototype.remove= function(){
			    var what, a= arguments, L= a.length, ax;
			    while(L && this.length){
			        what= a[--L];
			        while((ax= this.indexOf(what))!= -1){
			            this.splice(ax, 1);
			        }
			    }
			    return this;
			}
		},
		//AAOL-6377
		sendSelectedReplacement : function(accSelLength,prodIds,prodQty,allProdCodes){
			console.log("prodCodes" +allProdCodes);
			console.log("step2.1 checkReplacementProductPopup" );
			var replaceProdIds=undefined;
			var orderItemQty,propItemQty,oriPropItemQty=0;
			var hyLineItemNo,orderItemNo;

			var prodId_Qtys="";//AAOL-6377
			var jnjAddToCartCartFormList = [];
			var ajaxResponse = [];
			if (accSelLength === 0){
				$('#select-accnt-error-order-history').css('display','block');
				return false;
			}
			console.log("bf prodIds before split>" + prodIds);
			var prodIdList=prodIds.toString().split(",");
			console.log("bf prodIdList  " + prodIdList);
			//AAOL-6378
			var proposeItemRowIndex;
			$(".replacementItemSelection").each(function(){
				var propItemNo=[];
				var propProdId_Qtys;
				id = $(this).attr("data-name"); // using for popup index
				//AAOL-6378
				proposeItemRowIndex=$(".replacementItemSelection").index(this)+1;
				console.log("id : "+proposeItemRowIndex);
				if($(this).prop('checked') === true){
					var jnjAddToCartCartForm = new Object();
					hyLineItemNo = id; // this is using for hybris line litem no
					orderItemNo = $("#orderItemCd_"+id).text();
					orderItemQty = $("#orderItemQty_"+id).text();
					 /*AAOL-6378 start*/
					$('#replacementItemCd_'+proposeItemRowIndex+' .propProd-radio').each(function(i){
						if($(this).prop('checked')){
							propItemNo[0]=$(this).val();
						}
					});

					$('#replacementItemQtyTD_'+proposeItemRowIndex+' .replace-item-qty').each(function(i){
						if(!$(this).prop('disabled')){
							propItemQty = $(this).val();
						}
						 /*AAOL-6378 end*/
					});
					//propItemNo = $("#replacementItemCd_"+id).text();
					//propItemQty = $("#replacementItemQty_"+id).val();
					prodQty=propItemQty;//Soumitra: setting the prodQty as the propItemQty, so that the quantity suggested by user is added.
					oriPropItemQty = $("#origReplaceItemQty_"+id).val();
					console.log("orderItemNo : "+orderItemNo +" and orderItemQty : "+ orderItemQty +" and propItemNo : "+ propItemNo +" and propItemQty : "+ propItemQty + " and oriPropItemQty : "+oriPropItemQty);
				    //prodId_Qtys+=","+propItemNo+":"+propItemQty;
					prodIdList = prodIdList.remove(orderItemNo);
					//AAOL-6377 start
					if(allProdCodes!=null)
					{
						allProdCodes=allProdCodes.remove(orderItemNo+":"+orderItemQty);
						console.log(allProdCodes);
					}
					//AAOL-6377 end
					console.log("af prodIdList  " + prodIdList);
					//replace product collecting
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(replaceProdIds)){
						replaceProdIds = replaceProdIds+","+propItemNo;  // in controller using hyLineItemNo only updating the QTY not in product id
					}else{
						replaceProdIds = propItemNo;
					}
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(prodId_Qtys)){
						prodId_Qtys += ","+propItemNo+":"+propItemQty;
					}else{
						prodId_Qtys = propItemNo+":"+propItemQty;
					}
					jnjAddToCartCartForm.hybrisLineItemNo= hyLineItemNo;
					jnjAddToCartCartForm.proposedItemNo= propItemNo;
					jnjAddToCartCartForm.originalItemNo= orderItemNo;
					jnjAddToCartCartFormList.push(jnjAddToCartCartForm);
				}
			});
			console.log("replaceProdIds : "+replaceProdIds +" and final product ids after removed obsolete product : "+prodIdList);
			if(ACC.formCheckFunc.isNotNullAndNotEmpty(prodIdList)){
				prodIdList = prodIdList+","+replaceProdIds;  // in controller using hyLineItemNo only updating the QTY not in product id
			}else{
				prodIdList = replaceProdIds;
			}
			console.log("merged product list : "+prodIdList);
			/*
			 * invoking add to cart method to create a cart with user accepted product and available product
			 */
			console.log("  jnjAddToCartCartFormList : "+jnjAddToCartCartFormList);
			console.log("  prodIdList  : "+prodIdList+ " and prodQty : "+prodQty);
			//ACC.addToCartHome.ValidateNonContractProductAddToCartWithQty(prodIdList,prodQty);
			//AAOL-6377 start
			//merging with proposed product list start
			if(allProdCodes!=null){
				for(var i = 0 ; i< allProdCodes.length ; i++){
					var originalProd = allProdCodes[i];
					console.log("originalProd : "+originalProd);
					if(ACC.formCheckFunc.isNotNullAndNotEmpty(prodId_Qtys)){
						prodId_Qtys += ","+originalProd;
					}else{
						prodId_Qtys = originalProd;
					}
				}
			}

			ACC.addToCartHome.addObsoluteandActiveMultiProduct(prodIdList, prodId_Qtys);
			//AAOL-6377 end
			ACC.cartPage.updateProductLineItem(jnjAddToCartCartFormList);
		},
		//AAOL-6377 start
		addObsoluteandActiveMultiProduct : function (prodIdList, prodId_Qtys){
			console.log("prodId_Qtys"+prodId_Qtys);
			var temp = prodId_Qtys.split(",");
			//var prodEntry = [];
			prodId_Qtys = "";
			for(var i = 0 ; i< temp.length ; i++){

				console.log(" third Product : "+temp[i]);
				var prodIdandQty = temp[i].split(":");
				console.log("prodId"+prodIdandQty[0]);
				console.log("prodQty"+prodIdandQty[1]);
				ACC.addToCartHome.ValidateNonContractProductAddToCartWithQty(prodIdandQty[0],prodIdandQty[1]);

			}
			//AAOL-6377 end
		},

	/* Upload file functionality  Ends here*/
};


function showErrorPopup(data)
{
	var markup = "<div>";
	markup +="<ul>";
	//	for(var key in data) {
		$.each(data, function(key, field){
			if(key.toLowerCase() != "success" && key != "showMinQtyMsg" && key != "totalCartCount") {
				markup += "<li><b>";
				markup += field;
				markup += "</b></li>";
			}
		});  //}
		markup += "</ul></div>";
$("#uploadErrorMsg").html(markup);

$("#errorDetailsSpn").show().click(function(){
	$("#error-detail-popup").modal("show");
});
$("#error-detail-popup").modal("show");

//custom header for error heading
if("EmptySheet" in data)
{
	$("#ErrorDetails").hide();
	$("#EmptyTemplateErrorDetails").show();
}
else
{
	$("#EmptyTemplateErrorDetails").hide();
	$("#ErrorDetails").show();
}
}

function showReplacementPopup(data){
	var selectedProdCodes = [];
	var totalCount=0;
	var prdCodes='';

	$.each(data, function(key, field){
		if(key.toLowerCase() != "success" && key != "showMinQtyMsg" && key != "totalCartCount"){
			totalCount++;
		}

	});
	$.each(data, function(key, field){
	if(key.toLowerCase() != "success" && key != "showMinQtyMsg" && key != "totalCartCount") {

		for (var int=0;int<=totalCount;int++) {
			prodCodes=prdCodes+=","+key;
				selectedProdCodes.push(key.toUpperCase()+":1");/*AAOL-6377*/
		}



	}
	});

	var obsoleteProductList =	ACC.addToCartHome.isObsoleteProduct(selectedProdCodes);
	if(ACC.formCheckFunc.isNotNullAndNotEmpty(obsoleteProductList)){
		ACC.addToCartHome.checkReplacementProductPopup(obsoleteProductList,prodCodes,1,null);/*AAOL-6377*/

}
}

$(window).bind('beforeunload', function(){
	if($('.batchNumber').length > 0 || $('.serialNumber').length > 0){
	ACC.cartPage.updateBatchAndSerialDetails();
	}
});

$('.consignmentCharge .shoppingcartOrderEntryList').each(function( index ) {
	var disabled =false;
	  if($(this).find('.batchNumber').length > 0 || $(this).find('.serialNumber').length > 0){
		  var disabled =true;
		}
	    $("#fetchBatchSerial").prop('disabled', disabled);
	  if($(this).find('.notSerialManaged').length > 0 && $(this).find('.notBatchManaged').length > 0){
		  $(this).find('.chargeCopyLine').hide();
		}
});
