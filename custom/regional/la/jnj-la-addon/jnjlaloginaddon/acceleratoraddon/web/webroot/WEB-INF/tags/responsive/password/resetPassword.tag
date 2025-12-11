<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value="/login" var="loginUrl"/>
<!-- Modal content-->
<div class="modal fade jnj-popup-container in" id="resetPasswordpopup" role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content" id='recoverpasswordP3' style='display:none'>
		<div id="passwordExpired"></div>
		<form:form id="recoverpasswordFormP3" method="post" action="javascript:;">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='la.popup.close'/></button>
				<h4 class="modal-title">
						<!-- AAOL-4915 -->
						<c:choose>
							<c:when test="${passwordExipred eq 'true'}">
								<spring:message code="password.forgotPassword.passwordExpired" />
							</c:when>
							<c:otherwise>
								<spring:message code="password.forgotPassword.resetPassword" />
							</c:otherwise>
						</c:choose>
						<!-- END AAOL-4915 -->
				</h4>
			</div>
			<div class="modal-body row">
				<div class="col-xs-12" id="profilepage">
					<div class=" m-b-10"><spring:message code="password.expired.text.message"/></div>
                    <div class="col-xs-12 col-md-12 col-lg-12 no-padd field-holder required resetPwdHolder">
                    	<div class='naPwdwidgetdiv tooltipNA margintop5' id='resetPwd'> 
                    	 	<p><spring:message code='register.password.tooltip'/></p>
							<p><spring:message code='register.password.complexity.1'/></p>
							<p><spring:message code='register.password.complexity.2'/></p>
							<p><spring:message code='register.password.complexity.3'/></p>
							<p><spring:message code='register.password.complexity.4'/></p>
                    	 </div>
				         <div class="col-xs-12 col-md-12 col-lg-12 no-padd">
				              <label class="col-xs-12 col-md-12 col-lg-12 no-padd field-label"><spring:message code="tab.five.password.title"/></label>
				              <div class="col-xs-12 col-md-12 col-lg-12 no-padd ">
				                  <spring:message code="tab.five.password.tooltip" var="tooltip"/>
				                  <input type="password" placeholder="<spring:message code="tab.five.password.title"/>" id="reset_password" />
				              </div>
				         </div>
				         <div class="col-xs-12 no-gutters no-padd">
				              <span class="field-enter-valid-msg"><spring:message code="tab.five.password.validation.valid-password"/>
				              </span>
				         </div>
				         <div class="col-xs-12 no-gutters no-padd" id="strengthAddAfter">
				              <span class="field-error-msg"><spring:message code="tab.five.password.validation.enter-password"/></span>
				         </div>
				    </div>
					<div class="col-xs-12 col-md-12 col-lg-12 no-padd field-holder required">
						<label class="col-xs-12 col-md-12 col-lg-12 no-padd field-label"><spring:message code="tab.five.conf-password.title"/></label>
						<div class="col-xs-12 col-md-12 col-lg-12 no-padd">
							<input type="password" placeholder="<spring:message code="tab.security-question.confirm-password.placeholder"/>" id="reset_confirm_password" class="reset-password-confirmation" />
						</div>
				         <div class="col-xs-12 no-gutters no-padd">
							<span class="field-enter-valid-msg"><spring:message code="tab.five.conf-password.validation.valid-conf-password"/></span>
						</div>
						<div class="col-xs-12 no-gutters no-padd">
							<span class="field-error-msg"><spring:message code="tab.five.conf-password.validation.enter-conf-password"/></span>
						</div>
						<div class="recPassError"></div>
					</div>
					
				</div>
				<input id="complexityInvalid" type="hidden" value='<spring:message code="profile.password.error"/>' />
				</div>
				<div class="modal-footer ftrcls textCenter">
					<input class="secondarybtn emailvalidate btn btnclsactive text-uppercase no-border m-b-10" id="resetYourPasswordBtn" value='<spring:message code="password.forgotPassword.changePassword" />' type="submit" />
					<div>
					    <a href="${loginUrl}" class="btn jnj-btn-secondary text-uppercase">
		                    <spring:message code="password.expired.popup.back.login"/>
		                </a>
		            </div>
				</div>
			
			</form:form>
		</div>
	</div>
</div>
