<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>

<c:url value="/reports/inventoryAnalysis/inventoryReport" var="inventory" />
<input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> 
<input id="selectedText" type="hidden" value="<spring:message code='reports.backorder.selected' />" />
<input id="allText" type="hidden" value="<spring:message code='reports.backorder.all' />" />
<input type="hidden" id="accountid" value="${currentAccountId}" />
<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>
<input type="hidden" id="originalFormAction" value="${inventory}" />
<input type="hidden" value="${currentAccountId}" id="hddnAccountsString" />

<form:form id="inventoryReportForm" action="${inventory}" modelAttribute="jnjGlobalInventoryReportForm" method="POST">
<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
			<div class="row jnj-panel mainbody-container">
				<div class="col-lg-12 col-md-12">
					<div class="row jnj-panel-body">
						<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
							<div class="checkbox checkbox-info pull-left selectchkbox">
								 <form:checkbox path="displayZeroStocks"   class="styled" id="stocks" />
								 <div id="delivered-label">
									<label for="stocks"><messageLabel:message messageCode='reports.inventory.zero.stocks' /></label>
								</div>	 
							</div>
						</div>
					</div>
					<div class="row jnj-panel-body">
						<div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
							<div class="row row-gap-mobi">
								<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start inventory-date-holder row-gap">
									<label class="pull-left form-label form-label-date boldtext"><spring:message code='reports.backorder.date.start' />:<span class="redStar">*</span></label>
									<div class="input-group form-element form-element-date">
										<form:input type="text" data-msg-required='' id="reports-startDate" path="startDate"  class="inventory-date iconCalender required date-picker form-control"/>
										<label for="reports-startDate"	class="input-group-addon btn">
										<span class="glyphicon glyphicon-calendar"></span>
										 </label>
									</div>
								<!--  <div class="registerError"></div>  -->
								</div>
								<div
									class="col-lg-12 col-md-12 col-sm-6 col-xs-12 end inventory-date-holder endDateHolder">
									<label class="pull-left form-label form-label-date boldtext"><spring:message code='reports.backorder.date.end' />:<span class="redStar">*</span></label>
									<div class="input-group form-element form-element-date">
									     <form:input type="text" data-msg-required="" path="endDate" id="reports-endDate" class="inventory-date iconCalender required date-picker form-control" />
										 <label for="reports-endDate" class="input-group-addon btn">
										 <span class="glyphicon glyphicon-calendar"></span> 
										 </label>
									</div>
									<!-- <div class="registerError"></div> -->
								</div>
							</div>
						</div>
						<div
							class="form-group col-lg-8 col-md-8 col-sm-12 col-xs-12 companybrand">
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 row-gap marginTop20pxIphone marginTop20pxIpad">
									<label	class="pull-left form-label boldtext textheightipad form-inventory-label-select">
									<messageLabel:message messageCode='reports.inventory.rep.ucn' /><span class="redStar">*</span>
									</label>
									<div class="cut-report-field form-inventory-input-select  floatLeftiPh">
									<c:choose>
										<c:when test="${selectedAccounts ne null && not empty selectedAccounts}">
											<form:input type="text" id="ucnNo" path="repUCNs" value="${selectedAccounts}" class="required form-control" data-msg-required=''/>
										</c:when>
										<c:otherwise>
											<form:input type="text" id="ucnNo" path="repUCNs" value="${currentAccountId}" class="required form-control" data-msg-required=''/>
										</c:otherwise>
									</c:choose>
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12  row-gap marginTop20pxIphone marginTop20pxIpad">
									<label class="cut-report-label form-label boldtext textheightipad form-inventory-label-select"><messageLabel:message messageCode='reports.inventory.product.lot.number' /></label>
									<div class="cut-report-field form-inventory-input-select  floatLeftiPh">
										<form:input id="lotNumber" path="lotNumber"  class="form-control" type="text" />
									</div>
								</div>
								<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 orderedfrom marginTop20px">
									<label
										class="pull-left form-label boldtext textheightipad form-inventory-label-select"><messageLabel:message messageCode='reports.inventory.product.code' /></label>
									<div class="cut-report-field form-inventory-input-select floatLeftiPh">
										<form:input id="prodCode" path="productCode" class="form-control"  type="text" />
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row jnj-panel-footer">
						<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
							<span>
							<span class="link-txt boldtext"><messageLabel:message messageCode='reports.download.label' />
							</span>
							<a class="tertiarybtn marginRight excel" href="javascript:;"><spring:message code='reports.excel.label' /></a> | <a class="tertiarybtn pdf" href="javascript:;"><spring:message code='reports.pdf.label' /></a></span>
							<div>
								<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
							</div>
						</div>
						<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
							<div class="pull-right btn-mobile">
							<a href="javascript:;" class="secondarybtn btn btnclsactive generatereport pull-right" id="inventoryReportSubmit">
							<spring:message code='reports.search.labelUX' />
							</a>
								<!-- <button type="button" class="btn btnclsactive generatereport pull-right">GENERATE REPORT</button> -->
								<a href="${inventory}" class="tertiarybtn floatLeft btn btnclsnormal reset">
								 		<messageLabel:message messageCode='reports.reset.label' />
								 	</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form:form>
		<div class="row">
			<div class="col-lg-12 col-md-12">
				<div
					class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
					<table id="datatab-desktop"
						class="table table-bordered table-striped sorting-table reports-table-desktop">
						<thead>
							<tr>
								<th class="no-sort account-head"><messageLabel:message
										messageCode='reports.inventory.table.ucn.number' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.product.code' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.desc' /></th>
								<th class="no-sort productcode"><messageLabel:message
										messageCode='reports.inventory.table.lot.number' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.unrestricted' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.restricted' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.quality.stock' /></th>
								<th class="no-sort"><messageLabel:message
										messageCode='reports.inventory.table.total.quantity' /></th>
								<th class="no-sort textCenter"><messageLabel:message
										messageCode='reports.inventory.table.unit' /></th>

							</tr>
						</thead>
						<tbody>
							<c:forEach items="${jnjGTInventoryReportResponseDataList}"
								var="inventoryLine" varStatus="count">
								<tr>
									<td>${inventoryLine.ucnNumber}</td>
									<td><c:url value="${inventoryLine.productURL}"
											var="productURL" /> <a href="${productURL}">${inventoryLine.productCode}</a>
									</td>
									<td>${inventoryLine.description}</td>
									<td>${inventoryLine.lotNumber}</td>
									<td>${inventoryLine.unrestricted}</td>
									<td>${inventoryLine.restricted}</td>
									<td>${inventoryLine.qualityStock}</td>
									<td>${inventoryLine.totalQty}</td>
									<td class="textCenter">${inventoryLine.unit}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- Table collapse for ipad device-->

		<div
			class="visible-sm hidden-lg hidden-xs hidden-md jnj-panel-for-table mainbody-container">
			<table id="datatab-tablet"
				class="table table-bordered table-striped sorting-table bordernone mobile-table">
				<thead>
					<tr>
						<th class="account-headipad"><messageLabel:message
								messageCode='reports.inventory.table.ucn.number' /></th>
						<th class="no-sort"><messageLabel:message
								messageCode='reports.inventory.table.product.code' /></th>
						<th class="no-sort"><messageLabel:message
								messageCode='reports.inventory.table.desc' /></th>
						<th class="no-sort"><messageLabel:message
								messageCode='reports.inventory.table.lot.number' /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jnjNAInventoryReportResponseDataList}"
						var="inventoryLine" varStatus="count">
						<tr>
							<td class="vlign-top"><c:url
									value="${inventoryLine.productURL}" var="productURL" /> <a
								data-toggle="collapse" data-parent="#accordion"
								href="${productURL}"
								class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
									class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>${inventoryLine.productCode}
							</a>
								<div id="collapse1" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.unrestricted' />
											</p>
											<p>${inventoryLine.unrestricted}</p>
										</div>

										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.restricted' />
											</p>
											<P>${inventoryLine.restricted}</P>
										</div>

										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.quality.stock' />
											</P>
											<P>${inventoryLine.qualityStock}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.total.quantity' />
											</P>
											<P>${inventoryLine.totalQty}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.unit' />
											</P>
											<P>${inventoryLine.unit}</P>
										</div>
									</div>
								</div></td>
							<td class="vlign-top"><a href="${productURL}">${inventoryLine.productCode}</a></td>
							<td class="vlign-top">${inventoryLine.description}</td>
							<td class="vlign-top textCenter">${inventoryLine.lotNumber}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!--Accordian for ipad Ends here -->

		<!-- Table collapse for mobile device -->
		<div
			class="visible-xs hidden-lg hidden-sm hidden-md jnj-panel-for-table mainbody-container">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table bordernone mobile-table">
				<thead>
					<tr>
						<th class="no-sort"><messageLabel:message
								messageCode='reports.inventory.table.ucn.number' /></th>
						<th class="no-sort"><messageLabel:message
								messageCode='reports.inventory.table.product.code' /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jnjNAInventoryReportResponseDataList}"
						var="inventoryLine" varStatus="count">
						<tr>
							<td class="vlign-top"><c:url
									value="${inventoryLine.productURL}" var="productURL" /> <a
								data-toggle="collapse" data-parent="#accordion"
								href="#collapse2" class="toggle-link panel-collapsed skyBlue">
									<span
									class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>${inventoryLine.productCode}
							</a>
								<div id="collapse2" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.desc' />
											</div>
											<div>${inventoryLine.description}</div>
										</div>
										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.lot.number' />
											</div>
											<div>${inventoryLine.lotNumber}</div>
										</div>
										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.unrestricted' />
											</div>
											<div>${inventoryLine.unrestricted}</div>
										</div>

										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.restricted' />
											</div>
											<div>${inventoryLine.restricted}</div>
										</div>

										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.quality.stock' />
											</div>
											<div>${inventoryLine.qualityStock}</div>
										</div>
										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">
												<messageLabel:message
													messageCode='reports.inventory.table.total.quantity' />
											</div>
											<div>${inventoryLine.totalQty}</div>
										</div>
										<div class="sub-details-row">
											<div style="font-family: jnjlabelfont; font-size: 10px">Unit</div>
											<div>EA</div>
										</div>
									</div>
								</div></td>
							<td class="vlign-top">
								<div class="sub-details-row margintop0px">
									<div>
										<a href="${productURL}">${inventoryLine.productCode}</a>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- Accordian for mobile ends here -->
