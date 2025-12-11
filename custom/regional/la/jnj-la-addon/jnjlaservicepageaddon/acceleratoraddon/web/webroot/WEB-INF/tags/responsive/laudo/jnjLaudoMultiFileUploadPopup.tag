<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%-- CSV File Upload --%>
<div class="multiFileUploadLaudo modal fade" id="returnOrder-popup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">

		<div class="modal-content popup">
			<div class="multiFileUploadLaudoScreen">
				<input type="hidden" name="fileExtension" id="multiFileExtension" />
				<div class='lightboxtemplate' id='multiLaudoUploadLightBox'>
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"
							id="select-accnt-close">
							<spring:message code="account.change.popup.close" />
						</button>
						<h4 class="modal-title selectTitle">
							<spring:message code="misc.services.selectZipFile" />
						</h4>
					</div>
					<c:url value="/services/laudo/uploadMultipe" var="uploadMultiLaudo" />
					<form:form id="multiLaudoUploadForm" enctype="multipart/form-data"
						action="${uploadMultiLaudo}" method="post" modelAttribute="jnjLaudoFileUploadForm">
						<input type="hidden" name="fileExtension" id="zipFileExtension" />
						<div id="multiUploadError" class="registerError hidden">
							<label class="error"> <spring:message
									code="misc.services.zipFormatError" />
							</label>
						</div>
						<div id="fileSizeError" class="registerError hidden">
							<label class="error"> <spring:message
									code="misc.services.fileSizeError" />
							</label>
						</div>
						<div class="marTop20 errorDetailsMultiUpload hidden"></div>

						<div class="modal-footer ftrcls">
							<span id="fileInfolaudomulti" class="filename"
								style="width: 50px; margin-bottom: 6px; display: none;"></span>
							<div id="firstblock" class="fileinputs fileUploadDivFloat">
								<input id="browseMultiFileLaudo" type="file" class="file"
									name="files"
									style="display: block; visibility: hidden; width: 0; height: 0;" />
								<div class="fakefile">
									<input class="secondarybtn btn btnclsactive"
										id="firstFakeButton3" type="button"
										value='<spring:message code="misc.services.browseFile"/>' />
								</div>
							</div>
							<input type="button" id="saveMultiFileLaudo"
								class="primarybtn btn btnclsactive" disabled="disabled"
								value='<spring:message code="misc.services.uploadFile"/>' />
						</div>
					</form:form>
				</div>
			</div>
			<div class="multiFileUploadLaudoStatus">
				<div class='lightboxtemplate' id='multiLaudoUploadLightBox'>
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"
							id="select-accnt-close">
							<spring:message code="account.change.popup.close" />
						</button>
						<h4 class="modal-title selectTitle">
							<spring:message code="misc.services.selectZipFile" />
						</h4>
					</div>
					<div class="modal-body">
						<%-- SUCCESS SCENARIO --%>
						<c:choose>
							<c:when test="${zipStatusSuccess}">
								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-success">
										<div class="panel-heading">
											<h4 class="panel-title display-row">
												<span class="glyphicon glyphicon-ok table-cell" style="top: 2px;padding-right:5px"></span> <span
													class="table-cell sorry-msg"><spring:message
														code="misc.services.zipSuccess" /></span>
											</h4>
										</div>
									</div>
								</div>
							</c:when>
							<c:when test="${zipStatusPartial}">
								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-success">
										<div class="panel-heading">
											<h4 class="panel-title display-row">
												<span class="glyphicon glyphicon-ok table-cell" style="top: 2px;padding-right:5px"></span> <span
													class="table-cell sorry-msg"><spring:message
														code="misc.services.zipPartialSuccess" /></span>
											</h4>
										</div>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h4 class="panel-title display-row registerError">
												<span class="glyphicon glyphicon-ban-circle table-cell"
													style="top: 2px;padding-right:5px"></span> <span class="table-cell sorry-msg">
													<spring:message code="misc.services.fileUploadError" />
												</span>
											</h4>
										</div>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						<%-- FILE LIST --%>
						<ul>
							<c:forEach var="jnjLaudoFileStatusData"
								items="${jnjLaudoFileStatusList}">
								<div>
									<li><span id="errorFileName" class="boldtext">${jnjLaudoFileStatusData.fileName}</span>
										<span id="errorReason">${jnjLaudoFileStatusData.statusMessage}</span>
									</li>
								</div>
							</c:forEach>
						</ul>

					</div>
				</div>
			</div>
		</div>
	</div>
</div>