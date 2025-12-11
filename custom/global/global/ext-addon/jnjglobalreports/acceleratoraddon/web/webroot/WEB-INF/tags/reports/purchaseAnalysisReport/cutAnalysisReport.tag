<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="row jnj-panel mainbody-container cutOrderReportBlock">
	<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
	<input id="multipleText" type="hidden"
		value="<spring:message code='reports.backorder.multiple' />" /> <input
		id="selectedText" type="hidden"
		value="<spring:message code='reports.backorder.selected' />" /> <input
		id="allText" type="hidden"
		value="<spring:message code='reports.backorder.all' />" />
	<c:url value="/reports/purchaseAnalysis/single" var="singlePurchaseURL" />
	<c:url value="/reports/purchaseAnalysis/multi" var="multiPurchaseURL" />
	<c:url value="/reports/purchaseAnalysis/backorder" var="backOrderURL" />
	<c:url value="/reports/cutorder" var="cutReportUrl"></c:url>
   <c:url value="/reports/inventoryAnalysis/consignmentInventory" var="consignmentInventoryURL" />
<c:url value="/reports/inventoryAnalysis/backorder" var="inventoryBackOrderURL" />
<c:url value="/reports/inventory" var="inventoryURL" />
 <c:url value="/reports/inventoryAnalysis/cutorder" var="inventoryCutReportUrl"></c:url>
	<input type="hidden" id="originalFormAction" value="${cutReportUrl}" />
	<input type="hidden" id="accountid" value="${currentAccountId}" />
	<c:forEach items="${user.roles}" var="role">
		<c:if test="${role eq 'viewOnlyStandardGroup'}">
			<c:out value="${user.roles}"></c:out>
			<c:set var="viewOnlyStandard">false</c:set>
		</c:if>
	</c:forEach>
	<div class="col-lg-12 col-md-12 cutReportPage">
		<form:form action="${cutReportUrl}" id="cutOrderReportForm"
			modelAttribute="cutReportForm" method="POST">
			<form:input id="downloadType" type="hidden" path="downloadType"
				value="" />
			<div class="row jnj-panel-header">
				<div class="col-lg-8 col-md-8 col-sm-8 col-xs-11">
					<div class="amazon">
						<spring:message code='reports.account.label' />
						&nbsp;<span id="selectedAccountsText">${currentAccountId}</span>
						<form:input type="hidden" value="${currentAccountId}"
							id="hddnAccountsString" path="accountIds" />
						<form:input type="hidden" id="hddnAccountsSelectedValue"
							path="accountsSelectedValue" />
						<form:input type="hidden" value="${currentAccountId}"
							id="hddnAccountsString" path="accountIdsList" />
						<c:if test="${showChangeAccountLink eq true}">
							<a id="accountSelectionLink" href="javascript:; " class="change">
								<spring:message code='reports.purchase.analysis.change' />
							</a>
						</c:if>
					</div>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
					<div class="checkbox checkbox-info pull-right selectchkbox">
						<input id="check4" class="styled selectAllAccount" type="checkbox">
						<label for="check4"><spring:message
								code='reports.account.selection.all' /></label>
					</div>
				</div>
			</div>
			<div class="row jnj-panel-body">
      <c:choose>
          <c:when test="${inventoryAnalysis eq true}">
            <div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
              <label class="pull-left form-label form-label-select-large boldtext">Category</label> <select
                class="form-control form-element form-element-select-large" id="category">
                <option selected="selected" value="${backOrderURL}">Inventory Analysis</option>
                <option value="#">Order Analysis</option>
                <c:if test="${inventry eq 'true'}">
                  <option value="#">Financial Analysis</option>
                </c:if>
              </select>
            </div>
            <div class="form-group col-lg-2 col-md-2 col-sm-2"></div>
            <div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
              <label class="pull-left form-label form-label-select-large boldtext"><spring:message code='reports.report.type' /></label> <select
                class="form-control form-element form-element-select-large" id="reportType">
                <option value="${inventoryBackOrderURL}"><spring:message code='reports.backorder.header' /></option>
                <option selected="selected" value="${inventoryCutReportUrl}"><spring:message code='cutReport.label' /></option>
                <c:if test="${inventry eq 'true'}">
                  <option value="${inventoryURL}"><spring:message code='reports.inventory.header.new' /></option>
                </c:if>
                <option value="${consignmentInventoryURL}">Consignment Inventory Report</option>
              </select>
            </div>
          </c:when>
          <c:otherwise>
				<div class="form-group col-lg-6 col-md-6 col-sm-6 col-xs-12">

					<label
						class="cut-report-label form-label boldtext"><spring:message
							code="reports.report.type" /></label> <div class="cut-report-field"><select
						class="form-control form-element form-element-select-large"
						id="reportType" data-width="100%">
						<option value="${singlePurchaseURL}"><spring:message
								code='reports.purchase.analysis.single' /></option>
						<option value="${multiPurchaseURL}"><spring:message
								code='reports.purchase.analysis.multi' /></option>
						<option value="${backOrderURL}"><spring:message
								code='reports.backorder.header' /></option>
						<option selected="selected" value="${cutReportUrl}"><spring:message
								code='cutReport.label' /></option>

					</select></div> 
				</div>
        </c:otherwise>
        </c:choose>
        <input id="hddnCurrentAccount" value="${currentAccountId}"
            type="hidden" />
				<div class="form-group col-lg-6 col-md-5 col-sm-6 col-xs-12">
					<label class="cut-report-label form-label boldtext"><spring:message
							code="cutReport.poNumber.label" /></label>
					<div class="cut-report-field">
						<form:input id="poNumber" path="poNumber" class="form-control" />
					</div>
				</div>
			</div>
			<div class="row jnj-panel-body">
				<!-- <div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12"> -->
				<div class="form-group col-lg-6 col-md-6 col-sm-6 col-xs-12">
					<div class="row form-mobile-row-gap">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 form-row-gap start">
							<label class="pull-left form-label cut-report-label boldtext"><spring:message
									code="cutReport.startDate.label" /><span class="redStar">*</span></label>
							<%-- <div class="cut-report-field" id="cut-report-startDat">	
																	<div class="input-group form-element">
																		<input id="cut-report-startDate" placeholder="<spring:message code='cart.common.selectDate'/>" class="date-picker form-control required" type="text" name="postartDate">
																		<label for="cut-report-startDate" class="input-group-addon btn"><span class="glyphicon glyphicon-calendar"></span> </label>
																	</div>
																</div> --%>
							<div class="input-group  input-append date"
								id="cutReportPoStartDate">
								<input type="text" id="postartDate" name="postartDate"
									placeholder="<spring:message code='cart.common.selectDate'/>"
									class="date-picker form-control required" /> <label
									for="cut-report-startDate" class="input-group-addon btn"><span
									class="glyphicon glyphicon-calendar"></span></label>
							</div>
						</div>
						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12  end">
							<label class="cut-report-label form-label boldtext"><spring:message
									code="cutReport.endDate.label" /><span class="redStar">*</span></label>
							<div class="cut-report-field">
								<div class="input-group input-append date"
									id="cutReportPoEndDate">
									<input id="cut-report-endDate" name="poendDate"
										placeholder="<spring:message code='cart.common.selectDate'/>"
										class="date-picker form-control required" type="text">
									<label for="cut-report-endDate" class="input-group-addon btn"><span
										class="glyphicon glyphicon-calendar"></span> </label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
					<div class="row form-mobile-row-gap">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 form-row-gap start">
							<label class="cut-report-label form-label boldtext"><spring:message
									code="cutReport.productCode.label" /></label>
							<div class="cut-report-field">
								<form:input class="form-control" id="productCode"
									path="productCode"></form:input>
							</div>
						</div>

						<div
							class="col-lg-12 col-md-12 col-sm-12 col-xs-12 end">
							<label class="cut-report-label form-label boldtext"><spring:message
									code="cutReport.ship.label" /></label>
							<div class="cut-report-field">
								<form:select class="selectshiptopicker" data-width="100%"
									id="shipTo" path="shipTo">
									<option>All</option>
									<%-- <c:forEach var="reasonCode" items="${shipToAddressDropDown}">
																			<option value="${reasonCode.id}" >${reasonCode.line1} ,${reasonCode.postalCode}</option>
																				</c:forEach> --%>
								</form:select>

							</div>
						</div>
					</div>
				</div>

			</div>

		</form:form>
		<div class="row jnj-panel-footer">
			<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
				<span><span class="link-txt boldtext"><spring:message
							code='reports.download.label' /></span> <a href="#"
					id="cutOrderReportExcel" class="tertiarybtn marginRight excel">
						<spring:message code='reports.excel.label' />
				</a> |<a href="#" id="cutOrderReportPdf" class="tertiarybtn pdf"> <spring:message
							code='reports.pdf.label' />
				</a> </span>
			</div>
			<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
				<div class="pull-right btn-mobile">
					<a id="cutOrderReportSubmit"
						class="secondarybtn btn btnclsactive generatereport pull-right"
						href="#"> <spring:message code='reports.search.labelUX' />
					</a> <a class="tertiarybtn btn btnclsnormal reset"
						href="${cutReportUrl}"> <spring:message
							code='reports.reset.label' />
					</a>
				</div>
			</div>
		</div>
	</div>
</div>
<c:if test="${cutReportOrders eq null && noDataFound eq 'true'}">
	<div class="info positive reportBody no-data-report boxshadow">
		<spring:message code='reports.table.no.data' />
	</div>
</c:if>
<c:if test="${cutReportOrders ne null }">
	<div class="Sorting-by">
		<label> <strong><spring:message code="cutReport.product.sortby"/></strong> <select class="sortBy"
			id="cutsortBy">
				<c:forEach items="${sortOptions}" var="sortOption">
					<option value="${sortOption.key}">${sortOption.value}</option>
				</c:forEach>
		</select>
		</label>
	</div>
	<div class="row">

		<div class="col-lg-12 col-md-12">
			<div
				class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">

				<table id="datatab-desktop"
					class="table table-bordered table-striped sorting-table reports-table-desktop">
					<thead>
						<tr>
							<th class="account-head"><span><spring:message
										code="cutReport.header.column2.label" /></span></th>
							<!-- 	<th class="no-sort">Ship-To NAME</th> -->
							<th><span><spring:message
										code="cutReport.product.name.label" /></span></th>
							<th><span><spring:message
										code="cutReport.product.code.label" /></span></th>
							<th class="productcode"><span><spring:message
										code="cutReport.product.gtin.upc.label" /></span></th>
							<th id="poNumber"><span><spring:message
										code="cutReport.header.column3.label" /></span></th>
							<th id="orderNumber"><span><spring:message
										code="cutReport.header.column4.label" /></span></th>
							<th id="orderDate"><span><spring:message
										code="cutReport.header.column5.label" /></span></th>
							<!-- <th class="no-sort">Order Line</th> -->
							<th id="cutReason"><span><spring:message
										code="cutReport.cut.reason.label" /></span></th>
							<th><span><spring:message
										code="cutReport.cut.quantity.label" /></span></th>
							<th><span><spring:message
										code="cutReport.order.quan.label" /></span></th>
							<!-- <th class="no-sort">Unit</th> -->
							<th><span><spring:message
										code="cutReport.product.next.available.Date" /></span></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${cutReportOrders}" var="orderData">
							<c:forEach items="${orderData.cutReportEntries}"
								var="cutReportDetails">


								<tr>
									<td>${orderData.accountNumber}</td>
									<!-- <td>Richard</td> -->
									<td>${cutReportDetails.productName}</td>
									<td><c:url value="${cutReportDetails.productUrl}"
											var="productUrl" /> <a href="${productUrl}">${cutReportDetails.productCode}</a></td>
									<td><c:if
											test="${cutReportDetails.gtin ne null and cutReportDetails.upc ne null}">
															${cutReportDetails.gtin} <span>/</span> ${cutReportDetails.upc}
															
															</c:if> <c:if
											test="${cutReportDetails.gtin ne null and cutReportDetails.upc eq null}">
															${cutReportDetails.gtin} <span>/</span> ${cutReportDetails.upc}
															
															</c:if> <c:if
											test="${cutReportDetails.gtin eq null and cutReportDetails.upc ne null}">
															${cutReportDetails.gtin} <span>/</span> ${cutReportDetails.upc}
															
															</c:if></td>
									<td><c:if
											test="${not empty orderData.PONumber && orderData.PONumber ne null}">${orderData.PONumber}</c:if>
									</td>
									<td><c:url
											value="/order-history/order/${orderData.hybrisOrderNumber}"
											var="orderDetailUrl" /> <span><a
											href="${orderDetailUrl}"><c:if
													test="${not empty orderData.orderNumber && orderData.orderNumber ne null}">${orderData.orderNumber}</c:if></a></span></td>
									<td><span><c:if
												test="${not empty orderData.orderDate && orderData.orderDate ne null}">
												<fmt:formatDate pattern="${dateformat}"
													value="${orderData.orderDate}" />
											</c:if> </span></td>
									<!-- <td>000580</td> -->
									<td>${cutReportDetails.cutReason}</td>
									<td>${cutReportDetails.cutQuantity}</td>
									<td>${cutReportDetails.orderQuantity}</td>
									<!-- <td>CS</td> -->
									<td><span><c:if
												test="${not empty cutReportDetails.availabilityDate && cutReportDetails.availabilityDate ne null}">
												<fmt:formatDate pattern="${dateformat}"
													value="${cutReportDetails.availabilityDate}" />
											</c:if> </span></td>
								</tr>
							</c:forEach>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</c:if>
	<!-- Table collapse for ipad device-->
<c:if test="${cutReportOrders ne null }">
	<div
		class="visible-sm hidden-lg hidden-xs hidden-md jnj-panel-for-table mainbody-container">
		<table id="datatab-tablet"
			class="table table-bordered table-striped sorting-table bordernone mobile-table">
			<thead>
				<tr>
					<th class="account-headipad"><span><spring:message
								code="cutReport.header.column2.label" /></span></th>
					<th class="no-sort"><span><spring:message
								code="cutReport.product.name.label" /></span></th>
					<th class="no-sort"><span><spring:message
								code="cutReport.product.code.label" /></span>/<span><spring:message
								code="cutReport.product.gtin.upc.label" /></span></th>
					<th class="no-sort"><span><spring:message
								code="cutReport.header.column4.label" /></span></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cutReportOrders}" var="orderData" varStatus="count1">
					<c:forEach items="${orderData.cutReportEntries}"
						var="cutReportDetails" varStatus="count2">

						<tr>
							<td class="vlign-top"><a data-toggle="collapse"
								data-parent="#accordion" href="#collapse-${count1.count}${count2.count}"
								class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
									class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>${orderData.accountNumber}
							</a>
								<div id="collapse-${count1.count}${count2.count}" class="panel-collapse collapse">
									<div class="panel-body details">
										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.header.column3.label" /></span>
											</p>
											<p>
												<c:if
													test="${not empty orderData.PONumber && orderData.PONumber ne null}">${orderData.PONumber}</c:if>
											</p>
										</div>

										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.header.column5.label" /></span>
											</p>
											<P>
												<span><c:if
														test="${not empty orderData.orderDate && orderData.orderDate ne null}">
														<fmt:formatDate pattern="${dateformat}"
															value="${orderData.orderDate}" />
													</c:if> </span>
											</P>
										</div>

										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.cut.reason.label" /></span>
											</P>
											<P>${cutReportDetails.cutReason}</P>
										</div>


										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.cut.quantity.label" /></span>
											</P>
											<P>${cutReportDetails.cutQuantity}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.order.quan.label" /></span>
											</P>
											<P>${cutReportDetails.orderQuantity}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.product.next.available.Date" /></span>
											</P>
											<P>
												<span><c:if
														test="${not empty cutReportDetails.availabilityDate && cutReportDetails.availabilityDate ne null}">
														<fmt:formatDate pattern="${dateformat}"
															value="${cutReportDetails.availabilityDate}" />
													</c:if> </span>
											</P>
										</div>
									</div>
								</div></td>

							<td class="vlign-top">${cutReportDetails.productName}</td>
							<td class="vlign-top">${cutReportDetails.productCode}<a
								href="${productUrl}">Show</a> <c:if
									test="${cutReportDetails.gtin ne null or cutReportDetails.upc ne null}">
																																		${cutReportDetails.gtin} <span></span> ${cutReportDetails.upc}
																																		</c:if>



							</td>
							<td class="vlign-top"><c:url
									value="/order-history/order/${orderData.hybrisOrderNumber}"
									var="orderDetailUrl" /> <span><a
									href="${orderDetailUrl}"><c:if
											test="${not empty orderData.orderNumber && orderData.orderNumber ne null}">${orderData.orderNumber}</c:if></a></span></td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!--Accordian for ipad Ends here -->
</c:if>
	<c:if test="${cutReportOrders ne null }">
	<!-- Table collapse for mobile device -->
	<div
		class="visible-xs hidden-lg hidden-sm hidden-md jnj-panel-for-table mainbody-container">
		<table id="datatab-mobile"
			class="table table-bordered table-striped sorting-table bordernone mobile-table">
			<thead>
				<tr>
					<th class="no-sort"><span><spring:message
								code="cutReport.header.column2.label" /></span></th>
					<th class="no-sort"><span><spring:message
								code="cutReport.header.column4.label" /></span></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cutReportOrders}" var="orderData" varStatus="count1">
					<c:forEach items="${orderData.cutReportEntries}"
						var="cutReportDetails" varStatus="count2">
						<tr>
							<td class="vlign-top"><a data-toggle="collapse"
								data-parent="#accordion"  href="#collapse${count1.count}${count2.count}"
								class="toggle-link skyBlue collapsed panel-collapsed" aria-expanded="false"> <span
									class="glyphicon skyBlue toggle-plus-minus glyphicon-plus"></span>
							</a>${orderData.accountNumber}
								<div id="collapse${count1.count}${count2.count}" class="panel-collapse collapse">
									<div class="panel-body details">

										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><span><spring:message
															code="cutReport.product.name.label" /></span></span>
											</p>
											<p>${cutReportDetails.productName}</p>
										</div>
										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.product.code.label" /></span>/<span><spring:message
														code="cutReport.product.gtin.upc.label" /></span>
											</p>
											<p>
												${cutReportDetails.productCode}<a href="${productUrl}"> <spring:message
														code="reports.purchase.analysis.show"/></a>
												<c:if
													test="${cutReportDetails.gtin ne null or cutReportDetails.upc ne null}">
																																		${cutReportDetails.gtin} <span></span> ${cutReportDetails.upc}
																																		</c:if>

											</p>
										</div>













										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.header.column3.label" /></span>
											</p>
											<p>
												<c:if
													test="${not empty orderData.PONumber && orderData.PONumber ne null}">${orderData.PONumber}</c:if>
											</p>
										</div>

										<div class="sub-details-row">
											<p style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.header.column5.label" /></span>
											</p>
											<P>
												<span><c:if
														test="${not empty orderData.orderDate && orderData.orderDate ne null}">
														<fmt:formatDate pattern="${dateformat}"
															value="${orderData.orderDate}" />
													</c:if> </span>
											</P>
										</div>

										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.cut.reason.label" /></span>
											</P>
											<P>${cutReportDetails.cutReason}</P>
										</div>


										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.cut.quantity.label" /></span>
											</P>
											<P>${cutReportDetails.cutQuantity}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.order.quan.label" /></span>
											</P>
											<P>${cutReportDetails.orderQuantity}</P>
										</div>
										<div class="sub-details-row">
											<P style="font-family: jnjlabelfont; font-size: 10px">
												<span><spring:message
														code="cutReport.product.next.available.Date" /></span>
											</P>
											<P>
												<span><c:if
														test="${not empty cutReportDetails.availabilityDate && cutReportDetails.availabilityDate ne null}">
														<fmt:formatDate pattern="${dateformat}"
															value="${cutReportDetails.availabilityDate}" />
													</c:if> </span>
											</P>
										</div>
									</div>
								</div></td>
							<td class="vlign-top">
								<div class="sub-details-row">
									<c:url
										value="/order-history/order/${orderData.hybrisOrderNumber}"
										var="orderDetailUrl" />
									<span><a href="${orderDetailUrl}"><c:if
												test="${not empty orderData.orderNumber && orderData.orderNumber ne null}">${orderData.orderNumber}</c:if></a></span>
								</div>

							</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>

</c:if>
<div id="changeAccountPopupContainer"></div>
