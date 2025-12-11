<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="templateLa"
	tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="lacommon"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>


<templateLa:page pageTitle="${pageTitle}">

	<div class="checkoutshipping">

		<div>

			<div class="row">
				<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
			</div>

			<div class="row">
				<div class="col-xs-12 col-sm-3 col-md-3 col-lg-3 headingTxt content">
					<spring:message code="cart.shipping.header" />
				</div>
				<div class="col-xs-12 col-sm-9 col-md-9 col-lg-9">
                        <div class="btn-group pull-right">
                            <ul id="breadcrumbs-one">
                                <li><a href="shipping"><strong>1. Shipping</strong></a></li>
                                <li><a href="paymentContinue">2. Payment</a></li>
                                <li><a href="orderReview">3. Review</a></li>
                            </ul>
                        </div>
                 </div>
			</div>

			<c:if test="${cartData.isContractCart}">
				<lacommon:genericMessage contractNumber="${cartData.contractId}"
					messageCode="cart.common.contract.messageInfo" icon="list-alt"
					panelClass="danger" />
			</c:if>

			<div class="addresspane">
				<div class="row shipping-row-padding">
					<div
						class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ship-address-pane">
						<div class="row">
							<c:choose>
								<c:when test="${splitCart}">

								</c:when>
								<c:otherwise>
									<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
										<strong><spring:message code="cart.common.orderType" /></strong>&nbsp;
										<spring:message
											code="cart.common.orderType.${cartData.orderType}" />
									</div>
								</c:otherwise>
							</c:choose>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 ">
								<strong><spring:message
										code="header.information.accountnumber" /></strong>
								${cartData.b2bUnitId}
							</div>
						</div>
					</div>
				</div>
				<div class="row shipping-address">
					<div
						class="col-lg-12 col-md-12 col-sm-12 col-xs-12 shipmentAddressDetails">
						<div>
							<%-- <label><spring:message code="cart.shipping.attention"/> &nbsp; </label> <input type="text" name="attention" id="attention" class="form-control textboxstyle ${addOnVisibilityCss}" ${disabled} value="${cartData.attention}" ></input> --%>
						</div>
						<div class="shipmentAddress margintop30">
							<h5 style="font-size: 14px !important;">
								<strong><spring:message code="cart.shipping.ShipToAdd" /></strong>
							</h5>
							<cart:deliveryAddress
								deliveryAddress="${cartData.deliveryAddress}" />
						</div>

                        <c:choose>
                           <c:when test="${shippingAddressess.size()>=1}">
						        <div class="shiptoAlternativeAddress">
							        <a href="#" data-bs-toggle="modal" data-bs-target="#selectaddresspopup"><spring:message
									    code="shipping.page.altaddress" /></a>
						        </div>
                           </c:when>
                                <c:otherwise>
                                    <div class="shiptoAlternativeAddress"></div>
                                </c:otherwise>
                        </c:choose>
					</div>
				</div>
			</div>

			<div class="row">
				<div
					class="col-lg-12 col-sm-12 col-md-12 col-xs-12 hidden-xs padding0">
					<div
						class="col-lg-3 col-sm-4 col-md-4 col-xs-12 pull-right btnclsactive marginbtm30">
						<a href="javascript:;" class="anchorwhiteText continuetopayment"><spring:message
								code="cart.shipping.ContinuetoPayment" /></a>
					</div>
				</div>
			</div>
		</div>

		<standardCart:checkoutEntries />


		<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
			<div
				class="col-lg-3 col-sm-4 col-md-4 col-xs-12 pull-right btnclsactive marginbtm30 marginbtm31">
				<a href="javascript:;" class="anchorwhiteText continuetopayment"><spring:message
						code="cart.shipping.ContinuetoPayment" /></a>
			</div>
		</div>

		<!-- 		MOBILE DEVICE	 -->

		<div class="Subcontainer  d-block d-sm-none">
			<table id="ordersTablemobile"
				class="table table-bordered table-striped tabsize  sorting-table bordernone">
				<thead>
					<tr>
						<th class="text-left no-sort"><spring:message
								code="cart.shipping.product" /></th>
						<th class="text-left no-sort"><spring:message
								code="cart.shipping.total" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="status">
						<tr>
							<td class="panel-title text-left valign-top"><a
								data-bs-toggle="collapse" data-bs-parent="#accordion"
								href="#collapse${status.count}"
								class="ref_no toggle-link panel-collapsed skyBlue">
									<table>
										<tbody>
											<tr>
												<td class="padding0"><i class="bi bi-plus skyBlue"></i></td>
												<td class="paddingforText">${entry.product.name}</td>
											</tr>
										</tbody>
									</table>
							</a>
								<div id="collapse${status.count}"
									class="panel-collapse collapse">
									<div class="panel-body details">
										<p>Unit: Case(24 each)</p>
										<p>
											<spring:message code="cart.shipping.RequestedDeliveryDate" />
										</p>
										<div class="form-group selectmethod">
											<select class="form-control" id="sel1">
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
										</div>
										<div class="input-group date-picker-holder">

											<input id="date-picker-mob-${status.count}" 
												name="toDate"
												value="${entry.expectedDeliveryDate}"
												placeholder="Select date"
												class="date-picker form-control date-picker-body requestDeliveryDataPicker"
												type="text"> 
												<label for="date-picker-mob-${status.count}"
													class="input-group-addon btn">
														<span
															class="bi bi-calendar3">
														</span> 
												</label>
										</div>
										<p>
											<spring:message code="cart.shipping.quantity" />
										</p>
										<p>${entry.quantity}</p>
										<P>
											<spring:message code="cart.shipping.unitprice" />
										</P>
										<P>
											<format:price priceData="${entry.basePrice}" />

										</P>
									</div>
								</div></td>
							<td class="shipping-address-padding2 valign-top"><format:price
									priceData="${entry.totalPrice}" /></td>
						</tr>

					</c:forEach>
				</tbody>

			</table>
			<div class="row basecontainer">
				<div class="bgcls1">
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen shipping-address-padding2">
							<strong><format:price priceData="${cartData.subTotal}" /></strong>
						</div>
					</div>
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginright17">
							<strong><spring:message code="cart.shipping.SubTotal" /></strong>
						</div>
					</div>
					
				</div>
				<c:if test="${cartData.totalFreightFees.value > 0}">
				    <div class="bgcls2">
						<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						    <div class="pull-right hypen">
							    <strong><format:price priceData="${cartData.totalFreightFees}" /></strong>
						    </div>
					    </div>
					    <div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						    <div class="pull-right removetxt marginright17">
							    <strong><spring:message code="cart.shipping.shipping" /></strong>
						    </div>
					    </div>
					   
					    <hr class="line pull-right">
				    </div>
				</c:if>
				<c:if test="${cartData.totalFees.value > 0}">
				    <div class="bgcls2">
						<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						    <div class="pull-right hypen">
							    <strong><format:price priceData="${cartData.totalFees}" /></strong>
						    </div>
					    </div>
					    <div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						    <div class="pull-right removetxt marginright17">
							    <strong><spring:message code="cart.total.totalFees" /></strong>
						    </div>
					    </div>
					   
					    <hr class="line pull-right">
				    </div>
				</c:if>
				<c:if test="${cartData.totalTax.value > 0}">
				    <div class="bgcls2">
						<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						    <div class="pull-right hypen">
							    <strong><format:price priceData="${cartData.totalTax}" /></strong>
						    </div>
					    </div>
					    <div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						    <div class="pull-right removetxt marginright17">
							    <strong><spring:message code="order.history.taxes" /></strong>
						    </div>
					    </div>
					   
					    <hr class="line pull-right">
				    </div>
				</c:if>
				<c:if test="${cartData.discountTotal.value > 0}">
					<div class="bgcls2">
						<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						    <div class="pull-right hypen">
							    <strong><format:price priceData="${cartData.discountTotal}"/></strong>
						    </div>
					    </div>
					    <div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						    <div class="pull-right removetxt marginright17">
							    <strong><spring:message code="order.history.discounts"/></strong>
						    </div>
					    </div>
					   
					    <hr class="line pull-right">
				    </div>
				</c:if>
				<div class="bgcls3">
					<div class="col-lg-3 col-md-3 col-sm-3 col-xs-8">
						<div class="pull-right hypen">
							<strong class="fontsize30px"><format:price
									priceData="${cartData.totalGrossPrice}" /></strong>
						</div>
					</div>
					<div class="col-lg-9 col-md-9 col-sm-9 col-xs-4">
						<div class="pull-right removetxt marginrightct18">
							<strong><spring:message code="cart.shipping.total" /></strong>
						</div>
					</div>
					
				</div>
			</div>

			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div
					class="col-lg-3 col-sm-4 col-md-4 col-xs-12 pull-right btnclsactive marginbtm30">
					<a href="#" class="anchorwhiteText continuetopayment"><spring:message
							code="cart.shipping.ContinuetoPayment" /></a>
				</div>
			</div>
		</div>
	</div>




	<!-- MOBILE DEVICE -->


	<div class="col-lg-12 col-md-12" id="selectanaddress1">

		<!-- Modal -->
		<div class="modal fade" id="selectaddresspopup" role="dialog">
			<div class="modal-dialog modalcls">
				<c:url value="/cart/updateShippingAddress" var="updateShippingAdd" />
				<form id="changeAddForm" method="post" action="${updateShippingAdd}">

					<!-- Modal content-->
					<div class="modal-content popup">
						<div class="modal-header">
							<button type="button" class="close clsBtn" data-bs-dismiss="modal">
								<spring:message code="account.change.popup.close" />
							</button>
							<h4 class="modal-title selectTitle">
								<spring:message code="shipping.page.selectaddrs" />
							</h4>
						</div>
						<div class="modal-body">

							<div class="list-group listclass">
								<c:forEach var="shippingAddress" items="${shippingAddressess}"
									varStatus="count">

									<!-- if(odd) -->
									<div class="odd-row">
										<div class="list-group-item-text descTxt" id="test">
											<input type="hidden" name="shippingAddress1"
												value="${shippingAddress.id}" id="shippingAddress1"
												class="shipp">
											<%-- <div class="address-txt">${shippingAddress.companyName}</div> --%>
											<div class="address-txt">${shippingAddress.line1}<%--  ${shippingAddress.line2} --%>
											</div>
											<div class="address-txt">${shippingAddress.town}</div>
											<div class="address-txt">${shippingAddress.postalCode}</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>

						<div class="row ftrcls">
							<spring:message code='shipping.page.changeaddrs'
								var="changeAddress" />

							<input type="button" class="btn btnclsactive pull-right"
								data-bs-dismiss="modal" id="laSubmitChangeAddress"
								value="${changeAddress}">
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--End of Modal pop-up-->
	</div>
</templateLa:page>