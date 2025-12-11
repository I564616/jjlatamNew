<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	<!-- select Address pop up in validate and empty -->
			<div class="col-lg-12 col-md-12" id="selectanaddress1">
									<div class="modal fade" id="selectaddresspopupBillTO" role="dialog">
										<div class="modal-dialog modalcls">
										<c:url value="/cart/updateBillingAddress" var="updateBillingAdd" />
				                       
										
											<!-- Modal content-->
											<div class="modal-content popup">
												<div class="modal-header">
												  <button type="button" class="close clsBtn" data-dismiss="modal" id="close-btn-address"><spring:message code="password.forgotPassword.close" /></button>
												  <h4 class="modal-title selectTitle"><spring:message code="shipping.page.selectaddrs" /></h4>
												</div>
												<div class="modal-body">
												     <div class="form-group searchArea">
												     <input type="text" style="padding-right: 40px; placeholder="<spring:message code="changeAddress.search.billToAddress" />" value="${BillingSearchTerm}" id="searchBillingAddressAjax" class="form-control searchBox"/>
												     <button class="glyphicon glyphicon-search searchBtn pull-right" id="searchSelectAddBtn1" style="border: none;background: none"/></button> 
														<%-- <input type="text" class="form-control searchBox" id="usr" placeholder="<spring:message code='cart.searchAddress'/>">
														<span class="glyphicon glyphicon-search searchBtn pull-right"></span> --%>
													</div>
													<c:url value="/cart/updateBillingAddress" var="updateBillingAdd" />
				                        			<form:form id="changeBillAddForm" method="post" action="${updateBillingAdd}">
				                        			<div id="searchAddresDiv1">
													<div class="list-group listclass scroll-y-div" id="addressDataId1">
															<input type="hidden" id="billingAddressCounts" name="shippingAddressCounts" value="${billingAddressess.size()}">
															<c:forEach var="billingAddress" items="${billingAddressess}" varStatus="count">
															<div class="odd-row">
																<div class="list-group-item-text descTxt" id="test">
																    <input type="hidden" name="billingAddress1" value="${billingAddress.id}" id = "billingAddress1" class="shipp">
																	<div class="address-txt">${billingAddress.companyName}</div>
																	<div class="address-txt">
																	${billingAddress.line1}
																	<c:if test="${not empty billingAddress.line1}">
																	,
																	</c:if>
											                         ${billingAddress.line2}</div>
																	<div class="address-txt">${billingAddress.town}
																	<c:if test="${not empty billingAddress.town}">
																	,
																	</c:if>
																	${billingAddress.postalCode}</div>
																	<div class="address-txt">
																	<c:if test="${not empty billingAddress.taxid}">
																	${billingAddress.taxid}
																	</c:if>
																	</div>
																</div>
															</div>
															</c:forEach>
														</div>
													</div>
													</div>
												<div class="row ftrcls">
													<div class="col-lg-6">
													<!-- AAOL-3612 -->
														<span  id="selectbillingAddressErr"><spring:message code='cart.validate.changeAddressError'/></span>
													</div>	
													<div class="col-lg-6">
														<input type="button" class="btn btnclsactive pull-right"  id="submitChangeAddressForBilling" value="<spring:message code='cart.confirmation.changeAddress'/>">
													</div>
														
												</div>
											</div>
										  </form:form>
										</div>
									</div>
									<div id="SaveAsTemplateHolder"></div>
								<!--select Address pop up in validate and empty-->
							</div>