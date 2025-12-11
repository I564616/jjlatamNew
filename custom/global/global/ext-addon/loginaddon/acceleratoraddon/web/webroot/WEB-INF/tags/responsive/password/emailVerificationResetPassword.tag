<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!-- Modal forgotPopup -->
			<form:form id='recoverpasswordForm' action="javascript:;" method='post'>
				<!-- Modal content-->
				<div class="modal-content" id='recoverpasswordP1'>
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='popup.close'/></button>
					  <!-- AAOL-4915 -->
						<c:choose>
							<c:when test="${passwordExipred eq 'true'}">
								 <h4 class="modal-title"><spring:message code="password.forgotPassword.passwordExpired" /></h4>
							</c:when>
							<c:otherwise>
								 <h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword"/></h4>
							</c:otherwise>
						</c:choose>
						<!-- END AAOL-4915 -->
					</div>
					<div class="modal-body row">						
						<div class="col-xs-10">
							<p><spring:message code="password.forgotPassword.emailHeader"/></p>
							<div class="form-group">
								<label ><spring:message code="password.forgotPassword.email"/></label>
								<!-- <input type="text" class="form-control padding0" id="global-search-txt" > -->
									<input type='email' id='useremail' class='required form-control' placeholder='<spring:message code="password.forgotPassword.emptyEmailText"/>' data-msg-required='<spring:message code="password.forgotPassword.email.error"/>'/>
									<input type='hidden' id='blockedUser' data-msg-required='<spring:message code="password.forgotPassword.blockedError"/>' />
									<input type='hidden' id='accountDisabled' data-msg-required='<spring:message code="login.error.account.disbaled"/>' />
									<input type='hidden' id='useremailError' value='<spring:message code="password.forgotPassword.emailMismatchError"/>' />
							</div>
							<div class='recPassError'></div>
						</div>
					</div>
					<div class="modal-footer ftrcls">
						<input id='passnext1' class='secondarybtn emailvalidate btn btnclsactive col-xs-2 text-uppercase pull-right no-border'  type='submit' value='<spring:message code="password.forgotPassword.next"/>'> </input>
					</div>
				</div>
				</form:form>
			
	<!--End of forgotPopup-->