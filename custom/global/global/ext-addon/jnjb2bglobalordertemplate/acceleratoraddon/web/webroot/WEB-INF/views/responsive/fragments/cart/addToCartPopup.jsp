<%@ page trimDirectiveWhitespaces="true" contentType="application/json" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="json" uri="jakarta.tags.core" %>
{
	<%-- "showMinQtyMsg":'${showMinQtyMsg}', --%>
	"cartModData": [
		<c:forEach items="${addStatus}" var="addStatus" varStatus="status">
			{
				"${addStatus.key}": "${addStatus.value}"
			}<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	],
	"cartPopupHtml": "<spring:escapeBody javaScriptEscape="true">
		<spring:theme code="text.addToCart" var="addToCartText"/>
		<c:url value="/cart" var="cartUrl"/>
		<c:url value="/cart/checkout" var="checkoutUrl" />
		<c:url value="${product.url}" var="productURL" />
		<input type="hidden" value="${lines}" id="lineInput" />
		<div class="lightboxtemplate" id="miniCart">
		<div class="title">
			<%-- <theme:image code="img.addToCartIcon" alt="${addToCartText}" title="${addToCartText}"/> --%>
			<h2> <spring:theme code="basket.added.to.basket" /></h2>
			<a href="#" id="add_to_cart_close"><span id="cboxClose" class="hover" ></span></a>
		</div>

		<div class="sectionBlock body">
			<div class="prod_image left">
				<product:productPrimaryImage product="${product}" format="cartIcon"/>
			</div>
			<div class="left marLeft10 marTop10">
				<p>Product Name: ${product.name}</p>
				<p>Product Code: <a href="${productURL}">${product.code}</a></p>
				<p>Product Quantity: ${cartData.cartModifications[0].quantityAdded}</p>
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
"showMinQtyMsg":"<c:out value="${showMinQtyMsg}"/>" 
}
