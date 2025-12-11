<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- Selection screen markup --%>
<div class="laudoFileUploadSelection">
	<div class='lightboxtemplate modal fade' id='manageLaudoUploadLightBox'
		role="dialog" data-firstLogin='true'>
		<div class="modal-dialog modalcls">
			<div class="modal-content popup">
				<div class="modal-header">
					<button type="button" class="close clsBtn" data-dismiss="modal"
						id="select-accnt-close">
						<spring:message code="account.change.popup.close" />
					</button>
					<h4 class="modal-title selectTitle">
						<spring:message code="misc.services.uploadLaudo" />
					</h4>
				</div>
				<div class="modal-footer ftrcls">
					<input type="button"
						class="pull-left btn btnclsactive secondarybtn singleFileLaudo"
						value='<spring:message code="misc.services.singleFile"/>' /> <input
						type="button" class="btn btnclsactive secondarybtn multiFileLaudo"
						value='<spring:message code="misc.services.multiFile"/>' />
				</div>
			</div>
		</div>
	</div>
</div>


