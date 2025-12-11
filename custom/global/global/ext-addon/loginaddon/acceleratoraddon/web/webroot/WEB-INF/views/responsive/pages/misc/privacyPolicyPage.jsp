<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade jnj-popup" id="privacypopupNew" role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
					  <button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
					  <h4 class="modal-title"><spring:message code="acclogin.page.privacy.policy"/></h4>
					</div>
					<div class="modal-body">
					<div>
					
						<cms:pageSlot position="MainBody" var="feature" limit="1">
							<cms:component component="${feature}"/>
						</cms:pageSlot>
					</div>
					
					</div>
				</div>
			</div>
		</div>

