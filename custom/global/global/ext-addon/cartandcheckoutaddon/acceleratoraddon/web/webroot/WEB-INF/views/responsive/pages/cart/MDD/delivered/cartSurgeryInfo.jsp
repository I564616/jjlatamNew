<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/cart/saveSurgeryInfo" var="saveSurgeryInfoURL" />


<div class="modal fade jnj-popup-container" id="cartSurgeryInfoPopup"
	role="dialog" data-firstLogin='true'>
	<div class="modal-dialog modalcls">
		<div class="modal-content popup">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code='popup.close'/></button>
				<h4 class="modal-title selectTitle"><spring:message code='cart.confirmation.surgeryInfoLink'/></h4>
			</div>
			
			<div class="modal-body">
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label">
							<label> <spring:message
									code="surgeryInfoPopup.surgeryinfo.surgerySpeciality"></spring:message>
							</label>
						</div>
						<div>
							<%-- <select id="surgerySpecialty" class="selectpicker" name="surgerySpecialty"
								data-width="100%">
								<c:forEach items="${surgeryInfoData.surgerySpecialty}"
									var="surgerySpecialityId">
									<option value="${surgerySpecialityId}"
										${surgerySpecialityId == savedSurgeryInfo.surgerySpecialty ? 'selected="selected"' : ''}><spring:message
											code="surgeryInfoPopup.surgerySpeciality.${surgerySpecialityId}" /></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${cartData.surgeryInfo.surgerySpecialty}" class="selectpicker">
						</div>
					</div>
					<div class="col-lg-6">
						<div class="surge-label"><label for="levelsInstrumented"><spring:message code="surgeryInfoPopup.surgeryinfo.levelsInstrumented"></spring:message></label></div>
						<div>
							<%-- <select name="levelsInstrumented" class="selectpicker" id="levelsInstrumented" data-width="100%">
								<c:forEach items="${surgeryInfoData.levelsInstrumented}"
									var="levelsInstrumentedId">
									<option value="${levelsInstrumentedId}" ${levelsInstrumentedId == savedSurgeryInfo.levelsInstrumented ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.levelsInstrumented.${levelsInstrumentedId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.levelsInstrumented}" class="selectpicker">
						</div>
						
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label"><label><spring:message code="surgeryInfoPopup.surgeryinfo.orthobiologics"></spring:message></label></div>
						<div>
							<%-- <select id="orthobiologics" class="selectpicker" name="orthobiologics" data-width="100%">
								<c:forEach items="${surgeryInfoData.orthobiologics}"
									var="orthobiologicsId">
									<option value="${orthobiologicsId}" ${orthobiologicsId == savedSurgeryInfo.orthobiologics ? 'selected="selected"' : ''}><spring:message
									 code="surgeryInfoPopup.orthobiologics.${orthobiologicsId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.orthobiologics}" class="selectpicker">
						</div>
					</div>
					<div class="col-lg-6">
						<div class="surge-label"><label for="pathology"><spring:message code="surgeryInfoPopup.surgeryinfo.pathology"></spring:message></label></div>
						<div>
							<%-- <select id="pathology" class="selectpicker" name="pathology" data-width="100%">
								<c:forEach items="${surgeryInfoData.pathology}"
									var="pathologyId">
									<option value="${pathologyId}" ${ pathologyId== savedSurgeryInfo.pathology ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.pathology.${pathologyId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.pathology}" class="selectpicker">
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label"><label for="surgicalApproach"><spring:message code="surgeryInfoPopup.surgeryinfo.surgicalApproach"></spring:message></label></div>
						<div class="surge-form-elem">
							<%-- <select id="surgicalApproach"  class="selectpicker"name="surgicalApproach" data-width="100%">
								<c:forEach items="${surgeryInfoData.surgicalApproach}"
									var="surgicalApproachId">
									<option value="${surgicalApproachId}" ${surgicalApproachId == savedSurgeryInfo.surgicalApproach ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.surgicalApproach.${surgicalApproachId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.surgicalApproach}" class="selectpicker">
						</div>
					</div>
					<div class="col-lg-6">
						<div class="surge-label"><label for="zone"><spring:message code="surgeryInfoPopup.surgeryinfo.zone"></spring:message></label></div>
						<div class="surge-form-elem">
							<%-- <select data-width="100%" class="selectpicker" id="Zone" name="Zone">
								<c:forEach items="${surgeryInfoData.zone}" var="zoneId">
									<option value="${zoneId}" ${ zoneId == savedSurgeryInfo.zone ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.zone.${zoneId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.zone}" class="selectpicker">
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label"><label for="interbody"><spring:message code="surgeryInfoPopup.surgeryinfo.interbody"></spring:message></label></div>
						<div class="surge-form-elem">
							<%-- <select id="interbody" class="selectpicker" name="interbody" data-width="100%">
								<c:forEach items="${surgeryInfoData.interbody}"
									var="interbodyId">
									<option value="${interbodyId}" ${ interbodyId == savedSurgeryInfo.interbody ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.interbody.${interbodyId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.interbody}" class="selectpicker">
						</div>
					</div>
					<div class="col-lg-6">
						<div class="surge-label"><label for="caseNumber"><spring:message code="surgeryInfoPopup.surgeryinfo.caseNumber"></spring:message></label></div>
						<div class="surge-form-elem">
							<input type="text" id="caseNumber" name="caseNumber" readonly="readonly" class="form-control" value="${savedSurgeryInfo.caseNumber}"/>							
						</div>
					</div>
				</div>
				<div class="row surgery-info-row">
					<div class="col-lg-6">
						<div class="surge-label"><label for="interbodyFusion"><spring:message code="surgeryInfoPopup.surgeryinfo.interbodyFusion"></spring:message></label></div>
						<div class="surge-form-elem">
							<%-- <select data-width="100%" class="selectpicker" id="interbodyFusion" name="interbodyFusion">
								<c:forEach items="${surgeryInfoData.interbodyFusion}"
									var="interbodyFusionId">
									<option value="${interbodyFusionId}" ${interbodyFusionId == savedSurgeryInfo.interbodyFusion ? 'selected="selected"' : ''}><spring:message code="surgeryInfoPopup.interbodyFusion.${interbodyFusionId}"/></option>
								</c:forEach>
							</select> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.interbodyFusion}" class="selectpicker">
						</div>
					</div>
					<div class="col-lg-6">
						<div class="surge-label">
						<label for="datePicker2">
						<spring:message code="surgeryInfoPopup.enterCaseDate.date"></spring:message></label></div>
						<div class="surge-form-elem">
							<%-- <div class="input-group">
								<input id="datePicker2" name="caseDate"
											placeholder="<spring:message code='cart.common.selectDate'/>" class="date-picker form-control"
											type="text" value="${savedSurgeryInfo.caseDate}"> 
											<label for="datePicker2" class="input-group-addon btn"><span
											class="glyphicon glyphicon-calendar"></span> </label>	
							</div> --%>
							<input type="text" readonly="readonly" value ="${savedSurgeryInfo.caseDate}" class="selectpicker">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer ftrcls">
				<a href="#" class="pull-left canceltxt"><spring:message code='cart.common.cancel'/></a>
<!-- 				<button type="button" class="btn btnclsactive" data-dismiss="modal" -->
<!-- 					id="accept-btn">OK</button> -->
					<span> <input type="submit"
						class="secondarybtn btn btnclsactive" value="<spring:message code='cart.common.ok'/>" /></span>
			</div>
		</div>
	</div>
</div>

