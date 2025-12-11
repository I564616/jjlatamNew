<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<div class="hidden-xs" id="AddItemsCartpage">
  <!--table starts here  -->
  <div class="boxshadow">
    <table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines fee-price-table">
      <thead>
        <tr>
          <th class="no-sort snoClass"><spring:message code="cart.review.entry.number" /></th>
          <th class="no-sort text-center"><spring:message code="cart.payment.product" /></th>
          <th class="no-sort text-center"><spring:message code="cart.payment.shipping" /></th>
          <th class="no-sort"><spring:message code="cart.payment.quantityQty" /></th>
          
          <th class="no-sort unitprice-thead"><spring:message code="cart.validate.unitPrice" /></th>
          <th class="no-sort text-uppercase"><spring:message code="cart.review.entry.total" /></th>
          <th class="no-sort text-uppercase"><spring:message code="cart.review.productDesc.status"/></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
        <c:if test="${entry.product.code ne entry.originalOrderItem}">
		  <tr>
			<td colspan="7" style="text-align: left !important; color: green; padding: 0px !important">
			  <div class="panel-group" style="margin-bottom:0px !important">
			  <div class="panel panel-success">
				<div class="panel-heading">
				  <div class="">
					<span class="glyphicon glyphicon-ok"></span> ${entry.originalOrderItem} <spring:message code="scheduleline.grouping.message"/> ${entry.product.code}
				  </div>
				</div>
			  </div>
			</div>
		   </td>
		   <td style="display:none"></td><td style="display:none"></td><td style="display:none"></td><td style="display:none"></td><td style="display:none"></td><td style="display:none"></td>
		  </tr>
		</c:if>
          <c:forEach items="${entry.scheduleLines}" var="deliverySch" varStatus="deliverySchCount">
          <tr id="orderentry-${count.count}">
              <td class="snoData">${count.count}.${deliverySchCount.count}</td>
           <td class="text-left"><standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
           <td class="text-center">
              <div>${entry.shippingMethod}</div>
              <div>
                  <commonTags:entryLevelDates entry="${entry}" deliverySch="${deliverySch}" />
              </div>
            </td>
              <td class="valign-middle">${deliverySch.quantity}<br />
              <p class="thirdline">
                <spring:message code="product.detail.addToCart.unit" />
                ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
              </p>
            </td>
            <td class="valign-middle" id="basePrice_${entry.entryNumber}"><format:price priceData="${entry.basePrice}" /></td>
            <td class="valign-middle totalrps fee-cell" id="totalPrice_${entry.entryNumber}"><ycommerce:testId code="cart_totalProductPrice_label">
                 <span class="txt-nowrap"><format:totalFromBasePrice priceData="${entry.basePrice}" quantity="${deliverySch.quantity}" /></span>
              </ycommerce:testId></td>
                <td class="valign-middle text-lowercase">
                <c:choose>
                  <c:when test="${deliverySch.lineStatus  eq 'CONFIRMED'}">
                  <spring:message code="product.variants.available" />
                  </c:when>
                  <c:otherwise>
                  ${deliverySch.lineStatus}
                  </c:otherwise>
                </c:choose>
               </td>
          </tr>
          <!-- Changes for Bonus Item -->
          <c:if test="${freeGoodsMap ne null}">
            <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
            <c:if test="${not empty valueObject.itemCategory}">
              <tr class="noborder">
                <td class="text-right"><standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
                <td class="valign-middle"></td>
                <td>
                  <div class="text-center">${valueObject.materialQuantity}</div>
                  <p class="thirdline">
                    <spring:message code="product.detail.addToCart.unit" />
                    ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                  </p>
                </td>
                <td><span class="txt-nowrap"><spring:message code="cart.freeitem.message" /></span></td>
                <td class="valign-middle"></td>
              </tr>
            </c:if>
          </c:if>
          </c:forEach>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<div class="visible-xs hidden-lg hidden-sm hidden-md">
  <table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines">
    <thead>
      <tr>
        <th class="no-sort text-left"><spring:message code="cart.payment.product" /></th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
        <tr>
          <td class="text-left"><standardCart:productDescriptionBeforeValidation entry="${entry}" rowcount="${count.count}" showRemoveLink="false"
              showStatus="false" />
            <div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
              <p>
                <spring:message code="cart.payment.quantityQty" />
              </p>
              ${entry.quantity}
              <p class="thirdline">
                <strong><spring:message code="cart.payment.unitPriceforMobile" /> </strong> ${entry.product.deliveryUnit}
                (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
              </p>
              <%-- 						<p>${entry.basePrice}</p> --%>
              <p>
                <spring:message code="cart.shipping.unitprice" />
              </p>
              <p>
                <format:price priceData="${entry.basePrice}" />
              </p>
              <p></p>
              <%-- 						<p>${entry.totalPrice}</p> --%>
              <p>
                <spring:message code="cart.payment.total" />
              </p>
              <p>
                <format:price priceData="${entry.totalPrice}" />
              </p>
              <p></p>
            </div></td>
        </tr>
        <!-- Changes for Bonus Item -->
        <c:if test="${freeGoodsMap ne null}">
          <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
          <c:if test="${not empty valueObject.itemCategory}">
            <tr class="noborder">
              <td class="text-right"><standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
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
<!-- Start - Total Price Summary -->
<div class="boxshadow">
  <div class="row basecontainer fee-toggle-container">
    <table class="total-summary-table">
      <tr>
        <td class="total-summary-label"><spring:message code="cart.common.subTotal" /></td>
        <td class="total-summary-cost totalrps"><format:price priceData="${cartData.subTotal}" /></td>
      </tr>
      <tr>
        <td class="total-summary-label"><spring:message code="cart.validate.cartTotal.fees" /></td>
        <td class="total-summary-cost totalrps"><format:price priceData="${cartData.totalFees}" /></td>
        <td class="toggle-fee"><a data-toggle="collapse" class="toggle-fee-link toggle-link panel-collapsed" href="#fee-mobile-collpase"><span
            class="glyphicon glyphicon-chevron-up"></span></a></td>
      </tr>
      <tr class="fee-panel">
        <td colspan='2'>
          <table id="fee-mobile-collpase" class="fee-collpase-table total-summary-table panel-collapse collapse" style="font-size: 10px !important">
            <tr>
              <td class="total-summary-label" style="width: 25px; font-size: 10px !important"><spring:message code="cart.validate.cartTotal.dropShipFee" /></td>
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
</div>
