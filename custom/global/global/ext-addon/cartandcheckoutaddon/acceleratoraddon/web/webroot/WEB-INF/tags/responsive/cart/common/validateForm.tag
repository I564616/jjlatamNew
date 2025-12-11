<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="/cart/checkout" var="checkoutUrl" />

<form:form action="${checkoutUrl}" id="cartValidateForm" method="post">
	<input type="hidden" value="${timeOutExtended}" name="timeOutExtended" >
</form:form>