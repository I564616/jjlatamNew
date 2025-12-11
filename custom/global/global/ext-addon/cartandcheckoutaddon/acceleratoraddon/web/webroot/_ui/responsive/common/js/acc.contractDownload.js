
ACC.contractFunc = {

		bindAll: function() {
			//
			ACC.contractFunc.searchContractForm();
			ACC.contractFunc.loadContractForm();
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
			
			/*$(".contractpdf").click(function(){
				var originalAction = $("#contractForm").attr("action");
				$("#contractForm").attr("action", ACC.config.contextPath +"/my-account/contract/downloadData?downloadType=PDF");
				$("#contractForm").attr("method","POST");
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
				$("#contractForm").attr("action", originalAction);
				$("#contractForm").attr("method","POST");
			});
			
			$(".contractexcel").click(function(){
				var originalAction = $("#contractForm").attr("action");
				$("#contractForm").attr("action", ACC.config.contextPath +"/my-account/contract/downloadData?downloadType=EXCEL");
				$("#contractForm").attr("method","POST");
				$("#loadNoOfRecordsHidden").val(getShowCount());
				$("#contractForm").submit();
				$("#contractForm").attr("action", originalAction);
				$("#contractForm").attr("method","POST");
			});*/
			
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
			   
			   //////////////////////
		   /* $("#showOrHideFilter").click(function(){
		        $("#filterInnerBox").toggle();
		    });*/
		    
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
						//var filter2Len = filterBy2Val.split(",");
						if(filter1Len.length === 2 || filter1Len === undefined ?$("#filterBy1").val(""):$("#filterBy1").val(filterBy1Val));
						if(filter2Len.length === 2 || filter2Len === undefined ?$("#filterBy2").val(""):$("#filterBy2").val(filterBy2Val));
						/*$("#filterBy1").val(filterBy1Val);
						$("#filterBy2").val(filterBy2Val);*/
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
			var currViewScreenName = $("#currViewScreenName").val();
			$(".myContractsRow").each( function() {
				var myContractRowId = $(this).attr('id'); 
				var myContractRowStatus = myContractRowId.split("_")[1];
				var screenName = myContractRowId.split("_")[3];
				if(currViewScreenName === screenName){ //identify which screen the page loading
					idx = parseInt(idx)+1;
					var contType = $("#"+myContractRowId).find(".contractType").html(); //mobile and desktop
					//for Contract Status inactive
					if (myContractRowStatus == "false") {
						discontinued = parseInt(discontinued)+1;
						$(this).addClass("opaque"); 
					}
					//for Contract Type Z19 and Z20
					if(contType === 'BID'){
						selBidCount = parseInt(selBidCount)+1;
					}
					
					if(contType === 'Commercial Agreement'){
						selCommercialCount = parseInt(selCommercialCount)+1;
					}
				}
			});
			 //console.log("selBidCount : "+ selBidCount+" selCommercialCount : "+selCommercialCount+" discontinued :  "+discontinued+" idx : "+idx)
			ACC.contractFunc.setContractCount(selBidCount,selCommercialCount,discontinued,idx);
			/*$("#bidCount").text(selBidCount);
			$("#commercialCount").text(selCommercialCount);
			
			$("#discontinueCount").text(discontinued);
			$("#activeCount").text(idx - discontinued);*/
			/*$("#filterInnerBox").show();*/
			$("#slidingDiv").show();
		},
		setContractCount: function(selBidCount,selCommercialCount,discontinued,idx){
			$(".bidCount").text(selBidCount);
			$(".commercialCount").text(selCommercialCount);
			
			$(".discontinueCount").text(discontinued);
			$(".activeCount").text(idx - discontinued);
			
			if(discontinued >0){
				$('.0').prop('checked', true);
			}
			if((idx-discontinued) >0){
				$('.1').prop('checked', true);
			}
			
			if(selBidCount >0){
				$('.Z19').prop('checked', true);
			}
			if(selCommercialCount >0){
				$('.Z20').prop('checked', true);
			}
			
			
		}
		
};

function getShowCount(){
	/*AAOL-6296 modified the code to remove any possible spaces in count
	 var myString = $('#datatab-desktop_length li.selected a span.text').text();
	alert('valuee >> '+$('#contractForm select[name="datatab-desktop_length"]').val());
	alert("String is: "+myString);
	var fileName = myString.split(' ');*/
	var count = $('#contractForm select[name="datatab-desktop_length"]').val();  
	return count;
}
$(document).ready(function() {
	ACC.contractFunc.bindAll();
});