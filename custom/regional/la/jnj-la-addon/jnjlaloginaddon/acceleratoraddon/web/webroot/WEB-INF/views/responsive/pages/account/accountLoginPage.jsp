<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('jnj.la.login.email.regex')" var="allowChars"/>
<input type="hidden" id="regex-chars" value="${allowChars}"/>

<template:loginPage pageTitle="${pageTitle}">
    <input type="hidden" id="domainUrl" value="${domainUrl}"/>

	<body style="overflow: hidden" onload="checkCookie()">
		<div class="container-fluid" id="loginContainer">
			<div id="jnj-bg-img-holder">
				<cms:pageSlot position="BackgroundImage" var="logo" limit="1">
					<cms:component component="${logo}" />
				</cms:pageSlot>
			</div>
			<div class="main row">
				<div class="formContent ">

					<div class="jnjlogo row">
						<div class="col-6">
							<cms:pageSlot position="LoginLogo" var="logo" limit="1">
								<cms:component component="${logo}" />
							</cms:pageSlot>
						</div>
						<div class="col-2"></div>
						<div class="col-4">
							<div class="">
								<label class="pull-left" style="width: 40%; line-height: 40px"></label>
								<header:languageSelector languages="${languages}" currentLanguage="${currentLanguage}" />
							</div>
						</div>

					</div>

					<p class="displayinlineblk loginTxt">
						<spring:message code="login.header" />
					</p>

					<div class="float-right-to-none">
                        <c:url value="/register" var="registerActionUrl" />
                        <a href="${registerActionUrl}" type="button" class="btn displayinlineblk signUp">
                            <spring:message	code="login.user.profile.signup" />
                        </a>

                        <a href="${samlLoginUrl}" type="button" class="btn-md  pull-right submitBtn primarybtn">
                            <spring:message code ="login.sso"></spring:message>
                        </a>
                    </div>

					<p class="displayinlineblk pull-right newLink">
						<spring:message code="login.user.new" />
					</p>

					<form:form role="form" method="post" action="${pageContext.request.contextPath}/j_spring_security_check"
						id="loginForm" name="loginForm">
						<div class="form-group">
							<label for="email">
							<spring:message	code="login.user.login" />
							</label>
							<input type="text" data-msg-required="Username or password incorrect." class="login_img form-control required email-validate" id="j_username" name="j_username" />
						</div>
						<div class="form-group">
							<label for="pwd">
							<spring:message	code="login.user.password" />
							</label>
							 <a href="javascript:;"	class="password-forgotten pull-right">
							 <spring:message code="login.user.forgot" />
							 </a>
							 <input type="password"	data-msg-required="Username or password incorrect."	class="pass_img form-control required" id="j_password"	name="j_password" />
						</div>
						<div class="recPassError"></div>
						<c:if test="${!empty loginError}">
							<div class="registerError" style="color: red">
								<!-- Style Added by Vijay-->
								<c:choose>
									<c:when test="${attemptsExceeded ne null}">
										<label for="state" class="error">
										<label:message messageCode="login.attempts.exceededlatam" />
										</label>
									</c:when>
									<c:otherwise>
                                        <c:if test="${loginError ne 'passwordExpiredForThisUser'}">
                                            <label for="state" class="error">
                                            <spring:message	code="${loginError}" />
                                            </label>
                                        </c:if>
                                    </c:otherwise>
								</c:choose>
							</div>
						</c:if>
						<div class="row">
							<div class="col-sm-12 col-lg-12 col-md-12 mt-3">
								<button type="button" class="btn-md  pull-right submitBtn primarybtn " id="loginButton">
									<spring:message code="login.header" />
								</button>
								<div class="form-check checkbox displayinlineblk rememberMe pull-right checkbox-info">
								  <input class="form-check-input" type="checkbox" id="saveuserChkBox">
								  <label class="form-check-label" for="saveuserChkBox">
								    <spring:message	code="login.user.save" />
								  </label>
								</div>
							</div>
						</div>
					</form:form>
                    <div class="row login-disclaimer">
                        <div class="col-lg-12">
						    <cms:pageSlot position="logindisclaimerContent" var="disclaimer" limit="1">
				    		    <cms:component component="${disclaimer}" />
						    </cms:pageSlot>
					    </div>
					</div>
                    <span>
                        <b><label style="color: red ;font-weight: normal;margin-top: -18px;font-size: 11px;">
                        <spring:message code="browser.login.options" /></label></b>
               		</span>
					<div class="privacyLinks pull-right">
						<cms:pageSlot position="Footer" var="footer">
							<cms:component component="${footer}" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
		</div>

		<div id="privacypolicypopuopholder"></div>
		<div id="legalnoticepopupholder"></div>
		<div id="termsandconditionsholder"></div>
		<div id="contactuspopupholder"></div>
		<div id="forgotpasswordPopup"></div>

		<input type="hidden" id="passwordExpiryFlag"
			value="${fn:escapeXml(passwordExpired)}">
		<input type="hidden" id="passwordExpireToken"
			value="${fn:escapeXml(passwordExpireToken)}">
		<input type="hidden" id="helpFlag" value="${fn:escapeXml(helpFlag)}">
		<input type="hidden" id="email" value="${fn:escapeXml(email)}">
		<input type="hidden" id="loginPage" value=""/>
		<input type="hidden" id="resetFlag" value="${resetFlag}"/>
        <input type="hidden" id="token" value="${tokenOption.token}"/>
        <input type="hidden" id="errorCode" value="${tokenOption.errorCode}"/>
        <input type="hidden" id="errorMessage" value="${tokenOption.errorMessage}"/>
        <div id="resetPass"></div>
        <c:if test="${loginError eq 'passwordExpiredForThisUser'}">
            <input type="hidden" id="passwordExpiredForUser" value="${passwordExpiredForUser}">
        </c:if>
	</body>
</template:loginPage>
