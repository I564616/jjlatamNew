<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="orderData" required="true" type="com.jnj.facades.data.JnjGTOrderData" %>
<%@ attribute name="isMddSite" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/jnjb2bbrowseandsearch/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/desktop/cart/standard"%>
<%@ taglib prefix="consAddress" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/desktop/cart/house"%>
<%@ taglib prefix="deliveredCart"	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/desktop/cart/delivered"%>
<c:url value="/order-history" var="orderHistoryURL"/>
<a href="${orderHistoryURL}" class="secondarybtn marTop10" ><spring:message code="orderDetailPage.back.button"/></a>
<div class="contractOrderMsg slideDownButton">
	<span class="iconExpandCollapse"></span> 
	<span>
		<c:if test="${not empty orderData.orderType}">
			<span class="labelText">
				<spring:message code="orderDetailPage.orderData.orderType"/>
			</span> 
			<span class="bold"><spring:message code="cart.common.orderType.${orderData.orderType}"/></span>
		</c:if> 
	</span> 
	<span>
		<span class="labelText">
			<spring:message code="orderDetailPage.orderData.orderNumber"/>
		</span>
	   	<span class="bold">
	   		${orderData.orderNumber}
	   	</span>
	</span>
	<span>
		<c:if test="${not empty orderData.soldToAccount}">
			<span class="labelText">
				<spring:message code="orderDetailPage.orderData.accountNumber"/>
			</span> 
			<span class="bold">${orderData.soldToAccount}</span>
		</c:if>
	</span> 
	<c:if test="${isMddSite && not empty orderData.gln}">
		<span>
			<span class="labelText">
				<spring:message code="orderDetailPage.orderData.gln"/>
			</span> 
			<span class="bold">${orderData.gln}</span>
		</span>
	</c:if>
</div>
<div class="prodDeliveryInfo sectionBlock hoConfirm mar0">
	<ul class="orderAddress">
		<li>
			<div>
				<div>
					<span class="labelText"><spring:message code="orderDetailPage.orderData.orderDate"/></span>
				</div>
				<div>
					<fmt:formatDate value="${orderData.created}" pattern="MM/dd/yyyy "/>
				</div>
			</div>
			<c:if test="${not empty orderData.statusDisplay}">
				<div>
					<div>
						<span class="labelText"><spring:message code="orderDetailPage.orderData.status"/></span>
					</div>
					<div>
						<span>${orderData.statusDisplay}</span>
					</div>
				</div>
			</c:if>
			<c:if test="${not empty orderData.orderedBy}">
				<div>
					<div>
						<span class="labelText"><spring:message code="orderDetailPage.orderData.orderBy"/></span>
					</div>
					<div>
						<span>${orderData.orderedBy}</span></br>
					</div>
				</div>
			</c:if>
			<c:if test="${not empty orderData.reasonCode}">
			<div>
				<div>
					<span class="labelText"><spring:message code="orderDetailPage.orderData.reasonCode"/></span>
				</div>
				<div>
					<span>${orderData.reasonCode} : <spring:message code="${orderData.reasonCode}" /></span></br>
				</div>
			</div>
			</c:if>
			<c:if test="${not empty orderData.attention}">
				<div>
					<div>
						<span class="labelText"><spring:message code="orderDetailPage.orderData.attention"/></span>
					</div>
					<div>
						<span>${orderData.attention}</span>
					</div>
				</div>
			</c:if>
			
			<!-- added by rajesh -->
	                 <!-- Defect #48 #49-->
			<div>
			<c:if test="${isMddSite && not empty orderData.surgeonName  && 'ZDEL' eq orderData.orderType}">
				<p>
					<spring:message code="cart.common.surgeonName"/>
				</p>
				<span>${orderData.surgeonName}</span>
				</c:if>
				
			</div>
			
			<c:if test="${isMddSite && orderData.displaySurgeonInfo  && 'ZDEL' eq orderData.orderType}">						
				<div>
				<a class="surgeryInfoPopup" href="javascript:;"> <spring:message code="cart.confirmation.surgeryInfoLink" />
				</a>
			</div> <deliveredCart:viewOrderSurgeryInfoDiv orderData="${orderData}" />
				
			</c:if>

		</li>
		<li>
			<c:if test="${not empty orderData.purchaseOrderNumber}">
				<div class="wordWrap">
					<div>
						<span class="labelText"><spring:message code="orderDetailPage.orderData.purchaseOrder"/></span>
					</div>
					<div>${orderData.purchaseOrderNumber}</div>
				</div>
			</c:if>
			<c:if test="${isMddSite}">
				<c:if test="${orderData.paymentType != null}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.paymentMethod"/></span>
						</div>
						<div>${orderData.paymentType.displayName}</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.paymentInfo}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.creditCardDetails"/></span>
						</div>
						<div>
							<span>${orderData.paymentInfo.cardType}</span></br>
							<span>${orderData.paymentInfo.cardNumber}</span></br>
							<span>${orderData.paymentInfo.expiryMonth}/${orderData.paymentInfo.expiryYear}</span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.dealerPoNumber}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.dealerPO"/></span>
						</div>
						<div>
							<span>${orderData.dealerPoNumber}</span>
						</div>
					</div>
				</c:if>
			
				<c:if test="${not empty orderData.dropShipAccount}">
					<div>
						<div>
							<span class="labelText">Drop Ship Account #</span>
						</div>
						<div>
							<span>${orderData.dropShipAccount}</span>
						</div>
					</div>
				</c:if>
				<c:if test="${'ZNC' eq orderData.orderType && not empty orderData.cordisHouseAccount}">
					<div>
						<div>
							<span class="labelText">Cordis House Account #</span>
						</div>
						<div>
							<span>${orderData.cordisHouseAccount}</span>
						</div>
					</div>
				</c:if>
			</c:if>
		</li>
		<li>
			<c:if test="${!isMddSite}">
				<c:if test="${not empty orderData.dropShipAccount}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.dropShipAccount"/></span>
						</div>
						<div>${orderData.dropShipAccount}</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.requestedDeliveryDate}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.reqDeliveryDate"/></span>
						</div>
						<div>
							<span>${orderData.requestedDeliveryDate}</span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.expectedDeliveryDate}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.expectedDeliveryDate"/></span>
						</div>
						<div>
							<span><fmt:formatDate value="${orderData.expectedDeliveryDate}" pattern="MM/dd/yyyy "/></span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.actualShipDate}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.actualShipDate"/></span>
						</div>
						<div>
							<span><fmt:formatDate value="${orderData.actualShipDate}" pattern="MM/dd/yyyy "/></span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.actualDeliveryDate}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.actualDeliveryDate"/></span>
						</div>
						<div>
							<span><fmt:formatDate value="${orderData.actualDeliveryDate}" pattern="MM/dd/yyyy "/></span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.shipmentLocation}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.shipmentLocation"/></span>
						</div>
						<div>
							<span>${orderData.shipmentLocation}</span>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty orderData.salesTerritory}">
					<div>
						<div>
							<span class="labelText"><spring:message code="orderDetailPage.orderData.salesTerritory"/></span>
						</div>
						<div>
							<span>${orderData.salesTerritory}</span>
						</div>
					</div>
				</c:if>
			</c:if>
		</li>
		<li>
			<c:if test="${not empty orderData.deliveryAddress}">
				<div class="labelText"><spring:message code="orderDetailPage.orderData.shipToAddress"/></div>
				<div class="minHeight">
					<c:choose>
						<c:when test="${isMddSite}">
							<address:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" />
						</c:when>
						<c:otherwise>
							<consAddress:deliveryAddress deliveryAddress="${orderData.deliveryAddress}" />
						</c:otherwise>
					</c:choose>
					<br />
				</div>
			</c:if>
			<c:if test="${not empty orderData.billingAddress}">
			<c:if test ="${isMddSite}">
				<div class="labelText"><spring:message code="orderDetailPage.orderData.billingNameAndAddress"/></div>
				</c:if>
				<c:if test ="${!isMddSite}">
				<div class="labelText"><spring:message code="cart.common.soldToAddress"/></div>
				</c:if>
				<div class="minHeight">
					<c:choose>
						<c:when test="${isMddSite}">
							<address:deliveryAddress deliveryAddress="${orderData.billingAddress}" companyName="${orderData.b2bUnitName}" />
						</c:when>
						<c:otherwise>
							<address:deliveryAddress deliveryAddress="${orderData.billingAddress}" />
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>
			<c:if test="${not empty orderData.stockPartnerNumber}">
				<div>
					<div>
						<span class="labelText">Special Stock Partner Number (Rep UCN) </span>
					</div>
					<div>
						<span>${orderData.stockPartnerNumber}</span>
					</div>
				</div>
			</c:if>
			<div>
				<c:url value="/order-history/order/invoiceDetail/${orderData.code}" var="invoiceDetailUrl" />
				<a href="${invoiceDetailUrl}"><spring:message code="orderDetailPage.orderData.viewInvoiceDetails"/></a>
			</div>
			<c:if test="${isMddSite}">
				<div>
					<a href="#" class="orderDetailShipmentInfo"><spring:message code="orderDetailPage.orderData.viewShipmentDetails"/></a>
				</div>
			</c:if>
		</li>
		<li class="last">
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
		</li>
	</ul>
</div>
<c:choose>
	<c:when test="${errorOccured ne '' && errorOccured ne null}">
		<div class="error" id="errorMessage" style="margin: 5px 0 10px 0;">
			<p>${errorOccured}</p>
		</div>
	</c:when>
</c:choose>
<div style="display:none;" id="orderDetailShipmentInfoPopUp">
	<div id="shippingDetail" class="lightboxtemplate">
		<h2>Shipping Detail</h2>
		<div class="sectionBlock body">	
			<div class="prodDeliveryInfo sectionBlock hoConfirm mar0">
	       		<ul class="orderAddress">
	       			<c:if test="${isMddSite}">
						<li>
		                	<div class="labelText"><spring:message code="orderDetailPage.orderData.packingList"/></div>
							<div>
								<c:choose>
								<c:when test="${not empty orderData.packingListDetails && packingListExpiryDate<=60}">
									
										<p>
											<c:forEach items="${orderData.packingListDetails}" var="packingListDetailEntry" varStatus="status">
												<c:choose>
													<c:when test="${'#' eq  packingListDetailEntry.value}">
														${packingListDetailEntry.key}<br>
													</c:when>
													<c:otherwise>
													<p>
														<a href="${JNJ_SITE_URL}${packingListDetailEntry.value}${packingListDetailEntry.key}" 
															onclick="window.open(this.href);return false;" >${packingListDetailEntry.key}</a>	
													</p>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</p>
									</c:when>
									<c:otherwise>
										<p>
											<spring:message code="orderDetailPage.orderData.notAvailable"/>
										</p>
									</c:otherwise>
								</c:choose>
							</div>
	                	</li>
	                </c:if>
	                <li>
	                	<c:if test="${not empty orderData.carrier}">
							<div class="labelText"><spring:message code="orderDetailPage.orderData.carrier"/></div>
							<div>
								<c:forEach items="${orderData.carrier}" var="carrier">
									<p class="textBlack">${carrier}</p>
								</c:forEach>
							</div>
						</c:if>
	                </li>
					<li>
	
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
	                </li>
	                <li class="last">
						<div class="labelText"><spring:message code="orderDetailPage.orderData.tracking"/></div>
						<c:choose>
							<c:when test="${empty orderData.trackingIdList}">
								<spring:message code="orderDetailPage.orderData.notAvailable"/>
							</c:when>
							<c:otherwise>
								<c:forEach items="${orderData.trackingIdList}" var="shippingTrackingInfo">
									<p>
										<a href="${shippingTrackingInfo.value}" target="_blank">${shippingTrackingInfo.key} </a>
									</p>
								</c:forEach>
							</c:otherwise>
						</c:choose>
	                </li>
	            </ul>
     		</div>
		</div>
	</div> 				
</div>