<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url value="/home" var="homePageUrl" />
<c:url value="/cart/requestQuote" var="requestQuoteUrl" />
<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
<%@ attribute name="saveOrderTemplateRequired" required="false" type="java.lang.Boolean" %>
<!-- Disable checkout buttons if cart is empty or user does  not have rights of checkout -->
<%-- <c:if test="${empty cartData.entries && user.jnjSiteName eq 'CONS'}">						
	<c:set value="buttonDisable" var="classForValidate"/>					
</c:if> --%>
<c:if test="${empty cartData.entries}">						
	<c:set value="linkDisable" var="classForSaveTemplate"/>					
</c:if>

<div class="buttonWrapper sectionBlock buttonWrapperWithBG continueShopping">
<ul>
	<li>&nbsp;</li>
	<c:if test="${saveOrderTemplateRequired eq 'true'}">
	<li class="center"><a class="${classForSaveTemplate}" href="javascript:;"><spring:message code="cart.review.cartPageAction.saveTemplate"/></a></li>
	</c:if>
	<c:if test="${saveOrderTemplateRequired eq 'false'}">
	<li class="center">&nbsp;</li>
	</c:if>
	<li><a href="${requestQuoteUrl}" class="secondarybtn floatRight requestPriceQuoteBtn ${classForValidate}"><spring:message code="cart.priceQuote.getPriceQuote"/></a></li>
</ul>
</div>