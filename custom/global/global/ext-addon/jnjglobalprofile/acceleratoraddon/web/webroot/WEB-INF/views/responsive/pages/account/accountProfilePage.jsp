<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjglobalprofile/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/form"%>
<%@ taglib prefix="registration"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>
	<%@taglib prefix="fn" uri="jakarta.tags.functions" %>

<template:page pageTitle="${pageTitle}">

	<div class="" id="profilepage">

		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row content">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code="profile.changePassword.myprofile" />
			</div>
		</div>
		<div class="panel-group" id="profile-updated-panel">
			<div class="panel panel-success">
				<div class="panel-heading" style="font-size:16px">
					
						<span><span class="glyphicon glyphicon-ok"></span>
							<spring:message code="profile.commom.success.message"></spring:message></span>
					
				</div>
			</div>
		</div>
		<div class="boxshadow">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion">
					<div class="profile-accordian panel">
						<div class="profile-accordian-header">
							<a data-toggle="collapse" data-parent="#accordion"
								href="#myPersonalInfo"
								class="toggle-link panel-collapsed clickontext"><span
								class="glyphicon glyphicon-plus plusicon"></span><spring:message code="Myprofile.personalinformation.text"/></a>
								
						</div>
						
						<div class="span-18 last profile-accordian-body panel-collapse collapse bordertop" id="myPersonalInfo">
							<div id="myprofileview" class="myProfilePanel">
						
										<div class="myProfileGlobalError registerError">
											<label class="error" style="display: none;"><spring:message code="profile.profile.text"/>.<br />
											</label>
										</div>
								

								<c:url value="/my-account/personalInformation"
									var="personalInfomationUrl" />
								<form:form id="myprofilecheck" action="${personalInfomationUrl}"
									method="POST" commandName="updateProfileForm"
									autocomplete="false">
									<div class="">
										
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12">
												<spring:message code="register.stepThree.first.name" />
											</div>
											<form:input type="hidden" value="${customerData.firstName}"
												path="firstName" />
											<div
												class="col-lg-4 col-md-6 col-sm-6 col-xs-12  margin-all-top">${customerData.firstName}</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="register.stepThree.last.name" />
											</div>
											<form:input type="hidden" value="${customerData.lastName}"
												path="lastName" />
											<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12 margintop ">${customerData.lastName}</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.email" />
											</div>
											<div class="col-lg-4 col-md-6 col-sm-6  col-xs-12 margintop ">${customerData.email}</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.address" />
												1<span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
											</div>
											<label for="profileAddress1" class="getErrorMessage"
												data="<spring:message code='register.stepTwo.required.address'/>"></label>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<form:input type="text" data-msg-required="<spring:message code='register.stepTwo.required.address'/>"
													class="required form-control margintop profile-input-mobile"
													id="profileAddress1" path="addressLine1" name="profileAddress1"
													value="${customerData.contactAddress.line1}" />
											</div>
											<div class="cell col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
												</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.address" />
												2
											</div>
											<label for="profileAddress2" class="getErrorMessage"
												data="<spring:message code='register.stepTwo.required.address'/>"></label>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<form:input type="text"
													class="form-control margintop profile-input-mobile"
													id=" profileAddress2" path="addressLine2"
													value="${customerData.contactAddress.line2}" />
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.city" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<label for="profileCity" class="getErrorMessage"
												data="<spring:message code='register.stepTwo.required.city'/>"></label>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<form:input type="text" data-msg-required="<spring:message code='register.stepTwo.required.city'/>"
													class="form-control margintop profile-input-mobile required"
													id="profileCity" path="city"
													value="${customerData.contactAddress.town}"
													style="width:100%" />
											</div>
												<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>

										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.country" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<label for="profileCity" class="getErrorMessage"
												data="<spring:message code='register.stepTwo.required.city'/>"></label>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop">
												<form:select id="country" path="country" class="required"
													 data-width="100%"
													style="min-width: 185px; width: 185px;">
													<c:out value=""></c:out>
													<c:forEach var="country" items="${countryList}">
														<option value="${country.isocode}"
															<c:if test="${country.isocode eq customerData.contactAddress.country.isocode}">selected='true'</c:if>>
															${country.name}</option>
													</c:forEach>
												</form:select>
											</div>
												<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>


										<c:choose>
											<c:when
												test="${'US' eq customerData.contactAddress.country.isocode}">
												<c:set var="disableUs" value="false" />
												<c:set var="disableNonUs" value="true" />
											</c:when>
											<c:otherwise>
												<c:set var="disableUs" value="true" />
												<c:set var="disableNonUs" value="false" />
											</c:otherwise>
										</c:choose>
										 <div class="row">	
														<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message
													code="profile.myprofile.state" /></div>	
														
														<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
															<form:input type="text" class=" form-control margintop profile-input-mobile " 
															id="state" path="state"  value="${customerData.contactAddress.state}"/>
														</div>												
													</div> 

										 
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="register.stepTwo.zipcode" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<label for="postalCode" class="getErrorMessage"	data="<spring:message code='profile.personal.zip.error'/>"></label>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<form:input type="text" data-msg-required="<spring:message code='profile.personal.zip.error'/>"
													class="form-control margintop profile-input-mobile alphanumeric required"
													id="postalCode" path="zip"
													value="${customerData.contactAddress.postalCode}" />
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.phoneNumber" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
												<div class="boxtext1">
													<form:input type="text" 
														class="form-control margintop profile-input-mobile phone-only"
														id=" profilePhoneNumberPrefix"
														path="phoneNumberPrefix"
														value="${customerData.contactAddress.phoneCode}" />
												</div>
												<div class="boxtext2">
												<label for="profilePhoneNumber" class="getErrorMessage" data="<spring:message code='register.stepThree.required.phone'/>"></label>
													<form:input type="text" class="form-control required comboInput numbersonly required phoneFormateUS removeAdditionalText removeSplChar" 
														id="profilePhoneNumber" path="phone" data-msg-required="Please enter Phone Number"
														value="${customerData.contactAddress.phone}" />
												</div>
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
											
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.mobileNumber" />
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
												<div class="boxtext1">
													<form:input type="text"
														class="form-control margintop profile-input-mobile phone-only"
														id=" profileMobileNumberPrefix"
														path="mobileNumberPrefix"
														value="${customerData.contactAddress.mobileCode}" />
												</div>
												<div class="boxtext2">
												<label for="profileMobileNumber" class="getErrorMessage" data="<spring:message code='register.stepThree.required.mobile'/>"></label>
													<form:input type="text" class="form-control comboInput numbersonly phoneFormateUS removeAdditionalText removeSplChar"
														id="profileMobileNumber" path="mobile"
														value="${customerData.contactAddress.mobile}" />
												</div>
											</div>
												<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.faxNumber" />
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
												<label for="profileFaxNumber" class="getErrorMessage"
													data="<spring:message code='register.stepThree.fax'/>"></label>
												<div class="boxtext1">

													<form:input type="text"
														class="form-control margintop phone-only profile-input-mobile"
														id="profileFaxNumberPrefix" path="faxNumberPrefix"
														value="${customerData.contactAddress.faxCode}" />
												</div>
												<div class="boxtext2">
												<label for="profileFaxNumber" class="getErrorMessage" data="<spring:message code='register.stepThree.fax'/>"></label>
													<form:input type="text" class="form-control numbersonly comboInput removeAdditionalText faxFormate removeSplChar"
														path="fax" id="profileFaxNumber"
														value="${customerData.contactAddress.fax}" />
												</div>
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="register.stepThree.super.name" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<label for="supervisorName" class="getErrorMessage"
													data="<spring:message code='register.stepThree.required.super.name'/>"></label>
												<form:input type="text" data-msg-required="<spring:message code='register.stepThree.required.super.name'/>"
													class="form-control profile-input-mobile required "
													id="supervisorName" path="supervisorName"
													value="${customerData.supervisorName}" />
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.supervisorPhone" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">

												<div class="boxtext1">
													<form:input type="text"
														class="form-control margintop phone-only profile-input-mobile"
														id="profileSupervisorPhonePrefix" path="supervisorPhoneCode"
														value="${customerData.supervisorPhoneCode}" />
												</div>
												<div class="boxtext2">
													<label for="profileSupervisorPhone" class="getErrorMessage"
														data="<spring:message code='profile.myprofile.requiredsupervisorPhone'/>"></label>
													<form:input type="text" class="form-control required comboInput numbersonly required phoneFormateUS removeAdditionalText removeSplChar" data-msg-required="<spring:message code='profile.myprofile.supervisorPhone'/>"
														id="profileSupervisorPhone"
														path="supervisorPhone"
														value="${customerData.supervisorPhone}" />
												
												</div>
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div> 
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="register.stepThree.super.email" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<label for="supervisorEmail" class="getErrorMessage"
													data="<spring:message code='register.stepThree.super.email'/>"></label>
												<form:input type="text" data-msg-required="<spring:message code='register.stepThree.super.email'/>"
													class="form-control profile-input-mobile required"
													id="supervisorEmail" name="supervisorEmail" path="supervisorEmail"
													value="${customerData.supervisorEmail}" />
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="register.stepThree.org.name" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>

											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<label for="profileOrganizationName" class="getErrorMessage"
													data="<spring:message code='register.stepThree.org.name'/>"></label>
												<form:input type="text" data-msg-required="<spring:message code='register.stepThree.org.name'/>"
													class="form-control profile-input-mobile required"
													id="profileOrganizationName" path="orgName"
													value="${customerData.orgName}" />
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.department" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<form:select
													class="selectpicker margintop"
													data-width="100%" id="profileDepartment"
													path="department">
													<c:forEach var="department" items="${departments}">
														<option value="${department.key}"
															<c:if test="${department.key eq customerData.department}">selected='true'</c:if>>
															${department.value}</option>
													</c:forEach>
												</form:select>
											</div>
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.role" />
											</div>
											<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12 margintop ">
												<c:forEach var="role" items="${roleMap}">
																${role.value}&nbsp;
															</c:forEach>
											</div>
											<div class="cell">
												<div class="registerError"></div>
											</div>
										</div>
										<c:if test="${success}">
			 									<p class="correctAnswer mar0 profile-updated"><spring:message code="profile.changeSecurityQuestion.changesSaved"/>.</p>
			 							</c:if>
										<div class="row">
											<button type="button" class="btn btnclsactive save-change"
												id="profilSaveupdate"><spring:message
																	code="profile.changepassword.savepassword" /></button>
										</div>

									</div>
								</form:form>
							</div>

</div>
</div>
						<c:if test="${not isSerialUser }">
							<div class="profile-accordian panel">
								<div class="profile-accordian-header">
								<!-- 5508  -->
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse2"
										class="toggle-link panel-collapsed clickontext"><span
										class="glyphicon glyphicon-plus plusicon"></span><spring:message code="register.setpFour.CommunicationPreferences"/></a>
									
								</div>

								<div id="collapse2"	class="profile-accordian-body panel-collapse collapse bordertop">
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<div class="span-18 last" id="myEmailPref">
												<!-- My Profile VIEW START -->
												<div class="sectionBlock myProfilePanel emailPreferences">
													<p>
														<spring:message code="profile.emailPrefrences.preferences" />
													</p>
													<!-- 5509 start-->
													
													<c:url value="/my-account/emailPreferences"	var="emailPrefrenceUrl" />
													<form:form id="emailPreferencesForm" action="${emailPrefrenceUrl}" method="POST"	commandName="emailPrefrenceForm">
													     <div class="row">
														 <div class="col-lg-2 col-md-2 col-sm-3 col-xs-12">
															<div class="inline-block" id="prefer-mobile-label"><spring:message code="register.stepFour.PreferredMobileNumber"/></div> 
													     </div>
												      	<div class="col-lg-10 col-md-10 col-sm-9 col-xs-12" id="pref-mobi-holder">
												      	<c:set var="dateParts" value="${fn:split(customerData.preferredMobileNumber, '|')}" />
																	<div id="prefer-mobi-number-code"><input type="text" id="mobileNumberPrefix" path="preferredMobileNumberPrefix" value="${dateParts[0]}"  class="comboSelection form-control prefer-mobi-number phone-only " name="preferredMobileNumberPrefix" readonly/></div>
																	<div id="prefer-mobi-number"><input id="preferredMobileNumber" name="preferredMobileNumber" value="${dateParts[1]}"  path="preferredMobileNumber" class="form-control prefer-mobi-number phone-only"  readonly/></div>
																	<div class="inline-block" id="prefmob-edit-link"><span  class="hyerlink"><spring:message code="register.emailPreferences.edit"/></span></div>&nbsp;&nbsp;&nbsp;<spring:message code="register.emailPreferences.note"/>
																</div>	</div>
																<div class="margintop" style="margin-bottom:15px">
																	<spring:message code="profile.emailPrefrences.sendEmail" />	:
															</div>
												      	<div class="checkBoxAlignment"><spring:message code="register.emailPreferences.email"/></div> <%-- ${userEmailPrefrenceList} --%>
																<div class="checkBoxAlignment"><spring:message code="register.emailPreferences.test"/></div><%-- ${userSmsPrefrenceList}:${customerData.preferredMobileNumber} --%>
														<div class="checkbox checkbox-info checkboxmargin">
															<c:if test="${not empty emailPrefrencesList}">
																<c:forEach var="emailPrefrence" varStatus="count"	items="${emailPrefrencesList}">
															
																	<c:set var="key" value="${emailPrefrence.key}"></c:set>	
																	<c:set var = "length" value = "${fn:length(emailPrefrence.key)}"/>																	
																	<c:choose>
																			<c:when test="${length=='16'}">
																				<c:set var = "length1" value = "${fn:substring(emailPrefrence.key, 15, 16)}"/>
																				<c:set var = "KeyForSms" value = "smsPrefrence${length1}"/>
																			</c:when>
																			<c:otherwise>
																			<c:set var = "length1" value = "${fn:substring(emailPrefrence.key, 15, 17)}"/>  
																			<c:set var = "KeyForSms" value = "smsPrefrence${length1}"/>     																	
																			</c:otherwise>
																		</c:choose>
																	
																<div class="row">																	
																	<c:if test="${ key ne 'emailPreference7' && key ne 'emailPreference9'	&& key ne 'emailPreference10'}">
																	
																	<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment pull-left">
																	<input type="checkbox" class="styled profileEmailPrefrences" id="check${count.count}"	name="emailPreferences" value="${emailPrefrence.key}"	<c:if test="${userEmailPrefrenceList.contains(key)}">checked</c:if>>	
																		<label for="check${count.count}">
																		</label>
																	</div>
																	
																	<div class=" checkbox checkbox-info checkboxmargin margintop10 pull-left" >
																		<input  class="styled" type="checkbox" value="${KeyForSms}" name="smsPreferences" <c:if test="${userSmsPrefrenceList.contains(KeyForSms)}">checked</c:if>>
																		<label for="">${emailPrefrence.value}	</label>
																	</div>													
																	
																	</c:if>
																	</div>
																<div class="row">
																	<c:if test="${ key eq 'emailPreference7' &&  (customerData.consumerSector=='true' || customerData.noCharge=='true' )}">
																	
																	<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment pull-left">											
																	<input type="checkbox" class="styled profileEmailPrefrences" id="check${count.count}"	name="emailPreferences" value="${emailPrefrence.key}"
																		<c:if test="${userEmailPrefrenceList.contains(key)}">checked</c:if>>
																	<label for="check${count.count}">	
																
																	</label>
																</div>
																<div class=" checkbox checkbox-info checkboxmargin margintop10 pull-left" >
																		<input  class="styled" type="checkbox" value="${KeyForSms}" name="smsPreferences" <c:if test="${userSmsPrefrenceList.contains(KeyForSms)}">checked</c:if>>
																		<label for="">${emailPrefrence.value}	</label>
																	</div>
															</c:if>
															</div>
																<div class="row">
															<c:if test="${ key eq 'emailPreference10' &&  customerData.mddSector=='true'}">
																<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment  pull-left">
																		
																		
																		<input type="checkbox" class="styled profileEmailPrefrences" id="check${count.count}"	name="emailPreferences" value="${emailPrefrence.key}"
																		<c:if test="${userEmailPrefrenceList.contains(key)}">checked</c:if>>
																	<label for="check${count.count}">	
																		</label>
																	</div>
															<div class="myProfileInfo checkbox checkbox-info checkboxmargin margintop10  pull-left">
																	<input  class="styled" type="checkbox" value="${KeyForSms}" name="smsPreferences" <c:if test="${userSmsPrefrenceList.contains(KeyForSms)}">checked</c:if>>
																		<label for="">${emailPrefrence.value}	</label>
																</div>
															
															</c:if>
														</div>
															<div class="row">
																<c:if test="${ key eq 'emailPreference9' &&  customerData.mddSector=='true'}">
																<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment  pull-left">
																		
																		
																		<input type="checkbox" class="styled profileEmailPrefrences" id="check${count.count}"	name="emailPreferences" value="${emailPrefrence.key}"	<c:if test="${userEmailPrefrenceList.contains(key)}">checked</c:if>>
																	<label for="check${count.count}">	
																		</label>
																	</div>
															<div class="myProfileInfo checkbox checkbox-info checkboxmargin margintop10  pull-left">
																	
																<input  class="styled" type="checkbox" value="${KeyForSms}" name="smsPreferences" <c:if test="${userSmsPrefrenceList.contains(KeyForSms)}">checked</c:if>>
																		<label for="">${emailPrefrence.value}	</label>
															</div>
															
															
																
															</c:if>
														</div>	
															<c:if test="${ key eq 'emailPreference6' }">																		
															<div id=radiodivShippedOrder style="display: inline; margin-left: 25px; ">
															<c:choose>
															 	<c:when test="${shippedOrderEmailType eq 'daily'}">
															 		<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "daily"/>
															 			<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly"  class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${shippedOrderEmailType eq 'weekly'}">
															 		<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "weekly"/>
																		<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																	<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "weekly"/>
																		<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>	
															<!-- AAOL-6470-->															
															<!-- <c:if test="${ key eq 'emailPreference9' }">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${backorderEmailType eq 'daily'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "daily"/>
															 			<input type="radio" name="backdialy"  id="backdialy" value="daily"  checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly"   style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${backorderEmailType eq 'weekly'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if> -->
															<c:if test="${ key eq 'emailPreference11'}">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${backorderEmailType eq 'daily'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "daily"/>
															 			<input type="radio" name="backdialy"  id="backdialy" value="daily"  checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly"   style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${backorderEmailType eq 'weekly'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>
															<!--<c:if test="${ key eq 'emailPreference13' }">																		
															<div id=radiodivShippedOrder style="display: inline; margin-left: 25px; ">
															<c:choose>
															 	<c:when test="${shippedOrderEmailType eq 'daily'}">
															 		<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "daily"/>
															 			<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly"  class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${shippedOrderEmailType eq 'weekly'}">
															 		<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "weekly"/>
																		<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																	<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = "weekly"/>
																		<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>	-->													
															<!-- AAOL - 4856 -->
															<c:if test="${ key eq 'emailPreference12'}">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${backorderEmailType eq 'daily'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "daily"/>
															 			<input type="radio" name="backdialy"  id="backdialy" value="daily"  checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly"   style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${backorderEmailType eq 'weekly'}">
															 	 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = "weekly"/>
																		<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>
															<!-- <c:if test="${ key eq 'emailPreference16' }">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${invoiceEmailPreferenceType eq 'daily'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "daily"/>
															 	
															 			<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															
															<<br/>
																</c:when>
															 	<c:when test="${invoiceEmailPreferenceType eq 'weekly'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
															 	
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																												<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
																
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>-->
															
															<!-- <c:if test="${ key eq 'emailPreference15' }">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${deliveryNoteEmailPreferenceType eq 'daily'}">
															 													<form:input type="hidden" id="deliveryNoteEmailPreference" path="deliveryNoteEmailPrefType" value = "daily"/>
															 	
															 		<input type="radio" name="delNotedialy"  id="deldialy" value="daily" class="deldaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="delNotedialy"  id="dekweekly" value="weekly"  class="delweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
															 	<c:when test="${deliveryNoteEmailPreferenceType eq 'weekly'}">
															 													<form:input type="hidden" id="deliveryNoteEmailPreference" path="deliveryNoteEmailPrefType" value = "weekly"/>
															 	
																	<input type="radio" name="delNotedialy"  id="deldialy" value="daily" class="deldaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="delNotedialy"  id="dekweekly" value="weekly" checked="checked" class="delweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																												<form:input type="hidden" id="deliveryNoteEmailPreference" path="deliveryNoteEmailPrefType" value = "weekly"/>
																
																<input type="radio" name="delNotedialy"  id="deldialy" value="daily" class="deldaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="delNotedialy"  id="dekweekly" value="weekly" checked="checked" class="delweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if> -->
															<!-- AAOL-4856 -->

															<!-- AAOL-5520 Start -->
															<!-- AAOL-6470 -->
															<!-- <c:if test="${ key eq 'emailPreference6' }">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${invoiceEmailPreferenceType eq 'daily'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "daily"/>
															 	
															 			<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															
															<<br/>
																</c:when>
															 	<c:when test="${invoiceEmailPreferenceType eq 'weekly'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
															 	
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																												<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
																
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if> -->
															<c:if test="${ key eq 'emailPreference9' }">			
															<div id=radiodiv style="display: inline; margin-left: 25px;">
															<c:choose>
															 	<c:when test="${invoiceEmailPreferenceType eq 'daily'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "daily"/>
															 	
															 			<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															
															<<br/>
																</c:when>
															 	<c:when test="${invoiceEmailPreferenceType eq 'weekly'}">
															 													<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
															 	
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:when>
																<c:otherwise>
																												<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = "weekly"/>
																
																	<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily"  style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
																		<input type="radio" name="invdialy"  id="invweekly" value="weekly" class="invweekly" checked="checked" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															<br/>
																</c:otherwise>
															</c:choose>  
															</div>  
															</c:if>
															<!-- AAOL-5520 End -->
															

														
														</c:forEach>
															</c:if>
															</div>
													<br>
														<c:if test="${successEmail}">
															<p class="correctAnswer mar0 profile-updated">
																<spring:message
																	code="profile.changeSecurityQuestion.changesSaved" />
															</p>
														</c:if>
														<div class="buttonWrapperWithBG myProfileButtonWrapper ">
															<button type="submit"
																class="btn btnclsactive save-change buttonWrapperWithBG myProfileButtonWrapper"
																id="emailPreferencesPageSubmit">
																<spring:message
																	code="profile.changepassword.savepassword" />
															</button>
														</div>
													</form:form>
												</div>

											</div>
										</div>
									</div>
								</div>
							</div>
							
							</c:if>
							
							<c:if test="${not isSerialUser }">
							<!--  4659  -->
							<div class="profile-accordian panel">
								<div class="profile-accordian-header">
									<a data-toggle="collapse" data-parent="#accordion" href="#collapse31" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span>
										<spring:message code="profile.defaultAcc.defaultOrder"></spring:message></a>
										</div>
											<div id="collapse31" class="profile-accordian-body panel-collapse collapse bordertop">										
												<c:url var="updateDefaultAccAndOrder"	value="/my-account/updateDefaultAccAndOrder" />
												<form:form id="updateDefaultAccAndOrder" action="${updateDefaultAccAndOrder}"
													method="POST" commandName="defaultAccAndOrderForm"
													autocomplete="false">
												<div class="row">	
													<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12"><span class="label-middle"><spring:message code="profile.defaultaccount.title" /></span></div>
													<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
														<form:select class="selectpicker" id="b2bUnitId" data-width="100%" path="defaultAccount">
														<option value=""><spring:message code="cart.review.select" /></option>
															<c:forEach var="B2bunit" items="${customerData.b2bUnits}">
																<option value="${B2bunit.uid}"
																	<c:if test="${customerData.defaultB2BUnitID eq B2bunit.uid}">selected='true'</c:if>>${B2bunit.uid},${B2bunit.name}</option>
															</c:forEach>
														</form:select>
													</div>
													</div>
													<div class="row">	
														<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop"><span class="label-middle"><spring:message code="profile.defaultorderType.title" /></span></div>
														<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
															<form:select class="selectpicker margintop" data-width="100%" id="orderType" path="defaultOrder">
																<option value=""><spring:message code="cart.review.select" /></option>
																<c:forEach var="orderType" items="${orderTypeList}">
																	<option value="${orderType}"
																	<c:if test="${customerData.defaultOrderType eq orderType}">selected='true'</c:if>>
																	<spring:message code="cart.common.orderType.${orderType}"></spring:message>

																</c:forEach>												
															</form:select>
														</div>
													</div>
													
													<c:if test="${successdflt=='true'}">
															<label class="correctAnswer mar0 profile-updated cell"><spring:message
																	code="profile.changeSecurityQuestion.changesSaved" /></label>
													</c:if>
													<div class="row">
													    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
															 <div id="orderType-savemsg"><spring:message code="profile.defaultorderType.Savemsg" /></div>
															<button type="submit" id="saveDefaultB2bUnitAndOrder"  class="btn btnclsactive save-change"><spring:message code="profile.defaultorderType.Savechange" /></button>
														</div>		
													</div>
													</form:form>
												</div>
							</div>

								<!-- 4659  -->
							</c:if>
							<div class="profile-accordian panel">
								<div class="profile-accordian-header">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse3"
										class="toggle-link panel-collapsed clickontext"><span
										class="glyphicon glyphicon-plus plusicon"></span><spring:message code="profile.changePassword.header" /></a>
								</div>
								<div id="collapse3"
									class="profile-accordian-body panel-collapse collapse bordertop">
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

											<c:url value="/my-account/updatePassword"
												var="updatePasswordAction" />
											<form:form id="ProfileChangePassword"
												name="ProfileChangePassword"
												commandName="updatePasswordForm" method="POST"
												action="${updatePasswordAction}">
												<div><spring:message code="profile.changePassword.useform" /></div>
												<div class="margintop">
													<label for="checkpass"><spring:message code="profile.changePassword.currentpassword" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
												</div>
											<%-- 	<label for="checkpass" class="getErrorMessage" data="<spring:message code='profile.changepassword.datamsg'/>"></label> --%>
												<form:password class="required form-control pwdfield" id="checkpass"
													path="currentPassword"
													data-msg-required="Please Enter Current Password." />
												<div class="registerError"></div>
												<div class='Newpassword'>
									                <div for='regpwd' class="margintop10"><spring:message code="profile.changePassword.newpassword" /><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></div>
									                <div class='pwdwidgetdiv margintop5' id='thepwddiv' data-toggle="tooltip"
													data-placement="top"
													title="Password Strength Your password must be 8 characters or longer and have two of the following four complexity classes: * Uppercase letters (A-Z)  * Lowercase letters (a-z)  * Numerals (0-9) * Non-alphabetic characters (such
															as !,#,$,%)"></div>
									               
									                <noscript>
									                <div>	
									                 <label for="newPassword"><spring:message code="profile.changePassword.newpassword" /><span
										class="redStar">*</span></label> 
									                	<form:password class=" required form-control pwdfield"
														id="newPassword" path="newPassword"
														data-msg-required="<spring:message code='profile.newPassword'/>"   name='regpwd'  /></div>  
									                </noscript>
									                
									               </div>
											
												<div class="registerError"></div><br>
				
										<div>
												<div class="reEnter" for="checkpass2">
													<spring:message code="profile.changePassword.renter-newpassword" />:<span class="redStar"><spring:message code="register.stepOne.required.star"/></span>
												</div>
												 
												<form:password class="required form-control pwdfield"
													id="checkNewPassword" path="checkNewPassword" data-msg-required="Re-Enter New Password!"  />
													<label for="checkNewPassword" class="getErrorMessage"
													data="<spring:message code='profile.changepassword.datamsg'/>"></label> 
										</div>
												<div class="recPassError">
													<c:choose>
														<c:when test="${successPwd}">
															<p class="correctAnswer mar0 profile-updated">
																<spring:message
																	code="profile.changePassword.successMessage" />
															</p>
														</c:when>
														<c:when
															test="${successPwd=='false' && status eq 'oldPassword'}">
															<label class="error"><spring:message
																	code="profile.changePassword.errormessage.oldPasswords" /></label>
														</c:when>
														<c:when
															test="${successPwd=='false' && status eq 'passMismatch'}">
															<label class="error"><spring:message
																	code="profile.changePassword.errormessage.wrongPassword" /></label>
														</c:when>
													</c:choose>
												</div>
												<div class="buttonWrapperWithBG myProfileButtonWrapper">
													<button type="submit" id="emailPreferencesSubmit"
														class="primarybtn btn btnclsactive save-change">
														<spring:message code="profile.changepassword.savepassword" />
													</button>
												</div>
											</form:form>

										</div>
									</div>
								</div>
							</div>



							<div class="profile-accordian panel">
								<div class="profile-accordian-header">
									<a data-toggle="modal" data-target="#securitycheckpopup"
										class="clickontext table-cell menubody-heightcheck" href="#" id="changesecuritypsd"> <span
										id="change-security-sign"
										class="glyphicon glyphicon-plus plusicon table-cell"></span> <span
										class="table-cell"><spring:message
												code="profile.changeSecurityQuestion.text" /></span>
									</a>
								</div>
								<div id="collapse4"
									class="profile-accordian-body panel-collapse collapse bordertop">
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<div>
												<spring:message
													code="profile.changeSecurityQuestion.updateQuestions" />
												.
											</div>
										</div>
									</div>

									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
											<c:url var="updateSecretQuestion"
												value="/my-account/secret-questions" />
												
												
											<form:form action="${updateSecretQuestion}" method="post"
												commandName="secretQuestionForm"
												id="myprofilesecretquescheck">
												<c:forEach varStatus="status" begin="0" end="2">
													<c:choose>
														<c:when test="${status.index lt 3}">
															<div class="row">
																<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
																	<div class="row">
																	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 secret-quest-dropdown-label no-margin">
																		<formElement:jnjSecretQuestionFormBox
																			idKey="questionList[${status.index}].code"
																			items="${secretQuestions}" itemLabel="question"
																			labelKey="profile.secret.question${status.index}"
																			path="questionList[${status.index}].code"
																			mandatory="true"
																			errorMsg="${secretQuestionForm.errorMsgForQuestion}"
																			selectCSSClass="required selectpicker select-security-ques" />

																		<input type="hidden" id="hddnQuestionErrorMsg"
																			value="<spring:message code="error.secret.question.required" />">
																		<input type="hidden" id="hddnAnswerErrorMsg"
																			value="<spring:message code="error.secret.answer.required" />">
																	</div>
																	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 errorPosition errorPosition-profile">
																		<div class="registerError"></div>
																	</div>
																	</div>
																	<div class="row">
																		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 secret-quest-text-label">
																			<formElement:jnjProfileInputBox
																				idKey="profile.secret.answer${status.index}"
																				path="questionList[${status.index}].answer"
																				inputCSS="required form-control" mandatory="true"
																				errorMsg="${secretQuestionForm.errorMsgForAnswer}" />
																		</div>
																		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 errorPosition">
																			<div class="registerError"></div>
																		</div>
																	</div>
																</div>
															</div>
														</c:when>

														<c:otherwise>

															<formElement:jnjSecretQuestionFormBox
																idKey="profile.secret.question${status.index}"
																items="${secretQuestions}" itemLabel="question"
																labelKey="profile.secret.question${status.index}"
																path="questionList[${status.index}].code"
																mandatory="false" />

														
															<div class="size3of7 cell">
																<formElement:jnjProfileInputBox
																	idKey="profile.secret.answer${status.index}"
																	path="questionList[${status.index}].answer"
																	mandatory="false" />
															</div>
															 


														</c:otherwise>
													</c:choose>
												</c:forEach>

												<div class="buttonWrapperWithBG myProfileButtonWrapper row">
													<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<button type="submit"
															class="btn btnclsactive save-change primarybtn"
															id="securityQuestionSubmit">
															<spring:message
																code="profile.changepassword.savepassword" />
														</button>

														<c:if test="${successQue=='true'}">
															<label class="correctAnswer mar0 profile-updated cell"><spring:message
																	code="profile.changeSecurityQuestion.changesSaved" /></label>
														</c:if>
													</div>
												</div>
											</form:form>
										</div>

									</div>
								</div>
							</div>

							<c:if test="${not isSerialUser }">
							<div class="profile-accordian panel">
								<div class="profile-accordian-header">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse5"
										class="toggle-link panel-collapsed clickontext"><span
										class="glyphicon glyphicon-plus plusicon"></span><spring:message code="Myprofile.addaccount.text"/></a>
								</div>
								<div id="collapse5"
									class="profile-accordian-body panel-collapse collapse bordertop">
									<registration:profileaccount />
									<div class="span-18 last rdiCont" id="myAddAccountExisting">
										<div class="sectionBlock addAccountExisting">
											<c:url value="/my-account/addNewAccount"
												var="updatePasswordAction" />
											<form:form id="addAccountExistingForm"
												name="addAccountExistingForm" commandName="addaccountForm"
												method="POST" action="${updatePasswordAction}">

												<div class="row">

													<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<div><spring:message code="profile.changeSecurityQuestion.selectone" />:</div>
														<div class="radio radio-info">
															<input type="radio" id="inlineRadio1"
																class="menubody-heightcheck accessToAnotherAccount"
																value="option1" name="radioInline"> <label
																for="inlineRadio1"> <spring:message code="profile.addAccount.userAccess" /></label>
														</div>
														<div class="radio radio-info">
															<input type="radio" id="inlineRadio2"
																class="menubody-heightcheck accessToNewAccount"
																value="option1" name="radioInline"> <label
																for="inlineRadio2"> <spring:message code="profile.addAccount.newUser" /></label>
														</div>
														<div class="another-account-number jnj-content-hidden">
															<div>
																<label for="nameOrNumberOfExistingAccount"><spring:message
																		code="profile.myprofile.account" /><span>*</span> <br />
																	<i><spring:message
																			code="profile.myprofile.instruction" /></i> </label>
															</div>
															<div>
																<form:input type="text"
																	class="required form-control accnobox  margintop5"
																	id="nameOrNumberOfExistingAccount"
																	path="nameOrNumberOfExistingAccount" data-msg-required="<spring:message code='cart.error.global.message'/>"/>
																<label for="nameOrNumberOfExistingAccount" class="getErrorMessage"
																	data="<spring:message code='cart.error.global.message'/>"></label>
															</div>
														</div>
													</div>

												</div>
												<div class="row">
												<div>

														<div class="cellReg permLevel choose-product">
															<p>
																<spring:message code="profile.myprofile.question1" />
															</p>
															<div class="radio radio-info radio-inline">
																<form:radiobutton path="sector" value="MDD"
																	name="viewOnly" id="inlineRadio3 " />
																<label for="viewOnly"><spring:message
																		code="register.stepOne.mdd" /></label>
															</div>
															<div class="radio radio-info radio-inline">
																<form:radiobutton path="sector" name="viewOnly"
																	value="CONSUMER" id="inlineRadio4 placeOrder" />
																<label for="placeOrder"><spring:message
																		code="profile.addAccount.products" /></label>
															</div>
															<div class="radio radio-info radio-inline">
																<form:radiobutton path="sector" name="viewOnly"
																	value="PHARMA" id="inlineRadio5 pharma" />
																<label for="pharma"><spring:message
																		code="register.stepOne.pharma" /></label>
															</div>
														</div>

													</div>
													<registration:companyInformation className="required" />

													<div
														class="col-lg-12 col-md-12 col-sm-12 col-xs-12 buttonWrapperWithBG sectionBlock myProfileButtonWrapper">
														<button type="button"
															class="primarybtn btn btnclsactive save-change"
															id="addAccountExistingSubmit">
															<spring:message code="profile.addAccount.submitRequest" />
														</button>
													</div>

												</div>
											</form:form>
										</div>
									</div>
								</div>
							</div>
						</c:if>

						</div>
					
				</div>
			</div>
		</div>

			<!-- 	Security Check pop-up Starts -->

		<div class="modal fade" id="securitycheckpopup" role="dialog"
			data-firstLogin='true'>
			<div class="modal-dialog modalcls">
				<div class="modal-content security-popup">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							id="security-check-close"><spring:message code="profile.changeSecurityQuestion.close"/></button>
						<h4 class="modal-title selectTitle"><spring:message code="profile.changeSecurityQuestion.check"/></h4>
					</div>
					<c:url var="verifyCurrentPassword"	value="/my-account/verifyCurrentPassword" />
						<form:form id="changeQuestionForm" commandName="checkPassword"	method="POST" action="${verifyCurrentPassword}">
							
								<div>
									
									<div class="modal-body">
									<spring:message code='profile.pwd.invalid' var="invalidPwd"/>
										<form:input type="password" id="chkPassword securityQuestionAjax" class="form-control required"
											path="currentPassword" data-msg-required="<spring:message code='profile.changeSecurityQuestion.passwordRequired'/>" 
											placeholder="${invalidPwd}" />
										<div class="registerError jnj-security-check"  data-securityFlag='${successChk}' style="margin-top:5px">
											<c:if test="${successChk=='false'}">
												<label class="error"><spring:message code="profile.changeSecurityQuestion.passwordnotvalid"/></label>
											</c:if>
										</div>	
									</div>

										
								</div>
							<div class="buttonWrapperWithBG myProfileButtonWrapper modal-footer ftrcls">
								<a href="<c:url value='/my-account/personalInformation'/>"
									class="tertiarybtn pull-left security-check-cancel-btn"><spring:message code="profile.cancel"/></a> 
									<input class="btn btnclsactive panel-collapsed  security-pop-up-link toggle-link secondarybtn"
									value="<spring:message code="profile.ok"/>" type="submit" id="changeQuestionPasswordSubmit" />
							</div>
						</form:form>
						
						
				</div>
			</div>
		</div>
		
					<!-- 	Security Check pop-up Ends -->
		
</template:page>
