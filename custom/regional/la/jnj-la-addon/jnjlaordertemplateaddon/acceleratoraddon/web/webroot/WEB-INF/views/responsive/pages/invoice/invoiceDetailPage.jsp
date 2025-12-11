<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/standard"%>
<%@ taglib prefix="consAddress"	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/house"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>
<%@ taglib prefix="lacommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="templateLa" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/template"%>

<templateLa:page pageTitle="${pageTitle}">
    <div class="row">
        <div class="col-xs-12 col-md-12 col-sm-12 col-lg-12 headerLinks">
            <breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12 headingTxt content">
            <spring:message code="invoiceDetailPage.heading" />
        </div>
    </div>
    <div class="row">
            <%-- Invoice Download Error Message --%>
        <c:if test="${not empty invoiceErrorMessage}">
            <div class="rejected">
               <span class="smallFont">
                   <lacommon:genericMessage messageCode="${invoiceErrorMessage}" icon="ban-circle" panelClass="danger"/>
               </span>
            </div>
        </c:if>
    </div>
    <div style="background-color:white">
    <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12">
        <div class="row"> 
        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 padding30px" >
            <span class="subhead boldtext text-uppercase">
                <spring:message code="orderDetailPage.orderData.orderNumber"/>
            </span>
            <p>${orderData.sapOrderNumber}<p/>
        </div>
        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 padding30px" >
            <span class="subhead boldtext text-uppercase">
                <spring:message code="orderDetailPage.orderData.purchaseOrder"/>
            </span>
            <c:if test="${not empty orderData.purchaseOrderNumber}">
                <p>${orderData.purchaseOrderNumber}</p>
            </c:if>
            <c:if test="${empty orderData.purchaseOrderNumber}">
                <p>${orderData.orderNumber}</p>
            </c:if>
        </div>
        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 padding30px" >
            <span class="subhead boldtext text-uppercase">
                <spring:message code="orderDetailPage.orderData.accountNumber"/>
            </span>
            <p>
                <c:if test="${not empty orderData.soldToAccount}">
                    ${orderData.soldToAccount}
                </c:if>
            </p>
        </div>
        </div>
    </div>
    </div>
    <div style="background-color:white">
    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
        <div class="row">
        <div class="col-lg-2 col-md-3 col-sm-3 col-xs-12 padding30px" >
            <span class="subhead boldtext text-uppercase">
                <spring:message code="orderDetailPage.orderData.orderDate"/>
            </span>
            <p><laFormat:formatDate dateToFormat="${orderData.created}"/><p/>
        </div>
        <c:if test="${not empty orderData.statusDisplay}">
            <div class="col-lg-2 col-md-3 col-sm-3 col-xs-12 padding30px" >
                <span class="subhead boldtext text-uppercase">
                    <spring:message code="orderDetailPage.orderData.status"/>
                </span>
                <p>${orderData.statusDisplay}<p/>
            </div>
        </c:if>
    </div>
    </div>
    </div>
    <div style="background-color:white">
    <div class="col-lg-7 col-md-7 col-sm-7 col-xs-7 ">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding30px" >
            <c:if test="${not empty orderData.deliveryAddress}">
                   <span class="subhead boldtext text-uppercase">
                       <spring:message code="orderDetailPage.orderData.shipToAddress"/>
                   </span>
                <p><address:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" /><p/>
            </c:if>
        </div>
    </div>
    </div>
    <div id="invoiceDetailsPage" class="row jnj-panel mainbody-container">
        <c:if test="${isMddSite}">
            <c:url value="${orderData.ghxUrl}" var="ghxUrl"></c:url>
            <div class="info marTop20">
                <p>
                    <spring:message code="invoiceDetailPage.ghxENote" />
                    &nbsp;<a href="${ghxUrl}"
                             onclick="window.open(this.href);return false;"><spring:message
                        code="invoiceDetailPage.ghxEinvoicing" /></a>
                    <spring:message code="invoiceDetailPage.ghxENotePortal" />
                </p>
            </div>
        </c:if>

        <div class="col-lg-12 col-md-12">
            <div class="table-padding">
                <c:choose>
                    <c:when test="${not empty orderInvoices }">
                        <c:forEach items="${orderInvoices}" var="invoiceData"
                                   varStatus="count">
                            <div class="hidden-xs">
                                <div class="invoice-accordian">
                                    <div class="invoice-accordian-header row">
                                        <div class="col-lg-2 col-md-3 col-sm-4">
                                            <a data-bs-toggle="collapse" data-bs-parent="#accordion"
                                               href="#collapse${count.count}"
                                               class="ref_no toggle-link panel-collapsed">
                                               <i class="bi bi-plus"></i>
                                                <spring:message code="invoiceDetailPage.invoice" /> &nbsp;${count.count}</a>
                                        </div>
                                        <div class="col-lg-2 col-md-3 col-sm-4">
                                            <div class="invoice-label-head text-uppercase">
                                                <spring:message code="invoiceDetailPage.invDocNo" />
                                            </div>
                                            <c:choose>
                                                <c:when test="${countryInfo eq 'BR'}">
                                                    <div class="invoice-number-table">${invoiceData.nfNumber}</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="invoice-number-table">${invoiceData.invDocNo}</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-lg-5 col-md-3 col-sm-4"></div>
                                        <c:if test="${invoiceData.carrierEstimateDeliveryDate ne null}">
                                            <div class="visually-hidden col-lg-3 col-md-3 col-sm-4">
                                                <div class="invoice-label-head text-uppercase">
                                                    <spring:message code="invoiceDetailPage.estimated.delivery.date" />
                                                </div>
                                                <c:choose>
                                                    <c:when test="${'en' eq sessionLanguage}">
                                                        <span class="txtFont">&nbsp;<fmt:formatDate value="${invoiceData.carrierEstimateDeliveryDate}" pattern="MM/dd/yyyy"/></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="txtFont">&nbsp;<fmt:formatDate value="${invoiceData.carrierEstimateDeliveryDate}" pattern="dd/MM/yyyy"/></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>

                                        <c:if test="${invoiceData.carrierConfirmedDeliveryDate ne null}">
                                            <div class="visually-hidden col-lg-2 col-md-3 col-sm-4">
                                                <div class="invoice-label-head text-uppercase">
                                                    <spring:message code="invoiceDetailPage.actual.delivery.date" />
                                                </div>
                                                <c:choose>
                                                    <c:when test="${'en' eq sessionLanguage}">
                                                        <span class="txtFont">&nbsp;<fmt:formatDate value="${invoiceData.carrierConfirmedDeliveryDate}" pattern="MM/dd/yyyy"/></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="txtFont">&nbsp;<fmt:formatDate value="${invoiceData.carrierConfirmedDeliveryDate}" pattern="dd/MM/yyyy"/></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                        <c:if test="${pfdInvoiceLinkFlag or xmlInvoiceLinkFlag}">
                                            <form class="col-lg-2 col-md-3 col-sm-4" id="invoiceDetail" action='<c:url value="/order-history/invoiceDocFile" />'>
                                                <input type="hidden" id="orderCode" value="${orderData.code}" name="orderCode">
                                                <span id="invoicedownloadlinks" class="pull-right">
                                                    <span class="link-txt boldtext">
                                                        <spring:message code="product.search.download" />
                                                    </span>&nbsp;&nbsp;
                                                    <c:if test="${pfdInvoiceLinkFlag}">
                                                        <input type="submit" class="tertiarybtn pdf pdfdownloadlinks invoicePDFButton" value="PDF" name="downloadType" id="invoicePdf" />
                                                    </c:if>
                                                    <c:if test="${pfdInvoiceLinkFlag and (xmlInvoiceLinkFlag or brXmlInvoiceLinkFlag)}">
                                                        <span>|</span>
                                                    </c:if>
                                                    <c:if test="${!brXmlInvoiceLinkFlag and xmlInvoiceLinkFlag}">
                                                        <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XML" name="downloadType" id="invoiceExcel" />
                                                    </c:if>
                                                </span>
                                                <input type="hidden" id="invoiceNumber" value="${invoiceData.invDocNo}" name="invoiceNumber">
                                            </form>
                                        </c:if>
                                        <c:if test="${brXmlInvoiceLinkFlag}">
                                            <form class="col-lg-1 col-md-3 col-sm-4" id="invoiceDetail" action='<c:url value="/order-history/nfeFile" />'>
                                                <input type="hidden" id="orderCode" value="${orderData.code}" name="orderCode">
                                                <span id="invoicedownloadlinks" class="pull-right">
                                                    <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XML" id="invoiceExcel" />
                                                </span>
                                                <input type="hidden" id="invoiceNumber" value="${invoiceData.invDocNo}" name="invoiceNumber">
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="invoice-accordian-body panel-collapse collapse table-responsive" id="collapse${count.count}">
                                    <table id="datatab-desktop" class="hidden-xs table table-bordered table-striped invoice-desktop-table">
                                        <thead>
                                        <tr>
                                            <th class="text-uppercase text-left">
                                                <spring:message code="invoiceDetailPage.item" />
                                            </th>
                                            <th class="text-uppercase text-left">
                                                <spring:message code="invoiceDetailPage.catalogCode" />
                                            </th>
                                            <th class="no-sort text-uppercase text-left">
                                                <spring:message code="orderDetailPage.orderData.product" />
                                            </th>
                                            <th class="no-sort text-uppercase text-center">
                                                <spring:message code="invoiceDetailPage.Quantity" />
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${invoiceData.entries}" var="entry" varStatus="count">
                                            <tr>
                                                <td class="text-left">
                                                    <c:choose>
                                                        <c:when test="${placeOrderGroupFlag eq true}" >
                                                            <c:url value="${entry.lamaterial.url}" var="productUrl" />
                                                            <a href="${productUrl}">${entry.lamaterial.code}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${entry.lamaterial.code}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-left">
                                                    ${entry.lamaterial.catalogId}
                                                </td>
                                                <td class="text-left">
                                                    <div class="column2">
                                                        <c:url value="${entry.lamaterial.url}" var="productUrl" />
                                                        <div class="orderProdDesc left">
                                                            <h4 title="${entry.lamaterial.name}">

                                                                <c:choose>
                                                                    <c:when test="${empty entry.lamaterial.url}">
                                                                        ${entry.lamaterial.name}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <p href="${productUrl}" style=" font-size: 18px; font-weight: normal;">
                                                                        ${entry.lamaterial.name}</p>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </h4>
                                                            <p>
                                                                <spring:message code="product.uom.invoice" />
                                                                :<span class="strong">${entry.lamaterial.deliveryUnitCode}</span>
                                                            </p>
                                                            <c:if test="${isMddSite}">
                                                                <c:if test="${not empty entry.contractNum}">
                                                                    <p>
																		<span class="smallFont">
																		    <spring:message code="orderDetailPage.orderData.contract" />
																		</span>
                                                                        ${entry.contractNum}
                                                                    </p>
                                                                </c:if>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </td>
                                                <c:if test="${!isMddSite}">
                                                    <td class="text-center text-nowrap">
                                                        <p class="inputBox">
                                                            <input type="text" disabled="disabled"
                                                                   class=" qtyUpdateTextBox form-control txtWidth"
                                                                   value="${entry.qty}" />
                                                            <div style="margin-left:85px;margin-top:-50px">${entry.lamaterial.salesUnit}</div>
                                                        </p>
                                                        <div>
                                                            <spring:message code="product.multiple.invoice" />
                                                            &nbsp;${entry.lamaterial.numeratorDUOM}
                                                        </div>
                                                        <p>
                                                            <c:if test="${not empty entry.lamaterial.deliveryUnit}">
																	<span class="descSmall block">
																		${entry.lamaterial.deliveryUnit}
																		(${entry.lamaterial.numerator}
																		${entry.lamaterial.baseUnitCode}) </span>
                                                            </c:if>
                                                        </p>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div>
                            <span><spring:message code="orderDetailPage.no.results" /></span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- Table collapse for mobile device-->
            <div class="Subcontainer visually-hidden">
                <div class="invoice-accordian">
                    <c:choose>
                        <c:when test="${not empty orderInvoices }">
                            <c:forEach items="${orderInvoices}" var="invoiceData" varStatus="count">
                                <div class="invoice-accordian-header row">
                                    <div class="col-xs-6">
                                        <a data-bs-toggle="collapse" data-bs-parent="#accordion"
                                           href="#mobile-collapse${count.count}"
                                           class="ref_no toggle-link panel-collapsed">
                                           <i class="bi bi-plus"></i>
                                           <spring:message code="invoiceDetailPage.invoice" />${count.count}
                                        </a>
                                    </div>
                                    <div class="col-xs-6">
                                        <div class="invoice-label-head text-uppercase">
                                            <spring:message code="invoiceDetailPage.invDocNo" />
                                        </div>
                                        <div class="invoice-number-table">${invoiceData.invDocNo}</div>
                                    </div>
                                    <form id="invoiceDetail" action='<c:url value="/order-history/orderInvoiceDetailDownload" />'>
                                        <div class="col-xs-12 invoice-download-link">


                                            <c:if test="${brXmlInvoiceLinkFlag}">
                                                <form id="invoiceDetail" action='<c:url value="/order-history/nfeFile" />'>
                                                    <input type="hidden" id="orderCode" value="${orderData.code}" name="orderCode">
                                                    <span id="invoicedownloadlinks" class="pull-right">
                                                        <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XML" id="invoiceExcel" />
                                                    </span>
                                                    <input type="hidden" id="invoiceNumber" value="${invoiceData.invDocNo}" name="invoiceNumber">
                                                </form>
                                            </c:if>
                                            <c:if test="${pfdInvoiceLinkFlag or xmlInvoiceLinkFlag}">
                                                <form id="invoiceDetail" action='<c:url value="/order-history/invoiceDocFile" />'>
                                                    <input type="hidden" id="orderCode" value="${orderData.code}" name="orderCode">
                                                    <span id="invoicedownloadlinks" class="pull-right">
                                                        <span class="link-txt boldtext">
                                                            <spring:message code="product.search.download" />
                                                        </span>&nbsp;&nbsp;
                                                        <c:if test="${pfdInvoiceLinkFlag}">
                                                            <input type="submit" class="tertiarybtn pdf pdfdownloadlinks invoicePDFButton" value="PDF" name="downloadType" id="invoicePdf" />
                                                        </c:if>
                                                        <c:if test="${pfdInvoiceLinkFlag and (xmlInvoiceLinkFlag or brXmlInvoiceLinkFlag)}">
                                                            <span>|</span>
                                                        </c:if>
                                                        <c:if test="${!brXmlInvoiceLinkFlag and xmlInvoiceLinkFlag}">
                                                            <input type="submit" class="tertiarybtn excel pdfdownloadlinks" value="XLS" name="downloadType" id="invoiceExcel" />
                                                        </c:if>
                                                    </span>
                                                    <input type="hidden" id="invoiceNumber" value="${invoiceData.invDocNo}" name="invoiceNumber">
                                                </form>
                                            </c:if>
                                        </div>
                                    </form>
                                </div>
                                <div class="invoice-accordian-body panel-collapse collapse" id="mobile-collapse${count.count}">
                                    <table id="datatab-mobile" class="table table-bordered table-striped invoice-desktop-table bordernone">
                                        <thead>
                                            <tr>
                                                <th class="text-left text-uppercase">
                                                    <spring:message code="invoiceDetailPage.item" />
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${invoiceData.entries}" var="entry" varStatus="count">
                                                <tr>
                                                    <td class="panel-title text-left">
                                                        <a data-toggle="collapse" data-parent="#accordion"
                                                            href="#mob-collapse${count.count}"
                                                            class="ref_no toggle-link panel-collapsed">
                                                            <span class="glyphicon glyphicon-plus"></span>
                                                                ${entry.lamaterial.code}
                                                        </a>
                                                        <div id="mob-collapse${count.count}" class="panel-collapse collapse">
                                                            <div class="panel-body details">
                                                                <div class="invoice-bottom-margin">
                                                                    <div class="text-uppercase">
                                                                        <spring:message code="orderDetailPage.orderData.product" />
                                                                    </div>
                                                                    <div class="invoice-content">
                                                                        <c:url value="${entry.lamaterial.url}" var="productUrl" />
                                                                        <div class="orderProdDesc left">
                                                                            <h4 title="${entry.lamaterial.name}">
                                                                                <c:choose>
                                                                                    <c:when test="${empty entry.lamaterial.url}">
                                                                                        ${entry.lamaterial.name}
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <a href="${productUrl}">${entry.lamaterial.name}</a>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </h4>
                                                                        </div>
                                                                    </div>
                                                                <div>
																    <span class="invoice-label-head">
																        <spring:message code="orderDetailPage.orderData.jnjID" />
																    </span>
                                                                    <span class="invoice-content">
                                                                        ${entry.lamaterial.code}
                                                                    </span>
                                                                </div>
                                                                <div>
																    <span class="invoice-label-head">
																        <spring:message code="invoiceDetailPage.catalogCode" />#
																    </span>
                                                                    <span class="invoice-content">
                                                                        ${entry.lamaterial.catalogId}
                                                                    </span>
                                                                </div>
                                                                <div>
															        <span class="invoice-label-head">
															            <spring:message code="orderDetailPage.orderData.gtin" />
															        </span>
															        <span class="invoice-content">
															            ${entry.lamaterial.ean}
															        </span>
                                                                </div>
                                                                <c:if test="${isMddSite}">
                                                                    <c:if test="${not empty entry.contractNum}">
                                                                        <div>
																	        <span class="invoice-label-head">
																	            <spring:message code="orderDetailPage.orderData.contract" />
																	        </span>
                                                                            <span class="invoice-content">
                                                                                ${entry.contractNum}
                                                                            </span>
                                                                        </div>
                                                                    </c:if>
                                                                </c:if>
                                                            </div>
                                                            <c:if test="${!isMddSite}">
                                                                <div class="text-uppercase">
                                                                    <spring:message code="invoiceDetailPage.Quantity" />
                                                                </div>
                                                                <div>
                                                                    <p class="inputBox">${entry.qty}</p>
                                                                    <p>
                                                                        <c:if test="${not empty entry.lamaterial.deliveryUnit}">
																	        <span class="descSmall block">
																		        ${entry.lamaterial.deliveryUnit}
																			    (${entry.lamaterial.numerator} &nbsp;
																			    ${entry.lamaterial.baseUnitCode})
																	        </span>
                                                                        </c:if>
                                                                    </p>
                                                                </div>
                                                            </c:if>
                                                            <div class="text-uppercase">
                                                                <spring:message code="orderDetailPage.orderData.itemPrice" />
                                                            </div>
                                                            <div>
                                                                <c:choose>
                                                                    <c:when test="${not empty entry.lamaterial.displayPrice}">
                                                                        <span class="txtFont">${entry.lamaterial.displayPrice}</span>
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
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div>
                            <span><spring:message code="orderDetailPage.no.results" /></span>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</templateLa:page>