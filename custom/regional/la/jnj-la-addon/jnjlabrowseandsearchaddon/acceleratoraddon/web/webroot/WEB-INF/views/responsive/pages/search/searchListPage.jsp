<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/product" %>
<%@ taglib prefix="productLa" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/product" %>
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
<%@ taglib prefix="commonLa" tagdir="/WEB-INF/tags/addons/jnjlabrowseandsearchaddon/responsive/common" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>


<templateLa:page pageTitle="${pageTitle}">

	<div id="searchResults" class="shipmentContainer">
		<!-- Breadcrunb start here -->
		<div class="col-lg-12 col-md-12 catalogLanding">
			<div class="row">
				<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
			</div>
			<div class="panel-group" id="isMinQtyMsgShow" style="display:none;">
				<div class="panel panel-success">
					<div class="panel-heading" style="font-size:12px">

							<span><span class="glyphicon glyphicon-ok"></span>
								<spring:message code="product.add.zero.qty.msg" /></span>

					</div>
				</div>
			</div>
			<div class="panel-group" id="successMsgShow" style="display:none;">
				<div class="panel panel-success">
					<div class="panel-heading" style="font-size:12px">

							<span><span class="glyphicon glyphicon-ok"></span>
								<spring:message code="success" /></span>

					</div>
				</div>
			</div>
					<div class="panel-group" id="displayMessageError" style="display:none;">
					<div class="panel panel-danger">
						<div class="panel-heading" id="pdp150Error" style="font-size:16px">
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
            				<div class="col-xs-12 headingTxt content">
            					<spring:message code="product.search.result.textfor" />
            				</div>
            				<div class="col-xs-12 resultsTxt">
            					<spring:message code="product.search.result.for" />
            					"${searchPageData.freeTextSearch}"
            				</div>
            			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12 col-xs-12 ">
					<div class="bg-white-pad">
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12">
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
								<div class="row">
									<label class="pull-left pg-dropdown-label lineHeightShowDDiPad"><spring:message code="reports.purchase.analysis.show"/>&nbsp;&nbsp;</label>
									<select id="lineStatus" class="form-control form-element form-element-select">
										<option value="10" ${searchSortForm.pageSize==10 ? 'selected="selected"' : ''}><spring:message code="pagination.page.size.10"/></option>
                                        <option value="25" ${searchSortForm.pageSize==25 ? 'selected="selected"' : ''}><spring:message code="pagination.page.size.25"/></option>
                                        <option value="50" ${searchSortForm.pageSize==50 ? 'selected="selected"' : ''}><spring:message code="pagination.page.size.50"/></option>
                                        <option value="100" ${searchSortForm.pageSize==100 ? 'selected="selected"' : ''}><spring:message code="pagination.page.size.100"/></option>
                                        <option value="500" ${searchSortForm.pageSize==500 ? 'selected="selected"' : ''}><spring:message code="pagination.page.size.500"/></option>
									</select>
								</div>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 searchSortFormHolder">
							<div class="row">
								<form:form class="searchSortForm" id ="searchSortForm" name="searchSortForm" method="POST"  modelAttribute="searchSortForm" >
									<form:input type="hidden" id="plpPageNumber" name="pageNumber" path="pageNumber" value="${searchSortForm.pageNumber}" />
									<form:input type="hidden" id="plpLastPageNumber" name="lastPageNumber" path="lastPageNumber" value="${searchSortForm.lastPageNumber}" />
									<form:input type="hidden" id="plpPageSize" name="pageSize" path="pageSize" value="${searchSortForm.pageSize}" />
									<label for="sortby" style="font-weight: bold; margin-left:-10px"><spring:message code="product.search.sort"/></label>
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
							</div>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 marginTop15pxiPad">
							<div class="col-lg-8 col-md-9 col-sm-7 col-xs-12 paddingRightLeft0pxiPad">
								<div class="custom-pg-info pull-right" role="status" aria-live="polite">
									<span class="first_row_pg"><strong>${searchSortForm.displayResults}</strong></span> <em><spring:message code="items.list.of"/></em>&nbsp;
									<span class="total_row_pg"><strong>${searchSortForm.totalNumberOfResults}</strong></span>&nbsp;<spring:message code="reports.results"/>
								</div>
							</div>
							<div id="custom-pg-btn" class="col-lg-4 col-md-3 col-sm-5 col-xs-12">
                            <ul class="pagination pull-right">
									<li class="paginate_button previous" id="custom-pg-btn_previous">
										<a href="javascript:void(0)" aria-controls="datatab-desktop" data-dt-idx="0" tabindex="0">&lt;</a>
									</li>
									<li class="paginate_button next" id="custom-pg-btn_next">
										<a href="javascript:void(0)" aria-controls="datatab-desktop" data-dt-idx="1" tabindex="0">&gt;</a>
									</li>
								</ul>
							</div>
						</div>
						<div class="clear">
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
						<div class="hidden-xs table-padding"><c:set value="${'MDD' eq JNJ_SITE_NAME ? true : false}" var="isMddSite"></c:set>
					<%-- <div class="SortingTable" id="ordersTable_length">
							<form:form class="searchSortForm" name="searchSortForm" method="get"  modelAttribute="searchSortForm" >
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
						</div> --%>
						<div class="flitersPanel text-left padding10px" style="display:none;">
							<a data-bs-toggle="collapse" data-bs-parent="#accordion"	href="#collapse1" class="ref_no toggle-link panel-collapsed skyBlue">
								<table>
									<tbody>
										<tr>
											<td class="padding0"><span
												class="bi bi-plus-lg" style="-webkit-text-stroke: 1px;"></span></td>
											<td class="paddingforText"><spring:message code="product.search.ShowFilters" /></td>
										</tr>
									</tbody>
								</table>
							</a>
							<div class="pull-right downloadlinks">
							<form:form class="searchSortForm" name="searchSortForm" method="POST"  modelAttribute="searchSortForm" >
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

						<!-- Checkbox end -->
			         <table id="plpTable" class="table table-bordered table-striped">

							<tbody>

								<!-- Table Data Start here -->

								<c:forEach items="${searchPageData.results}" var="product"	varStatus="status">
									<productLa:productListerItem product="${product}"	rowId="${status.index %2 == 0 ? 'even' : 'odd'}"	index="${status.index}" isMddSite="${isMddSite}" />
								</c:forEach>
								<!-- Table Data End here -->
							</tbody>
						</table>

					</div>
				</div>
			</div>
			<!-- Table collapse for mobile device-->
			<div class="row Subcontainer d-none d-md-none d-sm-block">

									    <%-- <div class="SortingTable" id="ordersTable_length">
										<label><strong><spring:message code="product.search.SortBy" /></strong>
										<select name="ordersTable_length" aria-controls="ordersTable" class="form-control input-sm">
										<option value="Name (ascending)"><spring:message code="product.search.ascending"/></option>
										<option value="Name (descending)"><spring:message code="product.search.descending"/></option>
										</select>
										</label>
										</div> --%>

										<!-- //Mobile/Tablet defect #20, 21, 22 -->
										<div class="flitersPanel-mobile text-left padding0" style="background-color:white;border-top:1px solid #f2f2f2">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed skyBlue">
														<table style="margin-top:10px">
															<tbody>
																<tr>
																	<td class="padding0"><span class="glyphicon glyphicon-plus skyBlue"></span></td>
																	<td class="paddingforText"><spring:message code="product.search.ShowFilters" /></td>
																</tr>
															</tbody>
														</table>
														</a>
														<div class="pull-right downloadlinks downloadLinksSearchResults">
															<form:form id="searchSortForm" name="searchSortForm" method="get"  modelAttribute="searchSortForm" >
																<div class="downloadPlaceHolder">
																	<strong>
																		<spring:message code="product.search.download" />
																	</strong>
																	<input type="submit" class="tertiarybtn pdfdownloadlinks" value="XLS" name="downloadType" />
																   <span class="pipesymbol">|</span>
																   <input type="submit" class="tertiarybtn marginRight pdfdownloadlinks" value="PDF" name="downloadType" />
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
										<table id="ordersTablemobile" class="col-xs-12 table table-bordered table-striped  bordernone">
											<thead class="hidden-xs">
													<tr>
														<th class="text-left no-sort"></th>
														<th class="text-left no-sort"></th>
													</tr>
											</thead>
											<tbody>

										<!-- END - //Mobile/Tablet defect #20, 21, 22 -->
												<c:forEach items="${searchPageData.results}" var="product"	varStatus="status">
									<productLa:productListerItemForMobile product="${product}"	rowId="${status.index %2 == 0 ? 'even' : 'odd'}"	index="${status.index}" isMddSite="${isMddSite}" />
								</c:forEach>
											</tbody>
										</table>
									</div>


			<!--Accordian Ends here -->
		</div>
		 <!-- Add to cart Modal pop-up to identify  contract or non contract start-->
			<div  id="contractPopuppage">
				<div class="modal fade" id="contractpopup" role="dialog" data-firstLogin='true'>
					<div class="modal-dialog modalcls">
						<div class="modal-content popup">
							<div class="modal-header">
							  <button type="button" class="close clsBtn" data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
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
								<a href="#" class="pull-left canceltxt" data-bs-dismiss="modal" id="cancel-btn-addtocart"><spring:message code="cart.common.cancel"/></a>
								<button type="button" class="btn btnclsactive" data-bs-dismiss="modal" id="accept-btn-addtocart" ><spring:message code="contract.page.accept"/></button>
							</div>
						</div>
					</div>
				</div>
		</div>
		<!--  Add to cart Modal pop-up to identify  contract or non contract end -->
	</div>

</templateLa:page>