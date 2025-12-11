<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
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
<%@ taglib prefix="consAddress" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/house"%>
<template:page pageTitle="${pageTitle}">
  <!-- Start - Body Content -->
  <div id="invoice-details-page">
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
    <div class="row jnj-panel mainbody-container">
      <div class="col-lg-12 col-md-12">
        <div class="row jnj-panel-header">
          <div class="col-lg-3 col-md-8 col-sm-5 col-xs-11">
            <div class="invoice-label-head">
              <spring:message code="orderDetailPage.orderData.orderNumber" />
            </div>
            <div id="invoice-num">${orderData.orderNumber}</div>
          </div>
          <div class="col-lg-3 col-md-8 col-sm-4 col-xs-11">
            <div class="invoice-label-head">
              <spring:message code="orderDetailPage.orderData.orderType" />
            </div>
            <div class="invoice-head-content">
              <spring:message code="cart.common.orderType.${orderData.orderType}" />
            </div>
          </div>
          <div class="col-lg-2 col-md-8 col-sm-3 col-xs-11">
            <div class="invoice-label-head">
              <spring:message code="orderDetailPage.orderData.accountNumber" />
            </div>
            <div class="invoice-head-content">${orderData.accountNumber}</div>
          </div>
          <div class="col-lg-2 col-md-8 col-sm-6 col-xs-11"></div>
        </div>
        <div class="row jnj-panel-body">
          <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 invoice-leftTotop-content">
            <div class="invoice-content-holder invoice-left">
              <c:if test="${not empty orderData.deliveryAddress}">
                <div class="invoice-label-head text-uppercase">
                  <spring:message code="orderDetailPage.orderData.shipToAddress" />
                </div>
                <div class="invoice-content">
                  <c:choose>
                    <c:when test="${isMddSite}">
                      <address:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" />
                    </c:when>
                    <c:otherwise>
                      <consAddress:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" />
                    </c:otherwise>
                  </c:choose>
                </div>
              </c:if>
            </div>
            <div class="invoice-content-holder invoice-left">
              <c:if test="${not empty orderData.billingAddress.formattedAddress}">
                <div class="text-uppercase invoice-cutom-name-label">
                  <spring:message code="orderDetailPage.orderData.billingNameAndAddress" />
                </div>
                <div class="invoice-content">${orderData.billingAddress.formattedAddress}</div>
              </c:if>
            </div>
            <div class="invoice-content-holder invoice-left">
              <div class="invoice-label-head text-uppercase">
                <spring:message code="orderDetailPage.orderData.status" />
              </div>
              <div class="invoice-content">${orderData.statusDisplay}</div>
            </div>
          </div>
          <div class="col-lg-1 col-md-1 col-sm-1 col-xs-12" id="invoice-separator"></div>
          <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 invoice-rightTobottom-content">
            <div class="row paddingForIpadNA">
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase">
                    <spring:message code="orderDetailPage.orderData.orderDate" />
                  </div>
                  <div class="invoice-content"><!-- Modified by Archana for AAOL-5513 -->
                  <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
                    <fmt:formatDate value="${orderData.created}" pattern="${dateformat}" />
                  </div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase">
                    <spring:message code="orderDetailPage.orderData.purchaseOrder" />
                  </div>
	            <c:choose>											
							<c:when test="${not empty orderData.purchaseOrderNumber}">
								<div class="invoice-content">${orderData.purchaseOrderNumber}</div>										
							</c:when>
							<c:otherwise>
								<div class="invoice-content"><spring:message code="cart.return.entries.notAvailable" /></div>
							</c:otherwise>
				</c:choose>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.carrier" /></div>
                  <c:forEach items="${orderData.carrier}" var="carrier" varStatus="count">
                    <div class="invoice-content">${carrier}</div>
                  </c:forEach>
                   <c:if test="${0 eq orderData.carrier.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                     </c:if> 
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.tracking" /></div>
                  <div class="invoice-content">
                    <c:forEach var="trackingDetails" items="${orderData.shippingTrackingInfo}"> ${trackingUrls.key}
                    <c:forEach var="trackingDetail" items="${trackingDetails.value}">
                        <div class="invoice-content" style="margin-bottom:10px">
                          <a class="word-break" href="${trackingDetail.trackingUrl}">${trackingDetail.trackingId}</a>
                        </div>
                      </c:forEach>
                    </c:forEach> 
                      <c:if test="${0 eq orderData.shippingTrackingInfo.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                     </c:if>                   
                  </div>
                </div>
              </div>
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="invoiceDetailPage.packingList" /></div>
                  <c:choose>
                    <c:when test="${not empty orderInvoices }">
                      <c:forEach items="${orderInvoices}" var="invoiceData" varStatus="count">
                        <c:forEach items="${invoiceData.packingList}" var="pl" varStatus="count">
                          <div class="invoice-content">${invoiceData.invoiceNumber}:${pl}</div>
                        </c:forEach>
                         <c:if test="${0 eq invoiceData.packingList.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                          </c:if>  
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <div class="invoice-content"><spring:message code="orderDetailPage.orderData.notAvailable" /></div>
                    </c:otherwise>
                  </c:choose>
                  <div class="invoice-content">${pl}</div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase">
                    <spring:message code="orderDetailPage.orderData.paymentMethod" />
                  </div>
                  <div class="invoice-content">
                    <spring:message code="invoiceDetailPage.payment.method.text" />
                  </div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.billOfLading" /></div>
                  <c:forEach items="${orderData.billOfLading}" var="bol" varStatus="count">
                    <div class="invoice-content">${bol}</div>
                  </c:forEach>
                   <c:if test="${0 eq orderData.billOfLading.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                     </c:if> 
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="invoiceDetailPage.proof.delivery.link" /></div>
                  <c:forEach var="entry" items="${orderData.proofOfDelDetails}">
                    <div class="invoice-content">${entry.key}:<!-- Modified by Archana for AAOL-5513 -->
                      <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
                      <fmt:formatDate value="${entry.value}" pattern="${dateformat}" />
                    </div>
                  </c:forEach>
                  <c:if test="${0 eq orderData.proofOfDelDetails.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                     </c:if>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="panel-group " style="margin: 0px 0 25px 0">
      <div class="panel panel-success">
        <div class="panel-heading">
          <h4 class="panel-title display-row">
            <span class="glyphicon glyphicon-ok table-cell"></span> <span class="retrieveInfo table-cell"><spring:message code="invoiceDetailPage.ghxENote" /> 
            <a href="#"> <spring:message code="invoiceDetailPage.ghxEinvoicing" /></a> <spring:message code="invoiceDetailPage.ghxENotePortal" />
            </span>
          </h4>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-12 col-md-12">
        <div class="table-padding">
          <div class="hidden-xs">
            <c:choose>
              <c:when test="${not empty orderInvoices }">
                <c:forEach items="${orderInvoices}" var="invoiceData" varStatus="count">
                  <div class="invoice-accordian">
                    <div class="invoice-accordian-header row">
                      <div class="col-lg-3 col-md-3 col-sm-4" style="padding-right: 0px">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="ref_no toggle-link panel-collapsed"><span
                          class="glyphicon glyphicon-plus invoice-accordian-icon"></span><spring:message code="invoiceDetailPage.invoice" /> ${count.count}</a>
                        <div class="invoice-number-table paddingleft15px">${invoiceData.invoiceNumber}</div>
                      </div>
                      <div class="col-lg-2 col-md-2 col-sm-4" style="padding-left: 0px">
                        <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="invoiceDetailPage.billingDate" /></div>
                        <div class="invoice-number-table lineheight32px">
                       <c:choose>
                         <c:when test="${null ne invoiceData.billingdate }"><!-- Modified by Archana for AAOL-5513 -->
                         <c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
                          <fmt:formatDate value="${invoiceData.billingdate}" pattern="${dateformat}" />
                          </c:when>
                         <c:otherwise>Not Available</c:otherwise></c:choose>
                        </div>
                      </div>
                      <div class="col-lg-3 col-md-3 col-sm-3">
                        <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="invoiceDetailPage.carrierName" /></div>
                        <div class="invoice-number-table lineheight32px">${invoiceData.carrier}</div>
                      </div>
                     <c:url value="/order-history/orderInvoiceDetailDownload" var="orderInvoiceDetailDownloadUrl" />
                    		 <form:form id="invoiceDetail" action="${orderInvoiceDetailDownloadUrl}" method="POST">
                       <div class="col-lg-4 col-md-4 col-sm-4 invoice-download-link" style="margin-top: 14px;">
                       	 
	                          <span class="pull-right"> <span class="link-txt boldtext"><spring:message code="product.search.download" /></span> 
	                            <input type="hidden" value="${orderData.code}" name="orderCode"> 
	                            <input type="submit"
	                            class="pdfdownloadlinks" value="XLS" name="downloadType" id="invoiceExcel" /> | 
	                            <input type="submit"
	                            class="pdfdownloadlinks" value="PDF" name="downloadType" id="invoicePdf" /> 
	                            <input type="hidden"
	                            value="${invoiceData.invoiceNumber}" name="invoiceNumber">
	                          </span>
                         
                       </div>
                      </form:form>
                      <div class="col-lg-offset-3 col-lg-2 col-md-offset-3 col-md-2 col-sm-3 col-xs-12 packingList clearBoth">
                        <div >
                          <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="cart.return.entries.packingList" /></div>
                         <c:choose>
                        <c:when test="${not empty  invoiceData.packingList}">
                          <c:forEach items="${invoiceData.packingList}" var="pl" varStatus="count">
                            <div class="invoice-number-table lineheight32px">${pl}</div>
                          </c:forEach></c:when></c:choose>
                          <c:if test="${0 eq invoiceData.packingList.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                          </c:if>                          
                        </div>
                      </div>
                      <div class="col-lg-3 col-md-3 col-sm-3 billofLandIpad">
                        <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="orderDetailPage.orderData.billOfLading" /></div>
                        <c:forEach items="${orderData.billOfLading}" var="bol" varStatus="count">
                          <div class="invoice-number-table lineheight32px">${bol}</div>
                        </c:forEach>
                         <c:if test="${0 eq orderData.billOfLading.size()}">
                           <div class="invoice-number-table lineheight32px"><spring:message code="cart.return.entries.notAvailable" /></div>
                          </c:if> 
                        
                      </div>
                      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
                      <c:if test="${orderData.displayDisputeOption}">
                        <div class="pull-right" style="padding-top: 17px">
                        <c:url value="/order-history/order/invoice/disputeItem?invoiceNum=${invoiceData.invoiceNumber}&orderCode=${orderData.code}&productCode=${entry.product.baseMaterialNumber}&qty=${entry.orderedQty} 
								&contractNum=${entry.contractNum}&totalPrice=${entry.totalPrice.value}" var="disputeItemUrl" />
                          <a class="btn btnclsnormal dispute-button naDisputeItem" id="disputeItemBottom" href="${disputeItemUrl}" orderCode= "${orderCode}" productCode="${entry.product.baseMaterialNumber}" 
										invoiceNum="${invoiceData.invoiceNumber}" totalPrice="${entry.totalPrice.value}" contractNum="${entry.contractNum }"
										qty="${entry.orderedQty}"><spring:message code="invoiceDetailPage.disputeItemsInvoice" /></a>
                          
                        </div>
                       </c:if> 
                      </div>
                    </div>
                    <div class="invoice-accordian-body panel-collapse collapse table-responsive" id="collapse1">
                      <table id="datatab-desktop"
                        class="hidden-xs hidden-sm table table-bordered table-striped invoice-desktop-table na-invoice-desktop">
                        <thead>
                          <tr>
                            <th class="text-uppercase text-left"><spring:message code="invoiceDetailPage.item" /></th>
                            <th class="no-sort text-uppercase text-left"><spring:message code="orderDetailPage.orderData.product" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="invoiceDetailPage.Quantity" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="orderDetailPage.orderData.itemPrice" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="orderDetailPage.orderData.Total" /></th>
                            <th class="no-sort text-uppercase text-left"><spring:message code="orderDetailPage.orderData.Shipping" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="orderDetailPage.orderData.status" /></th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${invoiceData.invoiceEntries}" var="entry" varStatus="count">
                            <tr>
                              <td class="text-left"><a href="#">${count.count}</td>
                              <td class="text-left">
                                <div class="display-row">
                                  <div class="table-cell">
                                    <span class="Tablesubtxt">
                                      <p class="firstline">${entry.product.name}</p>
                                      <p class="secondline">
                                        <spring:message code="orderDetailPage.orderData.jnjID" />
                                        <c:url value="${entry.product.url}" var="productUrl" />
                                        <a href="${productUrl}">${entry.product.code}</a>
                                      </p>
                                      <p class="secondline">
                                        <spring:message code="orderDetailPage.orderData.gtin" />
                                        ${entry.product.gtin}
                                      </p>
                                    </span>
                                  </div>
                                </div>
                              </td>
                              <c:if test="${!isMddSite}">
                                <td class="text-center text-nowrap">
                                  <p class="inputBox">${entry.qty}</p>
                                  <p>
                                    <c:if test="${not empty entry.product.deliveryUnit}">
                                      <span class="descSmall block"> ${entry.product.deliveryUnit} (${entry.product.numerator} &nbsp;
                                        ${entry.product.salesUnit}) </span>
                                    </c:if>
                                  </p>
                                </td>
                              </c:if>
                              <!-- <td class="text-center text-nowrap">
																		3<div>each (1 each)</div>													
																	</td> -->
                              <td class="text-center"><c:choose>
                                  <c:when test="${not empty entry.basePrice}">
                                    <span class="txtFont">${entry.basePrice.formattedValue}</span>
                                  </c:when>
                                  <c:otherwise>
                                    <span class="txtFont">&nbsp;</span>
                                  </c:otherwise>
                                </c:choose></td>
                              <!-- <td class="text-center">$1,452.00</td> -->
                              <td class="text-center"><c:choose>
                                  <c:when test="${not empty entry.totalPrice.formattedValue}">
                                    <span class="txtFont priceList">${entry.totalPrice.formattedValue}</span>
                                  </c:when>
                                  <c:otherwise>
                                    <span class="txtFont">&nbsp;</span>
                                  </c:otherwise>
                                </c:choose></td>
                              <!-- <td class="text-left text-nowrap">FedEx Ground</td>	 -->
                              <td class="text-left text-nowrap"><c:choose>
                                  <c:when test="${not empty entry.shippingMethod}">
                                    <span class="txtFont">${entry.shippingMethod}</span>
                                  </c:when>
                                  <c:otherwise>
                                    <span class="txtFont">&nbsp;</span>
                                  </c:otherwise>
                                </c:choose></td>
                              <td class="text-left">
                                <div class="invoice-status">${entry.status}</div> <c:if
                                  test="${isMddSite && not empty orderData.shippingTrackingInfo }">
                                  <div>
                                    <spring:message code="orderDetailPage.orderData.trackingHash" />
                                  </div>
                                  <c:forEach items="${orderData.shippingTrackingInfo[entry.lineNumber]}" var="data">
                                    <div>
                                      <a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
                                    </div>
                                  </c:forEach>
                                </c:if>
                                <div>
                                  <a href="#"><spring:message code="invoiceDetailPage.proof.delivery.link" /></a>
                                </div>
                              </td>
                            </tr>
                          </c:forEach>
                        </tbody>
                      </table>
                    <table id="datatab-tablet"
                        class="table table-bordered table-striped bordernone mobile-table hidden-xs hidden-md hidden-lg invoice-tablet-table">
                        <thead>
                          <tr>
                            <th class="text-uppercase no-sort text-left"><spring:message code="orderDetailPage.orderData.hash" /></th>
                            <th class="no-sort text-uppercase text-left"><spring:message code="order.product" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="invoiceDetailPage.invoicedQuantity" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="order.itemPrice" /></th>
                            <th class="no-sort text-uppercase text-center"><spring:message code="order.total" /></th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${invoiceData.invoiceEntries}" var="tabentry" varStatus="tabcount">
                          <tr>
                            <td class="vlign-top orderno">
                            <a data-toggle="collapse" data-parent="#accordion" href="#tablet-collapse${tabcount.count}"
                              class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
                                class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
                            </a> 
                            <a href="#"> ${tabentry.product.code}</a>
                              <div id="tablet-collapse${tabcount.count}" class="panel-collapse collapse">
                                <div class="margintop15">
                                  <div class="sub-details-row">
                                    <p style="font-family: jnjlabelfont; font-size: 10px">SHIPPING</p>
                                    <p>
									<c:choose>
                                          <c:when test="${not empty tabentry.shippingMethod}">
                                            <span class="txtFont">${tabentry.shippingMethod}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont"></span>
                                          </c:otherwise>
                                        </c:choose>
										</p>
                                  </div>
                                  <div class="sub-details-row">
                                    <p style="font-family: jnjlabelfont; font-size: 10px">STATUS</p>
                                    <P>
                                    <div class="invoice-status">${tabentry.status}</div>
                                    <div>Tracking</div>
                                    <div>
                                      <c:if test="${isMddSite && not empty orderData.shippingTrackingInfo }">
                                        <div class="text-uppercase">
                                          <spring:message code="orderDetailPage.orderData.trackingHash" />
                                        </div>
                                        <div>
                                          <a href="#"> <c:forEach items="${orderData.shippingTrackingInfo[mobentry.lineNumber]}" var="data">
                                              <p class="smallFont floatRight">
                                                <a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
                                              </p>
                                            </c:forEach>
                                          </a>
                                        </div>
                                      </c:if>
                                      <div>
		                                  <a href="#"><spring:message code="invoiceDetailPage.proof.delivery.link" /></a>
		                                </div>
                                    </div>
                                   
                                </div>
                              </div>
                              </td>
                            <td class="text-left">
                              <div class="display-row">
                                <div class="table-cell">
                                  <span class="Tablesubtxt">
                                    <span class="invoice-content"> <span class="invoice-label-head"></span> <span class="invoice-content">
                                            <c:url value="${tabentry.product.url}" var="productUrl" />
                                            <div class="orderProdDesc left">
                                              <h4 title="${tabentry.product.name}" />
                                              <c:choose>
                                                <c:when test="${empty tabentry.product.url}">
																					${tabentry.product.name}
																				</c:when>
                                                <c:otherwise>
                                                  <a href="${productUrl}">${tabentry.product.name}</a>
                                                </c:otherwise>
                                              </c:choose>
                                            </div>
                                        </span>
                                        </span>
                                    <p class="secondline"><spring:message code="orderDetailPage.orderData.jnjID" />${tabentry.product.code}</p>
                                    <p class="secondline"><spring:message code="orderDetailPage.orderData.gtin" />${tabentry.product.gtin}</p>
                                  </span>
                                </div>
                              </div>
                            </td>
                            <td class="text-center text-nowrap">
								<c:if test="${!isMddSite}">
                                          <div>
                                            <p class="inputBox">${tabentry.qty}</p>
                                            <p>
                                              <c:if test="${not empty tabentry.product.deliveryUnit}">
                                                <span class="descSmall block"> ${tabentry.product.deliveryUnit} (${tabentry.product.numerator}
                                                  &nbsp; ${tabentry.product.salesUnit}) </span>
                                              </c:if>
                                            </p>
                                          </div>
                                        </c:if>
                            </td>
                            <td class="text-center">
							<c:choose>
                                          <c:when test="${not empty tabentry.basePrice}">
                                            <span class="txtFont">${tabentry.basePrice.formattedValue}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont">&nbsp;</span>
                                          </c:otherwise>
                                        </c:choose>
							</td>
                            <td class="text-center">
							<c:choose>
                                          <c:when test="${not empty tabentry.totalPrice.formattedValue}">
                                            <span class="txtFont priceList">${tabentry.totalPrice.formattedValue}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont"><spring:message code="orderDetailPage.orderData.notAvailable" /></span>
                                          </c:otherwise>
                                        </c:choose>
							</td>
                          </tr>
                          </c:forEach>
                        </tbody>
                      </table> 
                    </div>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
               <%--  <div class="invoice-nodata-msg">
                  <span><spring:message code="orderDetailPage.no.results" /></span>
                </div> --%>
              </c:otherwise>
            </c:choose>
          </div>
          <c:choose>
            <c:when test="${not empty orderInvoices }">
              <c:forEach items="${orderInvoices}" var="invoiceDatamob" varStatus="statcount">
                <!-- Table collapse for mobile device-->
                <div class="Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
                  <div class="invoice-accordian">
                    <div class="invoice-accordian-header row">
                      <div class="col-xs-6">
                        <a data-toggle="collapse" data-parent="#accordion" href="#mobile-collapse1" class="ref_no toggle-link panel-collapsed"><span
                          class="glyphicon glyphicon-plus invoice-accordian-icon"></span>Invoice 1</a>
                      </div>
                     <c:url value="/order-history/orderInvoiceDetailDownload" var="orderInvoiceDetailDownloadUrl" />
                    		 <form:form id="invoiceDetail" action="${orderInvoiceDetailDownloadUrl}" method="POST"> 
                      <div class="col-xs-6">
                        <div class="invoice-label-head text-uppercase">
                          <spring:message code="invoiceDetailPage.invoiceNumber" />
                        </div>
                        <div class="invoice-number-table">${invoiceDatamob.invoiceNumber}</div>
                      </div>
                      <div class="col-xs-12 invoice-download-link">
                        <span class="pull-right"><span class="link-txt boldtext"><spring:message code="product.search.download" /></span> <a href="#">
                        <input type="hidden"
	                            value="${invoiceDatamob.invoiceNumber}" name="invoiceNumber">
                       <input type="hidden" value="${orderData.code}" name="orderCode"> 
                        <input type="submit"
                            class="tertiarybtn excel pdfdownloadlinks" value="XLS" name="downloadType" id="invoiceExcel" /></a> | <a href="#">
                            <input
                            type="submit" class="tertiarybtn pdf pdfdownloadlinks" value="PDF" name="downloadType" id="invoicePdf" /></a></span>
                      </div>
                       </form:form>
                      <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 pull-right">
	                      <c:if test="${orderData.displayDisputeOption}">
	                        <div class="full-width-btns pull-right" style="padding-top: 17px">
	                        <c:url value="/order-history/order/invoice/disputeItem?invoiceNum=${invoiceDatamob.invoiceNumber}&orderCode=${orderData.code}&productCode=${entry.product.baseMaterialNumber}&qty=${entry.orderedQty} 
									&contractNum=${entry.contractNum}&totalPrice=${entry.totalPrice.value}" var="disputeItemUrl" />
	                          <a class="btn btnclsnormal dispute-button naDisputeItem" id="disputeItemBottom" href="${disputeItemUrl}" orderCode= "${orderCode}" productCode="${entry.product.baseMaterialNumber}" 
											invoiceNum="${invoiceDatamob.invoiceNumber}" totalPrice="${entry.totalPrice.value}" contractNum="${entry.contractNum }"
											qty="${entry.orderedQty}"><spring:message code="invoiceDetailPage.disputeItemsInvoice" /></a>
	                          
	                        </div>
	                       </c:if> 
	                      </div>
                    </div>
                    
					                    
                    <div class="invoice-accordian-body panel-collapse collapse" id="mobile-collapse${statcount.count}">
                      <table class="table table-bordered table-striped invoice-mobile-table bordernone">
                        <thead>
                          <tr>
                            <th class="text-left text-uppercase"><spring:message code="invoiceDetailPage.item" /></th>
                          </tr>
                        </thead>
                        <c:forEach items="${invoiceDatamob.invoiceEntries}" var="mobentry" varStatus="incount">
                          <tbody>
                            <tr>
                              <td class="panel-title text-left">
                              <a data-toggle="collapse" data-parent="#accordion"
                                href="#invoice-${statcount.count}-collapse${incount.count}" class="ref_no toggle-link panel-collapsed"><span
                                  class="glyphicon glyphicon-plus"></span>
                               </a>
                                  <a href="#" class="ref_no"> ${mobentry.product.code}</a>
                                <div id="invoice-${statcount.count}-collapse${incount.count}" class="panel-collapse collapse">
                                  <div class="details">
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="orderDetailPage.orderData.product" />
                                      </div>
                                      <div>
                                        <span class="invoice-content"> <span class="invoice-label-head"></span> <span class="invoice-content">
                                            <c:url value="${mobentry.product.url}" var="productUrl" />
                                            <div class="orderProdDesc left">
                                              <h4 title="${mobentry.product.name}" />
                                              <c:choose>
                                                <c:when test="${empty mobentry.product.url}">
																					${mobentry.product.name}
																				</c:when>
                                                <c:otherwise>
                                                  <a href="${productUrl}">${mobentry.product.name}</a>
                                                </c:otherwise>
                                              </c:choose>
                                            </div>
                                        </span>
                                        </span>
                                      </div>
                                      <div>
                                        <span class="invoice-label-head"><spring:message code="orderDetailPage.orderData.jnjID" /></span> <span
                                          class="invoice-content">${mobentry.product.code}</span>
                                      </div>
                                      <div>
                                        <span class="invoice-label-head"><spring:message code="orderDetailPage.orderData.gtin" /></span> <span
                                          class="invoice-content">${mobentry.product.gtin}</span>
                                      </div>
                                      <%-- <div>
                                        <span class="invoice-label-head"><spring:message code="orderDetailPage.orderData.contract" /></span> <span
                                          class="invoice-content">${mobentry.contractNum}</span>
                                      </div> --%>
                                      <!-- <div><span class="invoice-label-head">HAZMAT CODE</span> <span class="invoice-content">NA</span></div> -->
                                    </div>
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="invoiceDetailPage.Quantity" />
                                      </div>
                                      <div>
                                        <c:if test="${!isMddSite}">
                                          <div>
                                            <p class="inputBox">${mobentry.qty}</p>
                                            <p>
                                              <c:if test="${not empty mobentry.product.deliveryUnit}">
                                                <span class="descSmall block"> ${mobentry.product.deliveryUnit} (${mobentry.product.numerator}
                                                  &nbsp; ${mobentry.product.salesUnit}) </span>
                                              </c:if>
                                            </p>
                                          </div>
                                        </c:if>
                                      </div>
                                    </div>
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="orderDetailPage.orderData.itemPrice" />
                                      </div>
                                      <div>
                                        <c:choose>
                                          <c:when test="${not empty mobentry.basePrice}">
                                            <span class="txtFont">${mobentry.basePrice.formattedValue}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont">&nbsp;</span>
                                          </c:otherwise>
                                        </c:choose>
                                      </div>
                                    </div>
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="orderDetailPage.orderData.Total" />
                                      </div>
                                      <div>
                                        <c:choose>
                                          <c:when test="${not empty mobentry.totalPrice.formattedValue}">
                                            <span class="txtFont priceList">${mobentry.totalPrice.formattedValue}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont"><spring:message code="orderDetailPage.orderData.notAvailable" /></span>
                                          </c:otherwise>
                                        </c:choose>
                                      </div>
                                    </div>
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="orderDetailPage.orderData.Shipping" />
                                      </div>
                                      <div>
                                        <c:choose>
                                          <c:when test="${not empty mobentry.shippingMethod}">
                                            <span class="txtFont">${mobentry.shippingMethod}</span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="txtFont"></span>
                                          </c:otherwise>
                                        </c:choose>
                                      </div>
                                    </div>
                                    <div class="sub-details-row">
                                      <div class="text-uppercase">
                                        <spring:message code="orderDetailPage.orderData.status" />
                                      </div>
                                      <div>${mobentry.status}</div>
                                      <c:if test="${isMddSite && not empty orderData.shippingTrackingInfo }">
                                        <div class="text-uppercase">
                                          <spring:message code="orderDetailPage.orderData.trackingHash" />
                                        </div>
                                        <div>
                                          <a href="#"> <c:forEach items="${orderData.shippingTrackingInfo[mobentry.lineNumber]}" var="data">
                                              <p class="smallFont floatRight">
                                                <a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
                                              </p>
                                            </c:forEach>
                                          </a>
                                        </div>
                                      </c:if>
                                    </div>
                                  </div>
                                </div></td>
                            </tr>
                          </tbody>
                        </c:forEach>
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
          <!--Accordian Ends here -->
        </div>
      </div>
    </div>
  </div>
</template:page>