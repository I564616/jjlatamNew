<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="consignmentFillUpCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentFillUp"%>
<!-- consignmentFillUp/cartValidationPage.jsp -->
<template:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title" code="cart.page.title" />
<consignmentFillUpCart:validateEntries />
<cart:saveAsTemplateDiv/>
</template:page>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
