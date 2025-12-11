<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjlaglobalreportsaddon/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="reports" tagdir="/WEB-INF/tags/addons/jnjlaglobalreportsaddon/reports/common" %>

<templateLa:page pageTitle="${pageTitle}">
    <input type="hidden" name="countryCode" id="countryCode" value="${currentLanguage.isocode}"/>
    <c:url value="/reports/openordersreport" var="openOrderURL" />
    <jsp:useBean id="date" class="java.util.Date"/>
    <fmt:formatDate pattern="MM/dd/yyyy" value="${date}" var="currentDate" />
    <input type="hidden" value='${sessionLanguage}' id="currentLocale"/>
	<div id="Reportspage">
	    <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
		<div class="row content">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<spring:message code='reports.openItem.title.heading'/>
			</div>
		</div>
		<input type="hidden" id="openOrdersReportFormAction" value="${openOrderURL}" />
		<div class="row mainbody-container" id="openOrdersReportPage">
        <reports:openordersreport />
        <div id="getTempDataList">
            <c:forEach var="data" items="${openOrdersReportTemplateData}" varStatus="loop">
                <input type="hidden" id="indexNumber_${loop.index}"
                    templateName="${data.templateName}"
                    accountIds="${data.accountIds}"
                    quickSelection="${data.quickSelection}"
                    fromDate="${data.fromDate}"
                    toDate="${data.toDate}"
                    orderType="${data.orderType}"
                    orderNumber="${data.orderNumber}"
                    productCode="${data.productCode}"
                    shipTo="${data.shipTo}"
                    reportColumns="${data.reportColumns}" >
            </c:forEach>
        </div>
		<form:form name="jnjLaOpenOrdersReportForm" id="openOrdersReportForm" method="POST" action="${openOrderURL}" modelAttribute="jnjLaOpenOrdersReportForm">
		<input id="hddnAccountsString" name="accountIds" type="hidden" value="">
		<input id="hddnAccountsSelectedValue" type="hidden" value="">
        <input id="selectedText" type="hidden" value="selected">
        <input id="downloadType" name="downloadType" type="hidden" path="downloadType" value="" >
        <input id="allText" type="hidden" value="All available accounts">
        <input type="hidden" id="accountid" value="${currentAccountId}">
        <input id="multipleText" type="hidden" value="Multiple">
            <div class="row jnj-panel-body">
                <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="form-label form-label-select boldtext"><spring:message code='openItem.quickselection.heading'/></label>
                            <select id="openOrdersQuickSelection" class="form-element form-element-select selectpicker" tabindex="-98">
                                <option selected value="7"><spring:message code='openItem.quickselection.seven.days'/></option>
                                <option value="15"><spring:message code='openItem.quickselection.fifteen.days'/></option>
                                <option value="30"><spring:message code='openItem.quickselection.thirty.days'/></option>
                                <option value="90"><spring:message code='openItem.quickselection.ninety.days'/></option>
                            </select>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="pull-left form-label form-label-date boldtext">
                                <spring:message code='reports.openItem.calendar.date.FROM'/>
                            </label>
                            <div class="input-group form-element form-element-date">
                                <input id="openOrdersStartDate" name="fromDate" placeholder='select a date' class="iconCalender date-picker form-control openOrdersReportDate" type="text" value="">
                                <label for="openOrdersStartDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;">
                                    <i class="bi bi-calendar3"></i>
                                </label>
                            </div>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="pull-left form-label form-label-date boldtext">
                                <spring:message code='reports.openItem.calendar.date.TO'/>
                            </label>
                            <div class="input-group form-element form-element-date">
                                <input id="openOrdersEndtDate" name="toDate" placeholder='select a date' class="iconCalender date-picker form-control openOrdersReportDate" type="text" value="${currentDate}">
                                <label for="openOrdersEndtDate" class="input-group-addon btn" style="border-left: 1px solid #ced4da;">
                                    <i class="bi bi-calendar3"></i>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <div class="row" id="dropDownWidth">
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="form-label form-label-select boldtext"><spring:message code='reports.openItem.orderType.heading'/></label>
                            <select id="openOrdersOrderType" class="form-element form-element-select selectpicker" name="orderType" tabindex="-98">
                                <option selected value="all">All</option>
                                <c:forEach var="data" items="${orderTypes}">
                                    <option value="${data.key}">${data.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="pull-left salesreport-form-label form-label-select boldtext textheightipad" for="orderNumber"><spring:message code='reports.openItem.orderNumber.heading'/></label>
                            <input type="text" id="orderNumber" name="orderNumber" class="form-control form-element-select" placeholder="<spring:message code='reports.openItem.placeHolder.orderNumber'/>" value="">
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <input id="openOrdersColumnList" type="hidden" name="reportColumns" value="" >
                            <label class="form-label form-label-select boldtext"><spring:message code='reports.openItem.columnInReport.heading'/></label>
                            <select id="selectColumnList" class="form-element form-element-select selectpicker" multiple tabindex="-98">
                            		<option value="default" selected disabled><spring:message code='reports.openItem.default.heading'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.SoldToName'/>"><spring:message code='reports.openItem.columns.SoldToName'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.OrderReference'/>"><spring:message code='reports.openItem.columns.OrderReference'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.EAN'/>"><spring:message code='reports.openItem.columns.EAN'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.SubFranchise'/>"><spring:message code='reports.openItem.columns.SubFranchise'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.TotalUnitsQuantity'/>"><spring:message code='reports.openItem.columns.TotalUnitsQuantity'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.UnitOfMeasure'/>"><spring:message code='reports.openItem.columns.UnitOfMeasure'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.OrderQuantitySalesUnits'/>"><spring:message code='reports.openItem.columns.OrderQuantitySalesUnits'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.OpenOrder'/>"><spring:message code='reports.openItem.columns.OpenOrder'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.OrderCreationDate'/>"><spring:message code='reports.openItem.columns.OrderCreationDate'/></option>
                            		<option value="<spring:message code='reports.openItem.columns.DateRequestedDate'/>"><spring:message code='reports.openItem.columns.DateRequestedDate'/></option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="pull-left salesreport-form-label form-label-select boldtext textheightipad" for="shipTO"><spring:message code='reports.openItem.shipTo.heading'/></label>
                            <input type="text" id="shipTO" name="shipTo" class="form-control form-element-select" placeholder="<spring:message code='reports.openItem.placeHolder.shipTo'/>" value="">
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3">
                            <label class="pull-left salesreport-form-label form-label-select boldtext textheightipad" for="ProductCode"><spring:message code='reports.openItem.productCode.heading'/></label>
                            <input type="text" id="ProductCode" name="productCode" class="form-control form-element-select" placeholder="<spring:message code='reports.openItem.placeHolder.productCode'/>" value="">
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 my-3"></div>
                    </div>
                </div>
            </div>
            <div class="row jnj-panel-body openOrderFormFotter">
                <div class="col-lg-4 col-md-3 col-sm-12 col-xs-12 reportTestSearch mt-4">
                    <div>
                        <label class="form-label form-label-select boldtext"><spring:message code='reports.openItem.label.selecttemplate'/></label>
                            <select id="selectTemplateName" class="form-element form-element-select selectpicker" tabindex="-98">
                                <option selected value="default"><spring:message code='reports.openItem.label.selecttemplate'/></option>
                                <c:forEach var="data" items="${openOrdersReportTemplateData}">
                                    <option value="${data.templateName}">${data.templateName}</option>
                                </c:forEach>
                            </select>
                        </label>
                    </div>
                </div>
                <div class="col-lg-8 col-md-9 col-sm-12 col-xs-12 link-holder" id="openOrdersPageButton">
                    <div class="pull-right mt-3 OpenOrderRepurtButton">
                        <div class="resetBtnDiv">
                            <button type="button" class="btn btnclsnormal resetButton">
                                <spring:message code='reports.openItem.Reset.Button'/>
                            </button>
                        </div>
                        <div class="deletTempDiv no-display">
                            <button type="button" class="btn btnclsnormal deletTempBtn">
                                <spring:message code='reports.openItem.button.DeleteTemplate'/>
                            </button>
                        </div>
                        <button type="button" class="btn btnclsactive templatebutton" >
                            <spring:message code='reports.openItem.saveas.Button'/>  <br>
                            <spring:message code='reports.openItem.template.Button'/>
                        </button>
                        <button id="runReport"  type="submit" class="btn btnclsactive" >
                            <spring:message code='reports.openItem.generate.button'/>
                        </button>
                    </div>
                    <div class="downloadButtonHolder">
                        <span><span class="link-txt boldtext"><spring:message code='reports.openItem.label.download'/></span>
                            <a href="#" id="openOrdersReportExcel" class="tertiarybtn marginRight excel">
                               <spring:message code='reports.openItem.downloadType.excel'/>
                            </a> |
                            <a href="#" id="openOrdersReportPdf" class="tertiarybtn pdf">
                               <spring:message code='reports.openItem.downloadType.pdf'/>
                            </a>
                        </span>
                    </div>
                </div>
            </div>
        </div>
		</form:form>
		<!-- openOrders Table start -->
		<div class="row mainbody-container mt-5" id="openOrdersReportTable" style="display: none">
            <div class="col-lg-3 col-md-2 mt-3">
                <div class="bottomBorder">
                   <i class="bi bi-list" style=" -webkit-text-stroke: 1px;"></i>
                   <lable class="ml-1 boldtext"><u> <spring:message code='reports.openItem.outline.heading'/> </u></lable>
               </div>
               <p class="mt-3 boldtext"> <spring:message code='reports.openItem.columns.heading'/> </p>
               <input id="columnSearch" class="form-control text placeholder ui-autocomplete-input" type="text" name="text" maxlength="100" placeholder="<spring:message code='reports.openItem.placeHolder.addColumns'/>" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true" style="color: rgb(204, 204, 204);">
                <div class="columnButtons">
                    <label id="SoldTo" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.SoldTo'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                    </label>
                    <label id="SoldToName" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.SoldToName'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                    </label>
                    <label id="ShipTo" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.ShipTo'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                    </label>
                    <label id="ShipToName" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.ShipToName'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                    </label>
                    <label id="OrderReference" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.OrderReference'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                    </label>
                    <label id="JnJOrderNumber" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.JnJOrderNumber'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="OrderType" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.OrderType'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="ProductCode" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.ProductCode'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="EAN" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.EAN'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="ProductDescription" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.ProductDescription'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="SubFranchise" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.SubFranchise'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="TotalUnitsQuantity" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.TotalUnitsQuantity'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="UnitOfMeasure" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.UnitOfMeasure'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="OrderQuantitySalesUnits" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.OrderQuantitySalesUnits'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="OpenOrder" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.OpenOrder'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="LineNumber" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.LineNumber'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="scheduleLine" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.scheduleLine'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="OrderCreationDate" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.OrderCreationDate'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="DateRequestedDate" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.DateRequestedDate'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                    <label id="EstimatedDeliveryDate" class="btn radiousButton boldtext">
                        <spring:message code='reports.openItem.columns.EstimatedDeliveryDate'/>
                        <i class="bi bi-x-lg columeCloseIcon" style=" -webkit-text-stroke: 1px;"></i>
                     </label>
                </div>
            </div>
            <div class="col-lg-9 col-md-10 p-0 openOrdersReportTable">
                <div id="datatab-desktop-wrapper" class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container table-responsive">
                    <table id="datatab-desktop" class="ajaxTableWrapperContent table table-bordered table-striped lasorting-table" data-total-results="${searchPageData.pagination.totalNumberOfResults}">
                        <thead>
                            <tr>
                                <th class="no-sort column_1 default"          id="SoldTo"> <spring:message code='reports.openItem.columns.SoldTo'/> </th>
                                <th class="no-sort column_2"          id="SoldToName"> <spring:message code='reports.openItem.columns.SoldToName'/> </th>
                                <th class="no-sort column_3 default"  id="ShipTo"> <spring:message code='reports.openItem.columns.ShipTo'/> </th>
                                <th class="no-sort column_4 default"  id="ShipToName"> <spring:message code='reports.openItem.columns.ShipToName'/> </th>
                                <th class="no-sort column_5"          id="OrderReference"> <spring:message code='reports.openItem.columns.OrderReference'/> </th>
                                <th class="no-sort column_6 default"  id="JnJOrderNumber"> <spring:message code='reports.openItem.columns.JnJOrderNumber'/> </th>
                                <th class="no-sort column_7 default"  id="OrderType"> <spring:message code='reports.openItem.columns.OrderType'/> </th>
                                <th class="no-sort column_8 default"  id="ProductCode"> <spring:message code='reports.openItem.columns.ProductCode'/> </th>
                                <th class="no-sort column_9"          id="EAN"> <spring:message code='reports.openItem.columns.EAN'/> </th>
                                <th class="no-sort column_10 default" id="ProductDescription"> <spring:message code='reports.openItem.columns.ProductDescription'/> </th>
                                <th class="no-sort column_11"         id="SubFranchise"> <spring:message code='reports.openItem.columns.SubFranchise'/> </th>
                                <th class="no-sort column_12"         id="TotalUnitsQuantity"> <spring:message code='reports.openItem.columns.TotalUnitsQuantity'/> </th>
                                <th class="no-sort column_13"         id="UnitOfMeasure"> <spring:message code='reports.openItem.columns.UnitOfMeasure'/> </th>
                                <th class="no-sort column_14"         id="OrderQuantitySalesUnits"> <spring:message code='reports.openItem.columns.OrderQuantitySalesUnits'/> </th>
                                <th class="no-sort column_15"         id="OpenOrder"> <spring:message code='reports.openItem.columns.OpenOrder'/> </th>
                                <th class="no-sort column_16 default" id="LineNumber"> <spring:message code='reports.openItem.columns.LineNumber'/> </th>
                                <th class="no-sort column_17 default" id="scheduleLine"> <spring:message code='reports.openItem.columns.scheduleLine'/> </th>
                                <th class="no-sort column_18"         id="OrderCreationDate"> <spring:message code='reports.openItem.columns.OrderCreationDate'/> </th>
                                <th class="no-sort column_19"         id="DateRequestedDate"> <spring:message code='reports.openItem.columns.DateRequestedDate'/> </th>
                                <th class="no-sort column_20 default" id="EstimatedDeliveryDate"> <spring:message code='reports.openItem.columns.EstimatedDeliveryDate'/> </th>
                            </tr>
                        </thead>
                        <tbody>
                           <c:forEach var="data" items="${openOrdersReportResponseDataList}">
                           <c:if test="${fn:length(data.scheduleLines) > 0}">
                           <c:forEach var="scheduleLinesData" items="${data.scheduleLines}">
                                <tr>
                                <td class="no-sort column_1 default"          id="SoldTo"> ${data.accountNumber} </td>
                                <td class="no-sort column_2"          id="SoldToName"> ${data.accountName} </td>
                                <td class="no-sort column_3 default"  id="ShipTo"> ${data.shipToAccount} </td>
                                <td class="no-sort column_4 default"  id="ShipToName"> ${data.shipToName} </td>
                                <td class="no-sort column_5"          id="OrderReference">${data.customerPO}</td>
                                <td class="no-sort column_6 default"  id="JnJOrderNumber"><c:set var="orderNumberArr" value="${fn:split(data.orderNumber, '|')}" /> <c:url
                    value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" /> <span class="orderNumber"><a
                    href="${orderDetailUrl}">${orderNumberArr[0]}</a></span></td>
                                <td class="no-sort column_7 default"  id="OrderType"> ${data.orderType} </td>
                                <td class="no-sort column_8 default"  id="ProductCode"> <span class="productCode"> <c:url value="${data.productUrl}" var="productURL" /> <a
                    href="${productURL}">${data.productCode}</a>
                </span></td>
                                <td class="no-sort column_9"          id="EAN"> ${data.productGTIN} </td>
                                <td class="no-sort column_10 default" id="ProductDescription"> ${data.productName} </td>
                                <td class="no-sort column_11"         id="SubFranchise"> ${scheduleLinesData.subFranchise} </td>
                                <td class="no-sort column_12"         id="TotalUnitsQuantity"> ${scheduleLinesData.requestedUnitsTotalQuantity} </td>
                                <td class="no-sort column_13"         id="UnitOfMeasure"> ${data.unit} </td>
                                <td class="no-sort column_14"         id="OrderQuantitySalesUnits">${scheduleLinesData.amountPendingDelivery}   </td>
                                <td class="no-sort column_15"         id="OpenOrder">${scheduleLinesData.quantityPendingStock}</td>
                                <td class="no-sort column_16 default" id="LineNumber"> ${data.lineNumber} </td>
                                <td class="no-sort column_17 default" id="scheduleLine">${scheduleLinesData.lineNumber}</td>
                                 <td class="no-sort column_18"         id="OrderCreationDate"> ${data.orderDate} </td>
                                <td class="no-sort column_19"         id="DateRequestedDate"> ${data.requestedDeliveryDate} </td>
                                <td class="no-sort column_20 default" id="EstimatedDeliveryDate"> ${scheduleLinesData.formattedDeliveryDate} </td>

                                </tr>
                                </c:forEach>
                                    </c:if>

                               <c:if test="${fn:length(data.scheduleLines) == 0}">
                                     <tr>
                                <td class="no-sort column_1 default"          id="SoldTo"> ${data.accountNumber} </td>
                                <td class="no-sort column_2"          id="SoldToName"> ${data.accountName} </td>
                                <td class="no-sort column_3 default"  id="ShipTo"> ${data.shipToAccount} </td>
                                <td class="no-sort column_4 default"  id="ShipToName"> ${data.shipToName} </td>
                                <td class="no-sort column_5"          id="OrderReference">${data.customerPO}</td>
                                <td class="no-sort column_6 default"  id="JnJOrderNumber"><c:set var="orderNumberArr" value="${fn:split(data.orderNumber, '|')}" /> <c:url
                    value="/order-history/order/${orderNumberArr[1]}" var="orderDetailUrl" /> <span class="orderNumber"><a
                    href="${orderDetailUrl}">${orderNumberArr[0]}</a></span></td>
                                <td class="no-sort column_7 default"  id="OrderType"> ${data.orderType} </td>
                                <td class="no-sort column_8 default"  id="ProductCode"> <span class="productCode"> <c:url value="${data.productUrl}" var="productURL" /> <a
                    href="${productURL}">${data.productCode}</a>
                </span></td>
                                <td class="no-sort column_9"          id="EAN"> ${data.productGTIN} </td>
                                <td class="no-sort column_10 default" id="ProductDescription"> ${data.productName} </td>
                                <td class="no-sort column_11"         id="SubFranchise"></td>
                                <td class="no-sort column_12"         id="TotalUnitsQuantity"></td>
                                <td class="no-sort column_13"         id="UnitOfMeasure"> ${data.unit} </td>
                                <td class="no-sort column_14"         id="OrderQuantitySalesUnits"> </td>
                                <td class="no-sort column_15"         id="OpenOrder"></td>
                                <td class="no-sort column_16 default" id="LineNumber"> ${data.lineNumber} </td>
                                <td class="no-sort column_17 default" id="scheduleLine"></td>
                                 <td class="no-sort column_18"         id="OrderCreationDate"> ${data.orderDate} </td>
                                <td class="no-sort column_19"         id="DateRequestedDate"> ${data.requestedDeliveryDate} </td>
                                <td class="no-sort column_20 default" id="EstimatedDeliveryDate"> ${data.estimatedDeliveryDate} </td>

                                </tr>
                                </c:if>
                                </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>
		</div>
	</div>
	<!-- openOrders Table end -->
    <!-- Modal content-->
    <div class="modal fade jnj-popup" id="openOrdersTemplate" role="dialog">
        <div class="modal-dialog modalcls modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close clsBtn" data-bs-dismiss="modal">
                        <spring:message code='reports.openItem.button.Colse'/>
                    </button>
                    <h4 class="modal-title">
                        <spring:message code='reports.openItem.button.SaveTemplate'/>
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="saveTemplateForm">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <label class="pull-left salesreport-form-label form-label-select boldtext textheightipad" for="saveTemplate"><spring:message code='reports.openItem.Popup.TemplateName'/></label>
                                <input type="text" id="saveTemplate" name="TemplateName" class="form-control form-element-select" placeholder="<spring:message code='reports.openItem.modal.placeHolder'/>" value="">
                                <div class="tempError" style="display: none">
                                    <p style="color: red;text-align: center;margin-top: 5px;font-size: initial;"> <spring:message code='reports.openItem.template.error'/> </p>
                                </div>
                                <div class="tempErrorDuplicate" style="display: none">
                                    <p style="color: red;text-align: center;margin-top: 5px;font-size: initial;"> <spring:message code='reports.openItem.template.duplicateError'/> </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" style="border-color: whitesmoke" class="btnclsactive  pull-right"
                        id="saveTemplateButton">
                        <spring:message code='reports.openItem.Popup.save.button'/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</templateLa:page>
