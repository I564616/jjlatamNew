<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/_ui/responsive/common/images/spinner.gif" var="spinnerUrl" />
<spring:url value="/checkout/single/summary/setPaymentType.json" var="setPaymentTypeURL" />
<spring:url value="/checkout/single/summary/getCheckoutCart.json" var="getCheckoutCartUrl" />
<spring:url value="/checkout/single/summary/setPurchaseOrderNumber.json" var="setPurchaseOrderNumberURL" />
<script type="text/javascript"> // set vars
	var setPaymentTypeURL = '${setPaymentTypeURL}';
	var setPurchaseOrderNumberURL = '${setPurchaseOrderNumberURL}';
</script>

<div class="checkout_summary_flow_a" id="checkout_summary_paymentType_div">
	<div class="item_container_holder">
		<ycommerce:testId code="paymentType_text">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2><spring:theme code="checkout.summary.paymentType.header" htmlEscape="false"/><span></span></h2>
			</div>

			<div class="item_container">
				<div class="radiobuttons_paymentselection">
                    <c:forEach items="${paymentTypes}" var="paymentType">
                        <form:radiobutton path="paymentTypes" id="PaymentTypeSelection_${paymentType.code}" name="PaymentType" value="${paymentType.code}" label="${paymentType.displayName}"/><br>
                    </c:forEach>
				</div>
				
				<div class="pon">
					<label><spring:theme code="checkout.summary.purchaseOrderNumber"/></label>
					<br>
					<input type="text" id="PurchaseOrderNumber" style="left" name="PurchaseOrderNumber" maxlength="255" />
				</div>
			</div>
			
		</ycommerce:testId>
	</div>
	
</div>