
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<div id="changeAccountPopupContainer" style="display: none;">
	<div class="lightboxtemplate changeAccountBasketPopUp">
		<div id="templateorderdelete">
			<div class="temlpateOrderDelPage">
				<h2 class="arrowHeader">
					<spring:message code="template.removalOfTemplate.heading" />
				</h2>
				<p>
					<spring:message code="template.removalOfTemplate.alertMsg" />
					(${templateCode}) ?
				</p>
				<div class="buttonWrapper txtCenter">
					<a class="tertiarybtn marginRight deleteTempNo"
						href="javascript:;"><label:message
							messageCode="common.label.no" /></a>
					 <a class="tertiarybtn deleteTempYes"
						id="temp-templateordersuccess" href="javascript:;"><label:message
							messageCode="common.label.yes" /></a>
				</div>
			</div>
		</div>
	</div>
</div>
