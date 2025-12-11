<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/nav/breadcrumb" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

<template:page pageTitle="${pageTitle}">
	<div id="searchResults" class="shipmentContainer">
		<!-- Breadcrunb start here -->
		<div class="col-lg-12 col-md-12 catalogLanding">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
			</div>
			<div id="infoMessages">
				<div class="marTop20" style="display: none;">
					<div class="info positive">
						<p>
							<spring:message code="product.add.zero.qty.msg" />
						</p>
					</div>
				</div>
			</div>
			<c:if test="${specialCustomer eq true}">
				<div id="infoMessages">
					<div class="marTop20">
						<div class="info positive">
							<p>
								<spring:message code="header.information.account.customer.group" />
							</p>
						</div>
					</div>
				</div>
			</c:if>

			<div class="row">
				
				<div class="col-xs-12 resultsTxt">
					<h2>${breadcrumbs[fn:length(breadcrumbs)-1].name}</h2>  
				</div>
			</div>
			<!-- <div class="row" style="margin-bottom:20px">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<button class="btn btnclsactive pull-right">Add all selected items to cart</button>										
				</div>	
			</div> -->
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="hidden-xs table-padding"><c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}" var="isMddSite"></c:set>
					<div class="flitersPanel text-left padding0" style="display:none">
							<a data-toggle="collapse" data-parent="#accordion"	href="#collapse1" class="ref_no toggle-link panel-collapsed skyBlue">
								<table>
									<tbody>
										<tr>
											<td class="padding0"><span
												class="glyphicon glyphicon-plus skyBlue"></span></td>
											<td class="paddingforText"><spring:message code="product.search.ShowFilters" /></td>
										</tr>
									</tbody>
								</table>
							</a>
							<div class="pull-right downloadlinks">
							<form:form class="searchSortForm" name="searchSortForm" method="get"  commandName="searchSortForm" >
									<input type="hidden" name="fullCount" value="${searchPageData.pagination.totalNumberOfResults}" >
									<common:downloadResultsTag/>
									<c:catch var="errorException">
									<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/>
									<input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>
									</c:catch> 
									<input style="display:none;" hidden="true"  id="showMoreCounter" name="showMoreCounter" value="${showMoreCounter}" />
									<input style="display:none;" hidden="true"  id="showMore" name="showMore" value="${searchSortForm.showMore}" />
									</form:form>
							
							</div>
							
							<div id="collapse1" class="panel-collapse collapse">
											<div class="panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse ">
												<nav:facetNavRefinements pageData="${searchPageData}" />
											</div>
										</div>
						</div>
						<div class="SortingTable" id="ordersTable_length">
							<form:form class="searchSortForm" name="searchSortForm" method="get"  commandName="searchSortForm" >
							<label for="sortby"><spring:message code="product.search.sort"/></label>
							<select id="sortby" name="sortCode">
								<c:forEach items="${searchPageData.sorts}" var="sort">
									<option value="${sort.code}" ${sort.selected ? 'selected="selected"' : ''}>
										<c:if test="${not empty sort.name}">
											${sort.name}
										</c:if>
									</option>
								</c:forEach>
							</select>
							
					    <input type="hidden" name="fullCount" value="${searchPageData.pagination.totalNumberOfResults}" >
						<c:catch var="errorException">
							<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/>
							<input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>
						</c:catch> 
						<input style="display:none;" hidden="true"  id="showMoreCounter" name="showMoreCounter" value="${showMoreCounter}" />
						<input style="display:none;" hidden="true"  id="showMore" name="showMore" value="${searchSortForm.showMore}" />
							</form:form>
						</div>

						<div class="row" id="selectAllProductCheckboxRow">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12" id="selectAllProductCheckboxCol">
								<div class="checkbox checkbox-info">
									<input id="select-all" class="styled" type="checkbox">
									<label for="select-all">Select All</label>
								</div>
							</div>
							<c:if test="${userType ne 'VIEW_ONLY'}">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12" style="margin-top: 8px">
								<button class="btn btnclsactive pull-right add-all-item-btn">Add all selected items to cart</button>
							</div>
							</c:if>	
						</div>

						<table id="ordersTable"	class="table table-bordered table-striped sorting-table">
							<thead>
								<tr>
									<th class="no-sort"></th>
									<th class="no-sort"></th>
									<th class="no-sort"></th>
									<th class="no-sort"></th>
								</tr>
							</thead>
							<tbody>
								
								<!-- Table Data Start here -->
								
								<c:forEach items="${searchPageData.results}" var="product"	varStatus="status">
									<product:productListerItemForCatalog product="${product}"	rowId="${status.index %2 == 0 ? 'even' : 'odd'}"	index="${status.index}" isMddSite="${isMddSite}" />
								</c:forEach>
								<!-- Table Data End here -->
							</tbody>
						</table>

					</div>
				</div>
			</div>
			<!-- Table collapse for mobile device-->
			<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
										<div class="SortingTable" id="ordersTable_length">
										<label><strong><spring:message code="product.search.SortBy" /></strong>
										<select name="ordersTable_length" aria-controls="ordersTable" class="form-control input-sm">
										<option value="Name (ascending)"><spring:message code="product.search.ascending"/></option>
										<option value="Name (descending)"><spring:message code="product.search.descending"/></option>
										</select>
										</label>
										</div>
										<!-- //Mobile/Tablet defect #20, 21, 22 -->
										<div class="flitersPanel-mobile text-left padding0" style="background-color:white;border-top:1px solid #f2f2f2">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed skyBlue">
														<table>
															<tbody>
																<tr>
																	<td class="padding0"><span class="glyphicon glyphicon-plus skyBlue"></span></td>
																	<td class="paddingforText"><spring:message code="product.search.ShowFilters" /></td>
																</tr>
															</tbody>
														</table>
														</a>
														<div class="pull-right downloadlinks">
															<form:form id="searchSortForm" name="searchSortForm" method="get"  commandName="searchSortForm" >	
																<div class="downloadPlaceHolder">
																	<strong> 
																		<spring:message code="product.search.download" />
																	</strong> 
																	<input type="submit" class="tertiarybtn pdfdownloadlinks" value="<spring:message code='cart.confirmation.excel'/>" name="downloadType" />
																   <span class="pipesymbol">|</span> 
																   <input type="submit" class="tertiarybtn marginRight pdfdownloadlinks" value="<spring:message code='category.pdf'/>" name="downloadType" />		
																</div>
																<c:catch var="errorException">
											                	<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/>
												               <input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>
												            </c:catch> 
																<input style="display:none;" hidden="true"  id="showMoreCounter" name="showMoreCounter" value="${showMoreCounter}" />
																<input style="display:none;" hidden="true"  id="showMore" name="showMore" value="${searchSortForm.showMore}" />
															</form:form>
													 </div>
													<!-- Filters Starts here -->
														<div id="collapse2" class="panel-collapse collapse">
															<div class="panel-body details col-lg-12 col-xs-12 col-sm-12 col-md-12 paddingforcollapse ">																					
																<nav:facetNavRefinements pageData="${searchPageData}" />					
																
																
															</div>
														</div>
												<!-- START - //Mobile/Tablet defect #20, 21, 22 -->
											</div>
										<table id="ordersTablemobile" class="table table-bordered table-striped sorting-table bordernone">
											<thead class="hidden-xs">
													<tr>
														<th class="text-left no-sort"></th>
														<th class="text-left no-sort"></th>
													</tr>
											</thead>
											<tbody>
											
										<!-- END - //Mobile/Tablet defect #20, 21, 22 -->
												<c:forEach items="${searchPageData.results}" var="product"	varStatus="status">
									<product:productListerItemForMobile product="${product}"	rowId="${status.index %2 == 0 ? 'even' : 'odd'}"	index="${status.index}" isMddSite="${isMddSite}" />
								</c:forEach>
											</tbody>
										</table>								
									</div> 
			<div class="row" style="margin-top:20px">
				<c:if test="${userType ne 'VIEW_ONLY'}">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<button class="btn btnclsactive pull-right add-all-item-btn">Add all selected items to cart</button>										
				</div>	
				</c:if>	
			</div>
			
			<!--Accordian Ends here -->
		</div>
		
		 <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="contractPopuppage">
				<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
					<div class="modal-dialog modalcls">
						<div class="modal-content popup">
							<div class="modal-header">
							  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
							  <h4 class="modal-title selectTitle"><spring:message code="contract.page.addprod"/></h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-danger">
									<div class="panel-heading">
										<h4 class="panel-title">
										<table class="contract-popup-table">
											<tr>
											<td><div class="glyphicon glyphicon-ok"></div></td>
											<td><div class="info-text"><spring:message code="contract.page.infotext"/></div></td>														
											</tr>
										</table>
										</h4>
									</div>
								</div>													
								<div><spring:message code="contract.page.msg"/></div>
								<div class="continueques"><spring:message code="contract.page.continue"/></div>
							</div>											
							<div class="modal-footer ftrcls">
								<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="cancel-btn-addtocart"><spring:message code="cart.common.cancel"/></a>
								<button type="button" class="btn btnclsactive" data-dismiss="modal" id="accept-btn-addtocart" ><spring:message code="contract.page.accept"/></button>
							</div>
						</div>
					</div>
				</div>
		</div>		
		<!--  Add to cart Modal pop-up to identify  contract or non contract end -->
	</div>
  <!-- AAOL-2406 start-->
<div id="replacement-line-item-holder" style="display: none;"></div>
<!-- AAOL-2406 end-->
</template:page>