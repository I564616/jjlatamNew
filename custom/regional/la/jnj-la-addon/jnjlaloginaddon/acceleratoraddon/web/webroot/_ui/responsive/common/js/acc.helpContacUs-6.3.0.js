var flag=true;
var btnDisabledStyle = "btn-disabled-style", contactuspopupNew = "#contactuspopupNew",
    contactuspopupNewContactUsSubjectLb = "#contactuspopupNew #contactUsSubjectLb",
    contactuspopupNewContactUsAgree = "#contactuspopupNew #contactus-agree",
    contactuspopupNewContactUsFormSubmitLb = "#contactuspopupNew #contactUsFormSubmitLb",
    contactuspopupNewContactUsFormPage2Lb = "#contactuspopupNew .contactUsFormPage2Lb",
    contactuspopupNewContactUsSubmitBtn = "#contactuspopupNew .contactUsSubmitBtn",
    contactuspopupNewContactUsFormLb = "#contactuspopupNew .contactUsFormLb",
    contactuspopupNewContactUsOrderNumberLb = "#contactuspopupNew .contactUsOrderNumberLb",
    contactuspopupNewContactUsProductNumberLb = "#contactuspopupNew .contactUsProductNumberLb",
    contactHelpAgree = "#contactHelp-agree", helpContactUsFormSubmitLb = "#helpContactUsFormSubmitLb",
    contactuspopupContactUsOrderNumberLb = "#contactuspopup .contactUsOrderNumberLb",
    contactuspopupContactUsProductNumberLb = "#contactuspopup .contactUsProductNumberLb";

ACC.helpContactUs = {
    bindAll: function() {
        ACC.helpContactUs.selectSubject();
        ACC.helpContactUs.bindcontactUspopupLink($('.contactuspopup_hn'));
    },
    selectSubject: function()
    {},
    bindcontactUspopupLink : function(link) {
        link.click(function() {
            $.get(ACC.config.contextPath + "/help/contactUs").done(function(data) {
                $(contactuspopupNew).remove();
                $('#contactuspopupholder').html(data);
                $(contactuspopupNew).modal('show');
                $(contactuspopupNew).on('shown.bs.modal', function () {checkContactUs();});
                $('#contactuspopupNew .contactUsField').keyup(function(){checkContactUs();});
                $(contactuspopupNewContactUsSubjectLb).change(function(){checkContactUs();});
                $(contactuspopupNewContactUsAgree).click(function(){checkContactUs();});
                $(contactuspopupNewContactUsFormSubmitLb).prop('disabled',true);
                ACC5.privacypolicypop.bindAll();

                function checkContactUs(){
                    var mandatoryFieldFlag=true;

                    //checkbox
                    if(!$(contactuspopupNewContactUsAgree).prop('checked')){
                        mandatoryFieldFlag=false;
                    }
                        if(mandatoryFieldFlag){
                            $(contactuspopupNewContactUsFormSubmitLb).prop('disabled',false);
                            $(contactuspopupNewContactUsFormSubmitLb).removeClass(btnDisabledStyle);
                        }
                        else{
                            $(contactuspopupNewContactUsFormSubmitLb).prop('disabled',true);
                            $(contactuspopupNewContactUsFormSubmitLb).addClass(btnDisabledStyle);
                        }
                    }

                    $(contactuspopupNewContactUsFormPage2Lb).hide();
                    $(contactuspopupNewContactUsSubmitBtn).click(function(){
                        var dataObj = new Object();
                        dataObj.captchaResponse = $("#captchaResponse").val();

                        jQuery.ajax({
                            type: "GET",
                            url: ACC.config.contextPath + "/help/validateCaptchaResponse",
                            data: dataObj,
                            success: function (data) {
                                if (data)
                                    {
                                        if($(contactuspopupNewContactUsFormLb).valid()){
                                            $(contactuspopupNewContactUsFormSubmitLb).removeClass(btnDisabledStyle);
                                            $(contactuspopupNewContactUsFormLb).find(".contactUsGlobalError").find(".error").hide();
                                            $(contactuspopupNewContactUsFormLb).ajaxSubmit({
                                                success : function(data)
                                                {
                                                    $(contactuspopupNewContactUsSubmitBtn).hide();
                                                    $(contactuspopupNewContactUsFormLb).hide();
                                                    $(contactuspopupNewContactUsFormPage2Lb).show();
                                                }
                                            });
                                        }else{
                                            $(contactuspopupNewContactUsFormLb).find(".contactUsGlobalError").find(".error").show();
                                        }
                                        ACC.globalActions.menuNdbodyHeight();
                                      }else
                                    {
                                        $('.contactUsErrorCaptcha').html( $("#contactUsCaptchaId").val());
                                        $('.contactUsErrorCaptcha').show();
                                          grecaptcha.reset();
                                    }
                            },
                            error : function(e) {
                            }
                        });
                    });

                    $("#contactuspopupNew .backToContactUsForm").click(function(e){
                        e.preventDefault();

                        $(contactuspopupNewContactUsFormLb).show();
                        $(contactuspopupNewContactUsFormPage2Lb).hide();
                        $("#contactuspopupNew #contactUsMessage").val("");
                        $(contactuspopupNewContactUsSubjectLb).val("");
                        $(contactuspopupNewContactUsSubmitBtn).show();
                        $("#contactuspopupNew #contactUsOrderNumber").val("");
                        $("#contactuspopupNew #contactUsProductNumber").val("");
                        $(contactuspopupNewContactUsOrderNumberLb).css("display","none");
                        $(contactuspopupNewContactUsProductNumberLb).css("display","none");
                        $(contactuspopupNewContactUsFormSubmitLb).addClass(btnDisabledStyle);
                        $(contactuspopupNewContactUsAgree).prop('checked', false);
                        $("#contactuspopupNew #contactUsMessage").css('height',"150px");

                    });


                    $(contactuspopupNewContactUsSubjectLb).change(function(){
                        if($(this).val() === "1") {
                            $(contactuspopupNewContactUsOrderNumberLb).css("display","block");
                            $(contactuspopupNewContactUsProductNumberLb).css("display","none");

                        } else if($(this).val() === "4"){
                            $(contactuspopupNewContactUsProductNumberLb).css("display","block");
                            $(contactuspopupNewContactUsOrderNumberLb).css("display","none");

                        }else{
                            $(contactuspopupNewContactUsOrderNumberLb).css("display","none");
                            $(contactuspopupNewContactUsProductNumberLb).css("display","none");
                        }
                    });
                });
            });
        }
    };

    $(document).ready(function() {
        ACC.helpContactUs.bindAll();

        $(contactHelpAgree).prop('checked',false);
        checkHelpContactUs();
        $(contactHelpAgree).click(function(){checkHelpContactUs();});
        $(helpContactUsFormSubmitLb).prop('disabled',true);
        function checkHelpContactUs(){
            var mandatoryFieldFlag=true;

            //checkbox
            if(!$(contactHelpAgree).prop('checked')){
                mandatoryFieldFlag=false;
            }
            if(mandatoryFieldFlag){
                $(helpContactUsFormSubmitLb).prop('disabled',false);
                $(helpContactUsFormSubmitLb).removeClass(btnDisabledStyle);
            }
            else{
                $(helpContactUsFormSubmitLb).prop('disabled',true);
                $(helpContactUsFormSubmitLb).addClass(btnDisabledStyle);
            }
        }
        $("#contactuspopup #contactUsSubjectLb").change(function(){
            if($(this).val() === "1") {
                $(contactuspopupContactUsOrderNumberLb).css("display","block");
                $(contactuspopupContactUsProductNumberLb).css("display","none");

            } else if($(this).val() === "4"){
                $(contactuspopupContactUsProductNumberLb).css("display","block");
                $(contactuspopupContactUsOrderNumberLb).css("display","none");
            }else{
                $(contactuspopupContactUsOrderNumberLb).css("display","none");
                $(contactuspopupContactUsProductNumberLb).css("display","none");
            }
        });
    });

