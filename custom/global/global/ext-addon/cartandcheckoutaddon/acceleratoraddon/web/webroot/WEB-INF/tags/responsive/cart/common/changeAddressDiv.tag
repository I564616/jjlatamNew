<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	<!-- select Address pop up in validate and empty -->
			<div class="col-lg-12 col-md-12" id="selectanaddress1">
									<div class="modal fade" id="selectaddresspopup" role="dialog">
										<div class="modal-dialog modalcls">
										<c:url value="/cart/updateShippingAddress" var="updateShippingAdd" />
										
											<!-- Modal content-->
											<div class="modal-content popup">
												<div class="modal-header">
												  <button type="button" class="close clsBtn" data-dismiss="modal" id="close-btn-address"><spring:message code="password.forgotPassword.close" /></button>
												  <h4 class="modal-title selectTitle"><spring:message code="shipping.page.selectaddrs" /></h4>
												</div>
												<div class="modal-body">
												     <div class="form-group searchArea">
												     <input type="text" style="padding-right: 40px; placeholder="<spring:message code="changeAddress.search.shipToAddress" />" value="${shippingSearchTerm}" id="searchAddressAjax" class="form-control searchBox"/>
												     <button class="glyphicon glyphicon-search searchBtn pull-right" id="searchSelectAddBtn" style="border: none;background: none"/></button> 
														<!-- <input type="text" class="form-control searchBox" id="usr" placeholder="Search for an address">
														<span class="glyphicon glyphicon-search searchBtn pull-right"></span> -->
													</div>
												<c:url value="/cart/updateShippingAddress" var="updateShippingAdd" />
				                       			<form:form id="changeAddForm" method="post" action="${updateShippingAdd}">
												<div id="searchAddresDiv">
													<div class="list-group listclass scroll-y-div" id="addressDataId">
															<input type="hidden" id="shippingAddressCounts" name="shippingAddressCounts" value="${shippingAddressess.size()}">
															<c:forEach var="shippingAddress" items="${shippingAddressess}" varStatus="count">
															<div class="odd-row">
																<div class="list-group-item-text descTxt" id="test">
																    <input type="hidden" name="shippingAddress1" value="${shippingAddress.id}" id = "shippingAddress1" class="shipp">
																	<div class="address-txt">${shippingAddress.companyName}</div>
																	<div class="address-txt">
																	${shippingAddress.line1}
																	<c:if test="${not empty shippingAddress.line1}">
																	,
																	</c:if>
											                         ${shippingAddress.line2}</div>
																	<div class="address-txt">${shippingAddress.town}
																	<c:if test="${not empty shippingAddress.town}">
																	,
																	</c:if>
																	${shippingAddress.postalCode}</div>
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
														<span  id="selectAdressErr"><spring:message code="cart.validate.changeAddressError" /></span>
													</div>	
													<div class="col-lg-6">
														<input type="button" class="btn btnclsactive pull-right"  id="submitChangeAddress" value="CHANGE ADDRESS">
													</div>
														
												</div>
											</div>
										  </form:form>
										</div>
									</div>
									<div id="SaveAsTemplateHolder"></div>
								<!--select Address pop up in validate and empty-->
							</div>