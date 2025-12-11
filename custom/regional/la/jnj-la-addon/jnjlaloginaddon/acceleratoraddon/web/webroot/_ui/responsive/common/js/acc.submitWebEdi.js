var filecounthome = 0;
$(function(){
	$('#uploadEdiFile').MultiFile({
		list: '#uploadFilesList',
		afterFileAppend : function(data) {
			var fmovecount = ++filecounthome;
			$('.webEdiFileMessage').hide();
			$('#uploadbtnWrapper').show();
			updateAndShowFilesQuantity();
			
			$(".buttonContainer .MultiFile-list").show();
			var totalDivSize = $("#uploadEdiDiv").height() + $("#uploadbtnWrapper").height();
			$(".borderforplaceorder").height(totalDivSize);
		},
		afterFileRemove : function() {
			updateAndShowFilesQuantity();
			var fmovecount = --filecounthome;
			if (fmovecount == 0) {
				$('.browsetxtWrapperOnHome').show();
				$('.MultiFile-list').css({
					'background' : 'none'
				});
				$('#uploadbtnWrapper').hide();
				$('#ediFilesAttached').hide();
				location.reload();
			}
		},
		STRING : {
			remove : '[x]'
		}
	});
	
	function updateAndShowFilesQuantity(){
		var size = $('#placeOrderEdiForm input[type=file]').length-1;
		var ediFileCount = 0; 
		for(var i = 0; i < size; i ++){
			ediFileCount += $('#placeOrderEdiForm input[type=file]')[i].files.length;
		}
		
		$('#totalFilesAttached').text(ediFileCount);
		$('#ediFilesAttached').show();
	}
});

$("#ediPlaceOrderfile").click(function(e) {
	loadingCircleShow("show");
	$('#placeOrderEdiForm').ajaxSubmit({
		success: function(data){
			loadingCircleShow("hide");
			var constainsError = false;
			var constainsReceived = false;
			var successMessageContent = $('#textHomePageWebEdiUploadSuccess').val();
			successMessageContent += "<div id='successMessageContent' ><ul class='list-group ediResultListContent'>";
			$.each(data, function(fileName, fileStatus){
				 
				if(fileStatus == 'Received'){
					successMessageContent += "<li>";
					constainsReceived = true;
					successMessageContent += "<span class='list-group-item list-group-item-success glyphicon glyphicon-ok ediListIcon'> " + fileName + "</span>";
					successMessageContent += "</li>";
				}else{
					constainsError = true;
				}
			});
			successMessageContent += "</ul></div>";
			if(!constainsReceived){
				successMessageContent = "";
			}
			// Found duplicated file(s)
			var errorMessageContent = "";
			if(constainsError){
				errorMessageContent = $('#textHomePageWebEdiUploadDuplicated').val();
				errorMessageContent += "<div id='errorMessageContent' ><ul class='list-group ediResultListContent'>";
				$.each(data, function(fileName, fileStatus){
					if(fileStatus == 'Error'){ // File is duplicated
						errorMessageContent += "<li>";
						errorMessageContent += "<span class='list-group-item list-group-item-danger glyphicon glyphicon-remove ediListIcon'> " + fileName + "</span>"; 
						errorMessageContent += "</li>";
					}
				});
				errorMessageContent += "</ul></div>";
			}
			$("#ediResultMessageContent").html(successMessageContent + errorMessageContent);
				
			$("#ediOrderFileUploadDetailPopup").modal("show");
		}
	});
});


$("#ediResultMessageClose").click(function(){
	$("#uploadEdiFile").MultiFile("reset");
});

$("#uploadEdiFile").change(function(e) {
 
	processFile();
	async function processFile() {
try {
	var input = document.getElementById("uploadEdiFile");
	const file = input.value; // Replace with the actual file
	const resultFinals = await uploadAndScanFile(file);
	if(resultFinals==false){
	$('#malware-detail-popup').modal('show');
	$("#uploadFilesList").empty();
}
} catch (error) {
	console.error('Error processing file:', error);
}
}
 
});