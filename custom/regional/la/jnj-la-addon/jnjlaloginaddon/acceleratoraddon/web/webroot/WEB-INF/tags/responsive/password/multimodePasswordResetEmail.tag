<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal multimode 1 -->
<form:form id='multimodeEmail' action="javascript:;" method='post' style="display:none;">
	<!-- Modal content-->
	<div class="modal-content" id='recoverpasswordMulti2'>
		<div class="modal-header">
			<h4 class="modal-title"><spring:message code="password.multimode.email.title" /></h4>
			<a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="la.popup.close"/></a>
		</div>
		<div class="modal-body row">						
			<div class="col-xs-10">
				<p><spring:message code="password.multimode.email.description1"/></p>
				<p><spring:message code="password.multimode.email.description2"/></p>
			</div>
		</div>
		<div class="modal-footer ftrcls">
			<input id="resetText" class="resendTitleText" type="hidden" value='<spring:message code="password.multimode.email.resentTitle"/>' />
			<input id='multimodeEmailResend' class='secondarybtn btn btnclsactive col-xs-2 text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.multimode.email.resend"/>'> </input>
		</div>
	</div>
</form:form>
			
<!--End of multimode 1-->