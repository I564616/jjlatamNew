<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url value="/checkout/single/placeOrder?skipSAPWS=true" var="placeOrderInHybrisURL" />
<div class="hide" id="createBatchOrderDiv">
		<div class="lightboxtemplate">
			<h2><spring:message code="cart.review.connection.error"/></h2>
			<div class="okecancel ttBlock"><spring:message code="cart.checkout.batchMode.alert"/>
				<form:form method="post">
					<div class='popupButtonWrapper txtRight marTop20'>						
						<span class="floatLeft"><a id="cancelBatchOrder" class="tertiarybtn closePopup" href='javascript:;'><spring:message code="cart.common.cancel"/></a></span>
						<span><a href="${placeOrderInHybrisURL}" class='primarybtn'><spring:message code="cart.common.continue"/></a></span>
					</div>
				</form:form>
			</div>
	</div>
</div>