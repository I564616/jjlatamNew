$(".contactUsFormSubmitLb").click(function() {
    checkContactUs();
    if ($(".contactUsFormLb").valid()) {
        $(".contactUsFormLb").ajaxSubmit({
            type: 'post',
            url: ACC.config.contextPath + "/help/contactUs"
        });
        $("#contactUsFormPage2Lbs").css("display", "block");

    } else {
        $(".contactUsFormLb").find(".contactUsGlobalError").find(".error").show();
    }

});

function checkContactUs() {
    var mandatoryFieldFlag = true;
    //checkbox
    if ($('#contactus-agree').prop('checked') == false) {
        mandatoryFieldFlag = false;
    }
    if (mandatoryFieldFlag == true) {
        $('.contactUsFormSubmitLb').removeClass('btn-disabled-style');
        $('.contactUsFormSubmitLb').removeAttr("disabled");
    } else {
        $('.contactUsFormSubmitLb').addClass('btn-disabled-style');
        $('.contactUsFormSubmitLb').attr('disabled', 'disabled');
    }

}

$('#contactus-agree').click(function() {
    checkContactUs();
});


function setDefaultFields(checkedRadio) {
    var count = 0;
    var accountList = "";
    var loggedUserAccounts = "";
    var selectedGroups = $("#hddnAccountsString").val();
    loggedUserAccounts = $("#hddnloggedUserAccountList").val();
    if (loggedUserAccounts !== null && loggedUserAccounts !== undefined) {
        $(loggedUserAccounts.split(",")).each(function() {
            var account = $.trim(this + "");
            if (accountList.length > 0) {
                accountList += "," + account;
            } else {
                accountList += account;
            }
            count++;
        });
    }

    if (count != 0) {

        accountList = accountList.substring(1, accountList.length - 1);
    }

    $(".multiAccountSelection").each(function() {
        var accountId = $(this).attr('value');
        var checkId = $(this).attr('id');
        if (selectedGroups !== null && selectedGroups !== undefined) {
            if ($.inArray(accountId, selectedGroups.split(",")) !== -1) {
                $("#" + checkId).prop('checked', true);
            } else {
                $("#" + checkId).prop('checked', false);
            }
        }

        if (accountList !== null && accountList !== undefined && $.inArray(accountId, accountList.split(",")) === -1) {
            var id = $(this).parent().parent().parent().attr('id');
            var isChecked = $("#" + id + " #" + checkId).prop('checked');
            if (!isChecked) {
                $("#" + id).hide();
            }
            $(this).addClass("opaque");
            $(this).find(".multiAccountSelection").removeClass("multiAccountSelection");
        }
    });

    $(".grpAccount").each(function(i, v) {
        if ((i % 2) === 0) {
            $(this).addClass('even').removeClass('odd');
        } else {
            $(this).addClass('odd').removeClass('even');
        }
    });

    if (selectedGroups !== undefined && selectedGroups != null) {
        selectedGroups.split(",").forEach(function(group){
            $(".account_" + group).show();
        });
    }

}


jQuery.validator.addMethod("lettersonlyNew", function(value, element) {
    return this.optional(element) || /^[\u00BF-\u1FFF\u2C00-\uD7FF\A-Za-z !@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+$/i.test(value);
}, $("#name_latam").val());


jQuery("#createNewProfileForm").validate({
    rules: {
        firstName: {
            lettersonlyNew: true
        },
        lastName: {
            lettersonlyNew: true
        }
    }
});

jQuery("#editUserProfileForm").validate({
    rules: {
        firstName: {
            lettersonlyNew: true
        },
        lastName: {
            lettersonlyNew: true
        }
    }

});

function validateRolesAndGroupsFields() {

    var roleFlag = false;
    var accountFlag = false;
    $(".userRoleList").each(function() {
        if ($(this).is(':checked')) {
            roleFlag = true;
            return false;
        }
    });

    if (!roleFlag) {
        $('#roleMsg').html('<label class="error">'+$('#noRoleMessage').val()+'</label>');
    }

    $(".multiAccountSelection").each(function() {
        if ($(this).is(':checked')) {
            accountFlag = true;
            return false;
        }
    });

    if (!accountFlag) {
        $('#accountMsg').html('<label class="error">'+$('#noAccountMessage').val()+'</label>');
    }

    return (roleFlag && accountFlag ? true : false);
}

$(".userStatusChangelatam").click(function() {
    var emailAddress = $("#userId").val();
    var ids = $(this).attr("data");
    $("#status").val(ids);
    if (ids == 'Disabled') {
        status = true;
    } else {
        status = false;
    }
    jQuery.ajax({
        type: 'POST',
        url: ACC.config.contextPath + '/resources/usermanagement/enableOrDisableUser?status=' + status + '&&emailAddress=' + emailAddress,
        async: false,
        success: function(data) {
            window.location.href = ACC.config.contextPath + '/resources/usermanagement/edit?user=' + emailAddress;
        },
        error: function(e) {}
    });

});

$("#laCreateProfileButton").click(function() {

    var numberOfGrpSelected = 0;
    var currSelectedGroups = [];

    var prevSelectedGroups = $("#hddnAccountsString").val();

    $(".multiAccountSelection").each(function() {
        if ($(this).is(':checked')) {
            currSelectedGroups.push($(this).attr('value'))
            numberOfGrpSelected++;
        }
    });
    $("#hddnAccountsString").val(currSelectedGroups);

    $("#createNewProfileFormError").hide();
    var emailAddress = $("#email").val();

    var dataObj = new Object();
    dataObj.email = emailAddress; {
        jQuery.ajax({
            type: 'POST',
            url: ACC.config.contextPath + '/resources/usermanagement/isUidExists',
            async: false,
            data: dataObj,
            success: function(data) {
                if (!data) {
                    var validForm = $("#createNewProfileForm").valid();
                    var validRolesAndAccounts = validateRolesAndGroupsFields();
                    if (validForm && validRolesAndAccounts) {
                        $("#createNewProfileForm").submit();
                    } else {
                        $("#email-error").show();
                    }
                } else {
                    $(".duplicateError").text($("#hiddenMsgValue").val());
                    $("#emailLogin-error").text("");
                }

            },
            error: function(e) {

            }
        });
    }

});

$(".laUpdateProfileButton").click(function(){
	var numberOfGrpSelected = 0;
	var currSelectedGroups = [];
	var prevSelectedGroups = $("#hddnAccountsString").val();
	$(".multiAccountSelection").each( function() {
		if($(this).is(':checked')) {
			currSelectedGroups.push($(this).attr('value'))
			numberOfGrpSelected++;
		}
	});
	$("#hddnAccountsString").val(currSelectedGroups);
	//newly added for multiple accounts in sprint-2 end
	var emailAddress = $("#emailLogin").val();
	var existingEmail =  $("#existingEmail").val()

	if(emailAddress == existingEmail ){
	    var validForm = $("#editUserProfileForm").valid();
        var validRolesAndAccounts = validateRolesAndGroupsFields();
        if (validForm && validRolesAndAccounts) {
			$("#editUserProfileForm").submit();
		}else{
			$("#emailLogin").next().text("");
		}
		return;
	}

	var dataObj = new Object();
	dataObj.email = emailAddress;
	jQuery.ajax({
        type: 'POST',
        url: ACC.config.contextPath +'/resources/usermanagement/isUidExists',
        async: false,
        data: dataObj,
        success: function (data) {
            if(!data){
                if($("#editUserProfileForm").valid()){
                    $("#editUserProfileForm").submit();
                }else{
                    $(".duplicateError").text("");
                }
            }
            else{
                $(".duplicateError").text($("#hiddenMsgValue").val());
                $("#emailLogin-error").text("");
            }
        },
        error: function (e) {

        }
    });

});