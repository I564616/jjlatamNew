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
<%@ taglib prefix="consignmentFillUpCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<!-- consignmentFillUp/shippingPage.jsp -->
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
						<input type="text" name="attention" id="attention" class="form-control textboxstyle ${addOnVisibilityCss}" ${disabled} value="${cartData.attention}"></input>
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
		<consignmentFillUpCart:checkoutEntries />
		<div class="row hidden-xs">
			<div class="col-lg-12 col-sm-12 col-md-12 col-xs-12">
				<div class="pull-right btnclsactive marginbtm30">
					<%-- <a href="javascript:;" class="anchorwhiteText continuetopayment">13<spring:message
							code="cart.shipping.ContinuetoPayment" /></a> --%>
						<a href="javascript:;" class="anchorwhiteText text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview" /></a>
				</div>
			</div>
		</div>
	</div>
</template:page>