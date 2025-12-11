/**********No charge Order AAOL-3392*/
$(document).ready(function() {
	

	var selectedDropShip = '';
searchBtnClick();

function searchBtnClick(){
	
	

	var dropShipAnchrClicked=false;
	$('#select-accnt-error').hide();	
	$('.selectaccountpopupdrop .anchorcls').on('click',function(){
		
		dropShipAnchrClicked=true;
		$('#select-accnt-error').hide();
		$('.anchorcls').each(function(){
			$(this).removeClass('anchor-selected');
		});
		$(this).addClass('anchor-selected');
		changeDropShipAcntList();
	});
	
	
	$('#submitChangeDropShipAdd').click(function(){
		
		
		if(dropShipAnchrClicked){
			$('#dropShipAccountForm').ajaxSubmit({
				success : function(data)
				{	
					
					$('#dropShip').val(selectedDropShip);
					$('#dropShipDel').val(selectedDropShip);
					$('#pi-shipToaddress-drop').html(data);
					
				}
			});
		}
		else{
			$('#select-accnt-error').show();
		}
	});
	
	//AAOL-3678
	$(document).on("click",'.dropShipaccountListPopUp',function(e){		
		e.preventDefault();
		var headVal;
		headVal = $(this).find('.list-group-item-heading').text();
		$("#dropShip").val(headVal);		
	});
	
	//added for AAOL-4032
	$(document).on("click",'.clearDropShipToAccountVal',function(e){	
		$("#dropShip").val("");
	});
}
	
	changeDropShipAcntList();
	function changeDropShipAcntList(){
		
		$('.dropShipaccountListPopUp').each(function() {
			$(this).click(function() {
				selectedDropShip=$($(this).find("#dropShipAccount:input[type='hidden']")).val();
				
				/** Creating dummy form for submission for account change request **/
				var dropShipAccountForm = jQuery('<form>', {
					'action' : ACC.config.contextPath + '/cart/updateDropShipAccount',
					'id' : 'dropShipAccountForm',
					'method' : 'POST'
				}).append(jQuery('<input>', { //  Account UID input created
					'name' : 'dropShipAccount',
					'value' : $($(this).find("#dropShipAccount:input[type='hidden']")).val(),
					'type' : 'hidden'
				})).append(jQuery('<input>', { // Account GLN input created 
			    	'name' : 'CSRFToken',
			    	 'value' : ACC.config.CSRFToken,
			    	 'type' : 'hidden' 
				}));
				 $("#dropShipAccountForm").remove();
				
				$("#selectaccountpopup").append(dropShipAccountForm);
				
				/** Submitting the dummy form **/
				
			});
		});

	}
	function loadingCircleShow(option)
	{
		if(option=="show"){
			 $("#laodingcircle").show();
		}else if(option=="hide"){
			
		 $("#laodingcircle").hide();
		}
		
	}
		
});

/**********No charge Order AAOL-3392*/