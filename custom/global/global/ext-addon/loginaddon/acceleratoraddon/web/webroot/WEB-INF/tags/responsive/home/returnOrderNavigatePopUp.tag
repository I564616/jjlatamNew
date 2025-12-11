<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="modal fade jnj-popup-container" id="startNewOrderpopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
	<form:form method="post" id="newOrderForm" action="javascript:;">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="homePage.newReturn.close"/></button>
				<h4 class="modal-title selectTitle"><spring:message code="homePage.newReturn.items"/></h4>
			</div>
			
			<div class="modal-body">
				<div><spring:message code="homePage.newReturn.shoppingcart"/></div>
				<div class="radio radio-info">
					<input type="radio" name="newOrder" id="keepItems" value="keepItems" checked="checked">
					<label for="radio3"> <spring:message code="homePage.newOrder.keepItems"/></label>
				</div>
				<div class="radio radio-info">
					<input type="radio" name="newOrder" id="deleteItems" value="deleteItems"
						checked> <label for="radio4"><spring:message code="homePage.newReturn.deleteitems"/></label>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<a href="#" class="pull-left canceltxt" data-dismiss="modal"><spring:message code="homePage.newReturn.cancel"/></a>
				<a type="button" href="#" class="btn btnclsactive leave-page-btn"
					id="leaveReturnOk"><spring:message code="homePage.newReturn.ok"/></a>
			</div>
		</div>
	</form:form>	
	</div>
</div>
