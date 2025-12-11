<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="hide" id="skipValidationDiv">
		<div class="lightboxtemplate">
			<h2><spring:message code="cart.review.validateOrder"/></h2>
			<div class="okecancel ttBlock"><spring:message code="cart.review.skipValidation.alert"/>
				<form:form id= "skipValidationForm" method="post" action="cart/returnConfirmationPage">
					<div class='popupButtonWrapper txtRight'>
						<c:url value="/cart/validate" var="orderValidateUrl" />
						<input type="hidden" name = "skipSAPValidation" value="true">
						<span class="floatLeft"><a id="skipValidatonCancel"	class="tertiarybtn closePopup" href='javascript:;'><spring:message code="cart.common.no"/></a></span>
						<span><a id="skipValidatonOK" href="javascript:;" class='secondarybtn'><spring:message code="cart.common.yes"/></a></span>
					</div>
				</form:form>
			</div>
	</div>
</div>