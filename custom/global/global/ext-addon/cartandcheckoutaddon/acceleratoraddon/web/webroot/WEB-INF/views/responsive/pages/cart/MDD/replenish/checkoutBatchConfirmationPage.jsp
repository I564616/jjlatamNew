<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="commonCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/common" %>
<!-- replenish/checkoutBatchConfirmationPage.jsp -->
<template:page pageTitle="${pageTitle}">
	<spring:theme text="Your Shopping Cart" var="title"
	code="cart.page.title" />
	<commonCart:batchConfirmationMessage />
</template:page>