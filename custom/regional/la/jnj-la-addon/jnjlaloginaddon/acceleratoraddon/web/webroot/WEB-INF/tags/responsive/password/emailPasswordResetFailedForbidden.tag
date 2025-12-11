<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form id='passwordResetFailedForbidden' action="javascript:;" method='post' style="display:none;">
	<div class="modal-content" id='passwordResetFailedForbidden'>
		<div class="modal-header">
			<button type="button" class="close clsBtn" data-bs-dismiss="modal" onclick="passwordResetRefreshReturn ()"><spring:message code='la.popup.close'/></button>
			<h4 class="modal-title"><spring:message code="password.multimode.passwordResetFail.forbidden.title"/></h4>
		</div>
		<div class="modal-body row">
			<div class="passwordResetUnlocked col-xs-10">				
				<div class="passwordResetFailText">
					<p><spring:message code="password.multimode.passwordResetFail.forbidden.description1"/></p>
					<p><spring:message code="password.multimode.passwordResetFail.forbidden.description2"/></p>
				</div>			
			</div>		
		</div>		
	</div>
</form:form>		
