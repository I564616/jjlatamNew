<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/quote"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:url	value="/checkout/single/orderConfirmationDownload/${sapOrderNumber}" var="priceInquiryConfirmationDownload" />
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<!-- breadcrumb : START -->
<div class="row jnj-body-padding" id="jnj-body-content">
	<div class="col-lg-12 col-md-12">
		<div id="priceInquiryConfirmation">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
</div>
<!-- breadcrumb : End -->
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<!-- globalMessages : START -->
<div id="globalMessages">
	<common:globalMessages />
	<cart:cartRestoration />
	<cart:cartValidation />
</div>
<!-- globalMessages : END -->

<!-- Check for add to cart by codes Errors -->
		<cart:addItemByCodeErrors
			cartModificationData="${cartModificationData}" />
		<div class="row pi-title">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<spring:message code="cart.priceQuote.resultPage.heading" />
			</div>
		</div>
		<div class="panel-group">
			<div class="panel panel-success">
				<div class="panel-heading">
					<spring:message code="cart.priceQuote.resultPage.noteLine1" /><br/>
					<fmt:formatDate value="${cartData.quoteEndDate}"
						pattern="${dateformat}" var="quoteExpirationDate" />
					<spring:message code="cart.priceQuote.resultPage.noteLine2"
						arguments="${quoteExpirationDate}" />
						</div>
			</div>
		</div>
	<!-- KIT Added -->
<c:if test="${not empty orthoKitProductsList}"> 
					<div class="error">
						<p>
							<spring:message code="cart.priceInquiry.orthoKit.msg1" />
							${orthoKitProductsList}
							<spring:message code="cart.priceInquiry.orthoKit.msg2" />
							<spring:message code="cart.priceInquiry.orthoKit.msg3" />
						</p>
					</div>
			 </c:if>
<!-- KIT Added -->
	<quote:quoteResultPageActions hybrisOrderNumber="${sapOrderNumber}"/>

		<div class="mainbody-container">
		
			<div class="row no-margin-row prd-quantity-panel">
											<div class="col-md-12">
												<div class="col-lg-6 col-sm-6 col-md-6 col-xs-12 prd-quantity-field"><spring:message code="cart.priceQuote.resultPage.priceQuoteNumber" /> ${sapOrderNumber}</div>
												<div class="col-lg-6 col-sm-6 col-md-6 col-xs-12">
													<div class="pull-right download-links-area">
														<strong><spring:message code="cart.confirmation.download" /></strong><a href="${priceInquiryConfirmationDownload}?downloadType=PDF" class="links-margin"><spring:message code="cart.confirmation.pdf"/></a>
													</div>
		</div>
	</div>
										</div>

	<c:if test="${not empty excludedProducts}">
					<div class="error">
						<p>${excludedProducts}</p>
					</div>
	</c:if>
	<!-- Cart Entries-->
			<quote:checkoutEntries isQuoteResultPage="true" />
			
			
				<div class="row basecontainer">
										
												<table class="total-summary-table">
													<tr>
														<td class="total-summary-label"><spring:message code="cart.shipping.SubTotal" /></td>
														<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td>
													</tr>
													<tr class="summary-bline">
														<td class="total-summary-label"><spring:message code="orderDetailPage.orderData.Shipping" /></td>
														<td class="total-summary-cost">--</td>
													</tr>
													<tr class="total-price-row">
														<td class="total-summary-label"><spring:message code="cart.validate.cartTotal.inquiryTotal"/></td>
														<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${cartData.totalPrice}" /></td>
													</tr>
												</table>
												
										</div>
										
</div>
<div class="panel-group" id="pi-bottom-message">
										<div class="panel panel-success">
											<div class="panel-heading">

<spring:message code="cart.disclaimar.PriceInquirytext"/> 
	</div>
	</div>  
</div>

			 <quote:quoteResultPageActions hybrisOrderNumber="${sapOrderNumber}" /> 
<!-- Div for display save as Template Pop Up content -->
<div id="SaveAsTemplateHolder"></div>
				<cart:saveAsTemplateDiv orderId="${sapOrderNumber}"/>