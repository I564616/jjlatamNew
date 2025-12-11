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
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<c:url value="/cart/reviseOrder" var="cartUrl"/>

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
												<div class="col-lg-5 col-md-5">
													<div class="btn-group pull-right">
														<ul id="breadcrumbs-one">
															<li><a href="shipping"><spring:message code="cart.review.tabsShipping" /></a></li>
															<li><a href="paymentContinue"><spring:message code="cart.review.tabsPayment" /></a></li>
															<li><a href="orderReview"><strong><spring:message code="cart.review.tabsReview" /></strong></a></li>    
														</ul>
													</div>
												</div>
											</div>
										     <div class="row headingTxt content">
												<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderreviewtxt"><spring:message code="cart.review.review"/></div>
												
											</div>
											
												
										</div>
									</div>	
									
									<div class="row table-padding">
									
										<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
											<p class="subhead boldtext text-uppercase"><spring:message	code="cart.common.purchaseOrder" /></P>
											<div class="subtext boldtext">${cartData.purchaseOrderNumber}</div>
											<hr>
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.orderType" /></P>
												<div class="subtext boldtext"><spring:message code="cart.common.orderType.${cartData.orderType}" /></div>
											</div>
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.review.account.number" /></P>
												<div class="subtext boldtext">${cartData.b2bUnitId}</div>
											</div>
											<div class="order-column-x-100">
												<hr>
											</div>
											<c:if test="${not empty cartData.expectedShipDate}">
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase"><!-- Requested Delivery Date --><spring:message code="cart.common.requestedDeliveryDate"/></P>
												<!--  AAOL-6138 changes start date format changes -->
												<div class="subtext boldtext">
												<fmt:formatDate value="${cartData.expectedShipDate}" pattern="${dateformat}" />
												</div>
												<!--  AAOL-6138 changes end -->
											</div>
											</c:if>
											<c:if test="${not empty cartData.specialText}">
											<div class="order-column50">												
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.specialInstructions"/></P>
												<div class="subtext boldtext">${cartData.specialText}</div>
											</div></c:if>
											<c:if test="${not empty cartData.expectedShipDate || not empty cartData.specialText }">
											<div class="order-column-x-100">
												<hr>
											</div>
											</c:if>
											 <c:if test="${not empty cartData.attention}">
											<div class="order-column50">
												<c:choose>											
											<c:when test="${not empty cartData.attention}">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.attention"/> </P>
												 <div class="subtext boldtext">${cartData.attention}</div>
												</c:when>
												<c:otherwise>
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.attention"/> </P>
													<spring:theme code="text.notAvailable" text="NA" />
												</c:otherwise>
												</c:choose>
											</div>
											</c:if>
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase">Payment method</P>
												<div class="subtext boldtext">${cartData.paymentType.displayName}</div>
											</div>
											<div class="order-column-x-100">
												<hr>
											</div>
											
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.ShipToAdd" /></p>
											<div class="subtext boldtext"><cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"  companyName="${cartData.b2bUnitName}"/></div>
											
											<hr>
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.BillingAdd" /></p>
											<div class="subtext boldtext"><cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />
											</div>
											<br>
											
										</div>
										<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 placeorder-btn-holder">
											<button type="button" class="btn btnclsactive pull-right margintop12px placeOrderBtn"><spring:message code="cart.common.placeOrder"/>
											<a href="${cartUrl}"><button type="button" class="btn btnclsnormal pull-right margintop12px revisebtn">REVISE ORDER</button></a>
										</div>										
									</div>
									<br><br>
                       
									
						<br><br>
						 <standardCart:reviewEntries />
						<div class="row bottomrightbuttons">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<div class="float-right-to-none">
												<c:set value="saveorderastemplate" var="classForSaveTemplate" />
												<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate} btn-gap-desk">
													<spring:message code="cart.review.cartPageAction.saveTemplate" />
												</button>
												<button type="button" class="btn btnclsactive  placeOrderBtn"><spring:message code="cart.common.placeOrder"/></button>
											</div>
										</div>
						</div>
						
						</div>
<commonTags:checkoutForm/>
<cart:saveAsTemplateDiv/>
</template:page>

