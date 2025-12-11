<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label"	uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%> 
<script type="text/javascript">
	// set vars
	/*<![CDATA[*/
	var cartRemoveItem = true;
	/*]]>*/
</script>
<!--  AAOL-6138 changes start -->
<c:set var="dateformat"> <spring:message code='date.dateformat'/></c:set>
<!--  AAOL-6138 changes end -->
<c:url value="/cart/changeOrderType" var="changeOrderTypeURL" />
<form:form method="post" action="${changeOrderTypeURL}" id="changeOrderTypeForm"></form:form>
<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll"	method="post" commandName="UpdateMultipleEntriesInCartForm">
</form:form>
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />

	<div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-5 col-xs-12">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-6 col-md-6 col-sm-7 col-xs-12">
			<div class="float-right-to-none">
				<div class="orderType-holder">
					<span class="orderType-label">
					<spring:message	code="cart.common.orderType" />
					</span>
					<!-- Changes for Default Order Type AAOL-4660 -->
					<div class="orderType-dropdown">
						<select id="changeOrderType" name="orderType" data-width="100%">
							<option value="${cartorderType}"> <spring:message code="cart.common.orderType.${cartorderType}"></spring:message></option>
							<c:forEach items="${orderTypes}" var="orderType">
								<c:if test="${cartorderType ne orderType}">
									<option value="${orderType}"><spring:message code="cart.common.orderType.${orderType}"></spring:message></option>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</div>			
				<button type="button" class="btn btnclsactive  cartvalidateInternational">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
			</div>
		</div>
	</div>
	
<!--4069 story changes starts-->
	<c:if test="${not empty priceError}">
		<div class="error">
			<p style="color: red">
				<spring:message code="cart.common.zeroPrice.error" />				
			</p>
		</div>
	</c:if>
	<!--4069 story changes ends-->
	<div id="globalMessages">
		<common:globalMessages />
		<cart:cartRestoration />
		<cart:cartValidation />
	</div>
	<div class="mainbody-container">
		<input type="hidden" name="makeThisAddrDefaultChk"	id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}" />
		 <input	type="hidden" name="defaultChekAddid" id="defaultChekAddid"	value="${defaultChekAddid}">
		<!-- Added for Shipping address Display in Validation page -->
		<commonTags:Addresses />
		<commonTags:changeAddressDiv />
		<commonTags:changeBillToAddress />

		<div class="row jnjPanelbg">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
				<div class="txt-label-inline request-delivery-label"><spring:message code="cart.common.requestedDeliveryDate"/></div>
				<div class="txt-box-inline">
					<div class="input-group request-delivery-textbox">
						<div id="requestedDateForShipping "></div>
						<input id="date-picker-1" name="toDate"	class="date-picker form-control requestDeliveryDate required" type="text" value="<fmt:formatDate value="${cartData.expectedShipDate}"  pattern="${dateformat}" />">
						<label for="date-picker-1" class="input-group-addon btn">
						<span	class="glyphicon glyphicon-calendar"></span>
						 </label>
					</div>
					<div id="reqDateError"></div>
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 surgeon-name-holder">
				<div class="txt-label-inline special-instruction-label">
					<spring:message code="cart.common.specialText" />
				</div>
				<div class="txt-box-inline special-instr-txt">
					<input type="text" id="specialText" name="specialText"	class="form-control ${addOnVisibilityCss}" ${disabled}	value="${cartData.specialText}">
				</div>
			</div>
		</div>

		<div class="row jnjPanelbg">
			<form:form name="mltiAddToCartForm" id="mltiAddToCartForm" action="javascript:;">
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
					<div class="enter-product-label">
						<spring:message code="cart.enterproducts.header" />
					</div>
					<div class="enter-product-label-disc">
						<spring:message code="cart.commaseperated.text" />
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle marginProd">
				<div class="float-right-to-none">
		             <div class="price-txt-width">
		             	 <input type="text" id ="prodCode"  class="form-control" placeholder="<spring:message code='cart.productnum.text'/>"></input>
		             </div> 
		             <div class="price-quantity"> 
		             	 <input type="text"  id ="prodQty" class="form-control" placeholder="<spring:message code='gt.price.inquiry.pdf.quantity'/>"></input>
		             </div> 
		         </div> 		
			</div>
				<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
					<div class="full-width-btns">
						<input type="button" class="btn btnclsnormal new-add-tocart full-width-btns"	id="addToCartForm_2"	value="<spring:message code='homePage.addtocart' />"> 
						<input	type="button" style="" id="errorMultiCart"	class="full-width-btns tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn" value="<spring:message code='homePage.errorDetails' />" />
					</div>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top: 8px">
					<div class="registersucess registersucess-style"></div>
				</div>
			</form:form>
		</div>

 	<div class="hidden-xs">
			<table id="datatab-desktop"
				class="table table-bordered table-striped sorting-table-lines">
				<thead>
					<tr>
						<th class="no-sort text-left text-uppercase">
						<spring:message	code="cart.review.entry.numberText" />
						</th>
						<th class="no-sort text-center text-uppercase">
						<spring:message	code="cart.review.entry.lineItems" />
						</th>
						<th class="no-sort text-center text-uppercase quanity-cell">
						<spring:message	code="cart.review.entry.quantity" />
							<div class="cart-update-all-link">
								<a class="cartUpdateAllbtn cartUpdateAllbutton"	id="cartUpdateAllbutton" href="javascript:;">
								<spring:message	code="cart.review.entry.Updateall" />
								</a>
							</div></th>
						<th class="no-sort text-uppercase">
						<spring:message	code="cart.review.entry.itemPrice" />
						</th>
						<th class="no-sort total-thead text-uppercase">
						<spring:message	code="cart.review.entry.total" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"	varStatus="count">
						<tr>
							<td class="text-left">${count.count}</td>
							<td class="text-left">
							<standardCart:productDescription entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="true" showStatus="false" />
							</td>
							<td>
								<div class="cart-update-link">
									<a href="javascript:void();" id="quantity${entry.entryNumber}_update"	entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
										<spring:message code="cart.review.productDesc.updateItem" />
									</a>
								</div>
								 <c:url value="/cart/update" var="cartUpdateFormAction" />
								  <form:form id="updateCartForm${entry.entryNumber}_desktop" action="${cartUpdateFormAction}" method="post"	commandName="updateQuantityForm${entry.entryNumber}">
									<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"		value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
									<div>
										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity"	for="quantity${entry.entryNumber}">
											</form:label>
											<form:input disabled="${not entry.updateable}" type="text" id="quantity${entry.entryNumber}"	entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity" />
										</ycommerce:testId>
									</div>

									<p class="thirdline">
										<strong> <spring:message code="product.detail.addToCart.unit" />
										</strong>${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>

									<ycommerce:testId code="cart_product_removeProduct">
										<p>
											<a href="javascript:void(0);" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
											 <spring:message code="cart.review.productDesc.removeItem" />
											</a>
										</p>
									</ycommerce:testId>

								</form:form>
								 <c:forEach var="division" items="${divisionList}">
									<c:if test="${division eq entry.product.salesOrgCode}">
										<p class="log">
											<span> <label class="descSmall block" for="LotComment${entry.entryNumber}">
											<spring:message	code="cart.review.entry.lotComment" />
											</label> 
											<input type='text'	class='insertInput lotCommentInput' title="<spring:message code="cart.common.code.enter"/>"	id="LotComment_${entry.entryNumber}"value="${entry.lotComment}" />
											</span>
										</p>
									</c:if>
								</c:forEach>
							</td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div> 
		<!-- Table collapse for mobile device-->
                         <div class="visible-xs hidden-lg hidden-sm hidden-md">
											<table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines">
												<thead>
													<tr>
													<th class="no-sort text-left"><spring:message code="cart.validate.product"/>
													<div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
													</th>
													</tr>
												</thead>
												<tbody>
												<c:forEach items="${cartData.entries}" var="entry"	varStatus="count">
													<tr>
														<td class="text-left">
															
																<div class="display-row">
																	<div class="table-cell jnj-toggle-sign">
																		<a href="#collapse${count.count}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed">
																			<span class="glyphicon glyphicon-plus"></span>
																			</a>	
																	</div>
																	<div class="table-cell">
																		<standardCart:productDescription entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="true"  showStatus="false" />
																	</div>
																</div>
														
															<div id="collapse${count.count}" class="panel-collapse collapse img-accordian">	
															 <c:url value="/cart/update" var="cartUpdateFormAction" />
															 <form:form id="updateCartForm${entry.entryNumber}_mobile" action="${cartUpdateFormAction}" method="post"	commandName="updateQuantityForm${entry.entryNumber}">
																<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
																<input type="hidden" name="productCode"		value="${entry.product.code}" />
																<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
															    												
																<p><spring:message code="cart.shipping.quantity"/></p>
																		<div class="cart-update-link">
																			<a href="javascript:void();" id="quantity${entry.entryNumber}_mobile_update"	entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile">
																				<spring:message code="cart.review.productDesc.updateItem" />
																			</a>
																		</div>
																<div>
																	<ycommerce:testId code="cart_product_quantity">
																		<form:label cssClass="skip" path="quantity"	for="quantity${entry.entryNumber}">
																		</form:label>
																		<form:input disabled="${not entry.updateable}" type="text" id="quantity${entry.entryNumber}_mobile"	entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity" />
																	</ycommerce:testId>
																</div>
							
																<p class="thirdline">
																	<strong> <spring:message code="product.detail.addToCart.unit" />
																	</strong>${entry.product.deliveryUnit}(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
																</p>
							
																<ycommerce:testId code="cart_product_removeProduct">
																	<p>
																		<a href="javascript:void(0);" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
																		 <spring:message code="cart.review.productDesc.removeItem" />
																		</a>
																	</p>
																</ycommerce:testId>
																</form:form>
																<p><spring:message code="cart.shipping.itemPrice"/></p>
																<p></p>
																<p><spring:message code="cart.shipping.total"/></p>
																<p></p>
																
															</div>	
														</td>
													</tr>
													</c:forEach>	
												</tbody>
											</table>
										</div>	

		<!--Accordian Ends here -->

		<!-- Start - Total Price Summary -->
		<div class="row basecontainer boxshadow">
			<table class="total-summary-table">
				<tr>
					<td colspan="2" class="total-summary-label"><spring:message code="cart.shipping.SubTotal"/></td>
					<td class="total-summary-cost totalrps no-right-pad"></td>
				</tr>

			</table>


		</div>
	</div>
	<!-- End - Total Price Summary -->

	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
				<button type="button" class="btn btnclsnormal checkout-clear-cart"	id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button>
				<div class="empty-btn">
					<cms:pageSlot position="BuildOrderCatalog" var="feature"
						element="div">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
				<div class="float-right-to-none">
					<!-- <a href="#" type="button" class="btn btnclsnormal btn-gap">Save as template</a> -->
					<c:set value="saveorderastemplate" var="classForSaveTemplate" />
					<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}">
						<spring:message code="cart.review.cartPageAction.saveTemplate" />
					</button>
					<button type="button"
						class="btn btnclsactive  cartvalidateInternational">
						<spring:message code="cart.review.progressBar.validate" />
					</button>
				</div>
		</div>

	</div>