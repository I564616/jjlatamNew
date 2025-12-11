<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ attribute name="orderData" required="true"
	type="com.jnj.facades.data.JnjGTOrderData"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/reponsive/cart/standard"%>

<c:url	value="/checkout/single/downloadOrderConfirmation/${orderData.code}" var="orderConfirmationDownload" />
<c:url	value="/home" var="homePageURL" />
		
<ul>
	<li><spring:message code="cart.confirmation.download"/><a class="tertiarybtn excel" href="${orderConfirmationDownload}?downloadType=EXCEL"><spring:message code="cart.confirmation.excel"/></a> <a class="tertiarybtn pdf" href="${orderConfirmationDownload}?downloadType=PDF"><spring:message code="cart.confirmation.pdf"/></a></li>
	<li ><a class="saveorderastemplate" href="javaScript:;"><spring:message code="cart.review.cartPageAction.saveTemplate"/></a></li>
	<li> <a class="secondarybtn floatRight" href="${homePageURL}"><spring:message code="cart.confirmation.done"/></a> </li>
</ul>

<!-- Div for display save as Template Pop Up content -->
<standardCart:saveAsTemplateDiv orderId="${orderData.code}"/> 			
		
