<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:url value="/home" var="home" />

<div class="success">
<p>
	<label>
		<spring:message code='batch.checkout.error.text' />
		<br />
	</label>
</p>
</div>
<div class="batchConfirmationMsg">
	<div class="btnContainer">
		<a href="${home}" class="secondarybtn">
			<spring:message code='cart.confirmation.done' />
		</a>
	</div>
	<div class="txtContainer">
		<spring:message code='batch.checkout.order.number' />
		&nbsp;
		${orderData.orderNumber}
		<br />
		<spring:message code='batch.checkout.message' />
	</div>
</div>