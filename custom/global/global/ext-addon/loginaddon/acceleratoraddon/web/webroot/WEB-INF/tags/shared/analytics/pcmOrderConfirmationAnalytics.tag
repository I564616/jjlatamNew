<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>

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
		  'brand': '${entry.product.consumerSpecification.brand}' // Franchise of Product
		});
	</c:forEach>
	
	ga('ec:setAction', 'purchase', {
		  id: '${orderData.code}', // SAP order Number
		});
	
	ga('send', 'pageview');

</script>
</c:if>