<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<div class="row jnj-panel backOrderReportBlock" style="padding-top:0px">
	<c:url value="/reports/inventoryAnalysis/backorder" var="backOrderURL" />
	<input id="multipleText" type="hidden" value="<spring:message code='reports.backorder.multiple' />" /> 
	<input id="selectedText" type="hidden" value="<spring:message code='reports.backorder.selected' />" />
	<input id="allText" type="hidden" value="<spring:message code='reports.backorder.all' />" />
	<input type="hidden" id="originalFormAction" value="${backOrderURL}" /> 
	<input type="hidden" id="accountid" value="${currentAccountId}" />
	<input type="hidden" id="hddnCurrentAccount" value="${currentAccountId}"/>
	
	<c:forEach items="${user.roles}" var="role">
	  <c:if test="${role eq 'viewOnlyStandardGroup'}">
	    <c:out value="${user.roles}"></c:out>
	    <c:set var="viewOnlyStandard">false</c:set>
	  </c:if>
	</c:forEach>
	
	<div class="col-lg-12 col-md-12">
	    <form:form id="backOrderReportForm" action="${backOrderURL}" modelAttribute="jnjGlobalBackorderReportForm" method="POST">
	    <form:input type="hidden" value="${currentAccountId}" id="hddnAccountsString" path="accountIds" />
		<form:input type="hidden" id="hddnAccountsSelectedValue" path="accountsSelectedValue" />
	    <form:input id="downloadType" type="hidden" path="downloadType" value="" />
	      <div class="row jnj-panel-body">
	        <div class="form-group col-lg-3 col-md-3 col-sm-12 col-xs-12">
	          <div class="row form-mobile-row-gap">
	            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 form-row-gap start">
	              <div>
	                <label class="getErrorMessage pull-left form-label form-label-date boldtext" for="fromDate"
	                  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /><span
	                  class="redStar">*</span></label>
	                <div class="input-group form-element form-element-date">
	                  <form:input type="text" data-msg-required='' id="reports-fromDate" path="fromDate"
	                    class="iconCalender date-picker form-control required" name="fromDate" placeholder="Select date" />
	                  <label for="reports-fromDate" class="input-group-addon btn"> <span class="glyphicon glyphicon-calendar"></span>
	                  </label>
	                </div>
	                <div class="registerError"></div>
	              </div>
	            </div>
	            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12  end">
	              <div>
	                <label class="getErrorMessage pull-left form-label form-label-date boldtext ipadterritory" for="toDate"
	                  data-msg-required="<spring:message code='reports.date.end.error' />"><spring:message code='reports.backorder.date.end' /><span
	                  class="redStar">*</span></label>
	                <div class="input-group form-element form-element-date">
	                  <form:input type="text" data-msg-required="" name="toDate" placeholder="Select date" path="toDate" id="reports-toDate"
	                    class="iconCalender  date-picker form-control required" />
	                  <label for="reports-toDate" class="input-group-addon btn"> <span class="glyphicon glyphicon-calendar"></span>
	                  </label>
	                </div>
	               <div class="registerError"></div>
	              </div>
	            </div>
	          </div>
	        </div>
	        <div class="form-group col-lg-9 col-md-9 col-sm-12 col-xs-12">
	          <div class="row">
	            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
	              <label class="pull-left form-label form-label-select boldtext textheightipad reportslabelwidth getErrorMessage" for="productCode"
	                class="getErrorMessage" data-msg-required="<spring:message code='reports.purchase.analysis.product.code.error' />"><spring:message
	                  code='reports.inventory.product.code' /></label>
	              <form:input class="form-control form-element prd-code-txt" type="text" data-msg-required="" id="productCode" name="productCode"
	                path="productCode" style="width: 64%" />
	              <div class="registerError">
	                <c:if test="${invalidProduct eq true}">
	                  <label for="productCode" class="error"> <spring:message code='reports.purchase.analysis.product.invalid' />
	                  </label>
	                </c:if>
	              </div>
	            </div>
	            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 no-pad-right-cl">
	              <div class="checkbox checkbox-info pull-right show-all-chckbox">
	                <div id="allreport-label">
	                  <label for="allreports"> <spring:message code='reports.backorder.all.reports' />
	                  </label>
	                </div>
	                <form:checkbox path="allreports" id="allreports" value="" checked="true" />
	              </div>
	              <!--AAOL-5794 start -->
	              <input type="hidden" value="${allreports}" id="selectAllReports">
	              <!-- AAOL-5794 end -->
	            </div>
	          </div>
	        </div>
	      </div>
	    </form:form>
	    <div class="row jnj-panel-footer">
	      <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
	        <span><span class="link-txt boldtext"><spring:message code='reports.download.label' /></span> <a href="#" id="backOrderReportExcel"
	          class="tertiarybtn marginRight excel"> <spring:message code='reports.excel.label' />
	        </a> |<a href="#" id="backOrderReportPdf" class="tertiarybtn pdf"> <spring:message code='reports.pdf.label' />
	        </a> </span>
			<div>
			<b><label class="link-txt boldtext" style="color: limegreen ;font-weight: normal;margin-top: -18px;font-size: 14px;"><spring:message code='reports.extra.fields'/></label></b>
		</div>
	      </div>
	      <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
	        <div class="pull-right btn-mobile">
	          <a id="backOrderReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="#"> <spring:message
	              code='reports.search.labelUX' />
	          </a> <a class="tertiarybtn btn btnclsnormal reset" href="${backOrderURL}"> <spring:message code='reports.reset.label' />
	          </a>
	        </div>
	      </div>
	    </div>
	 </div>
</div>
<c:if test="${jnjGTBackorderReportResponseDataList eq null && noDataFound eq 'true'}">
  <div class="info positive reportBody no-data-report boxshadow">
    <spring:message code='reports.table.no.data' />
  </div>
</c:if>

<c:if test="${jnjGTBackorderReportResponseDataList ne null}">
  <div class="row">
    <div class="col-lg-12 col-md-12">
      <div class="hidden-xs jnj-panel-for-table mainbody-container">
        <table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
          <thead>
            <tr>
              <!-- text-center no-sort-->
              <th class="text-left-cl text-uppercase"><spring:message code='orderHistoryPage.account' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.customerPORef' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.jnjorderNumber' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.orderDate' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.productName' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.productCodeRpt' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.qtyRpt' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.unitRpt' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.status' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='reports.backorder.table.estimatedAvailability' /></th>
            </tr>
          </thead>
          <tbody>
            <c:set var="total" value="0" />
            <c:set var="isoCode" value="" />
            <c:forEach items="${jnjGTBackorderReportResponseDataList}" var="backorderLine" varStatus="count">
              <c:set var="extendedPrice" value="${fn:split(backorderLine.extendedPrice, ' ')}" />
              <c:set var="itemExtendedPrice" value="${extendedPrice[0]}" />
              <c:set var="isoCode" value="${extendedPrice[1]}" />
              <tr>
                <td class="text-left-cl">${backorderLine.accountNumber}</td>
                <td class="text-left-cl">${backorderLine.customerPO}</td>
                <td class="text-left-cl"><c:set var="orderNumberArr" value="${fn:split(backorderLine.orderNumber, '|')}" /> <c:url
                    value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" /> <span class="orderNumber"><a
                    href="${orderDetailUrl}">${orderNumberArr[1]}</a></span></td>
                <td class="text-left-cl">${backorderLine.orderDate}
               </td>
                <td class="text-left-cl">${backorderLine.productName}</td>
                <td class="text-left-cl"><span class="productCode"> <c:url value="${backorderLine.productUrl}" var="productURL" /> <a
                    href="${productURL}">${backorderLine.productCode}</a>
                </span></td>
                <td class="text-left-cl">${backorderLine.qty}</td>
                <td class="text-left-cl">${backorderLine.unit}</td>
                <td class="text-left-cl">${backorderLine.status}</td>
                <td class="text-left-cl">${backorderLine.estimatedAvailability}
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      <!-- Table collapse for mobile device-->
      <div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
        <!-- <div class="row horizontal-line"></div> -->
        <div class="SortingTable" id="ordersTable_length"></div>
        <table id="multiReportTablemobile" class="table table-bordered table-striped sorting-table bordernone">
          <thead>
            <tr>
              <th class="text-left">
                <!-- Mobile/Tablet Fix - 23 --> <spring:message code='reports.backorder.table.jnjorderNumber' />
              </th>
            </tr>
          </thead>
          <tbody>
            <c:set var="total" value="0" />
            <c:set var="isoCode" value="" />
            <c:forEach items="${jnjGTBackorderReportResponseDataList}" var="backorderLine" varStatus="count">
              <c:set var="extendedPrice" value="${fn:split(backorderLine.extendedPrice, ' ')}" />
              <c:set var="itemExtendedPrice" value="${extendedPrice[0]}" />
              <c:set var="isoCode" value="${extendedPrice[1]}" />
              <tr class="myReportRow">
                <td><a data-toggle="collapse" data-parent="#accordion" href="#report-mobi-${Count.count}"
                  class="ref_no toggle-link panel-collapsed"> <span class="glyphicon glyphicon-plus"></span>
                </a> <a class="mobileContractRow" href="#"> <c:set var="orderNumberArr" value="${fn:split(backorderLine.orderNumber, '|')}" /> <c:url
                      value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" /> <span class="orderNumber"><a
                      href="${orderDetailUrl}">${orderNumberArr[1]}</a></span>
                </a>
                  <div id="report-mobi-${Count.count}" class="panel-collapse collapse">
                    <div class="panel-body details">
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.orderDate" />
                      </div>
                      <div>${backorderLine.orderDate}
                      </div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.customerPORef" />
                      </div>
                      <div>${backorderLine.customerPO}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.shipTo" />
                      </div>
                      <div>${backorderLine.shipToName}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.productCodeRpt" />
                      </div>
                      <div>
                        <span class="productCode"> <c:url value="${backorderLine.productUrl}" var="productURL" /> <a href="${productURL}">${backorderLine.productCode}</a>
                        </span>
                      </div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.productName" />
                      </div>
                      <div>${backorderLine.productName}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.qtyRpt" />
                      </div>
                      <div>${backorderLine.qty}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.unitRpt" />
                      </div>
                      <div>${backorderLine.unit}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.estimatedAvailability" />
                      </div>
                      <div>${backorderLine.estimatedAvailability}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.itemPriceRpt" />
                      </div>
                      <div>${backorderLine.currency}${backorderLine.itemPrice}</div>
                      <div class="lblFont boldtext">
                        <spring:message code="reports.backorder.table.total.price" />
                      </div>
                      <div>
                        <c:set var="total" value="${total+itemExtendedPrice}" />
                        <input type="hidden" value="${backorderLine.extendedPrice}" class="extendedPriceField" /> <span class="priceList">${backorderLine.currency}${backorderLine.extendedPrice}</span>
                      </div>
                    </div>
                  </div></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      <!--Accordian Ends here -->
    </div>
  </div>
</c:if>
