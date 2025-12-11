<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="row jnj-panel mainbody-container multiPurchaseReportBlock rdiCont sectionBlock">
		<input id="multipleText" type="hidden" value ="<spring:message code='reports.backorder.multiple' />" />
		<input id="selectedText" type="hidden" value ="<spring:message code='reports.backorder.selected' />" />
		<input id="allText" type="hidden" value ="<spring:message code='reports.backorder.all' />" />
		<c:url value="/reports/purchaseAnalysis/single" var="singlePurchaseURL" />
		<c:url value="/reports/purchaseAnalysis/multi" var="multiPurchaseURL" />
		<c:url value="/reports/purchaseAnalysis/backorder" var="backOrderURL" />
		<c:url value="/reports/inventory" var="inventoryURL" />
		<c:url value="/reports/cutorder" var="cutReportUrl"></c:url>
		<c:url value="/reports/orderAnalysis/dl" var="delieveryListURL" />
		

		<input type="hidden" id="originalFormAction" value="${multiPurchaseURL}" />
		<input type="hidden" id="accountid" value="${currentAccountId}" />
		<input type="hidden" id="selectallFlag" value="${currentAccountId}" />
		
	<div class="col-lg-12 col-md-12">
	<form:form id="multiPurchaseReportForm" action="${multiPurchaseURL}" novalidate="novalidate" modelAttribute="jnjGlobalMultiPurchaseAnalysisReportForm">
	<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
		<div class="row jnj-panel-header">
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
		</div>
		<div class="row jnj-panel-body">
			<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
				<label class="pull-left form-label form-label-select-large boldtext"><spring:message code='reports.report.type'/></label>
				<select class="form-control form-element form-element-select-large" id="reportType">
					 <option value="${singlePurchaseURL}"><spring:message code='reports.purchase.analysis.single' /></option>
					 <option selected="selected" value="${multiPurchaseURL}"><spring:message code='reports.purchase.analysis.multi' /></option>
					<option  value="${backOrderURL}"><spring:message code='reports.backorder.header' /></option>
					
					<c:if test="${inventry eq 'true'}">
					<option  value="${inventoryURL}"><spring:message code='reports.inventory.header.new' /></option>
					</c:if>
					<option value="${cutReportUrl}"><spring:message code='cutReport.label' /></option>
					<option value="${delieveryListURL}"><spring:message code='reports.order.analysis.category.DeliveryList' /></option>
				</select>
				<input id="hddnCurrentAccount" value="${currentAccountId}" type="hidden" />
			</div>
			<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
				<label class="form-label form-label-select-large boldtext territory" for="territory"><spring:message code='reports.purchase.analysis.territory' /></label>
				<select id="territory" name="territory" class="form-control form-element form-element-select-large">
						<option value="All"><spring:message code='reports.purchase.analysis.all.available' /></option>
						<c:forEach items="${territories}" var="territoryValue">
							<c:choose>
								<c:when
									test="${territoryValue eq jnjGTMultiPurchaseAnalysisReportForm.territory}">
									<option value="${territoryValue}" selected="selected">${territoryValue}</option>
								</c:when>
								<c:otherwise>
									<option value="${territoryValue}">${territoryValue}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select>
			</div>
		</div>
		<div class="row jnj-panel-body">
			<div class="col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<div class="row form-mobile-row-gap">
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start form-row-gap um-element-col">
						
						<label class="pull-left form-label form-label-date boldtext" for="startDate"  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /><span class="redStar">*</span></label>
						<div class="input-group form-element form-element-date">
						
							<input type="text"  name="startDate"  placeholder="<spring:message code='cart.common.selectDate'/>"   value="${startDate}" id="reports-startDate" path="startDate" class=" date-picker form-control required" />
							<label	for="reports-startDate" class="input-group-addon btn"> 
							 <span	class="glyphicon glyphicon-calendar"></span>
							 </label>
						</div>
					<div><div class="registerError"></div></div>
					</div>
					<div
						class="col-lg-12 col-md-12 col-sm-6 col-xs-12  end   um-element-col">
						<%-- <label class="pull-left form-label form-label-date boldtext ipadterritory">End</label> --%>
						<label class=" pull-left form-label form-label-date boldtext  getErrorMessage" for="endDate" data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /><span class="redStar">*</span></label>
						<div class="input-group form-element form-element-date">
							<spring:message code='cart.common.selectDate' var="selDate"/>
							<!-- <input id="date-picker-1" name="toDate" placeholder="<spring:message code='cart.common.selectDate'/>" class="date-picker form-control" type="text"> -->
							<form:input type="text" name="toDate" placeholder="${selDate}"  data-msg-required="" value="${endDate}" path="endDate" id="reports-endDate" class=" date-picker form-control iconCalenderReport required" /> 
								<label	for="reports-endDate" class="input-group-addon btn">
								<span	class="glyphicon glyphicon-calendar"></span> 
								</label>
					
						</div>
						<div class="registerError"></div>
					</div>
				</div>
			</div>
			<div class="form-group col-lg-9 col-md-9 col-sm-12 col-xs-12 companybrand">
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						<label class="pull-left form-label form-label-select boldtext textheightipad" for="operatingCompany"><spring:message code='reports.purchase.analysis.operating.company' /></label> 
						<select id="operatingCompany" name="operatingCompany" class="form-control form-element form-element-select">
								<option value="All"><spring:message
										code='reports.purchase.analysis.all' /></option>
								<c:forEach items="${operatingCompany}" var="companyValue">
									<c:choose>
										<c:when
											test="${companyValue.key eq jnjGTMultiPurchaseAnalysisReportForm.operatingCompany}">
											<option value="${companyValue.key}" selected="selected">${companyValue.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${companyValue.key}">${companyValue.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 analysisvariable">
						<label 	class="pull-left form-label form-label-select boldtext textheightipad ipadterritory" for="analysisAvailable"><spring:message code='reports.purchase.analysis.analysis.variable'/></label>
						 <select id="analysisAvailable" name="analysisVariable"	class="form-control form-element form-element-select">
								<c:forEach items="${analysisVariable}" var="analysisValue">
									<c:choose>
										<c:when
											test="${analysisValue.key eq jnjGlobalMultiPurchaseAnalysisReportForm.analysisVariable}">
											<option value="${analysisValue.key}" selected="selected">${analysisValue.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${analysisValue.key}">${analysisValue.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>
						</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px orderedfrom">
						<label	class="pull-left form-label form-label-select boldtext textheightipad"><spring:message code='reports.purchase.analysis.ordered.from' /></label> 
						<select id="orderedFrom" name="orderedFrom" 	class="form-control form-element form-element-select">
								<option value="All"><spring:message
										code='reports.purchase.analysis.all' /></option>
								<c:forEach items="${orderedFrom}" var="orderValue">
									<c:choose>
										<c:when
											test="${orderValue.key eq jnjGlobalMultiPurchaseAnalysisReportForm.orderedFrom}">
											<option value="${orderValue.key}" selected="selected">${orderValue.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${orderValue.key}">${orderValue.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>
						</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px">
						<label	class="pull-left form-label form-label-select boldtext ipadterritory"><spring:message code='reports.purchase.analysis.products.display' /></label>
						<select id="productDisplay" name="productsToDisplay" class="form-control form-element form-element-select">
										<option value="All"><spring:message code='reports.purchase.analysis.all' /></option>
										<c:forEach items="${productDisplay}" var="productValue">
											<c:choose>
												<c:when
													test="${productValue.key eq jnjGTMultiPurchaseAnalysisReportForm.productsToDisplay}">
													<option value="${productValue.key}" selected="selected">${productValue.value}</option>
												</c:when>
												<c:otherwise>
													<option value="${productValue.key}">${productValue.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										
						</select>
					</div>
					
					<div class="franchiseCode col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px">
								<label class="pull-left form-label form-label-select boldtext paddingright0px ipadterritory" for="franchise"><spring:message code='reports.purchase.analysis.franchise' />&nbsp;</label> 
								
									<select id="franchise" name="franchiseDivCode"  class="form-control form-element form-element-select">
									  <option value="All" ><spring:message code='reports.purchase.analysis.all' /></option>
									  <c:forEach items="${franchiseCodes}" var="code">
											<c:choose>
												<c:when
													test="${code.key eq jnjGTMultiPurchaseAnalysisReportForm.franchiseDivCode}">
													<option value="${code.key}" selected="selected">${code.value}</option>
												</c:when>
												<c:otherwise>
													<option value="${code.key}">${code.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								
							</div>
				</div>
			</div>
		</div>
			</form:form>
		<div class="row jnj-panel-footer buttonWrapperWithBG">
			<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder buttonWrapperWithBG">
				<span class="link-txt boldtext"><spring:message code='reports.download.label' />
				</span>
				<a href="#" class="tertiarybtn marginRight excel">
				 			<spring:message code='reports.excel.label' />
				 		</a> 
				| <a href="#" class="tertiarybtn pdf">
				 			<spring:message code='reports.pdf.label' />
				 		</a>
			</div>
			<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
				<div class="pull-right btn-mobile">
					<a id="multiPurchaseReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="javascript:;">
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
<c:if test="${jnjGTMultiPurchaseOrderReportResponseDataList eq null && noDataFound eq 'true'}">
		<div class="info positive reportBody boxshadow no-data-report">
			<spring:message code='reports.table.no.data' />
		</div>
	</c:if>
 <c:if test="${jnjGTMultiPurchaseOrderReportResponseDataList ne null}"> 
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class="hidden-xs jnj-panel-for-table mainbody-container">
		
			<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
				<thead>
					<tr>
						<th class="text-left-cl account-head"><spring:message code='reports.purchase.analysis.account.number' /></th>
						<th class="text-left-cl"><spring:message code='reports.purchase.analysis.product.name' /></th>
						<th class="text-left-cl productcode"><spring:message code='reports.purchase.analysis.product.code' /></th>
						<th class="text-left-cl"><spring:message code='reports.purchase.analysis.amount' /></th>
						<th class="text-left-cl"><spring:message code='reports.purchase.analysis.spending' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.purchase.analysis.qty' /></th>
						<th class="text-left-cl"><spring:message code='reports.purchase.analysis.frequency' /></th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${jnjGTMultiPurchaseOrderReportResponseDataList}" var="response">
						<fmt:parseNumber var="totalspending" type="number"	value="${totalSpendingMap[response.accountNumber]}" />
						<fmt:parseNumber var="amount" type="number"	value="${response.amount.value}" />
						<c:set value="${total + response.amount.value}" var="total"></c:set>
						<c:set value="${(amount/totalspending)*100}" var="percentSpending"></c:set>
						<tr>
						<td class="text-left-cl tabdata1">${response.accountNumber}</td>
						<td class="text-left-cl">${response.productName}</td>
						<td class="text-left-cl">${response.productCode}
						<input type="hidden" class="hddnPrdCode" value="${response.productCode}"> <span class="marLeft10">
						 <a href="#" class="showSingleReport"><spring:message code='reports.purchase.analysis.show' />
				         </a>
				        <c:if test="${response.productGTIN ne null}">${response.productGTIN}</c:if><%-- need to check what should be done in the case of UPC --%>
						</td>
						<td class="text-left-cl">
						<sup class="supmd"></sup><strong><format:price priceData="${response.amount}"/></strong>
						</td>
						<td class="text-left-cl"><fmt:formatNumber maxFractionDigits="2" value="${percentSpending}"/>%</td>
						<td class="text-left-cl">${response.unitQuantity} ${response.uom}</td>
						<td class="text-left-cl">${response.orderFrequency}</td>
					</tr>
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
					<c:forEach items="${jnjGTMultiPurchaseOrderReportResponseDataList}" var="response" varStatus="Count">
						<fmt:parseNumber var="totalspending" type="number"	value="${totalSpendingMap[response.accountNumber]}" />
						<fmt:parseNumber var="amount" type="number"	value="${response.amount.value}" />
						<c:set value="${total + response.amount.value}" var="total"></c:set>
						<c:set value="${(amount/totalspending)*100}" var="percentSpending"></c:set>
						 
						<tr class="myReportRow">
							<td>
								<a data-toggle="collapse" data-parent="#accordion" href="#report-mobi-${Count.count}" class="ref_no toggle-link panel-collapsed">
									<span class="glyphicon glyphicon-plus"></span>
								</a> 
								<a class ="mobileContractRow"  href="#"> ${response.accountNumber} </a>

								<div id="report-mobi-${Count.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.product.name" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productName && '' ne response.productName}">
													<span class="txtFont">${response.productName}</span>
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
													<c:if test="${response.productGTIN ne null}">${response.productGTIN}</c:if><%-- need to check what should be done in the case of UPC --%>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.amount" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productName && '' ne response.productName}">
													<span class="txtFont"><sup class="supmd"></sup><strong><format:price priceData="${response.amount}"/></strong></span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>

										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.spending" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productName && '' ne response.productName}">
													<span class="txtFont"><fmt:formatNumber maxFractionDigits="2" value="${percentSpending}"/>%</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.qty" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productName && '' ne response.productName}">
													<span class="txtFont">${response.unitQuantity} ${response.uom}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.frequency" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productName && '' ne response.productName}">
													<span class="txtFont">${response.orderFrequency}</span>
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
</div>
 </c:if> 