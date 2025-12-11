<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form id='passwordResetFailed' action="javascript:;" method='post' style="display:none;">
	<div class="modal-content" id='passwordResetFailedP1'>
		<div class="modal-header">
			<button type="button" class="close clsBtn" data-bs-dismiss="modal"><spring:message code='la.popup.close'/></button>
			<h4 class="modal-title"><spring:message code="password.multimode.passwordResetFail.title"/></h4>
		</div>
		<div class="modal-body row">
			<div class="passwordResetUnlocked col-xs-10">				
				<div class="passwordResetFailText">
					<p><spring:message code="password.multimode.passwordResetFail.description1"/></p>
					<p><spring:message code="password.multimode.passwordResetFail.description2"/></p>
				</div>
				<div id="passwordResetFailError">
					<p><spring:message code="password.multimode.passwordResetFail.error"/><span id="passwordResetFailErrorMessage"></span></p>
				</div>
			</div>
			<div class="passwordResetLocked col-xs-10" style="display:none">
				<p><spring:message code="password.multimode.passwordResetFail.numberAttempts.description1"/></p>
				<p><spring:message code="password.multimode.passwordResetFail.numberAttempts.description2"/></p>
			</div>
		</div>
		<div class="modal-footer ftrcls">
			<input id='passResetFail' class='secondarybtn btn btnclsactive col-xs-2 text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.multimode.passwordResetFail.resendButtion"/>'> </input>
		</div>
	</div>
</form:form>