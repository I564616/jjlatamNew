<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="replenishCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/replenish"%>
<!-- replenish/cartValidationPage.jsp -->
<template:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
<replenishCart:validateEntries />
<cart:saveAsTemplateDiv/>
</template:page>
<%-- <script type="text/javascript"	src="${commonResourcePath}/js/acc.cartCommon.js"></script> --%>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<%-- <script type="text/javascript" src="${commonResourcePath}/js/acc.cartCheckoutPage.js"></script> --%>



<%-- <template:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
<c:url value="/cart/checkout" var="checkoutUrl" />
<c:url value="/cart" var="cartPageUrl" />
<c:url value="/home" var="homePageUrl" />
	<input type="hidden" id="cartPageIndicator" value="true" />	
	<spring:theme code="basket.add.to.cart" var="basketAddToCart" />
	<spring:theme code="cart.page.checkout" var="checkoutText" />

	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>
	
	<div id="globalMessages">
		<cart:cartRestoration />
		<cart:cartValidation />
	</div>
	
	
	<div class="pageBlock shoppingCartPage stepDeliver2 standard">
	 <!-- h1 : START -->   
		<h1><spring:message code="cart.validate.submitCart" /></h1>        	
  	 <!-- h1 : End -->              
           
     <!-- progressbarNav : START -->  
     <cart:progressBar step2="active" stepbar="2"/>
     <!--progressbarNav block ends-->
     
	 <!-- <div class="success"><p>Your order was successfully validated</p></div> -->
	 
	<common:globalMessages />
     <!--Order Submission block starts-->
     
       	<replenishCart:validatePageHeader currentPage="cartValidationPage"/>
       
        <!--Order Submission block ends-->
        <div class="buttonWrapper sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<standardCart:validatePageActions/>			
        </div>		
   	<!--Order Derail Row starts-->
  			<replenishCart:validateEntries />
    <!--Order Derail Row ends-->            
     
       <div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
      	<standardCart:cartTotals/>
      </div>
      
       <div class="sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<standardCart:validatePageActions/>	
       </div>

  	</div>
  	<commonTags:validateForm/>
  	<!-- Div for display save as Template Pop Up content -->
  	<cart:saveAsTemplateDiv/>
</template:page>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartValidationPage.js"></script> --%>