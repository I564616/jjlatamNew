<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/cart/validate" var="cartValidateURL" />
<c:url value="/checkout/single/placeOrder/batch" var="batchOrderURL" />

<div class="hide" id="extendTimeOutDiv">
		<div class="lightboxtemplate">
			<h2><spring:message code="cart.pricing.availability.heading"/></h2>
			<div class="okecancel ttBlock"><spring:message code="cart.pricing.availability.message"/>
				<form method="post">
					<div class='popupButtonWrapper' style="display: inline-block;">
						<ul class="floatLeft batchModePopup">						
							<li>
								<input type="button" id="submitBatchOrder"  class='primarybtn' value='<spring:message code="cart.common.submit.now"/>' />
								<div><spring:message code="cart.pricing.submitNow.desc"/></div>
							</li>
							<li class="marTop20" id="extendedValidationBlock">
								<input type="button" id="extendedValidation" class='secondarybtn' value='<spring:message code="cart.common.extended.time"/>' />
								<div style="line-height: 14px;"><spring:message code="cart.pricing.continueProcessing.desc"/></div>
							</li>
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