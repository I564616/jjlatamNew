<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="houseCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/house"%>


<template:page pageTitle="${pageTitle}">
	<spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
	
	<c:url value="/home" var="homePageUrl" />
	<c:url value="/cart/checkout" var="checkoutUrl" />
	<input type="hidden" value="${canValidateCart}" id="canValidateCart">	
	<input type="hidden" value="${displayBatchModeAlert}" id="displayBatchModeAlert">
	
	<div class="contentWrapper">
		<div id="content">
			<a id="skip-to-content"></a>
			<!-- breadcrumb : START -->
			<div class="breadCrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<!-- breadcrumb : End -->

			<!-- globalMessages : START -->
			<div id="globalMessages">
				<common:globalMessages />
				<cart:cartRestoration />
				<cart:cartValidation />
			</div>
			<!-- globalMessages : END -->

			<!-- Check for add to cart by codes Errors -->
			<cart:addItemByCodeErrors	cartModificationData="${cartModificationData}" />

			<div class="pageBlock shoppingCartPage stepDeliver1 standard">
				<form:form id="cartStep1Check" action="javascript:;" novalidate="novalidate">				
					<h1><label:message messageCode="cart.review.shoppingCart"/></h1>
					<standardCart:progressBar step1="active" stepbar="1"/>
					<c:if test="${not empty customerExcludedError}">
							<div class="error">
							<p>	<label:message messageCode="cart.common.excluded.customer.error" /></p>
						</div>
					</c:if>
					<houseCart:cartPageHeader currentPage="cartPage" orderType="${cartData.orderType}"/>					
					<standardCart:cartPageActions/>
				</form:form>				
				<commonTags:quickAddToCart />
				
				<div>
			        <label:message messageCode="cart.common.totalWeight"/><b>&nbsp;${cartData.orderWeight}</b><b>&nbsp;${cartData.orderWeightUOM}</b>
					&nbsp;&nbsp;&nbsp;<label:message messageCode="cart.common.totalVolume"/><b>&nbsp;${cartData.orderVolume}</b><b>&nbsp;${cartData.orderVolumeUOM}</b>
				</div>
				
				<!-- Cart Entries-->
				<houseCart:cartEntries />				

				<div class="sectionBlock buttonWrapperWithBG borDer">
					<div class="total">         	
						<p><span><label:message messageCode="cart.common.subTotal"/></span> <span class="jnjID"><format:price priceData="${cartData.totalPrice}" /></span></p>
					</div>		
				</div>
				<standardCart:cartPageActions/>
			</div>				
		</div>
	</div>
	
	
	
	<div class="hide" id="validateOrderDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><label:message messageCode="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock"><label:message messageCode="cart.review.poNumber"/>
				<form:form method="post">
					<div class='popupButtonWrapper txtRight'>
						<c:url value="/cart/validate" var="orderValidateUrl" />
						<span class="floatLeft"><a id="validateOrderCancel"	class="tertiarybtn closePopup" href='#'><label:message messageCode="cart.review.cancel"/></a></span>
						<span><a id="validateOrderOk" href="${orderValidateUrl}" class='secondarybtn'><label:message messageCode="cart.review.validateOrder"/></a></span>
					</div>
				</form:form>
			</div>
		</div>
	</div>	
	<!-- GTR-1693 Starts Here -->
	  
	<!--<div class="hide" id="validateOrderDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><label:message messageCode="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock"><label:message messageCode="cons.cart.review.poNumber"/>
				<form method="post">
					<br>
					<div class='popupButtonWrapper txtRight'>						
						<span class="floatLeft">&nbsp;</span>
						<span><a class="tertiarybtn closePopup" href="javascript:;"><label:message messageCode="po.num.used.alert.close"/></a></span>
					</div>
				</form>
			</div>
		</div>
	</div> -->
  
	<div class="hide" id="validateOrderNumDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><label:message messageCode="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock">
			<font color=red><b><label:message messageCode="cart.controller.poorder.restriction"/></b></font><label:message messageCode="cart.controller.poorder.restriction1"/></br></br>
			<label:message messageCode="cart.controller.poorder.restriction2"/>
					<div class='popupButtonWrapper txtRight'>
						<span class="floatRight"><a id="validateOrderCancel"	class="tertiarybtn closePopup" href='#'><label:message messageCode="cart.review.cancel"/></a></span>
					</div>
			</div>
		</div>
	</div>
	
	<!-- GTR-1693 Ends Here -->
	<!-- Div for display save as Template Pop Up content -->
  	<standardCart:saveAsTemplateDiv/>  	
  	<commonTags:extendedTimeOutDiv/>
	<commonTags:cartErrors/>
	<commonTags:changeAddressDiv/>
</template:page>

<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript"  src="${commonResourcePath}/js/acc.cartAddTocart.js"></script>