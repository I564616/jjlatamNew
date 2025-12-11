<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/responsive/form" %>

<script type="text/javascript"> // set vars
	var cartDataquoteAllowed = ${cartData.quoteAllowed};
	var negotiateQuote = ${placeOrderForm.negotiateQuote};
</script>

<div class="span-20 last" id="negotiate-quote-div" style="display:none">
		<div class="item_container_holder">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2><spring:theme code="checkout.summary.negotiateQuote.quoteReason"/></h2>
			</div>
			<div class="item_container">
				<form:textarea cssClass="text" id="quoteRequestDescription" path="quoteRequestDescription" />
			</div>
		</div>
		<div class="item_container_cancel_placeorder">
			<form:input type="hidden" class="negotiateQuoteClass" path="negotiateQuote"/>
			<button class="positive right pad_right negotiateQuote" id="negotiateQuoteButton">
				<spring:theme code="checkout.summary.negotiateQuote.proceed"/>
			</button>

		
					<a href="javascript: ;" class="edit_complete change_address_button" id="cancel-quote-negotiation"><spring:theme code="checkout.summary.negotiateQuote.cancel"/></a>
			
		</div>
</div>