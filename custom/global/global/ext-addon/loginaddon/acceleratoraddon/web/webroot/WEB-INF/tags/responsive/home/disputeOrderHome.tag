<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!-- AAOL-3641 -->
<div class="modal fade jnj-popup-container" id="dispute-order" role="dialog" data-firstLogin='true'>
			<div class="modal-dialog modalcls">
				<div class="modal-content popup">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close" /></button>
					   <h4 class="modal-title selectTitle"><spring:message code="orderDetailPage.orderData.disputeorder" /></h4>
					</div>
					<div class="modal-body">												
						<div class="table-row">
							<label class="table-cell order-number-label"><spring:message code="disputeOrderHome.orderData.orderNumber" /></label>
							<div class="table-cell order-number-txt">
								<input type="text" id="orderCode" class="form-control"/>
								<div class="registerError" id="orderCodeInvalidMsg" style="display:none;">
									<label class="error"><spring:message code="dispute.order.validation.message" /></label>
								</div>
							</div>
						</div>
					</div>	
														
					<div class="modal-footer modal-footer-style">
						<button type="button" class="btn btnclsactive" id="disputeOrderPopup"><spring:message code="disputeOrderHome.orderData.nextButton" /></button>
					</div>
				</div>
			</div>
</div>
<!-- AAOL-3641 -->