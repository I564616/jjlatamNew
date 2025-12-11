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
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<template:page pageTitle="${pageTitle}"> 
  <!-- Start - Body Content -->
  <!--  AAOL-6138 changes start -->
	<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
	<!--  AAOL-6138 changes end -->
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
                <div class="invoice-label-head text-uppercase"><spring:message code="cart.common.ShipToAdd" /></div>
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
                <div class="text-uppercase invoice-cutom-name-label"><spring:message code="cart.common.BillingAdd" /></div>
                <div class="invoice-content">${orderData.billingAddress.formattedAddress}</div>
              </c:if>
            </div>
            <%--  <div class="invoice-content-holder invoice-left">
              <div class="invoice-label-head text-uppercase">
                <spring:message code="orderDetailPage.orderData.status" />
              </div>
              <div class="invoice-content">${orderData.statusDisplay}</div>
            </div> --%>
          </div>
          <div class="col-lg-1 col-md-1 col-sm-1 col-xs-12" id="invoice-separator"></div>
          <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 invoice-rightTobottom-content">
            <div class="row paddingForIpadNA">
              <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 no-left-right-pad-mobi">
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="customer.po.number"/></div>
                  <div class="invoice-content">${orderData.purchaseOrderNumber}</div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="po.date"/></div>
                  <div class="invoice-content">
                    <p>
                      <fmt:formatDate value="${orderData.poDate}" pattern="${dateformat}" />
                    </p>
                  </div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="stock.user"/></div>
                  <div class="invoice-content">${orderData.stockUser}</div>
                </div>
                
                <%-- <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.carrier"/></div>
                  <div class="invoice-content">${orderData.carrierName}</div>
                </div> 
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.tracking"/></div>
                  <div class="invoice-content">
                    <a href="#">${orderData.tracking}</a>
                  </div>
                </div> 
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="shipping.instructions"/></div>
                  <div class="invoice-content">${orderData.shippingInstructions}</div>
                </div>
              </div> --%>
              
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 no-left-right-pad-mobi">
            	<div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.paymentMethod"/></div>
                  <div class="invoice-content">${orderData.paymentmethod}</div>
                </div>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="overall.status"/></div>
                  <div class="invoice-content">${orderData.statusDisplay}</div>
                </div>
                <%-- <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.reqDeliveryDate"/></div>
                  <div class="invoice-content">
                    <p>
                      <fmt:formatDate value="${orderData.requestedDeliveryDate}" pattern="${dateformat}" />
                    </p>
                  </div>
                </div> --%>
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="order.consignment.end.user"/></div>
                  <div class="invoice-content">${orderData.endUser}</div>
                </div>
                <%-- <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="invoiceDetailPage.packingList"/></div>
                  <div class="invoice-content">
                    <a href="#">${orderData.packingList}</a>
                  </div>
                </div> 
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="orderDetailPage.orderData.billOfLading"/></div>
                  <div class="invoice-content">${orderData.billOfLanding}</div>
                </div> 
                <div class="invoice-content-holder invoice-right">
                  <div class="invoice-label-head text-uppercase"><spring:message code="invoiceDetailPage.proof.delivery.link"/></div>
                  <div class="invoice-content" style="margin-top: 40px">
                    <p>${orderData.proofOfDelivery}</p>
                  </div>
                </div> --%>
              </div>
          </div>
          </div>
          <div class="row">
            <div class="col-lg-12 col-md-12">
              <c:forEach items="${orderInvoices}" var="orderInvoices">
                <div class="table-padding">
                  <div class="hidden-xs">
                    <div class="invoice-accordian">
                      <div class="invoice-accordian-header row">
                        <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12" style="padding-right: 0px">
                          <a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="ref_no toggle-link panel-collapsed"><span
                            class="glyphicon glyphicon-plus invoice-accordian-icon"></span></a><spring:message code="invoiceDetailPage.invoice"/>
                          <div class="invoice-number-table paddingleft15px">${orderInvoices.invoiceNumber }</div>
                        </div>
                        <div class="col-lg-2 col-md-2 col-sm-2 col-xs-12" style="padding-left: 0px">
                          <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="invoiceDetailPage.billingDate"/></div>
                          <div class="invoice-number-table lineheight32px">
                            <fmt:formatDate value="${orderInvoices.billingdate}" pattern="${dateformat}" />
                          </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
                          <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="invoiceDetailPage.carrierName"/></div>
                          <div class="invoice-number-table lineheight32px">${orderData.carrierName}</div>
                        </div>
                        <c:url value="/order-history/orderInvoiceDetailDownload" var="orderInvoiceDetailDownloadUrl" />
                        <form:form id="invoiceDetail" action="${orderInvoiceDetailDownloadUrl}" method="POST">
                          <div class="col-lg-4 col-md-4 col-sm-4 invoice-download-link" style="margin-top: 14px;">
                            <span class="pull-right"> <span class="link-txt boldtext"><spring:message code="product.search.download" /></span>
                              <input type="hidden" value="${orderData.code}" name="orderCode"> <input type="submit" class="pdfdownloadlinks"
                              value="XLS" name="downloadType" id="invoiceExcel" /> | <input type="submit" class="pdfdownloadlinks" value="PDF"
                              name="downloadType" id="invoicePdf" /> <input type="hidden" value="${orderInvoices.invoiceNumber}" name="invoiceNumber">
                            </span>
                          </div>
                        </form:form>
                        <div class="col-lg-5 col-md-5 col-sm-3 col-xs-12 clearBoth">
                          <div class="packingList" style="padding-right: 35px">
                            <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="invoiceDetailPage.packingList"/></div>
                            <div class="invoice-number-table lineheight32px">
                              <a href="#">${orderData.packingList}</a>
                            </div>
                          </div>
                        </div>
                        <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12 billofLandIpad">
                          <div class="invoice-label-head text-uppercase paddingtop15px"><spring:message code="orderDetailPage.orderData.billOfLading"/></div>
                          <div class="invoice-number-table lineheight32px">${orderData.billOfLanding}</div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 pull-right">
                          <div class="full-width-btns pull-right" style="padding-top: 17px">
                            <button class="btn btnclsnormal dispute-button"><spring:message code="invoiceDetailPage.disputeItemsInvoice"/></button>
                          </div>
              </c:forEach>
            </div>
          </div>
          <div class="invoice-accordian-body panel-collapse collapse table-responsive" id="collapse1">
            <table id="datatab-desktop" class="hidden-xs hidden-sm table table-bordered table-striped invoice-desktop-table na-invoice-desktop">
              <thead>
                <tr>
                  <th class="text-uppercase no-sort text-center snoWidth"><spring:message code="orderDetailPage.orderData.item"/></th>
                  <th class="no-sort text-uppercase text-left"><spring:message code="orderDetailPage.orderData.product"/></th>
                  <th class="no-sort text-uppercase text-center"><spring:message code="invoiceDetailPage.invoicedQuantity"/></th>
                  <th class="no-sort text-uppercase text-center"><spring:message code="cart.review.entry.itemPrice"/></th>
                  <th class="no-sort text-uppercase text-center"><spring:message code="cart.review.entry.total"/></th>
                  <th class="no-sort text-uppercase text-center"><spring:message code="orderDetailPage.orderData.Shipping"/></th>
                  <th class="no-sort text-uppercase text-center"><spring:message code="orderDetailPage.orderData.status"/></th>
                </tr>
              </thead>
              <tbody>
                <c:set var="fieldLength" value="${fn:length(orderData.entries)}" />
                <c:forEach items="${orderData.entries}" var="entry" varStatus="count">
                  <tr>
                    <td class="text-center"><a href="#">${count.count}</td>
                    <td class="text-left">
                      <div class="display-row">
                        <div class="table-cell valign-middle">
                          <a href="javascript:;" class="showProductDeatils anchor-img" data="${productCode}"><product:productPrimaryImage
                              product="${entry.product}" format="cartIcon" /></a>
                        </div>
                        <div class="table-cell">
                          <span class="Tablesubtxt">
                            <p class="firstline">${entry.product.name}</p>
                            <p class="secondline"><spring:message code="cart.review.productDesc.jnJID"/> ${entry.product.code}</p>
                            <p class="secondline">
                              <spring:message code="orderDetailPage.orderData.gtin" />${entry.product.gtin}
                            </p>
                            <!-- <p class="secondline">GTIN#: 10758750007691</p> -->
                          </span>
                        </div>
                      </div>
                    </td>
                    <td class="text-center text-nowrap">${entry.quantity}
                      <div>${orderData.entries[fieldLength - count.count].product.numerator}&nbsp;${orderData.entries[fieldLength - count.count].product.salesUnit}</div>
                    </td>
                    <td class="text-center">${entry.basePrice.formattedValue}</td>
                    <td class="text-center">${entry.totalPrice.formattedValue}</td>
                    <td class="text-left text-nowrap">${orderData.carrierName}</td>
                    <td class="text-left">
                      <div class="invoice-status">${orderData.invoiceStatus}</div>
                      <div></div><spring:message code="orderDetailPage.orderData.trackingHash"/>
                      <div>
                        <a href="#">${orderData.tracking}</a>
                      </div>
                      <div>
                        <a href="#"><spring:message code="invoiceDetailPage.proof.delivery.link"/>
                          <p>${orderData.proofOfDelivery}</p>
                        </a>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            <%-- <table id="datatab-tablet" class="table table-bordered table-striped bordernone mobile-table hidden-xs hidden-md hidden-lg invoice-tablet-table">
															<thead>
																<tr>
																	<th class="text-uppercase no-sort text-center snoWidthNAIpad">#</th>
																	<th class="no-sort text-uppercase text-left">product</th>
																	<th class="no-sort text-uppercase text-center">Invoiced quantity</th>
																	<th class="no-sort text-uppercase text-center">Item price</th>
																	<th class="no-sort text-uppercase text-center">total</th>
																</tr>
															</thead>
															<tbody>
																<tr>
																	<td class="vlign-top orderno">
																		<a data-toggle="collapse" data-parent="#accordion" href="#tablet-collapse1" class="toggle-link panel-collapsed skyBlue ipadacctoggle">
																			<span class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
																		</a>
																		<a href="#">1-00</a>
																		<div id="tablet-collapse1" class="panel-collapse collapse">
																			<div class="margintop15">
																				<div class="sub-details-row">
																					<p style="font-family:jnjlabelfont; font-size:10px">SHIPPING</p>
																					<p>FedEx Ground</p>
																				</div>																					
																				<div class="sub-details-row">	
																					<p style="font-family:jnjlabelfont; font-size:10px">STATUS</p>
																					<P><div class="invoice-status">Invoiced</div>
																					<div>Tracking</div>
																					<div><a href="#">${orderData.tracking}</a></div>																					
																					<div><a href="#">Proof of Delivery</a></div>
																				</div>	
																			</div>
																		</div>
																	</td>
															
																	<td class="text-left">
																		<div class="display-row">
																			<div class="table-cell">
																				<img src="images/lactaid.jpg" class="imgprop"></img>
																			</div>
																			<div class="table-cell">
																				<span class="Tablesubtxt">
																					<p class="firstline">Lactaid<sup>@</sup> Fast Act Caplets, 32 Count</p>
																					<p class="secondline">J&J ID#: 231816</p>	
																					<p class="secondline">GTIN#: 10758750007691</p>																				
																				</span>
																			</div>	
																		</div>
																	</td>	
																	<td class="text-center text-nowrap">
																		3<div>each (1 each)</div>													
																	</td>
																	<td class="text-center">$484.00</td>
																	<td class="text-center">$1,452.00</td>
																	
																</tr>
															</tbody>
														</table>	
													</div>	
												</div>
												
											</div>
           --%>
            <%--           	<div class="hidden-xs ">
		<table id="datatab-desktop"	class="table table-bordered table-striped sorting-table error-on-top">
			<thead>
				<tr>
					<th class="no-sort text-uppercase">#${orderData.entries}</th>
				 	<th class="no-sort text-uppercase quanity-cell"><spring:message code="cart.common.customer.Product.code"/></th> 
				
					<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
					<th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
						 <div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
					</th>
					<th class="no-sort"><spring:message code="cart.validate.unitPrice"/></th>
					<th class="no-sort multitotal-head paddingleft10px"><spring:message code="cart.review.entry.total"/></th>
				 <th class="no-sort text-uppercase"><spring:message code="cart.common.customer.UoM"/></th>
					 <th class="no-sort text-center text-uppercase est-dateCol"><spring:message code="Estimated.Date"/></th>
					<th class="no-sort text-uppercase"><spring:message code="cart.priceQuote.status"/></th>  
				</tr>
			</thead>
			
			<!-- Change for GTUX_1259  for recently added product to top -->
			
			<tbody id="AddItemsCartpage" >
				<c:set var="fieldLength" value="${fn:length(orderData.entries)}"/>
		 		<c:forEach items="${orderData.entries}" var="entry"  varStatus="count">
				<tr id="orderentry- ${orderData.entries[fieldLength - count.count]}" class="shoppingcartOrderEntryList">
				<td class="vertical-align-top text-center">${count.count}</td>
					<td class="text-center">${entry.product.code}</td>
					<td class="text-left">
						<standardCart:productDescriptionBeforeValidation entry="${orderData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}" showRemoveLink="true"  showStatus="false"  rowcount="${count.count}"/>
					</td>
					<td>${entry.quantity}</td>
					<!-- <td></td>
					<td></td> -->
					 <td>${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit} </td>
					<td></td>
					<td></td> 
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>  --%>
            <%--  
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
          
    		
                          
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
                        <c:forEach items="${invoiceData.invoiceEntries}" var="entry" varStatus="count">
                          <tr>
                            <td class="vlign-top orderno"><a data-toggle="collapse" data-parent="#accordion" href="#tablet-collapse1"
                              class="toggle-link panel-collapsed skyBlue ipadacctoggle"> <span
                                class="glyphicon glyphicon-plus skyBlue toggle-plus-minus"></span>
                            </a> <a href="#">${count}</a>
                              <div id="tablet-collapse1" class="panel-collapse collapse">
                                <div class="margintop15">
                                  <div class="sub-details-row">
                                    <p style="font-family: jnjlabelfont; font-size: 10px">SHIPPING</p>
                                    <p>FedEx Ground</p>
                                  </div>
                                  <div class="sub-details-row">
                                    <p style="font-family: jnjlabelfont; font-size: 10px">STATUS</p>
                                    <P>
                                    <div class="invoice-status">Invoiced</div>
                                    <div>Tracking</div>
                                    <div>
                                      <a href="#">1Z8AR9541</a>
                                    </div>
                                    <div>
                                      <a href="#">Proof of Delivery</a>
                                    </div>
                                  </div>
                                </div>
                              </div></td>
                            <td class="text-left">
                              <div class="display-row">
                                <div class="table-cell">
                                  <img src="images/lactaid.jpg" class="imgprop"></img>
                                </div>
                                <div class="table-cell">
                                  <span class="Tablesubtxt">
                                    <p class="firstline">
                                      Lactaid<sup>@</sup> Fast Act Caplets, 32 Count
                                    </p>
                                    <p class="secondline">J&J ID#: 231816</p>
                                    <p class="secondline">GTIN#: 10758750007691</p>
                                  </span>
                                </div>
                              </div>
                            </td>
                            <td class="text-center text-nowrap">3
                              <div>each (1 each)</div>
                            </td>
                            <td class="text-center">$484.00</td>
                            <td class="text-center">$1,452.00</td>
                          </tr>
                          </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div class="invoice-nodata-msg">
                  <span><spring:message code="orderDetailPage.no.results" /></span>
                </div>
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
                      <div class="col-xs-6">
                        <div class="invoice-label-head text-uppercase">
                          <spring:message code="invoiceDetailPage.invoiceNumber" />
                        </div>
                        <div class="invoice-number-table">${invoiceDatamob.invoiceNumber}</div>
                      </div>
                      <div class="col-xs-12 invoice-download-link">
                        <span class="pull-right"><span class="link-txt boldtext">Download</span> <a href="#"><input type="submit"
                            class="tertiarybtn excel pdfdownloadlinks" value="XLS" name="downloadType" id="invoiceExcel" /></a> | <a href="#"><input
                            type="submit" class="tertiarybtn pdf pdfdownloadlinks" value="PDF" name="downloadType" id="invoicePdf" /></a></span>
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
                              <td class="panel-title text-left"><a data-toggle="collapse" data-parent="#accordion"
                                href="#invoice-${statcount.count}-collapse${incount.count}" class="ref_no toggle-link panel-collapsed"><span
                                  class="glyphicon glyphicon-plus"></span></a><a href="#" class="ref_no"> ${mobentry.product.code}</a>
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
                                      <div>
                                        <span class="invoice-label-head"><spring:message code="orderDetailPage.orderData.contract" /></span> <span
                                          class="invoice-content">${mobentry.contractNum}</span>
                                      </div>
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
  </div> --%>
            <%-- <c:when test="${not empty orderInvoices }">
                      <c:forEach items="${orderInvoices}" var="invoiceData" varStatus="count">
                        <c:forEach items="${invoiceData.packingList}" var="pl" varStatus="count">
                          <div class="invoice-content">${invoiceData.invoiceNumber}:${pl}</div>
                        </c:forEach>
                      </c:forEach>
                    </c:when> --%>
</template:page>