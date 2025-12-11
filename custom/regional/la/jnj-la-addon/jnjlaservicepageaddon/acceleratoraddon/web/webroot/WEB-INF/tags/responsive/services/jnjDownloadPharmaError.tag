<%@ taglib prefix="messageLabel" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<div id='downloadPharmaErrorPopup' style="display:none;">
	<div class='lightboxtemplate'>
		<h2>
			<messageLabel:message messageCode="misc.services.downloadPharmaPriceList" />
		</h2>
		<div class="registerError downloadPharmaErrorText">
			<label class="error">
				<messageLabel:message messageCode="misc.services.pharmaPriceList.error.text"/>
			</label>
		</div>
	</div>
</div>
