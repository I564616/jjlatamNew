<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="row jnj-panel mainbody-container salesReportBlock rdiCont sectionBlock" id="Reportspage">
<c:url value="/reports/orderAnalysis/salesreport" var="salesReportURL" />
		<input id="multipleText" type="hidden" value ="<spring:message code='reports.backorder.multiple' />" />
		<input id="selectedText" type="hidden" value ="<spring:message code='reports.backorder.selected' />" />
		<input id="allText" type="hidden" value ="<spring:message code='reports.backorder.all' />" />
		<input type="hidden" id="originalFormAction" value="${salesReportURL}" />
		<input type="hidden" id="accountid" value="${currentAccountId}" />
<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>
		
	<div class="col-lg-12 col-md-12">
	<form:form id="salesReportAnalysisForm" action="${salesReportURL}" novalidate="novalidate" modelAttribute="JnjGTSalesReportAnalysisForm">
	<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
	<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
	<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
	<input id="hddnCurrentAccount" value="${currentAccountId}" type="hidden" />
		<%-- <div class="row jnj-panel-header">
			<div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
				<div class="amazon">
				<spring:message code='reports.account.label' />&nbsp;<span id="selectedAccountsText">${currentAccountId}</span>
					<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
					<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
						<c:if test="${showChangeAccountLink eq true}">
							<a id="accountSelectionLink" href="javascript:; " class="change">
								<spring:message code='reports.purchase.analysis.change' />
							</a>
						</c:if>
					</div>
			</div>
			<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
				<div class="checkbox checkbox-info float-right-to-none selectchkbox">
					 <input id="check4" class="styled selectAllAccount" type="checkbox"> 
					<label	for="check4"><spring:message code='reports.account.selection.all'/></label> 
				</div>
			</div>
		</div> --%>
		
		<%-- <!-- Start - Category and Report Type content-->
		<div class="row jnj-panel-body">
			<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
				<label class="form-label form-label-select-large boldtext territory" for="category"><spring:message code='reports.order.analysis.category' /></label>
				<select id="category" name="category" class="form-control form-element form-element-select-large">
						<option value="OrderAnalysis"><spring:message code='reports.order.analysis.category.order.analysis' /></option>
						<option value="InventoryAnalysis"><spring:message code='reports.order.analysis.category.Inventory.Analysis' /></option>
						<option value="FinancialAnalysis"><spring:message code='reports.order.analysis.category.Financial.Analysis' /></option>
						<option value="DeliveryList"><spring:message code='reports.order.analysis.category.DeliveryList' /></option>	
				</select>
			</div>
		
		
			<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
				<label class="pull-left form-label form-label-select-large boldtext "><spring:message code='reports.report.type'/></label>
				<select class="form-control form-element form-element-select-large" id="reportType">
					 <option value="${singlePurchaseURL}"><spring:message code='reports.purchase.analysis.single' /></option>
					 <option selected="selected" value="${salesReportURL}"><spring:message code='reports.order.analysis.salereport' /></option>
					 <option  value="${delieveryListURL}"><spring:message code='reports.order.analysis.category.DeliveryList' /></option>
					 <option value="${multiPurchaseURL}"><spring:message code='reports.purchase.analysis.multi' /></option>
					
				</select>
				<input id="hddnCurrentAccount" value="${currentAccountId}" type="hidden" />
			</div>
		</div> --%>
		
<!-- End - Category and Report Type content-->
		
		
	
		<div class="row jnj-panel-body">
<!-- Start - StartDate ,EndDate content-->	
<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
			<div class="row marginbottomipad25px">
				<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start">
					<label class="pull-left form-label form-label-date boldtext" for="startDate"  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /><!-- <span class="redStar">*</span> --></label>											
					   <div class="input-group form-element form-element-date">
						<input type="text"  name="startDate"  placeholder="Select date"   value="${startDate}" id="reports-startDate" path="startDate" class=" date-picker form-control" />
							<label	for="reports-startDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;"> 
							 <i class="bi bi-calendar3"></i>
							 </label>
						</div>
						<div class="registerError"></div>
				</div>					
				
													
				<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px end">
				  <label class=" pull-left form-label form-label-date boldtext ipadterritory  getErrorMessage" for="endDate" data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /><!-- <span class="redStar">*</span> --></label>
					<div class="input-group form-element form-element-date">
						<form:input type="text" name="toDate" placeholder="Select date"  data-msg-required="" value="${endDate}" path="endDate" id="reports-endDate" class=" date-picker form-control iconCalenderReport" /> 
							<label	for="reports-endDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;">
							<i class="bi bi-calendar3"></i> 
							</label>
					</div>
					<div class="registerError"></div>
				</div>
				
				<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
					<label class="pull-left form-label form-label-date boldtext ipadterritory" for="status"><spring:message code='reports.order.analysis.status' /></label>												
					<div class="form-element form-element-date"> 
					<select data-width="100%" id="status" name="status">
						<option value="All" ><spring:message code='reports.purchase.analysis.all' /></option>
						<c:forEach items="${orderStatus}" var="Ordstatus">
						<c:if test="${not empty Ordstatus.name}">
											<c:choose>
												<c:when test="${Ordstatus.code eq JnjGTSalesReportAnalysisForm.status}">
													<option value="${Ordstatus.code}" selected="selected">${Ordstatus.name}</option>
												</c:when>
												<c:otherwise>
													<option value="${Ordstatus.code}">${Ordstatus.name}</option>
												</c:otherwise>
											</c:choose>
											</c:if>
						</c:forEach>
					</select>
					</div>
				</div>
				
				
																	
	</div>
</div>
<!-- End - StartDate ,EndDate content-->	


<!-- Start - Customer SalesNo,Sales Doc No,Invoice No content-->	
				<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 orderedfrom">
								<label class="pull-left salesreport-form-label form-label-select boldtext textheightipad" for="customerPONo"><spring:message code='reports.order.analysis.customerPo.no' /></label> 												
								<input type="text" id="customerPONo" name="customerPONo" class="form-control form-element-select" placeholder="Customer PO No" value="<c:if test='${not empty JnjGTSalesReportAnalysisForm.customerPONo}'>  ${JnjGTSalesReportAnalysisForm.customerPONo} </c:if>" >
							</div>																												

						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px analysisvariable">
							<label 	class="pull-left salesreport-form-label form-label-select boldtext textheightipad ipadterritory" for="salesDocumentNo"><spring:message code='reports.order.analysis.sales.document.no'/></label>												
							<input type="text" id="salesDocNo" name="salesDocNo" class="form-control form-element-select" placeholder="sales Document No" value="<c:if test='${not empty JnjGTSalesReportAnalysisForm.salesDocNo}'>  ${JnjGTSalesReportAnalysisForm.salesDocNo} </c:if>">														
						</div>		
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
							<label class="pull-left form-label form-label-select boldtext"><spring:message code='reports.order.analysis.order.type'/></label>												
							<div class="form-element form-element-select"> 
								<select id="orderType" name="orderType" data-width="100%">
									<option value="All" ><spring:message code='reports.purchase.analysis.all' /></option>
									<c:forEach items="${orderTypes}" var="orderTypes">
														<c:choose>
															<c:when
																test="${orderTypes.code eq JnjGTSalesReportAnalysisForm.orderType}">
																<option value="${orderTypes.code}" selected="selected">${orderTypes.name}</option>
															</c:when>
															<c:otherwise>
																<option value="${orderTypes.code}">${orderTypes.name}</option>
															</c:otherwise>
														</c:choose>
									</c:forEach>
																			
								</select>
							</div>
						</div>																									
	
						<%-- <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px">
							<label 	class="pull-left salesreport-form-label form-label-select boldtext textheightipad ipadterritory" for="invoiceNo"><spring:message code='reports.order.analysis.invoice.no'/></label>												
							<input type="text" id="invoiceNo" name="invoiceNo" class="form-control form-element-select" placeholder="Invoice No" value="<c:if test='${not empty JnjGTSalesReportAnalysisForm.invoiceNo}'>  ${JnjGTSalesReportAnalysisForm.invoiceNo} </c:if>">
						</div>	 --%>
					</div>
			</div>
			
<!-- End - Customer SalesNo,Sales Doc No,Invoice No content-->		


<!-- Start - Delivery No,Product Code,francise Code content-->		
	<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">	
		<div class="row">
			<%-- <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintopipad20px">
				<label class="pull-left salesreport-form-label form-label-select boldtext ipadterritory"><spring:message code='reports.order.analysis.delivery.No'/></label>												
				<input type="text" id="deliveryNo" name="deliveryNo" class="form-control form-element-select" placeholder="Delivery No" value="<c:if test='${not empty JnjGTSalesReportAnalysisForm.deliveryNo}'>  ${JnjGTSalesReportAnalysisForm.deliveryNo} </c:if>">
			</div> --%>
			
			<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintopipad20px orderedfrom">
				<label class="pull-left form-label form-label-select boldtext textheightipad"><spring:message code='reports.order.analysis.productcode'/></label>												
				<input type="text" id="productCode" name="productCode" class="form-control form-element-select" placeholder="Product Code" value="<c:if test='${not empty JnjGTSalesReportAnalysisForm.productCode}'>  ${JnjGTSalesReportAnalysisForm.productCode} </c:if>">
			</div>
			<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px">
			    <label class="pull-left form-label-select boldtext ipadterritory"><spring:message code='reports.order.analysis.franchise.description'/></label>												
					<select id="franchiseDesc" name="franchiseDesc"  class="form-control form-element form-element-select">
							<option value="All"  selected="selected"><spring:message
												code='reports.purchase.analysis.all' /></option>
								<c:forEach items="${categoryData}" var="franchise">
								<c:choose>
									<c:when
										test="${not empty JnjGTSalesReportAnalysisForm.franchiseDesc && JnjGTSalesReportAnalysisForm.franchiseDesc eq franchise.code}">
										<option value="${franchise.code}" selected="selected">${franchise.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${franchise.code}">${franchise.name}</option>
									</c:otherwise>
								</c:choose>
								</c:forEach>
			    </select>
			</div>
	</div>
</div>

<!-- End - Delivery No,Product Code,francise Code content-->		
			
</div>
			
</form:form>

		<div class="row jnj-panel-footer buttonWrapperWithBG">
			<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder buttonWrapperWithBG">
				<span class="link-txt boldtext"><spring:message code='reports.download.label' />
				</span>
				<a href="#" class="tertiarybtn marginRight excel">
				 			<spring:message code='reports.excel.label' />
				 		</a> 
				<!-- | <a href="#" class="tertiarybtn pdf">
				 			<spring:message code='reports.pdf.label' />
				 		</a> -->
				<div>
					<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
				</div>
			</div>
			<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
				<div class="pull-right btn-mobile">
					<a id="salesReportPageSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="javascript:;">
				 		<spring:message code='reports.search.labelUX' />
				 	</a>
					<a class="tertiarybtn  btn btnclsnormal reset" href="${multiPurchaseURL}">
				 		<spring:message code='reports.reset.label' />
				 	</a>
				</div>
			</div>
		</div>
		
		
	</div>
</div>


<div id="changeAccountPopupContainer"></div>
<c:if test="${jnjGTSalesReportResponseDataList eq null && noDataFound eq 'true'}">
		<div class="info positive reportBody boxshadow no-data-report">
			<spring:message code='reports.table.no.data' />
		</div>
	</c:if>
 <c:if test="${jnjGTSalesReportResponseDataList ne null}"> 
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class="hidden-xs jnj-panel-for-table mainbody-container">
		
			<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
				<thead>
					<tr>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.order.type' /></th>
						<th class="text-left-cl productcode"><spring:message code='reports.order.analysis.sales.document.no' /></th>
						<th class="text-left-cl account-head"><spring:message code='reports.financial.table.product.lineItem' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.order.date' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.status' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.customerPo.no' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.productcode' /></th>
						<th class="text-left-cl account-head"><spring:message code='reports.order.analysis.product.description' /></th>
						<th class="text-left-cl productcode"><spring:message code='reports.order.analysis.order.quantity' /></th>
						<th class="text-left-cl productcode"><spring:message code='sales.report.line.backorderqty' /></th><!-- New -->
						<th class="text-left-cl"><spring:message code='reports.order.analysis.UOM' /></th>
						<th class="text-left-cl"><spring:message code='reports.backorder.table.estimatedDeliveryDate' /></th><!-- New -->
						<th class="text-left-cl"><spring:message code='reports.order.analysis.order.delivery.block' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.order.credit.block' /></th>
					</tr>
				</thead>
				<tbody>
				<c:set var="count" value="0" scope="page" />
				<c:forEach items="${jnjGTSalesReportResponseDataList}" var="response">
						<c:forEach items="${response.orderEntryList}" var="orderEntry">
						<tr>
							<td class="text-left-cl">${response.orderType}</td>
							<td class="text-left-cl">${response.salesDocNo}</td>
							<td class="text-left-cl">${orderEntry.lineItem}</td> 
							<td class="text-left-cl">${response.orderDate}</td>
							<td class="text-left-cl">${response.status}</td>
							<td class="text-left-cl">${response.customerPONo} </td>
							<td class="text-left-cl">${orderEntry.productCode} </td>
							<td class="text-left-cl">${orderEntry.description} </td>
							<td class="text-left-cl">${orderEntry.quantity}</td>
							<td class="text-left-cl">${response.backOrderQty}</td>
							<td class="text-left-cl">${orderEntry.UOM} </td>
							<td class="text-left-cl">${response.estDeliveryDate}</td>
							<td class="text-left-cl">${response.deliveryBlock}</td>
							<td class="text-left-cl">${response.creditBlock}</td>
						</tr>
					</c:forEach>
					</c:forEach>
				</tbody>
			</table>
			
		</div>
		
		
		
		<!-- Table collapse for mobile device-->
		<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
			<!-- <div class="row horizontal-line"></div> -->
			<div class="SortingTable" id="ordersTable_length">
			</div>
			<table id="multiReportTablemobile" class="table table-bordered table-striped sorting-table bordernone">
				<thead>
					<tr>
						<th class="text-left">
						<label:message  messageCode="reports.purchase.analysis.account.number" /></th>
					</tr>
				</thead>
				<tbody>
<%-- 					<c:forEach items="${jnjGTSalesReportResponseDataList}" var="response" varStatus="Count">
						 
						<tr class="myReportRow">
							<td>
								<a data-toggle="collapse" data-parent="#accordion" href="#report-mobi-${Count.count}" class="ref_no toggle-link panel-collapsed">
									<span class="glyphicon glyphicon-plus"></span>
								</a> 
								<a class ="mobileContractRow"  href="#"> ${response.accountNumber} </a>

								<div id="report-mobi-${Count.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.type" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.orderType && '' ne response.orderType}">
													<span class="txtFont">${response.orderType}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.product.code" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productCode && '' ne response.productCode}">
													<span class="txtFont">${response.productCode}</span>
													<input type="hidden" class="hddnPrdCode" value="${response.productCode}"> <span class="marLeft10">
													 <a href="#" class="showSingleReport"><spring:message code='reports.purchase.analysis.show' />
													 </a>
													<c:if test="${response.productGTIN ne null}">${response.productGTIN}</c:if>need to check what should be done in the case of UPC
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.sales.document.no" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.salesDocNo && '' ne response.salesDocNo}">
													<span class="txtFont"><sup class="supmd">${response.salesDocNo}</strong></span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>

										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.date" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.orderDate && '' ne response.orderDate}">
													<span class="txtFont">${response.orderDate}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.status" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.status && '' ne response.status}">
													<span class="txtFont">${response.status} </span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.customerPo.no" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.customerPONo && '' ne response.customerPONo}">
													<span class="txtFont">${response.customerPONo}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.product.description" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productDesc && '' ne response.productDesc}">
													<span class="txtFont">${response.customerPONo}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.UOM" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.UOM && '' ne response.UOM}">
													<span class="txtFont">${response.UOM}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
											<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.quantity" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.orderQty && '' ne response.orderQty}">
													<span class="txtFont">${response.orderQty}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
											<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.delivery.block" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.deliveryBlock && '' ne response.deliveryBlock}">
													<span class="txtFont">${response.deliveryBlock}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.credit.block" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.creditBlock && '' ne response.creditBlock}">
													<span class="txtFont">${response.creditBlock}</span>
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
					</c:forEach> --%>
				</tbody>
			</table>
		</div>
		<!--Accordian Ends here -->
	</div>
</div>
 </c:if> 