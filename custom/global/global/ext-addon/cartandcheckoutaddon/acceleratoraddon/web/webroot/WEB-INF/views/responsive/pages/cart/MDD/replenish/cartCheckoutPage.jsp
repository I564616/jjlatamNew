<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/standard"%>
<%@ taglib prefix="replenishCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/replenish"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/common"%>

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
     <cart:progressBar step3="active" stepbar="3"/>
     <!--progressbarNav block ends-->
     <c:if test="${cartData.showCutOffTimeMessage}">
     	<commonTags:moreInfo/>
     </c:if>
  <!-- JJEPIC-213 -->
   
	<!--  <div class="success"><p> <spring:message code="cart.checkout.orderinfo"></spring:message></p></div>-->
    
     <!--Order Submission block starts-->
     
     <replenishCart:validatePageHeader currentPage="cartCheckoutPage"/> 
      
     
        <!--Order Submission block ends-->
        <div class="buttonWrapper sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<cart:checkoutPageActions/>		
         </div>
			<!--Order Derail Row starts-->
            	<replenishCart:checkoutEntries />
            <!--Order Derail Row ends-->    
		     
         <%--  <div class="sectionBlock buttonWrapperWithBG borDer smarLeft">
         	<standardCart:cartTotals/>
         </div> --%>
       <div class="sectionBlock buttonWrapperWithBG continueShopping shopStep2">
			<cart:checkoutPageActions/>			
       </div>
         
  <!-- section1 : END -->  
  	</div>   	 
<cart:saveAsTemplateDiv/>
<commonTags:extendedTimeOutDiv/>
<commonTags:checkoutForm/>
</template:page>
<script type="text/javascript"	src="${commonResourcePath}/js/acc.cartCommon.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCheckoutPage.js"></script>