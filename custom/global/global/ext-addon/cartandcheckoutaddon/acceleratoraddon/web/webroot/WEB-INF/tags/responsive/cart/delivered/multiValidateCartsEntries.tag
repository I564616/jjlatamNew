<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="jnjCartData" required="true"
	type="com.jnj.facades.data.JnjGTCartData"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="varRowCount" required="true"
	type="java.lang.Integer"%>

<input type="hidden" value="false ${refreshSapPrice}"
	id="refreshSapPrice">
<!--Order Derail Row starts-->
<form:form id="UpdateMultipleEntriesInCartForm" action="updateAll"
	method="post" commandName="UpdateMultipleEntriesInCartForm">
</form:form>
<!-- <div class="orderDetailPanel"> -->
<%-- <span><span class="txtFont"><spring:message code="cart.common.orderType" /></span><b><spring:message code="cart.common.orderType.${jnjCartData.orderType}" /></b></span> --%>
<div class="hidden-xs dropShipmentTable"	id="dropShipmentTable-${varRowCount}">
	<div class="dropshipment-label-value">
		<label class="dropshipment-label"> 
		<strong><spring:message code="cart.common.orderType" /></strong>
			<div class="dropshipment-value">
				<spring:message  code="cart.common.orderType.${jnjCartData.orderType}" />
			</div>
		</label>
	</div>
	<table id="datatab-desktop"
		class="table table-bordered table-striped sorting-table-lines fee-price-table">
		<thead>
			<tr>
				<th class="no-sort text-left">
					<spring:message  code="cart.validate.product" /></th>
				<th class="no-sort">
					<spring:message code="cart.validate.quantity" />
					<div class="cart-update-all-link">
						<a class="cartUpdateAllbtn splitcartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"> 
							<spring:message code="cart.review.entry.Updateall" />
						</a>
					</div>
				</th>
				<th class="no-sort unitprice-thead">
					<spring:message  code="cart.validate.unitPrice" /></th>
				<th class="no-sort total-thead fee-cell">
					<spring:message code="cart.review.entry.total" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jnjCartData.entries}" var="entry"  varStatus="count">
				<tr id="orderentry-${count.count}">
					<%-- 					<div class="floatLeft column1 paddingLeft"> ${count.count}</div> --%>
					<td class="text-left">
						<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false" /></td>

					<td class="valign-middle">
						<div class="cart-update-link">
							<a href="javascript:void();" id="quantity${entry.entryNumber}" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
								<spring:message code="cart.review.productDesc.updateItem" />
							</a>
						</div> <c:url value="/cart/update" var="cartUpdateFormAction" /> 
						<form:form  id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post"
							commandName="updateQuantityForm${entry.entryNumber}">
							<c:if test="${freeGoodsMap ne null}">
								<c:set var="freeObject"  value="${freeGoodsMap[entry.product.code]}" />
							</c:if>
							<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
							<input type="hidden" name="productCode" value="${entry.product.code}" />
							<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
							<div>
								<ycommerce:testId code="cart_product_quantity">
									<form:label cssClass="skip" path="quantity"  for="quantity${entry.entryNumber}"></form:label>
									<form:input disabled="${not entry.updateable}" type="text"
										onclick="hideFreeGood('${entry.product.code}', this,'${freeObject.orderedQuantity}');"
										id="quantity${entry.entryNumber}" 
										entryNumber="${entry.entryNumber}"
										class=" qtyUpdateTextBox form-control txtWidth"
										path="quantity" />

								</ycommerce:testId>
							</div>
							<p class="thirdline">
								<spring:message code="product.detail.addToCart.unit" />
								${entry.product.deliveryUnit}
								(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
							</p>

							<ycommerce:testId code="cart_product_removeProduct">
								<p>
									<a href="javascript:void();"  id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
										<spring:message code="cart.review.productDesc.removeItem" /> </a>
								</p>
							</ycommerce:testId>
							<p class="msgHighlight">${entry.product.hazmatCode}</p>
						</form:form>
					</td>

					<td class="valign-middle" id="basePrice_${entry.entryNumber}">
						<format:price priceData="${entry.basePrice}" />
					</td>

					<td class="valign-middle totalrps fee-cell" id="totalPrice_${entry.entryNumber}">
						<ycommerce:testId  code="cart_totalProductPrice_label">
							<format:price priceData="${entry.totalPrice}" />
						</ycommerce:testId>
					</td>
				</tr>
				
				<!-- Changes for Bonus Item -->
				<c:if test="${freeGoodsMap ne null}">
					<c:set var="valueObject"
						value="${freeGoodsMap[entry.product.code]}" />
					<c:if test="${not empty valueObject.itemCategory}">
						<tr class="noborder" id="freeGood${entry.product.code}">
							<td class="text-right">
								<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" /></td>
							<td>
								<div class="text-center">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
										<input type="hidden" name="productCode" value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity" value="${entry.quantity}" />

										<input type="hidden" size="4" value="${valueObject.materialQuantity}"
											id="freeGoodQuantity${entry.product.code}" /> 
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

	<standardCart:multiValidateCartTotalItem jnjCartData="${jnjCartData}" varRowCount="${varRowCount}"/>
