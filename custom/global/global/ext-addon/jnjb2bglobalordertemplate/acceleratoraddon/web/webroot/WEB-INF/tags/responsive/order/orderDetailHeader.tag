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
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/order"%>
	
<c:url value="/order-history" var="orderHistoryURL" />

<c:url value="/order-history/refineOrderDetail" var="refineUrl"></c:url>
<c:set var="numberOfEntries" value="${fn:length(orderData.entries)}"></c:set>
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
					class="col-lg-5 col-md-5 col-sm-5 col-xs-12 text-right downloadLinks downloadLinks-mobile" >
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
				<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 shippingAddressNA leftSidePanel">
					<div class="leftPanelContent">
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 borderbtm sub-padding">
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
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 sub-padding" >
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
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 leftSidePanelLink linkspadding" >
						<c:url	value="/order-history/order/invoiceDetail/${orderData.code}" var="invoiceDetailUrl" />
						<a href="${invoiceDetailUrl}">
						<spring:message	code="orderDetailPage.orderData.viewInvoiceDetails" /></a> 
						
						<c:if test="${isMddSite}">
						<span class="pipesymbol">|</span>
							<a href="#" data-target="#shipping-detail-popup"
							data-toggle="modal"><spring:message
								code="orderDetailPage.orderData.shipmentinfo" /></a>
						</c:if>
					</div>
				</div>
				<div class="row" id="orderdetails-hline-holder">
					<div id="orderdetails-hline" class="col-lg-12 col-md-12 col-sm-12 col-xs-12"></div>
				</div>	
				
				<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12  orderDetailsAndStatus rightSidePanel">
					<div class="rightPanelContent">
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 TextPadding">
							<h6>
								<spring:message code="orderDetailPage.orderData.orderDate" />
							</h6>
							<p><!-- Modified by Archana for AAOL-5513 -->
							<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
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
						
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 TextPadding">
							 <c:if test="${not empty orderData.reasonCode}"> 
								<h6>
									<spring:message code="orderDetailPage.orderData.reasonCode" />
								</h6>
								<p class="breakWord"><label:message messageCode="${orderData.reasonCode}"/></p>
							 </c:if>
							<!-- Jira#3647 start-->
							<c:if test="${not empty orderData.attention}">
								<h6 class="text-uppercase">
									 <c:choose>
									<c:when test="${'ZOR' eq orderData.orderType}">
									<spring:message code="orderDetailPage.orderData.ImportantInstructions" />
									</c:when>
									<c:when test="${'ZNC' eq orderData.orderType}">
									<spring:message code="orderDetailPage.orderData.ImportantInstructions" />
									</c:when>
									<c:otherwise>
									<spring:message code="orderDetailPage.orderData.attention" />
									</c:otherwise>									
									</c:choose>
								</h6>
								<p class="breakWord">${orderData.attention}</p>
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
									<p><!-- Modified by Archana for AAOL-5513 -->
									<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
									<fmt:formatDate value="${orderData.requestedDeliveryDate}" pattern="${dateformat}" />
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
					</div>
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 linkspadding rightSidePanelLink" >
						<a href="#"><spring:message
									code="orderDetailPage.orderData.return" /></a> 
					<!-- AAOL-3681 -->	
						<c:if test="${orderData.displayDisputeOption}">
						<span class="pipesymbol">|</span>
							<span>
							<c:url value="/order-history/order/disputeOrder?orderCode=${orderData.code}" var="disputedetailsUrl" />
								<a class="tertiarybtn disputeOrder" id="disputeOrder" href="${disputedetailsUrl}">
									<spring:message code="orderDetailPage.orderData.disputeThisOrder"/>
								</a>
							</span>
					</c:if>
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

	<order:shipmentInfo orderData="${orderData}" isMddSite="${isMddSite}"/>

	<!--  Shipping Information  pop up Ends-->