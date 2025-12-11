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
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjCartData" required="true" type="com.jnj.facades.data.JnjGTCartData"%>
<%@ attribute name="globalCount" required="true" type="java.lang.String" %>

<div class="hidden-xs dropShipmentTable" id="dropShipmentTable-${globalCount}">
	<div class="dropshipment-label-value">
		<label class="dropshipment-label"> <strong><spring:message
					code="cart.common.orderType" /></strong>
			<div class="dropshipment-value">
				<spring:message code="cart.common.orderType.${jnjCartData.orderType}" />
			</div>
		</label>
	</div>
	<table id="datatab-desktop"
		class="table table-bordered table-striped sorting-table"
		 data-paging="false" data-info="false">
		<thead>
			<tr>
				<th class="no-sort text-left text-uppercase"><spring:message
						code="cart.validate.product" /></th>
				<th class="no-sort text-left text-uppercase"><spring:message
						code="cart.shipping.RequestedDeliveryDate" /></th>
                <c:if test="${displayIndirectCustomerHeader}">
                    <th class="text-left no-sort"><spring:theme
                            code="text.account.buildorder.indirectCustomer"
                            text="INDIRECT CUSTOMER" /></th>
                </c:if>
                <c:if test="${displayIndirectPayerHeader}">
                    <th class="text-left no-sort"><spring:theme
                            code="text.account.buildorder.indirectPayer"
                            text="INDIRECT PAYER" /></th>
                </c:if>
				<th class="no-sort text-uppercase"><spring:message
						code="cart.validate.quantity" /></th>
				<th class="no-sort text-uppercase"><spring:message
						code="cart.validate.unitPrice" /></th>
				<th class="no-sort text-uppercase totalTableCell"><spring:message
						code="cart.validate.total" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jnjCartData.entries}" var="entry" varStatus="count">
				<tr id="orderentry-${count.count}">
					<td class="text-left"><standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false" />
                        <div class="txtWidth text-left">
                          <c:if test="${cartData.partialDelivFlag && !cartData.holdCreditCardFlag && showATPFlagMap.get(entry)}">
                              <standardCart:entryMoreInfo entry="${entry}" showInfoLbl="true" showDescLbl="false"/>
                          </c:if>
                        </div>
                    </td>
					<c:choose>
                        <c:when test="${!checkoutoption}">
                            <td class="valign-middle">
                                <fmt:formatDate pattern="${sessionlanguagePattern}" value="${entry.expectedDeliveryDate}" /><br>
                            </td>
                            </c:when>
                            <c:otherwise>
                            <td class="valign-middle">${entry.shippingMethod}<br> <c:if
                                    test="${not entry.expandableSchedules}">

                                    <commonTags:entryLevelDates entry="${entry}" />

                                </c:if>
                            </td>
                        </c:otherwise>
					</c:choose>

                    <!-- Checking indirect customer line level flag -->
                    <c:if test="${displayIndirectCustomerLine[count.index]}">
                        <td class="txtWidth valign-middle">
                            <p class="descMid" id="indirectCustomerId${count.index}">${entry.indirectCustomer}</p>
                            <p class="descMid" id="indirectCustomerName${count.index}">${entry.indirectCustomerName}</p>
                        </td>
                    </c:if>
                    <c:if test="${displayIndirectCustomerHeader eq true && (empty displayIndirectCustomerLine || displayIndirectCustomerLine[count.index] eq false)}">
                        <td></td>
                    </c:if>

                    <!-- Checking indirect payer line level flag -->
                    <c:if test="${displayIndirectPayerLine[count.index]}">
                        <td class="txtWidth valign-middle">
                            <p class="descMid" id="indirectPayerId${count.index}">${entry.indirectPayer}</p>
                            <p class="descMid" id="indirectPayerName${count.index}" >${entry.indirectPayerName}</p>
                        </td>
                    </c:if>
                    <c:if test="${displayIndirectPayerHeader eq true && (empty displayIndirectPayerLine || displayIndirectPayerLine[count.index] eq false)}">
                        <td></td>
                    </c:if>

					<td class="valign-middle">
						<div>${entry.quantity}&nbsp;${entry.product.salesUnitCode}</div>
					</td>
					<td class="valign-middle"><format:price priceData="${entry.basePrice}" /></td>
					<td class="valign-middle totalTableCell"><ycommerce:testId
							code="cart_totalProductPrice_label">
							<format:price priceData="${entry.totalPrice}" />
						</ycommerce:testId>
				    </td>

                    <!-- Free item -->
                    <c:if test="${freeGoodsMap ne null}">
                        <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
				        <c:if test="${freeGoodsMap[entry.product.code] == valueObject.materialNumber}">
                	        <standardCart:bonusItem entry="${entry}"/>
                	    </c:if>
                	    <c:if test="${freeGoodsMap[entry.product.code] != valueObject.materialNumber}">
                	        <standardCart:alternateMaterial entry="${entry}" valueObject="${valueObject}"/>
                        </c:if>
                    </c:if>
				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>

		<c:set var="cartCount" value="0"/>
		<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
						<c:set var="cartCount" value="${count.count}"/>
		</c:forEach>
		<input type="hidden" name="numberOfItemscart" id="numberOfItemscart" value="${cartCount}"/>
					
<!-- Table collapse for mobile device-->

<div class="row visible-xs hidden-lg hidden-sm hidden-md">
	<div class="col-xs-12">
		<table id="datatab-mobile"
			class="table table-bordered table-striped sorting-table">
			<thead>
				<tr>
					<th class="no-sort text-left text-uppercase"><spring:message
							code="cart.validate.product" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${jnjCartData.entries}" var="entry" varStatus="count">
					<tr>
						<td class="text-left"><standardCart:productDescriptionBeforeValidation
								entry="${entry}" showRemoveLink="true" showStatus="false" />
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
							<p class="thirdline">${entry.product.salesUnit}</p>
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
							</p></td>
					</tr>
				</c:forEach>


			</tbody>
		</table>
	</div>
</div>

<!--Accordian Ends here -->

<!-- Start - Total Price Summary -->
<div class="row basecontainer">

	<table class="total-summary-table">
		<tr>
			<td class="total-summary-label text-uppercase"><spring:message
					code="cart.validate.cartTotal.subTotal" /></td>
			<td class="total-summary-cost totalrps no-right-pad"><format:price
					priceData="${jnjCartData.subTotal}" /></td>
			<td class="hidden"></td>
		</tr>
		<c:if test="${jnjCartData.totalFreightFees.value > 0}">
		    <tr class="summary-bline">
			    <td class="total-summary-label text-uppercase"><spring:message code="orderDetailPage.orderData.Shipping"/></td>
			    <td class="total-summary-cost totalrps no-right-pad">
			        <format:price priceData="${jnjCartData.totalFreightFees}" /></td>
		    </tr>
		</c:if>
		<c:if test="${jnjCartData.totalFees.value > 0}">
			<tr>
				<td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
				<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.totalFees}" /></td>
			</tr>
		</c:if>
		<c:if test="${jnjCartData.totalTax.value > 0}">
			<tr>
				<td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
				<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjCartData.totalTax}"/></td>
			</tr>
		</c:if>
		<tr class="total-price-row">
			<td class="total-summary-label text-uppercase"><spring:message
					code="cart.validate.cartTotal.totals" /></td>
			<td class="total-summary-cost totalsum no-right-pad"><format:price
					priceData="${jnjCartData.totalGrossPrice}" /></td>
			<td class="hidden"></td>
		</tr>
	</table>

</div>
<!-- End - Total Price Summary -->
