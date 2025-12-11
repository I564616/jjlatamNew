<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/responsive/cart/quote"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title"
	code="cart.page.title" />
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/checkout" var="checkoutUrl" />
<cart:quotecart></cart:quotecart>
<commonTags:extendedTimeOutDiv/>
</templateLa:page>

<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartAddTocart.js"></script>