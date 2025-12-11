





ACC.contractFunc = {

		bindAll: function() {
			ACC.contractFunc.searchContractForm();
			
			$(document).on('mouseover mouseout', '#contractpage', function(){
				ACC.contractFunc.loadContractForm();
			});
			
			$(document).on('mouseover mouseout', '#ContractDetailsPage', function(){
				ACC.contractFunc.loadContractDetailForm();
			});
		},
		
		/**
		 * Submits request/form on click of SEARCH from Contract page.
		 */
		searchContractForm: function() {
			$('#searchByInput').on('keypress', function(e) {
		 		if (e.keyCode == 13) 
		 		{
		 			e.preventDefault();
		 		}
			});
			
			/*** On click of Search button, set the 'searchRequest' attribute in the form and reset 'showMoreCounter' back to 0 ***/
			$("#contractSearch").click(function(e) {
				var contractForm = $("#contractForm");
				contractForm.find('input[name="searchRequest"]').val(true);
				contractForm.find('input[name="showMoreCounter"]').val(0); 
				$("#loadNoOfRecordsHidden").val(getShowCount());
				contractForm.submit();
			});
			
			$(".contractPgpdf").click(function(){
				var originalAction = $("#contractForm").attr("action");
				var url = "/my-account/contract/downloadData?downloadType=PDF";
				var form = "#contractForm";
				ACC.contractCommon.downloadFile(originalAction,url,"PDF",getShowCount(),form);
			});
			
			$(".contractPgexcel").click(function(){
				var originalAction = $("#contractForm").attr("action");
				var url = "/my-account/contract/downloadData?downloadType=EXCEL";
				var form = "#contractForm";
				ACC.contractCommon.downloadFile(originalAction,url,"EXCEL",getShowCount(),form);
			});
			
			/* Submit Form by onChange SortBy */
			$("#sortbynumberContract").bind('change', function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
			});
			/* Submit Form by onChange Filter ContractType */
			$("#filterBy1").bind('change', function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
			});
			/* Submit Form by onChange Filter active or InActive */
			$("#filterBy2").bind('change', function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
			});
			
			/* Submit Form For My Contract */
			$("#sortbyPno").bind('change', function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
			});

			$("#contractLoadMore").click(function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val($(this).data('id'));
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$('#scrollPos').val(window.pageYOffset);
				$("#contractForm").submit();
			});
			
			$("#contractLoadNext").click(function(e) {
				e.preventDefault();
				$("#loadNoOfRecordsHidden").val($(this).data('id'));
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$('#scrollPos').val(window.pageYOffset);
				$("#contractForm").submit();
			});
			
			 ////////////////////////////////
			$(".slidingDiv").hide();
			$(".show_hide").show();

			$('.show_hide').toggle(function(){
				$("#plus").text("- Hide Filters");
				$(".slidingDiv").slideDown();
			},function(){
			       $("#plus").text("+ Show Filters");
			       $(".slidingDiv").slideUp();
            });
			   

			$('.select_filter').click( function() {
				var selStatusCount =0;
				var selContTypeCount  = 0;
				var id, name,filterBy1Val,filterBy2Val;
				$(".select_filter").each( function() {
					if($(this).is(':checked')) {
						id= $(this).attr('id');
						name=  $(this).attr('name');
						//for contract Type 
						if(name === 'contractTypeFilter'){
							selContTypeCount = parseInt(selContTypeCount)+1;
							if(filterBy1Val !== undefined && filterBy1Val !== "" && filterBy1Val !== null){
								filterBy1Val = filterBy1Val+","+id;
							}else{
								filterBy1Val =  id;
							} 
						}
						
						//for contract Status
						if(name === 'statusFilter'){
							selStatusCount = parseInt(selStatusCount)+1;
							if(filterBy2Val !== undefined && filterBy2Val !== "" && filterBy2Val !== null){
								filterBy2Val = filterBy2Val+","+id;
							}else{
								filterBy2Val =  id;
							}
						}
						
						var filter1Len = 0;
						var filter2Len = 0;
						if(filterBy1Val !== undefined && filterBy1Val !== null){
							filter1Len = filterBy1Val.split(",");
						}
						if(filterBy2Val !== undefined && filterBy2Val !== null){
							filter2Len = filterBy2Val.split(",");
						}
						if(filter1Len === 2 || filter1Len === undefined ?$("#filterBy1").val(""):$("#filterBy1").val(filterBy1Val));
						if(filter2Len === 2 || filter2Len === undefined ?$("#filterBy2").val(""):$("#filterBy2").val(filterBy2Val));
					}
				});
				
				if(selContTypeCount === 0) {
					$("#filterBy1").val("");
				}
				
				if(selStatusCount === 0) {
					$("#filterBy2").val("");
				}
				 
				$("#loadNoOfRecordsHidden").val(getShowCount());
				//finally submiting
				$("#contractForm").submit();
				
			});
			 
		    
			
		},
		loadContractForm: function() {
			var idx = 0;
			var selBidCount =0;
			var selCommercialCount  = 0;
			var discontinued  = 0;
			$(".myContractsRow").each( function() {
				idx = parseInt(idx)+1;
				var myContractRowId = $(this).attr('id'); 
				var myContractRowStatus = myContractRowId.split("_")[1];
				var contType = $("#"+myContractRowId).find(".column5 span").html().toUpperCase();  
				//for Contract Status inactive
				if (myContractRowStatus == "false") {
					discontinued = parseInt(discontinued)+1;
					$(this).addClass("opaque"); 
				}
				//for Contract Type Z19 and Z20
				var bidName = $("#bidCount").data('name').toUpperCase();
				var commercialName = $("#commercialCount").data('name').toUpperCase();
				if(contType === bidName){
					selBidCount = parseInt(selBidCount)+1;
				}
				
				if(contType === commercialName){
					selCommercialCount = parseInt(selCommercialCount)+1;
				}
			});
			ACC.contractFunc.setContractCount(selBidCount,selCommercialCount,discontinued,idx);
			$("#slidingDiv").show();
		},
		setContractCount: function(selBidCount,selCommercialCount,discontinued,idx){
			$("#bidCount").text(selBidCount);
			$("#commercialCount").text(selCommercialCount);
			
			$("#discontinueCount").text(discontinued);
			$("#activeCount").text(idx - discontinued);
			
			if(discontinued >0){
				$('#0').prop('checked', true);
			}
			if((idx-discontinued) >0){
				$('#1').prop('checked', true);
			}
			
			if(selBidCount >0){
				$('#Z19').prop('checked', true);
			}
			if(selCommercialCount >0){
				$('#Z20').prop('checked', true);
			}
		},
		loadContractDetailForm: function(){
			$(contractDetailTable).find('.contract-tcell-chckbox').click(function(){
				if($(this).prop('checked')==false){
					$('#contract-select-all').prop('checked',false);
				}
				var headCheckFlag=true;
				$(contractDetailTable).find('.contract-tcell-chckbox').each(function(){
					if($(this).prop('checked')==false){
						headCheckFlag=false;
					}	
				});
				if(headCheckFlag==true){
					$('#contract-select-all').prop('checked',true);
				}
				else{
					$('#contract-select-all').prop('checked',false);
				}
			});	
			
			//for mobile checkbox
			$(contractDetailTableMobi).find('.contract-tcell-chckbox').click(function(){
				if($(this).prop('checked')==false){
					$('#contract-select-all-mobi').prop('checked',false);
				}
				var headCheckFlag=true;
				$(contractDetailTableMobi).find('.contract-tcell-chckbox').each(function(){
					if($(this).prop('checked')==false){
						headCheckFlag=false;
					}	
				});
				if(headCheckFlag==true){
					$('#contract-select-all-mobi').prop('checked',true);
				}
				else{
					$('#contract-select-all-mobi').prop('checked',false);
				}
			});
			var contractHeaderActive = $("#contractHeaderActive").val();
			var contractEntryCount= $("#totalEntriesInContract").val();
			var inactiveEntryCount = 0;
			$(".contractDetailRow").each(function(){
				var rowId = $(this).attr('id');
				var rowStatus = rowId.split("_")[1]; 
				if(contractHeaderActive=="false" || rowStatus=="false"){
					inactiveEntryCount= parseInt(inactiveEntryCount)+1;
					$(this).addClass("opaque");
					$(this).find(".contract-tcell-chckbox").css("visibility","hidden");
					$(this).find(".contract-tcell-chckbox").removeClass("contract-tcell-chckbox");
				}
			});
			 if((contractHeaderActive=="false" || parseInt(contractEntryCount)==inactiveEntryCount)){
				$(".addContractToCart").addClass("disabledButton");
				$(".addContractToCart").prop('disabled', true);
			} else {
				$(".addContractToCart").removeClass("disabledButton");
				$(".addContractToCart").prop('disabled', false);
			} 
		}
};

$(".conttab #datatab-desktop_length").on("change",function(){ 
	ACC.contractFunc.loadContractForm();
	});

function getShowCount(){
	var myString = $('#datatab-desktop_length li.selected a span.text').text();
	var fileName = myString.split(' ');
	var count = fileName[0].replace(/\s/g,'');
	return count;
}
$(document).ready(function() {
	ACC.contractFunc.bindAll();
});