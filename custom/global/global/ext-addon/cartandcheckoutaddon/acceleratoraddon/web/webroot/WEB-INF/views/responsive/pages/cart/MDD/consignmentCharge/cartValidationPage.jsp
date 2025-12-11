<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="consignmentChargeCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentCharge"%>
<!-- consignmentCharge/cartValidationPage.jsp -->
<template:page pageTitle="${pageTitle}">
<!-- <c:if test="${pOvalidation eq 'duplicate'}">
<div style="color: #FF4500;" id="poDuplicate"><spring:message code="consignment.return.po.duplicate"/></div>	</c:if> -->
  <spring:theme text="Your Shopping Cart" var="title" code="cart.page.title" />
  <consignmentChargeCart:validateEntries />
  <cart:saveAsTemplateDiv />
</template:page>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
