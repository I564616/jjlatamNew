<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="commonTags"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>

	
<%@ attribute name="currentPage" required="false"
	type="java.lang.String"%>

<!-- This logic is used to make text boxes hidden in case of page is not cart page. -->
<c:if test="${currentPage ne 'cartPage'}">
	<c:set var="addOnVisibilityCss" value="disableTextbox" />
	<c:set var="disabled" value="disabled" />
</c:if>

<commonTags:customerInfoStrip />

<div class="prodDeliveryInfo sectionBlock">
	<ul class="shippingAddress">
		<li>
			<div class="wordWrap">
				<div>
					<label for="purchOrder"><spring:message
							code="cart.common.purchaseOrder" /></label>
				</div>
				<div>
					<span>${cartData.purchaseOrderNumber}</span>
				</div>
			</div>
			<div class="${hiddenFields.distPurOrder} marTop55">
				<div>
				<c:if test="${not empty cartData.distributorPONumber}">
					<label for="distPurOrder"><spring:message
							code="cart.common.disributorPO"/></label>
							</c:if>
				</div>
				<div>
					<span><input type="text" id="distPurOrder"
						class="${addOnVisibilityCss}" ${disabled}
						value="${cartData.distributorPONumber}"></span>
				</div>
				<div class="">
					<div class="registerError"></div>
				</div>
			</div>
		</li>

		<li>
			<div class="txtFont ">
				<spring:message code="cart.common.ShipToAdd" />
			</div>
			<div id="deliveryAddressTag" class="minHeight">
				<cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}" />
			</div>
			<div>
				<div>
				<c:if test="${not empty cartData.attention}">
					<label for="attention"><spring:message
							code="cart.common.attention"/> </label>
							</c:if>
				</div>
				<div>
					<span>${cartData.attention}</span>
				</div>
			</div>
		</li>
		<li>
			<div class="txtFont">
				<spring:message code="cart.common.BillingAdd" />
			</div>
			<div class="minHeight">
				<cart:deliveryAddress deliveryAddress="${cartData.billingAddress}" companyName="${cartData.b2bUnitName}" />								
			</div>
			<div class="${hiddenFields.enterDropShip} marTop23">
				<div>
				<c:if test="${not empty cartData.dropShipAccount}">
					<label for="dropShip"><spring:message
							code="cart.common.dropShipAcc"/></label>
							</c:if>
				</div>
				<div>${cartData.dropShipAccount}</div>
				<div>
					<div class="registerError"></div>
				</div>
			</div>
		</li>

		<li class="last"><span class="txtFont"><spring:message
					code="cart.common.paymentMethod" /></span> <c:choose>
				<c:when test="${not empty cartData.paymentInfo}">
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
							<span>${cartData.paymentInfo.cardType}</span><br> <span>****
								**** **** ${cartData.paymentInfo.cardNumber}
								<spring:message code="cart.review.orderInfo.exp" />
								${cartData.paymentInfo.expiryMonth}/${cartData.paymentInfo.expiryYear}
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
			</c:choose> <!-- End : Payment Info block for cart validation/Checkout page -->
		</li>

	</ul>
</div>