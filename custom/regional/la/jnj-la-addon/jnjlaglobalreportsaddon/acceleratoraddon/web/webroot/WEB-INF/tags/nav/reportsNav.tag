<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="selected" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="span-6 myAccountNavList">
	<cms:pageSlot position="SideContent" var="feature" element="ul" class="navList">
		<c:if test="${(feature.uid eq 'InventoryReportLinkComponent' && isInventoryAccessible eq true) || (feature.uid ne 'InventoryReportLinkComponent')}">
			<li class="${selected eq feature.uid ? 'active' : ''}">
				<div>
					<cms:component component="${feature}" />
					<span class="rightPointer"></span>
				</div>
			</li>
		</c:if>
	</cms:pageSlot>	
</div>