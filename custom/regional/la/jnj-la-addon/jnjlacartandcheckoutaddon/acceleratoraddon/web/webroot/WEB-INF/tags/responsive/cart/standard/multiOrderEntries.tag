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
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="standardCartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjOrderData" required="true" type="com.jnj.facades.data.JnjGTOrderData" %>
<%@ attribute name="varRowCount" required="true" type="java.lang.Integer"%>
<c:url value="/order-history/order/${jnjOrderData.orderNumber}"
	var="orderDetailURL" />
<div class="mainbody-container">
	<div class="hidden-xs dropShipmentTable" id="dropShipmentTable-${varRowCount}">
		<div class="dropshipment-label-value">
		<label class="dropshipment-label"> <strong><spring:message
					code="cart.common.orderType" /></strong>
			<div class="dropshipment-value">
				<spring:message code="cart.common.orderType.${jnjOrderData.orderType}" />
			</div>
		</label>
		<label class="dropshipment-label">
			<strong><spring:message code="cart.confirmation.orderNumber" /></strong>  
			<div class="dropshipment-value">
				<a href="${orderDetailURL}">
						${jnjOrderData.sapOrderNumber}</a>
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
					<th class="no-sort text-uppercase"><spring:message
							code="cart.validate.quantity" /></th>
					<th class="no-sort text-uppercase"><spring:message
							code="cart.validate.unitPrice" /></th>
					<th class="no-sort text-uppercase totalTableCell"><spring:message
							code="cart.validate.total" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${jnjOrderData.entries}" var="entry"
					varStatus="count">
					<tr id="orderentry-${count.count}">
						<td class="text-left"><standardCartLa:productDescriptionOrderComplete
								entry="${entry}" showRemoveLink="true" showStatus="false" />
								
                            <div class="txtWidth text-left">
                                <c:if test="${!jnjOrderData.holdCreditCardFlag  && jnjOrderData.partialDelivFlag}">
                                    <standardCartLa:entryMoreInfo entry="${entry}" showInfoLbl="true" showDescLbl="false"/>
                                </c:if>
                            </div>
								
						</td>
						<c:choose>
					<c:when test="${!checkoutoption}">
					<td class="valign-middle">
					    <fmt:formatDate pattern="MM/dd/yyyy" value="${entry.expectedDeliveryDate}" /><br>
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
						<td class="valign-middle">
							<div>${entry.quantity}&nbsp;${entry.product.salesUnitCode}</div>
						</td>
						<td class="valign-middle"><format:price priceData="${entry.basePrice}" /></td>
						<td class="valign-middle totalTableCell"><ycommerce:testId
								code="cart_totalProductPrice_label">
								<format:price priceData="${entry.totalPrice}" />
							</ycommerce:testId></td>

                        <!-- Free item -->
                        <c:if test="${freeGoodsMap ne null}">
                            <c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
                            <c:if test="${freeGoodsMap[entry.product.code] == valueObject.materialNumber}">
						        <standardCartLa:bonusItem entry="${entry}"/>
						    </c:if>
						    <c:if test="${freeGoodsMap[entry.product.code] != valueObject.materialNumber}">
						        <standardCartLa:alternateMaterial entry="${entry}" valueObject="${valueObject}"/>
                            </c:if>
                        </c:if>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>


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
					<c:forEach items="${jnjOrderData.entries}" var="entry"
						varStatus="count">
						<tr>
							<td class="text-left"><standardCartLa:productDescriptionOrderComplete
									entry="${entry}" showRemoveLink="true" showStatus="false" />
								<P>${entry.shippingMethod}<br>
									<c:if test="${not entry.expandableSchedules}">

										<commonTags:entryLevelDates entry="${entry}" />


									</c:if>
								</P>
								<p class="text-uppercase">
									<spring:message code="cart.validate.quantityQty" />
								</p>
								<p>${entry.quantity}&nbsp;${entry.product.salesUnit}</p>
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
						priceData="${jnjOrderData.subTotal}" /></td>

			</tr>
            <c:if test="${jnjOrderData.totalFreightFees.value > 0}">
			    <tr class="summary-bline">
				    <td class="total-summary-label text-uppercase"><spring:message code="cart.shipping.shipping" /></td>
				    <td class="total-summary-cost totalrps no-right-pad">
				        <format:price priceData="${jnjOrderData.totalFreightFees}" /></td>
			    </tr>
			</c:if>
			<c:if test="${jnjOrderData.totalFees.value > 0}">
			    <tr>
				    <td class="total-summary-label"><spring:message code="cart.total.totalFees" /></td>
				    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjOrderData.totalFees}" /></td>
			    </tr>
			</c:if>
			<c:if test="${jnjOrderData.totalTax.value > 0}">
			    <tr>
				    <td class="total-summary-label"><spring:message code="order.history.taxes" /></td>
				    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjOrderData.totalTax}"/></td>
			    </tr>
			</c:if>
			<c:if test="${jnjOrderData.discountTotal.value > 0}">
			    <tr>
				    <td class="total-summary-label"><spring:message code="order.history.discounts" /></td>
				    <td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${jnjOrderData.discountTotal}"/></td>
			    </tr>
			</c:if>
			<tr class="total-price-row">
				<td class="total-summary-label text-uppercase"><spring:message
						code="cart.validate.cartTotal.totals" /></td>
				<td class="total-summary-cost totalsum no-right-pad"><format:price
						priceData="${jnjOrderData.totalGrossPrice}" /></td>

			</tr>
		</table>

	</div>
	<!-- End - Total Price Summary -->
</div>