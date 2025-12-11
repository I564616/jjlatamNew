<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url value="/cart/checkout" var="checkoutUrl" />

<form action="${checkoutUrl}" id="cartValidateForm" method="post">
	<input type="hidden" value="${timeOutExtended}" name="timeOutExtended" >
</form>