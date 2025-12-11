<%@ taglib prefix="message" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Modal forgotPopup -->
		
			<form:form id='recoverpasswordForm' action="javascript:;" method='post'>
				<!-- Modal content-->
				<div class="modal-content" id='recoverpasswordP1'>
					<div class="modal-header">
					  <h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword"/></h4>
					  <a type="button" class="close clsBtn" data-bs-dismiss="modal"><spring:message code='la.popup.close'/></a>
					</div>
					<div class="modal-body row">						
						<div class="col-xs-10">
							<p><spring:message code="password.forgotPassword.emailHeader"/></p>
							<div class="form-group">
								<label ><spring:message code="password.forgotPassword.email"/></label>
								<!-- <input type="text" class="form-control padding0" id="global-search-txt" > -->
				<input type='email' style="width: 86%" id='useremail' class='required form-control' placeholder='<spring:message code="password.forgotPassword.emptyEmailText"/>' data-msg-email='<spring:message code="password.forgotPassword.email.error"/>' data-msg-required='<spring:message code="password.forgotPassword.email.error"/>'/>
				<input type='hidden' id='blockedUser' data-msg-required='<spring:message code="password.forgotPassword.blockedError"/>' />
				<input type='hidden' id='accountDisabled' data-msg-required='<spring:message code="login.error.account.disbaled"/>' />
				<input type='hidden' id='useremailError' value='<spring:message code="password.forgotPassword.emailMismatchError"/>' />
								
							</div>
							<div class='recPassError'></div>
						</div>
					</div>
					
						<div class="modal-footer ftrcls">
							<input id='passnext1' class='secondarybtn btn btnclsactive col-xs-2 text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.forgotPassword.next"/>'> </input>
						</div>
					
					
				</div>
				</form:form>
			
	<!--End of forgotPopup-->