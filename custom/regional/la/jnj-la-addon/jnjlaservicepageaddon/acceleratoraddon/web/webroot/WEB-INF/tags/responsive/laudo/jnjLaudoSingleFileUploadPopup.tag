<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%-- Single File Upload Markup --%>
<div class="singleFileUploadLaudo modal fade" id="ManageLaudo-popup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<%-- Single File Upload Screen --%>
			<div class="singleLaudoUploadScreen">
				<c:url value="/services/laudo/uploadSingle" var="uploadSingleLaudo" />
				<form:form id="manageLaudoSingleUploadForm"
					enctype="multipart/form-data" action="${uploadSingleLaudo}"
					method="post" modelAttribute="jnjLaudoFileUploadForm">
					<div class='lightboxtemplate' id='manageLaudoSingleUploadLightBox'>
						<input type="hidden" id="hddnEmptyFieldsError"
							value='<spring:message code="misc.services.emptyFieldsError"/>' />
						<div class="modal-header">
							<button type="button" class="close clsBtn" data-bs-dismiss="modal"
								id="select-accnt-close">
								<spring:message code="account.change.popup.close" />
							</button>
							<h4 class="modal-title selectTitle">
								<spring:message code="misc.services.addLaudo" />
							</h4>
						</div>
						<div id="emptyFieldsError" class="registerError"></div>
						<div id="fileSizeSingleError" class="registerError hidden">
							<label class="error"> <spring:message
									code="misc.services.fileSizeError" />
							</label>
						</div>
						<div id="fileFormatSingleError" class="registerError hidden">
							<label class="error"> <spring:message
									code="misc.services.pdfFormatError" />
							</label>
						</div>
						<div class="modal-body">
							<div class="row">
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label">
									<label for="productCode"> <spring:message
											code="misc.services.productId" /> <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<input class="form-control mandatory" name="productCode"  maxlength="30"
										type="text" />
								</div>

							</div>
							<input type="hidden" value="${(isBatchRequired)}" id="isBatchMandatory"/>
						<c:choose>
							<c:when test="${(isBatchRequired)}">
								<div class="row margintop20px">
									<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label">
										<label class="textLabel" for="laudoNumber"> <spring:message
											code="misc.services.downloadlaudo.loteNumber" /> <sup
											class="star">*</sup>
										</label>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<input class="form-control mandatory"  name="laudoNumber" maxlength="30"
											type="text" />
									</div>
							    </div>
							</c:when>
							<c:otherwise>
								<div class="row margintop20px">
									<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label">
										<label class="textLabel" for="laudoNumber"> <spring:message
											code="misc.services.downloadlaudo.loteNumber" /> <sup
											class="star"></sup>
										</label>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
										<input class="form-control" id="batchNumber" name="laudoNumber" maxlength="30"
											type="text" />
									</div>
								</div>
							</c:otherwise>
						</c:choose>
							<div class="row margintop20px">
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label expDate">
									<label class="textLabel" for="expirationDate"> <spring:message
											code="misc.services.expirationDate" /> <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<div class="input-group form-element form-element-date">
										<c:set var="selectDatePlacehodler">
											<spring:message code="misc.services.selectDate" />
										</c:set>
										<input id="popUpExpirationDate"
											class="date-picker form-control mandatory" name="expirationDate"
											type="text" placeholder="${selectDatePlacehodler}" /> <label
											for="popUpExpirationDate" class="input-group-addon btn">
											<i class="bi bi-calendar3"></i>
											</label>
									</div>
								</div>
							</div>
							<div class="row margintop20px">
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label">
									<label class="textLabel"> <spring:message
											code="misc.services.attachFile" /> <sup class="star">*</sup>
									</label>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<input class="form-control" id="attachFileLaudo"
										name="attachFile" disabled="disabled" type="text" />
								</div>
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 lineHeight-label browsebtn">
									<input id="hddnBrowseFileText" type="hidden"
										value='<spring:message code="misc.services.browseFile"/>' />
									<div id="firstblock" class="fileinputs">
										<input id="browseSingleFileLaudo" type="file" class="file"
											name="multipartFile"
											style="display: block; visibility: hidden; width: 0; height: 0;" />
										<div class="fakefile">
											<input class="btn btnclsactive" style="letter-spacing: 0.7px;" id="firstFakeButton1"
												type="button"
												value='<spring:message code="misc.services.browseFile"/>' />
										</div>
									</div>
								</div>
							</div>

							<div class="checkbox checkbox-info ">
								<input  id="documentDelete" name="documentDeleted"
								class="styled" type="checkbox">
								<label for="documentDeleted"><spring:message code="misc.services.deleteExpire" /></label>
							</div>
						</div>
						<div class="modal-footer ftrcls">
							<button type="button"
								class="btn btnclsactive pull-left cancel-Txt"
								data-bs-dismiss="modal">
								<spring:message code="template.create.cancel" />
							</button>
							<input id="saveSingleFileLaudo" type="button"
								class="btn btnclsactive"
								value='<spring:message code="misc.services.save"/>' />
						</div>

					</div>
				</form:form>
			</div>
			<%-- Single File Upload Status --%>
			<div class="singleLaudoUploadError">
				<div class='lightboxtemplate' id='singleLaudoUploadErrorLightBox'>
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-bs-dismiss="modal"
							id="select-accnt-close">
							<spring:message code="account.change.popup.close" />
						</button>
						<h4 class="modal-title selectTitle">
							<spring:message code="misc.services.addLaudo" />
						</h4>
					</div>
					<div class="modal-body">
						<c:choose>
							<%-- SUCCESS SCENARIO --%>
							<c:when test="${jnjLaudoFileStatusData.processed}">

								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-success">
										<div class="panel-heading">
											<h4 class="panel-title display-row">
												<span>
												    <i class="bi bi-check-lg"></i>
												</span>
												<span class="table-cell sorry-msg">
													<spring:message code="misc.services.addLaudoUploadSuccess" />
												</span>
											</h4>
										</div>
									</div>
								</div>
								<div>
									<span class="boldtext">${jnjLaudoFileStatusData.fileName}</span>
									${jnjLaudoFileStatusData.statusMessage}
								</div>
							</c:when>
							<%-- ERROR SCENARIO --%>
							<c:otherwise>

								<div class="panel-group hidden-xs hidden-sm"
									style="margin: 0px 0px 10px 0px">
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h4 class="panel-title display-row">
												<span class="glyphicon glyphicon-ban-circle table-cell"
													style="top: 2px;padding-right:5px"></span> <span class="table-cell sorry-msg">
													<spring:message code="misc.services.fileUploadError" />
												</span>
											</h4>
										</div>
									</div>
								</div>
								<span class="boldtext">${jnjLaudoFileStatusData.fileName}</span> ${jnjLaudoFileStatusData.statusMessage}
				</c:otherwise>
						</c:choose>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>