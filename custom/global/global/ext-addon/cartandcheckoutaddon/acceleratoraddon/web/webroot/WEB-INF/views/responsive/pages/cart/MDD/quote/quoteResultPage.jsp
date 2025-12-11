<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/quote"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/addons/loginaddon/shared/analytics" %>
<spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/checkout" var="checkoutUrl" />

<template:page pageTitle="${pageTitle}">
<analytics:orderConfirmationAnalytics/>				
<cart:quoteResultPage></cart:quoteResultPage>
</template:page>

<%--<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartAddTocart.js"></script>--%>