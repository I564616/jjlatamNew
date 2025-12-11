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

<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/form"%>
<%@ taglib prefix="registration"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/registration"%>
 <div class=" jnj-signup-container"> 
<template:registrationPage pageTitle="${pageTitle}">
	 <div class="row jnj-body-padding" id="jnj-body-content"> 
	 	<div class="col-lg-12 col-md-12" id="signuppage">
		 	<%-- <div class="row content">
				<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12"><spring:message code="signup.header.text"/></div>
			</div> --%>
			<div class="boxshadow">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

						<div class="intro-content signup-success-content"> 
								<cms:slot var="content" position="MainBody">
									<cms:component component="${content}" />
								</cms:slot>
						</div>
					</div>
				</div>
			</div>
				<c:url value="/login" var="loginUrl"></c:url>
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">		
						<a href="${loginUrl}" class="btn btnclsactive pull-right"><spring:message code="signup.success.login"/></a>														
					</div>
				</div>
			</div>
		</div>
	
	<div id="privacypolicypopuopholder"></div>	
	<div id="termsandconditionsholder"></div>
	<div id="legalnoticepopupholder"></div>		
	<div id="contactuspopupholder"></div>
	
</template:registrationPage>
 </div>