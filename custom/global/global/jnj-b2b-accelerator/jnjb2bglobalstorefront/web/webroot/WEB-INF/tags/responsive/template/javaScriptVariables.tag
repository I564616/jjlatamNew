<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- JS configuration --%>
<script type="text/javascript"> // set vars
		/*<![CDATA[*/
		<%-- Define a javascript variable to hold the content path --%>
		var ACC = { config: {} };
	<c:url value="/"  var="contextPathURL"/>
			<c:set var="contextURL" value="${fn:substring(contextPathURL,0, (fn:length(contextPathURL)-1))}" />
			ACC.config.contextPath = "${contextURL}".replace(/\/;jsessionid.*/,'');
            ACC.config.encodedContextPath = "${contextPath}";
			ACC.config.commonResourcePath = "${commonResourcePath}";
			ACC.config.themeResourcePath = "${themeResourcePath}";
			ACC.config.siteResourcePath = "${siteResourcePath}";
			ACC.config.language = "${language}";
			ACC.config.rootPath = "${siteRootUrl}";
			ACC.config.currentLocale= "${currentLocale}";
			ACC.config.CSRFToken = "${CSRFToken}";
			ACC.config.sessionPollInt = "${sessionPollInt}";
			ACC.config.sessionExpMin = "${sessionExpMin}";
			ACC.config.sessionWarnMin = "${sessionWarnMin}";
			ACC.config.isContract = "${isContract}";
			ACC.config.hjid = "${hjid_Id}";
			//AAOL-4914 To make RecaptchaPublicKey available all over the site 
			ACC.config.RecaptchaPublicKey="${RecaptchaPublicKey}";

	<c:if test="${request.secure}"><c:url value="/search/autocompleteSecure"  var="autocompleteUrl"/></c:if>
	<c:if test="${not request.secure}"><c:url value="/search/autocomplete"  var="autocompleteUrl"/></c:if>
	ACC.autocompleteUrl = '${autocompleteUrl}';

			<c:forEach var="jsVar" items="${jsVariables}">
				<c:if test="${not empty jsVar.qualifier}" >
					ACC.${jsVar.qualifier} = '${jsVar.value}';
				</c:if>
			</c:forEach>
	
		/*]]>*/
</script>

<!-- Changes to remove hard-coding in js files-->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- <input type="hidden" id="hardCodedReplacement" value='<spring:message code="profile.phonenumber"/>">
<input type="hidden" id="atleastsector" value='<spring:message code="user.atleast.sector"/>"> --%>

<input type="hidden" id="productValidCode" value="<spring:message code="register.valid.code"/>">
<input type="hidden" id="uploadfileerror" value="<spring:message code="home.upload.file.error"/>">
<input type="hidden" id="showfiltersUpper" value="<spring:message code="product.show.filters.uppercase"/>">
<input type="hidden" id="hidefiltersUpper" value="<spring:message code="product.hide.filters.uppercase"/>">
<input type="hidden" id="showfiltersLower" value="<spring:message code="product.show.filters.lowercase"/>">
<input type="hidden" id="hidefiltersLower" value="<spring:message code="product.hide.filters.lowercase"/>">
<input type="hidden" id="nodata" value="<spring:message code="reports.no.data"/>">
<input type="hidden" id="reportsLines" value="<spring:message code="reports.lines"/>">
<input type="hidden" id="reportsResults" value="<spring:message code="reports.results"/>">

<input type="hidden" id="items_list_of" value="<spring:message code="items.list.of"/>">

<input type="hidden" id="dataShowMenu" value="<spring:message code="datatable.show.menu"/>">
<input type="hidden" id="secquestion" value="<spring:message code="register.security.questions.select"/>">
<input type="hidden" id="signuppassword" value="<spring:message code="register.signup.password"/>">
<input type="hidden" id="reenterpassword" value="<spring:message code="register.reenter.password"/>">
<input type="hidden" id="complexity" value="<spring:message code="password.forgotPassword.complexity"/>">
<input type="hidden" id="passwordMismatch" value="<spring:message code="profile.changePassword.errormessage.passwordMismatch"/>">
<input type="hidden" id="acceptTerms" value="<spring:message code="register.accept.terms"/>">
<input type="hidden" id="acceptPolicy" value="<spring:message code="register.accept.policy"/>">
<input type="hidden" id="invalidCaptcha" value="<spring:message code="invalid.captcha"/>">
<input type="hidden" id="accordionFields" value="<spring:message code="accordian.mandatory.fields"/>">
<input type="hidden" id="requiredAccount" value="<spring:message code="register.stepTwo.required.account"/>">
<input type="hidden" id="requiredBusiness" value="<spring:message code="register.stepTwo.required.businessType"/>">
<input type="hidden" id="requiredCountry" value="<spring:message code="register.stepTwo.required.country"/>">
<input type="hidden" id="requiredCity" value="<spring:message code="register.stepTwo.required.city"/>">
<input type="hidden" id="requiredAddress1" value="<spring:message code="register.stepTwo.required.billAddressOne"/>">
<input type="hidden" id="requiredState" value="<spring:message code="register.stepTwo.required.state"/>">
<input type="hidden" id="requiredPostalCode" value="<spring:message code="register.stepTwo.required.postalcode"/>">
<input type="hidden" id="requiredZipCode" value="<spring:message code="register.stepTwo.required.zipcode"/>">
<input type="hidden" id="validAccount" value="<spring:message code="register.valid.account"/>">
<input type="hidden" id="requiredWwidDiv" value="<spring:message code="register.wwid.division"/>">
<input type="hidden" id="requiredOpeningOrder" value="<spring:message code="register.opening.order"/>">
<input type="hidden" id="requiredEstAmount" value="<spring:message code="register.stepTwo.required.estimatedamount"/>">
<input type="hidden" id="requiredProduct" value="<spring:message code="register.select.product"/>">
<input type="hidden" id="validPrefix" value="<spring:message code="register.valid.prefix"/>">
<input type="hidden" id="requiredFname" value="<spring:message code="register.stepThree.required.first.name"/>">
<input type="hidden" id="requiredLname" value="<spring:message code="register.stepThree.required.last.name"/>">
<input type="hidden" id="requiredOrgName" value="<spring:message code="register.stepThree.required.org.name"/>">
<input type="hidden" id="requiredDept" value="<spring:message code="register.stepThree.required.department"/>">
<input type="hidden" id="requiredBusinessEmail" value="<spring:message code="register.stepThree.required.email"/>">
<input type="hidden" id="reEnterEmail" value="<spring:message code="register.stepThree.required.reenter.email"/>">
<input type="hidden" id="emailMismatch" value="<spring:message code="register.email.mismatch"/>">
<input type="hidden" id="requiredSuperPhone" value="<spring:message code="register.stepThree.required.super.phone"/>">

<input type="hidden" id="requiredPhone" value="<spring:message code="register.stepThree.required.phone"/>">
<input type="hidden" id="requiredSuperName" value="<spring:message code="register.stepThree.required.super.name"/>">
<input type="hidden" id="requiredSuperEmail" value="<spring:message code="register.stepThree.required.super.email"/>">

<input type="hidden" id="invalidEmail" value="<spring:message code="password.forgotPassword.emailMismatchError"/>">
<input type="hidden" id="passwordMismatch" value="<spring:message code="profile.changePassword.errormessage.passwordMismatch"/>">
<input type="hidden" id="pricegreater" value="<spring:message code="password.product.pricegreater"/>">
<input type="hidden" id="price" value="<spring:message code="password.product.price"/>">
<input type="hidden" id="selectSector" value="<spring:message code="user.select.sector"/>">
<input type="hidden" id="invalidFax" value="<spring:message code="profile.fax.error"/>">
<input type="hidden" id="dateselection" value="<spring:message code="register.date.selection"/>">
<input type="hidden" id="dateFromInvalidRange" value="<spring:message code="register.date.from.invalid.range"/>">
<input type="hidden" id="dateToInvalidRange" value="<spring:message code="register.date.to.invalid.range"/>">
<input type="hidden" id="letteronly" value="<spring:message code="register.letteronly"/>">
<input type="hidden" id="invalidaccount" value='<spring:message code="register.stepOne.invalid.account.error"/>'>
<input type="hidden" id="mobilenumber" value="<spring:message code="profile.valid.mobilenumber"/>">
<input type="hidden" id="invalidPhone" value="<spring:message code="profile.phonenumber"/>">
<input type="hidden" id="postalcode" value='<spring:message code="profile.postalcode.error"/>'>
<input type="hidden" id="specifyphonenumber" value='<spring:message code="profile.specifyphonenumber"/>'>
<input type="hidden" id="registrationinvalidemail" value='<spring:message code="password.forgotPassword.email.error"/>'>
<input type="hidden" id="allField" value='<spring:message code="report.shipto.all"/>'>
<input type="hidden" id="items" value='<spring:message code="items.all"/>'>
<%--Added for serialization --%>
<input type="hidden" id="invalidgtin" value='<spring:message code="serialization.error.numeric"/>'>
<input type="hidden" id="invalidserialnumber" value='<spring:message code="serialization.error.alphanumeric"/>'>
<input type="hidden" id="invalidbatchnumber" value='<spring:message code="serialization.error.alphanumeric"/>'>
<!--Added for 5513 Date picker  -->
<input type="hidden" id="dateformatacrosstheportal" value='<spring:message code="date.dateformat"/>'>
<input type="hidden" id="dateformatacrosstheportalToDisplay" value='<spring:message code="date.dateformat.to.display"/>'>


<input type="hidden" id="numbersOnly" value="<spring:message code="password.numbers"/>">
<input type="hidden" id="NoFranchiseError" value="<spring:message code="user.management.NoFranchiseError"/>">
<input type="hidden" id="qtyCheck" value="${jalosession.tenant.config.getParameter('min.quantity.popup.check')}">
<%-- <input type="hidden" id="reportsslideshow" value="<spring:message code="reports.slideshow"/>">
<input type="hidden" id="linkPreviousPage" value="<spring:message code="text.storefinder.desktop.page.linkPreviousPage"/>">
<input type="hidden" id="choosefile" value="<spring:message code="choose.file"/>"> --%>

<script type="text/javascript">
var	ITEMS=document.getElementById("items").value;
	var	INVALID_PHONE_NUMBER=document.getElementById("invalidPhone").value;
	var	REGISTRATION_INVALID_EMAIL=document.getElementById("registrationinvalidemail").value;
	var VALID_CODE=document.getElementById("productValidCode").value;
	var UPLOAD_FILE_ERROR=document.getElementById("uploadfileerror").value;
	var SHOW_FILTERS_UPPER=document.getElementById("showfiltersUpper").value;
	var HIDE_FILTERS_UPPER=document.getElementById("hidefiltersUpper").value;
	var SHOW_FILTERS_LOWER=document.getElementById("showfiltersLower").value;
	var HIDE_FILTERS_LOWER=document.getElementById("hidefiltersLower").value;
	var NO_DATA=document.getElementById("nodata").value;
	var REPORTS_LINES=document.getElementById("reportsLines").value;
	var REPORTS_RESULTS=document.getElementById("reportsResults").value;

	var ITEMS_LIST_OF=document.getElementById("items_list_of").value;

	var DATA_SHOW_MENU=document.getElementById("dataShowMenu").value;
	var SECURITY_QUESTION=document.getElementById("secquestion").value;
	var SIGNUP_PASSWORD='<li>'+document.getElementById("signuppassword").value+'</li>';
	var REENTER_PASSWORD='<li>'+document.getElementById("reenterpassword").value+'</li>';
    var PASSWORD_COMPLEXITY=document.getElementById("complexity").value;
    var PASSWORD_MISMATCH=document.getElementById("passwordMismatch").value;
    var ACCEPT_TERMS=document.getElementById("acceptTerms").value;
    var ACCEPT_POLICY=document.getElementById("acceptPolicy").value;
    var INVALID_CAPTCHA=document.getElementById("invalidCaptcha").value;
    var ACCORDION_FIELDS=document.getElementById("accordionFields").value;
    var REQUIRED_ACCOUNT=document.getElementById("requiredAccount").value;
    var REQUIRED_BUSINESS=document.getElementById("requiredBusiness").value;
    var REQUIRED_COUNTRY=document.getElementById("requiredCountry").value;
    var REQUIRED_CITY=document.getElementById("requiredCity").value;
    var REQUIRED_ADDRESS1=document.getElementById("requiredAddress1").value;
    var REQUIRED_STATE=document.getElementById("requiredState").value;
    var REQUIRED_POSTALCODE=document.getElementById("requiredPostalCode").value;
    var REQUIRED_ZIPCODE=document.getElementById("requiredZipCode").value;
    var VALID_ACCOUNT=document.getElementById("validAccount").value;
    var REQUIRED_WWID_DIV=document.getElementById("requiredWwidDiv").value;
    var REQUIRED_EST_AMOUNT=document.getElementById("requiredEstAmount").value;
    var REQUIRED_PRODUCT=document.getElementById("requiredProduct").value;
    var VALID_PREFIX=document.getElementById("validPrefix").value;
    var REQUIRED_OPENING_ORDER=document.getElementById("requiredOpeningOrder").value;
    var REQUIRED_FIRST_NAME=document.getElementById("requiredFname").value;
    var REQUIRED_LAST_NAME=document.getElementById("requiredLname").value;
    var REQUIRED_ORG_NAME=document.getElementById("requiredOrgName").value;
    var REQUIRED_DEPARTMENT=document.getElementById("requiredDept").value;
    var REQUIRED_BUSINESS_EMAIL=document.getElementById("requiredBusinessEmail").value;
    var RE_ENTER_EMAIL=document.getElementById("reEnterEmail").value;
    var EMAIL_MISMATCH=document.getElementById("emailMismatch").value;
    var REQUIRED_SUPER_PHONE=document.getElementById("requiredSuperPhone").value;

    var REQUIRED_SUPER_EMAIL=document.getElementById("requiredSuperEmail").value;
    var REQUIRED_PHONE=document.getElementById("requiredPhone").value;
    var REQUIRED_SUPER_NAME=document.getElementById("requiredSuperName").value;
	var INVALID_EMAIL=document.getElementById("invalidEmail").value;

	var NUMBERS_ONLY=document.getElementById("numbersOnly").value;
	var PRODUCT_PRICE_GREATER=document.getElementById("pricegreater").value;
	var PRODUCT_PRICE=document.getElementById("price").value;
	var SELECT_SECTOR=document.getElementById("selectSector").value;
	var INVALID_FAX=document.getElementById("invalidFax").value;
	var DATE_SELECTION=document.getElementById("dateselection").value;
    var DATE_FROM_INVALID_RANGE=document.getElementById("dateFromInvalidRange").value;
    var DATE_TO_INVALID_RANGE=document.getElementById("dateToInvalidRange").value;
	var LETTER_ONLY=document.getElementById("letteronly").value;
	var INVALID_ACCOUNT_NUMBER=document.getElementById("invalidaccount").value;
	var INVALID_MOBILE_NUMBER=document.getElementById("mobilenumber").value;
	var INVALID_POSTALCODE=document.getElementById("postalcode").value;
	var SPECIFY_PHONE_NUMBER=document.getElementById("specifyphonenumber").value;
	/* Added for serialization */
	var INVALID_GTIN=document.getElementById("invalidgtin").value;
	var INVALID_SERIAL_NUMBER=document.getElementById("invalidserialnumber").value;
	var INVALID_BATCH_NUMBER=document.getElementById("invalidbatchnumber").value;
	var NO_FRANCHISE_ERROR=document.getElementById("NoFranchiseError").value;
	/*Added for 5513 Date picker*/
	var DATE_FORMAT=document.getElementById("dateformatacrosstheportal").value;
	var DATE_FORMAT_TO_DISPLAY=document.getElementById("dateformatacrosstheportalToDisplay").value;

	var QTY_CHECK=document.getElementById("qtyCheck").value;	
	
    /*var CHOOSE_FILE=document.getElementById("choosefile").value; */
</script>