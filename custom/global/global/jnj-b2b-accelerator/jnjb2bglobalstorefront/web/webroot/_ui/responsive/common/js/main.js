var catLev1 = ".catLev1", hoverEnabledClass = "hoverEnabled", catalogLev1 = ".catalogLev1", catalogLev2 = ".catalogLev2",
catalogLev2Box = ".catalogLev2Box", activeClass = "active";

$(document).ready(function(){

//Changes for AFFG-1
	var called=false;
	var screenName = $("#screenName").val();
	if(screenName=="productDetailPage"){
		var product_id = $("#pcode").text(); /*id needs to be changed based on the ID in JSP*/
		$(window).scroll(function () {

			if(!called)
			{
				if ($(window).height() + $(window).scrollTop() > $(document).height() - $("#jnj-footer").outerHeight()) {
					called=true;
					$.ajax({
						type: "GET",

						url: ACC.config.contextPath + '/p/getTogetherBoughtProducts?productCode='+product_id,
						headers: {
							'Content-Type': 'application/json'
						},
						success: function(data){
							console.log(data);
							var count = data.length;
							var count2 = count/4;
							console.log(count);
							console.log(count2);
							console.log(data[0].productCode);

							var htmlContent = '<div class="relatedProdSection hidden-xs"><div class="row">'+
							'<div class="col-lg-12 relatedProductLabel">Customers Who Bought This Also Bought</div>'+
							'<div class="col-lg-11 hrLine"></div>'+
							'<div class="col-lg-1 swipePageHolder">'+
							'<div class="swiper-pagination sp2"></div>'+
							'</div>'+
							'<div class="swiper-container s2 boxshadow">'+
							'<div class="swiper-wrapper">';
							for (var j=0;j<count2;j++) {
								htmlContent+= '<div class="swiper-slide">'+
								'<div class="row productSwiperHolder">';
								for (var i=0;i<=3;i++) {
									count = count-1;
									if (count>=0) {
										htmlContent +=                '<div class="col-lg-3 productHolder">'+
										'<div class="imgPlaceHolder">'+
										'<img src="'+data[i+4*j].imageURL+'" class="imgProductProp"></img>'+
										'</div>'+
										'<div class="imgProductDescrp">'+
										'<p class="productCode"><a href="'+data[i+4*j].productUrl+'">'+data[i+4*j].productCode+'</a></p>'+
										'<p class="prodName"><a href="'+data[i+4*j].productUrl+'">'+data[i+4*j].productName+'</a></p>'+
										'</div>'+
										'<div class="likeProductHold">'+
										'<span class="block getPrice" id="getPrice_'+(i+4*j)+'" productcode="'+data[i+4*j].productCode+'" productprice="" > <a class="pull-left btn btnclsnormal quickViewBtn" href="#">View Price</a></span>'+
										'<div class="pull-right"><a href="#" class="btn addToCartBtn">ADD TO CART</a></div>'+
										'<div class="pull-right boxElementIcons"><i class="fa far fa-heart-o heartIcon"></i></div>'+
										'</div>'+
										'</div>';
									}
								}
								htmlContent += '</div></div>';

							}
							htmlContent+='</div>'+
							'<div class="swiper-button-next next-2">></div>'+
							'<div class="swiper-button-prev prev-2"><</div>'+
							'</div>'+
							'</div> </div>';
							var htmlContentMob = '<div class="relatedProdSection hidden-lg hidden-md hidden-sm"><div class="row">'+
							'<div class="col-lg-12 relatedProductLabel">Customers Who Bought This Also Bought</div>'+
							'<div class="col-lg-12 col-xs-12 hrLine"></div>'+
							'<div class="swiper-container s4 boxshadow hidden-lg hidden-sm hidden-md">'+
							'<div class="swiper-wrapper">';
							for (var k=0; k<count; k++) {
								htmlContentMob += '<div class="swiper-slide"><div class="row productSwiperHolder productSwiperHolderMob">'+
								'<div class="col-lg-3 productHolder productHolderMobile"><div class="imgPlaceHolder">'+
								'<a href="'+data[k].productURL+'"><img src="'+data[k].imageURL+'" class="imgProductProp"></img></a></div><div class="imgProductDescrp">'+
								'<p class="productCode"><a href="'+data[k].productURL+'">'+data[k].productCode+'</a></p>'+
								'<p class="prodName"><a href="'+data[k].productURL+'">'+data[k].productName+'</a></p></div><div class="likeProductHold">'+
								'<span class="priceRule" id="MobilePriceRule_'+k+'"></span>'+
								'<span class="block getPricemobile" id="MobileGetgetPrice_'+k+'" productcode="'+data[k].productCode+'" productpricemobile=""> <a class="pull-left btn btnclsnormal quickViewBtn" href="#">View Price</a></span>'+
								'<div class="pull-right"><a href="#" class="btn btnclsactive addToCartBtn addToCartCarousel">add to cart</a></div></div></div></div></div>';
							}

							htmlContentMob += '</div>'+
							'<div class="swiper-button-next next-4">></div>'+
							'<div class="swiper-button-prev prev-4"><</div>'+
							'</div>'+
							'</div></div>';

							$(".scrollBox").html(htmlContent);
							$(".scrollBoxMob").html(htmlContentMob);
							var swiper2 = new Swiper('.s2', {
								pagination: {
									el:'.sp2',
								},
								paginationClickable: true,
								navigation: {
									nextEl: '.next-2',
									prevEl: '.prev-2',
								},
							});
							var mobile_swiper2 = new Swiper('.s4', {
								navigation: {
									nextEl: '.next-4',
									prevEl: '.prev-4',
								},
							});
							var pageHeight = $(document).height();
							$("#jnj-usr-details-menu").css("height",pageHeight);


						}
					});
				}
			}
		});
	}

/*Added for removing spacing in catalog screen	*/
	var catalogCol=($('#catalog-wrapper').width()/2)-1;
    
    $('.catalog-column').each(function(){
           $(this).width(catalogCol)
    });
    
    $('#catalog-wrapper').masonry({ 
     itemSelector: '.catalog-column', 
     percentPosition: true 
});

	

/* AAOL-4937 Upload file for return order popup page */
	
	var reasonCodeVal;
	$('#reasonCodeReturn').change(function(){
		reasonCodeVal=$(this).val().trim('');
		if(reasonCodeVal=='R11' || reasonCodeVal=='R12'){
			$('#upload-file-link').show();
		}	
		else{$('#upload-file-link').hide();}	
	});
	$('#reasonCodeReturn').trigger('change');
	
	/* Upload file for return order popup page AAOL-4937 */
	var fileUploadFlag=false;
	$(document).on('change','.fileUploadInput',function(){		
		var selectedFile = $(this).val();
		var fileUploadIndex=$(".fileUploadInput").index(this);
		var uploadedFileName = selectedFile.split('\\').pop();		
		
		if($(".uploadFileTextbox:eq("+fileUploadIndex+")").val().length==0){
			$(".upload-file-add-btn:eq("+fileUploadIndex+")").removeClass('btn-disable').addClass('btn-enable');
		} 
		$(".uploadFileTextbox:eq("+fileUploadIndex+")").val(uploadedFileName);
		
//		$(".upload-file-add-btn:eq("+fileUploadIndex+")").removeClass('btn-disable').addClass('btn-enable');
		
		/*Changes for AAOL-5807*/
		if($('.upload-file-row').length==5){
			$('.upload-file-add-btn:eq(4)').removeClass('btn-enable').addClass('btn-disable');
			$('.upload-file-del-btn:eq(4)').removeClass('btn-disable').addClass('btn-enable');
			
		}
		
		if($('.upload-file-row').length>1 && fileUploadIndex>0){
			$(".upload-file-del-btn:eq("+fileUploadIndex+")").removeClass('btn-disable').addClass('btn-enable');
		}
		fileUploadFlag=true;
	});	
	
	$('#error-msg-holder').hide();
	$('.upload-file-errors').hide();
	
	var maxSizeError=false;
	var maxFileError=false;
	var fileTypeError=true;
	var emptyfileError=true;
	
	$('#returnImageSubmitBtn').click(function(){
		//More than 5MB throw an error	
		var totalFileSize=0;
		var _validFileExtensions = [".jpeg",".jpg",".png",".xls",".doc",".docx",".tif",".pdf",".xlsx"];  
		
		$('.fileUploadInput').each(function(){
			
			if(this.files[0]!=undefined){
				totalFileSize=totalFileSize+this.files[0].size;
				emptyfileError = false;
			}
			else{
				emptyfileError = true;
				return false;
			}
		});	
		if((totalFileSize/(1024*1024)).toFixed(5)>5){
			maxSizeError=true;
		}
		else{
			maxSizeError=false;
		}
		
		//File name extention error
	
		$('.uploadFileTextbox').each(function(){
			var sFileName=$(this).val();
			fileTypeError=true;
			if (sFileName.length > 0) {
				var blnValid = false;
				for (var j = 0; j < _validFileExtensions.length; j++) {
					var sCurExtension = _validFileExtensions[j];				
					if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toUpperCase() == sCurExtension.toUpperCase()) {
						fileTypeError = false;
						break;
					}
					
				}
				if(fileTypeError){
					return false;
				}
			}
		});
		
		displayError();
		
	}); 
	
	
	var uploadFileHTML=$('.upload-file-row:eq(0)').html();
	
	$(document).on('click','.upload-file-add-btn',function(){
		if($('.upload-file-row').length<5){
			$(this).removeClass('btn-enable').addClass('btn-disable');
			//$(this).next().removeClass('hide-btn').addClass('show-btn');
			
			$('#upload-file-row-holder').append("<div class='upload-file-row'>"+uploadFileHTML+"</div>");	
			
			if($('.upload-file-row').length>1){
				$('.upload-file-row:eq(0)').find('.upload-file-del-btn').removeClass('btn-disable').addClass('btn-enable');
			}
		
			if($('.browse-btn').length>0){
				$('.browse-btn').each(function(i){
					$(this).attr('for','fileUploadInput-'+i);
					$(this).next().attr('id','fileUploadInput-'+i);
					$(this).next().attr('name','files['+i+']');
				});
			}
			
			if($('.upload-file-row').length==5){
				for(var i=0;i<$('.upload-file-del-btn').length-1;i++){
					if($('.uploadFileTextbox:eq('+i+')').val().trim('').length>0){
						$('.upload-file-del-btn:eq('+i+')').removeClass('btn-disable').addClass('btn-enable');
					}
					
				}
			} 
		}
	});
	
	$(document).on('click','.upload-file-del-btn',function(){
		maxFileError=false;	
		
		//$('.upload-file-add-btn:eq(4)').css('visibility','visible');
		$(this).parents().closest('.upload-file-row').remove();
		if($('.upload-file-row').length==1){
			$('#upload-file-row-holder').find('.upload-file-del-btn').removeClass('btn-enable').addClass('btn-disable');
			if($('.uploadFileTextbox').val().trim('').length>0){
				$('#upload-file-row-holder').find('.upload-file-add-btn').removeClass('btn-disable').addClass('btn-enable');
			}
			else{
				$('#upload-file-row-holder').find('.upload-file-add-btn').removeClass('btn-enable').addClass('btn-disable');
			}
		}
		else{
			
			if($('.upload-file-row:last-child .uploadFileTextbox').val().trim('').length>0){
				$('.upload-file-row:last-child .upload-file-add-btn').removeClass('btn-disable').addClass('btn-enable');
				$('.upload-file-row:last-child .upload-file-del-btn').removeClass('btn-disable').addClass('btn-enable');
			}	
			else{
				$('.upload-file-row:last-child .upload-file-add-btn').removeClass('btn-enable').addClass('btn-disable');
				$('.upload-file-row:last-child .upload-file-del-btn').removeClass('btn-enable').addClass('btn-disable');
				
			}
		}
		if($('.browse-btn').length>0){
			$('.browse-btn').each(function(i){
				$(this).attr('for','fileUploadInput-'+i);
				$(this).next().attr('id','fileUploadInput-'+i);
				$(this).next().attr('name','files['+i+']');
			});
		}
		
		if($('.upload-file-row').length==4){
			if($('.upload-file-row:last-child .uploadFileTextbox').val().trim('').length>0){
				$('.upload-file-row:last-child .upload-file-add-btn').removeClass('btn-disable').addClass('btn-enable');
				$('.upload-file-row:last-child .upload-file-del-btn').removeClass('btn-disable').addClass('btn-enable');
			}	
			else{
				$('.upload-file-row:last-child .upload-file-add-btn').removeClass('btn-enable').addClass('btn-disable');
				$('.upload-file-row:last-child .upload-file-del-btn').removeClass('btn-enable').addClass('btn-disable');
				
			}
			
		}
		 
	});
	
	//Error message display
	function displayError(){
		//alert("maxSizeError >> "+maxSizeError+"<br> maxFileError >> "+maxFileError+"<br> fileTypeError >> "+fileTypeError);
		if($('.upload-file-row').length==5){
			maxFileError=true;	
		}
		if(maxSizeError || fileTypeError){
			$('#error-msg-holder').show();
		}
		else{
			/*ajax call to upload images*/
			$("#uploadFileforReturnForm").submit();
		}
		
		
		if(fileTypeError && !emptyfileError){
			$('#upload-file-type-error').show();
		}
		else{
			$('#upload-file-type-error').hide();
		}
		
		if(maxSizeError && !fileTypeError){
			$('#upload-maxsize-error').show();
		}
		else{
			$('#upload-maxsize-error').hide();
		}
		
		if(emptyfileError){
			$('#upload-emptyfile-error').show();
		}
		else{
			$('#upload-emptyfile-error').hide();
		}
	}

	//added for sub report
	$(".subReportsMenu").hide(); 
	$(".reportsMenu").hover(function(){     
        $(".subReportsMenu", this).stop().slideToggle(400);
    });
    //end
	/*if($("#poDate").val() == '' || $("#returnCreatedDate").val() == '' || $("#requestDelDate").val() == '' ){
		$('#poDate,#returnCreatedDate,#requestDelDate').datepicker('setDate',new Date());
	}*/
	if($("#ordersTablehome").length>0){
		$("#ordersTablehome").tableHeadFixer();
	}

	//numeric check porduct code
	$('#prodQty').on('keyup', function (event) { 
	    this.value = this.value.replace(/[^0-9]/g, '');
	    
	});
	var centerItX = function (el /* (jQuery element) Element to center */) { 
        if (!el) { 
                return; 
        } 
        var moveIt = function () { 
            var winWidth = $(window).width(); 

            var elemXPos=winWidth-$(el).width();
            elemXPos=elemXPos/2;
//            console.log(winWidth+"-"+$(el).outerWidth()+"="+elemPos)
            $(el).css("position","absolute").css("left", elemXPos+ "px"); 
        }; 
 
        $(window).resize(moveIt); 
        moveIt(); 
	}; 
 
	
	var centerItXY = function (el /* (jQuery element) Element to center */) { 
        if (!el) { 
                return; 
        } 
        var moveIt = function () { 
           
			var winHeight = $(window).height();
			var winWidth = $(window).width();
			
			var elemXPos=winWidth-$(el).width();
			var elemYPos=winHeight-$(el).height();
			
			elemXPos=elemXPos/2;
			elemYPos=elemYPos/2;
				
			$(el).css("position","absolute").css("left", elemXPos+ "px");	
			$(el).css("position","absolute").css("top", (elemYPos-30)+ "px");
		}; 
        $(window).resize(moveIt); 
        moveIt(); 
	}; 
	//centerIt('.modalcls');
 
	 
	/* Start - getting window width */
	 var width = $(window).width();
	 var winHeight = $(window).innerHeight();
	 
	 if(width>=480){
	  if($('#cart-left-content').height()>$('#cart-right-content').height()){
	   $('#cart-left-content').css('border-right','1px solid #f2f2f2');
	  }
	  else{
	   $('#cart-right-content').css('border-left','1px solid #f2f2f2');
	  }
	  
	  /* LeftSidePanel - rightSidePanel */
	 
	   var containerHeight; 
	   
	   if($('.leftSidePanel').length>0){
		   if($('.leftSidePanel').height()>$('.rightSidePanel').height()){   
		    $('.leftSidePanel').css('border-right','1px solid #f2f2f2');
		    containerHeight = $('.leftSidePanel').height()-$('.leftSidePanelLink').height();  
		   }
		   else{  
		    $('.rightSidePanel').css('border-left','1px solid #f2f2f2');
		    containerHeight = $('.rightSidePanel').height()-$('.rightSidePanelLink').height();  
		   } 
		   $('#leftPanelContent,#rightPanelContent,.leftPanelContent,.rightPanelContent').height(containerHeight);
	   }
	  
	  /* Ends */
	  
	 }

	 
	  $('.dispute-an-item-table').find('.invoiceDisputeQtyTxtField').prop('disabled',true)
	   $('.dispute-an-item-table').find('.invoiceDisputeTxtField').prop('disabled',true)
	   $('.dispute-an-item-table').find('.invoiceDisputeMobieTxtinvoiceDisputeMobieTxt').prop('disabled',true)
	    $('.dispute-an-item-table').find('a.toggle-link-sub').addClass('disableClick');
	   
	  $('.invoice-dispute-mobichkbox input[type="checkbox"]').on('click',function(){
            var currentMobIndex=$(this).parents().closest('.dispute-an-item-table').find('.invoice-dispute-mobichkbox input[type="checkbox"]').index(this);
            
            if($(this).is(":checked")){
                if(currentMobIndex!=-1){
                	$(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+')').find('td').removeClass('disable-cell').addClass('normal-cell');
                    $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+') .invoiceDisputeMobieTxt').prop('disabled',false)
                    $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+')').find('a.toggle-link-sub').removeClass('disableClick');
                }              
            }
            else{
                if(currentMobIndex!=-1){
                    $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+') .invoiceDisputeMobieTxt').prop('disabled',true)
                    $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+')').find('td').removeClass('normal-cell').addClass('disable-cell');
                    $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+currentMobIndex+')').find('a.toggle-link-sub').addClass('disableClick');
                }              
                            
            }
	                
	});
 
	$('.invoice-dispute-checkbox input[type="checkbox"]').on('click',function(){ 
			var currentItem=$(this).parents().closest('.dispute-an-item-table').find('.invoice-dispute-checkbox input[type="checkbox"]').index(this);
			
		var disableHeaderFlag=true;
		  $(this).parents().closest('.dispute-an-item-table').find('.invoice-dispute-checkbox input[type="checkbox"]').each(function(){
		   if($(this).is(":checked")){
		    disableHeaderFlag=false;
		   }
		  });
		  
		  if(disableHeaderFlag){
		   $(this).parents().closest('.dispute-an-item-table').find('th').removeClass('normal-cell').addClass('disable-cell');
		  }
		  else{
		   $(this).parents().closest('.dispute-an-item-table').find('th').removeClass('disable-cell').addClass('normal-cell');
		  }
			
		  if($(this).is(":checked")){
		   $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td .invoiceDisputeQtyTxtField').prop('disabled',false)
		   $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td .invoiceDisputeTxtField').prop('disabled',false)
		   $(this).parents().closest('.dispute-an-item-table').find('.invoice-dispute-checkbox input[type="checkbox"]').each(function(){
			   
		   })
		/*   $(this).parents().closest('.dispute-an-item-table').find('th').css('color','#828282')*/
		   $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td').removeClass('disable-cell').addClass('normal-cell');
		   //$(this).parents().closest('.dispute-an-item-table').removeClass('table-disbaled');
		  }
		  else{
			  $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td .invoiceDisputeQtyTxtField').prop('disabled',true)
			   $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td .invoiceDisputeTxtField').prop('disabled',true)
		   //$(this).parents().closest('.dispute-an-item-table').addClass('table-disbaled');
		   /*$(this).parents().closest('.dispute-an-item-table').find('th').css('color','rgb(176,176,176)')*/
		   $(this).parents().closest('.dispute-an-item-table').find('tr:eq('+(currentItem+1)+') td').removeClass('normal-cell').addClass('disable-cell');
		  }
		 });

	
	
	
	$('.inventory-date').datepicker('setDate',new Date());
	
	if($('#jnj-head-panel').is(':hidden')){
		
		$('#addressBodycontent').removeClass('panel-collapse collapse');	
	
		$("#mobile-ship-header").html($("#pi-shipToaddress-head").html());
		$("#mobile-bill-header").html($("#pi-billToaddress-head").html());
	}
	
	
	$(document).on('show.bs.modal', '.modal', function (event) {
		
		if($(window).width()<768){
			$('.popup').css({'margin-left':'0px','width':$(window).width()-20});
		}
		
		
	     var zIndex = 1040 + (10 * $('.modal:visible').length);
	     $(this).css('z-index', zIndex);
	     setTimeout(function() {
	       	$('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
	     }, 0);
	     

	     //centerIt($(this).find('.modal-dialog'))

	    
	});    

	if($('.changeAccntModal').length>0){
		$(document).on('shown.bs.modal', '.modal', function (event) { centerItXY($(this).find('.changeAccntModal'));});
	}
	else{
		$(document).on('shown.bs.modal', '.modal', function (event) { centerItX($(this).find('.modal-dialog'));});
	}	

	if($(window).width()<768){
		$("#currViewScreenName").val("mobile"); // for contract page grid setting which screen the application open
	}
	else{
		$("#currViewScreenName").val("desktop");
	}
	

	//Changes for Change Account popup screen
	if($(window).width()<=767){
		$("#currAccScreenName").val("mobile"); // for contract page grid setting which screen the application open
	}
	else if($(window).width()>=768 && $(window).width()<=1024){
		$("#currAccScreenName").val("tablet");
	}
	else{
		$("#currAccScreenName").val("desktop");
	}

	/*start-validate  product shuffling   */
	var deviceName=$("#currViewScreenName").val();
	 $('#datatab-'+deviceName+'.error-on-top tr').each(function(){
		 var rowContent=$(this).html();
		  if($(this).find('.error-prod-msg').length>0){
			  $(this).addClass('error-row');
			  $('<tr>'+rowContent+'</tr>').insertBefore( $('#datatab-'+deviceName+'.error-on-top tr:eq(1)'));
			  $(this).remove();
		  }
	 });
	 /*end-validate  product shuffling   */
	 
	/*Start - empty menu remove*/

		$('.menu-item-name').each(function(){

			if($(this).find('a').length<1){

			$(this).parents().closest('.menu-item').remove();

	}

	});

/*Start - empty menu remove*/

	/* Added by jayashree for product number and quantity check */

	$('.prod-quanity').keyup(function() {
	 if (this.value.match(/[^0-9 ]/g)) {
	  this.value = this.value.replace((/[^0-9 ]/g), '');
	 }
	});
	
	/*$('.prod-number').keyup(function() {
		 if (this.value.match(/-@./#&+/w/s)) {
		  this.value = this.value.replace((/[^0-9 ]/g), '');
		 }
		});*/
	/* ends */
	
	//Changes for Auto scroll to display error
	$(".continueToreviewBtn").click(function(){
		  $("html, body").animate({
		   scrollTop: $('.panel-body').offset().top
		  }, 600); 
		 });
	
	//added by lokesh nov28 
	/*$('#video-close').click(function(){
	  $('#jnj-inro-video').attr('src', $('#jnj-inro-video').attr('src'));
	 });*/
	
	$("#contactUsSubjectLb").on('change',function(){
		if($(this).val() == "1"){
			$(".contactUsOrderNumberLb").css("display","block");
		}else{
			$(".contactUsOrderNumberLb").css("display","none");
		}
		 setTimeout(ACC.globalActions.menuNdbodyHeight,500);
	}).trigger('change');	
	
	/*if($('#order-detail-left-panel').height()>$('#order-detail-right-panel').height()){  
		var rightpanelheight = $('#order-detail-left-panel').height() - ($("#orderDetail-leftLink").height()+20);
		  $('#orderDetail-rightside').height(rightpanelheight);
		 }
		 else{
			 var leftpanelheight = $('#order-detail-right-panel').height() - ($("#orderDetail-leftLink").height()+20);
			  $('#orderDetail-lefttside').height(leftpanelheight);
		  //$('#order-detail-left-panel').height($('#order-detail-right-panel').height());
		 }*/
		 
	
	//$('#contractpopup').modal("show");	
	/* Bonus item background */
	
	//added by lokesh oct 25
	if($('.pwdwidgetdiv').length>0){
		if($('#thepwddiv').length>0){
			var profilePWdStrngth = new PasswordWidget('thepwddiv','newPassword');
			profilePWdStrngth.MakePWDWidget();
		}
		//added by lokesh Nov 29 for signup
		if($('#signuppwddiv').length>0){
			var profilePWdStrngth = new PasswordWidget('signuppwddiv','password');
			profilePWdStrngth.MakePWDWidget();
		}
		//added by parthiban for reset password.
		if($('#resetPwd').length>0){
			var profilePWdStrngth = new PasswordWidget('resetPwd','checkpass');
			profilePWdStrngth.MakePWDWidget();
		}
			
	}
	
	/* naga */
	/* set default start and end date for report page */
	var today = new Date();
	var month = today.getMonth() - 1,
		year = today.getFullYear();
	if (month < 0) {
		month = 11;
		year -= 1;
	}
	var oneMonthAgo = new Date(year, month, today.getDate());
/*	$('#reports-startDate').datepicker('setDate', oneMonthAgo);		
	$('#reports-endDate').datepicker('setDate', new Date());*/
	
	/*  ends */
	
	var bonusItemBg =$("tr.noborder").prev().css("background-color");
	$("tr.noborder").css("background-color",bonusItemBg);
	/*Sorted th bold by Siva*/
	
	setTimeout(function(){
		/* added by siva */		
		$(".sorting").click(function(){ 
			$(this).css("font-weight","bold");
			$(".sorting").each(function(){
				$(this).css("font-weight","normal"); 
			}); 
		});
	},1000);

	if($(".accessToNewAccount").is(":checked")){
		$("#myAddAccountChoice").css("display", "none");
		$("#myAddAccountExisting").css("display", "block");
		$(".accessToNewAccount").prop("checked", true);
		 $('.another-account-number').removeClass('jnj-content-show').addClass('jnj-content-hidden');
	}
	
		if($(".accessToAnotherAccount").is(":checked")){
			$("#myAddAccountChoice").css("display", "block");
			$("#myAddAccountExisting").css("display", "none");
			$(".accessToAnotherAccount").prop("checked", true);
		} 
	//$('#contractpopup').modal('show'); // moved to contract derail.js
/* Added by jayashree for profile page add account drop down */	
	$('#inlineRadio1').prop('checked',true);
	/*$('#inlineRadio1').click(function(){
		  if($(this).prop('checked')){
		   $('.another-account-number').removeClass('jnj-content-hidden').addClass('jnj-content-show');
		  }
		  else{
		   $('.another-account-number').removeClass('jnj-content-show').addClass('jnj-content-hidden');
		  }
		  
		 }); 
		 $('.accessToAnotherAccount').on('click',function(){  
			 $("#myAddAccountChoice").css("display", "block");
			 $("#myAddAccountExisting").css("display", "none");
	  $('.another-account-number').removeClass('jnj-content-show').addClass('jnj-content-hidden');
		 });
		 $('.accessToNewAccount').on('click',function(){
		  $('.account-number-block').css("display", "none");
		  $("#myAddAccountChoice").css("display", "none");
		$("#myAddAccountExisting").css("display", "block");
		 });*/

	/*  */
	
	/* added by lokesh oct19 for mobile/tablet */
	$('#clear-txt-icon').click(function(){
		$('#global-search-txt').val('');
	});
	
	/* added by jshree */
	$('[data-toggle="tooltip"]').tooltip(function(){
		title.addClass('tootipcls');
	}); 

	/* Start - Change account popup*/
	/*$( "#selectact" ).click(function() {
		var dataObj = new Object();
		createChangeAccountAjaxCall(dataObj, "");
	});*/
	
	function createChangeAccountAjaxCall(dataObj, pageNumber) {
		if(null!=pageNumber && pageNumber!="") {
			dataObj.showMoreCounter = pageNumber;
			dataObj.showMore = "true";
		}else{
			dataObj.showMoreCounter = "1";
			dataObj.showMore = "false";
		}
		dataObj.showMode = "Page";
		dataObj.page = "0";
		var escapeKeyHide = false;
		var overlayHide = false;
		var a = "";
		var b = "";
		var c = "";
		if(isFirstTimeModal) {
			dataObj.addCurrentB2BUnit = "true";
			escapeKeyHide = false;
			overlayHide = false;
		}
		jQuery.ajax({
			type : "POST",
			data : dataObj,
			url : ACC.config.contextPath + ACC.config.currentLocale + '/home/getAccounts',
			
			success : function(data) {
				$('#selectaccountpage').html(data);
				$('#select-accnt-close').show();
				$('#selectaccountpopup').modal({backdrop: 'static', keyboard: false, show: true});
				$('#selectaccountpopup .anchorcls').on('click',function(){
					anchrClicked=true;
					$('#select-accnt-error').hide();
					$('.anchorcls').each(function(){
						$(this).removeClass('anchor-selected');
					});
					$(this).addClass('anchor-selected');
				});
			
				$(".change-accnt-link").on('click',function(){
					changeAcntList();
				});
				changeAcntList();
				function changeAcntList(){
					$('#select-accnt-error').hide();	
					$('.accountListPopUp').each(function() {
						$(this).click(function() {
							/** Creating dummy form for submission for account change request **/
							var changeAccountForm = jQuery('<form>', {
								'action' : ACC.config.contextPath + '/home/changeAccount',
								'id' : 'changeAccountForm',
								'method' : 'POST'
							}).append(jQuery('<input>', { //  Account UID input created
								'name' : 'uid',
								'value' : $($(this).find("#accountUID:input[type='hidden']")).val(),
								'type' : 'hidden'
							})).append(jQuery('<input>', { //  Account Name input created
								'name' : 'accountName',
								'value' : $($(this).find("#accountName:input[type='hidden']")).val(),
								'type' : 'hidden'
							})).append(jQuery('<input>', { //  Account GLN input created
								'name' : 'accountGLN',
								'value' : $($(this).find("#accountGLN:input[type='hidden']")).val(),
								'type' : 'hidden'
							})).append(jQuery('<input>', { // Account GLN input created 
						    	'name' : 'CSRFToken',
						    	 'value' : ACC.config.CSRFToken,
						    	 'type' : 'hidden' 
							}));
							$("#changeAccountPopup").append(changeAccountForm);
							
							/** Submitting the dummy form **/
							
						});
					});
				}
				
				/*$('#change-select-btn').click(function(){
					alert('main.js')
					if(anchrClicked){
						$("#changeAccountForm").submit();
						anchrClicked=false;
					}
					else{
						$('#select-accnt-error').show();
					}
					
				});*/
			}
		});
	}
	/* End - Change account popup*/

	//$('#contractpopup').modal("show");
	
	/* Start -  change address popup sep23 - lokesh */
	$('#selectaddresspopup .odd-row,#selectaddresspopup .even-row').on('click',function(){
		$('#selectaddresspopup .odd-row').each(function(){
			$(this).removeClass('addressSelected');
		});
		$('#selectaddresspopup .even-row').each(function(){
			$(this).removeClass('addressSelected');
		});
		$(this).addClass('addressSelected');
	});
	/* End -  change address popup sep23 */
	
	/* Added by Vijay For Qutantiy valiadtion*/
	if($(".numeric").length>0){
		$(".numeric").numeric();
	}
	 /* Added by Vijay For Qutantiy valiadtion*/

	/* slider code responsive */
	 $('.pgwSlideshow').pgwSlideshow(); 
	/* slider code responsive  */
	
	 /*Sorted th bold by Siva*/
	 setTimeout(function(){
		 /*siva*/
		  $(".flitersPanel").css("display","block").insertAfter("div#ordersTable_info + .row"); 
		 $(".sorting").click(function(){		 
			 $(this).css("font-weight","bold");
			 $(".sorting").each(function(){
				$(this).css("font-weight","normal"); 
			 });		
		});
	 },500);
	 
	/* Drop shipment toggle 
	$(".dropShipment").click(function(){
		$(".dropShipmentToggle").toggle();
		menuNdbodyHeight();
	});
	
	 Drop shipment toggle ends here*/
	
	
	
	$("#taxExemptCertificate").change(function(){	
		var myString = $(this).val();
		var fileName = myString.split('\\').pop();		
		$("#filename").text(fileName); 
	});
	//updated by lokesh April 5th 
	
	
	/* Start- adding btn class active for emty button */	
	 if($(".clearCart").length>0){
			$('.clearCart').each(function(){
				$(this).children().find('a').addClass('btn btnclsactive');
			})
		}
	 if($(".empty-btn").length>0){
		$('.empty-btn').each(function(){
			$(this).children().find('a').addClass('btn btnclsnormal');
		})
	}
	if($(".clearCart").length>0){
		$('.clearCart').each(function(){
			$(this).children().find('a').addClass('btn btnclsactive');
		})
	}
	/* End- adding btn class active for emty button */	
	
	/* Start- adding selectpicker*/	
	$('select').each(function(){
		if(!$(this).hasClass('only-form-control')){
			$(this).removeClass('form-control');
			$(this).removeClass('input-sm');
			$(this).addClass('selectpicker');
		}
		
		
	});
	/* End- adding selectpicker*/
	
	/* Start - select account highlight*/
	var anchrClicked=false;
	$('#select-accnt-error').hide();	
	$('#selectaccountpopup .anchorcls').on('click',function(){
		anchrClicked=true;
		$('#select-accnt-error').hide();
		$('.anchorcls').each(function(){
			$(this).removeClass('anchor-selected');
		});
		$(this).addClass('anchor-selected');
	});
	/* End - select account highlight*/
	
	/* Start - Change account form submit*/
	/*$('#change-select-btn').click(function(){
		alert('main 2.js')
		if(anchrClicked){
			$("#changeAccountForm").submit();
		}
		else{
			$('#select-accnt-error').show();
		}
	});*/
	/* End - Change account form submit*/
		
	/* Start - check for build an order page */
	if($('#selectaccountpopup').length>0 && $('.accountListPopUpUL li').length>0){
		if($('#updatedLegalPolicy').val() == "false"){
			$('#selectaccountpopup').hide();
		}
		else if($('#selectaccountpopup').attr('data-firstLogin')){
			$('#select-accnt-close').hide();
			const modal = document.querySelector('#selectaccountpopup');
            modal.setAttribute('data-bs-backdrop', 'static');
            modal.setAttribute('data-bs-keyboard', 'false');
            $('#selectaccountpopup').modal('show');
		}
		else{
			$('#select-accnt-close').show();
			$('#selectaccountpopup').removeData('bs.modal');
			const modal = document.querySelector('#selectaccountpopup');
            modal.setAttribute('data-bs-backdrop', 'true');
            modal.setAttribute('data-bs-keyboard', 'true');
			$('#selectaccountpopup').hide();
		}
		/*$(".change-accnt-link").on('click',function(e){
			e.preventDefault();
			$('#select-accnt-close').show();
			$('#selectaccountpopup').attr('data-firstLogin',false);
			$('#selectaccountpopup').removeData('bs.modal').modal({backdrop: true, keyboard: true, show: true});
			$('#selectaccountpopup').show();
		});*/
	}	
	
	/* Start - check for build an order page Mobile */
	if($('#selectaccountpopupMobile').length>0 && $('.accountListPopUpUL li').length>0){
		if($('#updatedLegalPolicy').val() == "false"){
			$('#selectaccountpopupMobile').hide();
		}
		else if($('#selectaccountpopupMobile').attr('data-firstLogin')){
			$('#select-accnt-close-Mobile').hide();
			$('#selectaccountpopupMobile').modal({backdrop: 'static', keyboard: false, show: true});
		}
		else{
			$('#select-accnt-close-Mobile').show();
			$('#selectaccountpopupMobile').removeData('bs.modal').modal({backdrop: true, keyboard: true,show:false});
			$('#selectaccountpopupMobile').hide();
		}
		/*$(".change-accnt-link").on('click',function(e){
			e.preventDefault();
			$('#select-accnt-close-Mobile').show();
			$('#selectaccountpopupMobile').attr('data-firstLogin',false);
			$('#selectaccountpopupMobile').removeData('bs.modal').modal({backdrop: true, keyboard: true, show: true});
			$('#selectaccountpopupMobile').show();
		});*/
	}	
	
	
	/* Start - check for build an order page 
	var inBuildanorderPage=false;
	
	if($('#buildorderpage').length>0 && $('.desk-build-order-table').find('tr').length>1){
		inBuildanorderPage=true;	
	}updated by lokesh & naga sept30
	$('.build-ordr-cancel-btn,.menu-item-name,#jnj-menu-list li,#jnj-logo-holder').on('click',function(e){
		jQuery.ajax({
			type: "GET",
			url:  ACC.config.contextPath +'/buildorder/clearSessionItem' ,
		});
		
		if($(this).find('a').attr('href')!=undefined){
			$('#leave-page-btn').attr('href',$(this).find('a').attr('href'));
		}
		else if($(this).attr('href')!=undefined){
			$('#leave-page-btn').attr('href',$(this).attr('href'));
		}
		if(inBuildanorderPage){
			e.preventDefault();
			$('#items-in-order-popup').modal('show');
			return false;
		}
	});*/
	/* End - check for build an order page */

	/* Start - current menu item */
/*Start AAOL-3813 Nav Bar Issue */	
	
	
	
	/*$('.menu-item a').click(function(e){
		
		
		$('.leave-page-btn').attr('href', $(this).attr('href'));
		if ($('#returnOrderPage').val()=='true') {
			e.preventDefault();
			$("#startNewOrderpopup").modal('show');
			//$('#returnOrderPage').val(false);
			return false;
		}
		else{
			menuClicked($(this).attr('href'),$('.menu-item a').index(this))
		}
		
	});	*/
	
	$('.menu-item').click(function(e){
		$('.leave-page-btn').attr('href', $(this).find('a').attr('href'));
		if ($('#returnOrderPage').val()=='true') {
			e.preventDefault();
			$("#startNewOrderpopup").modal('show');
			//$('#returnOrderPage').val(false);
			return false;
		}
		else{
			menuClicked($(this).find('a').attr('href'),$('.menu-item').index(this))
		}
		localStorage.setItem('displayTableRows',10);
		
	});
	
	localStorage.setItem('currentMenu',0);
	
	
	if($('.menu-item').length>0){
		var isMenuNotSelected=true;
		/*$('.menu-item').click(function(){
		*/
		
		function menuClicked(href,index){
			window.location.href=href;
			
			
			isMenuNotSelected=false;
			var selectMenuIndex=index;
			localStorage.setItem('currentMenu',selectMenuIndex);
			/*currentMenu=localStorage.getItem('currentMenu');	*/	
			
			$('.menu-item').each(function(){
				$(this).removeClass('isactive');
			});
		}
			
			/*$('.menu-item:eq('+currentMenu+')').addClass('isactive');*/
		/*});*/
		
		if(isMenuNotSelected){
			//verify menu selection based on breadcrumb
			var breadCrumb=$('#BC').val();
			/*if(breadCrumb=="Shopping Cart"){
				localStorage.setItem('currentMenu',2);
			}*/
			$('.menu-item').each(function(i){
				if((breadCrumb=="Shopping Cart" && $(this).find('a').text()=="Build An Order") || ($(this).find('a').text()==breadCrumb)){					
					localStorage.setItem('currentMenu',i);
				}
				$(this).removeClass('isactive');
			});
		/*	$('.menu-item:eq('+currentMenu+')').addClass('isactive');*/
		}
		currentMenu=localStorage.getItem('currentMenu');		
		/*$('.menu-item').each(function(){
			$(this).removeClass('isactive');
		});*/
		$('.menu-item:eq('+currentMenu+')').addClass('isactive');
	}
	/*End AAOL-3813 Nav Bar Issue*//* End - current menu item */
	
	/*Start - added for push menu */
	$menuLeft = $('.pushmenu-left');
	$nav_list = $('#hamburg-btn');
	
	$nav_list.click(function() {
		$(this).toggleClass('active');
		$('.pushmenu-push').toggleClass('pushmenu-push-toright');
		$menuLeft.toggleClass('pushmenu-open');
	});
	/*End - added for push menu */
	
	/* Start - Window resize check */
	$(window).resize(function () {setTimeout(mobileSize, 500);});
	function mobileSize() {
		sizes(0);
	}
	$(window).load(function() {
		sizes(0); 
	});
	/* End - Window resize check */
	/* Start - getting window width */
	var width = $(window).width();
	var winHeight = $(window).innerHeight();
	
	function sizes(height) {
		width = $(window).width();
		winHeight = $(window).height();
		var loginContentTopmargin=(winHeight-$('.logincontent-area').outerHeight())/2;
		$('.jnj-bg-tab-img').addClass('hidden-xs')
		//Mobile & Tablet login page bg image 
		if(!$('#jnj-bg-img-holder .jnj-bg-desk-img').is(":visible") && !$('#jnj-bg-img-holder .jnj-bg-tab-img').is(":visible")){
			
			if($('.logincontent-area').height()<winHeight){
				$('.logincontent-area').css({'height':winHeight+'px','margin-top':'0px'});
			}
			else{
				$('.jnj-bg-tab-img').css('display','block !important').removeClass('hidden-xs');
				$('.logincontent-area').css({'height':'auto','margin-top':'20px','margin-bottom':'20px'});
				$('body').css('overflow-y','auto')
				
			}	
		}
		else{
			$('.logincontent-area').css({'height':'auto','margin-top':loginContentTopmargin+'px'});
		}	
		if(winHeight>$('#jnj-bg-img-holder img:eq(0)').height()){
			$('#jnj-bg-img-holder img:eq(0)').height(winHeight)
		}
		if(winHeight>$('#jnj-bg-img-holder img:eq(1)').height()){
			$('#jnj-bg-img-holder img:eq(1)').height(winHeight)
		}
	}
	/* End - getting window width */
	
	/* Start - Added by lokesh nov 3 */
	 $(".menubody-heightcheck,#createAddToTemplate").on('click',function(){  
	  setTimeout(ACC.globalActions.menuNdbodyHeight,500);
	 });
	 $("#createAddToTemplate").on('click',function(){  
		 toggleBtnClicked=true;
		  setTimeout(ACC.globalActions.menuNdbodyHeight,500);
	 });
	 $("#changesecuritypsd").click(function(){ 
		 toggleBtnClicked=true;
	 }); 
	 /* End - Added by lokesh nov 3 */
	 $('#addAccountExistingSubmit').click(function(){
		
		 setTimeout(ACC.globalActions.menuNdbodyHeight,500);
	 });
	 
	
	 

	 ACC.globalActions.bindAll();
	 ACC.globalActions.menuNdbodyHeight();

	/* Start - adjusting menu-body height */
	/*var toggleBtnClicked=false;
	function menuNdbodyHeight(){ 
			
	  var leftHeight=$('#jnj-usr-details-menu').innerHeight();  
	  var rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
	  var bodyContentHeight=leftHeight-($('#jnj-header').innerHeight()+$('#jnj-footer').innerHeight());
	  
	  var productnameheight= $('.productspanel').innerHeight();
	  var productviewheight= $('.ps-current').innerHeight();
	  
	  if(productviewheight<productnameheight){
	    
	    $('.ps-current').removeAttr('style');
	   $('.ps-current').css('height',productnameheight+'px !important');
	  }
	  
		    
	  	if(leftHeight==rightHeight){
              $('#jnj-body-content').innerHeight('auto');
              rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
              $('#jnj-usr-details-menu').innerHeight(rightHeight);
          
		}
		else if(toggleBtnClicked){
		     if(leftHeight<rightHeight){
		    	 $('#jnj-usr-details-menu').innerHeight(rightHeight); 
		     }else{
		    	  $('#jnj-body-content').innerHeight(bodyContentHeight);
		     }
		     
		}
		else{
		     $('#jnj-body-content').innerHeight(bodyContentHeight);
		} 
	}*/
	/* End - adjusting menu-body height */
	//$('#jnj-menu').hide();
	
	/* Start - Menu toggle functionality */
	$(document).on('click', '#hamburg-btn', function(e){
		if($('#jnj-menu').hasClass('menu-closed')){
			$('#jnj-menu').removeClass('menu-closed').addClass('menu-opened');
			$('#jnj-menu').show();
		}
		else{
			$('#jnj-menu').removeClass('menu-opened').addClass('menu-closed');
			$('#jnj-menu').hide();
		}
	});
	/* End - Menu toggle functionality */
	
	/* Start - panel toggle */
	/* Modified by lokesh oct 26 */
	
	/*Start - sub toggle for dispute itme for invoice screen*/
	$(document).on('click', '.toggle-link-sub', function(){ 
        var $this = $(this); 
        if($this.hasClass('panel-collapsed')) {         
                $this.removeClass('panel-collapsed'); 
                $this.find('span.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus'); 
        } 
        else{ 
                $this.addClass('panel-collapsed'); 
                $this.find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
        } 
        
	});
	/*End - sub toggle for dispute itme for invoice screen*/
	
	$(document).on('click', '.toggle-link', function(){ 
		var $this = $(this); 
		
	/*	if($this.parents().closest('div').hasClass('panel')) {  
		   $('.panel').find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
		   $this.find('span.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus'); 
		 }*/
		
		/*start - added for dispute item for invoice*/
		$('.dispute-an-item-mobi').find('.panel-collapse').collapse('hide'); 
		$('.dispute-an-item-mobi').find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus');
		$('.dispute-an-item-mobi').find('a.toggle-link-sub').addClass('disableClick');
		$('.invoice-dispute-mobichkbox input[type="checkbox"]').prop('checked',false);
		/*End - added for dispute item for invoice*/
		
		if($this.hasClass('panel-collapsed')) {
			if($this.parents().closest('div').hasClass('panel')){   
               $('.panel').find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
               $('.panel').find('.toggle-link').addClass('panel-collapsed'); 
           }
		   $this.removeClass('panel-collapsed'); 
		   $this.find('span.glyphicon').removeClass('glyphicon-plus').addClass('glyphicon-minus');    
		   
		   if($(this).hasClass('security-pop-up-link')){
		    $("#change-security-sign").removeClass('glyphicon-plus').addClass('glyphicon-minus'); 
		   }
		   if($(this).hasClass('toggle-fee-link')){
		    $this.find('span.glyphicon').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down'); 
		    //$('.fee-panel').hide();
		   }
		   
		   var trimmedTxt=$this.find('.paddingforText').text();
		   if(trimmedTxt.toUpperCase()==SHOW_FILTERS_UPPER){
		    $this.find('.paddingforText').text(HIDE_FILTERS_LOWER);
		    $(".catalogLanding .paddingforText,#contractpage .paddingforText").text(HIDE_FILTERS_LOWER);
		   }
		  }
		else {  
			   $this.addClass('panel-collapsed'); 
			   $this.find('span.glyphicon').removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
			   
			   if($(this).hasClass('security-pop-up-link')){ 
			    $("#change-security-sign").removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
			   }
			   if($(this).hasClass('toggle-fee-link')){
			    $this.find('span.glyphicon').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up'); 
			    //$('.fee-panel').show();
			   }
			   var trimmedTxt=$this.find('.paddingforText').text();
			   if(trimmedTxt.toUpperCase()==HIDE_FILTERS_UPPER){
			    $this.find('.paddingforText').text(SHOW_FILTERS_LOWER);
			    $(".catalogLanding .paddingforText,#contractpage .paddingforText").text(SHOW_FILTERS_LOWER);
			   }
			   
			  } 
		  toggleBtnClicked=true;
		  
		  //Filter toggle divider line
		  var searchFitlerRowHeight=[]; 
          var filterLineHeightArr=[]; 
          var filterLineHeight; 
          
         
          $('.filterDivider').each(function(i){ 
        	 
              filterLineHeightArr.push($(this).innerHeight()); 
              if((i+1)%3==0 && i!=0){ 
            	 
                      setFilterLineHeight(filterLineHeightArr,i); 
                      filterLineHeightArr=[]; 
              } 
          }); 
          
          function setFilterLineHeight(filterLineHeightArr,index){ 
        
              var filterIndex=0; 
              if(index>2){ 
                      filterIndex=index; 
              } 
              filterLineHeight = Math.max.apply(Math,filterLineHeightArr); 
      
              for(var i=0;i<=index;i++){ 
                  if(!$('.filterDivider:eq('+i+')').hasClass('done')){ 
                          $('.filterDivider:eq('+i+')').height(filterLineHeight).addClass('done'); 
                  } 
                  if(i==index){ 
                          $('.filterDivider:eq('+i+')').removeClass('borderrightgrey'); 
                  } 
              } 
              filterIndex=index; 
          }
		  setTimeout(ACC.globalActions.menuNdbodyHeight,500);
		  
		
	});
	/* End - panel toggle */
	
	/* Added by siva */
	$(document).on('click', '.toggle-link-payment', function(){ 	
		var $this = $(this); 
		if($this.hasClass('collapsed')) { 
			$this.removeClass('collapsed'); 
			 $('.toggle-link-payment').each(function(){				
				$(this).find('i').removeClass('fa fa-circle').addClass('fa fa-circle-o'); 
			});  
			
			$this.find('i').removeClass('fa fa-circle-o').addClass('fa fa-circle'); 			
			
		} 
		else {		
			$this.addClass('collapsed'); 
			  $('.toggle-link-payment').each(function(){				
				$(this).find('i').removeClass('fa fa-circle').addClass('fa fa-circle-o'); 
			});	 		
			$this.find('i').removeClass('fa fa-circle-o').addClass('fa fa-circle'); 					
		} 
		setTimeout(	ACC.globalActions.menuNdbodyHeight,500);
	});
	$('.payment').bind("click",function(){	
		$(".paymentMethod").hide();
		var paytype = $(this).attr('data-num');		
		$('#paymentType'+paytype).show();		
	});
	/* Added by siva */
	

/*	 Start - data table 
   $('#ordersTablehome').DataTable({
			"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"sLengthMenu": "Show &nbsp;_MENU_" ,"paginate": {"next": '  â�¯',"previous": 'â�®  '},"emptyTable":"No data found."},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false,
});			
$('.only-sort-table').DataTable({
			"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"sLengthMenu": "Show &nbsp;_MENU_" ,"paginate": {"next": '  â�¯',"previous": 'â�®  '},"emptyTable":"No data found."},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false
});

$('.sorting-table').DataTable({
			"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language":{"sLengthMenu": "Show &nbsp;_MENU_" ,"paginate": {"next": '  â�¯',"previous": 'â�®  '},"emptyTable":"No data found.","info": " <b>_START_</b> - <b>_END_</b> <i>of</i>&nbsp;&nbsp;<b>_TOTAL_</b> Results"},'pagingType': "simple"
});

$('.sorting-table-lines').DataTable({
		"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language":{"sLengthMenu": "Show &nbsp;_MENU_" ,"paginate": {"next": '  â�¯',"previous": 'â�®  '},"emptyTable":"No data found.","info": " <b>_START_</b> - <b>_END_</b> <i>of</i>&nbsp;&nbsp;<b>_TOTAL_</b> Lines"},'pagingType': "simple"
});

$('.invoice-desktop-table').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":"No data found."},"paging":   false,"info":     false
});*/
/*	  $('#ordersTablehome').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false,
});			
$('.only-sort-table').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false
});

$('.sorting-table').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language":{"emptyTable":NO_DATA,"info": REPORTS_RESULTS},'pagingType': "simple"
});

$('.sorting-table-lines').DataTable({
"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language":{"emptyTable":NO_DATA,"info": REPORTS_LINES},'pagingType': "simple"
});

$('.invoice-desktop-table').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA},"paging":   false,"info":     false
});*/

$('#ordersTablehome').DataTable({
"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info":" 	<b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "   >","sPrevious":"<  "},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false,
});                                          
$('.only-sort-table').DataTable({
        "aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info":" 	<b>_START_</b> - <b>_END_</b> <i> " + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "   >","sPrevious":"< "},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple","bPaginate": false,"bFilter": false,"bInfo": false
});

if(localStorage.getItem('displayTableRows')>10){
	var sortingTable=$('.sorting-table').DataTable({
		"iDisplayLength": Number(localStorage.getItem('displayTableRows')),"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info": " 	<b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "   >","sPrevious":"<"},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple"
}); 
}
else{
	var sortingTable=$('.sorting-table').DataTable({
		"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info": " 	<b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "   >","sPrevious":"<"},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple"
}); 
}

/*START This is scroll bar implimentation 6337*/
var reportTable=$(".reports-table-"+deviceName);
reportTable.parent().parent().addClass('no-margin-right');
reportTable.parent().addClass('add-scroll');
var reporttableHeight=reportTable.height();

reportTable.parent().css('max-height',(winHeight-100)+'px');
reportTable.tableHeadFixer();

/*$('select[name="datatab-'+deviceName+'_length"]').change(function(){
	if($(this).val()>10){
		reportTable.parent().css('height','500px');
		reportTable.tableHeadFixer(); 
	}
	else{
		reportTable.parent().css('height','auto');
	}
	setTimeout(ACC.globalActions.menuNdbodyHeight,500);
}).trigger('change'); */
/*END This is scroll bar implimentation 6337*/

$('.sorting-table-scroll-x').DataTable({
	"aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info": " 	<b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "   >","sPrevious":"<"},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple"
}); 

$('.sorting-table-lines').DataTable({
    "aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info": " 	<b>_START_</b> - <b>_END_</b> <i>of</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_LINES,  "oPaginate": {"sNext": "   >","sPrevious":"< "},"lengthMenu":DATA_SHOW_MENU},'pagingType': "simple"                                                                                                  
});

$('.invoice-desktop-table,.invoice-tablet-table,.invoice-mobile-table').DataTable({
        "aaSorting": [  ],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":NO_DATA,"info": " 	<b>_START_</b> - <b>_END_</b> <i>" + ITEMS_LIST_OF +  "</i>&nbsp;&nbsp;<b>_TOTAL_</b> " + REPORTS_RESULTS,  "oPaginate": {"sNext": "  >","sPrevious":"<  "},"lengthMenu":DATA_SHOW_MENU},"paging":   false,"info":false
});

setTimeout(ACC.globalActions.menuNdbodyHeight,1000);

//checking the display length of the table
$(document).on('change','.dataTables_length .selectpicker',function(){
localStorage.setItem('displayTableRows',$(this).val());
});
	
	//contract detail page moved to contract detail.js
	
	$("#ordersTable_info").detach().prependTo('#ordersTable_wrapper');
	$("#ordersTable_paginate").detach().prependTo('#ordersTable_wrapper');
	
	$("#ordersTablemobile_info").detach().prependTo('#ordersTablemobile_wrapper');
	$("#ordersTablemobile_paginate").detach().prependTo('#ordersTablemobile_wrapper'); 
	
			
	$("#datatab-desktop_info").detach().prependTo('#datatab-desktop_wrapper');
	$("#datatab-desktop_paginate").detach().prependTo('#datatab-desktop_wrapper');
	
	$("#datatab-tablet_info").detach().prependTo('#datatab-tablet_wrapper');
	$("#datatab-tablet_paginate").detach().prependTo('#datatab-tablet_wrapper');
	
	$("#datatab-mobile_info").detach().prependTo('#datatab-mobile_wrapper');
	$("#datatab-mobile_paginate").detach().prependTo('#datatab-mobile_wrapper');
	
	for(var i=1;i<=$('.dropShipmentTable').length;i++){
		$("#dropShipmentTable-"+i+" .dataTables_info").detach().prependTo("#dropShipmentTable-"+i+" .dataTables_wrapper");
		$("#dropShipmentTable-"+i+" .dataTables_paginate").detach().prependTo("#dropShipmentTable-"+i+" .dataTables_wrapper");
		$("#dropShipmentTable-"+i+" .dateselectordropdown").insertAfter("#dropShipmentTable-"+i+" .dataTable thead").css("display","block");
	}
	for(var i=1;i<=$('.dropShipmentTableMobile').length;i++){
		$("#dropShipmentTableMobile-"+i+" .dataTables_info").detach().prependTo("#dropShipmentTableMobile-"+i+" .dataTables_wrapper");
		$("#dropShipmentTableMobile-"+i+" .dataTables_paginate").detach().prependTo("#dropShipmentTableMobile-"+i+" .dataTables_wrapper");
	}
	
	//Changes for Schedule Line JSP	
	$('tbody').on('click', 'td.details-control', function (){
        var tr = $(this).closest('tr');
        var row = sortingTable.row( tr );
		var indexVal=$('td.details-control').index(this);
		
        if(row.child.isShown()){
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shownDetils');
			$(this).find('.table-toggle-icon').addClass('glyphicon-plus').removeClass('glyphicon-minus');
        }
        else{
            // Open this row
            row.child( format(indexVal)).show();
            tr.addClass('shownDetils');
			$(this).find('.table-toggle-icon').addClass('glyphicon-minus').removeClass('glyphicon-plus');
        }
		menuNdbodyHeight();
    });
	
	/* Formatting function for row details - modify as you need */
	function format ( d ) {
		// `d` is the original data object for the row
		return $('.subDetails:eq('+d+')').html();
		menuNdbodyHeight();
	}
	
			
	/* datatab-desktop_info */
	/* Start - setting width for sorted columns */
	if($('.dataTable').length!=0){
		$('.dataTable th').each(function(){
			if(!$(this).hasClass('no-sort')){
				var newWidth = $(this).outerWidth()+100;
				$(this).outerWidth(newWidth)
			}
		});
	/* End - setting width for sorted columns */
	
	/* Added by siva for filters*/
	$(".flitersPanel").css("display","block").insertAfter("#searchResults div#ordersTable_info + .row");
	//Mobile/Tablet defect #20, 21, 22
	$(".flitersPanel-mobile").insertAfter("#searchResults div#ordersTablemobile_info + .row");
	
	  $("#selectAllProductCheckboxRow").css("display","block").insertAfter(".flitersPanel");
	
	$('.error-on-top tr').each(function(){
		 var rowContent=$(this).html();
		  if($(this).find('.error-prod-msg').length>0){
			  $(this).addClass('error-row');
		  }
	 });

	//added by lokesh nov 24
	 var table=0; 
	 $('.dateselectordropdown').each(function(){
		 if($('.dropShipmentTable').length>0){
			 $(this).insertAfter(".dropShipmentTable:eq("+table+") .dataTable thead").css("display","block");
		 }
		 else{
			 $(this).insertAfter(".dataTable:eq("+table+") thead").css("display","block");
		 
		 }
	  
	  table=table+1;
	 });
	 
	
	
	/* Start - contract */	
	var contractFilterHtml=$('#toggle-filter').html();
	$('#toggle-filter').remove();	
	
	/* Modified by lokesh oct 26 */
	if(width<768){  
        $('<div class="panel-title text-left row" style="margin:0px">'+contractFilterHtml+'</div>').insertAfter($('#contractpage #ordersTablemobile_info').next());
	} else{
		$('<div class="panel-title text-left">'+contractFilterHtml+'</div>').insertAfter($('#contractpage #datatab-desktop_info').next('.row'));   
	}
	
	/* End - contract */	

	/* Start - appending items text to dropdown */
		$('.dataTables_length select option').each(function(){
			var x=$(this).val();
			$(this).text(x+ITEMS)
	   });
	}
	/* End - appending items text to dropdown */
		$('select').each(function(){
		if(!$(this).hasClass('selectpicker') && !$(this).hasClass('only-form-control')){
			$(this).removeClass('form-control input-sm')
			$(this).addClass('selectpicker');
			}
		}); 
	/* End - data table */
	
		 $(document).on('click','.paginate_button',function(){
				//alert('s')
			 setTimeout(ACC.globalActions.menuNdbodyHeight,500);
		 });
		 
	/* Modified by lokesh oct 26 */
	setTimeout(function(){
		var labelSelectHolder=$('.contract-serach').width();
		$('.sort-by-contract-mobile-holder,#ordersTablemobile_length').width(labelSelectHolder+8)
	},1000);	
	
	$('.orderno').css('width','50px !important');
	/* End - Added by lokesh Aug-10 */
	/*  Added for AsPAC consignment order screen*/
	
	
	
	/* Start - Contract Details page chechbox functionality moved to contract detail.js */

	if (width > 767) {
	
		if($('#contract-separator').length>0){
			getDividerHeight('.contract-leftTotop-content','.contract-rightTobottom-content','#contract-separator');
		}
		if($('#invoice-separator').length>0){
			getDividerHeight('.invoice-leftTotop-content','.invoice-rightTobottom-content','#invoice-separator');
		}	
		
		function getDividerHeight(leftPane,rightPane,dividerId){
			var leftPaneHeight=$(leftPane).innerHeight()+25;
			var rightPaneHeight=$(rightPane).innerHeight()+25;
			
			if(leftPaneHeight>=rightPaneHeight){
				$(dividerId).innerHeight(leftPaneHeight);
			}
			else{
				$(dividerId).innerHeight(rightPaneHeight);
			}
		}
		
	}	
	/* End - Contract Details page divider line dimension*/
	
	if (width < 480) {
		$(".catalogLanding #ordersTablemobile_info").next('.row').css('height','183px');
		
		//select allignment
		var rowWidth=$('.catalogLanding #ordersTablemobile_length').parent().width()-10;
		var orderTableSelectWidth = rowWidth-$('.catalogLanding #ordersTablemobile_length strong').width()
		$('.catalogLanding #ordersTablemobile_length select').css('width',orderTableSelectWidth+"px");
		$('.catalogLanding #ordersTable_length select').css('width',orderTableSelectWidth+"px");
	}
	/* Start - swiper in orderStatus page */
	var swiper = new Swiper('.swiper-container', {
		pagination: '.swiper-pagination',
		paginationClickable: true
	});
	/* End - swiper in orderStatus page */	
		
	/* Start - date picker */
	//$(".date-picker").datepicker();	 
/* this is  added for date format setup for GT   */

	var languageCode=$('html').attr('lang');
	
	if(languageCode.indexOf('_')>0){
		languageCode=languageCode.split('_')[0];
	}
	
	$(".date-picker").datepicker({
        format: DATE_FORMAT.toLowerCase(),language:languageCode
    });
	
	/*if($('#datepickerformat').length>0){
		var region=$('#datepickerformat').val();
		if(region.toUpperCase()=='GT'){
			$(".date-picker").datepicker({
		        format: 'dd/mm/yyyy'
		    });
		}
		else if(region.toUpperCase()=='NA'){
			$(".date-picker").datepicker({
		        format: 'mm/dd/yyyy'
		    });
		}
		else if(region.toUpperCase()=='EMEA'){
			$(".date-picker").datepicker({
		        format: 'dd/mm/yyyy'
		    });
		}
		else if(region.toUpperCase()=='ASPAC'){
			$(".date-picker").datepicker({
		        format: 'dd/mm/yyyy'
		    });
		}
		else{
			$(".date-picker").datepicker();	
		}
	}else{
		$(".date-picker").datepicker();	
	}*/
	
	/* $('#po-date,#request-date,#invoiceDueDate').datepicker('setDate', new Date()); */
	if($("#invoiceDueDate").val()=="" || $("#invoiceDueDate").val()==undefined)
	{
		$('#invoiceDueDate').datepicker('setDate', new Date());
	}
	if($("#request-date").val()=="" || $("#request-date").val()==undefined)
	{
		$('#request-date').datepicker('setDate', new Date());
	}
	if($("#po-date").val()=="" || $("#po-date").val()==undefined)
	{
		$('#po-date').datepicker('setDate', new Date());
	}
	/*  ends*/
	
	$('#poDate,#returnCreatedDate,#requestDelDate').datepicker(
        'setDate',new Date()
    );
	/* this is  added for date format setup for GT   */
	$(".date-picker").on('changeDate', function(ev){
		$(this).datepicker('hide');
	});
	/* End - date picker */
	
	/*Start - Product details */
	var  productDetailHTML=$('#product-detail-html').html();
	$(productDetailHTML).insertAfter(".ps-current");
	$('#product-detail-html').remove();
	
	var productnameheight= $('.productspanel').innerHeight();
	var productviewheight= $('.ps-current').innerHeight();
	/*Lokesh OCT-28 for Mobile*/
	if (width > 480) {
		if(productviewheight<=productnameheight){
			$('.pgwSlideshow .ps-current').innerHeight(productnameheight);			
			$('.pgwSlideshow .ps-current ul').innerHeight(productnameheight);
			$('.pgwSlideshow .ps-current ul li').each(function(){
				$(this).height(productnameheight);				
			}); 
		}
		else{
			$('.pgwSlideshow .ps-current').innerHeight(productnameheight);			
			$('.pgwSlideshow .ps-current ul').innerHeight(productnameheight);
			$('.pgwSlideshow .ps-current ul li').each(function(){
				$(this).height(productnameheight);				
			}); 		
			productviewheight= $('.ps-current').innerHeight();
			 $('.productspanel').innerHeight(productviewheight);
		}
	}
	/*End - Product details */

	/*End - Product details */
	 $('.ps-list img').each(function(){
		 
	  var imgIndex=$('.ps-list img').index(this);
	  if($(this).hasClass('no-preview-img')){
	   $('.ps-current img:eq('+imgIndex+')').addClass('noImgPreviewImgSige');
	  }
	  else{
	   $('.ps-current img:eq('+imgIndex+')').addClass('PreviewImgSige');
	  }
	 });
	/* change by tharun */
	/*$('.shipping-head-dropdown').on('change',function(){
		var tableHolderId=$(this).parents().closest('.dropShipmentTable').attr('id');
        var shippingHeadDDValue=$(this).val();
        var shippingHeadDDText=$('.shipping-head-dropdown:eq(0) option[value="'+shippingHeadDDValue+'"]').text();
		
        $('#'+tableHolderId+' .shipping-body-dropdown').each(function(){
        	$('#'+tableHolderId+' shipping-body-dropdown .filter-option').text(shippingHeadDDText);
            $(this).val(shippingHeadDDValue);
        });
	}).trigger('change');*/
	 
	/*modified on 24 oct lokesh/tharun*/
	$('.shipping-head-dropdown').on('change',function(){ 

	  var tableHolderId=$(this).parents().closest('.dropShipmentTable').attr('id');
	  var shippingHeadDDValue=$(this).val();
	  var shippingHeadDDText=$('#'+tableHolderId+' .shipping-head-dropdown option[value="'+shippingHeadDDValue+'"]').text();

	  $('#'+tableHolderId+' .shipping-body-dropdown').each(function(){
		   $//('#'+tableHolderId+' shipping-body-dropdown .filter-option').text(shippingHeadDDText);
		   $(this).children().find('.filter-option').text(shippingHeadDDText);
		   //$(this).val('option 1');
		   $(this).val(shippingHeadDDValue);
	  });

	}).trigger('change');


	$('.date-picker-head').on('changeDate',function(){
		 var tableHolderId=$(this).parents().closest('.dropShipmentTable').attr('id');
		 var shippingHeadDate=$(this).val();
		  $('#'+tableHolderId+' .date-picker-body').each(function(){
			$(this).val(shippingHeadDate);
			/* Changes by Swathi for Shipping Date*/	
                    var entryNo = $(this).attr('data');	    	   
         	    	   jQuery.ajax({
         	    			url : ACC.config.contextPath + '/cart/updateShippingDateForCart?expectedShipDate='+shippingHeadDate+'&entryNumber='+entryNo,
         	    			async: false,
         	    			success : function(data) {}
		});
			/* Changes by Swathi for Shipping Date*/		
	});
	});
	
	$('#shipping-head-dropdown').on('change',function(){
		  
        var shippingHeadDDValue=$(this).val();
        var shippingHeadDDText=$('#shipping-head-dropdown option[value="'+shippingHeadDDValue+'"]').text();
  
        $('.shipping-body-dropdown').each(function(){
                        $('.shipping-body-dropdown .filter-option').text(shippingHeadDDText);
                        $(this).val(shippingHeadDDValue);
        });
	}).trigger('change'); 
	
	$('#date-picker-head').on('changeDate',function(){
		  var shippingHeadDate=$(this).val();
		  $('.date-picker-body').each(function(){
		   $(this).val(shippingHeadDate);
		   /* Changes by Swathi for Shipping Date*/ 
		                    var entryNo = $(this).attr('data');         
		                  jQuery.ajax({
		                 url : ACC.config.contextPath + '/cart/updateShippingDateForCart?expectedShipDate='+shippingHeadDate+'&entryNumber='+entryNo,
		                 async: false,
		                 success : function(data) {}
		  });
		   /* Changes by Swathi for Shipping Date*/  
		 });
	 });
 
    /* End- shipping details - sep-21*/
	/* change by tharun */
	
	
	$( ".home_Contract_Link").click(function() {
		window.location.href = ACC.config.contextPath +'/my-account/contract';
	});
	
	//
	var strIsUserLoggedIn = $("#isUserLoggedIn").val();
	if(strIsUserLoggedIn !== undefined && strIsUserLoggedIn !== "" && strIsUserLoggedIn !== null){
		initSessionMonitor();
	}

	//Adding kit image in product detils page 
    if($('.kit-img').length>0){ 
        $('.kit-img').each(function(){ 
        	
                var imgKit=$(this).find('.kit-img-thumb').clone(); 
                $(imgKit).insertBefore($(this).find('.productdetail-img')); 
                $(this).find('.kit-img-thumb:eq(1)').remove(); 
        }); 
        
        var kitImg=$('#kit-img-src').val();
        $('.kit-img-product').each(function(){
        		if($(this).find('img').hasClass('PreviewImgSige')){
        			$('<img src="'+kitImg+'" class="kit-img-product-detail for-preview-kit"></img>').insertBefore($(this).find('.PreviewImgSige'));
        		}
        		else{
        			$('<img src="'+kitImg+'" class="kit-img-product-detail for-nopreview-kit"></img>').insertBefore($(this).find('.noImgPreviewImgSige'));
        		} 

        }); 
    
    }
    
    //menu alignment
    $('.selectpicker').change(function(){
   	 	ACC.globalActions.menuNdbodyHeight();
    });

    //Changes for - catalog browsing experience
    var catalogBoxHeight = $(".catalogLev1Box").height();
    $(".catalogLev2Box").css("height", catalogBoxHeight);
    $(".catalogLev2").each(function(){
    var catalogLevel2Height = $(this).height();
    if(catalogLevel2Height > catalogBoxHeight) {
    $(this).css("height", catalogBoxHeight-2);
    $(this).css("overflow-y", "scroll");
    }
    else{
    if (catalogBoxHeight > catalogLevel2Height) {
    $(this).css("height", catalogBoxHeight+2);
    $(this).css("overflow-y", "auto");
    }
    }
    });
    $(catLev1).click(function(){
    var cat_id = $(this).attr("id");
    $(catLev1).removeClass(hoverEnabledClass);
    $(catalogLev1).removeClass(activeClass);
    $(this).addClass(activeClass);
    $(catalogLev2).removeClass(activeClass);
    $("#sub_"+cat_id).addClass(activeClass);
    });
    $(document).on('mouseover', ''+catLev1+'.'+hoverEnabledClass+'', function(){
    var cat_id = $(this).attr("id");
    $(catalogLev1).removeClass(activeClass);
    $(this).addClass(activeClass);
    $(catalogLev2).removeClass(activeClass);
    $("#sub_"+cat_id).addClass(activeClass);
    $(this).parent().siblings(catalogLev2Box).show();
    }).on('mouseout', ''+catLev1+'.'+hoverEnabledClass+'', function() {
    $(catalogLev1).removeClass(activeClass);
    $(catalogLev2).removeClass(activeClass);
    $(catalogLev2Box).hide();
    });

    $(".selectpicker").selectpicker().selectpicker("render");

    /*changes for AEKL-1052 start*/
        	var appendSlide1 = $('<div class="swiper-slide"><div class="row productSwiperHolder" id="slide_1"></div></div>');
        	var appendSlide2 = $('<div class="swiper-slide"><div class="row productSwiperHolder" id="slide_2"></div></div>');
        	var appendSlide3 = $('<div class="swiper-slide"><div class="row productSwiperHolder" id="slide_3"></div></div>');
        	var appendSlide4 = $('<div class="swiper-slide"><div class="row productSwiperHolder" id="slide_4"></div></div>');
        		appendSlide1.insertBefore("#product_1");
        		appendSlide2.insertBefore("#product_5");
        		appendSlide3.insertBefore("#product_9");
        		appendSlide4.insertBefore("#product_13");
        		$("#slide_1").append($("#product_1, #product_2, #product_3, #product_4"));
        		$("#slide_2").append($("#product_5, #product_6, #product_7, #product_8"));
        		$("#slide_3").append($("#product_9, #product_10, #product_11, #product_12"));
        		$("#slide_4").append($("#product_13, #product_14, #product_15, #product_16"));


        	var swiper = new Swiper('.s1', {
        		pagination: {
        			el:'.sp1',
        			clickable: true,
        		},
        		navigation: {
        		  nextEl: '.next-1',
        		  prevEl: '.prev-1',
        		},
        	});

        	var mobile_swiper = new Swiper('.s3', {
        		navigation: {
        		  nextEl: '.next-3',
        		  prevEl: '.prev-3',
        		},
        	});
        	$(".addToCartCarousel").click(function(){
        		loadingCircleShow("show");
        		var productCode = $(this).parent().parent().siblings(".imgProductDescrp").children(".productCode").text();
        		var prodId_Qtys =productCode+":1";
        		var dataObj = new Object();
        		dataObj.source = "addToCartForm_1";
        		dataObj.prodId_Qtys = prodId_Qtys;
        		jQuery.ajax({
        			type: "POST",
        			url:  ACC.config.contextPath +'/cart/addTocartPLP' ,
        			data: dataObj,
        			success: function (data) {
        				location.reload();
        			}
        		});
        		loadingCircleShow("hide");
        	});
        	/*changes for AEKL-1052 end*/
	
});	


/* Loading Icon Script Start by Vijay*/
function loadingCircleShow(option)
{
	if(option=="show"){
		 $("#laodingcircle").show();
	}else if(option=="hide"){
		
	 $("#laodingcircle").hide();
	}
	
}
/* Loading Icon Script End */


//session time out start

//LOAD JqUERY FIRST
//how frequently to check for session expiration in milliseconds
var sess_pollInterval = ACC.config.sessionPollInt;
//how many minutes the session is valid for
var sess_expirationMinutes = ACC.config.sessionExpMin; //15
//how many minutes before the warning prompt
var sess_warningMinutes = ACC.config.sessionWarnMin; //13

var sess_intervalID;
var sess_lastActivity;

function initSessionMonitor(){
	
	sess_lastActivity = new Date();
	sessSetInterval();
  $(document).on("keypress session", function(ed,e){
    sessKeyPressed(ed,e);
  });
}

function sessSetInterval(){
	sess_intervalID = setInterval('sessInterval()', sess_pollInterval);
}

function sessClearInterval(ed,e){
    clearInterval(sess_intervalID);
}

function sessKeyPressed(ed,e){
	sess_lastActivity = new Date();
}

function sessPingServer(){
	//call an ajax function to keep-alive our session if need any update in server side
	//callHeartBeatAJAX();
}

function callHeartBeatAJAX(){
	var strMessage = "HELLO";         
    jQuery.ajax({
    	url : ACC.config.contextPath + '/login/ecoHeartBeat',
    	async: false,
    	success : function(data) {
    		loadingCircleShow("hide");
    	}
	});
}

function sessionLogOut(){
	window.location.href = ACC.config.contextPath+ '/logout';	
}

function sessInterval(){
	var now = new Date();
	var diff = now - sess_lastActivity
	var diffMins = (diff/1000/60);
	
	if(diffMins >= sess_warningMinutes){
		//stop timer
		sessClearInterval();
		//prompt for attentation
		sessionConfirmDialog(now);
	}else{
		sessPingServer();
	}
}

//warn before expiring
function sessionConfirmDialog(now){
	//should not close popup even if i clicked outside of the popup
	   $('#sessionTimeout-popup').modal({
		    backdrop: 'static',
		    keyboard: false  
	 });
	$('#sessionTimeout-popup').modal('show');
	$("#sessionInfoText1").text((sess_expirationMinutes-sess_warningMinutes));
	$("#sessionInfoText2").text(now.toTimeString());
	
	$(document).off('click', '#logoff-btn').on('click', '#logoff-btn',function(e) {
		sessionCallback(false,now);
	});
	$(document).off('click', '#logIn-btn').on('click', '#logIn-btn',function(e) {
		sessionCallback(true,now);
	});
}

 function sessionCallback(value,now) {
	if (value) {
		loadingCircleShow("hide");
		now = new Date();
		diff = now - sess_lastActivity;
		diffMins = (diff/1000/60);
		console.log("sessLogOut invoking if diffMins : " + diffMins +" > sess_expirationMinutes : "+ sess_expirationMinutes );
		if(diffMins > sess_expirationMinutes){
			//timed Out
			sessionLogOut();
		}else{
			console.log("reset time  invoking.");
			//reset inactivity timer
			sessPingServer();
			sessSetInterval();
			sess_lastActivity = new Date();
		
		}
		
    } else {
    	console.log("sessionLogOut invoking.");
    	loadingCircleShow("hide");
		sessionLogOut();
    }
}
//session time out end
 var toggleBtnClicked=false;
 
 ACC.globalActions = {

	bindAll: function()
	{

		ACC.globalActions.menuNdbodyHeight();
	},	
 	menuNdbodyHeight: function(){ 
		 /* var leftHeight=$('#jnj-usr-details-menu').innerHeight();  
		  var rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
		  var bodyContentHeight=leftHeight-($('#jnj-header').innerHeight()+$('#jnj-footer').innerHeight());*/
		  
		  var leftHeight=$('#jnj-usr-details-menu').innerHeight();		
		
		  var rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight(); 
		  var menuDetailsHeight=$('#jnj-menu-list').innerHeight()+$('#usr-details-row').innerHeight()
		  
		  var productnameheight= $('.productspanel').innerHeight();
		  var productviewheight= $('.ps-current').innerHeight();
		  
		  if(productviewheight<productnameheight){
		    
		    $('.ps-current').removeAttr('style');
		   $('.ps-current').css('height',productnameheight+'px !important');
		  }
		  
			/*    
		  	if(leftHeight==rightHeight){
	           $('#jnj-body-content').innerHeight('auto');
	           rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
	           $('#jnj-usr-details-menu').innerHeight(rightHeight);
	       
			}
			else if(toggleBtnClicked){
			     if(leftHeight<rightHeight){
			    	 $('#jnj-usr-details-menu').innerHeight(rightHeight); 
			     }else{
			    	  $('#jnj-body-content').innerHeight(bodyContentHeight);
			     }
			     
			}
			else{
			     $('#jnj-body-content').innerHeight(bodyContentHeight);
			} */
		  
		  
		  if(leftHeight!=null){

			  	if(leftHeight==$(window).height() && rightHeight<=leftHeight){
			  		return true;
			  	}
			  	else if(leftHeight==rightHeight && toggleBtnClicked || leftHeight<rightHeight && toggleBtnClicked || leftHeight==rightHeight ||leftHeight<rightHeight || menuDetailsHeight<=rightHeight){
			  		
					$('#jnj-body-content').innerHeight('auto');
					rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
					$('#jnj-usr-details-menu').innerHeight(rightHeight);
				}
			  	else{
			  		
					var bodyContentHeight=leftHeight-($('#jnj-header').innerHeight()+$('#jnj-footer').innerHeight());
					$('#jnj-body-content').innerHeight(bodyContentHeight);
				}  
			} 
		}
 }	
 

