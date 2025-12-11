<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

	<div
		class="BalanceSummarysection col-lg-12 col-md-12 col-sm-12 col-xs-12 padding0px">
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
								<td class="text-left">${balanceSummaryReportDataEntery.getDueDate()}</td>
								<td class="text-right">${balanceSummaryReportDataEntery.getAmountPaid()}</td>
							</tr>
							<tr>
								<td class="text-left"><strong>Total</strong></td>
								<td class="text-right">${balanceSummaryReportDataEntery.getTotal()}</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						No Data Available from balance Summary jsp!
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>
