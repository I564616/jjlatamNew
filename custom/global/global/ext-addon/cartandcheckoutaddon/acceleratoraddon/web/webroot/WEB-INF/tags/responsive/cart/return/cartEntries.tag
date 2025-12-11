<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ taglib prefix="label" uri="/WEB-INF/tld/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart" %>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ attribute name="validationPage" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="returnCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/return"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<script type="text/javascript"> // set vars
/*<![CDATA[*/
var cartRemoveItem = true;
/*]]>*/

</script>
<c:if test="${empty cartData.entries || canCheckout}">						
	<c:set value="buttonDisable" var="classForSaveTemplate"/>
	<c:set value="buttonDisable" var="classForValidate"/>
	
						
</c:if>
 <form:form id="UpdateMultipleEntriesInCartForm" action="cart/updateAll" method="post" commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
 


	<div class="row table-padding">
				<div class=" col-lg-12 col-md-12">
				<!-- <div class="col-lg-12 col-md-12"> -->

					<div class="row subcontent2">
						
							<div id="noProduct" style="display: none; color: red">
								<spring:message code="cart.productnum.empty" />
							</div>
							<div id="noQty" style="display: none; color: red">
								<spring:message code="cart.incorrect.Qty.empty" />
							</div>
							<form name="mltiAddToCartForm" id="mltiAddToCartForm"
								action="javascript:;">
							<!-- new code start-->
							<div class="form-group col-lg-12 col-md-12">	
							<div class="form-group col-lg-5 col-md-6 col-sm-5 col-xs-12">
								<label for="prodCode" class="report-label"><spring:message
											code="cart.return.addreturnProduct"></spring:message></label> 
								<div class="report-form-field">
									<input type="text" class="form-control" id="prodCode">
								</div>			
								
							</div>
								
							<%-- 	<div class="form-group col-lg-6 col-md-6 col-sm-6 col-xs-12">
									<label for="prodCode" class="prodctCodelabel"><spring:message
											code="cart.return.addreturnProduct"></spring:message></label> <input type="text"
										class="form-control prodctCodeTxt" id="prodCode">
								</div> --%>
								<div class="form-group col-lg-5 col-md-5 col-sm-4 col-xs-12">
									<label for="prodQty" class="reason-code-label"><spring:message
											code="cart.common.qty"></spring:message></label>
									<input type="text" class="form-control reason-code-field"
										id="prodQty" value="">
								</div>
								<div class="cartbtn col-lg-2 col-md-2 col-sm-3 col-xs-12">
									<button type="button"
										class="btn btnclsnormal new-add-tocart"
										id="addToCartForm_2"><spring:message
											code="cart.return.addToCart"></spring:message></button>
									<input type="button" style="" id="errorMultiCart"
										class="tertiarybtn homeCartErrors btn btnclsactive new-error-detail-btn"
										value="<spring:message code='homePage.errorDetails' />" />
									<!-- Error Details Button appears after reload the cart page Apac-UAT-556-->
										<input type="hidden" value="${errorDetailMap}" id="errorDetailMSG"/>	
								</div>
								
								</div><!-- new code end-->
								<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12"
									style="margin-top: 8px">
									<div class="registersucess registersucess-style"></div>
								</div>
							</form>
						</div>
				

				

				<commonTags:cartErrors />
<!-- AAOL-6377 -->
<div id="replacement-line-item-holder" style="display: none;"></div>


				<!-- Desktop -->
				<div class="row hidden-xs ">


 
						<input type="hidden" id="productCode_${entry.entryNumber}" name="productCode_${entry.entryNumber}" value="${entry.product.code}"/>
						<table id="datatab-desktop"	class="table table-bordered table-striped sorting-table-lines error-on-top">

						<thead>
							<tr>

								<th class="no-sort text-left text-uppercase"><spring:message
										code="cart.return.product" /></th>
								<th class="no-sort text-center text-uppercase"><spring:message
										code="cart.common.qty" />
									<div class="cart-update-all-link">
										<a href="#" class="cartUpdateAllbtn cartUpdateAllbutton" id="cartUpdateAllbutton" style="text-transform: initial"><spring:message
											code="cart.return.updateAll"></spring:message></a>
									</div></th>
								<th class="no-sort text-left text-uppercase"><spring:message
										code="cart.return.entries.lotNumber" /><span class="redStar">*</span></th>
								<th class="no-sort text-left text-uppercase"><spring:message
										code="cart.return.entries.poNumber" /></th>
								<th class="no-sort text-left text-uppercase"><span><spring:message
											code="cart.return.entries.invoiceNumber" />
								<!--AAOL-4251 - Sprint 4 demofix  for UI --><span
										id="invoiceStar" class="redStar">*</span></span></th>
							</tr>
						</thead>
						<tbody id="AddItemsCartpage">
						
							<c:if test="${not empty entry.validationErrorKeys && showPreValidationError}">	            	
		            	<div class="registerError cartErrMargin">
		            		<c:forEach items="${entry.validationErrorKeys}" var="errorKey">	            	       	
		            	        <label class="error"><spring:message code="${errorKey}"/></label>	            	        
		            	    </c:forEach>
		            	</div>
	            	</c:if>
							<c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
							<c:forEach items="${cartData.entries}" var="entry"
								varStatus="count">
								<tr
									id="orderentry- ${cartData.entries[fieldLength - count.count]}"
									class="shoppingcartOrderEntryList">
									<td class="text-left"><returnCart:productDescriptionBeforeValidation
											entry="${cartData.entries[fieldLength - count.count]}"
											errorCode="${validationErrorMsg}" priceError="${priceValidationErrorMsg}" showRemoveLink="true"
											showStatus="false" rowcount="${count.count}" /></td>
	                                      <c:if test="${not empty cartData.entries[fieldLength - count.count].validationErrorKeys && showPreValidationError}">	
										   <div class="registerError cartErrMargin">
							            		<c:forEach items="${cartData.entries[fieldLength - count.count].validationErrorKeys}" var="errorKey">	
							           		<c:if test="${errorKey eq 'cart.return.invalid.qty'}"> 
							            		<div class="panel panel-danger">
																	<div class="panel-heading">
																		<h4 class="panel-title" style="font-size:14px;">
																			<spring:message code="cart.return.invalid.qty"/>																		
																		</h4>
																  </div>
																</div>         	       	
							            	        </c:if>              	        
							            	    </c:forEach></div>
							            	   </c:if>
											
											
											
											
											
											
											
											</td>
									<td style="vertical-align: middle;">
										<div class="cart-update-link">
											<a href="javascript:void();"
												id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_update"
												entryNum="${cartData.entries[fieldLength - count.count].entryNumber}"
												class="qtyUpdateTextBox1"><spring:message
													code="cart.review.productDesc.updateItem" /></a>
										</div> <c:url value="/cart/update" var="cartUpdateFormAction" /> <form:form
											id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_desktop"
											action="${cartUpdateFormAction}" method="post"
											commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">
											<input type="hidden" name="entryNumber"
												value="${cartData.entries[fieldLength - count.count].entryNumber}" />
											<input type="hidden" name="productCode"
												value="${cartData.entries[fieldLength - count.count].product.code}" />
											<input type="hidden" name="initialQuantity"
												value="${cartData.entries[fieldLength - count.count].quantity}" />
											<div>
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
											<p class="thirdline">
												<strong><spring:message
														code="product.detail.addToCart.unit" /> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit}
												(${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})
											</p>
											<ycommerce:testId code="cart_product_removeProduct">
												<p>
													<a href="javascript:void();"
														id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
														class="smallFont submitRemoveProduct"><spring:message
															code="cart.review.productDesc.removeItem" /> </a>
												</p>
											</ycommerce:testId>
											<p class="msgHighlight">${cartData.entries[fieldLength - count.count].product.hazmatCode}</p>
										</form:form>
									</td>
								
							
									<td style="vertical-align: top;padding-top:18px;">
									
						<div><form:form id= "returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}" class="returnEntriesRequiredForm"
						action="javaScript:;">
						<input type="hidden" id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].product.code}"/>
											<input type="text" class="form-control textbox-size lotNumber required" id="lotNumber_desktop_${cartData.entries[fieldLength - count.count].entryNumber}" placeholder="Lot Number"
								  	 data="${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].lotNumber}" data-msg-required="<spring:message code="cart.return.lotNumber.enter"/>" />
									 <div class="error hide invalidLotNum" id="invalidLotNum_desktop_${cartData.entries[fieldLength - count.count].entryNumber}"><spring:message code="cart.common.invalid.lot"/>#</div>	
								  	 <div class="registerError"></div>
										</form:form></div>
										
										</td>
									<td style="vertical-align: top;padding-top:18px;"><div><form:form id= "returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}" class="returnEntriesRequiredForm"
						action="javaScript:;">
						<input type="hidden" id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].product.code}"/>
											<input type="text" class="form-control textbox-size poNumber alphanumeric" id="poNumber_desktop_${cartData.entries[fieldLength - count.count].entryNumber}" name="poNumber" 
									data="${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].poNumber}"
												placeholder="<spring:message code='cart.return.entries.poNumber'/>">
												</form:form>
										</div>
										<div class="registerError"></div></td>
									<td style="vertical-align: top;padding-top:18px;"><div><form:form id= "returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}" class="returnEntriesRequiredForm"
						action="javaScript:;">
						<input type="hidden" id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].product.code}"/>
											<input type="text" class="form-control textbox-size returnEntriesRequired invoiceNumber alphanumeric" id="invoiceNumber_desktop_${cartData.entries[fieldLength - count.count].entryNumber}" name="invoiceNumber"
									 data="${cartData.entries[fieldLength - count.count].entryNumber}" value="${cartData.entries[fieldLength - count.count].returnInvNumber}"
									data-msg-required="<spring:message code="cart.return.reasonCode.invoice"/>"
												placeholder="<spring:message code='cart.return.entries.invoiceNumber'/>">
										</form:form></div>
										<c:if test="${not empty cartData.entries[fieldLength - count.count].validationErrorKeys && showPreValidationError}">	
										   <div class="registerError cartErrMargin">
							            		<c:forEach items="${cartData.entries[fieldLength - count.count].validationErrorKeys}" var="errorKey">	
							            		<c:if test="${errorKey eq 'cart.return.invoiceNumber.enter'}">          	       	
									 					<div class="error invalidInvoiceNum" id="invoiceNumber_desktop_${cartData.entries[fieldLength - count.count].entryNumber}"><spring:message code="cart.common.invalid.invoice"/>#</div>	
							            	        </c:if>              	        
							            	    </c:forEach></div>
							            	   </c:if>
																			
										<div class="registerError"></div>
										</td>
									
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
								<button type="button" class="close clsBtn" data-dismiss="modal">
									<spring:message code="cart.review.close" />
								</button>
								<h4 class="modal-title">
									<spring:message code="homePage.errorDetails" />
								</h4>
							</div>
							<form method="post" action="javascript:;">
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
							</form>
						</div>
					</div>
				</div>
				<!-- Error Details Popup End -->

								<div class="row visible-xs hidden-lg hidden-sm hidden-md">
														<input type="hidden" id="productCode_${entry.entryNumber}" name="productCode_${entry.entryNumber}" value="${entry.product.code}"/>

			<table id="datatab-mobile"
				class="table table-bordered table-striped sorting-table-lines returnOrderScreen">

				<thead>
					<tr>
						<th class="no-sort"><spring:message
								code="cart.return.product" />
							<div class="cart-update-all-link">
								<a class="cartUpdateAllbtn cartUpdateAllbutton"
									id="cartUpdateAllbutton" href="javascript:;"><spring:message
										code="cart.review.entry.Updateall" /></a>
							</div></th>
					</tr>

				</thead>
				<tbody>
					<c:if
						test="${not empty entry.validationErrorKeys && showPreValidationError}">
						<div class="registerError cartErrMargin">
							<c:forEach items="${entry.validationErrorKeys}" var="errorKey">
								<label class="error"><spring:message code="${errorKey}" /></label>
							</c:forEach>
						</div>
					</c:if>
					<c:set var="fieldLength" value="${fn:length(cartData.entries)}" />
					<c:forEach items="${cartData.entries}" var="entry"
						varStatus="count">
						<tr id="orderentry- ${cartData.entries[fieldLength - count.count]}"
							class="shoppingcartOrderEntryList">
							<td class="text-left"><returnCart:productDescriptionBeforeValidation
									entry="${cartData.entries[fieldLength - count.count]}"
									errorCode="${validationErrorMsg}"  priceError="${priceValidationErrorMsg}" showRemoveLink="true"
									showStatus="false" rowcount="${count.count}" />

								<div id="mobi-collapse${count.count}"
									class="panel-collapse collapse img-accordian">
									<p>
										<spring:message code="cart.validate.quantityQty" />
									</p>
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form
										id="updateCartForm${cartData.entries[fieldLength - count.count].entryNumber}_mobile"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${cartData.entries[fieldLength - count.count].entryNumber}">


										<input type="hidden" name="entryNumber"
											value="${cartData.entries[fieldLength - count.count].entryNumber}" />
										<input type="hidden" name="productCode"
											value="${cartData.entries[fieldLength - count.count].product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${cartData.entries[fieldLength - count.count].quantity}" />


										<div class="cart-update-link">
											<a href="#"
												id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile_update"
												entryNum="${cartData.entries[fieldLength - count.count].entryNumber}"
												class="qtyUpdateTextBox1 click_updateBtn_mobile"><spring:message
													code="cart.review.productDesc.updateItem" /></a>
										</div>

										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity"
												for="quantity${cartData.entries[fieldLength - count.count].entryNumber}">
												<spring:theme code="basket.page.quantity" />
											</form:label>
											<form:input
												disabled="${not cartData.entries[fieldLength - count.count].updateable}"
												type="text"
												id="quantity${cartData.entries[fieldLength - count.count].entryNumber}_mobile"
												entryNumber="${cartData.entries[fieldLength - count.count].entryNumber}"
												class=" qtyUpdateTextBox form-control txtWidth"
												path="quantity" />
										</ycommerce:testId>
										<p>
											<strong><spring:message
													code="product.detail.addToCart.unit" /> </strong>${cartData.entries[fieldLength - count.count].product.deliveryUnit}
											(${cartData.entries[fieldLength - count.count].product.numerator}&nbsp;${cartData.entries[fieldLength - count.count].product.salesUnit})
										</p>
										<ycommerce:testId code="cart_product_removeProduct">
											<a href="javascript:void();"
												id="RemoveProduct_${cartData.entries[fieldLength - count.count].entryNumber}"
												class="smallFont submitRemoveProduct"><spring:message
													code="cart.review.productDesc.removeItem" /></a>
										</ycommerce:testId>
										<p class="msgHighlight">${entry.product.hazmatCode}</p>
									</form:form>
									<p>
										<spring:message code="cart.return.entries.lotNumber" />
										<span class="redStar">*</span>
									</p>
									<p>
									<div>
										<form:form
											id="returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}"
											class="returnEntriesRequiredForm" action="javaScript:;">
											<input type="hidden"
												id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
												name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
												value="${cartData.entries[fieldLength - count.count].product.code}" />
											<input type="text"
												class="form-control textbox-size lotNumber required"
												id="lotNumber_mobile_${cartData.entries[fieldLength - count.count].entryNumber}"
												placeholder="Lot Number"
												data="${cartData.entries[fieldLength - count.count].entryNumber}"
												value="${cartData.entries[fieldLength - count.count].lotNumber}"
												data-msg-required="<spring:message code="cart.return.lotNumber.enter"/>" />
											<div class="error hide invalidLotNum"
												id="invalidLotNum_${cartData.entries[fieldLength - count.count].entryNumber}">
												<spring:message code="cart.common.invalid.lot" />
												#
											</div>
											<div class="registerError"></div>
										</form:form>
									</div>
									</p>
									<p>
										<spring:message code="cart.return.entries.poNumber" />
									</p>
									<p>
									<div>
										<form:form
											id="returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}"
											class="returnEntriesRequiredForm" action="javaScript:;">
											<input type="hidden"
												id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
												name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
												value="${cartData.entries[fieldLength - count.count].product.code}" />
											<input type="text"
												class="form-control textbox-size poNumber alphanumeric"
												id="poNumber_mobile_${cartData.entries[fieldLength - count.count].entryNumber}"
												name="poNumber"
												data="${cartData.entries[fieldLength - count.count].entryNumber}"
												value="${cartData.entries[fieldLength - count.count].poNumber}"
												placeholder="<spring:message code='cart.return.entries.poNumber'/>">
										</form:form>
									</div>
									<div class="registerError"></div>
									</p>

							
								<p>
									<span><spring:message
											code="cart.return.entries.invoiceNumber" /> <!--AAOL-4251 - Sprint 4 demofix  for UI -->
										<span id="invoiceStar" class="redStar">*</span></span>
								</p>


								<p>
								<div>
									<form:form
										id="returnEntriesRequiredFormID_${cartData.entries[fieldLength - count.count].entryNumber}"
										class="returnEntriesRequiredForm" action="javaScript:;">
										<input type="hidden"
											id="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
											name="productCode_${cartData.entries[fieldLength - count.count].entryNumber}"
											value="${cartData.entries[fieldLength - count.count].product.code}" />
											
										<input type="text"
											class="form-control textbox-size returnEntriesRequired invoiceNumber alphanumeric"
											id="invoiceNumber_mobile_${cartData.entries[fieldLength - count.count].entryNumber}"
											name="invoiceNumber"
											data="${cartData.entries[fieldLength - count.count].entryNumber}"
											value="${cartData.entries[fieldLength - count.count].returnInvNumber}"
											data-msg-required="<spring:message code="cart.return.reasonCode.invoice"/>"
											placeholder="<spring:message code='cart.return.entries.invoiceNumber'/>">
									</form:form>
								</div> <c:if
									test="${not empty cartData.entries[fieldLength - count.count].validationErrorKeys && showPreValidationError}">
									<div class="registerError cartErrMargin">
										<c:forEach
											items="${cartData.entries[fieldLength - count.count].validationErrorKeys}"
											var="errorKey">
											<c:if test="${errorKey eq 'cart.return.invoiceNumber.enter'}">
												<div class="error invalidInvoiceNum"
													id="invoiceNumber_mobile_${cartData.entries[fieldLength - count.count].entryNumber}">
													<spring:message code="cart.common.invalid.invoice" />
													#
												</div>
											</c:if>
										</c:forEach>
									</div>
								</c:if>

								<div class="registerError"></div>

								</p>
								
									</div></td>

						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
		

			</div>
			<!-- End - Total Price Summary -->
	
</div>
<br>



	<div class="row validatebtn">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="float-right-to-none">
			 		<c:set value="saveorderastemplate" var="classForSaveTemplate"/>
	  				<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}  ${classForValidate}"><spring:message code="cart.review.cartPageAction.saveTemplate"/></button>
						<button type="button" class="btn btnclsactive  ${classForValidate} submitReturn"><spring:message code="cart.return.returnAction.submitReturns"/></button>
		
	 		</div>
		</div>
	</div>


<div id="laodingcircle" style="display:none">
    <div class="modal-backdrop in">
     <!-- Modal content-->
       <div class="row panelforcircle">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
         <div class="loadcircle"><img src="${webroot}/_ui/responsive/common/images/ajax-loader.gif"></div>
        </div>
               
       </div>
      </div>        
</div>




