<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('website.mdd-deCMSsite.https')" var="env" scope="session"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('sso.login.url')" var="ssoUrl" scope="session"/>
<template:loginPage pageTitle="${pageTitle}">
	<body onload="checkCookie()" style="overflow: hidden">
		<div class="container" id="loginContainer">
		<!-- BackgroundLoginMobileImage BackgroundImage -->
			<div id="jnj-bg-img-holder">
				<div class="hidden-xs hidden-sm jnj-bg-desk-img">
					<cms:pageSlot position="BackgroundImage" var="logo" limit="1">
						<cms:component component="${logo}" />
					</cms:pageSlot>
				</div>	
				
				<div class="hidden-lg hidden-md hidden-xs jnj-bg-tab-img">
					<cms:pageSlot position="BackgroundLoginMobileImage" var="logo" limit="1">
						<cms:component component="${logo}" />
					</cms:pageSlot>
				</div>
			</div>
			<div class="main row login-disc-container">
				<div class="row formContent logincontent-area">

					<div class="jnjlogo row">
						<div class="col-xs-6">
							<cms:pageSlot position="LoginLogo" var="logo" limit="1">
								<cms:component component="${logo}" />
							</cms:pageSlot>
						</div>

						<div class="col-xs-6">
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
						<a href="${registerActionUrl}" type="button" class="btn btnsnormal displayinlineblk signUp" style="margin-right:5px">
							<spring:message	code="login.user.profile.signup" />
						</a>
						
						<a href="${env}${ssoUrl}" type="submit" class="btn btnsactive submitBtn primarybtn " id="loginButton">
							<!-- <button type="submit" class="btn-md  pull-right submitBtn primarybtn " id="loginButton"> -->
							<spring:message code ="login.sso"></spring:message>
							<!--</button> -->
						</a>
					
					</div>
					<p class="displayinlineblk pull-right newLink">
						<spring:message code="login.user.new" />
					</p>
					<c:url value="/j_spring_security_check" var="loginActionUrl" />
					<form:form role="form" method="post" action="${loginActionUrl}"
						id="loginForm" name="loginForm">
						<div class="form-group">
							<label for="email">
							<spring:message	code="login.user.login" />
							</label> 
							<input type="text" data-msg-required="<spring:message code='login.invalidUserPwd'/>" class="login_img form-control required" id="j_username" name="j_username" />
						</div>
						<div class="form-group">
							<label for="pwd">
							<spring:message	code="login.user.password" />
							</label>
							 <div href="javascript:;"	class="password-forgotten pull-right">
							 <spring:message code="login.user.forgot" />
							 </div> 
							 <input type="password"	data-msg-required="<spring:message code='login.invalidUserPwd'/>"	class="pass_img form-control required" id="j_password"	name="j_password" />
						</div>
						<div class="recPassError"></div>
						<c:if test="${!empty loginError}">
							<div class="registerError" style="color: red">
								<!-- Style Added by Vijay-->
								<!-- AAOL-4915 -->
								<c:choose>
									<c:when test="${attemptsExceeded ne null}">
										<label for="state" class="error">
										 <spring:message code="login.attempts.exceeded" />
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
								<!-- End AAOL-4915 -->
							</div>
						</c:if>
						<button type="submit" class="btn-md  pull-right submitBtn primarybtn " id="loginButton">
							<spring:message code="login.header" />
						</button>
						<div class="checkbox displayinlineblk rememberMe pull-right checkbox-info ">
							<input id="saveuserChkBox" class="styled" type="checkbox">
							<label for="saveuserChkBox "> 
							<spring:message	code="login.user.save" />
							</label>
						</div>
					</form:form>
					<div class="row login-disclaimer"><div class="col-lg-12">
							<cms:pageSlot position="logindisclaimerContent" var="disclaimer" limit="1">
								<cms:component component="${disclaimer}" />
							</cms:pageSlot>
						</div>
					</div>
					<!--Added Disclaimer for Demo comments AAOL - 5399 -->
					<span>
						<b><label style="color: red ;font-weight: normal;margin-top: -18px;font-size: 12px;"><spring:message code="browser.login.options" /></label></b>
					</span>
					
					<div class="privacyLinks pull-right">
						<cms:pageSlot position="Footer" var="footer">
							<input type="hidden" name="pdfPopupGenration" id="pdfPopupGenration" value="${pdfPopupGenrationFlg}"/>
							<cms:component component="${footer}" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
		</div>
		<!-- start - video popup -->
		<!-- Modal -->
		<%-- <div class="modal fade jnj-popup" id="video-popup" role="dialog">
			<div class="modal-dialog modalcls modal-lg">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close clsBtn" id="video-close"	data-dismiss="modal">
							<spring:message code="account.change.popup.close" />
						</button>
						<h4 class="modal-title">
							<spring:message code="login.tour.header" />
						</h4>
					</div>
					<div class="modal-body">
						<iframe id="jnj-inro-video" width="100%" height="500" src="https://www.youtube.com/embed/GXWrDSCl1Jw" frameborder="0" allowfullscreen>
						</iframe>
					</div>
				</div>
			</div>
		</div> --%>
		<div id="privacypolicypopuopholder"></div>
		<div id="legalnoticepopupholder"></div>
		<div id="termsandconditionsholder"></div>
		<div id="contactuspopupholder"></div>
		<div id="forgotpasswordPopup"></div>

<div class="modal fade jnj-popup-container" id="cookiepopup" role="dialog"
			data-firstLogin='true'>
			<div class="modal-dialog modalcls">
				<div class="modal-content popup">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close" /></button>
						<h4 class="modal-title selectTitle"><spring:message code="login.user.cookie" /></h4>
					</div>
					<div class="modal-body">

						<cms:pageSlot position="CookieContent" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
					<div class="modal-footer ftrcls">
						<a href="#" class="pull-left canceltxt"><spring:message	code="login.user.cancel"/></a>
						<button type="button" class="btn btnclsactive"
							data-dismiss="modal" id="accept-btn"><spring:message code="login.user.ok"/></button>
					</div>
				</div>
			</div>
		</div>

		<input type="hidden" id="passwordExpiryFlag"
			value="${passwordExpired}">
		<input type="hidden" id="passwordExpireToken"
			value="${passwordExpireToken}">
		<input type="hidden" id="helpFlag" value="${helpFlag}">
		<input type="hidden" id="email" value="${email}">
       	<input type="hidden" id="loginPage" value=""/>
       	<div id="resetPass"></div>
       	<!-- AAOL-4915 -->
       	<c:if test="${loginError eq 'passwordExpiredForThisUser'}">
			<input type="hidden" id="passwordExpiredForUser" value="${passwordExpiredForUser}">
		</c:if>
		<!-- End AAOL-4915 -->
	</body>
</template:loginPage>