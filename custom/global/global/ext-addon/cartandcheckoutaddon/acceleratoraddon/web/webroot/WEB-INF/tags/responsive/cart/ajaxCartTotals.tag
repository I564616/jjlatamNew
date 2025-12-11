<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<script id="cartTotalsTemplate" type="text/x-jquery-tmpl">

	<dl class="order_totals">
		<dt><spring:theme code="basket.page.totals.subtotal"/></dt>
		<dd>{{= subTotal.formattedValue}}</dd>
		<dt class="savings"><spring:theme code="basket.page.totals.savings"/></dt>
		<dd class="savings">{{= totalDiscounts.formattedValue}}</dd>
		{{if deliveryCost}}
			<dt><spring:theme code="basket.page.totals.delivery"/></dt>
			<dd>
				{{if deliveryCost.value > 0}}
					{{= deliveryCost.formattedValue}}
				{{else}}
					<spring:theme code="basket.page.free"/>
				{{/if}}
			</dd>
		{{/if}}
		{{if net && totalTax.value > 0 }}
			<dt><spring:theme code="basket.page.totals.netTax"/></dt>
			<dd>{{= totalTax.formattedValue}}</dd>
		{{/if}}
		<dt class="total"><spring:theme code="basket.page.totals.total"/></dt>
		{{if net}}
			<dd class="total">{{= totalPriceWithTax.formattedValue}}</dd>
		{{else}}
			<dd class="total">{{= totalPrice.formattedValue}}</dd>
		{{/if}}
	</dl>
	{{if !net}}
		<p><spring:theme code="basket.page.totals.grossTax" arguments="{{= totalTax.formattedValue}}" argumentSeparator="!!!!" /></p>
	{{/if}}
	{{if net && totalTax.value <= 0}}
		<p><spring:theme code="basket.page.totals.noNetTax" /></p>
	{{/if}}
	

</script>

<div class="item_container_holder order-totals">
	<div class="title_holder">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2><spring:theme code="order.order.totals" /></h2>
	</div>
	<div class="item_container" id="cart_totals_div">
		<dl class="order_totals">
			<dt><spring:theme code="basket.page.totals.subtotal"/></dt>
			<dd><format:price priceData="${cartData.subTotal}"/></dd>
			<dt class="savings"><spring:theme code="basket.page.totals.savings"/></dt>
			<dd class="savings"><format:price priceData="${cartData.totalDiscounts}"/></dd>
			<c:if test="${not empty cartData.deliveryCost}">
				<dt><spring:theme code="basket.page.totals.delivery"/></dt>
				<dd>
					<c:choose>
						<c:when test="${cartData.deliveryCost.value > 0}">
							<format:price priceData="${cartData.deliveryCost}"/>
						</c:when>
						<c:otherwise>
							<spring:theme code="basket.page.free"/>
						</c:otherwise>
					</c:choose>
				</dd>
			</c:if>
			<c:if test="${cartData.net && cartData.totalTax.value > 0}">
				<dt><spring:theme code="basket.page.totals.netTax"/></dt>
				<dd><format:price priceData="${cartData.totalTax}"/></dd>
			</c:if>
			<dt class="total"><spring:theme code="basket.page.totals.total"/></dt>
			<c:choose>
				<c:when test="${cartData.net}">
					<dd class="total"><ycommerce:testId code="checkout_totalPrice_label"><format:price priceData="${cartData.totalPriceWithTax}"/></ycommerce:testId></dd>
				</c:when>
				<c:otherwise>
					<dd class="total"><ycommerce:testId code="checkout_totalPrice_label"><format:price priceData="${cartData.totalPrice}"/></ycommerce:testId></dd>
				</c:otherwise>
			</c:choose>
		</dl>
		<c:if test="${not cartData.net}">
			
				<ycommerce:testId code="checkout_tax_label">
					<spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!" />
				</ycommerce:testId>
			
		</c:if>
		<c:if test="${cartData.net && cartData.totalTax.value <= 0}">
			<p>
				<ycommerce:testId code="checkout_tax_label">
					<spring:theme code="basket.page.totals.noNetTax" />
				</ycommerce:testId>
			</p>
		</c:if>
	</div>
</div>
