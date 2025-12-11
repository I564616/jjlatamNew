<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="internationalCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/international"%>
<template:page pageTitle="${pageTitle}">
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/checkout" var="checkoutUrl" />
<input type="hidden" value="${canValidateCart}" id="canValidateCart">
<input type="hidden" value="${displayBatchModeAlert}" id="displayBatchModeAlert">


	<!-- globalMessages : START -->
	<%-- <div id="globalMessages">
		<common:globalMessages />
		<cart:cartRestoration />
		<cart:cartValidation />
	</div> --%>


	<c:if test="${empty cartData.entries}">
 <cart:emptyCart/>
</c:if>
<c:if test="${ not empty cartData.entries}">
 <internationalCart:cartEntries />
 
</c:if>

<cart:saveAsTemplateDiv/>
</template:page>


