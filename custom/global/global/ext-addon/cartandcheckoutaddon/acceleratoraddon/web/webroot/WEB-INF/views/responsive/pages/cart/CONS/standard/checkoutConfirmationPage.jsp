<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/checkout" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/addons/loginaddon/shared/analytics" %>


<template:page pageTitle="${pageTitle}">
	<spring:theme text="Your Shopping Cart" var="title"
	code="cart.page.title" />
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>
	
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<analytics:orderConfirmationAnalytics/>
	
	 <div class="pageBlock shoppingCartPage stepDeliver4">
	 <!-- h1 : START -->   
		<h1><spring:message code="cart.confirmation.shoppingCart"/></h1>        	
  	 <!-- h1 : End -->              
           
     <!-- progressbarNav : START -->  
     <standardCart:progressBar step4="active" stepbar="4"/>
     <!--progressbarNav block ends-->
     <!-- Contract Order msg : START -->
     <div class="success">
	 	<p>
	 		<span class="strong cartTabbedError">
	 			<spring:message code="cart.confirmation.thanksMessage"/>
	 		</span>
	 		<c:if test="${orderData.containsOCDProduct}">
	 		<span class="cartTabbedError">
	 			<spring:message code="ocd.cart.true"/>
	 		</span>
	 		</c:if>
	 		<c:if test="${orderData.showCreditHold}">
			<span class="cartTabbedError">
	 			<spring:message code="cart.confirmation.creditHoldMessage"/>
	 		</span>
	 		</c:if>
	 		<span class="cartTabbedError">
	 			<spring:message code="cart.confirmation.processMessage"/>
	 		</span>
	 	</p>
	 </div> 
      <!-- Contract Order msg : END --> 
    
     <!--Order Submission block starts-->
       <standardCart:confirmationPageHeader currentPage="orderConfirmationPage" orderData="${orderData}"/>
       
        <!--Order Submission block ends-->
        <div class="sectionBlock buttonWrapperWithBG continueShopping shopStep4">
			<checkout:orderConfirmationButtons orderData="${orderData}" />
		</div>
       
        <!--Order Derail Row starts-->
		<checkout:orderEntries orderData="${orderData}"/>
      	 <div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
       		 <checkout:orderTotals/>
      	</div>
		<div class="sectionBlock buttonWrapperWithBG continueShopping shopStep4">
			<checkout:orderConfirmationButtons orderData="${orderData}" />
		</div>
    </div>
</template:page>

<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
