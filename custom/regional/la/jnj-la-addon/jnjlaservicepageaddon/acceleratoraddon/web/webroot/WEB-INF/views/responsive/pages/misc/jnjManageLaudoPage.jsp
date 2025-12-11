<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="laudoPopup"
	tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/laudo"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="laCommon"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<div id="ServicesPage">
		<div id="ManageLaudoPage">
			<input type="hidden" id="addFileText"
				value="<messageLabel:message messageCode='misc.services.addFile'/>" />
			<input type="hidden" id="singleAutoPopUp"
				value="${singleAutoPopUpFlag}" /> <input type="hidden"
				id="csvAutoPopUp" value="${csvAutoPopUpFlag}" /> <input
				type="hidden" id="multiAutoPopUp" value="${multiAutoPopUpFlag}" />
			<input type="hidden" id="fileSizeFlag" value="${fileSizeExceeded}" />
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div id="breadcrumb" class="breadcrumb">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</div>
					<div class="row">
						<div class="content" style="margin-bottom: 0px !important;">
							<spring:message code="misc.managelaudo.header" />
						</div>
						<div class="laudotoppanel">
							<div class="req-text">
								(<span style="color: #b41601;">*</span>)
								<spring:message code="text.downloadlaudo.required" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="marTop10 marBott10">
				<c:if test="${deleteStatusMap ne null}">
					<c:choose>
						<c:when test="${deleteStatusMap['type'] eq 'single'}">
							<c:choose>
								<c:when test="${deleteStatusMap['status'] eq 'success'}">
									<laCommon:genericMessage
										messageCode="services.laudo.download.singleDeleteSuccess"
										icon="ok" panelClass="success" />
								</c:when>
								<c:when test="${deleteStatusMap['status'] eq 'failure'}">
									<laCommon:genericMessage
										messageCode="services.laudo.download.singleDeleteFailure"
										icon="ban-circle" panelClass="danger" />
								</c:when>
							</c:choose>
						</c:when>
						<c:when test="${deleteStatusMap['type'] eq 'multiple'}">
							<c:choose>
								<c:when test="${deleteStatusMap['status'] eq 'success'}">
									<laCommon:genericMessage
										messageCode="services.laudo.download.multiDeleteSuccess"
										icon="ok" panelClass="success" />
								</c:when>
								<c:when test="${deleteStatusMap['status'] eq 'partial'}">
									<laCommon:genericMessage
										messageCode="services.laudo.download.multiDeletePartialSuccess"
										icon="ok" panelClass="success" />

								</c:when>
								<c:when test="${deleteStatusMap['status'] eq 'failure'}">
									<laCommon:genericMessage
										messageCode="services.laudo.download.multiDeleteFailure"
										icon="ban-circle" panelClass="danger" />
								</c:when>
							</c:choose>
						</c:when>
					</c:choose>
				</c:if>
			</div>
			<c:url value="/services/laudo/manage" var="manageLaudo" />
			<form:form id="manageLaudoForm" modelAttribute="jnjLaudoForm"
				action="${manageLaudo}" method="get">
				<div class="row table-padding manage-block">
					<div class="row" style="padding-bottom: 25px">

						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
							<div class="manage-Display-row">
								<label for="jnjId" class="manage-label-width"> <spring:message
										code="misc.services.downloadlaudo.jnjId" /> <spring:message
										code="misc.services.colon" /><sup class="star">*</sup>
								</label>
								<form:input id="jnjId" path="searchTextJnjId" maxlength="30"
									type="text"
									class="jnjId form-control dwld-inputbox-manage manage-Text-width"
									tabindex="1" />
							</div>
						</div>

						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
							<input type="button" id="manageLaudoRemoveFiles"
								class="btn btnclsnormal pull-right"
								style="text-transform: none;"
								value='<spring:message code="misc.services.removeSelected"/>' />
							<input type="button"
								class="btn btnclsactive pull-right marginRight10px manageLaudoAddFiles"
								style="text-transform: none;"
								value='<messageLabel:message messageCode="misc.services.addFiles"/>' />
							<input type="button" id="manageLaudoSerach"
								class="btn btnclsactive pull-right marginRight10px"
								style="text-transform: none;"
								value='<spring:message code="misc.services.downloadlaudo.search"/>' />
						</div>
					</div>
					<input type="hidden" value="${(isBatchRequired)}" id="isBatchMandatory"/>
			<c:choose>
				<c:when test = "${(isBatchRequired)}">
					<div class="row manageRegulatory">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
							<div class="manage-Display-row">
								<label for="loteNumber" class="manage-label-width"> <spring:message
									code="misc.services.downloadlaudo.loteNumber" /> <spring:message
									code="misc.services.colon" /><sup class="star">*</sup>
								</label>
									<form:input id="loteNumber" path="searchTextLoteNumber"
										maxlength="30" type="text"
										class="loteNumber form-control dwld-inputbox-manage manage-Text-width"
										tabindex="2" />
							</div>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="row manageRegulatory">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
							<div class="manage-Display-row">
								<label for="loteNumber" class="manage-label-width"> <spring:message
									code="misc.services.downloadlaudo.loteNumber" /> <spring:message
									code="misc.services.colon" /><sup class="star">*</sup>
									</label>
								<form:input id="loteNumber" path="searchTextLoteNumber"
									maxlength="30" type="text"
									class="loteNumber form-control dwld-inputbox-manage manage-Text-width"
									tabindex="2" />
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>	
					<div class="row manage-secondRow">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="">
								<label for="laudoSortBy"
									class="pull-left form-label form-label-select-large manage-sortBy ">
									<spring:message code="misc.services.sortBy" /> <spring:message
										code="misc.services.colon" />
								</label>
								<form:select id="laudoSortBy" path="sortCode"
									class="form-control form-element form-element-select-large newToOld manage-select-width">
									<form:option value="byDateDesc">
										<spring:message code="misc.services.dateNewToOld" />
									</form:option>
									<form:option value="byDateAsc">
										<spring:message code="misc.services.dateOldToNew" />
									</form:option>
								</form:select>
							</div>
						</div>
						<div class="col-lg-8 col-md-8 col-sm-12 col-xs-12 pull-right">

							<span
								style="float: left; padding-top: 6px; font-weight: 700; padding-right: 3px"><spring:message
									code="misc.services.expirationDate" /></span> <label for="fromdate"
								class="expdate-label"><spring:message
									code="misc.services.fromDate" /></label>
							<div class="manage-fromDate input-group">
								<form:input id="fromDateLaudo" path="fromDate"
									class="date-picker form-control date-picker-body" type="text" />
								<label for="fromDateLaudo" class="input-group-addon btn">
								    <i class="bi bi-calendar3"></i></label>
							</div>
							<label class="to-text" for="todate"><spring:message
									code="misc.services.toDate" /></label>
							<div class="manage-toDate input-group">
								<form:input id="toDateLaudo"
									class="date-picker form-control date-picker-body" type="text"
									path="toDate" name="toDate" />
								<label for="toDateLaudo" class="input-group-addon btn">
								    <i class="bi bi-calendar3"></i></label>
							</div>

						</div>

					</div>
				</div>
				<input type="hidden" id="hddnMandatoryError"
					value="<messageLabel:message messageCode='services.laudo.download.mandatoryError' />" />
				<input type="hidden" id="hddnSelectionError"
					value="<messageLabel:message messageCode='services.laudo.download.noSelectionError' />" />
				<div class='mandatoryError registerError'></div>
				<form:input id="currentPageSize" path="currentPageSize"
					name="currentPageSize" type="text" style="display:none;" />
				<form:input id="searchBy" path="searchBy" name="searchBy"
					type="text" style="display:none;" />
				<form:input style="display:none;" id="laudoLoadMoreClickCounter"
					name="laudoLoadMoreClickCounter" path="loadMoreClickCounter" />
				<form:input style="display:none;" id="laudoMoreResults"
					name="laudoMoreResults" path="moreResults" />
			</form:form>
			<c:url value="/services/laudo/delete" var="deleteLaudo" />
			<form:form id="deleteLaudoForm" action="${deleteLaudo}" method="post"
				modelAttribute="JnjDeleteLaudoForm">
				<div class="row d-none d-sm-block">
					<div class="col-lg-12 col-md-12">
						<div class="hidden-xs jnj-panel-for-table mainbody-container">
							<table id="manageLaudoTable"
								class="table table-bordered table-striped">
								<c:if
									test="${laudoData.results ne null && fn:length(laudoData.results) ne 0}">
									<thead>
										<tr>
											<th class="no-sort">
												<div class="checkbox checkbox-info ">
													<input disabled id="manage-select-all"
														class="checkboxLaudoHead styled" type="checkbox">
													<label for="manage-select-all"><spring:message
															code="misc.services.downloadlaudo.jnjId" /> </label>
												</div>
											</th>
											<th class="no-sort"><spring:message
													code="misc.services.downloadlaudo.loteNumber" /></th>
											<th class="no-sort"><spring:message
													code="misc.services.fileName" /></th>
											<th class="no-sort"><spring:message
													code="misc.services.expirationDate" /></th>
											<th class="no-sort"><spring:message
													code="misc.services.download" /></th>
										</tr>
									</thead>

								</c:if>
								<tbody>
									<c:choose>
										<c:when
											test="${laudoData.results ne null && fn:length(laudoData.results) ne 0 }">
											<c:forEach var="laudo" items="${laudoData.results}"
												varStatus="status">

												<tr
													class="managelaudoRow ${status.index %2 == 0 ? 'even' : 'odd'}"
													id="laudo_${status.index}_${laudo.active}">

													<td class="manageTable-firstdata">
														<div class="display-table-row">
															<input type="hidden" class="hddnDeleteData" />
															<div
																class="checkbox checkbox-info display-table-cell column1">
																<input id="manage-check-${status.count}"
																	class="checkboxLaudo styled" type="checkbox"
																	value="${laudo.pdfFileName}" hidden="hidden" /> <label
																	for="manage-check-${status.count}"></label>

															</div>

															<div class="display-table-cell">
																<div class="column2">${laudo.productCode}</div>
															</div>
															<%-- <input class="checkboxLaudo" type="checkbox" value="${laudo.pdfFileName}" /> --%>
														</div>
													</td>

													<c:choose>
                                                      <c:when test="${laudo.laudoNumber eq 'none'}" >
                                                         <td class="column3"></td>
                                                      </c:when>
                                                      <c:otherwise>
                                                         <td class="column3">${laudo.laudoNumber}</td>
                                                      </c:otherwise>
                                                    </c:choose>
													<td class="column4">${laudo.pdfFileName}</td>
													<c:choose>
   													 <c:when test="${'en' eq sessionLanguage}">
       												  <td><span class="txtFont column5"><fmt:formatDate value="${laudo.expirationDate}" pattern="MM/dd/yyyy"/></span></td>
    												 </c:when>
    												<c:otherwise>
        											  <td><span class="txtFont"><fmt:formatDate value="${laudo.expirationDate}" pattern="dd/MM/yyyy"/></span></td>
    												</c:otherwise>
													</c:choose>
													<td class="column6"><i
														class="fa fa-trash trash-icon deleteRowLaudo"
														aria-hidden="true"></i> <c:url
															value="/services/laudo/download/${laudo.pdfFileName}"
															var="downloadLaudoEntryUrl" /> <a
														href="${downloadLaudoEntryUrl}"> </a> <input type="button"
														class="btn btnclsnormal add-btn anchorClicker"
														value='<spring:message code="misc.services.pdf"/>' /></td>

												</tr>

											</c:forEach>
										</c:when>
										<c:otherwise>
											<div class="row margintop20px" id="managenosearchalert">
												<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
													<div class="panel-group " style="margin: 0px 0px 10px 0px">
														<div class="panel panel-danger">
															<div class="panel-heading">
																<div id="manageLaudoNoSearchInfo">
																	<h4 class="panel-title">
																		<span><i class="fa fa-info info-icon"
																			aria-hidden="true"></i> <spring:message
																				code="services.laudo.download.emptySearchResults" /></span>
																	</h4>
																</div>
															</div>
														</div>
													</div>
												</div>

											</div>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<input type="hidden" id="deleteFileNames" name="deleteFileNames" />
			</form:form>

		</div>
	</div>


	<%-- Popup Tags --%>
	<div id="manageLaudoHddnLightBox">
		<laudoPopup:jnjLaudoUploadSelectionPopup />
		<laudoPopup:jnjLaudoSingleFileUploadPopup />
		<laudoPopup:jnjLaudoCSVFileUploadPopup />
		<laudoPopup:jnjLaudoCSVUploadStatusPopup />
		<laudoPopup:jnjLaudoMultiFileUploadPopup />
	</div>
</templateLa:page>