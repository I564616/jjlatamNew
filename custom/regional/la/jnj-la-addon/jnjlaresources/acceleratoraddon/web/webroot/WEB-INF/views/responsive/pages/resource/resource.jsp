<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
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
			<form action="${sendEmail}" class="contactUsFormLb" method="POST">
				<div class="row">
					<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6">
						<div class="">
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
											data-msg-required="Please enter your name" value="" />
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
											data-msg-required="Please enter your email" value="" />
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

							<div class="row">
								<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 margintop10"
									style="width: 200%">
									<div class="form-group">
										<label for="contactUsMessage"><spring:message
												code="help.page.contactus.message" /><span class="redStar">*</span></label>

										<textarea rows="5" id="contactUsMessage"
											class="required form-control" name="contactUsMessage"
											data-msg-required="<spring:message code='help.page.message.error'/>"></textarea>
									</div>
								</div>
							</div>
							<div class="content contactUsFormText checkbox checkbox-info selectchkbox inline-element"
								style="width: 209%; border-bottom: 1px solid #ccc;">
								<input id="contactus-agree" class="styled" type="checkbox">
                                <label for="contactus-agree"><spring:message code="help.page.contactus.help.statement" /></label>
							</div>
							<div class="modal-footer">
								<button type="button" class=" primarybtn contactUsFormSubmitLb btnclsactive contactUsSubmitBtn pull-right btn-disabled-style"  disabled>
							<spring:message code="help.page.contactus.submit" />
						</button>
							</div>
							
							<div class="contactUsFormPage2Lb" id="contactUsFormPage2Lbs"
								style="display: none;">
								<div>
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
									<a class="backToContactUsForm" href="#"><spring:message
											code="help.page.contactus.success.anothermessage" /></a>
								</div>
							</div>

						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	</div>
</templateLa:page>