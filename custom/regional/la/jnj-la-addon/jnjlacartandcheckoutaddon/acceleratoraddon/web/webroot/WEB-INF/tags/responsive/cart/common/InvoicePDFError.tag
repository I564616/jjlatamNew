<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/cart/validate" var="cartValidateURL" />
<c:url value="/checkout/single/placeOrder/batch" var="batchOrderURL" />

<div class="hide" id="InvoicePDFError">
		<div class="lightboxtemplate">
			<div class="okecancel ttBlock"><spring:message code="${param.errorMessage}"/>
				<form method="post">
					<div class='popupButtonWrapper' style="display: inline-block;">
						<ul class="floatLeft batchModePopup">						
							<li class="marTop20">
								<input type="button" id="skipValidatonCancel" class="tertiarybtn closePopup" value='<spring:message code="cart.common.try.later"/>' />
								<div><spring:message code="cart.pricing.tryAgain.desc"/></div>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</div>
	
	<form action="${cartValidateURL}" id="extendedValidationForm" method="post">
		<input type="hidden" value="true" name="timeOutExtended">
	</form>
	<form action="${batchOrderURL}" id="createBatchOrderForm" method="post">
	</form>
	
</div>