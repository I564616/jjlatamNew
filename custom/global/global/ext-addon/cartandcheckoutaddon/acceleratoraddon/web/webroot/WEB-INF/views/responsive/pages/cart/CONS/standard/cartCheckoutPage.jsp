<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>


<template:page pageTitle="${pageTitle}">
<spring:theme text="Your Shopping Cart" var="title"	code="cart.page.title" />
<input type="hidden" id="sapCreateOrderFail" value="${sapCreateOrderFail}" />
	<input type="hidden" id="cartPageIndicator" value="true" />	
	<spring:theme code="basket.add.to.cart" var="basketAddToCart" />
	<spring:theme code="cart.page.checkout" var="checkoutText" />

	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>
	
	<div id="globalMessages">
		<common:globalMessages />
		<cart:cartRestoration />
		<cart:cartValidation />
	</div>
	
         
    <div class="pageBlock shoppingCartPage stepDeliver3 standard">
	 <!-- h1 : START -->   
		<h1><spring:message code="cart.common.shoppingCartCheckout"/></h1>        	
  	 <!-- h1 : End -->              
           
      <!-- progressbarNav : START -->  
     <standardCart:progressBar step3="active" stepbar="3"/>
     <!--progressbarNav block ends-->
     <c:if test="${cartData.showCutOffTimeMessage}">
     	<commonTags:moreInfo/>
     </c:if>
     <!--Order Submission block starts-->
    <standardCart:validatePageHeader/>
     
        <!--Order Submission block ends-->
        <div class="buttonWrapper sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<standardCart:checkoutPageActions/>		
         </div>
			<!--Order Derail Row starts-->
            	<cart:checkoutEntries />
            <!--Order Derail Row ends-->    
		     
          <div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
         	<standardCart:cartTotals/>
         </div>
       <div class="sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<standardCart:checkoutPageActions/>			
       </div>
         
  <!-- section1 : END -->  
  	</div>   	 
<standardCart:saveAsTemplateDiv/>
<commonTags:extendedTimeOutDiv/>
<commonTags:checkoutForm/>
</template:page>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCheckoutPage.js"></script>