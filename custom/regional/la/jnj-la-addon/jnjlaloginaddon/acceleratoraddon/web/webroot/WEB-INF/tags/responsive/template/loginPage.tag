<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ tag pageEncoding="UTF-8" %>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="headerLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/common/header"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<template:master pageTitle="${pageTitle}">
	<div id="wrapper redBgcolor">
		<div id="page">
			<!-- 1. headerWrapper START -->
			<div class="headerWrapper languageHeader" style="height: 100px;">
				<div class="loginControl pull-right">
					<ul style="margin-top: 0;">
						<headerLa:languageSelector languages="${languages}" currentLanguage="${currentLanguage}" />
					</ul>
				</div>
			</div>
		</div>
	</div>
	<jsp:doBody />
</template:master>