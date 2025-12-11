<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderData" required="true"
	type="com.jnj.facades.data.JnjGTOrderData"%>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="address"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/standard"%>
<%@ taglib prefix="consAddress"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/cart/house"%>
<%@ taglib prefix="deliveredCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
	<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:url value="/order-history" var="orderHistoryURL" />

<c:url value="/order-history/refineOrderDetail" var="refineUrl"></c:url>
<c:set var="numberOfEntries" value="${fn:length(orderData.entries)}"></c:set>
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<div class="">
	<form:form class="searchSortForm" id="searchSortForm1"
		name="searchSortForm" method="get" action="${refineUrl}"
		commandName="searchSortForm">
		<input style="display: none;" type="hidden" id="orderCode"
			name="orderCode" value="${orderData.code}" />
		<div
			class="orderdetailscontainer col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ordertypedetails">
			<!-- 	<div class="row bottom-border"> -->
					<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12" >
						<table class="table">
							<thead>
								<tr>
									<th><strong><spring:message
											code="orderDetailPage.orderData.orderNumber" /></strong></th>
									<th><strong><spring:message
											code="orderDetailPage.orderData.orderType" /></strong></th>
									<th><strong><spring:message
											code="orderDetailPage.orderData.accountNumber" /></strong></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="fontsize25">${orderData.orderNumber}</td>
									<td class="fontsize17"><c:if
											test="${not empty orderData.orderType}">
											<spring:message
												code="cart.common.orderType.${orderData.orderType}" />
										</c:if></td>
									<td><c:if test="${not empty orderData.soldToAccount}">${orderData.soldToAccount}</c:if></td>
	
								</tr>
							</tbody>
						</table>
					</div>
					<div
					class="col-lg-5 col-md-5 col-sm-5 col-xs-12 text-right downloadLinks" >
					<p>
						<spring:message code="product.search.download" />
						<span> <input type="submit"
							class="tertiarybtn pdfdownloadlinks" value="XLS"
							name="downloadType" /> <span class="pipesymbol">|</span>&nbsp; <input
							type="submit" class="tertiarybtn marginRight pdfdownloadlinks"
							value="PDF" name="downloadType" /></span>
					</p>


				</div>
			</div> 
				<div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12 shippingAddressDetails">
					<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shippingAddress" id="order-detail-left-panel">
						<div id="orderDetail-leftside" >
						 <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding30px borderbtm">
							<c:if test="${not empty orderData.deliveryAddress}">
								<h6>
									<spring:message code="orderDetailPage.orderData.shipToAddress" />
								</h6>
								<c:choose>
									<c:when test="${isMddSite}">
										<address:deliveryAddress
											deliveryAddress="${orderData.deliveryAddress}" />
									</c:when>
									<c:otherwise>
										<consAddress:deliveryAddress
											deliveryAddress="${orderData.deliveryAddress}" />
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>

						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 padding30px">
							<c:if test="${not empty orderData.billingAddress}">
								<c:if test="${isMddSite}">
									<h6>
										<spring:message
											code="orderDetailPage.orderData.billingNameAndAddress" />
									</h6>
								</c:if>
								<c:if test="${!isMddSite}">
									<div class="labelText">
										<spring:message code="cart.common.soldToAddress" />
									</div>
								</c:if>
								<div class="minHeight">
									<c:choose>
										<c:when test="${isMddSite}">
											<address:deliveryAddress
												deliveryAddress="${orderData.billingAddress}"
												companyName="${orderData.b2bUnitName}" />
										</c:when>
										<c:otherwise>
											<address:deliveryAddress
												deliveryAddress="${orderData.billingAddress}" />
										</c:otherwise>
									</c:choose>
								</div>
							</c:if>
						</div>		
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 marginbtm20" id="orderDetail-leftLink">
							<c:url	value="/order-history/order/invoiceDetail/${orderData.code}" var="invoiceDetailUrl" />
							<%-- <a href="${invoiceDetailUrl}">
							<spring:message	code="orderDetailPage.orderData.viewInvoiceDetails" /></a> 
							<span class="pipesymbol">|</span> --%>
							<c:if test="${isMddSite}">
								<a href="#" data-target="#shipping-detail-popup"
									data-toggle="modal"><spring:message
										code="orderDetailPage.orderData.shipmentinfo" /></a>
							</c:if>
						</div>
					</div>

					<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 orderDetailsAndStatus"  id="order-detail-right-panel" >
						<div id="orderDetail-rightside">
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 padding30px">
						<c:if test="${not empty orderData.purchaseOrderNumber}">
							<h6 class="text-uppercase"><spring:message code="customer.po.number" /></h6><p>${orderData.purchaseOrderNumber}</p>
						</c:if>
							<h6  class="capital"><spring:message code="po.date" /></h6><p><fmt:formatDate value="${orderData.poDate}"
									pattern="${dateformat} " /></p>
							<h6 class="text-uppercase"><spring:message code="stock.user" /></h6><p>${orderData.stockUser}</p>	
							<h6 class="text-uppercase"><spring:message code="cart.common.shipping.instructions" /></h6><p>${orderData.shippingInstructions}</p>	
							<%-- <h6>
							<h6>
								<spring:message code="orderDetailPage.orderData.orderDate" />
							</h6>
							<p>
								<fmt:formatDate value="${orderData.created}"
									pattern="${dateformat}" />
							</p>

							<c:if test="${not empty orderData.purchaseOrderNumber}">

								<h6>
									<spring:message code="orderDetailPage.orderData.purchaseOrder" />
								</h6>
								<p>${orderData.purchaseOrderNumber}</p>
							</c:if>
							<c:if test="${not empty orderData.statusDisplay}">
								<h6>
									<spring:message code="orderDetailPage.orderData.status" />
								</h6>
								<p>${orderData.statusDisplay}</p>
							</c:if>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 padding30px">
							 <c:if test="${not empty orderData.reasonCode}"> 
								<h6>
									<spring:message code="orderDetailPage.orderData.reasonCode" />
								</h6>
								<p class="breakWord">${orderData.reasonCode}</p>
							 </c:if>
							<!-- Jira#3647 start-->
							<c:if test="${not empty orderData.attention}">
								<h6>
									<spring:message code="orderDetailPage.orderData.attention" />
								</h6>
								<p>${orderData.attention}</p>
							</c:if>
							<!-- Jira#3647 end-->
							<c:if test="${isMddSite}">
								<c:if test="${orderData.paymentType != null}">
									<h6>
										<spring:message code="orderDetailPage.orderData.paymentMethod" />
									</h6>
									<p>${orderData.paymentType.displayName}</p>
								</c:if>

								<c:if test="${not empty orderData.paymentInfo}">
									<div>
										<div>
											<span class="labelText"><spring:message
													code="orderDetailPage.orderData.creditCardDetails" /></span>
										</div>
										<div>
											<span>${orderData.paymentInfo.cardType}</span></br> <span>${orderData.paymentInfo.cardNumber}</span></br>
											<span>${orderData.paymentInfo.expiryMonth}/${orderData.paymentInfo.expiryYear}</span>
										</div>
									</div>
								</c:if>
							</c:if>
							 <c:if test="${not empty orderData.requestedDeliveryDate}">
							 	<h6>
										<spring:message code="cart.common.requestDeliveryDate"/>
									</h6>
									<p>
									<fmt:formatDate value="${orderData.requestedDeliveryDate}" pattern="MM/dd/yyyy" />
									</p>
								
							 </c:if>
							<%-- <c:if test="${!isMddSite}"> --%> <!-- Jira#3647 -->
								<c:if test="${not empty orderData.dropShipAccount}">
									<h6>
										<spring:message
											code="orderDetailPage.orderData.dropShipAccount" />
									</h6>
									<p>${orderData.dropShipAccount}</p>
								</c:if>
							<%-- </c:if> --%>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 TextPadding">
														<h6><spring:message code="order.consignment.overall.status" /></h6><p>${orderData.status}</p>		
														<h6><spring:message code="order.consignment.return.created.date" /></h6><p><fmt:formatDate value="${orderData.createdOn}" pattern="${dateformat}" /></p>
														<h6><spring:message code="order.consignment.end.user" /></h6><p>${orderData.endUser}</p>																											
													</div>
					
						</div>
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
							<%-- <a href="#"><spring:message
									code="orderDetailPage.orderData.return" /></a> <span
								class="pipesymbol">|</span>  --%>
							<!-- AAOL-3681 -->	
								
									<span>
									<c:url value="/order-history/order/disputeOrder?orderCode=${orderData.code}" var="disputedetailsUrl" />
										<a class="tertiarybtn disputeOrder" id="disputeOrder" href="${disputedetailsUrl}">
											<spring:message code="orderDetailPage.orderData.disputeThisOrder"/>
										</a>
									</span>
							
							<!-- AAOL-3681 -->
						</div>
						</div>
						
					</div>
					
					

			</div>
		</div>
	</form:form>
	
	<div class="last">
			<c:if test="${!isMddSite}">
               	<c:if test="${not empty orderData.carrier}">
					<div class="labelText"><spring:message code="orderDetailPage.orderData.carrier"/></div>
					<div>
						<c:forEach items="${orderData.carrier}" var="carrier">
							<p class="textBlack">${carrier}</p>
						</c:forEach>
					</div>
				</c:if>
	            <c:choose>
					<c:when test="${not empty orderData.billOfLading}">
						<div class="labelText"><spring:message code="orderDetailPage.orderData.billOfLading"/></div>
						<div>
							<c:forEach items="${orderData.billOfLading}" var="billOfLading" varStatus="status">
								<p class="textBlack">${billOfLading}</p>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
						<p class="textBlack"><spring:message code="orderDetailPage.orderData.notAvailable"/></p>
					</c:otherwise>
				</c:choose>               
				<div class="labelText"><spring:message code="orderDetailPage.orderData.tracking"/></div>
				<c:choose>
					<c:when test="${empty orderData.shippingTrackingInfo}">
						<spring:message code="orderDetailPage.orderData.notAvailable"/>
					</c:when>
					<c:otherwise>
						<c:forEach items="${orderData.shippingTrackingInfo}" var="shippingTrackingInfo">
							<c:choose>
								<c:when test="${not empty shippingTrackingInfo.value}">
									<c:forEach items="${shippingTrackingInfo.value}" var="data" >
										<c:choose>
										   <c:when test="${data.trackingUrl eq '#' && !isMddSite}">
											<p class="textBlack">
												${data.trackingId}
											</p>
										  </c:when>
										<c:otherwise>
											<p>
												<a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
											</p>
										</c:otherwise>
									  </c:choose>
									</c:forEach>	
								</c:when>
							</c:choose>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</c:if>
			<%-- <c:if test="${orderData.displaySurgeonInfo }">
				<div>
					<a href="#" id="surgeonInformation" orderCode="${orderData.code}">Surgeon Information</a>
				</div>
			</c:if> --%>
			 <c:if test="${orderData.rgaLinkURL}">
			  <c:url value="/order-history/order/${orderData.code}/${orderData.orderNumber}/callRga/orderDetails" var="rgaUrl" />
				<div><a id="rgaId" href="${rgaUrl}"><spring:message code="orderDetailPage.orderData.rgaLink"/></a></div>
			</c:if>
			<c:if test="${orderData.codLinkURL}">
			<c:url value="/order-history/order/${orderData.code}/${orderData.orderNumber}/callCmod/orderDetails" var="cmodUrl" />	
				<div><a id="cmodId" href="${cmodUrl}"><spring:message code="orderDetailPage.orderData.codLink"/></a></div>
			</c:if>
		</div>


	<!--  Shipping Information  pop up-->

	<div class="modal fade jnj-popup" id="shipping-detail-popup"
		role="dialog">
		<div class="modal-dialog modalcls modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close clsBtn" data-dismiss="modal">
						<spring:message code="orderDetailPage.orderData.shippingClose" />
					</button>
					<h4 class="modal-title">
						<spring:message code="orderDetailPage.orderData.shippingheader" />
					</h4>
				</div>
				<div class="modal-body">
					<div class="row" id="shipping-detail-panel">

						<c:if test="${isMddSite}">
							<div
								class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
								<div class="shipping-popup-title">
									<spring:message code="orderDetailPage.orderData.packingList" />
								</div>
								<div>
									<c:choose>
										<c:when
											test="${not empty orderData.packingListDetails && packingListExpiryDate<=60}">
											<p>
												<c:forEach items="${orderData.packingListDetails}"
													var="packingListDetailEntry" varStatus="status">
													<c:choose>
														<c:when test="${'#' eq  packingListDetailEntry.value}">
															${packingListDetailEntry.key}<br>
														</c:when>
														<c:otherwise>
															<p>
																<a href="${JNJ_SITE_URL}${packingListDetailEntry.value}${packingListDetailEntry.key}"
																	onclick="window.open(this.href);return false;">${packingListDetailEntry.key}</a>
															</p>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</p>
										</c:when>
										<c:otherwise>
											<p>
												<b><spring:message
														code="orderDetailPage.orderData.notAvailable" /></b>
											</p>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:if>
					<!-- 	AAOL-3638 -->
						<c:choose>
						<c:when test="${not empty orderData.carrier}">
							<div
								class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
								<div class="shipping-popup-title">
									<spring:message code="orderDetailPage.orderData.carrier" />
								</div>
								<div>
									<c:forEach items="${orderData.carrier}" var="carrier">
								${carrier}
								</c:forEach>

								</div>
							</div>
						</c:when>
						<c:otherwise>
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
									<div class="shipping-popup-title">
										<spring:message code="orderDetailPage.orderData.carrier" />
									</div>
									<div>
										<b><spring:message
												code="orderDetailPage.orderData.notAvailable" /></b>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					<!-- AAOL-3638 -->
						<c:choose>
							<c:when test="${not empty orderData.billOfLading}">
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
									<div class="shipping-popup-title">
										<spring:message code="orderDetailPage.orderData.billOfLading" />
									</div>
									<div>
										<c:forEach items="${orderData.billOfLading}"
											var="billOfLading" varStatus="status">
									${billOfLading}
								</c:forEach>
									</div>
								</div>
							</c:when>
							<c:otherwise>
								<div
									class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
									<div class="shipping-popup-title">
										<spring:message code="orderDetailPage.orderData.billOfLading" />
									</div>
									<div>
										<b><spring:message
												code="orderDetailPage.orderData.notAvailable" /></b>
									</div>
								</div>
							</c:otherwise>
						</c:choose>

						<div
							class="col-lg-3 col-md-3 col-sm-3 col-xs-12 shipping-detail-row">
							<div class="shipping-popup-title">
								<spring:message code="orderDetailPage.orderData.tracking" />
							</div>
							<div>
								<c:choose>
									<c:when test="${empty orderData.trackingIdList}">
										<b><spring:message
												code="orderDetailPage.orderData.notAvailable" /></b>
									</c:when>
									<c:otherwise>
										<c:forEach items="${orderData.trackingIdList}"
											var="shippingTrackingInfo">
											<p>
												<a href="${shippingTrackingInfo.value}" target="_blank">${shippingTrackingInfo.key}
												</a>
											</p>
										</c:forEach>
									</c:otherwise>
								</c:choose>

							</div>
						</div>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 text-right downloadLinks">
							<p> <spring:message code="reports.download.label" /> 
								<span> &nbsp;&nbsp;&nbsp;&nbsp;
								<c:url value="/order-history/updateShippingList?shippingDetails=${orderData.packingListDetails}&orderCode=${orderData.code}" var="updateShippingList"/>
								<a href="${updateShippingList}"><spring:message code="reports.excel.label" /></a> 
								</span>
							</p>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

	<!--  Shipping Information  pop up Ends-->