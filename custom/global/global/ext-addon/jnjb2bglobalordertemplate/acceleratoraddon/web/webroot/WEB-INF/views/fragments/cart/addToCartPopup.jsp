<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="json" uri="jakarta.tags.core" %>

{
	"cartModData": {
		"products":	[ <c:forEach items="${cartModData.cartModifications}" var="cartEntry" varStatus="status">
						{
							"code":			"${cartEntry.entry.product.code}",
							"name":			"${cartEntry.entry.product.name}",
							"quantity":		"${cartEntry.entry.quantity}",
							"price":		"${cartEntry.entry.basePrice.value}",
							"categories":	[<c:forEach items="${cartEntry.entry.product.categories}" var="category" varStatus="categoryStatus">
												"${category.name}"<c:if test="${not categoryStatus.last}">,</c:if>
											</c:forEach>]
						}<c:if test="${not status.last}">,</c:if>
					</c:forEach>]
	},

	"cartPopupHtml": "<spring:escapeBody javaScriptEscape="true">
						<spring:theme code="text.addToCart" var="addToCartText"/>
						<c:url value="/cart" var="cartUrl"/>
						<c:url value="/cart/checkout" var="checkoutUrl"/>
					    <c:url value="${product.baseMaterialNumber}" var="productCode"/>
					    <c:url value="${product.url}" var="productURL" />
					    <input type="hidden" value="${lines}" id="lineInput" />
						<div class="lightboxtemplate" id="miniCart">
						<div class="title">
							<h2> <spring:theme code="basket.added.to.basket" /></h2>
							<a href="#" id="add_to_cart_close"><span id="cboxClose" class="hover" ></span></a>
						</div>

						<div class="sectionBlock body">
							<div class="prod_image left">
								<product:productPrimaryImage product="${product}" format="cartIcon"/>
							</div>
							<div class="left marLeft10 marTop10">
								<p>Product Name: ${product.name}</p>
								<p>Product Code: <a href="${productURL}">${product.baseMaterialNumber}</a></p>
								<p>Product Quantity: 
								<c:choose>
								<c:when test="${not empty product.lastAddedProductQuantity}">
								${product.lastAddedProductQuantity}
								</c:when>
								<c:otherwise>
									${cartModData.cartModifications[0].quantityAdded}
								</c:otherwise>
								</c:choose>
								</p>
								<br />
								<p><b>Total items in Cart: </b> ${lines}</p>
							</div>
						</div>
						<div class="popupButtonWrapper txtRight">
						<span class="right"><a href="${cartUrl}" class="primarybtn"><spring:theme code="checkout.checkout" /></a>
						</span>
						</div>
						</div>
					</spring:escapeBody>",
					
	"invalidProductCodes": "<c:out value="${product.invalidProductCodes}"></c:out>",
	"code": "<c:out value="${product.code}"></c:out>"			
}
