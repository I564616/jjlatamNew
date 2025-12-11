<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/addQuoteToCart" var="addQuoteToCartURL" />
<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
<%@ attribute name="hybrisOrderNumber" required="true" type="java.lang.String"%>

<!-- Disable checkout buttons if cart is empty or user does  not have rights of checkout -->
<c:if test="${empty cartData.entries || !canCheckout}">						
	<c:set value="linkDisable" var="classForSaveTemplate"/>
	<c:set value="buttonDisable" var="classForValidate"/>					
</c:if>
<div class="row emeo-btn-holder">
<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<button type="button" class="btn btnclsactive mobile-view-btn ${classForSaveTemplate} saveorderastemplate">
				<spring:message code="cart.priceQuote.resultPage.savePriceQuoteAsTemplate"/></button>
</div>	
	<input type="hidden" id="hybrisOrderNumber" value="${hybrisOrderNumber}" />
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="float-right-to-none">
			
	<a href="${homePageUrl}"  type="button" class="secondarybtn cartStep1Saveupdate btn btnclsnormal emeo-btn-done-gap mobile-view-btn">
	<spring:message code="cart.priceQuote.resultPage.quoteResultPageAction.done"/></a>	 		
			
			<c:if test="${canCheckout}">
			<button type="button" class="btn btnclsactive mobile-view-btn primarybtn addToCartPriceQuote ${classForValidate} marLeft10" >
			<spring:message code="cart.priceQuote.resultPage.quoteResultPageAction.addAllToCart"/></button>
			</c:if>
		</div>
	</div>	
	
      
<form:form id="addQuoteToCartForm" action="${addQuoteToCartURL}"></form:form>
</div>