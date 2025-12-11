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
<%@ attribute name="cartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
<!-- <div class="total marLeft224">         	 -->
<%-- <c:if test="${cartData.subTotal.value > 0}"> --%>
<!-- 	<p> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.subTotal"/> --%>
<!-- 		</span>  -->
<%-- 		<span class="jnjID"><format:price priceData="${cartData.subTotal}"/></span></p> --%>
<%-- </c:if> --%>
<%-- <c:if test="${cartData.totalFees.value ne '0.0'}"> --%>
<!-- 	<p class="slideDownButton"> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.fees"/> --%>
<!-- 		</span>  -->
<%-- 		<span class="jnjID"><format:price priceData="${cartData.totalFees}"/></span> --%>
<!-- 		<span class="iconExpandCollapse freightIcon" role="directionLeft" style="background-position: 0px -19px;"> </span> -->
<!-- 	</p> -->
<!-- 	<div>					 -->
<%-- 		<c:if test="${cartData.totalDropShipFee.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.dropShipFee" /> --%>
<!-- 				</span>  -->
<!-- 				<span class="jnjID"> -->
<%-- 					<format:price priceData="${cartData.totalDropShipFee}" /> --%>
<!-- 				</span> -->
<!-- 			</p> -->
<%-- 		</c:if>					 --%>
<%-- 		<c:if test="${cartData.totalminimumOrderFee.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.minimumOrderFee" /> --%>
<!-- 				</span> -->
<!-- 				<span class="jnjID"> -->
<%-- 				<format:price priceData="${cartData.totalminimumOrderFee}" /> --%>
<!-- 				</span> -->
<!-- 			</p> -->
<%-- 		</c:if> --%>
<%-- 		<c:if test="${cartData.totalFreightFees.value ne '0.0'}"> --%>
<!-- 			<p class="freightTxt"> -->
<!-- 				<span> -->
<%-- 					<spring:message code="cart.validate.cartTotal.freightCost"/> --%>
<!-- 				</span>  -->
<!-- 				<span class="jnjID"> -->
<%-- 			 		<format:price priceData="${cartData.totalFreightFees}"/> --%>
<!-- 				 </span> -->
<!-- 			</p> -->
<%-- 		</c:if> --%>
<!-- 	</div> -->
<%-- </c:if> --%>
<%-- <c:if test="${cartData.hsaPromotion.value > 0}"> --%>
<!-- 	<p> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.hsaPromotion"/> --%>
<!-- 		</span>  -->
<!-- 		<span class="jnjID"> -->
<%-- 			<format:price priceData="${cartData.hsaPromotion}"/> --%>
<!-- 		</span> -->
<!-- 	</p> -->
<%-- </c:if> --%>
<!-- <p> -->
<!-- 	<span> -->
<%-- 		<spring:message code="cart.validate.cartTotal.tax"/> --%>
<!-- 	</span>  -->
<%-- 	<span class="jnjID"> <format:price priceData="${cartData.totalTax}"/></span> --%>
<!-- </p> -->
<%-- <c:if test="${cartData.totalGrossPrice.value > 0}">				 --%>
<!-- 	<p class="totalSum"> -->
<!-- 		<span> -->
<%-- 			<spring:message code="cart.validate.cartTotal.orderTotal"/> --%>
<!-- 		</span>  -->
<!-- 		<span class="jnjID"> -->
<%-- 			<format:price priceData="${cartData.totalGrossPrice}"/> --%>
<!-- 		</span> -->
<!-- 	</p> -->
<%-- </c:if>		 --%>
<!-- </div> -->
<table class="total-summary-table">
	<tr>
		<td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
		<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td>
	</tr>
	<c:if test="${cartData.totalFreightFees.value > 0}">
	    <tr class="summary-bline">
		    <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.shipping"/></td>
		    <td class="total-summary-cost totalrps no-right-pad">
		        <format:price priceData="${cartData.totalFreightFees}" /></td>
	    </tr>
	</c:if>
	<c:if test="${cartData.totalFees.value > 0}">
	    <tr>
			<td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
			<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.totalFees}" /></td>
		</tr>
	</c:if>
	<c:if test="${cartData.totalTax.value > 0}">
		<tr>
			<td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
			<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.totalTax}"/></td>
		</tr>
	</c:if>
	<c:if test="${cartData.discountTotal.value > 0}">
	    <tr>
			<td class="total-summary-label"><spring:message code="order.history.discounts" /></td>
			<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.discountTotal}"/></td>
		</tr>
	</c:if>
	<tr class="total-price-row">
		<td class="total-summary-label"><spring:message code="cart.review.shoppingCart.total"/></td>
		<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${cartData.totalGrossPrice}" /></td>
		
	</tr>
</table>
