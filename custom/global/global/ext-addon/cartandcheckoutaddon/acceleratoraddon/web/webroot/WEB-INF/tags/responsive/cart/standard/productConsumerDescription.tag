<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ attribute name="entry" type="com.jnj.facades.data.JnjGTOrderEntryData" required="true" %>
<%@ attribute name="showRemoveLink" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="${entry.product.url}" var="productUrl"/>
<div class="prodImage">
<!-- If a product is not viewable in the catalog, the product name will not be clickable.  -->
<c:choose>
	<c:when test="${!entry.product.isProdViewable || empty productUrl}">
		<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
	</c:when>
	<c:otherwise>
		
		<a class="showProductDeatils" data="${entry.product.code}" href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="cartIcon"/></a>
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
				<a href="javascript:;" class="showProductDeatils" data="${entry.product.code}">${entry.product.name}</a>
			</c:otherwise>
		</c:choose>
	</h4>
	<p class="jnjID"><spring:message code="cart.review.productDesc.jnJID"/><span class="strong">${entry.product.code}</span>
	</p>
	<p><spring:message code="cart.review.productDesc.upc"/>${entry.product.upc}</p>
	<!-- <p class="smallFont"><spring:message code="cart.review.productDesc.contractNumber"/>${entry.product.code}</p> -->
	<c:if test="${showRemoveLink}">
		<ycommerce:testId code="cart_product_removeProduct">
			<p>
				<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}"
					class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/><%-- <spring:theme code="basket.page.removeItem"/> --%>
				</a>
			</p>
		</ycommerce:testId>
	</c:if>
	<p class="msgHighlight">${entry.product.hazmatCode}</p>
</div>