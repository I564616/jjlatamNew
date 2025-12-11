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
<%-- <%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%> --%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="noCharge" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/noCharge"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<c:url value="/cart/reviseOrder" var="cartUrl"/>

<template:page pageTitle="${pageTitle}">
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
															<%-- <li><a href="paymentContinue"><spring:message code="cart.review.tabsPayment" /></a></li> --%>
															<li><a href="orderReview"><strong><spring:message code="cart.review.noCharge.tabsReview" /></strong></a></li>    
														</ul>
													</div>
												</div>
											</div>
											<div class="row headingTxt content">
												<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderreviewtxt"><spring:message code="cart.review.review"/></div>
												
											</div>
										</div>
									</div>	
                          <!--review start here for address and billing address sub total of the review   -->
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
											<div class="order-column50">
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.noCharge.orderInfo.noChargeReasonCode"></spring:message></P>
											<div class="subtext boldtext"><label:message messageCode="${cartData.reasonCode}" /></div>
											
											</div>
											
											<div class="order-column50">
												
											<c:choose>		
												<c:when test="${not empty cartData.dropShipAccount}">
												<p class="subhead boldtext text-uppercase"><spring:message
													code="cart.common.dropShipAcc" /></P>
													<br><div class="subtext boldtext">${cartData.dropShipAccount}</div>
												</c:when>
											</c:choose>
											</div>
											<div class="order-column-x-100">
												<hr>
											</div>
											
											<div class="order-column50">
											<c:choose>											
												<c:when test="${not empty cartData.attention}">
													<p class="subhead boldtext text-uppercase"><spring:message code="cart.shipping.ImportantInstructions"/> </P>
													 <div class="subtext boldtext">${cartData.attention}</div>												</c:when>
												<c:otherwise>
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.shipping.ImportantInstructions"/> </P>
													<spring:theme code="text.notAvailable" text="NA" />
												</c:otherwise>
												</c:choose>
											</div>
										<div class="order-column-x-100">										
												<hr>											
											</div>
										<div class="order-column-x-100">
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.ShipToAdd" /></p>
											<cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"  companyName="${cartData.b2bUnitName}"/>
											<hr>
											</div>
											
											<div class="order-column-x-100">
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.BillingAdd" /></p>
											<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />	
											<hr>
											</div>
										</div>
										<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 placeorder-btn-holder">
											<div class="float-right-to-none">
										<a href="${cartUrl}">
												<button type="button" class="btn btnclsnormal margintop12px revisebtn"><spring:message code="cart.validate.revisedOrder"></spring:message></button></a>
											<button type="button" class="btn btnclsactive pull-right margintop12px placeOrderBtn"><spring:message code="cart.common.placeOrder"/></button>
											</div>
										</div>										
									</div>
                              <%--       <div class="row table-padding">
										<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
											<p class="subhead boldtext text-uppercase"><spring:message	code="cart.common.purchaseOrder" /></P>
											<div class="subtext boldtext">${cartData.purchaseOrderNumber}</div>
											<hr>
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase">Order Type</P>
												<div class="subtext boldtext">${cartData.orderType}</div>
											</div>
											<div class="order-column50">
												<p class="subhead boldtext text-uppercase">Account Number</P>
												<div class="subtext boldtext">${cartData.b2bUnitId}</div>
											</div>
											<c:if test="${not empty cartData.attention}">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.attention"/> </P>
												</c:if>
											    <div class="subtext boldtext">${cartData.attention}</div><hr>
											    <c:if test="${cartData.orderType eq 'ZNC'}">
											   
											<c:if test="${not empty cartData.reasonCode}">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.reasonCode"/> </P>
												
											    <div class="subtext boldtext">${cartData.reasonCode} : <label:message messageCode="${cartData.reasonCode}" /></div><hr>
											    </c:if>
											    </c:if>
											<c:if test="${not empty cartData.dropShipAccount}">
												<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.dropShipAcc"/> </P>
												
											    <div class="subtext boldtext">${cartData.dropShipAccount}</div><hr>    
											    </c:if>
											    
											    
										 <!-- ship-to address start here -->
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.ShipToAdd" /></p>
											<cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"  companyName="${cartData.b2bUnitName}"/>
												
										<!-- ship - to address ends here -->
											<hr>
											<!-- Billing name and addresss start here -->
											<p class="subhead boldtext text-uppercase"><spring:message code="cart.common.BillingAdd" /></p>
											<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />	
											<br>
											
											<!-- Billing name and addresss end here -->
										</div>
										
										<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 placeorder-btn-holder">
										<div class="float-right-to-none">
										<a href="${cartUrl}">
												<button type="button" class="btn btnclsnormal margintop12px revisebtn">REVISE ORDER</button></a>
											<button type="button" class="btn btnclsactive pull-right margintop12px placeOrderBtn"><spring:message code="cart.common.placeOrder"/></button>
											</div>
										</div>
										</div> --%>
									
						<br><br>
						<noCharge:reviewEntries />
						<br>
						<div class="row validatebtn">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<div class="pull-right float-right-to-none">
		 		<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
	  				<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}"><spring:message code="cart.review.cartPageAction.saveTemplate"/></button>
																		<button type="button" class="btn btnclsactive placeorder placeOrderBtn "><spring:message code="cart.common.placeOrder"/></button>
											</div>
										</div>
						</div>
						
						</div>
<commonTags:checkoutForm/>
<cart:saveAsTemplateDiv/>
</template:page>

