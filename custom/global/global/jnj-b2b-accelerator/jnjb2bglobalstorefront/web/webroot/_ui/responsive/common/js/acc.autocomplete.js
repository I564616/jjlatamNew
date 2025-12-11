ACC.autocomplete = {

	bindAll: function()
	{
		this.bindSearchAutocomplete();
	},

	bindSearchAutocomplete: function()
	{
		$( "#global-search-txt" ).autocomplete({
			source: function( request, response ) {
             ACC.autocompleteUrl = ACC.config.contextPath +'/search/autocompleteSecure'; 
				$.getJSON(
						ACC.autocompleteUrl, 
						{
							term : $('#global-search-txt').val()
						},
						function(data) {
							//alert(data);
							response(data);
						}
					);
			},
			minLength: 3,
			open: function(event, ui) { $(".ui-menu").css("z-index", 10000); },
			close: function(event, ui) { $(".ui-menu").css("z-index", -1); },
			select: function(event, ui) {
				if(ui.item) {
					$('#global-search-txt').val(ui.item.value.trim());
				}
				document.forms['search_form'].submit();
			},
			autoFocus: false

		});
	}
	
};

$(document).ready(function() {
	ACC.autocomplete.bindAll();
});
