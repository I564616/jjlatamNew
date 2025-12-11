<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal fade jnj-popup" id="editCreditCard" role="dialog">
							<div class="modal-dialog modalcls modal-md">
								<!-- Modal content-->
								<div class="modal-content">
									<div class="modal-header">
									  <button type="button" class="close clsBtn" data-dismiss="modal">Close</button>
									  <h4 class="modal-title"><spring:message code="cart.review.details"/></h4>
									</div>
									<div class="modal-body">
										<div class="row">
											<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
												<div class="creditcardDetails">
												<input type="hidden" id ="cvvSizeErrorMessage" value='<spring:message code="cart.paymetric.cvvSizeErrorMessage"/>'/>
												<input type="hidden" id ="issuerNumberSizeErrorMessage" value='<spring:message code="cart.paymetric.issuerNumberSizeErrorMessage"/>'/>
												<input type="hidden" id ="cvvRequiredErrorMessage" value='<spring:message code="cart.paymetric.cvvRequiredErrorMessage"/>'/>
												<input type="hidden" id ="ccNumberRequiredMessage" value='<spring:message code="cart.paymetric.ccNumberRequiredMessage"/>'/>
												<input type="hidden" id ="ccNumberLuhnErrorMessage" value='<spring:message code="cart.paymetric.ccNumberLuhnErrorMessage"/>'/>
												<input type="hidden" id ="ccNumberCardTypeErrorMessage" value='<spring:message code="cart.paymetric.ccNumberCardTypeErrorMessage"/>'/>
												<input type="hidden" id ="expirationDateErrorMessage" value='<spring:message code="cart.paymetric.expirationDateErrorMessage"/>'/>
												<form action="" method="post">
													<script src="${URL}" type="text/javascript" language="javascript"></script>
													<script type="text/javascript" language="javascript">
							            			var SignedPayload = "${SignedPayload}";
							            			UpdatePaymentPageContent(SignedPayload);
							                		var IssuerNumberSizeErrorMessage = $("#issuerNumberSizeErrorMessage").val(),
							                		CVVRequiredErrorMessage = $("#cvvRequiredErrorMessage").val(),
							                		CVVSizeErrorMessage = $("#cvvSizeErrorMessage").val(),
							                		CCNumberRequiredMessage = $("#ccNumberRequiredMessage").val(),
							                		CCNumberLuhnErrorMessage = $("#ccNumberLuhnErrorMessage").val(),
							                		CCNumberCardTypeErrorMessage = $("#ccNumberCardTypeErrorMessage").val(),
							                		ExpirationDateErrorMessage = $("#expirationDateErrorMessage").val();
							            			</script>
							            			<div class="checkbox checkbox-info text-left">
													<input id=remember class="styled" type="checkbox">
													<label  id="credit-card-remem" for="remember">
														<spring:message code="cart.review.cardRemember"/>
														<%-- <span><input type="checkbox" id="remember"/></span><label for="remember"><spring:message code="cart.review.cardRemember"/></label> --%>
													</label>
												</div>
													</form>
													
												</div>
											</div>
																		
										</div>
									</div>
									
								</div>
							</div>
						</div>

