<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="registration"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>

<div class="span-18 last rdiCont" id="myAddAccountChoice">
	<div class="sectionBlock myProfilePanel addAccountExisting">

		<c:url value="/my-account/addExistingAccount"
			var="addExistingAccountAction" />
		<form:form id="addAccountExistingChoiceForm"
			name="addAccountExistingChoiceForm" commandName="addaccountForm"
			method="POST" action="${addExistingAccountAction}">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div>Select one:</div>
					<div class="radio radio-info">
						<input type="radio" id="inlineRadio1"
							class="menubody-heightcheck accessToAnotherAccount"
							value="option1" name="radioInline" checked="checked"> <label
							for="inlineRadio1"> <spring:message
								code="profile.addAccount.userAccess" /></label>
					</div>
					<div class="radio radio-info">
						<input type="radio" id="inlineRadio2"
							class="menubody-heightcheck accessToNewAccount" value="option1"
							name="radioInline"> <label for="inlineRadio2"> <spring:message
								code="profile.addAccount.newUser" /></label>
					</div>
					<div class="another-account-number">
						<div>
							<label><spring:message
									code="profile.myprofile.account" /><span>*</span> <br /> <i><spring:message
										code="profile.myprofile.instruction" /></i> </label>
						</div>
						<div>
						<!-- AAOL-4662 My Profile: User is able to add the existing account again to the profile. Check needed or not. -->
							<form:input type="text" name="nameOrNumberOfExistingAccount" class="required form-control accnobox  margintop5"	id="nameOrNumberOfExistingAccount" path="nameOrNumberOfExistingAccount" data-msg-required="<spring:message code='profile.profile.text'/>"/>
							<label for="nameOrNumberOfExistingAccount" class="getErrorMessage" data="<spring:message code='profile.profile.text'/>"></label>
						</div>
						<div class="size1of2 cell">
						<div class="registerError"></div>
					</div>	
					</div>
				</div>

			</div>
<!-- AAOL-4662 My Profile: User is able to add the existing account again to the profile. Check needed or not. -->
				<c:if test="${not empty successAcc}">
					<p class="correctAnswer mar0">
						${successAcc}<spring:message code="profile.myprofile.info" />
					</p>
				</c:if>
				<c:if test="${not empty duplicateValues}">
				  <div class="recPassError  AddAccountError">
					<label class="error">${duplicateValues}<spring:message code="profile.myprofile.duplicateAccount1" /> <spring:message code="profile.myprofile.duplicateAccount2" /></label>
					</div>
				</c:if>
					<c:if test="${not empty invalidAccountNumbers}">
						<div class="recPassError  AddAccountError">
							<label class="error">${invalidAccountNumbers}<spring:message code="profile.myprofile.errorAccount1" /> <spring:message code="profile.myprofile.errorAccount2" /></label>
						</div>
					</c:if>
				
			<c:if test="${isSuccess=='true'}">
				<label class="correctAnswer mar0"><spring:message code="profile.changeSecurityQuestion.changesSaved" /></label>
			</c:if>
<!-- AAOL-4662 My Profile: User is able to add the existing account again to the profile. Check needed or not. -->	
			<div class="row">

				<div
					class="col-lg-12 col-md-12 col-sm-12 col-xs-12 buttonWrapperWithBG sectionBlock myProfileButtonWrapper">
					<button type="submit"
						class="primarybtn btn btnclsactive save-change"
						id="emailPreferencesSubmit">
						<spring:message code="profile.addAccount.submitRequest" />
					</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
