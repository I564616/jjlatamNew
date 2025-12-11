<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="total">
	<p>
		<span><spring:message
				code="cart.validate.cartTotal.subTotal" /></span> <span
			class="jnjID"><format:price
				priceData="${orderData.totalNetValue}" displayFreeForZero="false" /></span>
	</p>	
	<c:if test="${orderData.totalFees.value > 0.0}">
		<p class="slideDownButton">
			<span><spring:message
					code="cart.validate.cartTotal.fees" /></span> <span class="jnjID"><format:price
					priceData="${orderData.totalFees}" displayFreeForZero="false" /></span><span
				class="iconExpandCollapse freightIcon" role="directionLeft"
				style="background-position: 0px -19px;"> </span>
		</p>
		
		<div style="display: block;">
			<c:if test="${orderData.totalminimumOrderFee.value  > 0.0}">
				<p class="freightTxt">
					<span><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></span> <span class="jnjID"> <format:price	priceData="${orderData.totalminimumOrderFee}" /></span>
				</p>
			</c:if>
			<c:if test="${orderData.totalDropShipFee.value  > 0.0}">
				<p class="freightTxt">	
					<span><spring:message code="cart.validate.cartTotal.dropShipFee" /></span> <span	class="jnjID"> <format:price priceData="${orderData.totalDropShipFee}" /></span>
				</p>	
			</c:if>
	</div>
	</c:if>

	<c:if test="${orderData.hsaPromotion.value  > 0.0}">
	<p>
		<span><spring:message
				code="cart.validate.cartTotal.hsaPromotion" /></span> <span
			class="jnjID"><format:price
				priceData="${orderData.hsaPromotion}" displayFreeForZero="false" /></span>
	</p>
	</c:if>
	<c:if test="${orderData.totalFreightFees.value  > 0.0}">
		<p>
			<span><spring:message
					code="cart.validate.cartTotal.freightCost" /></span> <span
				class="jnjID"><format:price
					priceData="${orderData.totalFreightFees}" displayFreeForZero="false" /></span>
		</p>
	</c:if>
	<c:if test="${orderData.totalTax.value  > 0.0}">
	<p>
		<span><spring:message code="cart.validate.cartTotal.tax" /></span>
		<span class="jnjID"><format:price
				priceData="${orderData.totalTax}" displayFreeForZero="false" /></span>
	</p>
	</c:if>
	<p class="totalSum">
		<span><spring:message
				code="cart.validate.cartTotal.orderTotal" /></span> <span
			class="jnjID"><format:price
				priceData="${orderData.totalPrice}" displayFreeForZero="false" /></span>
	</p>
</div>
