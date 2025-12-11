$(document).ready(function() {

    var isFirstTimeModal = ($(".accountListPopUp").size() > 0 && $('#updatedLegalPolicy').val() != "false");

    searchBtnClick();

    function searchBtnClick() {

        $("#searchSelectAccountBtnHeader").on('click', function() {
            if (null != $("#changeAccountNoAjax").val() && $.trim($("#changeAccountNoAjax").val()) != "") {
                var dataObj = {};
                dataObj.searchTerm = $.trim($("#changeAccountNoAjax").val());
                createChangeAccountAjaxCall(dataObj, "");
            }
        });

        var anchrClicked = false;
        $('#select-accnt-error').hide();
        $('#selectaccountpage .anchorcls').on('click', function() {
            anchrClicked = true;
            $('#select-accnt-error').hide();
            $('.anchorcls').each(function() {
                $(this).removeClass('anchor-selected');
            });
            $(this).addClass('anchor-selected');
            changeAcntList();
        });


        $('#change-select-btn-latam').click(function() {
            loadingCircleShow("show");
            if (anchrClicked) {
                $("#changeAccountForm").submit();
            } else {
                $('#select-accnt-error').show();
            }
        });


        $(".change-accnt-link").on('click', function() {
            changeAcntList();
        });
    }

    function createChangeAccountAjaxCall(dataObj, pageNumber) {

        if (null != pageNumber && pageNumber != "") {
            dataObj.showMoreCounter = pageNumber;
            dataObj.showMore = "true";
        } else {
            dataObj.showMoreCounter = "1";
            dataObj.showMore = "false";
        }
        dataObj.showMode = "Page";
        dataObj.page = "0";

        if (isFirstTimeModal) {
            dataObj.addCurrentB2BUnit = "true";
        }

        jQuery.ajax({
            type: "POST",
            data: dataObj,
            url: ACC.config.contextPath + ACC.config.currentLocale + '/home/getAccounts',
            success: function(data) {

                $('#selectaccountpage').html(data);

                $('#select-accnt-error').hide();
                $('#selectaccountpopup').modal('hide');
                $('#selectaccountpopup').modal('show');
                $('.clsBtn').click(function() {
                    $('.modal-backdrop').hide();
                });

                searchBtnClick();

                var totalPages = $("#accountNumberOfPages").val();
                $("#changeAccountPopupContainer").show();
                $.colorbox.resize();
                $("#changeAccountNoAjax").change(function() {
                    if ((null == $("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val()) == "") &&
                        $(this).val() != null && $.trim($(this).val()) != "") {
                        $("#clearSelectAccountBtn").attr("disabled", false);
                        $("#clearSelectAccountBtn").removeClass("tertiarybtn");
                        $("#clearSelectAccountBtn").addClass("primarybtn");
                    } else if ((null == $("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val()) == "") &&
                        ($(this).val() == null || $.trim($(this).val()) == "")) {
                        $("#clearSelectAccountBtn").attr("disabled", true);
                        $("#clearSelectAccountBtn").removeClass("primarybtn");
                        $("#clearSelectAccountBtn").addClass("tertiarybtn");
                    }
                });
                if (isFirstTimeModal) {
                    $('#cboxClose').attr("style", "display:none;");
                }

                if (totalPages > 1) {
                    $("a.loadMoreAccounts").attr("style", "display:block;text-align:center");
                    $("a.loadMoreAccounts").click(function() {
                        var searchTerm = $("#accountSearchTerm").val();
                        var loadMoreObject = {};
                        if (null != searchTerm && searchTerm != "") {
                            loadMoreObject.searchTerm = searchTerm;
                        }
                        createChangeAccountAjaxCall(loadMoreObject, parseInt($("#currentPage").val(), 10) + 1);
                    });
                    $.colorbox.resize();
                } else {
                    $("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
                }
                $(".clearSelectAccountBtn").unbind();
            },

            error: function(x, e) {
                var status = "" + x.status;
                if (status.startsWith("9") || x.status == 901) {
                    window.location.href = ACC.config.contextPath + "/";
                }
            }

        });
    }


    changeAcntList();

    function changeAcntList() {
        $('.accountListPopUp').each(function() {

            $(this).click(function() {
                /** Creating dummy form for submission for account change request **/
                var changeAccountForm = jQuery('<form:form>', {
                    'action': ACC.config.contextPath + '/home/changeAccount',
                    'id': 'changeAccountForm',
                    'method': 'POST'
                }).append(jQuery('<input>', { //  Account UID input created
                    'name': 'uid',
                    'value': $($(this).find("#accountUID:input[type='hidden']")).val(),
                    'type': 'hidden'
                })).append(jQuery('<input>', { //  Account Name input created
                    'name': 'accountName',
                    'value': $($(this).find("#accountName:input[type='hidden']")).val(),
                    'type': 'hidden'
                })).append(jQuery('<input>', { //  Account GLN input created
                    'name': 'accountGLN',
                    'value': $($(this).find("#accountGLN:input[type='hidden']")).val(),
                    'type': 'hidden'
                })).append(jQuery('<input>', { //  Account GLN input created
                    'name' : 'CSRFToken',
                    'value' : ACC.config.CSRFToken,
                    'type' : 'hidden'
     			}));
                $("#changeAccountPopup").append(changeAccountForm);
            });
        });
    }
});

$(document).ready(function() {

    if ($(".accountListPopUp").size() > 0 && $('#updatedLegalPolicy').val() != "false") {

        $('#cboxClose').attr("style", "display:none;");

        $("#changeAccountNoAjax").change(function() {
            if ((null == $("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val()) == "") &&
                $(this).val() != null && $.trim($(this).val()) != "") {
                $("#clearSelectAccountBtn").attr("disabled", false);
                $("#clearSelectAccountBtn").removeClass("tertiarybtn");
                $("#clearSelectAccountBtn").addClass("primarybtn");
            } else if ((null == $("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val()) == "") &&
                ($(this).val() == null || $.trim($(this).val()) == "")) {
                $("#clearSelectAccountBtn").attr("disabled", true);
                $("#clearSelectAccountBtn").removeClass("primarybtn");
                $("#clearSelectAccountBtn").addClass("tertiarybtn");
            }
        });
        if ($("#accountNumberOfPages").val() > 1) {
            $("a.loadMoreAccounts").attr("style", "display:block;text-align:center");
            $("a.loadMoreAccounts").click(function() {
                var searchTerm = $("#accountSearchTerm").val();
                var loadMoreObject = {};
                if (null != searchTerm && searchTerm != "") {
                    loadMoreObject.searchTerm = searchTerm;
                }
                createChangeAccountAjaxCall(loadMoreObject, parseInt($("#currentPage").val(), 10) + 1);
            });
            $.colorbox.resize();
        } else {
            $("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
        }

        $(".clearSelectAccountBtn").click(function() {
            $("#changeAccountNoAjax").val("");
        });
    }
});

var currentLocale = $("#countryCode").val();

$("#datePicker1").datepicker(addFormat({
    endDate: '0'
}));

$("#datePicker2").datepicker(addFormat({
    endDate: '0'
}));

$("#datePicker1").datepicker().on('changeDate', function(ev){
    hideDateError("#datePicker1");
    if ($("#datePicker2").val() != ''){
        var thisDate = $(this).datepicker('getDate');
        var otherDate = $("#datePicker2").datepicker('getDate');
        if (thisDate > otherDate){
            showDateError("#datePicker1");
        }
	}
});

$("#datePicker2").datepicker().on('changeDate', function(ev){
    hideDateError("#datePicker2");
    if ($("#datePicker1").val() != ''){
        var thisDate = $(this).datepicker('getDate');
        var otherDate = $("#datePicker1").datepicker('getDate');
        if (thisDate < otherDate){
            showDateError("#datePicker2");
        }
	}
});

$("#searchByInput").keypress(function (event) {
   enterPressToClickEvent(event, '#ordHistorySearch');
});

$("#ordHistorySearch").hover(function(){
    callCheckDates();
});

function callCheckDates(){
    var startDate = $("#datePicker1").val();
    var endDate = $("#datePicker2").val();
    var startDateArray = [];
    var endDateArray = [];

    if (startDate) {
        startDateArray = startDate.split('/');
    }
    if (endDate){
        endDateArray = endDate.split('/');
    }

    if (startDateArray.length > 2 && '' != startDate){
        checkDates("#datePicker1", startDateArray);
    }
    else if ('' != startDate){
        showDateError("#datePicker1");
    }

    if (endDateArray.length > 2 && '' != endDate){
        checkDates("#datePicker2", endDateArray);
    }
    else if ('' != endDate){
        showDateError("#datePicker2");
    }
}

function checkDates(datePicker, date){
    var day = date[0];
    var month = date[1];
    if (currentLocale == "en"){
        day = date[1];
        month = date[0];
    }
    if (currentLocale == "es" || currentLocale == "pt"){
        day = date[0];
        month = date[1];
    }

    if (day > 31 || day < 1 || month < 1 || month > 12){
        showDateError(datePicker);
    }
}

function showDateError(datePicker){
    $(datePicker).val("");
    $(datePicker+"Error").removeClass("d-none");
    $(datePicker).css('border-color', '#CC0000');
    $(datePicker+"Icon").css('border-color', '#CC0000');
}

function hideDateError(datePicker){
    $(datePicker+"Error").addClass("d-none");
    $(datePicker).css('border-color', '');
    $(datePicker+"Icon").css('border-color', '');
}

function enterPressToClickEvent(event, buttonId){
	if (event.which == '13') {
		$(buttonId).click();
	}
}

$(document).ready(function(){
	
	  $('.downloadbuttons .pdf').click(function(){
		  var obj = new Object();
			obj.show = "All";
			obj.downloadType="PDF";
			var detailDownloadForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/download/"+$("span.templateNumber").text(),
				'id' : 'detailFormPDFDownload',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'show',
				'value' : obj.show,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'downloadType',
				'value' : obj.downloadType,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
			    'name' : 'CSRFToken',
			    'value' : ACC.config.CSRFToken,
			    'type' : 'hidden'
				}));
			$(".sectionBlock").append(detailDownloadForm);
			/** Submitting the form **/
			detailDownloadForm.submit();
		});
	  
	  $('.downloadbuttons .excel').click(function(){
		  var obj = new Object();
		  obj.show = "All";
			obj.downloadType="EXCEL";
			var detailDownloadForm = jQuery('<form>', {
				'action' : ACC.config.contextPath + "/templates/templateDetail/download/"+$("span.templateNumber").text(),
				'id' : 'detailFormExcelDownload',
				'method' : 'POST'
			}).append(jQuery('<input>', { 
				'name' : 'show',
				'value' : obj.show,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
				'name' : 'downloadType',
				'value' : obj.downloadType,
				'type' : 'hidden'
			})).append(jQuery('<input>', { 
			    'name' : 'CSRFToken',
			    'value' : ACC.config.CSRFToken,
			    'type' : 'hidden'
				}));
			$(".sectionBlock").append(detailDownloadForm);
			/** Submitting the form **/
			detailDownloadForm.submit();
		});
  });