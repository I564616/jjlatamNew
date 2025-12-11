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

<script type="text/javascript">
	 var captchaSiteKey = "${googleCaptchaKey}";
      var otploadCallback = function() {
    	  console.log("Register Page");
        captchaContainer = grecaptcha.render('register_recaptcha_content', {
          'sitekey' : captchaSiteKey,
          'callback' : function(response) {
            console.log("check here" + response);
            if(response!= ' '){
            	$('#finishButtonText').show();
            	$("#captchaResponse").val(response);
     			$('.registerErrorCaptcha .error').remove();
            }
          }
        });
      };

</script>

<!--  <div class=" jnj-signup-container"> --> 
<template:registrationPage pageTitle="${pageTitle}">
<input type="hidden" id="captchaResponse" value=""/> 
	<!-- globalMessages : START -->
	<c:if test="${not empty registationError}">
		<div class="globalError">
			<p>${registationError}</p>
		</div>
	</c:if>
	<!-- globalMessages : END -->  
    <c:url value="/register/process" var="registrationUrl" />
    <c:url value="/login" var="loginUrl"></c:url>
	 
	 							
		 <form:form id="registerForm"  method="POST" action="${registrationUrl}" commandName="registrationForm">
								<div class="sectionBlock registrationGlobalError error" id="globalerrorform" hidden="true">
									<div class="cell">
										<div class="registerError registerErrorLast error">
											<p><spring:message code="signup.page.register.reqfields"/></p>
										</div>
									</div>
								</div>							
								<div class="row content" id="signup-title-holder">
									<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12"  id="signup-title"><spring:message code="signup.header.text"/></div>
									<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">														
										<a href="${loginUrl}" id="cancelButtonText"  class="btn btnclsactive pull-right cancel-register"><spring:message code="register.cancel.button.text"/></a>
										<div class="req-text">(<span style="color:#b41601;">*</span><spring:message code="signup.required.text"/></div>
									</div>																							
								</div>								
			 <div class="boxshadow">
									<div class="row">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion">
					
											<div class="intro-content"> 
												<div><spring:message code="register.stepOne.createAccountNotice" />
												<a class="privacypolicypopup_hn jnj-blue" data-toggle="modal" data-target="#privacypopup" href="#"><spring:message code="register.stepOne.privacyPolicy" /></a></div>
											</div>
											
							 <!-- Product Type Information Block Start -->
											<div class="profile-accordian panel">											
												<div class="profile-accordian-header">
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="toggle-link panel-collapsed clickontext table-cell">
														<span class="glyphicon glyphicon-plus plusicon table-cell"></span>
														<span class="table-cell"><spring:message code="register.stepOne.chooseProductsType"/></span>
													</a>
												</div>	
												<div id="collapse1" class="profile-accordian-body panel-collapse collapse bordertop">
												 <form:checkbox class="styled signup-chk-1" id="medicalDevices"  checked="checked" path="mddSector" /> <label class="fontweightnormal signup-label-1" for="medicalDevices"><spring:message code="register.stepOne.mdd"/></label>
												 <form:checkbox class="styled signup-chk-1" id="consumerProd" path="consumerProductsSector"/> <label class="fontweightnormal signup-label-1" for="consumerProd"><spring:message code="register.stepOne.consumer"/></label>
												 <form:checkbox class="styled signup-chk-1" id="pharma" path="pharmaSector"/> <label class="fontweightnormal signup-label-1" for="pharma"><spring:message code="register.stepOne.pharma"/></label>
											
												<div>
													<a class="clickontext table-cell">														
														<span class="table-cell"><spring:message code="register.stepOne.chooseProfile"/></span>
													</a>													
												</div>
												<div class="bordertop">													
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">															
															<div class="radio radio-info ">
																<form:radiobutton name="jjProfile" checked="checked" class="jjProfile customRadio" id="custAccount"  value="custAccount" path="typeOfProfile"/>
																<label for="custAccount"><spring:message code="register.stepOne.custAccount"/></label>
															</div>
															<div class="addprofile-block">
																<div class="consProductNo">
																		<div >
																			<label for="accNumbersRadio" class="consProductYes">
																			<input type="radio" checked="checked" name="yesAcc" id="accNumbersRadio" /></label>
																			<label class="fontweightnormal" for="accNumbers"  class="getErrorMessage" data="<spring:message code='register.stepOne.required.account'/>"> <spring:message code="register.stepOne.addAccountDesc"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
																			<div class="row">
																				<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
																					<form:input id="accNumbers" name="accNumbers" class="accounts required form-control txtwidth50 pull-left"  path="accountNumbers"  autocomplete="off"/> 
																					<span class="smallFont enter-txt pull-left"><spring:message code="signup.page.text"/></span>
																				</div>
																			</div>
																			<div class="clear"></div>
																		</div>
																		<div><div class="registerError"></div></div>
																			<p class="smallFont"> <spring:message code="register.stepOne.accountMessage"/></p>
																			<p class="consProductYes">
																				<input type="radio" name="yesAcc" id="myCompany"/>  
																				<!--AAOL-6469 -->
																				<label for="myCompany" style="font-weight:normal"><spring:message code="register.stepOne.unknownAccountMsg"/></label>
																			</p>
																			<input type="hidden" value="false" name="unknownAccount" id="unknownAccount" />
															 </div>		
															</div>
															<div class="radio radio-info ">
																<form:radiobutton name="jjProfile"  class="jjProfile" id="jjCustomer" value="jjCustomer" path="typeOfProfile"/>
																<label for="jjCustomer"><spring:message code="register.stepOne.newCustomer"/></label>																
															</div>
															<div class="radio radio-info ">
																<form:radiobutton name="jjProfile" class="jjProfile customRadio" id="jjEmployee" value="jjEmployee" path="typeOfProfile"/>
																<label for="jjEmployee"><spring:message code="register.stepOne.jnjEmployee"/></label>																																
															</div>	
															<div class="jjEmployeeYes">
																<div class="row cellLeft" style="margin-bottom:10px">
																	<div class="col-lg-1 col-sm-1 col-md-1 col-xs-12">
																		<label for="wwid"  class="getErrorMessage label-middle" data="<spring:message code='register.stepOne.required.wwid'/>"><spring:message code="register.stepOne.wwid"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
																		 								
																	</div>
																	<div class="col-lg-4 col-sm-4 col-md-4 col-xs-12">
																		<form:input  type="text" id="wwid" path="wWID" class="form-control required numbersonly"  autocomplete="off" data-msg-required="<spring:message code='register.stepOne.required.wwid'/>" />
																	</div>
																</div>
																
																<div class="row cellLeftMidd" style="margin-bottom:10px">
																	<div class="col-lg-1 col-sm-1 col-md-1 col-xs-12">
																		<label for="divis" class="getErrorMessage label-middle" data="<spring:message code='register.stepOne.required.division'/>"><spring:message code="register.stepOne.division"/> <span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label>
																	</div>
																	<div class="col-lg-4 col-sm-4 col-md-4 col-xs-12">
																		<form:select id="divis" name="divis" class="required selectpicker" path="division" data-width="100%">							
																			<option value="">select</option>
																		</form:select>
																	</div>
																</div>
																<div class="row cellLeftRight">
																	<div class="col-lg-1 col-sm-1 col-md-1 col-xs-12">
																		<label for="gln" ><spring:message code="register.stepOne.account.gln"/></label>
																	</div>
																	<div class="col-lg-4 col-sm-4 col-md-4 col-xs-12">
																		<form:input type="text"  autocomplete="off" id="gln" path="glnOrAccountNumber" class="accounts form-control"/>
																	</div>
																</div>
																
															</div>
																												
														</div>
													</div>	
													<div class="row">
													<div class="error" id="productInformationError" hidden="true"></div>
									 				 <a data-toggle="collapse" data-parent="#accordion" href="#" id="tab1NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return productInformationValidate()"><spring:message code="register.stepOne.next"/></a>
													</div>																									
												</div>
											</div>
												</div> 
							 <!-- Product Type Information Block End -->
							 
							 <!-- Company Information Block Started -->
											<div class="profile-accordian panel">
												<div class="profile-accordian-header">
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse8" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span><spring:message code="register.stepTwo.header"/></a>
												</div>
												<div id="collapse8" class="profile-accordian-body panel-collapse collapse bordertop">
													<div class="Company-Information-block1">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 "><spring:message code="register.stepTwo.accountName"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 company-name-txtbox"> 
																 <form:input type="text" maxlength="50" autocomplete="off" path="accountName" name="accountName" id="accountName" class="form-control required " data-msg-required= "" />
															</div>
															<div id="accountNameError" class="col-lg-4 col-md-4 col-sm-4 col-xs-12 error errorProfileInfo marginGap10"></div>	
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message code="register.stepTwo.gln"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																  <form:input type="text" autocomplete="off" path="gLN" name="globalLocNo" id="globalLocNo" class="form-control margintop " />
															</div>
														</div>
														<div class="row">	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="bussType" ><spring:message code="register.stepTwo.businessType"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:select path="typeOfBusiness" id="bussType" name="bussType" class="selectpicker margintop" data-width="100%" data-msg-required="">
																<form:option value="" >Select</form:option>
																<c:if test="${not empty businessType}">
																<form:options items="${businessType}"/>
																</c:if>
															</form:select> 
															</div>
															<div id="bussTypeError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="subsidiary"><spring:message code="register.stepTwo.subsidiary"/></label>:</div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																 <form:input type="text"  autocomplete="off" path="subsidiaryOf" name="subsidiary" id="subsidiary" class="form-control margintop " />
															</div>
														</div>
													</div>
													<div class="Company-Information-block2">
														<div style="color:#0a8caa;"><strong><spring:message code="register.stepTwo.billToHeader"/></strong></div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="billCountry" ><spring:message code="register.stepTwo.country"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																	<form:select id="billCountry1" class="selectpicker margintop" name="billCountry"  data-width="100%" data-msg-required="" path="billToCountry">
																	<form:option value="">Select country or region</form:option>
																	<c:if test="${not empty countryList}">
																  	<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
																	</c:if>
																</form:select>
															</div>	
														<div id="billCountryError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="billAddress1"><spring:message code="register.stepTwo.billAddressOne"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																 <form:input type="text"  autocomplete="off"  name="billAddress1" id="billAddress1" class="form-control margintop address-only" data-msg-required="" path="billToLine1"/>
															</div>
															<div id="billAddress1Error" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="billAddress2"><spring:message code="register.stepTwo.billAddressTwo"/></label> </div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															  <form:input type="text"   autocomplete="off" class="form-control margintop address-only" path="billToLine2" name="signup-Bill-To-add2" id="signup-Bill-To-add2" />																
															</div>
														</div>
														<div class="row">	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="billCity"><spring:message code="register.stepTwo.city"/></label><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																  <form:input type="text"  class="form-control margintop " autocomplete="off" path="billToCity" name="billCity" id="billCity" data-msg-required="" />																	
															</div>	
															<div id="billCityError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="billState" ><spring:message code='register.stepTwo.state'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12" id="UsBillStatediv"> 
																<form:select id="billState" path="billToState" name="billState" class="form-control margintop"  disabled="false" data-width="100%" data-msg-required="<spring:message code='register.stepTwo.required.state'/>">
																  <form:option value=""><spring:message code='signup.state.select'/></form:option>
																</form:select>
															</div>
															
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 hide" id="noUsBillStatediv"> 
																  <form:input type="text" path="billToState" name="billToState" id="noUsBillState" disabled="true" class="form-control margintop " data-msg-required="<spring:message code='register.stepTwo.required.state'/>"/>																	
															</div>	
															
															<div id="billStateError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
                         								</div>
															<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="billToZipCode" ><spring:message code='register.stepTwo.zipcode'/> <sup class="star"><spring:message code="register.stepOne.required.star"/> </sup></label></div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:input type="text"  autocomplete="off" class="form-control margintop  alphanumeric" path="billToZipCode" name="billToZipCode" id="billToZipCode" data-msg-required="<spring:message code='register.stepTwo.required.postalcode'/>" />															 
															</div>
															<div id="billToZipCodeError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
														</div>
													</div>
													<div class="Company-Information-block2">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12" style="color:#0a8caa;"><strong><spring:message code='signup.shiplocation.text'/></strong></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12 checkbox checkbox-info ship-to-check"> 
																<input type="checkbox" class="styled"  id="billToLocation" name="sameasbillto" />  
																<label for="billToLocation" >
																 <spring:message code='register.stepTwo.same.as.billto'/> 
																</label>
															</div>
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message code="register.stepTwo.country"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															  <form:select id="shipToCountry" name="shipToCountry" data-width="100%" class="selectpicker margintop "  data-msg-required="" path="shipToCountry">
																<form:option value=""><spring:message code='signup.selectcountry.text'/></form:option>
															  	<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
															</form:select> 
															</div>	
														<div id="shipToCountryError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="shipAddress1"><spring:message code="register.stepTwo.shipAddressOne"/> <sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															  <form:input type="text"  autocomplete="off"  name="shipAddress1" id="shipAddress1" class="form-control margintop address-only" data-msg-required="" path="shipToLine1"/> 
															</div>	
														<div id="shipAddress1Error" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
														</div>
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="shipAddress2"><spring:message code="register.stepTwo.shipAddressTwo"/></label> </div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:input type="text"   class="form-control margintop address-only"  autocomplete="off" path="shipToLine2" name="shipAddress2 address-only" id="signup-ship-To-add2" />
															</div>
														</div>
														<div class="row"> 
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="shipCity"><spring:message code="register.stepTwo.city"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input type="text"  autocomplete="off" class="form-control margintop " path="shipToCity" name="shipCity" id="shipCity" data-msg-required=""/>
															</div>
															<div id="shipCityError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
														</div>
														<div class="row">	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="shipState"><spring:message code="register.stepTwo.state"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>																			
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12" id="UsShipStatediv">
															 <form:select id="shipState" path="shipToState" name="shipState" class="form-control margintop" disabled="false" data-width="100%" data-msg-required="<spring:message code='register.stepTwo.required.state'/>"  >
																	<option value=""><spring:message code='signup.state.select'/> </option>
																		<form:options itemValue="isocode" itemLabel="name" items="${stateList}"/>
																</form:select> 												
															</div>
															
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 hide" id="noUsShipStatediv"> 
																  <form:input type="text" path="shipToState" name="shipState" id="noUsShipState"  disabled="true" class="form-control margintop " data-msg-required="<spring:message code='register.stepTwo.required.state'/>"/>																	
															</div>	
															
															<div id="shipStateError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
														</div>
														<div class="row"> 
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="shipToZipCode" ><spring:message code="register.stepTwo.zipcode"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"> 
																<form:input type="text" autocomplete="off" path="shipToZipCode" name="shipToZipCode" id="shipToZipCode" data-msg-required="<spring:message code='register.stepTwo.required.postalcode'/>" class="form-control margintop alphanumeric" />
															</div>
															<div id="shipToZipCodeError" class="error errorProfileInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
														</div>
													</div>
													<div class="Company-Information-block3">
														<div><spring:message code='register.stepTwo.taxtext'/></div>
														<div class="margintop10 cellReg">
															<div class="radio radio-info radio-inline cellReg">
															 <form:radiobutton path="salesAndUseTaxFlag" value="true" name="yes" checked="checked" id="yes"  data-msg-required="" />
															 <label class="bill" for="yes">  <spring:message code="register.stepTwo.yes"/></label> 
															</div>
															<div class="radio radio-info radio-inline cellReg">
															<form:radiobutton name="no" id="no" path="salesAndUseTaxFlag" value="false"/>
															<label class="bill" for="no"><spring:message code="register.stepTwo.no"/></label> 
															</div>	
															<p class="selNo" style="display:none;"><spring:message code="register.stepTwo.notice.text"/></p>
														</div>
														<div class="margintop cellReg"><spring:message code="register.stepTwo.opening.order"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup>
														
														<div class="row">
															<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
																 <form:select id="estAmount" path="initialOpeningOrderAmount" name="estAmount" class="selectpicker margintop10 " data-width="100%" data-msg-required="" >
																	<option value=""><spring:message code="signup.selection.text"/></option>
																	<c:if test="${not empty estimatedAmount}">
																	<form:options items="${estimatedAmount}"/>
																	</c:if>
																</form:select>
															</div>
															<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 signup-note"><spring:message code="register.stepTwo.opening.note"/></div>																
														<div id="estAmountError" class="error errorProfileInfo marginGap10  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
														
														</div>
														</div>
														<div class="row">
															<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop10">
																<div><label class="fontweightnormal" for="estimatedAmountPerYear"><spring:message code="register.stepTwo.estimatedamount"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup>
																  </label></div>																
															</div>
															<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margintop10">
															<form:input path="estimatedAmountPerYear"  type="text" autocomplete="off"  id="estimatedAmountPerYear" name="estimatedAmountPerYear" class="form-control numbersonly" data-msg-required=""/>
														</div>
														<div id="estimatedAmountPerYearError" class="error errorProfileInfo marginGap20  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
														</div>
														<div class="margintop cellReg"><spring:message code="register.stepTwo.products"/> <sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
														<div class="row product-block">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
																<div class="checkbox checkbox-info checkboxmargin margintop10 prodPur">
																	<ul class="prodtopurchase">
																	<c:if test="${not empty purchaseProduct}">
																	 <%--  <label for="medicalProductsPurchase" class="getErrorMessage" data="<spring:message code='profile.myprofile.productpurchased'/>"></label> --%>	
																	 <form:checkboxes items="${purchaseProduct}" id="medicalProductsPurchase" path="medicalProductsPurchase"  element="li" class="checkbox checkbox-info checkboxmargin margintop10" data-msg-required="" />
																	 <div class="cellReg width450"><div class="registerError"></div></div>
																	</c:if>
																	</ul>	
																</div>												
															</div>
														</div>
														<div class="row">
														<div class="error" id="companyInformationError" hidden="true"></div>
														<a data-toggle="collapse" data-parent="#accordion" href="#" id="tab2NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return companyInformationValidate()"><spring:message code="register.stepOne.next"/></a>
									 					</div>
													</div>
												</div>
												</div>
							         <!-- Company Information Block End -->
												
				                      <!-- Your Information Block Start -->
												<div class="profile-accordian panel">
												<div class="profile-accordian-header">
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse3" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span><spring:message code='register.stepThree.header'/></a>
												</div>
												<div id="collapse3" class="profile-accordian-body panel-collapse collapse bordertop">
													<div class="row permission-block">
														<div><spring:message code='register.stepThree.permission'/> <sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
														<div class="margintop10">
															<div class="radio radio-info radio-inline col-lg-2 col-md-3 col-sm-3 col-xs-12">
															<form:radiobutton name="viewOnly" checked="checked" value="viewOnly" path="permissionLevel" id="viewOnly" class="required" data-msg-required="Please select Permission Level Request!" />
															<label for="viewOnly"> <spring:message code='register.stepThree.view.order'/></label> 
															</div>
															<div class="radio radio-info radio-inline col-lg-2 col-md-3 col-sm-3 col-xs-12">
																<form:radiobutton name="placeOrder" path="permissionLevel" value="placeOrder" id="placeOrder" /><label for="placeOrder"><spring:message code='register.stepThree.place.order'/></label>
															</div>
															<!--AAOL-2429 and AAOL-2433 changes -->
															<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
																<div class="radio radio-info radio-inline"  id="noChargeSection">
																	<form:radiobutton name="noCharge" path="permissionLevel" value="noCharge" id="noCharge" /><label for="noCharge"><spring:message code='register.stepThree.place.noCharge'/></label>
																</div>
																
															</div>
															<div class="pull-right  your-information-note"><span><spring:message code='register.stepThree.user.role.note'/></span></div>
														</div>
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop30px fname-mobile"> <label class="fontweightnormal" for="firstName" ><spring:message code="register.stepThree.first.name"/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														<form:input type="text"  autocomplete="off" path="firstName" name="firstName" id="firstName" class="required form-control margintop30px alpha-only"  data-msg-required=""/>
														</div>
													<div id="firstNameError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="lastName"><spring:message code='register.stepThree.last.name'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input type="text" class="form-control margintop alpha-only" path="lastName" id="signup-lname"  placeholder=""/>
														</div>
													<div id="signup-lnameError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="organizationName"><spring:message code='register.stepThree.org.name'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														<form:input type="text"  autocomplete="off" path="orgName" name="organizationName" id="organizationName" class="form-control margintop required address-only" data-msg-required=""/> 
														</div>	
													<div id="organizationNameError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>															
													</div>
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><spring:message code='register.stepThree.department'/> <sup class="star"><spring:message code="register.stepOne.required.star"/></sup></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														 <form:select id="department" path="department" name="department" class="selectpicker margintop required" data-width="100%" data-msg-required="" >
																	<option value=""><spring:message code="signup.selection.text"/></option>
																	<c:if test="${not empty departments}">
																	<form:options items="${departments}"/>
																	</c:if>
														</form:select> 
														</div>
													<div id="departmentError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>			
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">
															<div><label class="fontweightnormal" for="emailAddress"><spring:message code='register.stepThree.email'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
															<div><em><spring:message code='register.stepThree.email.msg'/></em></div>
														</div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														 <form:input type="text"  autocomplete="off" path="emailAddress" name="emailAddress" id="emailAddress" class="form-control margintop comboInput email required" data-msg-required=""/>
														</div>
													<div id="emailAddressError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="reenterEmailAddress"><spring:message code='register.stepThree.reenter.email'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														 <input type="text"  autocomplete="off" name="reenterEmailAddress" id="reenterEmailAddress" class="form-control margintop comboInput email required" data-msg-required=""/>
													 		<div id="emailAddressValidateError" class="error errorYourInfo"></div>
														</div>
														<div id="reenterEmailAddressError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>
																										
													</div>													
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="supervisorName"><spring:message code='register.stepThree.super.name'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														 <form:input  type="text"  autocomplete="off" id="supervisorName" path="supervisorName" name="supervisorName" class="form-control margintop required alpha-only" data-msg-required="" /> 
														</div>	
													<div id="supervisorNameError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>													
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="supNumber" ><spring:message code='register.stepThree.super.phone'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
															<div class="boxtext1"><form:input type="text"  autocomplete="off" id="supervisorPhonePrefix" path="supervisorPhonePrefix" value="" name="supervisorPhonePrefix" class="form-control margintop phone-only" data-msg-required="<spring:message code='register.stepThree.required.super.phone'/>"/></div>
															<div class="boxtext2"><form:input  type="text"   autocomplete="off" id="supNumber" name="supNumber" path="supervisorPhone" class="form-control margintop required phoneWidth numbersonly removeSplChar" data-msg-required="<spring:message code='register.stepThree.required.super.phone'/>" />
															<input type="hidden"   id="supervisorPhone" name="supervisorPhone" value=""  /></div>
														</div>
													<div id="supNumberError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>																
													</div>
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="supervisorEmail"><spring:message code='register.stepThree.super.email'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														<form:input type="text"  autocomplete="off" path="supervisorEmail" name="supervisorEmail" id="supervisorEmail" class="form-control margintop comboInput email required" data-msg-required=""/>
														</div>	
														<div id="supervisorEmailError" class="error errorYourInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>	
													<div class="row">
														<div class="error" id="yourInformationError" hidden="true"></div>
									 					<a data-toggle="collapse" data-parent="#accordion" href="#" id="tab3NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return yourInformationValidate()"><spring:message code="register.stepOne.next"/></a>
									 				</div>																								
												</div>
											</div>
								<!-- Your Information Block End -->
								
								<!-- Personal Information Block Start -->
											<div class="profile-accordian panel">											
												<div class="profile-accordian-header">
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse4" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span><spring:message code='register.personal.information' /></a>
												</div>	
												<div id="collapse4" class="profile-accordian-body panel-collapse collapse bordertop">
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 "> <label class="fontweightnormal" for="regPhoneNumber"><spring:message code='register.stepThree.phone'/><sup class="star"><spring:message code="register.stepOne.required.star"/></sup></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
															<div class="boxtext1"><form:input type="text"  autocomplete="off" id="regPhoneNumberPrefix" path="phoneNumberPrefix" value="" class="form-control phone-only"   name="phoneNumberPrefix"/></div>
															<div class="boxtext2"><form:input  type="text" autocomplete="off" id="regPhoneNumber" path="phone" name="regPhoneNumber" class="form-control numbersonly removeSplChar" data-msg-required="<spring:message code='register.stepThree.required.phone'/>" />
															<input type="hidden" id="phone" name="phone" value="" />
															</div>
														</div>
														<div id="regPhoneNumberError" class="error errorPersonalInfo marginGap10  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>
													
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> 	 <label class="fontweightnormal" for="regMobileNumber"><spring:message code='register.stepThree.mobile'/></label>   </div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
															<div class="boxtext1"><form:input type="text" autocomplete="off" id="regMobileNumberPrefix" path="mobileNumberPrefix" value=""  class="comboSelection form-control margintop phone-only" name="mobileNumberPrefix"/></div>
															<div class="boxtext2"><form:input  type="text" autocomplete="off" id="regMobileNumber" path="mobile" name="regMobileNumber" class="comboInput phoneWidth  form-control margintop numbersonly removeSplChar" />
															<input type="hidden" id="mobile" name="mobile" value="" />
															</div>
														</div>
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"><label class="fontweightnormal" for="regFaxNumber"><spring:message code='register.stepThree.fax'/></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
															<div class="boxtext1"><form:input type="text" autocomplete="off" id="regFaxNumberPrefix" path="faxPrefix" value=""   class="comboSelection form-control margintop phone-only" name="faxNumberPrefix"/></div>
															<div class="boxtext2"><form:input  type="text" autocomplete="off" id="regFaxNumber" path="fax" name="regFaxNumber" class="comboInput phoneWidth  form-control margintop numbersonly removeSplChar" />
															 <input type="hidden" id="fax" name="fax" value=""  />	
															</div>
														</div>														
													</div>													
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="regCountry"  class="getErrorMessage" data="<spring:message code='register.stepTwo.required.country'/>"><spring:message code="register.stepTwo.country"/> <span class="star"><spring:message code="register.stepOne.required.star"/></span></label></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														    <form:select id="regCountry" name="regCountry" class="selectpicker margintop required" data-width="100%" data-msg-required="" path="country">
															<form:option value="">Select country or region</form:option>
														  	<form:options itemValue="isocode" itemLabel="name" items="${countryList}"/>
														</form:select>
														</div>
														<div id="regCountryError" class="error errorPersonalInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="regAddress1"><spring:message code="register.stepThree.personalAddressOne"/><span class="star"><spring:message code="register.stepOne.required.star"/></span></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:input name="regAddress1" autocomplete="off" path="addressLine1" id="regAddress1" class="form-control margintop address-only" data-msg-required=""/>
														</div>
														<div id="regAddress1Error" class="error errorPersonalInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">  <label class="fontweightnormal" for="regAddress2" ><spring:message code='register.stepThree.personalAddressTwo'/></label>     </div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:input path="addressLine2"  autocomplete="off" name="regAddress2" id="regAddress2"  class="form-control margintop address-only"/>
														</div>
													</div>
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop">  <label class="fontweightnormal" for="regCity" ><spring:message code="register.stepTwo.city"/> <span class="star"><spring:message code="register.stepOne.required.star"/></span></label></div>
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															 <form:input path="city" autocomplete="off" name="regCity" id="regCity" class="form-control margintop " data-msg-required=""/>																		
														</div>
														<div id="regCityError" class="error errorPersonalInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>			
													</div>
													<div class="row">	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop" > <label class="fontweightnormal" for="regState"><spring:message code='register.stepTwo.state'/><span class="star"><spring:message code="register.stepOne.required.star"/></span></label></div>																			
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12" id="UsRegStatediv">
															  <form:select id="regState" path="state" name="regState" class="form-control margintop " disabled ="false" data-width="100%" data-msg-required="<spring:message code='register.stepTwo.required.state'/>" >
																<option value=""><spring:message code="signup.selection.text"/></option>
															</form:select>																		
														</div>
														
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 hide" id="noUsRegStatediv">
															  <form:input id="noUsRegState" path="state" name="regState" class="form-control margintop " disabled ="true" data-width="100%" data-msg-required="<spring:message code='register.stepTwo.required.state'/>" />
														</div> 
														<div id="regStateError" class="error errorPersonalInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>	
													</div>
													<div class="row">
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 margintop"> <label class="fontweightnormal" for="zip" ><spring:message code='register.stepTwo.zipcode'/><span class="redStar"><spring:message code="register.stepOne.required.star"/></span></label></div>	
														<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
														<form:input path="zip" autocomplete="off" name="zip" id="zip" class="form-control margintop alphanumeric" data-msg-required=""/>
														</div>
														<div id="zipError" class="error errorPersonalInfo marginGap30  col-lg-4 col-md-4 col-sm-4 col-xs-12"></div>		
													</div>	
													<div class="row">
													<div class="error" id="personalInformationError" hidden="true"></div>
									 				  <a data-toggle="collapse" data-parent="#accordion" href="#" id="tab4NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return personalInformationValidate()"><spring:message code="register.stepOne.next"/></a>
									 				 </div>
												</div>
											</div>
							<!-- Personal Information Block End -->
							
							<!-- Email Preferences Block Start -->			
											<div class="profile-accordian panel">
												<div class="profile-accordian-header">
												<!-- 4910  -->
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse5" class="toggle-link panel-collapsed clickontext emailPreferenceLink"><span class="glyphicon glyphicon-plus plusicon"></span><spring:message code="register.setpFour.CommunicationPreferences"/></a>
												</div>
												 <form:input type="hidden"  id="backorderEmailType" path="backorderEmailType" value = ""/>
												<form:input type="hidden" id="shippedOrderEmail" path="shippedOrderEmailType" value = ""/>
												<form:input type="hidden" id="inoviceEmailPreference" path="invoiceEmailPrefType" value = ""/>
												<form:input type="hidden" id="deliveryNoteEmailPreference" path="deliveryNoteEmailPrefType" value = ""/>
												
												<div id="collapse5" class="profile-accordian-body panel-collapse collapse bordertop emailPreferenceAccordian">
												<div class="error" id="emailPreferencesError" hidden="true"></div>													
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<p><spring:message code="register.stepFour.body.text"/></p>
														<p class="marTop20"><spring:message code="register.stepFour.text"/></p>
														<ul class="checkbox checkbox-info checkboxmargin margintop10">
														<c:if test="${not empty emailPreferences}">
															<!-- form:checkboxes items="${emailPreferences}" element="li" path="emailPreferences" /-->															
															<c:forEach var="emailPrefrence" items="${emailPreferences}">
															<li>
																			<c:set var="key" value="${emailPrefrence.key}"></c:set>
																			<input type="checkbox"  class="profileEmailPrefrences" name="emailPreferences" id="check${count.count}
																				value="${emailPrefrence.key}"
																				<c:if test="${ key eq 'emailPreference12' || key eq 'emailPreference13'}">checked</c:if>> 
																				<label for="check${count.count}">	
																					${emailPrefrence.value}
																				</label>
																				<br/>
															
															</li>
															<c:if test="${ key eq 'emailPreference12' }">					
															<div id="radiodiv1" style="display: inline; margin-left: 25px; ">
																<form:radiobutton name="backdialy"  id="backdialy" value="daily" path="backorderEmailType"  style="opacity: 1"/>Daily &nbsp;&nbsp;&nbsp;    
										    					<form:radiobutton name="backdialy" value="weekly" id="backweekly" checked="checked" path="backorderEmailType" style="opacity: 1"/>Weekly
															</div><br/>
															</c:if>
															<c:if test="${ key eq 'emailPreference13' }">
															<div id="radiodivShippedOrder1" style="display: inline; margin-left: 25px;">
																<form:radiobutton name="shipdialy"  id="shipdialy" value="daily" path="shippedOrderEmailType" class="shipdaily" style="opacity: 1"/>Daily  &nbsp;&nbsp;&nbsp;     
										    					<form:radiobutton name="shipdialy" value="weekly" id="shipweekly" checked="checked" path="shippedOrderEmailType" class="shipweekly" style="opacity: 1"/>Weekly
															</div><br/>
															<!-- AAOL-4856 -->
															</c:if>
															 <c:if test="${ key eq 'emailPreference15' }">
															<div id="radiodiv" style="display: inline; margin-left: 25px;">
																<form:radiobutton name="delNotedialy"  id="deldialy" value="daily" path="deliveryNoteEmailPrefType" class="deldaily" style="opacity: 1"/>Daily  &nbsp;&nbsp;&nbsp;     
										    					<form:radiobutton name="delNotedialy" value="weekly" id="dekweekly" checked="checked" path="deliveryNoteEmailPrefType" class="delweekly" style="opacity: 1"/>Weekly
															</div><br/>
															</c:if>
															<c:if test="${ key eq 'emailPreference16' }">
															<div id="radiodiv2" style="display: inline; margin-left: 25px;">
																<form:radiobutton name="invdialy"  id="invdialy" value="daily" path="invoiceEmailPrefType" class="invdaily" style="opacity: 1"/>Daily  &nbsp;&nbsp;&nbsp;     
										    					<form:radiobutton name="invdialy" value="weekly" id="invweekly" checked="checked" path="invoiceEmailPrefType" class="invweekly" style="opacity: 1"/>Weekly
															</div><br/>
															</c:if> 
																<!-- AAOL-4856 -->
															</c:forEach>
															</c:if>
															</ul>
														</div>
													</div> 
													<div class="row">
									 				 	<a data-toggle="collapse" data-parent="#accordion" href="#" id="tab5NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return emailPreferencesValidate()"><spring:message code="register.stepOne.next"/></a>
									 				 </div>
												</div>
											</div>		
								<!-- Email Preferences Block End -->	
								
								<!-- Create Password Block Start -->										
											<div class="profile-accordian panel" id="passwordContainer">
												<div class="profile-accordian-header">
													<a data-toggle="collapse" data-parent="#accordion" href="#collapse6" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span><spring:message code='register.stepFive.header'/></a>
												</div>
													<div id="collapse6" class="profile-accordian-body panel-collapse collapse bordertop">	
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
															<div class='Newpassword signuppagepwd'>
																<div for='password'><spring:message code="signup.password.choose"/><sup class="star">*</sup></div>
																 <div class='pwdwidgetdiv' id='signuppwddiv' data-toggle='tooltip' data-placement="top" title="<spring:message code='profile.title.passwordStrength'/>" data-msg-required='<spring:message code="profile.profile.text"/>' data-msg-complexity='<spring:message code="profile.changePassword.errormessage.oldPasswords"/>' id="current-password"></div> 
																<noscript>
																<form:password  path="password" class="smallInputBox"  autocomplete="off" id="password" name="password" data-msg-required="" />
																</noscript>
															</div>
															<div class="reEnter"><spring:message code="signup.password.re-enter"/><sup class="star">*</sup></div>
															<input type="password" class="form-control pwdWidth" id="checkpass2">		
														</div>
														<div class="row">
														<div class="error pull-left" id="createpasswordError" hidden="true"></div>
									 					<a data-toggle="collapse" data-parent="#accordion" href="#" id="tab6NextButtonText" class="btn btnclsactive pull-right profile-nxt-btn" onclick="return createpasswordValidate()"><spring:message code="register.stepOne.next"/></a>
									 					</div>
													</div>	
												 </div>
											 </div>	
											<!-- Create Password Block End -->	
							
							<!-- Security Question Block Start -->						
								 <div class="profile-accordian panel">
								      <div class="profile-accordian-header">
											 <a data-toggle="collapse" data-parent="#accordion" href="#collapse7" class="toggle-link panel-collapsed clickontext"><span class="glyphicon glyphicon-plus plusicon"></span>	<spring:message code='register.security.questions' /></a>
										 </div>
										 <div id="collapse7" class="profile-accordian-body panel-collapse collapse bordertop">
										  <div class="row">
										     <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
										      <div class="error" id="signupsecretquestError" hidden="true"></div>
											 	 <c:forEach varStatus="status" begin="0" end="2">
													 <c:choose>
														  <c:when test="${status.index lt 3}">
															<div class="secret-quest-dropdown-label">
															    <formElement:jnjSecretQuestionFormBox selectCSSClass="required selectpicker select-security-ques" idKey="questionList${status.index}" items="${secretQuestions}" itemLabel="question" labelKey="profile.secret.question${status.index}" path="secretQuestionsAnswers[${status.index}].code" mandatory="false" errorMsg="<spring:message code='register.secQ'/>" />
																	 <input type="hidden" id="hddnQuestionErrorMsg"
																			 value="<spring:message code="error.secret.question.required" />">
																      <input type="hidden" id="hddnAnswerErrorMsg"
																			 value="<spring:message code="error.secret.answer.required" />">
																		<div class="cell">
																			<div class="registerError errorPosition"></div>
																		</div>
															</div>
															<div class="signup-secret-quest-text-label">
																<formElement:jnjProfileInputBox idKey="profile.secret.answer${status.index}" labelKey="profile.secret.answer${status.index}" path="secretQuestionsAnswers[${status.index}].answer" inputCSS="required form-control secQusCls" mandatory="false" />
															 </div>
														 </c:when>
												      <c:otherwise>
															 <div class="secret-quest-dropdown-label">
																 <formElement:jnjSecretQuestionFormBox selectCSSClass="required selectpicker select-security-ques" idKey="questionList${status.index}" items="${secretQuestions}" itemLabel="question" labelKey="profile.secret.question${status.index}" path="secretQuestionsAnswers[${status.index}].code"  mandatory="false" />
															 </div>
															 <div class="signup-secret-quest-text-label">
																 <formElement:jnjProfileInputBox idKey="profile.secret.answer${status.index}" labelKey="profile.secret.answer${status.index}" path="secretQuestionsAnswers[${status.index}].answer" inputCSS="required form-control" mandatory="false" />
																 </div>
													   </c:otherwise>  
													 </c:choose>
												 </c:forEach>
											 </div>																		
									      </div>
									      <div class="row">
											
									 		 <a data-toggle="collapse" data-parent="#accordion" href="#" id="tab7NextButtonText" class="btn btnclsactive pull-right" onclick="return secretQuestionValidate()"><spring:message code="register.stepOne.next"/></a>
									 	 </div>
										 </div>	
									  </div>
									  <!-- Security Question Block End -->	
								</div>	
							</div>

			<div class="conclusion">
				
				<div class="profileRegBlock">
					<div class="policy">
						<ul>
							<li class="registerPrivacynote checkbox checkbox-info checkboxmargin">
							<input id="terms-conditions"
								class="noTextInput" name="notif"
								data-msg-required="<spring:message code='register.acceptTerms'/>"
								type="checkbox" /> 
								<label for="notif"><spring:message
										code="text.register.policy.part1" />
										<a class="termconditionpopup_hn jnj-blue" href="#"><spring:message
											code="text.register.terms.conditions" /></a></label>
								</li>

							<li class="registerPrivacynote checkbox checkbox-info checkboxmargin">
								<div class="cell">
									<input id="privacy-policy" class="noTextInput"
										name="agreepolicy"
										data-msg-required="<spring:message code='register.acceptTerms'/>"
										type="checkbox" /> <label for="agreepolicy"><spring:message
											code="text.register.policy.part1" /> 
											<a class="privacypolicypopup_hn jnj-blue" data-toggle="modal" data-target="#privacypopup" href="#"><spring:message
												code="text.register.privacy.policy" /> </a>.</label>
												
								</div>
								 <div class="cell clear">
									<div class="registerError"></div>
								</div> 
							</li>

						</ul>

					</div>
				</div> 
				<div class="profileRegBlock">
					<div class="captchaInput overFlow">
						<label for="captcha"><spring:message
								code="text.register.validation" /><span><spring:message
									code="register.stepOne.required.star" /></span> <spring:message
								code="text.register.colon" /></label><br>
						<p>
							<spring:message code="text.register.captcha.instruction" />
						</p>
						<!-- Google reCaptcha -->
						<%-- <div id="recaptcha_content" style="width: 440px;">
							  <noscript> 
								<iframe
									src="https://www.google.com/recaptcha/api/noscript?k=6Le8aucSAAAAAHg-8At0to9flqyrhG2_ctqLbdAT"
									height="300" width="500" frameborder="0"></iframe>
									
								<br>
								<textarea name="recaptcha_challenge_field"
									id="recaptcha_challenge_field" rows="3" cols="40">
						                                   </textarea>
								<input type="hidden" name="recaptcha_response_field"
									id="recaptcha_response_field" class="required"
									data-msg-required='<spring:message code="text.register.invalid.code" />'
									value="manual_challenge" />
							</noscript>
						</div> --%>
						<div id="register_recaptcha_content" style="width: 440px;"></div>
						<script src="https://www.google.com/recaptcha/api.js?onload=otploadCallback&render=explicit&hl=${googleCaptchaLanguageIso}" async defer></script>
						
						<div class="cell">
							<div class="registerError registerErrorCaptcha"></div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="error" id="finalsubmissionError" hidden="true"></div>
				</div>
			</div>

		</div>	<!-- boxshadow Div End -->		
		 																	
								<div class="row">
									
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">	
										<a href="${loginUrl}" id="cancelButtonText"  class="btn btnclsactive pull-right cancel-register"><spring:message code="register.cancel.button.text"/></a>		
										<button type="submit" onclick="return validationRegistrationForm()" id="finishButtonText"  class="btn btnclsnormal pull-right submit-approval"><spring:message code="register.submit.button.text"/></button>									
									</div>
								</div>
		 </form:form>	
    

	<div id="privacypolicypopuopholder"></div>	
	<div id="termsandconditionsholder"></div>
	<div id="legalnoticepopupholder"></div>		
	<div id="contactuspopupholder"></div>		 
	
   
</template:registrationPage>
<!--  </div>  -->
