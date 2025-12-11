<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:url value="/home" var="homePageUrl" />

<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
<!-- standard/cartPageActions.tag -->
<!-- Disable checkout buttons if cart is empty or user does  not have rights of checkout -->
<c:if test="${empty cartData.entries || canCheckout}">						
	<c:set value="linkDisable" var="classForSaveTemplate"/>
	<c:set value="buttonDisable" var="classForValidate"/>					
</c:if>
<div class="sectionBlock buttonWrapperWithBG continueShopping">
<ul>
	<li><a href="${homePageUrl}"><spring:message code="cart.review.cartPageAction.continue"/></a></li>
	<li class="center"><a class="${classForSaveTemplate}" href="javascript:;"><spring:message code="cart.review.cartPageAction.saveTemplate"/></a></li>
	<li class="mar0"><input	class="secondarybtn floatRight cartStep1Saveupdate ${classForValidate}" value="Validate Order" type="button"></li>
</ul>
</div>