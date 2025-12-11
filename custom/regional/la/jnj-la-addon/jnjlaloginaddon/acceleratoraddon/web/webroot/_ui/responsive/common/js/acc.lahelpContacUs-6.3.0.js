var contactUsOrderNumberLb = ".contactUsOrderNumberLb", contactUsForm = "#contactUsForm",
    contactUsGlobalError = ".contactUsGlobalError", contactuspopupNew = "#contactuspopupNew",
    contactUsSubjectLb = "#contactUsSubjectLb", contactUsAgree = "#contactus-agree",
    contactUsFormSubmitLb = "#contactUsFormSubmitLb", btnDisabledStyle = "btn-disabled-style",
    contactUsFormPage2Lb = ".contactUsFormPage2Lb", contactUsSubmitBtn = ".contactUsSubmitBtn",
    contactUsFormLb = ".contactUsFormLb", contactUsOrderNumber = "#contactUsOrderNumber";

ACC.lahelpContactUs = {

    bindAll: function() {
        ACC.lahelpContactUs.selectSubject();
        ACC.lahelpContactUs.bindcontactUspopupLink($('.contactuspopup_hnLatam'));
    },
    selectSubject: function()
    {
        $(".contactUsFormSubmit").click(function(){
            if ($(contactUsForm).valid()){
                $(contactUsForm).find(contactUsGlobalError).find(".error").hide();
            } else {
                $(contactUsForm).find(contactUsGlobalError).find(".error").show();
            }
            if ($("#contactUsMessageBox").val() !== '' && ($(contactUsOrderNumber).val() === '1' || $("#contactUsSubject").val() !== ''))
            {
                $(contactUsForm).ajaxSubmit({type: 'POST', url: ACC.config.contextPath + "/help/contactUs"});
            }
        });
    },
    bindcontactUspopupLink : function(link) {
        link.click(function() {
            $.get(ACC.config.contextPath + "/help/contactUs").done(function(data) {
                $(contactuspopupNew).remove();
                $('#contactuspopupholder').html(data);
                $(contactuspopupNew).modal('show');
                $(contactuspopupNew).on('shown.bs.modal', function () {checkContactUs();});
                $('.contactUsField').keyup(function(){checkContactUs();});
                $(contactUsSubjectLb).change(function(){checkContactUs();});
                $(contactUsAgree).click(function(){checkContactUs();});
                ACC5.privacypolicypop.bindAll();

                function checkContactUs(){
                    var mandatoryFieldFlag=true;
                    //checkbox
                    if(!$(contactUsAgree).prop('checked')){
                        mandatoryFieldFlag=false;
                    }
                    if (mandatoryFieldFlag) {
                          $(contactUsFormSubmitLb).removeClass(btnDisabledStyle);
                          $(contactUsFormSubmitLb).removeAttr("disabled");
                    }
                    else{
                         $(contactUsFormSubmitLb).addClass(btnDisabledStyle);
                         $(contactUsFormSubmitLb).attr('disabled', 'disabled');
                    }
                }

                $(contactUsFormPage2Lb).hide();
                $(contactUsSubmitBtn).click(function(){
                    checkContactUs();
                if($(contactUsFormLb).valid()){
                        $(contactUsFormLb).find(contactUsGlobalError).find(".error").hide();
                        $('.contactUsFormLb').ajaxSubmit({
                            success : function(data) {
                                $(contactUsSubmitBtn).hide();
                                $(contactUsFormLb).hide();
                                $(contactUsFormPage2Lb).show();
                            }
                        });
                    }else{
                        $(contactUsFormLb).find(contactUsGlobalError).find(".error").show();
                    }
                });

                $(".backToContactUsForm").click(function(e){
                    e.preventDefault();
                    $(contactUsFormLb).show();
                    $(contactUsFormPage2Lb).hide();
                    $("#contactUsMessage").val("");
                    $(contactUsSubjectLb).val("");
                    $(contactUsSubmitBtn).show();
                    $(contactUsOrderNumber).val("");
                    $(contactUsOrderNumberLb).css("display","none");
                    $(contactUsFormSubmitLb).addClass(btnDisabledStyle);
                    $(contactUsAgree).prop('checked', false);
                });

                $(contactUsSubjectLb).change(function() {
                    if($(this).val() === "1") {
                        $(contactUsOrderNumberLb).css("display","block");
                    } else {
                        $(contactUsOrderNumberLb).css("display","none");
                    }
                });
            });
        });
    }
};

$(document).ready(function() {
    ACC.lahelpContactUs.bindAll();
});
