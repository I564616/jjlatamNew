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
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<!-- replenish/shippingPage.jsp -->
<template:page pageTitle="${pageTitle}">
	<div class="checkoutshipping">
		<div class="row">
			<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12 col-sm-3 col-md-3 col-lg-3 headingTxt content">
				<spring:message code="cart.shipping.header" />
			</div>
			<div class="btn-group btn-breadcrumb pull-right">
				<ul id="breadcrumbs-one">
					<li><a href="shipping" style="font-weight: bold;">1. <spring:message code="cart.shipping.header" /></a></li>
					<li><a href="#">2. <spring:message code="cart.shipping.review" /></a></li>
				</ul>
			</div>
		</div>
			<!-- KIT Added -->
<c:if test="${not empty orthoKitProductsList}"> 
					<div class="error">
						<p>
							<spring:message code="cart.priceInquiry.orthoKit.msg1" />
							${orthoKitProductsList}
							<spring:message code="cart.priceInquiry.orthoKit.msg2" />
							<spring:message code="cart.priceInquiry.orthoKit.msg3" />
						</p>
					</div>
			 </c:if>
<!-- KIT Added -->
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
							<strong><spring:message code="header.information.accountnumber" /></strong>
							${cartData.b2bUnitId}
						</div>
					</div>
				</div>
			</div>
			<div class="row shipping-address">
				<div
					class="col-lg-12 col-md-12 col-sm-12 col-xs-12 shipmentAddressDetails">
					<div>
						<label><spring:message code="cart.shipping.attention" /> &nbsp; </label> 
						<input type="text" name="attention" id="attention" maxlength="35" class="form-control textboxstyle ${addOnVisibilityCss}" ${disabled} value="${cartData.attention}"></input>
					</div>
					<div class="shipmentAddress margintop30">
						<h5>
							<strong><spring:message code="cart.shipping.ShipToAdd" /></strong>
						</h5>
						<cart:deliveryAddress
							deliveryAddress="${cartData.deliveryAddress}" />
					</div>
				</div>
			</div>


			<div
				class="col-lg-12 col-sm-12 col-md-12 col-xs-12 hidden-xs padding0">
				<div class=" pull-right btnclsactive marginbtm30">
					<%-- <a href="javascript:;" class="anchorwhiteText continuetopayment">12<spring:message
							code="cart.shipping.ContinuetoPayment" /></a> --%>
						<a href="javascript:;" class="anchorwhiteText text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview" /></a>
				</div>
			</div>

		</div>
		<standardCart:checkoutEntries />
		<div class="row hidden-xs">
			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div class="pull-right btnclsactive marginbtm30">
					<%-- <a href="javascript:;" class="anchorwhiteText continuetopayment">13<spring:message
							code="cart.shipping.ContinuetoPayment" /></a> --%>
						<a href="javascript:;" class="anchorwhiteText text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview" /></a>
				</div>
			</div>
		</div>
		<!-- 		MOBILE DEVICE	 -->

		<%-- <div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
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
											<spring:message code="cart.shipping.shipping" />
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
										<p>
											<spring:message code="cart.shipping.quantity" />
										</p>
										<p>${entry.quantity}
										<br />
											<p class="thirdline">
												<spring:message code="product.detail.addToCart.unit" />A
												${entry.product.deliveryUnit}
												(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
											</p></p>
										<P>
											<spring:message code="cart.shipping.unitprice" />
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

			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div
					class="col-lg-3 col-sm-4 col-md-4 col-xs-12 pull-right btnclsactive marginbtm30">
					<a href="#" class="anchorwhiteText continuetopayment"><spring:message
							code="cart.shipping.ContinuetoPayment" /></a>
					<a href="javascript:;" class="anchorwhiteText text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview" /></a>		
				</div>
			</div>
		</div> --%>


	</div>
</template:page>