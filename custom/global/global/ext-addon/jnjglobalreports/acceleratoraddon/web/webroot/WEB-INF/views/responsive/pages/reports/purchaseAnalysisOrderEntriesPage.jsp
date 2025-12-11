<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal fade" id="OrderedProductpopup" role="dialog" data-firstLogin='true'>
<form:form action="javascript:;" id="orderedProductReportForm" method="post">
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"
					id="select-accnt-close"><spring:message code='popup.close' /></button>
				<h4 class="modal-title selectTitle"><label:message messageCode='reports.purchase.analysis.popup.header' /></h4>
			</div>
			<div class="modal-body">
				<div class="orderedProduct-details">
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div><label:message messageCode='reports.purchase.analysis.popup.period' />&nbsp; ${period}</div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div><label:message messageCode='reports.purchase.analysis.popup.product' />
							<c:set var="productInfo" value="${fn:split(productInfo, '_')}" />
								
				<span>
					<c:out value="${productInfo[0]}"></c:out>
				</span></div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div class="perdiodSpan insertAccount"><label:message messageCode='reports.purchase.analysis.popup.account' /> </div>
						</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div><label:message messageCode='reports.purchase.analysis.popup.gtin' />
									<span> <c:out value="${productInfo[1]}"></c:out>
									</span>
								</div>
						</div>
					</div>
				</div>
				<div class="hidden-xs">
					<table id="datatab-desktop"
						class="table table-bordered table-striped">
						<thead>
							<tr style="border-top: 1px solid #f2f2f2;">
								<th class="no-sort text-left text-uppercase"><spring:message code='reports.purchase.analysis.popup.orderNumber' /></th>
								<th class="no-sort text-uppercase"><spring:message code='reports.purchase.analysis.popup.orderDate'/></th>
								<th class="no-sort text-uppercase"><spring:message code='reports.purchase.analysis.popup.status' /></th>
								<th class="no-sort text-uppercase"><spring:message code='reports.purchase.analysis.popup.amount' /></th>
								<th class="no-sort text-uppercase"><spring:message code='reports.purchase.analysis.popup.quantity' /></th>
								<th class="no-sort text-left text-uppercase"><spring:message code='reports.purchase.analysis.popup.unit' /></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${jnjGTSinglePurchaseAnalysisEntriesList}" var="reportResponse">
						<tr>
						<c:set var="orderNumberArr" value="${fn:split(reportResponse.orderNumber, '|')}" />
						<td class="text-left">${orderNumberArr[0]}
						<c:url value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" />
						<a href="${orderDetailUrl}" style="padding-left: 15px;">
						      <label:message messageCode='reports.purchase.analysis.popup.orderDetails' />
						 </a>
						</td>
								<td>${reportResponse.orderDate}</td>
								<td>${reportResponse.status}</td>
								<td><format:price priceData="${reportResponse.amount}"/></td>
								<td>${reportResponse.quantity}</td>
								<td>${reportResponse.unit}</td>
						</tr>
						 
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" id="change-select-btn"><spring:message code='popup.close' /></button>
			</div>
		</div>
	</div>
	</form:form>
</div>