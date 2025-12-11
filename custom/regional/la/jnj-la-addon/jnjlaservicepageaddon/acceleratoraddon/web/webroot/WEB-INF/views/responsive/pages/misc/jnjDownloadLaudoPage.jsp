<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">

	<div id="ServicesPage">
		<div id="DownloadLaudoPage">
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div id="breadcrumb" class="breadcrumb">
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
					</div>

					<div class="row">
						<div class="margintop40 content">
							<spring:message code="misc.downloadlaudo.header" />
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
			<c:url value="/services/laudo/download" var="downloadLaudo"></c:url>
			<div class="row table-padding downloadLaudo-block">
				<form:form id="downloadLaudoForm" modelAttribute="jnjLaudoForm"
					action="${downloadLaudo}" method="get">

                <div class="row">
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"
						style="padding: 0px">
						<div>
							<label id="labelJnjId" for="jnjId" class="manage-label-width"> <spring:message
									code="misc.services.downloadlaudo.jnjId.${countryIsoCode}" /> <spring:message
									code="misc.services.colon" /><sup class="star">*</sup>
							</label>
							<div>
								<form:input id="downloadJnjId" path="searchTextJnjId"
									maxlength="30" data-msg-required='${mandatoryErrorMessage}'
									type="text" class="jnjId" />
							</div>
						</div>
					</div>
					<input type="hidden" id="hddnMandatoryError"
					value="<messageLabel:message messageCode='services.laudo.download.mandatoryError' />" />
					<input type="hidden" value="${(isBatchRequired)}" id="isBatchMandatory"/>
					<c:choose>
						<c:when test="${(isBatchRequired)}">
						  <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
						<div>
							<label for="loteNumber" class="manage-label-width"> <spring:message
									code="misc.services.downloadlaudo.loteNumber" /> <spring:message
									code="misc.services.colon" /><sup class="star">*</sup>
							</label>
							<div>
								<form:input id="downloadLoteNumber" path="searchTextLoteNumber"
									maxlength="30" type="text" class="loteNumber" />
							</div>
						</div>
					</div>
						</c:when>
						<c:otherwise>
						   <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
						<div>
							<label for="loteNumber" class="manage-label-width"> <spring:message
									code="misc.services.downloadlaudo.loteNumber" /> <spring:message
									code="misc.services.colon" /><sup class="star">*</sup>
							</label>
							<div>
								<form:input id="downloadLoteNumber" path="searchTextLoteNumber"
									maxlength="30" type="text"  class="loteNumber" />
							</div>
						</div>
					</div>
						</c:otherwise>
					</c:choose>
					<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"
						style="padding: 0px">
						<input id="downloadLaudoSearchButton" type="button"
							class="btn btnclsactive pull-right"
							value='<spring:message code="misc.services.downloadlaudo.search"/>' />
					</div>
					<form:input id="downloadSearchBy" path="searchBy" name="searchBy"
						type="text" style="display:none;" />
					<input id="downloadSearchFlag" name="search" type="text"
						style="display: none;" />
                </div>
				</form:form>
			</div>
			<div class='mandatoryError'></div>
			<c:choose>
				<c:when test="${'true' == search}">
					<c:choose>
						<c:when
							test="${laudoData.results ne null && fn:length(laudoData.results) ne 0 }">
							<div class="row">
								<div class="col-lg-12 col-md-12">
									<div class="hidden-xs jnj-panel-for-table mainbody-container">
										<table id="manageLaudoTable"
											class="table table-bordered table-striped">
											<thead>
												<tr>
													<th class="no-sort"><spring:message
															code="misc.services.downloadlaudo.jnjId" /></th>
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
											<tbody>
												<c:forEach var="laudo" items="${laudoData.results}"
													varStatus="status">
													<tr>
														<td class="manageTable-firstdata">
															<div class="display-table-row">
																<input type="hidden" class="hddnDeleteData" />
																<div class="display-table-cell">
																	<div>${laudo.productCode}</div>
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
														<td>${laudo.pdfFileName}</td>
														<td><c:choose>
																<c:when test="${'en' eq sessionlanguage}">
																	<span class="txtFont"><fmt:formatDate
																			value="${laudo.expirationDate}" pattern="MM/dd/yyyy" /></span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont"><fmt:formatDate
																			value="${laudo.expirationDate}" pattern="dd/MM/yyyy" /></span>
																</c:otherwise>
															</c:choose></td>
														<td><i class="fa fa-trash trash-icon deleteRowLaudo"
															aria-hidden="true"></i> <c:url
																value="/services/laudo/download/${laudo.pdfFileName}"
																var="downloadLaudoEntryUrl" /> <a
															href="${downloadLaudoEntryUrl}"> </a> <input
															type="button"
															class="btn btnclsnormal add-btn anchorClicker"
															value='<spring:message code="misc.services.pdf"/>' /></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>

						</c:when>
						<c:otherwise>
							<div class="row margintop20px">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="panel-group " style="margin: 0px 0px 10px 0px">
										<div class="panel panel-danger">
											<div class="panel-heading">
												<div id="downloadLaudoNoSearchInfo">
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
				</c:when>
				<c:otherwise>


					<div class="row margintop20px" id="defaultalert">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
							<div class="panel-group " style="margin: 0px 0px 10px 0px">
								<div class="panel panel-danger">
									<div class="panel-heading">
										<div id="downloadLaudoNoSearchInfo">
											<h4 class="panel-title">
												<span><i class="fa fa-info info-icon"
													aria-hidden="true"></i> <spring:message
														code="services.laudo.download.beforeSearchInfoMessage" /></span>
											</h4>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

</templateLa:page>
