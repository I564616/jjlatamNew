<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="deliveredCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/delivered"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<c:url value="/cart/deliveredOrderFileUpload" var="DeliveredOrderFileUpload" />

<script type="text/javascript"> // set vars
/*<![CDATA[*/
var cartRemoveItem = true;
/*]]>*/

</script>

<form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll"
	method="post" commandName="UpdateMultipleEntriesInCartForm">
</form:form>
<c:url value="/cart/changeOrderType" var="changeOrderTypeURL" />
<form:form method="post" action="${changeOrderTypeURL}" id="changeOrderTypeForm"></form:form>
<c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="hideSelectDropShip" />
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL" />
<div id="AddItemsCartpage">
	<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	<div class="row content">
		<div class="col-lg-6 col-md-6 col-sm-5 col-xs-12">
			<spring:message code="cart.review.shoppingCart" />
		</div>
		<div class="col-lg-6 col-md-6 col-sm-7 col-xs-12">
			<div class="float-right-to-none">
				<div class="orderType-holder">
					<span class="orderType-label"> <spring:message
							code="cart.common.orderType" />
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
				<button type="button"
					class="btn btnclsactive cartDeliveryValidate">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
			</div>

		</div>
	</div>
	<c:if test="${not empty validationError}">
		<div class="error">

			<%-- <p style="color:red">
								${validationErrorMsg}
						</p> --%>

			<p style="color: red">
				<spring:message code="dropshipment.error.not.found" />
			</p>
		</div>
	</c:if>
	
	<!--4069 story changes starts-->
	<c:if test="${not empty priceError}">
		<div class="error">


			<p style="color: red">
				<spring:message code="cart.common.zeroPrice.error" />				
			</p>
		</div>
	</c:if>
	<!--4069 story changes ends-->
	
	<!-- flash message for contract product -->
	<div class="panel-group contract-product-show"
		style="margin-bottom: 20px">
		<div class="panel panel-success">
			<div class="panel-heading">
				<h4 class="panel-title">
					<spring:message code="cart.review.popup.text" />
					:&nbsp;<span id="contract_product_msg"></span>
				</h4>
			</div>
		</div>
	</div>
	 <div id="globalMessages">
				<common:globalMessages />
				<cart:cartRestoration />
				<cart:cartValidation />
			</div>	
	<div class="mainbody-container">
		<input type="hidden" name="makeThisAddrDefaultChk"
			id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}" /> <input
			type="hidden" name="defaultChekAddid" id="defaultChekAddid"
			value="${defaultChekAddid}">
		<!-- Added for Shipping address Display in Validation page -->

		<commonTags:Addresses />
		<form name="headervalidateCartForm" id="headervalidateCartForm"
			action="javascript:;">
			<deliveredCart:delOrderHeader />
		</form>
		<div id="surgeryInfoPopupHolder"></div>
		<div class="cartpage-surgeon" id="surgeonPopupHolder"></div>
		<deliveredCart:uploadDeliveredOrderPopUp/>
	 
	<commonTags:cartErrors />
	<commonTags:changeAddressDiv />
	<commonTags:changeBillToAddress />
	<div class="row jnjPanelbg">
		<form name="mltiAddToCartForm" id="mltiAddToCartForm"
			action="javascript:;">
			<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="enter-product-label">
					<spring:message code="cart.enterproducts.header" />
				</div>
				<div class="enter-product-label-disc">
					<spring:message code="cart.commaseperated.text" />
				</div>
			</div>
			<div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle txt-box-mobie">
				<div class="float-right-to-none">
					<div class="price-txt-width">
						<input type="text" id="prodCode" class="form-control"
							placeholder='<spring:message code="cart.productnum.text"/>'></input>
					</div>
					<div class="price-quantity">
						<input type="text" id="prodQty" class="form-control"
							placeholder='<spring:message code="product.detail.addToCart.quantity"/>'></input>
					</div>
				</div>

			</div>
			<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="full-width-btns">
					<input type="button" class="btn btnclsnormal new-add-tocart"
						id="addToCartForm_2"
						value="<spring:message code='homePage.addtocart' />"> <input
						type="button" style="" id="errorMultiCart"
						class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn"
						value="<spring:message code='homePage.errorDetails' />" />
					<!-- 					<button class="btn btnclsnormal full-width-btns" >Add to Cart</button> -->
					<!-- 					<button class="btn btnclsnormal" id="error-detail-btn" data-target="#error-detail-popup" data-toggle="modal">Error Details</button> -->
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"
				style="margin-top: 8px">
				<div class="registersucess registersucess-style"></div>
			</div>
		</form>
	</div>

	<!-- Desktop -->

	<div class="hidden-xs ">
		<table id="datatab-desktop"
			class="table table-bordered table-striped sorting-table-lines error-on-top">
			<thead>
				<tr>
					<th class="no-sort text-left text-uppercase">#</th>
					<th class="no-sort text-left"><spring:message
							code="cart.validate.product" /></th>
					<th class="no-sort"><spring:message
							code="cart.review.entry.quantity" />
						<div class="cart-update-all-link">
							<a class="cartUpdateAllbtn cartUpdateAllbutton"
								id="cartUpdateAllbutton" href="javascript:;"><spring:message
									code="cart.review.entry.Updateall" /></a>
						</div></th>
					<th class="no-sort"><spring:message
							code="cart.validate.unitPrice" /></th>
					<th class="no-sort multitotal-head paddingleft10px"><spring:message
							code="cart.review.entry.total" /></th>
				</tr>
			</thead>

			<!-- Change for GTUX_1259  for recently added product to top -->

			<tbody id="AddItemsCartpage">
				<c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
				<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
					<tr id="orderentry- ${cartData.entries[fieldLength - count.count]}"
						class="shoppingcartOrderEntryList">
						<td class="text-left">${count.count}</td>
						<td class="text-left"><standardCart:productDescriptionBeforeValidation
								entry="${cartData.entries[fieldLength - count.count]}"
								errorCode="${validationErrorMsg}" priceError="${priceValidationErrorMsg}" showRemoveLink="true"
								showStatus="false" rowcount="${count.count}" /></td>
						<td>
							<div class="cart-update-link">
								<a href="javascript:void();"
									id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_update"
									entryNum="${cartData.entries[fieldLength - count.count].entryNumber}"
									class="qtyUpdateTextBox1"><spring:message
										code="cart.review.productDesc.updateItem" /></a>
							</div> <c:url value="/cart/update" var="cartUpdateFormAction" /> 
							<form:form
								id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_desktop"
								action="${cartUpdateFormAction}" method="post"
								commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
								<input type="hidden" name="entryNumber"
									value="${cartData.entries[fieldLength - count.count].entryNumber}" />
								<input type="hidden" name="productCode"
									value="${cartData.entries[fieldLength - count.count].product.code}" />
								<input type="hidden" name="initialQuantity"
									value="${cartData.entries[fieldLength - count.count].quantity}" />
								<div class="text-center">
									<ycommerce:testId code="cart_product_quantity">
										<form:label cssClass="skip" path="quantity"
											for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
										<form:input
											disabled="${not cartData.entries[fieldLength - count.count].updateable}"
											type="text"
											id="quantity${cartData.entries[fieldLength - count.count].entryNumber}"
											entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}"
											class=" qtyUpdateTextBox form-control txtWidth"
											path="quantity" />
									</ycommerce:testId>
								</div>
								<p class="thirdline text-center">
									<strong><spring:message
											code="product.detail.addToCart.unit" /> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit}
									(${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})
								</p>
								<ycommerce:testId code="cart_product_removeProduct">
									<p class="text-center">
										<a href="javascript:void();"
											id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
											class="smallFont submitRemoveProduct"><spring:message
												code="cart.review.productDesc.removeItem" /> </a>
									</p>
								</ycommerce:testId>
								<p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p>
							</form:form>
							<div class="special-stock">
								<form class="splStockPartnerForm_desktop" 
									name="specialStockPartForm" action="javascript:;"
									id="sspForm_desktop_${entry.entryNumber}">

									<div class="special-stockrow">
										<div class="text-left">
											<spring:message code="cart.review.entry.specialSP" />
											<span class="redStar">*</span>
										</div>
										<div>
											<input type='text'
												class='form-control full-width-txt specialStockPartnerEntry stockPartnerError'
												id="specialStock_desktop_${entry.entryNumber}" name="sspNumber"
												value="${entry.specialStockPartner}" data="${entry.entryNumber}"
												data-msg-required="<spring:message code="cart.required.field"/>" />
		
										</div>
										<div class="registerError text-left"></div>
									</div>									
									<c:set var="errorMsg"><spring:message code="cart.valid.stockPartner.number"/></c:set>
									<input id="stockErrorMsg" type="hidden" value="${errorMsg}"/>
								</form>
								<c:if test="${cartData.entries[fieldLength - count.count].product.mddSpecification.inFieldInd}">
									<form class="orderEntry_desktop" id="lotNoForm_desktop_${entry.entryNumber}" name="lotNoForm"
										action="javascript:;">
										<input type="hidden" id="productCode_${entry.entryNumber}"
											name="productCode_${entry.entryNumber}"
											value="${cartData.entries[fieldLength - count.count].product.code}" />
										<div class="special-stockrow">
											<div class="text-left">
												<spring:message code="cart.review.entry.lotNumber" />
												<span class="redStar">*</span>
											</div>
											<div>
												<input type='text' value="${entry.lotNumber}"
												class='form-control full-width-txt lotNumberEntry'
												title="<spring:message code="cart.common.code.enter"/>" id="lotNumber_desktop_${entry.entryNumber}"
												name="lotNo" data="${entry.entryNumber}"
												data-msg-required="<spring:message code="cart.required.field"/>" />
											</div>
											
											<div class="registerError redStar text-left"></div>
										</div>
										<c:set var="errorMsgLot"><spring:message code="cart.return.lotNumberNotFound"/></c:set>
										<input id="lotErrorMsg" type="hidden" value="${errorMsgLot}"/>
									</form>
								</c:if>
							</div>
						</td>
						<td></td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- Error Details Popup Start -->

	<div class="modal fade jnj-popup" id="error-detail-popup" role="dialog">
		<div class="modal-dialog modalcls modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close clsBtn" data-dismiss="modal">
						<spring:message code="cart.review.close" />
					</button>
					<h4 class="modal-title">
						<spring:message code="homePage.errorDetails" />
					</h4>
				</div>
				<form:form method="post" action="javascript:;">
					<div class="modal-body">
						<div class="row">
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="panel-group">
									<div class="panel panel-danger">
										<div class="panel-heading">
											<h4 class="panel-title cart-error-msg">
												<span><span class="glyphicon glyphicon-ban-circle"></span>&nbsp;<spring:message
														code="homePage.errordetails.addfailed" /></span>
											</h4>
										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<div class="scroll error-content" style="font-weight: bold">
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<!-- Error Details Popup End -->

	<!-- Table collapse for mobile device-->
	<input type="hidden" value="" id="currViewScreenName" />
	<div class=" Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
		<table id="datatab-mobile"
			class="table table-bordered table-striped sorting-table-lines error-on-top">
			<thead>
				<tr>
					<th class="no-sort text-left"><spring:message
							code="cart.validate.product" />
					<th class="no-sort"><spring:message
							code="cart.review.entry.quantity" />
						<div class="cart-update-all-link">
							<a class="cartUpdateAllbtn cartUpdateAllbutton"
								id="cartUpdateAllbutton" href="javascript:;"><spring:message
									code="cart.review.entry.Updateall" /></a>
						</div>
						</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
				<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
					<tr>
					
						<td class="text-left valign-top">
							
								<div class="display-row">
									<div class="table-cell jnj-toggle-sign valign-top">
										<a href="#collapse${count.count}" data-toggle="collapse" data-parent="#accordion" class="toggle-link panel-collapsed">
											<span class="glyphicon glyphicon-plus"></span>
											</a>	
									</div>
									<div class="table-cell valign-top">
										<standardCart:productDescription entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="true" showStatus="false" />
									</div>
								</div>
						
							<div id="collapse${count.count}" class="panel-collapse collapse img-accordian" style="margin-top:10px">	
							 <c:url value="/cart/update" var="cartUpdateFormAction" />
							 <div class="mobi-row-gap">
								 
									<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"		value="${cartData.entries[fieldLength - count.count].product.code}" />
									<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
							
							<div class="mobi-row-gap">
								<p><spring:message code="cart.validate.unitPrice"/></p>
								<p></p>
							</div>
							<div class="mobi-row-gap-last">
								<p><spring:message code="cart.shipping.total"/></p>
								<p></p>
							</div>
							</div>
							</div>
							</td>
								<td class="text-left">
								<form:form id="updateCartForm${entry.entryNumber}_mobile" action="${cartUpdateFormAction}" method="post"	commandName="updateQuantityForm${entry.entryNumber}">										
									<input type="hidden" name="entryNumber"	value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"		value="${cartData.entries[fieldLength - count.count].product.code}" />
									<input type="hidden" name="initialQuantity"	value="${entry.quantity}" />
									
									<p><spring:message code="cart.shipping.quantity"/></p>
									<div class="cart-update-link">
										<a href="javascript:void();" id="quantity${entry.entryNumber}_mobile_update"	entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile">
											<spring:message code="cart.review.productDesc.updateItem" />
										</a>
									</div>
									<div>
										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity"	for="quantity${entry.entryNumber}" style="display:none">
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
									
									<div class="special-stock">
									<form class="splStockPartnerForm_mobile" name="specialStockPartForm" action="javascript:;" id="sspForm_mobile_${entry.entryNumber}">
										<input type="hidden" id="productCode_${entry.entryNumber}"
												name="productCode_${entry.entryNumber}"
												value="${cartData.entries[fieldLength - count.count].product.code}" />
										<div class="special-stockrow">
											<div class="text-left">
												<spring:message code="cart.review.entry.specialSP" />
												<span class="redStar">*</span>
											</div>
											<div>
											<input type='text'
												class='form-control full-width-txt specialStockPartnerEntry stockPartnerError'
												id="specialStock_mobile_${entry.entryNumber}" name="sspNumber"
												value="${entry.specialStockPartner}" data="${entry.entryNumber}"
												data-msg-required="<spring:message code="cart.required.field"/>" />
		
										</div>
										<div class="registerError text-left"></div>
										</div>									
										<c:set var="errorMsg"><spring:message code="cart.valid.stockPartner.number"/></c:set>
										<input id="stockErrorMsg" type="hidden" value="${errorMsg}"/>
										
										
										
									</form>
									<c:if test="${entry.product.mddSpecification.inFieldInd}">
										<form class="orderEntry_mobile" id="lotNoForm_mobile-${entry.entryNumber}" name="lotNoForm" action="javascript:;">
											<input type="hidden" id="productCode_${entry.entryNumber}"
												name="productCode_${entry.entryNumber}"
												value="${cartData.entries[fieldLength - count.count].product.code}" />
											<div class="special-stockrow">
												<div class="text-left">
													<spring:message code="cart.review.entry.lotNumber" />
													<span class="redStar">*</span>
												</div>
												<div>
													<input type='text' value="${entry.lotNumber}"
													class='form-control full-width-txt lotNumberEntry'
													title="<spring:message code="cart.common.code.enter"/>" id="lotNumber_mobile_${entry.entryNumber}"
													name="lotNo" data="${entry.entryNumber}"
													data-msg-required="<spring:message code="cart.required.field"/>" />
												</div>
												
												<div class="registerError text-left redStar"></div>
											</div>
											<c:set var="errorMsgLot"><spring:message code="cart.return.lotNumberNotFound"/></c:set>
											<input id="lotErrorMsg" type="hidden" value="${errorMsgLot}"/>
										</form>
									</c:if>
								</div>
								
							
							
						</td>
						
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


<!--  Changes Made for PurChase Order Pop Up -->
<div class="modal fade jnj-popup" id="validateOrderDivId-popup"
	role="dialog">
	<div class="modal-dialog modalcls modal-md" id="validateOrderPOpopup">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close"></spring:message></button>
				<h4 class="modal-title">
					<spring:message code="cart.review.validateOrder" />
				</h4>
			</div>
			<div class="modal-body">
				<spring:message code="cart.review.poNumber" />
			</div>
			<div class="modal-footer">
				<c:url value="/cart/validate" var="orderValidateUrl" />
				<a href="#" class="pull-left" data-dismiss="modal"><spring:message
							code="orderHistoryUpdateSurgeonpPopup.cancel" /></a>
				<button type="button" class="btn btnclsactive pull-right"
					data-dismiss="modal" onclick="location.href='${orderValidateUrl}'"><spring:message code="cart.review.validateOrder"></spring:message></button>
			</div>
		</div>
	</div>
</div>
<!--  Changes Made for PurChase Order Pop Up -->

</div>
</div>
<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
				<button type="button" class="btn btnclsnormal checkout-clear-cart"	id="RemoveCartData">
					<spring:message code="cart.payment.clearCart" />
				</button>
				<div class="empty-btn continue-shop" >
	 <cms:pageSlot position="BuildOrderCatalog" var="feature" element="div">
												<cms:component component="${feature}"/>
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
					class="btn btnclsactive cartDeliveryValidate">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
				</div>
		</div>

	</div>



