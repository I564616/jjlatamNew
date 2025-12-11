$(document).ready(function () {
	$("#j_username").focus(); // Focus on the user-name field
	/** VALIDATION **/
	jQuery("#loginForm").validate({
		rules:{
			j_username:{
				required :true
			},
			j_password:{
				required:true
			}
		},
		errorPlacement: function (error, element) {
			element.parent().parent().find('div.recPassError').html(error);
		},
		onfocusout: false,
		focusCleanup: false
	});
	/** Enter key action on the password field **/
	$("#j_password").on("keypress", function(event){
		if (event.keyCode == 13) {
			$("#loginButton").trigger("click");
		}
	});
	/** Enter key action on the user-name field **/
	$("#j_username").on("keypress", function(event){
		if (event.keyCode == 13 && $("#j_password").val()) {
			$("#loginButton").trigger("click");
		}
	});
	/** click of login button action **/
	$("#loginButton").click(function(){
		
		if($("#loginForm").valid()){
			$('div.logininfo').find('div.recPassError').html("");
			$("#loginForm").submit();
		}
	});
	/**
	 * This method is used to save the user-name on the login page
	 * @author sanchit.a.kumar
	 */
	$("#saveuserChkBox").click(function () {
		/** Checking for the save user-name check-box checked **/
		if ($("#saveuserChkBox:checked").length != 0 && $("#j_username").val() != "") {
			var username = $("#j_username").val();
			/** Setting the user name in the cookie with expiry in 365 days **/
			setCookie("username", username, 365);
		}
	});
});
/**
 * This function sets the cookie
 * @author sanchit.a.kumar
 */
function setCookie(c_name, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
	document.cookie = c_name + "=" + c_value;
}

function setConsentCookie(c_name, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());	//alert("I m here");
	document.cookie = c_name+"="+value+";path=/";
}


/**
 * This method checks if cookie is present for the user name
 * This method is called on load of the account login page body
 * @author sanchit.a.kumar
 */
function checkCookie() {
	var param = getParam('error');
	var cookieconsent = getCookie("cookieconsent");
	
	if (param == "true") {
		document.getElementById("j_username").value = "";
	} else {
		var username = getCookie("username");
		if (username != null && username != "") {
			$("#j_username").val(username);
		} else {
			username = $("#j_username").val();
			if (cookieconsent =="OK") {
			/** Setting the user name in the cookie with expiry in 365 days **/
			if (username != null && username != "") {
				setCookie("username", username, 365);
			}
		}
	}
}
	
	if (cookieconsent == null || cookieconsent=="" || cookieconsent!="OK") {
		openPopup();
	}
	

}

/**
 * This method gets the cookie
 * @param c_name
 * @returns c_value
 * @author sanchit.a.kumar
 */
function getCookie(c_name) {
	
	var c_value = document.cookie;
	var c_start = c_value.indexOf(c_name + "=");
	if (c_start == -1) {
		c_value = null;
	} else {
		c_start = c_value.indexOf("=", c_start) + 1;
		var c_end = c_value.indexOf(";", c_start);
		if (c_end == -1) {
			c_end = c_value.length;
		}
		c_value = unescape(c_value.substring(c_start, c_end));
	}
	return c_value;
}
/**
 * This method creates the regex to get the parameter
 * @param name
 * @returns results
 * @author sanchit.a.kumar
 */

function getParam(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null) {
		return "";
	} else {
		return results[1];
	}
}

function openPopup(){
	$('#cookiepopup').modal('show');
}

$("#accept-btn").click(function() {
	setConsentCookie("cookieconsent", "OK", 365);
	$('#cookiepopup').modal('hide');

});


$(".canceltxt").click(function() {
	$('#cookiepopup').modal('hide');
});