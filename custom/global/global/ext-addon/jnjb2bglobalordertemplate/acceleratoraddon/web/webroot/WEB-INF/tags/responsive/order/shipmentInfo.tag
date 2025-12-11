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
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/jnjb2bglobalordertemplate/responsive/order"%>

<c:url value="/order-history/refineOrderDetail" var="refineUrl"></c:url>

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