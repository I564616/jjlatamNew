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
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:url value="/home" var="homePageURL" />

<template:page pageTitle="${pageTitle}">
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
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
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						<div class="float-right-to-none">
						<button type="button" class="btn btnclsnormal" onclick="window.location.href='${homePageURL}'">
								<spring:message code="cart.confirmation.done"></spring:message>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--review start here for address and billing address sub total of the review   -->
	<c:url value="/order-history/order/${orderData.orderNumber}"	var="orderDetailUrl" />
		<div class="panel-group">
			<div class="panel panel-success">
				<div class="panel-heading">
					<span><span class="ok-icon glyphicon glyphicon-ok"></span>
					<strong><spring:message code="cart.confirmation.thanksMessage" /></strong><spring:message code="cart.confirmation.processMessage" /></span>
				</div>
			</div>
		</div>
		<div class="row table-padding">
			<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.purchaseOrder" /></P>
					<div class="subtext boldtext">${orderData.purchaseOrderNumber}</div>
				</div>
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
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.orderType"/></P>
					<div class="subtext boldtext"><spring:message	code="cart.common.orderType.${orderData.orderType}" /></div>
				</div>
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.review.account.number" /></P>
					<div class="subtext boldtext">${orderData.b2bUnitId}</div>
				</div>
				<div class="order-column-x-100">
					<hr>
				</div>
               <c:if test="${not empty orderData.requestedDeliveryDate}">
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.requestDeliveryDate"/></P>
					<div class="subtext boldtext">
					<!--  AAOL-6138 changes start date format changes -->
					
					<fmt:formatDate value="${orderData.requestedDeliveryDate}" pattern="${dateformat}" />
					<!--  AAOL-6138 changes end -->
					</div>
				</div>
				<!-- <div class="order-column50">
					<p class="subhead boldtext text-uppercase">SPECIAL INSTRUCTIONS</P>
					<div class="subtext boldtext">Instructions</div>
				</div> -->
				<div class="order-column-x-100">
					<hr>
				</div>
                    </c:if>
                    <c:choose>
	                    <c:when test="${not empty orderData.attention}">
							<div class="order-column50">
								<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.attention" /></P>
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
				<div class="order-column50">
					<p class="subhead boldtext text-uppercase"><spring:message code="orderconfirmationPage.orderData.status" /></P>
					<div class="subtext boldtext">${orderData.statusDisplay}</div>
				</div>


				<div class="order-column-x-100">
					<hr>
				</div>
				<p class="subhead boldtext text-uppercase"><spring:message code="orderDetailPage.orderData.paymentMethod" /></P>
				<div class="subtext boldtext">${orderData.paymentType.displayName}</div>
				<hr>
				<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.ShipToAdd" /></p>
				<div class="subtext boldtext"><cart:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" companyName="${orderData.b2bUnitName}" />
				</div>

				<hr>
				<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.BillingAdd" /></p>
				<div class="subtext boldtext"><cart:deliveryAddress deliveryAddress="${orderData.billingAddress}"
												companyName="${orderData.b2bUnitName}" /></div>
				<br>

			</div>
			<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 pull-right">
				<!-- Start - Total Price Summary -->
				<div class="row basecontainer2">
					<table class="total-summary-table">
						<tr class="summary-bline">
							<td class="total-summary-label text-uppercase"><spring:message code="cart.validate.cartTotal.subTotal" /></td>
							<td class="total-summary-cost totalrps"><format:price priceData="${orderData.subTotal}" /></td>
						</tr>
						<tr class="total-price-row">
							<td class="total-summary-label text-uppercase"><spring:message code="cart.validate.cartTotal.orderTotal" /></td>
							<td class="total-summary-cost totalrps"><format:price priceData="${orderData.totalPrice}" /></td>
						</tr>
					</table>
				</div>
				<!-- End - Total Price Summary -->
			</div>
                                 
		</div>


		<c:url	value="/checkout/single/downloadOrderConfirmation/${orderData.code}" var="orderConfirmationDownload" />
		<div class="row replishorder-row" style="padding: 20px 0;">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"></div>
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 replishment-download-link"	style="margin-top: 10px;">
				<span class="pull-right"><span class="link-txt boldtext">
				<spring:message	code="cart.confirmation.download" /></span>
				 <a class="tertiarybtn excel" href="${orderConfirmationDownload}?downloadType=EXCEL">
				 <spring:message code="cart.confirmation.excel" /></a> | <a class="tertiarybtn pdf"	href="${orderConfirmationDownload}?downloadType=PDF">
				 <spring:message code="cart.confirmation.pdf" /></a>
				  </span>
			</div>
		</div>
		<standardCart:orderEntries />
		<div class="row bottomrightbuttons">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				
				<div class="float-right-to-none">
				<c:set value="saveorderastemplate" var="classForSaveTemplate" />
					<button type="button"
						class="btn btnclsnormal templatebtn ${classForSaveTemplate} btn-gap-desk">
						<spring:message code="cart.review.cartPageAction.saveTemplate" />
					</button>

				</div>
			
												
			</div>
		</div>
	</div>
 <cart:saveAsTemplateDiv/>
</template:page>

