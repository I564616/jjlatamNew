<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="resource"
	tagdir="/WEB-INF/tags/addons/jnjglobalresources/responsive/resource"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>

<script type="text/javascript">
	 var captchaSiteKey = "${googleCaptchaKey}";
      var otploadCallback = function() {
    	  console.log("Help Page");
        captchaContainer = grecaptcha.render('help_recaptcha_content', {
          'sitekey' : captchaSiteKey,
          'callback' : function(response) {
            if(response!= ' '){
            	document.getElementById("helpContactUsFormSubmitLb").disabled=false;
            	//$('#helpContactUsFormSubmitLb').removeClass('btn-disabled-style');
            	$("#captchaResponse").val(response); 
            	$('.contactUsErrorCaptcha').hide();
            	
            }
          }
        });
      };

</script>
<input type="hidden" id="nameOfCustomerValue" value="${nameOfCustomer}"/>
<input type="hidden" id="captchaResponse" value=""/> 
<template:page pageTitle="${pageTitle}">
	<div id="help-page">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row content">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code="resources.help" />
			</div>
		</div>
		<div class="boxshadow ">
			<c:set var="count" value="0" />
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 boxshadow"
				id="accordion" style="padding-left: 0px; padding-right: 0px">

				<cms:pageSlot position="MainBody" var="feature">
					<c:set var="name" value="${feature.name}" />


					<c:if test="${fn:contains(name ,'paragraph')}">
						<c:set var="count" value="${count}" />

						<c:if test="${count != '0'}">
			</div>
		</div>
		</c:if>
		<div class="help-accordian panel">
			<div class="help-accordian-header">

				<a data-toggle="collapse" data-parent="#accordion"
					href="#collapse${count}" class="ref_no toggle-link panel-collapsed"><span
					class="glyphicon glyphicon-plus help-accordian-icon"></span> <cms:component
						component="${feature}" /> </a>

			</div>
			<div class="help-accordian-body panel-collapse collapse"
				id="collapse${count}">
				<c:set var="count" value="${count+1}" />
		</c:if>



		<c:if test="${fn:contains(name ,'linkcomponent')}">


			<div class="help-links">
				<cms:component component="${feature}" />

			</div>

		</c:if>




		</cms:pageSlot>
	</div>
	</div>

	<div class="boxshadow jnj-popup" id="contactuspopup"
		style="margin-top: 20px">
		<div class="modal-header">
			<h4 class="modal-title">
				<spring:message code="help.page.contactus.label" />
			</h4>
		</div>

		<div class="modal-body">
			<c:url value="/help/contactUs" var="sendEmail"></c:url>
				<form:form action="${sendEmail}" class="contactUsFormLb" method="POST">
							<div class="row">
								<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
									<p><spring:message
												code="help.page.contactus.text" /></p>
									<div class="size1of1 cell">
										<div class="cell">
											<div class="contactUsGlobalError registerError">
												<label class="error" style="display: none;"><spring:message
												code="help.page.contactus.reqfields" />.<br />
												</label>
											</div>
										</div>
									</div>
									<div class="form-group ">
										<label for="contactUsFromNameLb"><spring:message
												code="help.page.contactus.from" />:<span class="redStar">*</span></label>
										<c:choose>
											<c:when test="${not empty nameOfCustomer}">
												<input name="contactUsFromName" id="contactUsFromNameLb"
													value="${nameOfCustomer}" disabled="disabled"
													class="form-control" />
											</c:when>
											<c:otherwise>
												<input name="contactUsFromName" class="required form-control"
													id="contactUsFromNameLb"
													data-msg-required="<spring:message code='help.page.contactus.enterName'/>" value="" />
											</c:otherwise>
										</c:choose>
									</div>
		
									<div class="form-group">
										<label for="contactUsEmailLb"><spring:message
												code="help.page.contactus.email" />:<span class="redStar">*</span></label>
										<c:choose>
											<c:when test="${not empty emailOfCustomer}">
												<input name="contactUsEmail" id="contactUsEmailLb"
													value="${emailOfCustomer}" disabled="disabled"
													class="form-control" />
											</c:when>
											<c:otherwise>
												<input name="contactUsEmail" class="required form-control"
													id="contactUsEmailLb"
													data-msg-required="<spring:message code='help.page.contactus.enterEmail'/>" value="" />
											</c:otherwise>
										</c:choose>
									</div>
									<div class="form-group">
										<div>
											<label for="contactUsSubjectLb"> <spring:message
													code="help.page.contactus.subject" />:<span class="redStar">*</span></label>
										</div>
		
										<select class="required" id="contactUsSubjectLb"
											name="contactUsSubject"
											data-msg-required="<spring:message code='help.page.subject.error'/>">
											<option value=""><spring:message
													code="help.page.issue.selectanyone" /></option>
											<c:forEach items="${subjectDropDown}" var="subjectMap">
												<option value="${subjectMap.key}"><spring:message
														code="${subjectMap.value}" /></option>
											</c:forEach>
										</select>
										<div class="registerError"></div>
									</div>
									<div id="contactUsOrderNumberLb" class="contactUsOrderNumberLb"
										style="display: none;">
										<label for="contactUsOrderNumberLb"><spring:message
												code="help.page.contactus.ordernumber" />:<span
											class="redStar">*</span></label><br> <input class="form-control"
											id="contactUsOrderNumber" name="contactUsOrderNumberLb"
											data-msg-required="<spring:message code='help.page.order.error'/>"
											required="required">
									</div>
									<!-- AAOL-6476 -->
									 <div id="contactUsProductNumber1" class="contactUsProductNumberLb" style="display: none">
										<label><spring:message code="help.page.contactus.productnumber" /></label><br>
										<input id="contactUsProductNumber" class="form-control " name="contactUsProductNumber" />
									</div>
								</div>
							</div>	
							<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 margintop10">
									<div class="form-group">
									<!-- AAOL-4728 -->
										<label for="contactUsMessage"><spring:message
												code="help.page.contactus.message" />: <span class="redStar">*</span></label>
			
										<textarea rows="5" id="contactUsMessage"
											class="required form-control" name="contactUsMessage"
											data-msg-required="<spring:message code='help.page.message.error'/>"></textarea>
										<!-- AAOL-4914 Adding Placeholder for captcha content -->
										<div class="profileRegBlock"  style="width: 354px;padding: 0px;">
										<div class="captchaInput overFlow">
										<spring:message code="text.register.validation" /><span>
										<spring:message code="register.stepOne.required.star" /></span> 
										<spring:message code="text.register.colon" /></label><br>
												<p>
													<spring:message code="text.register.captcha.instruction" />
												</p>
												<div id="help_recaptcha_content" style="width: 440px;">
												</div>
												<script src="https://www.google.com/recaptcha/api.js?onload=otploadCallback&render=explicit&hl=${googleCaptchaLanguageIso}" async defer></script>
												<input type="hidden" id="contactUsCaptchaId"
													value='<spring:message code="error.text.validation.captcha"/>'/>
												<div class="contactUsErrorCaptcha"></div>
											</div>
										</div>
										<!-- End AAOL-4914-->
									</div>
								</div>
							</div>
							<!-- AAOL-3624 -->
							<div class="checkbox checkbox-info selectchkbox inline-element" style="border-bottom: 1px solid #ccc;">
			                             <input id="contactHelp-agree" class="styled" type="checkbox">
			                             <label for="contactus-agree"><spring:message code="help.page.contactus.popup.statement" /></label>
			     					</div>
							<!-- AAOL-3624 -->
							 <div class="modal-footer" style="text-align:center">
								<button type="button" class="btnclsactive contactUsSubmitBtn btn-disabled-style" id="helpContactUsFormSubmitLb">
									<spring:message code="help.page.contactus.sendbutton"/> 
								</button>
							</div>
						
							<div  class="contactUsFormPage2Lb" id="contactUsFormPage2Lbs"
								style="display: none;width:200%">
								<div class="panel-group">
									<div class="panel panel-success">
										<div class="panel-heading">
											
											<h3>
											<spring:message
												code="help.page.contactus.success.successmessage" />
											</h3>
											<p>
												<spring:message
													code="help.page.contactus.success.thankyoumessage" />
											</p>
											<p>
												<spring:message
													code="help.page.contactus.success.responsemessage" />
											</p>
											
										</div>
									</div>
								</div>	
							
								<div><a class="backToContactUsForm" href="#"><spring:message
												code="help.page.contactus.success.anothermessage" /></a>
									</div>
								</div>
							
					
				</form:form>
			</div>
		</div>
	</div>

</template:page>