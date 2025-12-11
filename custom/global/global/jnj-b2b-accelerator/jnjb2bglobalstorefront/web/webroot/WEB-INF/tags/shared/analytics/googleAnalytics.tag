<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:if test="${not empty googleAnalyticsTrackingId}">


	<script type="text/javascript">
		window.dataLayer = window.dataLayer || [];
		var accNumber = '${googleUserAccountNumber}';
		var userSector = '${googleUserSector}';
		 
		if (null !== userSector && "" != userSector) {
			window.dataLayer.push({
				'userSector' : userSector
			});
		}
		if (null !== accNumber && "" != accNumber) {
			window.dataLayer.push({
				'userAccountNumber' : accNumber
			});
		}
	</script>
	<script>
		(function(w, d, s, l, i) {
			w[l] = w[l] || [];
			w[l].push({
				'gtm.start' : new Date().getTime(),
				event : 'gtm.js'
			});
			var f = d.getElementsByTagName(s)[0], j = d.createElement(s), dl = l != 'dataLayer' ? '&l='
					+ l
					: '';
			j.async = true;
			j.src = 'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
			f.parentNode.insertBefore(j, f);
		})(window, document, 'script', 'dataLayer', 'GTM-5H9CTSG');
	</script>
	<!-- End Google Tag Manager -->


</c:if>