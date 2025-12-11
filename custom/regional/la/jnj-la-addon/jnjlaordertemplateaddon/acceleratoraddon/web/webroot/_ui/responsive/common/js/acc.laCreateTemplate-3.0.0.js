jQuery(document).ready(function () {
    var currViewScreenName = $(".currViewScreenName").val();
	if($('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').length == 0){
	    $("#createOrdersTable-"+currViewScreenName).hide();
	}

	var inCreateTemplatePage = false;
	var createOrderTable = $('#createOrdersTable-desktop').DataTable({
		"bDestroy": true,
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": $("#tableNoData").val()},
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
		"language": {"emptyTable": $("#tableNoData").val()},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	var editOrderTable = $('#editOrdersTable-desktop').DataTable({
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": $("#tableNoData").val()},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	var editOrderTableMobile = $('#editOrdersTable-mobile').DataTable({
		"aaSorting": [],
		"aoColumnDefs": [{'bSortable': false, 'aTargets': ['no-sort']}],
		"language": {"emptyTable": $("#tableNoData").val()},
		'pagingType': "simple",
		"bPaginate": false,
		"bFilter": false,
		"bInfo": false,
		"bDestroy": true
	});
	$("#createTemplate").click(function (e) {
		window.location.href = ACC.config.contextPath + '/templates/createTemplate';
	});

	$('.build-ordr-cancel-btn,.menu-item-name,#jnj-menu-list li,#jnj-logo-holder img').on('click', function (e) {
		if ($(this).find('a').attr('href') != undefined) {
			$('#leave-page-btn').attr('href', $(this).find('a').attr('href'));
		}
		else if ($(this).attr('href') != undefined) {
			$('#leave-page-btn').attr('href', $(this).attr('href'));
		}
		if (inCreateTemplatePage) {
			e.preventDefault();
			$('#items-in-order-popup').modal('show');
		}
	});

	$("#leave-page-btn-template").click(function (e) {
		window.location.href = ACC.config.contextPath + '/templates';
	});

	$("#laCreateAddToTemplate").click(function (e) {
		$("#noTemplateNameMsg").css("display", "none");
		$("#createTemplateMsg").css("display", "none");
		$("#emptyProductCodeMsg").css("display", "none");
		$("#validQuantityMsg").css("display", "none");
		var dataObj = {};
		dataObj.productCode = $("#productNumber").val().trim();
		dataObj.qty = $("#QtyQuantity").val().trim();
		var quantity = $("#QtyQuantity").val().trim();

        if($("#productNumber").val().trim() == null || dataObj.productCode ==""){
        	$("#emptyProductCodeMsg").css("display", "block");
        }
        else{
        	if ($("#QtyQuantity").val().trim() == null || dataObj.qty == "" || dataObj.qty == "0") {
    			$("#validQuantityMsg").css("display", "block");
    		}
    		else{
    			jQuery.ajax({
    				type: "POST",
    				url: ACC.config.contextPath + '/templates/laProductValidate',
    				data: dataObj,
    				success: function (data) {

    					if (data == "invalid" || data == "inactive" || data == "restricted") {
    						$("#createTemplateMsg").css("display", "block");
    					}
    					else {
    						var splitedDetails = data.split("$$$");
    						if (splitedDetails[2] == null) {
    							splitedDetails[2] = "unit";
    						}

    						if (($('#' + $("#productNumber").val().trim()).length) >= 1) {
    							$('#createOrdersTable-'+currViewScreenName+' tr').each(function (i) {
    								if (i > 0) {
    									var prdCode = $(this).find(".product-code").html().trim();
    									var prdNumb = $("#productNumber").val().trim();
    									if (prdCode === prdNumb) {
    										$(this).find('#addProdQnty').val(function (i, oldval) {
    											var a = parseInt(oldval, 10);
    											var b = parseInt($('#QtyQuantity').val(), 10);

    											oldval = a + b;
    											$('#productNumber').val('');
    											$('#QtyQuantity').val('');
    											return oldval;
    										});
    									}
    								}
    							});
    						}
    						else{
    	                        if(currViewScreenName === 'desktop'){
        							$("#createOrdersTable-"+currViewScreenName).show();
        							createOrderTable.row.add([
                                        '<span class="product-code" id='+splitedDetails[0]+'>' + splitedDetails[0] + '</span>',
                                        splitedDetails[1],
    								    "<input type='text' class='form-control txtWidth' value=" + quantity+ " id='addProdQnty'><p class='margintop10'>"+$("#unit_latam").val() + splitedDetails[4]+"("+splitedDetails[2] +splitedDetails[4]+")</p></td>",
                                            '<a href="#" id="deleteCreateTemplateProdLatam">' +$("#delete_latam").val() +'</a>'
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
        									+'<p><a href="#" id="deleteCreateTemplateProd">'+$("#delete_latam").val()+'</a></p>'
        									+'</div></div></td></tr>'
        							]).draw();
        						}
    							$('#productNumber').val('');
    							$('#QtyQuantity').val('');
    						}
    						if ($('.desk-create-template-table-'+currViewScreenName+' tbody').find('tr td').length > 1) {
                            	inCreateTemplatePage = true;
                          	}

    						if($('#createOrdersTable-'+currViewScreenName).find('td:first').hasClass('dataTables_empty')==false){
    							var rowCount = $('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').length;
    						}
    						if(rowCount>=1 ){
    							$("#createNewTemplate").removeAttr("disabled");
    							$("#createNewTemplate2").removeAttr("disabled");
    						}
    					}
    				}
    			});
    		}
        }
	});

	$(document).on('click', 'a', function () {
		createOrderTable.row( $(this).parents('tr') ).remove().draw();
		var rowCount=0;
		if($('#createOrdersTable-'+currViewScreenName).find('td:first').hasClass('dataTables_empty')==false){
		 rowCount = $('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').length;
		}
		if(rowCount <2){
			$("#createNewTemplate").attr("disabled", "disabled");
			$("#createNewTemplate2").attr("disabled", "disabled");
		}
	});

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

	$(document).on("click", '#createNewTemplate,#createNewTemplate2', function () {
		$("#noTemplateNameMsg").css("display", "none");
		if($('#templateName').val() != ""){
		    var tempProd_Qtys = "";
		    $('#createOrdersTable-'+currViewScreenName+' > tbody  > tr').each(function(i) {
		        var $this = $(this)
		        var value = $this.find("span.product-code").html();
		        var quantity = $this.find("input#addProdQnty").val();

		        if(i==0){
		            tempProd_Qtys = value + ":" + quantity;
		        }
		        else{
		            tempProd_Qtys += "," + value + ":" + quantity;
		        }
		    });

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
		    })).append(jQuery('<input>', {
                 'name' : 'CSRFToken',
                 'value' : ACC.config.CSRFToken,
                 'type' : 'hidden'
            }));

		    $(".mobile-no-pad").append(createTemplateForm);
		    createTemplateForm.submit();
		}
	    else{
		    $("#noTemplateNameMsg").css("display", "block");
	    }
	});


	$(".editTemplatebtn").click(function (e) {
		window.location.href = ACC.config.contextPath + '/templates/editTemplate/' + $("#hddnTemplateNumber").val();
	});

	$("#laEditAddToTemplate").click(function (e) {
		$("#editTemplateMsg").css("display", "none");
		$("#validQuantityMsg").css("display", "none");
		dataObj.productCode = $("#editProductNumber").val().trim();
		dataObj.qty = $("#editQuantity").val().trim();
		var quantity = $("#editQuantity").val().trim();

		if ($("#editQuantity").val().trim() == null || dataObj.qty == "" || dataObj.qty == "0") {
			$("#validQuantityMsg").css("display", "block");
		}else{
			jQuery.ajax({
				type: "POST",
				url: ACC.config.contextPath + '/templates/laProductValidate',
				data: dataObj,
				async:false,
				success: function (data) {
					if (data == "inactive" || data == "invalid" || data == "restricted") {
						$("#editTemplateMsg").css("display", "block");
					}else {
						var splitedDetails = data.split("$$$");
						if (($('#' + $("#editProductNumber").val().trim().toUpperCase()).length) >= 1) {
							$('#editOrdersTable-'+currViewScreenName+' tr').each(function (i) {

	                            if (i > 0 && $(this).find(".editProductCode").html().trim() == $("#editProductNumber").val().trim().toUpperCase()) {

	                                $(this).find('#prodQnty').val(function (i, oldval) {
	                                    var a = parseInt(oldval, 10);
	                                    var b = parseInt($('#editQuantity').val(), 10);

	                                    oldval = a + b;
	                                    $('#editProductNumber').val('');
	                                    $('#editQuantity').val('');
	                                    return oldval;
	                                });
	                            }
							});
						}else {
							editOrderTable.row.add([
								'<span class="editProductCode" id=' + splitedDetails[0] + '>' + splitedDetails[0] + '</span>',
								splitedDetails[1],
								"<input type='text' class='form-control txtWidth' value=" + quantity + " id='prodQnty'><p class='margintop10'>"+$("#unit_latam").val()+splitedDetails[4]+"("+splitedDetails[2] +splitedDetails[4]+")</p></td>",
								'<a href="#" class="deleteTemplateProd">'+$("#delete_latam").val()+'</a>'
							]).draw();
							$('#editProductNumber').val('');
							$('#editQuantity').val('');
							return false;
						}
					}
				}
			});
		}
		inCreateTemplatePage = true;
	});

	var editTempProd_Qtys=null;

	$(document).on("click", '#lasaveEditedTemplate', function () {
		if($('#templateName').val() != ""){
		    var rowCount=0;
		    if($('#editOrdersTable-'+currViewScreenName).find('td:first').hasClass('dataTables_empty')==false){
		    	var rowCount = $('#editOrdersTable-'+currViewScreenName+' > tbody  > tr').length;
		    }
    		if(rowCount>=1 ){
	    	    $('#editOrdersTable-'+currViewScreenName+' > tbody  > tr').each(function(i) {
		            var $this = $(this)
		            var value = $this.find("span.editProductCode").html();
		            var quantity = $this.find("input#prodQnty").val();
	    	        if(i==0){
		    	        editTempProd_Qtys =value+":"+ quantity;
			        }else{
			            editTempProd_Qtys+=","+value+":"+ quantity;
    			    }
	    	    });

		        var obj = new Object();
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
		        })).append(jQuery('<input>', {
                    'name' : 'CSRFToken',
                    'value' : ACC.config.CSRFToken,
                    'type' : 'hidden'
                }));

		        $(".mobile-no-pad").append(editTemplateForm);
		        editTemplateForm.submit();
	        }else{
		        $("#templateEditForm").submit();
            }
        }else{
		    $("#noTemplateNameMsg").css("display", "block");
	    }
    });

	$('#QtyQuantity,#editQuantity').keyup(function() {
        if (this.value.match(/[^0-9 ]/g)) {
		   this.value = this.value.replace((/[^0-9 ]/g), '');
		}
    });

	$('#prodQnty').keyup(function() {
        if (this.value.match(/[^1-9 ]/g)) {
		    this.value = this.value.replace((/[^1-9 ]/g), '');
		}
    });
});