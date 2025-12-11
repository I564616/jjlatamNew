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
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/addons/loginaddon/shared/analytics" %>
<%@ taglib prefix="houseCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/house"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<spring:theme text="Your Shopping Cart" var="title"
	code="cart.page.title" />
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
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
	 <div class="success"><p> <span class="strong"><spring:message code="cart.confirmation.thanksMessage"/></span><spring:message code="cart.confirmation.processMessage"/></p></div>    
      <!-- Contract Order msg : END --> 
    <div id="globalMessages">
		<common:globalMessages />
	</div>	
     <!--Order Submission block starts-->
       <houseCart:confirmationPageHeader currentPage="orderConfirmationPage" orderData="${orderData}" orderType="${orderData.orderType}"/>
       
        <!--Order Submission block ends-->
        <div class="sectionBlock buttonWrapperWithBG continueShopping shopStep4">
			<checkout:orderConfirmationButtons orderData="${orderData}" />
		</div>
		<div>
        <spring:message code="cart.common.totalWeight"/><b>&nbsp;${orderData.orderWeight}</b>&nbsp;<b>${orderData.orderWeightUOM}</b>&nbsp;&nbsp;&nbsp;
		<spring:message code="cart.common.totalVolume"/><b>&nbsp;${orderData.orderVolume}</b>&nbsp;<b>${orderData.orderVolumeUOM}</b>
		</div>
       
        <!--Order Derail Row starts-->
		<houseCart:confirmationPageEntries orderData="${orderData}" />
          <div class="sectionBlock buttonWrapperWithBG borDer">
	         <div class="total marLeft224 conTotal">
			<p><span><spring:message code="cart.validate.cartTotal.subTotal"/></span> <span class="jnjID"><format:price priceData="${orderData.totalPrice}" /></span></p>		
			<c:if test="${cartData.totalFees.value ne '0.0'}">
			
			<p><span><spring:message code="cart.validate.cartTotal.tax"/></span> <span class="jnjID"> <format:price priceData="${orderData.totalTax}"/></span></p>
			</c:if>
			<p class="totalSum"><span><spring:message code="cart.validate.cartTotal.totals"/></span> <span class="jnjID"><format:price priceData="${orderData.totalPrice}"/></span></p>
	   		</div>
      	  </div>
		<div class="sectionBlock buttonWrapperWithBG continueShopping shopStep4">
			<checkout:orderConfirmationButtons orderData="${orderData}" />
		</div>
    </div>
</templateLa:page>

<script type="text/javascript"src="${commonResourcePath}/js/acc.cartPage.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/acc.cartCommon.js"></script>

