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
<div class="hidden-xs" id="AddItemsCartpage">
	<!--table starts here  -->
	<div class="hidden-xs dropShipmentTable"
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
		<div class="boxshadow">
		<table id="datatab-desktop"
						class="table table-bordered table-striped sorting-table-lines fee-price-table">
			<thead>
				<tr>
					<th class="no-sort text-left"><spring:message
							code="cart.payment.product" /></th>
					<th class="no-sort text-left"><spring:message
							code="cart.payment.shipping" /></th>
					<th class="no-sort"><spring:message
							code="cart.payment.quantityQty" /></th>
<%-- 					<th class="no-sort multipay-unitprice"><spring:message --%>
<%-- 							code="cart.payment.unitPrice" /></th> --%>
<%-- 					<th class="no-sort total-thead"><spring:message --%>
<%-- 							code="cart.payment.total" /></th> --%>
					<th class="no-sort unitprice-thead"><spring:message
							code="cart.validate.unitPrice" /></th>
					<th class="no-sort total-thead fee-cell text-uppercase"><spring:message
							code="cart.payment.total" /></th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${jnjCartData.entries}" var="entry"
					varStatus="count">
					<tr id="orderentry-${count.count}">
						<td class="text-left"><standardCart:productDescriptionBeforeValidation
								entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
						<td class="text-left">
							<div>${entry.shippingMethod}</div>
							<div>
								<commonTags:entryLevelDates entry="${entry}" />
							</div>
						</td>
						<td class="valign-middle">${entry.quantity} <br />
							<p class="thirdline">
								<spring:message code="product.detail.addToCart.unit" />
								${entry.product.deliveryUnit}
								(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
							</p>
						</td>
						<td class="valign-middle" id="basePrice_${entry.entryNumber}">
							<format:price priceData="${entry.basePrice}" />
						</td>
						<td class="valign-middle totalrps fee-cell "
							id="totalPrice_${entry.entryNumber}"><ycommerce:testId
								code="cart_totalProductPrice_label">
								<span class="txt-nowrap"><format:price priceData="${entry.totalPrice}" /></span>
							</ycommerce:testId></td>
					</tr>
					<!-- Changes for Bonus Item -->
					<c:if test="${freeGoodsMap ne null}">
	             		<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}"/>
						<c:if test="${not empty valueObject.itemCategory}">
							<tr class="noborder">
								<td class="text-right">
									<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false"/>
								</td>
								<td class="valign-middle"></td>
								<td>
									<div class="text-center">${valueObject.materialQuantity}
									</div>
									<p class="thirdline">
										<spring:message code="product.detail.addToCart.unit" />
										${entry.product.deliveryUnit}
										(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>
								</td>
								<td>
										<span class="txt-nowrap"><spring:message code="cart.freeitem.message" /></span>
									</td>
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
					<td class="total-summary-label" style="font-size:10px !important"><spring:message
							code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${jnjCartData.subTotal}" /></td>
				</tr>
				<tr>
					<td class="total-summary-label" style="width: 25px;"><spring:message
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
							class="fee-collpase-table total-summary-table panel-collapse collapse" style="font-size:10px !important">
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
<div class="visible-xs hidden-lg hidden-sm hidden-md">
	<table id="datatab-mobile"
		class="table table-bordered table-striped sorting-table-lines">
		<thead>
			<tr>
				<th class="no-sort text-left"><spring:message
						code="cart.payment.product" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jnjCartData.entries}" var="entry"
				varStatus="count">
				<tr>
					<td class="text-left"><standardCart:productDescriptionBeforeValidation
							entry="${entry}" showRemoveLink="false" showStatus="false" />
						<p>
							<spring:message code="cart.payment.quantityQty" />
						</p> ${entry.quantity}
						<p class="thirdline">
							<strong><spring:message
									code="cart.payment.unitPriceforMobile" /> </strong>
							${entry.product.deliveryUnit}
							(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
						</p>

						<p>${entry.basePrice}</p>
						<p></p>
						<p>${entry.totalPrice}</p>
						<p></p></td>
				</tr>
				<!-- Changes for Bonus Item -->
					<!-- Changes for Bonus Item -->
					<c:if test="${freeGoodsMap ne null}">
	             		<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}"/>
						<c:if test="${not empty valueObject.itemCategory}">
							<tr class="noborder">
								<td class="text-right">
									<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false"/>
								</td>
								<td class="valign-middle"></td>
								<td>
									<div class="text-center">${valueObject.materialQuantity}
									</div>
									<p class="thirdline">
										<spring:message code="product.detail.addToCart.unit" />
										${entry.product.deliveryUnit}
										(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>
								</td>
							<%-- 	<td>
										<spring:message code="cart.freeitem.message" />
									</td> --%>
									<td class="valign-middle"></td>
							</tr>
						</c:if>
	             	</c:if>
			</c:forEach>

		</tbody>
	</table>
</div>
<!-- Start - Total Price Summary -->
