<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>

<template:master pageTitle="${pageTitle}">
		<%-- <div id="page">
			<!-- 1. headerWrapper START -->
			<div class="headerWrapper">
				<header id="jnj-main-header">
					<div class="row" id="jnj-header">						
						<div class="col-lg-12 shadow-header">
							<div class="col-lg-4 col-md-4 col-sm-3 col-xs-3 no-padding">
								<!-- <img src="images/logo.png" id="logo"></img> -->
								<cms:pageSlot position="SiteLogo" var="logo" limit="1">
									<cms:component component="${logo}" />
								</cms:pageSlot>
							</div>
													
						</div>				
					</div>						
				</header>
			BODY
			<div class="contentWrapper">
				<div id="content">
					Renders the Body of the parent JSP
					<jsp:doBody />
				</div>
			</div>
			FOOTER
			<div class="footerWrapper">
				<footer:footer/>
			</div>
		</div>
	</div> --%>

	<div class="jnj-signup-container full-height">
		<div class="row full-height" id="main-container">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 full-height" id="content-area">
				<header id="jnj-main-header">
					<div class="row" id="jnj-header">
						<div class="col-lg-12 shadow-header">
							<div class="col-lg-4 col-md-4 col-sm-3 col-xs-3 no-padding">
								<cms:pageSlot position="SiteLogo" var="logo" limit="1">
									<cms:component component="${logo}" />
								</cms:pageSlot>
							</div>
						</div>
					</div>
				</header>
				<section>
					<div class="row jnj-body-padding" id="jnj-body-content">
						<div class="col-lg-12 col-md-12" id="signuppage">
						<jsp:doBody />
						</div>
					</div>
				</section>
				<div class="footer-area">
				<footer>
					<div class="row jnj-grey" id="jnj-footer">
					<footer:footer/>
					</div>
				</footer>
				</div>
			</div>
		</div>
	</div>

</template:master>
