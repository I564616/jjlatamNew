<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:choose>
	<c:when test="${popUpType eq 'SELECT_ACCOUNTS' }">
		<order:orderHistorySelectAccountPopUp/>
	</c:when>
	<c:when test="${popUpType eq 'UPDATE_PO'}">
		<order:orderHistoryUpdatePOPopUP/>
	</c:when>
	<c:when test="${popUpType eq 'UPDATE_SURGEON' }">
		<order:orderHistoryUpdateSurgeonpPopup/>
	</c:when>
	<c:when test="${popUpType eq 'SURGEON_INFORMATION' }">
		<order:orderDetailSurgeonInformationPopUp/>
	</c:when>
</c:choose>
