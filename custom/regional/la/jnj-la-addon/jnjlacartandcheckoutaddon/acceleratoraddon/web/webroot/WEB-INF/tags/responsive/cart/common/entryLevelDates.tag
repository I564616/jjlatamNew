<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${entry.status ne 'BACKORDERED'}">
		<p class="textLeft">
			<span class="labelText"><spring:message code="cart.confirmation.estimatedShipDate"/></span>
			<span class="textBlack">
				<fmt:formatDate value="${entry.scheduleLines[0].shippingDate}" pattern="MM/dd/yyyy"/>
			</span>
		</p>
		<p class="textLeft">
			<span class="labelText">
				<spring:message code="cart.confirmation.estimatedDeliveryDate"/>
			</span>
			<span class="textBlack">
				<fmt:formatDate value="${entry.scheduleLines[0].deliveryDate}" pattern="MM/dd/yyyy"/>
			</span>
		</p>
	</c:when>
	<c:otherwise>
		<p class="textLeft">
			<span class="labelText">
				<spring:message code="cart.confirmation.materialAvailabilityDate"/>
			</span>
			<span class="textBlack">
				<fmt:formatDate value="${entry.scheduleLines[0].materialAvailabilityDate}" pattern="MM/dd/yyyy"/>
			</span>
		</p>
	</c:otherwise>
</c:choose>