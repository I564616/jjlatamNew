<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalprofile/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="formElement"	tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/form"%>
<%@ taglib prefix="registration" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<div id="profilepage">
	    <input type="hidden" value="true" id="isProfilePage" />
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row content">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code="Myprofile.myprofile.text" />
			</div>
		</div>
		<!--Begin: Success or error messages -->
		<div id="myprofileHeaderMessages" class="recPassError error">
			<c:if test="${success or successEmail or successQue}">
			<div id ="success1">
				<lacommon:genericMessage messageCode="profile.myprofile.changesSaved" icon="ok" panelClass="success" />
				</div>
			</c:if>
			<c:if test="${successPwd}">
			<div id ="success2">
				<lacommon:genericMessage messageCode="profile.myprofile.changePassword.success" icon="ok" panelClass="success" />
				</div>
			</c:if>
            <c:choose>
                <c:when test="${successPwd=='false' && status eq 'oldPassword'}">
                <div id ="error1">
                    <lacommon:genericMessage messageCode="profile.changePassword.errormessage.oldPasswords" icon="ban-circle" panelClass="danger" />
               </div>
                </c:when>
                <c:when test="${successPwd=='false' && status eq 'passMismatch'}">
                <div id ="error2">
                    <lacommon:genericMessage messageCode="profile.changePassword.errormessage.wrongPassword" icon="ban-circle" panelClass="danger" />
                </div>
                </c:when>
            </c:choose>
        </div>
		<!--End: Success or error messages -->
		<div class="boxshadow">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion">
					<div class="profile-accordian panel">
						<div class="profile-accordian-header">
							<a data-bs-toggle="collapse" data-parent="#accordion"
							    id="personalInfoAcrdn"
								href="#myPersonalInfo"
								class="toggle-link panel-collapsed clickontext">
								<i class="bi bi-plus" style="-webkit-text-stroke: 1px;"></i>
								 <spring:message
									code="profile.personalinformation.header" /></a>
						</div>
						<!-- 	Personal Information  -->
						<div
							class="span-18 last profile-accordian-body panel-collapse collapse bordertop"
							id="myPersonalInfo">
							<div id="myprofileview" class="myProfilePanel">
								<div class="myProfileGlobalError registerError">
									<label class="error" style="display: none;"><spring:message
											code="profile.profile.text" />.<br /> </label>
								</div>
								<c:url value="/my-account/personalInformation"
									var="personalInfomationUrl" />
								<div class="profileAcknowledgementTopClass">
									<spring:message code="profile.page.acknowledgement.top" />
								</div>
								<form:form id="myprofilecheck" action="${personalInfomationUrl}"
									method="POST" modelAttribute="updateProfileForm"
									autocomplete="false">
									<div>
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
												:
											</div>
											<div class="col-lg-4 col-md-6 col-sm-6  col-xs-12 margintop ">${customerData.email}</div>
										</div>


										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.phoneNumber" />
												:<sup class="star">*</sup>
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
												<div class="boxtext1">
													<form:input type="text"
														class="form-control margintop profile-input-mobile numbersOnlyInput"
														id="profilePhoneNumberPrefix" path="phoneNumberPrefix"
														value="${customerData.contactAddress.phoneCode}"
														onchange="removeNonNumbers('#profilePhoneNumberPrefix')"
														maxlength="5"/>
												</div>
												<div class="boxtext2">
													<label for="profilePhoneNumber" class="getErrorMessage"
														data="<spring:message code='register.stepThree.required.phone'/>"></label>
													<form:input type="text"
														class="form-control required comboInput required accp-PhoneNumber removeAdditionalText numbersOnlyInput"
														id="profilePhoneNumber" path="phone"
														data-msg-required="Please enter Phone Number!"
														value="${customerData.contactAddress.phone}"
														onchange="removeNonNumbers('#profilePhoneNumber')"
													    maxlength="15"/>
												</div>
											</div>
											<div class="cell">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.mobileNumber" />
												:<sup class="star">*</sup>
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 ">
												<div class="boxtext1">
													<form:input type="text"
														class="form-control margintop profile-input-mobile numbersOnlyInput"
														id="profileMobileNumberPrefix" path="mobileNumberPrefix"
														value="${customerData.contactAddress.mobileCode}"
														onchange="removeNonNumbers('#profileMobileNumberPrefix')"
														maxlength="5"/>
												</div>
												<div class="boxtext2">
													<label for="profileMobileNumber" class="getErrorMessage"
														data="<spring:message code='register.stepThree.required.mobile'/>"></label>
													<form:input type="text"
														class="comboInput removeAdditionalText form-control accp-PhoneNumber numbersOnlyInput"
														id="profileMobileNumber" path="mobile"
														value="${customerData.contactAddress.mobile}"
														onchange="removeNonNumbers('#profileMobileNumber')"
														maxlength="15"/>
												</div>
											</div>
											<div class="cell">
												<div class="registerError"></div>
											</div>
										</div>

										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.page.acknowledgement.language" />
												:<sup class="star">*</sup>
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop">
												<form:select id="language" path="country" class="required"
													data-width="100%" style="min-width: 185px; width: 185px;">
													<c:out value=""></c:out>
													<c:forEach var="lang" items="${languages}">
														<c:choose>
															<c:when test="${lang.isocode == currentLanguage.isocode}">
																<option value="${lang.isocode}" selected="selected" lang="${lang.isocode}">
																	${lang.nativeName}
																</option>
															</c:when>
															<c:otherwise>
																<option value="${lang.isocode}" lang="${lang.isocode}">
																	${lang.nativeName}
																</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</form:select>
											</div>
											<div class="registerError"></div>
										</div>
										<div class="row">
											<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 margintop">
												<spring:message code="profile.myprofile.role" />
												:
											</div>
											<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12 margintop ">
												<c:forEach var="role" items="${roleMap}">
																<p>${role.value}&nbsp;</p>
                                                </c:forEach>
											</div>
											<div class="cell">
												<div class="registerError"></div>
											</div>
										</div>
										<div class="row">
											<div class="profileAcknowledgementBottomClass">
												<label for="checkAcknowledgement1">
                                                    <input type="checkbox"
                                                    onclick="validateSaveButton(this, 'profilSaveupdate')"
                                                    id="checkAcknowledgement1">
												    <spring:message code="profile.page.acknowledgement.bottom" />
                                                </label>
											</div>
										</div>

										<div class="buttonWrapperWithBG myProfileButtonWrapper">
											<button type="submit" class="btn btnclsactive save-change"
												disabled="disabled" id="profilSaveupdate">
												<spring:message code="profile.save.updates" />
											</button>
										</div>
									</div>
								</form:form>
							</div>
						</div>
					</div>

					<!-- 	Email Preferences  -->
					<div class="profile-accordian panel">
						<div class="profile-accordian-header">
							<a data-bs-toggle="collapse" data-parent="#accordion"
								href="#collapse2"
								class="toggle-link panel-collapsed clickontext">
								<i class="bi bi-plus" style="-webkit-text-stroke: 1px;"></i>
								<spring:message code="register.setup.for.communication.preference" />
						    </a>
						</div>
						<div id="collapse2"
							class="profile-accordian-body panel-collapse collapse bordertop">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="span-18 last" id="myEmailPref">
										<!-- My Profile VIEW START -->
										<div class="profileAcknowledgementTopClass">
											<spring:message code="profile.page.acknowledgement.top" />
										</div>
										<div class="sectionBlock myProfilePanel emailPreferences">
											<p>
												<spring:message code="profile.myprofile.emailPrefrences.preferences" />
											</p>
											<c:url value="/my-account/laEmailPreferences" var="emailPreferenceUrl" />

                                            <form:form id="emailPreferencesForm" action="${emailPreferenceUrl}" method="POST" modelAttribute="emailPrefrenceForm" onsubmit="return validateEmailPrefrenceForm()">

									    	    <div class="myProfileInfo checkbox checkbox-info checkboxmargin margintop10">
								    			    <c:if test="${not empty emailPrefrencesList}">
							    					    <c:forEach var="emailPreference" varStatus="count" items="${emailPrefrencesList}">
						    							    <c:set var="key" value="${emailPreference.key}"/>
					    								    <input type="checkbox" class="styled profileEmailPrefrences" id="check${count.count}" name="emailPreferences" value="${emailPreference.key}"
                                                            <c:if test="${fn:contains(userEmailPrefrenceList,key)}">checked</c:if>
														    >
														    <label for="check${count.count}">${emailPreference.value} </label>
														    <br>
													    </c:forEach>
													</c:if>
												</div>

                                                <br/>
                                                <c:if test="${displayOrderNotificationPreference}">
                                                    <spring:message code="profile.myprofile.emailPrefrences.periodicity"/>
												    <br/>
                                                    <label>
                                                        <input type="radio" ${emailPeriodicity eq "NONE" ? "checked" : ''} name="emailPeriodicity" value="NONE" />
													    <spring:message code="profile.myprofile.emailPrefrences.periodicity.none"/> &nbsp;
                                                    </label>
												    <br/>
                                                    <label>
                                                        <input type="radio" ${emailPeriodicity eq "IMMEDIATE" ? "checked" : ''} name="emailPeriodicity" value="IMMEDIATE" />
													    <spring:message code="profile.myprofile.emailPrefrences.periodicity.immediate"/> &nbsp;
                                                    </label>
												    <br/>
                                                    <label>
                                                        <input type="radio" ${emailPeriodicity eq "DAILY" ? "checked" : ''} name="emailPeriodicity" value="DAILY" />
													    <spring:message code="profile.myprofile.emailPrefrences.periodicity.daily"/> &nbsp;
                                                    </label>
												    <br/>
                                                    <label>
                                                        <input class="ConsolidatedRadio" type="radio" ${emailPeriodicity eq "CONSOLIDATED" ? "checked" : ''} name="emailPeriodicity" value="CONSOLIDATED" />
													    <spring:message code="profile.myprofile.emailPrefrences.periodicity.consolidated"/>
                                                    </label>
                                                </c:if>
												<br>
												<!-- Consolidated Email Story AFFG-25871 -->
												<input type="hidden" id="varOrderTypes" data-ordertypedefault="${currentAccountPreferenceData.orderTypes}" value="" />
                                                <input type="hidden" id="varMonthdays" data-monthDay="${currentAccountPreferenceData.daysOfTheMonth}" value="" />
                                                <input type="hidden" id="varWeekDay" data-weekDay="${currentAccountPreferenceData.dayOfTheWeek}" value="" />
                                                <input type="hidden" id="orderList" name="orderTypes" value="" />
                                                <input type="hidden" id="CalenderDate" name="daysOfTheMonth" value="" />

                                                <div class="ConsolidatedEmail" style="display: none; width: 100%;">
                                                    <div class="row marTopWantToShow">

                                                        <!-- Order Type select option  -->
                                                        <div class="col-lg-12 mt-4 col-md-12 col-sm-12 col-xs-12">
                                                            <p>
                                                                <strong>
                                                                    <spring:message code="profile.myprofile.consolidateEmail.orderType.heading"/>
                                                                </strong>
                                                            </p>
                                                        </div>
                                                        <div class="col-lg-4 col-md-4 col-sm-5 col-xs-12 mt-2">
                                                            <label for="OrderType"><spring:message code="profile.myprofile.consolidateEmail.orderType"/></label>
                                                        </div>
                                                        <div class="OrderTypeClass col-lg-8 col-md-8 col-sm-8 col-xs-12">
                                                            <select class="selectpicker OrderTypeCheckBox" multiple data-selected-text-format="count">
                                                              <c:forEach items="${orderTypes}" var="orderTypeCode">
                                                                <option <c:if test="${fn:contains(currentAccountPreferenceData.orderTypes, orderTypeCode.key)}">selected</c:if>
                                                                    value="${orderTypeCode.key}">${orderTypeCode.value}
                                                                </option>
                                                              </c:forEach>
                                                            </select>
                                                        </div>
                                                        <!-- Reporting Start -->
                                                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 mt-4">
                                                            <p><strong><spring:message code="profile.myprofile.consolidateEmail.reporting"/></strong></p>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 reportSubheading">
                                                                <div class="row">
                                                                    <div class="frequencyClick  mt-4 ml-3 col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                                                      <input checked type="radio" id="frequencyDaily"
                                                                          ${jnJEmailFrequency eq "DAILY" ? "checked" : ''}
                                                                          name="jnJEmailFrequency"
                                                                          value="DAILY" />
                                                                      <label for="frequencyDaily"><spring:message code="profile.myprofile.consolidateEmail.daily"/></label>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="frequencyClick mt-4 ml-3 col-lg-12 col-md-12 col-sm-12 col-xs-12"">
                                                                      <input type="radio" id="frequencyWeekly"
                                                                          ${jnJEmailFrequency eq "WEEKLY" ? "checked" : ''}
                                                                          name="jnJEmailFrequency"
                                                                          value="WEEKLY" />
                                                                      <label for="frequencyWeekly"><spring:message code="profile.myprofile.consolidateEmail.weekly"/></label>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                     <div class="frequencyClick mb-5 mt-4 ml-3 col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                                                         <input type="radio" id="frequencyMonthly"
                                                                             ${jnJEmailFrequency eq "MONTHLY" ? "checked" : ''}
                                                                             name="jnJEmailFrequency"
                                                                             value="MONTHLY" />
                                                                         <label for="frequencyMonthly"><spring:message code="profile.myprofile.consolidateEmail.monthly"/></label>
                                                                     </div>
                                                                </div>
                                                            </div>
                                                            <!-- Calendar and weekDay table  -->
                                                            <div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
                                                                <div class="row">
                                                                    <div class="weeklyClassHeight mt-5 col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                                                        <div class="row weeklyDaysSection">
                                                                        <input class="noSelect" type="hidden" value="notSelected" />
                                                                         
							                                              <c:forEach items="${weekDay}" var="weekdays" varStatus="loop">
									                                         <div id="${weekdays.key}" class="form-check text-center">
                                                                                     <label for="${weekdays.key}">${weekdays.value}</label><br>
                                                                                     <div class="form-check">
                                                                                     <input class="form-check-input weekDay" type="radio" value="${weekdays.key}" name="dayOfTheWeek" />
                                                                                     </div>
                                                                                 </div>
									                                      </c:forEach>
																			
                                                                        </div>
                                                                        <p class="weekErrorMsg">
                                                                            <spring:message code="profile.myprofile.consolidateEmail.weekday.errorMsg"/>
                                                                        </p>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                     <div class="displayCalender col-lg-4 col-md-12 col-sm-12 col-xs-12 p-0">
                                                                            <div class="calender">
                                                                                    <div class="calenderHeading">
                                                                                        <p>
                                                                                            <spring:message code="profile.myprofile.consolidateEmail.calender.heading"/>
                                                                                        </p>
                                                                                    </div>

                                                                                   <ul class="days">
                                                                                       <c:forEach begin="1" end="29" varStatus="calenderDay">
                                                                                           <li value="${calenderDay.index}"><span>${calenderDay.index}</span></li>
                                                                                       </c:forEach>
                                                                                       <li value="30" data-toggle="tooltip" data-placement="bottom"
                                                                                           title="<spring:message code="profile.myprofile.consolidateEmail.calender.tooltip"/>">
                                                                                           <span>30</span>
                                                                                       </li>
                                                                                       <li value="31" data-toggle="tooltip" data-placement="bottom"
                                                                                           title="<spring:message code="profile.myprofile.consolidateEmail.calender.tooltip"/>">
                                                                                           <span>31</span>
                                                                                       </li>
                                                                                   </ul>
                                                                            </div>
                                                                            <p class="calendarErrorMsg">
                                                                                <spring:message code="profile.myprofile.consolidateEmail.calender.errorMsg"/>
                                                                            </p>
                                                                     </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                     </div>
                                                </div>
                                                <!-- end story AFFG-25871  -->

										        <div class="row">
												    <div class="profileAcknowledgementBottomClass">
                                                        <label for="checkAcknowledgement2">
                                                            <input type="checkbox" onclick="validateSaveButton(this, 'emailPreferencesPageSubmit')" id="checkAcknowledgement2">
                                                            <spring:message code="profile.page.acknowledgement.bottom" />
                                                        </label>
													</div>
											    </div>

												<div class="buttonWrapperWithBG myProfileButtonWrapper ">
									    	        <button type="submit" disabled="disabled" class="btn btnclsactive save-change buttonWrapperWithBG myProfileButtonWrapper" id="emailPreferencesPageSubmit">
												        <spring:message code="profile.save.updates" />
											        </button>
											    </div>
											</form:form>

										</div>

									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- 	Change Password  -->
					<div class="profile-accordian panel">
						<div class="profile-accordian-header">
							<a data-bs-toggle="collapse" data-parent="#accordion"
								href="#collapse3"
								class="toggle-link panel-collapsed clickontext">
								<i class="bi bi-plus" style="-webkit-text-stroke: 1px;"></i>
								<spring:message
									code="profile.changePassword.header" />
							</a>
						</div>
						<div id="collapse3"
							class="profile-accordian-body panel-collapse collapse bordertop">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

									<c:url value="/my-account/updatePassword"
										var="updatePasswordAction" />

									<div class="profileAcknowledgementTopClass">
										<spring:message code="profile.page.acknowledgement.top" />
									</div>
									<form:form id="laProfileChangePassword"
										name="laProfileChangePassword" modelAttribute="updatePasswordForm"
										method="POST" action="${updatePasswordAction}">
										<label for="checkpass">
										    <spring:message code="profile.changePassword.currentpassword" />
										    <sup class="star">*</sup>
                                        </label>
										<label for="checkpass" class="getErrorMessage"
											data="<spring:message code='profile.changepassword.datamsg'/>"></label>

											<spring:message code="profile.changePassword.strength" var="changePasswordStrength"/>
											<spring:message code="profile.changePassword.current.required" var="changePasswordCurrentRequired"/>
											<spring:message code="profile.changePassword.newpassword.required" var="changePasswordNewRequired"/>
										<form:password class=" form-control pwdWidth"
											id="checkpass" path="currentPassword" />
										<div class="registerError"></div>

										

										<div class='Newpassword'>
											<div for='regpwd' class="margintop10">
												<spring:message code="profile.changePassword.newpassword" />
												<sup class="star">*</sup>
											</div>
											<div class='lapwdwidgetdiv margintop5' id='thepwddiv' data-toggle="tooltip" data-placement="right" title="${complexityMessage}">></div>
											<noscript>
												<div>
													<label for="newPassword"><spring:message
															code="profile.changePassword.newpassword" /><span
														class="redStar">*</span></label>
													<form:password class="form-control pwdWidth"
														id="newPassword" path="newPassword"
														name='regpwd' title="${changePasswordStrength}"/>
												</div>
											</noscript>
										</div>
										<div class="registerError"></div>
										<br>
										<div>
											<div class="reEnter" for="checkpass2">
												<spring:message
													code="profile.changePassword.newpassword.required2" />
												:<sup class="star">*</sup>
											</div>
											<label for="checkNewPassword" class="getErrorMessage"
												data="<spring:message code='profile.changepassword.datamsg'/>"></label>
											<form:password class=" form-control pwdWidth"
												id="checkNewPassword" path="checkNewPassword" title="${changePasswordStrength}"/>
										</div>

										<div class="recPassError"></div>

										<div class="row">
											<div class="profileAcknowledgementBottomClass">
												<label for="checkAcknowledgement3">
                                                    <input type="checkbox"
                                                    onclick="validateSaveButton(this, 'changePasswordSubmit')"
                                                    id="checkAcknowledgement3">
												    <spring:message code="profile.page.acknowledgement.bottom" />
                                                </label>
											</div>
										</div>
										<div class="buttonWrapperWithBG myProfileButtonWrapper">
											<button type="submit" disabled="disabled" id="changePasswordSubmit"
												class="primarybtn btn btnclsactive save-change">
												<spring:message code="profile.save.updates" />
											</button>
										</div>
									</form:form>
								</div>
							</div>
						</div>
					</div>

					<!-- 	Change Security Questions -->
					<div class="profile-accordian panel">
						<div class="profile-accordian-header">
							<a data-bs-toggle="collapse" data-parent="#accordion"
								href="#collapse4"
								class="toggle-link panel-collapsed clickontext"
								id="securityQuestionsAnchor">
								<i class="bi bi-plus" style="-webkit-text-stroke: 1px;"></i>
                                <spring:message code="profile.changeSecurityQuestions.header" />
							</a>
						</div>
						<div id="collapse4"
							class="profile-accordian-body panel-collapse collapse bordertop">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
									<c:url var="updateSecretQuestion"
										value="/my-account/secret-questions" />
									<div class="profileAcknowledgementTopClass">
										<spring:message code="profile.page.acknowledgement.top" />
									</div>
									<br>
                                    <div  id="registrationCompleteLabel">
                                        <div class="marTop10 marBott10" >
			 						        <lacommon:genericMessage messageCode="profile.changeSecurityQuestions.must" icon="ban-circle" panelClass="danger"  />
									    </div>
									 </div>
			
									<form:form action="${updateSecretQuestion}" method="post"
										modelAttribute="secretQuestionForm" id="myprofilesecretquescheck">
										<c:forEach varStatus="status" begin="0" end="2">
											<c:choose>
												<c:when test="${status.index lt 3}">
													<div class="row">
														<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
															<div class="secret-quest-dropdown-label">
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
																<div class="cell">
																	<div class="registerError errorPosition"></div>
																</div>
															</div>
															<div class="secret-quest-text-label">
																<formElement:jnjProfileInputBox
																	idKey="profile.secret.answer${status.index}"
																	path="questionList[${status.index}].answer"
																	inputCSS="required form-control" mandatory="true"
																	errorMsg="${secretQuestionForm.errorMsgForAnswer}" />
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
										<div class="row">
											<div class="profileAcknowledgementBottomClass">
												<label for="checkAcknowledgement4">
                                                    <input type="checkbox"
                                                    onclick="validateSaveButton(this, 'securityQuestionSubmit')"
                                                    id="checkAcknowledgement4">
												    <spring:message code="profile.page.acknowledgement.bottom" />
                                                </label>
											</div>
										</div>
										<div class="buttonWrapperWithBG myProfileButtonWrapper row">
											<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
												<button type="submit" disabled="disabled"
													class="btn btnclsactive save-change primarybtn"
													id="securityQuestionSubmit">
													<spring:message code="profile.save.updates" />
												</button>
											</div>
										</div>
									</form:form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</templateLa:page>

