<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/addons/loginaddon/message.tld" %>

	<div class="modal fade jnj-popup" id="tncpopupNew" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
					  <h4 class="modal-title"><spring:message code="acclogin.page.termscondition"/></h4>
					</div>
					<div class="modal-body">
					<cms:pageSlot position="MainBody" var="component">
								<cms:component component="${component}"/>
						</cms:pageSlot>
						
					</div>
				</div>
			</div>
		</div>
	