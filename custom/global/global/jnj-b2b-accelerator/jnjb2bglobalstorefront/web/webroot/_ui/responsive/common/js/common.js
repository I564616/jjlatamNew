jQuery(document).ready(function() {
	/** Start: sanchit.a.kumar :: Code to fetch error message from message label in case of spring form tags **/
	// Fetching each label with the class getErrorMessage. This label will be for the spring form input
	// who's data message is required. Hence it is required to give the for attribute of the label same 
	// as the id of the spring form input
	$("label.getErrorMessage").each(function(){  
		var inputId = $(this).attr("for"); // fetching the id of the input stored in the for attribute of the label.
		var errorMessage = $(this).attr("data"); // We will store the error message label tag in the 'lang' attribute in the label tag.
		if($("#" + inputId).length!=0) {
			$("#" + inputId).attr("data-msg-required", errorMessage); // Fetching the spring form input and setting the 'data-msg-required' with errorMessage
		} else {
			$("input[name='"+inputId+"']").attr("data-msg-required", errorMessage); // Fetching the spring form input and setting the 'data-msg-required' with errorMessage
		}
	});
	/** Start: Search functionality for searching a Template by pressing enter key**/
	if($('#templates').length>0){
		$(document).keypress(function(e) {
		    if(e.which == 13) {
		    	$('#templates').submit();
		    }
		});
	}
	/** End: Search functionality for searching a Template by pressing enter key**/
	
	/** End: sanchit.a.kumar :: Code to fetch error message from message label in case of spring form tags **/
	/** Start: sanchit.a.kumar :: Code for ajax loading feedback spinner **/
	$("#ajaxCallOverlay").fadeOut(800); // once application document is ready, the overlay shall fade
	$("#search").on("focus",function(){
		$.ajaxSetup().global = false; // This disables the ajax call detection for the search box to allow auto complete 
	});
	$("#search").on("blur",function(){
		$.ajaxSetup().global = true; // This re-enables the ajax call detector for the application once the search field is no longer in focus
	});
	/** AJAX START detects any ajax call in the whole application 
	  * 	To disable use $.ajaxSetup().global = false
	  * 	To re enable use $.ajaxSetup().global = true
	 **/
	$("#ajaxCallOverlay").ajaxStart(function(e) {
		$(this).fadeTo(0, 0.6); // on detection of any ajax call, overlay must fade in to 0.6 opacity 
		$("#modal-ui-dialog").fadeTo(0, 0.6); // on detection of any ajax call, dialog and spinner must fade in to 0.6 opacity
	});
	/** AJAX STOP detects termination of any ajax call in the whole application 
	  * 	To disable use $.ajaxSetup().global = false
	  * 	To re enable use $.ajaxSetup().global = true
	 **/
	$("#ajaxCallOverlay").ajaxStop(function(e){
		$(this).hide(); // on detection of termination of any ajax call, overlay must fade out
		$("#modal-ui-dialog").hide(); // on detection of termination of any ajax call, dialog and spinner must fade out
	});
	/** End: sanchit.a.kumar :: Code for ajax loading feedback spinner **/
	
	
	jQuery('#dvtab ul li a').click(function(e){		
		jQuery('#dvtab ul li a').removeClass('Tabactive');
		jQuery('#dvtab .listWrap .rdiCont').hide();		
		var currTabLi = jQuery(this).parent().attr('class');
		jQuery(this).addClass('Tabactive');
		jQuery('#'+currTabLi).show();
		//alert(currTabLi);
		return false;
		
	});
	$("#productId").val($("#productId").attr("placeholder")).addClass("placeholderForce");
	$("#productId").focus(function() {
		var input = $(this);
		if (input.val() == input.attr("placeholder")) {
			input.val("");
			input.removeClass("placeholderForce");
		}
	}).blur(function() {
		var input = $(this);
		if (input.val() == "" || input.val() == input.attr("placeholder")) {
			input.addClass("placeholderForce");
			input.val(input.attr("placeholder"));
		}
	}).blur();
	
//Replenish Schedule button click:: checkout
$('#showreplenishschedule').click(function(e){
	e.preventDefault();
	$('.replenishSchedule').css('display','block');	
});
$('#hidereplenishschedule').click(function(e){
	e.preventDefault();
	$('.replenishSchedule').css('display','none');	
});

//Commented by surabhi 
/*$("#contactuspopup .contactUsFormSubmitLb").click(function(){
	if($("#contactuspopup .contactUsFormLb").valid()){
		$("#contactuspopup .contactUsFormLb").ajaxSubmit({type: 'post', url: ACC.config.contextPath + "/help/contactUs"});
		//window.location.href = ACC.config.contextPath+ '/resources/usefullinks';
		
		$("#contactuspopup #contactUsFormPage2Lbs").css("display","block");
		
	}else{
		$("#contactuspopup .contactUsFormLb").find(".contactUsGlobalError").find(".error").show();
	}
		
	});*/

//AAOL-3624
//AAOL-4914 changed by surabhi
$("#contactuspopup .contactUsSubmitBtn").click(function(){
		//AAOL-4914
	var dataObj = new Object();
	dataObj.captchaResponse = $("#captchaResponse").val();
	
	jQuery.ajax({
		type: "GET",
		url: ACC.config.contextPath + "/help/validateCaptchaResponse",
		data: dataObj,
		success: function (data) {
			if (data)
			{ 
	                   		if($("#contactuspopup .contactUsFormLb").valid()){
								$("#contactuspopup .contactUsFormLb").ajaxSubmit({
									type: 'post',
									url:  ACC.config.contextPath+ "/help/contactUs"
									});
								$("#contactuspopup #contactUsFormPage2Lbs").css("display","block");
								
								//AAOL-4914
								$('#contactHelp-agree').prop('checked',false);
								checkHelpContactUs();
								//AAOL-3624
								$('#contactHelp-agree').click(function(){checkHelpContactUs();});
							    $('#helpContactUsFormSubmitLb').prop('disabled',true);
							    function checkHelpContactUs(){
							        var mandatoryFieldFlag=true;
							       
							        //checkbox
							        if($('#contactHelp-agree').prop('checked')==false){
							                        mandatoryFieldFlag=false;
							        }
							        if(mandatoryFieldFlag==true){
							        	  $('#helpContactUsFormSubmitLb').prop('disabled',false);
							              $('#helpContactUsFormSubmitLb').removeClass('btn-disabled-style');
							        }
							        else{ 
							        	 $('#helpContactUsFormSubmitLb').prop('disabled',true);
							        	 $('#helpContactUsFormSubmitLb').addClass('btn-disabled-style');
							        }
							    }
							    //End
	                   		}
	                   		else{
		                		$("#contactuspopup .contactUsFormLb").find(".contactUsGlobalError").find(".error").show();
		                   	  }
	                   		//Recaptcha.reload();
	                   		grecaptcha.reset();
	                   	  }else{
              				$('.contactUsErrorCaptcha').html( $("#contactUsCaptchaId").val());
              				$('.contactUsErrorCaptcha').show();
              				//Recaptcha.reload();
              				grecaptcha.reset();
                         }
	                   	ACC.globalActions.menuNdbodyHeight();
                  }
         });
		
});

//End
$("#contactuspopup .backToContactUsForm").click(function(e){
	e.preventDefault();
	$("#contactuspopup .contactUsFormLb").show();
	$("#contactuspopup .contactUsFormPage2Lb").hide();
	$("#contactuspopup  #contactUsMessage").val("");
	$("#contactuspopup #contactUsSubjectLb").val("");
	$("#contactuspopup .contactUsSubmitBtn").show();
	$("#contactuspopup #contactUsOrderNumber").val("");
	$("#contactuspopup #contactUsFormPage2Lbs").css("display","none");
	window.location.reload();
	
});

$("#contactuspopup  #contactUsSubjectLb").change(function(){
	
	if($(this).val() == "1") {
		
		$("#contactuspopup .contactUsOrderNumberLb").css("display","block");
	} else {
		$("#contactuspopup .contactUsOrderNumberLb").css("display","none");
	}
});	

/*--------------------copy shipping address to billing address----------------------*/
$("#billToLocation").on("click", copyAddress);

function copyAddress(){

	if(this.checked) {
		
		$(".copyToShipping").each(function(i, el){
	        $(".copyFromShipping").eq(i).val($(this).val());
	    });

	    $("label.error").hide();
			$(".error").removeClass("error");	
	}

}

/*-------------------- End copy shipping address to billing address----------------------*/

/*--------------------copy shipping address to billing address----------------------*/
$("#billToLocation").on("click", copyAddress);

function copyAddress(){
	if(this.checked) {
		$('#shipCountry').val($("#billCountry1").val());
		$('#shipToState').val($("#billToState").val());
		$("#shipAddress1").val($("#billAddress1").val());
		$("#shipAddress2").val($("#billAddress2").val());
		$("#shipCity").val($("#billCity").val());
		$("#shipPostalCode").val($("#billPostalCode").val());
		$("#shipToZipCode").val($("#billToZipCode").val());

		$(".profileReg .profileRegBlock:nth-child(3) label.error").hide();
			$(".profileReg .profileRegBlock:nth-child(3) .error").removeClass("error");	
	}
	else{
		$('#shipCountry').val("");
		$('#shipToState').val("");
		$("#shipAddress1").val("");
		$("#shipAddress2").val("");
		$("#shipCity").val("");
		$("#shipPostalCode").val("");
		$("#shipToZipCode").val("");	
	}
}
/*-------------------- End copy shipping address to billing address----------------------*/
//Add Row on click-- Services Page

$("input#radiocpf , input#radiotax").click(function(){
	$('#cpfdiv').show();		
});

$("input#radiocnpj").click(function(){
	$('#cpfdiv').hide();		
});


var countAddLine=0;
$('.removeLines').hide();
$('a.addMoreLines').on('click', function(e){
	countAddLine++;
	if(countAddLine >= 0)
	{
	$('.removeLines').show();
	}
	e.preventDefault();
    var thisRow = $(this).parent().prev();
    newRow = thisRow.clone(true).insertAfter(thisRow);
	}); 
	
	$('a.removeLines').on('click', function(e){
		countAddLine--;
		e.preventDefault();
		if(countAddLine >= 0)
		{
		$(this).parent().prev().remove();
			if(countAddLine == 0)
			{$('.removeLines').hide();}
		}
		else
		{
			countAddLine--;
			$('.removeLines').hide();			
			return false;	
		}
	});
//Add Group on click-- Services Page
$('a.addMoreGroup').on('click', function(e){
		e.preventDefault();
    var thisRow = $(this).parent().prev();
    newRow = thisRow.clone(true).insertAfter(thisRow);
	}); 
//Datepicker checkout page
$('.date').datepicker();
//checkout page browse button
//$('#browsebtn').bind('click' , function(){		
//	var total=	$('#uploadfile_wrap input').length;	
//	var inputuploadEle=	$("#uploadfile_wrap").find('input').eq(total-1);	
//			inputuploadEle.trigger('click');	
	
//});


	
// HOME page: Browse button functionality

$('#imageDrop').bind('click' , function(){							
	$('#uploadImage').trigger('click');
});
$('#uploadImage').bind('change', function() {
	var str = "";
	str = $(this).val();
	if(!str=='') 
	{ 
		$('.filename').css('display' , 'block');
		$('#uploadbtnWrapper').css('display' , 'block');
	} 
	var clean=str.split('\\').pop();
	$(".filename").text(clean);
});

$('#placeorderfile').click(function(){
	$('.uploadOrderBody > .filename').css('display','none');
	$('#uploadbtnWrapper').css('display' , 'none');
	/*Changes for AAOL-5676*/
	$("#uploadmultifilehome").attr('disabled',false);
	$('.image-upload img').removeClass('opaque').css('cursor','pointer');
	
});

$('#imageDrop').click(function(){
	
		$('.uploadOrderBody > .success').css('display','none');
		
});
					
//if the user click on "Select All" checkbox, then all other checkboxes will be checked.
//    $("#selall").click(function(){  
//	
//				 if($("#selall").is(':checked'))
//			{
//					$(".prodRow ").each(function(){
//						$('.selprod').attr('checked', 'checked');
//						$('.disabledProd .selprod').removeAttr('checked');						
//					  });
//			}
//if he unchecks the "Select All" checkbox , then everything will be unchecked.			
//				 if(!$("#selall").is(':checked'))
//			{
//					$(".prodRow ").each(function(){
//						$('.selprod').removeAttr('checked');
//					  });
//			}
//			
			
				
   //});	
	
//And if any one of the individual checkbox is unchecked, then the "Select all" will be unchecked.
	$(".selprod").click(function(){
		$(".selprod").each(function(){
		if($(this).is(':checked'))
		{
				$("#selall").removeAttr('checked');
		}
			  });
     });


//Help page right panel
jQuery('#sendbutton').click(function(e){
		if(jQuery("#contactusform").valid())
		{	$('.navListBodyFirst').hide();	
			$('.navListBodySecond').show();
					
		}
	
		if($("#helpsubject").attr("value")==0)
		{	
			$('label#subject').css('color','#B41601');
		
		}
		else
		{	
			$('label#subject').css('color','#646464');
		}
		if (!$.trim($("#helpmessage").val())){
			$('label#message').css('color','#B41601');
		}
		else
		{
			$('label#message').css('color','#646464');
		}
		
		}); 

$('#helpsubject').bind('blur' , function(){
		if($("#helpsubject").attr("value")==0)
		{	
			$('label#subject').css('color','#B41601');
		
		}
		else
		{	
			$('label#subject').css('color','#646464');
		}
});
$('#helpmessage').bind('keyup' , function(){
		if (!$.trim($("#helpmessage").val())){
			$('label#message').css('color','#B41601');
		}
		else
		{
			$('label#message').css('color','#646464');
		}
});
		
jQuery('#toggleback').click(function(e){
		e.preventDefault();
		$('.navListBodySecond').hide();
		$('.navListBodyFirst').show();	
			
}); 


//Make Product disable in the Product Catalog
	function disabled(){
		$('.productCatalog').find('.disabledProd a').css('cursor', 'auto');
		$('.productCatalog').find('.disabledProd input').attr('disabled' , 'disabled');
		
		    $('.productCatalog .disabledProd a').on("click", function (e) {
       			 e.preventDefault();
  			 });
		}
		disabled();

//Make entire Product clickable in the Product Catalog
		$('.prodClick').click(function(e){
			var href=$(this).find('a').attr('href');
			e.preventDefault()
			$(location).attr('href',href);
		});
		
/* Hide form input values on focus*/ 
    $('input.placeholder:text').each(function(){
        var txtval = $(this).val();
        $(this).focus(function(){
            if($(this).val() == txtval){
                $(this).val('')
				$(this).css('color' , '#646464');
            }
        });
        $(this).blur(function(){
            if($(this).val() == ""){
                $(this).val(txtval);
				$(this).css('color' , '#ccc');
            }
        });
    });	

/* Order Confirm Split expand and collapse functionality*/ 
$('.expandorderconfirm').click(function(e){
		e.preventDefault();
		$(this).parent().parent().next().css('display','block');
		$(this).hide();
		$(this).parent().parent().find('.column1 h4').css({"color":"#0a8caa"})
	});
$('.collapsepanel').click(function(e){
		e.preventDefault();
		$(this).parent().parent().css('display','none');
		$(this).parent().parent().prev().find('.column3 a.expandorderconfirm').show();
		$(this).parent().parent().prev().find('.column1 h4').css({"color":"#828282"})
});



/* Password Tip */ 		
	$('li.pwdTip').hover(function(){
		$(this).find('.pwdTipDesc').fadeIn(0);
		}, function() {
		$(this).find('.pwdTipDesc').fadeOut(0);
	});
	
/* Information Tip Register page */ 	
	$('.registerCompanyInfo>ul>li input.infoTip').focusin(
	function(){
	$(this).parent().find('.pwdTipDesc').show()
	}).focusout(
	function(){
	$(this).parent().find('.pwdTipDesc').hide();
	});

	
//Make entire Contract Row clickable
		$('.contractRowClick ').click(function(e){
			var href=$(this).find('a').attr('href');
			e.preventDefault()
			$(location).attr('href',href);
		
		});
		
//Make entire Order History Row clickable
		$('.orderHistoryRowClick ').click(function(e){
			var href=$(this).find('a').attr('href');
			e.preventDefault()
			$(location).attr('href',href);
		
		});
		
		
/* Search button on header */ 
		$('#searchresult').bind('click' , function(e){
			//e.preventDefault();
			
			self.location.href='SearchResult.html';
			//$(mine).delay(10);
				//window.location=mine;
				//alert(mine);
		});
		
		
					// if the user click on "Select All" checkbox, then all
					// other checkboxes will be checked.
					$(".selectall_contract").click(
							function() {

								if ($(".selectall_contract").is(':checked')) {
									$(".contractDetailRow").each(
											function() {
												$('.select_contract').attr('checked','checked');
												});
								}
								// if he unchecks the "Select All" checkbox ,
								// then everything will be unchecked.
								if (!$(".selectall_contract").is(':checked')) {
									$(".contractDetailRow ").each(function() {
										$('.select_contract').removeAttr('checked');
									});
								}

							});

					// And if any one of the individual checkbox is unchecked,
					// then the "Select all" will be unchecked.
					$(".select_contract").click(function() {
						$(".select_contract").each(function() {
							if ($(this).is(':checked')) {
								$(".selectall_contract").removeAttr('checked');
							}
						});
					});		
	
	/** Cart PDP popup **/			
	$(document).on("click",".showProductDeatils", function(e){
		e.preventDefault();
		jQuery.ajax({
			url : ACC.config.contextPath +'/**/p/productDetailPopUp?productCode='+ $(this).attr('data'),
			type: 'GET',
			content: 'text/html',
			success : function(data) {
				$('#product-details-popupholder').html(data);
			    $('#product-detail-popup').modal('show');
			}
		
		});
	});	
	/** Added for CPSIA **/
	bindCPSIASort();
	if($("#sortBy").val() == "") {
		$("#prdCode").addClass("asc");
	}
	$(".cpsia .reportBody span").each(function(){
		if($.trim($(this).text()) == "") {
			$(this).attr("style", "display:inline-block");
		}
	});
	
	
	$(".moreBroadCastBtn").click(function(e) {
		e.preventDefault();
		var popcontent=$(this).prev().val();
		$('#os-pls-read-popup .modal-body').html(popcontent);
	});
	
	/** Added for broadcast messages **/
	$(".broadcastMessageContainer").each(function(){
		var contentLength = 0;
		var contentBlock=$(this).find(".broadcastMessageContent");
		var boradcastContent=$(contentBlock).html();
		$(this).find(".broadcastMessageHidden").val(boradcastContent);
		/**
		 * Condition for more than one p tag in the content
		 */
		
		if(contentBlock.find("p").length > 1) {
			$(this).find(".moreBroadCastBtn").show();
			var count=0;
			contentBlock.find("p").each(function(){
				if(count!=0){
					$(this).remove();
				}
				count++;
			})
		}
		/**
		 * Condition for one p tag in the content
		 */
		else if (contentBlock.find("p").length == 1) {
			if($(contentBlock.find("p")).children().length > 0) {
				$(contentBlock.find("p")).children().each(function(){
					contentLength+=this.offsetWidth;
				});
				if(contentLength>contentBlock.width()) {
					$(this).find(".moreBroadCastBtn").show();
				}
			} else {
				if($.trim($(contentBlock.find("p")).text()).length > 90) {
					$(this).find(".moreBroadCastBtn").show();
				}
			}
		}
		/**
		 * Condition for no p tag in the content
		 */
		else {
			if($.trim($(contentBlock).text()).length > 90) {
				var broadcastId = $(contentBlock).find(".broadCastId");
				$(contentBlock).remove(".broadCastId");
				
				$(contentBlock).text($.trim($(contentBlock).text()).substring(0,90));
				$(contentBlock).append(broadcastId);
				$(this).find(".moreBroadCastBtn").show();
			}
		}
	});

	/** Added for user management for removal of pre filled country codes **/
    if($("#myprofilecheck").length!=0){
    	$(".removeAdditionalText").each(function(){
			if($(this).val()=="+1" || $(this).val()=="+1|"){
				$(this).val("");
			}
		});
	}
    
    /** Added for cut report **/
    $(".cutReportPage .reportBody .reportRow span").each(function(){
    	if($.trim($(this).html())==""){
    		$(this).attr("style","display:inline-block");
    	}
    });

    /** Added for multi purchase analysis UI fix **/
    $(".reportBody").find("span").each(function(){
    	if($.trim($(this).html())==""){
    		$(this).attr("style","display:inline-block");
    	}
    });
    
    /** Added for login page contact us auto open **/
   if ($('#helpFlag').val()=='true') {
	    $.get(ACC.config.contextPath + "/help/contactUs").done(function(data) {
	    $('#contactuspopupNew').remove();        
	    $('#contactuspopupholder').html(data);
	    $('#contactuspopupNew').modal('show');
	    $(".contactUsFormPage2Lb").hide();
	    $(".contactUsSubmitBtn").click(function(){
	    if($(".contactUsFormLb").valid()){
	    $(".contactUsFormLb").find(".contactUsGlobalError").find(".error").hide();
	    //$(".contactUsFormLb").ajaxSubmit({type: 'post', url: ACC.config.contextPath + "/help/contactUs"});
	    	$('.contactUsFormLb').ajaxSubmit({
	    	  success : function(data)  {             
	    			 $(".contactUsSubmitBtn").hide();
	    			 $(".contactUsFormLb").hide();
	    			 $(".contactUsFormPage2Lb").show();
	    	  }
	    	});
	
	    }else{
	    	$(".contactUsFormLb").find(".contactUsGlobalError").find(".error").show();
	    }
	    });
	
	    $(".backToContactUsForm").click(function(e){
	    	e.preventDefault();
	    	$(".contactUsFormLb").show();
	    	$(".contactUsFormPage2Lb").hide();
	    	$("#contactUsMessage").val("");
	    	$("#contactUsSubjectLb").val("");
	    	$(".contactUsSubmitBtn").show();
	    	$("#contactUsOrderNumber").val("");
	    	$(".contactUsOrderNumberLb").css("display","none");
	    });
	
	    $("#contactUsSubjectLb").change(function(){
	    	if($(this).val() == "1") {
	    	  $(".contactUsOrderNumberLb").css("display","block");
	    	} else {
	    	  $(".contactUsOrderNumberLb").css("display","none");
	    	}
	    });          
	
		    $('.privacypolicypopupFromContactUs').on('click',function(e) {
		    	$.get(ACC.config.contextPath+ "/privacyPolicy").done( function(data) {
						 $('#contactuspopupNew').modal('hide');
		
						 $('#privacypopupNew').remove();            
		
						 $('#privacypolicypopuopholder').html(data);
		
						 $('#privacypopupNew').modal('show');
		
						 $('#privacypopupNew').css("z-index","2000");
				  });
		    });
	    });
    }
     /** Date picker read only fix **/
    if($(".orderDetailsPage") == null || $(".orderDetailsPage") == '')
    $(".iconCalender").attr("readonly","readonly");
}); 
function initWriteReviewAction() {
	$('#write_review_action_main').click(function(e){
		e.preventDefault();
		$.scrollTo('#prod_tabs', 300, {axis: 'y'});
		$('#reviews').hide();
		$('#write_reviews').show();
		$( "#prod_tabs" ).tabs( "option", "selected", $('#tab_strip').children().size() - 1 );
		$('#reviewForm input[name=headline]').focus();
	});
}

function initBasedOnReviewsAction() {
	$('#based_on_reviews').click( function(e) {
		e.preventDefault();
		$.scrollTo('#prod_tabs', 300, {axis: 'y'});
		$( "#prod_tabs" ).tabs( "option", "selected", $('#tab_strip').children().size() - 1 );
		$('#write_reviews').hide();
		$('#reviews').show();
		$('#read_reviews_action').click();
		
	});
}

function initPageEvents() {
	$(".modal").colorbox();
	$("#carousel_alternate img").click(function() {
		$("#primary_image img").attr("src", $(this).attr("data-primaryimagesrc"));
		$("#zoomLink").attr("href", "${zoomImageUrlTemplate}".replace("POSITION", $(this).attr("data-galleryposition")));
		$("#imageLink").attr("href", "${zoomImageUrlTemplate}".replace("POSITION", $(this).attr("data-galleryposition")));
	});

	initWriteReviewAction();
	initBasedOnReviewsAction();


	$("#Size").change(function () {
		var url = "";
		var selectedIndex = 0;
		$("#Size option:selected").each(function () {
			url = $(this).attr('value');
			selectedIndex = $(this).attr("index");
		});
		if (selectedIndex != 0) {
			window.location.href=url;
		}
	});

	$("#variant").change(function () {
		var url = "";
		var selectedIndex = 0;
		
		$("#variant option:selected").each(function () {
			url = $(this).attr('value');
			selectedIndex = $(this).attr("index");
		});
		if (selectedIndex != 0) {
			window.location.href=url;
		}
	});
}
/** Cart PDP popup **/	
function showOrderDetailsPopUp(e,data) {
	e.preventDefault();
	/*$.colorbox({
		html : data,		
		height : 'auto',
		width : '1000px',
		overlayClose : false,
		onComplete : function() {
			$("#changeOrderCancel").click(function(e) {
				e.preventDefault();
				$.colorbox.close();
			});
			initPageEvents();
			
			setTimeout(function() {
				if($.query.get('tab') == 'writereview') {
					$('#write_review_action_main').click();
				}
				if($.query.get('tab') == 'readreviews') {
					$('#based_on_reviews').click();
				}
			}, 100);
		}
	});*/
}
/** Added for CPSIA **/
function bindCPSIASort(){
	$(".sortCpsia").click(function(){
		var order = "ASC";
		if($(this).hasClass("asc")) {
			$(this).removeClass("asc");
			$(this).addClass("desc");
			order = "DESC";
		} else if ($(this).hasClass("desc")) {
			$(this).removeClass("desc");
			$(this).addClass("asc");
		} else {
			$(this).addClass("asc");
		}
		var dataObj = new Object();
		dataObj.sortBy = $(this).attr("data") + "_" + order;
		jQuery.ajax({
			type: "POST",
			url: ACC.config.contextPath + '/resources/cpsia',
			data: dataObj,
			success: function (data) {
				$(".cpsia .userManagementBody").html(data);
				var sortByColumn = $("#sortBy").val().split("_")[0];
				var sortByOrder = $("#sortBy").val().split("_")[1];
				$("#" + sortByColumn).addClass(sortByOrder.toLowerCase());
				bindCPSIASort();
				$(".cpsia .reportBody span").each(function(){
					if($.trim($(this).text()) == "") {
						$(this).attr("style", "display:inline-block");
					}
				});
			},
			error: function(){
				$(".asc, .desc").each(function(){
					$(this).removeClass("asc");
					$(this).removeClass("desc");
				});
			}
		});
	});
}

$('.cust_acc div:odd').css( 'margin-right' , '0' );
$('.myservice_form div:odd').css( 'margin-right' , '0' );

/* MY ReviewOrder for HTML */ 
  $('#indirectcusto').click(function(e) {
	  e.preventDefault();
	  $(this).hide();
	  $('#insertindirectcuscode').show();
      $('.insertInput').show();
  });

/* MY ReviewOrder for HTML */ 	

/* My Profile */
		jQuery(function($){
		
		$('#myprofile #updatepersonalinfo').bind('click' , function(e){
			e.preventDefault();
			$('#myprofileview').css('display' , 'none');
			$('#myprofileedit').css('display' , 'block');
			$('#myprofileresetpassword').css('display' , 'none');
			$('#myprofilesecretquestion').css('display' , 'none');
		});
		
		$('#myprofile #changepassword').bind('click' , function(e){
			e.preventDefault();
			$('#myprofileview').css('display' , 'none');
			$('#myprofileedit').css('display' , 'none');
			$('#myprofileresetpassword').css('display' , 'block');
			$('#myprofilesecretquestion').css('display' , 'none');
		});
		
		$('#myprofile #updatesecretques').bind('click' , function(e){
			e.preventDefault();
			$('#myprofileview').css('display' , 'none');
			$('#myprofileedit').css('display' , 'none');
			$('#myprofileresetpassword').css('display' , 'none');
			$('#myprofilesecretquestion').css('display' , 'block');
		});
		
		$('#myprofile .backtomyprofile').bind('click' , function(e){
			e.preventDefault();
			$('#myprofileview').css('display' , 'block');
			$('#myprofileedit').css('display' , 'none');
			$('#myprofileresetpassword').css('display' , 'none');
			$('#myprofilesecretquestion').css('display' , 'none');
			$('#myprofileedit .success').css('display' , 'none');
		});
		

		
/* User Management */
		
		$('#usermanagement .temp-backusermanagement').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'block');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'none');			
			$('#usermanagement #deleteuser').css('display' , 'none');	
		});
		
		$('#usermanagement #createnewuser').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagement #adduser').css('display' , 'block');
			$('#usermanagement #usermanagementprofile').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'none');
			$('#usermanagement #deleteuser').css('display' , 'none');			
		});
		
		$('#usermanagement .edit').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'none');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'block');			
			$('#usermanagement #deleteuser').css('display' , 'none');	
		});
		
		$('#usermanagement .temp-disableuser').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'none');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'block');
			$('#usermanagement #edituser').css('display' , 'none');			
			$('#usermanagement #deleteuser').css('display' , 'none');	
		});
		
		$('#usermanagement .temp-userprofile').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'none');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'block');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'none');			
			$('#usermanagement #deleteuser').css('display' , 'none');	
		});
		
		$('#usermanagement .temp-deleteuser').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'none');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'none');	
			$('#usermanagement #deleteuser').css('display' , 'block');			

		});
		
		$('#deleteuser #savesuccess').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'block');
			$('#usermanagementprofile .successdelete').css('display' , 'block');
			$('#usermanagement #deleteuser').css('display' , 'none');	
			$('#usermanagementprofile .successnewuser').css('display' , 'none');
		});
		
		$('#usermanagement #temp-edituser').bind('click' , function(e){
			e.preventDefault();
			$('#usermanagementprofile').css('display' , 'none');
			$('#usermanagement #adduser').css('display' , 'none');
			$('#usermanagement #userprofile').css('display' , 'none');
			$('#usermanagement #disableuser').css('display' , 'none');
			$('#usermanagement #edituser').css('display' , 'block');			
			$('#usermanagement #deleteuser').css('display' , 'none');	
		});
	
		
	
		
		
		
		
/* My Contract */
		$('#mycontract .gotomycontractdetail').bind('click',function(e){
			e.preventDefault();
			$('#mycontractdetail').css('display','block');
			$('#mycontractsview').css('display','none');
		});
		
		
		
		/* Template Order */
		$('#templateorder .delOrder').bind('click',function(e){
			e.preventDefault();
			$('#templateorderdelete').css('display','block');
			$('#templateorderdefault').css('display','none');
		});
	
		
		$('#templateorderdelete #temp-templateordersuccess').bind('click' , function(e){
			e.preventDefault();
			$('#templateorderdefault').css('display' , 'block');
			$('#templateorderdefault .successdeleteorder').css('display' , 'block');
			$('#templateorderdelete').css('display' , 'none');	
			
		})
		$('#templateorderdefault #temp-temporderdetail').bind('click',function(e){
			e.preventDefault();
			$('#templateorderdetail').css('display' , 'block');
			$('#templateorderdelete').css('display','none');
			$('#templateorderdefault').css('display','none');
		});
		
		/* Order History Page */
		$('#orderhistoryview .gotoOrderHstryDetail').bind('click',function(e){
			e.preventDefault();
			$('#orderhistorydetail').css('display','block');
			$('#orderhistoryview').css('display','none');
			$('#orderhistoryinvoice').css('display','none');
		});
		$('#orderhistorydetail .gotoinvoicepage').bind('click',function(e){
			e.preventDefault();
			$('#orderhistorydetail').css('display','none');
			$('#orderhistoryview').css('display','none');
			$('#orderhistoryinvoice').css('display','block');
		});
		$('#orderhistoryinvoice .gotodetailpage').bind('click',function(e){
			e.preventDefault();
			$('#orderhistorydetail').css('display','block');
			$('#orderhistoryview').css('display','none');
			$('#orderhistoryinvoice').css('display','none');
		});
			/* Order Replenishment Page */
		$('#orderreplenishview .gotoreplenishdetail').bind('click',function(e){
			e.preventDefault();
			$('#orderreplenishdetail').css('display','block');
			$('#orderreplenishview').css('display','none');
			$('#orderreplenishremove').css('display','none');
		});
		
		$('#orderreplenishdetail .gotoremovereplenish,#orderreplenishview .gotoremovereplenish').bind('click',function(e){
			e.preventDefault();
			$('#orderreplenishdetail').css('display','none');
			$('#orderreplenishview').css('display','none');
			$('#orderreplenishremove').css('display','block');
		});
		$('#orderreplenishremove .successremovemsg').bind('click' , function(e){
			e.preventDefault();
			$('#orderreplenishview').css('display' , 'block');
			$('#orderreplenishview .successremove').css('display' , 'block');
			$('#orderreplenishdetail ').css('display' , 'none');	
			$('#orderreplenishremove ').css('display' , 'none');
		});
		
		});
		
		
		
/* MY ACCOUNT TEMPORARY CODE for HTML */ 		

	
jQuery(document).ready(function() {
var RecaptchaOptions = {
	theme : 'clean'
};
$('.changeContractContent ul li').click(function(){
	var contractId=$(this).attr('id');
	$(this).parent().parent().hide();
	$(this).parent().parent().parent().parent().find('#changecontractdisplay').show();
});

/*getPrice for Review Order */
$('.getPriceforentry').click(function(e){
	e.preventDefault(e);
	$(this).parent().parent().find('.defaultPrice').addClass('strike');
	$(this).parent().parent().find('.priceActive').show();	
	
});

/*HELp Page Tabs */

jQuery(window).bind('load' , function(e){
			if($('.myhelppage').size()>0)
			{$('.content').contents().unwrap();}
			/*jQuery('#hnewportal').accordion({
			collapsible: true,
			autoHeight: false	
	    	});*/
})





	jQuery('#helptabs ul.tabHeader li a').click(function(e){
		e.preventDefault();	
		jQuery('#helptabs ul.tabHeader li a').removeClass('Tabactive');
		jQuery('#helptabs .tabBody .rdiCont').hide();		
		var currTabLi = jQuery(this).parent().attr('class');
		jQuery(this).addClass('Tabactive');
		jQuery('#'+currTabLi).show();
		jQuery('#'+currTabLi).accordion({
			collapsible: true	
	    	});
		//alert(currTabLi);
		return false;
		
	});
	
 var previous;
 $(".secretQuestionJquery select").focus(function() {
        // Store the current value on focus and on change
         previous = this.value;
    }).change(function() {
        // Do something with the previous value after the change        
        $(".secretQuestionJquery select").not(this).find("option[value="+ previous + "]").show().prop("disabled", false);
        $(".secretQuestionJquery  select").not(this).find("option[value="+ $(this).val() + "]").hide().prop("disabled", true);
        // Make sure the previous value is updated
        previous = this.value;
    });
	
 var option;
	 $(".secretQuestionJquery select").each(function() {
    // Store the pre selected value on load
		 option = $(this).find(":selected").val();
        $(".secretQuestionJquery select").not(this).find("option[value="+ option + "]").show().prop("disabled", false);
        $(".secretQuestionJquery  select").not(this).find("option[value="+ $(this).val() + "]").hide().prop("disabled", true);
        // Make sure the option value is updated
         option = this.value;
    });
	
	
	$(".taxinfoWrapper .expandicon").bind('click' , function(e){
		e.stopPropagation();
		$(".taxinfoHide").slideToggle(200);
		jQuery(this).toggleClass("collapse");
	});
	
	$("#upldfile").bind('change' , function(){
		//jQuery('.browsetxtWrapper p').hide();
		//jQuery('.MultiFile-list').css({ 'background' : '#fff'});
		//jQuery('.browsetxtWrapper p').show();
		//jQuery('.MultiFile-list').css({ 'background' : 'none'});
		
	});	


/* Order Histroy Invoice expand and collapse functionality*/ 
$('.orderDetailInvoiceHead  .expandorderconfirmInVoice').click(function(e){
		e.preventDefault();
		$(this).hide();
		$(this).parents('.orderDetailInvoiceHead').next().show();

	});
$('.invoiceOpenWrapper .collapsepanelInVoice').click(function(e){
		e.preventDefault();		
		$(this).parents('.invoiceOpenWrapper').prev().find('.hcolumn5 a.expandorderconfirmInVoice').show();
		$(this).parents('.invoiceOpenWrapper').hide();
});	


});

$(function(){ 
var filecount=0;
 $('#upldfile').MultiFile({
	 	 afterFileAppend: function()
	  	{
	  		var fmovecount=++filecount;
			//alert(fmovecount);
			jQuery('.browsetxtWrapper p').hide();
			jQuery('.MultiFile-list').css({ 'background' : '#fff'});	

		},
		 afterFileRemove: function()
	  	{
	  		var fmovecount=--filecount;
			//alert(fmovecount);
			if(fmovecount==0)
			{
			jQuery('.browsetxtWrapper p').show();
			jQuery('.MultiFile-list').css({ 'background' : 'none'});	
			}

			
		}
	});

 	/********************
 	 * UPLOAD FILE HOME
 	 ********************/
	var filecounthome=0;
	$('#uploadmultifilehome').MultiFile({
			accept: "xls",
		 afterFileAppend: function()
		{
			 /*Changes for AAOL-5676*/
			$("#uploadmultifilehome").attr('disabled',true);
			$('.image-upload img').addClass('opaque').css('cursor','default');
			
			var fmovecount=++filecounthome;
			$("#uploadMessagesInstruction").hide();
			$(".uploadMessagesError").hide();
			$(".uploadMessagesSuccess").hide();
			$("#errorDetailsSpn").hide();
			$("#downLoadCartSpn").hide();
			$("#placeorderfile").show();
			$(".buttonContainer .MultiFile-list").show();
			$("#placeorderfileSpn").show();
		},
		afterFileRemove: function()
		{
		    var fmovecount=--filecounthome;
		    if(fmovecount==0)
		    {
		    	$("#uploadMessagesInstruction").show();
		    	$("#placeorderfile").hide();
		    	$(".buttonContainer .MultiFile-list").hide();
		    	$("#placeorderfileSpn").hide();
		    }                            
		},
	    STRING: {
	    	remove:'[x]',
	    	denied: UPLOAD_FILE_ERROR
		}
	}); 
});

/*--------------------Add Account-------------------------------- */
$(".accessToNewAccount").click(function() {

	if($(".accessToNewAccount").is(":checked")){
		$("#myAddAccountChoice").css("display", "none");
		$("#myAddAccountExisting").css("display", "block");
		$(".accessToNewAccount").prop("checked", true);
	}
});

$(".accessToAnotherAccount").click(function() {

	if($(".accessToAnotherAccount").is(":checked")){
		$("#myAddAccountChoice").css("display", "block");
		$("#myAddAccountExisting").css("display", "none");
		$(".accessToAnotherAccount").prop("checked", true);
	} 
});


/*--------------------End Add Account-------------------------------- */


// Toggle code
$(document).ready(function(){
	// Toggle plus, minus
   $('.showHideBlock').toggle(function(){
	   var nextCont = $(this).next();
	   $(this).children().text("+");
	   $(nextCont).slideUp();       
   },function(){
	   var nextCont = $(this).next();
       $(this).children().text("-");
       $(nextCont).slideDown();
   });
        
   // Shopping Cart Expand Collapse Code
   $('.iconExpandCollapse').toggle(function(){   
	   var nextCont = $(this).parent().next();
	   if($(this).attr('role') != 'undefined' && $(this).attr('role') === 'directionLeft')
			$(this).css('background-position', '0 0');
		else
			$(this).css('background-position', '0 -37px');	   
	   $(nextCont).slideUp();       
   },function(){
	   var nextCont = $(this).parent().next();
	   $(this).css('background-position', '0 -19px');
       $(nextCont).slideDown();
   });
   
   // Shopping cart
   $('input#purOrder').click(function(){
        $('.selectCard').hide();
   });
   
    $('input#creditCard').click(function(){
        $('.selectCard').show();
   });
   
   // Resource Information Accordion
  /* $("#resInfoAccordion").accordion();*/

   //check password strength
   $("#newPassword").keyup(function(){
		$("#newPassword").pstrength();
	});
   //end check password 
   
   // Cut Report- View Details
   $('.showViewDetails').click(function(e){
		var divShow = $(this).parent().parent().next();
		if($(this).parent().parent().next().hasClass('viewDetails'))
			$(divShow).toggle();	
   });
   
	// To open inline color box
	/*	$(".inline").colorbox({inline:true, width:"500px", height:"auto"});
		$(".creditCardPopup").colorbox({inline:true, width:"530px", height:"auto"});
		$(".productDetailsPopup").colorbox({inline:true, width:"1000px", height:"auto"});
		$(".selectAccountPopup").colorbox({inline:true, width:"530px", height:"auto"});
		$(".orderedProductReportPopup").colorbox({inline:true, width:"740px", height:"auto"});
		$(".disputeOrderPopup, .disputeItemPopup").colorbox({inline:true, width:"950px", height:"auto"});*/

		/*------------------Dispute Order: on check add required class------------------------*/

		$("#IncorrectPoNumberChk").click(function(){
			if(this.checked){
				$("#correctPoNumber").addClass("required");
			}
			else{
				$("#correctPoNumber").removeClass("required");
			}
		});

		$("#incorrectAddressChk").click(function(){
			if(this.checked){
				$("#country, #profileAddress1, #profileCity, #profileState, #postalCode").addClass("required");
			}
			else{
				$("#country, #profileAddress1, #profileCity, #profileState, #postalCode").removeClass("required");
			}
		});

		$("#disputeTaxChk").click(function(){
			if(this.checked){
				$("#exemptCertificate").addClass("required");
			}
			else{
				$("#exemptCertificate").removeClass("required");
			}
		});

		$("#pricingDisputeChk").click(function(){
			if(this.checked){
				$("#totalDisputed, #priceShouldBe").addClass("required");
			}
			else{
				$("#totalDisputed, #priceShouldBe").removeClass("required");
			}
		});

		$("#shortShippedChk").click(function(){
			if(this.checked){
				$("#productCode, #quantityOrdered, #quantityReceived").addClass("required");
			}
			else{
				$("#productCode, #quantityOrdered, #quantityReceived").removeClass("required");
			}
		});

		$("#overShipmentChk1").click(function(){
			if(this.checked){
				$("#productCode1, #quantityOrdered1, #quantityReceived1, #PoNumber").addClass("required");
			}
			else{
				$("#productCode1, #quantityOrdered1, #quantityReceived1, #PoNumber").removeClass("required");
			}
		});

		/*------------------End Dispute Order: on check add required class------------------------*/

		//select all account
		$("#selectAllAccount").click(function(event) { 
		    if(this.checked) {
		        // Iterate each checkbox
		        $(".accountSelect:checkbox").each(function() {
		            this.checked = true;                        
		        });
		    }
		    else{

		    	$(".accountSelect:checkbox").each(function() {
		            this.checked = false;                        
		        });

		    }
		});
		// unselect all if any one get unchecked
		$(".accountSelect").click(function(event){
			if(!this.checked) 
				$("#selectAllAccount").attr("checked", false);
        });

        


		/*------------------Survey popups ---------------------------*/
		if($('#updatedPrivacyPolicy').length){
			$.colorbox({
				html: $('#updatedPrivacyPolicy').html(),
				height: '190px',
				width: '565px',
				overlayClose: false
			});
			
			$('.closePopup').click(function(){					
				$.colorbox.close();
			});
		};
		/*------------------ End Survey popups ---------------------------*/

		/*-----------------------On check grey background----------------------------*/
		$(".selprod").click(function(event) { 
		    if(this.checked) {
		        $(this).parent().parent().css("background","#f9f9f9");
		    }
		    else{
		    	$(this).parent().parent().css("background","none");
		    }
		});
		/*------------------------End On check grey background-----------------------*/

		/*------------------------Setting up date calculations -----------------------*/  
		$(".backOrderReport #startDate").datepicker({
		        maxDate: -1,
		        minDate: "-2y",
		        inline: true,
		        onSelect: function(selected) {
		           $("#backOrderReportForm #endDate").datepicker("option","minDate", selected)
		      	}
          });

         

          $(".backOrderReport #endDate").datepicker({
                maxDate: 0,
                inline: true,
                onSelect: function(selected) {
             		$("#backOrderReportForm #startDate").datepicker("option","maxDate", selected)
           		}
          });

		/*------------------------End Setting up date calculations -----------------------*/

       /** START : Order History Date Settings **/
          $("#orderHistoryForm #fromdate").datepicker({
                          maxDate: 0,
                          minDate: "-2y",
                          inline: true,
                          onSelect: function(selected) {
                                  var fromDateInit = new Date(selected);
                                                  var dateToSet = new Date();
                                                  dateToSet.setMonth(fromDateInit.getMonth());
                                                  dateToSet.setFullYear(fromDateInit.getFullYear());
                                                  dateToSet.setDate(fromDateInit.getDate());
                             $("#orderHistoryForm #todate").datepicker("option","minDate", dateToSet);
                                  }
          });
          if($("#orderHistoryForm #fromdate").val()==""){
             $("#orderHistoryForm #fromdate").datepicker("setDate", "-10");
             $("#orderHistoryForm #todate").datepicker({
                                  maxDate: 0,
                                  minDate: "-10d",
                                  inline: true,
                                  onSelect: function(selected) {
                                                  var fromDateInit = new Date(selected);
                                                                  var dateToSet = new Date();
                                                                  dateToSet.setMonth(fromDateInit.getMonth());
                                                                  dateToSet.setFullYear(fromDateInit.getFullYear());
                                                                  dateToSet.setDate(fromDateInit.getDate());
                                                  $("#orderHistoryForm #fromdate").datepicker("option","maxDate", dateToSet);
                                  }
                          });
          } else {
             $("#orderHistoryForm #fromdate").datepicker("setDate", new Date($("#orderHistoryForm #fromdate").val()));
             var fromDate = new Date($("#orderHistoryForm #fromdate").val());
             var dateToSet = new Date();
             dateToSet.setMonth(fromDate.getMonth());
             dateToSet.setFullYear(fromDate.getFullYear());
             dateToSet.setDate(fromDate.getDate());
             $("#orderHistoryForm #todate").datepicker({
                                  maxDate: 0,
                                  minDate: dateToSet,
                                  inline: true,
                                  onSelect: function(selected) {
                                                  var fromDateInit = new Date(selected);
                                                                  var dateToSet = new Date();
                                                                  dateToSet.setMonth(fromDateInit.getMonth());
                                                                  dateToSet.setFullYear(fromDateInit.getFullYear());
                                                                  dateToSet.setDate(fromDateInit.getDate());
                                                  $("#orderHistoryForm #fromdate").datepicker("option","maxDate", dateToSet);
                                  }
                          });
          }
          if($("#orderHistoryForm #todate").val()==""){
                  $("#orderHistoryForm #todate").datepicker("setDate", new Date());
          } else {
                  $("#orderHistoryForm #todate").datepicker("setDate", new Date($("#orderHistoryForm #todate").val()));
          }
          /** END : Order History Date Settings **/
  
          
		/*-------------weekend disabled for house order page------------------*/

		//var d = new Date();
		//var curr_date = d.getDate();
		//var curr_month = d.getMonth();
        //curr_month++;
		//var curr_year = d.getFullYear();

		//$("#distPurOrderDate").val(curr_month + "/" + curr_date + "/" + curr_year);

		/*$("#distPurOrderDate").datepicker({
		       minDate: "2d",
		       inline: true,
		       defaultDate: new Date(),
		       beforeShowDay: $.datepicker.noWeekends
		});*/

		/*-------------end weekend disabled for house order page------------------*/

		/*---------------------disallow multi select security questions on my profile change security question-------------------------------------*/
		$(".secretQuestion").on("change", function(){
	        $("select[id^='profsecq']:not("+'#'+$(this).attr("id")+") option[value="+$(this).val()+"]").remove();
	    }); 
		/*---------------------End disallow multi select security questions-------------------------------------*/

		/*----------US Phone number format------------*/
		$("#profileFaxNumber, #profileMobileNumber, #profilePhoneNumber, #profileSupervisorPhone").blur(function() {
			if($(this).val() != ""){
		   		var phoneVal = $(this).val().split("-").join("");
				phoneVal = phoneVal.match(new RegExp(".{1,4}$|.{1,3}", "g")).join("-");
				$(this).val(phoneVal);
			}
		});
		
		$('.removeSplChar').click(function(e){
			var phoneNumber=$(this).val().replace(/-/g,'');
			$(this).val(phoneNumber);
		});
		/*----------End US Phone number format------------*/
		// Shopping cart Third party div show/hide
		$('#thirdParty').click(function(){
			if( $(this).is(':checked'))
				$('.thirdPartyHide').show();
			else
				$('.thirdPartyHide').hide();
		});


		$(".accessRadio").click(function(){

			var radioSelected = $(this).attr("id");

			switch(radioSelected){

				case "radio1":

					$("#addAccount").removeClass("linkDisable");
					$("#wwidDiv").hide();
					$("#moreDiv").hide();

				break;

				case "radio2":

					$("#addAccount").addClass("linkDisable");
					$("#wwidDiv").show();
					$("#moreDiv").hide();

				break;

				case "radio3":

					$("#addAccount").addClass("linkDisable");
					$("#wwidDiv").hide();
					$("#moreDiv").show();

				break;

			}

			
		});

		$('#addDiv').click(function(){
			
			var franRow = $('#moreDiv div:first-child').clone();
			var franRow = franRow.append('<button type="button" class="usm-access-add-btn btn btnclsactive del clrDisable">'+DELETE+'</button>');
				$('#moreDiv').append(franRow);
		});

		$(document).on('click', '.del', function(){
			$(this).parent().remove();
		});
		

		$(".lightBoxClose, .lightboxBlackOverlay").click(function(){
			$(".lightboxContent").css("display", "none");
			$(".lightboxBlackOverlay").css("display", "none");
		});
		/*end light box for credit card*/
				/*close button functionlity for enter password lightbox on change security page.*/
		$("#changeQuestionLightBox .close").on("click", function(){
			window.location.href = ACC.config.contextPath + "/my-account/personalInformation"; 
		});
		
		$("#sortbyOrdertemplate").on("change", function(){
			$("#templates").submit();
		});
		// Expand Order details on checkout
		$(".expendOrderDetailArrow").on("click", function(){
			var str = $(this).parent().parent().attr("id"),
				res = str.split("-"),
				identifier = res[1];
			if($("#orderEntryDetail"+identifier).is(":visible")) {
				$("#orderEntryDetail"+identifier).slideUp();
			} else {
				$("#orderEntryDetail"+identifier).slideDown();
			}
			toggleImage.call(this);
			function toggleImage(){
				$(this).hasClass("rightArrow")?$(this).removeClass("rightArrow").addClass("downArrow") : $(this).removeClass("downArrow").addClass("rightArrow");
			}
		});
		$(".expendOrderDetailArrowHistory").on("click", function(){
			var detailsSection = $(this).parent().parent().parent().find(".orderEntryDetail");
			if(detailsSection.is(":visible")) {
				detailsSection.slideUp();
			} else {
				detailsSection.slideDown();
			}
			toggleImage.call(this);
			function toggleImage(){
				$(this).hasClass("rightArrow")?$(this).removeClass("rightArrow").addClass("downArrow") : $(this).removeClass("downArrow").addClass("rightArrow");
			}
		});
		// end Expand Order details on checkout
		var position;
                $(document).bind('cbox_open', function() {
                 position = $(document).scrollTop();
                 $("html, body").animate({
                  scrollTop: 0
                 }, "slow");
                 $('html').css({
                  // overflow: 'hidden'
                 });
                });
                $(document).bind('cbox_closed', function() {
                 $('html').css({
                  // overflow: 'auto'
                 });
                 // $("html,body").scrollTop(position);
                 $("html, body").animate({
                  scrollTop: position
                 }, "slow");
                });

                

});
//add a CSRF request token to POST ajax request if its not available
//$.ajaxPrefilter(function (options, originalOptions, jqXHR)
//{
//	// Modify options, control originalOptions, store jqXHR, etc
//	if (options.type === "post" || options.type === "POST")
//	{
//		var noData = (typeof options.data === "undefined");
//		if (noData || options.data.indexOf("CSRFToken") === -1)
//		{
//			options.data = (!noData ? options.data + "&" : "") + "CSRFToken=" + ACC.config.CSRFToken;
//		}
//	}
//});
$.ajaxPrefilter(function (options, originalOptions, jqXHR){
	// Modify options, control originalOptions, store jqXHR, etc
	if (options.type === "post" || options.type === "POST"){
		var noData = (typeof options.data === "undefined");
		var nullData = (options.data === null);
		var isFormData = (options.data instanceof FormData);
		if (!isFormData && !nullData){
            if (noData || options.data.indexOf("CSRFToken") === -1){
                options.data = (!noData ? options.data + "&" : "") + "CSRFToken=" + ACC.config.CSRFToken;
            }
		}
		//Changes for IE add to cart from Upload a file
//		else if (isFormData && !nullData && !options.data.has("CSRFToken")){
		else if (isFormData && !nullData && !options.data.hasOwnProperty("CSRFToken")){
			options.data.append("CSRFToken", ACC.config.CSRFToken);
		}
	}
});

function validateInputData() {
    $('.validationevent').bind('input', function() {
        var selectedTxt = this.selectionStart,
            splChr = /[^a-z0-9 .]/gi,
            inpVal = $(this).val();
        if (splChr.test(inpVal)) {
            $(this).val(inpVal.replace(splChr, ''));
            selectedTxt--;
        }
        this.setSelectionRange(selectedTxt, selectedTxt);
    });
}

function validateAddress() {
    $('.addressvalidation').bind('input', function() {
        var selectedTxt = this.selectionStart,
            splChr = /[^a-z0-9 .,#-]/gi,
            inpVal = $(this).val();
        if (splChr.test(inpVal)) {
            $(this).val(inpVal.replace(splChr, ''));
            selectedTxt--;
        }
        this.setSelectionRange(selectedTxt, selectedTxt);
    });
}

function validateIdType() {
    $('.idtypevalidation').bind('input', function() {
        var selectedTxt = this.selectionStart,
            splChr = /[^a-z0-9 .-]/gi,
            inpVal = $(this).val();
        if (splChr.test(inpVal)) {
            $(this).val(inpVal.replace(splChr, ''));
            selectedTxt--;
        }
        this.setSelectionRange(selectedTxt, selectedTxt);
    });
}

function validateProductCode() {
    $('.productcodevalidation').bind('input', function() {
        var selectedTxt = this.selectionStart,
            splChr = /[^a-z0-9 -]/gi,
            inpVal = $(this).val();
        if (splChr.test(inpVal)) {
            $(this).val(inpVal.replace(splChr, ''));
            selectedTxt--;
        }
        this.setSelectionRange(selectedTxt, selectedTxt);
    });
}

$(document).on('keypress paste', '.validationevent', function(e) {
    validateInputData();
});

$(document).on('keypress paste', '.addressvalidation', function(e) {
    validateAddress();
});

$(document).on('keypress paste', '.idtypevalidation', function(e) {
    validateIdType();
});

$(document).on('keypress paste', '.productcodevalidation', function(e) {
    validateProductCode();
});