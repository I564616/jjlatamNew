<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<c:if test="${not empty cartData.potentialOrderPromotions}">
	<div class="item_container_holder promo">
		<div class="title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="basket.potential.promotions" /></h2>
		</div>
		<div class="item_container">
			<ycommerce:testId code="potentialPromotions_promotions_labels">
				<ul>
					<c:forEach items="${cartData.potentialOrderPromotions}" var="promotion">
						<li class="cart-promotions-potential">${promotion.description}</li>
					</c:forEach>
				</ul>
			</ycommerce:testId>
		</div>
	</div>
</c:if>
