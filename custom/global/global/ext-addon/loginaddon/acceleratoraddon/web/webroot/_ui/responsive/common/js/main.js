$(document).ready(function(){

	$('.sorting-table').DataTable({
		"aaSorting": [],"aoColumnDefs" : [ {'bSortable' : false,'aTargets' : [ 'no-sort'] } ],"language": {"emptyTable":"No data found."},'pagingType': "simple"
	});
	
	$("#datatab-desktop_info").detach().prependTo('#datatab-desktop_wrapper');
	$("#datatab-desktop_paginate").detach().prependTo('#datatab-desktop_wrapper');
	
	$("#datatab-mobile_info").detach().prependTo('#datatab-mobile_wrapper');
	$("#datatab-mobile_paginate").detach().prependTo('#datatab-mobile_wrapper');
	
	$(window).resize(function () {clearTimeout(this.id);this.id = setTimeout(mobileSize, 500);});
	function mobileSize() {
		sizes();
	}
	$(window).load(function() {
		sizes(); 
	});
	function sizes() {
		var width = $(window).width();
		if (width < 640) {

		}
		else if (width > 640 && width < 966) {

		}
		else if (width >= 1024) {
			menuNdbodyHeight();
		}
	}
	
	function menuNdbodyHeight(){

		var leftHeight=$('#usr-details-menu').innerHeight();
		var rightHeight=$('#jnj-header').innerHeight()+$('#jnj-body-content').innerHeight()+$('#jnj-footer').innerHeight();
		
		if(leftHeight<rightHeight){
			$('#usr-details-menu').innerHeight(rightHeight);
		}
		else{
			var bodyContentHeight=leftHeight-($('#jnj-header').innerHeight()+$('#jnj-footer').innerHeight());
			$('#jnj-body-content').innerHeight(bodyContentHeight);
		} 
	}
	
	//$('#jnj-menu').hide();
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
	
	$(document).on('click', '.toggle-link', function(e){
		var $this = $(this);
		if(!$this.hasClass('panel-collapsed')) {
			//$this.next().slideUp();
			$this.addClass('panel-collapsed');
			$this.find('i').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
		} else {
			//$this.next().slideUp();
			$this.removeClass('panel-collapsed');
			$this.find('i').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
		}
	});
	
	

	
	
});
