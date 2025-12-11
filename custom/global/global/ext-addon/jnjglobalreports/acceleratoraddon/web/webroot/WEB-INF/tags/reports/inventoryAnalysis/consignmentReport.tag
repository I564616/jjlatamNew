<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<div class="row jnj-panel mainbody-container consignmentReportBlock rdiCont sectionBlock" id="Reportspage">
  <c:url value="/reports/inventoryAnalysis/consignmentInventory" var="consignmentInventory" />
  <input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> <input id="selectedText" type="hidden"
    value="<spring:message code='reports.backorder.selected' />" /> <input id="allText" type="hidden"
    value="<spring:message code='reports.backorder.all' />" /> <input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}" /> <input
    type="hidden" id="originalFormAction" value="${consignmentInventory}" /> <input type="hidden" id="accountid" value="${currentAccountId}" />
  <form:form id="consignmentInventoryReportForm" action="${consignmentInventory}" modelAttribute="jnjGlobalConsignmentInventoryReportForm" method="POST">
    <form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
    <form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
    <form:input id="downloadType" type="hidden" path="downloadType" value="" />
    <div class="row jnj-panel mainbody-container">
      <div class="col-lg-12 col-md-12">
        <%--need to change below part later --%>
        <div class="row jnj-panel-body">
          <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
            <div class="row marginbottomipad25px">
              <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 start">
                <label class="pull-left ReportLabel consignmentReport-Label boldtext"><spring:message code='cart.common.stock.user' /></label>
                <c:set var="stockAccCommaSeparated"></c:set>
                <c:forEach items="${stockLocationAccounts}" var="stockLocationAccount">
                  <c:choose>
                    <c:when test="${not empty stockAccCommaSeparated}">
                      <c:set var="stockAccCommaSeparated" value="${stockAccCommaSeparated},${stockLocationAccount}"></c:set>
                    </c:when>
                    <c:otherwise>
                      <c:set var="stockAccCommaSeparated" value="${stockLocationAccount}"></c:set>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
                <form:select class="form-control form-element consignmentReport-select" path="stockLocationAcc" id="reports-stockLocationAcc">
                  <option selected="selected" value="${stockAccCommaSeparated}"><spring:message code='reports.purchase.analysis.all' /></option>
                  <form:options items="${stockLocationAccounts}" />
                </form:select>
              </div>
            </div>
          </div>
          <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
            <div class="row marginbottomipad25px">
              <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 start">
                <label class="pull-left ReportLabel consignmentReport-Label boldtext"><spring:message
                    code='reports.order.analysis.franchise.description' /></label>
                <form:select class="form-control form-element consignmentReport-select" path="franchiseDescription" id="reports-franchiseDescription">
                  <option selected="selected" value=""><spring:message code='reports.purchase.analysis.all' /></option>
                  <form:options items="${franchiseCode}" />
                </form:select>
              </div>
            </div>
          </div>
          <!--  <div class="form-group col-lg-4 col-md-8 col-sm-12 col-xs-12 companybrand">
					<div class="row">													
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12  margintopipad20px">
							<label class="pull-left ReportLabel consignmentReport-Label boldtext">Franchise Description</label>												
							 <form:select class="form-control form-element consignmentReport-select" path="franchiseDescription" id="reports-franchiseDescription">
								 <option value="All" selected="selected"><spring:message code='reports.purchase.analysis.all' /></option>
								 <option>Franchise name</option>
								 <form:options items="${franchiseCode}"/>
							  </form:select>															
						</div>
					</div>
				</div> -->
          <div class="form-group col-lg-4 col-md-4 col-sm-4 col-xs-12">
            <label class="pull-left form-label consignmentReport-Label boldtext textheightipad"><spring:message
                code='cutReport.product.code.label' /></label>
            <form:input type="text" data-msg-required='' id="conInvProductCode" path="productCode"
              class="form-control form-element consignmentReport-select" />
          </div>
        </div>
      </div>
    </div>
  </form:form>
  <div class="row jnj-panel-footer buttonWrapperWithBG">
    <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder buttonWrapperWithBG">
      <span class="link-txt boldtext"><spring:message code='reports.download.label' /> </span> <a href="#" class="tertiarybtn marginRight excel"
        id="consignmentReportExcel"> <spring:message code='reports.excel.label' />
      </a><%--  | <a href="#" class="tertiarybtn pdf" id="consignmentReportPdf"> <spring:message code='reports.pdf.label' />
      </a> --%>
	  <div>
			<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
		</div>
    </div>
    <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
      <div class="pull-right btn-mobile">
        <a id="consInventoryReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="javascript:;"> <spring:message
            code='reports.search.labelUX' />
        </a> <a class="tertiarybtn  btn btnclsnormal reset" href="${consignmentInventory}"> <spring:message code='reports.reset.label' />
        </a>
      </div>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-lg-12 col-md-12">
    <div class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
      <table id="datatab-desktop" class="orderAnalysisTable table table-bordered table-striped sorting-table reports-table-desktop">
        <thead>
          <tr>
            <!-- <th>Line Item</th> -->
            <th><spring:message code='cart.common.stock.user' /></th>
            <th><spring:message code='cart.common.stock.user.name' /></th>
            <th><spring:message code='reports.financial.table.product.franchiseDescription' /></th>
            <th><spring:message code='cutReport.product.code.label' /></th>
            <th style="width: 225px !important"><spring:message code='reports.financial.table.product.desc' /></th>
            <th><spring:message code='consignment.quantity.in.Stock' /></th>
            <th><spring:message code='consignment.quantity.agreed.level' /></th>
            <th><spring:message code='consignment.quantity.available' /></th>
            <th style="width: 60px"><spring:message code='reports.order.analysis.UOM' /></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${consInventoryReportList}" var="consInventoryReport" varStatus="count">
            <tr>
              <!-- <td>1</td> -->
              <td>${consInventoryReport.stockLocationAcc}</td>
              <td>${consInventoryReport.stockLocationName}</td>
              <td>${consInventoryReport.franchiseDescription}</td>
              <td>${consInventoryReport.productCode}</td>
              <td class="text-left" style="width: 225px !important">${consInventoryReport.productDesc}</td>
              <td>${consInventoryReport.qtyInStock}</td>
              <td>${consInventoryReport.parLevelQty}</td>
              <td>${consInventoryReport.availableOrderQty}</td>
              <td>${consInventoryReport.uom}</td>
            </tr>
          </c:forEach>
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
