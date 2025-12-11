<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false" %>
<%@ attribute name="showExpectedDeliveryDate" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="${entry.product.url}" var="productUrl"/>
<div class="prodImage">
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<!-- If a product is not viewable in the catalog, the product name will not be clickable.  -->
<c:choose>
	<c:when test="${!entry.product.isProdViewable || empty productUrl}">
		<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
	</c:when>
	<c:otherwise>
		<a href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="cartIcon"/></a>
	</c:otherwise>
</c:choose>

</div>
<div class="orderProdDesc">
	<h4 title="${entry.product.name}">
		<c:choose>
			<c:when test="${!entry.product.isProdViewable || empty productUrl}">
					${entry.product.name}
			</c:when>
			<c:otherwise>
				<a href="${productUrl}">
					${entry.product.name}
				</a>
			</c:otherwise>
		</c:choose>
	</h4>
	<p class="jnjID"><spring:message code="cart.review.productDesc.jnJID"/><span class="strong">${entry.product.code}</span>
	</p>
	<p><spring:message code="cart.review.productDesc.upc"/>${entry.product.upc}</p>
	<!-- <p class="smallFont"><spring:message code="cart.review.productDesc.contractNumber"/>${entry.product.code}</p> -->
	 <c:if test="${not empty entry.status}"><p><spring:message code="cart.review.productDesc.status"/>${entry.status}</p></c:if>
	<c:if test="${showExpectedDeliveryDate && not empty entry.expectedDeliveryDate}">
	<p><spring:message code="cart.confirmation.estimatedDeliveryDate"/><fmt:formatDate pattern="${dateformat}" value="${entry.expectedDeliveryDate}" /></p>
	</c:if>
	<p class="msgHighlight">${entry.product.hazmatCode}</p>
</div>