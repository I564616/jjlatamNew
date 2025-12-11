<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="orderData" required="true" type="com.jnj.facades.data.JnjGTOrderData" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/standard"%>
<%@ taglib prefix="consAddress" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/house"%>
<%@ taglib prefix="deliveredCart"	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
<%@ taglib prefix="laFormat" tagdir="/WEB-INF/tags/addons/jnjlaloginaddon/responsive/shared/format"%>
<c:url value="/order-history" var="orderHistoryURL"/>

<c:url value="/order-history/refineOrderDetail" var="refineUrl"></c:url>
<c:set var="numberOfEntries" value="${fn:length(orderData.entries)}"></c:set>

<div class="">
    <form:form class="searchSortForm" id="searchSortForm1" name="searchSortForm" method="get" action="${refineUrl}" modelAttribute="searchSortForm">
        <input style="display:none;" type="hidden" id= "orderCode" name="orderCode" value="${orderData.code}" />
            <div class="orderdetailscontainer col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ordertypedetails">
                  <div class="row">
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
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 downloadLinks">
                        <p>
                            <spring:message code="product.search.download"/>
                            <span>
                                <input type="submit" class="tertiarybtn pdfdownloadlinks" value="<spring:message code="reports.excel.label" />" name="downloadType" />
											
                                <span class="pipesymbol">|</span>&nbsp; <input type="submit" class="tertiarybtn marginRight pdfdownloadlinks" value="<spring:message code="reports.pdf.label" />" name="downloadType"  />
											
                            </span>
                        </p>
                    </div>
                    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
                      <div class="row">
                        <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 padding30px" >
                            <span class="subhead boldtext text-uppercase">
                                <spring:message code="orderDetailPage.orderData.orderDate"/>
                            </span>
                            <p><laFormat:formatDate dateToFormat="${orderData.created}"/><p/>
                        </div>
                        <c:if test="${not empty orderData.statusDisplay}">
                            <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 padding30px" >
                                <span class="subhead boldtext text-uppercase">
                                    <spring:message code="orderDetailPage.orderData.status"/>
                                </span>
                                <p>${orderData.statusDisplay}<p/>
                            </div>
                        </c:if>
                        <c:if test="${not empty orderData.contractNumber}">
                            <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 padding30px" >
                                <span class="subhead boldtext text-uppercase">
                                    <spring:message code="orderDetailPage.orderData.contractNumber"/>
                                </span>
                                <p>${orderData.contractNumber}<p/>
                            </div>
                        </c:if>
                      </div>
                    </div>
                    <div class="col-lg-7 col-md-7 col-sm-7 col-xs-7 p-0">
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding30px" >
                            <c:if test="${not empty orderData.deliveryAddress}">
                                <span class="subhead boldtext text-uppercase">
                                    <spring:message code="orderDetailPage.orderData.shipToAddress"/>
                                </span>
                                <p><address:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" /><p/>
                            </c:if>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 marginbtm20" >
                            <c:url value="/order-history/order/invoiceDetail/${orderData.code}" var="invoiceDetailUrl" />
                            <c:if test="${hasInvoice == true}">
                                <a href="${invoiceDetailUrl}"><spring:message code="orderDetailPage.orderData.viewInvoiceDetails"/></a>
                            </c:if>
                        </div>
                    </div>
                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 padding30px" >
                        <c:if test="${not empty orderData.reasonCode}">
                            <span class="subhead boldtext text-uppercase">
                                <spring:message code="orderDetailPage.orderData.reasonCode"/>
                            </span>
                            <p>${orderData.reasonCode}</p>
                        </c:if>
                        <c:if test="${not empty orderData.dropShipAccount}">
                            <span class="subhead boldtext text-uppercase">
                                <spring:message code="orderDetailPage.orderData.dropShipAccount"/>
                            </span>
                            <p>${orderData.dropShipAccount}</p>
                        </c:if>
                    </div>
                    </div>
                </div>
            </div>
    </form:form>
</div>