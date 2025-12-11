<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>


<c:choose>
	<c:when test="${errorFlag == null || errorFlag == 0 }">
		<cart:dropShipAccounts />
	</c:when>
	<c:otherwise>
~Error Occurred~
</c:otherwise>
</c:choose>