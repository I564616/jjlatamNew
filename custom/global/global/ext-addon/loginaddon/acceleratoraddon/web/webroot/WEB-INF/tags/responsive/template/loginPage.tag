<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%-- <%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%> --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<template:master pageTitle="${pageTitle}">
					<jsp:doBody />
</template:master>