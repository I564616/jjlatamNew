<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<%@ attribute name="validationPage" required="false"
	type="java.lang.Boolean"%>
<%@ taglib prefix="breadcrumb"	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>
<%@ taglib prefix="standardCart"
	tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<script type="text/javascript"> // set vars
/*<![CDATA[*/ var cartRemoveItem = true;
/*]]>*/
</script>


 <form:form id="UpdateMultipleEntriesInCartForm" action="updateAll"  commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
								<div id="AddItemsCartpage">
									
										<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
									
									<div class="row content">
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12"><spring:message code="cart.review.shoppingCart"/></div>
										<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
											<button type="button" class="btn btnclsactive pull-right validateprice shippingPage"><spring:message code="cart.review.shoppingCart.checkout"/></button>
										</div>
									</div>
									<div class="mainbody-container">
										<div class="hidden-xs">
											<table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines">
												<thead>
												  <tr>
													<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th>
		                                        	<th class="no-sort"><spring:message code="cart.validate.quantity"/><div class="cart-update-all-link"><a class="cartUpdateAllbtn" id="cartUpdateAllbutton" href="javascript:;"><spring:message code="cart.review.entry.Updateall"/></a></div></th>
													<th class="no-sort"><spring:message code="cart.validate.unitPrice"/></th>
													<th class="no-sort total-thead"><spring:message code="cart.review.entry.total"/></th>
												  </tr>
												</thead>
												<tbody>
												 
												 <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
													<tr id="orderentry-${count.count}">
														<td class="text-left">
			                                    					 <standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false"/>
			                                             </td>
														<td class="valign-middle">
		     <div class="cart-update-link"><a href="javascript:void();" id="quantity${entry.entryNumber}" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1" ><spring:message code="cart.review.productDesc.updateItem"/></a></div>
						                         <c:url value="/cart/update" var="cartUpdateFormAction" />
												<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
												<input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
												<input type="hidden" name="productCode" value="${entry.product.code}"/>
												<input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
												<div><ycommerce:testId code="cart_product_quantity">
											  
											    <form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"></form:label>
											   
												<form:input disabled="${not entry.updateable}"  type="text"  id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
												
												</ycommerce:testId>   </div>                           
                          						  <p class="thirdline"><spring:message code="product.detail.addToCart.unit"/> ${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</p>
           
												<ycommerce:testId code="cart_product_removeProduct">
													<p>
														<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}"
															class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/>
														</a>
														
														
													</p>
												</ycommerce:testId> 

	
						<p class="msgHighlight">${entry.product.hazmatCode}</p>
						</form:form>
						
	    </td>	
			<td class="valign-middle" id="basePrice_${entry.entryNumber}">
							<format:price priceData="${entry.basePrice}"/>							           
			</td>
			<td  class="valign-middle totalrps " id="totalPrice_${entry.entryNumber}">
							<ycommerce:testId code="cart_totalProductPrice_label">
								<format:price priceData="${entry.totalPrice}"/>
							</ycommerce:testId> 
			</td>
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
												<th class="no-sort text-left"><spring:message code="cart.validate.product"/></th></tr>
											</thead>
											<tbody>
											<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
												<tr>
													<td class="text-left">
														<standardCart:productDescriptionBeforeValidation entry="${entry}" showRemoveLink="true" showStatus="false"/>
														<p><spring:message code="cart.validate.quantityQty"/></p>
														 <c:url value="/cart/update" var="cartUpdateFormAction" />
														<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
														<input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
														<input type="hidden" name="productCode" value="${entry.product.code}"/>
														<input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
														<ycommerce:testId code="cart_product_quantity">
													    <form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.quantity"/></form:label>
														<form:input disabled="${not entry.updateable}"  type="text"  id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity"/>
														</ycommerce:testId>                              
                           
                
														<ycommerce:testId code="cart_product_removeProduct">
														<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}"class="smallFont submitRemoveProduct"><spring:message code="cart.review.productDesc.removeItem"/></a>
														</ycommerce:testId>
														<p class="msgHighlight">${entry.product.hazmatCode}</p>
														</form:form>
														<p><spring:message code="cart.validate.unitPrice"/></p>
														<p> <format:price priceData="${entry.basePrice}"/>	</p>
														<p><spring:message code="cart.review.entry.total"/></p>
														<p><ycommerce:testId code="cart_totalProductPrice_label">
													<p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
							                     </ycommerce:testId> </p>
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
										</div>	
										
										<!--Accordian Ends here -->
														
										<!-- Start - Total Price Summary -->
										<div class="row basecontainer">
										
												<table class="total-summary-table">
													<tr>
														<td class="total-summary-label"><spring:message code="cart.common.subTotal"/></td>
													<td class="total-summary-cost totalrps no-right-pad"><format:price priceData="${cartData.subTotal}" /></td>
													</tr>
													<tr class="summary-bline">
														<td class="total-summary-label"><spring:message code="cart.review.shoppingCart.shipping"/></td>
														<td class="total-summary-cost">--</td>
													</tr>
													<tr class="total-price-row">
														<td class="total-summary-label"><spring:message code="cart.review.shoppingCart.total"/></td>
														<td class="total-summary-cost totalsum no-right-pad"><format:price priceData="${cartData.subTotal}" /></td>
														
													</tr>
												</table>
												
										</div>
									</div>
									<!-- End - Total Price Summary -->
									
									<div class="row validatebtn">
										<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
											<div class="float-left-to-none">
												<button type="button" class="btn btnclsnormal checkout-clear-cart" id="RemoveCartData"> <spring:message code="cart.payment.clearCart"/></button>
											</div>	
											<div class="float-right-to-none">
												<c:set value="saveorderastemplate" var="classForSaveTemplate" />
												<button type="button" class="btn btnclsnormal templatebtn ${classForSaveTemplate}"><spring:message code="cart.review.shoppingCart.template"/></button>
												<button type="button" class="btn btnclsactive shippingPage"><spring:message code="cart.review.shoppingCart.checkout"/></button>
											</div>
										</div>
									</div>
									
								</div>
			
