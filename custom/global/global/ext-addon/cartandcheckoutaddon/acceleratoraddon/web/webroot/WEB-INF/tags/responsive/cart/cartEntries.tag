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
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<c:url value="/cart/simulateOrderSecondSAPCall" var="sapSecondCallFormUrl"/>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%> 
<script type="text/javascript"> // set vars
/*<![CDATA[*/
var cartRemoveItem = true;
/*]]>*/

</script>

 <form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
 <c:if test="${fn:length(dropShipAccounts) eq 0}">
	<c:set value="strictHide" var="hideSelectDropShip"/>
</c:if>
<c:url value="/cart/getDropShipAccounts" var="dropShipURL"/>
<c:url value="/cart/changeOrderType" var="changeOrderTypeURL" />
<form:form method="post" action="${changeOrderTypeURL}" id="changeOrderTypeForm"></form:form>
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
				<!-- No charge Order AAOL-3392 -->
				<c:choose>
					<c:when test="${cartorderType eq 'ZNC' }">
					<button type="button"
					class="btn btnclsactive cartStep1Saveupdate1NoCharge">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
					</c:when>
				<c:otherwise>
		 <button type="button" class="btn btnclsactive cartStep1Saveupdate1"><spring:message code="cart.review.progressBar.validate"/></button>
				
				</c:otherwise>
				</c:choose>
				<!-- No charge Order AAOL-3392 -->
			</div>
		</div>
	</div>
	<c:if test="${not empty validationError}">
						<div class="error">
						
						<%-- <p style="color:red">
								${validationErrorMsg}
						</p> --%>
						
						<p style="color:red">
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
	<!--4069 story changes starts-->
	
	<!-- flash message for contract product -->
<div class="panel-group contract-product-show" style="margin-bottom:20px" >
	<div class="panel panel-success">
		<div class="panel-heading">
			<h4 class="panel-title">
			<spring:message code="cart.review.popup.text"/>:&nbsp;<span id="contract_product_msg"></span> 
			</h4> 
		</div>
	</div>
</div>
<div class="mainbody-container">
<input type="hidden" name="makeThisAddrDefaultChk" id="makeThisAddrDefaultChk" value="${makeThisAddrDefaultChk}"/>
 <input type="hidden" name="defaultChekAddid" id = "defaultChekAddid" value="${defaultChekAddid}">
<!-- Added for Shipping address Display in Validation page -->
<div id="globalMessages">
				<common:globalMessages />
				<cart:cartRestoration />
				<cart:cartValidation />
			</div>	
<commonTags:Addresses/>
<!-- No charge Order AAOL-3392 -->
		<c:if test="${cartorderType eq 'ZNC' }">
			<div class="row jnjPanelbg">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="row">
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
						
							<div class="report-label">
								<spring:message code="cart.common.purchaseOrder"/><span class="redStar">*</span>
							</div>
							<div class="report-form-field">
								<input id="purchOrder" maxlength="35" type="text" class="form-control  ${disabled}" value="${cartData.purchaseOrderNumber}"/>
								<div class="registerPOError" style="color: red;display: none"></div>
							</div>
							
							<%-- <div class="display-table-full">
								<div class="display-table-row">
									<div class="txt-label-inline no-charge-reason"><spring:message code="cart.common.purchaseOrder"/><span class="redStar">*</span></div>
				
									<div class="txt-box-inline special-instr-txt po-padding">
										
											<input id="purchOrder" type="text"
												class="form-control  ${disabled}" value="${cartData.purchaseOrderNumber}"/>
										
									</div>
								</div>
								<div class="display-table-row">
									<div class="txt-label-inline hidden-xs"></div>
									<div class="registerPOError txt-box-inline" style="color: red;display: none"></div>
								</div>
							</div>	 --%>	
						</div>				
						<div class="col-lg-7 col-md-7 col-sm-7 col-xs-12 margin-row-gap-mobi">
							<div class="report-label">
								<spring:message code="cart.noCharge.orderInfo.noChargeReasonCode"/><span class="redStar">*</span>
							</div>
							<div class="report-form-field">
								<select class="selectnoChargepicker" data-width="100%" id="noChargeReasonCode" name="noChargeReasonCode">
									<option value="">
											<spring:message code="cart.review.orderInfo.selectReason" />
									</option>
									<c:forEach var="reasonCode" items="${reasonCodeNoCharge}">
										<option value="${reasonCode.key}"   ${reasonCode.key == cartData.reasonCode ? 'selected="selected"' : ''}   >${reasonCode.value}</option>
									</c:forEach>
								</select>
								<div class="registerReError" style="color: red;"></div>
							</div>
							<%-- 
							<div class="display-table-full">
								<div class="display-table-row">
									<div class="txt-label-inline no-charge-reason"><spring:message code="cart.noCharge.orderInfo.noChargeReasonCode"/><span class="redStar">*</span></div>
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
								</div>		
								<div class="display-table-row">
									<div class="txt-label-inline hidden-xs"></div>
									<div class="registerError txt-box-inline" style="color: red;"></div>
								</div>	
							
							</div> --%>
						</div>
					</div>
					<div class='row'>
						<div class="col-lg-5 col-md-5 col-sm-5 col-xs-12 margin-top-row-gap">
							<div class="report-label">
								<spring:message code="cart.common.dropShipAcc"/>
							</div>
							<div class="report-form-field">
								<input type="text" class="form-control" id="dropShip" value="${cartData.dropShipAccount}">
								<a href="#"  data-toggle="modal" >
									<span class="changeShipAddLighboxLink" data-url="${dropShipURL}"> 
										<button class="drop-ship-account-list-icon ${hideSelectDropShip}  fa fa-list" id="drop-ship-account-list-icon"></button>
									</span>
								</a>
								<div id="errorMsgDiv" class="registerError" style="color: red;"></div>
							</div>
							<%-- <div class="display-table-full">
								<div class="display-table-row">
									<div class="txt-label-inline dropshipAccntLabel"><spring:message code="cart.common.dropShipAcc"/></div>
									<div class="table-cell-toblock">
										<div class="txt-box-inline  display-table-cell full-width">
											<input type="text" class="form-control" id="dropShip" value="${cartData.dropShipAccount}">
											                                            
									 	</div>
										
										<div class="txt-box-inline display-table-cell"> 
										<a href="#"  data-toggle="modal" >
										<span class="changeShipAddLighboxLink" data-url="${dropShipURL}"> 
											<button class="drop-ship-account-list-icon ${hideSelectDropShip}  fa fa-list" id="drop-ship-account-list-icon"></button>
											
											</span>
											</a>
										</div>
							    </div>		
							   <div class="display-table-row">
									<div class="txt-label-inline hidden-xs"></div>
									<div id="errorMsgDiv" class="registerError txt-box-inline" style="color: red;"></div>
								</div>	 
							</div>
						</div>	 --%>								
										</div>	
				</div>	
			</div>			</div>
										</c:if>
										<div id="dropShipAccountholder"></div>
				
				
				
				
				
				
				
			</div>

			

<!-- No charge Order AAOL-3392 -->
<commonTags:cartErrors/>
<commonTags:changeAddressDiv/> 
<commonTags:changeBillToAddress/>
		<div class="row jnjPanelbg">	     
		<div id="noProduct" style="display:none;color:red"><spring:message	code="cart.productnum.empty" /></div>    
		<div id="noQty" style="display:none;color:red"><spring:message	code="cart.incorrect.Qty.empty" /></div>      
	     <form name="mltiAddToCartForm" id="mltiAddToCartForm" action="javascript:;">
	     	<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="enter-product-label"><spring:message
									code="cart.enterproducts.header" /></div>
				<div class="enter-product-label-disc"><spring:message
									code="cart.commaseperated.text" /></div>		
			</div>
			<div class="col-lg-6 col-md-6 col-sm-4 col-xs-12 align-middle marginProd">
				<%-- <label class="price-no-label"><spring:message
						code="cart.quote.product.number" /></label><input type="text" id ="prodCode" class="form-control price-no-txt"></input><input type="text" id ="prodQty" class="form-control price-noqty-txt"></input>
			 --%>
				<div class="float-right-to-none">
		             <%-- <label class="product-numbers"><spring:message
						code="cart.quote.product.number" /></label> --%>
		             <div class="price-txt-width">
		             	 <input type="text" id ="prodCode"  class="form-control" placeholder="<spring:message code='cart.productnum.text' />"></input>
		             </div> 
		             <div class="price-quantity"> 
		             	 <input type="text"  id ="prodQty" class="form-control" placeholder="<spring:message code='product.detail.addToCart.quantity' />"></input>
		             </div> 
		         </div> 	
			 </div>
			<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
				<div class="full-width-btns">
					<input type="button" class="btn btnclsnormal new-add-tocart" id="addToCartForm_2" value="<spring:message code='homePage.addtocart' />">
					<input type="button" style="" id="errorMultiCart" class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn" value="<spring:message code='homePage.errorDetails' />" />
					<!-- Error Details Button appears after reload the cart page 556-->
					<input type="hidden" value="${errorDetailMap}" id="errorDetailMSG"/>	
<!-- 					<button class="btn btnclsnormal full-width-btns" >Add to Cart</button> -->
<!-- 					<button class="btn btnclsnormal" id="error-detail-btn" data-target="#error-detail-popup" data-toggle="modal">Error Details</button> -->
				</div>
			</div>	
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top:8px">
				<div class="registersucess registersucess-style"></div>
			</div>
</form>			
	 </div>	
	 
<!-- Desktop -->

	<div class="hidden-xs ">
	
	<table id="datatab-desktop"	class="table table-bordered table-striped sorting-table-lines error-on-top">
		<thead>
			<tr>
			<th class="no-sort text-left" style="width: 60px"><spring:message code="cart.review.entry.number"/></th>
				<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
				<th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
					 <div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
				</th>
				<th class="no-sort"><spring:message code="cart.validate.unitPrice"/></th>
				<th class="no-sort multitotal-head paddingleft10px"><spring:message code="cart.review.entry.total"/></th>
			</tr>
		</thead>
		
		<!-- Change for GTUX_1259  for recently added product to top -->
		
		<tbody id="AddItemsCartpage" >
			<c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
	 		<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
			<tr id="orderentry- ${cartData.entries[fieldLength - count.count]}" class="shoppingcartOrderEntryList">
			<td>${count.count}</td>
				<td class="text-left">
					<standardCart:productDescriptionBeforeValidation entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}" priceError="${priceValidationErrorMsg}" showRemoveLink="true"  showStatus="false"  rowcount="${count.count}"/>
				</td>
				<td>
				 	<div class="cart-update-link">  <a href="javascript:void();" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_update" entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1" ><spring:message code="cart.review.productDesc.updateItem"/></a></div> 
           		   <c:url value="/cart/update" var="cartUpdateFormAction" />
						<form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_desktop" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
							<input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}"/>
							<input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}"/>
							<input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}"/>
							<div>
								<ycommerce:testId code="cart_product_quantity">
						    		<form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
									<form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}"  type="text" 
									 id="quantity${cartData.entries[fieldLength - count.count].entryNumber}" 
									 entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" 
									 class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
								</ycommerce:testId>
							</div>                              
		         		<p class="thirdline"><strong><spring:message code="product.detail.addToCart.unit"/> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit} (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})</p> 
							<ycommerce:testId code="cart_product_removeProduct">
								<p>
									<a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
										class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/>
									</a>
								</p>
							</ycommerce:testId> 
							<p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p>
						</form:form>
		    	</td>	
				<td></td>
				<td></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

	<!-- Error Details Popup Start -->

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
												<h4 class="panel-title cart-error-msg">
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
		<!-- Error Details Popup End -->
		
		<%-- <!-- Table collapse for mobile device-->
			<div class=" Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
				<table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines error-on-top" >
					<thead>
						<tr>
							<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
							<th class="no-sort"><spring:message code="cart.review.entry.quantity"/>
						 		<div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
							</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
					<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
						<tr>
						 <form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_mobile" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
							<td class="text-left">
								<standardCart:productDescriptionBeforeValidation entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}"  showRemoveLink="true" showStatus="false" rowcount="${count.count}"/>
								
								<div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<div class="sub-details-row">
										<form>
											<input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}"/>
											<input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}"/>
											<input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}"/>
											<!-- qty -->                             
									</div>
									<p><spring:message code="cart.validate.unitPrice"/></p>
									<p></p>
									<p><spring:message code="cart.review.entry.total"/></p>
									<p></p>
								</div>	
							</td>
							<td>
								 <div class="cart-update-link">  <a href="#" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile_update" entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile" ><spring:message code="cart.review.productDesc.updateItem"/></a></div> 
								<ycommerce:testId code="cart_product_quantity">
							   	<form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"></form:label>
									<form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}"  type="text"  id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile" 
									entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" 
									class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
								</ycommerce:testId> 
								<p><strong><spring:message code="product.detail.addToCart.unit"/> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit} (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})</p> 
								<ycommerce:testId code="cart_product_removeProduct">
									<a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}" class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/></a>
								</ycommerce:testId>
								<p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p> 
							</td>
							</form:form>
						</tr>
					</c:forEach>
					</tbody>
				</table>
		</div>	 --%>
		<div class=" Subcontainer visible-xs hidden-lg hidden-sm hidden-md">
					<table id="datatab-mobile" 
					class="table table-bordered table-striped sorting-table-lines error-on-top" >
					<thead>
						<tr>
							<th class="no-sort text-left"><spring:message code="cart.validate.product"/>
													 		<div class="cart-update-all-link"><a class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div> 
							</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="fieldLength" value="${fn:length(cartData.entries)}"/>
					<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
						<tr>
							<td class="text-left">
								<standardCart:productDescriptionBeforeValidation entry="${cartData.entries[fieldLength - count.count]}" errorCode="${validationErrorMsg}"  priceError="${priceValidationErrorMsg}" showRemoveLink="true" showStatus="false" rowcount="${count.count}"/>
								
								<div id="mobi-collapse${count.count}"
									class="panel-collapse collapse img-accordian">
									<p>
										<spring:message code="cart.validate.quantityQty" />
									</p>
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									 <form:form id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_mobile" action="${cartUpdateFormAction}" method="post" 
									 commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
											<input type="hidden" name="entryNumber" value="${cartData.entries[fieldLength - count.count].entryNumber}"/>
											<input type="hidden" name="productCode" value="${cartData.entries[fieldLength - count.count].product.code}"/>
											<input type="hidden" name="initialQuantity" value="${cartData.entries[fieldLength - count.count].quantity}"/>
														 <div class="cart-update-link">  <a href="#" id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile_update" entryNum="${cartData.entries[fieldLength - count.count].entryNumber}" class="qtyUpdateTextBox1 click_updateBtn_mobile" ><spring:message code="cart.review.productDesc.updateItem"/></a></div> 
						<ycommerce:testId code="cart_product_quantity">
							   	<form:label cssClass="skip" path="quantity" for="quantity${cartData.entries[fieldLength - count.count].entryNumber}"><spring:theme code="basket.page.quantity" /></form:label>
									<form:input disabled="${not cartData.entries[fieldLength - count.count].updateable}"  type="text"  id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile" 
									entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}" 
									class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
								</ycommerce:testId> 
								<p><strong><spring:message code="product.detail.addToCart.unit"/> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit} (${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})</p> 
								<ycommerce:testId code="cart_product_removeProduct">
									<a href="javascript:void();" id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}" class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/></a>
								</ycommerce:testId>
 				<p class="msgHighlight">${entry.product.hazmatCode}</p>
									</form:form>
									<p>
										<spring:message code="cart.validate.unitPrice" />
									</p>
									<p>
										<format:price priceData="${entry.basePrice}" />
									</p>
									<p>
										<spring:message code="cart.review.entry.total" />
									</p>
									<p>
										<ycommerce:testId code="cart_totalProductPrice_label">
											<p class="jnjID">
												<format:price priceData="${entry.totalPrice}" />
											</p>
										</ycommerce:testId>
									</p>
								</div></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
		</div>	
		<!-- Start - Total Price Summary -->
	   <div class="row basecontainer">
			<table class="total-summary-table">
					<tr>
						<td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
						<td class="total-summary-cost totalrps no-right-pad">--</td>
					</tr>
					<tr class="summary-bline">
						<td class="total-summary-label"><spring:message code="cart.review.entry.shipping"/></td>
						<td class="total-summary-cost">--</td>
					</tr>
					<tr class="total-price-row">
						<td class="total-summary-label"><spring:message code="cart.validate.total"/></td>
						<td class="total-summary-cost totalsum no-right-pad">
						 <sup class="supmd">--</sup> 
						</td>
					</tr>
				</table>
		</div>
	</div>
	<!-- End - Total Price Summary -->
	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="float-left-to-none">
	  			<button type="button" class="btn btnclsnormal checkout-clear-cart" id="RemoveCartData">
	  			 <spring:message code="cart.payment.clearCart"/></button>
			<div class="empty-btn">
				<cms:pageSlot position="BuildOrderCatalog" var="feature"
					element="div">
												<cms:component component="${feature}"/>
											</cms:pageSlot>
											</div>
	  			
	  		</div> 
			<div class="float-right-to-none">
			 		<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
	  				<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}"><spring:message code="cart.review.cartPageAction.saveTemplate"/></button>
						<c:choose>
					<c:when test="${cartorderType eq 'ZNC' }">
					<button type="button"
					class="btn btnclsactive cartStep1Saveupdate1NoCharge">
					<spring:message code="cart.review.progressBar.validate" />
				</button>
					</c:when>
				<c:otherwise>
	 				<button type="button" class="btn btnclsactive cartStep1Saveupdate1" ><spring:message code="cart.review.progressBar.validate"/></button>
					</c:otherwise>
				</c:choose>
	 		</div>
		</div>
	</div>
<div class="sapSecondCallData">
      <form:form action="${sapSecondCallFormUrl}" method="POST" id="simulateOrderForm" name="simulateOrderForm" commandName="simulateOrderForm">
      </form:form>
    </div>
<!--  Changes Made for PurChase Order Pop Up -->
<div class="modal fade jnj-popup" id="validateOrderDivId-popup" role="dialog">
	<div class="modal-dialog modalcls modal-md" id="validateOrderPOpopup">
		<div class="modal-content">
			<div class="modal-header">
										<button type="button" class="close clsBtn" data-dismiss="modal"><spring:message code="popup.close"/></button>
				<h4 class="modal-title"><spring:message code="cart.review.validateOrder"/></h4>
			</div>
			<div class="modal-body">
				<spring:message code="cart.review.poNumber"/>
			</div>
			<div class="modal-footer">
			<c:url value="/cart/validate" var="orderValidateUrl" />
										<a href="#"  class="pull-left"  data-dismiss="modal"><spring:message code="cart.common.cancel"/></a>
									<button type="button" id="validatePOButtonPopUP"class="btn btnclsactive pull-right" data-dismiss="modal" onclick="location.href='${orderValidateUrl}'"><spring:message code="cart.review.validateOrder"/></button>
			</div>	
		</div>
	</div>
</div>
<!--  Changes Made for PurChase Order Pop Up -->

<!-- AAOL-2405 start-->
<div id="proposed-line-item-holder" style="display: none;"></div>
<!-- AAOL-2405 end-->





