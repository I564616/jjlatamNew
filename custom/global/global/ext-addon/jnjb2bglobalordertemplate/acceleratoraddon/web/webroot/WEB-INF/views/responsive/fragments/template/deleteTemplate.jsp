
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<div class="modal-header">
	<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close"/></button>
	<h4 class="modal-title"><spring:message code="template.removalOfTemplate.heading" /></h4>
</div>
<div class="modal-body">
	<div class="row">
		<div class=" col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<p ><spring:message code="template.removalOfTemplate.alertMsg" />
					</p>
		</div>
		<div class="text-center">
			<div style="display: inline-block">
				<button type="submit" data-dismiss="modal"
					class="deleteTempNo confrimAlert btnclsactive contactUsSubmitBtn "><label:message
							messageCode="common.label.no" /></button>
				<button type="submit"
					class="confrimAlert btnclsactive contactUsSubmitBtn deleteTempYes"><label:message
							messageCode="common.label.yes" /></button>
			</div>
		</div>
	</div>

</div>