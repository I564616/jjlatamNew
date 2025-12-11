<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjglobalreports/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- AAOL #2419 -->
<div class="row jnj-panel mainbody-container singlePurchaseReportBlock">
  <c:url value="/reports/financialAnalysis/invoiceReport" var="financialURL" />
  <input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> <input id="selectedText" type="hidden"
    value="<spring:message code='reports.backorder.selected' />" /> <input id="allText" type="hidden"
    value="<spring:message code='reports.backorder.all' />" /> <input type="hidden" id="accountid" value="${currentAccountId}" /> <input
    type="hidden" id="originalFormAction" value="${financialURL}" /> <input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}" />
  <div class="col-lg-12 col-md-12">
    <form:form id="financialReportForm" action="${financialURL}" modelAttribute="JnjGlobalFinancialAnalysisReportForm" method="POST">
      <form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
      <form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
      <form:input id="downloadType" type="hidden" path="downloadType" />
      <div class="row jnj-panel-body">
        <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
          <div class="row marginbottomipad25px">
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start">
              <div>
                <label class="getErrorMessage pull-left form-label form-label-date boldtext" for="startDate"
                  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /></label>
                <div class="input-group form-element form-element-date">
                  <form:input type="text" data-msg-required='' id="invoice-startDate" path="startDate" class="iconCalender date-picker form-control"
                    name="toDate" placeholder="Select date" />
                  <label for="invoice-startDate" class="input-group-addon btn"> <span class="glyphicon glyphicon-calendar"></span>
                  </label>
                </div>
                <div class="registerError"></div>
              </div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px end">
              <div>
                <label class="getErrorMessage pull-left form-label form-label-date boldtext ipadterritory" for="endDate"
                  data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /></label>
                <div class="input-group form-element form-element-date">
                  <form:input type="text" data-msg-required="" name="toDate" placeholder="Select date" path="endDate" id="invoice-endDate"
                    class="iconCalender  date-picker form-control" />
                  <label for="invoice-endDate" class="input-group-addon btn"> <span class="glyphicon glyphicon-calendar"></span>
                  </label>
                </div>
                <div class="registerError"></div>
              </div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
              <label class="pull-left form-label form-label-date boldtext ipadterritory"><spring:message
                  code='product.detail.specification.status' />
                <!-- Status --></label>
              <div class="form-element form-element-date">
                <form:select data-width="100%" id="financialStatus" path="financialStatus" class="form-control form-element form-element-select">
                  <form:option value="PENDING">
                    <spring:message code='text.account.order.status.display.completed' />
                    <!-- Completed -->
                  </form:option>
                  <form:option value="ALL" selected="selected">
                    <spring:message code='reports.purchase.analysis.all' />
                    <!-- Completed -->
                  </form:option>
                </form:select>
              </div>
            </div>
          </div>
        </div>
        <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">
          <div class="row">
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12">
              <label class="pull-left ReportLabel form-label-select boldtext textheightipad getErrorMessage"><spring:message
                  code='reports.financial.table.product.customerPONumber' />
                <!-- Customer PO Number --></label>
              <!-- <input type="text" class="form-control form-element-select" value=" "> -->
              <form:input type="text" data-msg-required="" id="customerPONumber" path="customerPONumber" class=" form-control form-element-select" />
            </div>
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
              <label class="pull-left ReportLabel form-label-select boldtext textheightipad getErrorMessage"><spring:message
                  code='reports.financial.table.product.salesDocumentNumber' />
                <!-- Sales Document Number --></label>
              <!-- <input type="text" class="form-control form-element-select" value=" "> -->
              <form:input type="number" data-msg-required="" id="salesDocumentNumber" path="salesDocumentNumber"
                class=" form-control form-element-select"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
              <label class="pull-left ReportLabel form-label-select boldtext textheightipad getErrorMessages"><spring:message
                  code='reports.financial.table.product.invoiceNumber' />
                <!-- Invoice Number --></label>
              <!-- <input type="text" class="form-control form-element-select" value=" "> -->
              <form:input type="text" data-msg-required="" id="invoiceNumber" path="invoiceNumber" class=" form-control form-element-select" />
            </div>
          </div>
        </div>
        <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12 companybrand">
          <div class="row">
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12">
              <label class="pull-left ReportLabel form-label-select boldtext ipadterritory"><spring:message
                  code='reports.financial.table.product.franchiseDescription' /> <!-- Franchise Description --></label>
              <form:select id="franchiseDesc" path="franchiseDesc" class="form-control form-element form-element-select">
                <option value="All"  selected="selected"><spring:message code='reports.order.analysis.select' /></option><!-- AAOL-5710 -->
				<c:forEach items="${categoryData}" var="franchise">
				<c:choose>
					<c:when
						test="${not empty JnjGlobalFinancialAnalysisReportForm.franchiseDesc && JnjGlobalFinancialAnalysisReportForm.franchiseDesc eq franchise.code}">
						<option value="${franchise.code}" selected="selected">${franchise.name}</option>
					</c:when>
					<c:otherwise>
						<option value="${franchise.code}">${franchise.name}</option>
					</c:otherwise>
				</c:choose>
				</c:forEach>
			  </form:select>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px">
              <label class="pull-left form-label form-label-select boldtext ipadterritory"><spring:message
                  code='reports.financial.label.product.orderType' /> <!-- Order Type --></label>
              <form:select id="orderType" path="orderType" class="form-control form-element form-element-select">
              <!-- AAOL-5710 -->
                <form:option value=""  selected="selected">
			<spring:message code="orderHistoryPage.all" />
	      	</form:option>
                <form:option value="KB">
                  <spring:message code='reports.financial.table.product.consignmentFillup' />
                  <!-- Consignment Fill-Up -->
                </form:option>
                <form:option value="Consignment Return">
                  <spring:message code='reports.financial.table.product.consignmentReturn' />
                  <!-- Consignment Return -->
                </form:option>
                <form:option value="KE">
                  <spring:message code='reports.financial.table.product.consignmentCharge' />
                  <!-- Consignment Charge -->
                </form:option>
                <form:option value="ZOR">
                  <spring:message code='cart.common.orderType.ZOR' />
                  <!-- Consignment Fill-Up -->
                </form:option>
              </form:select>
            </div>
          </div>
        </div>
      </div>
    </form:form>
    <div class="row jnj-panel-footer">
      <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
        <span><span class="link-txt boldtext"><spring:message code='reports.download.label' /></span> <a href="#" id="financialReportExcel"
          class="tertiarybtn marginRight excel"> <spring:message code='reports.excel.label' />
        </a> <%-- |<a href="#" id="financialReportPdf" class="tertiarybtn pdf"> <spring:message code='reports.pdf.label' />
        </a>  --%></span>
        <div>
			<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
		</div>
      </div>
      <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
        <div class="pull-right btn-mobile">
          <a id="financialReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="#"> <spring:message
              code='reports.search.labelUX' />
          </a> <a class="tertiarybtn btn btnclsnormal reset" href="${financialURL}"> <spring:message code='reports.reset.label' />
          </a>
        </div>
      </div>
    </div>
  </div>
</div>
<div id="changeAccountPopupContainer"></div>
<div id="orderproductReportPopupAdd"></div>
<c:if test="${jnjGTFinancialAnalysisOrderReportResponseDataMap eq null && noDataFound eq 'true'}">
  <div class="info positive reportBody no-data-report boxshadow">
    <spring:message code='reports.table.no.data' />
  </div>
</c:if>
<c:if test="${jnjGTFinancialAnalysisOrderReportResponseDataMap ne null && jnjGTFinancialAnalysisOrderReportResponseDataMap.size ne 0}">
  <div class="row">
    <div class="col-lg-12 col-md-12">
      <div class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
        <table id="datatab-desktop" class="orderAnalysisTable table table-bordered table-striped sorting-table reports-table-desktop">
          <thead>
            <tr>
              <th><spring:message code='reports.financial.table.product.orderType' />
                <!-- Order Type --></th>
              
              <th><spring:message code='reports.financial.table.product.invoiceNo' /> <!-- Invoice No --></th>
              <th><spring:message code='reports.financial.table.product.lineItem' /> <!-- Line Item --></th>
              <th><spring:message code='reports.financial.table.product.customerPoNo' /> <!-- Customer PO No --></th>
              <th><spring:message code='reports.financial.table.product.productCode' /> <!-- PRODUCT CODE --></th>
              <th><spring:message code='reports.financial.table.product.productDescription' /> <!-- PRODUCT Description --></th>
              <th><spring:message code='reports.financial.table.product.invoicedQty' /> <!-- Invoiced QTY --></th>
              <th><spring:message code='reports.order.analysis.UOM' /><!-- Uom --></th>
              <th><spring:message code='reports.financial.invoicestatus' /> <!-- Status --></th>
              <th><spring:message code='reports.financial.table.product.currency' /> <!--  Currency --></th>
              <th><spring:message code='reports.invoicedue.totalamount' /> </th>
              <%-- <th><spring:message code='reports.financial.table.product.paidAmount' /> <!-- Paid Amount --></th>
              <th><spring:message code='reports.financial.table.product.openAmount' /> <!-- Open Amount --></th> --%>
              
              
            </tr>
          </thead>
          <tbody>
              <c:forEach items="${jnjGTFinancialAnalysisOrderReportResponseDataMap}" var="response">
               	<tr>
	                <td>${response.value.orderType}</td>
	                <c:url value="/order-history/order/invoiceDetail/${response.value.orderNumber}" var="invoiceDetailUrl" />
	                <td><a href="${invoiceDetailUrl}">${response.value.invoiceNumber}</a><!-- 40002164 --></td>
	                <td>${response.value.lineItem}<!-- 1 --></td>
	                <td>${response.value.customerPONumber}<!-- 225028 --></td>
	                <td>${response.value.productCode}<!-- 108867 --></td>
	                <td class="text-left">${response.value.productDescription}<!-- VAPR TRIPOLAR 90 SUCTION --></td>
	                <td>${response.value.invoiceQty}<!-- 4 --></td>
	                <td>${response.value.uom}</td>
	                <td>${response.value.status}</td>
	                <td>${response.value.currency}</td>
	                <td>${response.value.totalPrice}</td>
	                <%-- <td>${response.value.paidAmount}</td>
	                <td>${response.value.openAmount}</td> --%>
                </tr>
              </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</c:if>