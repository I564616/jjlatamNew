<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<br/>
<div class="row basecontainer boxshadow justify-content-end">
    <table class="total-summary-table">
        <tr>
            <td class="total-summary-label " colspan="2" style="text-align:center;font-size: 20px !important;"><p><spring:message code="orderDetailPage.consolidated.order.values"/></p><td/>
        </tr>
        <tr>
            <td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
            <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${orderData.subTotal}" /></td>
        </tr>
        <c:if test="${cartData.totalFreightFees.value > 0}">
            <tr class="summary-bline">
                <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.shipping"/></td>
                <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${orderData.totalFreightFees}" /></td>
            </tr>
        </c:if>
        <c:if test="${orderData.totalFees.value > 0}">
            <tr>
                <td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
                <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${orderData.totalFees}" /></td>
            </tr>
        </c:if>
		<c:if test="${orderData.totalTax.value > 0}">
            <tr>
                <td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
                <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${orderData.totalTax}"/></td>
            </tr>
        </c:if>
		<c:if test="${orderData.totalDiscounts.value > 0}">
            <tr>
                <td class="total-summary-label"><spring:message code="order.history.discounts" /></td>
                <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${orderData.totalDiscounts}"/></td>
            </tr>
        </c:if>
		<c:if test="${orderData.totalGrossPrice.value > 0}">
            <tr class="total-price-row">
                <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.total"/></td>
                <td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${orderData.totalGrossPrice}" /></td>
            </tr>
        </c:if>
    </table>
</div>