<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="list-group listclass scroll-y-div" id="addressDataId1">
<input type="hidden" id="billingAddressCounts" name="billingAddressCounts" value="${billingAddresses.size()}">
<c:forEach var="billingAddresses" items="${billingAddresses}" varStatus="count">
<div class="odd-row">
	<div class="list-group-item-text descTxt" id="test">
	    <input type="hidden" name="billingAddresses1" value="${billingAddresses.id}" id = "billingAddresses1" class="shipp">
		<div class="address-txt">${billingAddresses.companyName}</div>
		<div class="address-txt">${billingAddresses.line1}
		<c:if test="${not empty billingAddresses.line1}">
		,
		</c:if>
                     ${billingAddresses.line2}</div>
		<div class="address-txt">${billingAddresses.town}
		<c:if test="${not empty billingAddresses.town}">
		,
		</c:if>
		${billingAddresses.postalCode}</div>
		<div class="address-txt">
		<c:if test="${not empty billingAddress.taxid}">
		${billingAddress.taxid}
		</c:if>
		</div>
	</div>
</div>
</c:forEach>
</div>

<div id="bill-no-address-result-holder">
	<div id="bill-no-address-result"><spring:message code="changeAddress.search.noDataFound" />&nbsp;${BillingSearchTerm} </div>
 </div>