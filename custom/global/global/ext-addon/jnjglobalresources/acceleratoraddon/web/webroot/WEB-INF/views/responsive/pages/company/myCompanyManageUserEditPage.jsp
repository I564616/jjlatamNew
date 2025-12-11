<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="company"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/company"%>

<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="resource"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb"%>
<spring:url
	value="/my-company/organization-management/manage-users/create"
	var="manageUsersUrl" />
	

<template:page pageTitle="${pageTitle}">
	<input id="editUserSpecificTerr" value="${invalidTerritory}" type="hidden" />
	<div class="col-lg-12 col-md-12 mobile-no-pad">
		<div id="emeo-usrmanagement-createusr">
			<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content">
				<spring:message code="userSearch.breadCrumb" /></div>
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
					<div class="float-right-to-none floating-global-btns">
						<c:url value="/resources/usermanagement" var="searchPage" />
						<spring:url value="/resources/usermanagement/create"
							var="createUserProfile" />
							<a href="${createUserProfile}" type="button"
								class="btn btnclsactive buttonMargin"
								id="createUserProfileUserMangement">
								<spring:message code="text.company.manageUser.button.create" />
							</a> <a href="${searchPage}" type="button"
							class="btn btnclsnormal pull-right"><spring:message
								code='user.link.backToUserSearch' /></a>
					</div>
				</div>
			</div>

			<input type="hidden" value="${hiddenAccountNamesList}"
				id="hddnTempAccountNameList" /> <input type="hidden"
				value="${hiddenAccountNamesList}"
				id="hddnOriginalTempAccountNameList" /> <input type="hidden"
				value="true" id="valueSetFlag" />
			<c:url value="/resources/usermanagement/editUser" var="editUser" />

			<p class="registerError errorWidthSecQ" id="editUserProfileFormError"
				style="margin: 0; display: none;">
				<label class="error"> <spring:message
						code="password.forgotPassword.enterPassword" />
				</label>
			</p>
			<div id="myTrainingResources">
				<form:form method="post" action="${editUser}"
					id="editUserProfileForm" commandName="jnjGTB2BCustomerForm">
				
					<div class="row myTrainingResources"  id="myTrainingResourcesSuccess">
						<div class="col-md-12">
							<div class="panel-group">
								<div class="panel panel-success">
									<div class="panel-heading">
										<span class="glyphicon glyphicon-ok"></span> <span class="training-content"></span>
									</div>
								</div>
							</div>
						</div>
					</div> 
					<div class="row myTrainingResources"  id="myTrainingResourcesFailure">
						<div class="col-md-12">
							<div class="panel-group">
								<div class="panel panel-danger">
									<div class="panel-heading">
										<span class="glyphicon glyphicon-ban-circle"></span> <span class="training-content"> </span>
									</div>
								</div>
							</div>
						</div>
					</div> 
					<div class="col-lg-12 col-md-12 boxshadow whiteBg">
						<div class="row subcontent1">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 pane-title"><spring:message code='editProfile.breadCrumb' /></div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
								<div class="float-right-to-none status-dropdown">
									<label for="status" id="status-txt"><spring:message
											code='user.form.label.status' /></label>
									<c:if test="${selfTier1 || selfTier2 || isStatusDisabled}">
										<c:set var="statusEnabled" value="true" />
									</c:if>
									<form:select id="status-dropdown status" class="selectpicker"
										path="status" disabled="${statusEnabled}">
										<c:forEach items="${statuses}" var="status">
											<option value="${status.key}"
												<c:if test="${fn:containsIgnoreCase(status.value, 'pending')
													|| (fn:containsIgnoreCase(status.value, 'Rejected') && isTier1User)}">disabled="disabled"</c:if>
												<c:if test="${(fn:containsIgnoreCase(status.value, jnjGTB2BCustomerForm.status))}"> selected="selected"</c:if>>
												${status.value}</option>
										</c:forEach>
									</form:select>
									<form:input type="hidden" id="existingStatus"
										path="existingStatus" />

								</div>
							</div>
						</div>
						<div class="row password-info">
							<div
								class="col-lg-4 col-md-5 col-sm-4 col-xs-12 formPanel pw-field passInfo-mobile">
								<spring:message code='user.form.label.passwordChanged' />
								<span>${jnjGTB2BCustomerForm.passwordChangeDate}</span>
							</div>
							<div class="col-lg-4 col-md-5 col-sm-4 col-xs-12 pw-field txt-right-to-left passInfo-mobile">
								<spring:message code='user.form.label.passwordExpires' />
								<span>${jnjGTB2BCustomerForm.passwordExpiryDate}</span>
							</div>
							<div class="col-lg-4 col-md-2 col-sm-4 col-xs-12">
								<a href="javascript:;"
									class="btn btnclsactive secondarybtn float-right-to-none"
									type="button" id="resetPasswordButton"><spring:message
										code='user.form.link.resetPassword' /></a>

							</div>
						</div>

						<div class="row" id="select-profile">
							<div class="col-lg-12 col-md-12 col-sm-12">
								<span id="select-profile-txt" class="usm-create-subHead"><label><spring:message code='user.label.sectors' /><span
									class="redStar">*</span></label></span>
								<div class="checkbox checkbox-info checkbox-inline">
									<c:if
										test="${selfTier2 || selfTier1|| !isTier2User}">
										<c:set var="sectorEnableMent" value="true" />
									</c:if>
									<form:checkbox name="medical[]" id="mdd" value="option1"
										class="left profileSector checkBoxBtn" path="mdd"
										disabled="${sectorEnableMent}" />
									<div class="checkLabelHolder"><label for=mdd
									class="getErrorMessage checkLabel"
									data="<spring:message code='usermanagement.required.sector'/>"><spring:message
											code='user.form.sector.mdd' /></label></div>
								</div>
								<div class="checkbox checkbox-info checkbox-inline">
								<form:checkbox name="medical[]" id="consumer" value="option1"
										class="left profileSector checkBoxBtn" path="consumer"
										disabled="${sectorEnableMent}" />
										<div class="checkLabelHolder"><label for="consumer" class="checkLabel">
										<spring:message code='user.form.sector.consumer' />
										</label></div>
								</div>
								<div class="checkbox checkbox-info checkbox-inline">
								<form:checkbox name="medical[]" id="pharma" value="option1" class="left profileSector checkBoxBtn" path="pharma"
								disabled="${sectorEnableMent}" />
									<div class="checkLabelHolder"><label for="pharma" class="checkLabel"><spring:message
									code='user.form.sector.pharma' /></label></div>
								</div>
								<div class="" id="sectorMsg2"></div>
							</div>	
						</div>
						
						<div class="" id="um-create-form-holder">
							
							<div class="row um-form-row">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<form:input type="hidden" id="userId" name="userId" path="uid" />
										<label for="fisrtName" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.firstname'/>"><spring:message
												code='user.form.label.firstname' /><span class="redStar">*</span></label>
										<form:input type="text" name="fisrtName" id="fisrtName"
											disabled="${disablefield}"
											class="required medium form-control" path="firstName" />
										<div class="registerError"></div>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
									<div class="form-group">
										<label for="lastName" class="getErrorMessage"
											data="<spring:message code='usermanagement.required.lastname'/>"><spring:message
												code='user.form.label.lastname' /><span class="redStar">*</span></label>
										<form:input type="text" name="lastName" id="lastName"
											disabled="${disablefield}"
											class="required medium form-control" path="lastName" />
										<div class="registerError"></div>
									</div>
								</div>
							</div>
							
							
						<div class="row um-form-row">	
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="emailLogin" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.emailLogin'/>"><spring:message
											code='user.form.label.emailLogin' /><span class="redStar">*</span></label>
									<form:input type="email" name="emailLogin" id="emailLogin"
										disabled="true" class="required big form-control" path="email" />
									<form:input type="hidden" path="email" />
									<input type="hidden" id="hiddenMsgValue"
										value="<spring:message code='usermanagement.alreadyExists.emailLogin'/>" />
									<input type="hidden" id="existingEmail"
										value="${existingEmail}" />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="country" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.country'/>"><spring:message
											code='user.form.label.country' /><span class="redStar">*</span></label>
									<form:select id="country" class="required selectpicker"
										name="country" data-width="100%" disabled="${disablefield}"
										path="contactAddress.countryIso">
										<option value="">
											<spring:message code='usermanagement.select' />
										</option>
										<c:forEach items="${countries}" var="country">
											<c:choose>
												<c:when
													test="${country.isocode eq jnjGTB2BCustomerForm.contactAddress.countryIso}">
													<option value="${country.isocode}" selected="selected">${country.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${country.isocode}">${country.name}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select>
									<div class="registerError" id="countryMsg"></div>
								</div>
							</div>
						</div>	
						
						<div class="row um-form-row">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="address1" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.address1'/>"><spring:message code='user.form.label.address1'/><span
										class="redStar">*</span></label>
									<form:input type="text" name="address1" id="address1"
										disabled="${disablefield}"
										class="required medium form-control"
										path="contactAddress.line1" />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="cityName" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.city'/>"><spring:message
											code='user.form.label.city' /><span class="redStar">*</span></label>
									<form:input type="text" name="cityName" id="cityName"
										disabled="${disablefield}"
										class="required medium form-control"
										path="contactAddress.townCity" />
									<div class="registerError"></div>
								</div>
							</div>
						</div>	
							
							
						<div class="row um-form-row">	
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="address2" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.address2'/>"><spring:message code='user.form.label.address2'/></label>
									<form:input type="text" name="address2" id="address2"
										disabled="${disablefield}" class="medium form-control"
										path="contactAddress.line2" />
									<div class="registerError"></div>
								</div>
							</div>

							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="state" class="getErrorMessage"
										data="Please enter State!"><spring:message
											code='user.form.label.state' /><span class="redStar"
										${(jnjGTB2BCustomerForm.contactAddress.countryIso eq "US") ? '':'style="display:none;"'}
										id="stateRedStar">*</span></label>
									<c:choose>
										<c:when
											test="${'US' eq jnjGTB2BCustomerForm.contactAddress.countryIso}">
											<c:set var="disableUs" value="false" />
											<c:set var="disableNonUs" value="true" />
										</c:when>
										<c:otherwise>
											<c:set var="disableUs" value="true" />
											<c:set var="disableNonUs" value="false" />
										</c:otherwise>
									</c:choose>
									<div class="cell state"
										${(jnjGTB2BCustomerForm.contactAddress.countryIso eq "US") ? '':'style="display:none;"'} style="width:100%">
										<form:select id="state" name="state" disabled="${disableUs}"
											path="contactAddress.state" class="required" data-width="100%">
											<c:forEach items="${states}" var="state">
												<c:choose>
													<c:when
														test="${state.isocode eq jnjGTB2BCustomerForm.contactAddress.state}">
														<option value="${state.isocode}" selected="selected">${state.name}</option>
													</c:when>
													<c:otherwise>
														<option value="${state.isocode}">${state.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</form:select>
									</div>
									<div class="cell noUsState "
										${(jnjGTB2BCustomerForm.contactAddress.countryIso eq "US") ? 'style="display:none;"':''}>
										<form:input type="text" disabled="${disableNonUs}"
											class="form-control" autocomplete="off"
											path="contactAddress.state" name="state" id="noUsState" />
									</div>
								</div>
							</div>
						</div>	


				<div class="row um-form-row">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="postalCode" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.postalCode'/>"><spring:message
											code='user.form.label.postCode' /><span class="redStar">*</span></label>
									<form:input type="text" name="postalCode" id="postalCode"
										disabled="${disablefield}" class="required form-control alphanumeric"
										path="contactAddress.postcode" />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="phone"
										class="usm-phone-label getErrorMessage"
										data="<spring:message code='usermanagement.required.phone'/>"><spring:message
											code='user.form.label.PhoneNumber' /><span class="redStar">*</span></label>
									<div class="usm-phone-code">
										<label for="phonePrefix" class="widthAuto"> <form:input
												type="text" id="phonePrefix" value="+1" class="form-control phone-only"
												name="phonePrefix" path="phoneNumberPrefix" />
										</label>
									</div>
									<div class="usm-phone-number">
										<form:input id="phone" disabled="${disablefield}"
											type="text" name="phone"
											class="required phoneFormat phoneFormateUS form-control numbersonly removeSplChar" path="phone" />
											<div class="registerError"></div>
									</div>
									
								</div>
							</div>
						</div>	
							
							
					<div class="row um-form-row">		
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label class="usm-phone-label" for="mobileNumber""><spring:message
											code='user.form.label.mobileNumber' /></label>
									<div class="usm-phone-code">
										<label for="mobilePrefix" class="widthAuto"> <form:input
												id="mobilePrefix" value="+1" class="form-control phone-only"
												name="mobilePrefix" disabled="${disablefield}"
												path="contactAddress.mobileNumberPrefix" />
										</label>
									</div>
									<div class="usm-phone-number">
										<form:input id="mobileNumber" class="mobileFormat phoneFormateUS form-control numbersonly removeSplChar" type="text"
											name="mobileNumber" disabled="${disablefield}"
											path="contactAddress.mobileNumber" />
										<div class="registerError"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label class="usm-phone-label" for="faxNumber""><spring:message
											code='user.form.label.faxNumber' /></label>
									<div class="usm-phone-code">
										<label for="faxPrefix" class="widthAuto"> <form:input
												disabled="${disablefield}" id="faxPrefix" value="+1"
												class="form-control phone-only" name="faxPrefix"
												path="contactAddress.faxNumberPrefix" /></label>

									</div>
									<div class="usm-phone-number">
										<form:input id="faxNumber" class="faxFormate form-control numbersonly removeSplChar" type="text"
											name="faxNumber" path="contactAddress.faxNumber"
											disabled="${disablefield}" />
										<div class="registerError"></div>
									</div>
								</div>
							</div>
						</div>	
							
						<div class="row um-form-row">	
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="orgName" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.orgName'/>"><spring:message
											code='user.form.label.orgName' /><span class="redStar">*</span></label>
									<form:input type="text" name="orgName" id="orgName"
										disabled="${disablefield}" path="orgName"
										class="required big form-control" />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="department" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.department'/>"><spring:message
											code='user.form.label.department' /><span class="redStar">*</span></label>
									<form:select class="selectpicker required" value=""
										data-width="100%" id="department" name="department"
										disabled="${disablefield}" path="department">
										<option value="">
											<spring:message code='usermanagement.select' />
										</option>
										<c:forEach items="${departments}" var="department">
											<c:choose>
												<c:when
													test="${department.key eq jnjGTB2BCustomerForm.department}">
													<option value="${department.key}" selected="selected">${department.value}</option>
												</c:when>
												<c:otherwise>
													<option value="${department.key}">${department.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select>
									<div class="registerError" id="deptMsg"></div>
								</div>
							</div>
						</div>	
							
							
							
						<div class="row um-form-row">	
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="supName" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.supname'/>"><spring:message
											code='user.form.label.supervisionName' /><span
										class="redStar">*</span></label>
									<form:input type="text" name="supName" id="supName"
										disabled="${disablefield}"
										class="required medium form-control alpha-only" path="supervisorName" />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="supervisorPhone"
										class="getErrorMessage usm-phone-label"
										data="<spring:message code='usermanagement.required.supphone'/>"><spring:message
											code='user.form.label.supervisionPhone' /><span
										class="redStar">*</span></label>
									<div class="usm-phone-code">
										<label for="supPhonePrefix" class="widthAuto"> <form:input
												disabled="${disablefield}" type="text" id="supPhonePrefix"
												value="+1" class="form-control phone-only" name="supPhonePrefix"
												path="supervisorPhonePrefix" /></label>

									</div>
									<div class="usm-phone-number">
										<form:input id="supervisorPhone" type="text"
											name="supervisorPhone" disabled="${disablefield}"
											class="required phoneFormat phoneFormateUS form-control numbersonly removeSplChar" path="supervisorPhone" />
										<div class="registerError"></div>
									</div>
								</div>
							</div>
					</div>		
							
						<div class="row um-form-row">	
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="supEmail" class="getErrorMessage"
										data="<spring:message code='usermanagement.required.supemail'/>"><spring:message
											code='user.form.label.supervisionEmail' /><span
										class="redStar">*</span></label>
									<form:input type="email" name="supEmail" id="supEmail"
										disabled="${disablefield}" class="required big form-control"
										path="supervisorEmail"
										data-msg-email="The email address you entered is invalid. Please try again." />
									<div class="registerError"></div>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 formPanel um-element-col">
								<div class="form-group">
									<label for="language" class="getErrorMessage"><messageLabel:message
											messageCode='user.form.label.language' /> <span
										class="redStar">*</span></label>
									<form:select path="language" id="language"
										class="selectpicker required" data-width="100%">
										<c:forEach items="${languages}" var="languages">
											<option value="${languages.isocode}"
												<c:if test="${jnjGTB2BCustomerForm.language eq languages.isocode}"> selected="selected"</c:if>>${languages.name}
											</option>
										</c:forEach>
									</form:select>
									<div class="registerError error"></div>
								</div>
							</div>
						</div>	
				</div>
						<div class="row" id="usm-access-row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 formPanel um-element-col">
								<label><spring:message code='user.form.label.access.name' /><span class="star-sign">*</span></label>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 formPanel access-content">
								<div class="radio radio-info">
									<form:radiobutton name="radio1" id="access-radio-1"
										path="radio1" disabled="${selfEditAdminstartor}"
										value="${accessNotSalesRep}" class="accessRadio" />
									<label for="access-radio-1"> <spring:message
											code='user.form.label.access.notSalesRep' /></label>
								</div>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 formPanel access-content um-element-col">
								<div class="radio radio-info">
									<c:if test="${jnjGTB2BCustomerForm.radio1 eq accessWWID }">
										<c:set var="selectWWID" value='checked="checked"'></c:set>
									</c:if>
									<form:radiobutton name="radio1" id="access-radio-2"
										path="radio1" value="${accessWWID}" class="accessRadio"
										disabled="${selfEditAdminstartor}" />
									<label for="access-radio-2"><spring:message
											code='user.form.label.access.wwid' /></label>
								</div>
								<div class="usm-access-subdata paddleftaccess" id="access-content-2">

									<span class="marLeft20"><spring:message code='user.form.label.wwid'/> </span> 
									<span class="textBlack marRight54">${jnjGTB2BCustomerForm.wwid}</span>

									<div id="division-code">
										<label class="divisionCode" for="divCode">Division Code:</label>
									</div>	
									<form:select id="divCode" name="divCode" path="division"
										disabled="${disablefield}" data-width="100%">
										<option value="">
											<spring:message code='usermanagement.select' />
										</option>
										<c:forEach items="${consumerDivisonCodes}" var="division">
											<c:choose>
												<c:when
													test="${division.key eq jnjGTB2BCustomerForm.division}">
													<option value="${division.key}" selected="selected">
														${division.value}</option>
												</c:when>
												<c:otherwise>
													<option value="${division.key}">${division.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select>
								</div>
								<div class="cell">
									<div class="registerError"></div>
								</div>
							</div>
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12 formPanel access-content um-element-col" id="moreDiv">
							<div class="radio radio-info">
								<form:radiobutton name="radio1" id="access-radio-3"
									disabled="${selfEditAdminstartor}" path="radio1"
									value="${accessTerritories}" class="accessRadio" />
								<label for="access-radio-3"><spring:message
										code='user.form.label.access.territories' /></label>
							</div>

							<c:choose>
								<c:when test="${not empty jnjGTB2BCustomerForm.territorDivsion}">
									
										<div class="usm-access-subdata" id="access-content-3">
											<c:forEach items="${jnjGTB2BCustomerForm.territorDivsion}" var="territory" varStatus="status">
												<c:set var="territoryValues" value="${fn:split(territory, '|')}" />
												<div class="grant-access-row">
													<div class="usm-access-subdata-row">
														<label class="franchise" for="franchise"><spring:message code='user.form.label.frenchDiv' /></label>
														 <!-- (AAOL-4662)Better UI needed for granting access to multiple specific territories -->
														<form:select id="divCode" name="divCode" path="franchiseDivisions" class="form-control only-form-control no-margin-form" data-width="100%">
															<option value="">
																<spring:message code='usermanagement.select' />
															</option>
															<c:forEach items="${divisionCodes}" var="division">
																<option value="${division.key}"
																	<c:if test="${division.key eq territoryValues[1]}">selected='true'</c:if>>
																	${division.value}
																</option>
															</c:forEach>
														</form:select>
													</div>
													<div class="usm-access-subdata-row">
														<label class="divisionCode" for="territory"><spring:message code='user.form.label.territories' /></label>
														<form:input id="territory" class="form-control" type="text" path="territories" value="${territoryValues[0]}" />
													</div>
													<div class="usm-access-subdata-row">
														<c:if test="${status.index == 0}">
															<button type="button" class="usm-access-add-btn btn btnclsactive clrEnable ga-add-btn"><spring:message code='userSearch.Edit.Add' /></button>
														</c:if>
														<c:if test="${status.index != 0}">
															<button type="button" class="clrDisable usm-access-add-btn btn btnclsactive hide-btn ga-del-btn"><spring:message code='userSearch.Edit.delete' /></button>
														</c:if>

													</div>
												</div>
											</c:forEach>
										</div>
									

								</c:when>
								<c:otherwise>
									
										<div class="usm-access-subdata" id="access-content-3">
											<div class="grant-access-row">
												<div class="usm-access-subdata-row">
													<label class="franchise" for="franchise"><spring:message code='user.form.label.frenchDiv' /></label>
													<!-- (AAOL-4662)Better UI needed for granting access to multiple specific territories -->
													<form:select id="divCode" name="divCode" path="franchiseDivisions" class="form-control only-form-control no-margin-form" data-width="100%">
														<option value="">
															<spring:message code='usermanagement.select' />
														</option>
														<c:forEach items="${divisionCodes}" var="division">
															<option value="${division.key}">
																${division.value}
															</option>
														</c:forEach>
													</form:select>
												</div>
												<div class="usm-access-subdata-row">
													<label class="divisionCode" for="territory"><spring:message
															code='user.form.label.territories' /></label>
													<form:input id="territory" name="territory"
														class="form-control" path="territories" />
												</div>
												<div class="usm-access-subdata-row">
													<button type="button"
														class="usm-access-add-btn btn btnclsactive  clrEnable ga-add-btn""><spring:message code='userSearch.Edit.Add' /></button>
													<button type="button"
														class="clrDisable usm-access-add-btn btn btnclsactive hide-btn ga-del-btn"><spring:message code='userSearch.Edit.delete' /></button>
												</div>
											</div>
										</div>
									

								</c:otherwise>
							</c:choose>
							
							<c:if test="${invalidTerritory}">
								<div class="cell">
									<div class="registerError"></div>
									<label class="error"><spring:message code="profile.validTerritory"/></label>
								</div>
							</c:if>
						</div>
					</div>
            <div class="row um-element" id="um-roles-accounts">
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel">
                <div class="row">
                  <div class="usm-create-subHead col-lg-6">
                    <spring:message code='user.management.edit.franchise' />
                    <span class="star-sign">*</span>
                  </div>
                  <div class="col-lg-6">
                    <a class="btn btnclsactive secondarybtn float-right-to-none" data-target="#changeFranchisePopupForUsr" data-toggle="modal"
                      id="selectFranchiseUM"><spring:message code='user.management.edit.franchise' /></a>
                  </div>
                </div>
                <div class="usm-account-list scroll-y-holder" id="franchiseDiv">
                  <c:forEach var="franchise" varStatus="count" items="${allowedFranchise}">
                    <div class="${count.count %2 == 0 ? 'even-row' : 'odd-row'}">
                      <div class="display-table-cell">
                        <div class="">
                          <a href="#" class="usm-account-num">${franchise.name}</a>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
                </div>
                <div style="display: none;" id="franchisePopUpDiv">
                  <company:uMFranchisePopUp />
                </div>
              </div>
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel um-element-col">
                <div class="row">
                  <div class="usm-create-subHead col-lg-4" id="account-header">
                    <label for="accounts" class="getErrorMessage" data="<spring:message code='user.form.accounts'/>"> <spring:message
                        code='user.form.accounts' />
                    </label>
                  </div>
                  <div class="col-lg-8">
                  <a class="btn btnclsactive secondarybtn float-right-to-none" href="#" id="selectAccountEditUM"><spring:message code='user.form.addAccount' /></a>
                  </div>
                  <div class="registerError error"></div>
                </div>
                <form:input type="hidden" id="hddnAccountsString" path="groups" />
                <input type="hidden" value="${loggedUserAccountList}" id="hddnloggedUserAccountList" />
                <company:manageMultipleAccountsForEdit />
                <input type="hidden" id="preselectedAccounts" value="${selectedAccountsString}" />
                <form:input type="hidden" id="hddnAccountsStringUM" path="groups" />
              </div>
            </div>
            <div class="row um-element" id="um-roles-accounts">
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 rolesPanel um-element-col">
                <div class="roleTxt">
                  <label for="roles" class="getErrorMessage"> <messageLabel:message messageCode='user.form.label.rolePermission' /> <span
                    class="redStar">*</span></label>
                </div>
                <div class="">
                  <c:choose>
                    <c:when test="${jnjGTB2BCustomerForm.role eq 'jnjGTAdminGroup'||jnjGTB2BCustomerForm.role eq 'jnjGTCsrGroup' }">
                      <form:select path="role" id="role" disabled="true" data-width="100%">
                        <form:option value="admin">
                          <spring:message code="Admin" />
                        </form:option>
                      </form:select>
                    </c:when>
                    <c:otherwise>
                      <form:select path="role" id="role" disabled="${!isTier2User}" data-width="100%" onchange="activeFinancialAnalysis()">
                        <form:option value="viewOnlyBuyerGroup">
                          <spring:message code="userSearch.role.viewOnly" />
                        </form:option>
                        <form:option value="placeOrderBuyerGroup">
                          <spring:message code="userSearch.role.viewPlaceOrder" />
                        </form:option>
                        <form:option value="viewOnlySalesRepGroup">
                          <spring:message code="userSearch.role.viewOnlySalesRep" />
                        </form:option>
                        <form:option value="placeOrderSalesGroup">
                          <spring:message code="userSearch.role.viewPlaceOrderSalesRep" />
                        </form:option>
                        <form:option value="JnjGTInternalNoChargeUser">
                          <spring:message code="userSearch.role.noCharge" />
                        </form:option>
                      </form:select>
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="registerError" id="roleMsg"></div>
                <!-- No charge flag  > move to left column under Roles/ Permission dropdown(AAOL-4662) -->
                <div class="row">
                  <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="checkbox checkbox-info">
                      <c:if test="${(!isTier2User || selfTier2 || selfTier1) && jnjGTB2BCustomerForm.role eq 'JnjGTInternalNoChargeUser'}">
                        <c:set var="noChargeFlag" value="true" />
                      </c:if>
                      <form:checkbox id="noCharge" path="noCharge" checked="${noChargeFlag}" />
                      <div id="no-charge-label">
                        <label for="noCharge" class="left marRight5"> <spring:message code='user.form.label.noChargeFlag' /></label>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- added for AAOL-2422 -->
                <div class="row">
                  <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="checkbox checkbox-info">
                      <form:checkbox id="financialEnable" path="financialEnable" disabled="true" />
                      <!-- <input id="Consignment-Order-Entry" class="styled" type="checkbox"> -->
                      <div id="financial-enable">
                        <label for="financialEnable" class="left marRight5"><spring:message code='user.management.financial.analysis' /></label>
                      </div>
                    </div>
                  </div>
                  <!-- No charge flag  > move to left column under Roles/ Permission dropdown(AAOL-4662) -->
                </div>
                <!-- ended for AAOL-2422 -->
                <c:forEach var="item" items="${jnjGTB2BCustomerForm.roles}">
                  <c:if test="${item eq 'placeOrderBuyerGroup'}">
                    <div class="row">
                      <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="checkbox checkbox-info">
                          <form:checkbox name="consignmentEntryOrder" id="consignmentEntryOrder" class="left profileSector checkBoxBtn"
                            path="consignmentEntryOrder" />
                          <div class="checkLabelHolder">
                            <label for="consignmentEntryOrder" class="getErrorMessage checkLabel"
                              data="<spring:message code='Consignment.order.entry'/>"><spring:message code='Consignment.order.entry' /></label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </c:if>
                </c:forEach>
              </div>
            </div>
          </div>
          <div class="row" id="crs-notes">
            <div class="col-lg-12 col-md-12 col-sm-12 usm-create-subHead">
              <label for="notes"> <spring:message code='user.form.label.csrNote' />
              </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
              <form:textarea rows="4" id="notes" class="form-control" readonly="readonly" path="csrNotes" cols="150"></form:textarea>
            </div>
          </div>
          <div class="row" id="add-notes-container">
            <div class="col-lg-12 col-md-12 col-sm-12" id="add-tonote-btn">
              <a href="javascript:;" id="addToNotesButton" type="button" class="btn btnclsactive pull-right"><spring:message
                  code='user.form.button.addToNotes' /></a>
            </div>
          </div>
        </form:form>
      </div>
      <div class="row">
        <div class="col-md-12" id="update-cancel">
          <a href="${searchPage}" class="canceltxt build-ordr-cancel-btn"><spring:message code='userSearch.button.cancel' /></a>
          <button class="btn btnclsactive buttonMargin updateProfileButton" id="updateProfileButtonTop">
            <spring:message code='editUser.update.button' />
          </button>
        </div>
      </div>
    </div>
  </div>
  <div id="add-account-popup-holder"></div>
</template:page>
