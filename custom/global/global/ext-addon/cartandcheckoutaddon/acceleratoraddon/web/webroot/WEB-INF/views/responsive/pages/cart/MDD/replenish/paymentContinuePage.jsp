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
<!-- replenish/paymentContinuePage.jsp -->
<template:page pageTitle="${pageTitle}">
<div id="paymentpage">
		  <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row">
			<div class="col-xs-12 col-sm-5 col-md-5 col-lg-5 headingTxt content"><spring:message code="cart.review.Payment" /></div>
			
			<div class="btn-group btn-breadcrumb pull-right">
				 <ul id="breadcrumbs-one" >
					<li><a href="shipping"><spring:message code="cart.review.tabsShipping" /></a></li>
					<li><a href="paymentContinue" style="font-weight: bold;"><spring:message code="cart.review.tabsPayment" /></a></li>
					<li><a href="#"><spring:message code="cart.review.tabsReview" /></a></li>    
				</ul>
			</div>
		</div>
		<div class="Subcontainer boxshadow">
		<div class="addresspane">
		<div class="row shipping-row-padding">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ship-address-pane">	
				<div class="row">
					<c:choose>
						<c:when test="${splitCart}">
						</c:when>
						<c:otherwise>
							<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"><strong><spring:message code="cart.common.orderType" /></strong>&nbsp;<spring:message code="cart.common.orderType.${cartData.orderType}" /> </div>
						</c:otherwise>
					</c:choose>
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 "><strong><spring:message code="header.information.account.number" /></strong> ${cartData.b2bUnitId}</div>							
				</div>
			</div>
		</div>
		</div>
		<!-- Table collapse for mobile device-->
		<table id="ordersTable" class="table table-bordered table-striped tabsize">
			 <div class="panel-body details">																	
				<div><label><spring:message code="cart.payment.purchageorder"/> <span	class="redStar">*</span>&nbsp;</label> 
				<input id="purchOrder" maxlength="35" type="text" class="form-control textboxstyle ${disabled} value="${cartData.purchaseOrderNumber}">
				
				</div>
				<div class="registerError" style="color:red"></div>
				<div class="shipmentAddress margintop30"><h5>
				<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />																		
				</h5></div>
			 </div>
				<div id="accordion" role="tablist" aria-multiselectable="true">
					  <div class="paymentone">
						
						<div id="paymentType1" class="paymentMethod">
							  
						</div>
						<div class="panel-heading payment"  data-num="1">
						  <h4 class="panel-title">
							<a class="ref_no toggle-link-payment">
							  <span><i class="fa fa-circle" aria-hidden="true"></i></span><spring:message code="cart.payment.paywithPurchageOrder"/>
							</a>
						  </h4>
						</div>
					  </div>
					  <div class="paymenttwo">
				<c:set var="purOrderCheckValue" value="checked" />
				<c:set var="credutCardCheckValue" value="" />
				<c:set var="selectCardClass" value="hidden" />
				<input type="hidden" value="${payCreditcard}" id="payCreditcard">
				<c:if test="${cartData.paymentInfo != null}">
					<c:set var="purOrderCheckValue" value="" />
					<c:set var="credutCardCheckValue" value="checked" />
					<c:set var="selectCardClass" value="show" />
				</c:if>
				<div class="panel-heading payment"   data-num="2">
				  <h4 class="panel-title">
						<a class="ref_no toggle-link-payment ">
						 <i class="fa fa-circle-o" aria-hidden="true"></i></span> <spring:message code="cart.payment.PaywithCreditCard"/>
						</a>
					  </h4>
					</div>
					<div id="paymentType2" class="paymentMethod" >
						 <div class="panel-body details">
							<label><spring:message	code="cart.review.orderInfo.selectCard" /> &nbsp;</label>
							<select name="datatab-desktop_length" aria-controls="datatab-desktop" class="selectpicker" tabindex="-98" id="selectCard">
									<option value=""><spring:message	code="cart.review.orderInfo.select" /></option>
									<c:forEach var="creditCardInfo" items="${creditCardsInfos}">
										<c:set var="selectedValue" value="" />								
										<c:if test="${creditCardInfo.id == cartData.paymentInfo.id}">
											<c:set var="selectedValue" value="selected" />
										</c:if>
									<option value="${creditCardInfo.id}" ${selectedValue}>${creditCardInfo.cardType}-${creditCardInfo.cardNumber}-${creditCardInfo.expiryMonth}/${creditCardInfo.expiryYear}</option>
									</c:forEach>
									<c:if test="${selectedValue!='selected' && cartData.paymentInfo ne null}">
									<option value="${cartData.paymentInfo.id}" selected>${cartData.paymentInfo.cardType} -${cartData.paymentInfo.cardNumber} -${cartData.paymentInfo.expiryMonth}/${cartData.paymentInfo.expiryYear}</option>
									</c:if>
								</select> 
							<a type="button" data-toggle="modal" data-target="#editCreditCard" id="editCreditCardDetails"><spring:message code="cart.review.orderInfo.addEditCard"/></a>
						</div>
					</div>
				  </div>
				  <div class="paymentthree">
					<div class="panel-heading payment" data-num="3">
					  <h4 class="panel-title">
						<a class="ref_no toggle-link-payment">
						 <span><i class="fa fa-circle-o" aria-hidden="true"></i></span><spring:message code="payment.other.method"/>
						</a>
					  </h4>
					</div>
					<div id="paymentType3" class="paymentMethod" >
					<br>
				</div>
			  </div>
			</div>
		</table>
		
	</div>	
<!--Accordian Ends here -->

		<div class="row subcontent3">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
				<button type="button" class="btn btnclsactive pull-right text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview"/></button>											
			</div>
		</div>
		<standardCart:paymentCartEntries/>
		
		<div class="row subcontent3">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0">
				<button type="button" class="btn btnclsactive pull-right text-uppercase orderreview continueToreviewBtn"><spring:message code="cart.payment.continuetoreview"/></button>											
			</div>
		</div>
		
		<!-- End - Total Price Summary -->
		<commonTags:creditCartInfo/>
</div>
</template:page>
