$(document).ready(function() {

   $(".changeAccount").click(function() {

        var dataObj = new Object();
        createChangeAccountAjaxCall(dataObj, "");
    });


   searchBtnClick();
   function searchBtnClick(){

    $("#searchSelectAccountBtnHeader").on('click',function(){
        if(null!=$("#changeAccountNoAjax").val() && $.trim($("#changeAccountNoAjax").val())!="") {
            var dataObj = new Object();
            dataObj.searchTerm = $.trim($("#changeAccountNoAjax").val());
            createChangeAccountAjaxCall(dataObj, "");
        }
    });

    var anchrClicked=false;
    $('#select-accnt-error').hide();
    $('#selectaccountpage .anchorcls').on('click',function(){
        anchrClicked=true;
        $('#select-accnt-error').hide();
        $('.anchorcls').each(function(){
            $(this).removeClass('anchor-selected');
        });
        $(this).addClass('anchor-selected');
        changeAcntList();
    });


    $('#change-select-btn-latam').click(function(){
        loadingCircleShow("show");
        if(anchrClicked){
            $("#changeAccountForm").submit();
        }
        else{
            $('#select-accnt-error').show();
        }
    });

    $(".change-accnt-link").on('click',function(){
        changeAcntList();
    });
   }



    function changeAccountOnCompleteAction() {
        $("#changeAccountNoAjax").keypress(function(e){
            var key = e.which;
            if(key == 13){// the enter key code
                //$("#searchSelectAccountBtn").click();
            }
        });

        /** change account functionality on click of link **/

    }

    function createChangeAccountAjaxCall(dataObj, pageNumber) {

        if(null!=pageNumber && pageNumber!="") {
            dataObj.showMoreCounter = pageNumber;
            dataObj.showMore = "true";
        }else{
            dataObj.showMoreCounter = "1";
            dataObj.showMore = "false";
        }
        dataObj.showMode = "Page";
        dataObj.page = "0";
        var escapeKeyHide = false;
        var overlayHide = false;

        if(isFirstTimeModal) {
            dataObj.addCurrentB2BUnit = "true";
            escapeKeyHide = false;
            overlayHide = false;
        }



        function getAccounts(){}
        jQuery.ajax({
            type : "POST",
            data : dataObj,
            url : ACC.config.contextPath + ACC.config.currentLocale + '/home/getAccounts',
            success : function(data) {

                    $('#selectaccountpage').html(data);

                    $('#select-accnt-error').hide();
                     $('#selectaccountpopup').modal('hide');
                      $('#selectaccountpopup').modal('show');
                      $('.clsBtn').click(function(){
                          $('.modal-backdrop').hide();
                      });

                     setTimeout(function(){ searchBtnClick();},3000);


                /** Creating the markup for the popup **/
                /*$.colorbox({
                    html : data,
                    height : 'auto',
                    width : '565px',
                    escKey : escapeKeyHide, // Disable escape key close
                    overlayClose : overlayHide, // Disable overlay click close
                    onComplete : function() {*/
                        var totalPages = $("#accountNumberOfPages").val();
                        $("#changeAccountPopupContainer").show();
                        //$.colorbox.resize();
                        $("#changeAccountNoAjax").change(function(){
                            if((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
                                && $(this).val()!=null && $.trim($(this).val())!=""){
                                $("#clearSelectAccountBtn").attr("disabled", false);
                                $("#clearSelectAccountBtn").removeClass("tertiarybtn");
                                $("#clearSelectAccountBtn").addClass("primarybtn");
                            } else if ((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
                                    && ($(this).val()==null || $.trim($(this).val())=="")){
                                $("#clearSelectAccountBtn").attr("disabled", true);
                                $("#clearSelectAccountBtn").removeClass("primarybtn");
                                $("#clearSelectAccountBtn").addClass("tertiarybtn");
                            }
                        });
                        if(isFirstTimeModal){
                            /** Hiding the close button on the colorbox **/
                            $('#cboxClose').attr("style", "display:none;");
                            //changeAccountOnCompleteAction(true);
                        } else {
                            //changeAccountOnCompleteAction(false);
                        }
                        if(totalPages>1){
                            $("a.loadMoreAccounts").attr("style", "display:block;text-align:center");
                            $("a.loadMoreAccounts").click(function(){
                                var searchTerm = $("#accountSearchTerm").val();
                                var loadMoreObject = new Object()
                                if(null!=searchTerm && searchTerm!=""){
                                    loadMoreObject.searchTerm = searchTerm;
                                }
                                createChangeAccountAjaxCall(loadMoreObject, parseInt($("#currentPage").val(), 10) + 1);
                            });
                            $.colorbox.resize();
                        } else {
                            $("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
                        }
                        /** Clear button click action **/
                        $(".clearSelectAccountBtn").click(function(){
                            //createChangeAccountAjaxCall(new Object(), "");
                        });


                    /*	$(".change-accnt-link").on('click',function(){
    changeAcntList();
                        });*/
                        /*changeAcntList();
    function changeAcntList(){

        $('.accountListPopUp').each(function() {

            $(this).click(function() {
                                    *//** Creating dummy form for submission for account change request **//*
                var changeAccountForm = jQuery('<form>', {
                    'action' : ACC.config.contextPath + '/home/changeAccount',
                    'id' : 'changeAccountForm',
                    'method' : 'POST'
                }).append(jQuery('<input>', { //  Account UID input created
                    'name' : 'uid',
                    'value' : $($(this).find("#accountUID:input[type='hidden']")).val(),
                    'type' : 'hidden'
                })).append(jQuery('<input>', { //  Account Name input created
                    'name' : 'accountName',
                    'value' : $($(this).find("#accountName:input[type='hidden']")).val(),
                    'type' : 'hidden'
                })).append(jQuery('<input>', { //  Account GLN input created
                    'name' : 'accountGLN',
                    'value' : $($(this).find("#accountGLN:input[type='hidden']")).val(),
                    'type' : 'hidden'
                }));

                $("#changeAccountPopup").append(changeAccountForm);
                                    *//** Submitting the dummy form **//*

            });
        });

                        }*/
                        /*
                        $('#change-select-btn').click(function(){
                            alert('clicked');
                            if(anchrClicked){
        $("#changeAccountForm").submit();
                                anchrClicked=false;
                            }
                            else{
                                $('#select-accnt-error').show();
                            }

                        });*/
                    /*}
                });*/
            },

            error:function(x,e){
                /** JJEPIC-313 push to login page when session times out **/
                var status = ""+x.status;
                if(status.startsWith("9") || x.status == 901) {
                    window.location.href=ACC.config.contextPath+"/";
                }
            }

        });
    }


    changeAcntList();
    function changeAcntList(){

        $('.accountListPopUp').each(function() {

            $(this).click(function() {
                /** Creating dummy form for submission for account change request **/
                var changeAccountForm = jQuery('<form>', {
                    'action' : ACC.config.contextPath + '/home/changeAccount',
                    'id' : 'changeAccountForm',
                    'method' : 'POST'
                }).append(jQuery('<input>', { //  Account UID input created
                    'name' : 'uid',
                    'value' : $($(this).find("#accountUID:input[type='hidden']")).val(),
                    'type' : 'hidden'
                })).append(jQuery('<input>', { //  Account Name input created
                    'name' : 'accountName',
                    'value' : $($(this).find("#accountName:input[type='hidden']")).val(),
                    'type' : 'hidden'
                })).append(jQuery('<input>', { //  Account GLN input created
                    'name' : 'accountGLN',
                    'value' : $($(this).find("#accountGLN:input[type='hidden']")).val(),
                    'type' : 'hidden'
                })).append(jQuery('<input>', { //  Account GLN input created
                       'name' : 'CSRFToken',
                       'value' : ACC.config.CSRFToken,
                       'type' : 'hidden'
                }));
                $("#changeAccountPopup").append(changeAccountForm);
                /** Submitting the dummy form **/

            });
        });
    }
   });

   var isFirstTimeModal = ($(".accountListPopUp").size() > 0 && $('#updatedLegalPolicy').val() != "false");
   $(document).ready(function() {

    if ($(".accountListPopUp").size() > 0 && $('#updatedLegalPolicy').val() != "false") {



        /** Color box for the change account Popup **/
        /*$.colorbox({
            html : $("#changeAccountPopupContainer").html(),
            height : 'auto',
            width : '565px',
            escKey : false, // Disable escape key close
            overlayClose : false, // Disable overlay click close
            onComplete : function() {*/
                /** Hiding the close button on the colorbox **/
                $('#cboxClose').attr("style", "display:none;");
                $("#changeAccountNoAjax").change(function(){
                    if((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
                        && $(this).val()!=null && $.trim($(this).val())!=""){
                        $("#clearSelectAccountBtn").attr("disabled", false);
                        $("#clearSelectAccountBtn").removeClass("tertiarybtn");
                        $("#clearSelectAccountBtn").addClass("primarybtn");
                    } else if ((null==$("#accountSearchTerm").val() || $.trim($("#accountSearchTerm").val())=="")
                            && ($(this).val()==null || $.trim($(this).val())=="")){
                        $("#clearSelectAccountBtn").attr("disabled", true);
                        $("#clearSelectAccountBtn").removeClass("primarybtn");
                        $("#clearSelectAccountBtn").addClass("tertiarybtn");
                    }
                });
                if($("#accountNumberOfPages").val()>1){
                    $("a.loadMoreAccounts").attr("style", "display:block;text-align:center");
                    $("a.loadMoreAccounts").click(function(){
                        var searchTerm = $("#accountSearchTerm").val();
                        var loadMoreObject = new Object();
                        if(null!=searchTerm && searchTerm!=""){
                            loadMoreObject.searchTerm = searchTerm;
                        }
                        createChangeAccountAjaxCall(loadMoreObject, parseInt($("#currentPage").val(), 10) + 1);
                    });
                    $.colorbox.resize();
                } else {
                    $("a.loadMoreAccounts").attr("style", "display:none;text-align:center");
                }
                //changeAccountOnCompleteAction(true);
                /** Clear button click action **/
                $(".clearSelectAccountBtn").click(function(){
                    $("#changeAccountNoAjax").val("");
                    //$("#searchSelectAccountBtn").click();
                });
        /*	}
        });*/
    }
    /**
     * This function binds the header change account link click event.
     * @author sanchit.a.kumar
     */
    /*$($(".changeAccount").find("a")).click(function() {*/

});