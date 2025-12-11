//acc.contractCommon page
 

ACC.contractCommon = {
	bindAll: function()	{
		ACC.contractCommon.registerEvent();
	},
	/*
	 * Registering Event to select all check box for desktop or mobile
	 */
	registerEvent : function(){
		 
		
	},
	downloadFile: function(originalAction,url,type,count,form) {
		$(form).attr("action", ACC.config.contextPath + url); //"/my-account/contract/downloadData?downloadType=PDF");
		$(form).attr("method","POST");
		$("#loadNoOfRecordsHidden").val(count);
		$(form).submit();
		$(form).attr("action", originalAction);
		$(form).attr("method","POST");
	}
};

$(document).ready(function() {
	ACC.contractCommon.bindAll();
});

