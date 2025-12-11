ACC.selloutreportpopup = {

	bindAll: function()
	{
		this.bindselloutreportpopupLink($('#uploadselloutreport'));
	},

	bindselloutreportpopupLink: function(link)
	{
		link.click(function()
		{
			//alert("sellout")
			$("#selloutpopup").modal("show");
			$.get(link.data('url')).done(function(data) {
				if(inactivityAjaxCheck(data)){
					$.colorbox({
						html: data,
						height: '290px',
						width: '448px',
						overlayClose: false,
						onComplete : function()
						{
							$.colorbox.resize();
							$("#fileInfo").hide();
							$("#firstFile").bind('checkChange', function(){
								if(this.val() && this.val() != this.data('val')){
									this.trigger('change'); // Trigger change event
								}
							});
							$("#firstFile").bind('change',function(){
								$("#fileInfo").text($(this).val().split(/\\/).pop()); // Get file name
								$("#firstblock").hide();  // Hide first set of buttons
								$("#fileInfo").css("display","block"); // Show file name information span
								$("#secondblock").show(); // Show second set of buttons
								$("#errorMessage").hide(); // Hide error message if present
								$.colorbox.resize();
							});
							/*$("#secondFile").bind('change',function(){
								$("#fileInfo").text($(this).val().split(/\\/).pop()); // Get file name
							});*/
							$('#submitbutton').bind('click' , function(){
								alert("hi");
								$('#uploadForm').submit(); // Submit form
							});
							$('#companyId').bind('change' , function(){
								$('#hddnCompany').val($('#companyId').val()); // Change the hidden value of Company
							});
							if(document.getElementById("uploadStatus").value == 'false' && document.getElementById("boxClosed").value =='false' && document.getElementById("sizeError").value =='false'){
								$("#errorMessage").css('display','block'); // Show error message
								$.colorbox.resize();
							}
							/** Below lines to set mouse-over / mouse-out colors to the fake buttons **/
							$("#firstFile").hover(function(){
								$("#firstFakeButton").css("background-color", "#067B96");
							});
							$("#firstFile").mouseout(function(){
								$("#firstFakeButton").css("background-color","");
							});
							/*$("#secondFile").hover(function(){
								$("#secondFakeButton").css("background-color", "#067B96");
							});*/
							/*$("#secondFile").mouseout(function(){
								$("#secondFakeButton").css("background-color","");
							});*/
						}
					});
				}
			});
		});
	}
};

$(document).ready(function()
{
	if($("#file_name").html()!==""){
		$(".submitUploadButton").removeClass("hidden")
		
	} 
	ACC.selloutreportpopup.bindAll();
	
	$('.sortFormChange').bind('change' , function(){
		$('#sortType').val($('#sortby').val());
		$("#sortForm").attr("action", ACC.config.contextPath +"/my-account/sellout/sort");
		$("#sortForm").submit();		
	});		
		
	$('#cboxClose').bind('click' , function(){	
		$("#boxClosed").val('true');
	});	
		
	$("#sellOutPdfDownload").bind('click',function(){
		var originalAction = $("#sortForm").attr("action");
		$("#sortForm").attr("action", ACC.config.contextPath +"/my-account/sellout/downloadData?downloadType=PDF");
		$("#sortForm").attr("method","POST");
		$("#sortForm").submit();
		$("#sortForm").attr("action", originalAction);
		$("#sortForm").attr("method","POST");
	});
	
	/**
	 * showMore for Sellout report
	 */
	$('.showingFormChange').bind('change' , function(){
		$("#sortForm").attr("action", ACC.config.contextPath +"/my-account/sellout/showMore");
		$("#sortForm").submit();
	});
	
	$("#browse_file").click(function(){
	    $("#firstFile").click();	    
	});
	
	$('#companyId').bind('change' , function(){
		$('#hddnCompany').val($('#companyId').val()); // Change the hidden value of Company
	});
	
	$("#firstFile").bind('change',function(){
		$("#file_name").html($("#firstFile").val());
		$("#submitbutton").removeClass("hidden");
		$('#browse_file').removeClass("btnAlign")
	});	
	
	$('#sortby').bind('change' , function(){
		$('#sortType').val($('#sortby').val());
		$("#sortForm").submit();
	});
	
	$('#filterBy').bind('change' , function(){
		$('#filterType').val($('#filterBy').val());
		$("#filterForm").submit();		
	});
	
	var uploadFileSortbyWidth = $('#uploadFileSortby').width();
	var uploadFileFilterbyWidth = $('#uploadFileFilterby').width();
	if(uploadFileSortbyWidth > uploadFileFilterbyWidth){
	 // Set the with of both components to uploadFileSortbyWidth's width 
		$('#uploadFileSortby').width(uploadFileSortbyWidth);
		$('#uploadFileFilterby').width(uploadFileSortbyWidth);	
	}else{
	 // Set the with of both components to uploadFileFilterbyWidth width 
		$('#uploadFileSortby').width(uploadFileFilterbyWidth);
		$('#uploadFileFilterby').width(uploadFileFilterbyWidth);
	}	
	
});// JavaScript Document

function checkError(){
	document.getElementById("sortby").value=document.getElementById("sortFlag").value;
	var status = document.getElementById("uploadStatus").value;
	if(status=='false'){
		$("#uploadselloutreport").click();
		$.colorbox.resize();
	}
	
	document.getElementById("filterBy").value=document.getElementById("filterFlag").value;
	var status = document.getElementById("uploadStatus").value;
	if(status=='false'){
		$("#uploadselloutreport").click();
		$.colorbox.resize();
	}
	
}

function restrictFilesOnlyForSellOut() {
	//showModalSpinner();
	var data;
	var filename;
	/*if ($("#secondFile").val() != "") {
		filename = $("#secondFile").val();
	} else {
		filename = $("#firstFile").val();
	}*/
	filename = $("#firstFile").val();
	/* 35069 Sellout report must except all type of files start */
	if (document.getElementById("companyId").value == "medical") {
		for (i = 0; i < arguments.length; i++) {
			if (filename.substring(filename.length - arguments[i].length,
					filename.length) == arguments[i]) {
				data = "<div class='lightboxtemplate lightboxwidthsize1 sam' id='sellout_popup'><h2>NOTE</h2><div class='lightboxbody'><div class='section1report marTop20'><span class='marTop20'>Your file has been sent successfully!</span></div></div></div></div>";
				document.getElementById("uploadForm").submit();
				return true;
			}
		}
	} else {
		data = "<div class='lightboxtemplate lightboxwidthsize1 sam' id='sellout_popup'><h2>NOTE</h2><div class='lightboxbody'><div class='section1report marTop20'><span class='marTop20'>Your file has been sent successfully!</span></div></div></div></div>";
		document.getElementById("uploadForm").submit();
		return true;
	}
	/* 35069 Sellout report must except all type of files end */

	// data = "<div class='lightboxtemplate lightboxwidthsize1 sam'
	// id='sellout_popup'><h2>ERROR</h2><div class='lightboxbody'><div
	// class='registerError'><label class='error'>Sorry, occurred an error and
	// your file cannot be added. Please try again or choose another
	// file.</label></div></div></div></div>";
	// showReportUploadPopUp(data);
	$("#firstFile").attr("disabled", true);
	jQuery('#errorMessage').show();
	jQuery('#fileInfo').hide();
	//hideModalSpinner();
	return false;
}

$("#uploadOrderPdfDownload").bind('click',function(){
	
	var originalAction = $("#sortForm").attr("action");
	$("#sortForm").attr("action", ACC.config.contextPath +"/my-account/uploadorders/downloadData?downloadType=PDF");
	$("#sortForm").attr("method","POST");
	$("#sortForm").submit();
	$("#sortForm").attr("action", originalAction);
	$("#sortForm").attr("method","POST");
});



function inactivityAjaxCheck(data) {
	if (null != data || data === '') {
		/* console.log($.type(data)); */
		if ($.type(data) === 'string') {
			/* console.log("String Response."); */
			if (data.indexOf('loginForm') >= 0) {
				/* console.log("Session Inactive"); */
				window.location.href = ACC.config.contextPath + "/login";
			} else {
				/* console.log("Session Active"); */
				return true;
			}
		} else {
			/*
			 * console.log("Not String Response."); console.log("Session
			 * Active");
			 */
			return true;
		}
	} else {
		/*
		 * console.log("No Response Revieced."); console.log("Session Active");
		 */
		return true;
	}
	
	

}


$('#uploadOrderForm').bind('change' , function(){
	
	$("#uploadOrderForm").submit();
});	

// malware virus scan
$("#FileUpload").change(function(e) {
 
	processFile();
	async function processFile() {
try {
	var input = document.getElementById("firstFile");
	const file = input.value; // Replace with the actual file
	const resultFinals = await uploadAndScanFile(file);
	if(resultFinals==false){
	$('#malware-detail-popup').modal('show');
	$("#file_name").empty();
	$(".submitUploadButton").addClass("hidden")
}
} catch (error) {
	console.error('Error processing file:', error);
}
}
 
});

$(document).ready(function()
		{
	$('.trackid').click(function(e)
			{
		var valToCompare=$(this).find('a').attr('id');
			
		
		jQuery.ajax({ 
			url : ACC.config.contextPath + '/p/la/comparePOTrackingId?valToCompare='+$("#"+valToCompare).text(),
			async: false,
				success : function(data) {

					if(data==true)
						{
						window.location.href = ACC.config.contextPath + '/order-history';
						}
					else
						{
						$('#trackidpopup').show().modal();
						
						}
					
				}
		});
		
			});
			
	
	
		});



