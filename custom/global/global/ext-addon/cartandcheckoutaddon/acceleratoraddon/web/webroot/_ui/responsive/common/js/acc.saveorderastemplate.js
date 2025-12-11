
var ACC2 = {  };
ACC2.saveorderpopup = {

	bindAll: function()
	{
		this.bindsaveorderpopupLink($('.saveorderastemplate'));
	},


	bindsaveorderpopupLink: function(link)
	{
		link.click(function()
		{
			$.get(link.data('url')).done(function(data) {
				$.colorbox({
					html: "<div class='lightboxtemplate lightboxwidthsize1 uploadorderfiletemp'><h2>"+UPLOAD_FILE+"</h2><div class='lightboxbody'><div class='error'><p>"+ORDER_TEMPLATE+"</p></div><h4>"+ERROR_DETAILS+"</h4><p>"+FILE_NAME+" <br /> "+ERROR_ERROR+"</p></div></div>",
					height: '240px',
					width: '448px',
					overlayClose: false

				});
			});
		});
	}
};

$(document).ready(function()
{
	//ACC2.saveorderpopup.bindAll();
});// JavaScript Document