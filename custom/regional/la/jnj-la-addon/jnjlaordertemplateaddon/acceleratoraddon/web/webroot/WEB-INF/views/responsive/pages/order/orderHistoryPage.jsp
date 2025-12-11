<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="commonLa" tagdir="/WEB-INF/tags/addons/jnjlaordertemplateaddon/responsive/common" %>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <input type="hidden" name="countryCode" id="countryCode" value="${currentLanguage.isocode}"/>
    <div id="Orderhistorypage" class="lessPadding">
        <div class="row">
            <div class="col-lg-12 col-md-12">
                <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
                <div class="row content">
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                        <spring:message code="orderHistoryPage.heading" />
                    </div>
                </div>
            </div>
        </div>
        <jsp:useBean id="date" class="java.util.Date"/>

        <c:choose>
            <c:when test="${'en' eq sessionLanguage}">
                <fmt:formatDate pattern="MM/dd/yyyy" value="${date}" var="currentDate" />
            </c:when>
            <c:otherwise>
                <fmt:formatDate pattern="dd/MM/yyyy" value="${date}" var="currentDate" />
            </c:otherwise>
        </c:choose>

        <input type="hidden" value='${sessionLanguage}' id="currentLocale"/>

        <c:url value="/order-history" var="orderHistoryFormUrl" />
        <form:form id="orderHistoryForm" name="orderHistoryForm" method="POST" action="${orderHistoryFormUrl}" modelAttribute="orderHistoryForm">
            <input style="display:none;" type="hidden"  id="searchRequest" name="searchRequest" value="${orderHistoryForm.searchRequest}" />
            <input style="display:none;" type="hidden"  id="accounts" name="accounts" value="${selectedaccounts}" />
            <input style="display:none;" type="hidden"  id="totAccountsSelected" name="totAccountsSelected" value="${totalAccountsSelected}" />
            <form:input type="hidden" id="isSelectAllAccount" path="selectAllAccount" />
            <div class="row jnj-panel mainbody-container">
                <div class="col-lg-12 col-md-12">
                    <div class="row jnj-panel-header">
                        <div class="col-lg-8 col-md-8 col-sm-6 col-xs-11">
                            <div class="row">
                                <div id="usr-address" class="amazon">
						  <span id="defaultAccountSel">
                           <c:choose>
                               <c:when test="${accountUID ne null}">
                                   ${accountUID}
                               </c:when>
                               <c:otherwise>
                                   ${user.currentB2BUnitID}
                               </c:otherwise>
                           </c:choose>
                           ,
                           <c:choose>
                               <c:when test="${accountName ne null}">
                                   <c:set var="dispAccountName" value="${accountName}" />
                               </c:when>
                               <c:otherwise>
                                   <c:set var="dispAccountName" value="${user.currentB2BUnitName} " />
                               </c:otherwise>
                           </c:choose>
                           <c:choose>
                               <c:when test="${fn:length(dispAccountName)>45 }">
                                   <c:set var="dispAccountNameSubString" value="${fn:substring(dispAccountName, 0, 45)}..." />
                                   <label title="${dispAccountName}" class="label">${dispAccountNameSubString}</label>
                               </c:when>
                               <c:otherwise>
                                   <c:out value="${dispAccountName}"></c:out>
                               </c:otherwise>
                           </c:choose>
                           <c:if test="((accountGLN ne 'null' && not empty accountGLN) || not empty user.currentB2BUnitGLN )}">
                              <li>
                                 <spring:message code="header.information.account.gln" />
                                 &nbsp;
                                 <span>
                                    <c:choose>
                                        <c:when test="${accountGLN ne 'null' && not empty accountGLN}">
                                            ${accountGLN}
                                        </c:when>
                                        <c:otherwise>
                                            ${user.currentB2BUnitGLN}
                                        </c:otherwise>
                                    </c:choose>
                                 </span>
                              </li>
                           </c:if>
						   </span>
						   <span id="singleAccountSel" style="display:none;">
										<spring:message code="orderHistoryPage.account.heading" />: ${selectedaccounts}
									</span>
									<span id="multipleAccountSel" style="display:none;">
										<spring:message code="orderHistoryPage.totalAccount.heading.part1" />${totalAccountsSelected}&nbsp;<spring:message code="orderHistoryPage.totalAccount.heading.part2" />
						   </span>
                                    <a href="#" id="selectAccountlatam">
                                        <spring:message code="orderHistoryPage.change" />
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
                            <div class="checkbox checkbox-info pull-right selectchkbox mt-2">
                                <input id="check4" class="styled orderHistorySelectAll" type="checkbox">
                                <label for="check4">
                                    <spring:message code="orderHistoryPage.selectallaccounts" />
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row jnj-panel-body">
                        <div  class="form-group col-lg-5 col-md-5 col-sm-6 col-xs-12 searchby mb-3">
                            <label class="pull-left form-label form-label-select-large boldtext">
                                <spring:message code="orderHistoryPage.searchBy" />
                            </label>

                            <c:choose>
                                <c:when test="${not empty valToCompare}">
                                    <form:select id="searchby" path="searchBy"
                                                 class="form-control form-element form-element-select-large">
                                        <form:option value="${poNumberVal}">
                                        </form:option>
                                        <form:options items="${searchOptions}" itemLabel="name"
                                                      itemValue="code" />
                                    </form:select>
                                </c:when>
                                <c:otherwise>
                                    <form:select id="searchby" path="searchBy"
                                                 class="form-control form-element form-element-select-large">
                                        <form:option value="">
                                            <spring:message code="orderHistoryPage.select" />
                                        </form:option>
                                        <form:options items="${searchOptions}" itemLabel="name"
                                                      itemValue="code" />
                                    </form:select>
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <div class="col-lg-4 col-md-5 col-sm-6 col-xs-12 mb-3">
                            <input type="hidden"
                                   value='<spring:message code="orderHistoryPage.search.search" />'
                                   id="searchTitle" />
                            <c:choose>
                                <c:when test="${not empty valToCompare}">

                                    <form:input type="text" path="searchText" title="Enter Text"
                                                value="${valToCompare}" class="form-control rounded-textbox"
                                                id="searchByInput" />
                                </c:when>
                                <c:otherwise>

                                    <form:input type="text" path="searchText" title="Enter Text"
                                                placeholder="${searchTitle}"
                                                class="form-control rounded-textbox" id="searchByInput" />
                                </c:otherwise>


                            </c:choose>

                        </div>
                    </div>
                    <div class="row jnj-panel-body">
                        <div class="form-group col-lg-4 col-md-4 col-sm-12 col-xs-12">
                            <div class="row marginbottomipad25px">
                                <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 start">
                                    <label class="pull-left form-label form-label-date boldtext">
                                        <spring:message code="orderHistoryPage.from" />
                                    </label>
                                    <div class="input-group form-element form-element-date">
                                        <input id="datePicker1" name="startDate" placeholder='<spring:message code="orderHistoryPage.search.selectDate" />' class="date-picker form-control orderHistoryDate" type="text" value="${startDate}">
                                        <label for="datePicker1" class="input-group-addon btn input-group-addon btn border-end border-top border-bottom" id="datePicker1Icon">
                                            <i class="bi bi-calendar3"></i>
                                        </label>
                                    </div>
                                </div>
                                <p class="pull-right errorMessage d-none" style="color: #B41601; padding-right: 20px;" id="datePicker1Error">
                                    <spring:message code="orderHistoryPage.dateError" />
                                </p>
                                <div class="col-lg-12 col-md-12 col-sm-6 col-xs-12 margintop20px end">
                                    <label
                                            class="pull-left form-label form-label-date boldtext ipadterritory">
                                        <spring:message code="orderHistoryPage.to" />
                                    </label>
                                    <div class="input-group form-element form-element-date">
                                        <input id="datePicker2" name="endDate" placeholder='<spring:message code="orderHistoryPage.search.selectDate" />' class="date-picker form-control orderHistoryDate" type="text" value="${endDate}">
                                        <label for="datePicker2" class="input-group-addon btn input-group-addon btn border-end border-top border-bottom" id="datePicker2Icon">
                                            <i class="bi bi-calendar3"></i>
                                        </label>
                                    </div>
                                    <div id="orderHistoryDateError" class="registerError"></div>
                                </div>
                                <p class="pull-right errorMessage d-none" style="color: #B41601; padding-right: 20px;" id="datePicker2Error">
                                    <spring:message code="orderHistoryPage.dateError" />
                                </p>
                            </div>
                        </div>
                        <div class="form-group col-lg-8 col-md-8 col-sm-12 col-xs-12 companybrand">
                            <div class="row">
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                    <label class="pull-left form-label form-label-select boldtext textheightipad">
                                        <spring:message code="orderHistoryPage.orderTypeheader" />
                                    </label>
                                    <c:if test="${isMddSite}">
                                        <form:select path="orderType" id="orderType" class="form-control form-element form-element-select">
                                            <form:option value="">
                                                <spring:message code="orderHistoryPage.all" />
                                            </form:option>
                                            <form:options items="${orderTypes}" itemLabel="name" itemValue="code" />
                                        </form:select>
                                    </c:if>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 analysisvariable">
                                    <label class="pull-left form-label form-label-select boldtext textheightipad ipadterritory">
                                        <spring:message code="orderHistoryPage.channelColon" />
                                    </label>
                                    <form:select id="channel" path="channel" class="form-control form-element form-element-select">
                                        <form:option value="">
                                            <spring:message code="orderHistoryPage.all" />
                                        </form:option>
                                        <form:options items="${channels}" itemLabel="name" itemValue="code" />
                                    </form:select>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px orderedfrom">
                                    <label class="pull-left form-label form-label-select boldtext textheightipad">
                                        <spring:message code="orderHistoryPage.orderStatus" />
                                    </label>
                                    <form:select id="orderStatus" path="orderStatus" class="form-control form-element form-element-select">
                                        <form:option value="">
                                            <spring:message code="orderHistoryPage.all" />
                                        </form:option>
                                        <form:options items="${orderStatus}" itemLabel="name" itemValue="code" />
                                    </form:select>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop20px margintopipad20px linestatus">
                                    <label class="pull-left form-label form-label-select boldtext ipadterritory linestatustext">
                                        <spring:message code="orderHistoryPage.lineStatus" />
                                    </label>
                                    <form:select id="lineStatus" path="lineStatus" class="form-control form-element form-element-select">
                                        <form:option value="">
                                            <spring:message code="orderHistoryPage.all" />
                                        </form:option>
                                        <form:options items="${lineStatus}" itemLabel="name" itemValue="code" />
                                    </form:select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row jnj-panel-footer">
                        <div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
                            <commonLa:downloadResultsTag />
                        </div>
                        <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
                            <div class="pull-right btn-mobile">
                                <button type="button" class="btn btnclsactive searchbtn pull-right" id="ordHistorySearch">
                                    <spring:message code="orderHistoryPage.search" />
                                </button>
                                <button type="button" class="btn btnclsnormal reset orderHistoryReset">
                                    <spring:message code="orderHistoryPage.reset" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
        <div class="row">
            <div class="col-lg-12 col-md-12">
                <div id="datatab-desktop-wrapper" class="d-none d-lg-block d-xl-block d-xxl-block jnj-panel-for-table mainbody-container table-responsive">
                    <table id="datatab-desktop" class="ajaxTableWrapperContent table table-bordered lasorting-table" data-total-results="${searchPageData.pagination.totalNumberOfResults}">
                        <thead>
                        <tr>
                            <th data-column-name="order">
                                <spring:message code="orderHistoryPage.order" />
                            </th>
                            <th class="no-sort">
                                <spring:message code="orderHistoryPage.po" />
                            </th>
                            <th class="no-sort">
                                <spring:message code="orderHistoryPage.orderType" />
                            </th>
                            <th class="no-sort">
                                <spring:message code="orderHistoryPage.contractNumber" />
                            </th>
                            <th data-column-name="orderDate">
                                <spring:message code="orderHistoryPage.orderDate" />
                            </th>
                            <th class="no-sort">
                                <spring:message code="orderHistoryPage.channel" />
                            </th>
                            <th class="no-sort">
                                <spring:message code="orderHistoryPage.status" />
                            </th>
                            <th data-column-name="total">
                                <spring:message code="orderHistoryPage.total" />
                            </th>
                        </tr>
                        </thead>
                        <div class="">
                            <c:choose>
                                <c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
                                    <c:forEach items="${searchPageData.results}" var="order" >
                                        <c:url value="/order-history/order/${order.code}" var="orderDetailUrl" />
                                        <tr class="orderHistoryRow even gotoOrderHstryDetail">
                                            <td class="column1 labelText">
                                                <a href="${orderDetailUrl}" class="ordernumber">
                                                    <c:choose>
                                                        <c:when test="${not empty order.sapOrderNumber}">
                                                            ${order.sapOrderNumber}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${order.code}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </a>
                                                <c:if test="${order.surgeonUpdatInd}">
                                                    |
                                                    <a href="javascript:void(0);" id="updateSurgeon" class="updateSurgeon" orderNumber="${order.code}" surgeonName="${order.surgeonName}">
                                                        <spring:message code="orderHistoryPage.updateSurgeon" />
                                                    </a>
                                                </c:if>
                                            </td>
                                            <td class="column2">
                                                <c:choose>
                                                    <c:when test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
                                                        <a href="#" id="updatePoNumber" class="updatePoNumber" orderNumber="${order.code}">
                                                            <spring:message code="orderHistoryPage.updatedPO" />
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${order.purchaseOrderNumber}
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="column3">
                                                <c:choose>
                                                    <c:when test="${not empty order.ordertype}">
                                          <span class="txtFont">
                                             <spring:message code="cart.common.orderType.${order.ordertype}" />
                                          </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="txtFont">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="column4">
                                                <c:choose>
                                                    <c:when test="${not empty order.contractNumber}">
                                                        <span class="txtFont">${order.contractNumber}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="txtFont">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="column5">
                                                <c:choose>
                                                    <c:when test="${not empty order.placed}">
                                                        <c:choose>
                                                            <c:when test="${'en' eq sessionLanguage}">
                                                                <fmt:formatDate pattern="MM/dd/yyyy" value="${order.placed}"
                                                                                var="orderDate" />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <fmt:formatDate pattern="dd/MM/yyyy" value="${order.placed}"
                                                                                var="orderDate" />
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <span class="txtFont">${orderDate}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="txtFont">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="column6">
                                                <c:choose>
                                                    <c:when test="${not empty order.channel}">
                                          <span class="txtFont">
                                             <spring:message code="order.channel.${order.channel}" />
                                          </span>
                                                    </c:when>
                                                    <c:otherwise>
                                          <span class="txtFont">
                                             <spring:message code="order.channel.other" />
                                          </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <c:choose>
                                                <c:when test="${order.status == 'CREATED'}">
                                                    <spring:message code='order.status.message.created'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:when test="${order.status == 'BEING_PROCESSED'}">
                                                    <spring:message code='order.status.message.beingProcessed'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:when test="${order.status == 'CREDIT_HOLD'}">
                                                    <spring:message code='order.status.message.creditHold'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:when test="${order.status == 'CANCELLED'}">
                                                    <spring:message code='order.status.message.cancelled'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:when test="${order.status == 'COMPLETED'}">
                                                    <spring:message code='order.status.message.completed'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:when test="${order.status == 'IN_PICKING'}">
                                                    <spring:message code='order.status.message.beingPicked'
                                                                    var="orderStatusMessage" />
                                                </c:when>
                                                <c:otherwise><c:set var="orderStatusMessage" value=""></c:set></c:otherwise>
                                            </c:choose>
                                            <td class="column7">
                                                <c:choose>
                                                    <c:when test="${not empty order.statusDisplay}">
                                                        <span data-toggle="tooltip" data-placement="left" title="${orderStatusMessage}"> <span class="${order.status}"></span>${order.statusDisplay}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="${order.status}">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="column8">
                                                <c:choose>
                                                    <c:when test="${not empty order.total.formattedValue}">
                                                        <span class="priceList">${order.total.formattedValue}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="priceList">&nbsp;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </div>
                    </table>
                </div>
            </div>
        </div>
        <!-- Table collapse for ipad device-->
        <div id="datatab-tablet-wrapper" class="d-none d-sm-block d-md-block d-lg-none jnj-panel-for-table mainbody-container">
            <table id="datatab-tablet" class="ajaxTableWrapperContent table table-bordered lasorting-table bordernone mobile-table" data-total-results="${searchPageData.pagination.totalNumberOfResults}">
                <thead>
                <tr>
                    <th class="orderno" data-column-name="orderNumber">
                        <spring:message code="orderHistoryPage.orderNumber" />
                    </th>
                    <th class="no-sort">
                        <spring:message code="orderHistoryPage.poNomber" />
                    </th>
                    <th class="no-sort">
                        <spring:message code="orderHistoryPage.orderDate" />
                    </th>
                    <th class="no-sort" style="width: 119px;">
                        <spring:message code="orderHistoryPage.status" />
                    </th>
                    <th class="no-sort">
                        <spring:message code="orderHistoryPage.total" />
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
                        <c:forEach items="${searchPageData.results}" var="order" varStatus="count">
                            <c:url value="/order-history/order/${order.code}" var="orderDetailUrl" />
                            <tr>
                                <td class="vlign-top orderno">
                                    <a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse${count.count}" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
                                        <i class="bi bi-plus-lg" style="-webkit-text-stroke: 1.3px;"></i>
                                        <a href="${orderDetailUrl}" class="ordernumber">
                                            <c:choose>
                                                <c:when test="${not empty order.sapOrderNumber}">
                                                    ${order.sapOrderNumber}
                                                </c:when>
                                                <c:otherwise>
                                                    ${order.code}
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </a>
                                    <div id="collapse${count.count}" class="panel-collapse collapse">
                                        <div class="panel-body">
                                            <div class="sub-details-row">
                                                <p style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.stipTo" />
                                                </p>
                                                <p>
                                                    <c:choose>
                                                        <c:when test="${not empty order.shipToNumber}">
                                                            <span class="txtFont">${order.shipToNumber}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="txtFont">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                            <div class="sub-details-row">
                                                <p style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.orderType" />
                                                </p>
                                                <P>
                                                    <c:choose>
                                                        <c:when test="${not empty order.ordertype}">
                                                <span class="txtFont">
                                                   <spring:message code="cart.common.orderType.${order.ordertype}" />
                                                </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="txtFont">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </P>
                                            </div>
                                            <div class="sub-details-row">
                                                <P style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.channel" />
                                                </P>
                                                <P>
                                                    <c:choose>
                                                        <c:when test="${not empty order.channel}">
                                                <span class="txtFont">
                                                   <spring:message code="order.channel.${order.channel}" />
                                                </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                <span class="txtFont">
                                                   <spring:message code="order.channel.other" />
                                                </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </P>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td class="vlign-top text-center">
                                    <c:choose>
                                        <c:when test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
                                            <a href="#" id="updatePoNumber" class="updatePoNumber" orderNumber="${order.code}">
                                                <spring:message code="orderHistoryPage.updatedPO" />
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            ${order.purchaseOrderNumber}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="vlign-top">
                                    <c:choose>
                                        <c:when test="${not empty order.placed}">
                                            <fmt:formatDate pattern="MM/dd/yyyy" value="${order.placed}" var="orderDate" />
                                            <span class="txtFont">${orderDate}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="txtFont">&nbsp;</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="vlign-top">
                                    <c:choose>
                                        <c:when test="${not empty order.statusDisplay}">
                                            <span class="pendingStatus mr-2"></span>${order.statusDisplay}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="pendingStatus mr-2">&nbsp;</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="vlign-top">
                                    <div class="sub-details-row">
                                        <P>
                                            <c:choose>
                                                <c:when test="${not empty order.total.formattedValue}">
                                                    <span class="priceList">${order.total.formattedValue}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="priceList">&nbsp;</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </P>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>
                </tbody>
            </table>
        </div>
        <!--Accordian for ipad Ends here -->
        <!-- Table collapse for mobile device -->
        <div id="datatab-mobile-wrapper" class="d-block d-sm-none jnj-panel-for-table mainbody-container">
            <table id="datatab-mobile" class="ajaxTableWrapperContent table table-bordered lasorting-table bordernone mobile-table" data-total-results="${searchPageData.pagination.totalNumberOfResults}">
                <thead>
                <tr>
                    <th class="orderno" data-column-name="orderNumber">
                        <spring:message code="orderHistoryPage.orderNumber" />
                    </th>
                    <th class="no-sort text-center">
                        <spring:message code="orderHistoryPage.poNomber" />
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
                        <c:forEach items="${searchPageData.results}" var="order" varStatus="count">
                            <c:url value="/order-history/order/${order.code}"
                                   var="orderDetailUrl" />
                            <tr>
                                <td class="vlign-top orderno">
                                    <a data-bs-toggle="collapse" data-parent="#accordion" href="#collapse${count.count}" class="toggle-link panel-collapsed mobileacctoggle skyBlue">
                                      <i class="bi bi-plus-lg" style="-webkit-text-stroke: 1.6px;"></i>
                                        <a href="${orderDetailUrl}" class="ordernumber">
                                            <c:choose>
                                                <c:when test="${not empty order.sapOrderNumber}">
                                                    ${order.sapOrderNumber}
                                                </c:when>
                                                <c:otherwise>
                                                    ${order.code}
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </a>
                                    <div id="collapse${count.count}" class="panel-collapse collapse pl-2 pt-3"">
                                        <div class="panel-body">
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.orderDate" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.placed}">
                                                            <fmt:formatDate pattern="MM/dd/yyyy" value="${order.placed}" var="orderDate" />
                                                            <span class="txtFont">${orderDate}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="txtFont">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message
                                                            code="orderHistoryPage.status" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.statusDisplay}">
                                                            <span class="pendingStatus"></span>${order.statusDisplay}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="pendingStatus">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.stipTo" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.shipToNumber}">
                                                            <span class="txtFont">${order.shipToNumber}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="txtFont">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.orderType" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.ordertype}">
                                                <span class="txtFont">
                                                   <spring:message code="cart.common.orderType.${order.ordertype}" />
                                                </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="txtFont">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.channel" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.channel}">
                                                <span class="txtFont">
                                                   <spring:message code="order.channel.${order.channel}" />
                                                </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                <span class="txtFont">
                                                   <spring:message code="order.channel.other" />
                                                </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="sub-details-row">
                                                <div style="font-family: jnjlabelfont; font-size: 10px">
                                                    <spring:message code="orderHistoryPage.total" />
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${not empty order.total.formattedValue}">
                                                            <span class="priceList">${order.total.formattedValue}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="priceList">&nbsp;</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td class="vlign-top text-center">
                                    <c:choose>
                                        <c:when test="${empty order.purchaseOrderNumber && order.poNumberUpdateInd}">
                                            <a href="#" id="updatePoNumber" class="updatePoNumber" orderNumber="${order.code}">
                                                <spring:message code="orderHistoryPage.updatedPO" />
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            ${order.purchaseOrderNumber}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                </c:choose>
                </tbody>
            </table>
        </div>
        <!-- Accordian for mobile ends here -->
    </div>
    <div id="order-history-popup-holder"></div>
</templateLa:page>