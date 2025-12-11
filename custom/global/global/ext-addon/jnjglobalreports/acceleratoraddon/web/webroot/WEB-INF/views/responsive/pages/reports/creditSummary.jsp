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
				<c:when test="${not empty creditSummaryReportData}">
						<c:forEach items="${creditSummaryReportData}" var="creditSummaryReportDataEntery">
									<div class="CreditSection col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Open Order Value</label>
											<input type="text" value="${creditSummaryReportDataEntery.getOpenOrderValue()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Open Delivery Value</label>
											<input type="text" value="${creditSummaryReportDataEntery.getOpenDeliveryValue()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Amount Due</label>
											<input type="text" value="${creditSummaryReportDataEntery.getAmountDue()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Credit Used</label>
											<input type="text" value="${creditSummaryReportDataEntery.getCreditUsed()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Credit Limit</label>
											<input type="text" value="${creditSummaryReportDataEntery.getCreditLimit()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Over/Under Value</label>
											<input type="text" value="${creditSummaryReportDataEntery.getOverUnderValue()}" class="text-right form-control form-element scrollTextBox">
										</div>
										<div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<label class="form-label scrollLabel">Credit Limit Used (%)</label>
											<input type="text" value="${creditSummaryReportDataEntery.getCreditLimitUsed()}" class="text-right form-control form-element scrollTextBox">
										</div>																	
									</div>
						</c:forEach>
					</c:when>
			</c:choose>								
		
		
