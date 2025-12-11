<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


	<ycommerce:testId code="miniCart_items_label">
		<spring:theme text="items" code="cart.items" arguments="${totalItems}"/>
	</ycommerce:testId>
	-
<span>
	<ycommerce:testId code="miniCart_total_label">
		<c:if test="${totalDisplay == 'TOTAL'}">
			<format:price priceData="${totalPrice}"/>
		</c:if>
		<c:if test="${totalDisplay == 'SUBTOTAL'}">
			<format:price priceData="${subTotal}"/>
		</c:if>
		<c:if test="${totalDisplay == 'TOTAL_WITHOUT_DELIVERY'}">
			<format:price priceData="${totalNoDelivery}"/>
		</c:if>
	</ycommerce:testId>
</span>
