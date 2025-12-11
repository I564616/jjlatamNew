$(document).ready(function(){
	
	$('#priceInquiry-nodata-error').hide();
	$('#get-price-btn').click(function(){
		$('.price-inquiry-table tbody tr:eq(0) td').each(function(){
			if($(this).hasClass('dataTables_empty')){
				$('#priceInquiry-nodata-error').show();
			}
		});
	});
	
	
$('#bill-to-address-popup').modal('show');
	$('#cookiepopupclose').click(function(){
		//$('#cookiepopup').modal('toggle');
		$('#cookiepopup').modal('hide');
		$('#cookiepopup').modal('show');
	});
	var width = $(window).width();
	$('#emeo-toggle-icon').click(function(){
		setTimeout(drawDivider,100);
	});
	
	$('#pi-head-line').css('height',$('#jnj-head-panel').innerHeight()+'px');
	
	function drawDivider(){
		if (width > 767) {

			if($('#pi-shipToaddress').innerHeight()>$('#pi-billToaddress').innerHeight()){
				//$('#pi-shipToaddress').css('border-right','1px solid rgb(242,242,242)');
				
				$('#pi-body-line').css('height',$('#pi-shipToaddress').innerHeight()+'px');
			}
			else{
				/* $('#pi-billToaddress').css('border-left','1px solid rgb(242,242,242)'); */
				$('#pi-body-line').css('height',$('#pi-billToaddress').innerHeight()+'px');
			}
			if($('.emeo-pane').length>0){
				
				var emeoPaneArray=[];
				$('.emeo-pane').each(function(i){
					emeoPaneArray.push($(this).innerHeight());
				});
				var emeoDividerHeight = Math.max.apply(Math,emeoPaneArray);				
					//alert('s'+emeoDividerHeight)
				$('.emeo-separator').each(function(){
					$(this).innerHeight(emeoDividerHeight+30);
				});
				
			}
			
		}	
	}
	
	
	
	//User management create user
	
	
	var noChargeLebel=$('#no-charge-label').html();
	$('#no-charge-label').remove();
	$(noChargeLebel).insertAfter($('#noCharge'));

	//AAOL-2422 changes
	var financialEnable=$('#financial-enable').html();
	$('#financial-enable').remove();
	$(financialEnable).insertAfter($('#financialEnable'));
	//end of AAOL-2422 changes
	
	$('.checkLabelHolder').each(function(i){
		var checkLabel=$(this).html();
		$(this).remove();
		$(checkLabel).insertAfter($('.checkBoxBtn:eq('+i+')'));
	});
			
	$('.usm-access-subdata').hide();
	
	if($('#access-radio-1').prop('checked')){
		$('.usm-access-subdata').hide();
	}
	else if ($('#access-radio-2').prop('checked')){
		$('.usm-access-subdata').hide();$('#access-content-2').show()
	}
	else{
		$('.usm-access-subdata').hide();$('#access-content-3').show();	
	}
	$('#access-radio-1').click(function(){$('.usm-access-subdata').hide();});
	
	$('#access-radio-2').click(function(){ $('.usm-access-subdata').hide();$('#access-content-2').show()});
	$('#access-radio-3').click(function(){ 
		$('.usm-access-subdata').hide();
		$('#access-content-3').show();	
	});
	//Edit user editUserSpecificTerr
	
	if($('#editUserSpecificTerr').val()){
		$('#access-radio-3').prop('checked',true);
		$('.usm-access-subdata').hide();
		$('#access-content-3').show();	
	}
	
	//Grant access row creation dynamically

	var grantAccRowHTML=$('.grant-access-row:eq(0)').html();
	$(document).on('click','.ga-add-btn',function(){
		$(this).addClass('hide-btn');
		$(this).next().removeClass('hide-btn');
			$('#access-content-3').append("<div class='grant-access-row'>"+grantAccRowHTML+"</div>");
		$('.selectpicker').selectpicker('refresh');
	});
	$(document).on('click','.ga-del-btn',function(){
		$(this).parents().closest('.grant-access-row').remove();
		if($('.grant-access-row').length==1){
			$('#access-content-3').css({'height':'auto','padding-right':'0px'})
		}
	});
	
	$(document).on('click','.ga-add-btn:eq(0)',function(){
		var accessContent3Height= $('.grant-access-row:eq(0)').height()
		$('#access-content-3').css({'height':accessContent3Height+20,'padding-right':'10px'})
	});


});	
