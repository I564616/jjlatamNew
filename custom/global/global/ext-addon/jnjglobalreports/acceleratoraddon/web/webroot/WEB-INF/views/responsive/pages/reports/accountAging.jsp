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


<div
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
			<c:choose>
				<c:when test="${not empty accountAgingReportData}">
					<c:forEach items="${accountAgingReportData}"
						var="accountAgingReportDataEntery">
						 <tr>
							<td class="text-left">${accountAgingReportDataEntery.getDaysinArrears()}</td>
							<td class="text-right">${accountAgingReportDataEntery.getDueItem()}</td>
							<td class="text-right">${accountAgingReportDataEntery.getNotDue()}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					No Data Available from acc jsp!
					</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>