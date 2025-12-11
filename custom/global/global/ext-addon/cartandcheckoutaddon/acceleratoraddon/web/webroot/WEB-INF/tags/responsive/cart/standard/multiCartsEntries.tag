<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false"
	type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjCartData" required="true"
	type="com.jnj.facades.data.JnjGTCartData"%>
<%@ attribute name="globalCount" required="true" type="java.lang.String"%>
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class=" hidden-xs tableshadow">
			<div class="dateselectordropdown">
				<div class="dateselector">
					<div class="setshipping">
						<spring:message code="cart.shipping.Setallitems" />
					</div>
					<c:choose>
						<c:when test="${!checkoutoption}">
							<div class="input-group marginleft20">
								<input id="date-picker-head-${globalCount}" name="toDate"
									placeholder="<spring:message code='cart.option.selectDate'/>"
									class="date-picker form-control date-picker-head" type="text">
								<label for="date-picker-head-${globalCount}"
									class="input-group-addon btn"><span
									class="glyphicon glyphicon-calendar"></span> </label>
							</div>

						</c:when>
						<c:otherwise>


							<c:forEach items="${jnjCartData.entries}" var="entryList"
								varStatus="count">
								<c:if test="${'1' eq count.count}">
									<select class="shippingMethodSelect shipping-head-dropdown"
										data="${entryList.entryNumber}" id=" ">
										<option selected="selected" style="display: none;"><spring:message
												code="cart.common.selectmethod" /></option>
										<c:forEach items="${entryList.shippingMethodsList}"
											var="shippingMethod">
											<option
												value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
												<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
												<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
												<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"></c:if>>

												${shippingMethod.dispName}</option>
										</c:forEach>

									</select>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class=" hidden-xs tableshadow dropShipmentTable"
				id="dropShipmentTable-${globalCount}">


				<div class="dropshipment-label-value">
					<label class="dropshipment-label"> <strong><spring:message
								code="cart.common.orderType" /></strong>
						<div class="dropshipment-value">
							<spring:message
								code="cart.common.orderType.${jnjCartData.orderType}" />
						</div>
					</label>
				</div>

<!-- 				<table id="ordersTable" -->
<!-- 					class="table table-bordered table-striped sorting-table-lines"> -->
						<table id="datatab-desktop"
		class="table table-bordered table-striped sorting-table-lines fee-price-table">
					<thead>
						<tr>
							<th class="no-sort"><spring:message
									code="cart.shipping.product" /></th>
							<th class="no-sort"><spring:message
									code="cart.shipping.shipping" /></th>
							<th class="no-sort"><spring:message
									code="cart.shipping.quantity" /></th>
							<th class="no-sort unitprice-thead"><spring:message
									code="cart.shipping.unitprice" /></th>
<%-- 							<th class="no-sort multitotal-thead"><spring:message --%>
<%-- 									code="cart.shipping.total" /></th> --%>
							<th class="no-sort total-thead fee-cell multitotal-thead text-uppercase"><spring:message
								code="cart.review.entry.total" /></th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${jnjCartData.entries}" var="entry"
							varStatus="count">
							<tr id="orderentry-${count.count}">
								<td class="text-left jnj-img-txt"><standardCart:productDescriptionBeforeValidation
										entry="${entry}" showRemoveLink="true" showStatus="false" /></td>

								<td>


									<div class="floatLeft column6">
										<c:choose>
											<c:when test="${!checkoutoption}">
												<div class="input-group  margintop10">
													<input data="${entry.entryNumber}"
														id="date-picker-${count.count}" name="toDate"
														placeholder="<spring:message code='cart.option.selectDate'/>"
														class="date-picker form-control date-picker-body"
														type="text"> <label
														for="date-picker-${count.count}"
														class="input-group-addon btn"><span
														class="glyphicon glyphicon-calendar"></span> </label>
												</div>
											</c:when>
											<c:otherwise>
												<select class="shippingMethodSelect shipping-body-dropdown"
													data="${entry.entryNumber}">
													<c:forEach items="${entry.shippingMethodsList}"
														var="shippingMethod">
														<option
															value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
															<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
															<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
															<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"> </c:if>>
															${shippingMethod.dispName}</option>
													</c:forEach>
													<option selected="selected" style="display: none;"><spring:message
															code="cart.common.selectmethod" /></option>
												</select>
											</c:otherwise>
										</c:choose>
									</div>
								</td>

								<td class="valign-middle">${entry.quantity}<br />
									<p class="thirdline">
										<spring:message code="product.detail.addToCart.unit" />
										${entry.product.deliveryUnit}
										(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>


								</td>
								<td class="valign-middle" id="basePrice_${entry.entryNumber}">
									<format:price priceData="${entry.basePrice}" />
								</td>
								<td class="valign-middle totalrps fee-cell"
									id="totalPrice_${entry.entryNumber}"><ycommerce:testId
										code="cart_totalProductPrice_label">
										<span class="txt-nowrap"><format:price priceData="${entry.totalPrice}" /></span>
									</ycommerce:testId></td>
							</tr>

							<!-- Changes for Bonus Item -->
							<c:if test="${freeGoodsMap ne null}">
								<c:set var="valueObject"
									value="${freeGoodsMap[entry.product.code]}" />
								<c:if test="${not empty valueObject.itemCategory}">
									<tr class="noborder">
										<td class="text-right"><standardCart:productDescriptionBeforeValidation
												entry="${entry}" showRemoveLink="false" showStatus="false" />
										</td>
										<td class="valign-middle"></td>
										<td>
											<div class="text-center">
												${valueObject.materialQuantity}</div>
											<p class="thirdline">
												<spring:message code="product.detail.addToCart.unit" />
												${entry.product.deliveryUnit}
												(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
											</p>
										</td>
										<td class="valign-middle"><span class="txt-nowrap"><spring:message code="cart.freeitem.message" /></span></td>
										<td class="valign-middle"></td>
									</tr>
								</c:if>
							</c:if>
						</c:forEach>

					</tbody>

				</table>
				<div class="row basecontainer fee-toggle-container">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${jnjCartData.subTotal}" /></td>
				</tr>
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.fees" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${jnjCartData.totalFees}" /></td>
					<td class="toggle-fee"><a data-toggle="collapse"
						class="toggle-fee-link toggle-link panel-collapsed"
						href="#fee-mobile-collpase${globalCount}"><span
							class="glyphicon glyphicon-chevron-up"></span></a></td>
				</tr>
				<tr class="fee-panel">
					<td colspan='2'>
						<table id="fee-mobile-collpase${globalCount}"
							class="fee-collpase-table total-summary-table panel-collapse collapse">
							<tr>
								<td class="total-summary-label" style="width: 25px;font-size:10px !important"><spring:message code="cart.validate.cartTotal.dropShipFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalDropShipFee}" /></td>
							</tr>
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalminimumOrderFee}" /></td>
							</tr>
							
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.freightCost"/></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${jnjCartData.totalFreightFees}"/></td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="summary-bline ">
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.tax" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${jnjCartData.totalTax}" /></td>
				</tr>
				<tr class="total-price-row">
					<td class="total-summary-label"><spring:message
							code="cart.review.shoppingCart.total" /></td>
					<td class="total-summary-cost totalsum"><format:price
							priceData="${jnjCartData.totalPrice}" /></td>

				</tr>
			</table>

		</div>

			</div>

		</div>

	</div>
</div>
<!-- 	<div class="row basecontainer"> -->
<%-- 		<standardCart:multiCartTotalItem cartData="${jnjCartData}" /> --%>
<!-- 	</div> -->
