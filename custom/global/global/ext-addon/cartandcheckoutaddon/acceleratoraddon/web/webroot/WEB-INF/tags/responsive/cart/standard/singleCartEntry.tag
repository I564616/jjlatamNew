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
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ attribute name="globalCount" required="true" type="java.lang.String"%>
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class=" hidden-xs tableshadow">
			<!-- <div class="dateselectordropdown"> -->
            	<div class="SortingTable" id="ordersTable_length">
	             <div class="setshipping"><spring:message code="cart.shipping.Setallitems" /></div>
	                  <c:choose>
								<c:when test="${!checkoutoption}">
									<div class="input-group marginleft20">
										<input id="date-picker-head" name="toDate"	placeholder="<spring:message code='cart.common.selectDate' />" class="date-picker form-control"	type="text">
										 <label for="date-picker-head" class="input-group-addon btn">
										 <span	class="glyphicon glyphicon-calendar"></span> </label>
									</div>
								</c:when>
								<c:otherwise>
									<select class="shippingMethodSelect selectpicker"	data="${cartData.entries[0]}" id="shipping-head-dropdown">
										<option selected="selected" style="display: none;">Select Method</option>
										<c:forEach items="${cartData.entries[0].shippingMethodsList}" var="shippingMethod">
											<option
												value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
												<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
												<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
												<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"></c:if>>

												${shippingMethod.dispName}</option>
										</c:forEach>
									</select>
								</c:otherwise>
							</c:choose>
	            </div>
	       <!--   </div>	 -->
			<table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines fee-price-table">
				
				<thead>
					<tr>
						<th class="no-sort snoClassSingleCart"><spring:message code="cart.review.entry.number"/></th>
						<th class="no-sort product-cell"><spring:message code="cart.shipping.product" /></th>
						<th class="no-sort" style="width:136px !important"><spring:message code="cart.shipping.shipping" /></th>
						<th class="no-sort quantitySingleCart"><spring:message code="cart.shipping.quantity" /></th>
						<th class="no-sort unitprice-thead"><spring:message code="cart.validate.unitPrice" /></th>
						<th class="no-sort total-thead fee-cell multitotal-thead  text-uppercase"> <spring:message code="cart.shipping.total" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
						<tr id="orderentry-${count.count}">
						<td class="snoClassSingleCart">${count.count}</td>
							<td class="text-left">
								<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false" /></td>

							<td>
								<div class="floatLeft column6">
									<c:choose>
										<c:when test="${!checkoutoption}">
											<div class="input-group  margintop10">
												<input data="${entry.entryNumber}" id="date-picker-${count.count}" name="toDate"
													placeholder="<spring:message code='cart.common.selectDate' />" class="date-picker form-control date-picker-body" type="text"> 
													<label for="date-picker-${count.count}" class="input-group-addon btn">
													<span class="glyphicon glyphicon-calendar"></span> </label>
											</div>
										</c:when>
										<c:otherwise>
											<select class="shippingMethodSelect shipping-body-dropdown" data="${entry.entryNumber}" data-width="136px">
												<c:forEach items="${entry.shippingMethodsList}" var="shippingMethod">
													<option
														value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
														<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
														<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
														<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"> </c:if>>
														${shippingMethod.dispName}</option>
												</c:forEach>
												<option selected="selected" style="display: none;">
												<spring:message code="cart.common.selectmethod" /></option>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</td>

							<td class="valign-middle  text-center">${entry.quantity}<br />
								<p class="thirdline">
									<spring:message code="product.detail.addToCart.unit" />
									${entry.product.deliveryUnit}
									(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
								</p>
							</td>
							<td class="valign-middle" id="basePrice_${entry.entryNumber}">
								<format:price priceData="${entry.basePrice}" />
							</td>
							<td class="valign-middle totalrps fee-cell" id="totalPrice_${entry.entryNumber}">
								<ycommerce:testId code="cart_totalProductPrice_label">
									<span class="txt-nowrap">
									<format:price priceData="${entry.totalPrice}" /></span>
								</ycommerce:testId></td>
						</tr>

						<!-- Changes for Bonus Item -->
						<c:if test="${freeGoodsMap ne null}">
							<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
							<c:if test="${not empty valueObject.itemCategory}">
								<tr class="noborder">
									<td class="text-right">
									<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="false" showStatus="false" />
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
					<td class="total-summary-label"><spring:message code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps"><format:price priceData="${cartData.subTotal}" /></td>
				</tr>
				<tr>
					<td class="total-summary-label"><spring:message code="cart.validate.cartTotal.fees" /></td>
					<td class="total-summary-cost totalrps"><format:price priceData="${cartData.totalFees}" /></td>
					<td class="toggle-fee"><a data-toggle="collapse" class="toggle-fee-link toggle-link panel-collapsed"
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
		<!-- 		MOBILE DEVICE	 -->
		 <div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
			<table id="ordersTablemobile"
				class="table table-bordered table-striped tabsize  sorting-table-lines bordernone">
				<thead>
					<tr>
						<th class="text-left no-sort"><spring:message code="cart.shipping.product" /></th>
						<th class="text-left no-sort"><spring:message code="cart.shipping.total" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry" varStatus="status">
						<tr>
							<td class="panel-title text-left valign-top">
								<a data-toggle="collapse" data-parent="#accordion" href="#collapse${status.count}"
									class="ref_no toggle-link panel-collapsed skyBlue">
										<table>
											<tbody>
												<tr>
													<td class="padding0"><span class="glyphicon glyphicon-plus skyBlue"></span></td>
													<td class="paddingforText">${entry.product.name}</td>
												</tr>
											</tbody>
										</table>
								</a>
								<div id="collapse${status.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<!-- <p>Unit: Case(24 each)</p> -->
										<p>
											<strong><spring:message code="cart.shipping.shipping" /></strong>
										</p>
										<c:choose>
											<c:when test="${!checkoutoption}">
												<div class="input-group  margintop10">
													<input data="${entry.entryNumber}" id="date-picker-${count.count}" name="toDate"
														placeholder="<spring:message code='cart.common.selectDate' />" class="date-picker form-control date-picker-body" type="text"> 
														<label for="date-picker-${count.count}" class="input-group-addon btn">
														<span class="glyphicon glyphicon-calendar"></span> </label>
												</div>
											</c:when>
											<c:otherwise>
												<select class="shippingMethodSelect shipping-body-dropdown" data="${entry.entryNumber}">
													<c:forEach items="${entry.shippingMethodsList}" var="shippingMethod">
														<option
															value="${shippingMethod.route} +~~+ ${shippingMethod.expidateRoute}"
															<c:if test="${entry.shippingMethod eq shippingMethod.dispName}"> selected="selected"</c:if>
															<c:if test="${shippingMethod.route eq entry.defaultRoute}"> selected="selected"</c:if>
															<c:if test="${shippingMethod.route eq 'Standard' && (entry.shippingMethod eq null || !entry.selectableShippingMethod)}"> </c:if>>
															${shippingMethod.dispName}</option>
													</c:forEach>
													<option selected="selected" style="display: none;">
													<spring:message code="cart.common.selectmethod" /></option>
												</select>
											</c:otherwise>
										</c:choose>
										</p>
										<p>
											<strong><spring:message code="cart.shipping.quantity" /></strong>
										</p>
										<p>${entry.quantity}
										<!-- <br /> -->
										<p class="thirdline">
											<spring:message code="product.detail.addToCart.unit" />
											${entry.product.deliveryUnit}
											(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
										</p> 
										<P>
											<strong><spring:message code="cart.shipping.unitprice" /></strong>
										</P>
										<P>
											<format:price priceData="${entry.basePrice}" />
										</P>
									</div>
								</div></td>
							<td class="shipping-address-padding2 valign-top">
								<format:price priceData="${entry.totalPrice}" /></td>
						</tr>

					</c:forEach>
				</tbody>

			</table>
			<div class="basecontainer">
				<div class="row bgcls1">
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginright17">
							<strong><spring:message code="cart.shipping.SubTotal" /></strong>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen shipping-address-padding2">
							<strong><format:price priceData="${cartData.subTotal}" /></strong>
						</div>
					</div>
				</div>
				<div class="row bgcls2">
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginright20">
							<strong><spring:message code="cart.validate.cartTotal.fees" /></strong>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen">
							<strong><format:price priceData="${cartData.totalFees}" /></strong>
						</div>
					</div>
					
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginright20">
							<strong><spring:message code="cart.validate.cartTotal.tax" /></strong>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen">
							<strong><format:price priceData="${cartData.totalTax}" /></strong>
						</div>
					</div>
					<hr class="line pull-right">
				</div>
				<div class="row bgcls3">
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginright34">
							<strong><spring:message code="cart.review.shoppingCart.total" /></strong>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen">
							<strong class="fontsize30px"><format:price priceData="${cartData.totalPrice}" /></strong>
						</div>
					</div>
				</div>
			</div>

			<%-- <div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div
					class="col-lg-3 col-sm-4 col-md-4 col-xs-12 pull-right btnclsactive marginbtm30">
					<a href="#" class="anchorwhiteText continuetopayment"><spring:message
							code="cart.shipping.ContinuetoPayment" /></a>
					<a href="javascript:;" class="anchorwhiteText text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview" /></a>		
				</div>
			</div> --%>
		</div>
		<!-- Mobile part end -->
	</div>

</div>
