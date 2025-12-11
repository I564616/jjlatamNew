var ACC3 = {  };
ACC3.legalnoticepop = {

	bindAll: function()
	{
		this.bindlegalnoticepopupLink($('.legalnoticepopup_hn'));
	
	},

	bindlegalnoticepopupLink: function(link)
	{
		link.click(function()
		{
			$.get(ACC.config.contextPath + "/legalNotice").done(function(data) {
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
	ACC3.legalnoticepop.bindAll();
});*/// JavaScript Document