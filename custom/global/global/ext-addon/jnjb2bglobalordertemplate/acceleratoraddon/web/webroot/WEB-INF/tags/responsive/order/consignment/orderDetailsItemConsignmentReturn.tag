<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="entry" required="true"
	type="com.jnj.facades.data.JnjGTOrderEntryData"%>
<%@ attribute name="scheduleLine" required="false"
	type="com.jnj.facades.data.JnjDeliveryScheduleData"%>
<%@ attribute name="isSplitOrder" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="entryIndex" required="true" type="java.lang.String"%>
<%@ attribute name="schLineIndex" required="false"
	type="java.lang.String"%>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean"%>
<%@ attribute name="orderType" required="true" type="java.lang.String"%>
<%@ attribute name="displayDisputeOption" required="true"
	type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/common"%>



	<c:set
		value="${not (isMddSite && fn:contains('ZDELZRE', orderData.orderType))}"
		var="displayShipping" />
	<c:set value="${(empty entry.materialEntered) ? false : true}"
		var="isMaterialEntPresent" />
	<c:set value="${(empty entry.materialNumber) ? false : true}"
		var="isMaterialNumPresent" />
	<!--  AAOL-6138 changes start -->
	<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
	<!--  AAOL-6138 changes end -->

	<tr>
		<!-- to collecting product id for contract popup using class and id -->
			<!-- Adding for AAOL-3609 -->	
			
		<td class="orderHistoryList">${entryCounter.count +1}
			<c:if test="${entry.expandableSchedules}">
				<div class="expendOrderDetailArrowHistory rightArrow">+</div>
			</c:if>
		</td>
			<!-- Adding for AAOL-3609 -->	
		<td class="paddingtop10px">
			<div class="column2">
				<c:choose>
					<c:when test="${isMaterialNumPresent}">
						<div class="prodImage">
							<img src="${commonResourcePath}/images/prod_img.png"
								alt="Product Image" />
						</div>
					</c:when>
					<c:otherwise>
						<c:url value="${entry.product.url}" var="productUrl" />
					</c:otherwise>
				</c:choose>
				<div class="orderProdDesc left" >
					<c:choose>
						<c:when test="${isMaterialNumPresent}">
							<b><i>Product: <span class="strong">${entry.materialNumber}</span></i></b>
						</c:when>
						<c:otherwise>
							<input type="hidden"
								value="${entry.product.saleableInd && entry.salesRepDivisionCompatible}"
								class="saleableInd" id="${entry.product.code}" />
							<c:choose>
								<c:when test="${empty entry.product.url}">
									<h4 title="${entry.product.name}">${entry.product.name}</h4>
								</c:when>
								<c:otherwise>
									<!-- Adding for AAOL-3609 -->	
									<h4 title="${entry.product.name}">
										<p><a href="${productUrl}">${entry.product.name}</a></p>
									</h4>
										<!-- Adding for AAOL-3609 -->	
								</c:otherwise>
							</c:choose>
							<p class="jnjID">
								<spring:message code="orderDetailPage.orderData.jnjID" />
								<span class="strong">${entry.product.baseMaterialNumber}</span>
							</p>
							<%-- <span>
								 <c:url value="/order-history/order/disputeOrder?orderCode=${orderData.code}" var="disputedetailsUrl" />
									<a class="tertiarybtn disputeOrder" id="disputeOrder" href="${disputedetailsUrl}">
										<spring:message code="invoiceDetailPage.disputeItemsInvoice"/>
									</a> 
							</span> --%>
							<p>
								<c:url value="/order-history/order/disputeItem?orderCode=${orderData.code}&productCode=${entry.product.baseMaterialNumber} 
								&contractNum=${entry.contractNumber}&totalPrice=${entry.totalPrice.value}" var="disputeItemUrl" />
									<a href="${disputeItemUrl}" class="smallFont disputeItem" orderCode="${orderData.code}" productCode="${entry.product.baseMaterialNumber}" 
										contractNum="${entry.contractNumber}" totalPrice="${entry.totalPrice.value}" >
									<spring:message code="orderDetailPage.orderData.disputeThisItem" /></a>
								</p>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${isMddSite}">
							<!-- Adding for AAOL-3609 -->	
							<c:if test="${not empty entry.product.gtin}">
								<p>
									<span class="smallFont"> <spring:message
											code="orderDetailPage.orderData.gtin" /></span>
									${entry.product.gtin}
								</p>
							</c:if>
							<c:if test="${not empty entry.contractNumber}">
								<p>
									<span class="smallFont"><spring:message
											code="orderDetailPage.orderData.contract" /></span>
									${entry.contractNumber}
								</p>
							</c:if>
								<!-- Adding for AAOL-3609 -->	
								<!-- Adding for AAOL-3648 -->
							 <c:if test="${displayDisputeOption}">
								<p>
								<c:url value="/order-history/order/disputeItem?orderCode=${orderData.code}&productCode=${entry.product.baseMaterialNumber} 
								&contractNum=${entry.contractNumber}&totalPrice=${entry.totalPrice.value}" var="disputeItemUrl" />
									<a href="${disputeItemUrl}" class="smallFont disputeItem" orderCode="${orderData.code}" productCode="${entry.product.baseMaterialNumber}" 
										contractNum="${entry.contractNumber}" totalPrice="${entry.totalPrice.value}" >
									<spring:message code="orderDetailPage.orderData.disputeThisItem" /></a>
								</p>
							</c:if> 
							<!-- Adding for AAOL-3648 -->
							<c:if test="${not empty entry.hazmatInd}">
								<div>
									<p>Hazmat Code: ${entry.hazmatInd}
								</div>
							</c:if>
							<c:if test="${not empty entry.specialStockPartner}">
								<div>
									<p>Special Stock Partner: ${entry.specialStockPartner}
								</div>
							</c:if>
							</br>
							<c:if test="${not empty entry.lotNumber}">
								<p>
									<span class="smallFont">Lot Number:</span></br> <span
										class="txtFont">${entry.lotNumber}</span>
								</p>
							</c:if>


							<!-- Lot expiration removed as per JJEPIC-284 -->

							<%-- <c:if test="${not empty entry.lotComment}">
								<p>
									<span class="smallFont">Lot Comment:</span></br>
									<span class="txtFont">${entry.lotComment}</span>
								</p>
							</c:if> --%>
						</c:when>
						<c:otherwise>
							<p>
								<span class="smallFont"><spring:message
										code="product.detail.basic.upc" /></span> ${entry.product.upc}
							</p>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="orderProdDesc right">
					<c:choose>
						<c:when test="${isMddSite}">

							<c:if test="${not empty entry.priceOverrideReason}">
								<p>
									<span class="smallFont">Price Override Reason:</span></br><span
										class="txtFont">${entry.priceOverrideReason}</span>
								</p>
							</c:if>
							<c:if test="${not empty entry.approver}">
								<p>
									<span class="smallFont">Approver:</span></br> <span class="txtFont">${entry.approver}</span>
								</p>

							</c:if>
							<c:if test="${not empty entry.expectedShipDate and not entry.expandableSchedules}">
								<p>
									<span class="labelText">Estimated Ship Date:</span></br><span
										class="txtFont"><fmt:formatDate
											value="${entry.expectedShipDate}" pattern="${dateformat} " /></span>
								</p>
							</c:if>
							<c:if test="${not empty entry.shipmentLocation}">
								<p>
									<span class="labelText">Shipment Location:</span></br> <span
										class="txtFont">${entry.shipmentLocation}</span>
								</p>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:if test="${not empty entry.orderWeight}">
								<p>
								<div>
									<span class="labelText">Order Weight</span>
								</div>
								<div>
									<span>${entry.orderWeight}</span>
								</div>
								</p>
							</c:if>
							<c:if test="${not empty entry.cubicVolume}">
								<p>
								<div>
									<span class="labelText">Cubic Volume</span>
								</div>
								<div>
									<span>${entry.cubicVolume}</span>
								</div>
								</p>
							</c:if>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</td>
		<td class="text-nowrap"><c:if test="${not isMaterialNumPresent}">
													${entry.quantity} (${entry.product.numerator} &nbsp; ${entry.product.salesUnit})
													</c:if></td>


			<!-- Adding for AAOL-3609 -->	
		<c:if test="${displayShipping}">
			<td>
			<c:choose>
					<c:when test="${not empty entry.shippingMethod}">
						${entry.shippingMethod}
					</c:when>
					<c:otherwise>
						<span class="txtFont">&nbsp;</span>
					</c:otherwise>
				</c:choose></td>
		</c:if>
			<!-- Adding for AAOL-3609 -->	

		<td>
			<c:choose>
				<c:when test="${not empty entry.expectedDeliveryDate}">
					<c:choose>
						<c:when test="${'ZLZ' eq orderType}">
							<span class="txtFont">-</span>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${entry.expectedDeliveryDate}"
								pattern="${dateformat}" />
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<span class="txtFont">&nbsp;</span>
				</c:otherwise>

			</c:choose>
		</td>
			
				<!-- 	AAOL-3638 start -->
		<td class="text-nowrap paddingtop10px">
			<p>${entry.status}</p> 
			<c:if test="${isMddSite}">
				 <div class="smallFont floatRight">
				    <spring:message code="orderDetailPage.orderData.trackingHash" />
				 </div>
				<c:choose>
					<c:when test="${not empty orderData.shippingTrackingInfo }">
						<c:choose>
							<c:when
								test="${not empty orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}">
								<c:forEach
									items="${orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}"
									var="data">
									<c:choose>
										<c:when test="${not empty data.trackingId}">
											<c:choose>
												<c:when test="${data.trackingUrl eq '#'}">
													<p class="smallFont">${data.trackingId}</p>
												</c:when>
												<c:otherwise>
													<p class="smallFont">
														<a href="${data.trackingUrl}" target="_blank">${data.trackingId}</a>
													</p>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<p class="smallFont">
												<label:message
													messageCode="orderDetailPage.orderData.notAvailable" />
											</p>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<p class="smallFont">
									<label:message
										messageCode="orderDetailPage.orderData.notAvailable" />
								</p>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<p class="smallFont">
							<label:message
								messageCode="orderDetailPage.orderData.notAvailable" />
						</p>
					</c:otherwise>
				</c:choose>
			</c:if>
		</td>
		
		<!-- 	AAOL-3638 End -->
		
	

	</tr>





