<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<link rel="stylesheet" type="text/css" media="all" href="/jnjb2bglobalstorefront/_ui/addons/jnjlaloginaddon/responsive/common/css/jnjlaloginaddon-6.3.0.css"/>

<c:url value="/home" var="homeUrl"/>

<templateLa:page pageTitle="${pageTitle}">
    <ul class="breadcrumb">
			<li><a href="${homeUrl}" ><messageLabel:message messageCode="error.page.home.label" /></a></li>
			<li class="active"><messageLabel:message messageCode="help.faq.bids" /></li>
	</ul>
    <iframe src="https://${pageContext.request.serverName}/bid/default.htm&sap-language=${language}&bp_acc=${distributerId}&contact-fname=${firstName}&contact-lname=${LastName}&contact-email=${emailId}"
    width="100%" height="2500"></iframe >

</templateLa:page>