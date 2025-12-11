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
<cart:addItemByCodeErrors cartModificationData="${cartModificationData}" />

<div class="pageBlock shoppingCartPage stepDeliver1 standard">
	<h1>
		<spring:message code="cart.priceQuote.resultPage.heading" />
	</h1>
	<p>
		<spring:message code="cart.priceQuote.resultPage.noteLine1" />
	</p>
	<p>
		<fmt:formatDate value="${cartData.quoteEndDate}" pattern="MM/dd/yyyy" var="quoteExpirationDate"/>
		<spring:message code="cart.priceQuote.resultPage.noteLine2" arguments="${quoteExpirationDate}" />
	</p>
	<quote:quoteResultPageActions />

	<div class="sectionBlock addToCart">
		<div class="floatLeft">
			<p>
				<spring:message
					code="cart.priceQuote.resultPage.priceQuoteNumber" />
					<span class="textBlack">${sapOrderNumber}</span>
			</p>
		</div>
	</div>
	<c:if test="${not empty excludedProducts}">
					<div class="error">
						<p>${excludedProducts}</p>
					</div>
	</c:if>
	<!-- Cart Entries-->
	<quote:cartEntries isQuoteResultPage="true" />
	<quote:quoteResultPageActions />
</div>

<!-- Div for display save as Template Pop Up content -->
<standardCart:saveAsTemplateDiv />
