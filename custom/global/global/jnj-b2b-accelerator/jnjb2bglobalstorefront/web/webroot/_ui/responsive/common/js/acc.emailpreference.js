$(document).ready(function(){
	
	
	$("#shipdialy").on('change',function(){
		 
		   $('#shippedOrderEmail').val('daily');
		});
	$("#shipweekly").on('change',function(){
		  
		   $('#shippedOrderEmail').val('weekly');
		});
		
	$("#backdialy").on('change',function(){
		   
		   $('#backorderEmailType').val('daily');
		});
		
	$("#backweekly").on('change',function(){
		  
		   $('#backorderEmailType').val('weekly');
		});
	
	
	
	$("#invdialy").on('change',function(){
		  
		   $('#inoviceEmailPreference').val('daily');
		});
		
	$("#invweekly").on('change',function(){
		  
		   $('#inoviceEmailPreference').val('weekly');
		});
		
	$("#deldialy").on('change',function(){
		  
		   $('#deliveryNoteEmailPreference').val('daily');
		});
		
	$("#delweekly").on('change',function(){
		  
		   $('#deliveryNoteEmailPreference').val('weekly');
		});
		

		
});