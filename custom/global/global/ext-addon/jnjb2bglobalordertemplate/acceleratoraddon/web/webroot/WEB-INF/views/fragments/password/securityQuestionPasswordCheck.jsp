<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/desktop/nav/pagination"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<template:page pageTitle="${pageTitle}">
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>
	<div class="pageBlock ">
		<h1>
			<spring:message code="profile.changePassword.myprofile" />
		</h1>
		<div class="pageBlockDivision myProfile sectionBlock">
			<nav:accountNav selected="jnjGTChangepassword" />
			<%-- <nav:accountNav selected="jnjNAChangepassword" /> --%>
			<div class="span-18 last" id="myEmailPref">
				<div class="myProfilePanel changeSecurityQuestions">
					<h2>
						<spring:message code="profile.changeSecurityQuestion.text" />
					</h2>
					<p>
						<label:message
							messageCode="profile.changeSecurityQuestion.updateQuestions" />
					</p>
					<div id="changeQuestionLightBox" class="lightboxtemplate">
						<h2>
							<spring:message code="profile.changeSecurityQuestion.securityCheck"/><span class="close"></span>
						</h2>
						<c:url var="verifyCurrentPassword"
							value="/my-account/verifyCurrentPassword" />
						<form:form id="changeQuestionForm" commandName="checkPassword"
							method="POST" action="${verifyCurrentPassword}">
							<ul>
								<li>
									<div>
										<label for="chkPassword"><spring:message code="profile.changeSecurityQuestion.enterpassword"/></label>
									</div>
									<div>
										<form:input type="password" id="chkPassword" class="required"
											path="currentPassword" data-msg-required="<spring:message code='profile.changeSecurityQuestion.passwordRequired'/>" />
									</div>

										<div class="registerError">
											<c:if test="${success=='false'}">
												<label class="error"><spring:message code="profile.changeSecurityQuestion.passwordnotvalid"/></label>
											</c:if>
										</div>
								</li>
							</ul>
							<div class="buttonWrapperWithBG myProfileButtonWrapper">
								<a href="<c:url value='/my-account/personalInformation'/>"
									class="tertiarybtn"><spring:message code='profile.cancel'/></a> <input class="secondarybtn"
									value="OK" type="submit" id="changeQuestionPasswordSubmit" />
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>