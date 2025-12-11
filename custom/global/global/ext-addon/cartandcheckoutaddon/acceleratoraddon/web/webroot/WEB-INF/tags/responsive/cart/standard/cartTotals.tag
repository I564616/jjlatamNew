<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- <div class="total marLeft224">         	 -->
<%-- 			<p><span><spring:message code="cart.validate.cartTotal.subTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.subTotal}" /></span></p>		 --%>
			
<%-- 			<c:if test="${cartData.totalFees.value ne '0.0'}"> --%>
<%-- 				<p class="slideDownButton"><span><spring:message code="cart.validate.cartTotal.fees"/></span> <span class="jnjID"><format:price priceData="${cartData.totalFees}"/></span><span class="iconExpandCollapse freightIcon" role="directionLeft" style="background-position: 0px -19px;"> </span></p> --%>
				
<!-- 				<div>					 -->
<%-- 					<c:if test="${cartData.totalDropShipFee.value ne '0.0'}"> --%>
<%-- 						<p class="freightTxt">	<span><spring:message code="cart.validate.cartTotal.dropShipFee" /></span> <span class="jnjID"><format:price priceData="${cartData.totalDropShipFee}" /></span></p> --%>
<%-- 					</c:if>					 --%>
<%-- 					<c:if test="${cartData.totalminimumOrderFee.value ne '0.0'}"> --%>
<%-- 						<p class="freightTxt">	<span><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></span> <span class="jnjID"><format:price priceData="${cartData.totalminimumOrderFee}" /></span> --%>
<%-- 					</c:if>	 --%>
<!-- 				</div> -->
			
<%-- 			</c:if> --%>
			
<%-- 			<c:if test="${cartData.hsaPromotion.value ne '0.0'}"> --%>
<%-- 				<p><span><spring:message code="cart.validate.cartTotal.hsaPromotion"/></span> <span class="jnjID"><format:price priceData="${cartData.hsaPromotion}"/></span></p> --%>
<%-- 			</c:if> --%>
<%-- 			<c:if test="${cartData.totalFreightFees.value ne '0.0'}"> --%>
<%-- 				<p><span><spring:message code="cart.validate.cartTotal.freightCost"/></span> <span class="jnjID"> <format:price priceData="${cartData.totalFreightFees}"/></span></p> --%>
<%-- 			</c:if> --%>
<%-- 					<p><span><spring:message code="cart.validate.cartTotal.tax"/></span> <span class="jnjID"> <format:price priceData="${cartData.totalTax}"/></span></p> --%>
<%-- 			<p class="totalSum"><span><spring:message code="cart.validate.cartTotal.orderTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.totalPrice}"/></span></p> --%>
<!-- </div> -->
<div class="row basecontainer fee-toggle-container">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${cartData.subTotal}" /></td>
				</tr>
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.fees" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${cartData.totalFees}" /></td>
					<td class="toggle-fee"><a data-toggle="collapse"
						class="toggle-fee-link toggle-link panel-collapsed"
						href="#fee-mobile-collpase-total" ><span
							class="glyphicon glyphicon-chevron-up"></span></a></td>
				</tr>
				<tr class="fee-panel">
					<td colspan='2'>
						<table id="fee-mobile-collpase-total"
							class="fee-collpase-table total-summary-table panel-collapse collapse">
							<tr>
								<td class="total-summary-label" style="width: 25px;font-size:10px !important"><spring:message code="cart.validate.cartTotal.dropShipFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalDropShipFee}" /></td>
							</tr>
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalminimumOrderFee}" /></td>
							</tr>
							
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.freightCost"/></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalFreightFees}"/></td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="summary-bline ">
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.tax" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${cartData.totalTax}" /></td>
				</tr>
				<tr class="total-price-row">
					<td class="total-summary-label"><spring:message
							code="cart.review.shoppingCart.total" /></td>
					<td class="total-summary-cost totalsum"><format:price
							priceData="${cartData.totalPrice}" /></td>

				</tr>
			</table>

		</div>