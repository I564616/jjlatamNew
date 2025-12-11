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

<div class="item_container_holder order-totals">
	<div class="title_holder">
		<div class="title">
			<div class="title-top">
			<span></span>
			</div>
		</div>
		<h2><spring:theme code="order.order.totals" /></h2>
	</div>
	<div class="item_container">
		<dl class="order_totals">
			<dt><spring:theme code="basket.page.totals.subtotal"/></dt>
			<dd><format:price priceData="${cartData.subTotal}"/></dd>
			<c:if test="${cartData.totalDiscounts.value > 0}">
				<dt class="savings"><spring:theme code="basket.page.totals.savings"/></dt>
				<dd class="savings"><format:price priceData="${cartData.totalDiscounts}"/></dd>
			</c:if>
			<c:if test="${not empty cartData.deliveryCost}">
				<dt><spring:theme code="basket.page.totals.delivery"/></dt>
				<dd>
					<format:price priceData="${cartData.deliveryCost}" displayFreeForZero="TRUE"/>
				</dd>
			</c:if>

			<c:if test="${cartData.net && cartData.totalTax.value > 0}">
				<dt class="total"><spring:theme code="basket.page.totals.netTax"/></dt>
				<dd class="total"><format:price priceData="${cartData.totalTax}"/></dd>
			</c:if>

			<dt class="total"><spring:theme code="basket.page.totals.total"/></dt>
			<dd class="total">
				<ycommerce:testId code="cart_totalPrice_label">
					<ycommerce:testId code="cart_totalPrice_label">
					<c:choose>
						<c:when test="${cartData.net}">
								<format:price priceData="${cartData.totalPriceWithTax}"/>
						</c:when>
						<c:otherwise>
								<format:price priceData="${cartData.totalPrice}"/>
						</c:otherwise>
					</c:choose>
				</ycommerce:testId>
				</ycommerce:testId>
			</dd>
		</dl>

		<c:if test="${not cartData.net}">
			<ycommerce:testId code="cart_taxes_label">
				<p><spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!" /></p>
			</ycommerce:testId>
		</c:if>
		<c:if test="${cartData.net && cartData.totalTax.value <= 0}">
			<ycommerce:testId code="cart_taxes_label">
				<p><spring:theme code="basket.page.totals.noNetTax" /></p>
			</ycommerce:testId>
		</c:if>
	</div>
</div>
