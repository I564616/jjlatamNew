<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:choose>
	<c:when test="${popUpType eq 'DISPUTE_ORDER' }">
		<order:disputeOrderPopUp/>
	</c:when>
	<c:when test="${popUpType eq 'DISPUTE_ITEM'}">
		<order:disputeLineItemPopUp/>
	</c:when>
</c:choose>
