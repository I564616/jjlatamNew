<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
	
	
	
<c:url value="/order-history/submitDisputeOrdInq" var="url"/>
<form:form commandName="disputeOrderInquiryForm" id="disputeOrderInquiryForm"  enctype="multipart/form-data" name="disputeOrderInquiryForm" method="POST" 
			 action="${url}" >
<input type="hidden" value="${disputeOrderInquiryForm.orderCode}" name="orderCode" />
<input type="hidden" value="${disputeOrderInquiryForm.accountNumber}" name="accountNumber" >
<input type="hidden" value="${disputeOrderInquiryForm.purchaseOrderNumber}" name="purchaseOrderNumber" >	
	<div class="col-lg-12 col-md-12 mobile-no-pad disputeOrder">
								<div id="help-page">
									<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
									<div class="row">
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content"><spring:message code="orderDetailPage.orderData.disputeorder"/></div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
										<c:url value="/order-history/order/${orderData.code}" var="orderDetailUrl" />
											<input type="button" class="primarybtn submitDisputeOrder btn btnclsactive pull-right" value="<spring:message code='dispute.order.submit'/>" />
											<a href="${orderDetailUrl}" class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="dispute.order.cancel"/></a>	
											<div class="req-text"><span style="color:#b41601;"></span><spring:message code="dispute.order.required.fields"/></div>
										</div>
									</div>	
									
									<div class="boxshadow">
										<div class="row dispute-jnj-panel-header">											
											<div class="col-lg-3 col-md-8 col-sm-5 col-xs-11 form-label-style">
												<div class="dispute-label-head"><spring:message code="disputeOrderPage.orderData.orderNumber" /></div>
												<div id="dispute-num">${orderData.orderNumber}</div>
											</div>
											<div class="col-lg-3 col-md-8 col-sm-4 col-xs-11 form-label-style">
												<div class="dispute-label-head"><spring:message code="disputeOrderPage.orderData.orderType" /></div>
												<div class="dispute-head-content"><c:if
											test="${not empty orderData.orderType}">
											<spring:message
												code="cart.common.orderType.${orderData.orderType}" />
										</c:if></div>
											</div>
											<div class="col-lg-2 col-md-8 col-sm-3 col-xs-11 form-label-style">
												<div class="dispute-label-head"><spring:message code="disputeOrderPage.orderData.accountNumber" /></div>
												<div class="dispute-head-content"><c:if test="${not empty orderData.soldToAccount}">${orderData.soldToAccount}</c:if></div>
											</div>
											<div class="col-lg-2 col-md-8 col-sm-6 col-xs-11"></div>
										</div>
										
										<div class="row">
											<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"  id="accordion">
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse1" class="ref_no toggle-link panel-collapsed table-cell">
															<span class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
															<span class="table-cell"><spring:message code="dispute.order.overshipped.header" /></span>
														</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse1">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"><spring:message code="dispute.order.overshipped.account" /></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.accountNumber}</div>
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"><spring:message code="dispute.order.overshipped.po" /></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.purchaseOrderNumber}</div>
														</div>
														<div class="hidden-xs margintop10">
															<table id="datatab-desktop" class="table table-bordered table-striped">
																<thead>
																  <tr>
																	<th class="no-sort text-left text-uppercase"><spring:message code="dispute.order.overshipped.product" /><sup class="star">*</sup></th>
																	<th class="no-sort text-left text-uppercase"><spring:message code="dispute.order.overshipped.lot.separate"/></th>
																	<th class="no-sort text-uppercase"><spring:message code="dispute.order.overshipped.qty.received"/><sup class="star">*</sup></th>																	
																  </tr>
																</thead>
																<tbody>
																<c:forEach begin="0" end="4" varStatus="status"> 
																	<tr>
																		<td><input type="text" class="form-control prodCode medium productcode inputTxt" id="prodCode_desktop_${status.index}" index="${status.index}" name="prodCode"></td>
																		<td><input type="text" class="form-control lotNumbers big inputTxt" id="lotNumbers_desktop_${status.index}" index="${status.index}" name="lotNumbers"></td>
																		<td><input type="text" class="form-control prodQty small productcodeLast inputTxt" id="prodQty_desktop_${status.index}" index="${status.index}" name="prodQty"></td>	
																	<td style="display:none;" class="emptyQtyError error" id="emptyQtyError_desktop_${status.index}"><spring:message code="dispute.order.overshipped.qty.errormessage"/></td>
																	</tr>
																</c:forEach>
																	
																</tbody>
															</table>
															<div class="productCodeValidationError error" id="productcode-error" style="display:none;">
																<div class="registerError"><spring:message code="dispute.order.overshipped.product.errormessage"/></div>
															</div>
														</div>
														
														<!-- Table collapse for mobile device-->
							
														<div class="row visible-xs hidden-lg hidden-sm hidden-md">
															<div class="col-xs-12">
																<table id="datatab-mobile" class="table table-bordered table-striped">
																	<thead>
																		<tr>
																			<th class="no-sort text-left text-uppercase"></th>
																		</tr>
																	</thead>
																	<tbody>
																	<c:forEach begin="0" end="4" varStatus="status"> 
																		<tr>
																			<td class="text-left">
																			
																				<a href="#collapse-tab-${status.index}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed">																					
																					<span class="glyphicon glyphicon-plus"></span>
																					<span><spring:message code="orderDetailPage.orderData.productnumber"/><sup class="star">*</sup></span>
																				</a>
																				<input type="text" class="form-control prodCode medium table-po-inputbox" id="prodCode_mobile_${status.index}" index="${status.index}" name="prodCode">																		
																					
																				<div id="collapse-tab-${status.index}" class="panel-collapse collapse sub-inputs">
																					<p class="text-uppercase"><spring:message code="dispute.order.overshipped.lot.separate"/></p>
																					<input type="text" class="form-control lotNumbers big" id="lotNumbers_mobile_${status.index}" index="${status.index}" name="lotNumbers">
																					<p class="text-uppercase margintop15"><spring:message code="dispute.order.overshipped.qty.received"/><sup class="star">*</sup></p>
																					<input type="text" class="form-control prodQty small" id="prodQty_mobile_${status.index}" index="${status.index}" name="prodQty">																					
																				<div style="display:none;" class="emptyQtyError error" id="emptyQtyError_mobile_${status.index}"><spring:message code="dispute.order.overshipped.qty.errormessage"/></div>
																				</div>
																			
																			</td>
																		</tr>
																	</c:forEach>	
																	</tbody>
																</table>
																<div class="productCodeValidationError error" id="productcode-error" style="display:none;">
																<div class="registerError"><spring:message code="dispute.order.overshipped.product.errormessage"/></div>
															</div>
															</div>
														</div>	
														
														
														<!--Accordian Ends here -->
														
														<div><spring:message code="dispute.order.overshipped.note"/></div>
														<div class="margintop10"><spring:message code="dispute.order.overshipped.keepproduct.question"/></div>
														<div class="margintop10">
															<div class="radio radio-info radio-inline">
															<form:radiobutton path="keepProductsShipped" class="inputchkbox" id="productShipmentYes" value="true"/>
																<label for="dispute-yes">Yes</label>
															</div>
															<div class="radio radio-info radio-inline">
																<form:radiobutton path="keepProductsShipped" class="inputchkbox" id="productShipmentNo" value="false"/>
																<label for="dispute-no">No</label>
															</div>	
															<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.ponumber"/></div>
																<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<form:input path="newPONumber" class="form-control margintop inputTxt" id="poNumber" readonly="true"/>
																<span class="poNumberValidationError error form-label-style" style="display:none;">
																	<span class="registerError"><spring:message code="dispute.order.overshipped.newpo.errormessage.question"/></span>
																</span>
															</div>
														</div>
														</div>
													</div>	
												</div>
												
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse2" class="ref_no toggle-link panel-collapsed table-cell">
															<span class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
															<span class="table-cell"><spring:message code="dispute.order.dispute.fee.header"/></span>
														</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse disputeFee" id="collapse2">
														<div class="checkbox checkbox-info checkboxmargin manual-checkbox ">
															<input type="checkbox" class="styled inputchkbox" name="manual" id="manual" value="Manual"/>
 															<!--<input type="hidden" name="disputedFees" id="disputedFees"/>  -->
															<label for="manual"><spring:message code="dispute.order.dispute.option1"/></label>
														</div>
														<div class="checkbox checkbox-info checkboxmargin margintop10">
															<input type="checkbox"  class="styled inputchkbox" name="minimum" id="minimum" value="Minimum"/>
															<!--<input type="hidden" name="disputedFees" id="disputedFees"/>  -->
															<label for="Minimum"><spring:message code="dispute.order.dispute.option2"/></label>
														</div>
														<div class="checkbox checkbox-info checkboxmargin margintop10">
															<input type="checkbox" class="styled inputchkbox" name="expedited" id="expedited" value="Expedited"/>
 														<!--	<input type="hidden" name="disputedFees" id="disputedFees"/>  -->
															<label for="Expedited"><spring:message code="dispute.order.dispute.option3"/></label>
														</div>
														<div class="checkbox checkbox-info checkboxmargin margintop10">
															<input type="checkbox" class="styled inputchkbox" name="shipping" id="shipping" value="Shipping & Handling/Freight"/>
 														<!--	<input type="hidden" name="disputedFees" id="disputedFees"/>  -->
															<label for="shipping"><spring:message code="dispute.order.dispute.option4"/></label>
														</div>
														<div class="disputeFeeValidationError error" style="display:none;margin-top:10px">
															<div class="registerError"><spring:message code="dispute.order.dispute.fee.erromeessage"/></div>
														</div>
													</div>	
													
												</div>
												
												
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse3" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus help-accordian-icon"></span>Incorrect PO Number</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse3">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.po.number"/></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.purchaseOrderNumber}</div>
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.po.correct"/><sup class="star">*</sup></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
															<form:input path="correctPurchaseOrderNumber" id="correctPoNumber" class="form-control po-inputbox disputeFieldRequired number inputTxt" data-msg-required="Please enter PO Number!" />
															<div class="correctPONumValidationError error form-label-style" style="display:none;">
												    			<div class="registerError"><spring:message code="dispute.order.incorrect.po.errormessage"/></div>
												    		</div>
															</div>
														</div>
														
													</div>	
												</div>
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse4" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus help-accordian-icon"></span>Incorrect Address</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse4">
														<div><strong><spring:message code="dispute.order.incorrect.address.shipped.header"/></strong></div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.shipped.line1"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.shipToAddress.line1}</div>	
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.shipped.line2"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.shipToAddress.line2}</div>	
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.shipped.city"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.shipToAddress.town}</div>	
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.shipped.state"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.shipToAddress.region.name}</div>	
														</div>
														<div class="row margintop10">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.shipped.zip"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeOrderInquiryForm.shipToAddress.postalCode}</div>	
														</div>
														<div class="margintop20px"><strong><spring:message code="dispute.order.incorrect.address.correct.header"/></strong></div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.country"/><sup class="star">*</sup></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
																<div class="form-elemnt-inline">
																	<form:select id="shipCountry" name="shipCountry" class="widthSel required inputTxt" path="correctAddress.country.isocode" data-width="100%">
																		<form:option value=""><spring:message code="dispute.order.incorrect.address.country.select-option"/></form:option>
																		<form:options itemValue="isocode" itemLabel="name" items="${countries}"/>
																	</form:select>
																</div>	
																<span class="correctCountryValidationError error  form-elemnt-error" style="display:none;">
							        								<span class="registerError"><spring:message code="dispute.order.incorrect.address.correct.country.errormessage"/></span>
							       								 </span>
															</div>
															
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.line1"/><sup class="star">*</sup></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
																<form:input path="correctAddress.line1" class="form-control po-inputbox form-elemnt-inline inputTxt" id="addressLine1" />
																<span class="addLine1ValidationError error  form-elemnt-error" style="display:none;">
														        	<span class="registerError"><spring:message code="dispute.order.incorrect.address.correct.line1.errormessage"/></span>
														        </span>
															</div>
																
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.line2"/></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
															<form:input path="correctAddress.line2" class="form-control po-inputbox inputTxt" id="addressLine2" />
															</div>	
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.city"/><sup class="star">*</sup></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
															<form:input path="correctAddress.town" class="form-control po-inputbox form-elemnt-inline inputTxt" id="town" />
															<span class="cityValidationError error form-elemnt-error" style="display:none;">
													        	<span class="registerError"><spring:message code="dispute.order.incorrect.address.correct.city.errormessage"/></span>
													        </span>
															</div>
																
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.state"/><sup class="star">*</sup></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
															<div class="form-elemnt-inline">
																<form:select id="shipStates" name="shipStates" class="disputeFieldRequired inputTxt" path="correctAddress.region.isocode" data-width="100%">
						                    						<option value=""><spring:message code="dispute.order.incorrect.address.state.select-option"/></option>
						               							 </form:select>
						               							 </div>
						               							 <span class="stateValidationError error form-elemnt-error" style="display:none;">
						        								<span class="registerError"><spring:message code="dispute.order.incorrect.address.correct.state.errormessage"/></span>
						        							</span>
															</div>
															 
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="dispute.order.incorrect.address.correct.zip"/><sup class="star">*</sup></div>	
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
															<input id="postalCode" type="text" name="correctAddress.postalCode" class="form-control po-inputbox disputeFieldRequired number form-elemnt-inline inputTxt"/>
															<span class="zipCodeValidationError error form-elemnt-error" style="display:none;">
						        								<span class="registerError"><spring:message code="dispute.order.incorrect.address.correct.zip.errormessage"/></span>
						      							  </span>
															</div>	
															
														</div>
														
														<div class="registerError error" id="incorrect-add-err"><spring:message code="dispute.incorrect.address.close.resubmit"/></div>
													</div>	
												</div>
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse5" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus help-accordian-icon"></span>Dispute Tax</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse5">
														<div class="row">	
															<div class="col-xs-12 col-md-12 col-sm-12 col-lg-12">															
																  <div class="btn btnclsnormal btn-file dispute-upload-file">
												                <spring:message code="dispute.order.fileUpload"/><input id="taxExemptCertificate" type="file"  name="taxExemptCertificate"/>                 
												                </div> 
												                <span id="filename"></span>
																
															</div>															
														</div>
														<div><spring:message code="dispute.order.tax.attach"/><sup class="star">*</sup></div>
														<div class="taxCertiValidationError error" style="display:none; margin-top:10px">
															<div class="registerError"><spring:message code="dispute.order.tax.errormessage"/></div>
														</div>
													</div>	
													
												</div>												
											</div>	
										</div>	
									</div>
									<div class="row">										
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop15">
										<input type="button" class="primarybtn submitDisputeOrder btn btnclsactive pull-right" value="<spring:message code='dispute.order.submit'/>" />
											<a href="${orderDetailUrl}" class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="dispute.order.cancel"/></a>
										</div>
									</div>	
								</div>
							</div>
	</form:form>	
	
				