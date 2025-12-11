<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade jnj-popup-container" id="newQuotepopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal">
					<spring:message code="homePage.newReturn.close" />
				</button>
				<h4 class="modal-title selectTitle">
					<h2>
						<spring:message code="homePage.newQuote" />
					</h2>
				</h4>
			</div>
			<div class="modal-body">
				<div>
					<spring:message code="homePage.newReturn.shoppingcart" />
				</div>
				<div class="radio radio-info">
					<input type="radio" name="radio2" id="keepItems" value="option1">
					<label for="keepItems"> <spring:message
							code="homePage.newQuote.keepItems" />
					</label>
				</div>
				<div class="radio radio-info">
					<input type="radio" name="radio2" id="deleteItems" value="option2"
						checked> <label for="deleteItems"> <spring:message
							code="homePage.newReturn.deleteitems" />
					</label>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<a href="#" class="pull-left canceltxt" data-dismiss="modal"><spring:message
						code="homePage.newReturn.cancel" /></a>
				<button type="button" class="btn btnclsactive" data-dismiss="modal"
					id="newQuoteOk">
					<spring:message code="homePage.newReturn.ok" />
				</button>
			</div>
		</div>
	</div>
</div>

