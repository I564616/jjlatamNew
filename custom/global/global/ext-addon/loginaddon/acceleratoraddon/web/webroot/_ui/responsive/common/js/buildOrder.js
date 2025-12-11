$(document).ready(function(){
	$(document).on('click', '.toggle-link', function(){ 
		var $this = $(this); 
		if($this.hasClass('panel-collapsed')) { 
			//$this.next().slideUp(); 
			$this.addClass('panel-collapse'); 
			$this.children().removeClass('glyphicon-minus').addClass('glyphicon-plus'); 
		} else { 
			//$this.next().slideUp(); 
			$this.removeClass('panel-collapse'); 
			$this.children().removeClass('glyphicon-plus').addClass('glyphicon-minus'); 
		} 
	});
});