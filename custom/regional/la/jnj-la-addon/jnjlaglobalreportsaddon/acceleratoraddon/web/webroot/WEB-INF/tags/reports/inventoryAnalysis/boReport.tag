<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>

<div class="row jnj-panel backOrderReportBlock LATAM" style="padding-top:0px">
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
		        <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
		          <div class="row form-mobile-row-gap">
		            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 form-row-gap BOR-qucikSelectionDateContainer">
	                    <label class="form-label form-label-select boldtext BOR-qucikSelectionDate"><spring:message code='backorder.report.quickseletion'/></label>
	                    <select class="form-control form-element form-element-select" id="BOR-qucikSelectionDate">
	                        <option value="7"><spring:message code='backorder.quickselection.seven.days' /></option>
	                        <option value="15"><spring:message code='backorder.quickselection.fifteen.days' /></option>
	                        <option value="30"><spring:message code='backorder.quickselection.thirty.days' /></option>
	                        <option value="90"><spring:message code='backorder.quickselection.ninety.days' /></option>
	                      </select>
	                </div>
		            <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 form-row-gap start">
		              <div>
		                <label class="getErrorMessage pull-left form-label form-label-date boldtext" for="fromDate"
		                  data-msg-required='<spring:message code='reports.date.start.error' />'><spring:message code='reports.backorder.date.start' /><span
		                  class="redStar">*</span></label>
		                <div class="input-group form-element form-element-date">
		                  <form:input type="text" data-msg-required='' id="datePicker1" path="fromDate"
		                    class="iconCalender date-picker form-control required" name="fromDate" placeholder="Select date" />
		                  <label for="datePicker1" class="input-group-addon btn border-end border-top border-bottom"> <i class="bi bi-calendar3"></i>
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
		                  <form:input type="text" data-msg-required="" name="toDate" placeholder="Select date" path="toDate" id="datePicker2"
		                    class="iconCalender  date-picker form-control required" />
		                  <label for="datePicker2" class="input-group-addon btn border-end border-top border-bottom"> <i class="bi bi-calendar3"></i>
		                  </label>
		                </div>
		               <div class="registerError"></div>
		              </div>
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
		        	</a>
		        </span>
		     </div>
	      <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
	        <div class="pull-right btn-mobile">
	          <a id="backOrderReportSubmit" class="secondarybtn btn btnclsactive generatereport pull-right" href="#"> <spring:message
	              code='reports.search.labelUX' />
	          </a> <a class="tertiarybtn btn btnclsnormal reset" href="${backOrderURL}"> <spring:message code='backorder.report.reset.label' />
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
      <div class="d-none d-sm-block jnj-panel-for-table mainbody-container" style="overflow-x:auto;">
        <table id="datatab-desktop" class="hidden-xs table table-bordered table-striped sorting-table reports-table-desktop">
          <thead>
            <tr>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.account' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.orderNumber' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.cpo' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.orderDate' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.prodCode' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.prodName' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.qty' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.uom' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.unitPrice' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.totalPrice' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.orderLineStatus' /></th>
              <th class="text-left-cl text-uppercase"><spring:message code='backorder.excel.download.edd' /></th>
            </tr>
          </thead>
          <tbody>
            <c:set var="total" value="0" />
            <c:set var="isoCode" value="" />
            <c:forEach items="${jnjGTBackorderReportResponseDataList}" var="backorderLine" varStatus="count">
              <tr class='even'>
                <td class="text-left-cl">${backorderLine.accountNumber}</td>
                <td class="text-left-cl"><c:set var="orderNumberArr" value="${fn:split(backorderLine.orderNumber, '|')}" /> <c:url
                    value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" /> <span class="orderNumber"><a
                    href="${orderDetailUrl}">${orderNumberArr[0]}</a></span></td>
                <td class="text-left-cl">${backorderLine.customerPO}</td>
                <td class="text-left-cl">${backorderLine.orderDate}
               </td>
               <td class="text-left-cl"><span class="productCode"> <c:url value="${backorderLine.productUrl}" var="productURL" /> <a
                    href="${productURL}">${backorderLine.productCode}</a>
                </span></td>
                <td class="text-left-cl">${backorderLine.productName}</td>
                <td class="text-left-cl">${backorderLine.qty}</td>
                <td class="text-left-cl">${backorderLine.unit}</td>
                <td class="text-left-cl">${currentCurrency.symbol}  ${backorderLine.itemPrice}</td>
                <td class="text-left-cl">${currentCurrency.symbol}  ${backorderLine.extendedPrice}</td>
                <td class="text-left-cl">${backorderLine.status}</td>
                <td class="text-left-cl">
                	<c:choose>
	        			<c:when test="${fn:length(backorderLine.scheduleLines) > 0}">
	        				<c:forEach items="${backorderLine.scheduleLines}" var="scheduleLine">
                            	<p>
	                                <c:choose>
	                                     <c:when test="${not empty scheduleLine.deliveryDate && scheduleLine.quantity ne null && scheduleLine.deliveryDate ne defaultDate}">
	                                        <laFormat:formatDate dateToFormat="${scheduleLine.deliveryDate}" />
	                                        -
	                                        <span class="schedule-quantity">${scheduleLine.quantity}&nbsp;${backorderLine.unit}</span>
	                                    </c:when>
	                                    <c:otherwise>
	                                        <span class="txtFont">
	                                            <spring:message code="order.deliverydate.unavailable"/>
	                                        </span>
	                                    </c:otherwise>
	                                </c:choose>
                            	</p>
                        	</c:forEach>
	        			</c:when>
	        			<c:otherwise>
	        			    <spring:message code="order.deliverydate.unavailable"/>
	        			</c:otherwise>
        			</c:choose>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      <div class="Subcontainer d-block d-sm-none">
        <div class="SortingTable" id="ordersTable_length"></div>
        <table id="multiReportTablemobile" class="table table-bordered table-striped sorting-table bordernone">
          <thead>
            <tr>
              <th class="text-left">
                <spring:message code='reports.backorder.table.jnjorderNumber' />
              </th>
            </tr>
          </thead>
          <tbody>
            <c:set var="total" value="0" />
            <c:set var="isoCode" value="" />
            <c:forEach items="${jnjGTBackorderReportResponseDataList}" var="backorderLine" varStatus="count">
              <tr class="myReportRow">
                <td>
                 <a class="collapseBtn" data-bs-toggle="collapse" href="#report-mobi-${Count.count}" role="button" aria-expanded="false" aria-controls="report-mobi-${Count.count}">
                    <i class="bi bi-plus-lg"></i>
                  </a>
                  <a class="mobileContractRow" href="#"> <c:set var="orderNumberArr" value="${fn:split(backorderLine.orderNumber, '|')}" /> <c:url
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
                        <input type="hidden" value="${backorderLine.extendedPrice}" class="extendedPriceField" /> <span class="priceList">${backorderLine.currency}${backorderLine.extendedPrice}</span>
                      </div>
                    </div>
                  </div></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</c:if>