	<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<%@ attribute name="orderId" required="false"
	type="java.lang.String"%>
	<div id="outerSaveAsTemplate" class="hide">
		<div class="lightboxtemplate" id="saveAsTemplate">

			<h2><spring:message code="cart.review.saveTemplate.saveAsTemplate"/></h2>
			<div class="sectionBlock body">
				<span class="block"> <label for="templateName"><spring:message code="cart.review.saveTemplate.templateName"/><span class="redStar">*</span>
				</label> <input type="text" id="templateName" />
				</span> <span class="block"> <label><input id="shareTemplate" type="checkbox" /><spring:message code="cart.review.saveTemplate.shareWithAccount"/></label>
				</span>
				
			<input type="hidden" id="orderCode" value="${orderId}">
			</div>
			<div>
				<div class="registerError"></div>
			</div>
			<div class="okecancel ttBlock">
				<form:form method="post" action="javascript:;">
					<div class='popupButtonWrapper txtRight'>
						<span class="floatLeft"><input type="button"
							onclick='parent.$.colorbox.close(); return false;'
							class="tertiarybtn" value ='<spring:message code="cart.review.saveTemplate.cancel"/>'/></span> <span><input type="button"
							class='secondarybtn createCartTemplate' value="OK" /></span>
					</div>
				</form:form>
			</div>
		</div>
	</div>