<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>

<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	<div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"><spring:message code="cart.review.shoppingCart"/></div>

	</div>
	<div class="mainbody-container">
		<div class="row table-padding no-top-bottom-padding">
<input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}"/>
<input type="hidden" name="defaultChekAddid" id = "defaultChekAddid" value="${defaultChekAddid}">
			<div
				class="col-lg-6 col-md-6 col-sm-6 col-xs-12 shoppingart-panel-padding"
				id="cart-left-content">
				<p class="subhead boldtext"><spring:message code="cart.shipping.ShipToAdd"/></p>
				<cart:deliveryAddress deliveryAddress="${cartData.deliveryAddress}"/>
				<div class="checkbox checkbox-info" id="shippingAddrDefaultChkDiv">
					<input id="shippingAddrDefaultChk" class="styled" type="checkbox"> 
									<label for="shippingAddrDefaultChk">
										 <spring:message code="cart.shipping.defaultaddress"/>
									  </label>
				</div>

				<c:choose>
					<c:when test="${shippingAddressess.size()>1}">
						<div class="shiptoAlternativeAddress">
							<a href="#" data-bs-toggle="modal" data-bs-target="#selectaddresspopup"><spring:message
									code="cart.shipto.alternateaddress" /></a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="shiptoAlternativeAddress"></div>
					</c:otherwise>
				</c:choose>
			</div>

<form  class="col-lg-6 col-md-6 col-sm-6 col-xs-12" name="mltiAddToCartForm" id="mltiAddToCartForm"
	action="javascript:;">
			<div class="shoppingart-panel-padding"
				id="cart-right-content">
                      <c:if test = "${placeOrderResComUserGrpFlag eq false}">
                       <div class="row">
                              <div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 nopad-right">
                                  <div class="enter-product-label"><spring:message
                                              code="cart.enterproducts.header" /></div>
                                  <div class="enter-product-label-disc"><spring:message
                                              code="cart.commaseperated.text" /></div>
                              </div>
                              <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">

                              <input type="button" class="btn btnclsnormal new-add-tocart" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />" class="primarybtn right">
                              <input type="button" style="" id="errorMultiCart" class="tertiarybtn homeCartErrors btn labtnclsactive new-error-detail-btn"  value="<spring:message code='homePage.errorDetails' />" />
                              </div>
                          </div>
                          <div class="row" id="product-number-row">
                              <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 nopad-right"
                                  id="product-number-label"><spring:message
                                              code="cart.productnum.text" /></div>
                              <div class="col-lg-8 col-md-8 col-sm-8 col-xs-8"
                                  id="product-number-txt">
                                  <input type="text" class="form-control required" id="prodCode"></input>
                              </div>
                          </div>
                      </c:if>
			</div>
			
			
</form>			
		</div>
		<div class="cart-empty-table">
			<table id="cart-empty-table"
				class="table table-bordered table-striped">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
						<th class="no-sort cartqty"><spring:message code="cart.review.entry.quantity"/> 
						</th>
						<th class="no-sort cartprice"><spring:message code="cart.validate.unitPrice"/></th>
						<th class="no-sort multitotal-thead paddingleft10px" ><spring:message code="cart.review.entry.total"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="text-left jnj-img-txt" colspan="4">
							<div class="row cart-emtymsg-row">
								<div
									class="col-lg-12 col-md-12 col-xs-12 text-center empty-cart-msg"><spring:message code="acc.build.order.emptymsg"/></div>
								<div
									class="continuebtn col-lg-12 col-md-12 col-xs-12 text-center">
										<div class="empty-btn marginbottom20px" >
											<cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
												<cms:component component="${feature}"/>
											</cms:pageSlot>
										</div>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>

<!-- Error Details Popup Start-->

<div class="modal fade jnj-popup" id="error-detail-popup"
			role="dialog">
			<div class="modal-dialog modalcls modal-md">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="cart.review.close" /></button>
						<h4 class="modal-title"><spring:message code="homePage.errorDetails" /></h4>
					</div>
					<form method="post" action="javascript:;">
						<div class="modal-body">
							<div class="row">
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="panel-group">
										<div class="panel panel-danger">
											  <div class="panel-heading">
												<h4 class="panel-title">
												<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message code="homePage.errordetails.addfailed" /></span>
												</h4>
											  </div>
										</div>  
									</div>
								</div>
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
									<div class="scroll error-content" style="font-weight:bold">
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		
		
		<!-- Error Details Popup End-->
		
		<div class="col-lg-12 col-md-12" id="selectanaddress1">
							
								<!-- Modal -->
									<div class="modal fade" id="selectaddresspopup" role="dialog">
										<div class="modal-dialog modalcls">
										<c:url value="/cart/updateShippingAddress" var="updateShippingAdd" />
				                        <form id="changeAddForm" method="post" action="${updateShippingAdd}">
										
											<!-- Modal content-->
											<div class="modal-content popup">
												<div class="modal-header">
												  <button type="button" class="close clsBtn" data-bs-dismiss="modal"><spring:message code="account.change.popup.close"/></button>
												  <h4 class="modal-title selectTitle"><spring:message code="shipping.page.selectaddrs"/></h4>
												</div>
												<div class="modal-body">
											
													<div class="list-group listclass">
															<c:forEach var="shippingAddress" items="${shippingAddressess}" varStatus="count">
															
															<!-- if(odd) -->
															<div class="odd-row">
																<div class="list-group-item-text descTxt" id="test">
																    <input type="hidden" name="shippingAddress1" value="${shippingAddress.id}" id = "shippingAddress1" class="shipp">
																	<%-- <div class="address-txt">${shippingAddress.companyName}</div> --%>
																	<div class="address-txt">${shippingAddress.line1}<%--  ${shippingAddress.line2} --%></div>
																	<div class="address-txt">${shippingAddress.town}</div>
																	<div class="address-txt">${shippingAddress.postalCode}</div>
																</div>
															</div>
															</c:forEach>
														</div>
													</div>
													
												<div class="row ftrcls">
												<spring:message code='shipping.page.changeaddrs' var="changeAddress"/>
													<input type="button" class="btn btnclsactive pull-right" data-bs-dismiss="modal" id="laSubmitChangeAddress" value="${changeAddress}">
												</div>
											</div>
										  </form>
										</div>
									</div>
								<!--End of Modal pop-up-->
							</div>	
