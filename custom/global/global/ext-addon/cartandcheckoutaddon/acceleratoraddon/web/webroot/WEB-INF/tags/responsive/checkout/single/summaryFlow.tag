<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/responsive/checkout/single" %>

<spring:url value="/checkout/single/summary/getCheckoutCart.json" var="getCheckoutCartUrl" />

<script type="text/javascript"> // set vars
	 var getCheckoutCartUrl = '${getCheckoutCartUrl}';
</script>

<div class="checkout_summary_flow">
	<checkout:summaryFlowPaymentType />
	<checkout:summaryFlowCostCenter costCenter="${costCenter}" />
	<checkout:summaryFlowPayment />

	<checkout:summaryFlowDeliveryAddress deliveryAddress="${deliveryAddress}" costCenter="${costCenter}"/>
	<checkout:summaryFlowDeliveryMode deliveryMode="${deliveryMode}" />
</div>
