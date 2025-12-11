<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ attribute name="containerCSS" required="false"	type="java.lang.String"%>
<%@ attribute name="jnjCartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
<%@ attribute name="varRowCount" required="true" type="java.lang.Integer"%>
<!-- <div class="total marLeft224">         	 -->

<div class="row basecontainer fee-toggle-container">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label">
					<spring:message	code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps">
					<format:price priceData="${jnjCartData.subTotal}"/></td>
				</tr>
				<tr>
					<td class="total-summary-label ">
					<spring:message 	code="cart.validate.cartTotal.fees" /></td>
					<td class="total-summary-cost totalrps">
					<format:price priceData="${jnjCartData.totalFees}"/>
					</td>
					<td class="toggle-fee">
					<a data-toggle="collapse"	class="toggle-fee-link toggle-link panel-collapsed"	href="#fee-mobile-collpase${varRowCount}">	<span	class="glyphicon glyphicon-chevron-up"></span></a>
						</td>
				</tr>
				<tr class="fee-panel">
					<td colspan='2'>
						<table id="fee-mobile-collpase${varRowCount}"
							class="fee-collpase-table total-summary-table panel-collapse collapse">
							<tr>
								<td class="total-summary-label" style="width: 25px;font-size:10px !important"><spring:message code="cart.validate.cartTotal.dropShipFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalDropShipFee}" /></td>
							</tr>
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalminimumOrderFee}" /></td>
							</tr>
							
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.freightCost"/></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalFreightFees}"/></td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="summary-bline ">
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.tax" /></td>
					<td class="total-summary-cost totalrps">
					 <format:price priceData="${jnjCartData.totalTax}"/></td>
				</tr>
				<tr class="total-price-row">
					<td class="total-summary-label">
					<spring:message	code="cart.review.shoppingCart.total" /></td>
					<td class="total-summary-cost totalsum"><format:price priceData="${jnjCartData.totalGrossPrice}"/></td>
	                                                                 
				</tr>
			</table>

		</div>
