<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="formElement"	tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/form"%>
<%@ taglib prefix="registration" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>

<script type="text/javascript">
    var captchaSiteKey = "${googleCaptchaKey}";
    var otploadCallback = function() {
        captchaContainer = grecaptcha.render('register_recaptcha_content', {
            'sitekey' : captchaSiteKey,
            'callback' : function(response) {
                if(response!= ' '){
            	    toggleFinishRegistrationElements();
            	    $("#captchaResponse").val(response);
                }
            }
        });
    };
</script>

<template:registrationPage pageTitle="${pageTitle}">

	<input type="hidden" id="captchaResponse" value=""/>

	<c:if test="${not empty registationError}">
		<div class="globalError">
			<p>${registationError}</p>
		</div>
	</c:if>

	<c:url value="/register/laprocess" var="registrationUrl" />
	<c:url value="/login" var="loginUrl"></c:url>

	<form:form id="registerForm"  method="POST" action="${registrationUrl}" modelAttribute="registrationForm">

		<div class="sectionBlock registrationGlobalError error" id="globalerrorform" hidden="true">
			<div class="cell">
			    <div class="registerError registerErrorLast error">
				    <p><spring:message code="signup.page.register.reqfields"/></p>
				</div>
			</div>
		</div>
		<div class="row content" id="signup-title-holder">
			<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12"  id="signup-title">
			    <spring:message code="signup.header.text"/>
			</div>
			<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
				<a href="${loginUrl}" id="cancelButtonText"  class="btn btnclsactive pull-right cancel-register">
				    <spring:message code="register.cancel.button.text"/>
				</a>
				<div class="req-text">(<span style="color:#b41601;">*</span>
				    <spring:message code="signup.required.text"/>
				</div>
			</div>
		</div>
		<div class="boxshadow">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion">
				    <div class="intro-content">
					    <div>
					        <spring:message code="register.stepOne.createAccountNotice.part1" />
							<a class="privacypolicypopup_hn jnj-blue" data-toggle="modal" data-target="#privacypopup" href="#">
							    <spring:message code="register.stepOne.privacyPolicy" />
							</a><spring:message code="register.stepOne.createAccountNotice.part2" />
						</div>
				    </div>

					<div class="profile-accordian panel">
						<div class="profile-accordian-header" style="display: none;">
						<a data-toggle="collapse" data-parent="#accordion" href="javascript:void(0)" id="accordioncollapse9" class="toggle-link panel-collapsed clickontext">
						</a>
						</div>
						<div class="profile-accordian-header">
							<a data-toggle="collapse" data-parent="#accordion" href="javascript:void(0)" id="accordioncollapse8" class="toggle-link panel-collapsed clickontext">
							<i class="bi bi-plus"></i>
							<spring:message code="register.stepTwo.header"/></a>
						</div>
						<div id="collapse8" class="profile-accordian-body panel-collapse collapse bordertop">
    						<div class="Company-Information-block1">
    							<div class="row">
    								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
	    							    <spring:message code="register.stepTwo.customerCode"/>
	    							    <sup class="star">
	    							        <spring:message code="register.stepOne.required.star"/>
	    							    </sup>
	    							</div>
									<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 company-name-txtbox">
		    						    <form:input type="text" autocomplete="off" path="customerCode" name="customerCode"
		    						        id="customerCode" class="form-control required numeric" data-msg-required= "" />
									</div>
								    <div id="customerCodeError" style="display: none" class="col-lg-4 col-md-4 col-sm-4 col-xs-12 error errorProfileInfo marginGap10">
								        <spring:message code="register.stepTwo.customerCodeError"/>
								    </div>
							    </div>
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
								    <spring:message code="register.stepTwo.soldTo"/>
								    <sup class="star"><spring:message code="register.stepOne.required.star"/></sup>
								</div>
		    				    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
									<form:input type="text" autocomplete="off" path="soldTo" name="soldTo" id="soldTo" class="validationevent form-control margintop required" data-msg-required= "" />
								</div>
								<div id="soldToError" style="display: none" class="col-lg-4 col-md-4 col-sm-4 col-xs-12 error errorProfileInfo marginGap30">
								    <spring:message code="register.stepTwo.soldToError"/>
								</div>
							</div>
							<div class="row">
							    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							        <spring:message code="register.stepTwo.jnjAccountManager"/>
							        <sup class="star">
							            <spring:message code="register.stepOne.required.star"/>
							        </sup>
							    </div>
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								    <form:input type="text" autocomplete="off" path="jnjAccountManager"
								        name="jnjAccountManager" id="jnjAccountManager" class="validationevent form-control margintop required" data-msg-required= "" />
								</div>
								<div id="jnjAccountManagerError" style="display: none" class="col-lg-4 col-md-4 col-sm-4 col-xs-12 error errorProfileInfo marginGap30">
								    <spring:message code="register.stepTwo.jnjAccountManagerError"/>
								</div>
							</div>
							<div class="row">
							    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <spring:message code="register.stepTwo.country"/>
							    <sup class="star">
							        <spring:message code="register.stepOne.required.star"/>
							    </sup>
							</div>
						    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <form:select id="shipToCountry" name="shipToCountry" data-width="100%"
							        class="selectpicker margintop "  data-msg-required="" path="shipToCountry">
								    <form:option value="">
								        <spring:message code='signup.selectcountry.text'/>
								    </form:option>
									<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
								</form:select>
							</div>
							<div id="shipToCountryError" style="display: none" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code="register.stepTwo.countryError"/>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="shipAddress1">
							        <spring:message code="register.stepTwo.shipAddressOne"/>
							        <sup class="star">
							            <spring:message code="register.stepOne.required.star"/>
							        </sup>
								</label>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
						        <form:input type="text"  autocomplete="off"  name="shipAddress1" id="shipAddress1"
						            class="addressvalidation form-control margintop address" data-msg-required="" path="shipToLine1"/>
							</div>
							<div id="shipAddress1Error" style="display: none" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code="register.stepTwo.shipAddressOneError"/>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="shipCity">
							        <spring:message code="register.stepTwo.city"/>
							        <sup class="star">
							            <spring:message code="register.stepOne.required.star"/>
							        </sup>
							    </label>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<form:input type="text"  autocomplete="off"
								    class="validationevent form-control margintop" path="shipToCity" name="shipCity" id="shipCity" data-msg-required=""/>
							</div>
							<div id="shipCityError" style="display: none" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code="register.stepTwo.cityError"/>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="shipState">
							        <spring:message code="register.stepTwo.state"/>
							        <sup class="star">
							            <spring:message code="register.stepOne.required.star"/>
							        </sup>
							    </label>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<spring:message code='signup.state.select' var="countryStateCode"/>
							    <form:select id="shipState" path="shipToState" name="shipState" class="form-control margintop"
							        disabled="false" data-width="100%" data-msg-required="Please enter State!" data-none-selected-text="${countryStateCode}" >
									<form:options itemValue="isocode" itemLabel="name" items="${stateList}"/>
								</form:select>
								</div>
								<div id="shipStateError" style="display: none" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
								    <spring:message code="register.stepTwo.stateError"/>
								</div>
							</div>
							<div class="row">
		    					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
			   					    <label class="fontweightnormal" for="shipToZipCode" >
			    				        <spring:message code="register.stepTwo.zipcode"/>
			    				        <sup class="star">
			    				            <spring:message code="register.stepOne.required.star"/>
			    				        </sup>
			    				    </label>
			   					</div>
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								    <form:input type="text" autocomplete="off" path="shipToZipCode" name="shipToZipCode" id="shipToZipCode"
								        data-msg-required="Please enter Postal Code!" class="form-control margintop numeric" />
								</div>
								<div id="shipToZipCodeError" style="display: none" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
								    <spring:message code="register.stepTwo.zipcodeError"/>
								</div>
							</div>
							<div class="row justify-content-end">
							    <div class="error" id="companyInformationError" hidden="true"></div>
								<a data-toggle="collapse" data-parent="#accordion" href="javascript:void(0)" id="tab2NextButtonText"
								    class="btn btnclsactive pull-right profile-nxt-btn" onclick="return companyInformationValidate()">
								    <spring:message code="register.stepOne.next"/>
								</a>
							</div>
						</div>
					</div>
				</div>

				<div class="profile-accordian panel">
			        <div class="profile-accordian-header">
					    <a data-toggle="collapse" data-parent="#accordion" href='javascript:void(0)' id='accordioncollapse3' class="toggle-link panel-collapsed clickontext">
					        <i class="bi bi-plus"></i>
					        <spring:message code="register.stepThree.header"/>
					    </a>
					</div>
					<div id="collapse3" class="profile-accordian-body panel-collapse collapse bordertop">
					    <div class="row">
					        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop30px fname-mobile">
						        <label class="fontweightnormal" for="firstName" >
						            <spring:message code="register.stepThree.first.name"/>
						            <sup class="star">
						                <spring:message code="register.stepOne.required.star"/>
						            </sup>
						        </label>
						    </div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<form:input type="text"  autocomplete="off" path="firstName" name="firstName" id="firstName"
								    class="validationevent required form-control margintop30px"  data-msg-required=""/>
							</div>
							<div id="firstNameError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code="register.stepThree.first.nameError"/>
							</div>
						</div>
					    <div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="lastName">
							        <spring:message code='register.stepThree.last.name'/>
							        <sup class="star">
							           <spring:message code="register.stepOne.required.star"/>
							        </sup>
							    </label>
							</div>
						    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<form:input type="text" class="validationevent form-control margintop" path="lastName" id="signup-lname"  placeholder=""/>
				    		</div>
							<div id="signup-lnameError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code='register.stepThree.last.nameError'/>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <div>
							        <label class="fontweightnormal" for="emailAddress">
							            <spring:message code='register.stepThree.email'/>
							            <sup class="star">
							                <spring:message code="register.stepOne.required.star"/>
							            </sup>
							        </label>
							    </div>
		    					<div>
		    					    <em>
		   						        <spring:message code='register.stepThree.email.msg'/>
		   						    </em>
		    					</div>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<spring:message code='signup.email.regex.error' var="emailRegexError"/>
								<form:input type="email"  autocomplete="off" path="emailAddress" name="emailAddress"
		    					    id="emailAddress" class="form-control margintop comboInput email required" data-msg-required="" data-msg-email="${emailRegexError}"/>
							</div>
							<div id="emailAddressError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code='register.stepThree.emailError'/>
							</div>
							<div id="sectemailAddressError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
                                <spring:message code='register.stepThree.emailsectorerror'/>
                            </div>
						</div>
    					<div class="row">
						    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
						        <label class="fontweightnormal" for="reenterEmailAddress">
						            <spring:message code='register.stepThree.reenter.email'/>
						            <sup class="star">
						                <spring:message code="register.stepOne.required.star"/>
						            </sup>
						        </label>
						    </div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
								<input type="email"  autocomplete="off" name="reenterEmailAddress" id="reenterEmailAddress"
								    class="form-control margintop comboInput email required" data-msg-required="" data-msg-email="${emailRegexError}"/>
						    </div>
							<div id="reenterEmailAddressError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code='register.stepThree.reenter.emailError'/>
							</div>
							<div id="emailAddressValidateError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code='register.stepThree.reenter.email.validateError'/>
							</div>
						</div>

						<c:if test="${isCommercialUser eq 'true'}">

                        <div class="row margintop" >

                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 " >

                               <span id="select-register-sector-txt" class="labelText usm-create-subHead">

                                        <label style="font-size:14px !important"><spring:message code='register.stepThree.commercial.team'/>

                                        <sup class="star">

                                        <spring:message code="register.stepOne.required.star"/>

                                    </sup>

                                        </label>

                                    </span>

                            </div>


                                <div class="col-lg-2 col-md-2 col-sm-2 col-xs-12 ">

                                <input type="radio" checked="checked" id="radioCTY" name="commercialUserFlag"

                                value="true" class="required" />

                            <label for="radioCTY">

                               <spring:message code='register.stepThree.yes'/>

                            </label> 

                                </div>

                                <div class="col-lg-2 col-md-2 col-sm-2 col-xs-12">

                                <input type="radio" id="radioCTN" name="commercialUserFlag"

                                value="false" class="required" />

                            <label for="radioCTN">

                               <spring:message code='register.stepThree.no'/>

                            </label>

                                </div>
                      
                            <div class="" id="sectorMsg"></div> 

                        </div>    

 

                        <div class="row margintop" id="sectordiv" >

                            <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 " >

                               <span id="select-register-sector-txt" class="labelText usm-create-subHead">

                                        <label style="font-size:14px !important"><spring:message code='register.stepThree.user.sector'/>

                                        <sup class="star">

                                        <spring:message code="register.stepOne.required.star"/>

                                    </sup></label>

                                    </span>

                            </div>


                                <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 ">

                                <input type="radio"  id="radioSmdd" name="commercialUserSector"

                                value="MDD" class="required" />

                                        <label for="mdd" class="checkLabel">

                                                <spring:message code='register.stepThree.mdd'/></label>

                                </div>

                                <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12" style="margin-left: -80px;">

                                <input type="radio"  id="radioSphr" name="commercialUserSector"

                                value="PHR" class="required" />

                                        <label for="pharma" class="checkLabel">

                                        <spring:message code='register.stepThree.pharma'/></label>

                                </div>

                        <div id="commuser" style="display: none; margin-left: -82px;" class="error errorYourInfo marginGap30  col-lg-2 col-md-2 col-sm-2 col-xs-12" >
                            <spring:message code='register.stepThree.sectorError'/>
                            </div>
                            <div class="" id="sectorMsg"></div> 
                        </div>   
                        </c:if>   

                        <div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="regPhoneNumber">
							        <spring:message code='register.stepThree.phone'/>
							        <sup class="star">
							            <spring:message code="register.stepOne.required.star"/>
							        </sup>
							    </label>
							</div>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
							    <div class="boxtext1">
									<spring:message code='signup.prefix.select' var="countryPhoneCode"/>
									<form:select id="regPhoneNumberPrefix" path="phoneNumberPrefix" name="phoneNumberPrefix"
									    class="form-control margintop" disabled="false" data-width="100%" data-none-selected-text="${countryPhoneCode}" data-msg-required=""  >
                                        <form:options items="${countryCodes}"/>
									</form:select>
								</div>
								<div class="boxtext2">
							        <form:input  type="text" autocomplete="off" id="regPhoneNumber" path="phone" name="regPhoneNumber"
									    class="form-control margintop phone numeric" maxlength="15" data-msg-required="<spring:message code='register.stepThree.required.phone'/>" />
								    <input type="hidden" id="phone" name="phone" value="" />
								</div>
							</div>
							<div id="regCountryCodeError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code="register.country.code.error"/>
							</div>
							<div id="regPhoneNumberError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
							    <spring:message code='register.stepThree.phoneError'/>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
							    <label class="fontweightnormal" for="regMobileNumber">
							        <spring:message code='register.stepThree.mobile'/>
							    </label>
							</div>
						    <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
								<div class="boxtext1">
								    <form:select id="regMobileNumberPrefix" path="mobileNumberPrefix" name="mobileNumberPrefix" class="form-control margintop" disabled="false" data-width="100%" data-msg-required=""  >
									    <option value="">
									        <spring:message code='signup.prefix.select'/>
									    </option>
										<form:options items="${countryCodes}"/>
									</form:select>
								</div>
								<div class="boxtext2">
								    <form:input  type="text" autocomplete="off" id="regMobileNumber" path="mobile" name="regMobileNumber"
								        class="comboInput phoneWidth  form-control margintop phone numeric" maxlength="15" />
									<input type="hidden" id="mobile" name="mobile" value="" />
								</div>
							</div>
						</div>
						<div class="row">
						    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop">
						        <label for="create-password">
						            <a class="jnj-blue">
						                <spring:message code='register.stepFive.header'/>
						            </a>
						        </label>
						    </div>
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class='Newpassword signuppagepwd'>
								    <div for='signuppwd'>
								        <spring:message code="signup.password.choose"/>
								        <sup class="star">*</sup>
								    </div>

									<spring:message code='signup.password.complexity.message' var="complexityMessage"/>
									<div class='pwdwidgetdiv' id='signuppwddiv' data-toggle="tooltip" data-placement="right" title="${complexityMessage}"></div>

								</div>
								<div class="reEnter">
								    <spring:message code="signup.password.re-enter"/>
								    <sup class="star">*</sup>
								</div>
								<input type="password" class="form-control pwdWidth" id="reenter-password">
									<div id="createpasswordError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
									    <spring:message code='signup.password.chooseError'/>
									</div>
									<div id="reenter-passwordError" style="display: none" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12">
									    <spring:message code='signup.password.re-enterError'/>
									</div>
									<div class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12" id="createpasswordError1" style="display: none" >
									    <spring:message code='signup.create.password.Error1'/>
									</div>
									<div class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12" id="createpasswordError2" style="display: none" >
									    <spring:message code='signup.create.password.Error2'/>
									</div>
							</div>
						</div>
						<div class="row justify-content-end">
							<div class="error" id="yourInformationError" hidden="true"></div>
							<a data-toggle="collapse" data-parent="#accordion" href="javascript:void(0)" id="tab3NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return yourInformationValidate()">
							    <spring:message code="register.stepOne.next"/>
							</a>
						</div>
					</div>
				</div>

				<div class="profile-accordian panel">
					<div class="profile-accordian-header">
						<a data-toggle="collapse" data-parent="#accordion" href='javascript:void(0)' id='accordioncollapse5' class="toggle-link panel-collapsed clickontext">
						    <i class="bi bi-plus"></i>
						    <spring:message code="register.setup.for.communication.preference"/>
						</a>
					</div>
					<div id="collapse5" class="profile-accordian-body panel-collapse collapse bordertop">
						<div class="error" id="emailPreferencesError" hidden="true"></div>
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<p>
									    <spring:message code="register.stepFour.body.text"/>
									</p>
									<p class="marTop20">
									    <spring:message code="register.stepFour.text"/>
									</p>
									<ul class="checkbox checkbox-info checkboxmargin margintop10">
									    <c:if test="${not empty emailPreferences}">
										    <form:checkboxes  id="emailPreferences" items="${emailPreferences}" element="li" path="emailPreferences" />
										</c:if>
									</ul>
								</div>
							</div>
							<div class="row justify-content-end">
								<a data-toggle="collapse" data-parent="#accordion" href='javascript:void(0)' id="tab5NextButtonText"
								    class="btn btnclsactive pull-right profile-nxt-btn" onclick="return emailPreferencesValidate()">
								    <spring:message code="register.stepOne.next"/>
						    	</a>
							</div>
						</div>
					</div>

				    <div class="profile-accordian panel">
					    <div class="profile-accordian-header">
							<a data-toggle="collapse" data-parent="#accordion" href='javascript:void(0)' id='accordioncollapse7' class="toggle-link panel-collapsed clickontext">
							    <i class="bi bi-plus"></i>	<spring:message code='register.security.questions' />
							</a>
						</div>
						<div id="collapse7" class="profile-accordian-body panel-collapse collapse bordertop">
							<div class="row">
								<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
									<spring:message code='register.security.questions.message' />
								    <div class="error" id="signupsecretquestError" style="display: none" >
								        <spring:message code='register.security.questionsError' />
								    </div>
								    <c:forEach varStatus="status" begin="0" end="5">
										<c:choose>
											<c:when test="${status.index lt 3}">
												<div class="secret-quest-dropdown-label">
													<formElement:jnjSecretQuestionFormBox selectCSSClass="required selectpicker select-security-ques"
													    idKey="questionList${status.index}" items="${secretQuestions}" itemLabel="question"
													        labelKey="profile.secret.question${status.index}" path="secretQuestionsAnswers[${status.index}].code"
													            mandatory="true" errorMsg="Please try again select one secret question!" />
													<input type="hidden" id="hddnQuestionErrorMsg" value="<spring:message code="error.secret.question.required" />">
													<input type="hidden" id="hddnAnswerErrorMsg" value="<spring:message code="error.secret.answer.required" />">
													<div class="cell">
														<div class="registerError errorPosition"></div>
													</div>
												</div>
												<div class="signup-secret-quest-text-label">
													<formElement:jnjProfileInputBox idKey="profile.secret.answer${status.index}" labelKey="profile.secret.answer${status.index}"
													    path="secretQuestionsAnswers[${status.index}].answer" inputCSS="validationevent required form-control secQusCls" mandatory="true" errorMsg=""/>
												</div>
											</c:when>
									       	<c:otherwise>
												<div class="secret-quest-dropdown-label">
													<formElement:jnjSecretQuestionFormBox selectCSSClass="selectpicker select-security-ques" idKey="questionList${status.index}"
													    items="${secretQuestions}" itemLabel="question" labelKey="profile.secret.question${status.index}"
													        path="secretQuestionsAnswers[${status.index}].code"  mandatory="false" />
												</div>
												<div class="signup-secret-quest-text-label">
													<formElement:jnjProfileInputBox idKey="profile.secret.answer${status.index}" labelKey="profile.secret.answer${status.index}"
													    path="secretQuestionsAnswers[${status.index}].answer" inputCSS="form-control validationevent" mandatory="false" />
												</div>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</div>
							</div>
							<div class="row justify-content-end">
							    <a data-toggle="collapse" data-parent="#accordion" href="javascript:void(0)" id="tab7NextButtonText" class="btn btnclsactive pull-right"
							        onclick="return secretQuestionValidate()"><spring:message code="register.stepOne.next"/>
							    </a>
							</div>
						</div>
					</div>
				</div>
			</div>

		    <div class="conclusion">
			    <div class="profileRegBlock">
				    <div class="captchaInput overFlow">
                    	<label for="captcha">
                    	    <spring:message code="text.register.validation" />
                    	    <span>
                    	        <spring:message code="register.stepOne.required.star" />
                    	    </span>
                    	    <spring:message code="text.register.colon" />
                    	</label><br>
                    	<!-- Google reCaptcha -->
                    	<div id="register_recaptcha_content" style="width: 440px;"></div>
                    		<script src="https://www.google.com/recaptcha/api.js?onload=otploadCallback&render=explicit&hl=${googleCaptchaLanguageIso}" async defer></script>
                    		<div class="cell">
                    			<div class="registerError registerErrorCaptcha"></div>
                    		</div>
                   		</div>
				    </div>
			    <div class="profileRegBlock">
				    <div class="policy">
					    <ul style="list-style-type: none;">
						    <li class="registerPrivacynote checkbox checkbox-info checkboxmargin">
						        <input id="terms-conditions" class="noTextInput" name="notif"
							        data-msg-required="Please indicate your acceptance of the Terms and Conditions by selecting the check-box." type="checkbox" />
								<label for="terms-conditions">
							        <spring:message code="text.register.policy.part3" />
									<a class="privacypolicypopup_hn jnj-blue" href="#">
									    <spring:message code="text.register.privacy.policy" />
									</a>
									<spring:message code="text.register.and" />
									<a class="legalnoticepopup_hn jnj-blue" href="#">
									    <spring:message code="text.register.legal.notice" />
									</a>.
						        </label>
							</li>

					        <li class="registerPrivacynote checkbox checkbox-info ms-3">
							    <div class="cell">
								    <input id="privacy-policy" class="noTextInput" name="agreepolicy"
									    data-msg-required="Please indicate your acceptance of the latest Privacy Policy by selecting the check-box." type="checkbox" />
									<label for="privacy-policy">
								        <spring:message code="text.register.policy.part1" />
									    <a class="privacypolicypopup_hn jnj-blue" data-toggle="modal" data-target="#privacypopup" href="#">
									        <spring:message code="text.register.privacy.policy" />
									    </a>
									    <spring:message code="text.register.policy.part2" />
									</label>
							    </div>
							    <div class="cell clear">
								    <div class="registerError"></div>
							    </div>
						    </li>
					    </ul>
				    </div>
			    </div>
			    <div class="row justify-content-end">
				    <div class="error" id="finalsubmissionError" style="display: none" >
				        <spring:message code="final.submission.error" />
				    </div>
			    </div>
			</div>
        </div>

		<div class="row marginbottom35px">
		    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			    <a href="${loginUrl}" id="cancelButtonText"  class="btn btnclsactive pull-right cancel-register">
				    <spring:message code="register.cancel.button.text"/>
				</a>
				<button type="submit" onclick="return validationRegistrationForm()" id="finishButtonText" class="btn btnclsnormal pull-right submit-approval">
				    <spring:message code="register.submit.button.text"/>
				</button>
			</div>
		</div>
	</form:form>

	<div class="row conclusion-paragraph display-row">
	    <div class="tabel-cell blue-strip"></div>
	    <div class="table-cell col-lg-12 col-md-12 col-sm-12 col-xs-12 customerReg-conclusion">
		    <div>
		        <spring:message code="register.conclusion.content.part1"/>
		    </div>
		    <div class="margintop15px">
		        <spring:message code="register.conclusion.content.part2"/>
			</div>
	    </div>
    </div>

    <div id="privacypolicypopuopholder"></div>
    <div id="termsandconditionsholder"></div>
    <div id="legalnoticepopupholder"></div>
    <div id="contactuspopupholder"></div>
</template:registrationPage>