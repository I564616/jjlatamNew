<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false"
	type="java.lang.Boolean"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="laCommon" tagdir="/WEB-INF/tags/addons/jnjlacartandcheckoutaddon/responsive/cart/common"%>


<script type="text/javascript">
	// set vars
	/*<![CDATA[*/
	var cartRemoveItem = true;
	/*]]>*/
</script>

<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll"
	method="post" modelAttribute="UpdateMultipleEntriesInCartForm">
</form:form>
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	<div class="row content">
		<div class="col-lg-10 col-md-10 col-sm-10 col-xs-12">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-12">
			<button type="button" class="btn btnclsactive cartStep1Saveupdate1Latam">
				<spring:message code="cart.review.progressBar.validate" />
			</button>
		</div>
	</div>
	
		<%-- Information (confirmation) messages --%>
	<c:if test="${not empty accConfMsgs}">
		<c:forEach items="${accConfMsgs}" var="msg">
			<laCommon:genericMessage messageCode="${msg.code}" icon="ok"
				panelClass="success" />
		</c:forEach>
	</c:if>

	<%-- Warning messages --%>
	<c:if test="${not empty accInfoMsgs}">
		<c:forEach items="${accInfoMsgs}" var="msg">
			<laCommon:genericMessage messageCode="${msg.code}" icon="ban-circle"
					panelClass="danger" />
		</c:forEach>
	</c:if>

	<%-- Error messages (includes spring validation messages)--%>
	<c:if test="${not empty accErrorMsgs}">
			<c:forEach items="${accErrorMsgs}" var="msg">
				<laCommon:genericMessage messageCode="${msg.code}" messagearguments="${msg.attributes}" icon="ban-circle"
					panelClass="danger" />
			</c:forEach>
	</c:if>
	
	
	 <div id="successMessage_cart" style="display: none;">
	<div class="panel-group hidden-xs hidden-sm" style="margin: 5px 0 20px 0;">
	<div class="panel panel-success">
		<div class="panel-heading">
			<h4 class="panel-title">
				<span> <span class="glyphicon glyphicon-ok"></span>&nbsp;
					<spring:message code="text.template.saved.successmessage" />
				</span>
			</h4>
		</div>
	</div>
</div>
</div>
	
	<c:if test="${cartData.isContractCart}">
		<laCommon:genericMessage contractNumber="${cartData.contractId}"
		messageCode="cart.common.contract.messageInfo" icon="list-alt" panelClass="danger" />
	</c:if>
	
	<!-- Desktop -->
	<div class="mainbody-container">
			<div class="row table-padding no-top-bottom-padding">
<input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}"/>
<input type="hidden" name="defaultChekAddid" id = "defaultChekAddid" value="${defaultChekAddid}">
<input type="hidden" id="shippingAddressCounts" name="shippingAddressCounts" value="${shippingAddressess.size()}">
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

<form class="col-lg-6 col-md-6 col-sm-6 col-xs-12" name="mltiAddToCartForm" id="mltiAddToCartForm"
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
				<c:if test="${allProdQuantityStatus == null && fileAllProdQuantityStatus == null}">
				<div class="row" style="margin-top:10px">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 nopad-right">
						<div class="registersucess" style="color:#3c763d;font-weight:bold">
                            <c:if test = "${placeOrderResComUserGrpFlag eq false}">
						    <label for="productId1" class="success">
						        <spring:message code="home.upload.file.successOrder" />
						    </label>
							</c:if>
							<c:if test = "${placeOrderResComUserGrpFlag eq true}">
						    <label for="productId1" class="success" style="margin-top: 80px;">
						        <spring:message code="home.upload.file.successOrder" />
						    </label>
							</c:if>

						</div>
					</div>
				</div>	
				</c:if>
			</div>
			
			
</form>			
		</div>
		
		<div class="d-none d-sm-block">
			<table id="datatab-desktop" data-paging="false" data-info="false"
				class="table table-bordered table-striped sorting-table">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message
								code="cart.validate.product" /></th>
						<c:if test="${displayIndirectCustomerHeader}">
							<th class="text-left no-sort"><spring:theme
									code="text.account.buildorder.indirectCustomer"
									text="INDIRECT CUSTOMER" />
								<div class="cart-update-all-link">
									<a href="" class="cartUpdateAllbtn" data-bs-toggle="modal"
										id="selectindc"> <spring:message
											code="cart.review.entry.Updateall" />
									</a>
								</div></th>
						</c:if>
						<c:if test="${displayIndirectPayerHeader}">
							<th class="text-left no-sort"><spring:theme
									code="text.account.buildorder.indirectPayer"
									text="INDIRECT PAYER" />
								<div class="cart-update-all-link">
									<a href="" class="cartUpdateAllbtn" data-bs-toggle="modal"
										id="selectindp"> <spring:message
											code="cart.review.entry.Updateall" />
									</a>
								</div></th>
						</c:if>
						<th class="no-sort"><spring:message
								code="cart.review.entry.quantity" />
							<div class="cart-update-all-link">
								<a class="cartUpdateAllbtn cartUpdateAllbutton"
									id="cartUpdateAllbutton" href="javascript:;"> <spring:message
										code="cart.review.entry.Updateall" />
								</a>
							</div></th>
						<th class="no-sort"><spring:message
								code="cart.validate.unitPrice" /></th>
						<th class="no-sort total-thead"><spring:message
								code="cart.review.entry.total" /></th>
					</tr>
				</thead>
				<c:if test="${isTemplateAdd eq true && not empty addStatus}">
					<div class="panel-group hidden-xs hidden-sm" id="invalid-error-msg"
						style="margin: 10px 0px 10px 5px;">
						<div class="panel panel-danger">
							<div class="panel-heading">
								<h4 class="panel-title">
									<div class="span-24">
										<div class="information_message negative">
											<span class="single"></span>
											<p>
												<spring:message code="product.detail.basic.productCode" />
												${addStatus}
												<spring:message
													code="cart.addToCart.ordertemplate.productCodeInvalid" />
											</p>
										</div>
									</div>
								</h4>
							</div>
						</div>
					</div>
				</c:if>
				<tbody id="AddItemsCartpage">
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="count">
						<tr id="orderentry-${count.count}">
							<td class="text-left jnj-img-txt"><standardCart:productDescriptionBeforeValidation
									entry="${entry}" showRemoveLink="true" showStatus="false" /></td>
							<!-- Checking indirect customer line level flag -->
							<c:if test="${displayIndirectCustomerLine[count.index]}">
								<td class="txtWidth valign-middle"><input type="text"
									class="form-control insertInput indirectCustomer"
									autocomplete="on" id="indirectCustomerId${count.index}"
									name="indirectCustomer" value="${entry.indirectCustomer}"
									data="${count.index}"
									onchange="onChangeIndirectCustomerCart('${count.index}');">
									<p class="descMid" id="indirectCustomerName${count.index}">${entry.indirectCustomerName}</p>
									<p class="descMid errorMessage" style="color: #B41601">
										<spring:message code="${entry.indirectCustomerError}" />
									</p></td>
							</c:if>
							<c:if
								test="${displayIndirectCustomerHeader eq true && (empty displayIndirectCustomerLine || displayIndirectCustomerLine[count.index] eq false)}">
								<td></td>
							</c:if>

							<!-- Checking indirect payer line level flag -->
							<c:if test="${displayIndirectPayerLine[count.index]}">
								<td class="txtWidth valign-middle"><input type="text"
									class="form-control insertInput indirectPayer"
									autocomplete="on" id="indirectPayerId${count.index}"
									name="indirectPayer" value="${entry.indirectPayer}"
									data="${count.index}"
									onchange="onChangeIndirectPayerCart('${count.index}');">
									<p class="descMid" id="indirectPayerName${count.index}">${entry.indirectPayerName}</p>
									<p class="descMid errorMessage" style="color: #B41601">
										<spring:message code="${entry.indirectPayerError}" />
									</p></td>
							</c:if>
							<c:if
								test="${displayIndirectPayerHeader eq true && (empty displayIndirectPayerLine || displayIndirectPayerLine[count.index] eq false)}">
								<td></td>
							</c:if>

							<td class="valign-middle">
								<div class="cart-update-link">
									<a href="javascript:void(0);" id="quantity_${entry.entryNumber}"
										entryNum="${entry.entryNumber}" class="laUpdateQuantityProduct">
										<spring:message code="cart.review.productDesc.updateItem" />
									</a>
								</div> <c:url value="/cart/update" var="cartUpdateFormAction" /> <form:form
									id="updateCartForm${entry.entryNumber}"
									action="${cartUpdateFormAction}" method="post"
									modelAttribute="updateQuantityForm${entry.entryNumber}">
									<input type="hidden" name="entryNumber"
										value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"
										value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity"
										value="${entry.quantity}" />
									<div>
										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity"
												for="quantity${entry.entryNumber}"></form:label>
											<form:input disabled="${not entry.updateable}" type="text"
												id="quantity_${entry.entryNumber}"
												entryNumber="${entry.entryNumber}"
												class=" laQtyUpdateTextBox form-control txtWidth"
												path="quantity" />
                                        
                                    </ycommerce:testId>
									<div class="qntunit">${entry.product.salesUnitCode}</div>
									</div>
									<spring:message code="product.multiple" />&nbsp;${entry.product.multiplicity}

                                <ycommerce:testId
										code="cart_product_removeProduct">
										<p>
											<a href="javascript:void(0);"
												id="RemoveProduct_${entry.entryNumber}"
												class="smallFont laSubmitRemoveProduct"><spring:message
													code="cart.review.productDesc.removeItem" /> </a>
										</p>
									</ycommerce:testId>

									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>
							</td>
							<td><%--<format:price priceData="${entry.basePrice}"/>--%></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<c:set var="cartCount" value="0"/>
		<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
			<c:set var="cartCount" value="${count.count}"/>
		</c:forEach>
		<input type="hidden" name="numberOfItemscart" id="numberOfItemscart" value="${cartCount}"/>
		
		
		<!-- Table collapse for mobile device-->
		<div class="row Subcontainer d-block d-sm-none">
			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table">
				<thead>
					<tr>
						<th class="no-sort text-left"><spring:message
								code="cart.validate.product" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="count">
						<tr>
							<td class="text-left"><standardCart:productDescriptionBeforeValidation
									entry="${entry}" showRemoveLink="true" showStatus="false" />
								<p>
									<spring:message code="cart.validate.quantityQty" />
								</p> <c:url value="/cart/update" var="cartUpdateFormAction" /> <form:form
									id="updateCartForm${entry.entryNumber}"
									action="${cartUpdateFormAction}" method="post"
									modelAttribute="updateQuantityForm${entry.entryNumber}">
									<input type="hidden" name="entryNumber"
										value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"
										value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity"
										value="${entry.quantity}" />

									<ycommerce:testId code="cart_product_quantity">
										<form:label cssClass="skip" path="quantity"
											for="quantity${entry.entryNumber}">
											<spring:theme code="basket.page.quantity" />
										</form:label>
										<form:input disabled="${not entry.updateable}" type="text"
											id="quantity_${entry.entryNumber}"
											entryNumber="${entry.entryNumber}"
											class=" laQtyUpdateTextBox form-control txtWidth"
											path="quantity" />
									</ycommerce:testId>

									<ycommerce:testId code="cart_product_removeProduct">
										<a href="javascript:void(0);"
											id="RemoveProduct_${entry.entryNumber}"
											class="smallFont laSubmitRemoveProduct"> <spring:message
												code="cart.review.productDesc.removeItem" />
										</a>
									</ycommerce:testId>
									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>
								<p>
									<spring:message code="cart.validate.unitPrice" />
								</p>
								<p></p>
								<p>
									<spring:message code="cart.review.entry.total" />
								</p>
								<p></p></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<!-- Start - Total Price Summary -->
		<div class="row basecontainer">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps no-right-pad">--</td>
				</tr>
				<tr class="summary-bline">
					<td class="total-summary-label"><spring:message
							code="cart.review.entry.shipping" /></td>
					<td class="total-summary-cost">--</td>
				</tr>
				<tr class="total-price-row">
					<td class="total-summary-label"><spring:message
							code="cart.validate.total" /></td>
					<td class="total-summary-cost totalsum no-right-pad"><sup
						class="supmd">--</sup></td>
				</tr>
			</table>
		</div>
		<!-- End - Total Price Summary -->
	</div>
	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
				<button type="button" class="btn btnclsnormal checkout-clear-cart"
					id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button>
			</div>
			<div class="float-right-to-none">
				<button type="button" class="btn btnclsnormal templatebtn saveorderastemplatelatam" disabled="disabled">
					<spring:message code="cart.review.cartPageAction.saveTemplate" />
				</button>
				<button type="button" class="btn btnclsactive cartStep1Saveupdate1Latam">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
			</div>
		</div>
	</div>
	
	
	
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