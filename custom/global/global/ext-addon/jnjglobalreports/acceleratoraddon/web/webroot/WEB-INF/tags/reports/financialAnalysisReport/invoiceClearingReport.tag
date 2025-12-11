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
<c:url value="/reports/financialAnalysis/invoiceClearing" var="invoiceClearingURL" />
<input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> 
<input id="selectedText" type="hidden" value="<spring:message code='reports.backorder.selected' />" />
<input id="allText" type="hidden" value="<spring:message code='reports.backorder.all' />" />
<input type="hidden" id="accountid" value="${currentAccountId}" />
<input type="hidden" id="originalFormAction" value="${invoicePastDueURL}" /> 
<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>

	<div class="col-lg-12 col-md-12">
	<form:form id="invoiceClearingForm" action="${invoiceClearingURL}" novalidate="novalidate" modelAttribute="jnjGTInvoiceClearingForm" method="POST">
	<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
	<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />	
	<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
	<div class="row jnj-panel-body">
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
					<div class="row marginbottomipad25px">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 start">
							<label class="pull-left form-label form-label-select boldtext"><spring:message code='reports.financial.start.date' /></label>												
								<div class="input-group form-element form-element-select">
									<form:input id="invoiceStartDate" name="invoiceStartDate" path="startDate" placeholder="Select date" class="date-picker form-control" type="text"></form:input>
									<label for="invoiceStartDate" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
								</div>
						</div>																					
					</div>
				</div>	
					
				<div class="form-group col-lg-4 col-md-8 col-sm-12 col-xs-12 companybrand">
					<div class="row">									
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12  margintopipad20px">
							<label class="pull-left form-label form-label-select boldtext" for="invoiceNumber"><spring:message code='reports.financial.invoicenumber' /></label>												
							<form:input type="text" id="invoiceNumber" class="form-control form-element-select" path="invoiceNumber"></form:input>
						</div>
					</div>
				</div>
				<div class="form-group col-lg-4 col-md-8 col-sm-12 col-xs-12 companybrand">
					<div class="row">									
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12  margintopipad20px">
							<label class="pull-left form-label form-label-select boldtext" for="receiptNumber"><spring:message code='invoice.receipt.num' /></label>												
							<form:input type="text" id="receiptNumber" class="form-control form-element-select" path="receiptNumber"></form:input>
						</div>
					</div>
				</div>		
			</div>
		</div>			
		<%-- <div class="form-group col-lg-4 col-md-8 col-sm-12 col-xs-12 companybrand">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12  margintopipad20px">
					<label class="pull-left form-label small-label boldtext"><spring:message code='reports.purchase.analysis.popup.status' /></label>																
					<div class="small-label-dropdown form-element">
						<form:select data-width="100%" id="status" path="status">
							<option><spring:message code='text.account.order.status.display.completed' /></option>																
						</form:select>
					</div>	
				</div>
			</div>
		</div>	 --%>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
					<div class="row marginbottomipad25px">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 start">
							<label class="pull-left form-label form-label-select boldtext"><spring:message code='reports.financial.end.date' /></label>												
								<div class="input-group form-element form-element-select">
									<form:input id="invoiceEndDate" name="invoiceEndDate" path="endDate" placeholder="Select date" class="date-picker form-control" type="text"></form:input>
									<label for="invoiceEndDate" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
								</div>
						</div>																					
					</div>
				</div>	
			</div>
		</div>									
	</div>			
	
	<div class="row jnj-panel-footer buttonWrapperWithBG">
		<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder buttonWrapperWithBG">
			<span class="link-txt boldtext"><spring:message code='reports.download.label' />
			</span>
			<a href="#" id="InvoiceClearingReportExcel" class="tertiarybtn marginRight excel"><spring:message code='reports.excel.label' /></a> 
			<%-- | <a href="#" id="InvoiceClearingReportPdf"  class="tertiarybtn pdf"><spring:message code='reports.pdf.label' /></a> --%>
			
			<div>
			<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
		</div>
		</div>
		<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
			<div class="pull-right btn-mobile">
				<button id="invoiceClearingReport" type="submit" class="secondarybtn btn btnclsactive generatereport pull-right" href="javascript:;">
			 		<spring:message code='reports.search.labelUX' />
			 	</button>
				<a class="tertiarybtn  btn btnclsnormal reset" href="${invoicePastDueURL}">
			 		<spring:message code='reports.reset.label' />
			 	</a>
			</div>
		</div>
	</div>
	</form:form>
	</div>
</div>


<div id="changeAccountPopupContainer"></div>
<c:if test="${invoiceClearingResponse eq null && noDataFound eq 'true'}">
		<div class="info positive reportBody boxshadow no-data-report">
			<spring:message code='reports.table.no.data' />
		</div>
</c:if>
<c:if test="${invoiceClearingResponse ne null}"> 
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class="hidden-xs jnj-panel-for-table mainbody-container">
		
			<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
				<thead>
					<tr>
						<th class="text-uppercase"><spring:message code='reports.invoicedue.receiptnum' /></th>
						<th class="text-uppercase"><spring:message code='reports.financial.paymentdate' /></th>
						<th class="text-uppercase"><spring:message code='reports.financial.table.product.invoiceNo' /></th>
						<th class="text-uppercase"><spring:message code='reports.invoicedue.invoicedate' /></th>
						<th class="text-uppercase"><spring:message code='reports.invoicedue.soldtoaccount' /></th>
						<th class="text-uppercase"><spring:message code='reports.invoicedue.soldtoname' /></th>	
						<th class="text-uppercase"><spring:message code='reports.invoicedue.salesdocnum' /></th>	
						<th class="text-uppercase"><spring:message code='reports.financial.table.product.customerPoNo' /></th>													
						<th class="text-uppercase"><spring:message code='reports.financial.table.product.currency' /></th>
						<%-- <th class="text-uppercase"><spring:message code='reports.purchase.analysis.popup.status' /></th>	 --%>					
						<th class="text-uppercase"><spring:message code='reports.invoicedue.totalamount' /></th>
						<th class="text-uppercase"><spring:message code='reports.invoicedue.outstandingamount' /></th>
						
						
					
						
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${invoiceClearingResponse}" var="response">
						<tr>
							<td class="text-left-cl">${response.receiptNumber}</td>
							<td class="text-left-cl">${response.paymentDate}<!-- Modified by Archana for AAOL-5513 -->
							<%-- <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
							<fmt:formatDate value="${response.paymentDate}" pattern="${dateformat}"/> --%>
							</td>
							<td class="text-left-cl">${response.invoiceNum}</td>
							<td class="text-left-cl">${response.billingDate}<!-- Modified by Archana for AAOL-5513 -->
							<%-- <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
							<fmt:formatDate value="${response.billingDate}" pattern="${dateformat}"/> --%>
							</td>
							<td class="text-left-cl">${response.soldToAccNum}</td>
							<td class="text-left-cl">${response.soldToAccName}</td>
							<td class="text-left-cl">${response.salesDocNum}</td>
							<td class="text-left-cl">${response.customerPoNum}</td> 
							<td class="text-left-cl">${response.currency}</td>
							<%-- <td class="text-left-cl tabdata1">${response.status}</td>	 --%>						
							<td class="text-left-cl">${response.invoiceTotalAmount}</td>
							<td class="text-left-cl">${response.openAmount}</td>
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