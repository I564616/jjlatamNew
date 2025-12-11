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

<template:page pageTitle="${pageTitle}">
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
		<div class="col-xs-12 headingTxt content"><label:message messageCode="contract.myContracts"/>
		</div>
		</div>			
	<form:form action="${contractAction}" method="POST"  id='contractForm' name="contractForm" commandName="contractForm">						
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div class="table-padding">											
					<div class="firstrow">
					<div id="global-search-txt-holder" class="col-lg-7 col-md-6 col-sm-6 col-xs-12 search-txt-holder">
						    <form:input type="text" path="searchByCriteria" class="form-control" id="global-search-txt" placeholder="Search Contacts" /> 
						    <span class="glyphicon glyphicon-search search-icon" id="contractSearch"></span> 
						</div>
						<div class="col-lg-5 col-md-6 col-sm-6 col-xs-12">
							<div class="float-rightoleft contract-serach">
								<label class="form-label form-label-select-large searchby"><label:message messageCode="contract.searchBy"/></label>
								<form:select  id="sortbyno" path="searchParameter" class="first form-control form-element form-element-select-large">
								<form:option class="txtFont" value="All">All</form:option>
								<form:option class="txtFont" value="contractNumber"><label:message messageCode="contract.contractNumber"/></form:option>
								<form:option class="txtFont"  value="tenderNum"><label:message messageCode="contract.tenderNumber"/></form:option>
								<form:option class="txtFont"  value="indirectCustomer"><label:message messageCode="contract.indirectCustomer"/></form:option>
								</form:select> 
							</div>
						</div>
					</div>	
					<div class="SortingTable">
							<!-- <label:message messageCode="contract.sortBy"/>-->
							<label for="sortbynumber"><label:message messageCode="contract.sortBy"/></label>
							<form:select class="first"  name="sortByCriteria" id="sortbynumberContract" path="sortByCriteria" >
							<form:option class="txtFont" value="contractNumberDecreasing"><label:message messageCode="contract.sortyByContractNumberDecreasing"/></form:option>
							<form:option class="txtFont" value="contractNumberIncreasing"><label:message messageCode="contract.sortyByContractNumberIncreasing"/></form:option>
							<form:option class="txtFont" value="contractCreationDateDecreasing"><label:message messageCode="contract.sortyByDateNewestToOldest"/></form:option>
							<form:option class="txtFont" value="contractCreationDateIncreasing"><label:message messageCode="contract.sortyByDateOldestToNewest"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerIncreasing"><label:message messageCode="contract.sortyByIndirectCustomerIncreasing"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerDecreasing"><label:message messageCode="contract.sortyByIndirectCustomerDecreasing"/></form:option>   
								</form:select>
					</div>
					<div class="hidden-xs">
						<%-- <div class="SortingTable">
							<!-- <label:message messageCode="contract.sortBy"/>-->
							<label for="sortbynumber"><label:message messageCode="contract.sortBy"/></label>
							<form:select class="first"  name="sortByCriteria" id="sortbynumberContract" path="sortByCriteria" >
							<form:option class="txtFont" value="contractNumberDecreasing"><label:message messageCode="contract.sortyByContractNumberDecreasing"/></form:option>
							<form:option class="txtFont" value="contractNumberIncreasing"><label:message messageCode="contract.sortyByContractNumberIncreasing"/></form:option>
							<form:option class="txtFont" value="contractCreationDateDecreasing"><label:message messageCode="contract.sortyByDateNewestToOldest"/></form:option>
							<form:option class="txtFont" value="contractCreationDateIncreasing"><label:message messageCode="contract.sortyByDateOldestToNewest"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerIncreasing"><label:message messageCode="contract.sortyByIndirectCustomerIncreasing"/></form:option>
							<form:option class="txtFont" value="contractIndirectCustomerDecreasing"><label:message messageCode="contract.sortyByIndirectCustomerDecreasing"/></form:option>   
								</form:select>
						</div> --%> 
						<form:input type="hidden" path="selectCriteria" id="filterBy1" />
						<form:input type="hidden" path="filterCriteria2" id="filterBy2" />
							<div id="toggle-filter">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-5">
									<a data-toggle="collapse" data-parent="#accordion" href="#desktop-collapse1" class="ref_no toggle-link panel-collapsed skyBlue contractfilterpanel">
										<table>
											<tbody>
												<tr>
													<td><span class="glyphicon glyphicon-plus skyBlue"></span></td>
													<td class="paddingforText"><spring:message code="contract.showfilter.showFilter"/></td>
												</tr>
											</tbody>
										</table>
									</a>
								</div>	
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-7">	
									<div class="pull-right downloadlinks"> 
										<strong><label>
										<label:message messageCode="text.download"/></label></strong> &nbsp;&nbsp;&nbsp;&nbsp;<a class="tertiarybtn contractPgexcel" href="#" id ="contractExcelDownload" >
										<label:message messageCode="contract.excel.label"/></a> <span class="pipesymbol">|</span> &nbsp;<a class="tertiarybtn contractPgpdf" href="#" id ="contractPdfDownload" >
										<label:message messageCode="contract.pdf.label"/></a> 
									</div>
								</div>	
								<div id="desktop-collapse1" class="panel-collapse collapse">
									<div class="panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse">																					
										<div class="col-lg-3 col-xs-12 col-sm-3 col-md-4 borderrightgrey">
											<div class=""><spring:message code="contract.showfilter.status"/></div>
											<div class="checkbox checkbox-info checkboxmargin">
												<input id="1" class="styled select_filter 1" type="checkbox" title="check" name = "statusFilter" >
												<label for="1">
													  <spring:message code="contract.showfilter.active"/> (<span class="activeCount"></span>) 
												</label>
											</div>
											<div class="checkbox checkbox-info checkboxmargin">
												<input id="0" class="styled select_filter 0" type="checkbox" name = "statusFilter">
												<label for="0">
												 <spring:message code="contract.showfilter.discontinued"/>  (<span class="discontinueCount"></span>)
												</label>
											</div>
										</div>					
										<div class="col-lg-9 col-xs-12 col-sm-9 col-md-4">
											<div class=""><spring:message code="contract.showfilter.contractType"/> </div>
											<div class="checkbox checkbox-info checkboxmargin">
												<input id="Z19" class="styled select_filter Z19" type="checkbox" name = "contractTypeFilter" >
												<label for="Z19">
												<spring:message code="contract.showfilter.bid"/> (<span class="bidCount"></span>)
												</label>
											</div>
											<div class="checkbox checkbox-info checkboxmargin">
												<input id="Z20" class="styled select_filter Z20" type="checkbox" name = "contractTypeFilter" >
												<label for="Z20">
													<spring:message code="contract.showfilter.commericalAgreement"/>  (<span class="commercialCount"></span>)
												</label>
											</div>
										</div>																		
									</div>
								</div>																														
							</div>
														
						<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table-lines">
							<thead>
								<tr>
									<th class="text-uppercase"><span><label:message messageCode="contract.contractNumber"/></th>
									<th class="text-uppercase"><span><label:message messageCode="contract.tenderNumber"/></span></th>
									<th class="text-uppercase"><span><label:message messageCode="contract.indirectCustomer"/></th>
									<th class="text-uppercase"><span><label:message messageCode="contract.expirationDate"/></th>
									<th class="no-sort text-uppercase"><span><label:message messageCode="contract.contractType"/></th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${noResultFlag}">
										 <%-- <div class="buttonWrapper">
										 <span class = "orderHistorySearchText"><label:message messageCode="contract.noRecordsMessage"/></span>
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
											<tr class="myContractsRow ${contractClass} contractRowClick gotomycontractdetail "  id="headerStatus_${contract.active}_${contract.eccContractNum}_desktop">
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
																<!-- Modified by Archana for AAOL-5513 -->
																	<span class="txtFont"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
																	<fmt:formatDate value="${contract.endDate}" pattern="${dateformat}" /></span>
																</c:when>
																<c:otherwise>
																<!-- Modified by Archana for AAOL-5513 -->
																	<span class="txtFont"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
																	<fmt:formatDate value="${contract.endDate}" pattern="${dateformat}" /></span>
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
															<span class="txtFont contractType">${contract.contractOrderReason}</span>             		
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
						<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
							<!-- <div class="row horizontal-line"></div> -->
							<div class="SortingTable sort-by-contract-mobile-holder" id="ordersTable_length">
							</div>
							<table id="ordersTablemobile" class="table table-bordered table-striped sorting-table-lines bordernone">
								<thead>
									<tr>
										<th class="text-left">
										<label:message  messageCode="contract.contractNumber" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${contractList}" var="contract" varStatus="Count">
										<c:choose>
											<c:when test="${Count.count  mod 2 == 0}">
												<c:set var="contractClass" value="even"> </c:set>
											</c:when>
											<c:otherwise>
												<c:set var="contractClass" value="odd">
												</c:set>
											</c:otherwise>
										</c:choose>
										<!-- <tr> class="myContractsRow ${contractClass} contractRowClick gotomycontractdetail"  -->
										<tr class="myContractsRow ${contractClass}" id="headerStatus_${contract.active}_${contract.eccContractNum}_mobile">
											<td>
												<a data-toggle="collapse" data-parent="#accordion" href="#contract-mobi-collapse${Count.count}" class="ref_no toggle-link panel-collapsed">
													<span class="glyphicon glyphicon-plus"></span>
												</a> 
												<c:url var="contractDetails" value="/my-account/contract/getContractDetails/${contract.eccContractNum}"></c:url> 
												<a class ="mobileContractRow"  href="${contractDetails}"> ${contract.eccContractNum} </a>

												<div id="contract-mobi-collapse${Count.count}"
													class="panel-collapse collapse">
													<div class="panel-body details">
														<div>
															<label:message messageCode="contract.tenderNumber" />
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
															<label:message messageCode="contract.indirectCustomer" />
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
															<label:message messageCode="contract.expirationDate" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.endDate && '' ne contract.endDate}">
																	<c:choose>
																		<c:when test="${'en' eq sessionlanguage}">
																			<span class="txtFont"><!-- Modified by Archana for AAOL-5513 -->
																			<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
																			<fmt:formatDate
																					value="${contract.endDate}" pattern="${dateformat}" /></span>
																		</c:when>
																		<c:otherwise><!-- Modified by Archana for AAOL-5513 -->
																			<span class="txtFont"><c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
																			<fmt:formatDate
																					value="${contract.endDate}" pattern="${dateformat}" /></span>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<span class="txtFont">&nbsp;</span>
																</c:otherwise>
															</c:choose>
														</div>
														<div>
															<label:message messageCode="contract.contractType" />
														</div>
														<div>
															<c:choose>
																<c:when
																	test="${ not empty contract.contractOrderReason && '' ne contract.contractOrderReason}">
																	<span class="txtFont contractType">${contract.contractOrderReason}</span>
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
								</tbody>
							</table>
						</div>
						<!--Accordian Ends here -->
					</div>
					<!--Accordian Ends here -->
				</div>
			</div>
			<input type="hidden" name="loadNoOfRecords" id="loadNoOfRecordsHidden" />
			<input type="hidden" name="scrollPos" id="scrollPos" value="${scrollPos}" />
		</form:form>
		<input type="hidden" value="{contract.status}" id="contractLineStatus" />
		<input type="hidden" value="" id="currViewScreenName" />
</template:page>