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
<%@ taglib prefix="deliveredCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url	value="/cart/initiateReplenish" var="initiateReplenishURL" />
<c:url	value="/home" var="homePageURL" />


<template:page pageTitle="${pageTitle}">
	<div id="ordercompletePage" class="orderReviewpage">
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div class="row">
					<div class="col-lg-7 col-md-7">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</div>

				</div>
				<div class="row headingTxt content">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderreviewtxt">
						<spring:message code="cart.review.orderComplete" />
					</div>
					<form:form action="${initiateReplenishURL}" method="post">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						<div class="float-right-to-none">							
								
								<input type="hidden" value="${orderData.code}" name="orderNum">
									<button type="submit" class="btn btnclsactive">
										<spring:message
											code="cart.confirmation.createReplenishmentOrder"></spring:message>
									</button>
														
							<button type="button" class="btn btnclsnormal" onclick="window.location.href='${homePageURL}'">
								<spring:message code="cart.confirmation.done"></spring:message>
							</button>
						</div>
					</div>
					</form:form>	
				</div>
			</div>
		</div>
		<!--review start here for address and billing address sub total of the review   -->
		<c:url value="/order-history/order/${orderData.orderNumber}"
			var="orderDetailUrl" />
			<div class="panel-group">
			<div class="panel panel-success">
				<div class="panel-heading">
					<span><span class="ok-icon glyphicon glyphicon-ok"></span> <strong><spring:message code="cart.confirmation.thanksMessage" /></strong> <spring:message code="cart.confirmation.processMessage" /></span>
				</div>
			</div>
		</div>
		<div class="row table-padding">
			<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
				<c:choose>
					<c:when test="${not empty orderData.purchaseOrderNumber}">
						<div class="order-column-x-50">
							<p class="subhead boldtext text-uppercase">
								<spring:message code="cart.common.purchaseOrder" />
							</P>
							<div class="subtext boldtext">${orderData.purchaseOrderNumber}</div>
						</div>
					</c:when>
					
				</c:choose>

				<c:choose>
					<c:when test="${splitOrder}">
						<div class="subtext boldtext"></div>
						<hr>
					</c:when>
					<c:otherwise>
						<div class="order-column-x-50">
							<p class="subhead boldtext text-uppercase">
								<spring:message code="cart.confirmation.orderNumber" />
							</P>
							<div class="subtext boldtext">
								<a href="${orderDetailUrl}">${orderData.orderNumber}</a>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="order-column-x-100">
					<hr>
				</div>

				<div class="order-column50">
					<p class="subhead boldtext text-uppercase">
						<spring:message code="cart.common.orderType" />
					</P>
					<div class="subtext boldtext">
						<spring:message
							code="cart.common.orderType.${orderData.orderType}" />
					</div>
				</div>
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase">
						<spring:message code="cart.review.account.number" />
					</P>
					<div class="subtext boldtext">${orderData.b2bUnitId}</div>
				</div>
				<div class="order-column-x-100">
					<hr>
				</div>
				<c:if test="${divisionData.isSpine}">
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.spinSalesRepUCN"></spring:message></P>
					<div class="subtext boldtext">${orderData.spineSalesRepUCN}</div>
				</div>
				</c:if>
				<c:if test="${divisionData.isMitek}">
					<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.review.entry.specialSP"></spring:message></P>
					<div class="subtext boldtext">${orderData.spineSalesRepUCN}</div>
				</div>

				</c:if>
				<div class="order-column50">
				
					<p class="subhead boldtext text-uppercase">
						<spring:message code="cart.common.surgeonName"></spring:message>
					</P>
					<div class="subtext boldtext">${orderData.surgeonName}</div>
					<a type="button" href="" data-toggle="modal" id="showSurgeryInfoPopupConfirm"><spring:message 
								code="cart.deliver.surgeryInfo" /></a>
				</div>
				<deliveredCart:viewOrderSurgeryInfoDiv orderData="${orderData}" />
				<div class="order-column-x-100">
					<hr>
				</div>
				<c:if test="${not empty orderData.dropShipAccount}">
					<div class="order-column50">
						<p class="subhead boldtext text-uppercase"><spring:message code="order.detail.shipTo.Acc"></spring:message></P>
						<div class="subtext boldtext">${orderData.dropShipAccount}</div>
					</div>
					<div class="order-column50">
						<p class="subhead boldtext text-uppercase"><spring:message code="order.detail.distributor.po"></spring:message>
							</P>
						<div class="subtext boldtext">${orderData.distributorPONumber}</div>
					</div>
					<div class="order-column-x-100">
						<hr>
					</div>
				</c:if>
				
				
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase">
						<spring:message code="orderDetailPage.orderData.paymentMethod" />
					</P>
					<div class="subtext boldtext">${orderData.paymentType.displayName}</div>
				</div>
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase">
						<spring:message code="orderconfirmationPage.orderData.status" />
					</P>
					<div class="subtext boldtext">${orderData.statusDisplay}</div>
				</div>
				<div class="order-column-x-100">
					<hr>
				</div>
				<c:choose>
				<c:when test="${not empty orderData.attention}">
					<div class="order-column50">
						<p class="subhead boldtext text-uppercase">
							<spring:message code="cart.common.attention" />
						</P>
						<div class="subtext boldtext">${orderData.attention}</div>
					</div>
				</c:when>
				<c:otherwise>
				<p class="subhead boldtext text-uppercase">
							<spring:message code="cart.common.attention" />
						</P>
					<spring:theme code="text.notAvailable" text="NA" />
				</c:otherwise>
				</c:choose>
				<div class="order-column-x-100">
					<hr>
				</div>
				<!-- ship-to address start here -->
				<p class="subhead boldtext text-uppercase">
					<spring:message code="cart.common.ShipToAdd" />
				</p>
				<cart:deliveryAddress deliveryAddress="${orderData.deliveryAddress}"
					companyName="${orderData.b2bUnitName}" />

				<!-- ship - to address ends here -->
				<hr>
				<!-- Billing name and addresss start here -->
				<p class="subhead boldtext text-uppercase">
					<spring:message code="cart.common.BillingAdd" />
				</p>
				<cart:deliveryAddress deliveryAddress="${orderData.billingAddress}"
					companyName="${orderData.b2bUnitName}" />

				<!-- Billing name and addresss end here -->
			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 pull-right">
				<!-- Start - Total Price Summary -->
				<div class="row basecontainer2">
					<table class="total-summary-table">
						<tr class="summary-bline">
							<td class="total-summary-label text-uppercase"><spring:message
									code="cart.shipping.SubTotal"></spring:message></td>
							<td class="total-summary-cost totalrps"><format:price
									priceData="${orderData.subTotal}" /></td>
						</tr>
						<tr class="total-price-row">
							<td class="total-summary-label text-uppercase"><spring:message
									code="cart.validate.cartTotal.orderTotal"></spring:message></td>
							<td class="total-summary-cost totalrps"><format:price
									priceData="${orderData.totalPrice}" /></td>
						</tr>
					</table>
				</div>
				<!-- End - Total Price Summary -->
			</div>

		</div>



	</div>

	<br>
	<br>

	<deliveredCart:orderEntries />

</template:page>

