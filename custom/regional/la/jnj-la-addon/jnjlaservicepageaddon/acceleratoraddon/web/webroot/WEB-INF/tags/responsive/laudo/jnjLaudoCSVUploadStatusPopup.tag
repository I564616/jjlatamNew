<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- CSV Upload Status + Multi-file Upload Markup --%>
<div class="csvLaudoUploadStatus modal fade" role="dialog"
	data-firstLogin='true'>
	<div class='lightboxtemplate' id='manageLaudoUploadLightBox2'>
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
				<div class="modal-body">
					<c:forEach var="jnjLaudoFileStatusData"
						items="${jnjLaudoFileStatusList}">
						<c:choose>
							<%-- SUCCESS SCENARIO --%>
							<c:when test="${jnjLaudoFileStatusData.processed}">

								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-success">
										<div class="panel-heading">
											<h4 class="panel-title display-row">
												<span class="glyphicon glyphicon-ok table-cell" style="top: 2px;padding-right:5px"></span>
												<c:choose>
													<c:when test='${csvStatusPartial}'>
														<span class="table-cell sorry-msg"><spring:message
																code="misc.services.csvPartialSuccess" /></span>
													</c:when>
													<c:otherwise>
														<span class="table-cell sorry-msg"><spring:message
																code="misc.services.csvSuccess" /></span>
													</c:otherwise>
												</c:choose>
											</h4>
										</div>
									</div>
								</div>
							</c:when>

							<%-- ERROR SCENARIO --%>
							<c:otherwise>
								<input type="hidden" id="hddnCsvStatus"
									value="${jnjLaudoFileStatusData.processed}" />

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
					</c:forEach>
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

					<%-- UPLOAD LAUDOS --%>

					<div class="modal-footer ftrcls">
						<div class="errorHide">
							<input id="moveToLaudoMultiFileScreen" type="button"
								class="secondarybtn btn btnclsactive"
								value='<spring:message code="misc.services.uploadLaudos"/>' />
						</div>
						<div class="errorUnhide hidden">
							<input id="moveBackToCsvUpload" type="button"
								class="secondarybtn btn btnclsactive"
								value='<spring:message code="misc.services.back"/>' />
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</div>