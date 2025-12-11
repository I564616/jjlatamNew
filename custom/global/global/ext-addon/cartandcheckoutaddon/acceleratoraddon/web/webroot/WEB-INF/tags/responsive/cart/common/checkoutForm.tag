<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:url value="/checkout/single/placeOrder" var="placeOrderURL" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form action="${placeOrderURL}" id="cartCheckoutForm" method="GET">
	<input type="hidden" value="${timeOutExtended}" name="timeOutExtended" >	
</form:form>