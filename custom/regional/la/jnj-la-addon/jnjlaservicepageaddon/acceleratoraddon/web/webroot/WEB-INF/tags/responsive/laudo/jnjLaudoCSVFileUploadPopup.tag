<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%-- CSV File Upload --%>
<div class="csvFileUploadLaudo modal fade" id="returnOrder-popup"
	role="dialog" data-firstLogin='true'>
	<c:url value="/services/laudo/uploadMultipe" var="uploadCSVLaudo" />
	<form:form id="csvLaudoUploadForm" enctype="multipart/form-data"
		action="${uploadCSVLaudo}" method="post" modelAttribute="jnjLaudoFileUploadForm">
		<input type="hidden" name="fileExtension" id="csvFileExtension" />
		<div class='lightboxtemplate' id='csvLaudoUploadLightBox'>

			<div class="modal-dialog modalcls">

				<div class="modal-content popup">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-bs-dismiss="modal"
							id="select-accnt-close">
							<spring:message code="account.change.popup.close" />
						</button>
						<h4 class="modal-title selectTitle">
							<spring:message code="misc.services.selectCSVFile" />
						</h4>
					</div>

					<div id="csvUploadError" class="registerError hidden">
						<label class="error"> <spring:message
								code="misc.services.csvFormatError" />
						</label>
					</div>
					<div id="fileSizeCsvError" class="registerError hidden">
						<label class="error"> <spring:message
								code="misc.services.fileSizeError" />
						</label>
					</div>
					<div class="marTop20 errorDetailsCsvUpload hidden"></div>
					<div class="modal-footer ftrcls">
						<span id="fileInfolaudo" class="filename"
							style="width: 50px; margin-bottom: 6px; display: none;"></span>
						<div id="firstblock" class="fileinputs fileUploadDivFloat">
							<input id="browseCsvFileLaudo" type="file" class="file"
								name="files"
								style="display: block; visibility: hidden; width: 0; height: 0;" />
							<div class="fakefile">
								<input class="secondarybtn btn btnclsactive"
									id="firstFakeButton2" type="button"
									value='<spring:message code="misc.services.browseFile"/>' />
							</div>
						</div>
						<input type="button" id="saveCsvFileLaudo"
							class="primarybtn btn btnclsactive" disabled="disabled"
							value='<spring:message code="misc.services.uploadFile"/>' />
					</div>
				</div>
			</div>
		</div>
	</form:form>
</div>


