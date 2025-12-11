<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Modal content-->
  <!-- <div  id='recoverpasswordP3' style="background-color:#fff;padding:20px">  -->
<form:form id="recoverpasswordFormP1" method="post" action="javascript:;">
	<h4 class="headingTxt content">
			<c:choose>
				<c:when test="${passwordExpired eq 'true'}">
					<spring:message code="password.forgotPassword.passwordExpired" />
				</c:when>
				<c:otherwise>
					<spring:message code="password.forgotPassword.resetPassword" />
				</c:otherwise>
			</c:choose>
		</h4>
	<div  class="col-lg-12 col-md-12 boxshadow whiteBg" id='recoverpasswordP1' style="padding:20px;">
		<div class="success reset"  style='display:none'>
			<p class="correctAnswer mar0">
				<spring:message code="password.forgotPassword.correctAnswers" />
			</p>
		</div>
	<div class="row">
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
				<label for="checkpass"> <spring:message code="password.forgotPassword.newPassword" /> <sup><spring:message code="password.forgotPassword.star"/></sup></label> </div>
				<!-- <div class='pwdwidgetdiv margintop5' id='resetPwd' style="width:150px"></div> -->	
				
				 <div class='pwdwidgetdiv margintop5' id='resetPwd' style="width:150px" data-toggle='tooltip' data-placement="top" title="<spring:message code='profile.title.passwordStrength'/>" 
				 data-msg-required='<spring:message code="cart.error.global.message"/>' 
				 data-msg-complexity='<spring:message code="profile.password.error"/>' id="current-password"></div>
																									
				<input type="hidden" value="${email}" id="useremail" />
					<noscript>
						<div>
					    <input type="password"  class="form-control checkpassVal" id="checkpass" name="checkpass" data-toggle="tooltip" data-placement="top"	
					    title="<spring:message code='profile.title.passwordStrength'/>"  
					    data-msg-required='<spring:message code="password.forgotPassword.enterPassword" />' data-msg-complexity='<spring:message code="profile.password.error"/>'  /> 									
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
		<div class="modal-footer ftrcls formmargin">
			<input class="secondarybtn emailvalidate btn btnclsactive text-uppercase pull-right no-border" id="resetPassword" value='<spring:message code="password.forgotPassword.resetPassword" />' type="submit" />
		</div>
	</div>
	</form:form>
