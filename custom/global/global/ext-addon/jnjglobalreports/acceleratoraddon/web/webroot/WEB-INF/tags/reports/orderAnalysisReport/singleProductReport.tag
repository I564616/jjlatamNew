<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row jnj-panel mainbody-container singlePurchaseReportBlock">

<c:url value="/reports/orderAnalysis/single" var="singlePurchaseURL" />
<input id="multipleText" type="hidden" value ="<spring:message code='reports.backorder.multiple' />" />
<input id="selectedText" type="hidden" value ="<spring:message code='reports.backorder.selected' />" />
<input id="allText" type="hidden" value ="<spring:message code='reports.backorder.all' />" />
<input type="hidden" id="accountid" value="${currentAccountId}" />
<input type="hidden" id="originalFormAction" value="${singlePurchaseURL}" />
<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>

<div class="col-lg-12 col-md-12">
	<form:form id="singlePurchaseReportForm" action="${singlePurchaseURL}" modelAttribute="jnjGlobalSinglePurchaseAnalysisReportForm" method="POST">
	
	<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
	<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
	<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
	
		<div class="row jnj-panel-body">
			<div class="form-group col-lg-3 col-md-3 col-sm-12 col-xs-12">
				<div class="row marginbottomipad25px">
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start"><div>
						<label class="getErrorMessage pull-left form-label form-label-date boldtext" for="startDate" data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /><span class="redStar">*</span></label>
						<div class="input-group form-element form-element-date">
						
							<form:input type="text" data-msg-required='' id="reports-startDate"  path="startDate" class="iconCalender date-picker form-control required"  name="toDate" placeholder="Select date"/>
							 <label	for="reports-startDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;">
							 <i class="bi bi-calendar3"></i> 
							 </label>
						</div>
						<div class="registerError"></div>	
					</div>
					</div>
					<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px end">
						<div>
						<label class="getErrorMessage pull-left form-label form-label-date boldtext ipadterritory" for="endDate" data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /><span class="redStar">*</span></label>
						<div class="input-group form-element form-element-date">
							<form:input type="text" data-msg-required=""  name="toDate" placeholder="Select date" path="endDate" id="reports-endDate" class="iconCalender  date-picker form-control required" />
							 <label	for="reports-endDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;">
							 <i class="bi bi-calendar3"></i> 
							 </label>
						</div>
						<div class="registerError"></div>
						</div>
					</div>
				</div>
			</div>
			<div
				class="form-group col-lg-9 col-md-9 col-sm-12 col-xs-12 companybrand ">
				<div class="row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
						
						<label class="pull-left form-label form-label-select boldtext textheightipad reportslabelwidth getErrorMessage" for="productCode" class="getErrorMessage" data-msg-required="<spring:message code='reports.purchase.analysis.product.code.error' />"><spring:message code='reports.inventory.product.code' /><span class="redStar">*</span></label>
					
						<div class="product-code-element">
						 	<form:input  type="text" data-msg-required="" id="productCode" path="productCode" class=" form-control required"/>
							<div class="registerError">
								<c:if test="${invalidProduct eq true}">
									<label for="productCode" class="error"> 
									<spring:message	code='reports.purchase.analysis.product.invalid' />
									</label>
								</c:if>
							</div>
						</div>		
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 analysisvariable ">
						<label	class="pull-left form-label form-label-select boldtext textheightipad ipadterritory lineheight18px"><spring:message code='reports.purchase.analysis.period.breakdown' /></label>
						<form:select id="breakdown" path="periodBreakdown" class="form-control form-element form-element-select">
						  <form:option value="Week"><spring:message code='reports.purchase.analysis.week' /></form:option>
						  <form:option value="Month"><spring:message code='reports.purchase.analysis.month' /></form:option>
						</form:select>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px orderedfromdropdown">
						<label	class="pull-left form-label form-label-select boldtext textheightipad reportslabelwidth"><spring:message code='reports.purchase.analysis.order.form' /></label>
						<form:select path="orderedFrom" class="form-control form-element form-element-select">
									  <option value="All"><spring:message code='reports.purchase.analysis.order.from.select' /></option>	
									  <c:forEach items="${orderedFrom}" var="orderValue" >
									  	<c:choose>
									  		<c:when test="${orderValue.key eq jnjGlobalSinglePurchaseAnalysisReportForm.orderedFrom}">
									  			<option value="${orderValue.key}" selected="selected">${orderValue.value}</option>
									  		</c:when>
									  		<c:otherwise>
									  			<option value="${orderValue.key}">${orderValue.value}</option>
									  		</c:otherwise>
									  	</c:choose>
									  </c:forEach>								  
						</form:select>
					</div>
					<div
						class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px pull-right">
						<label	class="pull-left form-label form-label-select boldtext ipadterritory"><spring:message code='reports.inventory.product.lot.number' /></label>
						<form:input type="text" id="lotNumber" path="lotNumber" class="form-control" style="width: 54%" />
					</div>
				</div>
			</div>
		</div>
	</form:form>	
		<div class="row jnj-panel-footer">
			<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
				<span><span class="link-txt boldtext"><spring:message code='reports.download.label' /></span>
				        <a href="#" class="tertiarybtn marginRight excel">
				 			<spring:message code='reports.excel.label' />
				 		</a> |<a href="#" class="tertiarybtn pdf">
				 			<spring:message code='reports.pdf.label' />
				 		</a>
					<div>
					<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
					</div>
				 		
				</span>
			</div>
			<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
				<div class="pull-right btn-mobile">
					<a id="singlePurchaseReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="#">
				 		<spring:message code='reports.search.labelUX' />
				 	</a>
					
					<a class="tertiarybtn btn btnclsnormal reset" href="${singlePurchaseURL}">
				 		<spring:message code='reports.reset.label' />
				 	</a>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="changeAccountPopupContainer"></div>
<div id="orderproductReportPopupAdd"></div>
<c:if test="${jnjGTSinglePurchaseOrderReportResponseDataMap eq null && noDataFound eq 'true'}">
		<div class="info positive reportBody no-data-report boxshadow">
			<spring:message code='reports.table.no.data' />
		</div>
		</c:if>
<c:if test="${jnjGTSinglePurchaseOrderReportResponseDataMap ne null && jnjGTSinglePurchaseOrderReportResponseDataMap.size ne 0}">
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class="hidden-xs jnj-panel-for-table mainbody-container reportsSingle">
			<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
				<thead>
					<tr>
						<th class="text-left-cl"><spring:message code='reports.purchase.analysis.account.number' /></th>
						<th class="text-left-cl periodth"><spring:message code='reports.purchase.analysis.period' /></th>
						<th class="no-sort sorting_disabled" rowspan="1" colspan="1" aria-label="AMOUNT" style="width: 20%; padding: 0">
							<div class="text-center amountAlign text-uppercase"><spring:message code='reports.purchase.analysis.amount' /></div>
							<div class="pull-left amountsubdata text-uppercase"><spring:message code='reports.purchase.analysis.electronic' /></div>
							<div class="amountsubdata pull-left text-uppercase "><spring:message code='reports.purchase.analysis.other' /></div>
						</th>
						<th class="no-sort sorting_disabled" rowspan="1" colspan="1" aria-label="AMOUNT" style="width: 25%; padding: 0">
							<div style="margin-bottom: 5px; border-bottom: 1px solid #ccc; padding: 5px 0 5px 0" class="text-center">
								<spring:message code='reports.purchase.analysis.quantity' />
							</div>
							<div class="pull-left  quantitysubdata text-uppercase">&nbsp;&nbsp;&nbsp;<spring:message code='reports.purchase.analysis.electronic' /></div>
							<div class="pull-left quantitysubdata text-uppercase"><spring:message code='reports.purchase.analysis.other' /></div>
							<div class="pull-left quantitysubdata text-uppercase"><spring:message code='reports.purchase.analysis.unit' /></div>
						</th>
						<th class="no-sort sorting_disabled" rowspan="1" colspan="1" aria-label="AMOUNT" style="width: 20%; padding: 0">
							<div style="margin-bottom: 5px; border-bottom: 1px solid #ccc; padding: 5px 0 5px 0"
								class="text-center text-uppercase"><spring:message code='reports.purchase.analysis.freq' /></div>
							<div class="pull-left amountsubdata text-uppercase"><spring:message code='reports.purchase.analysis.electronic' /></div>
							<div class="pull-left amountsubdata text-uppercase"><spring:message code='reports.purchase.analysis.other' /></div>
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jnjGTSinglePurchaseOrderReportResponseDataMap}" var="response">
					<tr>
						<td class="text-center">${response.value.accountNumber}</td>
						<td class="text-center">
						<%-- ${response.value.periodFrom} - ${response.value.periodTo} --%>
						<span>${response.value.periodFrom} - ${response.value.periodTo}</span>
						 <input type="hidden" value="${response.value.accountNumber}" class="hddnAccountNumber" />
						<a class="showEntries" href="javascript:;">
							<spring:message code='reports.purchase.analysis.show' />
						</a></td>
						<td>
							<div class="pull-left amountsubdata"><format:price priceData="${response.value.amountElectronic}"/></div>
							<div class="pull-left amountsubdata"><format:price priceData="${response.value.amountOther}"/></div>
						</td>
						<td>
							<div class="pull-left quantitysubdata">${response.value.quantityElectronic}</div>
							<div class="pull-left quantitysubdata">${response.value.quantityOther}</div>
							<div class="pull-left quantitysubdata">${response.value.quantityUnit}</div>
						</td>
						<td>
							<div class="pull-left amountsubdata">${response.value.frequencyElectronic}</div>
							<div class="pull-left amountsubdata">${response.value.frequencyOther}</div>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<%-- </c:if> --%>
		</div>
		
		<!-- Table collapse for mobile device-->
		<div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
			<!-- <div class="row horizontal-line"></div> -->
			<div class="SortingTable" id="ordersTable_length">
			</div>
			<table id="singleReportTablemobile" class="table table-bordered table-striped sorting-table bordernone">
				<thead>
					<tr>
						<th class="text-left">
						<label:message  messageCode="reports.purchase.analysis.account.number" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jnjGTSinglePurchaseOrderReportResponseDataMap}" var="response" varStatus="Count">
						<tr class="myReportRow">
							<td>
								<a data-toggle="collapse" data-parent="#accordion" href="#report-mobi-${Count.count}" class="ref_no toggle-link panel-collapsed">
									<span class="glyphicon glyphicon-plus"></span>
								</a> 
								<a class ="mobileContractRow"  href="#"> ${response.value.accountNumber} </a>

								<div id="report-mobi-${Count.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="lblFont bld">
											<label:message messageCode="reports.purchase.analysis.period" />
										</div>
										<div>
												<span class="txtFont">${response.value.periodFrom} - ${response.value.periodTo}</span>
												<input type="hidden" value="${response.value.accountNumber}" class="hddnAccountNumber" />
												<a class="showEntries" href="javascript:;">
													<spring:message code='reports.purchase.analysis.show' />
												</a>
										</div>
										
										<div class="lblFont bld" aria-label="AMOUNT" style="width: 20%; padding: 0"> 
											<label:message messageCode="reports.purchase.analysis.amount" />
										</div>
										<div>
											 <div class="pull-left amountsubdata text-uppercase">
											 	<spring:message code='reports.purchase.analysis.electronic' /> : 
											 	<span class="txtFont"><format:price priceData="${response.value.amountElectronic}"/></span>
											 </div>
											 <div class="amountsubdata pull-left text-uppercase ">
											 	<spring:message code='reports.purchase.analysis.other' /> : 
											 	<span class="txtFont"><format:price priceData="${response.value.amountOther}"/></span>
											 </div>
										</div>
										
							
										<div class="lblFont bld"  aria-label="AMOUNT" style="width: 20%; padding: 0">
											<label:message messageCode="reports.purchase.analysis.quantity" />
										</div>
										<div>
											<div class="pull-left amountsubdata text-uppercase">
											 	<spring:message code='reports.purchase.analysis.electronic' /> :  
											 	<span class="txtFont">${response.value.quantityElectronic}</span>
											 </div>
											 <div class="amountsubdata pull-left text-uppercase ">
											 	<spring:message code='reports.purchase.analysis.other' /> : 
											 	<span class="txtFont">${response.value.quantityOther}</span>
											 </div>
											 <div class="amountsubdata pull-left text-uppercase ">
											 	<spring:message code='reports.purchase.analysis.unit' /> : 
											 	<span class="txtFont">${response.value.quantityUnit}</span>
											 </div>
										</div>

										<div class="lblFont bld"  aria-label="AMOUNT" style="width: 20%; padding: 0">
											<label:message messageCode="reports.purchase.analysis.freq" />
										</div>
										<div>
											 <div class="pull-left amountsubdata text-uppercase">
											 	<spring:message code='reports.purchase.analysis.electronic' /> : 
											 	<span class="txtFont">${response.value.frequencyElectronic}</span>
											 </div>
											 <div class="amountsubdata pull-left text-uppercase ">
											 	<spring:message code='reports.purchase.analysis.other' /> : 
											 	<span class="txtFont">${response.value.frequencyOther}</span>
											 </div>
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
