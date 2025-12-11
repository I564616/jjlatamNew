<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>



<div class="modal fade jnj-popup-container" id="updatePoNumberPopup"
	class="updatePoNumberPopup" role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup updatePo">
			<div class="modal-header">
			<a href="" type="button" class="close clsBtn" data-dismiss="modal" style="font-weight:normal"><spring:message code="popup.close"></spring:message></a>

				<h4 class="modal-title selectTitle">
					<spring:message
						code="orderHistoryUpdatePopUp.updatePurchaseOrderNumber"></spring:message>
				</h4>
			</div>
			<div class="modal-body">
				<div class="table-row">
					<label class="table-cell order-number-label"><spring:message
							code="orderHistoryUpdatePopUp.enterPONumber" /><span
						class="redStar">*</span>&nbsp; </label>
					<div class="table-cell order-number-txt">
						<input id="poNumber" type="text" class="form-control" data-msg-required="<spring:message code="cart.required.field"/>"></input>
						<div class="registerError"></div> 
					</div>
				</div>
			</div>
			<div class="modal-footer modal-footer-style">
				<a href="#" class="cancel pull-left" data-dismiss="modal"><spring:message
						code="cart.common.cancel" /></a>
				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-dismiss="modal" id="submitPoNumber">
					<spring:message code="cart.common.ok" />
				</button>
			</div>
		</div>
	</div>
<c:set var="reqErrorMsg"><spring:message code="cart.required.field"/></c:set>
<input id="thirdPartyErrorMsg" type="hidden" value="${reqErrorMsg}"/>
</div>
<!-- Update PO Success message -->

<div class="modal fade jnj-popup-container" id="success-dialogPopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup ">
			<div class="modal-header"></div>
			<div class="modal-body">
				<div class="panel-group">
					<div class="panel panel-success">
						<div class="panel-heading">
							<h4 class="panel-title">
								<span><span class="glyphicon glyphicon-ok"></span> <spring:message
										code="order.updatePo.success"></spring:message></span>
							</h4>

						</div>

					</div>

				</div>
			</div>
			<div class="modal-footer modal-footer-style">

				<button type="button" class="btn btnclsactive pull-right"
					data-dismiss="modal" id="dialog-ok">
					<spring:message code="cart.common.ok" />
				</button>
			</div>

		</div>
	</div>
</div>



