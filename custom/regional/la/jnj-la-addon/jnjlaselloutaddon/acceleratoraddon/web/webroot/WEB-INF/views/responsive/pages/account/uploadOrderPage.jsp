<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="cartLa" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/nav"%>
<%@ taglib prefix="services" tagdir="/WEB-INF/tags/addons/jnjlaservicepageaddon/responsive/services"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<input type="hidden" id="uploadStatus" value="${uploadStatus}" />
	<input type="hidden" id="sizeError" value="${sizeError}" />
	<input type="hidden" id="boxClosed" value="false" />
	<body onload="checkError();">
		<div id="breadcrumb" class="breadcrumb">
			<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		</div>
		<div id="globalMessages">
			<common:globalMessages />
		</div>
		<div class="row">
			<div class="col-lg-12 col-md-12 margintop15"></div>
		</div>


		<!-- Start - Body Content -->

		<div id="uploadFilePage">
			<c:set var="totalRecords"
				value="${dataList.pagination.totalNumberOfResults}" />
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<ul class="breadcrumb">
						<%-- <li><a href="home.html">Home</a></li>
						<li><class="active">Uploaded Files</a></li> --%>
					</ul>
					<div class="row content">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<label:message messageCode="text.uploadOrder.header" />

							<c:if test="${uploadStatus == true}">
								<div class="success loginBtnTopMargin">
									<p>
										<label:message messageCode="uploadOrder.success.label" />
									</p>
								</div>
							</c:if>
							<c:if test="${sizeError == true}">
								<div class="error loginBtnTopMargin">
									<p>
										<label:message messageCode="uploadOrder.size.error.label" />
									</p>
								</div>
							</c:if>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
							<div class="pull-right download-pdf">
								<strong><label:message messageCode="text.download" /></strong>
								<a href="#" id="uploadOrderPdfDownload"><label:message
										messageCode="order.history.pdf" /></a>
							</div>
						</div>

					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12">
					<div class="d-none d-sm-block mainbody-container">
						<div class="SortingTable" id="ordersTable_length">
							<form:form id="sortForm" method="POST"
								modelAttribute="sellOutReportsForm" action="uploadorders/sort">
								<span> 
									<label for="sortby" id="uploadFileSortby">
										<label:message messageCode="text.sellout.sortBy" />
									</label> 
									<input type="hidden" value="${sortflag}" id="sortFlag" /> 
									<select id="sortby">
										<option class="txtFont" value="Newest to oldest">
											<label:message messageCode="text.sellout.newest.to.oldest" />
										</option>
										<option class="txtFont" value="Oldest to newest">
											<label:message messageCode="text.sellout.oldest.to.newest" />
										</option>
									</select> 
									<input type="hidden" id="sortType" name="sortType" value="" />
								</span>
							</form:form>							
							<form:form id="filterForm" method="POST"
								modelAttribute="sellOutReportsForm" action="uploadorders/filterBy">
								<span> 
									<label for="filterBy" id="uploadFileFilterby">
										<spring:message code="text.sellout.filterBy" />
									</label> 
									<input type="hidden" value="${filterFlag}" id="filterFlag" /> 
									<select id="filterBy">
										<option class="txtFont" value="Customer">
											<spring:message code="text.filter.by.customer" />
										</option>
										<option class="txtFont" value="User">
											<spring:message code="text.filter.by.user" />
										</option>
									</select> 
									<input type="hidden" id="filterType" name="filterType" value="" />
								</span>
							</form:form>							
						</div>						
					<div class="row">	
					<div class="table_product_wrapper">
						<table id="datatab-desktop"
							class="table table-bordered table-striped lasorting-table">

							<thead>
								<tr>
									<th class="no-sort"><label:message messageCode="text.uploadOrder.document" /></th>
									<th class="no-sort"><label:message messageCode="text.uploadOrder.user" /></th>
									<th class="no-sort"><label:message messageCode="text.uploadOrder.customer" /></th>
									<th><label:message messageCode="text.uploadOrder.date" /></th>
									<th class="no-sort"><label:message messageCode="text.uploadOrder.trackingID" /></th>
									<th class="no-sort"><label:message messageCode="text.uploadOrder.status" /></th>
								</tr>
							</thead>
							<tbody>

								<c:forEach items="${dataList.results}" var="dataList" varStatus="loop">
									<tr>
										<div class="uploadOrderRow ${loop.count % 2 == 0 ? 'odd' : 'even'}">
											<td class="column1"
												style="word-wrap: break-word; padding-right: 10px"><c:choose>
													<c:when test="${not empty dataList.docName}">
														<span class="txtFont">${dataList.docName}</span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
											<td class="column2" style="word-wrap: break-word;"><c:choose>
													<c:when test="${not empty dataList.user}">
														<span class="txtFont">${dataList.user}</span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
											<td class="column3" style="word-wrap: break-word;"><c:choose>
													<c:when test="${not empty dataList.customer}">
														<span class="txtFont">${dataList.customer}</span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
											<td class="column4" style="word-wrap: break-word;"><c:choose>
													<c:when test="${not empty dataList.date}">
														<span class="txtFont">${dataList.date}</span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
											<td class="column5" style="word-wrap: break-word;"><c:choose>
													<c:when test="${not empty dataList.trackingID}">
														<span class="txtFont trackid"><a
															id="trackingId_${loop.count}" href="#">${dataList.trackingID}</a></span>
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
											<td class="column6" style="word-wrap: break-word;"><c:choose>
													<c:when test="${not empty dataList.status}">
														<!-- Added for EDI changes start -->
														<c:url
															value="/my-account/uploadorders/uplodedOrderDetails?fileNameId=${dataList.fileNameId}"
															var="uplodedOrderDetails"></c:url>
														<c:if test="${dataList.isLinkEnable}">
															<span class="txtFont"><a class="marginRight"
																href="${uplodedOrderDetails}"> <c:if
																		test="${dataList.status =='Error'}">
																		<label:message messageCode="text.uploadOrder.error" />
																	</c:if> <c:if
																		test="${dataList.status =='Sent With Restrictions'}">
																		<label:message
																			messageCode="text.uploadOrder.partialSuccess" />
																	</c:if>

															</a></span>


														</c:if>
														<c:if test="${!dataList.isLinkEnable}">
															<span class="txtFont"> <label:message
																	messageCode="text.uploadOrder.success" />
															</span>
														</c:if>
														<c:if test="${dataList.status =='Received'}">
															<label:message messageCode="text.uploadOrder.received" />
														</c:if>

														<!-- Added for EDI changes end -->
													</c:when>
													<c:otherwise>
														<span class="txtFont">&nbsp;</span>
													</c:otherwise>
												</c:choose></td>
									</tr>
									</div>
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
					</div>
				</div>
				<form:form id="loadMoreForm" method="POST"
					modelAttribute="sellOutReportsForm" action="uploadorders/loadMore">
					<c:if test="${totalRecords gt fn:length(dataList.results)}">
						<div class="clear ajaxLoad">
							<a id="loadMoreLink" style="cursor: pointer;" class="loadMore">
								<label:message messageCode="text.sellout.loadmore" />
							</a> <input type="hidden"
								value="${fn:length(dataList.results)+defaultPageSize}"
								name="pageSize" />
						</div>
					</c:if>
				</form:form>
				<input type="hidden" name="scrollPos" id="scrollPos"
					value="${scrollPos}" />

				<!-- Table collapse for mobile device -->
				<div
					class="d-block d-sm-none jnj-panel-for-table mainbody-container" style="overflow-x: scroll">
					<table id="datatab-mobile"
						class="table table-bordered table-striped sorting-table bordernone mobile-table sellout-mobile">
						<thead>
							<tr>
								<th class="no-sort"><label:message
										messageCode="text.uploadOrder.document" /></th>
								<th class="no-sort"><label:message
										messageCode="text.uploadOrder.user" /></th>
								<th class="no-sort"><label:message
										messageCode="text.uploadOrder.customer" /></th>
								<th><label:message messageCode="text.uploadOrder.date" /></th>
								<th class="no-sort"><label:message
										messageCode="text.uploadOrder.trackingID" /></th>
								<th class="no-sort"><label:message
										messageCode="text.uploadOrder.status" /></th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${dataList.results}" var="dataList"
								varStatus="loop">
								<tr>
									<div
										class="uploadOrderRow ${loop.count % 2 == 0 ? 'odd' : 'even'}">

										<td class="column1"
											style="word-wrap: break-word; padding-right: 10px"><c:choose>
												<c:when test="${not empty dataList.docName}">
													<span class="txtFont">${dataList.docName}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
										<td class="column2" style="word-wrap: break-word;"><c:choose>
												<c:when test="${not empty dataList.user}">
													<span class="txtFont">${dataList.user}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
										<td class="column3" style="word-wrap: break-word;"><c:choose>
												<c:when test="${not empty dataList.customer}">
													<span class="txtFont">${dataList.customer}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
										<td class="column4" style="word-wrap: break-word;"><c:choose>
												<c:when test="${not empty dataList.date}">
													<span class="txtFont">${dataList.date}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
										<td class="column5" style="word-wrap: break-word;"><c:choose>
												<c:when test="${not empty dataList.trackingID}">
													<span class="txtFont">${dataList.trackingID}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
										<td class="column6" style="word-wrap: break-word;"><c:choose>
												<c:when test="${not empty dataList.status}">
													<!-- Added for EDI changes start -->
													<c:url
														value="/my-account/uploadorders/uplodedOrderDetails?fileNameId=${dataList.fileNameId}"
														var="uplodedOrderDetails"></c:url>
													<c:if test="${dataList.isLinkEnable}">
														<span class="txtFont"><a class="marginRight"
															href="${uplodedOrderDetails}"> <c:if
																	test="${dataList.status =='Error'}">
																	<label:message messageCode="text.uploadOrder.error" />
																</c:if> <c:if
																	test="${dataList.status =='Sent With Restrictions'}">
																	<label:message
																		messageCode="text.uploadOrder.partialSuccess" />
																</c:if>

														</a></span>


													</c:if>
													<c:if test="${!dataList.isLinkEnable}">
														<span class="txtFont"> <label:message
																messageCode="text.uploadOrder.success" />
														</span>
													</c:if>
													<c:if test="${dataList.status =='Received'}">
														<label:message messageCode="text.uploadOrder.received" />
													</c:if>

													<!-- Added for EDI changes end -->
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose></td>
								</tr>
								</div>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- Accordian for mobile ends here -->

			</div>
		</div>


		<!-- End - Body Content -->


	</body>
	<!-- Modal for PO number -->
	<div class="modal fade" id="trackidpopup" role="dialog"
		data-firstLogin='true'>
		<div class="modal-dialog modalcls">

			<div class="modal-content popup">
				<div class="modal-header">
					<button type="button" class="close clsBtn" data-dismiss="modal">
						<spring:message code="account.change.popup.close" />
					</button>
					<h4 class="modal-title selectTitle">
						<spring:message code="upload.trackingId.message" />
					</h4>
				</div>
				<div class="modal-body">
					<div class="panel panel-danger">
						<div class="panel-heading">
							<h4 class="panel-title">
								<table class="contract-popup-table">
									<tr>
										<td><div class="glyphicon glyphicon-ok"></div></td>
										<td><div class="info-text">
												<spring:message code="upload.trackingId.error" />
											</div></td>
									</tr>
								</table>
							</h4>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal for PO number ends-->
</templateLa:page>