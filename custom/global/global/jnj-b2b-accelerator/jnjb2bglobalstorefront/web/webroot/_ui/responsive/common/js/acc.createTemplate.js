jQuery(document).ready(function () {
	var currViewScreenName = $("#currViewScreenName").val();
	
	if($('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').length == 0)
	{
	 $("#createOrdersTable-"+currViewScreenName).hide();
	}
	
	
	var allowReq = true;
	var allowCheck = false;
	var inCreateTemplatePage = false;
	var createOrderTable = $('#createOrdersTable-desktop').DataTable({
		"bDestroy": true,
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": "No data found."},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	var createOrderTableMobile = $('#createOrdersTable-mobile').DataTable({
		"bDestroy": true,
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": "No data found."},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	var editOrderTable = $('#editOrdersTable-desktop').DataTable({
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": "No data found."},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	
	var editOrderTableMobile = $('#editOrdersTable-mobile').DataTable({
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": "No data found."},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
 
	
	$("#createTemplate").click(function (e) {
		window.location.href = ACC.config.contextPath + '/templates/createTemplate';
	});


	/* Start - check for build an order page */


	/*updated by lokesh & naga sept30*/
	$('.build-ordr-cancel-btn,.menu-item a,#jnj-menu-list li,#jnj-logo-holder').on('click', function (e) {
		/*jQuery.ajax({
		 type: "GET",
		 url:  ACC.config.contextPath +'/buildorder/clearSessionItem' ,
		 });*/
		if ($(this).find('a').attr('href') != undefined) {
			$('#leave-page-btn').attr('href', $(this).find('a').attr('href'));
		}
		else if ($(this).attr('href') != undefined) {
			$('#leave-page-btn').attr('href', $(this).attr('href'));
		}
		if (inCreateTemplatePage) {
			e.preventDefault();
			
			$('.menu-item a').each(function(){
				$(this).addClass('noPointerEvents')
			})
			
			$('#items-in-order-popup').modal('show');
			return false;
		}
		else{
			$('.menu-item a').each(function(){
				$(this).removeClass('noPointerEvents')
			})
			if($(this).hasClass('goBackPage')){
				history.go(-1)
			}
		}
	});
	/* End - check for build an order page */
	$('#QtyQuantity').on('keyup keypress change', function()
	{
        var value = $(this).val();
        pttern = /^\d{1,6}$/g;
        if(value.match(pttern))
        {
			return true;
        }
		else
		{
			//$(this).val(value.substring(0,value.length-1));
			$(this).val('');
		}

    });
	$("#createAddToTemplate").click(function (e) {
		$("#createTemplateMsg").css("display", "none");
		var dataObj = {};
		dataObj.productCode = $("#productNumber").val().trim();
		dataObj.qty = $("#QtyQuantity").val().trim();
		var quantity = $("#QtyQuantity").val().trim();
		$("#createTemplateMsg").css("display", "none");
		if ($("#QtyQuantity").val().trim() == null || dataObj.qty == "" || dataObj.qty == "0" ) {
			dataObj.qty = "1";
			var quantity = "1";
		}
		var allValid = true;
		var productAdded = false;
		
		jQuery.ajax({
			type: "POST",
			url: ACC.config.contextPath + '/templates/productValidate',
			data: dataObj,
			async:false,
			success: function (data) {

				if (data == "invalid") {
					$("#createTemplateMsg").css("display", "block");
				}// addItem Ajax End
				else {
					var splitedDetails = data.split("$$$");
					/*if (splitedDetails[2] == null) {

						splitedDetails[2] = "unit";
					}*/
					var createPrd=[];
					var productExisting=false;
					$('.product-code-template').each(function(){
						createPrd.push($(this).text().trim(''));
					});
					
					/*$('#createOrdersTable-'+currViewScreenName+' tr').each(function (i) {
						if (i > 0) {
							//createPrd.push($(this).find(".product-code-template").html());
							createPrd.push($(this).find(".product-code-template").html());
						}
					});*/
					for(var l=0;l<createPrd.length;l++){
						if($('#productNumber').val().trim('')==createPrd[l]){
							productExisting=true;
						}
					}
					var i=0;
					//if (($('#' + $("#productNumber").val().trim()).length) >= 1) {
					if ($("#productNumber").val().trim().length>= 1 && productExisting) {	
						$('#createOrdersTable-'+currViewScreenName+' tr').each(function (i) {
							if (i > 0) {
								var prdCode = $(this).find(".product-code-template").html().trim();
								var prdNumb = $("#productNumber").val().trim();
								if (prdCode == prdNumb) {
									$(this).find('#addProdQnty').val(function (i, oldval) {
										var a = parseInt(oldval, 10);
										if($('#QtyQuantity').val() == undefined || $('#QtyQuantity').val()=="")
											{
											 var b = 1;
											}
										else
											{
										var b = parseInt($('#QtyQuantity').val(), 10);
									        }
										
										oldval = a + b;
										$('#productNumber').val('');
										$('#QtyQuantity').val('');
										return oldval;
									});
								}
							}
						});
					}
					else {
						if(currViewScreenName === 'desktop'){
							$("#createOrdersTable-"+currViewScreenName).show();
							createOrderTable.row.add([
								'<span class="product-code-template" id=' + splitedDetails[0] + '>' + splitedDetails[0] + '</span>',
								splitedDetails[1],
								"<input type='text' class='form-control txtWidth numeric-only' value=" + quantity + " id='addProdQnty'><p class='margintop10'> Unit:" + splitedDetails[4]+"("+splitedDetails[2] +splitedDetails[4]+")</p></td>",
								'<a href="#" id="deleteCreateTemplateProd">'+DELETE_TMPLT+'</a>'
							]).draw();
						}else if(currViewScreenName === 'mobile'){
							$("#createOrdersTable-"+currViewScreenName).show();
							createOrderTableMobile.row.add([
									'<tr id="#mobile-'+splitedDetails[0].trim()+'"><td class="panel-title text-left"><a data-toggle="collapse" data-parent="#accordion" href="#collapse'+splitedDetails[0].trim()+'" class="toggle-link panel-collapsed skyBlue ipadacctoggle">'
									+'<span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span><span class="product-code-template" id=' + splitedDetails[0] + '>'+splitedDetails[0]+'</span></a>'
									+'<div id="collapse'+splitedDetails[0].trim()+'" class="panel-collapse collapse">'
									+'<div class="panel-body details">'
									+'<p><spring:message code="template.create.product.name"/>'
									+'<p>'+splitedDetails[1]+'</p>'
									+'<p><spring:message code="template.create.quantity"/>'
									+'<p><input type="text" class="form-control txtWidth numeric-only" value= '+ quantity + ' id="addProdQnty"></p>'
									+'<p class="margintop10">Unit: '+splitedDetails[4]+'('+splitedDetails[2] +splitedDetails[4]+')</p>'
									+'<p><a href="#" id="deleteCreateTemplateProd">'+DELETE+'</a></p>'
									+'</div></div></td></tr>'
							]).draw();
						}
						
						$('#productNumber').val('');
						$('#QtyQuantity').val('');
						$("#noProdTemplateMsg").css("display", "none");
					}
					if ($('.desk-create-template-table-'+currViewScreenName+ ' tbody').find('tr td').length > 1) {
						inCreateTemplatePage = true;
					}
				}// productValidate Sucess End
			}

		});// productValidateAjai call End
		
	});// Add to Order End


	/*Modified by Vijay End*/

	$(".removeProd").click(function (e) {
		var dataObj = {};
		loadingCircleShow("show");
		var prdCode = $(this).attr('name');
		dataObj.productCode = prdCode;
		jQuery.ajax({
			type: "POST",
			url: ACC.config.contextPath + '/buildorder/deleteItem',
			data: dataObj,
			success: function (data) {
				location.reload();
				loadingCircleShow("hide");
			}
		});
	});

	$(".editTemplatebtn").click(function (e) {
		window.location.href = ACC.config.contextPath + '/templates/editTemplate/' + $("#hddnTemplateNumber").val();
	});

	$('#editQuantity').on('keyup keypress', function()
			{
		        var value = $(this).val();
		        pttern = /^\d{1,6}$/g;
		        if(value.match(pttern))
		        {
					return true;
		        }
				else
				{
					$(this).val(value.substring(0,value.length-1));
				}
		
		    });
	$("#editAddToTemplate").click(function (e) {
		$("#editTemplateMsg").css("display", "none");
		dataObj.productCode = $("#editProductNumber").val().trim();
		dataObj.qty = $("#editQuantity").val().trim();
		var quantity = $("#editQuantity").val().trim();
		$("#emptyTemplateMsg").css("display","none");

		if ($("#editQuantity").val().trim() == null || dataObj.qty == ""|| dataObj.qty == "0") {
			dataObj.qty = "1";
			var quantity = "1";
		}
		var allValid = true;
		var productAdded = false;
		jQuery.ajax({
			type: "POST",
			url: ACC.config.contextPath + '/templates/productValidate',
			data: dataObj,
			async:false,
			success: function (data) {


				if (data == "invalid") {
					$("#editTemplateMsg").css("display", "block");
				}// addItem Ajax End
				else {
					var splitedDetails = data.split("$$$");
					
					var editPrd=[];
					var productExist=false;
					/*$('.editProductCode').each(function(){
						editPrd.push($(this).text());
					});*/
					$('.editProductCode').each(function(){
						editPrd.push($(this).text().trim(''));
					});
					/*var j=0;
					$('#editOrdersTable-'+currViewScreenName+' tr').each(function (j) {
						if (j > 0) {
					
						editPrd.push($(this).find(".editProductCode").text().trim());
						}
					});*/
					
					for(var i=0;i<editPrd.length;i++){
						if($('#editProductNumber').val()==editPrd[i]){
							productExist=true;
						}
					}
				
					if ($("#editProductNumber").val().trim().length>= 1 && productExist) {
			
						$('#editOrdersTable-'+currViewScreenName+' tr').each(function (i) {
							
							if (i > 0) {
								if ($(this).find(".editProductCode").html().trim() == $("#editProductNumber").val().trim()) {
								
									$(this).find('#prodQnty').val(function (i, oldval) {
										
										
										var a = parseInt(oldval, 10);
										
										
										if($('#editQuantity').val() == undefined || $('#editQuantity').val()=="" ||$('#editQuantity').val()=="0")
										{
										 var b = 1;
										}
									else
										{
									     var b = parseInt($('#editQuantity').val(), 10);
								        }
										
										
										oldval = a + b;
										$('#editProductNumber').val('');
										$('#editQuantity').val('');
										return oldval;
									});
								}

							}
						});
					}
					else {
			
						/*if(currViewScreenName === 'desktop'){
							$("#editOrdersTable-"+currViewScreenName).show();*/
							editOrderTable.row.add([
													'<span class="editProductCode" id=' + splitedDetails[0] + '>' + splitedDetails[0] + '</span>',
													splitedDetails[1],
													"<input type='text' class='form-control txtWidth' value=" + quantity + " id='prodQnty'><p class='margintop10'>Unit:"+splitedDetails[4]+"("+splitedDetails[2] +splitedDetails[4]+")</p></td>",
													'<a href="#" class="deleteTemplateProd">'+DELETE+'</a>'
												]).draw();
						/*}else if(currViewScreenName === 'mobile'){
							$("#editOrdersTable-"+currViewScreenName).show();	*/
							editOrderTableMobile.row.add([
													'<span class="editProductCode" id=' + splitedDetails[0] + '>' + splitedDetails[0] + '</span>',
													splitedDetails[1],
													"<input type='text' class='form-control txtWidth' value=" + quantity + " id='prodQnty'><p class='margintop10'>Unit:"+splitedDetails[4]+"("+splitedDetails[2] +splitedDetails[4]+")</p></td>",
													'<a href="#" class="deleteTemplateProd">'+DELETE+'</a>'
												]).draw();
							
							setTimeout(function(){ACC.globalActions.menuNdbodyHeight},800);
						/*}*/
						$('#editProductNumber').val('');
						$('#editQuantity').val('');
						return false;
					}
				}
				
			} // productValidate Success End
		});
		// productValidateAjai call End


		inCreateTemplatePage = true;
		
	});
	$("#prodQnty").change(function(){
		inCreateTemplatePage = true;
	});
	$(document).on("click", '.deleteTemplateProd', function () {
	/*	if(currViewScreenName === 'desktop'){*/
			editOrderTable.row( $(this).parents('tr') ).remove().draw();
			editOrderTableMobile.row( $(this).parents('tr') ).remove().draw();
		/*}else if(currViewScreenName === 'mobile'){
			editOrderTableMobile.row( $(this).parents('tr') ).remove().draw();
		}*/

		if($('#editOrdersTable-'+currViewScreenName+' > tbody  > tr').length == 0)
		{
		 $("#editOrdersTable-"+currViewScreenName).hide();
		}
	});

	$(document).on('click', '#deleteCreateTemplateProd', function () {
		/*if(currViewScreenName === 'desktop'){*/
			createOrderTable.row( $(this).parents('tr') ).remove().draw();
			createOrderTableMobile.row( $(this).parents('tr') ).remove().draw();
		/*}else if(currViewScreenName === 'mobile'){
			createOrderTableMobile.row( $(this).parents('tr') ).remove().draw();
		}*/
	});
	
	 var tempProd_Qtys=null;
	 
	$(document).on("click", '.createNewTemplate', function () {
		$("#editTemplatezeroMsg").css("display", "none");
		var  qtyStatus=false;
		
		if($('#createOrdersTable-'+currViewScreenName).find('td:first').hasClass('dataTables_empty')==false){
			var rowCount = $('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').length;
			if($.trim($('#templateName').val())!=""){
			
			$('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').each(function(i) {
		   
			  $this = $(this)
			  var value = $this.find("span.product-code-template").html();
			  var quantity = $this.find("input#addProdQnty").val();
			  if(i==0)
				  {
				    tempProd_Qtys =value+":"+ quantity;
				    if(quantity=="0" || quantity ==""){
						  qtyStatus=true;
					  }
				  }
			  else
				  {
				  tempProd_Qtys+=","+value+":"+ quantity;
				  if(quantity=="0" || quantity ==""){
					  qtyStatus=true;
				  }
				  }
			  
			});
			if(qtyStatus==true){
				$("#editTemplatezeroMsg").css("display", "block");
				return false;
			}
			var obj = new Object();
			//obj.page =
			var createTemplateForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/addcreateTemplate",
				'id' : 'createTemplate',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'tempProdQtys',
				'value' : tempProd_Qtys,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'shareStatus',
				'value' : $('#check4').is(':checked'),
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'tempName',
				'value' : $('#templateName').val(),
				'type' : 'hidden'
			})).append(jQuery('<input>', { // Account GLN input created 
		    	'name' : 'CSRFToken',
		    	 'value' : ACC.config.CSRFToken,
		    	 'type' : 'hidden' 
			}));
			
			$(".mobile-no-pad").append(createTemplateForm);
			createTemplateForm.submit();
			}else{
				$("#editTemplatezeroMsg").css("display", "none");
				$("#noTemplateNameMsg").css("display", "block");
				return false;	
				
			}	
			
			
			
		} else{
			$("#noProdTemplateMsg").css("display", "block");
			$("#editTemplatezeroMsg").css("display", "none");
			return false;
		}
		
		
	});
	
	
	var editTempProd_Qtys=null;
	 
	$(document).on("click", '#saveEditedTemplate', function () {
		$("#editTemplatezeroMsg").css("display", "none");
		var  qtyStatus=false;
		var rowCount = $('#editOrdersTable-'+currViewScreenName+' > tbody  > tr').length;
		if(rowCount>=1 )
		{
			if($('#editOrdersTable-'+currViewScreenName+' > tbody  > tr > td').hasClass('dataTables_empty'))
			{
				$("#emptyTemplateMsg").css("display","block");
				$("#editTemplatezeroMsg").css("display", "none");
				return false;
			}
		$('#editOrdersTable-'+currViewScreenName+' > tbody  > tr').each(function(i) {
	   
		  $this = $(this)
		  var value = $this.find("span.editProductCode").html();
		  var quantity = $this.find("input#prodQnty").val();
		 // tempProd_Qtys=value;
		  if(i==0)
		  {
			  editTempProd_Qtys =value+":"+ quantity;
			  if(quantity=="0" || quantity ==""){
				  qtyStatus=true;
			  }
		  }
		  else
		  {
			  editTempProd_Qtys+=","+value+":"+ quantity;
			  if(quantity=="0"|| quantity ==""){
				  qtyStatus=true;
			  }
		  }
		  
		});
		
		if(qtyStatus==true){
			$("#editTemplatezeroMsg").css("display", "block");
			return false;
		}
		
		if($('#templateName').val()==""){
			$('#noTemplateNameMsg').show();
			return false;
		}
		
		var obj = new Object();
		//obj.page =
		
		var editTemplateForm = jQuery('<form>', {
			'action' : ACC.config.contextPath + "/templates/editCreatedTemplate",
			'id' : 'editTemplate',
			'method' : 'POST'
		}).append(jQuery('<input>', { 
			'name' : 'tempProdQtys',
			'value' : editTempProd_Qtys,
			'type' : 'hidden'
		})).append(jQuery('<input>', { 
			'name' : 'shareStatus',
			'value' : $('#shareWithAccountUsers').is(':checked'),
			'type' : 'hidden'
		})).append(jQuery('<input>', { 
			'name' : 'tempCode',
			'value' : $('#hddnEditTemplateNumber').val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { 
			'name' : 'tempName',
			'value' : $('#templateName').val(),
			'type' : 'hidden'
		})).append(jQuery('<input>', { // Account GLN input created 
	    	'name' : 'CSRFToken',
	    	 'value' : ACC.config.CSRFToken,
	    	 'type' : 'hidden' 
		}));
	
		$(".mobile-no-pad").append(editTemplateForm);
		
		editTemplateForm.submit();
		}
		else
		{	
		$("#templateEditForm").submit();
		}
	});
	
	
});
		
