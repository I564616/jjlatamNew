<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="contactUs"
	tagdir="/WEB-INF/tags/responsive/contactus"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="laCommon"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<div class="row jnj-body-padding" id="jnj-body-content">
		<div class="col-lg-12 col-md-12" id="ServicesPage">
			<div id="CrossReferencePage">
				<div class="row">
					<div class="col-lg-12 col-md-12">
						<!-- breadcrumb : START -->
						<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
						<!-- breadcrumb : End -->
						<div class="row">
							<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12 content">
								<spring:message code="text.crossref.header" />
							</div>
						</div>
					</div>
				</div>
				<div class="row table-padding crossReference-mainBody">
					<div class="row jnj-panel mainbody-container crossReference-body"
						id="global-search-txt-holder">
						<div class="col-lg-1 col-sm-4 col-md-4 col-xs-12 pull-left" style="padding: 6px 0px 0px 10px">

							<label class="searchby"><label:message
									messageCode="text.searchby" /></label>
							<c:if test="${empty searchTerm}">
								<c:set var="searchTerm" value="Search"></c:set>
							</c:if>
						</div>
						<div id="" class="col-lg-4 col-sm-4 col-md-4 col-xs-12">
							<spring:message code="template.page.search"
								var="searchplaceholder" />
							<c:url var="action" value="crossReferenceTable/searchCrossRef" />

							<form:form method="POST" modelAttribute="crossReferenceForm"
								id="crossReferenceForm" action="${action}">
								<form:input path="searchTerm" type="text" title="Enter Text"
									value="" placeholder="${searchplaceholder}"
									class=" inputWidth marLeft form-control" />
								<span class="glyphicon glyphicon-search search-icon"
									onclick="$('#crossReferenceForm').submit();"></span>
								<input type="hidden" value="${fn:length(dataList.results)}"
									name="pageSize" />
							</form:form>
						</div>

						<div
							class="pull-right downloadlinks col-lg-6 col-sm-5 col-md-5 col-xs-12">
							<strong><spring:message code="text.download" /></strong>
							&nbsp;&nbsp;&nbsp;&nbsp;<a id="downloadExcelCrossReference1"
								href="#"><label:message
									messageCode="text.sellout.button.excel" /></a> <span
								class="pipesymbol">|</span> &nbsp;<a
								id="downloadPdfCrossReference1" href="#"><label:message
									messageCode="text.sellout.button.pdf" /></a>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12 col-md-12">
							<div class="hidden-xs hidden-sm">
								<table id="datatab-desktop"
									class="crossReferenceTable table table-bordered table-striped lasorting-table">
									<thead>
										<tr>
											<th class="no-sort"><label:message
													messageCode="text.your.id" /></th>
											<th class="no-sort"><label:message
													messageCode="text.jnj.id" /></th>
											<th class="no-sort"><label:message
													messageCode="text.product.name" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${dataList.results}" varStatus="loop"
											var="dataList">




											<tr>
												<td><c:choose>
														<c:when test="${not empty dataList.clientProductID}">
															<span class="txtFont">${dataList.clientProductID}</span>
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${not empty dataList.jnjProductID}">
															<span class="txtFont">${dataList.jnjProductID}</span>
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose></td>
												<td><c:choose>
														<c:when test="${not empty dataList.productName}">
															<span class="txtFont">${dataList.productName}</span>
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</templateLa:page>