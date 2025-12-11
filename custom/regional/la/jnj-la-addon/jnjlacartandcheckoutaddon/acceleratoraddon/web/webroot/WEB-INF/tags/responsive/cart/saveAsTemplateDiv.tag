<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="orderId" required="false" type="java.lang.String"%>
<div id="outerSaveAsTemplate">
<div class="modal fade" id="save-as-tempalte-popup" role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.validate.templatePopupClose"/></button>
				<h4 class="modal-title popupTitle"><spring:message code="cart.review.saveTemplate.saveAsTemplate"/></h4>
			</div>
			<div class="modal-body">
				<div><label for="templateName"><spring:message code="cart.review.saveTemplate.templateName"/><sup class="sup-red"> <spring:message code="cart.validate.templatePopupstar"/></sup></label>
				</div>
				<div id="template-txt-field">
					<input type="text" class="form-control" id="templateName"></input>
				</div>
				<div class="checkbox checkbox-info checkboxmargin">
					<input id="share-account" class="styled" type="checkbox"> 
					<label for="share-account"><spring:message code="cart.review.saveTemplate.shareWithAccount"/></label>
				</div>
				<input type="hidden" id="orderCode" value="${orderId}">
			</div>
			<div>
				<div class="registerError" style="padding-left:18px"></div>
			</div>
			<div class="modal-footer ftrcls okecancel ttBlock">
				<form method="post" action="javascript:;">
					<a href="#" class="pull-left canceltxt" data-dismiss="modal" id="template-cancel-btn" ><spring:message code="cart.review.saveTemplate.cancel"/></a>
					<button type="button" class="btn btnclsactive createCartTemplatelatam" id="template-ok-btn"><spring:message code="cart.validate.templatePopupok"/></button>
				</form>
			</div>
		</div>
	</div>
</div>
</div>