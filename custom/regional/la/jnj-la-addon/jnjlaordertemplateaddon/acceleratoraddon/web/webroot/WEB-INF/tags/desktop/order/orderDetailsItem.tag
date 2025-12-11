<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="entry" required="true" type="com.jnj.facades.data.JnjGTOrderEntryData"%>
<%@ attribute name="scheduleLine" required="false" type="com.jnj.facades.data.JnjDeliveryScheduleData"%>
<%@ attribute name="isSplitOrder" required="true" type="java.lang.Boolean"%>
<%@ attribute name="entryIndex" required="true" type="java.lang.String"%>
<%@ attribute name="schLineIndex" required="false" type="java.lang.String"%>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean" %>
<%@ attribute name="orderType" required="true" type="java.lang.String" %>
<%@ attribute name="displayDisputeOption" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/desktop/common"%>
<div class="orderDetRow">
	<c:set value="${not (isMddSite && fn:contains('ZDELZRE', orderData.orderType))}" var="displayShipping" />
	<c:set value="${(empty entry.materialEntered) ? false : true}" var="isMaterialEntPresent" />
	<c:set value="${(empty entry.materialNumber) ? false : true}" var="isMaterialNumPresent" />
	<div class="column1">
		<div class="numValue">			
			<span class="sapOrderLIneNumber">${entry.sapOrderlineNumber}</span>			
			<%-- ADD FOR SCHEDULE LINES --%>
			<c:if test="${entry.expandableSchedules}">
				<div class="expendOrderDetailArrowHistory rightArrow">+</div>
			</c:if> 
		</div>
	</div>
	<c:choose>
		<c:when test="${isMaterialEntPresent}">
			<div class="prodNotFoundContainer">
			<label>
				Product ${entry.materialEntered} not found
			</label>
			</div>
		</c:when>
		<c:otherwise>
			<div class="column2">
				<c:choose>
					<c:when test="${isMaterialNumPresent}">
						<div class="prodImage">
							<img src="${commonResourcePath}/images/prod_img.png" alt="Product Image" />
						</div>
					</c:when>
					<c:otherwise>
						<c:url value="${entry.product.url}" var="productUrl"/>
						<div class="prodImage">
							<c:choose>
								<c:when test="${empty entry.product.url}">
									<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
								</c:when>
								<c:otherwise>
									<a href="${productUrl}">
										<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
									</a>	
								</c:otherwise>
							</c:choose>
						</div>
					</c:otherwise>
				</c:choose>
				<div class="orderProdDesc left">
					<c:choose>
						<c:when test="${isMaterialNumPresent}">
							<b><i>Product: <span class="strong">${entry.materialNumber}</span></i></b>
						</c:when>
						<c:otherwise>
							<input type="hidden" value="${entry.product.saleableInd && entry.salesRepDivisionCompatible}" class="saleableInd" 
								id="${entry.product.code}" />
							<c:choose>
								<c:when test="${empty entry.product.url}">
									<h4 title="${entry.product.name}">${entry.product.name}</h4>
								</c:when>
								<c:otherwise>
									<h4 title="${entry.product.name}"><a href="${productUrl}">${entry.product.name}</a></h4>
								</c:otherwise>
							</c:choose>
							<p class="jnjID">
								<spring:message code="orderDetailPage.orderData.jnjID" />
								<span class="strong">${entry.product.baseMaterialNumber}</span>
							</p>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${isMddSite}">
							<c:if test="${not empty entry.product.gtin}">
								<p>
									<span class="smallFont">
										<spring:message code="orderDetailPage.orderData.gtin" /></span>
									${entry.product.gtin}
								</p>
							</c:if>
							<c:if test="${not empty entry.contractNumber}">
								<p>
									<span class="smallFont"><spring:message code="orderDetailPage.orderData.contract" /></span>
									${entry.contractNumber}
								</p>
							</c:if>
							<c:if test="${displayDisputeOption}">
								<p>
									<a href="#" class="smallFont disputeItem" orderCode="${orderData.code}" productCode="${entry.product.baseMaterialNumber}" 
										contractNum="${entry.contractNumber}" totalLinePrice="${entry.totalPrice.value}"" >
									<spring:message code="orderDetailPage.orderData.disputeThisItem" /></a>
								</p>
							</c:if>
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
									<span class="smallFont">Lot Number:</span></br>
									<span class="txtFont">${entry.lotNumber}</span>
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
								<span class="smallFont"><spring:message code="product.detail.basic.upc" /></span>
								${entry.product.upc}
							</p>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="orderProdDesc right">
					<c:choose>
						<c:when test="${isMddSite}">

							 <c:if test="${not empty entry.priceOverrideReason}">
								<p>
									<span class="smallFont">Price Override Reason:</span></br>
									<span class="txtFont">${entry.priceOverrideReason}</span>
								</p>
							</c:if>
							<c:if test="${not empty entry.approver}">
								<p>
									<span class="smallFont">Approver:</span></br>
									<span class="txtFont">${entry.approver}</span>
								</p>

							</c:if> 
							<c:if test="${not empty entry.expectedShipDate and not entry.expandableSchedules}">
								<p>
									<span class="labelText">Estimated Ship Date:</span></br>
									<span class="txtFont"><fmt:formatDate value="${entry.expectedShipDate}" pattern="MM/dd/yyyy " /></span>
								</p>
							</c:if>
							<c:if test="${not empty entry.shipmentLocation}">
								<p>
									<span class="labelText">Shipment Location:</span></br>
									<span class="txtFont">${entry.shipmentLocation}</span>	
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
			<div class="column3">
				<div class="lbox">
					<p class="inputBox">${entry.quantity}</p>
					<c:if test="${not isMaterialNumPresent}">
						<p>
							<span class="descSmall block"> ${entry.product.deliveryUnit}
								(${entry.product.numerator} &nbsp; ${entry.product.salesUnit}) </span>
						</p>
					</c:if>
				</div>
			</div>
			<div class="column4">
				<div>
					<c:choose>
						<c:when test="${'ZLZ' eq orderType}">
							<span class="txtFont">-</span>					
						</c:when>
						<c:when test="${not empty entry.basePrice.formattedValue}">
							<span class="txtFont">${entry.basePrice.formattedValue}</span>
						</c:when>
						<c:otherwise>
							<span class="txtFont">&nbsp;</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="column5">
				<c:choose>
					<c:when test="${not empty entry.totalPrice.formattedValue}">
						<span class="txtFont priceList">${entry.totalPrice.formattedValue}</span>
					</c:when>
					<c:otherwise>
						<span class="txtFont">&nbsp;</span>
					</c:otherwise>
				</c:choose>
			</div>
			<c:if test="${displayShipping}">
				<div class="column6">
					<c:choose>


						<c:when test="${not empty entry.shippingMethod}">
							<span class="txtFont">${entry.shippingMethod}</span>
						</c:when>
						<c:otherwise>
							<span class="txtFont">&nbsp;</span>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<div class="column7">
				<c:choose>
					<c:when test="${not empty entry.expectedDeliveryDate}">
						<c:choose>
							<c:when test="${'ZLZ' eq orderType}">
								<span class="txtFont">-</span>					
							</c:when>
							<c:otherwise>

								<span class="txtFont"><fmt:formatDate value="${entry.expectedDeliveryDate}" pattern="MM/dd/yyyy" /></span>			
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<span class="txtFont">&nbsp;</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="column8">
				<p>${entry.status}</p>
				<br>
				<c:if test="${isMddSite}">
				 <p class="smallFont floatRight">
				    <spring:message code="orderDetailPage.orderData.trackingHash" />
				 </p><br>
					<c:choose>
						<c:when test="${not empty orderData.shippingTrackingInfo }">
							<c:choose>
								<c:when test="${not empty orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}">
									<c:forEach items="${orderData.shippingTrackingInfo[entry.sapOrderlineNumber]}" var="data">
										<c:choose>
										   <c:when test="${not empty data.trackingId}">
											   <c:choose>
											   		<c:when test="${data.trackingUrl eq '#'}">
													    <p class="smallFont">
															${data.trackingId}
														</p>
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
													<spring:message code="orderDetailPage.orderData.notAvailable"/>
												</p>
										   </c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<p class="smallFont">
										<spring:message code="orderDetailPage.orderData.notAvailable"/>
									</p>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<p class="smallFont">
								<spring:message code="orderDetailPage.orderData.notAvailable"/>
							</p>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
			<%-- ADD FOR SCHEDULE LINES --%>
		    <c:if test="${entry.expandableSchedules}">
		    	<commonTags:scheduleLines scheduleLineList="${entry.scheduleLines}" count="${count.count}" uom="${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})" productName="${entry.product.name}" jjId="${entry.product.code}"/>
		    </c:if>
    	</c:otherwise>
	</c:choose>
</div>
<c:if test="${not empty entry.rejectionReason}">
	<div class="registerError" style="padding:0px; text-align:left; display:block; clear:both;">
		<label class="error" >This product has been rejected because: ${entry.rejectionReason}</label>
	</div>
</c:if>
	