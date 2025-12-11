<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="financialSummaryTag" tagdir="/WEB-INF/tags/addons/jnjglobalreports/reports/financialAnalysisReport" %>
<!-- AAOL #2421 -->
<c:url value="/reports/financialAnalysis/financialSummary" var="financialSummaryURL" />
<input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> 
<input id="selectedText" type="hidden" value="<spring:message code='reports.backorder.selected' />" />
<input id="allText" type="hidden" value="<spring:message code='reports.backorder.all' />" />
 <input type="hidden" id="originalFormAction" value="${financialSummaryURL}" /> 
 <input type="hidden" id="accountid" value="${currentAccountId}" />
 <input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>
 
			<!-- Start - Body Content -->
<div class="row jnj-panel mainbody-container" id="FinancialAnalysisReport" style="padding-bottom:0px; padding-top:0px">
	<div class="col-lg-12 col-md-12">
<form:form id="financialSummaryReportForm" action="${financialSummaryURL}" modelAttribute="JnjGlobalFinancialSummaryReportForm" method="POST">
<form:input id="downloadType" type="hidden" path="downloadType"/>
<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />	
	<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue"/>	
		<div class="row financialSections">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 leftSidePanel">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
						<div class="subFinancialHead">Account Aging</div>
							<div class="row subFinancialText">
								<div
										class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12 marginTop15px">
										<label class="form-label form-label-select-large">Payer</label>
										<form:input type="hidden" id="accountAgingPayerID" path="accountAgingPayerID"/>
										<select id="accountAgingPayer" class="form-control form-element form-element-select-large">
										<option>Select</option>
											<c:forEach items="${payerid}" var="payerid">
												<option value="${payerid}">${payerid}</option>
											</c:forEach>
										</select>
									</div>
									<div id="accountAgingDiv">
										<!-- div
											class="BalanceSummarysection col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
											<table id="section-1" class="table table-bordered"
												style="margin-bottom: 0px">
												<thead>
													<tr>
														<th class="text-uppercase">Days in Arrears</th>
														<th class="text-uppercase text-right">Due Item</th>
														<th class="text-uppercase text-right">Not Due</th>
													</tr>
												</thead>
												<tbody>
											
												</tbody>
											</table>
										</div> -->
									</div>
							</div>
							<div class="subFinancialHead">Payment Summary</div>
							<div class="row subFinancialText">
								<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12 marginTop15px">
									<label class="form-label form-label-select-large">Payer</label>		
									<form:input type="hidden" id="paymentSummaryPayerID" path="paymentSummaryPayerID"/>										
									<select id="paymentSummaryPayer" class="form-control form-element form-element-select-large">		
									<option>Select</option>																
										<c:forEach items="${payerid}" var="payerid">
												<option value="${payerid}">${payerid}</option>
											</c:forEach>																	
									</select>
								</div>
										<div id="paymentSummaryDiv">	
												
										</div>
							</div>	
					</div>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 rightSidePanel">
				<div class="row">													
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
					
						<div class="subFinancialHead">AR Balance Summary</div>
							<div class="row subFinancialText">
								<div
									class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12 marginTop15px">
									<label class="form-label form-label-select-large">Payer</label> 
									<form:input type="hidden" id="balanceSummaryPayerID" path="balanceSummaryPayerID"/>
									<select id="balanceSummaryPayer"	class="form-control form-element form-element-select-large">
									<option>Select</option>
											<c:forEach items="${payerid}" var="payerid">
												<option value="${payerid}">${payerid}</option>
											</c:forEach>
									</select>
								</div>
									<div id="balanceSummaryDiv">
										<%-- <div class="BalanceSummarysection col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
											<table id="section-1" class="table table-bordered"
												style="margin-bottom: 0px">
												<thead>
													<tr>
														<th class="text-uppercase">Due Date</th>
														<th class="text-uppercase text-right" style="width: 120px">Open
															Amount</th>
													</tr>
												</thead>
												<tbody>
													
													<c:choose>
														<c:when test="${not empty balanceSummaryReportData}">
															<c:forEach items="${balanceSummaryReportData}"
																var="balanceSummaryReportDataEntery">
																<tr>
																	<td class="text-left">${balanceSummaryReportDataEntery.DueDate}</td>
																	<td class="text-right">${balanceSummaryReportDataEntery.AmountPaid}</td>
																</tr>
																<tr>
																	<td class="text-left"><strong>Total</strong></td>
																	<td class="text-right">${balanceSummaryReportDataEntery.Total}</td>
																</tr>
															</c:forEach>
														</c:when>
														<c:otherwise>
															No Data Available from balance Summary jsp!
														</c:otherwise>
													</c:choose>
													
												</tbody>
											</table>
										</div> --%>
									</div>
							</div>
						
							<div class="subFinancialHead">Credit Summary</div>
								<div class="row subFinancialText">
									<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12 marginTop15px">
										<label class="form-label form-label-select-large">Payer</label>
										<form:input type="hidden" id="creditSummaryPayerID" path="creditSummaryPayerID"/>
										<select id="creditSummaryPayer" class="form-control form-element form-element-select-large">		
											<option>Select</option>										
											<c:forEach items="${payerid}" var="payerid">
												<option value="${payerid}">${payerid}</option>
											</c:forEach>													
										</select>
									</div>	
										<div id="creditSummaryDiv">	
										
										</div>
							</div>	
															
					</div>
				</div>
			</div>								
		</div>
		
		<div class="row ">
			<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 dlink-holder">
					<span><span class="link-txt boldtext"><spring:message code='reports.download.label'/></span>
					<a href="#" id="financialSummaryReportExcel" class="tertiarybtn marginRight excel">
					<spring:message	code='reports.excel.label'/></a> | 
					<a href="#" id="financialSummaryReportPdf" class="tertiarybtn pdf">
					<spring:message code='reports.pdf.label'/></a>
					</span>
					<div>
			<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
		</div>
				</div>
		</div>
		</form:form>
	</div>
</div>