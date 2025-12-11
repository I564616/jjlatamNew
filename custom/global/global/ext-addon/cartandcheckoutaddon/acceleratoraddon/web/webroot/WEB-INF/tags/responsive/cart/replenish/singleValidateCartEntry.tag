<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="label" uri="/WEB-INF/tld/addons/loginaddon/message.tld"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart"%>
<%@ taglib prefix="standardCart" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/standard"%>
<%@ taglib prefix="commonTags" tagdir="/WEB-INF/tags/addons/cartandcheckoutaddon/responsive/cart/common"%>
<input type="hidden" value="false ${refreshSapPrice}" id="refreshSapPrice">
 <!--Order Derail Row starts-->
 <form:form id="UpdateMultipleEntriesInCartForm" action="updateAll"  commandName="UpdateMultipleEntriesInCartForm">
 </form:form>
 <!-- replenish/singleValidateCartEntry.tag -->
 <div id="AddItemsCartpage">
	<div class="mainbody-container">
		<div class="hidden-xs">
		<div class="dropshipment-label-value">
				<div class="dropshipment-label" style="margin-top:10px"> <strong><spring:message
							code="cart.common.orderType" /></strong>
							
					<span>&nbsp;<spring:message code="cart.common.orderType.${cartData.orderType}" /></span>
					<%-- <div class="dropshipment-value">
						<spring:message code="cart.common.orderType.${cartData.orderType}" />
					</div> --%>
				</div>
			</div>
			<table id="datatab-desktop" class="table table-bordered table-striped sorting-table-lines fee-price-table">
				<thead>
					<tr>
						<th class="no-sort snoClass"><spring:message code="cart.review.entry.number"/></th>
						<th class="no-sort text-left">
							<spring:message  code="cart.validate.product" />
						</th>
						<th class="no-sort">
							<spring:message  code="cart.validate.quantity" />
								<div class="cart-update-all-link">
									<a class="cartUpdateAllbtn cartUpdateAllbutton"  id="cartUpdateAllbutton" href="javascript:;">
										<spring:message  code="cart.review.entry.Updateall" /></a>
								</div>
							</th>
						<th class="no-sort unitprice-thead">
							<spring:message  code="cart.validate.unitPrice" />
						</th>
						<th class="no-sort total-thead fee-cell text-uppercase">
							<spring:message  code="cart.review.entry.total" />
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
						<tr id="orderentry-${count.count}">
							<td class="snoData">${count.count}</td>
							<td class="text-left">
								<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="true" showStatus="false" />
							</td>
							<td class="valign-middle">
								<div class="cart-update-link">
									<a href="javascript:void();" id="quantity${entry.entryNumber}_update" entryNum="${entry.entryNumber}" class="qtyUpdateTextBox1">
										<spring:message code="cart.review.productDesc.updateItem" /></a>
								</div> 
								<c:url value="/cart/update" var="cartUpdateFormAction" /> 
									<form:form id="updateCartForm${entry.entryNumber}_desktop" action="${cartUpdateFormAction}" method="post"
									commandName="updateQuantityForm${entry.entryNumber}">
									
									<c:if test="${freeGoodsMap ne null}">
										<c:set var="freeObject"  value="${freeGoodsMap[entry.product.code]}" />
									</c:if>
									<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
									<input type="hidden" name="productCode"  value="${entry.product.code}" />
									<input type="hidden" name="initialQuantity"  value="${entry.quantity}" />
									<div>
										<ycommerce:testId code="cart_product_quantity">
											<form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"></form:label>
											<form:input disabled="${not entry.updateable}" type="text"
												onclick="hideFreeGood('${entry.product.code}', this,'${freeObject.orderedQuantity}');"
												id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class=" qtyUpdateTextBox form-control txtWidth" path="quantity" />
										</ycommerce:testId>
									</div>
									<p class="thirdline">
										<spring:message code="product.detail.addToCart.unit" />
										${entry.product.deliveryUnit}
										(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
									</p>
									<ycommerce:testId code="cart_product_removeProduct">
										<p>
											<a href="javascript:void();" id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
												<spring:message code="cart.review.productDesc.removeItem" />
											</a>
										</p>
									</ycommerce:testId>
									<p class="msgHighlight">${entry.product.hazmatCode}</p>
								</form:form>
							</td>
							<td class="valign-middle" id="basePrice_${entry.entryNumber}">
							      <c:set var="contains" value="false" />
							     <c:forEach var="item" items="${kitValidationErrorMsg}">
									<c:if test="${item == entry.product.code}">
										<c:set var="contains" value="true" />
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${contains eq true}">
 									 </c:when>
									<c:otherwise>
  									 <format:price priceData="${entry.basePrice}" />
  									</c:otherwise>
								</c:choose>
							</td>
							<td class="valign-middle totalrps fee-cell"	id="totalPrice_${entry.entryNumber}">
							
							 <c:set var="contains" value="false" />
							     <c:forEach var="item" items="${kitValidationErrorMsg}">
									<c:if test="${item == entry.product.code}">
										<c:set var="contains" value="true" />
										
									</c:if>
								</c:forEach>
								<c:choose>
									<c:when test="${contains eq true}">
 									 </c:when>
									<c:otherwise>
  									<ycommerce:testId code="cart_totalProductPrice_label">
									<format:price priceData="${entry.totalPrice}" />
								  </ycommerce:testId>
  									</c:otherwise>
								</c:choose>
							
								
								</td>
						</tr>
						<!-- Changes for Bonus Item -->
						<c:if test="${freeGoodsMap ne null}">
							<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
							<c:if test="${not empty valueObject.itemCategory}">
								<tr class="noborder" id="freeGood${entry.product.code}">
									<td class="text-right">
										<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="false" showStatus="false" />
									</td>
									<td>
										<div class="text-center">
											<c:url value="/cart/update" var="cartUpdateFormAction" />
											<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post"
												commandName="updateQuantityForm${entry.entryNumber}">
												<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
												<input type="hidden" name="productCode" value="${entry.product.code}" />
												<input type="hidden" name="initialQuantity" value="${entry.quantity}" />

												<input type="hidden" size="4" value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}" />
												<div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
												<p class="thirdline">
													<spring:message code="product.detail.addToCart.unit" />
													${entry.product.deliveryUnit}
													(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
												</p>
											</form:form>
										</div>
									</td>
									<td class="valign-middle">
										<spring:message code="cart.freeitem.message" />
									</td>
									<td class="valign-middle"></td>
								</tr>
							</c:if>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- Table collapse for mobile device-->

		<div class="visible-xs hidden-lg hidden-sm hidden-md">
			<table id="datatab-mobile" class="table table-bordered table-striped sorting-table-lines">
				<thead>
					<tr>
						<th class="no-sort text-left">
							<spring:message code="cart.validate.product" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${cartData.entries}" var="entry" varStatus="count">
						<tr>
							<td class="text-left">
								<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}" rowcount="${count.count}" showRemoveLink="true" showStatus="false" />
								<div id="mobi-collapse${count.count}" class="panel-collapse collapse img-accordian">
									<p>
										<spring:message code="cart.validate.quantityQty" />
									</p>
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
										<input type="hidden" name="productCode" value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
										<form:label cssClass="skip" path="quantity"  for="quantity${entry.entryNumber}"> <spring:theme code="basket.page.quantity" /> </form:label>
										<form:input disabled="${not entry.updateable}" type="text"  id="quantity${entry.entryNumber}"  entryNumber="${entry.entryNumber}"
												class=" qtyUpdateTextBox form-control txtWidth" path="quantity" />
										</ycommerce:testId>
										
										
										<ycommerce:testId code="cart_product_removeProduct">
											<a href="javascript:void();"  id="RemoveProduct_${entry.entryNumber}" class="smallFont submitRemoveProduct">
												<spring:message code="cart.review.productDesc.removeItem" />
											</a>
										</ycommerce:testId>
										
										
										<p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>
                            <c:if test="${entry.updateable}" >
								<ycommerce:testId code="cart_product_updateQuantity">
									<a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}"><spring:theme code="basket.page.update"/></a>
								</ycommerce:testId>
                            </c:if>

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
						
						<!-- Changes for Bonus Item -->
						<c:if test="${freeGoodsMap ne null}">
							<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}" />
							<c:if test="${not empty valueObject.itemCategory}">
								<tr class="noborder" id="freeGood${entry.product.code}">
									<td class="text-right">
										<standardCart:productDescriptionBeforeValidation entry="${entry}" priceError="${priceValidationErrorMsg}" showRemoveLink="false" showStatus="false" /></td>
									<td>
										<div class="text-center">
											<c:url value="/cart/update" var="cartUpdateFormAction" />
											<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" 
												commandName="updateQuantityForm${entry.entryNumber}">
												<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
												<input type="hidden" name="productCode" value="${entry.product.code}" />
												<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
												<input type="hidden" size="4" value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}" />
												<div id="freeGoodQuantity${entry.product.code}">${valueObject.materialQuantity}</div>
												<p class="thirdline">
													<spring:message code="product.detail.addToCart.unit" />
													${entry.product.deliveryUnit}
													(${entry.product.numerator}&nbsp;${entry.product.salesUnit})
												</p>
											</form:form>
										</div>
									</td>
									<td class="valign-middle">
										<spring:message code="cart.freeitem.message" /></td>
									<td class="valign-middle"></td>
								</tr>
							</c:if>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<!--Accordian Ends here -->

		<!-- Start - Total Price Summary -->
		<div class="row basecontainer fee-toggle-container">
			<table class="total-summary-table">
				<tr>
					<td class="total-summary-label">
					<spring:message code="cart.common.subTotal" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${cartData.subTotal}" /></td>
				</tr>
				<tr>
					<td class="total-summary-label"><spring:message
							code="cart.validate.cartTotal.fees" /></td>
					<td class="total-summary-cost totalrps"><format:price
							priceData="${cartData.totalFees}" /></td>
					<td class="toggle-fee"><a data-toggle="collapse"
						class="toggle-fee-link toggle-link panel-collapsed"
						href="#fee-mobile-collpase"><span class="glyphicon glyphicon-chevron-up"></span></a></td>
				</tr>
				<tr class="fee-panel">
					<td colspan='2'>
						<table id="fee-mobile-collpase"
							class="fee-collpase-table total-summary-table panel-collapse collapse">
							<tr>
								<td class="total-summary-label" style="width: 25px;font-size:10px !important"><spring:message code="cart.validate.cartTotal.dropShipFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalDropShipFee}" /></td>
							</tr>
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.minimumOrderFee" /></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalminimumOrderFee}" /></td>
							</tr>
							
							<tr>
								<td class="total-summary-label" style="font-size:10px !important"><spring:message code="cart.validate.cartTotal.freightCost"/></td>
								<td class="total-summary-cost totalrps" style="font-size:12px !important"><format:price priceData="${cartData.totalFreightFees}"/></td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="summary-bline ">
					<td class="total-summary-label">
						<spring:message code="cart.validate.cartTotal.tax" /></td>
					<td class="total-summary-cost totalrps">
						<format:price priceData="${cartData.totalTax}" /></td>
				</tr>
				<tr class="total-price-row">
					<td class="total-summary-label">
						<spring:message code="cart.review.shoppingCart.total" /></td>
					<td class="total-summary-cost totalsum">
						<format:price priceData="${cartData.totalPrice}" /></td>
				</tr>
			</table>
		</div>
	</div>
	<!-- End - Total Price Summary -->
</div>
<%-- <div class="orderDetailPanel">
        	<div class="orderDetHead noBorderBtm reviewOrder minHeight45">
       			<div class="hcolumn1"><span><spring:message code="cart.review.entry.number"/></span></div>
       			<div class="hcolumn2"><span><spring:message code="cart.review.entry.lineItems"/></span></div>
		 		<div class="hcolumn3"><ul class="ulCartEntry">
                <li class="textColorWhite"><spring:message code="cart.review.entry.quantity"/>
                </li><li class="textColorWhite"><a class="cartUpdateAllbtn" id="cartUpdateAllbutton" href="javascript:;">Update All</a></li></ul></div>
                <div class="hcolumn4"><span><spring:message code="cart.review.entry.itemPrice"/></span></div>
                <div class="hcolumn5"><span><spring:message code="cart.review.entry.total"/></span></div>
            </div>
<div class="orderDetBody reviewOrder">  	
            <!--Order Derail Row starts-->
       <c:forEach items="${cartData.entries}" var="entry"  varStatus="count">
               	<c:choose>
	              	<c:when test="${count.count  mod 2 == 0}">
	              		<c:set var="orderEntriesClass" value="even"> </c:set>
	              	</c:when>
              		<c:otherwise>
              			<c:set var="orderEntriesClass" value="odd"> </c:set>
              		</c:otherwise>
              	</c:choose>				
	            <div class="orderDetRow ${orderEntriesClass}" id="orderentry-${count.count}">	             
					<div class="floatLeft column1 paddingLeft"> ${count.count}</div>
					<div class="floatLeft column2">
						<standardCart:productDescription entry="${entry}" showRemoveLink="true" showStatus="false"/>
	                </div>
	                <div class="floatLeft column3">
						<div class="lbox">
						
							<c:url value="/cart/update" var="cartUpdateFormAction" />
							<form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
							<input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
							<input type="hidden" name="productCode" value="${entry.product.code}"/>
							<input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
							<ycommerce:testId code="cart_product_quantity">
								<form:label cssClass="skip" path="quantity" for="quantity${entry.entryNumber}"><spring:theme code="basket.page.quantity"/></form:label>
							<p><span >	<form:input disabled="${not entry.updateable}"  type="text" size="3"   maxlength="6" id="quantity${entry.entryNumber}" entryNumber="${entry.entryNumber}" class="insertInput qtyUpdateTextBox" path="quantity"/></span></p>
							</ycommerce:testId>                              
                            <p><label class="descSmall block" for="quantOne">${entry.product.deliveryUnit} (${entry.product.numerator}&nbsp;${entry.product.salesUnit})</label></p>
                            <c:if test="${entry.updateable}" >
								<ycommerce:testId code="cart_product_updateQuantity">
									<a href="javascript:;" class="UpdateQtyLinkCart updateQuantityProduct" id="QuantityProduct_${entry.entryNumber}"><spring:theme code="basket.page.update"/></a>
								</ycommerce:testId>
                            </c:if>
						</form:form>
						<!-- Hide lot/comment if division is part of configured divisions -->
						<c:forEach var="division" items="${divisionList}">
						  <c:if test="${division eq entry.product.salesOrgCode}">
	  							<p class="log"><span >
								<label class="descSmall block" for="LotComment_${entry.entryNumber}"><spring:message code="cart.review.entry.lotComment"/></label>
								<input type='text' class='insertInput lotCommentInput' title="<spring:message code="cart.common.code.enter"/>" id="LotComment_${entry.entryNumber}" value="${entry.lotComment}" />
								</span></p>
						  </c:if>
						</c:forEach>
						</div>
	                </div>
	                
	                <div class="floatLeft column4" id="basePrice_${entry.entryNumber}">	                	
							<format:price priceData="${entry.basePrice}"/>							           
	                </div>
	                <div class="floatLeft column5" id="totalPrice_${entry.entryNumber}">
							<ycommerce:testId code="cart_totalProductPrice_label">
								<p class="jnjID"><format:price priceData="${entry.totalPrice}"/></p>
							</ycommerce:testId> 
	                </div>
					
					<!--Changes for Bonus Item -->
	                <c:if test="${freeGoodsMap ne null}">
					<c:set var="valueObject" value="${freeGoodsMap[entry.product.code]}"/>
					<c:if test="${not empty valueObject.itemCategory}">
						<div class="orderDetRow ${orderEntriesClass}" id="freeGood${entry.product.code}" >
					
		                <div class="floatLeft column2" style="padding-left: 50px">				
		                    <standardCart:productDescription entry="${entry}" showRemoveLink="false" showStatus="false"/>
		                </div>
		 				<div class="floatLeft expectedDeliveryDateCheckout" >
		                </div>
						<div class="floatLeft column3a indirectCustDesc indirectCustomerCart" style="width: 287px">
						</div>
						<div class="floatLeft column3" style="padding-left: 0px">
		                <c:url value="/cart/update" var="cartUpdateFormAction" />
		                            <form:form id="updateCartForm${entry.entryNumber}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
		                                <input type="hidden" name="entryNumber" value="${entry.entryNumber}"/>
		                                <input type="hidden" name="productCode" value="${entry.product.code}"/>
		                                <input type="hidden" name="initialQuantity" value="${entry.quantity}"/>
		                                
		                                    <form:input disabled="true"  type="text" size="2"  value="${valueObject.materialQuantity}" id="freeGoodQuantity${entry.product.code}"  path="quantity"/>

		                            </form:form>
		                    <span class="descSmall block"></span>
		                </div>
		                <div class="floatLeft column4">
		                	<span class="block">
									<font style="color: #12C2E9;"><spring:message code="cart.freeitem.message" /></font>
								</span>
		                </div>
		                <div class="floatLeft column6a totalPriceCart">
               		    </div>
		                </div>
		               </c:if> 
                </c:if>
	             </div>
		 </c:forEach>	
		   </div>
	</div> --%>