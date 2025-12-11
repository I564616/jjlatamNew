<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- consignmentCharge/cartTotals.tag -->
<div class="row basecontainer fee-toggle-container">
  <table class="total-summary-table">
    <tr>
      <td class="total-summary-label"><spring:message code="cart.common.subTotal" /></td>
      <td class="total-summary-cost totalrps"><format:price priceData="${cartData.subTotal}" /></td>
    </tr>
    <tr>
      <td class="total-summary-label"><spring:message code="cart.validate.cartTotal.fees" /></td>
      <td class="total-summary-cost totalrps"><format:price priceData="${cartData.totalFees}" /></td>
      <td class="toggle-fee"><a data-toggle="collapse" class="toggle-fee-link toggle-link panel-collapsed" href="#fee-mobile-collpase-total">
          <span class="glyphicon glyphicon-chevron-up"></span>
      </a></td>
    </tr>
    <tr class="fee-panel">
      <td colspan='2'>
        <table id="fee-mobile-collpase-total" class="fee-collpase-table total-summary-table panel-collapse collapse">
          <tr>
            <td class="total-summary-label" style="width: 25px; font-size: 10px !important"><spring:message
                code="cart.validate.cartTotal.dropShipFee" /></td>
            <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price priceData="${cartData.totalDropShipFee}" /></td>
          </tr>
          <tr>
            <td class="total-summary-label" style="font-size: 10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
            <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price priceData="${cartData.totalminimumOrderFee}" /></td>
          </tr>
          <tr>
            <td class="total-summary-label" style="font-size: 10px !important"><spring:message code="cart.validate.cartTotal.freightCost" /></td>
            <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price priceData="${cartData.totalFreightFees}" /></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr class="summary-bline ">
      <td class="total-summary-label"><spring:message code="cart.validate.cartTotal.tax" /></td>
      <td class="total-summary-cost totalrps"><format:price priceData="${cartData.totalTax}" /></td>
    </tr>
    <tr class="total-price-row">
      <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.total" /></td>
      <td class="total-summary-cost totalsum"><format:price priceData="${cartData.totalPrice}" /></td>
    </tr>
  </table>
</div>
