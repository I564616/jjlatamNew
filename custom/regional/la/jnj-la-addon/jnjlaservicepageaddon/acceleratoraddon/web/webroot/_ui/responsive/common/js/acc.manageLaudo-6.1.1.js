var laudoLightBoxMarkup;
var fromDateChange = false;
var toDateChange = false;
const LABEL="<label class='error'>";
$(document).ready(function(){
	laudoLightBoxMarkup = $("#manageLaudoHddnLightBox").html();
	$(".checkboxLaudo").attr("disabled",false); // Fool-proofing done to allow select all functionality to work only once doc is loaded
	$(".checkboxLaudoHead").attr("disabled",false); // Fool-proofing done to allow select all functionality to work only once doc is loaded
	$(".jnjId").focus();
	/**
	 * Binding the enter key press action on lote number field / jnj id field
	 */
	$(".loteNumber, .jnjId").keypress(function(e) {
		if(e.which == 13) {
			$("#manageLaudoSerach").click();
	    }
	});
	/**
	 * Custom date-picker for the expiration date filter
	 */
	$(".laudoDate").datepicker({
        numberOfMonths: 1,
        showButtonPanel: false,
        onClose: function(selectedDate) {
            if(selectedDate==''){
            	$(this).prop("value","");
        	}
          }
    });
	/**
	 * Binding laudo From Date change
	 */
	$("#fromDateLaudo").on("change", function(){
		fromDateChange = true;
		performDateFilter();
	});
	/**
	 * Binding laudo To Date change
	 */
	$("#toDateLaudo").on("change", function(){
		toDateChange = true;
		performDateFilter();
	});
	/**
	 * Function to perform the actual date search.
	 */
	function performDateFilter(){
		var fromDate = $('#fromDateLaudo').datepicker('getDate');
    	var toDate = $('#toDateLaudo').datepicker('getDate');
    	/*Case where only From Date is Entered*/
    	if(fromDateChange && null!=fromDate && $("#fromDateLaudo").val() != $("#miscServicesSelectDate").val()){
    		$("#manageLaudoForm").submit();
    	}
    	/*Case where only TO Date is Entered*/
    	else if(toDateChange && null!=toDate && $("#toDateLaudo").val() != $("#miscServicesSelectDate").val()){
    		$("#manageLaudoForm").submit();
    	}
    	/*Case where both From Date and TO Date is Entered*/
    	else if(fromDateChange && toDateChange && null!=fromDate && null!= toDate && $("#fromDateLaudo").val() != $("#miscServicesSelectDate").val() && $("#toDateLaudo").val() != $("#miscServicesSelectDate").val() && toDate>=fromDate){
			$("#manageLaudoForm").submit();
		}
		/*Case where no Date is Entered*/
		else if(fromDateChange && toDateChange && null==fromDate && null==toDate) {
			$("#manageLaudoForm").submit();
		}
	}
	/**
	 * Binding delete bin icon click
	 */
	$(".deleteRowLaudo").click(function(){
		var data = [];
		data.push($(this).parent().parent().find(".checkboxLaudo").val());
		var deleteFileNames = data.join(",");
		$('#deleteFileNames').val(deleteFileNames);
		$('#deleteLaudoForm').submit();
	});
	/**
	 * Binding remove selected files button
	 */
	$("#manageLaudoRemoveFiles").click(function(){
		var data = [];
		$(".checkboxLaudo:checked").each(function() {
			data.push($(this).val());
		});
		var deleteFileNames = data.join(",");
		$('#deleteFileNames').val(deleteFileNames);
		$('#deleteLaudoForm').submit();
	});
	/**
	 * Binding sort by change
	 */
	$("#laudoSortBy").on("change", function(){
		$("#manageLaudoForm").submit();
	});
	/**
	 * Binding the Download PDF button
	 */
	$(".anchorClicker").click(function(){
		window.location = $(this).parent().find("a").attr("href");
	});
	/**
	 * Auto Pop up on success / failure of the single file upload operation
	 */
	if($("#fileSizeFlag").val()!='true' && ($("#singleAutoPopUp").val()=='true' || $("#csvAutoPopUp").val()=='true' || $("#multiAutoPopUp").val()=='true')){
		$(".laudoFileUploadSelection").hide();
				if($("#singleAutoPopUp").val()=='true') {
					$(".singleFileUploadLaudo").modal("show");
					$(".singleLaudoUploadScreen").hide();
					$(".singleLaudoUploadError").show();
				}else if($("#csvAutoPopUp").val()=='true') {
					$(".csvLaudoUploadStatus").modal("show");
					if($("#hddnCsvStatus").val() == 'false') {
						$(".errorHide").hide();
						$(".errorUnhide").show();
						$("#moveBackToCsvUpload").click(function(){
							bindCsvFileLaudoPopup();
							$(".csvLaudoUploadStatus").modal("hide");
						});
					} else {
						$("#moveToLaudoMultiFileScreen").click(function (){
							bindMultiFileLaudoPopup();
						});
					}
				}else{
					$(".multiFileUploadLaudo").modal("show");
					$(".multiFileUploadLaudoScreen").hide();
					$(".multiFileUploadLaudoStatus").show();
				}
			
	} else if($("#fileSizeFlag").val()=='true') {
		if($("#singleAutoPopUp").val()=='true') {
					$("#fileSizeSingleError").removeClass("hidden");
					bindSingleFileLaudoPopup();
			
		} else if($("#csvAutoPopUp").val()=='true') {
					$("#fileSizeCsvError").removeClass("hidden");
					bindCsvFileLaudoPopup();
			
		} else if($("#multiAutoPopUp").val()=='true'){
					$(".laudoFileUploadSelection").hide();
					bindMultiFileLaudoPopup();
					$("#fileSizeError").removeClass("hidden");
			
		}
	}
	/** START: Check-box select all or none functionality **/
	/**
	 * Click of any check box in the rows
	 */
	$(".checkboxLaudo").click(function(){
		$(".mandatoryError").html("");
		// Checking if the boxes that are checked are less than the total boxes - 
		if($(".checkboxLaudo:checked").size() < $(".checkboxLaudo").size()) {
			$(".checkboxLaudoHead").attr("checked",false); // change header check box to unchecked
		// Checking if the boxes that are checked are equal to the total boxes - 
		} else if($(".checkboxLaudo:checked").size() == $(".checkboxLaudo").size()) {
			$(".checkboxLaudoHead").attr("checked",true); // change header check box to checked
		}
	});
	/**
	 * Click of the header check box
	 */
	$(".checkboxLaudoHead").click(function(){
		$(".mandatoryError").html("");
		// Checking if the header box is checked
		if(this.checked) {
			$(".checkboxLaudo").attr("checked",true); // Checked all boxes in rows
		}else{
			$(".checkboxLaudo").attr("checked",false); // Unchecked all boxes in rows
		}
	});
	/** END: Check-box select all or none functionality **/
	/**
	 * Binding Add Files button to open LightBox
	 */
	$(".manageLaudoAddFiles").click(function(){
		$("#manageLaudoUploadLightBox").modal("show");
		$('.laudoFileUploadSelection').show()
	});
	
	$('.singleFileLaudo').click(function(){
		bindSingleFileLaudoPopup();
	});
	
	$('.multiFileLaudo').click(function(){
		bindCsvFileLaudoPopup();
	});
	
	
	/**
	 * Binding the ManageLaudoSerach button to submit Form.
	 */
	$('#manageLaudoSerach').click(function(e){
		e.preventDefault();
		$(".mandatoryError").html("");
		if(!$.isEmptyObject($("#manageLaudoNoSearchInfo"))){
			$("#managenosearchalert").hide();
			$("#manageLaudoNoSearchInfo").hide();
		}
		if(($('#isBatchMandatory').val())==="true") {
				if(!!$('#jnjId').val() && !!$('#loteNumber').val()){
					$('#searchBy').val('productAndLaudo');
					$('#manageLaudoForm').submit();
				    }
					else {
						$(".mandatoryError").html(LABEL + $("#hddnMandatoryError").val() + "</label>");
					}
			} else {
					if(!!$('#jnjId').val()){
						$('#searchBy').val('productAndLaudo');
						$('#manageLaudoForm').submit();
				    } else {
						$(".mandatoryError").html(LABEL + $("#hddnMandatoryError").val() + "</label>");
					}
				}
	});
	/**
	 * Binding load more button click
	 */
	$('#laudoLoadMore').click(function(e){
		e.preventDefault();
		var form = $('#manageLaudoForm');
		$('#laudoMoreResults').val(true);
		var counter = $('#laudoLoadMoreClickCounter').val();
		var counterInteger=parseInt(counter);
		counterInteger++;
		$('#laudoLoadMoreClickCounter').val(counterInteger);
		form.submit();
	});
	/**
	 * This function performs all necessary actions when the single file upload popup is initiated
	 */
	function bindSingleFileLaudoPopup(){
		$('.singleFileUploadLaudo').modal("show");
		$(".singleLaudoUploadScreen").show();
		$(".singleLaudoUploadError").hide();
		$('#browseSingleFileLaudo').change(function(){
			$("#fileSizeSingleError").addClass("hidden");
			$("#fileFormatSingleError").addClass("hidden");
			$("#attachFileLaudo").val($('#browseSingleFileLaudo').val().substring($('#browseSingleFileLaudo').val().lastIndexOf('\\') + 1,$('#browseSingleFileLaudo').val().length));
			
		});
		$("#saveSingleFileLaudo").click(function(){
			var validity = true;
			var form = $("#manageLaudoSingleUploadForm");
			form.append(jQuery('<input>', {
			        'name' : 'CSRFToken',
			        'value' : ACC.config.CSRFToken,
			        'type' : 'hidden'}));
					
			$(".mandatory").each(function() {
				if(this.value==null || this.value==''){
					validity = false;
					$(this).css("border-color","#B31602");
					$(this).parent().find("label.textLabel").css("color","#B31602");
				}else{
					$(this).css("border-color","");
					$(this).parent().find("label.textLabel").css("color","");
				}
			});
			if($("#browseSingleFileLaudo").val() == null || $("#browseSingleFileLaudo").val() == ''){
				validity = false;
				$("#attachFileLaudo").css("border-color","#B31602");
				$("#attachFileLaudo").parent().find("label.textLabel").css("color","#B31602");
			}else{
				$("#attachFileLaudo").css("border-color","");
				$("#attachFileLaudo").parent().find("label.textLabel").css("color","");
			}
			if(validity) {
				$("#emptyFieldsError").html("");
				var extensionSplit = document.getElementById("browseSingleFileLaudo").value.split(/\.(?=[^\.]+$)/);
				if(typeof extensionSplit[1] == 'undefined' || (typeof extensionSplit[1] != 'undefined' && extensionSplit[1].toLowerCase() != "pdf")) {
					$("#fileFormatSingleError").show();
				}else{
					checkForDisabledFormElements();
					$("#manageLaudoSingleUploadForm").submit();
				}
			}else{
				$("#emptyFieldsError").html(LABEL + $("#hddnEmptyFieldsError").val() + "</label>");
				
			}
		});
		$('.laudoFileUploadSelection').hide();
		//$('.singleFileUploadLaudo').modal("show");
		
	}
	/**
	 * This function performs all necessary actions when the csv file upload popup is initiated
	 */
	function bindCsvFileLaudoPopup(){
		$('.laudoFileUploadSelection').hide();
		$('.csvFileUploadLaudo').modal("show");
		$("#browseCsvFileLaudo").change(function(){
			$("#csvUploadError").addClass("hidden");
			$("#fileSizeCsvError").addClass("hidden");
			if(this.value!=null && this.value!="") {
				if(typeof this.value.split(".")[1] != 'undefined' && this.value.split(".")[1].toLowerCase() == "csv") {
					$("#csvFileExtension").val("csv");
					$("#fileInfolaudo").text($(this).val().split(/\\/).pop()); // Get file name
					$("#fileInfolaudo").css("display","block");
					$("#saveCsvFileLaudo").removeAttr('disabled');
					$(".errorDetailsCsvUpload").html("<span>" + $(this).val().substring($(this).val().lastIndexOf('\\') + 1, $(this).val().length) + "</span>").show();
					$("#saveCsvFileLaudo").click(function (){
						var form = $("#csvLaudoUploadForm");
						 form.append(jQuery('<input>', {
						        'name' : 'CSRFToken',
						        'value' : ACC.config.CSRFToken,
						        'type' : 'hidden'}));
						 
						$("#csvLaudoUploadForm").submit();
					});
				}else{
					$("#fileInfolaudo").text($(this).val().split(/\\/).pop()); // Get file name
					$("#fileInfolaudo").css("display","block");
					$("#csvUploadError").removeClass("hidden");
				}
			} else {
				$("#fileInfolaudo").text($(this).val().split(/\\/).pop()); // Get file name
				$("#fileInfolaudo").css("display","block");
				$("#csvUploadError").removeClass("hidden");
			}
		});
	}
	/**
	 * This function performs all necessary actions when the multi file upload popup is initiated
	 */
	function bindMultiFileLaudoPopup(){
		$(".csvLaudoUploadStatus").modal("hide");
		$(".multiFileUploadLaudo").modal("show");
		$(".multiFileUploadLaudoScreen").show();
		$(".multiFileUploadLaudoStatus").hide();
		$("#browseMultiFileLaudo").change(function(){
			$("#multiUploadError").addClass("hidden");
			$("#fileSizeError").addClass("hidden");
			if(this.value!=null && this.value!="") {
				if(typeof this.value.split(".")[1] != 'undefined' && this.value.split(".")[1].toLowerCase() == "zip") {
					$("#zipFileExtension").val("zip");
					$("#fileInfolaudomulti").text($(this).val().split(/\\/).pop()); // Get file name
					$("#fileInfolaudomulti").css("display","block");
					$("#saveMultiFileLaudo").removeAttr('disabled');
					$(".errorDetailsMultiUpload").html("<span>" + $(this).val().substring($(this).val().lastIndexOf('\\') + 1, $(this).val().length) + "</span>").show();
					$("#saveMultiFileLaudo").click(function (){
						var form = $("#multiLaudoUploadForm");
						 form.append(jQuery('<input>', {
						        'name' : 'CSRFToken',
						        'value' : ACC.config.CSRFToken,
						        'type' : 'hidden'}));
						$("#multiLaudoUploadForm").submit();
					});
				}else{
					$("#fileInfolaudomulti").text($(this).val().split(/\\/).pop()); // Get file name
					$("#fileInfolaudomulti").css("display","block");
					$("#multiUploadError").removeClass("hidden");
				}
			}
			
		});
		
	}
	
	/**
	 * This method is used fade out the luado rows which do not have a 
	 * PDF attached to them. It also changes the 'downlaoad' 
	 * button to 'add file' thus allowing the user to add files 
	 * on each row
	 */
	$(".managelaudoRow").each(function() {
		var managelaudoRowId = $(this).attr('id');
		var managelaudoRowStatus = managelaudoRowId.split("_")[2];
		if (managelaudoRowStatus == "false") {
			$($(this).find('.anchorClicker')).remove();
			$($(this).find('.column6')).append('<input type="button" class="addSinglePdf btn btnclsnormal add-btn" value="'+$('#addFileText').val()+'"/>')
		}
	});
	/**
	 * Binding the Add PDF button
	 */
	$(".addSinglePdf").on("click", function(){
		var laudoEntryId = $($($(this).parent()).parent()).attr('id');
				bindSingleFileLaudoPopup();
				moidifySingleFileUploadPopup(laudoEntryId);
		
	});
	/**
	 * This function modifies the single file upload popup. It freezes and 
	 * preppopulated productCode, laudoNumber, expirationDate.
	 */
	function moidifySingleFileUploadPopup(laudoEntryId){
		var laudoEntry = $('#'+laudoEntryId);
		var productCode = laudoEntry.find(".column2").html(); 
		var laudoNumber = laudoEntry.find(".column3").html();
		/*Getting Expiration Date*/ 
		var expirationDateString  = laudoEntry.find(".column5").html();
		var dsplit = expirationDateString.split("/");
		var year = parseInt(dsplit[2]);
		var month = parseInt(dsplit[0])-1;
		var day = parseInt(dsplit[1]);
		var expirationDate=new Date(year,month,day);
		$(" #manageLaudoSingleUploadLightBox input[name=productCode]").attr('value',productCode);
		$(" #manageLaudoSingleUploadLightBox input[name=productCode]").addClass('disabled');
		$(" #manageLaudoSingleUploadLightBox input[name=productCode]").attr('disabled','disabled');
		$(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").attr('value',laudoNumber);
		$(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").addClass('disabled');
		$(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").attr('disabled','disabled');
		$(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").datepicker('setDate',expirationDate);
		$(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").addClass('disabled');
		$(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").attr('disabled','disabled');
	}
	/**
	 * This function removes the disabled attribute if already applied on Form Elements
	 */
	function checkForDisabledFormElements(){
		if($(" #manageLaudoSingleUploadLightBox input[name=productCode]").attr('disabled')=="disabled"){
			$(" #manageLaudoSingleUploadLightBox input[name=productCode]").removeAttr('disabled');
			$(" #manageLaudoSingleUploadLightBox input[name=productCode]").css('color','#6D6D6D');
		}
		if($(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").attr('disabled')=="disabled"){
			$(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").removeAttr('disabled');
			$(" #manageLaudoSingleUploadLightBox input[name=laudoNumber]").css('color','#6D6D6D')
		}
		if($(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").attr('disabled')=="disabled"){
			$(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").removeAttr('disabled');
			$(" #manageLaudoSingleUploadLightBox input[name=expirationDate]").css('color','#6D6D6D')
		}
	}
	
	
});
// single laudo upload file
$("#browseSingleFileLaudo").change(function(e) {
 
	processFile();
	async function processFile() {
try {
	var input = document.getElementById("browseSingleFileLaudo");
	const file = input.value; // Replace with the actual file
	const resultFinals = await uploadAndScanFile(file);
	if(resultFinals==false){
	$('#malware-detail-popup').modal('show');
	$("#attachFileLaudo").val('');
    }
    } catch (error) {
	  console.error('Error processing file:', error);
    }
    }
 
});

//multi laudo upload file
$("#browseCsvFileLaudo").change(function(e) {
 
	processFile();
	async function processFile() {
try {
	var input = document.getElementById("browseCsvFileLaudo");
	const file = input.value; // Replace with the actual file
	const resultFinals = await uploadAndScanFile(file);
	if(resultFinals==false){
	$('#malware-detail-popup').modal('show');
	$("#fileInfolaudo").empty();
	var multiladoupload = document.getElementById("saveCsvFileLaudo");
    multiladoupload.disabled=true;
    }
    } catch (error) {
	  console.error('Error processing file:', error);
    }
    }
 
});