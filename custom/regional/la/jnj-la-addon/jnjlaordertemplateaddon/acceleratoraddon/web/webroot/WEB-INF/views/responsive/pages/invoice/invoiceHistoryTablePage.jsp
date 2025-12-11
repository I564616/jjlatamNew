<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:choose>
    <c:when test="${param.table == 'mobile'}">
        <c:set var="tableId" value="datatab-mobile"/>
        <c:set var="wrapperId" value="datatab-mobile-wrapper"/>
        <c:set var="tableClass" value="bordernone mobile-table"/>
    </c:when>
    <c:when test="${param.table == 'tablet'}">
        <c:set var="tableId" value="datatab-tablet"/>
        <c:set var="wrapperId" value="datatab-tablet-wrapper"/>
        <c:set var="tableClass" value="bordernone mobile-table"/>
    </c:when>
    <c:otherwise>
        <c:set var="tableId" value="datatab-desktop"/>
        <c:set var="wrapperId" value="datatab-desktop-wrapper"/>
        <c:set var="tableClass" value=""/>
    </c:otherwise>
</c:choose>

<div id="${wrapperId}">
    <div class="ajaxTableWrapperContent no-scroll-x">
        <table id="${tableId}" class="table table-bordered table-striped lasorting-table ${tableClass}"
            data-hidden-message-selector=".notLoadedOrderMessage"
            data-hidden-message-show="${hiddenMessageShow}"
            data-total-results="${searchPageData.pagination.totalNumberOfResults}">
            <thead>
            <tr>
                <th data-column-name="invoiceNumber">
                    <spring:message code="invoiceHistoryPage.invoiceNumber"/>
                </th>
                <th class="no-sort">
                    <spring:message code="invoiceHistoryPage.invoiceOrderNumber"/>
                </th>
                <th data-column-name="creationDate" class="text-center-force">
                    <spring:message code="invoiceHistoryPage.invoiceDate"/>
                </th>
                <c:if test="${'mobile' != param.table}">
                    <th data-column-name="invoiceTotal" class="text-center-force">
                        <spring:message code="invoiceHistoryPage.invoiceTotal"/>
                    </th>
                </c:if>
                <c:if test="${hasDownloadAvailability}">
                    <th class="no-sort text-center-force">
                        <spring:message code="invoiceHistoryPage.download"/>
                    </th>
                </c:if>
            </tr>
            </thead>
            <c:choose>
                <c:when test="${not empty searchPageData && fn:length(searchPageData.results) > 0}">
                    <c:forEach items="${searchPageData.results}" var="invoiceOrder">
                        <tr class="orderHistoryRow even gotoOrderHstryDetail">
                            <td class="column1">
                                <c:url value="/order-history/order/invoiceDetail/${invoiceOrder.orderNumber}" var="invoiceDetailUrl"/>
                                <c:choose>
                                    <c:when test="${invoiceOrder.orderLoaded}">
                                        <a href="${invoiceDetailUrl}" class="ordernumber">
                                            ${invoiceOrder.invoiceNumber}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        ${invoiceOrder.invoiceNumber}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="column2">
                                ${invoiceOrder.sapOrderNumber}
                            </td>
                            <td class="column3 text-center-force">
                                <fmt:formatDate pattern="${sessionlanguagePattern}" value="${invoiceOrder.creationDate}" />
                            </td>
                            <c:if test="${'mobile' != param.table}">
                                <td class="column4 text-right">
                                    <c:choose>
                                        <c:when test="${not empty invoiceOrder.netValue.formattedValue}">
                                            <span class="priceList">${invoiceOrder.netValue.formattedValue}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="priceList">&nbsp;</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <c:if test="${hasDownloadAvailability}">
                                <td class="column5">
                                    <div class="text-center" style="min-width: 100px;">

                                        <c:set var="nonBrXmlInvoiceLinkFlag" value="${!brXmlInvoiceLinkFlag and xmlInvoiceLinkFlag}"/>
                                        <c:set var="displayPipe" value="${(brXmlInvoiceLinkFlag or nonBrXmlInvoiceLinkFlag) and pfdInvoiceLinkFlag}"/>

                                        <!-- Download Icons Logic -->
                                        <c:if test="${pfdInvoiceLinkFlag or nonBrXmlInvoiceLinkFlag}">
                                            <form id="invoiceDetail" action='<c:url value="/invoice-history/invoiceDocFile"/>' style="display: inline-block">
                                                <span id="invoiceDownload" class="pull-left" style="${displayPipe ? 'border-right: 1px solid;' : '' }">
                                                    <c:if test="${pfdInvoiceLinkFlag}">
                                                        <input type="submit" class="tertiarybtn pdf pdfdownloadlinks invoicePDFButton" value="PDF" name="downloadType" id="invoicePdf"/>
                                                    </c:if>
                                                </span>
                                                <span id="invoiceDownload" class="pull-right">
                                                    <c:if test="${nonBrXmlInvoiceLinkFlag}">
                                                        <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XML" name="downloadType" id="invoiceExcel"/>
                                                    </c:if>
                                                </span>
                                                <input type="hidden" id="invoiceNumber" value="${invoiceOrder.invDocNo}" name="invoiceNumber">
                                            </form>
                                        </c:if>

                                        <!-- BR NFe Download Logic -->
                                        <c:if test="${brXmlInvoiceLinkFlag}">
                                            <form id="invoiceDetail" action='<c:url value="/invoice-history/nfeFile"/>' style="display: inline-block">
                                                <span id="invoiceDownload" class="pull-right">
                                                    <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XML" id="invoiceExcel"/>
                                                </span>
                                                <input type="hidden" id="invoiceNumber" value="${invoiceOrder.invDocNo}" name="invoiceNumber"/>
                                            </form>
                                        </c:if>


                                    </div>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </c:when>
            </c:choose>
        </table>
    </div>
</div>