<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal forgotPopup -->
<form:form id='emailVerificationFailed' action="javascript:;" method='post' style="display:none;">
	<!-- Modal content-->
	<div class="modal-content" id='emailVerificationFailedP1'>
		<div class="modal-header">
			<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='la.popup.close'/></button>
			<h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword"/></h4>
		</div>
		<div class="modal-body row">						
			<div class="col-xs-10">
				<p><spring:message code="password.forgotPassword.invalidFail"/></p>
				<div class="form-group">
					<label ><spring:message code="password.forgotPassword.email"/></label>
					<!-- <input type="text" class="form-control padding0" id="global-search-txt" > -->
					<input type='email' id='failedUserName' class='required form-control' placeholder='<spring:message code="password.forgotPassword.emptyEmailText"/>' data-msg-email='<spring:message code="password.forgotPassword.email.error"/>' data-msg-required='<spring:message code="password.forgotPassword.email.error"/>'/>
				</div>
				<div class='recPassError'></div>
			</div>
		</div>
		<div class="modal-footer ftrcls">
			<input id='passResendFailed' class='secondarybtn emailvalidate btn btnclsactive text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.multimode.email.resend"/>'/>
		</div>
	</div>
</form:form>		
<!--End of forgotPopup-->