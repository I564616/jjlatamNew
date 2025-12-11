<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false" %>
<%@ attribute name="showStatus" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="${entry.product.url}" var="productUrl"/>
<c:url value="${entry.product.code}" var="productCode"/>
<div class="prodImage">
<!-- If a product is not viewable in the catalog, the product name will not be clickable.  -->

<c:choose>
	<c:when test="${!entry.product.isProdViewable || empty productUrl}">
		<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
	</c:when>
	<c:otherwise>
		<a href="javascript:;" class="showProductDeatils" data="${productCode}"><product:productPrimaryImage product="${entry.product}" format="cartIcon"/></a>
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
				<a href="javascript:;" class="showProductDeatils" data="${productCode}">${entry.product.name}</a>
			</c:otherwise>
		</c:choose>
	</h4>
	<p class="jnjID"><spring:message code="cart.review.productDesc.jnJID"/><span class="strong">${entry.product.code}</span>
	</p>
	 <p class="jnjID"><spring:message code="product.uom"/><span class="strong"> ${entry.product.deliveryUnitCode}</span>
	</p>
	<c:if test="${not empty entry.product.gtin}">
	<p><spring:message code="cart.review.productDesc.gtnNumber"/>${entry.product.gtin}</p>
	</c:if>
	<c:if test="${not empty entry.contractNumber}">
	<p><spring:message code="cart.review.productDesc.contractNumber"/>${entry.contractNumber}</p>
	</c:if>
	<c:if test="${showStatus}">
	<c:if test="${not empty entry.status}">
		<p><spring:message code="cart.review.productDesc.status"/>${entry.status}</p>
	</c:if>
	</c:if>
	<c:if test="${showRemoveLink}">
		<ycommerce:testId code="cart_product_removeProduct">
			<p>
				<a href="javascript:void(0);" id="RemoveProduct_${entry.entryNumber}"
					class="smallFont laSubmitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/><%-- <spring:theme code="basket.page.removeItem"/> --%>
				</a>
			</p>
		</ycommerce:testId>
	</c:if>
	<p class="msgHighlight">${entry.product.hazmatCode}</p>
</div>