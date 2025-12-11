<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>

<c:if test="${not empty googleAnalyticsTrackingId}">

<script type="text/javascript">
	ga('create', '${googleAnalyticsTrackingId}', 'auto');      // Id of analytics account
	ga('require', 'ec'); 

	ga('set', '&cu', 'USD');              // Set tracker currency to USD
	/* For every ordered product in order */
	<c:forEach items="${orderData.entries}" var="entry">
		ga('ec:addProduct', {
		  'id': '${entry.product.code}', // Product ID
		  'name': '${entry.product.name}',// Product Name
		  'category': <c:choose>
			<c:when test="${not empty entry.product.categories}">
				'${entry.product.categories[fn:length(entry.product.categories) - 1].name}'
			</c:when>
			<c:otherwise>
				''
			</c:otherwise>
		</c:choose>, //Immediate Category of the product
		  'brand': '${entry.product.salesOrgCode}', // Franchise of Product
		  'variant': '${entry.product.deliveryUnit}', // Unit of measure
		  'price': '${entry.totalPrice.formattedValue}', // Total net value
		  'quantity': '${entry.quantity}' // Quantity of order line
		});
	</c:forEach>
	
	ga('ec:setAction', 'purchase', {
		  id: '${orderData.code}', // SAP order Number
		  affiliation: '<spring:message code="cart.common.orderType.${orderData.orderType}" />', // Order Type: as Standard, Delivered, Replinishment, No Charge, International, Return, Quote
		  revenue: '${orderData.totalPrice.formattedValue}', // Total Net value of order (Including Tax & shipping)
		  tax: '${orderData.totalTax.formattedValue}', // Total Tax of Order
		  shipping: '${orderData.totalDropShipFee.formattedValue}', // Total Shipping Charges of Order
		  coupon: '' // Blank value
		});
	
	ga('send', 'pageview');

</script>
</c:if>