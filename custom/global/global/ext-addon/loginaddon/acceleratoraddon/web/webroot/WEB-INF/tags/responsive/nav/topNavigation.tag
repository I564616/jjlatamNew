<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div class="nav_main navpart2" id="nav_main">
	<cms:pageSlot position="TopNavigation" var="component" element="ul">
		<cms:component component="${component}" />
	</cms:pageSlot>
</div>
<cms:pageSlot position="MiniCart" var="cart" limit="1">
	<cms:component component="${cart}"/>
</cms:pageSlot>