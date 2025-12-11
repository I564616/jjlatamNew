<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade jnj-popup-container" id="resetPasswordsuccesspopup" role="dialog" data-firstLogin='true'>
<div class="modal-content" id="recoverpasswordP4" style='display:none'>
<%-- <form:form id="recoverpasswordFormP4" action="javascript:;" method="post">
				<div class="modal-header">
				  <h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword"/></h4>
				</div>
				<div class="modal-body row">						
					<div class="col-xs-10" id="profilepage">
						<p><spring:message code="password.forgotPassword.passwordChangeSuccess" /></p>
						</div>
					<div class="col-xs-12">
					<button class="btnclsactive text-uppercase pull-right no-border " style= "border:none;" data-bs-dismiss="modal"><spring:message code='la.popup.close'/></button>
					</div>
				</div>
</form:form> --%>
			<div class="modal-dialog modalcls">
			<div class="modal-content popup ">
			<div class="modal-header">
			 <h4 class="modal-title"><spring:message code="password.forgotPassword.resetPassword.header"/></h4>
			</div>
			<div class="modal-body">
				<div class="panel-group" style="margin-bottom:0px !important">
					<div class="panel panel-success">
						<div class="panel-heading">
							<h4 class="panel-title">
								<span><i class="bi bi-check fs-4" style="-webkit-text-stroke: 1px;"></i> <spring:message
										code="password.forgotPassword.passwordChangeSuccess"></spring:message></span>
							</h4>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer modal-footer-style">
				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-bs-dismiss="modal" id="reset-ok">
					<spring:message code="cart.common.ok" />
				</button>
			</div>
		</div>
	</div>
</div>
</div>
<div id="success-resetpassowrdpage" style='display:none'>
	<div  class="col-lg-12 col-md-12 boxshadow whiteBg" style="padding:20px;">
			<div class="row">
				<div class="col-xs-12">
					<div class="panel-group">
						<div class="panel panel-success">
							<div class="panel-heading">
								<spring:message code="password.forgotPassword.passwordChangeSuccess" />
							</div>
						</div>	
					</div>	
				</div>
			</div>	
			<div class="" style="border-top:1px solid #e5e5e5">
				<button class="btnclsactive text-uppercase pull-right no-border resetpassowrd-Close" style= "border:none;" id="pass4"><spring:message code="password.forgotPassword.close" /></button>
			</div>
	</div>
</div>
