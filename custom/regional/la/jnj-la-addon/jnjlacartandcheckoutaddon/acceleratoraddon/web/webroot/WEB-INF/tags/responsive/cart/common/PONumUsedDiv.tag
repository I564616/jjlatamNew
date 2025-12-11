<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="hide" id="validateOrderDivId">
		<div class="lightboxtemplate" id="okecancel">
			<h2><spring:message code="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock"><spring:message code="cart.review.poNumber"/>
				<form method="post">
					<div class='popupButtonWrapper txtRight'>
						<c:url value="/cart/validate" var="orderValidateUrl" />
						<span class="floatLeft"><a id="validateOrderCancel"	class="tertiarybtn closePopup" href='#'><spring:message code="cart.review.cancel"/></a></span>
						<span><a id="validateOrderOk" href="${orderValidateUrl}" class='secondarybtn'><spring:message code="cart.review.validateOrder"/></a></span>
					</div>
				</form>
			</div>
		</div>
	</div>