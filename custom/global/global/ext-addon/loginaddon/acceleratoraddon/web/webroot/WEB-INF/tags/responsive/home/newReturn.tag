<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <div class="hide" id="newReturn">
	<div class="lightboxtemplate">
		<h2><spring:message code="homePage.newReturn.items"/></h2>
		<form method="post" id="newReturnForm" action="javascript:;">
			<div class="sectionBlock body">
				<div id="checkPrivacyPolicyBlock">
					<p class="marBott10"><spring:message code="homePage.newReturn.shoppingcart"/>
						</p>
					<div>
						<span><input type="radio" name="orderReturn" checked="checked"
							value="keepItems" id="keepItems" /></span> <label for="keepItems">
							<spring:message code="homePage.newReturn.additems"/></label>
					</div>
					<div>
						<span><input type="radio" name="orderReturn"
							value="deleteItems" id="deleteItems" /></span> <label for="deleteItems"><spring:message code="homePage.newReturn.deleteitems"/>
							</label>
					</div>
				</div>
			</div>
			<div class='popupButtonWrapper txtRight'>
				<span class="floatLeft"><a
					onclick='parent.$.colorbox.close(); return false;' href="#"
					class="tertiarybtn"><spring:message code="homePage.newReturn.cancel"/></a></span><span> <a href="#"
					id="newReturnOk" class='primarybtn'> OK </a></span>
			</div>
		</form>
	</div>
</div> --%>

<div class="modal fade jnj-popup-container" id="newReturnpopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal">
					<spring:message code="homePage.newReturn.close" />
				</button>
				<h4 class="modal-title selectTitle">
					<h2>
						<spring:message code="homePage.newReturn.items" />
					</h2>
				</h4>
			</div>
			<div class="modal-body">
				<div>
					<spring:message code="homePage.newReturn.shoppingcart" />
				</div>
					<div class="radio radio-info">
					<input type="radio" name="newReturn" id="returnkeepItems" value="keepItems" checked>
					<label for="radio3"><spring:message
							code="homePage.newReturn.additems" /></label>
				</div>
				<div class="radio radio-info">
					<input type="radio" name="newReturn" id="returndeleteItems" value="deleteItems"
						 > <label for="radio4"><spring:message
							code="homePage.newReturn.deleteitems" /></label>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<a href="#" class="pull-left canceltxt" data-dismiss="modal"><spring:message
						code="homePage.newReturn.cancel" /></a>
				<button type="button" class="btn btnclsactive" data-dismiss="modal"
					id="newReturnOk">
					<spring:message code="homePage.newReturn.ok" />
				</button>
			</div>
		</div>
	</div>
</div>

