<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
	<div id="contractpage">
		<%-- <div id="breadcrumb" class="breadcrumb">
			<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		</div> --%>
		<ul class="breadcrumb">
			<li><breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" /></li>
		</ul>
				<c:set var="totalRecords" value="${searchPageData.pagination.totalNumberOfResults}" />
		<div id="globalMessages">
				<common:globalMessages />
			</div>
		 <c:url value="/my-account/contract/search" var="contractAction"></c:url>
		<div class="row">
		<div class="col-xs-12 headingTxt content"><spring:message code="contract.myContracts"/>
		</div>
		</div>			
	<form:form action="${contractAction}" method="POST"  id='contractForm' name="contractForm" modelAttribute="contractForm">
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div class="table-padding">											
					<div class="firstrow row">
					<div id="global-search-txt-holder" class="col-12 col-md-8 search-txt-holder">
						<spring:message code="contract.showfilter.searchcontracts" var="searchcontracts"/>
						    <form:input type="text" path="searchByCriteria" class="form-control" id="global-search-txt" placeholder="${searchcontracts}" /> 
						    <span class="glyphicon glyphicon-search search-icon" id="contractSearch"></span>
						    <i class="bi bi-search searchglyph "  id="contractSearch"></i>
						</div>
						<div class="col-12 col-md-4">
							<div class="float-rightoleft contract-serach">
								<label class="form-label form-label-select-large searchby" style="font-weight: bold;"><spring:message code="contract.searchBy"/></label>
								<form:select  id="sortbyno" path="searchParameter" class="first form-control form-element form-element-select-large">
								<form:option class="txtFont" value="All">	<spring:message code="contract.showfilter.all" /></form:option>
								<form:option class="txtFont" value="contractNumber"><spring:message code="contract.contractNumber"/></form:option>
								<form:option class="txtFont"  value="tenderNum"><spring:message code="contract.tenderNumber"/></form:option>
								<form:option class="txtFont"  value="indirectCustomer"><spring:message code="contract.indirectCustomer"/></form:option>
								</form:select> 
							</div>
						</div>
					</div>	
					<div class="d-none d-lg-block  d-md-block conttab">
						<div class="SortingTable">
							<!-- <spring:message code="contract.sortBy"/>-->
							<label for="sortbynumber" style="font-weight: bold;"><spring:message code="contract.sortBy"/></label>
							<form:select class="first"  name="sortByCriteria" id="sortbynumberContract" path="sortByCriteria" >
							<form:option class="txtFont" value="contractNumberDecreasing"><spring:message code="contract.sortyByContractNumberDecreasing"/></form:option>
							<form:option class="txtFont" value="contractNumberIncreasing"><spring:message code="contract.sortyByContractNumberIncreasing"/></form:option>
							<form:option class="txtFont" value="contractCreationDateDecreasing"><spring:message code="contract.sortyByDateNewestToOldest"/></form:option>
							<form:option class="txtFont" value="contractCreationDateIncreasing"><spring:message code="contract.sortyByDateOldestToNewest"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerIncreasing"><spring:message code="contract.sortyByIndirectCustomerIncreasing"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerDecreasing"><spring:message code="contract.sortyByIndirectCustomerDecreasing"/></form:option>   
								</form:select>
						</div> 
						<form:input type="hidden" path="selectCriteria" id="filterBy1" />
						<form:input type="hidden" path="filterCriteria2" id="filterBy2" />
						<div class="hidden-xs panel-title text-left" id="toggle-filter">
						<a data-bs-toggle="collapse" data-parent="#accordion" href="#desktop-collapse1" class="ref_no toggle-link panel-collapsed skyBlue contractfilterpanel">
							<table>
								<tbody>
									<tr>
										<td> <i class="bi bi-plus-lg" style="-webkit-text-stroke: 1px;"></i></td>
										<td class="paddingforText" style="font-weight: bold;"><spring:message code="contract.showfilter.showFilter"/></td>
									</tr>
								</tbody>
							</table>
							</a>
							<div class="pull-right laContractDownloadLinks">
							<strong><label>
							<spring:message code="text.download"/></label></strong> &nbsp;&nbsp;&nbsp;&nbsp;<a class="tertiarybtn contractPgexcel" href="#" id ="contractExcelDownload" >
							<spring:message code="contract.excel.label"/></a> <span class="pipesymbol">|</span> &nbsp;<a class="tertiarybtn contractPgpdf" href="#" id ="contractPdfDownload" >
							<spring:message code="contract.pdf.label"/></a> 
							</div>
							
							<div id="desktop-collapse1" class="panel-collapse collapse">
								<div class="row panel-body details col-12 col-xs-12 paddingforcollapse">
									<div class="col-3 col-xs-12 borderrightgrey">
										<div class=""><spring:message code="contract.showfilter.status"/></div>
										<div class="checkbox checkbox-info checkboxmargin">
											<input id="1" class="styled select_filter" type="checkbox" title="check" name = "statusFilter" >
											<label for="1">
												  <spring:message code="contract.showfilter.active"/> (<span id="activeCount"></span>) 
											</label>
										</div>
										<div class="checkbox checkbox-info checkboxmargin">
											<input id="0" class="styled select_filter" type="checkbox" name = "statusFilter">
											<label for="0">
											 <spring:message code="contract.showfilter.discontinued"/>  (<span id="discontinueCount"></span>)
											</label>
										</div>
									</div>					
									<div class="col-3 col-xs-12">
										<div class=""><spring:message code="contract.showfilter.contractType"/> </div>
										<div class="checkbox checkbox-info checkboxmargin">
											<input id="Z19" class="styled select_filter" type="checkbox" name = "contractTypeFilter" >
											<label for="Z19">
											<spring:message code="contract.showfilter.bid"/> (<span id="bidCount" data-name="<spring:message code="contract.showfilter.bid"/>"></span>)
											</label>
										</div>
										<div class="checkbox checkbox-info checkboxmargin">
											<input id="Z20" class="styled select_filter" type="checkbox" name = "contractTypeFilter" >
											<label for="Z20"  name="commercial-count">
												<spring:message code="contract.showfilter.commericalAgreement"/>  (<span id="commercialCount" data-name="<spring:message code="contract.showfilter.commericalAgreement"/>"></span>)
											</label>
										</div>
									</div>																		
								</div>
							</div>																														
						</div>										
						<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped lasorting-table">
							<thead>
								<tr>
									<th class="text-uppercase"><span><spring:message code="contract.contractNumber"/></th>
									<th class="text-uppercase"><span><spring:message code="contract.tenderNumber"/></span></th>
									<th class="text-uppercase"><span><spring:message code="contract.indirectCustomer"/></th>
									<th class="text-uppercase"><span><spring:message code="contract.expirationDate"/></th>
									<th class="no-sort text-uppercase"><span><spring:message code="contract.contractType"/></th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${noResultFlag}">
										 <%-- <div class="buttonWrapper">
										 <span class = "orderHistorySearchText"><spring:message code="contract.noRecordsMessage"/></span>
										</div> --%>
									</c:when>
				 					<c:otherwise>
									 	<c:forEach items="${contractList}" var="contract" varStatus="count">
											<c:choose>
												<c:when test="${count.count  mod 2 == 0}">
													<c:set var="contractClass" value="even"> </c:set>
												</c:when>
												<c:otherwise>
													<c:set var="contractClass" value="odd"> </c:set>
												</c:otherwise>
											</c:choose> 
											<tr class="myContractsRow ${contractClass} contractRowClick gotomycontractdetail "  id="headerStatus_${contract.active}_${contract.eccContractNum}">
												<!-- If con need to add here based on active /deactive status-->
												<td class="column1"><c:url var="contractDetails" value="/my-account/contract/getContractDetails/${contract.eccContractNum}"> </c:url>
													<a href="${contractDetails}"> ${contract.eccContractNum} </a>
												</td>
												<td class="column2">
													<c:choose>
														<c:when test="${ not empty contract.tenderNum && '' ne contract.tenderNum}">
														 <span class="txtFont">${contract.tenderNum}</span>                        		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="column3">
													<c:choose>
														<c:when test="${ not empty contract.indirectCustomer && '' ne contract.tenderNum}">
															<span class="txtFont">${contract.indirectCustomer}</span><br>
															<span class="txtFont">${contract.indirectCustomerName}</span>  <!-- 35406 Fixed -->                     		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>															
												</td>
												<td class="column4">
													<c:choose>
														<c:when test="${ not empty contract.endDate && '' ne contract.endDate}">
															<c:choose>
																<c:when test="${'en' eq sessionlanguage}">
																	<span class="txtFont"><fmt:formatDate value="${contract.endDate}" pattern="MM/dd/yyyy" /></span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont"><fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy" /></span>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="column5">
													<c:choose>
														<c:when test="${ not empty contract.contractOrderReason && '' ne contract.contractOrderReason}">
															<span class="txtFont">${contract.contractOrderReason}</span>             		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:forEach>
									 </c:otherwise>
								</c:choose>	
							</tbody>
						</table>
					</div>

<!-- Table collapse for mobile device-->                                                        
<%-- <div class="Subcontainer d-block d-sm-none">
	<div class="SortingTable">
		<label>
			<strong><spring:message code="contract.sortBy"/></strong>  
			 <select name="ordersTable_length" aria-controls="ordersTable" class="form-control input-sm">  
			<form:select name="sortByCriteria" aria-controls="ordersTable" class="form-control input-sm" id="sortbynumberContract" path="sortByCriteria" >
					<form:option class="txtFont" value="contractNumberDecreasing"><spring:message code="contract.sortyByContractNumberDecreasing"/></form:option>
					<form:option class="txtFont" value="contractNumberIncreasing"><spring:message code="contract.sortyByContractNumberIncreasing"/></form:option>
					<form:option class="txtFont" value="contractCreationDateDecreasing"><spring:message code="contract.sortyByDateNewestToOldest"/></form:option>
					<form:option class="txtFont" value="contractCreationDateIncreasing"><spring:message code="contract.sortyByDateOldestToNewest"/></form:option>
					<form:option class="txtFont" value="contractIndirectCustomerIncreasing"><spring:message code="contract.sortyByIndirectCustomerIncreasing"/></form:option>
					<form:option class="txtFont" value="contractIndirectCustomerDecreasing"><spring:message code="contract.sortyByIndirectCustomerDecreasing"/></form:option> 
				</form:select>
				<form:input type="hidden" path="selectCriteria" id="filterBy1" />
				<form:input type="hidden" path="filterCriteria2" id="filterBy2" />
		</label>
	</div>
	<div id="toggle-filter-mobile">													
		<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed skyBlue">
			<table>
				<tbody>
					<tr>
						<td><i class="bi bi-plus-lg"></i></td>
						<td class="paddingforText"><spring:message code="contract.showfilter.showFilter"/></td>
					</tr>
				</tbody>
			</table>
		</a>
		<div class="pull-right downloadlinks"><strong>
			<spring:message code="text.download"/></strong> &nbsp;&nbsp;<a class="tertiarybtn contractexcel" href="#" id ="contractExcelDownload" >
			<spring:message code="contract.excel.label"/></a> <span class="pipesymbol">|</span> &nbsp;<a class="tertiarybtn contractpdf" href="#" id ="contractPdfDownload" >
			<spring:message code="contract.pdf.label"/></a>
		</div>
		<div id="collapse2" class="panel-collapse collapse">
			<div class="panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse ">																					
				<div class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey">
					<div class=""><spring:message code="contract.showfilter.status"/></div>
					<div class="checkbox checkbox-info checkboxmargin">
						<input id="check4" class="styled" type="checkbox" >
						<label for="check4">Active (12)</label>
						<input id="1" class="styled select_filter" type="checkbox" title="check" name = "statusFilter" >
						<label for="1">
							  <spring:message code="contract.showfilter.active"/> (<span id="activeCount"></span>) 
						</label>
					</div>
					<div class="checkbox checkbox-info checkboxmargin">
						<input id="check4" class="styled" type="checkbox" >
						<label for="check4">Discontinued (1)</label>
						<input id="0" class="styled select_filter" type="checkbox" name = "statusFilter">
						<label for="0">
						 <spring:message code="contract.showfilter.discontinued"/>  (<span id="discontinueCount"></span>)
						</label>
					</div>
				</div>		
				
				<div class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey paddingleft30px">
					<div class=""><spring:message code="contract.showfilter.contractType"/></div>
					<div class="checkbox checkbox-info checkboxmargin">
						<input id="check4" class="styled" type="checkbox" >
						<label for="check4">Bid (13)</label>
						<input id="Z19" class="styled select_filter" type="checkbox" name = "contractTypeFilter" >
						<label for="Z19">
						<spring:message code="contract.showfilter.bid"/> (<span id="bidCount"></span>)
						</label>
					</div>
					<div class="checkbox checkbox-info checkboxmargin">
						<input id="check4" class="styled" type="checkbox" >
						<label for="check4">Commercial Agreement (0)</label>
						<input id="Z20" class="styled select_filter" type="checkbox" name = "contractTypeFilter" >
						<label for="Z20">
							<spring:message code="contract.showfilter.commericalAgreement"/>  (<span id="commercialCount"></span>)
						</label>
					</div>
				</div>	
			</div>
		</div>
	</div>
			<table id="datatab-mobile" class="table table-bordered table-striped sorting-table bordernone">
				<thead >
						<tr>
							<th class="text-left"><span><spring:message code="contract.contractNumber"/></th>
						</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${noResultFlag}">
							 <div class="buttonWrapper">
							 <span class = "orderHistorySearchText"><spring:message code="contract.noRecordsMessage"/></span>
							</div>
						</c:when>
	 					<c:otherwise>
	 						<c:forEach items="${contractList}" var="contract" varStatus="count">
		 						<c:choose>
									<c:when test="${count.count  mod 2 == 0}">
										<c:set var="contractClass" value="even"> </c:set>
									</c:when>
									<c:otherwise>
										<c:set var="contractClass" value="odd"> </c:set>
									</c:otherwise>
								</c:choose>
								<tr class="myContractsRow ${contractClass} contractRowClick gotomycontractdetail "  id="headerStatus_${contract.active}_${contract.eccContractNum}">
									<td class="panel-title text-left"><c:url var="contractDetails" value="/my-account/contract/getContractDetails/${contract.eccContractNum}"> </c:url>
										<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse3" class="ref_no toggle-link panel-collapsed">
											<span class="glyphicon glyphicon-plus"></span> ${contract.eccContractNum}</a>
											<a href="${contractDetails}"> ${contract.eccContractNum} </a>
										<div id="collapse3" class="panel-collapse collapse">
											<div class="panel-body details">															
												<div class="text-left"><spring:message code="contract.tenderNumber"/></div>
												<div>
													<c:choose>
														<c:when test="${ not empty contract.tenderNum && '' ne contract.tenderNum}">
														 <span class="txtFont">${contract.tenderNum}</span>                        		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</div>
												<div class="text-left"><spring:message code="contract.indirectCustomer"/></div>
												<div>
													<c:choose>
														<c:when test="${ not empty contract.indirectCustomer && '' ne contract.tenderNum}">
															<span class="txtFont">${contract.indirectCustomer}</span><br>
															<span class="txtFont">${contract.indirectCustomerName}</span>  <!-- 35406 Fixed -->                     		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</div>
												<div class="text-left"><spring:message code="contract.expirationDate"/></div>
												<div>
													<c:choose>
														<c:when test="${ not empty contract.endDate && '' ne contract.endDate}">
															<c:choose>
																<c:when test="${'en' eq sessionlanguage}">
																	<span class="txtFont"><fmt:formatDate value="${contract.endDate}" pattern="MM/dd/yyyy" /></span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont"><fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy" /></span>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
													</div>
												<div class="text-left"><spring:message code="contract.contractType"/></div>
												<div>
													<c:choose>
														<c:when test="${ not empty contract.contractOrderReason && '' ne contract.contractOrderReason}">
															<span class="txtFont">${contract.contractOrderReason}</span>             		
														</c:when>
														<c:otherwise>
															<span class="txtFont">&nbsp;</span>
														</c:otherwise>
													</c:choose>
												</div>
											</div>
										</div>
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</tbody>
		</table>
	</div> --%>
	
						<!-- Table collapse for mobile device-->
						<div class="Subcontainer d-block d-sm-none">
							<!-- <div class="row horizontal-line"></div> -->
							<div class="SortingTable sort-by-contract-mobile-holder"
								id="ordersTable_length">
								<div class="sort-by-contract-mobile">
									<spring:message code="contract.page.sortby" />
								</div>
								<select name="ordersTable_length" aria-controls="ordersTable"
									class="form-control input-sm" id="sort-by-contract-mobile">
									<option value="Name (ascending)"><spring:message
											code="contract.page.contractnum" /></option>
									<option class="txtFont" value="contractNumberDecreasing" />
									<label:message
										messageCode="contract.sortyByContractNumberDecreasing" />

									<option class="txtFont" value="contractNumberIncreasing" />
									<label:message
										messageCode="contract.sortyByContractNumberIncreasing" />

									<option class="txtFont" value="contractCreationDateDecreasing" />
									<spring:message code="contract.sortyByDateNewestToOldest" />

									<option class="txtFont" value="contractCreationDateIncreasing" />
									<spring:message code="contract.sortyByDateOldestToNewest" />

									<option class="txtFont"
										value="contractIndirectCustomerIncreasing" />
									<label:message
										messageCode="contract.sortyByIndirectCustomerIncreasing" />

									<option class="txtFont"
										value="contractIndirectCustomerDecreasing" />
									<label:message
										messageCode="contract.sortyByIndirectCustomerDecreasing" />
									<spring:message code="contract.sortyByDateNewestToOldest" />
								</select>
							</div>
							<div id="toggle-filter-mobile">
								<div class="row horizontal-line"></div>
								<div style="z-index: 1000;">
									<div style="padding-left: 25px">
										<a data-bs-toggle="collapse" data-parent="#accordion"
											href="#filter-mobile-collapse"
											class="ref_no toggle-link panel-collapsed skyBlue">
											<table>
												<tbody>
													<tr>
														<td><i class="bi bi-plus-lg"></i></td>
														<td class="paddingforText"><spring:message
																code="contract.showfilter.showFilter" /></td>
													</tr>
												</tbody>
											</table>
										</a>

										<div class="pull-right downloadlinks">
											<strong><spring:message code="text.download" /></strong>
											&nbsp;&nbsp;<a class="tertiarybtn contractexcel" href="#"
												id="contractExcelDownload"><label:message
													messageCode="contract.excel.label" /></a> <span
												class="pipesymbol">|</span> &nbsp;<a
												class="tertiarybtn contractpdf" href="#"
												id="contractPdfDownload"><label:message
													messageCode="contract.pdf.label" /></a>
										</div>
									</div>
									<div id="filter-mobile-collapse"
										class="panel-collapse collapse">
										<div
											class="panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse">
											<div
												class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey">
												<div class="">
													<spring:message code="contract.showfilter.status" />
												</div>
												<div class="checkbox checkbox-info checkboxmargin">
													<input id="check4" class="styled" type="checkbox">
													<label for="check4"> <spring:message
															code="contract.showfilter.active" />
													</label>
												</div>
												<div class="checkbox checkbox-info checkboxmargin">
													<input id="check4" class="styled" type="checkbox">
													<label for="check4"> <spring:message
															code="contract.showfilter.discontinued" />
													</label>
												</div>
											</div>
											<div
												class="col-lg-4 col-xs-12 col-sm-4 col-md-4 paddingleft30px paddingtop10px">
												<div class="">
													<spring:message code="contract.showfilter.contractType" />
												</div>
												<div class="checkbox checkbox-info checkboxmargin">
													<input id="check4" class="styled" type="checkbox">
													<label for="check4"> <spring:message
															code="contract.showfilter.bid" />
													</label>
												</div>
												<div class="checkbox checkbox-info checkboxmargin">
													<input id="check4" class="styled" type="checkbox">
													<label for="check4"> <spring:message
															code="contract.showfilter.commericalAgreement" />
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<table id="ordersTablemobile"
								class="table table-bordered table-striped sorting-table bordernone">

								<thead>
									<tr>
										<th class="text-left"><label:message
												messageCode="contract.contractNumber" /></th>

									</tr>
								</thead>

								<tbody>
									<c:forEach items="${contractList}" var="contract"
										varStatus="Count">
										<c:choose>
											<c:when test="${Count.count  mod 2 == 0}">
												<c:set var="contractClass" value="even">
												</c:set>
											</c:when>
											<c:otherwise>
												<c:set var="contractClass" value="odd">
												</c:set>
											</c:otherwise>
										</c:choose>
										<tr>
											<td><a data-bs-toggle="collapse" data-parent="#accordion"
												href="#contract-mobi-collapse${Count.count}"
												class="ref_no toggle-link panel-collapsed"><i class="bi bi-plus-lg"></i></a> <c:url
													var="contractDetails"
													value="/my-account/contract/getContractDetails/${contract.eccContractNum}">
												</c:url> <a href="${contractDetails}">
													${contract.eccContractNum} </a>

												<div id="contract-mobi-collapse${Count.count}"
													class="panel-collapse collapse">
													<div class="panel-body details">
														<div>
															<spring:message code="contract.tenderNumber" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.tenderNum && '' ne contract.tenderNum}">
																	<span class="txtFont">${contract.tenderNum}</span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont">&nbsp;</span>
																</c:otherwise>
															</c:choose>
														</div>
														<div>
															<spring:message code="contract.indirectCustomer" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.indirectCustomer && '' ne contract.tenderNum}">
																	<span class="txtFont">${contract.indirectCustomer}</span>
																	<br>
																	<span class="txtFont">${contract.indirectCustomerName}</span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont">&nbsp;</span>
																</c:otherwise>
															</c:choose>
														</div>

														<div>
															<spring:message code="contract.expirationDate" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.endDate && '' ne contract.endDate}">
																	<c:choose>
																		<c:when test="${'en' eq sessionlanguage}">
																			<span class="txtFont"><fmt:formatDate
																					value="${contract.endDate}" pattern="MM/dd/yyyy" /></span>
																		</c:when>
																		<c:otherwise>
																			<span class="txtFont"><fmt:formatDate
																					value="${contract.endDate}" pattern="dd/MM/yyyy" /></span>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<span class="txtFont">&nbsp;</span>
																</c:otherwise>
															</c:choose>
														</div>
														<div>
															<spring:message code="contract.contractType" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.contractOrderReason && '' ne contract.contractOrderReason}">
																	<span class="txtFont">${contract.contractOrderReason}</span>
																</c:when>
																<c:otherwise>
																	<span class="txtFont">&nbsp;</span>
																</c:otherwise>
															</c:choose>
														</div>
													</div>
												</div></td>

										</tr>


									</c:forEach>
								</tbody>
							</table>
						</div>
						<!--Accordian Ends here -->
					</div>
					<%-- <div class="Subcontainer d-block d-sm-none">
												<div class="SortingTable">
													<label>
														<strong><spring:message code="contract.page.sortby"/></strong>  
														<select name="ordersTable_length" aria-controls="ordersTable" class="form-control input-sm">
														<option value="contract number"><spring:message code="contract.page.contractnum"/></option></select>
													</label>
												</div>
												<div id="toggle-filter-mobile">													
													<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed skyBlue">
													<table>
														<tbody>
															<tr>
																<td><i class="bi bi-plus-lg"></i></td>
																<td class="paddingforText">Show Filters</td>
															</tr>
														</tbody>
													</table>
													</a>
													<div class="pull-right downloadlinks"><strong>Download</strong> &nbsp;&nbsp;<a href="#">XLS</a> <span class="pipesymbol">|</span> &nbsp;<a href="#">PDF</a></div>
													<div id="collapse2" class="panel-collapse collapse">
														<div class="row panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse ">
															<div class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey">
																<div class="">STATUS</div>
																<div class="checkbox checkbox-info checkboxmargin">
																	<input id="check4" class="styled" type="checkbox" >
																	<label for="check4">
																		Active (12)
																	</label>
																</div>
																<div class="checkbox checkbox-info checkboxmargin">
																	<input id="check4" class="styled" type="checkbox" >
																	<label for="check4">
																		Discontinued (1)
																	</label>
																</div>
															</div>					
															<div class="col-lg-4 col-xs-12 col-sm-4 col-md-4 borderrightgrey paddingleft30px">
																<div class="">CONTRACT TYPE</div>
																<div class="checkbox checkbox-info checkboxmargin">
																	<input id="check4" class="styled" type="checkbox" >
																	<label for="check4">
																		Bid (13)
																	</label>
																</div>
																<div class="checkbox checkbox-info checkboxmargin">
																	<input id="check4" class="styled" type="checkbox" >
																	<label for="check4">
																		Commercial Agreement (0)
																	</label>
																</div>
															</div>															
														</div>
													</div>
												</div>
												<table id="datatab-mobile" class="table table-bordered table-striped sorting-table bordernone">
													<thead >
															<tr>
																<th class="text-left">CONTRACT NUMBER</th>													
															</tr>
													</thead>
													<tbody>
												
														<tr>
															<td class="panel-title text-left">
																<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse3" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> 0001000</a>
																<div id="collapse3" class="panel-collapse collapse">
																	<div class="panel-body details">															
																		<div>TENDER NUMBER</div>
																		<div>29574893</div>
																		<div>INDIRECT CUSTOMER</div>
																		<div>0010021391</div>
																		<div>HOSPITAL NOSSA SENHORA DA</div>
																		<div>CONCEICAO</div>
																		<div>EXPIRATION DATE</div>
																		<div>29/06/2016</div>
																		<div>CONTRACT TYPE</div>
																		<div>BID</div>
																	</div>
																</div>
															</td>														
														</tr>
														<tr>
															<td class="panel-title text-left">
																<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse4" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> 0001000</a>
																<div id="collapse4" class="panel-collapse collapse">
																	<div class="panel-body details">															
																		<div>TENDER NUMBER</div>
																		<div>29574893</div>
																		<div>INDIRECT CUSTOMER</div>
																		<div>HOSPITAL NOSSA SENHORA DA CONCEICAO</div>
																		<div>EXPIRATION DATE</div>
																		<div>29/06/2016</div>
																		<div>CONTRACT TYPE</div>
																		<div>BID</div>
																	</div>
																</div>
															</td>														
														</tr>
														<tr>
															<td class="panel-title text-left">
																<a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse5" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus"></span> 0001000</a>
																<div id="collapse5" class="panel-collapse collapse">
																	<div class="panel-body details">															
																		<div>TENDER NUMBER</div>
																		<div>29574893</div>
																		<div>INDIRECT CUSTOMER</div>
																		<div>HOSPITAL NOSSA SENHORA DA CONCEICAO</div>
																		<div>EXPIRATION DATE</div>
																		<div>29/06/2016</div>
																		<div>CONTRACT TYPE</div>
																		<div>BID</div>
																	</div>
																</div>
															</td>														
														</tr>
													</tbody>
												</table>								
											</div>       --%>

					<!--Accordian Ends here -->

					<!--Accordian Ends here -->
				</div>
			</div>

			<input type="hidden" name="loadNoOfRecords"
				id="loadNoOfRecordsHidden" />
			<input type="hidden" name="scrollPos" id="scrollPos"
				value="${scrollPos}" />
		</form:form>

		<input type="hidden" value="{contract.status}" id="contractLineStatus" />
</templateLa:page>