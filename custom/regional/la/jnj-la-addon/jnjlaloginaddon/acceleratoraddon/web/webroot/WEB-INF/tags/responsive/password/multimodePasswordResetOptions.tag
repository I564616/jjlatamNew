<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal multimode 1 -->
<form:form id='chooseMultiMode' action="javascript:;" method='post' style="display:none;">
	<!-- Modal content-->
	<div class="modal-content" id='recoverpasswordMulti1'>
		<div class="modal-header">
			<h4 class="modal-title"><spring:message code="password.multimode.options.title" /></h4>
			<a class="close clsBtn" data-bs-dismiss="modal"><spring:message code="la.popup.close"/></a>
		</div>
		<div class="modal-body row">						
			<div class="col-xs-12">
				<p><spring:message code="password.multimode.options.description"/></p>
				<div class="form-group">
					<div class="multimode-btn">
						<input id="multimode-email" type="radio" name="multimode" class='multimode-input' value='<spring:message code="password.multimode.options.option1"/>'><label for='multimode-email'><spring:message code="password.multimode.options.option1"/></label><br>
					</div>
					<div class="multimode-btn">
						<input id="multimode-text" type="radio" name="multimode" class='multimode-input' value='<spring:message code="password.multimode.options.option2"/>'><label for='multimode-text'><spring:message code="password.multimode.options.option2"/></label><br>
					</div>
					<div class="multimode-btn">
						<input id="multimode-call" type="radio" name="multimode" class='multimode-input' value='<spring:message code="password.multimode.options.option3"/>'><label for='multimode-call'><spring:message code="password.multimode.options.option3"/></label><br>  
					</div> 
				</div>
				<div class='recPassError'></div>
			</div>
		</div>
		<div class="modal-footer ftrcls">
			<input id='multimodenext1' disabled="disabled" class='secondarybtn emailvalidate btn btnclsactive col-xs-2 text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.multimode.options.submit"/>'> </input>
		</div>
	</div>
</form:form>
			
<!--End of multimode 1-->