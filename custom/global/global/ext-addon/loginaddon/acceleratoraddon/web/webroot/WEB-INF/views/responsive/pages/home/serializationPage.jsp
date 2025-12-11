<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="template"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/template"%>
<%@ taglib prefix="messageLabel"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="label"
	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="home"
	tagdir="/WEB-INF/tags/addons/loginaddon/responsive/home"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<template:page pageTitle="${pageTitle}">
		<div class="row jnj-body-padding shipmentContainer"
			id="jnj-body-content">
			<div class="col-lg-12 col-md-12">
				<div id="Orderhistorypage">
					<div class="row">
						<div class="col-lg-12 col-md-12">
							<ul class="breadcrumb">

							</ul>
							<div class="row content">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"><spring:message code="serialization.header.verifySerial" /></div>
							</div>
						</div>
					</div>
					<c:url value="/serialization"
							var="serialization" />
					<form:form method="post" action="${serialization}"
							id="verifySerialForm" commandName="jnjGTSerializationForm">
						<input type="hidden" id="originalFormAction" value="${serialization}" /> 
						<input id="downloadType" type="hidden" name="downloadType" path="downloadType"/>
					<div class="row jnj-panel mainbody-container">
						<div class="col-lg-12 col-md-12">
							<div class="row jnj-panel-body"
								style="padding: 16px 16px 10px 16px;">
								<div class="row marginbottomipad25px">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<div
											class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12 searchby">
											<label for="gtin" class="pull-left form-label prod-serial-label" ><spring:message code="serialization.gtin" /><span
												class="star">*</span></label>
											<div class="form-element prod-serial-elem">
													<input id="gtin" name="gtin" type="text" maxlength="40"
														class="required form-control verify-serial-input"
														placeholder="<spring:message code="serialization.gtin" />" value="${verifySerialForm.gtin}"
														data-msg-required="<spring:message code="serialization.required.gtin" />" />
													<div class="verifyDetailError"></div>
											</div>
										</div>
										<div class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<label for="serialNumber" class="pull-left form-label prod-serial-label"><spring:message code="serialization.serialNumber" /><span class="star">*</span>
											</label>
											<div class="form-element prod-serial-elem">
												<input id="serialNumber" name="serialNumber"
															maxlength="40" type="text" class="required form-control verify-serial-input"
															placeholder="<spring:message code="serialization.serialNumber" />" value="${verifySerialForm.serialNumber}"
															data-msg-required="<spring:message code="serialization.required.serialNumber" />" />
														<div class="verifyDetailError"></div>
											</div>
										</div>
										<div class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<label for="batchNumber" class="pull-left form-label prod-serial-label"><spring:message code="serialization.batchNumber" /><span class="star">*</span>
											</label>
											<div class="form-element prod-serial-elem">
												<input id="batchNumber" name="batchNumber" maxlength="40"
															type="text" class="required form-control verify-serial-input"
															placeholder="<spring:message code="serialization.batchNumber" />" value="${verifySerialForm.batchNumber}"
															data-msg-required="<spring:message code="serialization.required.batchNumber" />" />
														<div class="verifyDetailError"></div>
											</div>
										</div>
									</div> 
								</div>
								 <div class="row marginbottomipad25px">
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<div
											class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12 searchby">
											<label for="expiryYear" class="pull-left form-label prod-serial-date-label">
												<spring:message code="serialization.expiryYear" /><span class="star">*</span>
											</label>
											<div class="form-element prod-serial-elem-date">
													<input type="hidden" value="${verifySerialForm.expiryYear}" class="verifiedSerialSelectValue" id="verfiedSelectedYear"/>
													<select id="expiryYear" name="expiryYear"
															class="required only-form-control form-control date-select-css verify-serial-select"
															data-msg-required="<spring:message code="serialization.required.expiryYear" />">
															<option value="" disabled selected>Select</option>
															<c:forEach var="expiryYear" items="${expiryYearRange}">
																<option value=${expiryYear}>${expiryYear}</option>
															</c:forEach>
														</select>
														<div class="verifyDetailError"></div>
											</div>

											<!-- 	<div class="form-element prod-serial-date-format">( DD )</div> -->
										</div>
										
										<div for="expiryMonth" class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<label class="pull-left form-label prod-serial-date-label">
												<spring:message code="serialization.expiryMonth" /><span class="star">*</span>
											</label>
											<div class="form-element prod-serial-elem-date">
													<input type="hidden" value="${verifySerialForm.expiryMonth}" class="verifiedSerialSelectValue" id="verfiedSelectedMonth"/>
													<select id="expiryMonth" name="expiryMonth"
															class="required only-form-control form-control date-select-css verify-serial-select"
															data-msg-required="<spring:message code="serialization.required.expiryMonth"/>">
															<option value="" disabled selected>Select</option>
															<option value=<spring:message code="serialization.expiryMonth.monthJan.number"/>><spring:message code="serialization.expiryMonth.monthJan"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthFeb.number"/>><spring:message code="serialization.expiryMonth.monthFeb"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthMar.number"/>><spring:message code="serialization.expiryMonth.monthMar"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthApr.number"/>><spring:message code="serialization.expiryMonth.monthApr"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthMay.number"/>><spring:message code="serialization.expiryMonth.monthMay"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthJun.number"/>><spring:message code="serialization.expiryMonth.monthJun"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthJul.number"/>><spring:message code="serialization.expiryMonth.monthJul"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthAug.number"/>><spring:message code="serialization.expiryMonth.monthAug"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthSep.number"/>><spring:message code="serialization.expiryMonth.monthSep"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthOct.number"/>><spring:message code="serialization.expiryMonth.monthOct"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthNov.number"/>><spring:message code="serialization.expiryMonth.monthNov"/></option>
															<option value=<spring:message code="serialization.expiryMonth.monthDec.number"/>><spring:message code="serialization.expiryMonth.monthDec"/></option>
														</select>
														<div class="verifyDetailError"></div>
											</div>

										</div>
										<div class="form-group col-lg-4 col-md-4 col-sm-6 col-xs-12">
											<label for="expiryDay" class="pull-left form-label prod-serial-date-label">
												<spring:message code="serialization.expiryDay" /> </label>
											<div class="form-element prod-serial-elem-date">
													<input type="hidden" value="${verifySerialForm.expiryDay}" class="verifiedSerialSelectValue"/>
													<select id="expiryDay" name="expiryDay"
														class="only-form-control form-control date-select-css verify-serial-select">
														<option value="" disabled selected>Select</option>
													</select>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="row jnj-panel-footer">
							
									<div class="col-lg-6 col-md-4 col-sm-4 col-xs-12 link-holder">
										<input type="hidden" value="${serialVerified}" id="serialVerfied"/>
											<c:if test="${downLoadPdf}">

												<span id="external-links"><span class="link-txt boldtext"><spring:message
															code="serialization.download" /></span><a id="serializationPDF"
													href="#" class="tertiarybtn pdf"><spring:message
															code="serialization.download.pdf" /></a> | <a
													id="serializationExcel" href="#"
													class="tertiarybtn marginRight excel"><spring:message
															code="serialization.download.excel" /></a></span>
											</c:if>
									</div>
								
								<div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
									<div class="pull-right btn-mobile">
										<button id="verifySerialButton" type="button"
											class="btn btnclsactive searchbtn pull-right">
											<spring:message code="serialization.button.verifySerial" />
										</button>
										<button id="resetButton" type="button"
											class="btn btnclsnormal reset">
											<spring:message code="serialization.button.reset" />
										</button>
										<%-- <a id="resetButton" type="button" class="btn btnclsnormal reset" href=#><spring:message code="serialization.button.reset" /></a> --%>
									</div>
								</div>
							</div> 

						</div> 

					</div>
					</form:form>

				<div class="row">
					<div class="col-lg-12 col-md-12">
						<div
							class="hidden-xs hidden-sm jnj-panel-for-table mainbody-container">
							<table id="datatab-desktop"
								class="table table-bordered table-striped verify-serial-table">
								<thead>
									<tr>
										<th class="no-sort text-left text-uppercase result-head-label"
											colspan="2"><spring:message code="serialization.result" /></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><b><spring:message
													code="serialization.result.serialNumber" /></b></td>
										<td class="serial-result" id="serial-number-result">${responseData.serialNumber}</td>
									</tr>
									<tr>
										<td><b><spring:message
													code="serialization.result.status" /></b>
										</td>
										<c:choose>
											<c:when test="${serialVerified ne true}">
												<td class="serial-result notVerified" id="status-result">${responseData.status}
												</td>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${downLoadPdf}">
														<td class="serial-result known" id="status-result">
															<span class="glyphicon glyphicon-ok">
															</span>&nbsp;${responseData.status}
														</td>
													</c:when>
													<c:otherwise>											
														<td class="serial-result unknown" id="status-result">
															<span class="glyphicon glyphicon-ban-circle">
															</span>&nbsp;${responseData.status}
														</td>
													</c:otherwise>
												</c:choose>											
											</c:otherwise>
										</c:choose>
										
										
									</tr>
									<c:if test="${downLoadPdf ne true}">
										<tr>
											<td><b><spring:message
														code="serialization.result.reason" /></b></td>
											<td class="serial-result" id="reason-result">${responseData.reason}</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>