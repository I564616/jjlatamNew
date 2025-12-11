<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<c:url value="/order-history/submitDisputeItemInq" var="url"/>
<form:form commandName="disputeItemInquiryForm" id="disputeItemInquiryForm" name="disputeItemInquiryForm" method="POST" action="${url}" >
<input type="hidden" value="${disputeItemInquiryForm.accountNumber}" name="accountNumber" >
<input type="hidden" value="${disputeItemInquiryForm.purchaseOrderNumber}" name="purchaseOrderNumber" >
<input type="hidden" value="${disputeItemInquiryForm.orderCode}" name="orderCode" />
			<div class="col-lg-12 col-md-12 mobile-no-pad disputeOrder" id="accordian">
								<div id="help-page">
										<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
									  <div class="row">
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 headingTxt content"><spring:message code="disputeItem.heading.text"/>
											<div class="heading-note" style="font-size:18px !important"><spring:message code="dispute.order.select.appropriate"/></div>
										</div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 margintop15">
											<c:url value="/order-history/order/${disputeItemInquiryForm.orderCode}" var="orderDetailUrl" />
											<input type="button"  class="primarybtn btn btnclsactive pull-right submitDisputeItem" value="Submit Inquiry" />
											<a href="${orderDetailUrl}" class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="dispute.order.cancel"/></a>	
											<div class="req-text">(<span style="color:#b41601;">*</span>) - Required fields</div>
										</div>
									</div>	
									<div class="boxshadow">
									
										<div class="row">
											<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="accordion">
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse11" class="ref_no toggle-link panel-collapsed table-cell">
															<span class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
															<span class="table-cell"><spring:message code="disputeItem.text.heading"/></span>
														</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse11">
														<div class="row">
															   <div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.pricingdispute.TotalDisputed"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="totalDisputed" type="text" class="form-control margintop inputTxt" path="totalDisputedAmount" data-msg-required="Please enter total Disputed!"/>
															</div>
															<div class="disputedAmountValidationError error form-elemnt-error" style="display:none;">
															<div class="registerError"><spring:message code="disputeItem.pricingdispute.totalDisputed.error"/></div>
															</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style">
					    										<div for="priceShouldBe"><spring:message code="disputeItem.pricingdispute.text"/><span class="redStar">*</span></div>
					    									</div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<form:input id="expectedPrice" type="text" class="form-control margintop inputTxt" path="expectedPrice" data-msg-required="Please enter expected price!"/>
															</div>
															<div class="expectedAmtValidationError error form-elemnt-error" style="display:none;">
															<div class="registerError"><spring:message code="disputeItem.pricingdispute.expectedprice.error"/></div>
														</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.pricingdispute.ContractNumber"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input type="text" class="form-control margintop inputTxt" path="contractNumber"/>
															</div>
														</div>	
													</div>	
												</div>
												
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse12" class="ref_no toggle-link panel-collapsed table-cell">
															<span class="glyphicon glyphicon-plus help-accordian-icon table-cell"></span>
															<span class="table-cell"><spring:message code="disputeItem.Shortshipped.text"/></span>
														</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse12">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.Shortshipped.productcode"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="shortShippedProductCode" class="form-control margintop inputTxt" type="text" path="shortShippedProductCode" />
															</div>
															<div class="shortShippedProductCodeValidationError error  form-elemnt-error" style="display:none;">
														        	<div class="registerError"><spring:message code="disputeItem.OverShipment.productcode.msg"/></div>
														        </div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.Shortshipped.QuantityOrdered"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<form:input id="shortShippedOrderedQuantity" class="form-control margintop small inputTxt" type="text" path="shortShippedOrderedQuantity" />
															</div>	
															<div class="shortShippedOrdQtyValidationError error  form-elemnt-error" style="display:none;">
														        	<div class="registerError"><spring:message code="disputeItem.Shortshipped.qtyordered.msg"/></div>
														        </div>
															
														</div>
														
														
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.Shortshipped.QuantityReceived"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="shortShippedReceivedQuantity" class="form-control margintop small inputTxt" type="text" path="shortShippedReceivedQuantity" />
															</div>
															<div class="shortShippedRecQtyValidationError error  form-elemnt-error" style="display:none;">
														        	<div class="registerError"><spring:message code="disputeItem.Shortshipped.qtyreceived.msg"/></div>
														        </div>
															
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.Shortshipped.replacementNeeded"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<div class="radio radio-info radio-inline">
																<form:radiobutton path="replacementRequired" id="replacementRequiredYes" value="true" checked=""/>
																<label for="dispute-yes">Yes</label>
															</div>
															<div class="radio radio-info radio-inline">
															<form:radiobutton path="replacementRequired" id="replacementRequiredNo" value="false" checked=""/>
																<label for="dispute-no">No</label>
															</div>	
															</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12"><spring:message code="disputeItem.Shortshipped.replacedPO"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<span class="textBlack replacedPo" hidden="true">${disputeItemInquiryForm.purchaseOrderNumber}</span>
															</div>
														</div>
													</div>	
												</div>
												
												<div class="help-accordian panel">
													<div class="help-accordian-header">
														<a data-toggle="collapse" data-parent="#accordion" href="#collapse13" class="ref_no toggle-link panel-collapsed"><span class="glyphicon glyphicon-plus help-accordian-icon"></span>Over-Shipment (received an unordered product)</a>
													</div>
													<div class="help-accordian-body panel-collapse collapse" id="collapse13">
														<div class="row">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.accountNumber"/></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeItemInquiryForm.accountNumber}</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.poNumber"/></div>
															<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">${disputeItemInquiryForm.purchaseOrderNumber}</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.poCode"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<form:input id="overShippedProductCode" class="form-control margintop inputTxt" type="text" path="overShippedProductCode" />
																
															</div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 overShippedProdCodeValidationError error  form-elemnt-error" style="display:none;padding-left:0px">
													        	<div class="registerError"><spring:message code="disputeItem.OverShipment.productcode.msg"/></div>
													        </div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.QuantityOrdered"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="overShippedOrderedQuantity" class="form-control margintop small inputTxt" type="text" path="overShippedOrderedQuantity"/>
															</div>
															<div class="overShippedOrdQtyValidationError error  form-elemnt-error" style="display:none;">
														        	<div class="registerError"><spring:message code="disputeItem.Shortshipped.qtyordered.msg"/></div>
														        </div>
															
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.QuantityReceived"/><sup class="star">*</sup></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="overShippedReceivedQuantity" class="form-control margintop inputTxt" type="text" path="overShippedReceivedQuantity"/>
															</div>
															<div class="overShippedRecQtyValidationError error  form-elemnt-error" style="display:none;">
														        	<div class="registerError"><spring:message code="disputeItem.OverShipment.qtyreceived.msg"/></div>
														        </div>
															
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.Lotnumbers"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
															<form:input id="lotNumbers" class="form-control margintop inputTxt" type="text" path="lotNumbers"/>
															</div>
															<div class="offset-col-lg-4 col-lg-8 pull-right"><spring:message code="disputeItem.OverShipment.text"/></div>
														</div>
														<div class="margintop15">
															<div ><spring:message code="disputeItem.OverShipment.shipped"/></div>
															<div>
																<div class="radio radio-info radio-inline">
																<form:radiobutton path="keepProductsShipped" id="productShipmentYesDL" value="true" />
																<label for="dispute-yes">Yes</label>
															</div>
															<div class="radio radio-info radio-inline">
															<form:radiobutton path="keepProductsShipped" id="productShipmentNoDL" value="false" />
																<label for="dispute-no">No</label>
															</div>	
															</div>
														</div>
														<div class="row margintop15">
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-label-style"><spring:message code="disputeItem.OverShipment.ponumber"/></div>
															<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12">
																<form:input path="newPurchaseOrderNumber" class="form-control margintop inputTxt" id="poNumber" readonly="true"/>
															</div>	
																<div class="poNumberValidationError error form-label-style" style="display:none;">
																	<div class="registerError"><spring:message code="dispute.order.overshipped.newpo.errormessage.question"/></div>
																</div>
															
														</div>
													</div>	
												</div>																								
											</div>	
										</div>	
									</div>
									<div class="row">										
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 margintop15">
										<input type="button"  class="primarybtn btn btnclsactive pull-right submitDisputeItem" value="Submit Inquiry" />
											<a href="${orderDetailUrl}" class="canceltxt pull-right build-ordr-cancel-btn"><spring:message code="dispute.order.cancel"/></a>
												
										</div>
									</div>	
								</div>
							</div>
</form:form>		
<!-- Success Popup 3648-->
<div class="modal fade jnj-popup-container" id="dispute-success-popup" role="dialog" data-firstLogin='true'>
          <div class="modal-dialog modalcls">
           <div class="modal-content popup">
            <div class="modal-header">
<%--               <button type="button" class="close clsBtn" data-dismiss="modal" id="closedispute"><spring:message code="popup.close"/></button> --%>
            </div>
            <div class="modal-body">
            <div>            
            	<div class="panel-success">
		              <div class="panel-heading">
			              <h4 class="panel-title">
				              <span class="glyphicon glyphicon-ok"></span>
				              <span id="dispute-success-info"></span>
			              </h4>
		              </div>
	            </div>
	            </div>	           
            </div>
             <div class="modal-footer modal-footer-style">

				<button type="button" class="btn btnclsactive pull-right mobile-auto-btn"
					data-dismiss="modal">
					<spring:message code="cart.common.ok" />
				</button>
			</div>           
           
           </div>
          </div>
</div>		
<!-- Success Popup -->				