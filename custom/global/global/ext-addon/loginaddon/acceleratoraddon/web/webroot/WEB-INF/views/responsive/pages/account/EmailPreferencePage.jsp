<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>

<%@ taglib prefix="formElement"	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/form"%>
<%@ taglib prefix="registration" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>
	<%@taglib prefix="fn" uri="jakarta.tags.functions" %>

						
												<div class="error" id="emailPreferencesError" hidden="true"></div>													
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<p><spring:message code="register.stepFour.body.text"/></p>
														<!-- 5506 -->
														
														<%-- <div  margin-left: 25px;">
												<spring:message code="PreferredMobileNumber"/><input type="text" id ="mobilenumberText"  class="form-control" readonly/></input>
														</div> --%>
														<div class="margintop prefer-mobi-number" style="margin-bottom:15px">
															<div class="row">
																<div class="col-lg-2 col-md-2 col-sm-3 col-xs-12">
																	<div class="inline-block" id="prefer-mobile-label"><spring:message code="register.stepFour.PreferredMobileNumber"/></div> 
																</div>
																<div class="col-lg-10 col-md-10 col-sm-9 col-xs-12" id="pref-mobi-holder">
																	<div id="prefer-mobi-number-code"><input type="text" id="mobileNumberPrefix" path="preferredMobileNumberPrefix" value=""  class="comboSelection form-control prefer-mobi-number phone-only " name="preferredMobileNumberPrefix" readonly/></div>
																	<div id="prefer-mobi-number"><input value="" id="preferredMobileNumber" name="preferredMobileNumber" value=""  path="preferredMobileNumber" class="form-control prefer-mobi-number phone-only"  readonly/></div>
																	<div class="inline-block" id="prefmob-edit-link"><span  class="hyerlink"><spring:message code="register.emailPreferences.edit"/></span></div>&nbsp;&nbsp;&nbsp;<spring:message code="register.emailPreferences.note"/>
																</div>
															</div>
																
																
															</div>
														<p class="marTop20"><spring:message code="register.stepFour.text"/></p>
														<div class="checkBoxAlignment"><spring:message code="register.emailPreferences.email"/></div>
														<div class="checkBoxAlignment"><spring:message code="register.emailPreferences.test"/></div>
														<ul class="checkbox checkbox-info checkboxmargin margintop10 ">
														<c:if test="${not empty emailPreferences}">
															<!-- form:checkboxes items="${emailPreferences}" element="li" path="emailPreferences" /-->															
															<c:forEach var="emailPrefrence" items="${emailPreferences}" varStatus="count">
															<%-- <li style="list-style: none">
																			<c:set var="key" value="${emailPrefrence.key}"></c:set>
																			<input type="checkbox"  class="profileEmailPrefrences" name="emailPreferences" id="check${count.count}"
																				value="${emailPrefrence.key}"
																				<c:if test="${ key eq 'emailPreference6' || key eq 'emailPreference9' || key eq 'emailPreference11' || key eq 'emailPreference12'}">checked</c:if>> 
																				<label for="check${count.count}">	
																					${emailPrefrence.value}
																				</label>
																				<br/>
															
															</li> --%>
															<div class="${emailPrefrence.key}">
															<div >
															<!-- 5506 -->
																<c:set var="key" value="${emailPrefrence.key}"></c:set>
																
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
																
																
																	<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment">
																		<input class="styled profileEmailPrefrences" type="checkbox"   name="emailPreferences" value="${emailPrefrence.key}" id="email${count.count}" <c:if test="${ key eq 'emailPreference6' || key eq 'emailPreference9' || key eq 'emailPreference11' || key eq 'emailPreference12'}">checked</c:if>>
																		<label for="email${count.count}"></label>
																	</div>
																	<div class="checkbox checkbox-info checkboxmargin margintop10 checkBoxAlignment ">
																		<input id="" class="styled profileEmailPrefrences enableCheckboxClass"  name="smsPreferences"  value="${KeyForSms}"  type="checkbox" disabled>
																		<label for=""></label>
																	</div>
																	 ${emailPrefrence.value}
																
																
																</div>
														<!-- AAOL-5189 -->
														<c:if test="${ key eq 'emailPreference6' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px">
															<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>													
															</c:if>
															<c:if test="${ key eq 'emailPreference9' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px">
															<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>													
															</c:if>
															
															<c:if test="${ key eq 'emailPreference11' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px">
															<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>													
															</c:if>
															
															<%-- <c:if test="${ key eq 'emailPreference13' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px">
															<input type="radio" name="shipdialy"  id="shipdialy" value="daily" class="shipdaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="shipdialy" value="weekly" id="shipweekly" checked="checked" class="shipweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>													
															</c:if> --%>
															
																<c:if test="${ key eq 'emailPreference12' }">					
															<div id="radiodiv1" style="display: inline;  padding-left:57px;">
																<input type="radio" name="backdialy"  id="backdialy" value="daily"  style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/> &nbsp;&nbsp;&nbsp;    
										    					<input type="radio" name="backdialy" value="weekly" id="backweekly" checked="checked"  style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>
															</c:if>
															
															<%-- <c:if test="${ key eq 'emailPreference16' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px ">
															<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="invdialy"  id="invweekly" value="weekly"checked="checked" class="invweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
															
															</div><br/>
															</c:if> --%>
															
															<%-- <c:if test="${ key eq 'emailPreference15' }">
															<div id="radiodivShippedOrder1" style="display: inline; padding-left:57px ">
															<input type="radio" name="delNotedialy"  id="deldialy" value="daily" class="deldaily" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="delNotedialy"  id="dekweekly" value="weekly" checked="checked" class="delweekly" style="opacity: 1"/>&nbsp;<spring:message code="register.stepOne.weekly"/>
														<!-- AAOL-5189 -->	
															</div><br/>

					</c:if>
															<!-- AAOL-5520 start-->
															<c:if test="${ key eq 'emailPreference6' }">
															<div id="radiodivShippedOrder1" style="display: inline; margin-left: 25px;">
															<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="invdialy"  id="invweekly" value="weekly"checked="checked" class="invweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															
															</div><br/>
															</c:if>
														
															<c:if test="${ key eq 'emailPreference9' }">
															<div id="radiodivShippedOrder1" style="display: inline; margin-left: 25px;">
															<input type="radio" name="invdialy"  id="invdialy" value="daily" class="invdaily" style="opacity: 1"/><spring:message code="register.stepOne.daily"/>  &nbsp;&nbsp;&nbsp;
															<input type="radio" name="invdialy"  id="invweekly" value="weekly"checked="checked" class="invweekly" style="opacity: 1"/><spring:message code="register.stepOne.weekly"/>
															
															</div><br/>
															</c:if>
															<!-- AAOL-5520 end-->

				                  	</c:if> --%>
															</div>
															</c:forEach>
															</c:if>
															</ul>
														</div>
													</div>	
													<div class="row">
									 				 	<a data-toggle="collapse" data-parent="#accordion" href="#" id="tab5NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return emailPreferencesValidate()">Next</a>
									 				 </div>
												