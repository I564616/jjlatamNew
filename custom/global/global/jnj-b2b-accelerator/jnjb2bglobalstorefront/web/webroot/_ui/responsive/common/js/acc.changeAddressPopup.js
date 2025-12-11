var ACC10 = {  };
ACC10.changeAddressPopup = {

bindAll: function()
{
	this.bindChangeAddressLightBoxLink($('.changeAddressLightBoxLink'));
},

bindChangeAddressLightBoxLink: function(link)
{
	link.click(function()
	{
		var data=$('#changeAddress').html();
		$.colorbox({
			html: data,
			height: 'auto',
			width: '700',
			overlayClose: false,

			onComplete: function(){
				$('.closePopup').click(function(){					
					$.colorbox.close();
				});	
				$("#changeAddForm .column2 > label").each(function(){
					if($.trim($(this).html()).indexOf(",")==0){
						$(this).html($.trim($(this).html()).substr(1,$.trim($(this)).length));
					}
					if($.trim($(this).html()).indexOf(",")==0){
						$(this).html($.trim($(this).html()).substr(1,$.trim($(this)).length));
					}
					if($.trim($(this).html()).indexOf(",")==$.trim($(this).html()).length){
						$(this).html($.trim($(this).html()).substr(0,$.trim($(this)).length)-1);
					}
				});
				$('#submitChangeAddress').click(function () {
					$('#changeAddForm').ajaxSubmit({
						success : function(data)
						{
							$('#deliveryAddressTag').html(data);
							$.colorbox.close();
						}
					});

				});
				
			}

		});
	});
}
};
/**********No charge Order AAOL-3392*/

ACC10.changeDropShipAddress = {

		bindAll: function()
		{
			this.changeDropShipAddress($('.changeShipAddLighboxLink'));
		},

		changeDropShipAddress: function(link)
		{
			link.click(function()
			{
				$('#errorMsgDiv').html(""); 
				
				loadingCircleShow("show");
				$.get(link.data('url')).done(function(data){
					
					loadingCircleShow("hide");
					$('#dropship-account-popup').remove();
					$("#dropShipAccountholder").append(data);
					$('#dropship-account-popup').modal('show');
				/*	alert(data);
					$('#dropship-account-popup').remove();
					$("#dropShipAccountholder").append(data);
					$('#dropship-account-popup').modal('show');*/
					/*$('#dropship-account-popup').remove();
					$("#dropShipAccountholder").html(data);
					location.reload();
					$('#dropship-account-popup').append(data);*/
					/*	
					$('#dropShipAccountholder').html(data);
					$('#dropship-account-popup').modal('show');
					
					*/
					/*$('#dropship-account-popup').remove();	
					$("#dropShipAccountholder").html(data);*/
					// window.location.reload(true);
				});
			});//link.click close
		}
		};
/**********No charge Order AAOL-3392*/
$(document).ready(function()
{
	ACC10.changeAddressPopup.bindAll();
	ACC10.changeDropShipAddress.bindAll();
	
	function loadingCircleShow(option)
	{
		if(option=="show"){
			 $("#laodingcircle").show();
		}else if(option=="hide"){
			
		 $("#laodingcircle").hide();
		}
		
	}
	
	
});