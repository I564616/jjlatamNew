<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal content-->
<div class="modal fade jnj-popup-container" id="resetPasswordpopup" role="dialog" data-firstLogin='true'>
 <div class="modal-dialog modalcls">
<div class="modal-content" id='recoverpasswordP3' style='display:none'>
<div id="passwordExpired"></div>
<form:form id="recoverpasswordFormP3" method="post" action="javascript:;">
	<div class="modal-header">
		<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='popup.close'/></button>
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
			<p><c:if test="${passwordExpired eq 'true'}">
				<p>
					<spring:message code="password.expired.text"/>
				</p>
			</c:if>
			</p>
			<div class='row'>
				<div class="col-xs-12">
				<div>
				<label for="checkpass"> <spring:message code="password.forgotPassword.newPassword" /> <sup class="star"><spring:message code="password.forgotPassword.star"/></sup></label> </div>
				<div class='pwdwidgetdiv margintop5' id='thepwddiv' style="width:200px"  data-toggle="tooltip" data-placement="top"	title="Password Strength: Your password must be 8 characters or longer and have two of the following four complexity classes: * Uppercase letters (A-Z)  * Lowercase letters (a-z)  * Numerals (0-9) * Non-alphabetic characters (such	as !,#,$,%)"  data-msg-required='<spring:message code="password.forgotPassword.enterPassword" />' data-msg-complexity='<spring:message code="profile.password.error"/>' ></div>										
				<script>
					var pwdwidget = new PasswordWidget('thepwddiv', 'checkpass');
					pwdwidget.MakePWDWidget();
				</script>	
					<noscript>
				
						<div>
					    <input type="password"  class="form-control checkpassVal" id="checkpass" name="checkpass" data-toggle="tooltip" data-placement="top"	title="<spring:message code='profile.title.passwordStrength'/>"  data-msg-required='<spring:message code="password.forgotPassword.enterPassword" />' data-msg-complexity='<spring:message code="profile.password.error"/>'  /> 									
						</div>
				
					</noscript>															
				</div>
			</div>
			<div class='row'>
				<div class="col-xs-12">
					<div><label class="margintop20px"><spring:message code="password.forgotPassword.retypePassword" /> <sup class="star"><spring:message code="password.forgotPassword.star"/></sup></label></div>
					<div>
					<input type="password"  style="width: 225px" class="form-control pwdWidth" id="checkpass2" name="checkpass2" data-msg-required='<spring:message code="password.forgotPassword.passwordMismatchError"/>' />
					</div>
				</div>		
			</div>
			<div class="recPassError"></div>
		</div>
          <input id="complexityInvalid" type="hidden" value='<spring:message code="profile.password.error"/>' />
		</div>
		<div class="modal-footer ftrcls">
			<input class="secondarybtn emailvalidate btn btnclsactive text-uppercase pull-right no-border" id="passnext3" value='<spring:message code="password.forgotPassword.resetPassword" />' type="submit" />
		</div>
	
	</form:form>
</div>
</div>
</div>

