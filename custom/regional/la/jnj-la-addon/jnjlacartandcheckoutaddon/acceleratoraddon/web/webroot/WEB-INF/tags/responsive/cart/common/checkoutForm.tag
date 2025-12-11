<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url value="/checkout/single/placeOrder" var="placeOrderURL" />
<form:form action="${placeOrderURL}" id="cartCheckoutForm" method="GET">
	<input type="hidden" value="${timeOutExtended}" name="timeOutExtended" >
</form:form>