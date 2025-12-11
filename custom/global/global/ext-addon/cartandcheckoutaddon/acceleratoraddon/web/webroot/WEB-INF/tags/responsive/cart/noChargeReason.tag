<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%> 


<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="hideSelectDropShip"/>
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>
<div class="row jnjPanelbg">

											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
												<div class="txt-label-inline no-charge-reason"><spring:message code="cart.common.noChargeReasonCode"/> <span class="star">*</span></div>
												<div class="txt-box-inline no-charge-select">
													<select class="selectnoChargepicker" data-width="100%" id="noChargeReasonCode" name="noChargeReasonCode">
														<option value="">
																<spring:message code="cart.review.orderInfo.selectReason" />
														</option>
														<c:forEach var="reasonCode" items="${reasonCodeNoCharge}">
															<option value="${reasonCode.key}"   ${reasonCode.key == cartData.reasonCode ? 'selected="selected"' : ''}   >${reasonCode.value}</option>
														</c:forEach>
													</select>
													
												</div>	
												<div class="registerError" style="color:red"></div>
											</div>
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgeon-name-holder">
												<div class="txt-label-inline dropshipAccntLabel"><spring:message code="cart.common.dropShipAcc"/></div>
												<div class="txt-box-inline">
													<input type="text" class="form-control" id="dropShip" value="${cartData.dropShipAccount}">
													
												</div>
												
												<div class="txt-box-inline">
												<a href="#"  data-toggle="modal" >
												<span class="changeShipAddLighboxLink" data-url="${dropShipURL}"> 
													<button class="drop-ship-account-list-icon ${hideSelectDropShip}  fa fa-list" id="drop-ship-account-list-icon"></button>
													
													</span>
													</a>
												</div>
												<div id="errorMsgDiv"></div>
											</div>	
												
										</div>	
										
										<div id="dropShipAccountholder"></div>
												<!-- Modal Shipping detail pop up-->
						<%--  <div class="modal fade jnj-popup" id="dropship-account-popup" role="dialog">
							<div class="modal-dialog modalcls modal-md " id="selectaccountpopup">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
										<h4 class="modal-title">Drop-Ship Account</h4>
									</div>
												
										
									<div class="modal-body">
										<div class="list-group listclass">
											<div id="changeAccountPopup">
										
											   <ul class="account-ul-list">
											   <c:forEach var="dropShipAccount" items="${dropShipAccounts}">
													<li class="dropShipaccountListPopUp odd-row" id="dropShipaccountListPopUp"> 
														<a class="changeAccountUnit list-group-item anchorcls o" href="javascript:;">
															<p class="list-group-item-heading code" >${dropShipAccount.jnjAddressId}</p>
															<input type="hidden" name="dropShipAccount" id="dropShipAccount" value="${dropShipAccount.jnjAddressId}" />
													    </a>
													   <p class="list-group-item-text descTxt">${dropShipAccount.formattedAddress}</p>
													  
													 </li> 
													</c:forEach>												 
												</ul>
											</div>
										</div>
									</div>
									<div class="modal-footer">
									<a href="#"  class="pull-left"  data-dismiss="modal">Cancel</a>
										<button type="button" class="btn btnclsactive pull-right" data-dismiss="modal" id="submitChangeDropShipAdd">OK</button>
									</div>	
									
								</div>
							</div>
						</div>  --%>
						<!--End of Modal privacypopup pop-up-->		