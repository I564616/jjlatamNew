<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div class="row jnj-panel mainbody-container oadlReportBlock rdiCont sectionBlock" id="Reportspage">
<c:url var="delieveryListURL" value="/reports/orderAnalysis/deliveryList"/>
<input id="multipleText" type="hidden" value ="<spring:message code='reports.backorder.multiple' />" />
<input id="selectedText" type="hidden" value ="<spring:message code='reports.backorder.selected' />" />
<input id="allText" type="hidden" value ="<spring:message code='reports.backorder.all' />" />
<input type="hidden" id="originalFormAction" value="${delieveryListURL}" />
<input type="hidden" id="accountid" value="${currentAccountId}" />
<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>
		
		
		<div class="col-lg-12 col-md-12">
			<form:form id="dlReportAnalysisForm" action="${delieveryListURL}" novalidate="novalidate" modelAttribute="jnjGlobalOADeliveryListReportForm">
				<form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
			<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
			<form:input id="downloadType" type="hidden" path="downloadType" value ="" />
				<div class="row jnj-panel-body">
				<!-- Start - StartDate ,EndDate content-->	
				<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
					<div class="row marginbottomipad25px">
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start">						
						<spring:message code='reports.oa.selectDate' var="selectDate"/>
							<label class="pull-left form-label form-label-date boldtext" for="startDate"  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /></label>											
							   <div class="input-group form-element form-element-date">
								<form:input type="text"  placeholder="${selectDate}" data-msg-required="<spring:message code='reports.oa.selectDate'/>" value="${jnjGTOADeliveryListReportForm.startDate}" id="reports-startDate" name="startDate" path="startDate" class=" date-picker form-control" />
									<label	for="reports-startDate" class=" input-group-addon btn"> 
									 	<span class=" glyphicon glyphicon-calendar"></span>
									 </label>
								</div>
						</div>					
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px end">
						  <label class=" pull-left form-label form-label-date boldtext  getErrorMessage" for="endDate" data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /></label>
							<div class="input-group form-element form-element-date">
								<form:input type="text" placeholder="${selectDate}"  data-msg-required="<spring:message code='reports.oa.selectDate'/>" value="${jnjGTOADeliveryListReportForm.endDate}" path="endDate" name="endDate" id="reports-endDate" class=" date-picker form-control iconCalenderReport" /> 
									<label	for="reports-endDate" class="input-group-addon btn">
										<span class=" glyphicon glyphicon-calendar"></span> 
									</label>
							</div>
							<div class="registerError"></div>
						</div>
						
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
					<label class="pull-left form-label form-label-date boldtext"><spring:message code='reports.order.analysis.order.type'/></label>												
					<div class="form-element form-element-date"> 
						<form:select id="orderType" name="orderType" path="orderType" data-width="100%">
							<form:option value="All" ><spring:message code='reports.purchase.analysis.all' /></form:option>
							<c:forEach items="${orderTypes}" var="ot">
								
								<c:choose>
												<c:when
													test="${ot.code eq JnjGTOADeliveryListReportForm.orderType}">
													<form:option value="${ot.code}" selected="selected">${ot.name}</form:option>
												</c:when>
												<c:otherwise>
													<form:option value="${ot.code}">${ot.name}</form:option>
												</c:otherwise>
											</c:choose>
							</c:forEach>										
						</form:select>
					</div>
				</div>		
						
																				
					</div>
				</div>
				<!-- End - StartDate ,EndDate content-->	
				<!-- Start - Customer SalesNo,Sales Doc No,Invoice No content-->	
				<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 orderedfrom">
						<spring:message code='reports.order.analysis.customerPo.no' var="custponum"/>
							<label class="pull-left ReportLabel form-label-select boldtext textheightipad" for="custPONum"><spring:message code='reports.order.analysis.customerPo.no' /></label> 												
							<form:input type="text" id="custPONum" path="custPONum" class="form-control form-element-select" placeholder="${custponum }" data-msg-required="<spring:message code='reports.order.analysis.customerPo.no' />"/>
						</div>																												
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px analysisvariable">
						<spring:message code='reports.order.analysis.sales.document.no' var="salesdocno"/>
							<label 	class="pull-left ReportLabel form-label-select boldtext textheightipad ipadterritory" style="margin-bottom:0px" for="salesDocNum"><spring:message code='reports.order.analysis.sales.document.no'/></label>												
							<form:input type="text" id="salesDocNum" path="salesDocNum" class="form-control form-element-select" placeholder="${salesdocno}" data-msg-required="<spring:message code='reports.order.analysis.sales.document.no'/>"/>														
						</div>																											
						<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px">
						<spring:message code='reports.order.analysis.delivery.No' var="delNo"/>
							<label class="pull-left form-label form-label-select boldtext ipadterritory" for="deliveryNum"><spring:message code='reports.order.analysis.delivery.No'/></label>												
							<form:input type="text" id="deliveryNum" path="deliveryNum" class="form-control form-element-select" placeholder="${delNo}" data-msg-required="<spring:message code='reports.order.analysis.delivery.No'/>"/>
						</div>
					</div>
				</div>
				<!-- End - Customer SalesNo,Sales Doc No,Invoice No content-->		
				<!-- Start - Delivery No,Product Code,francise Code content-->		
					<div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">	
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintopipad20px orderedfrom">
							<spring:message code='reports.order.analysis.productcode' var="pcode"/>
								<label class="pull-left form-label form-label-select boldtext textheightipad" for="productCode"><spring:message code='reports.order.analysis.productcode'/></label>												
								<form:input type="text" id="productCode" path="productCode" class="form-control form-element-select" placeholder="${pcode}" data-msg-required="<spring:message code='reports.order.analysis.productcode'/>"/>
							</div>
							<div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px margintopipad20px">
							    <label class="pull-left ReportLabel form-label-select boldtext ipadterritory"><spring:message code='reports.order.analysis.franchise.description'/></label>
							  						
								<select id="franchiseDesc" name="franchiseDesc" path="franchiseDesc"  class="form-control form-element form-element-select">
									 <option value="All"  selected="selected"><spring:message
												code='reports.purchase.analysis.all' /></option>
								<c:forEach items="${categoryData}" var="franchise">
								<c:choose>
									<c:when
										test="${not empty jnjGlobalOADeliveryListReportForm.franchiseDesc && jnjGlobalOADeliveryListReportForm.franchiseDesc eq franchise.code}">
										<option value="${franchise.code}" selected="selected">${franchise.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${franchise.code}">${franchise.name}</option>
									</c:otherwise>
								</c:choose>
								</c:forEach>
									 <%-- <option value="All" ><spring:message code='reports.purchase.analysis.all' /></option>
									  <c:forEach items="${categoryData}" var="franchise">
									  <option value="${franchise.code}">${franchise.name}</option>
										</c:forEach> --%>
									</select>
							</div>
						</div>
					</div>
		
				<!-- End - Delivery No,Product Code,francise Code content-->		
				</div>
			</form:form>
	
			<div class="row jnj-panel-footer buttonWrapperWithBG">
				<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder buttonWrapperWithBG">
					<span class="link-txt boldtext"><spring:message code='reports.download.label' />
					</span>
					<a href="#" class="tertiarybtn marginRight excel">
					 	<spring:message code='reports.excel.label' />
					 </a> 
					<%-- | <a href="#" class="tertiarybtn pdf">
					 	<spring:message code='reports.pdf.label' />
					 </a> --%>
					<div>
						<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
					</div>
				</div>
				<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
					<div class="pull-right btn-mobile">
						<a id="oadlReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="javascript:;">
					 		<spring:message code='reports.search.labelUX' />
					 	</a>
						<a class="tertiarybtn  btn btnclsnormal reset" href="${delieveryListURL}">
					 		<spring:message code='reports.reset.label' />
					 	</a>
					</div>
				</div>
			</div>
		</div>
</div>
<div id="changeAccountPopupContainer"></div>

<c:if test="${jnjGTOADeliveryListReportResponseDataList eq null && noDataFound eq 'true'}">
		<div class="info positive reportBody boxshadow no-data-report">
			<spring:message code='reports.table.no.data' />
		</div>
</c:if>
 <c:if test="${jnjGTOADeliveryListReportResponseDataList ne null}"> 
<div class="row">
	<div class="col-lg-12 col-md-12">
		<div class="hidden-xs jnj-panel-for-table mainbody-container">
		
			<table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
				<thead>
					<tr>
						<th class="text-left-cl account-head"><spring:message code='reports.order.analysis.order.type' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.sales.document.no' /></th>
						<th class="text-left-cl productcode"><spring:message code='reports.order.analysis.customerPo.no' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.delivery.No' /></th>
						<th class="text-left-cl"><spring:message code='reports.order.analysis.delivery.itemno' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.actualShipDate' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.franchise.description' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.productcode' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.product.description' /></th>
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.delivery.qty' /></th>	
						<th class="text-left-cl unitqty"><spring:message code='reports.order.analysis.UOM' /></th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${jnjGTOADeliveryListReportResponseDataList}" var="response">
						<tr>
						<td class="text-left-cl tabdata1">${response.orderType }</td>
						<td class="text-left-cl">${response.salesDocNum}</td>
						<td class="text-left-cl">${response.custPONum}</td>
						<td class="text-left-cl">${response.deliveryNum}</td>
						<td class="text-left-cl">${response.deliveryItemNum}</td>
						<td class="text-left-cl">${response.actualShipmentDate}</td>
						<td class="text-left-cl">${response.franchiseDesc }</td>
						<td class="text-left-cl">${response.productCode }
						<input type="hidden" class="hddnPrdCode" value="${response.productCode}"> <!-- TODO <span class="marLeft10">
						 <a href="#" class="showSingleReport"><spring:message code='reports.purchase.analysis.show' />
				         </a></span>-->
						</td>
						<td class="text-left-cl">${response.productDesc }</td>
						<td class="text-left-cl">${response.deliveryQuantity}</td>
						<td class="text-left-cl">${response.uom}</td>
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
			<table id="oadlReportTablemobile" class="table table-bordered table-striped sorting-table bordernone">
				<thead>
					<tr>
						<th class="text-left">
						<label:message  messageCode="reports.purchase.analysis.account.number" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${jnjGTOADeliveryListReportResponseDataList}" var="response" varStatus="Count">
						<tr class="myReportRow">
							<td>
								<a data-toggle="collapse" data-parent="#accordion" href="#report-mobi-${Count.count}" class="ref_no toggle-link panel-collapsed">
									<span class="glyphicon glyphicon-plus"></span>
								</a> 
								<a class ="mobileContractRow"  href="#"> $ response.accountNumber TODO </a>
								<div id="report-mobi-${Count.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.order.type" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.orderType && '' ne response.orderType}">
													<span class="txtFont">${response.orderType}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.sales.document.no" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.salesDocNum && '' ne response.salesDocNum}">
													<span class="txtFont">${response.salesDocNum}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.customerPo.no" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.custPONum && '' ne response.custPONum}">
													<span class="txtFont">${response.custPONum}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.delivery.No" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.deliveryNum && '' ne response.deliveryNum}">
													<span class="txtFont">${response.deliveryNum}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.delivery.itemno" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.deliveryItemNum && '' ne response.deliveryItemNum}">
													<span class="txtFont">${response.deliveryItemNum}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>	
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.actualShipDate" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.actualShipmentDate && '' ne response.actualShipmentDate}">
													<span class="txtFont">${response.actualShipmentDate}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.franchise.description" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.franchiseDesc && '' ne response.franchiseDesc}">
													<span class="txtFont">${response.franchiseDesc}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.productcode" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productCode && '' ne response.productCode}">
													<span class="txtFont">${response.productCode}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.product.description" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.productDesc  && '' ne response.productDesc}">
													<span class="txtFont">${response.productDesc }</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.UOM" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.uom && '' ne response.uom}">
													<span class="txtFont">${response.uom}</span>
												</c:when>
												<c:otherwise>
													<span class="txtFont">&nbsp;</span>
												</c:otherwise>
											</c:choose>	
										</div>
										<div class="lblFont bld">
											<label:message messageCode="reports.order.analysis.delivery.qty" />
										</div>
										<div>
											<c:choose> 
												<c:when
													test="${ not empty response.deliveryQuantity && '' ne response.deliveryQuantity}">
													<span class="txtFont">${response.deliveryQuantity}</span>
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
 