<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/nav"%>
<c:url value="/reports/inventoryAnalysis/consignmentInventory" var="consignmentInventoryURL" />
<c:url value="/reports/purchaseAnalysis/multi" var="multiPurchaseURL" />
<c:url value="/reports/inventoryAnalysis/backorder" var="backOrderURL" />
<c:url value="/reports/inventory" var="inventoryURL" />
 <c:url value="/reports/inventoryAnalysis/cutorder" var="cutReportUrl"></c:url>
<template:page pageTitle="${pageTitle}">
  <div id="Reportspage">
    <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
    <div class="row content">
      <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <%-- <messageLabel:message messageCode='reports.inventory.header' /> --%>
        Consignment Inventory Report
      </div>
    </div>
    <c:url value="/inventoryAnalysis/consignmentInventoryReport" var="consignmentInventory" />
    <input type="hidden" id="originalFormAction" value="${consignmentInventory}" /> <input type="hidden" value="${currentAccountId}"
      id="hddnAccountsString" /> <input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> <input
      id="selectedText" type="hidden" value="<spring:message code='reports.backorder.selected' />" /> <input id="allText" type="hidden"
      value="<spring:message code='reports.backorder.all' />" />
    <form:form id="consignmentInventoryReportForm" action="${consignmentInventory}" commandName="jnjGlobalConsignmentInventoryReportForm"
      method="POST">
      <%-- <form:input id="downloadType" type="hidden" path="downloadType" value ="" /> --%>
      <div class="row jnj-panel mainbody-container">
        <div class="col-lg-12 col-md-12">
          <div class="row jnj-panel-header">
            <div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
              <div class="amazon">
                <spring:message code='reports.account.label' />
                &nbsp;<span id="selectedAccountsText">${currentAccountId}</span>
                <%-- <form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="repUCNs" />
					                        <form:input type="hidden" id="hddnAccountsSelectedValue" path="repUCNs" /> --%>
                <a id="accountSelectionLink" href="javascript:; " class="change"> <spring:message code='reports.purchase.analysis.change' />
                </a>
              </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
              <div class="checkbox checkbox-info float-right-to-none selectchkbox">
                <input id="check4" class="styled selectAllAccount" type="checkbox"> <label for="check4"><spring:message
                    code='reports.account.selection.all' /></label>
              </div>
            </div>
          </div>
          <%--commented for new category righ now hard coded and will be change by saumitra --%>
          <%-- <div class="row jnj-panel-body">
						<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12 report-type">
							<label	class="pull-left form-label form-label-select-large boldtext"><spring:message code='reports.report.type' /></label>
							 <select class="form-control form-element form-element-select-large" id="reportType">
								<option value="${singlePurchaseURL}"><spring:message code='reports.purchase.analysis.single' /></option>
								<option value="${multiPurchaseURL}"><spring:message	code='reports.purchase.analysis.multi' /></option>
								<option value="${backOrderURL}"><spring:message	code='reports.backorder.header' /></option>
								<option selected="selected" value="${consignmentInventory}"><spring:message code='reports.inventory.header' />Consignment Inventory Report</option>
							</select>
						</div>
						<div class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12">
							<div class="checkbox checkbox-info pull-left selectchkbox">
								 <form:checkbox path="displayZeroStocks"   class="styled" id="stocks" />
								 <div id="delivered-label">
									<label for="stocks"><messageLabel:message messageCode='reports.inventory.zero.stocks' /></label>
								</div>	 
							</div>
						</div>
					</div> --%>
          <%--need to change below part later --%>
          <div class="row jnj-panel-body">
            <div class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12">
              <label class="form-label form-label-select-large boldtext">Category</label> <select
                class="form-control form-element form-element-select-large">
                <option>Inventory Analysis</option>
                <option>Order Analysis</option>
                <option>Financial Analysis</option>
              </select>
            </div>
            <div class="form-group col-lg-5 col-md-4 col-sm-6 col-xs-12">
              <label class="pull-left form-label form-label-select-large boldtext"><spring:message code='reports.report.type' /></label> <select
                class="form-control form-element form-element-select-large" id="reportType">
                <option value="${backOrderURL}"><spring:message code='reports.backorder.header' /></option>
                <option value="${cutReportUrl}"><spring:message code='cutReport.label' /></option>
                <c:if test="${inventry eq 'true'}">
                  <option value="${inventoryURL}"><spring:message code='reports.inventory.header.new' /></option>
                </c:if>
                <option selected="selected" value="${singlePurchaseURL}">Consignment Inventory Report</option>
              </select>
            </div>
            <div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12">
              <label class="pull-left form-label form-label-select-large boldtext textheightipad">Product Code</label>
              <!-- <input type="text" class="form-control form-element form-element-select-large" value=" "> -->
              <form:input type="text" data-msg-required='' id="conInvProductCode" path="productCode"
                class="form-control form-element form-element-select-large" />
            </div>
          </div>
          <%--end --%>
          <div class="row jnj-panel-body">
            <%-- <div class="col-lg-4 col-md-4 col-sm-12 col-xs-12">
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
						</div> --%>
            <div class="form-group col-lg-12 col-md-12 col-sm-12 col-xs-12 companybrand">
              <div class="row">
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 analysisvariable">
                  <label class="pull-left form-label-select boldtext textheightipad form-label">Stock Location Account</label>
                  <form:select class="form-control form-element form-element-select" path="stockLocationAcc" id="reports-stockLocationAcc">
                    <option>All</option>
                  </form:select>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintopipad20px orderedfrom">
                  <label class="pull-left form-label form-label-select boldtext textheightipad">Franchise Description</label>
                  <form:select class="form-control form-element form-element-select" path="franchiseDescription" id="reports-franchiseDescription">
                    <option>Franchise code</option>
                    <option>Franchise name</option>
                  </form:select>
                </div>
              </div>
            </div>
          </div>
          <div class="row jnj-panel-footer">
            <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
              <span> <span class="link-txt boldtext"><messageLabel:message messageCode='reports.download.label' /> </span> <a
                class="tertiarybtn marginRight excel" href="javascript:;"><spring:message code='reports.excel.label' /></a> | <a
                class="tertiarybtn pdf" href="javascript:;"><spring:message code='reports.pdf.label' /></a></span>
            </div>
            <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
              <div class="pull-right btn-mobile">
                <a href="javascript:;" class="secondarybtn btn btnclsactive generatereport pull-right" id="inventoryReportSubmit"> <spring:message
                    code='reports.search.labelUX' />
                </a>
                <!-- <button type="button" class="btn btnclsactive generatereport pull-right">GENERATE REPORT</button> -->
                <a href="${inventory}" class="tertiarybtn floatLeft btn btnclsnormal reset"> <messageLabel:message
                    messageCode='reports.reset.label' />
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form:form>
    <%-- <div class="row">
			<div class="col-lg-12 col-md-12">
				<div
					class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
					<table id="datatab-desktop"
						class="table table-bordered table-striped sorting-table cut-report">
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
		</div> --%>
    <div class="row">
      <div class="col-lg-12 col-md-12">
        <div class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
          <table id="datatab-desktop" class="orderAnalysisTable table table-bordered table-striped sorting-table">
            <thead>
              <tr>
                <!-- <th>Line Item</th> -->
                <th>Stock location account</th>
                <th>Stock location name</th>
                <th>Franchise Description</th>
                <th>PRODUCT CODE</th>
                <th style="width: 225px !important">PRODUCT Description</th>
                <th>Quantity in Stock</th>
                <th>Par Level Quantity</th>
                <th>Available Order Quantity</th>
                <th style="width: 60px">UOM</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <!-- <td>1</td> -->
                <td></td>
                <td></td>
                <td>S1</td>
                <td>40002164</td>
                <td class="text-left" style="width: 225px !important">VAPR TRIPOLAR 90 SUCTION</td>
                <td>10</td>
                <td>3</td>
                <td>6</td>
                <td>DZ</td>
              </tr>
              <tr>
                <!-- <td>2</td> -->
                <td></td>
                <td></td>
                <td>S1</td>
                <td>40002164</td>
                <td class="text-left" style="width: 225px !important">VAPR TRIPOLAR 90 SUCTION</td>
                <td>10</td>
                <td>3</td>
                <td>6</td>
                <td>DZ</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <!-- Table collapse for ipad device-->
    <div class="visible-sm hidden-lg hidden-xs hidden-md jnj-panel-for-table mainbody-container">
      <table id="datatab-tablet" class="table table-bordered table-striped sorting-table bordernone mobile-table">
        <thead>
          <tr>
            <th class="account-headipad"><messageLabel:message messageCode='reports.inventory.table.ucn.number' /></th>
            <th class="no-sort"><messageLabel:message messageCode='reports.inventory.table.product.code' /></th>
            <th class="no-sort"><messageLabel:message messageCode='reports.inventory.table.desc' /></th>
            <th class="no-sort"><messageLabel:message messageCode='reports.inventory.table.lot.number' /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jnjNAInventoryReportResponseDataList}" var="inventoryLine" varStatus="count">
            <tr>
              <td class="vlign-top"><c:url value="${inventoryLine.productURL}" var="productURL" /> <a data-toggle="collapse"
                data-parent="#accordion" href="${productURL}" class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
                  class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>${inventoryLine.productCode}
              </a>
                <div id="collapse1" class="panel-collapse collapse">
                  <div class="panel-body details">
                    <div class="sub-details-row">
                      <p style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.unrestricted' />
                      </p>
                      <p>${inventoryLine.unrestricted}</p>
                    </div>
                    <div class="sub-details-row">
                      <p style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.restricted' />
                      </p>
                      <P>${inventoryLine.restricted}</P>
                    </div>
                    <div class="sub-details-row">
                      <P style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.quality.stock' />
                      </P>
                      <P>${inventoryLine.qualityStock}</P>
                    </div>
                    <div class="sub-details-row">
                      <P style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.total.quantity' />
                      </P>
                      <P>${inventoryLine.totalQty}</P>
                    </div>
                    <div class="sub-details-row">
                      <P style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.unit' />
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
    <div class="visible-xs hidden-lg hidden-sm hidden-md jnj-panel-for-table mainbody-container">
      <table id="datatab-mobile" class="table table-bordered table-striped sorting-table bordernone mobile-table">
        <thead>
          <tr>
            <th class="no-sort"><messageLabel:message messageCode='reports.inventory.table.ucn.number' /></th>
            <th class="no-sort"><messageLabel:message messageCode='reports.inventory.table.product.code' /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jnjNAInventoryReportResponseDataList}" var="inventoryLine" varStatus="count">
            <tr>
              <td class="vlign-top"><c:url value="${inventoryLine.productURL}" var="productURL" /> <a data-toggle="collapse"
                data-parent="#accordion" href="#collapse2" class="toggle-link panel-collapsed skyBlue"> <span
                  class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>${inventoryLine.productCode}
              </a>
                <div id="collapse2" class="panel-collapse collapse">
                  <div class="panel-body details">
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.desc' />
                      </div>
                      <div>${inventoryLine.description}</div>
                    </div>
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.lot.number' />
                      </div>
                      <div>${inventoryLine.lotNumber}</div>
                    </div>
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.unrestricted' />
                      </div>
                      <div>${inventoryLine.unrestricted}</div>
                    </div>
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.restricted' />
                      </div>
                      <div>${inventoryLine.restricted}</div>
                    </div>
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.quality.stock' />
                      </div>
                      <div>${inventoryLine.qualityStock}</div>
                    </div>
                    <div class="sub-details-row">
                      <div style="font-family: jnjlabelfont; font-size: 10px">
                        <messageLabel:message messageCode='reports.inventory.table.total.quantity' />
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
  </div>
  <div id="changeAccountPopupContainer"></div>
</template:page>
