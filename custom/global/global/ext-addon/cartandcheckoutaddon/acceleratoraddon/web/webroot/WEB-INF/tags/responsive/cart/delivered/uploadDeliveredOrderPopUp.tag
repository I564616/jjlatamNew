<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/desktop/cart/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url value="/cart/deliveredOrderFileUpload" var="DeliveredOrderFileUpload" />
<div class="modal fade jnj-popup-container" id="uploadDeliveredOrder"
	role="dialog" data-firstLogin='true'>
	
	<div class="modal-dialog modalcls">
	
		<div class="modal-content popup">
			<div class="modal-header">
				<a href="" type="button" class="close clsBtn" data-dismiss="modal" style="font-weight:normal"><spring:message code="popup.close"></spring:message></a>
				<h4 class="modal-title selectTitle"><spring:message code="cart.deliver.fileUpload.header"/></h4>
			</div>
			<form:form action="${DeliveredOrderFileUpload}" enctype="multipart/form-data" id="submitDelivedOrderFileForm" method="POST">
			<div class="modal-body">
				<div class="row surgery-info-row">
					<div class="col-lg-12">
						<input id="uploadBrowseFile" type="file" name="deliveredOrderDoc" class="filestyle" data-buttonText="Choose File">
						<span style="position: relative;" id="attachDocNameSpan"> ${cartData.attachDocName} </span> 
						<span style="float:right;cursor: pointer;position: relative;" id="removeUploadForm"><a id="removeDoc">
						<spring:message code="cart.remove.file"></spring:message></a></span>
						<div class="upload-info"><spring:message code="cart.deliver.fileUpload.uploadPdfAndImageFiles"/></div>
						<div class="registerError invalidFileError" style="display:none;margin:0;">
						<label class="error">
							<spring:message code="cart.deliver.fileUpload.error"/>
						</label>
					</div>
					<div class="registerError emptyFileError" style="display:none;margin:0;">
						<label class="error">
							<spring:message code="cart.uploadDelivered.empty.file"/>
						</label>
					</div>
					<!-- Added --> 
					 <div class="registerError removeFileError" style="display:none;margin:0;">
						<label class="error">
						<spring:message code="cart.delivered.order.remove.file.error"/>
						</label>
					</div> 
					</div>
				</div>

			</div>
			<div class="modal-footer ftrcls">
				<a href="" class="pull-left canceltxt" data-dismiss="modal"><spring:message code="cart.common.cancel"/></a>
				<button type="button" class="btn btnclsactive submitDeliveredOrderFile mobile-auto-btn" value="<spring:message code="reports.account.selection.ok"/>" 
					id="accept-btn" onClick="TestFileType(document.getElementById('uploadBrowseFile').value,['gif', 'jpg', 'png', 'jpeg','pdf']);">
					<spring:message code="reports.account.selection.ok"/></button>
			</div>
			</form:form>
		</div>
	</div>
</div>
