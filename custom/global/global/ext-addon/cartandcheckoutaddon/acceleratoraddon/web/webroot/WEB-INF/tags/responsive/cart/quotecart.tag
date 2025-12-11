<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="quote" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/quote"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
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

<div class="pageBlock shoppingCartPage stepDeliver1 standard">

	<h2>
		<spring:message code="cart.priceQuote.heading" />
	</h2>
	<c:if test="${sapValidationError || not empty customerExcludedError}">
		<div class="error">
			<p>
			<spring:message code="cart.review.quote.quoteRequestNotValidated" />
			</p>
		</div>
	</c:if>
	<c:if test="${not empty cartEmptyError}">
			<div class="error">
			<p>
					<spring:message code="cart.common.empty.error" />
			</p>
		</div>
	</c:if>
	<c:if  test="${not empty excludedProducts}">
				<div class="error">
						<p>${excludedProducts}</p>
				</div>
	</c:if>
	<quote:cartPageActions saveOrderTemplateRequired="true" />

	<commonTags:quickAddToCart addToCartLabelKey="cart.common.addToQuote"/>

	<!-- Cart Entries-->
	<quote:cartEntries isQuoteResultPage="false" />

	<!--Order Submission block ends-->
	<quote:cartPageActions saveOrderTemplateRequired="false" />
	<!-- section1 : END -->
</div>
<!-- Div for display save as Template Pop Up content -->
<standardCart:saveAsTemplateDiv />
