<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<br/>
<div class="row basecontainer boxshadow">
    <table class="total-summary-table">
        <tr>
            <td class="total-summary-label " colspan="2" style="text-align:center;font-size: 20px !important;"><p><spring:message code="cart.consolidated.order.values"/></p><td/>
        </tr>
        <tr>
            <td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
            <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td>
        </tr>
        <c:if test="${cartData.totalFreightFees.value > 0}">
            <tr class="summary-bline">
                <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.shipping"/></td>
                <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.totalFreightFees}" /></td>
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

</div>