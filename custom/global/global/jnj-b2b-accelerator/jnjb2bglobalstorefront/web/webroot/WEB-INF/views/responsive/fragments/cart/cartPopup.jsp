<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<spring:theme code="text.addToCart" var="addToCartText"/>
<spring:theme code="text.popupCartTitle" var="popupCartTitleText"/>
<c:url value="/cart" var="cartUrl"/>
<c:url value="/cart/checkout" var="checkoutUrl"/>

<div class="title">
	<theme:image code="img.addToCartIcon" alt="${popupCartTitleText}" title="${popupCartTitleText}"/>
	<h3><spring:theme code="popup.cart.title"/></h3>
	<a href="#" class="close" id="ajax_cart_close" title="<spring:theme code="popup.close"/>" alt="<spring:theme code="popup.close"/>"></a>
</div>

<c:if test="${numberShowing > 0 }">
	<p class="legend">
		<spring:theme code="popup.cart.showing" arguments="${numberShowing},${numberItemsInCart}"/>
		<c:if test="${numberItemsInCart > numberShowing}">
			<a href="${cartUrl}">Show All</a>
		</c:if>
	</p>
</c:if>
<c:if test="${empty numberItemsInCart or numberItemsInCart eq 0}">
	<div class="cart_modal_popup empty-popup-cart">
		<spring:theme code="popup.cart.empty"/>
	</div>
</c:if>
<c:if test="${numberShowing > 0 }">
	<ul>
		<c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
			<c:url value="${entry.product.url}" var="entryProductUrl"/>
			<li class="cart_modal_popup">
				<div class="prod_image">
					<a href="${entryProductUrl}">
						<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
					</a>
				</div>
				<div class="prod_info">
					<a href="${entryProductUrl}"><p class="prod_name">${entry.product.name}</p></a>
					<p class="prod_price"><format:price priceData="${entry.basePrice}"/></p>
					<p class="prod_quantity"><span class="product-variant-label"><spring:theme code="popup.cart.quantity"/></span>${entry.quantity}</p>
					<p class="prod_options">
						<c:forEach items="${entry.product.baseOptions}" var="baseOptions">
							<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
								<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
									<span class="prod_color">
										<span class="product-variant-label"><spring:theme code="product.variants.colour"/></span>
										<img src="${baseOptionQualifier.image.url}" alt="${baseOptionQualifier.value}" title="${baseOptionQualifier.value}"/>
									</span>
								</c:if>
								<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
									<span class="prod_size"><span class="product-variant-label"><spring:theme code="product.variants.size"/></span>${baseOptionQualifier.value}</span>
								</c:if>
							</c:forEach>
						</c:forEach>
					</p>
				</div>
			</li>
		</c:forEach>
	</ul>
</c:if>

<div  class="prod_cart-total">
	<spring:theme code="popup.cart.total"/>&nbsp;<format:price priceData="${cartData.totalPrice}"/>
</div>

<c:if test="${not empty lightboxBannerComponent && lightboxBannerComponent.visible}">
	<div class="content_slot">
		<cms:component component="${lightboxBannerComponent}"/>
	</div>
</c:if>
<div class="links">
	<a href="${cartUrl}" class="positive large">
		<theme:image code="img.addToCartIcon" alt="${addToCartText}" title="${addToCartText}"/>
		<spring:theme code="checkout.checkout" />
	</a>
</div>
