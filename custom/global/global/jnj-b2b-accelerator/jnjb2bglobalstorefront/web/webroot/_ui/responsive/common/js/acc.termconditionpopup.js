var ACC4 = {  };
ACC4.termconditionpop = {

	bindAll: function()
	{
		this.bindtermconditionpopupLink($('.termconditionpopup_hn'));
	
	},

	bindtermconditionpopupLink: function(link)
	{
		link.click(function()
				{
			$.get(ACC.config.contextPath + "/termsAndConditions").done(function(data) {
				$.colorbox({
				html:data,
					height: '510px',
					width: '600px',
					overlayClose: false,
					onComplete: function(){
						$("#closePop").click(function(){
							$.colorbox.close();
						});
					}

				});
			});
		});
	}
};

/*$(document).ready(function()
{
	ACC4.termconditionpop.bindAll();
});*/// JavaScript Document