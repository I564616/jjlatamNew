<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer" %>

<template:master pageTitle="${pageTitle}">
	<div id="wrapper">
		<div id="page">
			<!-- 1. headerWrapper START -->
			<div class="headerWrapper">
				<div id="header" class="loginHeader">
					<div class="siteLogo">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" />
						</cms:pageSlot>
					</div>
				</div>
			</div>
			<%-- BODY --%>
			<div class="contentWrapper">
				<div id="content">
					<%-- Renders the Body of the parent JSP --%>
					<jsp:doBody />
				</div>
			</div>
			<%-- FOOTER --%>
			<div class="footerWrapper">
				<footer:footer/>
			</div>
		</div>
	</div>
</template:master>