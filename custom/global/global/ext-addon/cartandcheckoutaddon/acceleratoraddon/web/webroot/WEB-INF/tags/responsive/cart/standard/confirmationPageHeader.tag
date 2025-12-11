<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="returnOrderConfirmation"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/returns/returnsConfirmationPageTags"%>
<%@ attribute name="currentPage" required="false"
	type="java.lang.String"%>
<%@ attribute name="orderData" required="true"
	type="com.jnj.facades.data.JnjGTOrderData"%>
<c:url value="/order-history/order/${orderData.code}"
	var="orderDetailURL" />

<returnOrderConfirmation:customerInfoStrip />

<div class="prodDeliveryInfo sectionBlock">
	<ul class="shippingAddress">
		<li>
			<div>
				<p>
					<spring:message code="cart.confirmation.orderNumber" />
				</p>
				<span><a href="${orderDetailURL}">
						${orderData.orderNumber}</a></span>
			</div>
			<div class="wordWrap">
				<p>
					<spring:message code="cart.common.purchaseOrder" />
				</p>
				<span>${orderData.purchaseOrderNumber}</span>
			</div>
			<div>
			 <c:if test="${not empty orderData.shipToAccount}">
				<p>
					<spring:message code="cart.common.ShipToAcc"/>
				</p>
				</c:if>
				<span>${orderData.shipToAccount}</span>
			</div>
			<div>
			 <c:if test="${not empty orderData.distributorPONumber}">
				<p>
					<spring:message code="cart.common.disributorPO"/>
				</p>
				</c:if>
				<span> ${orderData.distributorPONumber}</span>
			</div>
		</li>

		<li>
			<div>
			 <c:if test="${not empty orderData.deliveryAddress}">
				<p>
					<spring:message
						code="orderDetailPage.orderData.shipToAddress"/>
				</p>
				</c:if>
				<div id="deliveryAddressTag"><cart:deliveryAddress deliveryAddress="${orderData.deliveryAddress}"/></div>
			</div>
			<div>
			 <c:if test="${not empty orderData.attention}">
				<p>
					<spring:message code="cart.common.attention"/>
				</p>
				</c:if>
				<span>${orderData.attention}</span>
			</div>
			<div>
			 <c:if test="${not empty orderData.spineSalesRepUCN}">
				<p>
					<spring:message code="cart.common.spineSales"/>
				</p>
				</c:if>
				<span><spring:message
						code="${orderData.spineSalesRepUCN}" /></span>
			</div>
		</li>

		<li>
			<div>
			 <c:if test="${not empty orderData.billingAddress}">
				<p>
					<spring:message
						code="orderDetailPage.orderData.billingNameAndAddress"/>
				</p>
				</c:if>
				
			<div  class="minHeight"><cart:deliveryAddress deliveryAddress="${orderData.billingAddress}" companyName="${orderData.b2bUnitName}" /></div>	
			</div>
			<div>
			 <c:if test="${not empty orderData.spineSalesRepUCN}">
				<p>
					<spring:message code="cart.common.surgeonName"/>
				</p>
				</c:if>
				<span><spring:message code="${orderData.surgeonName}" /></span>
			</div>

		</li>

		<!-- Start  : Payment Info block for cart validation/Checkout page -->
		<c:if
			test="${currentPage eq 'cartValidationPage' || currentPage eq 'cartCheckoutPage' || currentPage eq 'orderConfirmationPage'}">
			<li class="last"><p><spring:message
					code="cart.common.paymentMethod" /></p> <c:choose>
					<c:when test="${not empty orderData.paymentInfo}">
						<!-- Start  : Payment Info block for cart validation/Checkout page -->
						<div>
							<span><spring:message
									code="cart.review.orderInfo.creditOrder" /></span>
							<div>
								<p>
									<spring:message code="cart.review.orderInfo.creditOrder" />
									&amp;
									<spring:message code="cart.review.orderInfo.expiration" />
								</p>
								<span>${orderData.paymentInfo.cardType}</span><br><span> ${orderData.paymentInfo.cardNumber} <spring:message
										code="cart.review.orderInfo.exp" />
									${orderData.paymentInfo.expiryMonth}/${orderData.paymentInfo.expiryYear}
								</span>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div>
							<span><spring:message
									code="cart.review.orderInfo.purchaseOrder" /></span>
						</div>
					</c:otherwise>
				</c:choose></li>


		</c:if>
		<!-- End : Payment Info block for cart validation/Checkout page -->
	</ul>

</div>