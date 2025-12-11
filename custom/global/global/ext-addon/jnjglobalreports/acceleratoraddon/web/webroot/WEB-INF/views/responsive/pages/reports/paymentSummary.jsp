<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="nav"
	tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

			<c:choose>
					<c:when test="${not empty paymentSummaryReportData}">
							<c:forEach items="${paymentSummaryReportData}" var="paymentSummaryReportDataEntery">
												<div class="paymentSection col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="form-label scrollLabel">Amount Invoiced MTD</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getAmountInvoicedMTD()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Net Amount Paid MTD</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getNetAmountPaidMTD()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Amount Invoiced Prior Month</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getAmountInvoicedPriorMonth()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Net Amount Paid Prior Month</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getNetAmountPaidPriorMonth()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Amount Invoiced This Year</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getAmountInvoiceThisYear()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Net Amount Paid This Year</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getNetAmountPaidThisYear()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Amount Invoiced Prior Year</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getAmountInvoicedPriorYear()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="ReportLabel scrollLabel">Net Amount Invoiced Prior Year</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getNetAmountPaidPrioryear()}" class="text-right form-control form-element scrollTextBox">
													</div>
													<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
														<label class="form-label scrollLabel">Last Payment Amount</label>
														<input type="text" value="${paymentSummaryReportDataEntery.getLastPaymentAmount()}" class="text-right form-control form-element scrollTextBox">
													</div>
												</div>
					</c:forEach>
				</c:when>
		</c:choose>


		
	
	
