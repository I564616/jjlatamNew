	<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ attribute name="orderId" required="false"
	type="java.lang.String"%>
	<div id="enterOneTimeShippingAddress" class="hide">
		<div class="lightboxtemplate" id="saveAsTemplate">

			<h2><spring:message code="cart.common.enterOneTimeShipToAddress"/></h2>
			<div class="sectionBlock body">
				<span class="block"> <label for="templateName"><spring:message code="cart.review.saveTemplate.templateName"/><span class="redStar">*</span>
				</label> <input type="text" id="templateName" />
				</span> <span class="block"> <label><input type="checkbox" /><spring:message code="cart.review.saveTemplate.shareWithAccount"/></label>
				</span>
				
			<input type="hidden" id="orderCode" value="${orderId}">
			</div>
			<div class="okecancel ttBlock">
				<form method="post" action="javascript:;">
					<div class='popupButtonWrapper txtRight'>
						<span class="floatLeft"><a
							onclick='parent.$.colorbox.close(); return false;' href="#"
							class="tertiarybtn"><spring:message code="cart.review.saveTemplate.cancel"/></a></span> <span><input type="button"
							class='secondarybtn createCartTemplate' value="OK" /></span>
					</div>
				</form>
			</div>
		</div>
	</div>