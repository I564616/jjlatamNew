<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="consignmentFillUpCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/consignmentFillUp"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<input type="hidden" value="false ${refreshSapPrice}" id="refreshSapPrice">
 <!--Order Derail Row starts-->
 <form:form id="UpdateMultipleEntriesInCartForm" action="updateAll"  commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
 <!-- consignmentFillUp/singleValidateCartEntry.tag -->
 <!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
 <div id="AddItemsCartpage">
  <div class="mainbody-container">
    <div class="hidden-xs">
      <table id="datatab-desktop" class="table table-bordered table-striped sorting-table fee-price-table">
        <thead>
          <tr>
            <th class="no-sort text-uppercase">#</th>
            <%-- <th class="no-sort text-uppercase quanity-cell"><spring:message  code="product.detail.basic.productCode" /></th> --%>
            <th class="no-sort text-left"> <spring:message  code="product.description" /> </th>
            <th class="no-sort">
              <spring:message  code="cart.validate.quantity" />
                <%-- <div class="cart-update-all-link">
                  <a class="cartUpdateAllbtn cartUpdateAllbutton"  id="cartUpdateAllbutton" href="javascript:;">
                    <spring:message  code="cart.review.entry.Updateall" /></a>
                </div> --%>
            </th>
            <%-- <th class="no-sort text-uppercase"><spring:message code="cart.common.customer.UoM"/></th> --%>
            <th class="no-sort text-center text-uppercase est-dateCol"><spring:message  code="Estimated.Date" /></th>
            <th class="no-sort text-uppercase"><spring:message code="cart.priceQuote.status"/></th>  
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
          <c:if test="${entry.product.code ne entry.originalOrderItem}">
            <tr>
            <td colspan="5" style="text-align: left !important; color: green; padding: 0px !important">
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
             <td style="display:none"></td><td style="display:none"></td><td style="display:none"></td><td style="display:none"></td>
            </tr>
          </c:if>
          <%-- <c:if test="${entry.scheduleLines ne null}">
          <tr><td colspan="5" style="text-align: left !important; color: red;">${entry.originalOrderItem} has been substituted with ${entry.product.code}</td></tr>
          </c:if> --%>
          <c:forEach items="${entry.scheduleLines}" var="deliverySch"  varStatus="deliverySchCount">
            <tr id="orderentry-${count.count}">
              <td class="valign-middle">${count.count}.${deliverySchCount.count}</td>
              <%-- <td class="text-left">
              ${entry.product.code}
              </td> --%>
              <td style="text-align: left !important;">
                <consignmentFillUpCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false" />
              </td>
              <td class="valign-middle">
                <div class="cart-update-link">
                  <a href="javascript:void();" id="quantity${entry.entryNumber}" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
                    <spring:message code="cart.review.productDesc.updateItem" /></a>
                </div> 
                <c:url value="/cart/update" var="cartUpdateFormAction" /> 
                  <form:form id="updateCartForm${entry.entryNumber}_desktop" action="${cartUpdateFormAction}" method="post"
                  commandName="updateQuantityForm${entry.entryNumber}">
                  
                  <c:if test="${freeGoodsMap ne null}">
                    <c:set var="freeObject"  value="${freeGoodsMap[entry.product.code]}" />
                  </c:if>
                  <input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
                  <input type="hidden" name="productCode"  value="${entry.product.code}" />
                  <input type="hidden" name="initialQuantity"  value="${entry.quantity}" />
                  <div>
                    <ycommerce:testId code="cart_product_quantity">
                      <form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"></form:label>
                      <%-- <form:input disabled="${not entry.updateable}" type="text"
                        onclick="hideFreeGood('${entry.product.code}', this,'${freeObject.orderedQuantity}');"
                        id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity" /> --%>
                      <input disabled="${not entry.updateable}" type="text"
                        id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" value="${deliverySch.quantity}"/>
                        
                    </ycommerce:testId>
                  </div>
                  <p class="thirdline">
                    <spring:message code="product.detail.addToCart.unit" />
                    ${entry.product.deliveryUnit}
                    (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                  </p>
                  <%-- <ycommerce:testId code="cart_product_removeProduct">
                    <p>
                      <a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
                        <spring:message code="cart.review.productDesc.removeItem" />
                      </a>
                    </p>
                  </ycommerce:testId> --%>
                  <p class="msgHighlight">${entry.product.hazmatCode}</p>
                </form:form>
              </td>
              <td class="valign-middle">
                                <p class="textLeft">
                                  <span class="textBlack">
                                    <fmt:formatDate value="${deliverySch.deliveryDate}" pattern="${dateformat}"/>
                                  </span>
                                </p> 
               </td>
              <td class="text-lowercase   valign-middle">
                <c:choose>
                  <c:when test="${deliverySch.lineStatus  eq 'CONFIRMED'}">
                  <div > <spring:message code="product.variants.available" /> </div>  
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
                <tr class="noborder" id="freeGood${entry.product.code}">
                  <td class="text-right">
                    <consignmentFillUpCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" />
                  </td>
                  <td>
                    <div class="text-center">
                      <c:url value="/cart/update" var="cartUpdateFormAction" />
                      <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post"
                        commandName="updateQuantityForm${entry.entryNumber}">
                        <input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
                        <input type="hidden" name="productCode" value="${entry.product.code}" />
                        <input type="hidden" name="initialQuantity" value="${entry.quantity}" />

                        <input type="hidden" size="4" value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}" />
                        <div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
                        <p class="thirdline">
                          <spring:message code="product.detail.addToCart.unit" />
                          ${entry.product.deliveryUnit}
                          (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                        </p>
                      </form:form>
                    </div>
                  </td>
                  <td class="valign-middle">
                    <spring:message code="cart.freeitem.message" />
                  </td>
                  <td class="valign-middle"></td>
                </tr>
              </c:if>
            </c:if>
          </c:forEach>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <!-- Table collapse for mobile device-->

    <div class="visible-xs hidden-lg hidden-sm hidden-md">
      <table id="datatab-mobile" class="table table-bordered table-striped sorting-table">
        <thead>
          <tr>
            <th class="no-sort text-left">
              <spring:message code="cart.validate.product" /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${cartData.entries}" var="entry" varStatus="count">
            <tr>
              <td class="text-left">
                <consignmentFillUpCart:productDescriptionBeforeValidation entry="${entry}" rowcount="${count.count}" showRemoveLink="true" showStatus="false" />
                <div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
                  <p>
                    <spring:message code="cart.validate.quantityQty" />
                  </p>
                  <c:url value="/cart/update" var="cartUpdateFormAction" />
                  <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post"
                    commandName="updateQuantityForm${entry.entryNumber}">
                    <input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
                    <input type="hidden" name="productCode" value="${entry.product.code}" />
                    <input type="hidden" name="initialQuantity" value="${entry.quantity}" />
                    <ycommerce:testId code="cart_product_quantity">
                    <form:label cssClass="skip" path="quantity"  for="quantity${entry.entryNumber}"> <spring:theme code="basket.page.quantity" /> </form:label>
                    <form:input disabled="${not entry.updateable}" type="text"  id="quantity${entry.entryNumber}"  entryNumber="${entry.entryNumber}"
                        class=" qtyUpdateTextBox form-control txtWidth" path="quantity" />
                    </ycommerce:testId>
                    
                    
                    <ycommerce:testId code="cart_product_removeProduct">
                      <a href="javascript:void();"  id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
                        <spring:message code="cart.review.productDesc.removeItem" />
                      </a>
                    </ycommerce:testId>
                    
                    
                    <p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>
                                  <c:if test="${entry.updateable}" >
                      <ycommerce:testId code="cart_product_updateQuantity">
                        <a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}">
                        <spring:theme code="basket.page.update"/></a>
                      </ycommerce:testId>
                                  </c:if>

                    <p class="msgHighlight">${entry.product.hazmatCode}</p>
                  </form:form>
                  <p>
                    <spring:message code="cart.validate.unitPrice" />
                  </p>
                  <p>
                    <format:price priceData="${entry.basePrice}" />
                  </p>
                  <p>
                    <spring:message code="cart.review.entry.total" />
                  </p>
                  <p>
                    <ycommerce:testId code="cart_totalProductPrice_label">
                      <p class="jnjID">
                        <format:price priceData="${entry.totalPrice}" />
                      </p>
                    </ycommerce:testId>
                  </p>
                </div></td>
            </tr>
            
            <!-- Changes for Bonus Item -->
            <c:if test="${freeGoodsMap ne null}">
              <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
              <c:if test="${not empty valueObject.itemCategory}">
                <tr class="noborder" id="freeGood${entry.product.code}">
                  <td class="text-right">
                    <consignmentFillUpCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
                  <td>
                    <div class="text-center">
                      <c:url value="/cart/update" var="cartUpdateFormAction" />
                      <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" 
                        commandName="updateQuantityForm${entry.entryNumber}">
                        <input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
                        <input type="hidden" name="productCode" value="${entry.product.code}" />
                        <input type="hidden" name="initialQuantity" value="${entry.quantity}" />
                        <input type="hidden" size="4" value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}" />
                        <div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
                        <p class="thirdline">
                          <spring:message code="product.detail.addToCart.unit" />
                          ${entry.product.deliveryUnit}
                          (${entry.product.numerator}&nbsp;${entry.product.salesUnit})
                        </p>
                      </form:form>
                    </div>
                  </td>
                  <td class="valign-middle">
                    <spring:message code="cart.freeitem.message" /></td>
                  <td class="valign-middle"></td>
                </tr>
              </c:if>
            </c:if>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!--Accordian Ends here -->

    <!-- Start - Total Price Summary -->
    <%-- <div class="row basecontainer fee-toggle-container">
      <table class="total-summary-table">
        <tr>
          <td class="total-summary-label">
          <spring:message code="cart.common.subTotal" /></td>
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
            href="#fee-mobile-collpase"><span class="glyphicon glyphicon-chevron-up"></span></a></td>
        </tr>
        <tr class="fee-panel">
          <td colspan='2'>
            <table id="fee-mobile-collpase"
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
          <td class="total-summary-label">
            <spring:message code="cart.validate.cartTotal.tax" /></td>
          <td class="total-summary-cost totalrps">
            <format:price priceData="${cartData.totalTax}" /></td>
        </tr>
        <tr class="total-price-row">
          <td class="total-summary-label">
            <spring:message code="cart.review.shoppingCart.total" /></td>
          <td class="total-summary-cost totalsum">
            <format:price priceData="${cartData.totalPrice}" /></td>
        </tr>
      </table>
    </div> --%>
  </div>
  <!-- End - Total Price Summary -->
</div>
