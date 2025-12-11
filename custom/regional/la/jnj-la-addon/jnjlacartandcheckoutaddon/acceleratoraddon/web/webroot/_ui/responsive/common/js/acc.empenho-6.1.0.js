$("#uploadBrowseFile").css("width", "auto");
if ($("#isContractFlag").val() == "true") {
	$('.revieworder').prop("disabled", true);
}

$(function() {
	var filecount = 0;
	$('#uploadBrowseFile').MultiFile({
		list : '#empechoFilesList',
		afterFileAppend : function() {
			++filecount;
			$(".buttonContainer .MultiFile-list").show();
			$('.revieworder').prop("disabled", false);
		},
		afterFileRemove : function() {
			--filecount;
			if (filecount == 0) {
				$(".buttonContainer .MultiFile-list").hide();
				$('.revieworder').prop("disabled", true);
			}
		},
		STRING : {
			remove : '[x]'
		}
	});
});

window.onload=function(){

	if (document.URL.endsWith("validate") || document.URL.endsWith("paymentContinue"))
	 {
	   $(".dropshipment-label-value").css("display","none");
	 }
						
} 

$("#uploadBrowseFile").click(function() {
	$(".invalidFileError").hide();
});
$("#uploadBrowseFile").change(function() {

	var filenamedoc = $(this).val();

	if ($("#isContractFlag").val() == "true") {
		if (filenamedoc.length > 0) {
			$('.revieworder').prop("disabled", false);
		} else {
			$('.revieworder').prop("disabled", true);
		}
	}
    
	processFile();
	async function processFile() {
    try {
	    var input = document.getElementById("uploadBrowseFile");
	    const file = input.value; // Replace with the actual file
	    const resultFinals = await uploadAndScanFile(file);
	    if(resultFinals==false){
	    $('#malware-detail-popup').modal('show');
	    $("#empechoFilesList").empty();
		$('.revieworder').prop("disabled", true);
        }
    } catch (error) {
	console.error('Error processing file:', error);
    } 
    }
});


$(".revieworder").click(function() {

    var complementaryIn = $("#complementaryInfoOrder").val().trim();
	var filenamedoc = $("#uploadBrowseFile").val();


	    if ($("#isContractFlag").val() == "true") {
			if ((filenamedoc.length > 0) && (complementaryIn !=="")) {
			$('.revieworder').prop("disabled", false);
			$("#complementarymendatoryError").css("display","none");	
		    } else if((complementaryIn =="") && (complementaryIn.length == 0)) {
			$('.revieworder').prop("disabled", true);
			$("#complementarymendatoryError").css("display","block");	
		    } else{
			 $('.revieworder').prop("disabled", true);
			}
			
		}else if(($("#isContractFlag").val() == "false")){
	
        if(complementaryIn !==""){
			$('.revieworder').prop("disabled", false);
		    $("#complementarymendatoryError").css("display","none");
		}
		else {
			$('.revieworder').prop("disabled", true);
			$("#complementarymendatoryError").css("display","block");
		}
		}
});

$("#complementaryInfoOrder").change(function() {

    var complementaryIn = $(this).val().trim();
	
	    if ((complementaryIn.length >0) && (complementaryIn.length <=1500))  {
			$('.revieworder').prop("disabled", false);
			$("#complementarymendatoryError").css("display","none");
			$("#complementaryError").css("display","none");
		}
		else if(complementaryIn.length <1){
			$('.revieworder').prop("disabled", true);
				
			$("#complementaryError").css("display","none");
			$("#complementarymendatoryError").css("display","block");
			
		}
		else {
			$('.revieworder').prop("disabled", true);
			$("#complementaryError").css("display","block");
			$("#complementarymendatoryError").css("display","none");
				
	    }
});

$("#uploadBrowseFile1").css("width", "auto");

$(function() {
	var filecount = 0;
	$('#uploadBrowseFile1')
			.MultiFile(
					{

						list : '#empechoFilesList1',
						onFileSelect : function(e) {
							$(".invalidFileError1").hide();
							$("#fileTypeError").remove();
							var fileNameWithPath = e.value;
							var ext = fileNameWithPath.split('.').pop()
									.toLowerCase();
							var demo = $("#fileType").val();
							var arrDemo = demo.split(',');
							var flag = false;
							$.each(arrDemo, function(index, value) {
								if (ext == value) {
									flag = true;
								}

							});
							if (flag == false) {
								$('#uploadBrowseFile1').val('').attr('value',
										'')[0].value = '';
								$(".invalidFileError1")
										.show()
										.append(
												"<div id=\"fileTypeError\" style=\"display:inline-block;color:#B41601;font-weight:bold;font-size:14px;\">"
														+ demo + "</div>");
								return false;
							}
						},
						afterFileAppend : function() {

							++filecount;
							$(".buttonContainer .MultiFile-list").show();

						},
						afterFileRemove : function() {
							--filecount;
							if (filecount == 0) {
								$(".buttonContainer .MultiFile-list").hide();
							}
						},
						STRING : {
							remove : '[x]'
						}
					});
});