<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/cart/checkout" var="checkoutUrl" />
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/reviseOrder" var="cartPageUrl" />
<c:url value="/checkout/single/placeOrder?skipSAPWS=${skipSAPWS}" var="orderConfirmationURL" />

<ul>
	<li><a href="${homePageUrl}"><spring:message code="cart.review.cartPageAction.continue"/></a></li>
	<li class="center"><a href="${cartPageUrl}" class="returnToCart"><spring:message code="cart.validate.revisedOrder"/></a></li>
	<li class="center"><a href="javascript:;" class="saveorderastemplate"><spring:message code="cart.review.cartPageAction.saveTemplate"/></a></li>
	<li class="mar0"><a href="javascript:;"  class="secondarybtn floatRight placeOrderBtn"><spring:message code="cart.common.placeOrder"/></a> </li>
</ul> 
	