ACC.addToCartHome.submitPlaceorderfile = function(e) {
	loadingCircleShow("show");
    var data = new FormData();
	var IDs = $("#uploadDiv .MultiFile-wrap input")
		.map(function() {
			return this.id;
		})
		.get();

    var addedFiles = [];
	$.each(IDs, function(index, value) {
		$.each($('#' + value)[0].files, function(i, file) {
		    if ($.inArray(file.name, addedFiles) == -1) {
		        addedFiles.push(file.name);
                data.append('uploadmultifilehome', file);
		    }
		});
	});
    
	jQuery.ajax({
		url: ACC.config.contextPath + '/home/homepageFileUpload',
		data: data,
		cache: false,
		contentType: false,
		processData: false,
		accept: "text/html",
		type: "POST",
		async: false,
		success: function(data) {
			loadingCircleShow("hide");
			data = JSON.parse(data);
			for (let key in data){
            let value=data[key];
            let invalidtextkey=`${key}`
            let invalidtextValue=`${value}`
			if(invalidtextkey==="maxCount"){
				
	         $('#limit-detail-popup').modal('show');
			 
			 const itemcountdetails= document.getElementById('warningdetails')
			 const linescountdetails= document.getElementById('countdetails')
			 itemcountdetails.textContent=invalidtextValue;
			 linescountdetails.textContent=invalidtextValue;
			 return;
			}else{
		    $('#limit-detail-popup').modal('hide')
		    }
			}
            
			
			$(".uploadfilehome").MultiFile("reset");
			$(".buttonContainer .MultiFile-list").hide();
			$("#uploadMessagesInstruction").hide();
			$("#placeorderfileSpn").hide();
			$('#emptyPlaceOrderFileSpn').hide();
			$("#someProductsAdded").hide();
			var responseDataVal = undefined;
			var count = 0;
			var totalCartCount = 0;
			var partiallyAdded = false;

			for (var key in data) {
				count = count + 1;
			}

			$.each(data, function(key, field) {
				if (field === 'SUCCESS') {
					responseDataVal = field.toLowerCase();
				}
				if (key === 'totalCartCount') {
					totalCartCount = field;
				}
				if (key === 'someProductsAdded') {
					$("#someProductsAdded").show();
					partiallyAdded = true;
				}
				
			});

			if ("empty" in data && count == 1) {
				$(".uploadMessagesEmpty").show();
				$('#emptyPlaceOrderFileSpn').show();
				loadingCircleShow("hide");
				return false;
			} else if (responseDataVal !== undefined && responseDataVal === 'success' && !partiallyAdded) {
				window.location.href = ACC.config.contextPath + '/cart';
				loadingCircleShow("hide");
			} else {
				$("#uploadMessagesInstruction").hide();
				$(".uploadMessagesError").show();
				$(".uploadMessagesEmpty").hide();
				$('#emptyPlaceOrderFileSpn').hide();
				$('#items-msg').text(totalCartCount);

				if ("success" in data) {
					window.location.href = ACC.config.contextPath + '/cart';
					loadingCircleShow("hide");
				}

				var markup = "<div>"
				markup += "<ul>";
				$.each(data, function(key, field) {
					if (key.toLowerCase() != "success" && key != "showMinQtyMsg" && key != "totalCartCount" && key != "someProductsAdded" && field.toLowerCase() != "success") {
						markup += "<li><b>";
						markup += field;
						markup += "</b></li>";
					}
				});

				markup += "</ul></div>";

				$.each(data, function(key, field) {
					if (field === 'SUCCESS') {
						responseDataVal = field.toLowerCase();
					}
					if (key === 'totalCartCount') {
						totalCartCount = field;
					}
				});

				$("#uploadErrorMsg").html(markup);
				$("#error-detail-popup").modal("show");
				$("#errorDetailsSpn").show().click(function() {
					$("#error-detail-popup").modal("show");
				});
			}
		},
		error: function(data) {
			loadingCircleShow("hide");
		}
	});
};

// MULTIFILE CONFIGURATION
var fileCountHome=0;
var fileSelectorClickSet = false;
$('#uploadmultifilehome').MultiFile({
        accept: "xls, xlsx",
     afterFileAppend: function()
    {
        var fMoveCount=++fileCountHome;
        fileSelectorClickSet = false;
        $("#uploadMessagesInstruction").hide();
        $(".uploadMessagesError").hide();
        $(".uploadMessagesSuccess").hide();
        $("#errorDetailsSpn").hide();
        $("#downLoadCartSpn").hide();
        $("#placeorderfile").show();
        $(".buttonContainer .MultiFile-list").show();
        $("#placeorderfileSpn").show();
    },
    afterFileRemove: function()
    {
        var fMoveCount=--fileCountHome;
        fileSelectorClickSet = false;
        if(fMoveCount==0)
        {
            $("#uploadMessagesInstruction").show();
            $("#placeorderfile").hide();
            $(".buttonContainer .MultiFile-list").hide();
            $("#placeorderfileSpn").hide();
        }
    },
    STRING: {
        remove:'[x]',
        denied: $("#homeUploadFileWrongFormat").val(),
        duplicate: $("#homeUploadFileDuplicatedFile").val()
    }
});

// TO CORRECT MULTI UPLOAD COMPONENT
$('#uploadFileFormHome').on('mouseover', '#uploadDiv',( function() {
    if (!fileSelectorClickSet){
        var IDs = $("#uploadDiv .MultiFile-wrap input")
            .map(function() {
                return this.id;
            })
            .get();

        var correctInput = '#' + IDs[IDs.length-1];

        $("#uploadFileFormHome").off('click', '.uploadHomeLink');
        $("#uploadFileFormHome").on('click', '.uploadHomeLink', function () {
            $(correctInput).click();
        });
        fileSelectorClickSet = true;
    }
}));

ACC.addToCartHome.callbackPlaceOrderFile = function (value,nonContractProductInCart,nonContractProductInSelectedList) {
    if (value)
    {
        ACC.addToCartHome.submitPlaceorderfile();
    }
    else {
        if(nonContractProductInCart && !nonContractProductInSelectedList){
            ACC.addToCartHome.removeNonContractProductPlaceOrderFile();
        }
        else{
            loadingCircleShow("hide");
        }
    }
}
$('#limit-detail-popup .clsBtn').click(function(){
	$('#limit-detail-popup').modal('hide');  
	$('.modal-backdrop').css('display', 'none');
	
});
$("#placeorderfile").click(function () {  
loadingCircleShow("show");
});

$("#uploadFileFormHome").change(function(e) {
 
	processFile();
	async function processFile() {
try {
	var input = document.getElementById("uploadmultifilehome");
	const file = input.value; // Replace with the actual file
	const resultFinals = await uploadAndScanFile(file);
	if(resultFinals==false){
	$('#malware-detail-popup').modal('show');
	$("#uploadmultifilehome_wrap_list").empty();
	$("#placeorderfileSpn").hide();
	$("#uploadMessagesInstruction").show()
	$(".marginbtmplaceorder").show()
}
} catch (error) {
	console.error('Error processing file:', error);
}
}
 
});