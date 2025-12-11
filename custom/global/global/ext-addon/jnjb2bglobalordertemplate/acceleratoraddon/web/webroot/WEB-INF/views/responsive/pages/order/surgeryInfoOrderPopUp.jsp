<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal fade jnj-popup-container" id="orderHIstorySurgeryInfoPopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close"></spring:message></button>
				<h4 class="modal-title selectTitle">Surgery Info</h4>
			</div>
			
			<div class="modal-body">
				<div class="row surgery-info-row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label">
							<label> <spring:message
									code="surgeryInfoPopup.surgeryinfo.surgerySpeciality"></spring:message>
							</label>
						</div>
						<div>
							
							<input type="text" readonly="readonly" value="<spring:message code="surgeryInfoPopup.surgerySpeciality.${orderData.surgeryInfo.surgerySpecialty}"/>" class="selectpicker form-control">
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="levelsInstrumented"><spring:message code="surgeryInfoPopup.surgeryinfo.levelsInstrumented"></spring:message></label></div>
						<div>
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.levelsInstrumented.${orderData.surgeryInfo.levelsInstrumented}"/>" class="selectpicker form-control">
						</div>
						
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label><spring:message code="surgeryInfoPopup.surgeryinfo.orthobiologics"></spring:message></label></div>
						<div>
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.orthobiologics.${orderData.surgeryInfo.orthobiologics}"/>" class="selectpicker form-control">
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="pathology"><spring:message code="surgeryInfoPopup.surgeryinfo.pathology"></spring:message></label></div>
						<div>
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.pathology.${orderData.surgeryInfo.pathology}"/>" class="selectpicker form-control">
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label"><label for="surgicalApproach"><spring:message code="surgeryInfoPopup.surgeryinfo.surgicalApproach"></spring:message></label></div>
						<div class="surge-form-elem">
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.surgicalApproach.${orderData.surgeryInfo.surgicalApproach}"/>" class="selectpicker form-control">
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="zone"><spring:message code="surgeryInfoPopup.surgeryinfo.zone"></spring:message></label></div>
						<div class="surge-form-elem">
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.zone.${orderData.surgeryInfo.zone}"/>" class="selectpicker form-control">
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="interbody"><spring:message code="surgeryInfoPopup.surgeryinfo.interbody"></spring:message></label></div>
						<div class="surge-form-elem">
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.interbody.${orderData.surgeryInfo.interbody}"/>" class="selectpicker form-control">
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="caseNumber"><spring:message code="surgeryInfoPopup.surgeryinfo.caseNumber"></spring:message></label></div>
						<div class="surge-form-elem">
							<input type="text" id="caseNumber" name="caseNumber" class="selectpicker form-control" readonly="readonly" value="${orderData.surgeryInfo.caseNumber}"/>							
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label"><label for="interbodyFusion"><spring:message code="surgeryInfoPopup.surgeryinfo.interbodyFusion"></spring:message></label></div>
						<div class="surge-form-elem">
							
							<input type="text" readonly="readonly" value ="<spring:message code="surgeryInfoPopup.interbodyFusion.${orderData.surgeryInfo.interbodyFusion}"/>" class="selectpicker form-control">
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgery-info-col">
						<div class="surge-label">
						<label for="datePicker2">
						<spring:message code="surgeryInfoPopup.enterCaseDate.date"></spring:message></label></div>
						<div class="surge-form-elem">
							
							<input type="text" readonly="readonly" value ="${orderData.surgeryInfo.caseDate}" class="selectpicker form-control">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<a href="#" class="pull-left canceltxt" data-dismiss="modal"><spring:message code="cart.common.cancel"/></a>

					<span> <input type="submit" data-dismiss="modal"
						class="secondarybtn btn btnclsactive mobile-auto-btn" value="<spring:message code='cart.common.ok'/>"></span>
			</div>
		</div>
	</div>
</div>

	
