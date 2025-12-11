<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="list-group listclass scroll-y-div" id="addressDataId">
<input type="hidden" id="shippingAddressCounts" name="shippingAddressCounts" value="${shippingAddressess.size()}">
<c:forEach var="shippingAddress" items="${shippingAddressess}" varStatus="count">
<div class="odd-row">
	<div class="list-group-item-text descTxt" id="test">
	    <input type="hidden" name="shippingAddress1" value="${shippingAddress.id}" id = "shippingAddress1" class="shipp">
		<div class="address-txt">${shippingAddress.companyName}</div>
		<div class="address-txt">
		${shippingAddress.line1}
		<c:if test="${not empty shippingAddress.line1}">
		,
		</c:if>
                     ${shippingAddress.line2}</div>
		<div class="address-txt">${shippingAddress.town}
		<c:if test="${not empty shippingAddress.town}">
		,
		</c:if>
		${shippingAddress.postalCode}</div>
		<div class="address-txt">
		<c:if test="${not empty billingAddress.taxid}">
		${billingAddress.taxid}
		</c:if>
		</div>
	</div>
</div>
</c:forEach>
</div>

<div id="no-address-result-holder">
	<div id="no-address-result"><spring:message code="changeAddress.search.noDataFound" />&nbsp;${shippingSearchTerm}</div>
 </div>