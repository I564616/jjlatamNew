<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="consignmentChargeCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentCharge"%>
<div class="mainbody-container">
  <div class="hidden-xs">
    <table id="datatab-desktop" class="table table-bordered table-striped sorting-table fee-price-table">
      <thead>    
        <tr>													
		<th class="no-sort text-uppercase"><spring:message code="cart.review.entry.number" /></th>
		<!-- <th class="no-sort text-uppercase quanity-cell">Product code</th> -->
		<th class="no-sort text-uppercase quanity-cell"><spring:message code="cart.common.customer.Product.Description" /></th>
		<th class="no-sort text-uppercase"><spring:message code="cart.validate.quantity" /></th>
		<!-- <th class="no-sort text-uppercase">UoM</th> -->
		<th class="no-sort text-center text-uppercase"><spring:message code="cart.validate.batchNo" /></th>
		<th class="no-sort text-center text-uppercase"><spring:message code="cart.validate.serialNo" /></th>
		<th class="no-sort text-uppercase"><spring:message code="cart.validate.unitPrice" /></th>
		<th class="no-sort total-thead fee-cell multitotal-thead"><spring:message code="cart.review.entry.totalPrice" /></th>
	</tr>
      </thead>
      <tbody>
        <c:forEach items="${orderData.entries}" var="entry" varStatus="count">
          <tr id="orderentry-${count.count}">
            <td>${count.count}</td>
            <td class="text-left"><consignmentChargeCart:ProductDescriptionOrderComplete entry="${entry}" showRemoveLink="true"
                showStatus="false" /></td>
            <%-- <td>${entry.shippingMethod}<br> <c:if test="${not entry.expandableSchedules}">
                <commonTags:entryLevelDates entry="${entry}" />
              </c:if>
            </td> --%>
            <td>
              <div>${entry.quantity}</div>
              <p class="thirdline">
                <spring:message code="product.detail.addToCart.unit" />
                ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
              </p>
            </td>
            <td>${entry.batchNumber}</td>
            <td>${entry.serialNumber}</td>
            <td><format:price priceData="${entry.basePrice}" /></td>
            <td class="valign-middle totalrps fee-cell"><ycommerce:testId code="cart_totalProductPrice_label">
                <format:price priceData="${entry.totalPrice}" />
              </ycommerce:testId></td>
          </tr>
          <!-- Changes for Bonus Item -->
          <c:if test="${freeGoodsMap ne null}">
            <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
            <c:if test="${not empty valueObject.itemCategory}">
              <tr class="noborder">
                <td class="text-right"><consignmentChargeCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false"
                    showStatus="false" /></td>
                <td class="valign-middle"></td>
                <td>
                  <div class="text-center">${valueObject.materialQuantity}</div>
                  <p class="thirdline">
                    <spring:message code="product.detail.addToCart.unit" />
                    ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                  </p>
                </td>
                <td class="valign-middle"><spring:message code="cart.freeitem.message" /></td>
                <td class="valign-middle"></td>
              </tr>
            </c:if>
          </c:if>
        </c:forEach>
      </tbody>
    </table>
  </div>
  <!-- Table collapse for mobile device-->
  <div class="row visible-xs hidden-lg hidden-sm hidden-md">
    <div class="col-xs-12">
      <table id="datatab-mobile" class="table table-bordered table-striped sorting-table">
        <thead>
          <tr>
            <th class="no-sort text-left text-uppercase"><spring:message code="cart.validate.product" /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${orderData.entries}" var="entry" varStatus="scount">
            <tr>
              <td class="text-left"><consignmentChargeCart:ProductDescriptionOrderComplete rowcount="${scount.count}" entry="${entry}"
                  showRemoveLink="true" showStatus="false" />
                <div id="mobi-collapse${scount.count}" class="panel-collapse collapse img-accordian">
                  <p class="text-uppercase">
                    <spring:message code="cart.validate.shippingMethod" />
                  </p>
                  <P>${entry.shippingMethod}<br>
                    <c:if test="${not entry.expandableSchedules}">
                      <commonTags:entryLevelDates entry="${entry}" />
                    </c:if>
                  </P>
                  <p class="text-uppercase">
                    <spring:message code="cart.validate.quantityQty" />
                  </p>
                  <p>${entry.quantity}</p>
                  <p class="thirdline">${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p>
                  <p class="text-uppercase">
                    <spring:message code="cart.validate.unitPrice" />
                  </p>
                  <p>
                    <format:price priceData="${entry.basePrice}" />
                  </p>
                  <p class="text-uppercase">
                    <spring:message code="cart.validate.total" />
                  </p>
                  <p>
                    <ycommerce:testId code="cart_totalProductPrice_label">
                      <format:price priceData="${entry.totalPrice}" />
                    </ycommerce:testId>
                  </p>
                </div></td>
            </tr>
            <!-- Changes for Bonus Item -->
            <c:if test="${freeGoodsMap ne null}">
              <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
              <c:if test="${not empty valueObject.itemCategory}">
                <tr class="noborder">
                  <td class="text-right"><consignmentChargeCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false"
                      showStatus="false" /></td>
                  <td class="valign-middle"></td>
                  <td>
                    <div class="text-center">${valueObject.materialQuantity}</div>
                    <p class="thirdline">
                      <spring:message code="product.detail.addToCart.unit" />
                      ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                    </p>
                  </td>
                  <td class="valign-middle"><spring:message code="cart.freeitem.message" /></td>
                  <td class="valign-middle"></td>
                </tr>
              </c:if>
            </c:if>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
  <!--Accordian Ends here -->
  <!-- Start - Total Price Summary -->
  <div class="row basecontainer fee-toggle-container">
    <table class="total-summary-table">
      <tr>
        <td class="total-summary-label"><spring:message code="cart.common.subTotal" /></td>
        <td class="total-summary-cost totalrps"><format:price priceData="${orderData.subTotal}" /></td>
      </tr>
      <tr>
        <td class="total-summary-label"><spring:message code="cart.validate.cartTotal.fees" /></td>
        <td class="total-summary-cost totalrps"><format:price priceData="${orderData.totalFees}" /></td>
        <td class="toggle-fee"><a data-toggle="collapse" class="toggle-fee-link toggle-link panel-collapsed" href="#fee-mobile-collpase"><span
            class="glyphicon glyphicon-chevron-up"></span></a></td>
      </tr>
      <tr class="fee-panel">
        <td colspan='2'>
          <table id="fee-mobile-collpase" class="fee-collpase-table total-summary-table panel-collapse collapse">
            <tr>
              <td class="total-summary-label" style="width: 25px; font-size: 10px !important"><spring:message
                  code="cart.validate.cartTotal.dropShipFee" /></td>
              <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price priceData="${orderData.totalDropShipFee}" /></td>
            </tr>
            <tr>
              <td class="total-summary-label" style="font-size: 10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
              <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price
                  priceData="${orderData.totalminimumOrderFee}" /></td>
            </tr>
            <tr>
              <td class="total-summary-label" style="font-size: 10px !important"><spring:message code="cart.validate.cartTotal.freightCost" /></td>
              <td class="total-summary-cost totalrps" style="font-size: 12px !important"><format:price priceData="${orderData.totalFreightFees}" /></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr class="summary-bline ">
        <td class="total-summary-label"><spring:message code="cart.validate.cartTotal.tax" /></td>
        <td class="total-summary-cost totalrps"><format:price priceData="${orderData.totalTax}" /></td>
      </tr>
      <tr class="total-price-row">
        <td class="total-summary-label"><spring:message code="cart.review.shoppingCart.total" /></td>
        <td class="total-summary-cost totalsum"><format:price priceData="${orderData.totalPrice}" /></td>
      </tr>
    </table>
  </div>
  <!-- End - Total Price Summary -->
</div>